package com.formssafe.domain.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    public List<Test> getAll(){
        return testRepository.findAll();
    }
    public Test add(Test test){
        return testRepository.save(test);
    }
}
