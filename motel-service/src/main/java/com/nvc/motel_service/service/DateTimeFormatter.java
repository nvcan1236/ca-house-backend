package com.nvc.motel_service.service;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class DateTimeFormatter {
    Map<Long, Function<Instant, String>> strategiesMap = new LinkedHashMap<>();

    public DateTimeFormatter() {
        strategiesMap.put(60L, this::formatInSecond);
        strategiesMap.put(3600L, this::formatInMinute);
        strategiesMap.put(86400L, this::formatInHour);
        strategiesMap.put(Long.MAX_VALUE, this::formatInDate);
    }

    private String formatInSecond(Instant instant) {
        long seconds = ChronoUnit.SECONDS.between(instant, Instant.now());
        return seconds + "giây";
    }

    private String formatInMinute(Instant instant) {
        long minutes = ChronoUnit.MINUTES.between(instant, Instant.now());
        return minutes + "phút";
    }

    private String formatInHour(Instant instant) {
        long hours = ChronoUnit.HOURS.between(instant, Instant.now());
        return hours + "giờ";
    }

    private String formatInDate(Instant instant) {
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        java.time.format.DateTimeFormatter dateTimeFormatter = java.time.format.DateTimeFormatter.ISO_DATE;
        return localDateTime.format(dateTimeFormatter);
    }


    public String format(Instant instant) {
        long seconds = ChronoUnit.SECONDS.between(instant, Instant.now());
        var formatFunction = strategiesMap.entrySet().stream()
                .filter(longFunctionEntry -> seconds < longFunctionEntry.getKey())
                .findFirst().get().getValue();
        return formatFunction.apply(instant);
    }
}
