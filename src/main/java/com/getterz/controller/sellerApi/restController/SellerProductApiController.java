package com.getterz.controller.sellerApi.restController;

import com.getterz.domain.enumclass.OrderByType;
import com.getterz.network.Header;
import com.getterz.network.request.ProductApiRequest;
import com.getterz.network.response.ProductApiResponse;
import com.getterz.network.response.SearchApiResponse;
import com.getterz.service.sellerApi.SellerProductLogicService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/seller/product")
@RequiredArgsConstructor
public class SellerProductApiController {

    @Autowired
    private final SellerProductLogicService sellerProductLogicService;

    @GetMapping("")
    public Header<SearchApiResponse> searchProduct(
            @RequestParam(value="productName") String productName,
            @RequestParam(value="orderByType") OrderByType orderByType,
            @RequestParam(value="getterz_session") String session,
            @PageableDefault(sort = { "id" }, direction = Sort.Direction.ASC) Pageable pageable){
        return sellerProductLogicService.searchProduct(productName,orderByType,session,pageable);
    }

    @GetMapping("{id}")
    public Header<ProductApiResponse> getProduct(
            @RequestParam(value="getterz_session") String session,
            @PathVariable("id") Long id){
        return sellerProductLogicService.getProduct(session,id);
    }

    @PutMapping("")
    public Header<ProductApiResponse> editProduct(@RequestBody Header<ProductApiRequest> request){
        return sellerProductLogicService.editProduct(request);
    }

    @DeleteMapping("{id}")
    public Header<ProductApiResponse> deleteProduct(
            @RequestParam(value="getterz_session") String session,
            @PathVariable("id") Long id){
        return sellerProductLogicService.deleteProduct(session,id);
    }

    @PostMapping("")
    public Header<ProductApiResponse> registerProduct(@RequestBody Header<ProductApiRequest> request){
        return sellerProductLogicService.registerProduct(request);
    }

    @PostMapping("profile_image_upload")
    public Header<ProductApiResponse> profileImageUpload(
            @RequestParam("profileImage") MultipartFile profileImage,
            @RequestParam("fileName") String fileName) {
        return sellerProductLogicService.profileImageUpload(profileImage,fileName);
    }

}
