package com.crio.buildout.repository;
//CHECKSTYLE:OFF
import java.util.List;

import com.crio.buildout.repository.entity.Question;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionRepository extends MongoRepository<Question, String> {
    public List<Question> findAllByModuleId(String moduleName);
    
}