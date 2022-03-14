package com.example.s3uploaddownload.mapper;

import com.example.s3uploaddownload.dto.PostDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
    int savePost(PostDto dto);

    int saveLinks(PostDto dto);

    PostDto selectPost(String seq);

    List<String> selectLinks(String seq);
}
