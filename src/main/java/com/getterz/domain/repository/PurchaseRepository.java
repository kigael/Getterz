package com.getterz.domain.repository;

import com.getterz.domain.model.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase,Long> {

    @Query(
            value =
                    "SELECT * " +
                    "FROM PURCHASE " +
                    "WHERE buyer_id = :buyerId AND " +
                    "purchase_state IN :condition;"
            , nativeQuery = true)
    Page<Purchase> findHistoryWithCondition(
            @Param("buyerId")Long buyerId,
            @Param("condition")Set<String> condition,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PURCHASE " +
                    "WHERE buyer_id = :buyerId AND " +
                    "purchase_state IN :condition " +
                    "ORDER BY (SELECT(ST_DISTANCE_SPHERE((SELECT POINT(latitude,longitude) FROM seller WHERE seller.id = seller_id), (SELECT POINT(latitude,longitude) FROM buyer WHERE buyer.id = :buyerId))));"
            , nativeQuery = true)
    Page<Purchase> findHistoryWithConditionOrderByDistanceAsc(
            @Param("buyerId")Long buyerId,
            @Param("condition")Set<String> condition,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PURCHASE " +
                    "WHERE buyer_id = :buyerId AND " +
                    "purchase_state IN :condition " +
                    "ORDER BY (SELECT(ST_DISTANCE_SPHERE((SELECT POINT(latitude,longitude) FROM seller WHERE seller.id = seller_id), (SELECT POINT(latitude,longitude) FROM buyer WHERE buyer.id = :buyerId)))) DESC;"
            , nativeQuery = true)
    Page<Purchase> findHistoryWithConditionOrderByDistanceDesc(
            @Param("buyerId")Long buyerId,
            @Param("condition")Set<String> condition,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PURCHASE " +
                    "WHERE buyer_id = :buyerId AND " +
                    "purchase_state IN :condition " +
                    "ORDER BY total_cost;"
            , nativeQuery = true)
    Page<Purchase> findHistoryWithConditionOrderByCostAsc(
            @Param("buyerId")Long buyerId,
            @Param("condition")Set<String> condition,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PURCHASE " +
                    "WHERE buyer_id = :buyerId AND " +
                    "purchase_state IN :condition " +
                    "ORDER BY total_cost DESC;"
            , nativeQuery = true)
    Page<Purchase> findHistoryWithConditionOrderByCostDesc(
            @Param("buyerId")Long buyerId,
            @Param("condition")Set<String> condition,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PURCHASE " +
                    "WHERE buyer_id = :buyerId AND " +
                    "purchase_state IN :condition " +
                    "ORDER BY (SELECT COUNT(*) FROM review WHERE review.product_id = product_id AND review.purchase_result = \"SUCCESS\");"
            , nativeQuery = true)
    Page<Purchase> findHistoryWithConditionOrderByReviewAsc(
            @Param("buyerId")Long buyerId,
            @Param("condition")Set<String> condition,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PURCHASE " +
                    "WHERE buyer_id = :buyerId AND " +
                    "purchase_state IN :condition " +
                    "ORDER BY (SELECT COUNT(*) FROM review WHERE review.product_id = product_id AND review.purchase_result = \"SUCCESS\") DESC;"
            , nativeQuery = true)
    Page<Purchase> findHistoryWithConditionOrderByReviewDesc(
            @Param("buyerId")Long buyerId,
            @Param("condition")Set<String> condition,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PURCHASE " +
                    "WHERE buyer_id = :buyerId AND " +
                    "purchase_state = \"WAITING\" " +
                    "ORDER BY purchase_date_time DESC;"
            , nativeQuery = true)
    Page<Purchase> findPickUpList(
            @Param("buyerId")Long buyerId,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PURCHASE " +
                    "WHERE buyer_id = :buyerId AND " +
                    "purchase_state = \"DELIVERING\" " +
                    "ORDER BY purchase_date_time DESC;"
            , nativeQuery = true)
    Page<Purchase> findDeliverList(
            @Param("buyerId")Long buyerId,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PURCHASE " +
                    "WHERE buyer_id = :buyerId AND " +
                    "NOT wrote_review AND " +
                    "purchase_state = \"PICKED_UP\" OR " +
                    "purchase_state = \"DELIVERED\" " +
                    "ORDER BY arrival_date_time;"
            , nativeQuery = true)
    Page<Purchase> findReviewList(
            @Param("buyerId")Long buyerId,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PURCHASE " +
                    "WHERE buyer_id = :buyerId AND " +
                    "WHERE product_id = :productId AND " +
                    "NOT wrote_review AND " +
                    "purchase_state = \"PICKED_UP\" OR " +
                    "purchase_state = \"DELIVERED\" " +
                    "LIMIT 1 ;"
            , nativeQuery = true)
    Optional<Purchase> findPurchaseWithoutReview(
            @Param("buyerId")Long buyerId,
            Long productId);
}
