package com.nvc.user_service.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nvc.user_service.dto.request.*;
import com.nvc.user_service.dto.response.*;
import com.nvc.user_service.entity.InvalidatedToken;
import com.nvc.user_service.entity.Role;
import com.nvc.user_service.entity.User;
import com.nvc.user_service.enums.UserRole;
import com.nvc.user_service.exception.AppException;
import com.nvc.user_service.exception.ErrorCode;
import com.nvc.user_service.repository.InvalidatedTokenRepository;
import com.nvc.user_service.repository.RoleRepository;
import com.nvc.user_service.repository.UserRepository;
import com.nvc.user_service.repository.httpclient.OutboundIdentityClient;
import com.nvc.user_service.repository.httpclient.OutboundUserClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {
    private final RoleRepository roleRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.signer-key}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    @NonFinal
    @Value("${app.outbound.client_id}")
    protected String CLIENT_ID;

    @NonFinal
    @Value("${app.outbound.client_secret}")
    protected String CLIENT_SECRET;

    @NonFinal
    @Value("${app.outbound.redirect_uri}")
    protected String REDIRECT_URI;


    public AuthenticationResponse authenticateUser(AuthenticationRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);

        String token = generateToken(user);

        return AuthenticationResponse.builder()
                .authenticated(true)
                .token(token)
                .build();
    }

    public IntrospectResponse introspectToken(String token) throws ParseException, JOSEException {

        boolean isValid = true;
        try {
            verifyToken(token, false);
        } catch (Exception e) {
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    public String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        log.info("Scope: {}", buildScope(user));
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("nvc")
                .issueTime(new Date())
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .expirationTime(new Date(Instant.now()
                        .plus(VALID_DURATION, ChronoUnit.SECONDS)
                        .toEpochMilli()))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
                }
            });
        }
        return stringJoiner.toString();
    }

    SignedJWT verifyToken(String token, boolean isRefresh) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        Date expirationDate = isRefresh
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant()
                .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        if (!(signedJWT.verify(verifier) && expirationDate.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    public void logout(LogoutRequest request) throws JOSEException, ParseException {
        try {
            SignedJWT signedJWT = verifyToken(request.getToken(), true);

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .expiryTime(signedJWT.getJWTClaimsSet().getExpirationTime())
                    .id(signedJWT.getJWTClaimsSet().getJWTID())
                    .build();

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException exception) {
            log.info("Token is already expired");
        }

    }

    public AuthenticationResponse refresh(RefreshRequest request) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(request.getToken(), true);
        String jit = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);

        User user = userRepository.findByUsername(signedJWT.getJWTClaimsSet().getSubject())
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        String token = generateToken(user);

        return AuthenticationResponse.builder()
                .authenticated(true)
                .token(token)
                .build();
    }

    OutboundIdentityClient outboundIdentityClient;
    OutboundUserClient outboundUserClient;

    public AuthenticationResponse exchangeToken(String code) {
        try {
            log.info(code);

            var response = outboundIdentityClient.exchangeToken(ExchangeTokenRequest.builder()
                    .clientId(CLIENT_ID)
                    .clientSecret(CLIENT_SECRET)
                    .code(code)
                    .grantType("authorization_code")
                    .redirectUri(REDIRECT_URI)
                    .build());


            OutboundUserResponse userInfo = outboundUserClient.getUser("json", response.getAccessToken());

            Set<Role> roles = new HashSet<>();
            roles.add(Role.builder()
                    .name(UserRole.USER.toString())
                    .build());


            User user = userRepository.findByUsername(userInfo.getEmail())
                    .orElseGet(() -> User.builder()
                            .username(userInfo.getEmail())
                            .firstName(userInfo.getGivenName())
                            .lastName(userInfo.getFamilyName())
                            .email(userInfo.getEmail())
                            .avatar(userInfo.getPicture())
                            .roles(roles)
                            .build());


            userRepository.save(user);
            String token = generateToken(user);
            return AuthenticationResponse.builder().token(token).authenticated(true).build();
        } catch (Exception exception) {
            log.error("ERROR: {}", exception.getMessage());
        }
        return null;
    }

    public void createPassword(CreatePasswordRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new AppException(ErrorCode.UNAUTHENTICATED)
        );

        if (StringUtils.hasText(user.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_EXISTED);
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }
}
