package com.getterz.service.buyerApi;

import com.getterz.GetterzApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BuyerAuthenticationLogicServiceTest extends GetterzApplicationTests {

    @Test
    public void mkdirTest(){
        File directory = new File("/upload");
        if (!directory.exists()) {
            System.out.println("Dir doesn't exist");
            boolean result = directory.mkdir();
            System.out.println(result);
        }
        else{
            System.out.println("Dir exists");
        }
    }

    @Test
    public void resourceDir() throws IOException {
        System.out.println(new ClassPathResource(".").getFile());
    }

}