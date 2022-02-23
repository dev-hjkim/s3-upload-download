package com.example.s3uploaddownload.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class S3ControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void testUploadToAWS() {
        MockMultipartFile file = null;
        try {
            file = new MockMultipartFile(
                    "file",
                    "Changdeokgung_korea.jpg",
                    "img/jpeg",
                    getClass().getClassLoader().getResourceAsStream("Changdeokgung_korea.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mvc.perform(multipart("/upload")
                    .file(file))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}