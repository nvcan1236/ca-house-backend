package com.nvc.motel_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nvc.motel_service.dto.response.AIReviewResponse;
import com.nvc.motel_service.entity.Amenity;
import com.nvc.motel_service.entity.Motel;
import com.nvc.motel_service.entity.MotelImage;
import com.nvc.motel_service.exception.AppException;
import com.nvc.motel_service.exception.ErrorCode;
import com.nvc.motel_service.repository.MotelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.DataInput;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiService {
    @Value("${gemini.api.key}")
    String apiKey;

    @Value("${gemini.api.url}")
    String apiUrl;

    private final MotelRepository motelRepository;
    private final ObjectMapper objectMapper;

    public AIReviewResponse getJSON(String response) {
        String cleanJson = response
                .replace("```json", "")
                .replace("```", "")
                .trim();

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(cleanJson, AIReviewResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public AIReviewResponse reviewMotel(String motelId) throws Exception {
        Motel motel = motelRepository.findById(motelId)
                .orElseThrow(() -> new AppException(ErrorCode.MOTEL_NOT_FOUND));

        String amenitiesText = motel.getAmenities().stream()
                .map(Amenity::getName).toList().toString();

        String imageUrls = motel.getImages().stream()
                .map(MotelImage::getUrl)
                .collect(Collectors.joining(", "));

        String fullLocation = motel.getLocation() != null ? motel.getLocation().getFullLocation() : "Không có địa chỉ";

        String prompt = String.format(
                new StringBuilder()
                        .append("Vui lòng đánh giá thông tin nhà trọ sau đây về tính hợp lệ và có phải thông tin rác không:\n")
                        .append("Địa chỉ: %s\nGiá thuê: %s VND\nDiện tích: %s m²\nTiện ích: %s\nMô tả: %s\nURL hình ảnh (chỉ cần co URL): %s\n ")
                        .append("Kiểm tra xem thông tin có đủ không . Trả về một PLAIN TEXT nội dung là JSON với 'status' ('APPROVED', 'REJECTED', 'PENDING') và 'reason' (lý do giải thích cho trạng thái).")
                        .toString(),
                fullLocation,
                motel.getPrice(),
                motel.getArea(),
                amenitiesText,
                motel.getDescription(),
                imageUrls
        );

        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            String requestBody = objectMapper.writeValueAsString(Map.of(
                    "contents", List.of(Map.of(
                            "parts", List.of(Map.of("text", prompt))
                    ))
            ));
            log.info("Request Body gửi đến Gemini: {}", requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "?key=" + apiKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("Response từ Gemini (status code: {}): {}", response.statusCode(), response.body());
        } catch (Exception e) {
            log.error("Lỗi khi gọi Gemini API: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.UNCATEGORIZED_ERROR);
        }

        try {
            Map<String, Object> responseMap = objectMapper.readValue(response.body(), Map.class);
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseMap.get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                Map<String, Object> candidate = candidates.get(0);
                Map<String, Object> content = (Map<String, Object>) candidate.get("content");
                List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                if (parts != null && !parts.isEmpty()) {
                    Map<String, Object> part = parts.get(0);
                    String aiResponse = (String) part.get("text");
                    return getJSON(aiResponse);
                }
            }
            throw new AppException(ErrorCode.UNCATEGORIZED_ERROR);
        } catch (Exception e) {
            log.error("Lỗi khi xử lý response từ Gemini: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.UNCATEGORIZED_ERROR);
        }
    }
}