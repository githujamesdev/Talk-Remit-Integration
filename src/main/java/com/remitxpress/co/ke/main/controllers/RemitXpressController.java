/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.remitxpress.co.ke.main.controllers;

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
//import com.flightlogic.co.ke.main.utilities.Dbfunctions;
import com.remitxpress.co.ke.main.utilities.Httpcall;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import com.remitxpress.co.ke.main.repository.partnerIpAddressIpRepo;
import com.remitxpress.co.ke.main.services.DatabaseOps;
import com.remitxpress.co.ke.main.services.TokenService;
import com.remitxpress.co.ke.main.utilities.Utils;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javax.servlet.http.HttpServletRequest;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * // * @author jgithu
 */
@CrossOrigin(origins = "*", maxAge = 3600L)
@RestController
@RequestMapping("/")
public class RemitXpressController {

    private final Logger logger = LogManager.getLogger(RemitXpressController.class);

    private final TokenService tokenService;
    private final DatabaseOps databaseOps;
    private final Utils util;

    public String httpURL = "";
    public String brandId = "";
    public String pricePlan = "";
    public String amount = "";
    public String rechargeMsisdn = "";
    Httpcall httpcall = new Httpcall();
    //   Utils util = new Utils();

    @Value("${etswitch.token.url}")
    String switchtokenUrl;

    @Value("${etswitch.username}")
    String username;

    @Value("${etswitch.password}")
    String password;

    @Value("${etswitch.accountlookup.url}")
    String accountlookupURL;

    @Value("${etswitch.payment.url}")
    String paymentURL;

    @Autowired
    partnerIpAddressIpRepo ipAddressIpRepo;

    @Autowired
    public RemitXpressController(TokenService tokenService, DatabaseOps databaseOps, Utils util) {
        this.tokenService = tokenService;
        this.databaseOps = databaseOps;
        this.util = util;
    }

    @GetMapping("hello")
    public String hello() {
        return "Post URL";
    }

    @GetMapping("getCountries")
    @CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
    public String getCountries(@RequestHeader("Authorization") String authHeader, HttpServletRequest request) {
        logger.info("REQUEST getCountries  |  " + request.toString());
        JsonObject response = new JsonObject();

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.addProperty("response", "999");
            response.addProperty("responseDescription", "Missing or invalid Authorization header");
            return response.toString();
        }

        String token = authHeader.substring(7);
        Claims claims;
        try {
            claims = tokenService.getClaimsFromToken(token);
        } catch (RuntimeException e) {
            response.addProperty("response", "999");
            response.addProperty("responseDescription", "Invalid or expired token");
            return response.toString();
        }

        try {
            Map<String, Object> results = databaseOps.getCountries();
            int msgId = (int) results.get("MsgId");
            String resultMsg = (String) results.get("Result");
            JsonObject resultObj = JsonParser.parseString(resultMsg).getAsJsonObject();

            if (msgId == 0) {
                response.addProperty("response", "000");
                response.addProperty("responseDescription", "Success");

                response.add("countries", resultObj);
                //response.addProperty("Countries", resultMsg);

            } else {
                response.addProperty("response", "999");
                response.addProperty("responseDescription", "Failed");
                response.add("error", resultObj);

            }

        } catch (RuntimeException e) {

            response.addProperty("response", "999");
            response.addProperty("responseDescription", "Failed");
            response.addProperty("error", "Failed to fetch countries");
            logger.error("Failed to fetch countries", e);

        }
        return response.toString();
    }

    @GetMapping("getExchangeRate")
    public String getExchangeRate(@RequestParam Integer sourceCurrency,
            @RequestParam Integer destinationCurrency, @RequestHeader("Authorization") String authHeader) {
        logger.info("REQUEST   |  " + sourceCurrency + " " + destinationCurrency);
        JsonObject response = new JsonObject();
        //JsonObject incomingDetails = JsonParser.parseString(request).getAsJsonObject();

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.addProperty("response", "999");
            response.addProperty("responseDescription", "Missing or invalid Authorization header");
            return response.toString();
        }

        String token = authHeader.substring(7);
        Claims claims;
        try {
            claims = tokenService.getClaimsFromToken(token);
        } catch (RuntimeException e) {
            response.addProperty("response", "999");
            response.addProperty("responseDescription", "Invalid or expired token");
            return response.toString();
        }

        String partner = claims.getSubject();
        logger.info("Authenticated user: " + partner);

        if (!tokenService.validateToken(token)) {
            response.addProperty("response", "999");
            response.addProperty("responseDescription", "Invalid or expired token");
            return response.toString();
        }

        try {
//            Integer partnerID = null;
//            if (incomingDetails.has("partnerId")) {
//                partnerID = incomingDetails.get("partnerId").getAsInt();
//            }

            //get partner from Header 
            //  Integer SourceCurrency = incomingDetails.get("sourceCurrency").getAsInt();
            //Integer DestinationCurrency = incomingDetails.get("destinationCurrency").getAsInt();
            Map<String, Object> results = databaseOps.getExchangeRate(Integer.parseInt(partner), sourceCurrency, destinationCurrency);
            int msgId = (int) results.get("MsgId");
            String resultMsg = (String) results.get("Result");
            JsonObject resultObj = JsonParser.parseString(resultMsg).getAsJsonObject();

            if (msgId == 0) {
                response.addProperty("response", "000");
                response.addProperty("responseDescription", "Success");
                response.add("rate", resultObj);

            } else {
                response.addProperty("response", "999");
                response.addProperty("responseDescription", "Failed");
                response.add("error", resultObj);

            }

        } catch (RuntimeException e) {

            response.addProperty("response", "999");
            response.addProperty("responseDescription", "Failed");
            response.addProperty("error", "Failed to fetch Rates");
            logger.error("Failed to fetch Rates", e);

        }
        return response.toString();
    }

    @GetMapping("getAccountBalance")
    public String getAccountBalance(@RequestParam String accountNumber,
            @RequestHeader("Authorization") String authHeader) {
        logger.info("REQUEST getAccountBalance  |  " + accountNumber);
        JsonObject response = new JsonObject();
        //JsonObject incomingDetails = JsonParser.parseString(request).getAsJsonObject();

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.addProperty("response", "999");
            response.addProperty("responseDescription", "Missing or invalid Authorization header");
            return response.toString();
        }

        String token = authHeader.substring(7);
        Claims claims;
        try {
            claims = tokenService.getClaimsFromToken(token);
        } catch (RuntimeException e) {
            response.addProperty("response", "999");
            response.addProperty("responseDescription", "Invalid or expired token");
            return response.toString();
        }

        String username = claims.getSubject();
        logger.info("Authenticated user: " + username);
        try {
            // String accountNumber = incomingDetails.get("accountNumber").getAsString();
            Map<String, Object> results = databaseOps.getAccountBalance(accountNumber);
            int msgId = (int) results.get("MsgId");
            String resultMsg = (String) results.get("Result");
            //JsonObject resultObj = JsonParser.parseString(resultMsg).getAsJsonObject();

            if (msgId == 0) {

                String jsonArrayString = "[" + resultMsg + "]";
                JsonArray balanceArray = JsonParser.parseString(jsonArrayString).getAsJsonArray();

                response.addProperty("response", "000");
                response.addProperty("responseDescription", "Success");
                response.add("balance", balanceArray);

            } else {
                response.addProperty("response", "999");
                response.addProperty("responseDescription", "Failed");
                response.addProperty("error", resultMsg);

            }

        } catch (RuntimeException e) {

            response.addProperty("response", "999");
            response.addProperty("responseDescription", "Failed");
            response.addProperty("error", "Failed to fetch Account Balance");
            logger.error("Failed to fetch Account Balanc", e);

        }
        return response.toString();
    }

    @PostMapping("getTransactionStatus")
    public String getTransactionStatus(@RequestBody String request, @RequestHeader("Authorization") String authHeader) {
        logger.info("REQUEST getTransactionStatus  |  " + request);
        JsonObject response = new JsonObject();
        JsonObject incomingDetails = JsonParser.parseString(request).getAsJsonObject();

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.addProperty("response", "999");
            response.addProperty("responseDescription", "Missing or invalid Authorization header");
            return response.toString();
        }

        String token = authHeader.substring(7);
        Claims claims;
        try {
            claims = tokenService.getClaimsFromToken(token);
        } catch (RuntimeException e) {
            response.addProperty("response", "999");
            response.addProperty("responseDescription", "Invalid or expired token");
            return response.toString();
        }

        String PartnerId = claims.getSubject();
        logger.info("Authenticated user: " + PartnerId);
        try {
            String transactionReference = "";
            String partnerReference = "";
            if (incomingDetails.has("transactionReference")) {
                transactionReference = incomingDetails.get("transactionReference").getAsString();
            }
            if (incomingDetails.has("partnerReference")) {
                partnerReference = incomingDetails.get("partnerReference").getAsString();
            }

//            String transactionReference = incomingDetails.get("transactionReference").getAsString();
//            String partnerReference = incomingDetails.get("partnerReference").getAsString();
            Map<String, Object> results = databaseOps.GetTransactionStatus(PartnerId, transactionReference, partnerReference);
            int msgId = (int) results.get("msgId");
            String resultMsg = (String) results.get("result");
            JsonObject resultObj = JsonParser.parseString(resultMsg).getAsJsonObject();

            logger.info("GET MESSAGE ID   |  " + msgId);
            logger.info("GET STATUS   |  " + resultObj);
            if (msgId == 0) {

                //String jsonArrayString = "[" + resultMsg + "]";
                //  JsonArray balanceArray = JsonParser.parseString(resultMsg).getAsJsonArray();
                response.addProperty("response", "000");
                response.addProperty("responseDescription", "Success");
                response.add("transactionDetails", resultObj);

            } else {
                response.addProperty("response", "999");
                response.addProperty("responseDescription", "Failed");
                response.add("error", resultObj);

            }

        } catch (RuntimeException e) {

            response.addProperty("response", "999");
            response.addProperty("responseDescription", "Failed");
            response.addProperty("error", "Failed to fetch Transaction Status");
            logger.error("Failed to fetch Transaction Status", e);

        }
        return response.toString();
    }

    @GetMapping("getDeliveryMethod")
    public String getDeliveryMethod(@RequestParam Integer countryId, @RequestHeader("Authorization") String authHeader) {
        //  logger.info("REQUEST DeliveryMethod  |  " + request);
        JsonObject response = new JsonObject();
        // JsonObject incomingDetails = JsonParser.parseString(request).getAsJsonObject();

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.addProperty("response", "999");
            response.addProperty("responseDescription", "Missing or invalid Authorization header");
            return response.toString();
        }

        String token = authHeader.substring(7);
        Claims claims;
        try {
            claims = tokenService.getClaimsFromToken(token);
        } catch (RuntimeException e) {
            response.addProperty("response", "999");
            response.addProperty("responseDescription", "Invalid or expired token");
            return response.toString();
        }
        try {
            //Integer countryId = incomingDetails.get("CountryId").getAsInt();
            Map<String, Object> results = databaseOps.getDeliveryMethod(countryId);
            int msgId = (int) results.get("MsgId");
            String resultMsg = (String) results.get("Result");
            JsonObject resultObj = JsonParser.parseString(resultMsg).getAsJsonObject();
            JsonArray delivery = resultObj.getAsJsonArray("result");
            if (msgId == 0) {
                response.addProperty("response", "000");
                response.addProperty("responseDescription", "Success");
                response.add("deliveryMethod", delivery);

            } else {
                response.addProperty("response", "999");
                response.addProperty("responseDescription", "Failed");
                response.add("error", resultObj);

            }

        } catch (RuntimeException e) {

            response.addProperty("response", "999");
            response.addProperty("responseDescription", "Failed");
            response.addProperty("error", "Failed to fetch Delivery Methods");
            logger.error("Failed to fetch Delivery Method", e);

        }
        return response.toString();
    }

    @GetMapping("getDeliveryType")
    public String getDeliveryType(@RequestParam Integer countryId, @RequestParam Integer deliveryMethodId, @RequestHeader("Authorization") String authHeader) {
        //logger.info("REQUEST Delivery Type  |  " + request);
        JsonObject response = new JsonObject();
        // JsonObject incomingDetails = JsonParser.parseString(request).getAsJsonObject();

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.addProperty("response", "999");
            response.addProperty("responseDescription", "Missing or invalid Authorization header");
            return response.toString();
        }

        String token = authHeader.substring(7);
        Claims claims;
        try {
            claims = tokenService.getClaimsFromToken(token);
        } catch (RuntimeException e) {
            response.addProperty("response", "999");
            response.addProperty("responseDescription", "Invalid or expired token");
            return response.toString();
        }

        String username = claims.getSubject();
        logger.info("Authenticated user: " + username);
        try {
            // Integer CountryId = incomingDetails.get("countryId").getAsInt();
            // Integer DeliveryMethodId = incomingDetails.get("deliveryMethodId").getAsInt();
            Map<String, Object> results = databaseOps.getDeliveryType(countryId, deliveryMethodId);
            int msgId = (int) results.get("MsgId");
            String resultMsg = (String) results.get("Result");
            JsonObject resultObj = JsonParser.parseString(resultMsg).getAsJsonObject();

            if (msgId == 0) {
                response.addProperty("response", "000");
                response.addProperty("responseDescription", "Success");
                response.add("deliveryType", resultObj);

            } else {
                response.addProperty("response", "999");
                response.addProperty("responseDescription", "Failed");
                response.add("error", resultObj);

            }

        } catch (RuntimeException e) {

            response.addProperty("response", "999");
            response.addProperty("responseDescription", "Failed");
            response.addProperty("error", "Failed to fetch Delivery Types");
            logger.error("Failed to fetch Delivery Types", e);

        }
        return response.toString();
    }

    @GetMapping("getCurrencies")
    public String getCurrencies(@RequestHeader("Authorization") String authHeader, HttpServletRequest request) {
        logger.info("REQUEST getCurrencies  |  " + request.toString());
        JsonObject response = new JsonObject();

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.addProperty("response", "999");
            response.addProperty("responseDescription", "Missing or invalid Authorization header");
            return response.toString();
        }

        String token = authHeader.substring(7);
        Claims claims;
        try {
            claims = tokenService.getClaimsFromToken(token);
        } catch (RuntimeException e) {
            response.addProperty("response", "999");
            response.addProperty("responseDescription", "Invalid or expired token");
            return response.toString();
        }

        try {
            Map<String, Object> results = databaseOps.getCurrencies();
            int msgId = (int) results.get("MsgId");
            String resultMsg = (String) results.get("Result");
            JsonArray resultObj = JsonParser.parseString(resultMsg).getAsJsonArray();

            if (msgId == 0) {
                response.addProperty("response", "000");
                response.addProperty("responseDescription", "Success");
                response.add("currencies", resultObj);

            } else {
                response.addProperty("response", "999");
                response.addProperty("responseDescription", "Failed");
                response.add("error", resultObj);

            }

        } catch (RuntimeException e) {

            response.addProperty("response", "999");
            response.addProperty("responseDescription", "Failed");
            response.addProperty("error", "Failed to fetch Currencies");
            logger.error("Failed to fetch Currencies", e);

        }
        return response.toString();
    }

    @PostMapping("process")
    public String sendRequest(@RequestBody String request, HttpServletRequest httpRequest) {
        JsonObject response = new JsonObject();

        //String username = claims.getSubject();
        // logger.info("Authenticated user: " + username);
        try {
            JsonObject incomingDetails = JsonParser.parseString(request).getAsJsonObject();
            logger.info("----------INCOMING REQUEST  ----");
            logger.info("REQUEST  |  " + request);
            String action = incomingDetails.get("operation").getAsString();

            // Extract client IP
            String clientIp = httpRequest.getHeader("X-Forwarded-For");
            if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
                clientIp = httpRequest.getRemoteAddr();
            }
            clientIp = clientIp.split(",")[0].trim(); // Handle multiple proxies if present
            String username = incomingDetails.get("username").getAsString();
            String password = incomingDetails.get("password").getAsString();

            logger.error("Client IP  |  " + clientIp + " For partner " + incomingDetails.get("username").getAsString());
            // Validate IP against the database
            long ipCount = ipAddressIpRepo.countByIpAddressAndPartnerName(clientIp, username, 1);
            if (ipCount == 0) {
                response.addProperty("response", "999");
                response.addProperty("responseDescription", "Unauthorized IP address");
                return response.toString();
            }

            switch (action) {
                case "validatePartner":
                    try {
                    String token = tokenService.validateUserAndGenerateToken(username, password);
                    response.addProperty("response", "000");
                    response.addProperty("responseDescription", "Success");
                    response.addProperty("token", token);
                    Instant now = Instant.now();
                    Instant endDate = now.plus(5, ChronoUnit.MINUTES);
                    String formattedEndDate = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                            .withZone(ZoneOffset.UTC)
                            .format(endDate);
                    response.addProperty("endDate", formattedEndDate);

                } catch (RuntimeException e) {
                    response.addProperty("response", "999");
                    response.addProperty("responseDescription", e.getMessage());
                }
                break;
                default:
                    response.addProperty("response", "999");
                    response.addProperty("responseDescription", "Invalid operation");
                    break;
            }

        } catch (JsonSyntaxException ex) {
            logger.error("Exception  |  " + ex.getMessage());
            response.addProperty("response", "01");
            response.addProperty("responseDescription", "An error occurred while processing your request. Please try again later.");
        }
        return response.toString();
    }

    @PostMapping("accountLookup")
    public String accountLookup(@RequestBody String request, @RequestHeader("Authorization") String authHeader) throws IOException, MalformedURLException, ParseException {
        JsonObject response = new JsonObject();
        JsonObject tokenResponse = new JsonObject();
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.addProperty("response", "999");
            response.addProperty("responseDescription", "Missing or invalid Authorization header");
            return response.toString();
        }

        String beartoken = authHeader.substring(7);
        Claims claims;
        try {
            claims = tokenService.getClaimsFromToken(beartoken);
        } catch (RuntimeException e) {
            response.addProperty("response", "999");
            response.addProperty("responseDescription", "Invalid or expired token");
            return response.toString();
        }
        try {
            JsonObject incomingDetails = JsonParser.parseString(request).getAsJsonObject();
            logger.info("----------INCOMING REQUEST  ----");
            logger.info("REQUEST  |  " + request);
//            Integer institutionId = incomingDetails.get("institutionId").getAsInt();
//            String accountNumber = incomingDetails.get("accountNumber").getAsString();
            try {

                tokenResponse = util.tokenRequest(switchtokenUrl, username, password);
                logger.info("Token Response   |  " + tokenResponse);

                if (tokenResponse.has("token")) {
                    String token = tokenResponse.get("token").getAsString();
//                String token = util.tokenRequest(switchtokenUrl, username, password);
//                logger.info("Token   |  " + token);
//                if (!token.equalsIgnoreCase("")) {

                    JsonObject lookupReq = new JsonObject();
                    lookupReq.addProperty("institutionId", incomingDetails.get("institutionId").getAsString());
                    lookupReq.addProperty("accountNumber", incomingDetails.get("accountNumber").getAsString());

                    //send lookup 
                    response = util.lookRequest(accountlookupURL, lookupReq, token);

                    if (response.get("response").getAsString().equalsIgnoreCase("000")) {
                        // String accName = response.get("accountName").getAsString();
                        //mask account name 
//                        response.remove("accountName");
//                        String maskedAccountNumber = util.maskAccountNumber(accName);
//                        response.addProperty("accountName", maskedAccountNumber);
                    } else {

                    }

                }

            } catch (RuntimeException e) {
                response.addProperty("response", "999");
                response.addProperty("responseDescription", e.getMessage());
            }

        } catch (JsonSyntaxException ex) {
            logger.error("Exception  |  " + ex.getMessage());
            response.addProperty("response", "01");
            response.addProperty("responseDescription", "An error occurred while processing your request. Please try again later.");
        }
        return response.toString();
    }

    @PostMapping("initiateTransaction")
    public String initiateTransaction(@RequestBody String request, @RequestHeader("Authorization") String authHeader) throws IOException, MalformedURLException, ParseException {
        JsonObject response = new JsonObject();

        JsonObject lookupresponse = new JsonObject();
        JsonObject tokenResponse = new JsonObject();
        JsonObject resultObj = new JsonObject();
        JsonObject paymentReq = new JsonObject();
        String token = "";
        Integer userId;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.addProperty("response", "999");
            response.addProperty("responseDescription", "Missing or invalid Authorization header");
            return response.toString();
        }

        String beartoken = authHeader.substring(7);
        Claims claims;
        try {
            claims = tokenService.getClaimsFromToken(beartoken);
        } catch (RuntimeException e) {
            response.addProperty("response", "999");
            response.addProperty("responseDescription", "Invalid or expired token");
            return response.toString();
        }
        try {
            JsonObject incomingDetails = JsonParser.parseString(request).getAsJsonObject();
            logger.info("----------INCOMING REQUEST  ----");
            logger.info("REQUEST INITIATE TRANSACTION   |  " + request);
//            Integer institutionId = incomingDetails.get("institutionId").getAsInt();
//            String accountNumber = incomingDetails.get("accountNumber").getAsString();
            try {
                String partner = claims.getSubject();
                logger.info("Authenticated user: " + partner);
                //DO ACC LOOK UP KWANZA 

                String institutionID = databaseOps.getDeliveryTypeCode(incomingDetails.get("deliveryTypeId").getAsInt());

//                String lookUptoken = util.tokenRequest(switchtokenUrl, username, password);
//                logger.info("Token   |  " + lookUptoken);
//                if (!lookUptoken.equalsIgnoreCase("")) {
                tokenResponse = util.tokenRequest(switchtokenUrl, username, password);
                logger.info("Token Response   |  " + tokenResponse);

                if (tokenResponse.has("token")) {
                    String lookUptoken = tokenResponse.get("token").getAsString();

                    JsonObject lookupReq = new JsonObject();
                    lookupReq.addProperty("institutionId", institutionID);
                    lookupReq.addProperty("accountNumber", incomingDetails.get("accountNumber").getAsString());

                    //send lookup 
                    lookupresponse = util.lookRequest(accountlookupURL, lookupReq, lookUptoken);
                    logger.info("lookupresponse   |  " + lookupresponse);

                    if (lookupresponse.get("response").getAsString().equalsIgnoreCase("000")) {

                        //proceed
//                        String accName = response.get("accountName").getAsString();
//                        //mask account name 
//                        response.remove("accountName");
//                        String maskedAccountNumber = util.maskAccountNumber(accName);
//                        response.addProperty("accountName", maskedAccountNumber);
                    } else {

                        response.addProperty("response", "001");
                        response.addProperty("responseDescription", "Invalid Account");
                        return response.toString();

                    }

                }

                //SAVE FIRST THEN SEND A REQUEST 
                Map<String, Object> results = databaseOps.initiateTransaction(incomingDetails, partner);
                results.remove("#result-set-1");

                int msgId = (int) results.get("msgId");
                logger.info("DB MAP   |  " + results);

                String resultsasa = (String) results.get("results");
                logger.info("resultsasa   |  " + resultsasa);

                // Parse the result JSON to extract TransactionRef
                resultObj = JsonParser.parseString(resultsasa).getAsJsonObject();

                logger.info("DB RES 2   |  " + resultObj);
                if (msgId == 0) {
                    //here now post to Etswitch
//                    token = util.tokenRequest(switchtokenUrl, username, password);
//                    logger.info("Token   |  " + token);
//                    if (!token.equalsIgnoreCase("")) {
//
//                        response = util.paymentReq(paymentURL, resultObj, incomingDetails, token);
                    tokenResponse = util.tokenRequest(switchtokenUrl, username, password);
                    logger.info("Token Response   |  " + tokenResponse);

                    if (tokenResponse.has("token")) {
                        //tokenResponse.addProperty("userId", tokenRes.get("userId").getAsInt());
                        token = tokenResponse.get("token").getAsString();
                        userId = tokenResponse.get("userId").getAsInt();

                        response = util.paymentReq(paymentURL, resultObj, incomingDetails, token, userId);

                        if (!response.has("paymentRes")) {
                            response.addProperty("response", "999");
                            response.addProperty("responseDescription", "Transaction timed out. Please try again later ");
                            return response.toString();
                        }

                        String paymentRes = response.get("paymentRes").getAsString();
                        //  String paymentDesc = response.get("paymentDesc").getAsString();

                        int messageID = response.get("msgId").getAsInt();

                        if (paymentRes.equalsIgnoreCase("000")) {

                            response.addProperty("response", "000");
                            response.addProperty("responseDescription", "Success");

                        } else {
                            response.addProperty("response", "999");
                            response.addProperty("responseDescription", "Failed");
                        }
                        //UPDATE DB 

                        //check response if successful update DB 
                        //check if response is successful,.then update DB 
                    } else {
                        response.addProperty("response", "999");
                        response.addProperty("responseDescription", "An error occurred while processing your request. Please try again later.");
                    }
                } else {
                    response.addProperty("response", "999");
                    response.addProperty("responseDescription", "Failed");
                    response.add("error", resultObj);

                }

            } catch (RuntimeException e) {
                response.addProperty("response", "999");
                response.addProperty("responseDescription", e.getMessage());
            }

        } catch (JsonSyntaxException ex) {
            logger.error("Exception  |  " + ex.getMessage());
            response.addProperty("response", "999");
            response.addProperty("responseDescription", "An error occurred while processing your request. Please try again later.");
        }
        return response.toString();
    }

}
