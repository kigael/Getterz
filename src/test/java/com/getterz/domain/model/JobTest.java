package com.getterz.domain.model;

import com.getterz.GetterzApplicationTests;
import com.getterz.domain.repository.JobRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class JobTest extends GetterzApplicationTests {
    @Autowired
    private JobRepository jobRepository;
    @Test
    public void deleteAllJobs(){
        jobRepository.deleteAll();
    }
}