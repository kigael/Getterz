package com.getterz.controller.buyerApi.restController;

import com.getterz.network.Header;
import com.getterz.network.request.ReviewApiRequest;
import com.getterz.network.response.PurchaseHistoryApiResponse;
import com.getterz.network.response.ReviewApiResponse;
import com.getterz.service.buyerApi.BuyerReviewApiLogicService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/buyer/review")
@RequiredArgsConstructor
public class BuyerReviewApiController {

    @Autowired
    private final BuyerReviewApiLogicService buyerReviewApiLogicService;

    @GetMapping("")
    public Header<PurchaseHistoryApiResponse> reviewableList(
            @RequestParam(value = "getterz-session")String session,
            @PageableDefault(sort = { "id" }, direction = Sort.Direction.ASC) Pageable pageable){
        return buyerReviewApiLogicService.reviewableList(session,pageable);
    }

    @PostMapping("")
    public Header<ReviewApiResponse> reviewWrite(@RequestBody Header<ReviewApiRequest> request){
        return buyerReviewApiLogicService.reviewWrite(request);
    }

}
