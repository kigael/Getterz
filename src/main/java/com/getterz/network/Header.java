package com.getterz.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getterz.network.session.Session;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Header<T> {

    private LocalDateTime transactionTime;

    private String resultCode;

    private String transactionType;

    private String description;

    private T data;

    private String session;

    public static <T> Header<T> OK(String transactionType, String session){
        return (Header<T>)Header.builder()
                .transactionTime(LocalDateTime.now())
                .resultCode("OK")
                .transactionType(transactionType)
                .description("SUCCESS")
                .session(session)
                .build();
    }

    public static <T> Header<T> OK(String transactionType, String description, String session){
        return (Header<T>)Header.builder()
                .transactionTime(LocalDateTime.now())
                .resultCode("OK")
                .transactionType(transactionType)
                .description(description)
                .session(session)
                .build();
    }

    public static <T> Header<T> OK(String transactionType, T data, String session){
        return (Header<T>)Header.builder()
                .transactionTime(LocalDateTime.now())
                .resultCode("OK")
                .transactionType(transactionType)
                .description("SUCCESS")
                .data(data)
                .session(session)
                .build();
    }

    public static <T> Header<T> ERROR(String transactionType, String description, String session){
        return (Header<T>)Header.builder()
                .transactionTime(LocalDateTime.now())
                .resultCode("ERROR")
                .transactionType(transactionType)
                .description(description)
                .session(session)
                .build();
    }

    public static <T> Header<T> toHeader(String header){
        try{
            return (Header<T>)new ObjectMapper().readValue(header,Header.class);
        }catch (JsonProcessingException e){
            return null;
        }
    }

}
