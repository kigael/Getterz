package com.getterz.controller.adminApi.restController;

import com.getterz.controller.CrudController;
import com.getterz.domain.model.Product;
import com.getterz.network.request.ProductApiRequest;
import com.getterz.network.response.ProductApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductApiController extends CrudController<ProductApiRequest, ProductApiResponse, Product> {
}