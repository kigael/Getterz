package com.getterz.domain.model;

import com.getterz.GetterzApplicationTests;
import com.getterz.domain.repository.AdminRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest extends GetterzApplicationTests {

    @Autowired
    private AdminRepository adminRepository;

    @Test
    public void deleteAllAdmin(){
        adminRepository.deleteAll();
    }

}