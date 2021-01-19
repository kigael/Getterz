package com.getterz.domain.repository;

import com.getterz.domain.model.Buyer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer,Long> {

    Optional<Buyer> findByEmailAddressAndEmailCertifiedTrue(String emailAddress);

    List<Buyer> findByEmailAddressAndEmailCertifiedFalse(String emailAddress);

    Optional<Buyer> findByEmailAddressAndAdminCertifiedTrue(String emailAddress);

    Page<Buyer> findByEmailCertifiedTrueAndAdminCertifiedFalseOrderByDateOfJoinAsc(Pageable pageable);

}
