package com.getterz.domain.model;

import com.getterz.GetterzApplicationTests;
import com.getterz.domain.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest extends GetterzApplicationTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void deleteAllProducts(){
        productRepository.deleteAll();
    }

}