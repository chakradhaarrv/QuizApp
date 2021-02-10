package com.crio.buildout.controller;
//CHECKSTYLE:OFF
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.crio.buildout.dto.GetQuestionResponse;
import com.crio.buildout.dto.SubmitQuestionRequest;
import com.crio.buildout.dto.SubmitQuestionResponse;
import com.crio.buildout.service.QaService;
//import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quiz")
public class QApiController {

    @Autowired
    QaService qaService;

    @RequestMapping(value = "/{moduleId}", method = RequestMethod.GET)
    public ResponseEntity<GetQuestionResponse> qetQuestions(@PathVariable String moduleId,
            HttpServletRequest request) {
        GetQuestionResponse response = qaService.getModuleQuestions(moduleId);
        if (response.getQuestions().size() == 0) {
            return new ResponseEntity<GetQuestionResponse>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<GetQuestionResponse>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/{moduleId}", method = RequestMethod.POST)
    public ResponseEntity<SubmitQuestionResponse> submitResponse(@PathVariable String moduleId,
            @RequestBody String requestData) {
        ObjectMapper mapper = new ObjectMapper();
        SubmitQuestionRequest submitQuestionRequest = null;
        if (requestData == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            submitQuestionRequest = mapper.readValue(requestData, SubmitQuestionRequest.class);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        SubmitQuestionResponse response = qaService.validateResponse(moduleId, submitQuestionRequest);
        if (response.getQuestions().size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}