package com.getterz.domain.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PurchaseReason {

    COMPLETE(0,"Product is complete","Complete product was picked up or delivered."),
    INCOMPLETE(1,"Product is incomplete","Incomplete product was picked up or delivered."),
    MIA(2,"Product gone missing","Product was lost during processing, delivering step."),
    REFUND(3,"Buyer refunded product","Buyer requested refund due to damaged product, unsatisfying quality, delayed delivery.");

    private final Integer id;
    private final String title;
    private final String description;

}
