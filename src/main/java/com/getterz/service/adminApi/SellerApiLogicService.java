package com.getterz.service.adminApi;

import com.getterz.crypt.Cryptor;
import com.getterz.domain.model.Product;
import com.getterz.domain.model.Purchase;
import com.getterz.domain.model.Seller;
import com.getterz.domain.repository.SellerRepository;
import com.getterz.format.check.FormatCheck;
import com.getterz.network.Header;
import com.getterz.network.request.SellerApiRequest;
import com.getterz.network.response.SellerApiResponse;
import com.getterz.network.session.SessionApi;
import com.getterz.service.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerApiLogicService extends CrudService<SellerApiRequest, SellerApiResponse, Seller> {

    @Autowired
    private final SellerRepository sellerRepository;

    @Override
    public Header<SellerApiResponse> create(Header<SellerApiRequest> request) {
        String transactionType = "SELLER CREATE";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA",SessionApi.updateSession(request.getSession()));
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

                return Header.OK(
                        transactionType,
                        response(Seller.builder()
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
                                .soldAmount(BigDecimal.ZERO)
                                .build()),
                        SessionApi.updateSession(request.getSession()));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(request.getSession()));
            }
        }
    }

    @Override
    public Header<SellerApiResponse> read(Long id, String session) {
        String transactionType = "SELLER READ";
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
                        .map(seller -> Header.OK(transactionType,response(seller),SessionApi.updateSession(session)))
                        .orElseGet(()->Header.ERROR(transactionType,"SELLER NOT FOUND",SessionApi.updateSession(session)));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(session));
            }
        }
    }

    @Override
    public Header<SellerApiResponse> update(Header<SellerApiRequest> request) {
        String transactionType = "SELLER UPDATE";
        if(request==null) return Header.ERROR(transactionType,"NO HEADER",null);
        else if(request.getData()==null) return Header.ERROR(transactionType,"NO DATA",SessionApi.updateSession(request.getSession()));
        else if(request.getSession()==null) return Header.ERROR(transactionType,"NO SESSION",SessionApi.updateSession(request.getSession()));
        else if(!SessionApi.checkSession(request.getSession())) return Header.ERROR(transactionType,"INVALID SESSION",SessionApi.updateSession(request.getSession()));
        else{
            SellerApiRequest body = request.getData();
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
            if(isGreen){
                Optional<Seller> seller = baseRepository.findById(body.getId());
                if(seller.isEmpty()) return Header.ERROR(transactionType,"SELLER NOT FOUND",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.name(body.getName())) return Header.ERROR(transactionType,"INVALID NAME",SessionApi.updateSession(request.getSession()));

                if(sellerRepository.findByEmailAddressAndAdminCertifiedTrue(Cryptor.ENCRYPT(body.getEmailAddress())).isPresent()) return Header.ERROR(transactionType,"DUPLICATE EMAIL ADDRESS",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.password(body.getPassword())) return Header.ERROR(transactionType,"INVALID PASSWORD",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.password(body.getEmergencyPassword())) return Header.ERROR(transactionType,"INVALID EMERGENCY PASSWORD",SessionApi.updateSession(request.getSession()));

                if(body.getPassword().equals(body.getEmergencyPassword())) return Header.ERROR(transactionType,"DUPLICATE PASSWORD",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.emailAddress(body.getEmailAddress())) return Header.ERROR(transactionType,"INVALID EMAIL ADDRESS",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.cellNumber(body.getCellNumber())) return Header.ERROR(transactionType,"INVALID CELL NUMBER",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.coordinate(body.getLatitude(),body.getLongitude())) return Header.ERROR(transactionType,"INVALID COORDINATE",SessionApi.updateSession(request.getSession()));

                if(FormatCheck.address(body.getAddress())) return Header.ERROR(transactionType,"INVALID ADDRESS",SessionApi.updateSession(request.getSession()));

                return Header.OK(
                        transactionType,
                        response(seller.get()
                                .setName(Cryptor.ENCRYPT(body.getName()))
                                .setPassword(Cryptor.ENCRYPT(body.getPassword()))
                                .setEmergencyPassword(Cryptor.ENCRYPT(body.getEmergencyPassword()))
                                .setGender(body.getGender())
                                .setDateOfBirth(body.getDateOfBirth())
                                .setEmailAddress(Cryptor.ENCRYPT(body.getEmailAddress()))
                                .setCellNumber(Cryptor.ENCRYPT(body.getCellNumber()))
                                .setLatitude(body.getLatitude())
                                .setLongitude(body.getLongitude())
                                .setAddress(Cryptor.ENCRYPT(body.getAddress()))),
                        SessionApi.updateSession(request.getSession()));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(request.getSession()));
            }
        }
    }

    @Override
    public Header delete(Long id, String session) {
        String transactionType = "SELLER READ";
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
                        .map(seller->{
                            baseRepository.delete(seller);
                            return Header.OK(transactionType,SessionApi.updateSession(session));
                        })
                        .orElseGet(()->Header.ERROR(transactionType,"SELLER NOT FOUND",SessionApi.updateSession(session)));
            }
            else{
                return Header.ERROR(transactionType,description.substring(0,description.length()-1),SessionApi.updateSession(session));
            }
        }
    }

    private SellerApiResponse response(Seller seller){
        SellerApiResponse body = SellerApiResponse.builder()
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
                .soldAmount(seller.getSoldAmount())
                .build();
        if(seller.getProducts()!=null){
            body.setProducts(new ArrayList<>());
            for(Product product : seller.getProducts())
                body.getProducts().add(ProductApiLogicService.Body(product));
        }
        if(seller.getPurchases()!=null){
            body.setOrders(new ArrayList<>());
            for(Purchase purchase : seller.getPurchases())
                body.getOrders().add(PurchaseApiLogicService.Body(purchase));
        }
        return body;
    }

    public static SellerApiResponse Body(Seller seller){
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
                .soldAmount(seller.getSoldAmount())
                .build();
    }

}
