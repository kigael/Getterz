package com.getterz.domain.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserType {

    ADMIN(0),
    BUYER(1),
    SELLER(2);

    private final Integer id;

}
