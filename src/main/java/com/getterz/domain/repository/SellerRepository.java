package com.getterz.domain.repository;

import com.getterz.domain.model.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller,Long> {

    Optional<Seller> findByEmailAddressAndEmailCertifiedTrue(String emailAddress);

    List<Seller> findByEmailAddressAndEmailCertifiedFalse(String emailAddress);

    Optional<Seller> findByEmailAddressAndAdminCertifiedTrue(String emailAddress);

    Page<Seller> findByEmailCertifiedTrueAndAdminCertifiedFalseOrderByDateOfJoinAsc(Pageable pageable);

}