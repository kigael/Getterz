package com.getterz.controller.buyerApi.restController;

import com.getterz.controller.AuthenticationController;
import com.getterz.domain.model.Buyer;
import com.getterz.network.request.BuyerApiRequest;
import com.getterz.network.response.BuyerApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/buyer")
public class BuyerAuthenticationApiController extends AuthenticationController<BuyerApiRequest, BuyerApiResponse, Buyer> {
}
