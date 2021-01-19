package com.getterz.service.adminApi;

import com.getterz.crypt.Cryptor;
import com.getterz.domain.model.Buyer;
import com.getterz.domain.model.Job;
import com.getterz.domain.model.Purchase;
import com.getterz.domain.model.Review;
import com.getterz.domain.repository.BuyerRepository;
import com.getterz.domain.repository.JobRepository;
import com.getterz.format.check.FormatCheck;
import com.getterz.network.Header;
import com.getterz.network.request.BuyerApiRequest;
import com.getterz.network.response.BuyerApiResponse;
import com.getterz.network.session.SessionApi;
import com.getterz.service.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BuyerApiLogicService extends CrudService<BuyerApiRequest, BuyerApiResponse, Buyer> {

    @Autowired
    private final BuyerRepository buyerRepository;
    private final JobRepository jobRepository;

    @Override
    public Header<BuyerApiResponse> create(Header<BuyerApiRequest> request) {
        String transactionType = "BUYER CREATE";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA",SessionApi.updateSession(request.getSession()));
        else{
            BuyerApiRequest body = request.getData();
            StringBuilder description = new StringBuilder();
            Boolean isGreen = Boolean.TRUE;
            if(body.getName()==null) {
                description.append("NO NAME\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getPassword()==null) {
                description.append("NO PASSWORD\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getEmergencyPassword()==null) {
                description.append("NO EMERGENCY PASSWORD\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getGender()==null) {
                description.append("NO GENDER\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getDateOfBirth()==null){
                description.append("NO DATE OF BIRTH\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getEmailAddress()==null){
                description.append("NO EMAIL ADDRESS\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getCellNumber()==null){
                description.append("NO CELL NUMBER\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getLatitude()==null||body.getLongitude()==null){
                description.append("NO COORDINATE\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getAddress()==null){
                description.append("NO ADDRESS\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getJobs()==null){
                description.append("NO JOB\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getCryptoWallet()==null){
                description.append("NO CRYPTO WALLET\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                if(FormatCheck.name(body.getName())) return Header.ERROR(transactionType,"INVALID NAME",SessionApi.updateSession(request.getSession()));

                if(!buyerRepository.findByEmailAddressAndEmailCertifiedTrue(Cryptor.ENCRYPT(body.getEmailAddress())).isEmpty()) return Header.ERROR(transactionType,"DUPLICATE EMAIL ADDRESS",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.password(body.getPassword())) return Header.ERROR(transactionType,"INVALID PASSWORD",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.password(body.getEmergencyPassword())) return Header.ERROR(transactionType,"INVALID EMERGENCY PASSWORD",SessionApi.updateSession(request.getSession()));

                if(body.getPassword().equals(body.getEmergencyPassword())) return Header.ERROR(transactionType,"DUPLICATE PASSWORD",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.emailAddress(body.getEmailAddress())) return Header.ERROR(transactionType,"INVALID EMAIL ADDRESS",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.cellNumber(body.getCellNumber())) return Header.ERROR(transactionType,"INVALID CELL NUMBER",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.coordinate(body.getLatitude(),body.getLongitude())) return Header.ERROR(transactionType,"INVALID COORDINATE",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.address(body.getAddress())) return Header.ERROR(transactionType,"INVALID ADDRESS",SessionApi.updateSession(request.getSession()));

                if(body.getJobs().isEmpty()) return Header.ERROR(transactionType,"EMPTY JOB",SessionApi.updateSession(request.getSession()));

                for(String j : body.getJobs()) if(FormatCheck.jobName(j)) return Header.ERROR(transactionType,"INVALID JOB",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.cryptoWallet(body.getCryptoWallet())) return Header.ERROR(transactionType,"INVALID CRYPTO WALLET",SessionApi.updateSession(request.getSession()));

                Set<Job> jobsToInsert = new HashSet<>();
                for(String jobName : body.getJobs()) jobsToInsert.add(jobRepository.findFirstByName(jobName).orElse(jobRepository.save(Job.builder().name(jobName).build())));

                return Header.OK(
                        transactionType,
                        response(baseRepository.save(Buyer.builder()
                                .name(Cryptor.ENCRYPT(body.getName()))
                                .password(Cryptor.ENCRYPT(body.getPassword()))
                                .emergencyPassword(Cryptor.ENCRYPT(body.getEmergencyPassword()))
                                .gender(body.getGender())
                                .dateOfBirth(body.getDateOfBirth())
                                .emailAddress(Cryptor.ENCRYPT(body.getEmailAddress()))
                                .cellNumber(Cryptor.ENCRYPT(body.getCellNumber()))
                                .latitude(body.getLatitude())
                                .longitude(body.getLongitude())
                                .address(Cryptor.ENCRYPT(body.getAddress()))
                                .jobs(jobsToInsert)
                                .cryptoWallet(Cryptor.ENCRYPT(body.getCryptoWallet()))
                                .build())),
                        SessionApi.updateSession(request.getSession())
                );
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(request.getSession()));
            }
        }
    }

    @Override
    public Header<BuyerApiResponse> read(Long id, String session) {
        String transactionType = "BUYER READ";
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
                        .map(buyer -> Header.OK(transactionType,response(buyer),SessionApi.updateSession(session)))
                        .orElseGet(()->Header.ERROR(transactionType, "BUYER NOT FOUND",SessionApi.updateSession(session)));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(session));
            }
        }
    }

    @Override
    public Header<BuyerApiResponse> update(Header<BuyerApiRequest> request) {
        String transactionType = "BUYER UPDATE";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA",SessionApi.updateSession(request.getSession()));
        else if(request.getSession()==null) return Header.ERROR(transactionType,"NO SESSION",SessionApi.updateSession(request.getSession()));
        else if(!SessionApi.checkSession(request.getSession())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));
        else{
            BuyerApiRequest body = request.getData();
            StringBuilder description = new StringBuilder();
            Boolean isGreen = Boolean.TRUE;
            if(body.getId()==null){
                description.append("NO ID\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getName()==null) {
                description.append("NO NAME\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getPassword()==null) {
                description.append("NO PASSWORD\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getEmergencyPassword()==null) {
                description.append("NO EMERGENCY PASSWORD\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getGender()==null) {
                description.append("NO GENDER\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getDateOfBirth()==null){
                description.append("NO DATE OF BIRTH\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getEmailAddress()==null){
                description.append("NO EMAIL ADDRESS\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getCellNumber()==null){
                description.append("NO CELL NUMBER\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getLatitude()==null||body.getLongitude()==null){
                description.append("NO COORDINATE\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getAddress()==null){
                description.append("NO ADDRESS\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getJobs()==null){
                description.append("NO JOB\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getCryptoWallet()==null){
                description.append("NO CRYPTO WALLET\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                Optional<Buyer> buyer = baseRepository.findById(body.getId());
                if(buyer.isEmpty()) return Header.ERROR(transactionType,"BUYER NOT FOUND",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.name(body.getName())) return Header.ERROR(transactionType,"INVALID NAME",SessionApi.updateSession(request.getSession()));

                if(!buyerRepository.findByEmailAddressAndEmailCertifiedTrue(Cryptor.ENCRYPT(body.getEmailAddress())).isEmpty()) return Header.ERROR(transactionType,"DUPLICATE PROFILE NAME",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.password(body.getPassword())) return Header.ERROR(transactionType,"INVALID PASSWORD",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.password(body.getEmergencyPassword())) return Header.ERROR(transactionType,"INVALID EMERGENCY PASSWORD",SessionApi.updateSession(request.getSession()));

                if(body.getPassword().equals(body.getEmergencyPassword())) return Header.ERROR(transactionType,"DUPLICATE PASSWORD",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.emailAddress(body.getEmailAddress())) return Header.ERROR(transactionType,"INVALID EMAIL ADDRESS",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.cellNumber(body.getCellNumber())) return Header.ERROR(transactionType,"INVALID CELL NUMBER",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.coordinate(body.getLatitude(),body.getLongitude())) return Header.ERROR(transactionType,"INVALID COORDINATE",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.address(body.getAddress())) return Header.ERROR(transactionType,"INVALID ADDRESS",SessionApi.updateSession(request.getSession()));

                if(body.getJobs().isEmpty()) return Header.ERROR(transactionType,"EMPTY JOB",SessionApi.updateSession(request.getSession()));

                for(String j : body.getJobs()) if(FormatCheck.jobName(j)) return Header.ERROR(transactionType,"INVALID JOB",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.cryptoWallet(body.getCryptoWallet())) return Header.ERROR(transactionType,"INVALID CRYPTO WALLET",SessionApi.updateSession(request.getSession()));

                Set<Job> jobsToInsert = new HashSet<>();
                for(String jobName : body.getJobs()) jobsToInsert.add(jobRepository.findFirstByName(jobName).orElse(jobRepository.save(Job.builder().name(jobName).build())));

                return Header.OK(
                        transactionType,
                        response(baseRepository.save(buyer.get()
                                .setName(Cryptor.ENCRYPT(body.getName()))
                                .setPassword(Cryptor.ENCRYPT(body.getPassword()))
                                .setEmergencyPassword(Cryptor.ENCRYPT(body.getEmergencyPassword()))
                                .setGender(body.getGender())
                                .setDateOfBirth(body.getDateOfBirth())
                                .setEmailAddress(Cryptor.ENCRYPT(body.getEmailAddress()))
                                .setCellNumber(Cryptor.ENCRYPT(body.getCellNumber()))
                                .setLatitude(body.getLatitude())
                                .setLongitude(body.getLongitude())
                                .setAddress(Cryptor.ENCRYPT(body.getAddress()))
                                .setJobs(jobsToInsert)
                                .setCryptoWallet(Cryptor.ENCRYPT(body.getCryptoWallet())))),
                        SessionApi.updateSession(request.getSession())
                );
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(request.getSession()));
            }
        }
    }

    @Override
    public Header delete(Long id, String session) {
        String transactionType = "BUYER DELETE";
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
                        .map(buyer -> {
                            baseRepository.delete(buyer);
                            return Header.OK(transactionType,SessionApi.updateSession(session));
                        })
                        .orElseGet(()->Header.ERROR(transactionType, "BUYER NOT FOUND",SessionApi.updateSession(session)));
            }
            else {
                return Header.ERROR(transactionType, description.substring(0,description.length()-1),SessionApi.updateSession(session));
            }
        }
    }

    private BuyerApiResponse response(Buyer buyer){
        BuyerApiResponse body = BuyerApiResponse.builder()
                .id(buyer.getId())
                .name(buyer.getName())
                .password(buyer.getPassword())
                .emergencyPassword(buyer.getEmergencyPassword())
                .gender(buyer.getGender())
                .dateOfBirth(buyer.getDateOfBirth())
                .dateOfJoin(buyer.getDateOfJoin())
                .dateOfModify(buyer.getDateOfModify())
                .emailAddress(buyer.getEmailAddress())
                .cellNumber(buyer.getCellNumber())
                .latitude(buyer.getLatitude())
                .longitude(buyer.getLongitude())
                .address(buyer.getAddress())
                .cryptoWallet(buyer.getCryptoWallet())
                .build();
        if(buyer.getPurchases()!=null){
            body.setPurchases(new ArrayList<>());
            for(Purchase purchase : buyer.getPurchases())
                body.getPurchases().add(PurchaseApiLogicService.Body(purchase));
        }
        if(buyer.getReviews()!=null){
            body.setReviews(new ArrayList<>());
            for(Review review : buyer.getReviews())
                body.getReviews().add(ReviewApiLogicService.Body(review));
        }
        if(buyer.getJobs()!=null){
            body.setJobs(new ArrayList<>());
            for(Job job : buyer.getJobs())
                body.getJobs().add(JobApiLogicService.Body(job));
        }
        return body;
    }

    public static BuyerApiResponse Body(Buyer buyer){
        return BuyerApiResponse.builder()
                .id(buyer.getId())
                .name(buyer.getName())
                .password(buyer.getPassword())
                .emergencyPassword(buyer.getEmergencyPassword())
                .gender(buyer.getGender())
                .dateOfBirth(buyer.getDateOfBirth())
                .dateOfJoin(buyer.getDateOfJoin())
                .dateOfModify(buyer.getDateOfModify())
                .emailAddress(buyer.getEmailAddress())
                .cellNumber(buyer.getCellNumber())
                .latitude(buyer.getLatitude())
                .longitude(buyer.getLongitude())
                .address(buyer.getAddress())
                .cryptoWallet(buyer.getCryptoWallet())
                .build();
    }

}