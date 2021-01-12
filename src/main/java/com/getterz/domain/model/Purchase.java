package com.getterz.domain.model;

import com.getterz.domain.enumclass.DeliverMethod;
import com.getterz.domain.enumclass.PurchaseState;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
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

    private BigDecimal totalCost;

    @Enumerated(EnumType.STRING)
    private DeliverMethod deliverMethod;

    @Enumerated(EnumType.STRING)
    private PurchaseState purchaseState;

    @CreatedDate
    private LocalDateTime purchaseDateTime;

    private LocalDateTime arrivalDateTime;

    private Boolean feePaid;

    private Boolean wroteReview;

    private Boolean sellerDelete;

    private Boolean transporterDelete;

    private Boolean buyerDelete;

}
