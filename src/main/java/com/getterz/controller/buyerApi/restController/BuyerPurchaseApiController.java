package com.getterz.controller.buyerApi.restController;

import com.getterz.network.Header;
import com.getterz.network.request.PurchaseApiRequest;
import com.getterz.network.request.PurchaseHistoryApiRequest;
import com.getterz.network.response.PurchaseApiResponse;
import com.getterz.network.response.PurchaseHistoryApiResponse;
import com.getterz.service.buyerApi.BuyerPurchaseApiLogicService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/buyer/purchase")
@RequiredArgsConstructor
public class BuyerPurchaseApiController {

    @Autowired
    private final BuyerPurchaseApiLogicService buyerPurchaseApiLogicService;

    @PostMapping("history")
    public Header<PurchaseHistoryApiResponse> historyList(
            @RequestBody Header<PurchaseHistoryApiRequest> request,
            @PageableDefault(sort = { "id" }, direction = Sort.Direction.ASC) Pageable pageable){
        return buyerPurchaseApiLogicService.historyList(request,pageable);
    }

    @GetMapping("history")
    public Header<PurchaseApiResponse> historyRead(
            @RequestParam(value = "purchase_id")Long purchaseId,
            @RequestParam(value = "getterz-session")String session){
        return buyerPurchaseApiLogicService.historyRead(purchaseId,session);
    }

    @DeleteMapping("history")
    public Header<PurchaseApiResponse> historyDelete(
            @RequestParam(value = "purchase_id")Long purchaseId,
            @RequestParam(value = "getterz-session")String session){
        return buyerPurchaseApiLogicService.historyDelete(purchaseId,session);
    }

    @GetMapping("pickup")
    public Header<PurchaseHistoryApiResponse> pickUpList(
            @RequestParam(value = "getterz-session")String session,
            @PageableDefault(sort = { "id" }, direction = Sort.Direction.ASC) Pageable pageable){
        return buyerPurchaseApiLogicService.pickUpList(session,pageable);
    }

    @PutMapping("pickup")
    public Header<PurchaseApiResponse> pickUpCheck(@RequestBody Header<PurchaseApiRequest> request){
        return buyerPurchaseApiLogicService.pickUpCheck(request);
    }

    @GetMapping("delivered")
    public Header<PurchaseHistoryApiResponse> deliverList(
            @RequestParam(value = "getterz-session")String session,
            @PageableDefault(sort = { "id" }, direction = Sort.Direction.ASC) Pageable pageable){
        return buyerPurchaseApiLogicService.deliverList(session,pageable);
    }

    @PutMapping("delivered")
    public Header<PurchaseApiResponse> deliverCheck(@RequestBody Header<PurchaseApiRequest> request){
        return buyerPurchaseApiLogicService.deliverCheck(request);
    }

    @PostMapping("")
    public Header<PurchaseApiResponse> purchase(@RequestBody Header<PurchaseApiRequest> request){
        return buyerPurchaseApiLogicService.purchase(request);
    }

    @PostMapping("cancel")
    public Header<PurchaseApiResponse> cancel(@RequestBody Header<PurchaseApiRequest> request){
        return buyerPurchaseApiLogicService.cancel(request);
    }

}