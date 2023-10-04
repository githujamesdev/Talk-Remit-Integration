/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flightlogic.co.ke.main.controllers;

import com.flightlogic.co.ke.main.entities.Nation;
import com.flightlogic.co.ke.main.repository.NationRepository;
import com.flightlogic.co.ke.main.services.AirlineList;
import com.flightlogic.co.ke.main.services.AirportList;
import com.flightlogic.co.ke.main.services.FareRules;
import com.flightlogic.co.ke.main.services.FlightAvailability;
import com.flightlogic.co.ke.main.services.ValidateFare;
import com.flightlogic.co.ke.main.services.BookFlight;
import com.flightlogic.co.ke.main.services.OrderTicket;
import com.flightlogic.co.ke.main.services.TripDetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.flightlogic.co.ke.main.utilities.Dbfunctions;
import com.flightlogic.co.ke.main.utilities.Httpcall;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * // * @author jgithu
 */
@CrossOrigin(origins = {"*"}, maxAge = 3600L)
@RestController
@RequestMapping({"/"})
public class FlightLogicController {

    private final Logger loggger = LogManager.getLogger(FlightLogicController.class);

    private final NationRepository nationRepository;

    public String httpURL = "";
    public String brandId = "";
    public String pricePlan = "";
    public String amount = "";
    public String rechargeMsisdn = "";
    Httpcall utils = new Httpcall();
    FlightAvailability flight = new FlightAvailability();
    ValidateFare fare = new ValidateFare();
    FareRules farerule = new FareRules();
    AirportList airport = new AirportList();
    BookFlight bookflight = new BookFlight();
    OrderTicket order = new OrderTicket();
    TripDetails trip = new TripDetails();
    AirlineList airline = new AirlineList();

    @Autowired
    private Dbfunctions dbfunctions;

    @Value("${flight.user_id}")
    private String user_id;

    @Value("${flight.user_password}")
    private String user_password;

    @Value("${flight.access}")
    private String access;

    @Value("${flight.ip_address}")
    private String ip_address;
    //URLS
    @Value("${flight.post_url}")
    private String post_url;

    @Value("${flight.validate_fare}")
    String validate_fare;

    @Value("${flight.fare_rule}")
    String fare_rule;

    @Value("${flight.book_flight}")
    String book_flight;

    @Value("${flight.order_ticket}")
    String order_ticket;

    @Value("${flight.trip_details}")
    String trip_details;

    @Value("${flight.cancel_trip}")
    String cancel_trip;

    @Value("${flight.airport_list}")
    String airport_list;

    @Value("${flight.booking_note}")
    String booking_note;

    @Value("${flight.airline_list}")
    String airline_list;
    @Value("${flight.post_ticket_status}")
    String post_ticket_status;

    @Value("${flight.void_quote}")
    String void_quote;

    @Autowired
    public FlightLogicController(NationRepository nationRepository) {
        this.nationRepository = nationRepository;
    }

    @GetMapping("hello")
    public String hello() {
        return "Post URL: " + post_url + " " + user_id;
    }

    @PostMapping("process")
    public String sendRequest(@RequestBody String request) {
        JsonObject response = new JsonObject();
        JsonObject credentials = new JsonObject();
        JsonArray airports = new JsonArray();
        JsonArray airlines = new JsonArray();

        try {
            credentials.addProperty("user_id", user_id);
            credentials.addProperty("user_password", user_password);
            credentials.addProperty("access", access);
            credentials.addProperty("ip_address", ip_address);
            JsonObject IncommingDetails = JsonParser.parseString(request).getAsJsonObject();
            loggger.info("----------INCOMING REQUEST  ----");
            loggger.info("REQUEST  |  " + request);
            String action = IncommingDetails.get("operation").getAsString();

            switch (action) {
                case "FlightAvailability":
                    response = flight.flightAvailabilitySearch(request, post_url, credentials);
                    break;
                case "ValidateFare":
                    response = fare.validateFare(request, validate_fare, credentials);
                    break;
                case "FareRules":
                    response = farerule.fareRules(request, fare_rule, credentials);
                    break;
                case "AirportList":
                    airports = airport.airportList(request, airport_list, credentials);
                    response.add("airportlist", airports);
                    break;
                case "BookFlight":
                    response = bookflight.bookFlights(request, book_flight, credentials);
                    break;
                case "OrderTicket":
                    response = order.ticketOrder(request, order_ticket, credentials);
                    break;
                case "TripDetails":
                    response = trip.tripDetails(request, trip_details, credentials);
                    break;
                case "CancelTrip":
                    response = trip.cancelTrip(request, cancel_trip, credentials);
                    break;
                case "BookingNote":
                    response = trip.bookingNote(request, booking_note, credentials);
                    break;
                case "AirlineList":
                    airlines = airline.airlineList(request, airline_list, credentials);
                    response.add("airlines", airlines);
                    break;
                case "PostTicketStatus":
                    response = trip.postTicketStatus(request, post_ticket_status, credentials);
                    break;
                case "VoidQuote":
                    response = trip.postTicketStatus(request, post_ticket_status, credentials);
                    break;
                case "NationalCodes":
                    // Retrieve airport data from the database
                    List<Nation> nationData = nationRepository.findAll();
                    JsonArray nationList = new JsonArray();
                    for (Nation nation : nationData) {
                        JsonObject airportJson = new JsonObject();
                        airportJson.addProperty("nationcode", nation.getNationCode());
                        airportJson.addProperty("nationname", nation.getNationName());
                        nationList.add(airportJson);
                    }

                    response.add("nations", nationList);
                    break;
            }

        } catch (JsonSyntaxException ex) {
            loggger.error("Exception  |  " + ex.getMessage());
            response.addProperty("status", "01");
            response.addProperty("message", "An error occured while proccessing your request please try again later.");

        }
        return response.toString();

    }

    @PostMapping("send-request")
    public String vendorRequest(@RequestBody String request) {
        JsonObject response = new JsonObject();
        JsonObject credentials = new JsonObject();
        JsonArray airports = new JsonArray();
        JsonArray airlines = new JsonArray();
        try {
            credentials.addProperty("user_id", user_id);
            credentials.addProperty("user_password", user_password);
            credentials.addProperty("access", access);
            credentials.addProperty("ip_address", ip_address);
            JsonObject IncommingDetails = JsonParser.parseString(request).getAsJsonObject();
            loggger.info("----------INCOMING REQUEST  ----");
            loggger.info("REQUEST  |  " + request);
            String action = IncommingDetails.get("operation").getAsString();

            switch (action) {
                case "FlightAvailability":
                    response = flight.flightAvailabilitySearch(request, post_url, credentials);
                    break;
                case "ValidateFare":
                    response = fare.validateFare(request, validate_fare, credentials);
                    break;
                case "FareRules":
                    response = farerule.fareRules(request, fare_rule, credentials);
                    break;
                case "AirportList":
                    airports = airport.airportList(request, airport_list, credentials);
                    response.add("airportlist", airports);
                    break;
                case "BookFlight":
                    response = bookflight.bookFlights(request, book_flight, credentials);
                    break;
                case "OrderTicket":
                    response = order.ticketOrder(request, order_ticket, credentials);
                    break;
                case "TripDetails":
                    response = trip.tripDetails(request, trip_details, credentials);
                    break;
                case "CancelTrip":
                    response = trip.cancelTrip(request, cancel_trip, credentials);
                    break;
                case "BookingNote":
                    response = trip.cancelTrip(request, booking_note, credentials);
                    break;
                case "AirlineList":
                    airlines = airline.airlineList(request, airline_list, credentials);
                    response.add("airlines", airlines);
                    break;
                case "PostTicketStatus":
                    response = trip.postTicketStatus(request, post_ticket_status, credentials);
                    break;
                case "VoidQuote":
                    response = trip.postTicketStatus(request, post_ticket_status, credentials);
                    break;

            }

        } catch (JsonSyntaxException ex) {
            loggger.error("Exception  |  " + ex.getMessage());
            response.addProperty("status", "01");
            response.addProperty("message", "An error occured while proccessing your request please try again later.");

        }

        return response.toString();

    }

}
