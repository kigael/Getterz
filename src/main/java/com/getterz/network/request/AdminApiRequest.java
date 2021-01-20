package com.getterz.network.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminApiRequest {

    private Long id;

    private String password;

    private String emailAddress;

    private String cellNumber;

    private String key;

}
