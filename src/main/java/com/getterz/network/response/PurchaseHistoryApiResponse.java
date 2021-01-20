package com.getterz.network.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class PurchaseHistoryApiResponse {

    private List<PurchaseApiResponse> history;

    private Integer currentPage;

    private Integer currentElements;

    private Integer totalPage;

    private Long totalElements;

}
