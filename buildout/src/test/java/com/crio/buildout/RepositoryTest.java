package com.crio.buildout;
//CHECKSTYLE:OFF
//import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashMap;

import com.crio.buildout.repository.entity.Question;
import com.crio.buildout.repositoryservice.QuestionRepositoryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {BuildoutApplication.class})
public class RepositoryTest {
    
    @Autowired
    QuestionRepositoryService qRepositoryService;
    Question question1;
    Question question2;

    @BeforeEach
    public void init() {
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

    @Test
    public void clearOperationEmptiesDatabase() {
        qRepositoryService.clearDatabase();
        assertEquals(0, qRepositoryService.getModuleQuestions("1").size());
    }

    @Test
    public void populateDatabaseInsertsItemsToDatabase() {
        qRepositoryService.clearDatabase();
        qRepositoryService.populateDatabase(Arrays.asList(question1, question2));
        assertEquals(2, qRepositoryService.getModuleQuestions("1").size());
    }

    @Test
    public void getMoudleQuestionReturnsValidResponse() {
        qRepositoryService.clearDatabase();
        qRepositoryService.populateDatabase(Arrays.asList(question1, question2));
        question1.setModuleId("2");
        qRepositoryService.populateDatabase(Arrays.asList(question1));
        assertEquals(2, qRepositoryService.getModuleQuestions("1").size());
        assertEquals(1, qRepositoryService.getModuleQuestions("2").size());
        assertEquals(0, qRepositoryService.getModuleQuestions("3").size());
    }

    @Test
    public void duplicateInsertionThrowsException() {
        qRepositoryService.clearDatabase();
        qRepositoryService.populateDatabase(Arrays.asList(question1, question2));
        qRepositoryService.populateDatabase(Arrays.asList(question1, question2));
        assertEquals(2, qRepositoryService.getModuleQuestions("1").size());
    }
}