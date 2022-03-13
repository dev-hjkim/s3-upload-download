package com.example.s3uploaddownload.controller;

import com.example.s3uploaddownload.common.util.AwsUtil;
import com.example.s3uploaddownload.dto.PostDto;
import com.example.s3uploaddownload.service.PostService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value="/v1/post")
@RequiredArgsConstructor
public class PostController {
    private static final Logger log = LoggerFactory.getLogger(PostController.class);

    private final AwsUtil awsUtil;

    private final PostService postService;

    @PostMapping("/upload")
    public void uploadFileToAWS(@Valid @RequestBody PostDto dto) {
        log.info("uploadFileToAWS {}", dto);
        
        // 1. DB에 게시글 정보 저장
        postService.savePost(dto);
        
        // 2.이미지 업로드
        List<String> links = awsUtil.uploadImage(dto);
        dto.setLinks(links);

        // 3.이미지 정보 db 저장
        postService.saveLinks(dto);
    }
}
