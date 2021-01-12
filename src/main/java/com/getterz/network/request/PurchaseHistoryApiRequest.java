package com.getterz.network.request;

import com.getterz.domain.enumclass.OrderByType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class PurchaseHistoryApiRequest {

    private Boolean unpaid;

    private Boolean preparing;

    private Boolean waiting;

    private Boolean pickedUp;

    private Boolean delivering;

    private Boolean delivered;

    private OrderByType orderByType;

}
