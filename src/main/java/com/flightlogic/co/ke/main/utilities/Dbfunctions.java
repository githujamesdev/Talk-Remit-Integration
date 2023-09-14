/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flightlogic.co.ke.main.utilities;

import com.flightlogic.co.ke.main.repository.BrandsRepository;
import java.util.List;
import java.util.Map;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author jgithu
 */
@Component
public class Dbfunctions {

    private final BrandsRepository brandsRepository;

    @Autowired
    public Dbfunctions(BrandsRepository brandsRepository) {
        this.brandsRepository = brandsRepository;
    }

//    public Dbfunctions() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    
    public JSONObject checkBundleStatus(String bundleType) {
        // Call the repository method to get bundle details
        List<Map<String, Object>> bundleDetails = brandsRepository.getBundleDetails(bundleType);

        JSONObject bundleSelfObject = new JSONObject();
        JSONObject bundleObject = new JSONObject();
        JSONObject bundleOtherObject = new JSONObject();

        bundleDetails.stream().map(row -> {
            String status = row.get("status").toString();
            if ("SELF".equalsIgnoreCase(status)) {
                //set the details in an object
                bundleSelfObject.put("brandId", row.get("brandId").toString());
                bundleSelfObject.put("pricePlan", row.get("pricePlan").toString());
                bundleSelfObject.put("amount", row.get("amount").toString());
            } else if ("OTHER".equalsIgnoreCase(status)) {
                bundleOtherObject.put("brandId", row.get("brandId").toString());
                bundleOtherObject.put("pricePlan", row.get("pricePlan").toString());
                bundleOtherObject.put("amount", row.get("amount").toString());
            }
            return row;
        }).map(_item -> {
            bundleObject.put("Self", bundleSelfObject);
            return _item;
        }).forEachOrdered(_item -> {
            bundleObject.put("Other", bundleOtherObject);
        });
        return bundleObject;

    }
}
