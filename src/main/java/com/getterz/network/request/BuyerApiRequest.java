package com.getterz.network.request;

import com.getterz.domain.enumclass.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuyerApiRequest {

    private Long id;

    private String name;

    private String password;

    private String emergencyPassword;

    private Gender gender;

    private LocalDate dateOfBirth;

    private String emailAddress;

    private String cellNumber;

    private Double latitude;

    private Double longitude;

    private String address;

    private Set<String> job;

    private BigDecimal annualIncome;

    private String cryptoWallet;

}
