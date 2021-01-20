package com.getterz.domain.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderByType {

    ORDER_BY_DISTANCE_ASC(0),
    ORDER_BY_DISTANCE_DESC(1),
    ORDER_BY_COST_ASC(2),
    ORDER_BY_COST_DESC(3),
    ORDER_BY_REVIEW_ASC(4),
    ORDER_BY_REVIEW_DESC(5),
    ORDER_BY_REGISTER_DATE_ASC(6),
    ORDER_BY_REGISTER_DATER_DESC(7),
    DO_NOT_ORDER(8);

    private final Integer id;

}
