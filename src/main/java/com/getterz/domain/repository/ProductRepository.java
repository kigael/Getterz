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
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\";"
            , nativeQuery = true)
    Page<Product> findWithName(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    ":minimumCost <= cost;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMinimumCost(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    ":maximumCost >= cost;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMaximumCost(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("maximumCost")BigDecimal maximumCost,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    "cost BETWEEN :minimumCost AND :maximumCost;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMinimumCostAndMaximumCost(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            @Param("maximumCost")BigDecimal maximumCost,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND \"%\" + tag.name + \"%\" IN :tags) >= :numberOfTags;"
            , nativeQuery = true)
    Page<Product> findWithNameAndTags(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("tags")Set<String> tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    ":minimumCost <= cost AND " +
                    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND \"%\" + tag.name + \"%\" IN :tags) >= :numberOfTags;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMinimumCostAndTags(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            @Param("tags")Set<String> tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    ":maximumCost >= cost AND " +
                    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND \"%\" + tag.name + \"%\" IN :tags) >= :numberOfTags;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMaximumCostAndTags(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("maximumCost")BigDecimal maximumCost,
            @Param("tags")Set<String> tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    "cost BETWEEN :minimumCost AND :maximumCost AND " +
                    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND \"%\" + tag.name + \"%\" IN :tags) >= :numberOfTags;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMinimumCostAndMaximumCostAndTags(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            @Param("maximumCost")BigDecimal maximumCost,
            @Param("tags")Set<String> tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" " +
                    "ORDER BY (SELECT ST_DISTANCE_SPHERE((SELECT POINT(latitude, longitude) FROM seller WHERE seller.id=seller_id), POINT(:latitude, :longitude)));"
            , nativeQuery = true)
    Page<Product> findWithNameOrderByDistanceAsc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("latitude")Double latitude,
            @Param("longitude")Double longitude,
            @Param("productName")String productName,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" " +
                    "ORDER BY (SELECT ST_DISTANCE_SPHERE((SELECT POINT(latitude, longitude) FROM seller WHERE seller.id=seller_id), POINT(:latitude, :longitude))) DESC;"
            , nativeQuery = true)
    Page<Product> findWithNameOrderByDistanceDesc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("latitude")Double latitude,
            @Param("longitude")Double longitude,
            @Param("productName")String productName,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    ":minimumCost <= cost " +
                    "ORDER BY (SELECT ST_DISTANCE_SPHERE((SELECT POINT(latitude, longitude) FROM seller WHERE seller.id=seller_id), POINT(:latitude, :longitude)));"
            , nativeQuery = true)
    Page<Product> findWithNameAndMinimumCostOrderByDistanceAsc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("latitude")Double latitude,
            @Param("longitude")Double longitude,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    ":minimumCost <= cost " +
                    "ORDER BY (SELECT ST_DISTANCE_SPHERE((SELECT POINT(latitude, longitude) FROM seller WHERE seller.id=seller_id), POINT(:latitude, :longitude))) DESC;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMinimumCostOrderByDistanceDesc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("latitude")Double latitude,
            @Param("longitude")Double longitude,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    ":maximumCost >= cost " +
                    "ORDER BY (SELECT ST_DISTANCE_SPHERE((SELECT POINT(latitude, longitude) FROM seller WHERE seller.id=seller_id), POINT(:latitude, :longitude)));"
            , nativeQuery = true)
    Page<Product> findWithNameAndMaximumCostOrderByDistanceAsc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("latitude")Double latitude,
            @Param("longitude")Double longitude,
            @Param("productName")String productName,
            @Param("maximumCost")BigDecimal maximumCost,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    ":maximumCost >= cost " +
                    "ORDER BY (SELECT ST_DISTANCE_SPHERE((SELECT POINT(latitude, longitude) FROM seller WHERE seller.id=seller_id), POINT(:latitude, :longitude))) DESC;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMaximumCostOrderByDistanceDesc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("latitude")Double latitude,
            @Param("longitude")Double longitude,
            @Param("productName")String productName,
            @Param("maximumCost")BigDecimal maximumCost,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    "cost BETWEEN :minimumCost AND :maximumCost " +
                    "ORDER BY (SELECT ST_DISTANCE_SPHERE((SELECT POINT(latitude, longitude) FROM seller WHERE seller.id=seller_id), POINT(:latitude, :longitude)));"
            , nativeQuery = true)
    Page<Product> findWithNameAndMinimumCostAndMaximumCostOrderByDistanceAsc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("latitude")Double latitude,
            @Param("longitude")Double longitude,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            @Param("maximumCost")BigDecimal maximumCost,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    "cost BETWEEN :minimumCost AND :maximumCost " +
                    "ORDER BY (SELECT ST_DISTANCE_SPHERE((SELECT POINT(latitude, longitude) FROM seller WHERE seller.id=seller_id), POINT(:latitude, :longitude))) DESC;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMinimumCostAndMaximumCostOrderByDistanceDesc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("latitude")Double latitude,
            @Param("longitude")Double longitude,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            @Param("maximumCost")BigDecimal maximumCost,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND \"%\" + tag.name + \"%\" IN :tags) >= :numberOfTags " +
                    "ORDER BY (SELECT ST_DISTANCE_SPHERE((SELECT POINT(latitude, longitude) FROM seller WHERE seller.id=seller_id), POINT(:latitude, :longitude)));"
            , nativeQuery = true)
    Page<Product> findWithNameAndTagsOrderByDistanceAsc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("latitude")Double latitude,
            @Param("longitude")Double longitude,
            @Param("productName")String productName,
            @Param("tags")Set<String> tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND \"%\" + tag.name + \"%\" IN :tags) >= :numberOfTags " +
                    "ORDER BY (SELECT ST_DISTANCE_SPHERE((SELECT POINT(latitude, longitude) FROM seller WHERE seller.id=seller_id), POINT(:latitude, :longitude))) DESC;"
            , nativeQuery = true)
    Page<Product> findWithNameAndTagsOrderByDistanceDesc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("latitude")Double latitude,
            @Param("longitude")Double longitude,
            @Param("productName")String productName,
            @Param("tags")Set<String> tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    ":minimumCost <= cost AND " +
                    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND \"%\" + tag.name + \"%\" IN :tags) >= :numberOfTags " +
                    "ORDER BY (SELECT ST_DISTANCE_SPHERE((SELECT POINT(latitude, longitude) FROM seller WHERE seller.id=seller_id), POINT(:latitude, :longitude)));"
            , nativeQuery = true)
    Page<Product> findWithNameAndMinimumCostAndTagsOrderByDistanceAsc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("latitude")Double latitude,
            @Param("longitude")Double longitude,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            @Param("tags")Set<String> tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    ":minimumCost <= cost AND " +
                    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND \"%\" + tag.name + \"%\" IN :tags) >= :numberOfTags " +
                    "ORDER BY (SELECT ST_DISTANCE_SPHERE((SELECT POINT(latitude, longitude) FROM seller WHERE seller.id=seller_id), POINT(:latitude, :longitude))) DESC;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMinimumCostAndTagsOrderByDistanceDesc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("latitude")Double latitude,
            @Param("longitude")Double longitude,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            @Param("tags")Set<String> tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    ":maximumCost >= cost AND " +
                    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND \"%\" + tag.name + \"%\" IN :tags) >= :numberOfTags " +
                    "ORDER BY (SELECT ST_DISTANCE_SPHERE((SELECT POINT(latitude, longitude) FROM seller WHERE seller.id=seller_id), POINT(:latitude, :longitude)));"
            , nativeQuery = true)
    Page<Product> findWithNameAndMaximumCostAndTagsOrderByDistanceAsc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("latitude")Double latitude,
            @Param("longitude")Double longitude,
            @Param("productName")String productName,
            @Param("maximumCost")BigDecimal maximumCost,
            @Param("tags")Set<String> tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    ":maximumCost >= cost AND " +
                    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND \"%\" + tag.name + \"%\" IN :tags) >= :numberOfTags " +
                    "ORDER BY (SELECT ST_DISTANCE_SPHERE((SELECT POINT(latitude, longitude) FROM seller WHERE seller.id=seller_id), POINT(:latitude, :longitude))) DESC;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMaximumCostAndTagsOrderByDistanceDesc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("latitude")Double latitude,
            @Param("longitude")Double longitude,
            @Param("productName")String productName,
            @Param("maximumCost")BigDecimal maximumCost,
            @Param("tags")Set<String> tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    "cost BETWEEN :minimumCost AND :maximumCost AND " +
                    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND \"%\" + tag.name + \"%\" IN :tags) >= :numberOfTags " +
                    "ORDER BY (SELECT ST_DISTANCE_SPHERE((SELECT POINT(latitude, longitude) FROM seller WHERE seller.id=seller_id), POINT(:latitude, :longitude)));"
            , nativeQuery = true)
    Page<Product> findWithNameAndMinimumCostAndMaximumCostAndTagsOrderByDistanceAsc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("latitude")Double latitude,
            @Param("longitude")Double longitude,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            @Param("maximumCost")BigDecimal maximumCost,
            @Param("tags")Set<String> tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    "cost BETWEEN :minimumCost AND :maximumCost AND " +
                    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND \"%\" + tag.name + \"%\" IN :tags) >= :numberOfTags " +
                    "ORDER BY (SELECT ST_DISTANCE_SPHERE((SELECT POINT(latitude, longitude) FROM seller WHERE seller.id=seller_id), POINT(:latitude, :longitude))) DESC;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMinimumCostAndMaximumCostAndTagsOrderByDistanceDesc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("latitude")Double latitude,
            @Param("longitude")Double longitude,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            @Param("maximumCost")BigDecimal maximumCost,
            @Param("tags")Set<String> tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" " +
                    "ORDER BY cost;"
            , nativeQuery = true)
    Page<Product> findWithNameOrderByCostAsc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" " +
                    "ORDER BY cost DESC;"
            , nativeQuery = true)
    Page<Product> findWithNameOrderByCostDesc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    ":minimumCost <= cost " +
                    "ORDER BY cost;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMinimumCostOrderByCostAsc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("minimumCost") BigDecimal minimumCost,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    ":minimumCost <= cost " +
                    "ORDER BY cost DESC;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMinimumCostOrderByCostDesc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("minimumCost") BigDecimal minimumCost,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    ":maximumCost >= cost " +
                    "ORDER BY cost;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMaximumCostOrderByCostAsc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("maximumCost") BigDecimal maximumCost,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    ":maximumCost >= cost " +
                    "ORDER BY cost DESC;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMaximumCostOrderByCostDesc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("maximumCost") BigDecimal maximumCost,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    "cost BETWEEN :minimumCost AND :maximumCost " +
                    "ORDER BY cost;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMinimumCostAndMaximumCostOrderByCostAsc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            @Param("maximumCost")BigDecimal maximumCost,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    "cost BETWEEN :minimumCost AND :maximumCost " +
                    "ORDER BY cost DESC;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMinimumCostAndMaximumCostOrderByCostDesc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            @Param("maximumCost")BigDecimal maximumCost,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND \"%\" + tag.name + \"%\" IN :tags) >= :numberOfTags " +
                    "ORDER BY cost;"
            , nativeQuery = true)
    Page<Product> findWithNameAndTagsOrderByCostAsc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("tags")Set<String> tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND \"%\" + tag.name + \"%\" IN :tags) >= :numberOfTags " +
                    "ORDER BY cost DESC;"
            , nativeQuery = true)
    Page<Product> findWithNameAndTagsOrderByCostDesc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("tags")Set<String> tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    ":minimumCost <= cost AND " +
                    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND \"%\" + tag.name + \"%\" IN :tags) >= :numberOfTags " +
                    "ORDER BY cost;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMinimumCostAndTagsOrderByCostAsc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            @Param("tags")Set<String> tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    ":minimumCost <= cost AND " +
                    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND \"%\" + tag.name + \"%\" IN :tags) >= :numberOfTags " +
                    "ORDER BY cost DESC;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMinimumCostAndTagsOrderByCostDesc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            @Param("tags")Set<String> tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    ":maximumCost >= cost AND " +
                    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND \"%\" + tag.name + \"%\" IN :tags) >= :numberOfTags " +
                    "ORDER BY cost;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMaximumCostAndTagsOrderByCostAsc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("maximumCost")BigDecimal maximumCost,
            @Param("tags")Set<String> tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    ":maximumCost >= cost AND " +
                    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND \"%\" + tag.name + \"%\" IN :tags) >= :numberOfTags " +
                    "ORDER BY cost DESC;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMaximumCostAndTagsOrderByCostDesc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("maximumCost")BigDecimal maximumCost,
            @Param("tags")Set<String> tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    "cost BETWEEN :minimumCost AND :maximumCost AND " +
                    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND \"%\" + tag.name + \"%\" IN :tags) >= :numberOfTags " +
                    "ORDER BY cost;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMinimumCostAndMaximumCostAndTagsOrderByCostAsc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            @Param("maximumCost")BigDecimal maximumCost,
            @Param("tags")Set<String> tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    "cost BETWEEN :minimumCost AND :maximumCost AND " +
                    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND \"%\" + tag.name + \"%\" IN :tags) >= :numberOfTags " +
                    "ORDER BY cost DESC;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMinimumCostAndMaximumCostAndTagsOrderByCostDesc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            @Param("maximumCost")BigDecimal maximumCost,
            @Param("tags")Set<String> tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" " +
                    "ORDER BY (SELECT COUNT(*) FROM review WHERE product_id = id AND purchase_result = \"SUCCESS\");"
            , nativeQuery = true)
    Page<Product> findWithNameOrderByReviewAsc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" " +
                    "ORDER BY (SELECT COUNT(*) FROM review WHERE product_id = id AND purchase_result = \"SUCCESS\") DESC;"
            , nativeQuery = true)
    Page<Product> findWithNameOrderByReviewDesc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    ":minimumCost <= cost " +
                    "ORDER BY (SELECT COUNT(*) FROM review WHERE product_id = id AND purchase_result = \"SUCCESS\");"
            , nativeQuery = true)
    Page<Product> findWithNameAndMinimumCostOrderByReviewAsc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    ":minimumCost <= cost " +
                    "ORDER BY (SELECT COUNT(*) FROM review WHERE product_id = id AND purchase_result = \"SUCCESS\") DESC;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMinimumCostOrderByReviewDesc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    ":maximumCost >= cost " +
                    "ORDER BY (SELECT COUNT(*) FROM review WHERE product_id = id AND purchase_result = \"SUCCESS\");"
            , nativeQuery = true)
    Page<Product> findWithNameAndMaximumCostOrderByReviewAsc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("maximumCost")BigDecimal maximumCost,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    ":maximumCost >= cost " +
                    "ORDER BY (SELECT COUNT(*) FROM review WHERE product_id = id AND purchase_result = \"SUCCESS\") DESC;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMaximumCostOrderByReviewDesc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("maximumCost")BigDecimal maximumCost,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    "cost BETWEEN :minimumCost AND :maximumCost " +
                    "ORDER BY (SELECT COUNT(*) FROM review WHERE product_id = id AND purchase_result = \"SUCCESS\");"
            , nativeQuery = true)
    Page<Product> findWithNameAndMinimumCostAndMaximumCostOrderByReviewAsc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            @Param("maximumCost")BigDecimal maximumCost,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    "cost BETWEEN :minimumCost AND :maximumCost " +
                    "ORDER BY (SELECT COUNT(*) FROM review WHERE product_id = id AND purchase_result = \"SUCCESS\") DESC;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMinimumCostAndMaximumCostOrderByReviewDesc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            @Param("maximumCost")BigDecimal maximumCost,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND \"%\" + tag.name + \"%\" IN :tags) >= :numberOfTags " +
                    "ORDER BY (SELECT COUNT(*) FROM review WHERE product_id = id AND purchase_result = \"SUCCESS\");"
            , nativeQuery = true)
    Page<Product> findWithNameAndTagsOrderByReviewAsc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("tags")Set<String> tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND \"%\" + tag.name + \"%\" IN :tags) >= :numberOfTags " +
                    "ORDER BY (SELECT COUNT(*) FROM review WHERE product_id = id AND purchase_result = \"SUCCESS\") DESC;"
            , nativeQuery = true)
    Page<Product> findWithNameAndTagsOrderByReviewDesc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("tags")Set<String> tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    ":minimumCost <= cost AND " +
                    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND \"%\" + tag.name + \"%\" IN :tags) >= :numberOfTags " +
                    "ORDER BY (SELECT COUNT(*) FROM review WHERE product_id = id AND purchase_result = \"SUCCESS\");"
            , nativeQuery = true)
    Page<Product> findWithNameAndMinimumCostAndTagsOrderByReviewAsc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            @Param("tags")Set<String> tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    ":minimumCost <= cost AND " +
                    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND \"%\" + tag.name + \"%\" IN :tags) >= :numberOfTags " +
                    "ORDER BY (SELECT COUNT(*) FROM review WHERE product_id = id AND purchase_result = \"SUCCESS\") DESC;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMinimumCostAndTagsOrderByReviewDesc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            @Param("tags")Set<String> tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    ":maximumCost >= cost AND " +
                    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND \"%\" + tag.name + \"%\" IN :tags) >= :numberOfTags " +
                    "ORDER BY (SELECT COUNT(*) FROM review WHERE product_id = id AND purchase_result = \"SUCCESS\");"
            , nativeQuery = true)
    Page<Product> findWithNameAndMaximumCostAndTagsOrderByReviewAsc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("maximumCost")BigDecimal maximumCost,
            @Param("tags")Set<String> tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    ":maximumCost >= cost AND " +
                    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND \"%\" + tag.name + \"%\" IN :tags) >= :numberOfTags " +
                    "ORDER BY (SELECT COUNT(*) FROM review WHERE product_id = id AND purchase_result = \"SUCCESS\") DESC;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMaximumCostAndTagsOrderByReviewDesc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("maximumCost")BigDecimal maximumCost,
            @Param("tags")Set<String> tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    "cost BETWEEN :minimumCost AND :maximumCost AND " +
                    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND \"%\" + tag.name + \"%\" IN :tags) >= :numberOfTags " +
                    "ORDER BY (SELECT COUNT(*) FROM review WHERE product_id = id AND purchase_result = \"SUCCESS\");"
            , nativeQuery = true)
    Page<Product> findWithNameAndMinimumCostAndMaximumCostAndTagsOrderByReviewAsc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            @Param("maximumCost")BigDecimal maximumCost,
            @Param("tags")Set<String> tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

    @Query(
            value =
                    "SELECT * " +
                    "FROM PRODUCT " +
                    "WHERE allowed_gender LIKE \"%:gender%\" AND " +
                    ":age BETWEEN allowed_minimum_age AND allowed_maximum_age AND " +
                    "((SELECT COUNT(*) FROM product_allowed_jobs WHERE product_id = id AND allowed_jobs_id IN :jobs) > 0 OR " +
                    "(SELECT COUNT(*) FROM product_banned_jobs WHERE product_id = id AND banned_jobs_id IN :jobs) = 0) AND " +
                    ":annualIncome BETWEEN allowed_minimum_annual_income AND allowed_maximum_annual_income OR " +
                    "expose_to_no_qualify AND " +
                    "name LIKE \"%:productName%\" AND " +
                    "cost BETWEEN :minimumCost AND :maximumCost AND " +
                    "(SELECT COUNT(*) FROM product_tags, tag WHERE id = product_id AND tags_id = tag.id AND \"%\" + tag.name + \"%\" IN :tags) >= :numberOfTags " +
                    "ORDER BY (SELECT COUNT(*) FROM review WHERE product_id = id AND purchase_result = \"SUCCESS\") DESC;"
            , nativeQuery = true)
    Page<Product> findWithNameAndMinimumCostAndMaximumCostAndTagsOrderByReviewDesc(
            @Param("gender")String gender,
            @Param("age")Integer age,
            @Param("jobs")List<Long> jobs,
            @Param("annualIncome")BigDecimal annualIncome,
            @Param("productName")String productName,
            @Param("minimumCost")BigDecimal minimumCost,
            @Param("maximumCost")BigDecimal maximumCost,
            @Param("tags")Set<String> tags,
            @Param("numberOfTags")Integer numberOfTags,
            Pageable pageable);

}
