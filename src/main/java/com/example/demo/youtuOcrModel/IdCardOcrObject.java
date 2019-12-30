package com.example.demo.youtuOcrModel;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class IdCardOcrObject {
    private MultipartFile file;
    private String card_type;
}
