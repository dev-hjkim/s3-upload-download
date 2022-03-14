package com.example.s3uploaddownload.service.impl;

import com.example.s3uploaddownload.dto.PostDto;
import com.example.s3uploaddownload.mapper.PostMapper;
import com.example.s3uploaddownload.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostMapper postMapper;

    @Override
    public int savePost(PostDto dto) {
        return postMapper.savePost(dto);
    }

    @Override
    public int saveLinks(PostDto dto) {
        return postMapper.saveLinks(dto);
    }

    @Override
    public PostDto selectPost(String seq) {
        PostDto res = postMapper.selectPost(seq);
        res.setLinks(postMapper.selectLinks(seq));
        return res;
    }
}
