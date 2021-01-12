package com.getterz.service.buyerApi;

import com.getterz.crypt.Cryptor;
import com.getterz.domain.enumclass.OrderByType;
import com.getterz.domain.enumclass.PurchaseState;
import com.getterz.domain.enumclass.UserType;
import com.getterz.domain.model.*;
import com.getterz.domain.repository.*;
import com.getterz.network.Header;
import com.getterz.network.request.PurchaseApiRequest;
import com.getterz.network.request.PurchaseHistoryApiRequest;
import com.getterz.network.response.PurchaseApiResponse;
import com.getterz.network.response.PurchaseHistoryApiResponse;
import com.getterz.network.session.Session;
import com.getterz.network.session.SessionApi;
import com.getterz.service.adminApi.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuyerPurchaseApiLogicService {

    @Autowired
    private final ProductRepository productRepository;
    private final PurchaseRepository purchaseRepository;
    private final SellerRepository sellerRepository;
    private final BuyerRepository buyerRepository;

    public Header<PurchaseHistoryApiResponse> historyList(Header<PurchaseHistoryApiRequest> request, Pageable pageable) {
        String transactionType = "PURCHASE LIST";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA", SessionApi.updateSession(request.getSession()));
        else if(request.getSession()==null) return Header.ERROR(transactionType,"NO SESSION",SessionApi.updateSession(request.getSession()));
        else if(!SessionApi.checkSession(request.getSession())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));
        else{
            PurchaseHistoryApiRequest body = request.getData();
            StringBuilder description = new StringBuilder();
            Boolean isGreen = Boolean.TRUE;
            if(body.getUnpaid()==null||body.getPreparing()==null||body.getWaiting()==null||body.getPickedUp()==null||body.getDelivering()==null||body.getDelivered()==null){
                description.append("INCOMPLETE CONDITION\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                Page<Purchase> purchases = null;
                Optional<Buyer> buyer;
                try{
                    buyer = buyerRepository.findById(Session.toSession(request.getSession()).getId());
                    if(buyer.isEmpty()) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));
                }catch(NullPointerException e){
                    return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));
                }
                Set<String> condition = new HashSet<>();
                if(body.getUnpaid()) condition.add(PurchaseState.UNPAID.toString());
                if(body.getPreparing()) condition.add(PurchaseState.PREPARING.toString());
                if(body.getWaiting()) condition.add(PurchaseState.WAITING.toString());
                if(body.getPickedUp()) condition.add(PurchaseState.PICKED_UP.toString());
                if(body.getDelivering()) condition.add(PurchaseState.DELIVERING.toString());
                if(body.getDelivered()) condition.add(PurchaseState.DELIVERED.toString());

                if(body.getOrderByType()==null){
                    purchases = purchaseRepository.findHistoryWithCondition(buyer.get().getId(),condition,pageable);
                }
                else{
                    if(body.getOrderByType()==OrderByType.ORDER_BY_DISTANCE_ASC){
                        purchases = purchaseRepository.findHistoryWithConditionOrderByDistanceAsc(buyer.get().getId(),condition,pageable);
                    }
                    else if(body.getOrderByType()==OrderByType.ORDER_BY_DISTANCE_DESC){
                        purchases = purchaseRepository.findHistoryWithConditionOrderByDistanceDesc(buyer.get().getId(),condition,pageable);
                    }
                    else if(body.getOrderByType()==OrderByType.ORDER_BY_COST_ASC){
                        purchases = purchaseRepository.findHistoryWithConditionOrderByCostAsc(buyer.get().getId(),condition,pageable);
                    }
                    else if(body.getOrderByType()==OrderByType.ORDER_BY_COST_DESC){
                        purchases = purchaseRepository.findHistoryWithConditionOrderByCostDesc(buyer.get().getId(),condition,pageable);
                    }
                    else if(body.getOrderByType()==OrderByType.ORDER_BY_REVIEW_ASC){
                        purchases = purchaseRepository.findHistoryWithConditionOrderByReviewAsc(buyer.get().getId(),condition,pageable);
                    }
                    else if(body.getOrderByType()==OrderByType.ORDER_BY_REVIEW_DESC){
                        purchases = purchaseRepository.findHistoryWithConditionOrderByReviewDesc(buyer.get().getId(),condition,pageable);
                    }
                }
                PurchaseHistoryApiResponse purchaseHistoryApiResponse = new PurchaseHistoryApiResponse();
                List<PurchaseApiResponse> history = purchases.stream()
                        .map(PurchaseApiLogicService::Body)
                        .collect(Collectors.toList());
                purchaseHistoryApiResponse.setHistory(history)
                        .setCurrentPage(purchases.getNumber())
                        .setCurrentElements(purchases.getNumberOfElements())
                        .setTotalPage(purchases.getTotalPages())
                        .setTotalElements(purchases.getTotalElements());
                return Header.OK(transactionType,purchaseHistoryApiResponse,SessionApi.updateSession(request.getSession()));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(request.getSession()));
            }
        }
    }

    public Header<PurchaseApiResponse> historyRead(Long purchaseId, String session) {
        String transactionType = "PURCHASE READ";
        if(session==null) return Header.ERROR(transactionType,"NO SESSION",null);
        else if(!SessionApi.checkSession(session)) return Header.ERROR(transactionType,"INVALID SESSION",null);
        else {
            StringBuilder description = new StringBuilder();
            Boolean isGreen = Boolean.TRUE;
            if(purchaseId==null){
                description.append("NO ID\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                Optional<Purchase> purchase = purchaseRepository.findById(purchaseId);
                if(purchase.isEmpty()||purchase.get().getBuyerDelete()) return Header.ERROR(transactionType,"PURCHASE NOT FOUND",SessionApi.updateSession(session));

                if(!Session.verifyBuyer(session,purchase.get().getBuyer())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(session));

                return Header.OK(transactionType,response(purchase.get()),SessionApi.updateSession(session));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(session));
            }
        }
    }

    public Header<PurchaseApiResponse> historyDelete(Long purchaseId, String session) {
        String transactionType = "PURCHASE DELETE";
        if(session==null) return Header.ERROR(transactionType,"NO SESSION",null);
        else if(!SessionApi.checkSession(session)) return Header.ERROR(transactionType,"INVALID SESSION",null);
        else {
            StringBuilder description = new StringBuilder();
            Boolean isGreen = Boolean.TRUE;
            if(purchaseId==null){
                description.append("NO ID\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                Optional<Purchase> purchase = purchaseRepository.findById(purchaseId);
                if(purchase.isEmpty()||purchase.get().getBuyerDelete()) return Header.ERROR(transactionType,"PURCHASE NOT FOUND",SessionApi.updateSession(session));

                if(!Session.verifyBuyer(session,purchase.get().getBuyer())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(session));

                if(purchase.get().getPurchaseState()!=PurchaseState.DELIVERED&&purchase.get().getPurchaseState()!=PurchaseState.PICKED_UP)  return Header.ERROR(transactionType,"DELETE UNAVAILABLE",SessionApi.updateSession(session));

                if(!purchase.get().getWroteReview()) return Header.ERROR(transactionType,"DELETE UNAVAILABLE",SessionApi.updateSession(session));

                purchaseRepository.save(purchase.get().setBuyerDelete(true));

                return Header.OK(transactionType,SessionApi.updateSession(session));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(session));
            }
        }
    }

    public Header<PurchaseHistoryApiResponse> pickUpList(String session, Pageable pageable) {
        String transactionType = "PICKUP LIST";
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
            if(!Session.verifyBuyer(session,buyer.get())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(session));
            Page<Purchase> purchases = purchaseRepository.findPickUpList(buyer.get().getId(),pageable);
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

    //TODO: notify seller
    public Header<PurchaseApiResponse> pickUpCheck(Header<PurchaseApiRequest> request) {
        String transactionType = "PICKUP CHECK";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA", SessionApi.updateSession(request.getSession()));
        else if(request.getSession()==null) return Header.ERROR(transactionType,"NO SESSION",SessionApi.updateSession(request.getSession()));
        else if(!SessionApi.checkSession(request.getSession())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));
        else{
            PurchaseApiRequest body = request.getData();
            StringBuilder description = new StringBuilder();
            Boolean isGreen = Boolean.TRUE;
            if(body.getId()==null){
                description.append("NO ID\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                Optional<Purchase> purchase = purchaseRepository.findById(body.getId());
                if(purchase.isEmpty()) return Header.ERROR(transactionType,"PURCHASE NOT FOUND", SessionApi.updateSession(request.getSession()));

                if(!Session.verifyBuyer(request.getSession(),purchase.get().getBuyer())) return Header.ERROR(transactionType,"INVALID SESSION", SessionApi.updateSession(request.getSession()));

                if(purchase.get().getPurchaseState()!=PurchaseState.WAITING) return Header.ERROR(transactionType,"PICKUP UNAVAILABLE", SessionApi.updateSession(request.getSession()));

                return Header.OK(transactionType,response(purchaseRepository.save(purchase.get().setPurchaseState(PurchaseState.PICKED_UP).setArrivalDateTime(LocalDateTime.now()))),SessionApi.updateSession(request.getSession()));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(request.getSession()));
            }
        }
    }

    public Header<PurchaseHistoryApiResponse> deliverList(String session, Pageable pageable) {
        String transactionType = "DELIVER LIST";
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
            if(!Session.verifyBuyer(session,buyer.get())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(session));
            Page<Purchase> purchases = purchaseRepository.findDeliverList(buyer.get().getId(),pageable);
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

    public Header<PurchaseApiResponse> deliverCheck(Header<PurchaseApiRequest> request) {
        String transactionType = "DELIVER CHECK";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA", SessionApi.updateSession(request.getSession()));
        else if(request.getSession()==null) return Header.ERROR(transactionType,"NO SESSION",SessionApi.updateSession(request.getSession()));
        else if(!SessionApi.checkSession(request.getSession())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));
        else{
            PurchaseApiRequest body = request.getData();
            StringBuilder description = new StringBuilder();
            Boolean isGreen = Boolean.TRUE;
            if(body.getId()==null){
                description.append("NO ID\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                Optional<Purchase> purchase = purchaseRepository.findById(body.getId());
                if(purchase.isEmpty()) return Header.ERROR(transactionType,"PURCHASE NOT FOUND", SessionApi.updateSession(request.getSession()));

                if(!Session.verifyBuyer(request.getSession(),purchase.get().getBuyer())) return Header.ERROR(transactionType,"INVALID SESSION", SessionApi.updateSession(request.getSession()));

                if(purchase.get().getPurchaseState()!=PurchaseState.DELIVERING) return Header.ERROR(transactionType,"DELIVER UNAVAILABLE", SessionApi.updateSession(request.getSession()));

                return Header.OK(transactionType,response(purchaseRepository.save(purchase.get().setPurchaseState(PurchaseState.DELIVERED).setArrivalDateTime(LocalDateTime.now()))),SessionApi.updateSession(request.getSession()));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(request.getSession()));
            }
        }
    }

    //TODO: notify seller
    public Header<PurchaseApiResponse> purchase(Header<PurchaseApiRequest> request) {
        String transactionType = "BUYER PURCHASE";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA", SessionApi.updateSession(request.getSession()));
        else if(request.getSession()==null) return Header.ERROR(transactionType,"NO SESSION",SessionApi.updateSession(request.getSession()));
        else if(!SessionApi.checkSession(request.getSession())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));
        else{
            PurchaseApiRequest body = request.getData();
            StringBuilder description = new StringBuilder();
            Boolean isGreen = Boolean.TRUE;
            if(body.getSellerId()==null){
                description.append("NO SELLER ID\n");
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
            if(body.getQuantity()==null){
                description.append("NO QUANTITY\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getTotalCost()==null){
                description.append("NO TOTAL COST\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getDeliverMethod()==null){
                description.append("NO DELIVER METHOD\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                Optional<Seller> seller = sellerRepository.findById(body.getSellerId());
                if(seller.isEmpty()) return Header.ERROR(transactionType,"SELLER NOT FOUND",SessionApi.updateSession(request.getSession()));

                Optional<Buyer> buyer = buyerRepository.findById(body.getBuyerId());
                if(buyer.isEmpty()) return Header.ERROR(transactionType,"BUYER NOT FOUND",SessionApi.updateSession(request.getSession()));
                if(!Session.verifyBuyer(request.getSession(),buyer.get())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));

                Optional<Product> product = productRepository.findById(body.getProductId());
                if(product.isEmpty()) return Header.ERROR(transactionType,"PRODUCT NOT FOUND",SessionApi.updateSession(request.getSession()));

                if(body.getQuantity()<=0) return Header.ERROR(transactionType,"INVALID QUANTITY",SessionApi.updateSession(request.getSession()));

                if(body.getTotalCost().compareTo(BigDecimal.ZERO)<0) Header.ERROR(transactionType,"INVALID TOTAL COST",SessionApi.updateSession(request.getSession()));

                return Header.OK(
                        transactionType,
                        response(purchaseRepository.save(Purchase.builder()
                                .seller(seller.get())
                                .buyer(buyer.get())
                                .product(product.get())
                                .quantity(body.getQuantity())
                                .totalCost(body.getTotalCost())
                                .deliverMethod(body.getDeliverMethod())
                                .purchaseState(PurchaseState.PREPARING)
                                .feePaid(Boolean.FALSE)
                                .wroteReview(Boolean.FALSE)
                                .sellerDelete(Boolean.FALSE)
                                .transporterDelete(Boolean.FALSE)
                                .buyerDelete(Boolean.FALSE)
                                .build())),
                        SessionApi.updateSession(request.getSession()));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(request.getSession()));
            }
        }
    }

    //TODO: notify seller
    public Header<PurchaseApiResponse> cancel(Header<PurchaseApiRequest> request) {
        String transactionType = "PURCHASE CANCEL";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA", SessionApi.updateSession(request.getSession()));
        else if(request.getSession()==null) return Header.ERROR(transactionType,"NO SESSION",SessionApi.updateSession(request.getSession()));
        else if(!SessionApi.checkSession(request.getSession())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));
        else{
            PurchaseApiRequest body = request.getData();
            StringBuilder description = new StringBuilder();
            Boolean isGreen = Boolean.TRUE;
            if(body.getId()==null){
                description.append("NO ID\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                Optional<Purchase> purchase = purchaseRepository.findById(body.getId());
                if(purchase.isEmpty()||purchase.get().getBuyerDelete()) return Header.ERROR(transactionType,"PURCHASE NOT FOUND",SessionApi.updateSession(request.getSession()));

                if(!Session.verifyBuyer(request.getSession(),purchase.get().getBuyer())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));

                if(purchase.get().getPurchaseState()!=PurchaseState.UNPAID&&purchase.get().getPurchaseState()!=PurchaseState.PREPARING) return Header.ERROR(transactionType,"CANCEL UNAVAILABLE",SessionApi.updateSession(request.getSession()));

                purchaseRepository.save(purchase.get().setBuyerDelete(true));
                return Header.OK(transactionType,SessionApi.updateSession(request.getSession()));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(request.getSession()));
            }
        }
    }

    private PurchaseApiResponse response(Purchase purchase){
        PurchaseApiResponse body = PurchaseApiResponse.builder()
                .id(purchase.getId())
                .quantity(purchase.getQuantity())
                .totalCost(purchase.getTotalCost())
                .deliverMethod(purchase.getDeliverMethod())
                .purchaseState(purchase.getPurchaseState())
                .purchaseDateTime(purchase.getPurchaseDateTime())
                .arrivalDateTime(purchase.getArrivalDateTime())
                .wroteReview(purchase.getWroteReview())
                .sellerDelete(purchase.getSellerDelete())
                .transporterDelete(purchase.getTransporterDelete())
                .buyerDelete(purchase.getBuyerDelete())
                .build();
        if(purchase.getSeller()!=null) body.setSeller(SellerApiLogicService.Body(purchase.getSeller()));
        if(purchase.getBuyer()!=null) body.setBuyer(BuyerApiLogicService.Body(purchase.getBuyer()));
        if(purchase.getProduct()!=null) body.setProduct(ProductApiLogicService.Body(purchase.getProduct()));
        return body;
    }

}
