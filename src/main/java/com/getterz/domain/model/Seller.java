package com.getterz.domain.model;

import com.getterz.domain.enumclass.Gender;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"products","purchases"})
@Builder
@Accessors(chain = true)
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;                                // ENCRYPT

    private String password;                            // ENCRYPT

    private String emergencyPassword;                   // ENCRYPT

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate dateOfBirth;

    @CreatedDate
    private LocalDateTime dateOfJoin;

    @LastModifiedDate
    private LocalDateTime dateOfModify;

    private String emailAddress;                        // ENCRYPT

    private String cellNumber;                          // ENCRYPT

    private Double latitude;

    private Double longitude;

    private String address;                             // ENCRYPT

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "seller")
    private List<Product> products;

    private String cryptoWallet;                        // ENCRYPT

    private BigDecimal fee;

    private BigDecimal sold;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "seller")
    private List<Purchase> purchases;

    private Boolean adminCertified;

}
