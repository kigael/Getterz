package com.getterz.network.response;

import com.getterz.domain.enumclass.PurchaseReason;
import com.getterz.domain.enumclass.PurchaseResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewApiResponse {

    private Long id;

    private BuyerApiResponse buyer;

    private ProductApiResponse product;

    private LocalDateTime reviewDateTime;

    private PurchaseResult purchaseResult;

    private PurchaseReason purchaseReason;

}
