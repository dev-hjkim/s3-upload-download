package com.example.s3uploaddownload.dto;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@ToString
public class UploadDto {
    @NotNull
    private List<String> attachments;
}
