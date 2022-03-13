package com.example.s3uploaddownload.service;

import com.example.s3uploaddownload.dto.PostDto;

import java.util.List;

public interface PostService {
    int savePost(PostDto dto);

    int saveLinks(PostDto dto);
}
