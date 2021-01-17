package com.getterz.service.adminApi;

import com.getterz.crypt.Cryptor;
import com.getterz.domain.enumclass.UserType;
import com.getterz.domain.model.*;
import com.getterz.domain.repository.AdminRepository;
import com.getterz.domain.repository.BuyerRepository;
import com.getterz.format.check.FormatCheck;
import com.getterz.mail.GetterzMailService;
import com.getterz.network.Header;
import com.getterz.network.request.AdminApiRequest;
import com.getterz.network.request.BuyerVerifyApiRequest;
import com.getterz.network.response.*;
import com.getterz.network.session.Session;
import com.getterz.network.session.SessionApi;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminApiLogicService {

    @Autowired
    private final Environment env;
    private final AdminRepository adminRepository;
    private final BuyerRepository buyerRepository;
    private final GetterzMailService getterzMailService;

    public Header<AdminApiResponse> signup(Header<AdminApiRequest> request) {
        String transactionType = "ADMIN SIGNUP";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA", SessionApi.updateSession(request.getSession()));
        else{
            AdminApiRequest body = request.getData();
            StringBuilder description = new StringBuilder();
            Boolean isGreen = Boolean.TRUE;
            if(body.getPassword()==null) {
                description.append("NO PASSWORD\n");
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
            if(body.getKey()==null){
                description.append("NO KEY\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                if(adminRepository.findByEmailAddress(Cryptor.ENCRYPT(body.getEmailAddress())).isPresent()) return Header.ERROR(transactionType,"DUPLICATE EMAIL ADDRESS",null);

                if(FormatCheck.password(body.getPassword())) return Header.ERROR(transactionType,"INVALID PASSWORD",null);

                if(FormatCheck.emailAddress(body.getEmailAddress())) return Header.ERROR(transactionType,"INVALID EMAIL ADDRESS",null);

                if(FormatCheck.cellNumber(body.getCellNumber())) return Header.ERROR(transactionType,"INVALID CELL NUMBER",null);

                if(!body.getKey().equals(env.getProperty("credential.admin.admin_create_key"))) return Header.ERROR(transactionType,"INVALID KEY",null);

                adminRepository.save(Admin.builder()
                        .password(Cryptor.ENCRYPT(body.getPassword()))
                        .emailAddress(Cryptor.ENCRYPT(body.getEmailAddress()))
                        .cellNumber(Cryptor.ENCRYPT(body.getCellNumber()))
                        .build());

                return Header.OK(transactionType, null);
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),null);
            }
        }
    }

    public Header<AdminApiResponse> login(Header<AdminApiRequest> request) {
        String transactionType = "ADMIN LOGIN";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA", SessionApi.updateSession(request.getSession()));
        else{
            AdminApiRequest body = request.getData();
            StringBuilder description = new StringBuilder();
            Boolean isGreen = Boolean.TRUE;
            if(body.getPassword()==null) {
                description.append("NO PASSWORD\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getEmailAddress()==null){
                description.append("NO EMAIL ADDRESS\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                Optional<Admin> admin = adminRepository.findByEmailAddress(Cryptor.ENCRYPT(body.getEmailAddress()));
                if(admin.isEmpty()) return Header.ERROR(transactionType,"ADMIN NOT FOUND",null);

                if(admin.get().getPassword().equals(Cryptor.ENCRYPT(body.getPassword()))) return Header.OK(transactionType,response(admin.get()),SessionApi.startSession(admin.get()));

                return Header.ERROR(transactionType,"PASSWORD MISMATCH",null);
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),null);
            }
        }
    }

    public Header<AdminApiResponse> logout(Header<AdminApiRequest> request) {
        String transactionType = "ADMIN LOGOUT";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getSession()==null) return Header.ERROR(transactionType,"NO SESSION",SessionApi.updateSession(request.getSession()));
        else if(!SessionApi.checkSession(request.getSession())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));
        else{
            if(SessionApi.endSession(request.getSession())) return Header.OK(transactionType,null);
            else return Header.ERROR(transactionType,"INVALID SESSION",null);
        }
    }

    public static AdminApiResponse response(Admin admin){
        return AdminApiResponse.builder()
                .id(admin.getId())
                .emailAddress(Cryptor.DECRYPT(admin.getEmailAddress()))
                .cellNumber(Cryptor.DECRYPT(admin.getCellNumber()))
                .build();
    }

    public Header<BuyerVerifyApiResponse> buyerVerifyGetList(String session, Pageable pageable) {
        String transactionType="ADMIN BUYER VERIFY LIST";
        if(session==null) return Header.ERROR(transactionType,"NO SESSION",null);
        else if(!SessionApi.checkSession(session)) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(session));
        else{
            Session s = Session.toSession(Cryptor.DECRYPT(session));
            if(s==null||s.getUserType()==null||s.getUserType()!= UserType.ADMIN) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(session));
            Page<Buyer> buyers = buyerRepository.findByEmailCertifiedTrueAndAdminCertifiedFalseOrderByDateOfJoinAsc(pageable);
            BuyerVerifyApiResponse responseData = new BuyerVerifyApiResponse();
            List<BuyerApiResponse> buyerList = buyers.stream()
                    .map(AdminApiLogicService::Body)
                    .collect(Collectors.toList());
            responseData
                    .setBuyers(buyerList)
                    .setCurrentPage(buyers.getNumber())
                    .setCurrentElements(buyers.getNumberOfElements())
                    .setTotalPage(buyers.getTotalPages())
                    .setTotalElements(buyers.getTotalElements());
            return Header.OK(transactionType,responseData,SessionApi.updateSession(session));
        }
    }

    public Header<BuyerApiResponse> buyerVerifyGetBuyer(String session, Long id) {
        String transactionType="ADMIN BUYER VERIFY ITEM";
        if(session==null) return Header.ERROR(transactionType,"NO SESSION",null);
        else if(!SessionApi.checkSession(session)) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(session));
        else{
            Session s = Session.toSession(Cryptor.DECRYPT(session));
            if(s==null||s.getUserType()==null||s.getUserType()!= UserType.ADMIN) return Header.ERROR(transactionType,"INVALID SESSION",null);
            Optional<Buyer> buyer = buyerRepository.findById(id);
            if(buyer.isEmpty()) return Header.ERROR(transactionType,"BUYER NOT FOUND",SessionApi.updateSession(session));
            return Header.OK(transactionType,response(buyer.get()),SessionApi.updateSession(session));
        }
    }

    public Header<BuyerApiResponse> buyerVerify(Header<BuyerVerifyApiRequest> request) {
        String transactionType="ADMIN BUYER VERIFY";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getSession()==null) return Header.ERROR(transactionType,"NO SESSION",null);
        else if(!SessionApi.checkSession(request.getSession())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));
        else{
            Session s = Session.toSession(Cryptor.DECRYPT(request.getSession()));
            if(s==null||s.getUserType()==null||s.getUserType()!= UserType.ADMIN) return Header.ERROR(transactionType,"INVALID SESSION",null);
            BuyerVerifyApiRequest body = request.getData();
            Optional<Buyer> buyer = buyerRepository.findById(body.getBuyerId());
            if(buyer.isEmpty()) return Header.ERROR(transactionType,"BUYER NOT FOUND",SessionApi.updateSession(request.getSession()));
            try{
                getterzMailService.sendBuyerAdminMessage(buyer.get().getEmailAddress(),body.getMessage());
            }catch (Exception e){
                return Header.ERROR(transactionType,"EMAIL SEND FAIL",SessionApi.updateSession(request.getSession()));
            }
            if(body.getCertify()) buyerRepository.save(buyer.get().setAdminCertified(true));
            else buyerRepository.delete(buyer.get());
            return Header.OK(transactionType,SessionApi.updateSession(request.getSession()));
        }
    }

    private BuyerApiResponse response(Buyer buyer){
        BuyerApiResponse body = BuyerApiResponse.builder()
                .id(buyer.getId())
                .name(Cryptor.DECRYPT(buyer.getName()))
                .gender(buyer.getGender())
                .dateOfBirth(buyer.getDateOfBirth())
                .dateOfJoin(buyer.getDateOfJoin())
                .emailAddress(Cryptor.DECRYPT(buyer.getEmailAddress()))
                .cellNumber(Cryptor.DECRYPT(buyer.getCellNumber()))
                .latitude(buyer.getLatitude())
                .longitude(buyer.getLongitude())
                .address(Cryptor.DECRYPT(buyer.getAddress()))
                .cryptoWallet(Cryptor.DECRYPT(buyer.getCryptoWallet()))
                .verifyImageName(Cryptor.DECRYPT(buyer.getVerifyImageName()))
                .profileImageName(Cryptor.DECRYPT(buyer.getProfileImageName()))
                .build();
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
                .name(Cryptor.DECRYPT(buyer.getName()))
                .dateOfJoin(buyer.getDateOfJoin())
                .emailAddress(Cryptor.DECRYPT(buyer.getEmailAddress()))
                .cellNumber(Cryptor.DECRYPT(buyer.getCellNumber()))
                .build();
    }

}
