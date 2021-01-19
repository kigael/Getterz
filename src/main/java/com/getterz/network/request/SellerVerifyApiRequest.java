package com.getterz.network.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerVerifyApiRequest {

    private Boolean certify;

    private Long sellerId;

    private String message;

}
