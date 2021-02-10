package com.crio.buildout.dto;
//CHECKSTYLE:OFF
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitQuestionRequest {
    @JsonProperty("responses")
    List<SubmitQuestionRequestDto> userResponse;
}