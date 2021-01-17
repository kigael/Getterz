package com.getterz.network.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminApiResponse {

    private Long id;

    private String password;

    private String emailAddress;

    private String cellNumber;

}
