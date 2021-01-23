package com.getterz.controller.buyerApi.restController;

import com.getterz.domain.enumclass.OrderByType;
import com.getterz.network.Header;
import com.getterz.network.response.ProductApiResponse;
import com.getterz.network.response.SearchApiResponse;
import com.getterz.service.buyerApi.BuyerSearchApiLogicService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Set;

@RestController
@RequestMapping("/buyer/search")
@RequiredArgsConstructor
public class BuyerSearchApiController {

    @Autowired
    private final BuyerSearchApiLogicService buyerSearchApiLogicService;

    @GetMapping("")
    public Header<SearchApiResponse> search(
            @RequestParam(value="productName") String productName,
            @RequestParam(value="minimumCost", required = false) BigDecimal minimumCost,
            @RequestParam(value="maximumCost", required = false) BigDecimal maximumCost,
            @RequestParam(value="tags", required = false) Set<String> tags,
            @RequestParam(value="orderByType", required = false) OrderByType orderByType,
            @RequestParam(value="getterz_session") String session,
            @PageableDefault(sort = { "id" }, direction = Sort.Direction.ASC) Pageable pageable){
        return buyerSearchApiLogicService.search(productName,minimumCost,maximumCost,tags,orderByType,session,pageable);
    }

    @GetMapping("{id}")
    public Header<ProductApiResponse> read(
            @RequestParam(value = "product_id")Long productId,
            @RequestParam(value = "getterz-session")String session){
        return buyerSearchApiLogicService.read(productId,session);
    }

}
