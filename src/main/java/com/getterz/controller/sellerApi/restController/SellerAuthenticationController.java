package com.getterz.controller.sellerApi.restController;

import com.getterz.controller.AuthenticationController;
import com.getterz.domain.model.Seller;
import com.getterz.network.request.SellerApiRequest;
import com.getterz.network.response.SellerApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller")
public class SellerAuthenticationController extends AuthenticationController<SellerApiRequest, SellerApiResponse, Seller> {
}
