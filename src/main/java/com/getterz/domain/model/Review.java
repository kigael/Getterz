package com.getterz.domain.model;

import com.getterz.domain.enumclass.PurchaseReason;
import com.getterz.domain.enumclass.PurchaseResult;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"buyer","product"})
@Builder
@Accessors(chain = true)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Buyer buyer;

    @ManyToOne
    private Product product;

    @CreatedDate
    private LocalDateTime reviewDateTime;

    @Enumerated(EnumType.STRING)
    private PurchaseResult purchaseResult;

    @Enumerated(EnumType.STRING)
    private PurchaseReason purchaseReason;

}
