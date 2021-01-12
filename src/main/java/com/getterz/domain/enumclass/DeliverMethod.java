package com.getterz.domain.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeliverMethod {

    SELLER_DELIVERY(0,"Seller Delivery Service","Getterz certified seller will deliver product."),
    THIRD_PARTY_DELIVERY(1,"Third Party Delivery Service","Third-party delivery agency will deliver product."),
    PICK_UP(2,"Self Pick Up Service","Buyer will pick up product from where it's prepared.");

    private final Integer id;
    private final String title;
    private final String description;

}
