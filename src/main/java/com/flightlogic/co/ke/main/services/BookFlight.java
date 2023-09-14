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
import com.google.gson.JsonElement;
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
public class BookFlight {

    private final Logger loggger = LogManager.getLogger(FlightAvailability.class);
    Utils utility = new Utils();

    public JsonObject bookFlights(String request, String url, JsonObject credentials) {
        JsonObject responseObject = new JsonObject();

        try {
            JsonObject requestObject = JsonParser.parseString(request).getAsJsonObject();

            loggger.info("BOOK FLIGHT REQUEST " + requestObject);
            // Create the main request JSON object
            JsonObject requestJson = new JsonObject();

            JsonObject flightBookingInfo = new JsonObject();
            flightBookingInfo.addProperty("flight_session_id", requestObject.getAsJsonObject("flightBookingInfo").get("flight_session_id").getAsString());
            flightBookingInfo.addProperty("fare_source_code", requestObject.getAsJsonObject("flightBookingInfo").get("fare_source_code").getAsString());
            flightBookingInfo.addProperty("IsPassportMandatory", requestObject.getAsJsonObject("flightBookingInfo").get("IsPassportMandatory").getAsString());
            flightBookingInfo.addProperty("fareType", requestObject.getAsJsonObject("flightBookingInfo").get("fareType").getAsString());
            flightBookingInfo.addProperty("areaCode", requestObject.getAsJsonObject("flightBookingInfo").get("areaCode").getAsString());
            flightBookingInfo.addProperty("countryCode", requestObject.getAsJsonObject("flightBookingInfo").get("countryCode").getAsString());

            // Copy flightBookingInfo as-is from customerRequest
            requestJson.addProperty("operation", "BookFlight");
            requestJson.add("flightBookingInfo", flightBookingInfo);

            // Create the paxInfo JSON object
            JsonObject paxInfo = new JsonObject();
            paxInfo.addProperty("clientRef", requestObject.getAsJsonObject("paxInfo").get("clientRef").getAsString());
            paxInfo.addProperty("postCode", requestObject.getAsJsonObject("paxInfo").get("postCode").getAsString());
            paxInfo.addProperty("customerEmail", requestObject.getAsJsonObject("paxInfo").get("customerEmail").getAsString());
            paxInfo.addProperty("customerPhone", requestObject.getAsJsonObject("paxInfo").get("customerPhone").getAsString());
            paxInfo.addProperty("bookingNote", requestObject.getAsJsonObject("paxInfo").get("bookingNote").getAsString());

            // Create the paxDetails JSON object for adults
            //JsonObject paxDetails = new JsonObject();
            JsonArray paxDetails = new JsonArray();
            JsonObject adultDetails = new JsonObject();
            JsonObject adultObjectDetails = new JsonObject();
            JsonObject childDetails = new JsonObject();
            JsonObject childObjectDetails = new JsonObject();
            JsonObject infantDetails = new JsonObject();
            JsonObject infantObjectDetails = new JsonObject();

            // Check if the "paxDetails" array is present for adults
            if (requestObject.getAsJsonObject("paxInfo").has("paxDetails")) {
                JsonArray adultArray = requestObject.getAsJsonObject("paxInfo").getAsJsonObject("paxDetails").getAsJsonArray("adult");
                JsonArray childArray = requestObject.getAsJsonObject("paxInfo").getAsJsonObject("paxDetails").getAsJsonArray("child");
                JsonArray infantArray = requestObject.getAsJsonObject("paxInfo").getAsJsonObject("paxDetails").getAsJsonArray("infant");

                if (!adultArray.isJsonNull()) {
                    JsonArray titleArray = new JsonArray();
                    JsonArray firstNameArray = new JsonArray();
                    JsonArray lastNameArray = new JsonArray();
                    JsonArray dobArray = new JsonArray();
                    JsonArray nationalityArray = new JsonArray();
                    JsonArray passportNoArray = new JsonArray();
                    JsonArray passportIssueCountryArray = new JsonArray();
                    JsonArray passportExpiryDateArray = new JsonArray();

                    for (JsonElement adult : adultArray) {
                        JsonObject adultObject = adult.getAsJsonObject();

                        titleArray.add(adultObject.get("title").getAsString());
                        firstNameArray.add(adultObject.get("firstName").getAsString());
                        lastNameArray.add(adultObject.get("lastName").getAsString());
                        dobArray.add(adultObject.get("dob").getAsString());
                        nationalityArray.add(adultObject.get("nationality").getAsString());
                        passportNoArray.add(adultObject.get("passportNo").getAsString());
                        passportIssueCountryArray.add(adultObject.get("passportIssueCountry").getAsString());
                        passportExpiryDateArray.add(adultObject.get("passportExpiryDate").getAsString());

                    }

                    adultDetails.add("title", titleArray);
                    adultDetails.add("firstName", firstNameArray);
                    adultDetails.add("lastName", lastNameArray);
                    adultDetails.add("dob", dobArray);
                    adultDetails.add("nationality", nationalityArray);
                    adultDetails.add("passportNo", passportNoArray);
                    adultDetails.add("passportIssueCountry", passportIssueCountryArray);
                    adultDetails.add("passportExpiryDate", passportExpiryDateArray);
                    adultObjectDetails.add("adult", adultDetails);
                    // adultDetails
                    // paxDetails.add(adultObjectDetails);
                }

                if (!childArray.isJsonNull()) {
                    JsonArray childtitleArray = new JsonArray();
                    JsonArray childfirstNameArray = new JsonArray();
                    JsonArray childlastNameArray = new JsonArray();
                    JsonArray childdobArray = new JsonArray();
                    JsonArray childnationalityArray = new JsonArray();
                    JsonArray childpassportNoArray = new JsonArray();
                    JsonArray childpassportIssueCountryArray = new JsonArray();
                    JsonArray childpassportExpiryDateArray = new JsonArray();

                    for (JsonElement child : childArray) {
                        JsonObject childObject = child.getAsJsonObject();

                        childtitleArray.add(childObject.get("title").getAsString());
                        childfirstNameArray.add(childObject.get("firstName").getAsString());
                        childlastNameArray.add(childObject.get("lastName").getAsString());
                        childdobArray.add(childObject.get("dob").getAsString());
                        childnationalityArray.add(childObject.get("nationality").getAsString());
                        childpassportNoArray.add(childObject.get("passportNo").getAsString());
                        childpassportIssueCountryArray.add(childObject.get("passportIssueCountry").getAsString());
                        childpassportExpiryDateArray.add(childObject.get("passportExpiryDate").getAsString());

                    }

                    childDetails.add("title", childtitleArray);
                    childDetails.add("firstName", childfirstNameArray);
                    childDetails.add("lastName", childlastNameArray);
                    childDetails.add("dob", childdobArray);
                    childDetails.add("nationality", childnationalityArray);
                    childDetails.add("passportNo", childpassportNoArray);
                    childDetails.add("passportIssueCountry", childpassportIssueCountryArray);
                    childDetails.add("passportExpiryDate", childpassportExpiryDateArray);
                    adultObjectDetails.add("child", childDetails);
                    //paxDetails.add(adultObjectDetails);
                }

                if (!infantArray.isJsonNull()) {
                    JsonArray infanttitleArray = new JsonArray();
                    JsonArray infantfirstNameArray = new JsonArray();
                    JsonArray infantlastNameArray = new JsonArray();
                    JsonArray infantdobArray = new JsonArray();
                    JsonArray infantnationalityArray = new JsonArray();
                    JsonArray infantpassportNoArray = new JsonArray();
                    JsonArray infantpassportIssueCountryArray = new JsonArray();
                    JsonArray infantpassportExpiryDateArray = new JsonArray();

                    for (JsonElement infant : infantArray) {
                        JsonObject infantObject = infant.getAsJsonObject();

                        infanttitleArray.add(infantObject.get("title").getAsString());
                        infantfirstNameArray.add(infantObject.get("firstName").getAsString());
                        infantlastNameArray.add(infantObject.get("lastName").getAsString());
                        infantdobArray.add(infantObject.get("dob").getAsString());
                        infantnationalityArray.add(infantObject.get("nationality").getAsString());
                        infantpassportNoArray.add(infantObject.get("passportNo").getAsString());
                        infantpassportIssueCountryArray.add(infantObject.get("passportIssueCountry").getAsString());
                        infantpassportExpiryDateArray.add(infantObject.get("passportExpiryDate").getAsString());

                    }

                    infantDetails.add("title", infanttitleArray);
                    infantDetails.add("firstName", infantfirstNameArray);
                    infantDetails.add("lastName", infantlastNameArray);
                    infantDetails.add("dob", infantdobArray);
                    infantDetails.add("nationality", infantnationalityArray);
                    infantDetails.add("passportNo", infantpassportNoArray);
                    infantDetails.add("passportIssueCountry", infantpassportIssueCountryArray);
                    infantDetails.add("passportExpiryDate", infantpassportExpiryDateArray);
                    adultObjectDetails.add("infant", infantDetails);
                    //paxDetails.add(infantObjectDetails);
                }
            }
            paxDetails.add(adultObjectDetails);

            // Add paxDetails to paxInfo
            paxInfo.add("paxDetails", paxDetails);

            // Add paxInfo to the main request JSON
            requestJson.add("paxInfo", paxInfo);

            // Add the paxDetails array to paxInfo
            // Add paxInfo to the main request JSON
            loggger.info("BOOK FLIGHT REQUEST MODIFIED  |  " + requestJson);
            responseObject = utility.flightLogicRequest(requestJson, url);
            loggger.info("BOOK FLIGHT RESPONSE  |  " + responseObject);
            if (responseObject.has("errors")) {

                return responseObject;
            }

        } catch (IOException | ParseException ex) {

            loggger.info("EXCEPTION  |  " + ex.getMessage());
        }

        return responseObject;

    }

}
