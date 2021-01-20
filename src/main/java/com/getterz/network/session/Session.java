package com.getterz.network.session;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.getterz.crypt.Cryptor;
import com.getterz.domain.enumclass.UserType;
import com.getterz.domain.model.Admin;
import com.getterz.domain.model.Buyer;
import com.getterz.domain.model.Seller;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class Session {

    private Long id;

    private String password;

    private String emailAddress;

    private String cellNumber;

    private UserType userType;

    private LocalDateTime sessionStartTime;

    public String toString(){
        try{
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(this);
        }catch (JsonProcessingException e){
            e.printStackTrace();
            return null;
        }
    }

    public static Session toSession(String session) {
        try{
            return new ObjectMapper().registerModule(new JavaTimeModule()).readValue(session,Session.class);
        }catch (JsonProcessingException e){
            e.printStackTrace();
            return null;
        }
    }

    public static boolean verifySeller(String session, Seller seller){
        Session s = toSession(Cryptor.DECRYPT(session));
        try{
            if(!s.getId().equals(seller.getId())) return false;
            if(!s.getPassword().equals(seller.getPassword())) return false;
            if(!s.getEmailAddress().equals(seller.getEmailAddress())) return false;
            if(!s.getCellNumber().equals(seller.getCellNumber())) return false;
            if(s.getUserType()!=UserType.SELLER) return false;
        }catch(NullPointerException e){
            return false;
        }
        return true;
    }

    public static boolean verifyBuyer(String session, Buyer buyer){
        Session s = toSession(Cryptor.DECRYPT(session));
        try{
            if(!s.getId().equals(buyer.getId())) return false;
            if(!s.getPassword().equals(buyer.getPassword())) return false;
            if(!s.getEmailAddress().equals(buyer.getEmailAddress())) return false;
            if(!s.getCellNumber().equals(buyer.getCellNumber())) return false;
            if(s.getUserType()!=UserType.BUYER) return false;
        }catch(NullPointerException e){
            return false;
        }
        return true;
    }

    public static boolean verifyAdmin(String session, Admin admin){
        Session s = toSession(Cryptor.DECRYPT(session));
        try{
            if(!s.getId().equals(admin.getId())) return false;
            if(!s.getPassword().equals(admin.getPassword())) return false;
            if(!s.getEmailAddress().equals(admin.getEmailAddress())) return false;
            if(!s.getCellNumber().equals(admin.getCellNumber())) return false;
            if(s.getUserType()!=UserType.ADMIN) return false;
        }catch(NullPointerException e){
            return false;
        }
        return true;
    }

}
