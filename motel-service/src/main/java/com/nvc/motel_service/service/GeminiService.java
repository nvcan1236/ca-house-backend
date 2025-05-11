package com.nvc.motel_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nvc.motel_service.dto.response.AIReviewResponse;
import com.nvc.motel_service.entity.Motel;
import com.nvc.motel_service.entity.MotelImage;
import com.nvc.motel_service.exception.AppException;
import com.nvc.motel_service.exception.ErrorCode;
import com.nvc.motel_service.repository.MotelRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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

    public AIReviewResponse reviewMotel(String motelId) throws Exception {
        Motel motel = motelRepository.findById(motelId).orElseThrow(() -> new AppException(ErrorCode.MOTEL_NOT_FOUND));
        String prompt = String.format(
                new StringBuilder()
                        .append("Vui lòng đánh giá thông tin nhà trọ sau đây về tính hợp lệ và thực tế:\n")
                        .append("Địa chỉ: %s\nGiá thuê: %s VND\nDiện tích: %s m²\nTiện ích: %s\nMô tả: %s\nURL hình ảnh: %s\n")
                        .append("Kiểm tra xem thông tin có đầy đủ, thực tế và nhất quán không (ví dụ: giá thuê có hợp lý với diện tích và vị trí không, mô tả có khớp với tiện ích không, hình ảnh có phù hợp không?). Trả về một đối tượng JSON với 'status' ('APPROVED', 'REJECTED', 'PENDING') và 'reason' (lý do giải thích cho trạng thái).")
                        .toString(),
                motel.getLocation().getFullLocation(),
                motel.getPrice(),
                motel.getArea(),
                motel.getAmenities().stream().map(Object::toString),
                motel.getDescription(),
                motel.getImages().stream().map(MotelImage::getUrl)
        );

        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("{\"prompt\": \"" + prompt + "\"}"))
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }


        return objectMapper.readValue(response.body(), AIReviewResponse.class);
    }
}
