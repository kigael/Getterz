package com.getterz.domain.repository;

import com.getterz.domain.model.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer,Long> {
    Optional<Buyer> findByEmailAddress(String emailAddress);
}
