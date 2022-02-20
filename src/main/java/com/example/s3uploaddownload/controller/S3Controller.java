package com.example.s3uploaddownload.controller;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.UUID;

@RestController
public class S3Controller {
    private static final Logger log = LoggerFactory.getLogger(S3Controller.class);

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    @PostMapping("/upload")
    public String uploadToAWS(MultipartFile file) {
        String key = UUID.randomUUID() + "_" + file.getOriginalFilename();
        try {

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            PutObjectRequest request = new PutObjectRequest(bucketName, key, file.getInputStream(), metadata);
            request.withCannedAcl(CannedAccessControlList.AuthenticatedRead); // 접근권한 체크
            PutObjectResult result = s3Client.putObject(request);
            return key;
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            log.error("uploadToAWS AmazonServiceException error={}", e.getMessage());
        } catch (Exception e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            log.error("uploadToAWS SdkClientException error={}", e.getMessage());
        }

        return "";
    }

    @GetMapping("/display")
    @ResponseBody
    public String getFileURL(String fileName) {
        System.out.println("넘어오는 파일명 : "+fileName);

        // set expiration
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60; // 1 hour
        expiration.setTime(expTimeMillis);

        // Generate the presigned URL.
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, (fileName).replace(File.separatorChar, '/'))
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);

        return s3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }


    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity<byte[]> getFile(String fileName){
        log.info("fileName: "+ fileName);
        ResponseEntity<byte[]> result = null;
        try {
            HttpHeaders header = new HttpHeaders();

            // read from S3
            URL url = new URL(getFileURL(fileName));
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            InputStream fileIS = urlConn.getInputStream();

            // MIME regardless of extention
            header.add("Content-Type", URLConnection.guessContentTypeFromStream(fileIS));

            result = new ResponseEntity<>(IOUtils.toByteArray(fileIS), header, HttpStatus.OK);

        } catch(IOException e) {
            log.info("wrong file path");
        }
        return result;
    }

}
