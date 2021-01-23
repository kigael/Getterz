package com.getterz.service.buyerApi;

import com.getterz.crypt.Cryptor;
import com.getterz.domain.enumclass.OrderByType;
import com.getterz.domain.enumclass.UserType;
import com.getterz.domain.model.*;
import com.getterz.domain.repository.BuyerRepository;
import com.getterz.domain.repository.ProductRepository;
import com.getterz.format.converter.DtoConverter;
import com.getterz.network.Header;
import com.getterz.network.response.ProductApiResponse;
import com.getterz.network.response.SearchApiResponse;
import com.getterz.network.session.Session;
import com.getterz.network.session.SessionApi;
import com.getterz.service.adminApi.JobApiLogicService;
import com.getterz.service.adminApi.ReviewApiLogicService;
import com.getterz.service.adminApi.SellerApiLogicService;
import com.getterz.service.adminApi.TagApiLogicService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuyerSearchApiLogicService {

    @Autowired
    private final ProductRepository productRepository;
    private final BuyerRepository buyerRepository;

    public Header<SearchApiResponse> search(
            String productName,
            BigDecimal minimumCost,
            BigDecimal maximumCost,
            Set<String> tagSet,
            OrderByType orderByType,
            String session,
            Pageable pageable) {
        String transactionType = "BUYER PRODUCT SEARCH";
        if(session==null) return Header.ERROR(transactionType,"NO SESSION",null);
        else if(!SessionApi.checkSession(session)) return Header.ERROR(transactionType,"INVALID SESSION",null);
        else {
            Session s = Session.toSession(Cryptor.DECRYPT(session));
            if (s == null || s.getUserType() == null || s.getUserType() != UserType.BUYER)
                return Header.ERROR(transactionType, "INVALID SESSION", null);

            Optional<Buyer> buyer = buyerRepository.findById(s.getId());
            if (buyer.isEmpty()) return Header.ERROR(transactionType, "INVALID SESSION", null);

            if (productName == null || productName.isEmpty() || productName.isBlank()) productName = "";

            if (minimumCost == null) minimumCost = BigDecimal.ZERO;

            if (maximumCost == null) maximumCost = BigDecimal.valueOf(1000000000000000L);

            if (minimumCost.compareTo(BigDecimal.ZERO) < 0 || maximumCost.compareTo(BigDecimal.ZERO) < 0 || minimumCost.compareTo(maximumCost) > 0)
                return Header.ERROR(transactionType, "INVALID COST RANGE", null);

            if (tagSet == null) tagSet = new HashSet<>();
            else tagSet=tagSet.stream().map(String::toLowerCase).collect(Collectors.toSet());

            String gender = buyer.get().getGender().toString();
            Double latitude = buyer.get().getLatitude();
            Double longitude = buyer.get().getLongitude();
            Integer age = Period.between(buyer.get().getDateOfBirth(), LocalDate.now()).getYears();
            List<Long> jobs = buyer.get().getJobs().stream().map(Job::getId).collect(Collectors.toList());
            String tags = tagsToRegexp(tagSet);
            Integer numberOfTags = tagSet.size();

            Page<Product> products;

            if (orderByType == OrderByType.ORDER_BY_DISTANCE_ASC) {
                products = productRepository.buyerSearchProductOrderByDistanceAsc(gender,age,jobs,latitude,longitude,productName,minimumCost,maximumCost,tags,numberOfTags,pageable);
            } else if (orderByType == OrderByType.ORDER_BY_DISTANCE_DESC) {
                products = productRepository.buyerSearchProductOrderByDistanceDesc(gender,age,jobs,latitude,longitude,productName,minimumCost,maximumCost,tags,numberOfTags,pageable);
            } else if (orderByType == OrderByType.ORDER_BY_COST_ASC) {
                products = productRepository.buyerSearchProductOrderByCostAsc(gender,age,jobs,productName,minimumCost,maximumCost,tags,numberOfTags,pageable);
            } else if (orderByType == OrderByType.ORDER_BY_COST_DESC) {
                products = productRepository.buyerSearchProductOrderByCostDesc(gender,age,jobs,productName,minimumCost,maximumCost,tags,numberOfTags,pageable);
            } else if (orderByType == OrderByType.ORDER_BY_REVIEW_ASC) {
                products = productRepository.buyerSearchProductOrderByReviewAsc(gender,age,jobs,productName,minimumCost,maximumCost,tags,numberOfTags,pageable);
            } else if (orderByType == OrderByType.ORDER_BY_REVIEW_DESC) {
                products = productRepository.buyerSearchProductOrderByReviewDesc(gender,age,jobs,productName,minimumCost,maximumCost,tags,numberOfTags,pageable);
            } else {
                products = productRepository.buyerSearchProduct(gender, age, jobs, productName, minimumCost, maximumCost, tags, numberOfTags, pageable);
            }

            SearchApiResponse searchApiResponse = new SearchApiResponse();
            List<ProductApiResponse> productApiResponseList = products.stream()
                    .map(this::Body)
                    .collect(Collectors.toList());
            searchApiResponse.setProducts(productApiResponseList);
            searchApiResponse
                    .setCurrentPage(products.getNumber())
                    .setCurrentElements(products.getNumberOfElements())
                    .setTotalPage(products.getTotalPages())
                    .setTotalElements(products.getTotalElements());

            return Header.OK(transactionType, searchApiResponse, SessionApi.updateSession(session));
        }
    }

    private String tagsToRegexp(Set<String> tagSet) {
        StringBuilder ret = new StringBuilder();
        for(String t : tagSet)
            ret.append(t).append("|");
        return ret.substring(0,ret.length()-1);
    }

    public Header<ProductApiResponse> read(Long productId, String session) {
        String transactionType = "PRODUCT READ";
        if(session==null) return Header.ERROR(transactionType,"NO SESSION",null);
        else if(!SessionApi.checkSession(session)) return Header.ERROR(transactionType,"INVALID SESSION",null);
        else {
            StringBuilder description = new StringBuilder();
            Boolean isGreen = Boolean.TRUE;
            if(productId==null) {
                description.append("NO ID\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                Optional<Buyer> buyer;
                try{
                    Session s = Session.toSession(Cryptor.DECRYPT(session));
                    if(s.getUserType()!= UserType.BUYER) Header.ERROR(transactionType,"INVALID SESSION", SessionApi.updateSession(session));
                    buyer = buyerRepository.findById(s.getId());
                    if(buyer.isEmpty()) return Header.ERROR(transactionType,"INVALID SESSION", SessionApi.updateSession(session));
                }catch(NullPointerException e){
                    return Header.ERROR(transactionType,"INVALID SESSION", SessionApi.updateSession(session));
                }
                Optional<Product> product = productRepository.findById(productId);
                if(product.isEmpty()) return Header.ERROR(transactionType,"PRODUCT NOT FOUND", SessionApi.updateSession(session));
                else{
                    Set<Long> buyerJob1 = new HashSet<>();
                    Set<Long> buyerJob2 = new HashSet<>();
                    Set<Long> allowedJob = new HashSet<>();
                    Set<Long> bannedJob = new HashSet<>();
                    for(Job j : buyer.get().getJobs()) {buyerJob1.add(j.getId());buyerJob2.add(j.getId());}
                    for(Job j : product.get().getAllowedJobs()) allowedJob.add(j.getId());
                    for(Job j : product.get().getBannedJobs()) bannedJob.add(j.getId());
                    buyerJob1.retainAll(allowedJob);
                    buyerJob2.retainAll(bannedJob);
                    if(
                        !product.get().getAllowedGender().contains(buyer.get().getGender().toString())||
                        product.get().getAllowedMinimumAge()>Period.between(buyer.get().getDateOfBirth(),LocalDate.now()).getYears()||
                        Period.between(buyer.get().getDateOfBirth(),LocalDate.now()).getYears()>product.get().getAllowedMaximumAge()||
                        allowedJob.size()>0&&buyerJob1.size()==0&&
                        bannedJob.size()>0&&buyerJob2.size()>0
                    ) return Header.ERROR(transactionType,"INVALID SESSION", SessionApi.updateSession(session));
                    else return Header.OK(transactionType,response(product.get()),SessionApi.updateSession(session));
                }
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(session));
            }
        }
    }

    private ProductApiResponse response(Product product){
        ProductApiResponse body = ProductApiResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .cost(product.getCost())
                .registeredDate(product.getRegisteredDate())
                .lastModifiedDate(product.getLastModifiedDate())
                .quantity(product.getQuantity())
                .allowedGender(DtoConverter.stringToSetOfGender(product.getAllowedGender()))
                .allowedMinimumAge(product.getAllowedMinimumAge())
                .allowedMaximumAge(product.getAllowedMaximumAge())
                .exposeToNoQualify(product.getExposeToNoQualify())
                .build();
        if(product.getSeller()!=null) body.setSeller(SellerApiLogicService.Body(product.getSeller()));
        if(product.getTags()!=null){
            body.setTags(new ArrayList<>());
            for(Tag tag : product.getTags())
                body.getTags().add(TagApiLogicService.Body(tag));
        }
        if(product.getReviews()!=null){
            body.setReviews(new ArrayList<>());
            for(Review review : product.getReviews())
                body.getReviews().add(ReviewApiLogicService.Body(review));
        }
        if(product.getAllowedJobs()!=null){
            body.setAllowedJobs(new ArrayList<>());
            for(Job job : product.getAllowedJobs())
                body.getAllowedJobs().add(JobApiLogicService.Body(job));
        }
        if(product.getBannedJobs()!=null){
            body.setBannedJobs(new ArrayList<>());
            for(Job job : product.getBannedJobs())
                body.getBannedJobs().add(JobApiLogicService.Body(job));
        }
        return body;
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
