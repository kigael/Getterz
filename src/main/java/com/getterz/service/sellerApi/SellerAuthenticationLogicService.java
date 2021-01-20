package com.getterz.service.sellerApi;

import com.getterz.crypt.Cryptor;
import com.getterz.domain.enumclass.UserType;
import com.getterz.domain.model.Seller;
import com.getterz.domain.repository.SellerRepository;
import com.getterz.format.check.FormatCheck;
import com.getterz.mail.GetterzMailService;
import com.getterz.network.Header;
import com.getterz.network.request.SellerApiRequest;
import com.getterz.network.response.SellerApiResponse;
import com.getterz.network.session.SessionApi;
import com.getterz.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerAuthenticationLogicService extends AuthenticationService<SellerApiRequest, SellerApiResponse, Seller> {

    @Autowired
    private final SellerRepository sellerRepository;
    private final GetterzMailService getterzMailService;

    @Override
    public Header<SellerApiResponse> signup(Header<SellerApiRequest> request) {
        String transactionType = "SELLER SIGNUP";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA",null);
        else{
            SellerApiRequest body = request.getData();
            StringBuilder description = new StringBuilder();
            Boolean isGreen = Boolean.TRUE;
            if(body.getName()==null){
                description.append("NO NAME\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getPassword()==null){
                description.append("NO PASSWORD\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getEmergencyPassword()==null){
                description.append("NO EMERGENCY PASSWORD\n");
                isGreen=Boolean.FALSE;
            }
            if(body.getGender()==null){
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
            if(body.getVerifyImageName()==null){
                description.append("NO VERIFY IMAGE\n");
                isGreen=Boolean.FALSE;
            }
            if(isGreen){
                if(FormatCheck.name(body.getName())) return Header.ERROR(transactionType,"INVALID NAME",SessionApi.updateSession(request.getSession()));

                if(sellerRepository.findByEmailAddressAndEmailCertifiedTrue(Cryptor.ENCRYPT(body.getEmailAddress())).isPresent()) return Header.ERROR(transactionType,"DUPLICATE EMAIL ADDRESS",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.password(body.getPassword())) return Header.ERROR(transactionType,"INVALID PASSWORD",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.password(body.getEmergencyPassword())) return Header.ERROR(transactionType,"INVALID EMERGENCY PASSWORD",SessionApi.updateSession(request.getSession()));

                if(body.getPassword().equals(body.getEmergencyPassword())) return Header.ERROR(transactionType,"DUPLICATE PASSWORD",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.emailAddress(body.getEmailAddress())) return Header.ERROR(transactionType,"INVALID EMAIL ADDRESS",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.cellNumber(body.getCellNumber())) return Header.ERROR(transactionType,"INVALID CELL NUMBER",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.coordinate(body.getLatitude(),body.getLongitude())) return Header.ERROR(transactionType,"INVALID COORDINATE",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.address(body.getAddress())) return Header.ERROR(transactionType,"INVALID ADDRESS",SessionApi.updateSession(request.getSession()));

                Seller seller = sellerRepository.save(Seller.builder()
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
                        .soldAmount(BigDecimal.ZERO)
                        .adminCertified(false)
                        .verifyImageName(Cryptor.ENCRYPT(body.getVerifyImageName()))
                        .profileImageName(Cryptor.ENCRYPT(body.getProfileImageName()))
                        .build());

                try{
                    getterzMailService.sendSellerEmailVerification(body.getEmailAddress(),Cryptor.ENCRYPT(seller.getId()+"+"+seller.getEmailAddress()+"+"+ UserType.SELLER.toString()));
                }catch(Exception e) {
                    return Header.ERROR(transactionType, "INVALID EMAIL ADDRESS", SessionApi.updateSession(request.getSession()));
                }

                return Header.OK(transactionType, null);
            }
            else{
                return Header.ERROR(transactionType, description.substring(0,description.length()-1),null);
            }
        }
    }

    @Override
    public Header<SellerApiResponse> verifyImageUpload(MultipartFile verifyImage, String fileName) {
        String transactionType = "SELLER VERIFY IMAGE UPLOAD";
        if(verifyImage==null) return Header.ERROR(transactionType,"NO SELLER VERIFY IMAGE",null);
        else if(fileName==null) return Header.ERROR(transactionType,"NO FILE NAME",null);
        else {
            String dir = System.getProperty("user.dir") + "/upload/seller/verify_image/";
            makeDirectoryIfNotExist(dir);
            String name = fileName.concat(".").concat(FilenameUtils.getExtension(verifyImage.getOriginalFilename()));
            Path fileNamePath = Paths.get(dir,name);
            try {
                Files.write(fileNamePath, verifyImage.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                return Header.ERROR(transactionType,"INVALID SELLER VERIFY IMAGE",null);
            }
            return Header.OK(transactionType,name,null);
        }
    }

    @Override
    public Header<SellerApiResponse> profileImageUpload(MultipartFile profileImage, String fileName) {
        String transactionType = "SELLER PROFILE IMAGE UPLOAD";
        if(profileImage==null) return Header.ERROR(transactionType,"NO PROFILE IMAGE",null);
        else if(fileName==null) return Header.ERROR(transactionType,"NO FILE NAME",null);
        else {
            String dir = System.getProperty("user.dir") + "/upload/seller/profile_image/";
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
    public Header<SellerApiResponse> login(Header<SellerApiRequest> request) {
        String transactionType = "SELLER LOGIN";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA",null);
        else{
            SellerApiRequest body = request.getData();
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
                Optional<Seller> seller = sellerRepository.findByEmailAddressAndAdminCertifiedTrue(Cryptor.ENCRYPT(body.getEmailAddress()));
                if(seller.isEmpty()) return Header.ERROR(transactionType,"SELLER NOT FOUND",null);

                if(body.getPassword().equals(Cryptor.DECRYPT(seller.get().getPassword()))) return Header.OK(transactionType,response(seller.get()), SessionApi.startSession(seller.get()));

                else if(body.getPassword().equals(Cryptor.DECRYPT(seller.get().getEmergencyPassword()))) {
                    baseRepository.delete(seller.get());
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
    public Header<SellerApiResponse> logout(Header<SellerApiRequest> request) {
        String transactionType = "SELLER LOGOUT";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getSession()==null) return Header.ERROR(transactionType,"NO SESSION",SessionApi.updateSession(request.getSession()));
        else if(!SessionApi.checkSession(request.getSession())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));
        else{
            if(SessionApi.endSession(request.getSession())) return Header.OK(transactionType,null);
            else return Header.ERROR(transactionType,"INVALID SESSION",null);
        }
    }

    @Override
    public Header<SellerApiResponse> verifyEmail(String token) {
        token=Cryptor.DECRYPT(token);
        String transactionType = "SELLER EMAIL VERIFICATION";
        try{
            String[] tokens = token.split("\\+");
            if(!tokens[2].equals(UserType.SELLER.toString())) return Header.ERROR(transactionType, "INVALID TOKEN", null);
            Optional<Seller> seller = sellerRepository.findById(Long.parseLong(tokens[0]));
            if(seller.isEmpty()) return Header.ERROR(transactionType, "INVALID TOKEN", null);
            else if(!seller.get().getEmailAddress().equals(tokens[1])) return Header.ERROR(transactionType, "INVALID TOKEN", null);
            sellerRepository.save(seller.get().setEmailCertified(true));
            sellerRepository.deleteAll(sellerRepository.findByEmailAddressAndEmailCertifiedFalse(seller.get().getEmailAddress()));
            return Header.OK(transactionType,null);
        }catch (Exception e) {
            e.printStackTrace();
            return Header.ERROR(transactionType, "INVALID TOKEN", null);
        }
    }

    private SellerApiResponse response(Seller seller){
        return SellerApiResponse.builder()
                .name(Cryptor.DECRYPT(seller.getName()))
                .gender(seller.getGender())
                .dateOfBirth(seller.getDateOfBirth())
                .emailAddress(Cryptor.DECRYPT(seller.getEmailAddress()))
                .cellNumber(Cryptor.DECRYPT(seller.getCellNumber()))
                .latitude(seller.getLatitude())
                .longitude(seller.getLongitude())
                .address(Cryptor.DECRYPT(seller.getAddress()))
                .soldAmount(seller.getSoldAmount())
                .profileImageName(Cryptor.DECRYPT(seller.getProfileImageName()))
                .build();
    }

    private void makeDirectoryIfNotExist(String imageDirectory) {
        File directory = new File(imageDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

}
