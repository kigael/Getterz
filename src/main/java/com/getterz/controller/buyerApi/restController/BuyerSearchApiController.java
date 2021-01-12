package com.getterz.controller.buyerApi.restController;

import com.getterz.network.Header;
import com.getterz.network.request.SearchApiRequest;
import com.getterz.network.response.ProductApiResponse;
import com.getterz.network.response.SearchApiResponse;
import com.getterz.service.buyerApi.BuyerSearchApiLogicService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/buyer/search")
@RequiredArgsConstructor
public class BuyerSearchApiController {

    @Autowired
    private final BuyerSearchApiLogicService buyerSearchApiLogicService;

    @PostMapping("")
    public Header<SearchApiResponse> search(
            @RequestBody Header<SearchApiRequest> request,
            @PageableDefault(sort = { "id" }, direction = Sort.Direction.ASC) Pageable pageable){
        return buyerSearchApiLogicService.search(request,pageable);
    }

    @GetMapping("")
    public Header<ProductApiResponse> read(
            @RequestParam(value = "product_id")Long productId,
            @RequestParam(value = "getterz-session")String session){
        return buyerSearchApiLogicService.read(productId,session);
    }

}
