package com.getterz.service.buyerApi;

import com.getterz.crypt.Cryptor;
import com.getterz.domain.model.Buyer;
import com.getterz.domain.repository.BuyerRepository;
import com.getterz.network.Header;
import com.getterz.network.request.BuyerApiRequest;
import com.getterz.network.response.BuyerApiResponse;
import com.getterz.network.session.SessionApi;
import com.getterz.service.AuthenticationService;
import com.getterz.service.adminApi.BuyerApiLogicService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BuyerAuthenticationLogicService extends AuthenticationService<BuyerApiRequest, BuyerApiResponse, Buyer> {

    @Autowired
    private final BuyerApiLogicService buyerApiLogicService;
    private final BuyerRepository buyerRepository;

    @Override
    public Header<BuyerApiResponse> signup(Header<BuyerApiRequest> request) {
        return buyerApiLogicService.create(request);
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
                Optional<Buyer> buyer = buyerRepository.findByEmailAddress(Cryptor.ENCRYPT(body.getEmailAddress()));
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
                .annualIncome(buyer.getAnnualIncome())
                .cryptoWallet(buyer.getCryptoWallet())
                .build();
    }

}
