package com.getterz.service.adminApi;

import com.getterz.domain.model.*;
import com.getterz.domain.repository.JobRepository;
import com.getterz.domain.repository.ProductRepository;
import com.getterz.domain.repository.SellerRepository;
import com.getterz.domain.repository.TagRepository;
import com.getterz.format.check.FormatCheck;
import com.getterz.format.converter.DtoConverter;
import com.getterz.network.Header;
import com.getterz.network.request.ProductApiRequest;
import com.getterz.network.response.ProductApiResponse;
import com.getterz.network.session.SessionApi;
import com.getterz.service.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductApiLogicService extends CrudService<ProductApiRequest, ProductApiResponse, Product> {

    @Autowired
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final TagRepository tagRepository;
    private final JobRepository jobRepository;

    @Override
    public Header<ProductApiResponse> create(Header<ProductApiRequest> request) {
        String transactionType = "PRODUCT CREATE";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA",SessionApi.updateSession(request.getSession()));
        else if(request.getSession()==null) return Header.ERROR(transactionType,"NO SESSION", SessionApi.updateSession(request.getSession()));
        else if(!SessionApi.checkSession(request.getSession())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));
        else{
            ProductApiRequest body = request.getData();
            StringBuilder description = new StringBuilder();
            Boolean isGreen = Boolean.TRUE;
            if(body.getName()==null){
                description.append("NO NAME\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getSellerId()==null){
                description.append("NO SELLER ID\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getCost()==null){
                description.append("NO COST\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getTags()==null){
                description.append("NO TAGS\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getQuantity()==null){
                description.append("NO QUANTITY\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getAllowedGender()==null){
                description.append("NO ALLOWED GENDER\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getAllowedMinimumAge()==null){
                description.append("NO ALLOWED MINIMUM AGE\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getAllowedMaximumAge()==null){
                description.append("NO ALLOWED MAXIMUM AGE\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getAllowedJob()==null||body.getBannedJob()==null){
                description.append("NO JOB CONDITION\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getExposeToNoQualify()==null){
                description.append("NO EXPOSE TO NO QUALIFY\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getDescription()==null){
                description.append("NO DESCRIPTION\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                if(FormatCheck.productName(body.getName())) return Header.ERROR(transactionType,"INVALID NAME",SessionApi.updateSession(request.getSession()));

                Optional<Seller> seller = sellerRepository.findById(body.getSellerId());
                if(seller.isEmpty()) return Header.ERROR(transactionType,"SELLER NOT FOUND",SessionApi.updateSession(request.getSession()));

                if(body.getCost().compareTo(BigDecimal.ZERO)<0) return Header.ERROR(transactionType,"INVALID COST",SessionApi.updateSession(request.getSession()));

                if(body.getTags().isEmpty()) return Header.ERROR(transactionType,"EMPTY TAG",SessionApi.updateSession(request.getSession()));

                for(String t : body.getTags()) if(!FormatCheck.tagName(t)) return Header.ERROR(transactionType,"INVALID TAG NAME",SessionApi.updateSession(request.getSession()));

                if(body.getQuantity()<=0) return Header.ERROR(transactionType,"INVALID QUANTITY",SessionApi.updateSession(request.getSession()));

                if(body.getAllowedGender().isEmpty()) return  Header.ERROR(transactionType,"EMPTY GENDER",SessionApi.updateSession(request.getSession()));

                if(body.getAllowedMinimumAge()<0)  return  Header.ERROR(transactionType,"INVALID MINIMUM AGE",SessionApi.updateSession(request.getSession()));

                if(body.getAllowedMaximumAge()<0)  return  Header.ERROR(transactionType,"INVALID MAXIMUM AGE",SessionApi.updateSession(request.getSession()));

                if(body.getAllowedMinimumAge()>body.getAllowedMaximumAge()) return  Header.ERROR(transactionType,"INVALID AGE RANGE",SessionApi.updateSession(request.getSession()));

                for(String j : body.getAllowedJob()) if(FormatCheck.jobName(j)) return Header.ERROR(transactionType,"INVALID JOB",SessionApi.updateSession(request.getSession()));

                for(String j : body.getBannedJob()) if(FormatCheck.jobName(j)) return Header.ERROR(transactionType,"INVALID JOB",SessionApi.updateSession(request.getSession()));

                if(body.getAllowedJob().size()>0 && body.getBannedJob().size()>0) return Header.ERROR(transactionType,"INVALID JOB CONDITION",SessionApi.updateSession(request.getSession()));

                Set<Tag> tags = new HashSet<>();
                for(String tagName : body.getTags()) tags.add(tagRepository.findByName(tagName).orElse(tagRepository.save(Tag.builder().name(tagName).build())));

                Set<Job> allowedJobs = new HashSet<>();
                for(String jobName : body.getAllowedJob()) allowedJobs.add(jobRepository.findFirstByName(jobName).orElse(jobRepository.save(Job.builder().name(jobName).build())));

                Set<Job> bannedJobs = new HashSet<>();
                for(String jobName : body.getBannedJob()) allowedJobs.add(jobRepository.findFirstByName(jobName).orElse(jobRepository.save(Job.builder().name(jobName).build())));

                return Header.OK(
                        transactionType,
                        response(productRepository.save(Product.builder()
                        .name(body.getName())
                        .seller(seller.get())
                        .cost(body.getCost())
                        .tags(tags)
                        .quantity(body.getQuantity())
                        .allowedGender(DtoConverter.setOfGenderToString(body.getAllowedGender()))
                        .allowedMinimumAge(body.getAllowedMinimumAge())
                        .allowedMaximumAge(body.getAllowedMaximumAge())
                        .allowedJobs(allowedJobs)
                        .bannedJobs(bannedJobs)
                        .exposeToNoQualify(body.getExposeToNoQualify())
                        .build())),
                        SessionApi.updateSession(request.getSession()));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(request.getSession()));
            }
        }
    }

    @Override
    public Header<ProductApiResponse> read(Long id, String session) {
        String transactionType = "PRODUCT READ";
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
                return productRepository.findById(id)
                        .map(product->Header.OK(transactionType,response(product),SessionApi.updateSession(session)))
                        .orElseGet(()->Header.ERROR(transactionType,"PRODUCT NOT FOUND",SessionApi.updateSession(session)));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(session));
            }
        }
    }

    @Override
    public Header<ProductApiResponse> update(Header<ProductApiRequest> request) {
        String transactionType = "PRODUCT UPDATE";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA",SessionApi.updateSession(request.getSession()));
        else if(request.getSession()==null) return Header.ERROR(transactionType,"NO SESSION", SessionApi.updateSession(request.getSession()));
        else if(!SessionApi.checkSession(request.getSession())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));
        else{
            ProductApiRequest body = request.getData();
            StringBuilder description = new StringBuilder();
            Boolean isGreen = Boolean.TRUE;
            if(body.getId()==null){
                description.append("NO ID\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getName()==null){
                description.append("NO NAME\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getSellerId()==null){
                description.append("NO SELLER ID\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getCost()==null){
                description.append("NO COST\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getTags()==null){
                description.append("NO TAGS\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getQuantity()==null){
                description.append("NO QUANTITY\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getAllowedGender()==null){
                description.append("NO ALLOWED GENDER\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getAllowedMinimumAge()==null){
                description.append("NO ALLOWED MINIMUM AGE\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getAllowedMaximumAge()==null){
                description.append("NO ALLOWED MAXIMUM AGE\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getAllowedJob()==null||body.getBannedJob()==null){
                description.append("NO JOB CONDITION\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getExposeToNoQualify()==null){
                description.append("NO EXPOSE TO NO QUALIFY\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                Optional<Product> product = baseRepository.findById(body.getId());
                if(product.isEmpty()) return Header.ERROR(transactionType,"PRODUCT NOT FOUND",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.productName(body.getName())) return Header.ERROR(transactionType,"INVALID NAME",SessionApi.updateSession(request.getSession()));

                Optional<Seller> seller = sellerRepository.findById(body.getSellerId());
                if(seller.isEmpty()) return Header.ERROR(transactionType,"SELLER NOT FOUND",SessionApi.updateSession(request.getSession()));

                if(body.getCost().compareTo(BigDecimal.ZERO)<0) return Header.ERROR(transactionType,"INVALID COST",SessionApi.updateSession(request.getSession()));

                if(body.getTags().isEmpty()) return Header.ERROR(transactionType,"EMPTY TAG",SessionApi.updateSession(request.getSession()));

                for(String t : body.getTags()) if(FormatCheck.tagName(t)) return Header.ERROR(transactionType,"INVALID TAG NAME",SessionApi.updateSession(request.getSession()));

                if(body.getQuantity()<=0) return Header.ERROR(transactionType,"INVALID QUANTITY",SessionApi.updateSession(request.getSession()));

                if(body.getAllowedGender().isEmpty()) return  Header.ERROR(transactionType,"EMPTY GENDER",SessionApi.updateSession(request.getSession()));

                if(body.getAllowedMinimumAge()<0)  return  Header.ERROR(transactionType,"INVALID MINIMUM AGE",SessionApi.updateSession(request.getSession()));

                if(body.getAllowedMaximumAge()<0)  return  Header.ERROR(transactionType,"INVALID MAXIMUM AGE",SessionApi.updateSession(request.getSession()));

                if(body.getAllowedMinimumAge()>body.getAllowedMaximumAge()) return  Header.ERROR(transactionType,"INVALID AGE RANGE",SessionApi.updateSession(request.getSession()));

                for(String j : body.getAllowedJob()) if(FormatCheck.jobName(j)) return Header.ERROR(transactionType,"INVALID JOB",SessionApi.updateSession(request.getSession()));

                for(String j : body.getBannedJob()) if(FormatCheck.jobName(j)) return Header.ERROR(transactionType,"INVALID JOB",SessionApi.updateSession(request.getSession()));

                if(body.getAllowedJob().size()>0 && body.getBannedJob().size()>0) return Header.ERROR(transactionType,"INVALID JOB CONDITION",SessionApi.updateSession(request.getSession()));

                Set<Tag> tags = new HashSet<>();
                for(String tagName : body.getTags()) tags.add(tagRepository.findByName(tagName).orElse(tagRepository.save(Tag.builder().name(tagName).build())));

                Set<Job> allowedJobs = new HashSet<>();
                for(String jobName : body.getAllowedJob()) allowedJobs.add(jobRepository.findFirstByName(jobName).orElse(jobRepository.save(Job.builder().name(jobName).build())));

                Set<Job> bannedJobs = new HashSet<>();
                for(String jobName : body.getBannedJob()) allowedJobs.add(jobRepository.findFirstByName(jobName).orElse(jobRepository.save(Job.builder().name(jobName).build())));

                return Header.OK(
                        transactionType,
                        response(productRepository.save(product.get()
                        .setName(body.getName())
                        .setSeller(seller.get())
                        .setCost(body.getCost())
                        .setTags(tags)
                        .setQuantity(body.getQuantity())
                        .setAllowedGender(DtoConverter.setOfGenderToString(body.getAllowedGender()))
                        .setAllowedMinimumAge(body.getAllowedMinimumAge())
                        .setAllowedMaximumAge(body.getAllowedMaximumAge())
                        .setAllowedJobs(allowedJobs)
                        .setBannedJobs(bannedJobs)
                        .setExposeToNoQualify(body.getExposeToNoQualify()))),
                        SessionApi.updateSession(request.getSession()));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(request.getSession()));
            }
        }
    }

    @Override
    public Header delete(Long id, String session) {
        String transactionType = "PRODUCT DELETE";
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
                return productRepository.findById(id)
                        .map(product->{
                            productRepository.delete(product);
                            return Header.OK(transactionType,SessionApi.updateSession(session));
                        })
                        .orElseGet(()->Header.ERROR(transactionType,"PRODUCT NOT FOUND",SessionApi.updateSession(session)));
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

    public static ProductApiResponse Body(Product product){
        return ProductApiResponse.builder()
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
    }

}
