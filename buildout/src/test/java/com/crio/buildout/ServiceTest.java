package com.crio.buildout;
//CHECKSTYLE:OFF
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.crio.buildout.dto.*;
import com.crio.buildout.repository.entity.Question;
import com.crio.buildout.repositoryservice.QuestionRepositoryService;
import com.crio.buildout.service.QaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = {BuildoutApplication.class})
@ExtendWith(MockitoExtension.class)
public class ServiceTest {
    
    @MockBean
    QuestionRepositoryService qRepositoryService;

    @InjectMocks
    QaService qaService;

    ObjectMapper mapper;
    Question question1;
    Question question2;
    List<GetQuestionResponseDto> questionDtos;
    SubmitQuestionRequest userResponse;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        mapper = new ObjectMapper();
    }

    private void initQuestions() {
        question1 = new Question("001", "1", "What is the parent interface/class of Exception class?",
            "java Question", "Subjective", null, Arrays.asList("throwable"), null);
        HashMap<String, String> options = new HashMap<>();
        options.put("1", "0.0.0.0");
        options.put("2", "255.255.255.255");
        options.put("3", "192.168.1.0");
        options.put("4", "127.0.0.1");
        question2 = new Question("002", "1", "What is the default localhost IP address?", "General Question",
            "Objective-single", options, Arrays.asList("3"), null);
    }

    private void setUserResponse() {
        SubmitQuestionRequestDto q1Response = new SubmitQuestionRequestDto(
            "001", Arrays.asList("throwable"));
        SubmitQuestionRequestDto q2Response = new SubmitQuestionRequestDto(
            "002", Arrays.asList("2"));
        userResponse = new SubmitQuestionRequest(Arrays.asList(q1Response, q2Response));
    }

    @Test
    public void validGetQuestionRequestGenerateResponse() {
        initQuestions();
        when(qRepositoryService.getModuleQuestions("1"))
            .thenReturn(Arrays.asList(question1, question2));
        List<GetQuestionResponseDto> fetchedDTOs = qaService.getModuleQuestions("1").getQuestions();
        assert (fetchedDTOs.equals(fetchedDTOs));
    }

  @Test
  public void invalidModuleReturnsEmptyResponse() {
    when(qRepositoryService.getModuleQuestions(any(String.class)))
        .thenReturn(new ArrayList<Question>());
    int noOfFetchedQuestions = qaService.getModuleQuestions("5").getQuestions().size();
    assertEquals(0, noOfFetchedQuestions);
    int size = qaService.validateResponse("5", new SubmitQuestionRequest()).getQuestions().size();
    assertEquals(0, size);
  }

  @Test
  public void validateRequestWithEmptyBodyGeneratesReturnsFalseForAllQuestions() {
    initQuestions();
    when(qRepositoryService.getModuleQuestions(any(String.class)))
        .thenReturn(Arrays.asList(question1, question2));
    List<SubmitQuestionResponseDto> validationResult =
        qaService.validateResponse("5", new SubmitQuestionRequest()).getQuestions();
    for (SubmitQuestionResponseDto questionResponseDTO : validationResult) {
      assertEquals(false, questionResponseDTO.isAnswerCorrect());
    }
  }

  @Test
  public void checkValidResultGenerated() {
    initQuestions();
    setUserResponse();
    when(qRepositoryService.getModuleQuestions(any(String.class)))
        .thenReturn(Arrays.asList(question1, question2));
    SubmitQuestionResponse validationResult = qaService.validateResponse("1", userResponse);
    assertEquals(true, validationResult.getQuestions().get(0).isAnswerCorrect());
    assertEquals(false, validationResult.getQuestions().get(1).isAnswerCorrect());
  }





}