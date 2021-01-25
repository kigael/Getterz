package com.getterz.service.adminApi;

import com.getterz.crypt.Cryptor;
import com.getterz.domain.enumclass.PurchaseState;
import com.getterz.domain.model.*;
import com.getterz.domain.repository.BuyerRepository;
import com.getterz.domain.repository.ProductRepository;
import com.getterz.domain.repository.PurchaseRepository;
import com.getterz.domain.repository.SellerRepository;
import com.getterz.network.Header;
import com.getterz.network.request.PurchaseApiRequest;
import com.getterz.network.response.ProductApiResponse;
import com.getterz.network.response.PurchaseApiResponse;
import com.getterz.network.session.SessionApi;
import com.getterz.service.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseApiLogicService extends CrudService<PurchaseApiRequest, PurchaseApiResponse, Purchase> {

    @Autowired
    private final PurchaseRepository purchaseRepository;
    private final SellerRepository sellerRepository;
    private final BuyerRepository buyerRepository;
    private final ProductRepository productRepository;

    @Override
    public Header<PurchaseApiResponse> create(Header<PurchaseApiRequest> request) {
        String transactionType = "PURCHASE CREATE";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA", SessionApi.updateSession(request.getSession()));
        else if(request.getSession()==null) return Header.ERROR(transactionType,"NO SESSION", SessionApi.updateSession(request.getSession()));
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
            if(body.getTotalSatoshi()==null){
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

                Optional<Product> product = productRepository.findById(body.getProductId());
                if(product.isEmpty()) return Header.ERROR(transactionType,"PRODUCT NOT FOUND",SessionApi.updateSession(request.getSession()));

                if(body.getQuantity()<=0) return Header.ERROR(transactionType,"INVALID QUANTITY",SessionApi.updateSession(request.getSession()));

                if(body.getTotalSatoshi()<0) Header.ERROR(transactionType,"INVALID TOTAL COST",SessionApi.updateSession(request.getSession()));
                
                return Header.OK(
                        transactionType,
                        response(purchaseRepository.save(Purchase.builder()
                        .seller(seller.get())
                        .buyer(buyer.get())
                        .product(product.get())
                        .quantity(body.getQuantity())
                        .totalSatoshi(body.getTotalSatoshi())
                        .deliverMethod(body.getDeliverMethod())
                        .purchaseState(PurchaseState.PREPARING)
                        .wroteReview(Boolean.FALSE)
                        .sellerDelete(Boolean.FALSE)
                        .buyerDelete(Boolean.FALSE)
                        .build())),
                        SessionApi.updateSession(request.getSession()));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(request.getSession()));
            }
        }
    }

    @Override
    public Header<PurchaseApiResponse> read(Long id, String session) {
        String transactionType = "PURCHASE READ";
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
                        .map(purchase -> Header.OK(transactionType,response(purchase),SessionApi.updateSession(session)))
                        .orElseGet(()->Header.ERROR(transactionType, "PURCHASE NOT FOUND",SessionApi.updateSession(session)));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(session));
            }
        }
    }

    @Override
    public Header<PurchaseApiResponse> update(Header<PurchaseApiRequest> request) {
        String transactionType = "PURCHASE UPDATE";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA", SessionApi.updateSession(request.getSession()));
        else if(request.getSession()==null) return Header.ERROR(transactionType,"NO SESSION", SessionApi.updateSession(request.getSession()));
        else if(!SessionApi.checkSession(request.getSession())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));
        else{
            PurchaseApiRequest body = request.getData();
            StringBuilder description = new StringBuilder();
            Boolean isGreen = Boolean.TRUE;
            if(body.getId()==null){
                description.append("NO ID\n");
                isGreen=Boolean.FALSE;
            }
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
            if(body.getTotalSatoshi()==null){
                description.append("NO TOTAL COST\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getDeliverMethod()==null){
                description.append("NO DELIVER METHOD\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getPurchaseState()==null){
                description.append("NO PURCHASE STATE\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getWroteReview()==null){
                description.append("NO WROTE REVIEW\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getSellerDelete()==null){
                description.append("NO SELLER DELETE\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getTransporterDelete()==null){
                description.append("NO TRANSPORT DELETE\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getBuyerDelete()==null){
                description.append("NO BUYER DELETE\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                Optional<Purchase> purchase = purchaseRepository.findById(body.getId());
                if(purchase.isEmpty()) return Header.ERROR(transactionType,"PURCHASE NOT FOUND",SessionApi.updateSession(request.getSession()));

                Optional<Seller> seller = sellerRepository.findById(body.getSellerId());
                if(seller.isEmpty()) return Header.ERROR(transactionType,"SELLER NOT FOUND",SessionApi.updateSession(request.getSession()));

                Optional<Buyer> buyer = buyerRepository.findById(body.getBuyerId());
                if(buyer.isEmpty()) return Header.ERROR(transactionType,"BUYER NOT FOUND",SessionApi.updateSession(request.getSession()));

                Optional<Product> product = productRepository.findById(body.getProductId());
                if(product.isEmpty()) return Header.ERROR(transactionType,"PRODUCT NOT FOUND",SessionApi.updateSession(request.getSession()));

                if(body.getQuantity()<=0) return Header.ERROR(transactionType,"QUANTITY INCORRECT",SessionApi.updateSession(request.getSession()));

                if(body.getTotalSatoshi()<0) Header.ERROR(transactionType,"TOTAL COST INCORRECT",SessionApi.updateSession(request.getSession()));
                
                return Header.OK(
                        transactionType,
                        response(purchaseRepository.save(purchase.get()
                        .setSeller(seller.get())
                        .setBuyer(buyer.get())
                        .setProduct(product.get())
                        .setQuantity(body.getQuantity())
                        .setTotalSatoshi(body.getTotalSatoshi())
                        .setDeliverMethod(body.getDeliverMethod())
                        .setPurchaseState(body.getPurchaseState())
                        .setArrivalDateTime(Optional.ofNullable(body.getArrivalDateTime()).orElse(null))
                        .setWroteReview(body.getWroteReview())
                        .setSellerDelete(body.getSellerDelete())
                        .setBuyerDelete(body.getBuyerDelete()))),
                        SessionApi.updateSession(request.getSession()));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(request.getSession()));
            }
        }
    }

    @Override
    public Header delete(Long id, String session) {
        String transactionType = "PURCHASE DELETE";
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
                        .map(purchase -> {
                            baseRepository.delete(purchase);
                            return Header.OK(transactionType,SessionApi.updateSession(session));
                        })
                        .orElseGet(()->Header.ERROR(transactionType, "PURCHASE NOT FOUND",SessionApi.updateSession(session)));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(session));
            }
        }
    }

    private PurchaseApiResponse response(Purchase purchase){
        PurchaseApiResponse body = PurchaseApiResponse.builder()
                .id(purchase.getId())
                .quantity(purchase.getQuantity())
                .totalSatoshi(purchase.getTotalSatoshi())
                .deliverMethod(purchase.getDeliverMethod())
                .purchaseState(purchase.getPurchaseState())
                .purchaseDateTime(purchase.getPurchaseDateTime())
                .arrivalDateTime(purchase.getArrivalDateTime())
                .wroteReview(purchase.getWroteReview())
                .sellerDelete(purchase.getSellerDelete())
                .buyerDelete(purchase.getBuyerDelete())
                .build();
        if(purchase.getSeller()!=null) body.setSeller(SellerApiLogicService.Body(purchase.getSeller()));
        if(purchase.getBuyer()!=null) body.setBuyer(BuyerApiLogicService.Body(purchase.getBuyer()));
        if(purchase.getProduct()!=null) body.setProduct(Body(purchase.getProduct()));
        return body;
    }

    public static PurchaseApiResponse Body(Purchase purchase){
        return PurchaseApiResponse.builder()
                .id(purchase.getId())
                .quantity(purchase.getQuantity())
                .totalSatoshi(purchase.getTotalSatoshi())
                .deliverMethod(purchase.getDeliverMethod())
                .purchaseState(purchase.getPurchaseState())
                .purchaseDateTime(purchase.getPurchaseDateTime())
                .arrivalDateTime(purchase.getArrivalDateTime())
                .wroteReview(purchase.getWroteReview())
                .sellerDelete(purchase.getSellerDelete())
                .buyerDelete(purchase.getBuyerDelete())
                .build();
    }

    private ProductApiResponse Body(Product product){
        ProductApiResponse body = ProductApiResponse.builder()
                .id(product.getId())
                .name(Cryptor.DECRYPT(product.getName()))
                .cost(product.getCost())
                .quantity(product.getQuantity())
                .profileImageName(Cryptor.DECRYPT(product.getProfileImageName()))
                .build();
        if(product.getTags()!=null){
            body.setTags(new ArrayList<>());
            for(Tag tag : product.getTags())
                body.getTags().add(TagApiLogicService.Body(tag));
        }
        return body;
    }

}
