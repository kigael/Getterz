package com.getterz.network.response;

import com.getterz.domain.enumclass.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuyerApiResponse {

    private Long id;

    private String name;

    private String password;

    private String emergencyPassword;

    private Gender gender;

    private LocalDate dateOfBirth;

    private LocalDateTime dateOfJoin;

    private LocalDateTime dateOfModify;

    private String emailAddress;

    private String cellNumber;

    private Double latitude;

    private Double longitude;

    private String address;

    private List<JobApiResponse> jobs;

    private String cryptoWallet;

    private List<PurchaseApiResponse> purchases;

    private List<ReviewApiResponse> reviews;

    private Boolean adminCertified;

    private String verifyImageName;

    private String profileImageName;

}
