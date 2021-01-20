package com.getterz.controller.adminApi.restController;

import com.getterz.controller.CrudController;
import com.getterz.domain.model.Buyer;
import com.getterz.network.request.BuyerApiRequest;
import com.getterz.network.response.BuyerApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/buyer")
public class BuyerApiController extends CrudController<BuyerApiRequest, BuyerApiResponse, Buyer> {
}