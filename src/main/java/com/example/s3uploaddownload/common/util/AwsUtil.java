package com.example.s3uploaddownload.common.util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.s3uploaddownload.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AwsUtil {
    private static final Logger log = LoggerFactory.getLogger(AwsUtil.class);

    @Value("${aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3 s3Client;

    @Async
    public List<String> uploadImage(PostDto dto) {
        List<String> links = new ArrayList<>();
        int attachSeq = 1;
        for (MultipartFile item : dto.getAttachments()) {
            String key = "test/" + dto.getSeq() + attachSeq;

            try {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(item.getContentType());
                PutObjectRequest request = new PutObjectRequest(bucketName, key, item.getInputStream(), metadata);
                request.withCannedAcl(CannedAccessControlList.PublicRead); // 접근권한 체크
                s3Client.putObject(request);
                links.add(s3Client.getUrl(bucketName, key).toString());
            } catch (AmazonServiceException e) {
                log.error("uploadToAWS AmazonServiceException error={}", e.getMessage());
            } catch (Exception e) {
                log.error("uploadToAWS SdkClientException error={}", e.getMessage());
            }
            attachSeq += 1;
        }
        return links;
    }
}
