package com.nvc.motel_service.service;

import com.nvc.motel_service.entity.Motel;
import com.nvc.motel_service.repository.MotelRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
public class MotelService {
    MotelRepository motelRepository;

    public List<Motel> getAll() {
        return motelRepository.findAll();
    }

    public Motel create(String name) {
        Motel motel = new Motel();
        motel.setName(name);
        return motelRepository.save(motel);
    }
}
