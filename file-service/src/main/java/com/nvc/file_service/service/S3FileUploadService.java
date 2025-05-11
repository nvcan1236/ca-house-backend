package com.nvc.file_service.service;

import com.nvc.file_service.enums.FileCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectAclRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class S3FileUploadService {

    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public List<String> uploadMultiFiles(List<MultipartFile> files, String category) {
        List<String> fileURLS = new ArrayList<>();
        files.forEach(file -> {
            try {
                fileURLS.add(uploadFile(file, category));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return fileURLS;
    }

    public String uploadFile(MultipartFile file, String category) throws IOException {
        File convertedFile = convertMultiPartToFile(file);
        String fileName = generateFileName(category);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .cacheControl("public, max-age=31536000")
                .build();
        s3Client.putObject(putObjectRequest, convertedFile.toPath());
        PutObjectAclRequest putObjectAclRequest = PutObjectAclRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();
        s3Client.putObjectAcl(putObjectAclRequest);
        convertedFile.delete();
        return getFileUrl(fileName);
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        }
        return convertedFile;
    }

    private String generateFileName(String category) {
        FileCategory cat = FileCategory.valueOf(category);
        return String.format("%s/%s",cat.getFolderName(),
                UUID.randomUUID() + ".");
    }

    private String getFileUrl(String fileName) {
        return s3Client.utilities().getUrl(b -> b.bucket(bucketName).key(fileName)).toExternalForm();
    }
}