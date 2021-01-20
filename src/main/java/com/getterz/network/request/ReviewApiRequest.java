package com.getterz.network.request;

import com.getterz.domain.enumclass.PurchaseReason;
import com.getterz.domain.enumclass.PurchaseResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewApiRequest {

    private Long id;

    private Long buyerId;

    private Long productId;

    private PurchaseResult purchaseResult;

    private PurchaseReason purchaseReason;

}
