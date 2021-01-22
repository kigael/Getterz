package com.getterz.service.sellerApi;

import com.getterz.crypt.Cryptor;
import com.getterz.domain.enumclass.OrderByType;
import com.getterz.domain.enumclass.UserType;
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
import com.getterz.network.response.SearchApiResponse;
import com.getterz.network.session.Session;
import com.getterz.network.session.SessionApi;
import com.getterz.service.adminApi.JobApiLogicService;
import com.getterz.service.adminApi.ReviewApiLogicService;
import com.getterz.service.adminApi.SellerApiLogicService;
import com.getterz.service.adminApi.TagApiLogicService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellerProductLogicService {

    @Autowired
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final JobRepository jobRepository;
    private final TagRepository tagRepository;

    public Header<SearchApiResponse> searchProduct(String productName, OrderByType orderByType, String session, Pageable pageable) {
        String transactionType="SELLER SEARCH PRODUCT";
        if(session==null) return Header.ERROR(transactionType,"NO SESSION",null);
        else if(!SessionApi.checkSession(session)) return Header.ERROR(transactionType,"INVALID SESSION",null);
        else{
            Session s = Session.toSession(Cryptor.DECRYPT(session));
            if(s==null||s.getUserType()==null||s.getUserType()!= UserType.SELLER) return Header.ERROR(transactionType,"INVALID SESSION",null);

            Optional<Seller> seller = sellerRepository.findById(s.getId());
            if(seller.isEmpty()) return Header.ERROR(transactionType,"INVALID SESSION",null);

            if(productName==null||productName.isBlank()) productName="";
            Page<Product> products;

            if(orderByType==OrderByType.ORDER_BY_COST_ASC){
                products = productRepository.findBySellerAndNameContainingIgnoreCaseOrderByCostAsc(seller.get().getId(),productName,pageable);
            }
            else if(orderByType==OrderByType.ORDER_BY_COST_DESC){
                products = productRepository.findBySellerAndNameContainingIgnoreCaseOrderByCostDesc(seller.get().getId(),productName,pageable);
            }
            else if(orderByType==OrderByType.ORDER_BY_REGISTER_DATE_ASC){
                products = productRepository.findBySellerAndNameContainingIgnoreCaseOrderByRegisteredDateAsc(seller.get().getId(),productName,pageable);
            }
            else if(orderByType==OrderByType.ORDER_BY_REGISTER_DATER_DESC){
                products = productRepository.findBySellerAndNameContainingIgnoreCaseOrderByRegisteredDateDesc(seller.get().getId(),productName,pageable);
            }
            else if(orderByType==OrderByType.ORDER_BY_REVIEW_ASC){
                products = productRepository.findBySellerAndNameContainingIgnoreCaseOrderByReviewAsc(seller.get().getId(),productName,pageable);
            }
            else if(orderByType==OrderByType.ORDER_BY_REVIEW_DESC){
                products = productRepository.findBySellerAndNameContainingIgnoreCaseOrderByReviewDesc(seller.get().getId(),productName,pageable);
            }
            else{
                products = productRepository.findBySellerAndNameContainingIgnoreCase(seller.get().getId(),productName,pageable);
            }

            SearchApiResponse responseData = new SearchApiResponse();
            List<ProductApiResponse> productList = products.stream()
                    .map(this::Body)
                    .collect(Collectors.toList());
            responseData
                    .setProducts(productList)
                    .setCurrentPage(products.getNumber())
                    .setCurrentElements(products.getNumberOfElements())
                    .setTotalPage(products.getTotalPages())
                    .setTotalElements(products.getTotalElements());

            return Header.OK(transactionType,responseData,SessionApi.updateSession(session));
        }
    }

    public Header<ProductApiResponse> getProduct(String session, Long id) {
        String transactionType="SELLER VIEW PRODUCT";
        if(session==null) return Header.ERROR(transactionType,"NO SESSION",null);
        else if(!SessionApi.checkSession(session)) return Header.ERROR(transactionType,"INVALID SESSION",null);
        else{
            Session s = Session.toSession(Cryptor.DECRYPT(session));
            if(s==null||s.getUserType()==null||s.getUserType()!= UserType.SELLER) return Header.ERROR(transactionType,"INVALID SESSION",null);

            Optional<Seller> seller = sellerRepository.findById(s.getId());
            if(seller.isEmpty()) return Header.ERROR(transactionType,"INVALID SESSION",null);

            Optional<Product> product = productRepository.findById(id);
            if(product.isEmpty()) return Header.ERROR(transactionType,"PRODUCT NOT FOUND",SessionApi.updateSession(session));

            if(!product.get().getSeller().getId().equals(seller.get().getId())) return Header.ERROR(transactionType,"INVALID SESSION",null);

            return Header.OK(transactionType,response(product.get()),SessionApi.updateSession(session));
        }
    }

    public Header<ProductApiResponse> editProduct(Header<ProductApiRequest> request) {
        String transactionType = "SELLER EDIT PRODUCT";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA", SessionApi.updateSession(request.getSession()));
        else if(request.getSession()==null) return Header.ERROR(transactionType,"NO SESSION",null);
        else if(!SessionApi.checkSession(request.getSession())) return Header.ERROR(transactionType,"INVALID SESSION",null);
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
            if(body.getAllowedMinimumAge()==null||body.getAllowedMaximumAge()==null){
                description.append("NO AGE REGULATION\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getAllowedJob()==null&&body.getBannedJob()==null){
                description.append("NO JOB REGULATION\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getExposeToNoQualify()==null){
                description.append("NO EXPOSE OPTION\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getDescriptionLink()==null){
                description.append("NO DESCRIPTION LINK\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                Session s = Session.toSession(Cryptor.DECRYPT(request.getSession()));
                if(s==null||s.getUserType()==null||s.getUserType()!= UserType.SELLER) return Header.ERROR(transactionType,"INVALID SESSION",null);

                Optional<Seller> seller = sellerRepository.findById(s.getId());
                if(seller.isEmpty()) return Header.ERROR(transactionType,"INVALID SESSION",null);

                Optional<Product> product = productRepository.findById(body.getId());
                if(product.isEmpty()) return Header.ERROR(transactionType,"INVALID SESSION",null);

                if(!product.get().getSeller().getId().equals(seller.get().getId())) return Header.ERROR(transactionType,"INVALID SESSION",null);

                if(FormatCheck.productName(body.getName())) return Header.ERROR(transactionType,"INVALID PRODUCT NAME",SessionApi.updateSession(request.getSession()));

                if(body.getCost().compareTo(BigDecimal.ZERO)<=0) return Header.ERROR(transactionType,"INVALID PRODUCT COST",SessionApi.updateSession(request.getSession()));

                if(body.getTags()!=null){
                    for(String t : body.getTags()){
                        if(FormatCheck.tagName(t)) return Header.ERROR(transactionType,"INVALID TAG",SessionApi.updateSession(request.getSession()));
                    }
                }

                if(body.getQuantity()<=0) return Header.ERROR(transactionType,"INVALID QUANTITY",SessionApi.updateSession(request.getSession()));

                if(body.getAllowedMinimumAge()> body.getAllowedMaximumAge()) return Header.ERROR(transactionType,"INVALID AGE REGULATION",SessionApi.updateSession(request.getSession()));

                if(body.getAllowedJob()!=null&&body.getBannedJob()!=null) return Header.ERROR(transactionType,"INVALID JOB REGULATION",SessionApi.updateSession(request.getSession()));

                if(body.getAllowedJob()!=null){
                    for(String j : body.getAllowedJob()){
                        if(FormatCheck.jobName(j)) return Header.ERROR(transactionType,"INVALID JOB",SessionApi.updateSession(request.getSession()));
                    }
                }
                else if(body.getBannedJob()!=null){
                    for(String j : body.getBannedJob()){
                        if(FormatCheck.jobName(j)) return Header.ERROR(transactionType,"INVALID JOB",SessionApi.updateSession(request.getSession()));
                    }
                }

                if(!body.getDescriptionLink().startsWith("http://")&&!body.getDescriptionLink().startsWith("https://")) return Header.ERROR(transactionType,"INVALID DESCRIPTION LINK",SessionApi.updateSession(request.getSession()));

                Set<Tag> tags = new HashSet<>();
                for(String t : body.getTags()){
                    Optional<Tag> tag = tagRepository.findFirstByName(t);
                    if(tag.isEmpty()){
                        tags.add(tagRepository.save(Tag.builder().name(t).build()));
                    }
                    else{
                        tags.add(tag.get());
                    }
                }

                Set<Job> allowedJobs = new HashSet<>();
                if(body.getAllowedJob()!=null){
                    for(String j : body.getAllowedJob()){
                        Optional<Job> job = jobRepository.findFirstByName(j);
                        if(job.isEmpty()){
                            allowedJobs.add(jobRepository.save(Job.builder().name(j).build()));
                        }
                        else{
                            allowedJobs.add(job.get());
                        }
                    }
                }

                Set<Job> bannedJobs = new HashSet<>();
                if(body.getBannedJob()!=null){
                    for(String j : body.getBannedJob()){
                        Optional<Job> job = jobRepository.findFirstByName(j);
                        if(job.isEmpty()){
                            bannedJobs.add(jobRepository.save(Job.builder().name(j).build()));
                        }
                        else{
                            bannedJobs.add(job.get());
                        }
                    }
                }

                Product productEdit = productRepository.save(product.get()
                        .setName(body.getName())
                        .setCost(body.getCost())
                        .setTags(tags)
                        .setQuantity(body.getQuantity())
                        .setAllowedGender(DtoConverter.setOfGenderToString(body.getAllowedGender()))
                        .setAllowedMinimumAge(body.getAllowedMinimumAge())
                        .setAllowedMaximumAge(body.getAllowedMaximumAge())
                        .setAllowedJobs(allowedJobs)
                        .setBannedJobs(bannedJobs)
                        .setExposeToNoQualify(body.getExposeToNoQualify())
                        .setDescriptionLink(Cryptor.ENCRYPT(body.getDescriptionLink())));

                String fileName = seller.get().getId()+"-"+productEdit.getId();
                return Header.OK(transactionType,fileName,SessionApi.updateSession(request.getSession()));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(request.getSession()));
            }
        }
    }

    public Header<ProductApiResponse> deleteProduct(String session, Long id) {
        String transactionType="SELLER DELETE PRODUCT";
        if(session==null) return Header.ERROR(transactionType,"NO SESSION",null);
        else if(!SessionApi.checkSession(session)) return Header.ERROR(transactionType,"INVALID SESSION",null);
        else{
            Session s = Session.toSession(Cryptor.DECRYPT(session));
            if(s==null||s.getUserType()==null||s.getUserType()!= UserType.SELLER) return Header.ERROR(transactionType,"INVALID SESSION",null);

            Optional<Seller> seller = sellerRepository.findById(s.getId());
            if(seller.isEmpty()) return Header.ERROR(transactionType,"INVALID SESSION",null);

            Optional<Product> product = productRepository.findById(id);
            if(product.isEmpty()) return Header.ERROR(transactionType,"PRODUCT NOT FOUND",SessionApi.updateSession(session));

            if(!product.get().getSeller().getId().equals(seller.get().getId())) return Header.ERROR(transactionType,"INVALID SESSION",null);

            productRepository.delete(product.get());
            return Header.OK(transactionType,SessionApi.updateSession(session));
        }
    }

    private ProductApiResponse response(Product product){
        ProductApiResponse body = ProductApiResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .cost(product.getCost())
                .quantity(product.getQuantity())
                .allowedGender(DtoConverter.stringToSetOfGender(product.getAllowedGender()))
                .allowedMinimumAge(product.getAllowedMinimumAge())
                .allowedMaximumAge(product.getAllowedMaximumAge())
                .exposeToNoQualify(product.getExposeToNoQualify())
                .profileImageName(Cryptor.DECRYPT(product.getProfileImageName()))
                .descriptionLink(Cryptor.DECRYPT(product.getDescriptionLink()))
                .build();
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
                .name(product.getName())
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

    public Header<ProductApiResponse> registerProduct(Header<ProductApiRequest> request) {
        String transactionType = "SELLER PRODUCT REGISTER";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA", SessionApi.updateSession(request.getSession()));
        else if(request.getSession()==null) return Header.ERROR(transactionType,"NO SESSION",null);
        else if(!SessionApi.checkSession(request.getSession())) return Header.ERROR(transactionType,"INVALID SESSION",null);
        else {
            ProductApiRequest body = request.getData();
            StringBuilder description = new StringBuilder();
            Boolean isGreen = Boolean.TRUE;
            if(body.getName()==null) {
                description.append("NO NAME\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getCost()==null) {
                description.append("NO COST\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getName()==null) {
                description.append("NO NAME\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getQuantity()==null) {
                description.append("NO QUANTITY\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getAllowedGender()==null){
                description.append("NO ALLOWED GENDER\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getAllowedMinimumAge()==null||body.getAllowedMaximumAge()==null){
                description.append("NO AGE REGULATION\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getAllowedJob()==null&&body.getBannedJob()==null){
                description.append("NO JOB REGULATION\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getExposeToNoQualify()==null){
                description.append("NO EXPOSE OPTION\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getDescriptionLink()==null){
                description.append("NO DESCRIPTION LINK\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                Session s = Session.toSession(Cryptor.DECRYPT(request.getSession()));
                if(s==null||s.getUserType()==null||s.getUserType()!= UserType.SELLER) return Header.ERROR(transactionType,"INVALID SESSION",null);

                Optional<Seller> seller = sellerRepository.findById(s.getId());
                if(seller.isEmpty()) return Header.ERROR(transactionType,"INVALID SESSION",null);

                if(FormatCheck.productName(body.getName())) return Header.ERROR(transactionType,"INVALID PRODUCT NAME",SessionApi.updateSession(request.getSession()));

                if(body.getCost().compareTo(BigDecimal.ZERO)<=0) return Header.ERROR(transactionType,"INVALID PRODUCT COST",SessionApi.updateSession(request.getSession()));

                if(body.getTags()!=null){
                    for(String t : body.getTags()){
                        if(FormatCheck.tagName(t)) return Header.ERROR(transactionType,"INVALID TAG",SessionApi.updateSession(request.getSession()));
                    }
                }

                if(body.getQuantity()<=0) return Header.ERROR(transactionType,"INVALID QUANTITY",SessionApi.updateSession(request.getSession()));

                if(body.getAllowedMinimumAge()> body.getAllowedMaximumAge()) return Header.ERROR(transactionType,"INVALID AGE REGULATION",SessionApi.updateSession(request.getSession()));

                if(body.getAllowedJob()!=null&&body.getBannedJob()!=null) return Header.ERROR(transactionType,"INVALID JOB REGULATION",SessionApi.updateSession(request.getSession()));

                if(body.getAllowedJob()!=null){
                    for(String j : body.getAllowedJob()){
                        if(FormatCheck.jobName(j)) return Header.ERROR(transactionType,"INVALID JOB",SessionApi.updateSession(request.getSession()));
                    }
                }
                else if(body.getBannedJob()!=null){
                    for(String j : body.getBannedJob()){
                        if(FormatCheck.jobName(j)) return Header.ERROR(transactionType,"INVALID JOB",SessionApi.updateSession(request.getSession()));
                    }
                }

                if(!body.getDescriptionLink().startsWith("http://")&&!body.getDescriptionLink().startsWith("https://")) return Header.ERROR(transactionType,"INVALID DESCRIPTION LINK",SessionApi.updateSession(request.getSession()));

                Set<Tag> tags = new HashSet<>();
                for(String t : body.getTags()){
                    Optional<Tag> tag = tagRepository.findFirstByName(t);
                    if(tag.isEmpty()){
                        tags.add(tagRepository.save(Tag.builder().name(t).build()));
                    }
                    else{
                        tags.add(tag.get());
                    }
                }

                Set<Job> allowedJobs = new HashSet<>();
                if(body.getAllowedJob()!=null){
                    for(String j : body.getAllowedJob()){
                        Optional<Job> job = jobRepository.findFirstByName(j);
                        if(job.isEmpty()){
                            allowedJobs.add(jobRepository.save(Job.builder().name(j).build()));
                        }
                        else{
                            allowedJobs.add(job.get());
                        }
                    }
                }

                Set<Job> bannedJobs = new HashSet<>();
                if(body.getBannedJob()!=null){
                    for(String j : body.getBannedJob()){
                        Optional<Job> job = jobRepository.findFirstByName(j);
                        if(job.isEmpty()){
                            bannedJobs.add(jobRepository.save(Job.builder().name(j).build()));
                        }
                        else{
                            bannedJobs.add(job.get());
                        }
                    }
                }

                Product product = productRepository.save(Product.builder()
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
                        .descriptionLink(Cryptor.ENCRYPT(body.getDescriptionLink()))
                        .build());

                String fileName = seller.get().getId()+"-"+product.getId();
                return Header.OK(transactionType,fileName,SessionApi.updateSession(request.getSession()));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(request.getSession()));
            }
        }
    }

    public Header<ProductApiResponse> profileImageUpload(MultipartFile profileImage, String fileName) {
        String transactionType = "PRODUCT PROFILE IMAGE UPLOAD";
        if(profileImage==null) return Header.ERROR(transactionType,"NO PROFILE IMAGE",null);
        else if(fileName==null) return Header.ERROR(transactionType,"NO FILE NAME",null);
        else {
            Optional<Product> product;
            try{
                String[] splits = fileName.split("-");
                Long sellerId = Long.parseLong(splits[0]);
                Long productId = Long.parseLong(splits[1]);
                product = productRepository.findById(productId);
                if(product.isEmpty()) return Header.ERROR(transactionType,"INVALID FILE NAME",null);
                if(!product.get().getSeller().getId().equals(sellerId)) return Header.ERROR(transactionType,"INVALID FILE NAME",null);
            }catch(Exception e){
                return Header.ERROR(transactionType,"INVALID FILE NAME",null);
            }
            String dir = System.getProperty("user.dir") + "/upload/product/profile_image";
            makeDirectoryIfNotExist(dir);
            String name = fileName.concat(".").concat(FilenameUtils.getExtension(profileImage.getOriginalFilename()));
            Path fileNamePath = Paths.get(dir,name);
            try {
                Files.write(fileNamePath, profileImage.getBytes());
            } catch (IOException e) {
                return Header.ERROR(transactionType,"INVALID PROFILE IMAGE",null);
            }
            productRepository.save(product.get().setProfileImageName(Cryptor.ENCRYPT(name)));
            return Header.OK(transactionType,null);
        }
    }

    private void makeDirectoryIfNotExist(String imageDirectory) {
        File directory = new File(imageDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

}
