package com.getterz.network.request;

import com.getterz.domain.enumclass.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerApiRequest {

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

    private String verifyImageName;

    private String profileImageName;

}
