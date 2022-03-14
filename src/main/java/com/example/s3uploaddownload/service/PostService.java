package com.example.s3uploaddownload.service;

import com.example.s3uploaddownload.dto.PostDto;

public interface PostService {
    int savePost(PostDto dto);

    int saveLinks(PostDto dto);

    PostDto selectPost(String seq);
}
