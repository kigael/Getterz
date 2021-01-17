package com.getterz.service.sellerApi;

import com.getterz.crypt.Cryptor;
import com.getterz.domain.model.Seller;
import com.getterz.domain.repository.SellerRepository;
import com.getterz.network.Header;
import com.getterz.network.request.SellerApiRequest;
import com.getterz.network.response.SellerApiResponse;
import com.getterz.network.session.SessionApi;
import com.getterz.service.AuthenticationService;
import com.getterz.service.adminApi.SellerApiLogicService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerAuthenticationLogicService extends AuthenticationService<SellerApiRequest, SellerApiResponse, Seller> {

    @Autowired
    private final SellerApiLogicService sellerApiLogicService;
    private final SellerRepository sellerRepository;

    @Override
    public Header<SellerApiResponse> signup(Header<SellerApiRequest> request) {
        return sellerApiLogicService.create(request);
    }

    @Override
    public Header<SellerApiResponse> verifyImageUpload(MultipartFile verifyImage, String fileName) {
        return null;
    }

    @Override
    public Header<SellerApiResponse> profileImageUpload(MultipartFile profileImage, String fileName) {
        return null;
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
                Optional<Seller> seller = sellerRepository.findByEmailAddress(Cryptor.ENCRYPT(body.getEmailAddress()));
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
        return null;
    }

    private SellerApiResponse response(Seller seller){
        return SellerApiResponse.builder()
                .id(seller.getId())
                .name(seller.getName())
                .password(seller.getPassword())
                .emergencyPassword(seller.getEmergencyPassword())
                .gender(seller.getGender())
                .dateOfBirth(seller.getDateOfBirth())
                .dateOfJoin(seller.getDateOfJoin())
                .dateOfModify(seller.getDateOfModify())
                .emailAddress(seller.getEmailAddress())
                .cellNumber(seller.getCellNumber())
                .latitude(seller.getLatitude())
                .longitude(seller.getLongitude())
                .address(seller.getAddress())
                .backAccount(seller.getBackAccount())
                .soldAmount(seller.getSoldAmount())
                .build();
    }

}
