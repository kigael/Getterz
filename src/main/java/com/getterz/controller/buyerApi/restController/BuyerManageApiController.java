package com.getterz.controller.buyerApi.restController;

import com.getterz.network.Header;
import com.getterz.network.request.BuyerApiRequest;
import com.getterz.network.response.BuyerApiResponse;
import com.getterz.service.buyerApi.BuyerManageApiLogicService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/buyer/manage")
@RequiredArgsConstructor
public class BuyerManageApiController {

    @Autowired
    private final BuyerManageApiLogicService buyerManageApiLogicService;

    @GetMapping("")
    public Header<BuyerApiResponse> read(@RequestParam(value = "getterz-session") String variable){
        return buyerManageApiLogicService.read(variable);
    }

    @PutMapping("")
    public Header<BuyerApiResponse> update(@RequestBody Header<BuyerApiRequest> request){
        return buyerManageApiLogicService.update(request);
    }

}
