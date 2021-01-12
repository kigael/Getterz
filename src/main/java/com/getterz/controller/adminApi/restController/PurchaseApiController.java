package com.getterz.controller.adminApi.restController;

import com.getterz.controller.CrudController;
import com.getterz.domain.model.Purchase;
import com.getterz.network.request.PurchaseApiRequest;
import com.getterz.network.response.PurchaseApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class PurchaseApiController extends CrudController<PurchaseApiRequest, PurchaseApiResponse, Purchase> {
}
