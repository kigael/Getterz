package com.getterz.domain.repository;

import com.getterz.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query(
    value =
    "SELECT * " +
    "FROM PRODUCT " +
    "WHERE allowed_gender LIKE CONCAT('%',:gender,'%') AND " +
    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) OR " +
    "expose_to_no_qualify AND " +
    "LOWER(name) LIKE LOWER(CONCAT('%',:productName,'%')) AND " +
    "cost BETWEEN :minimumCost AND :maximumCost AND " +
    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND LOWER(tag.name) REGEXP :tags) >= :numberOfTags"
    , nativeQuery = true)
    Page<Product> buyerSearchProduct(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            @Param("maximumCost")BigDecimal maximumCost,
            @Param("tags")String tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
    value =
    "SELECT * " +
    "FROM PRODUCT " +
    "WHERE allowed_gender LIKE CONCAT('%',:gender,'%') AND " +
    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) OR " +
    "expose_to_no_qualify AND " +
    "LOWER(name) LIKE LOWER(CONCAT('%',:productName,'%')) AND " +
    "cost BETWEEN :minimumCost AND :maximumCost AND " +
    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND LOWER(tag.name) REGEXP :tags) >= :numberOfTags" +
    "ORDER BY (SELECT ST_DISTANCE_SPHERE((SELECT POINT(latitude, longitude) FROM seller WHERE seller.id=seller_id), POINT(:latitude, :longitude))) ASC"
    , nativeQuery = true)
    Page<Product> buyerSearchProductOrderByDistanceAsc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("latitude")Double latitude,
            @Param("longitude")Double longitude,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            @Param("maximumCost")BigDecimal maximumCost,
            @Param("tags")String tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
    value =
    "SELECT * " +
    "FROM PRODUCT " +
    "WHERE allowed_gender LIKE CONCAT('%',:gender,'%') AND " +
    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) OR " +
    "expose_to_no_qualify AND " +
    "LOWER(name) LIKE LOWER(CONCAT('%',:productName,'%')) AND " +
    "cost BETWEEN :minimumCost AND :maximumCost AND " +
    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND LOWER(tag.name) REGEXP :tags) >= :numberOfTags" +
    "ORDER BY (SELECT ST_DISTANCE_SPHERE((SELECT POINT(latitude, longitude) FROM seller WHERE seller.id=seller_id), POINT(:latitude, :longitude))) DESC"
    , nativeQuery = true)
    Page<Product> buyerSearchProductOrderByDistanceDesc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("latitude")Double latitude,
            @Param("longitude")Double longitude,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            @Param("maximumCost")BigDecimal maximumCost,
            @Param("tags")String tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);
    
    @Query(
    value =
    "SELECT * " +
    "FROM PRODUCT " +
    "WHERE allowed_gender LIKE CONCAT('%',:gender,'%') AND " +
    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) OR " +
    "expose_to_no_qualify AND " +
    "LOWER(name) LIKE LOWER(CONCAT('%',:productName,'%')) AND " +
    "cost BETWEEN :minimumCost AND :maximumCost AND " +
    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND LOWER(tag.name) REGEXP :tags) >= :numberOfTags" +
    "ORDER BY cost"
    , nativeQuery = true)
    Page<Product> buyerSearchProductOrderByCostAsc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            @Param("maximumCost")BigDecimal maximumCost,
            @Param("tags")String tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
    value =
    "SELECT * " +
    "FROM PRODUCT " +
    "WHERE allowed_gender LIKE CONCAT('%',:gender,'%') AND " +
    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) OR " +
    "expose_to_no_qualify AND " +
    "LOWER(name) LIKE LOWER(CONCAT('%',:productName,'%')) AND " +
    "cost BETWEEN :minimumCost AND :maximumCost AND " +
    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND LOWER(tag.name) REGEXP :tags) >= :numberOfTags" +
    "ORDER BY cost DESC"
    , nativeQuery = true)
    Page<Product> buyerSearchProductOrderByCostDesc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            @Param("maximumCost")BigDecimal maximumCost,
            @Param("tags")String tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
    value =
    "SELECT * " +
    "FROM PRODUCT " +
    "WHERE allowed_gender LIKE CONCAT('%',:gender,'%') AND " +
    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) OR " +
    "expose_to_no_qualify AND " +
    "LOWER(name) LIKE LOWER(CONCAT('%',:productName,'%')) AND " +
    "cost BETWEEN :minimumCost AND :maximumCost AND " +
    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND LOWER(tag.name) REGEXP :tags) >= :numberOfTags" +
    "ORDER BY (SELECT COUNT(*) FROM review WHERE product_id = id AND purchase_result = \"SUCCESS\")"
    , nativeQuery = true)
    Page<Product> buyerSearchProductOrderByReviewAsc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            @Param("maximumCost")BigDecimal maximumCost,
            @Param("tags")String tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
    value =
    "SELECT * " +
    "FROM PRODUCT " +
    "WHERE allowed_gender LIKE CONCAT('%',:gender,'%') AND " +
    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) OR " +
    "expose_to_no_qualify AND " +
    "LOWER(name) LIKE LOWER(CONCAT('%',:productName,'%')) AND " +
    "cost BETWEEN :minimumCost AND :maximumCost AND " +
    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND LOWER(tag.name) REGEXP :tags) >= :numberOfTags" +
    "ORDER BY (SELECT COUNT(*) FROM review WHERE product_id = id AND purchase_result = \"SUCCESS\") DESC"
    , nativeQuery = true)
    Page<Product> buyerSearchProductOrderByReviewDesc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            @Param("maximumCost")BigDecimal maximumCost,
            @Param("tags")String tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
    value =
    "SELECT * " +
    "FROM PRODUCT " +
    "WHERE seller_id = :sellerId AND " +
    "LOWER(name) LIKE LOWER(CONCAT('%',:productName,'%')) "
    , nativeQuery = true)
    Page<Product> findBySellerAndNameContainingIgnoreCase(
            @Param("sellerId") Long sellerId,
            @Param("productName") String productName,
            Pageable pageable);

    @Query(
    value =
    "SELECT * " +
    "FROM PRODUCT " +
    "WHERE seller_id = :sellerId AND " +
    "LOWER(name) LIKE LOWER(CONCAT('%',:productName,'%')) " +
    "ORDER BY cost ASC "
    , nativeQuery = true)
    Page<Product> findBySellerAndNameContainingIgnoreCaseOrderByCostAsc(
            @Param("sellerId") Long sellerId,
            @Param("productName") String productName,
            Pageable pageable);

    @Query(
    value =
    "SELECT * " +
    "FROM PRODUCT " +
    "WHERE seller_id = :sellerId AND " +
    "LOWER(name) LIKE LOWER(CONCAT('%',:productName,'%')) " +
    "ORDER BY cost DESC "
    , nativeQuery = true)
    Page<Product> findBySellerAndNameContainingIgnoreCaseOrderByCostDesc(
            @Param("sellerId") Long sellerId,
            @Param("productName") String productName,
            Pageable pageable);

    @Query(
    value =
    "SELECT * " +
    "FROM PRODUCT " +
    "WHERE seller_id = :sellerId AND " +
    "LOWER(name) LIKE LOWER(CONCAT('%',:productName,'%')) " +
    "ORDER BY registered_date ASC "
    , nativeQuery = true)
    Page<Product> findBySellerAndNameContainingIgnoreCaseOrderByRegisteredDateAsc(
            @Param("sellerId") Long sellerId,
            @Param("productName") String productName,
            Pageable pageable);

    @Query(
    value =
    "SELECT * " +
    "FROM PRODUCT " +
    "WHERE seller_id = :sellerId AND " +
    "LOWER(name) LIKE LOWER(CONCAT('%',:productName,'%')) " +
    "ORDER BY registered_date DESC "
    , nativeQuery = true)
    Page<Product> findBySellerAndNameContainingIgnoreCaseOrderByRegisteredDateDesc(
            @Param("sellerId") Long sellerId,
            @Param("productName") String productName,
            Pageable pageable);

    @Query(
    value =
    "SELECT * " +
    "FROM PRODUCT " +
    "WHERE seller_id = :sellerId AND " +
    "LOWER(name) LIKE LOWER(CONCAT('%',:productName,'%')) " +
    "ORDER BY (SELECT COUNT(*) FROM review WHERE product_id = id) ASC "
    , nativeQuery = true)
    Page<Product> findBySellerAndNameContainingIgnoreCaseOrderByReviewAsc(
            @Param("sellerId") Long sellerId,
            @Param("productName") String productName,
            Pageable pageable);

    @Query(
    value =
    "SELECT * " +
    "FROM PRODUCT " +
    "WHERE seller_id = :sellerId AND " +
    "LOWER(name) LIKE LOWER(CONCAT('%',:productName,'%')) " +
    "ORDER BY (SELECT COUNT(*) FROM review WHERE product_id = id) ASC "
    , nativeQuery = true)
    Page<Product> findBySellerAndNameContainingIgnoreCaseOrderByReviewDesc(
            @Param("sellerId") Long sellerId,
            @Param("productName") String productName,
            Pageable pageable);

}
