package com.getterz.domain.model;

import com.getterz.GetterzApplicationTests;
import com.getterz.domain.enumclass.Gender;
import com.getterz.network.Header;
import com.getterz.network.request.BuyerApiRequest;
import com.getterz.network.response.BuyerApiResponse;
import com.getterz.service.adminApi.BuyerApiLogicService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BuyerTests extends GetterzApplicationTests {

    @Autowired
    private BuyerApiLogicService buyerApiLogicService;

    @Test
    public void insertTestBuyer(){
        Set<String> jobs = new HashSet<>();
        jobs.add("University Student");
        jobs.add("Programmer");
        BuyerApiRequest request = BuyerApiRequest.builder()
                .name("Test")
                .password("TestPassword123!@#")
                .emergencyPassword("TestEmergency123!@#")
                .gender(Gender.MALE_STRAIGHT)
                .dateOfBirth(LocalDate.of(1995,2,4))
                .emailAddress("test01user@gmail.com")
                .cellNumber("+82 1012345678")
                .latitude(6.9)
                .longitude(7.4)
                .address("test address of somewhere 123-123")
                .job(jobs)
                .annualIncome(BigDecimal.valueOf(55000))
                .cryptoWallet("3FZbgi29cpjq2GjdwV8eyHuJJnkLtktZc5")
                .build();
        Header<BuyerApiResponse> response = buyerApiLogicService.create(Header.OK("BUYER CREATE",request,"no session"));
        System.out.println(response);
    }

}