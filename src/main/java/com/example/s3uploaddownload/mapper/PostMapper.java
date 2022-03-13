package com.example.s3uploaddownload.mapper;

import com.example.s3uploaddownload.dto.PostDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper {
    int savePost(PostDto dto);

    int saveLinks(PostDto dto);
}
