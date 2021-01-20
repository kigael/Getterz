package com.getterz.domain.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.getterz.GetterzApplicationTests;
import com.getterz.crypt.Cryptor;
import com.getterz.domain.enumclass.Gender;
import com.getterz.domain.repository.BuyerRepository;
import com.getterz.network.Header;
import com.getterz.network.request.BuyerApiRequest;
import com.getterz.network.response.BuyerApiResponse;
import com.getterz.network.session.Session;
import com.getterz.service.adminApi.BuyerApiLogicService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class BuyerTests extends GetterzApplicationTests {

    @Autowired
    private BuyerApiLogicService buyerApiLogicService;
    @Autowired
    private BuyerRepository buyerRepository;

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
                .jobs(jobs)
                .cryptoWallet("3FZbgi29cpjq2GjdwV8eyHuJJnkLtktZc5")
                .build();
        Header<BuyerApiResponse> response = buyerApiLogicService.create(Header.OK("BUYER CREATE",request,"no session"));
        System.out.println(response);
    }

    @Test
    public void deleteTestBuyer(){
        Optional<Buyer> buyer = buyerRepository.findById(1L);
        buyer.ifPresent(value -> buyerRepository.delete(value));
    }

    @Test
    public void deleteAllBuyers(){
        buyerRepository.deleteAll();
    }

    @Test
    public void convertingTest(){
        String token="FacqAH2D6GY9%2FZxoEGSOR%2FsT1KH0kBEEz%2BZjyXIxHThQBcCB3yA0mVqZUeMdHnhC6JBr0DsVrgACqLW81ds4ow%3D%3D";
        String sessionStr = Cryptor.DECRYPT(URLDecoder.decode(token,StandardCharsets.UTF_8));
        System.out.println(sessionStr);
    }

}