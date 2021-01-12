package com.getterz.domain.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"seller","tags","reviews"})
@Builder
@Accessors(chain = true)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    private Seller seller;

    private BigDecimal cost;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Tag> tags;

    @CreatedDate
    private LocalDateTime registeredDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    private Long quantity;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "product")
    private List<Review> reviews;

    private String allowedGender;

    private Integer allowedMinimumAge;

    private Integer allowedMaximumAge;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Job> allowedJobs;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Job> bannedJobs;

    private BigDecimal allowedMinimumAnnualIncome;

    private BigDecimal allowedMaximumAnnualIncome;

    private Boolean exposeToNoQualify;

    private String description;

}
