/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flightlogic.co.ke.main.services;

import com.flightlogic.co.ke.main.utilities.Utils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.text.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author jgithu
 */
public class FareRules {

    private final Logger loggger = LogManager.getLogger(FlightAvailability.class);
    Utils utility = new Utils();

    public JsonObject fareRules(String request, String url, JsonObject credentials) {
        JsonObject responseObject = new JsonObject();

        try {

            JsonObject requestObject = JsonParser.parseString(request).getAsJsonObject();
            JsonObject flightRequestObject = new JsonObject();
            //form request here 
            flightRequestObject.addProperty("session_id", requestObject.get("session_id").getAsString());
            flightRequestObject.addProperty("fare_source_code", requestObject.get("fare_source_code").getAsString());
            loggger.info(" FARE RULE REQUEST |  URL " + url + "  REQ " + flightRequestObject);
            responseObject = utility.flightLogicRequest(flightRequestObject, url);
            loggger.info("FARE RULE RESPONSE  |  " + responseObject);

            if (responseObject.has("errors")) {

                return responseObject;
            }

        } catch (IOException | ParseException ex) {
            loggger.info("Exception  |  " + ex.getMessage());

        }

        return responseObject;

    }
}
