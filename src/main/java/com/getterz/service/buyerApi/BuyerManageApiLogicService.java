package com.getterz.service.buyerApi;

import com.getterz.crypt.Cryptor;
import com.getterz.domain.enumclass.UserType;
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
import com.getterz.network.session.Session;
import com.getterz.network.session.SessionApi;
import com.getterz.service.adminApi.JobApiLogicService;
import com.getterz.service.adminApi.PurchaseApiLogicService;
import com.getterz.service.adminApi.ReviewApiLogicService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BuyerManageApiLogicService {

    @Autowired
    private final BuyerRepository buyerRepository;
    private final JobRepository jobRepository;

    public Header<BuyerApiResponse> read(String session) {
        String transactionType = "BUYER READ";
        if(session==null) return Header.ERROR(transactionType,"NO SESSION",null);
        else if(!SessionApi.checkSession(session)) return Header.ERROR(transactionType,"INVALID SESSION",null);
        else {
            Optional<Buyer> buyer;
            try{
                Session s = Session.toSession(Cryptor.DECRYPT(session));
                if(s.getUserType()!=UserType.BUYER) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(session));
                buyer = buyerRepository.findById(s.getId());
                if(buyer.isEmpty()) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(session));
            }catch(NullPointerException e){
                return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(session));
            }
            if(!Session.verifyBuyer(session,buyer.get())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(session));
            return Header.OK(transactionType,response(buyer.get()),SessionApi.updateSession(session));
        }
    }

    public Header<BuyerApiResponse> update(Header<BuyerApiRequest> request) {
        String transactionType = "BUYER UPDATE";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA", SessionApi.updateSession(request.getSession()));
        else if(request.getSession()==null) return Header.ERROR(transactionType,"NO SESSION",SessionApi.updateSession(request.getSession()));
        else if(!SessionApi.checkSession(request.getSession())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));
        else {
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
            if(body.getJob()==null){
                description.append("NO JOB\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getAnnualIncome()==null){
                description.append("NO ANNUAL INCOME\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getCryptoWallet()==null){
                description.append("NO CRYPTO WALLET\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                Optional<Buyer> buyer;
                try{
                    Session session = Session.toSession(Cryptor.DECRYPT(request.getSession()));
                    if(session.getUserType()!=UserType.BUYER) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));
                    buyer = buyerRepository.findById(session.getId());
                    if(buyer.isEmpty()||!buyer.get().getId().equals(body.getId())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));
                }catch(NullPointerException e){
                    return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));
                }

                if(FormatCheck.name(body.getName())) return Header.ERROR(transactionType,"INVALID NAME",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.password(body.getPassword())) return Header.ERROR(transactionType,"INVALID PASSWORD",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.password(body.getEmergencyPassword())) return Header.ERROR(transactionType,"INVALID EMERGENCY PASSWORD",SessionApi.updateSession(request.getSession()));

                if(body.getPassword().equals(body.getEmergencyPassword())) return Header.ERROR(transactionType,"DUPLICATE PASSWORD",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.emailAddress(body.getEmailAddress())) return Header.ERROR(transactionType,"INVALID EMAIL ADDRESS",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.cellNumber(body.getCellNumber())) return Header.ERROR(transactionType,"INVALID CELL NUMBER",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.coordinate(body.getLatitude(),body.getLongitude())) return Header.ERROR(transactionType,"INVALID COORDINATE",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.address(body.getAddress())) return Header.ERROR(transactionType,"INVALID ADDRESS",SessionApi.updateSession(request.getSession()));

                if(body.getJob().isEmpty()) return Header.ERROR(transactionType,"EMPTY JOB",SessionApi.updateSession(request.getSession()));

                for(String j : body.getJob()) if(FormatCheck.jobName(j)) return Header.ERROR(transactionType,"INVALID JOB",SessionApi.updateSession(request.getSession()));

                if(body.getAnnualIncome().compareTo(BigDecimal.ZERO)<0) return Header.ERROR(transactionType,"INVALID ANNUAL INCOME",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.cryptoWallet(body.getCryptoWallet())) return Header.ERROR(transactionType,"INVALID CRYPTO WALLET",SessionApi.updateSession(request.getSession()));

                List<Job> jobsToInsert = new ArrayList<>();
                for(String jobName : body.getJob()) jobsToInsert.add(jobRepository.findByName(jobName).orElse(jobRepository.save(Job.builder().name(jobName).build())));

                return Header.OK(
                        transactionType,
                        response(buyerRepository.save(buyer.get()
                                .setName(Cryptor.ENCRYPT(body.getName()))
                                .setPassword(Cryptor.ENCRYPT(body.getPassword()))
                                .setEmergencyPassword(Cryptor.ENCRYPT(body.getEmergencyPassword()))
                                .setGender(body.getGender())
                                .setDateOfBirth(body.getDateOfBirth())
                                .setCellNumber(Cryptor.ENCRYPT(body.getCellNumber()))
                                .setLatitude(body.getLatitude())
                                .setLongitude(body.getLongitude())
                                .setAddress(Cryptor.ENCRYPT(body.getAddress()))
                                .setJob(jobsToInsert)
                                .setAnnualIncome(body.getAnnualIncome())
                                .setCryptoWallet(Cryptor.ENCRYPT(body.getCryptoWallet())))),
                        SessionApi.updateSession(request.getSession())
                );
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(request.getSession()));
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
                .annualIncome(buyer.getAnnualIncome())
                .cryptoWallet(buyer.getCryptoWallet())
                .build();
        if(buyer.getPurchases()!=null){
            body.setOrders(new ArrayList<>());
            for(Purchase purchase : buyer.getPurchases())
                body.getOrders().add(PurchaseApiLogicService.Body(purchase));
        }
        if(buyer.getReviews()!=null){
            body.setReviews(new ArrayList<>());
            for(Review review : buyer.getReviews())
                body.getReviews().add(ReviewApiLogicService.Body(review));
        }
        if(buyer.getJob()!=null){
            body.setJob(new ArrayList<>());
            for(Job job : buyer.getJob())
                body.getJob().add(JobApiLogicService.Body(job));
        }
        return body;
    }

}
