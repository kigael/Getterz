package com.getterz.service.buyerApi;

import com.getterz.crypt.Cryptor;
import com.getterz.domain.enumclass.OrderByType;
import com.getterz.domain.enumclass.UserType;
import com.getterz.domain.model.*;
import com.getterz.domain.repository.BuyerRepository;
import com.getterz.domain.repository.ProductRepository;
import com.getterz.format.converter.DtoConverter;
import com.getterz.network.Header;
import com.getterz.network.request.SearchApiRequest;
import com.getterz.network.response.ProductApiResponse;
import com.getterz.network.response.SearchApiResponse;
import com.getterz.network.session.Session;
import com.getterz.network.session.SessionApi;
import com.getterz.service.adminApi.*;
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

    public Header<SearchApiResponse> search(Header<SearchApiRequest> request, Pageable pageable) {
        String transactionType = "PRODUCT SEARCH";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA", SessionApi.updateSession(request.getSession()));
        else if(request.getSession()==null) return Header.ERROR(transactionType,"NO SESSION",SessionApi.updateSession(request.getSession()));
        else if(!SessionApi.checkSession(request.getSession())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));
        else {
            SearchApiRequest body = request.getData();
            StringBuilder description = new StringBuilder();
            Boolean isGreen = Boolean.TRUE;
            if(body.getName()==null) {
                description.append("NO NAME\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                Optional<Buyer> buyer;
                try{
                    buyer = buyerRepository.findById(Session.toSession(request.getSession()).getId());
                }catch(NullPointerException e){
                    return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));
                }
                if(buyer.isEmpty()) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));
                Page<Product> products = null;
                if(body.getOrderByType()==null){
                    if(body.getMinimumCost()==null&&body.getMaximumCost()==null&&body.getTags()==null){
                        products = findWithName(buyer.get(),body.getName(),pageable);
                    }
                    else if(body.getMinimumCost()!=null&&body.getMaximumCost()==null&&body.getTags()==null){
                        products = findWithNameAndMinimumCost(buyer.get(),body.getName(),body.getMinimumCost(),pageable);
                    }
                    else if(body.getMinimumCost()==null&&body.getMaximumCost()!=null&&body.getTags()==null){
                        products = findWithNameAndMaximumCost(buyer.get(),body.getName(),body.getMaximumCost(),pageable);
                    }
                    else if(body.getMinimumCost()!=null&&body.getMaximumCost()!=null&&body.getTags()==null){
                        products = findWithNameAndMinimumCostAndMaximumCost(buyer.get(),body.getName(),body.getMinimumCost(),body.getMaximumCost(),pageable);
                    }
                    else if(body.getMinimumCost()==null&&body.getMaximumCost()==null&&body.getTags()!=null){
                        products = findWithNameAndTags(buyer.get(),body.getName(),body.getTags(),pageable);
                    }
                    else if(body.getMinimumCost()!=null&&body.getMaximumCost()==null&&body.getTags()!=null){
                        products = findWithNameAndMinimumCostAndTags(buyer.get(),body.getName(),body.getMinimumCost(),body.getTags(),pageable);
                    }
                    else if(body.getMinimumCost()==null&&body.getMaximumCost()!=null&&body.getTags()!=null){
                        products = findWithNameAndMaximumCostAndTags(buyer.get(),body.getName(),body.getMaximumCost(),body.getTags(),pageable);
                    }
                    else if(body.getMinimumCost()!=null&&body.getMaximumCost()!=null&&body.getTags()!=null){
                        products = findWithNameAndMinimumCostAndMaximumCostAndTags(buyer.get(),body.getName(),body.getMinimumCost(),body.getMaximumCost(),body.getTags(),pageable);
                    }
                }
                else{
                    if(body.getMinimumCost()==null&&body.getMaximumCost()==null&&body.getTags()==null){
                        if(body.getOrderByType()== OrderByType.ORDER_BY_DISTANCE_ASC){
                            products = findWithNameOrderByDistanceAsc(buyer.get(),body.getName(),pageable);
                        }
                        else if(body.getOrderByType()== OrderByType.ORDER_BY_DISTANCE_DESC){
                            products = findWithNameOrderByDistanceDesc(buyer.get(),body.getName(),pageable);
                        }
                        else if(body.getOrderByType()== OrderByType.ORDER_BY_COST_ASC){
                            products = findWithNameOrderByCostAsc(buyer.get(),body.getName(),pageable);
                        }
                        else if(body.getOrderByType()== OrderByType.ORDER_BY_COST_DESC){
                            products = findWithNameOrderByCostDesc(buyer.get(),body.getName(),pageable);
                        }
                        else if(body.getOrderByType()== OrderByType.ORDER_BY_REVIEW_ASC){
                            products = findWithNameOrderByReviewAsc(buyer.get(),body.getName(),pageable);
                        }
                        else if(body.getOrderByType()== OrderByType.ORDER_BY_REVIEW_DESC){
                            products = findWithNameOrderByReviewDesc(buyer.get(),body.getName(),pageable);
                        }
                    }
                    else if(body.getMinimumCost()!=null&&body.getMaximumCost()==null&&body.getTags()==null){
                        if(body.getOrderByType()==OrderByType.ORDER_BY_DISTANCE_ASC){
                            products = findWithNameAndMinimumCostOrderByDistanceAsc(buyer.get(),body.getName(),body.getMinimumCost(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_DISTANCE_DESC){
                            products = findWithNameAndMinimumCostOrderByDistanceDesc(buyer.get(),body.getName(),body.getMinimumCost(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_COST_ASC){
                            products = findWithNameAndMinimumCostOrderByCostAsc(buyer.get(),body.getName(),body.getMinimumCost(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_COST_DESC){
                            products = findWithNameAndMinimumCostOrderByCostDesc(buyer.get(),body.getName(),body.getMinimumCost(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_REVIEW_ASC){
                            products = findWithNameAndMinimumCostOrderByReviewAsc(buyer.get(),body.getName(),body.getMinimumCost(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_REVIEW_DESC){
                            products = findWithNameAndMinimumCostOrderByReviewDesc(buyer.get(),body.getName(),body.getMinimumCost(),pageable);
                        }
                    }
                    else if(body.getMinimumCost()==null&&body.getMaximumCost()!=null&&body.getTags()==null){
                        if(body.getOrderByType()==OrderByType.ORDER_BY_DISTANCE_ASC){
                            products = findWithNameAndMaximumCostOrderByDistanceAsc(buyer.get(),body.getName(),body.getMaximumCost(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_DISTANCE_DESC){
                            products = findWithNameAndMaximumCostOrderByDistanceDesc(buyer.get(),body.getName(),body.getMaximumCost(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_COST_ASC){
                            products = findWithNameAndMaximumCostOrderByCostAsc(buyer.get(),body.getName(),body.getMaximumCost(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_COST_DESC){
                            products = findWithNameAndMaximumCostOrderByCostDesc(buyer.get(),body.getName(),body.getMaximumCost(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_REVIEW_ASC){
                            products = findWithNameAndMaximumCostOrderByReviewAsc(buyer.get(),body.getName(),body.getMaximumCost(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_REVIEW_DESC){
                            products = findWithNameAndMaximumCostOrderByReviewDesc(buyer.get(),body.getName(),body.getMaximumCost(),pageable);
                        }
                    }
                    else if(body.getMinimumCost()!=null&&body.getMaximumCost()!=null&&body.getTags()==null){
                        if(body.getOrderByType()==OrderByType.ORDER_BY_DISTANCE_ASC){
                            products = findWithNameAndMinimumCostAndMaximumCostOrderByDistanceAsc(buyer.get(),body.getName(),body.getMinimumCost(),body.getMaximumCost(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_DISTANCE_DESC){
                            products = findWithNameAndMinimumCostAndMaximumCostOrderByDistanceDesc(buyer.get(),body.getName(),body.getMinimumCost(),body.getMaximumCost(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_COST_ASC){
                            products = findWithNameAndMinimumCostAndMaximumCostOrderByCostAsc(buyer.get(),body.getName(),body.getMinimumCost(),body.getMaximumCost(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_COST_DESC){
                            products = findWithNameAndMinimumCostAndMaximumCostOrderByCostDesc(buyer.get(),body.getName(),body.getMinimumCost(),body.getMaximumCost(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_REVIEW_ASC){
                            products = findWithNameAndMinimumCostAndMaximumCostOrderByReviewAsc(buyer.get(),body.getName(),body.getMinimumCost(),body.getMaximumCost(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_REVIEW_DESC){
                            products = findWithNameAndMinimumCostAndMaximumCostOrderByReviewDesc(buyer.get(),body.getName(),body.getMinimumCost(),body.getMaximumCost(),pageable);
                        }
                    }
                    else if(body.getMinimumCost()==null&&body.getMaximumCost()==null&&body.getTags()!=null){
                        if(body.getOrderByType()==OrderByType.ORDER_BY_DISTANCE_ASC){
                            products = findWithNameAndTagsOrderByDistanceAsc(buyer.get(),body.getName(),body.getTags(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_DISTANCE_DESC){
                            products = findWithNameAndTagsOrderByDistanceDesc(buyer.get(),body.getName(),body.getTags(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_COST_ASC){
                            products = findWithNameAndTagsOrderByCostAsc(buyer.get(),body.getName(),body.getTags(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_COST_DESC){
                            products = findWithNameAndTagsOrderByCostDesc(buyer.get(),body.getName(),body.getTags(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_REVIEW_ASC){
                            products = findWithNameAndTagsOrderByReviewAsc(buyer.get(),body.getName(),body.getTags(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_REVIEW_DESC){
                            products = findWithNameAndTagsOrderByReviewDesc(buyer.get(),body.getName(),body.getTags(),pageable);
                        }
                    }
                    else if(body.getMinimumCost()!=null&&body.getMaximumCost()==null&&body.getTags()!=null){
                        if(body.getOrderByType()==OrderByType.ORDER_BY_DISTANCE_ASC){
                            products = findWithNameAndMinimumCostAndTagsOrderByDistanceAsc(buyer.get(),body.getName(),body.getMinimumCost(),body.getTags(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_DISTANCE_DESC){
                            products = findWithNameAndMinimumCostAndTagsOrderByDistanceDesc(buyer.get(),body.getName(),body.getMinimumCost(),body.getTags(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_COST_ASC){
                            products = findWithNameAndMinimumCostAndTagsOrderByCostAsc(buyer.get(),body.getName(),body.getMinimumCost(),body.getTags(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_COST_DESC){
                            products = findWithNameAndMinimumCostAndTagsOrderByCostDesc(buyer.get(),body.getName(),body.getMinimumCost(),body.getTags(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_REVIEW_ASC){
                            products = findWithNameAndMinimumCostAndTagsOrderByReviewAsc(buyer.get(),body.getName(),body.getMinimumCost(),body.getTags(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_REVIEW_DESC){
                            products = findWithNameAndMinimumCostAndTagsOrderByReviewDesc(buyer.get(),body.getName(),body.getMinimumCost(),body.getTags(),pageable);
                        }
                    }
                    else if(body.getMinimumCost()==null&&body.getMaximumCost()!=null&&body.getTags()!=null){
                        if(body.getOrderByType()==OrderByType.ORDER_BY_DISTANCE_ASC){
                            products = findWithNameAndMaximumCostAndTagsOrderByDistanceAsc(buyer.get(),body.getName(),body.getMaximumCost(),body.getTags(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_DISTANCE_DESC){
                            products = findWithNameAndMaximumCostAndTagsOrderByDistanceDesc(buyer.get(),body.getName(),body.getMaximumCost(),body.getTags(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_COST_ASC){
                            products = findWithNameAndMaximumCostAndTagsOrderByCostAsc(buyer.get(),body.getName(),body.getMaximumCost(),body.getTags(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_COST_DESC){
                            products = findWithNameAndMaximumCostAndTagsOrderByCostDesc(buyer.get(),body.getName(),body.getMaximumCost(),body.getTags(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_REVIEW_ASC){
                            products = findWithNameAndMaximumCostAndTagsOrderByReviewAsc(buyer.get(),body.getName(),body.getMaximumCost(),body.getTags(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_REVIEW_DESC){
                            products = findWithNameAndMaximumCostAndTagsOrderByReviewDesc(buyer.get(),body.getName(),body.getMaximumCost(),body.getTags(),pageable);
                        }
                    }
                    else if(body.getMinimumCost()!=null&&body.getMaximumCost()!=null&&body.getTags()!=null){
                        if(body.getOrderByType()==OrderByType.ORDER_BY_DISTANCE_ASC){
                            products = findWithNameAndMinimumCostAndMaximumCostAndTagsOrderByDistanceAsc(buyer.get(),body.getName(),body.getMinimumCost(),body.getMaximumCost(),body.getTags(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_DISTANCE_DESC){
                            products = findWithNameAndMinimumCostAndMaximumCostAndTagsOrderByDistanceDesc(buyer.get(),body.getName(),body.getMinimumCost(),body.getMaximumCost(),body.getTags(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_COST_ASC){
                            products = findWithNameAndMinimumCostAndMaximumCostAndTagsOrderByCostAsc(buyer.get(),body.getName(),body.getMinimumCost(),body.getMaximumCost(),body.getTags(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_COST_DESC){
                            products = findWithNameAndMinimumCostAndMaximumCostAndTagsOrderByCostDesc(buyer.get(),body.getName(),body.getMinimumCost(),body.getMaximumCost(),body.getTags(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_REVIEW_ASC){
                            products = findWithNameAndMinimumCostAndMaximumCostAndTagsOrderByReviewAsc(buyer.get(),body.getName(),body.getMinimumCost(),body.getMaximumCost(),body.getTags(),pageable);
                        }
                        else if(body.getOrderByType()==OrderByType.ORDER_BY_REVIEW_DESC){
                            products = findWithNameAndMinimumCostAndMaximumCostAndTagsOrderByReviewDesc(buyer.get(),body.getName(),body.getMinimumCost(),body.getMaximumCost(),body.getTags(),pageable);
                        }
                    }
                }
                SearchApiResponse searchApiResponse = new SearchApiResponse();
                List<ProductApiResponse> productApiResponseList = products.stream()
                        .map(ProductApiLogicService::Body)
                        .collect(Collectors.toList());
                searchApiResponse.setProducts(productApiResponseList);
                searchApiResponse
                        .setCurrentPage(products.getNumber())
                        .setCurrentElements(products.getNumberOfElements())
                        .setTotalPage(products.getTotalPages())
                        .setTotalElements(products.getTotalElements());
                return Header.OK(transactionType,searchApiResponse,SessionApi.updateSession(request.getSession()));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(request.getSession()));
            }
        }
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
                    for(Job j : buyer.get().getJob()) {buyerJob1.add(j.getId());buyerJob2.add(j.getId());}
                    for(Job j : product.get().getAllowedJobs()) allowedJob.add(j.getId());
                    for(Job j : product.get().getBannedJobs()) bannedJob.add(j.getId());
                    buyerJob1.retainAll(allowedJob);
                    buyerJob2.retainAll(bannedJob);
                    if(
                        !product.get().getAllowedGender().contains(buyer.get().getGender().toString())||
                        product.get().getAllowedMinimumAge()>Period.between(buyer.get().getDateOfBirth(),LocalDate.now()).getYears()||
                        Period.between(buyer.get().getDateOfBirth(),LocalDate.now()).getYears()>product.get().getAllowedMaximumAge()||
                        product.get().getAllowedMinimumAnnualIncome().compareTo(buyer.get().getAnnualIncome())>0||
                        buyer.get().getAnnualIncome().compareTo(product.get().getAllowedMaximumAnnualIncome())>0||
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
                .allowedMinimumAnnualIncome(product.getAllowedMinimumAnnualIncome())
                .allowedMaximumAnnualIncome(product.getAllowedMaximumAnnualIncome())
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

    private Page<Product> findWithName(Buyer buyer, String productName, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithName(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                pageable);
    }

    private Page<Product> findWithNameAndMinimumCost(Buyer buyer, String productName, BigDecimal minimumCost, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMinimumCost(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                minimumCost,
                pageable);
    }

    private Page<Product> findWithNameAndMaximumCost(Buyer buyer, String productName, BigDecimal maximumCost, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMaximumCost(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                maximumCost,
                pageable);
    }

    private Page<Product> findWithNameAndMinimumCostAndMaximumCost(Buyer buyer, String productName, BigDecimal minimumCost, BigDecimal maximumCost, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMinimumCostAndMaximumCost(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                minimumCost,
                maximumCost,
                pageable);
    }

    private Page<Product> findWithNameAndTags(Buyer buyer, String productName, Set<String> tags, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndTags(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                tags,
                tags.size(),
                pageable);
    }

    private Page<Product> findWithNameAndMinimumCostAndTags(Buyer buyer, String productName, BigDecimal minimumCost, Set<String> tags, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMinimumCostAndTags(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                minimumCost,
                tags,
                tags.size(),
                pageable);
    }

    private Page<Product> findWithNameAndMaximumCostAndTags(Buyer buyer, String productName, BigDecimal maximumCost, Set<String> tags, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMaximumCostAndTags(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                maximumCost,
                tags,
                tags.size(),
                pageable);
    }

    private Page<Product> findWithNameAndMinimumCostAndMaximumCostAndTags(Buyer buyer, String productName, BigDecimal minimumCost, BigDecimal maximumCost, Set<String> tags, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMinimumCostAndMaximumCostAndTags(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                minimumCost,
                maximumCost,
                tags,
                tags.size(),
                pageable);
    }

    private Page<Product> findWithNameOrderByDistanceAsc(Buyer buyer, String productName, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameOrderByDistanceAsc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                buyer.getLatitude(),
                buyer.getLongitude(),
                productName,
                pageable);
    }

    private Page<Product> findWithNameOrderByDistanceDesc(Buyer buyer, String productName, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameOrderByDistanceDesc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                buyer.getLatitude(),
                buyer.getLongitude(),
                productName,
                pageable);
    }

    private Page<Product> findWithNameAndMinimumCostOrderByDistanceAsc(Buyer buyer, String productName, BigDecimal minimumCost, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMinimumCostOrderByDistanceAsc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                buyer.getLatitude(),
                buyer.getLongitude(),
                productName,
                minimumCost,
                pageable);
    }

    private Page<Product> findWithNameAndMinimumCostOrderByDistanceDesc(Buyer buyer, String productName, BigDecimal minimumCost, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMinimumCostOrderByDistanceDesc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                buyer.getLatitude(),
                buyer.getLongitude(),
                productName,
                minimumCost,
                pageable);
    }

    private Page<Product> findWithNameAndMaximumCostOrderByDistanceAsc(Buyer buyer, String productName, BigDecimal maximumCost, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMaximumCostOrderByDistanceAsc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                buyer.getLatitude(),
                buyer.getLongitude(),
                productName,
                maximumCost,
                pageable);
    }

    private Page<Product> findWithNameAndMaximumCostOrderByDistanceDesc(Buyer buyer, String productName, BigDecimal maximumCost, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMaximumCostOrderByDistanceDesc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                buyer.getLatitude(),
                buyer.getLongitude(),
                productName,
                maximumCost,
                pageable);
    }

    private Page<Product> findWithNameAndMinimumCostAndMaximumCostOrderByDistanceAsc(Buyer buyer, String productName, BigDecimal minimumCost, BigDecimal maximumCost, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMinimumCostAndMaximumCostOrderByDistanceAsc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                buyer.getLatitude(),
                buyer.getLongitude(),
                productName,
                minimumCost,
                maximumCost,
                pageable);
    }

    private Page<Product> findWithNameAndMinimumCostAndMaximumCostOrderByDistanceDesc(Buyer buyer, String productName, BigDecimal minimumCost, BigDecimal maximumCost, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMinimumCostAndMaximumCostOrderByDistanceDesc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                buyer.getLatitude(),
                buyer.getLongitude(),
                productName,
                minimumCost,
                maximumCost,
                pageable);
    }

    private Page<Product> findWithNameAndTagsOrderByDistanceAsc(Buyer buyer, String productName, Set<String> tags, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndTagsOrderByDistanceAsc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                buyer.getLatitude(),
                buyer.getLongitude(),
                productName,
                tags,
                tags.size(),
                pageable);
    }

    private Page<Product> findWithNameAndTagsOrderByDistanceDesc(Buyer buyer, String productName, Set<String> tags, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndTagsOrderByDistanceDesc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                buyer.getLatitude(),
                buyer.getLongitude(),
                productName,
                tags,
                tags.size(),
                pageable);
    }

    private Page<Product> findWithNameAndMinimumCostAndTagsOrderByDistanceAsc(Buyer buyer, String productName, BigDecimal minimumCost, Set<String> tags, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMinimumCostAndTagsOrderByDistanceAsc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                buyer.getLatitude(),
                buyer.getLongitude(),
                productName,
                minimumCost,
                tags,
                tags.size(),
                pageable);
    }

    private Page<Product> findWithNameAndMinimumCostAndTagsOrderByDistanceDesc(Buyer buyer, String productName, BigDecimal minimumCost, Set<String> tags, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMinimumCostAndTagsOrderByDistanceDesc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                buyer.getLatitude(),
                buyer.getLongitude(),
                productName,
                minimumCost,
                tags,
                tags.size(),
                pageable);
    }

    private Page<Product> findWithNameAndMaximumCostAndTagsOrderByDistanceAsc(Buyer buyer, String productName, BigDecimal maximumCost, Set<String> tags, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMaximumCostAndTagsOrderByDistanceAsc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                buyer.getLatitude(),
                buyer.getLongitude(),
                productName,
                maximumCost,
                tags,
                tags.size(),
                pageable);
    }

    private Page<Product> findWithNameAndMaximumCostAndTagsOrderByDistanceDesc(Buyer buyer, String productName, BigDecimal maximumCost, Set<String> tags, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMaximumCostAndTagsOrderByDistanceDesc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                buyer.getLatitude(),
                buyer.getLongitude(),
                productName,
                maximumCost,
                tags,
                tags.size(),
                pageable);
    }

    private Page<Product> findWithNameAndMinimumCostAndMaximumCostAndTagsOrderByDistanceAsc(Buyer buyer, String productName, BigDecimal minimumCost, BigDecimal maximumCost, Set<String> tags, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMinimumCostAndMaximumCostAndTagsOrderByDistanceAsc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                buyer.getLatitude(),
                buyer.getLongitude(),
                productName,
                minimumCost,
                maximumCost,
                tags,
                tags.size(),
                pageable);
    }

    private Page<Product> findWithNameAndMinimumCostAndMaximumCostAndTagsOrderByDistanceDesc(Buyer buyer, String productName, BigDecimal minimumCost, BigDecimal maximumCost, Set<String> tags, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMinimumCostAndMaximumCostAndTagsOrderByDistanceDesc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                buyer.getLatitude(),
                buyer.getLongitude(),
                productName,
                minimumCost,
                maximumCost,
                tags,
                tags.size(),
                pageable);
    }

    private Page<Product> findWithNameOrderByCostAsc(Buyer buyer, String productName, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameOrderByCostAsc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                pageable);
    }

    private Page<Product> findWithNameOrderByCostDesc(Buyer buyer, String productName, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameOrderByCostDesc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                pageable);
    }

    private Page<Product> findWithNameAndMinimumCostOrderByCostAsc(Buyer buyer, String productName, BigDecimal minimumCost, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMinimumCostOrderByCostAsc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                minimumCost,
                pageable);
    }

    private Page<Product> findWithNameAndMinimumCostOrderByCostDesc(Buyer buyer, String productName, BigDecimal minimumCost, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMinimumCostOrderByCostDesc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                minimumCost,
                pageable);
    }

    private Page<Product> findWithNameAndMaximumCostOrderByCostAsc(Buyer buyer, String productName, BigDecimal maximumCost, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMaximumCostOrderByCostAsc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                maximumCost,
                pageable);
    }

    private Page<Product> findWithNameAndMaximumCostOrderByCostDesc(Buyer buyer, String productName, BigDecimal maximumCost, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMaximumCostOrderByCostDesc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                maximumCost,
                pageable);
    }

    private Page<Product> findWithNameAndMinimumCostAndMaximumCostOrderByCostAsc(Buyer buyer, String productName, BigDecimal minimumCost, BigDecimal maximumCost, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMinimumCostAndMaximumCostOrderByCostAsc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                minimumCost,
                maximumCost,
                pageable);
    }

    private Page<Product> findWithNameAndMinimumCostAndMaximumCostOrderByCostDesc(Buyer buyer, String productName, BigDecimal minimumCost, BigDecimal maximumCost, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMinimumCostAndMaximumCostOrderByCostDesc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                minimumCost,
                maximumCost,
                pageable);
    }

    private Page<Product> findWithNameAndTagsOrderByCostAsc(Buyer buyer, String productName, Set<String> tags, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndTagsOrderByCostAsc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                tags,
                tags.size(),
                pageable);
    }

    private Page<Product> findWithNameAndTagsOrderByCostDesc(Buyer buyer, String productName, Set<String> tags, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndTagsOrderByCostDesc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                tags,
                tags.size(),
                pageable);
    }

    private Page<Product> findWithNameAndMinimumCostAndTagsOrderByCostAsc(Buyer buyer, String productName, BigDecimal minimumCost, Set<String> tags, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMinimumCostAndTagsOrderByCostAsc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                minimumCost,
                tags,
                tags.size(),
                pageable);
    }

    private Page<Product> findWithNameAndMinimumCostAndTagsOrderByCostDesc(Buyer buyer, String productName, BigDecimal minimumCost, Set<String> tags, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMinimumCostAndTagsOrderByCostDesc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                minimumCost,
                tags,
                tags.size(),
                pageable);
    }

    private Page<Product> findWithNameAndMaximumCostAndTagsOrderByCostAsc(Buyer buyer, String productName, BigDecimal maximumCost, Set<String> tags, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMaximumCostAndTagsOrderByCostAsc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                maximumCost,
                tags,
                tags.size(),
                pageable);
    }

    private Page<Product> findWithNameAndMaximumCostAndTagsOrderByCostDesc(Buyer buyer, String productName, BigDecimal maximumCost, Set<String> tags, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMaximumCostAndTagsOrderByCostDesc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                maximumCost,
                tags,
                tags.size(),
                pageable);
    }

    private Page<Product> findWithNameAndMinimumCostAndMaximumCostAndTagsOrderByCostAsc(Buyer buyer, String productName, BigDecimal minimumCost, BigDecimal maximumCost, Set<String> tags, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMinimumCostAndMaximumCostAndTagsOrderByCostAsc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                minimumCost,
                maximumCost,
                tags,
                tags.size(),
                pageable);
    }

    private Page<Product> findWithNameAndMinimumCostAndMaximumCostAndTagsOrderByCostDesc(Buyer buyer, String productName, BigDecimal minimumCost, BigDecimal maximumCost, Set<String> tags, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMinimumCostAndMaximumCostAndTagsOrderByCostDesc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                minimumCost,
                maximumCost,
                tags,
                tags.size(),
                pageable);
    }

    private Page<Product> findWithNameOrderByReviewAsc(Buyer buyer, String productName, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameOrderByReviewAsc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                pageable);
    }

    private Page<Product> findWithNameOrderByReviewDesc(Buyer buyer, String productName, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameOrderByReviewDesc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                pageable);
    }

    private Page<Product> findWithNameAndMinimumCostOrderByReviewAsc(Buyer buyer, String productName, BigDecimal minimumCost, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMinimumCostOrderByReviewAsc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                minimumCost,
                pageable);
    }

    private Page<Product> findWithNameAndMinimumCostOrderByReviewDesc(Buyer buyer, String productName, BigDecimal minimumCost, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMinimumCostOrderByReviewDesc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                minimumCost,
                pageable);
    }

    private Page<Product> findWithNameAndMaximumCostOrderByReviewAsc(Buyer buyer, String productName, BigDecimal maximumCost, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMaximumCostOrderByReviewAsc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                maximumCost,
                pageable);
    }

    private Page<Product> findWithNameAndMaximumCostOrderByReviewDesc(Buyer buyer, String productName, BigDecimal maximumCost, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMaximumCostOrderByReviewDesc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                maximumCost,
                pageable);
    }

    private Page<Product> findWithNameAndMinimumCostAndMaximumCostOrderByReviewAsc(Buyer buyer, String productName, BigDecimal minimumCost, BigDecimal maximumCost, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMinimumCostAndMaximumCostOrderByReviewAsc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                minimumCost,
                maximumCost,
                pageable);
    }

    private Page<Product> findWithNameAndMinimumCostAndMaximumCostOrderByReviewDesc(Buyer buyer, String productName, BigDecimal minimumCost, BigDecimal maximumCost, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMinimumCostAndMaximumCostOrderByReviewDesc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                minimumCost,
                maximumCost,
                pageable);
    }

    private Page<Product> findWithNameAndTagsOrderByReviewAsc(Buyer buyer, String productName, Set<String> tags, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndTagsOrderByReviewAsc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                tags,
                tags.size(),
                pageable);
    }

    private Page<Product> findWithNameAndTagsOrderByReviewDesc(Buyer buyer, String productName, Set<String> tags, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndTagsOrderByReviewDesc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                tags,
                tags.size(),
                pageable);
    }

    private Page<Product> findWithNameAndMinimumCostAndTagsOrderByReviewAsc(Buyer buyer, String productName, BigDecimal minimumCost, Set<String> tags, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMinimumCostAndTagsOrderByReviewAsc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                minimumCost,
                tags,
                tags.size(),
                pageable);
    }

    private Page<Product> findWithNameAndMinimumCostAndTagsOrderByReviewDesc(Buyer buyer, String productName, BigDecimal minimumCost, Set<String> tags, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMinimumCostAndTagsOrderByReviewDesc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                minimumCost,
                tags,
                tags.size(),
                pageable);
    }

    private Page<Product> findWithNameAndMaximumCostAndTagsOrderByReviewAsc(Buyer buyer, String productName, BigDecimal maximumCost, Set<String> tags, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMaximumCostAndTagsOrderByReviewAsc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                maximumCost,
                tags,
                tags.size(),
                pageable);
    }

    private Page<Product> findWithNameAndMaximumCostAndTagsOrderByReviewDesc(Buyer buyer, String productName, BigDecimal maximumCost, Set<String> tags, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMaximumCostAndTagsOrderByReviewDesc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                maximumCost,
                tags,
                tags.size(),
                pageable);
    }

    private Page<Product> findWithNameAndMinimumCostAndMaximumCostAndTagsOrderByReviewAsc(Buyer buyer, String productName, BigDecimal minimumCost, BigDecimal maximumCost, Set<String> tags, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMinimumCostAndMaximumCostAndTagsOrderByReviewAsc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                minimumCost,
                maximumCost,
                tags,
                tags.size(),
                pageable);
    }

    private Page<Product> findWithNameAndMinimumCostAndMaximumCostAndTagsOrderByReviewDesc(Buyer buyer, String productName, BigDecimal minimumCost, BigDecimal maximumCost, Set<String> tags, Pageable pageable){
        List<Long> jobIds = new ArrayList<>();
        for(Job j : buyer.getJob()) jobIds.add(j.getId());
        return productRepository.findWithNameAndMinimumCostAndMaximumCostAndTagsOrderByReviewDesc(
                buyer.getGender().toString(),
                Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears(),
                jobIds,
                buyer.getAnnualIncome(),
                productName,
                minimumCost,
                maximumCost,
                tags,
                tags.size(),
                pageable);
    }

}
