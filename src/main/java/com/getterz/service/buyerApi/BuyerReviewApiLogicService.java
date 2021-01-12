package com.getterz.service.buyerApi;

import com.getterz.crypt.Cryptor;
import com.getterz.domain.enumclass.PurchaseReason;
import com.getterz.domain.enumclass.PurchaseResult;
import com.getterz.domain.enumclass.UserType;
import com.getterz.domain.model.Buyer;
import com.getterz.domain.model.Product;
import com.getterz.domain.model.Purchase;
import com.getterz.domain.model.Review;
import com.getterz.domain.repository.BuyerRepository;
import com.getterz.domain.repository.ProductRepository;
import com.getterz.domain.repository.PurchaseRepository;
import com.getterz.domain.repository.ReviewRepository;
import com.getterz.network.Header;
import com.getterz.network.request.ReviewApiRequest;
import com.getterz.network.response.PurchaseApiResponse;
import com.getterz.network.response.PurchaseHistoryApiResponse;
import com.getterz.network.response.ReviewApiResponse;
import com.getterz.network.session.Session;
import com.getterz.network.session.SessionApi;
import com.getterz.service.adminApi.BuyerApiLogicService;
import com.getterz.service.adminApi.ProductApiLogicService;
import com.getterz.service.adminApi.PurchaseApiLogicService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuyerReviewApiLogicService {

    @Autowired
    private final ReviewRepository reviewRepository;
    private final PurchaseRepository purchaseRepository;
    private final ProductRepository productRepository;
    private final BuyerRepository buyerRepository;

    public Header<PurchaseHistoryApiResponse> reviewableList(String session, Pageable pageable) {
        String transactionType = "REVIEW LIST";
        if(session==null) return Header.ERROR(transactionType,"NO SESSION",null);
        else if(!SessionApi.checkSession(session)) return Header.ERROR(transactionType,"INVALID SESSION",null);
        else {
            Session s = Session.toSession(Cryptor.DECRYPT(session));
            Optional<Buyer> buyer;
            try{
                if(s.getUserType()!= UserType.BUYER) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(session));
                buyer = buyerRepository.findById(s.getId());
                if(buyer.isEmpty()) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(session));
            }catch(NullPointerException e){
                return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(session));
            }
            Page<Purchase> purchases = purchaseRepository.findReviewList(buyer.get().getId(),pageable);
            PurchaseHistoryApiResponse purchaseHistoryApiResponse = new PurchaseHistoryApiResponse();
            List<PurchaseApiResponse> history = purchases.stream()
                    .map(PurchaseApiLogicService::Body)
                    .collect(Collectors.toList());
            purchaseHistoryApiResponse.setHistory(history)
                    .setCurrentPage(purchases.getNumber())
                    .setCurrentElements(purchases.getNumberOfElements())
                    .setTotalPage(purchases.getTotalPages())
                    .setTotalElements(purchases.getTotalElements());
            return Header.OK(transactionType,purchaseHistoryApiResponse,SessionApi.updateSession(session));
        }
    }

    public Header<ReviewApiResponse> reviewWrite(Header<ReviewApiRequest> request) {
        String transactionType = "REVIEW WRITE";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA", SessionApi.updateSession(request.getSession()));
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
                description.append("NO PURCHASE RESULT\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getPurchaseReason()==null){
                description.append("NO PURCHASE REASON\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                Optional<Buyer> buyer = buyerRepository.findById(body.getBuyerId());
                if(buyer.isEmpty()) return Header.ERROR(transactionType,"BUYER NOT FOUND", SessionApi.updateSession(request.getSession()));

                Optional<Product> product = productRepository.findById(body.getProductId());
                if(product.isEmpty()) return Header.ERROR(transactionType,"PRODUCT NOT FOUND", SessionApi.updateSession(request.getSession()));

                if(purchaseRepository.findPurchaseWithoutReview(body.getBuyerId(),body.getProductId()).isEmpty()) return Header.ERROR(transactionType,"PURCHASE NOT FOUND", SessionApi.updateSession(request.getSession()));

                if(!Session.verifyBuyer(request.getSession(),buyer.get())) return Header.ERROR(transactionType,"INVALID SESSION", SessionApi.updateSession(request.getSession()));

                if(body.getPurchaseResult()== PurchaseResult.SUCCESS){
                    if(body.getPurchaseReason()== PurchaseReason.MIA||body.getPurchaseReason()== PurchaseReason.REFUND) return Header.ERROR(transactionType,"INVALID REVIEW", SessionApi.updateSession(request.getSession()));
                }
                if(body.getPurchaseResult()== PurchaseResult.FAIL){
                    if(body.getPurchaseReason()== PurchaseReason.COMPLETE||body.getPurchaseReason()== PurchaseReason.INCOMPLETE) return Header.ERROR(transactionType,"INVALID REVIEW", SessionApi.updateSession(request.getSession()));
                }

                return Header.OK(
                        transactionType,
                        response(reviewRepository.save(Review.builder()
                                .buyer(buyer.get())
                                .product(product.get())
                                .reviewDateTime(LocalDateTime.now())
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

}
