package com.getterz.network.request;

import com.getterz.domain.enumclass.OrderByType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchApiRequest {

    private String name;

    private BigDecimal minimumCost;

    private BigDecimal maximumCost;

    private Set<String> tags;

    private OrderByType orderByType;

}
