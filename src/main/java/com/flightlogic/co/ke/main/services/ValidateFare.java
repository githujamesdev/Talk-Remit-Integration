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
public class ValidateFare {

    private final Logger loggger = LogManager.getLogger(FlightAvailability.class);
    Utils utility = new Utils();

    public JsonObject validateFare(String request, String url, JsonObject credentials) {
        JsonObject responseObject = new JsonObject();

        try {

            JsonObject requestObject = JsonParser.parseString(request).getAsJsonObject();

            JsonObject flightRequestObject = new JsonObject();
            //form request here 
            flightRequestObject.addProperty("session_id", requestObject.get("session_id").getAsString());
            flightRequestObject.addProperty("fare_source_code", requestObject.get("fare_source_code").getAsString());
            loggger.info("VALIDATE FARE  REQUEST |  URL " + url + "  REQ " + flightRequestObject);
            responseObject = utility.flightLogicRequest(flightRequestObject, url);
            loggger.info("VALIDATE FARE RESPONSE  |  " + responseObject);

            if (responseObject.has("Errors")) {

                return responseObject;
            } else {
                JsonObject response = JsonParser.parseString(responseObject.toString()).getAsJsonObject();
                if (response.has("AirRevalidateResponse")) {
                    JsonObject airRevalidateResponse = response.getAsJsonObject("AirRevalidateResponse");
                    if (airRevalidateResponse.has("AirRevalidateResult")) {
                        JsonObject airRevalidateResult = airRevalidateResponse.getAsJsonObject("AirRevalidateResult");
                        if (airRevalidateResult.has("FareItineraries")) {
                            JsonObject fareItineraries = airRevalidateResult.getAsJsonObject("FareItineraries");
                            if (fareItineraries.has("FareItinerary")) {
                                JsonObject fareItinerary = fareItineraries.getAsJsonObject("FareItinerary");
                                if (fareItinerary.has("AirItineraryFareInfo")) {
                                    JsonObject airItineraryFareInfo = fareItinerary.getAsJsonObject("AirItineraryFareInfo");
                                    if (airItineraryFareInfo.has("FareBreakdown")) {
                                        airItineraryFareInfo.remove("FareBreakdown");
                                    }
                                    if (airItineraryFareInfo.has("ItinTotalFares")) {
                                        JsonObject itinTotalFares = airItineraryFareInfo.getAsJsonObject("ItinTotalFares");
                                        itinTotalFares.remove("BaseFare");
                                        itinTotalFares.remove("EquivFare");
                                        itinTotalFares.remove("ServiceTax");
                                        itinTotalFares.remove("TotalTax");
                                    }
                                }
                            }
                        }
                    }
                }

                responseObject = response;
            }

        } catch (IOException | ParseException ex) {
            loggger.info("Exception  |  " + ex.getMessage());
        }

        return responseObject;

    }

}
