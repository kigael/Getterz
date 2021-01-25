package com.getterz.network.response;

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
public class PurchaseApiResponse {

    private Long id;

    private SellerApiResponse seller;

    private BuyerApiResponse buyer;

    private ProductApiResponse product;

    private Long quantity;

    private Long totalSatoshi;

    private DeliverMethod deliverMethod;

    private PurchaseState purchaseState;

    private LocalDateTime purchaseDateTime;

    private LocalDateTime arrivalDateTime;

    private Boolean feePaid;

    private Boolean wroteReview;

    private Boolean sellerDelete;

    private Boolean transporterDelete;

    private Boolean buyerDelete;

}
