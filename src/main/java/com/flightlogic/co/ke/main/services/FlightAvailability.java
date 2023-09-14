/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flightlogic.co.ke.main.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flightlogic.co.ke.main.controllers.FlightLogicController;
import com.flightlogic.co.ke.main.utilities.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.text.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author jgithu
 */
public class FlightAvailability {

    private final Logger loggger = LogManager.getLogger(FlightAvailability.class);
    Utils utility = new Utils();

    public JsonObject flightAvailabilitySearch(String request, String url, JsonObject credentials) {
        JsonObject responseObject = new JsonObject();

        try {

            JsonObject requestObject = JsonParser.parseString(request).getAsJsonObject();
            System.out.print("INCOMING__REQUEST" + requestObject);
            String JourneyType = requestObject.get("journeyType").getAsString();
            JsonObject flightRequestObject = new JsonObject();
            //form request here 

            flightRequestObject.addProperty("user_id", credentials.get("user_id").getAsString());
            flightRequestObject.addProperty("user_password", credentials.get("user_password").getAsString());
            flightRequestObject.addProperty("access", credentials.get("access").getAsString());
            flightRequestObject.addProperty("ip_address", credentials.get("ip_address").getAsString());
            flightRequestObject.addProperty("requiredCurrency", "USD");
            flightRequestObject.addProperty("journeyType", requestObject.get("journeyType").getAsString());
            JsonArray OriginDestinationInfo = new JsonArray();
            JsonObject OriginDestinationObject = new JsonObject();
            OriginDestinationObject.addProperty("departureDate", requestObject.get("departureDate").getAsString());
            if (JourneyType.equalsIgnoreCase("Return")) {
                OriginDestinationObject.addProperty("returnDate", requestObject.get("returnDate").getAsString());
            }
            OriginDestinationObject.addProperty("airportOriginCode", requestObject.get("airportOriginCode").getAsString());
            OriginDestinationObject.addProperty("airportDestinationCode", requestObject.get("airportDestinationCode").getAsString());
            OriginDestinationInfo.add(OriginDestinationObject);
            flightRequestObject.add("OriginDestinationInfo", OriginDestinationInfo.getAsJsonArray());
            flightRequestObject.addProperty("class", requestObject.get("class").getAsString());
            flightRequestObject.addProperty("airlineCode", requestObject.get("airlineCode").getAsString());
            flightRequestObject.addProperty("adults", requestObject.get("adults").getAsInt());
            flightRequestObject.addProperty("childs", requestObject.get("childs").getAsInt());
            flightRequestObject.addProperty("infants", requestObject.get("infants").getAsInt());
            loggger.info("FLIGHT AVAILABILITY REQUEST |  URL " + url + "  REQ " + flightRequestObject);

            responseObject = utility.flightLogicRequest(flightRequestObject, url);
            loggger.info("FLIGHT AVAILABILITY RESPONSE  |  " + responseObject);
//{"Errors":{"ErrorCode":"FLSEARCHVAL","ErrorMessage":"departureDate can not be earlier that today."}}
            if (responseObject.has("Errors")) {
                // responseObject.addProperty("status", "01");
                //responseObject.addProperty("message", responseObject.get("Errors").getAsJsonObject().get("ErrorMessage").getAsString());
                loggger.info("IKO NA ERROR");
                return responseObject;
                //  responseObject.add("response", responseObject);

            } else {

                JsonObject response = JsonParser.parseString(responseObject.toString()).getAsJsonObject();
                if (response.has("AirSearchResponse")) {
                    JsonObject airSearchResponse = response.getAsJsonObject("AirSearchResponse");
                    if (airSearchResponse.has("AirSearchResult")) {
                        JsonObject airSearchResult = airSearchResponse.getAsJsonObject("AirSearchResult");
                        if (airSearchResult.has("FareItineraries")) {
                            JsonArray fareItineraries = airSearchResult.getAsJsonArray("FareItineraries");
                            for (int i = 0; i < fareItineraries.size(); i++) {
                                JsonObject fareItinerary = fareItineraries.get(i).getAsJsonObject().getAsJsonObject("FareItinerary");
                                if (fareItinerary.has("AirItineraryFareInfo")) {
                                    JsonObject airItineraryFareInfo = fareItinerary.getAsJsonObject("AirItineraryFareInfo");
                                    airItineraryFareInfo.remove("FareBreakdown");
                                    if (airItineraryFareInfo.has("ItinTotalFares")) {
                                        JsonObject itinTotalFares = airItineraryFareInfo.getAsJsonObject("ItinTotalFares");
                                        if (itinTotalFares.has("BaseFare")) {
                                            itinTotalFares.remove("BaseFare");
                                        }
                                        if (itinTotalFares.has("EquivFare")) {
                                            itinTotalFares.remove("EquivFare");
                                        }
                                        if (itinTotalFares.has("ServiceTax")) {
                                            itinTotalFares.remove("ServiceTax");
                                        }
                                        if (itinTotalFares.has("TotalTax")) {
                                            itinTotalFares.remove("TotalTax");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

//                String modifiedJson = objectMapper.writeValueAsString(rootNode);
//                System.out.println(modifiedJson);
//
//                loggger.info("Iko hapa" + modifiedJson);
                responseObject = response;//(JsonObject) JsonParser.parseString(modifiedJson);

                //responseObject.addProperty("status", "01");
                //responseObject.addProperty("message", "Successful");
                // responseObject.add("response", responseObject);
            }
        } catch (IOException | ParseException ex) {
            loggger.info("Exception  |  " + ex.getMessage());
            //responseObject.addProperty("status", "01");
            //esponseObject.addProperty("message", ex.getMessage());
            //  responseObject.addProperty("response", "");

        }

        return responseObject;

    }

}
