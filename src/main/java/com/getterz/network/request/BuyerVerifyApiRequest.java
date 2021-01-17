package com.getterz.network.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuyerVerifyApiRequest {

    private Boolean certify;

    private Long buyerId;

    private String message;

}
