package com.getterz.domain.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {

    MALE_STRAIGHT(0,"Male&Straight"),
    MALE_GAY(1,"Male&Gay"),
    MALE_BISEXUAL(2,"Male&Bisexual"),
    FEMALE_STRAIGHT(3,"Female&Straight"),
    FEMALE_GAY(4,"Female&Gay"),
    FEMALE_BISEXUAL(5,"Female&Bisexual");

    private final Integer id;
    private final String title;

}
