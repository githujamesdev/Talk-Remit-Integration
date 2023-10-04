/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flightlogic.co.ke.main.services;

import com.flightlogic.co.ke.main.utilities.Utils;
import com.google.gson.JsonArray;
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
public class AirlineList {
    
    
    private final Logger loggger = LogManager.getLogger(FlightAvailability.class);
    Utils utility = new Utils();

    public JsonArray airlineList(String request, String url, JsonObject credentials) {
        JsonArray responseObject = new JsonArray();

        try {

            //JsonObject incomingRequest = new JsonObject(request);
            JsonObject requestObject = JsonParser.parseString(request).getAsJsonObject();
            //  System.out.print("JSON REQUEST" + request);
            JsonObject flightRequestObject = new JsonObject();
            //form request here 

            flightRequestObject.addProperty("user_id", credentials.get("user_id").getAsString());
            flightRequestObject.addProperty("user_password", credentials.get("user_password").getAsString());
            flightRequestObject.addProperty("access", credentials.get("access").getAsString());
            flightRequestObject.addProperty("ip_address", credentials.get("ip_address").getAsString());
            loggger.info("AIRLINE LIST REQUEST |  URL " + url + "  REQ " + flightRequestObject);
            responseObject = utility.airportRequest(flightRequestObject, url);
            //loggger.info("AIRLINE LIST RESPONSE |  URL " + url + "  REQ " + responseObject);

        } catch (IOException | ParseException ex) {
            loggger.info("Exception  |  " + ex.getMessage());
            //responseObject.addProperty("status", "01");
        }
        return responseObject;

    }
}
