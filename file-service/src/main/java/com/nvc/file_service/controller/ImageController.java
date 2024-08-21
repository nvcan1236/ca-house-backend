package com.nvc.file_service.controller;

import com.nvc.file_service.service.S3FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class ImageController {

    @Autowired
    private S3FileUploadService s3FileUploadService;


    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file,
                                              @RequestParam String category)
            throws IOException {
        String imageUrl = s3FileUploadService.uploadFile(file, category);

        return new ResponseEntity<>(imageUrl, HttpStatus.OK);
    }
}
