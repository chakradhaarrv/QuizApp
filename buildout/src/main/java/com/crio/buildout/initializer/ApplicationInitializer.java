package com.crio.buildout.initializer;
//CHECKSTYLE:OFF
import java.io.File;
import java.io.IOException;
import java.util.List;

import com.crio.buildout.repository.entity.Question;
import com.crio.buildout.repositoryservice.QuestionRepositoryService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;


public class ApplicationInitializer implements CommandLineRunner {
    
    @Autowired
    private QuestionRepositoryService questionRepositoryService;

    private final Logger logger = LogManager.getLogger(ApplicationInitializer.class);

    @Override
    public void run(String... args) {
        File databaseFile = new File(System.getProperty("user.dir") + "/../initial_data_load.json");
        logger.info("databaseFile @" + databaseFile.getAbsolutePath());
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Question> questions = mapper.readValue(databaseFile,
                new TypeReference<List<Question>>() {});
            questionRepositoryService.clearDatabase();
            int questionsAddedCount = questionRepositoryService.populateDatabase(questions);
            logger.info(String.format("Added %d items to database", questionsAddedCount));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}