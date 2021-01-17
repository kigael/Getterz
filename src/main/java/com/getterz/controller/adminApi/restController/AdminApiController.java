package com.getterz.controller.adminApi.restController;

import com.getterz.network.Header;
import com.getterz.network.request.AdminApiRequest;
import com.getterz.network.request.BuyerVerifyApiRequest;
import com.getterz.network.response.AdminApiResponse;
import com.getterz.network.response.BuyerApiResponse;
import com.getterz.network.response.BuyerVerifyApiResponse;
import com.getterz.service.adminApi.AdminApiLogicService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminApiController {

    @Autowired
    private final AdminApiLogicService adminApiLogicService;

    @PostMapping("signup")
    public Header<AdminApiResponse> signup(@RequestBody Header<AdminApiRequest> request) {
        return adminApiLogicService.signup(request);
    }

    @PostMapping("login")
    public Header<AdminApiResponse> login(@RequestBody Header<AdminApiRequest> request) {
        return adminApiLogicService.login(request);
    }

    @PostMapping("logout")
    public Header<AdminApiResponse> logout(@RequestBody Header<AdminApiRequest> request) {
        return adminApiLogicService.logout(request);
    }

    @GetMapping("buyer_verify")
    public Header<BuyerVerifyApiResponse> buyerVerifyGetList(
            @RequestParam(value="getterz_session") String session,
            @PageableDefault(sort = { "id" }, direction = Sort.Direction.ASC) Pageable pageable){
        return adminApiLogicService.buyerVerifyGetList(session,pageable);
    }

    @GetMapping("buyer_verify/{id}")
    public Header<BuyerApiResponse> buyerVerifyGetBuyer(
            @RequestParam(value="getterz_session") String session,
            @PathVariable("id") Long id){
        return adminApiLogicService.buyerVerifyGetBuyer(session,id);
    }

    @PostMapping("buyer_verify")
    public Header<BuyerApiResponse> buyerVerify(@RequestBody Header<BuyerVerifyApiRequest> request){
        return adminApiLogicService.buyerVerify(request);
    }

}
