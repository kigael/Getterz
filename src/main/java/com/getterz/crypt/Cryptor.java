package com.getterz.crypt;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Component
public class Cryptor {

    @Autowired
    private Environment env;

    private static Cipher encryptCipher, decryptCipher;

    @PostConstruct
    void init(){
        try{
            String MASTER_KEY = env.getProperty("credential.cryptor.master_key");
            String INITIAL_VECTOR = env.getProperty("credential.cryptor.initial_vector");
            SecretKey secretKey = new SecretKeySpec(MASTER_KEY.getBytes(), "AES");
            encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey,new IvParameterSpec(INITIAL_VECTOR.getBytes()));
            decryptCipher.init(Cipher.DECRYPT_MODE, secretKey,new IvParameterSpec(INITIAL_VECTOR.getBytes(StandardCharsets.UTF_8)));
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public static String ENCRYPT(String plain){
        try {
            return new String(Base64.encodeBase64(encryptCipher.doFinal(plain.getBytes(StandardCharsets.UTF_8))));
        }catch(Exception e) {
            return null;
        }
    }

    public static String DECRYPT(String cipher){
        try{
            return new String(decryptCipher.doFinal(Base64.decodeBase64(cipher.getBytes())),StandardCharsets.UTF_8);
        }catch (Exception e){
            return null;
        }
    }

}
