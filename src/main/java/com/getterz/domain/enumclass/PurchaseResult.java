package com.getterz.domain.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PurchaseResult {

    SUCCESS(0,"Delivered Successfully","Delivery of product has been completed successfully."),
    FAIL(0,"Delivery Failed","Delivery of product has failed for some reason.");

    private final Integer id;
    private final String title;
    private final String description;

}
