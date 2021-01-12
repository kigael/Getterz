package com.getterz.controller.adminApi.restController;

import com.getterz.controller.CrudController;
import com.getterz.domain.model.Seller;
import com.getterz.network.request.SellerApiRequest;
import com.getterz.network.response.SellerApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller")
public class SellerApiController extends CrudController<SellerApiRequest, SellerApiResponse, Seller> {
}