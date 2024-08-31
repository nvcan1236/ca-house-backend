package com.nvc.motel_service.enums;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JobConverter {
    public static String listToString(List<Job> array) {
        return array != null
                ? array.stream()
                .map(Enum::name)  // Sử dụng name() để lấy tên của enum
                .collect(Collectors.joining(", "))
                : null;
    }

    public static List<Job> stringToList(String jobs) {
        return Arrays.stream(jobs.split(", "))
                .map(Job::valueOf)
                .toList();
    }
}
