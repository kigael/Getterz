package com.getterz.network.response;

import com.getterz.domain.enumclass.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductApiResponse {

    private Long id;

    private String name;

    private SellerApiResponse seller;

    private BigDecimal cost;

    private List<TagApiResponse> tags;

    private LocalDateTime registeredDate;

    private LocalDateTime lastModifiedDate;

    private Long quantity;

    private List<ReviewApiResponse> reviews;

    private Set<Gender> allowedGender;

    private Integer allowedMinimumAge;

    private Integer allowedMaximumAge;

    private List<JobApiResponse> allowedJobs;

    private List<JobApiResponse> bannedJobs;

    private Boolean exposeToNoQualify;

    private String description;

}
