/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flightlogic.co.ke.main.repository;

import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jgithu
 */
@Repository
public class BrandsRepository {

    private final JdbcTemplate jdbcTemplate;

    public BrandsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> getBundleDetails(String bundleType) {
        String query = "SELECT productName, brandId, pricePlan, amount,status  FROM tb_bundle_brands WHERE  productName = ?";

        return jdbcTemplate.queryForList(query, bundleType);
    }

}
