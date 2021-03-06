package com.getterz.network.session;

import com.getterz.crypt.Cryptor;
import com.getterz.domain.enumclass.UserType;
import com.getterz.domain.model.Admin;
import com.getterz.domain.model.Buyer;
import com.getterz.domain.model.Seller;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class SessionApi {

    private static final Map<String, LocalDateTime> sessions = new HashMap<>();

    public static String startSession(Admin user){
        clearSession();
        String str = Cryptor.ENCRYPT(Session.builder()
                .id(user.getId())
                .password(user.getPassword())
                .emailAddress(user.getEmailAddress())
                .cellNumber(user.getCellNumber())
                .userType(UserType.ADMIN)
                .sessionStartTime(LocalDateTime.now())
                .build().toString());
        sessions.put(str,LocalDateTime.now());
        return str;
    }

    public static String startSession(Buyer user) {
        clearSession();
        String str = Cryptor.ENCRYPT(Session.builder()
                .id(user.getId())
                .password(user.getPassword())
                .emailAddress(user.getEmailAddress())
                .cellNumber(user.getCellNumber())
                .userType(UserType.BUYER)
                .sessionStartTime(LocalDateTime.now())
                .build().toString());
        sessions.put(str,LocalDateTime.now());
        return str;
    }

    public static String startSession(Seller user) {
        clearSession();
        String str = Cryptor.ENCRYPT(Session.builder()
                .id(user.getId())
                .password(user.getPassword())
                .emailAddress(user.getEmailAddress())
                .cellNumber(user.getCellNumber())
                .userType(UserType.SELLER)
                .sessionStartTime(LocalDateTime.now())
                .build().toString());
        sessions.put(str,LocalDateTime.now());
        return str;
    }

    public static boolean checkSession(String session){
        if(session==null||!sessions.containsKey(session)){
            return false;
        }
        else {
            return !sessions.get(session).plusHours(1).isBefore(LocalDateTime.now());
        }
    }

    public static boolean endSession(String session) {
        if(session==null||!sessions.containsKey(session)) {
            return false;
        }
        else {
            sessions.remove(session);
            return true;
        }
    }

    public static String updateSession(String session) {
        if(session==null||!sessions.containsKey(session)) {
            return null;
        }
        else {
            sessions.replace(session,LocalDateTime.now());
            return session;
        }
    }

    private static void clearSession(){
        for(Map.Entry<String, LocalDateTime> s : sessions.entrySet())
            if(s.getValue().plusHours(1).isBefore(LocalDateTime.now()))
                sessions.remove(s.getKey());
    }

}
