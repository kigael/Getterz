package com.getterz.domain.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PurchaseState {

    UNCHECKED(0,"Waiting Confirmation","Buyer is currently waiting for seller's confirmation."),
    UNPAID(1,"Waiting Payment","Seller is currently waiting for buyer's payment."),
    PREPARING(2,"Preparing Product","Seller is currently preparing product."),
    WAITING(3,"Waiting to be Picked Up","Product is waiting to be picked up by buyer."),
    PICKED_UP(4,"Product is Picked Up","Product is picked up by buyer."),
    DELIVERING(5,"Delivering Product","Product is currently being delivered."),
    DELIVERED(6,"Product is Delivered","Product is delivered to buyer.");

    private final Integer id;
    private final String title;
    private final String description;

}
