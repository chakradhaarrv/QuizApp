package com.crio.buildout.dto;
//CHECKSTYLE:OFF
import lombok.Data;

import java.util.Map;

@Data
public class GetQuestionResponseDto {
    String questionId;
    String title;
    String type;
    Map<String, String> options;
}