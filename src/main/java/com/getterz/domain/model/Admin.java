package com.getterz.domain.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;                            // ENCRYPT

    @CreatedDate
    private LocalDateTime dateOfJoin;

    @LastModifiedDate
    private LocalDateTime dateOfModify;

    private String emailAddress;                        // ENCRYPT

    private String cellNumber;                          // ENCRYPT

}
