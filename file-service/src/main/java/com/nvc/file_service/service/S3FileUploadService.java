package com.nvc.file_service.service;
import com.nvc.file_service.enums.FileCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectAclRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
        String fileName = generateFileName(file, category);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
        PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, convertedFile.toPath());
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
        File convertedFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(file.getBytes());
        fos.close();
        return convertedFile;
    }

    private String generateFileName(MultipartFile file, String category) {
        FileCategory cat = FileCategory.valueOf(category);
        return String.format("%s/%s",cat.getFolderName(),
                UUID.randomUUID() + "_" + file.getOriginalFilename());
    }

    private String getFileUrl(String fileName) {
        return s3Client.utilities().getUrl(b -> b.bucket(bucketName).key(fileName)).toExternalForm();
    }
}