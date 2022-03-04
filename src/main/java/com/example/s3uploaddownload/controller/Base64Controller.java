package com.example.s3uploaddownload.controller;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.example.s3uploaddownload.dto.UploadDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

@RestController
@RequestMapping(value="/v1/base64")
@RequiredArgsConstructor
public class Base64Controller {
    private static final Logger log = LoggerFactory.getLogger(Base64Controller.class);

    @Value("${aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3 s3Client;

    @PostMapping("/upload")
    public String uploadBase64ToAWS(@Valid @RequestBody UploadDto dto) {
        log.info("uploadBase64ToAWS {}", dto);
        byte[] decodedBytes = Base64.getDecoder().decode(dto.getAttachments().get(0));
        InputStream toBeUploaded = new ByteArrayInputStream(decodedBytes);

        String key = "masters/1_1";
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/png");
            metadata.setContentLength(decodedBytes.length);
            PutObjectRequest request = new PutObjectRequest(bucketName, key, toBeUploaded, metadata);
            request.withCannedAcl(CannedAccessControlList.PublicRead); // 접근권한 체크
            PutObjectResult result = s3Client.putObject(request);
            return key;
        } catch (AmazonServiceException e) {
            log.error("uploadToAWS AmazonServiceException error={}", e.getMessage());
        } catch (Exception e) {
            log.error("uploadToAWS SdkClientException error={}", e.getMessage());
        }

        return "";
    }

}
