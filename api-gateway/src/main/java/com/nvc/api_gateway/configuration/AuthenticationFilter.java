package com.nvc.api_gateway.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nvc.api_gateway.dto.response.ApiResponse;
import com.nvc.api_gateway.service.IdentityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationFilter implements GlobalFilter, Ordered {
    IdentityService identityService;
    ObjectMapper objectMapper;

    @NonFinal
    private String[] publicEndpoints = {
            "/identity/users",
            "/identity/users/**",
            "/identity/auth/token",
            "/identity/auth/introspect",
            "/identity/auth/refresh",
            "/identity/auth/logout",
            "/identity/auth/outbound/authentication",
            "/motel/",
            "/motel/nearest",
            "/motel/**/review",
            "/motel/**",
            "/post/**/comment",
            "/post/**",
            "/post/",
            "/post",
    };

    @NonFinal
    @Value("${app.api-prefix}")
    private String apiPrefix;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        List<String> authToken = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);

        if (!CollectionUtils.isEmpty(authToken)) {
            String token = authToken.getFirst().substring("Bearer ".length());

            return identityService.introspect(token).flatMap(response -> {
                if (response.getResult().isValid()) {
                    return chain.filter(exchange);
                } else
                    return unauthenticated(exchange.getResponse());

            }).onErrorResume(throwable -> unauthenticated(exchange.getResponse()));
        }

        if (isPublicEndpoint(exchange.getRequest()))
            return chain.filter(exchange);

        return unauthenticated(exchange.getResponse());


    }

    @Override
    public int getOrder() {
        return -1;
    }

    private boolean isPublicEndpoint(ServerHttpRequest request) {
        String requestPath = request.getURI().getPath();
        log.info("Checking if the request path is public: " + requestPath);

        return Arrays.stream(publicEndpoints).anyMatch(s -> {
            String regexPattern = apiPrefix + s.replace("**", ".*").replace("*", "[^/]*");
            log.info(String.valueOf(requestPath.matches(regexPattern)));
            return requestPath.matches(regexPattern);
        });
    }

    Mono<Void> unauthenticated(ServerHttpResponse response) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(2001)
                .message("Unauthenticated")
                .build();
        String body = null;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return response.writeWith((
                Mono.just(response.bufferFactory().wrap(body.getBytes())))
        );
    }
}

