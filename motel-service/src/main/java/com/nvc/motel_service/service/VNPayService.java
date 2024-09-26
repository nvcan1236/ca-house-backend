package com.nvc.motel_service.service;

import com.nvc.motel_service.configuration.VNPayConfig;
import com.nvc.motel_service.repository.MotelRepository;
import com.nvc.motel_service.repository.ReservationRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
@Slf4j
public class VNPayService {
    private final ReservationRepository reservationRepository;
    private final MotelRepository motelRepository;
    VNPayConfig vnPayConfig;
    public String createVnPayPayment(HttpServletRequest request, int amount, String reservationId) {

        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount * 100L));
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));
        vnpParamsMap.put("vnp_TxnRef",  reservationId);

        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);

        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);

        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;

        return vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
    }

    public static class VNPayUtil {
        public static String hmacSHA512(final String key, final String data) {
            try {
                if (key == null || data == null) {
                    throw new NullPointerException();
                }
                final Mac hmac512 = Mac.getInstance("HmacSHA512");
                byte[] hmacKeyBytes = key.getBytes();
                final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
                hmac512.init(secretKey);
                byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
                byte[] result = hmac512.doFinal(dataBytes);
                StringBuilder sb = new StringBuilder(2 * result.length);
                for (byte b : result) {
                    sb.append(String.format("%02x", b & 0xff));
                }
                return sb.toString();

            } catch (Exception ex) {
                return "";
            }
        }

        public static String getIpAddress(HttpServletRequest request) {
            String ipAdress;
            try {
                ipAdress = request.getHeader("X-FORWARDED-FOR");
                if (ipAdress == null) {
                    ipAdress = request.getRemoteAddr();
                }
            } catch (Exception e) {
                ipAdress = "Invalid IP:" + e.getMessage();
            }
            return ipAdress;
        }

        public static String getRandomNumber(int len) {
            Random rnd = new Random();
            String chars = "0123456789";
            StringBuilder sb = new StringBuilder(len);
            for (int i = 0; i < len; i++) {
                sb.append(chars.charAt(rnd.nextInt(chars.length())));
            }
            return sb.toString();
        }

        public static String getPaymentURL(Map<String, String> paramsMap, boolean encodeKey) {
            return paramsMap.entrySet().stream()
                    .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty())
                    .sorted(Map.Entry.comparingByKey())
                    .map(entry ->
                            (encodeKey
                                    ? (URLEncoder.encode(entry.getKey(), StandardCharsets.US_ASCII))
                                    : entry.getKey())
                                    + "="
                                    + URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII))
                    .collect(Collectors.joining("&"));
        }
    }
}
