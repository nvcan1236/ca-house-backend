package com.nvc.motel_service.service;

import com.nvc.event.dto.NotificationEvent;
import com.nvc.event.enums.TemplateEnum;
import com.nvc.motel_service.dto.request.MotelCreationRequest;
import com.nvc.motel_service.dto.request.MotelUpdationRequest;
import com.nvc.motel_service.dto.response.*;
import com.nvc.motel_service.entity.Motel;
import com.nvc.motel_service.entity.Reservation;
import com.nvc.motel_service.enums.MotelStatus;
import com.nvc.motel_service.enums.MotelType;
import com.nvc.motel_service.exception.AppException;
import com.nvc.motel_service.exception.ErrorCode;
import com.nvc.motel_service.mapper.MotelMapper;
import com.nvc.motel_service.repository.MotelRepository;
import com.nvc.motel_service.repository.ReservationRepository;
import com.nvc.motel_service.repository.httpclient.UserClient;
import com.nvc.motel_service.validator.AdminOnly;
import com.nvc.motel_service.validator.OwnerOnly;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
@Slf4j
public class MotelService {
    MotelRepository motelRepository;
    MotelMapper motelMapper;
    UserClient userClient;
    GeometryFactory geometryFactory;
    DateTimeFormatter dateTimeFormatter;
    KafkaTemplate<String, Object> kafkaTemplate;
    ReservationService reservationService;

    public boolean isOwner(String motelId, String username) {
        Motel motel = motelRepository.findById(motelId).orElseThrow(() -> new AppException(ErrorCode.MOTEL_NOT_FOUND));

        return motel.getOwnerId().equals(username);
    }

    public List<MotelResponse> searchMotels(String keyword) {
        String tsQuery = convertToTsQuery(keyword);
        return motelRepository.searchMotels(tsQuery).stream().map(motelMapper::toMotelResponse)
                .toList();
    }

    public String convertToTsQuery(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        String cleanedInput = input.trim().toLowerCase().replaceAll("[^\\p{L}\\p{N}\\s]", "");

        // Tách từ và nối lại bằng toán tử & (AND)
        return Arrays.stream(cleanedInput.split("\\s+"))
                .filter(word -> !word.isBlank())
                .collect(Collectors.joining(" & "));
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void checkReservedMotels() {
        List<Motel> motels = motelRepository.findAllByStatusIs(MotelStatus.RESERVED);
        motels.forEach(motel -> {
            Reservation reservation = reservationService.getReservationByMotel(motel.getId());
            Instant createdAt = reservation.getCreatedAt();
            LocalDate expiryDate = createdAt
                    .plus(reservation.getDuration(), ChronoUnit.DAYS)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            if (!expiryDate.isAfter(LocalDate.now())) {
                motel.setStatus(MotelStatus.AVAILABLE);
                motelRepository.save(motel);
            }
        });
    }

    public PageResponse<MotelResponse> getAll(int page, int size, MotelType roomType, Double minPrice, Double maxPrice, List<String> amenities) {
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        long amenitiesSize = (amenities != null) ? amenities.size() : 0;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().toString().contains("ROLE_ADMIN");
        Page<Motel> motelData = motelRepository.findAllFiltered(pageable, isAdmin, roomType, minPrice, maxPrice, amenities, amenitiesSize);

        List<MotelResponse> motelResponses = motelData.stream().map(motel -> {
                    MotelResponse motelResponse = motelMapper.toMotelResponse(motel);
                    motelResponse.setSaved(motel.getSavings().stream()
                            .anyMatch(s -> s.getUserId().equals(authentication.getName()) && s.getIsActive()));
                    return motelResponse;
                })
                .toList();

        return PageResponse.<MotelResponse>builder()
                .currentPage(page)
                .pageSize(motelData.getSize())
                .totalPage(motelData.getTotalPages())
                .totalElement(motelData.getTotalElements())
                .data(motelResponses)
                .build();
    }

    public DetailMotelResponse getMotelById(String id) {

        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().contains("ROLE_ADMIN");

        Motel motel = motelRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.MOTEL_NOT_FOUND));
        DetailMotelResponse detailMotelResponse = motelMapper.toDetailMotelResponse(motel);
        detailMotelResponse.setSaved(motel.getSavings().stream()
                .anyMatch(s -> s.getUserId().equals(currentUser) && s.getIsActive()));
        UserResponse owner = userClient.getUserById(detailMotelResponse.getOwnerId()).getResult();
        detailMotelResponse.setOwner(owner);
        detailMotelResponse.setCreatedAt(dateTimeFormatter.format(motel.getCreatedAt()));

        if (isAdmin
                || detailMotelResponse.getStatus().equals(MotelStatus.AVAILABLE)
                || detailMotelResponse.getOwnerId().equals(currentUser)) {
            return detailMotelResponse;
        }

        throw new AppException(ErrorCode.MOTEL_NOT_FOUND);
    }


    public MotelResponse create(MotelCreationRequest request) {
        Double CHARGE_RATE = 0.05;
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Motel motel = motelMapper.toMotel(request);
        motel.setCreatedAt(Instant.now());
        motel.setStatus(MotelStatus.NOT_APPROVED);
        motel.setOwnerId(username);
        motel.setCharge(motel.getPrice() * CHARGE_RATE);
        motel.setVisibleUtil(null);
        motelRepository.save(motel);
        return motelMapper.toMotelResponse(motel);
    }

    @OwnerOnly
    public MotelResponse update(String motelId, MotelUpdationRequest request) {
        Motel motel = motelRepository.findById(motelId).orElseThrow(() -> new AppException(ErrorCode.MOTEL_NOT_FOUND));
        try {
            motelMapper.updateMotel(motel, request);
            motelRepository.save(motel);
            return motelMapper.toMotelResponse(motel);
        } catch (RuntimeException exception) {
            log.error(exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    @OwnerOnly
    public MotelResponse delete(String motelId) {
        Motel motel = motelRepository.findById(motelId).orElseThrow(()
                -> new AppException(ErrorCode.MOTEL_NOT_FOUND));
        motel.setStatus(MotelStatus.DELETED);
        motelRepository.save(motel);
        return motelMapper.toMotelResponse(motel);
    }

    @AdminOnly
    public MotelResponse approveMotel(String motelId) {
        Motel motel = motelRepository.findById(motelId).orElseThrow(() -> new AppException(ErrorCode.MOTEL_NOT_FOUND));
        if (motel.isApproved()) {
            motel.setStatus((MotelStatus.NOT_APPROVED));
        } else {
            motel.setStatus((MotelStatus.AVAILABLE));
        }
        motel.setApproved(!motel.isApproved());
        motelRepository.save(motel);

        if (motel.isApproved()) {
            UserResponse userResponse = userClient.getUserById(motel.getOwnerId()).getResult();

            Map<String, String> params = new HashMap<>();
            params.put("userName", String.format("%s %s", userResponse.getFirstName(), userResponse.getLastName()));
            params.put("motelName", motel.getName());
            params.put("address", motel.getLocation().getFullLocation());
            params.put("price", motel.getPrice().toString());
            params.put("motelLink", "https://cahouse.vn/motel/" + motel.getId());

            NotificationEvent notificationEvent = NotificationEvent.builder().chanel("EMAIL").recipient(userResponse.getEmail()).template(TemplateEnum.CREATE_MOTEL_SUCCESS).params(params).build();

            kafkaTemplate.send("notification-delivery", notificationEvent);
        }

        return motelMapper.toMotelResponse(motel);
    }

    public List<MotelResponse> getNearestMotel(Double longitude, Double latitude, Double radius) {
        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        return motelRepository.findNearestMotels(point, radius).stream().map(motelMapper::toMotelResponse).toList();
    }

    public List<MotelResponse> getMotelsByUser(String userId) {
        return motelRepository
                .findByOwnerId(userId)
                .stream().map(motelMapper::toMotelResponse)
                .filter(motel -> !motel.getStatus().equals(MotelStatus.DELETED))
                .toList();
    }

    public List<StatPriceResponse> getMotelsByPriceGroup() {
        return motelRepository.statByPrices().stream().map(r -> StatPriceResponse.builder().range(Float.parseFloat(r[0].toString())).count(Integer.parseInt(r[1].toString())).build()).collect(Collectors.toList());
    }

    public List<StatAreaResponse> getMotelsByAreaGroup() {
        return motelRepository.statByArea().stream().map(r -> StatAreaResponse.builder().range(Float.parseFloat(r[0].toString())).count(Integer.parseInt(r[1].toString())).build()).collect(Collectors.toList());
    }

    public List<StatPeriodResponse> getMotelsByTime(LocalDate startDate, LocalDate endDate) {
        Instant startInstant = startDate.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endInstant = endDate.atStartOfDay(ZoneOffset.UTC).toInstant();
        return motelRepository.statByTime(startInstant, endInstant).stream().map(r -> StatPeriodResponse.builder().period(r[1].toString() + "/" + r[0].toString()).count(Integer.parseInt(r[2].toString())).build()).collect(Collectors.toList());
    }

    public List<StatTypeResponse> getMotelsByType() {
        return motelRepository.statByType().stream().map(r -> StatTypeResponse.builder().type(r[0].toString()).count(Integer.parseInt(r[1].toString())).build()).collect(Collectors.toList());
    }


}
