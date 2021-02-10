package com.crio.buildout.service;
//CHECKSTYLE:OFF
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.crio.buildout.dto.*;
import com.crio.buildout.repository.entity.Question;
import com.crio.buildout.repositoryservice.QuestionRepositoryService;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QaService {
    @Autowired
    QuestionRepositoryService questionRepositoryService;

    public GetQuestionResponse getModuleQuestions(String module) {
        List<Question> moduleQuestions = questionRepositoryService.getModuleQuestions(module);
        GetQuestionResponse response = new GetQuestionResponse();
        List<GetQuestionResponseDto> questionResponseDto = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (Question moduleQuestion : moduleQuestions) {
            GetQuestionResponseDto dto = modelMapper.map(moduleQuestion, GetQuestionResponseDto.class);
            questionResponseDto.add(dto);
        }
        response.setQuestions(questionResponseDto);
        return response;
    }

    public SubmitQuestionResponse validateResponse(String moduleId, SubmitQuestionRequest request) {
        Integer score = 0;
        List<Question> moduleQuestions = questionRepositoryService.getModuleQuestions(moduleId);
        HashMap<String, SubmitQuestionRequestDto> userSubmission = mapUserResponse(request);
        List<SubmitQuestionResponseDto> resultList = new ArrayList<>();
        for (Question moduleQuestion : moduleQuestions) {
            SubmitQuestionResponseDto questionResult = new SubmitQuestionResponseDto();
            addQuestionDescription(questionResult, moduleQuestion);
            SubmitQuestionRequestDto userResponseForQuestion =
                userSubmission.get(moduleQuestion.getQuestionId());
            if (userResponseForQuestion != null) {
                questionResult.setUserAnswer(userResponseForQuestion.getUserResponse());
                if (questionResult.getCorrect().equals(questionResult.getUserAnswer())) {
                    questionResult.setAnswerCorrect(true);
                    score += 1; 
                }
            }
            resultList.add(questionResult);
        }
        SubmitQuestionResponse response = new SubmitQuestionResponse();
        response.setQuestions(resultList);
        Summary summary = new Summary(score, moduleQuestions.size());
        response.setSummary(summary);
        return response;
    }

    private void addQuestionDescription(SubmitQuestionResponseDto questionResult,
            Question moduleQuestion) {
        ModelMapper modelMapper = new ModelMapper();
        PropertyMap<Question, SubmitQuestionResponseDto> propertyMap =
            new PropertyMap<Question, SubmitQuestionResponseDto>() {
                @Override
                protected void configure() {
                    map().setCorrect(source.getCorrectAnswer());
                    map().setAnswerCorrect(false);
                }
            };
        modelMapper.addMappings(propertyMap);
        modelMapper.map(moduleQuestion, questionResult);
        questionResult.setCorrect(moduleQuestion.getCorrectAnswer());
    }

    HashMap<String, SubmitQuestionRequestDto> mapUserResponse(SubmitQuestionRequest request) {
        HashMap<String, SubmitQuestionRequestDto> mappedUserSubmission = new HashMap<>();
        List<SubmitQuestionRequestDto> userSubmission = request.getUserResponse();
        if (userSubmission != null) {
            for (SubmitQuestionRequestDto requestDto : userSubmission) {
                mappedUserSubmission.put(requestDto.getQuestionId(), requestDto);
            }
        }
        return mappedUserSubmission;
    }
}