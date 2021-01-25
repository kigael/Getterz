package com.getterz.domain.model;

import com.getterz.domain.enumclass.DeliverMethod;
import com.getterz.domain.enumclass.PurchaseState;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"seller","buyer","product"})
@Builder
@Accessors(chain = true)
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Seller seller;

    @ManyToOne
    private Buyer buyer;

    @ManyToOne
    private Product product;

    private Long quantity;

    private Long totalSatoshi;               // TODO: handle this real time

    @Enumerated(EnumType.STRING)
    private DeliverMethod deliverMethod;

    @Enumerated(EnumType.STRING)
    private PurchaseState purchaseState;

    @CreatedDate
    private LocalDateTime purchaseDateTime;

    private LocalDateTime arrivalDateTime;

    private Boolean sellerChecked;

    private Boolean wroteReview;

    private Boolean sellerDelete;

    private Boolean buyerDelete;

}
