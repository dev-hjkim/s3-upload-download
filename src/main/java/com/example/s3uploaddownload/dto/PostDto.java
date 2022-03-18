package com.example.s3uploaddownload.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    @NotEmpty
    private String title;

    private final String writer = "hjkim";

    @NotNull
    @Size(max=3, min=3)
    private List<MultipartFile> attachments;

    private String seq;

    private List<String> links;
}
