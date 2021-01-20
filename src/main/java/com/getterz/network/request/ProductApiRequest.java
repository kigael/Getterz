package com.getterz.network.request;

import com.getterz.domain.enumclass.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductApiRequest {

    private Long id;

    private String name;

    private BigDecimal cost;

    private Set<String> tags;

    private Long quantity;

    private Set<Gender> allowedGender;

    private Integer allowedMinimumAge;

    private Integer allowedMaximumAge;

    private Set<String> allowedJob;

    private Set<String> bannedJob;

    private Boolean exposeToNoQualify;

    private String descriptionLink;

}
