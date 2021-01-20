package com.getterz.controller.adminApi.restController;

import com.getterz.controller.CrudController;
import com.getterz.domain.model.Tag;
import com.getterz.network.request.TagApiRequest;
import com.getterz.network.response.TagApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tag")
public class TagApiController extends CrudController<TagApiRequest, TagApiResponse, Tag> {
}