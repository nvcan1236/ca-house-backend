package com.nvc.file_service.controller;

import com.nvc.file_service.service.S3FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
public class ImageController {

    @Autowired
    private S3FileUploadService s3FileUploadService;


    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadImage(@RequestParam("images") List<MultipartFile> files,
                                                    @RequestParam String category) {
        log.info(files.toString());
        List<String> imageUrls = s3FileUploadService.uploadMultiFiles(files, category);

        return new ResponseEntity<>(imageUrls, HttpStatus.OK);
    }
}
