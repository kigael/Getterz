package com.getterz.network.request;

import com.getterz.domain.enumclass.DeliverMethod;
import com.getterz.domain.enumclass.PurchaseState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseApiRequest {

    private Long id;

    private Long sellerId;

    private Long buyerId;

    private Long productId;

    private Long quantity;

    private Long totalSatoshi;

    private DeliverMethod deliverMethod;

    private PurchaseState purchaseState;

    private LocalDateTime arrivalDateTime;

    private Boolean feePaid;

    private Boolean wroteReview;

    private Boolean sellerDelete;

    private Boolean transporterDelete;

    private Boolean buyerDelete;

}
