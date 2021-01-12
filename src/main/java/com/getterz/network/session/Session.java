package com.getterz.network.session;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getterz.crypt.Cryptor;
import com.getterz.domain.enumclass.Gender;
import com.getterz.domain.enumclass.UserType;
import com.getterz.domain.model.Buyer;
import com.getterz.domain.model.Seller;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@Accessors(chain = true)
public class Session {

    private Long id;

    private String name;

    private String password;

    private String emergencyPassword;

    private Gender gender;

    private LocalDate dateOfBirth;

    private String emailAddress;

    private String cellNumber;

    private Double latitude;

    private Double longitude;

    private String address;

    private String cryptoWallet;

    private UserType userType;

    private LocalDateTime sessionStartTime;

    public String toString(){
        try{
            return new ObjectMapper().writeValueAsString(this);
        }catch (JsonProcessingException e){
            return null;
        }
    }

    public static Session toSession(String session) {
        try{
            return new ObjectMapper().readValue(session,Session.class);
        }catch (JsonProcessingException e){
            return null;
        }
    }

    public static boolean verifySeller(String session, Seller seller){
        Session s = toSession(Cryptor.DECRYPT(session));
        try{
            if(!s.getId().equals(seller.getId())) return false;
            if(!s.getName().equals(seller.getName())) return false;
            if(!s.getPassword().equals(seller.getPassword())) return false;
            if(!s.getEmergencyPassword().equals(seller.getEmergencyPassword())) return false;
            if(s.getGender()!=seller.getGender()) return false;
            if(!s.getDateOfBirth().equals(seller.getDateOfBirth())) return false;
            if(!s.getEmailAddress().equals(seller.getEmailAddress())) return false;
            if(!s.getCellNumber().equals(seller.getCellNumber())) return false;
            if(!s.getLatitude().equals(seller.getLatitude())) return false;
            if(!s.getLongitude().equals(seller.getLongitude())) return false;
            if(!s.getAddress().equals(seller.getAddress())) return false;
            if(!s.getCryptoWallet().equals(seller.getCryptoWallet())) return false;
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
            if(!s.getName().equals(buyer.getName())) return false;
            if(!s.getPassword().equals(buyer.getPassword())) return false;
            if(!s.getEmergencyPassword().equals(buyer.getEmergencyPassword())) return false;
            if(s.getGender()!=buyer.getGender()) return false;
            if(!s.getDateOfBirth().equals(buyer.getDateOfBirth())) return false;
            if(!s.getEmailAddress().equals(buyer.getEmailAddress())) return false;
            if(!s.getCellNumber().equals(buyer.getCellNumber())) return false;
            if(!s.getLatitude().equals(buyer.getLatitude())) return false;
            if(!s.getLongitude().equals(buyer.getLongitude())) return false;
            if(!s.getAddress().equals(buyer.getAddress())) return false;
            if(!s.getCryptoWallet().equals(buyer.getCryptoWallet())) return false;
            if(s.getUserType()!=UserType.BUYER) return false;
        }catch(NullPointerException e){
            return false;
        }
        return true;
    }

}
