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
public class OrderTicket {

    private final Logger loggger = LogManager.getLogger(FlightAvailability.class);
    Utils utility = new Utils();

    public JsonObject ticketOrder(String request, String url, JsonObject credentials) {
        JsonObject responseObject = new JsonObject();

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
            flightRequestObject.addProperty("UniqueID", requestObject.get("UniqueID").getAsString());

            loggger.info("ORDER TICKET REQUEST |  URL " + url + "  REQ " + flightRequestObject);

            responseObject = utility.flightLogicRequest(flightRequestObject, url);
            loggger.info("ORDER TICKET RESPONSE  |  " + responseObject);

            if (responseObject.has("errors")) {
                responseObject.addProperty("status", "01");
                responseObject.addProperty("message", "An error occured while processing your request. Please try again later");

                return responseObject;
            }

        } catch (IOException | ParseException ex) {
            loggger.info("Exception  |  " + ex.getMessage());

        }

        return responseObject;

    }

}
