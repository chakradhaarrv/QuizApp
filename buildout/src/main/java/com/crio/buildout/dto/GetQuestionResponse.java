package com.crio.buildout.dto;
//CHECKSTYLE:OFF
import lombok.Data;

import java.util.List;

@Data
public class GetQuestionResponse {
    List<GetQuestionResponseDto> questions;
}