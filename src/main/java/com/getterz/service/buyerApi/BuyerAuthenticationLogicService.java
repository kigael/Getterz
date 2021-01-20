package com.getterz.service.buyerApi;

import com.getterz.crypt.Cryptor;
import com.getterz.domain.enumclass.UserType;
import com.getterz.domain.model.Buyer;
import com.getterz.domain.model.Job;
import com.getterz.domain.repository.BuyerRepository;
import com.getterz.domain.repository.JobRepository;
import com.getterz.format.check.FormatCheck;
import com.getterz.mail.GetterzMailService;
import com.getterz.network.Header;
import com.getterz.network.request.BuyerApiRequest;
import com.getterz.network.response.BuyerApiResponse;
import com.getterz.network.session.SessionApi;
import com.getterz.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BuyerAuthenticationLogicService extends AuthenticationService<BuyerApiRequest, BuyerApiResponse, Buyer> {

    @Autowired
    private final BuyerRepository buyerRepository;
    private final JobRepository jobRepository;
    private final GetterzMailService getterzMailService;

    @Override
    public Header<BuyerApiResponse> signup(Header<BuyerApiRequest> request) {
        String transactionType = "BUYER SIGNUP";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA",null);
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
            if(body.getVerifyImageName()==null){
                description.append("NO VERIFY IMAGE\n");
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
                for(String jobName : body.getJobs()){
                    Optional<Job> j = jobRepository.findFirstByName(jobName);
                    if(j.isPresent()){
                        jobsToInsert.add(j.get());
                    }
                    else{
                        jobsToInsert.add(jobRepository.save(Job.builder().name(jobName).build()));
                    }
                }
                Buyer buyer = baseRepository.save(Buyer.builder()
                        .name(Cryptor.ENCRYPT(body.getName()))
                        .password(Cryptor.ENCRYPT(body.getPassword()))
                        .emergencyPassword(Cryptor.ENCRYPT(body.getEmergencyPassword()))
                        .gender(body.getGender())
                        .dateOfBirth(body.getDateOfBirth())
                        .emailAddress(Cryptor.ENCRYPT(body.getEmailAddress()))
                        .emailCertified(false)
                        .cellNumber(Cryptor.ENCRYPT(body.getCellNumber()))
                        .latitude(body.getLatitude())
                        .longitude(body.getLongitude())
                        .address(Cryptor.ENCRYPT(body.getAddress()))
                        .jobs(jobsToInsert)
                        .cryptoWallet(Cryptor.ENCRYPT(body.getCryptoWallet()))
                        .adminCertified(false)
                        .verifyImageName(Cryptor.ENCRYPT(body.getVerifyImageName()))
                        .profileImageName(Cryptor.ENCRYPT(body.getProfileImageName()))
                        .build());
                try{
                    getterzMailService.sendBuyerEmailVerification(body.getEmailAddress(),Cryptor.ENCRYPT(buyer.getId()+"+"+buyer.getEmailAddress()+"+"+UserType.BUYER.toString()));
                }catch(Exception e) {
                    return Header.ERROR(transactionType, "INVALID EMAIL ADDRESS", SessionApi.updateSession(request.getSession()));
                }
                return Header.OK(transactionType, null);
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(request.getSession()));
            }
        }
    }

    @Override
    public Header<BuyerApiResponse> verifyImageUpload(MultipartFile verifyImage, String fileName) {
        String transactionType = "BUYER VERIFY IMAGE UPLOAD";
        if(verifyImage==null) return Header.ERROR(transactionType,"NO JOB VERIFY IMAGE",null);
        else if(fileName==null) return Header.ERROR(transactionType,"NO FILE NAME",null);
        else {
            String dir = System.getProperty("user.dir") + "/upload/buyer/verify_image/";
            makeDirectoryIfNotExist(dir);
            String name = fileName.concat(".").concat(FilenameUtils.getExtension(verifyImage.getOriginalFilename()));
            Path fileNamePath = Paths.get(dir,name);
            try {
                Files.write(fileNamePath, verifyImage.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                return Header.ERROR(transactionType,"INVALID JOB VERIFY IMAGE",null);
            }
            return Header.OK(transactionType,name,null);
        }
    }

    @Override
    public Header<BuyerApiResponse> profileImageUpload(MultipartFile profileImage, String fileName) {
        String transactionType = "BUYER PROFILE IMAGE UPLOAD";
        if(profileImage==null) return Header.ERROR(transactionType,"NO PROFILE IMAGE",null);
        else if(fileName==null) return Header.ERROR(transactionType,"NO FILE NAME",null);
        else {
            String dir = System.getProperty("user.dir") + "/upload/buyer/profile_image/";
            makeDirectoryIfNotExist(dir);
            String name = fileName.concat(".").concat(FilenameUtils.getExtension(profileImage.getOriginalFilename()));
            Path fileNamePath = Paths.get(dir,name);
            try {
                Files.write(fileNamePath, profileImage.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                return Header.ERROR(transactionType,"INVALID PROFILE IMAGE",null);
            }
            return Header.OK(transactionType,name,null);
        }
    }

    @Override
    public Header<BuyerApiResponse> login(Header<BuyerApiRequest> request) {
        String transactionType = "BUYER LOGIN";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA",null);
        else{
            BuyerApiRequest body = request.getData();
            StringBuilder description = new StringBuilder();
            Boolean isGreen = Boolean.TRUE;
            if(body.getEmailAddress()==null) {
                description.append("NO EMAIL ADDRESS\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getPassword()==null) {
                description.append("NO PASSWORD\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                Optional<Buyer> buyer = buyerRepository.findByEmailAddressAndAdminCertifiedTrue(Cryptor.ENCRYPT(body.getEmailAddress()));
                if(buyer.isEmpty()) return Header.ERROR(transactionType,"BUYER NOT FOUND",null);

                if(body.getPassword().equals(Cryptor.DECRYPT(buyer.get().getPassword()))) return Header.OK(transactionType,response(buyer.get()),SessionApi.startSession(buyer.get()));

                else if(body.getPassword().equals(Cryptor.DECRYPT(buyer.get().getEmergencyPassword()))) {
                    baseRepository.delete(buyer.get());
                    return Header.OK(transactionType,"EMERGENCY LOGIN",null);
                }

                else return Header.ERROR(transactionType,"PASSWORD MISMATCH",null);
            }
            else{
                return Header.ERROR(transactionType, description.substring(0,description.length()-1),null);
            }
        }
    }

    @Override
    public Header<BuyerApiResponse> logout(Header<BuyerApiRequest> request) {
        String transactionType = "BUYER LOGOUT";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getSession()==null) return Header.ERROR(transactionType,"NO SESSION",SessionApi.updateSession(request.getSession()));
        else if(!SessionApi.checkSession(request.getSession())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));
        else{
            if(SessionApi.endSession(request.getSession())) return Header.OK(transactionType,null);
            else return Header.ERROR(transactionType,"INVALID SESSION",null);
        }
    }

    @Override
    public Header<BuyerApiResponse> verifyEmail(String token) {
        token=Cryptor.DECRYPT(token);
        String transactionType = "BUYER EMAIL VERIFICATION";
        try{
            String[] tokens = token.split("\\+");
            if(!tokens[2].equals(UserType.BUYER.toString())) return Header.ERROR(transactionType, "INVALID TOKEN", null);
            Optional<Buyer> buyer = buyerRepository.findById(Long.parseLong(tokens[0]));
            if(buyer.isEmpty()) return Header.ERROR(transactionType, "INVALID TOKEN", null);
            else if(!buyer.get().getEmailAddress().equals(tokens[1])) return Header.ERROR(transactionType, "INVALID TOKEN", null);
            buyerRepository.save(buyer.get().setEmailCertified(true));
            buyerRepository.deleteAll(buyerRepository.findByEmailAddressAndEmailCertifiedFalse(buyer.get().getEmailAddress()));
            return Header.OK(transactionType,null);
        }catch (Exception e) {
            e.printStackTrace();
            return Header.ERROR(transactionType, "INVALID TOKEN", null);
        }
    }

    private BuyerApiResponse response(Buyer buyer){
        return BuyerApiResponse.builder()
                .name(Cryptor.DECRYPT(buyer.getName()))
                .gender(buyer.getGender())
                .dateOfBirth(buyer.getDateOfBirth())
                .emailAddress(Cryptor.DECRYPT(buyer.getEmailAddress()))
                .cellNumber(Cryptor.DECRYPT(buyer.getCellNumber()))
                .latitude(buyer.getLatitude())
                .longitude(buyer.getLongitude())
                .address(Cryptor.DECRYPT(buyer.getAddress()))
                .cryptoWallet(Cryptor.DECRYPT(buyer.getCryptoWallet()))
                .profileImageName(Cryptor.DECRYPT(buyer.getProfileImageName()))
                .build();
    }

    private void makeDirectoryIfNotExist(String imageDirectory) {
        File directory = new File(imageDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

}
