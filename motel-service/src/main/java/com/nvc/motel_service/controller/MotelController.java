package com.nvc.motel_service.controller;

import com.nvc.motel_service.entity.Motel;
import com.nvc.motel_service.service.MotelService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MotelController {
    MotelService motelService;
    @GetMapping("/")
    public List<Motel> getAll() {
        return motelService.getAll();
    }

    @PostMapping
    public Motel create(@RequestBody String name) {
        log.info("Name: {}", name);
        return motelService.create(name);
    }
}
