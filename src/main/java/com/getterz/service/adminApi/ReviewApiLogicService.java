package com.getterz.service.adminApi;

import com.getterz.domain.model.Buyer;
import com.getterz.domain.model.Product;
import com.getterz.domain.model.Review;
import com.getterz.domain.repository.BuyerRepository;
import com.getterz.domain.repository.ProductRepository;
import com.getterz.network.Header;
import com.getterz.network.request.ReviewApiRequest;
import com.getterz.network.response.ReviewApiResponse;
import com.getterz.network.session.SessionApi;
import com.getterz.service.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewApiLogicService extends CrudService<ReviewApiRequest, ReviewApiResponse, Review> {

    @Autowired
    private final BuyerRepository buyerRepository;
    private final ProductRepository productRepository;

    @Override
    public Header<ReviewApiResponse> create(Header<ReviewApiRequest> request) {
        String transactionType = "REVIEW CREATE";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA",SessionApi.updateSession(request.getSession()));
        else if(request.getSession()==null) return Header.ERROR(transactionType,"NO SESSION",SessionApi.updateSession(request.getSession()));
        else if(!SessionApi.checkSession(request.getSession())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));
        else{
            ReviewApiRequest body = request.getData();
            StringBuilder description = new StringBuilder();
            Boolean isGreen = Boolean.TRUE;
            if(body.getBuyerId()==null){
                description.append("NO BUYER ID\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getProductId()==null){
                description.append("NO PRODUCT ID\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getPurchaseResult()==null){
                description.append("NO ORDER RESULT\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getPurchaseReason()==null){
                description.append("NO ORDER REASON\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                Optional<Buyer> buyer = buyerRepository.findById(body.getBuyerId());
                if(buyer.isEmpty()) return Header.ERROR(transactionType,"BUYER NOT FOUND",SessionApi.updateSession(request.getSession()));

                Optional<Product> product = productRepository.findById(body.getProductId());
                if(product.isEmpty()) return Header.ERROR(transactionType,"PRODUCT NOT FOUND",SessionApi.updateSession(request.getSession()));

                return Header.OK(
                        transactionType,
                        response(baseRepository.save(Review.builder()
                        .buyer(buyer.get())
                        .product(product.get())
                        .purchaseResult(body.getPurchaseResult())
                        .purchaseReason(body.getPurchaseReason())
                        .build())),
                        SessionApi.updateSession(request.getSession()));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(request.getSession()));
            }
        }
    }

    @Override
    public Header<ReviewApiResponse> read(Long id, String session) {
        String transactionType = "REVIEW READ";
        if(session==null) return Header.ERROR(transactionType,"NO SESSION",null);
        else if(!SessionApi.checkSession(session)) return Header.ERROR(transactionType,"INVALID SESSION",null);
        else {
            StringBuilder description = new StringBuilder();
            Boolean isGreen = Boolean.TRUE;
            if(id==null){
                description.append("NO ID\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                return baseRepository.findById(id)
                        .map(review -> Header.OK(transactionType,response(review),SessionApi.updateSession(session)))
                        .orElseGet(()->Header.ERROR(transactionType,"REVIEW NOT FOUND",SessionApi.updateSession(session)));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(session));
            }
        }
    }

    @Override
    public Header<ReviewApiResponse> update(Header<ReviewApiRequest> request) {
        String transactionType = "REVIEW UPDATE";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA",SessionApi.updateSession(request.getSession()));
        else if(request.getSession()==null) return Header.ERROR(transactionType,"NO SESSION",SessionApi.updateSession(request.getSession()));
        else if(!SessionApi.checkSession(request.getSession())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));
        else{
            ReviewApiRequest body = request.getData();
            StringBuilder description = new StringBuilder();
            Boolean isGreen = Boolean.TRUE;
            if(body.getId()==null){
                description.append("NO ID\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getBuyerId()==null){
                description.append("NO BUYER ID\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getProductId()==null){
                description.append("NO PRODUCT ID\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getPurchaseResult()==null){
                description.append("NO ORDER RESULT\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getPurchaseReason()==null){
                description.append("NO ORDER REASON\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                Optional<Review> review = baseRepository.findById(body.getId());
                if(review.isEmpty()) return Header.ERROR(transactionType,"REVIEW NOT FOUND",SessionApi.updateSession(request.getSession()));

                Optional<Buyer> buyer = buyerRepository.findById(body.getBuyerId());
                if(buyer.isEmpty()) return Header.ERROR(transactionType,"BUYER NOT FOUND",SessionApi.updateSession(request.getSession()));

                Optional<Product> product = productRepository.findById(body.getProductId());
                if(product.isEmpty()) return Header.ERROR(transactionType,"PRODUCT NOT FOUND",SessionApi.updateSession(request.getSession()));

                return Header.OK(
                        transactionType,
                        response(baseRepository.save(review.get()
                        .setBuyer(buyer.get())
                        .setProduct(product.get())
                        .setPurchaseResult(body.getPurchaseResult())
                        .setPurchaseReason(body.getPurchaseReason()))),
                        SessionApi.updateSession(request.getSession()));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(request.getSession()));
            }
        }
    }

    @Override
    public Header delete(Long id, String session) {
        String transactionType = "REVIEW DELETE";
        if(session==null) return Header.ERROR(transactionType,"NO SESSION",null);
        else if(!SessionApi.checkSession(session)) return Header.ERROR(transactionType,"INVALID SESSION",null);
        else {
            StringBuilder description = new StringBuilder();
            Boolean isGreen = Boolean.TRUE;
            if(id==null){
                description.append("NO ID\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                return baseRepository.findById(id)
                        .map(review -> {
                            baseRepository.delete(review);
                            return Header.OK(transactionType,SessionApi.updateSession(session));
                        })
                        .orElseGet(()->Header.ERROR(transactionType,"REVIEW NOT FOUND",SessionApi.updateSession(session)));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(session));
            }
        }
    }

    private ReviewApiResponse response(Review review){
        ReviewApiResponse body = ReviewApiResponse.builder()
                .id(review.getId())
                .reviewDateTime(review.getReviewDateTime())
                .purchaseResult(review.getPurchaseResult())
                .purchaseReason(review.getPurchaseReason())
                .build();
        if(review.getBuyer()!=null) body.setBuyer(BuyerApiLogicService.Body(review.getBuyer()));
        if(review.getProduct()!=null) body.setProduct(ProductApiLogicService.Body(review.getProduct()));
        return body;
    }

    public static ReviewApiResponse Body(Review review){
        return ReviewApiResponse.builder()
                .id(review.getId())
                .reviewDateTime(review.getReviewDateTime())
                .purchaseResult(review.getPurchaseResult())
                .purchaseReason(review.getPurchaseReason())
                .build();
    }

}
