package com.getterz.controller.adminApi.restController;

import com.getterz.controller.CrudController;
import com.getterz.domain.model.Review;
import com.getterz.network.request.ReviewApiRequest;
import com.getterz.network.response.ReviewApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/review")
public class ReviewApiController extends CrudController<ReviewApiRequest, ReviewApiResponse, Review> {
}