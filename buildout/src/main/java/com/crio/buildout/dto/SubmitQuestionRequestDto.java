package com.crio.buildout.dto;
//CHECKSTYLE:OFF
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitQuestionRequestDto {
    String questionId;
    List<String> userResponse;
}