package com.getterz.format.converter;

import com.getterz.domain.enumclass.Gender;

import java.util.HashSet;
import java.util.Set;

public class DtoConverter {

    public static String setOfStringToString(Set<String> input){
        StringBuilder ret = new StringBuilder();
        for(String i : input) ret.append("[").append(i).append("]").append("+");
        return ret.deleteCharAt(ret.length()-1).toString();
    }

    public static Set<String> stringToSetOfString(String input){
        Set<String> ret = new HashSet<>();
        for(String s : input.split("\\+")) ret.add(s.substring(1,s.length()-1));
        return ret;
    }

    public static String setOfGenderToString(Set<Gender> input){
        StringBuilder ret = new StringBuilder();
        for(Gender g : input) ret.append("[").append(g.name()).append("]").append("+");
        return ret.deleteCharAt(ret.length()-1).toString();
    }

    public static Set<Gender> stringToSetOfGender(String input){
        Set<Gender> ret = new HashSet<>();
        for(String s : input.split("\\+")) ret.add(Gender.valueOf(s.substring(1,s.length()-1)));
        return ret;
    }

}
