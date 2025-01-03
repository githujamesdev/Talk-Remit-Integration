package com.remitxpress.co.ke.main.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.remitxpress.co.ke.main.services.DatabaseOps;
import java.io.IOException;
import java.net.MalformedURLException;
//import lombok.SneakyThrows;
import org.jboss.resteasy.client.jaxrs.BasicAuthentication;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class Utils {

    private final DatabaseOps databaseOps;

    @Autowired
    public Utils(DatabaseOps databaseOps) {
        this.databaseOps = databaseOps;
    }

    private final org.apache.logging.log4j.Logger log = LogManager.getLogger(Utils.class);

    RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<String> SendPostHttpRequest(JsonObject requestObject, String url) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("accept", "*/*");
        HttpEntity<String> httpEntity = new HttpEntity<>(requestObject.toString(), httpHeaders);
        return restTemplate.postForEntity(url, httpEntity, String.class);
    }

    public ResponseEntity<String> SendGetHttpRequest(JsonObject requestObject, String url, RestTemplate restTemplate) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Basic " + "YjM4OGY3NGQtMTljZi00NzM0LTk0NjktMTYyN2VjNmJhNzY3OjAxMTE4MGExLWQ0YjctNDc5YS04ZmY1LWM2YjMxODE2MDY5ZA==");
        httpHeaders.set("accept", "*/*");
        HttpEntity<String> httpEntity = new HttpEntity<>(null, httpHeaders);
        return restTemplate.postForEntity(url, httpEntity, String.class);
    }

    public Response sendGetRequest(String url, String username, String password) {
        ResteasyClient httpClient = new ResteasyClientBuilder().build();
        httpClient.register(new BasicAuthentication(username, password));
        Response response1 = httpClient.target(url).request().get();
        System.out.println("Response Get: \n" + response1.getStatus());
        return response1;
    }

    public Response sendPostRequest(String url, JsonObject requestBody, String username, String password) {
        ResteasyClient httpClient = new ResteasyClientBuilder().build();
        httpClient.register(new BasicAuthentication(username, password));
        Response response1 = httpClient.target(url).request().post(Entity.entity(requestBody.toString(), "application/json"));
        return response1;
    }

    public String generateRef() {
        Random rand = new Random();
        int randInt = rand.nextInt(10000);
        String date = new SimpleDateFormat("mmssSS").format(new Date());
        return String.valueOf(randInt).concat(date);
    }

    public JsonObject parseRequest(String request) {
        JsonObject requestObject = JsonParser.parseString(request).getAsJsonObject();
        return requestObject;
    }

    public JsonObject responseObject(String type) {
        JsonObject requestObject = new JsonObject();

        if (type.equals("fail")) {
            requestObject.addProperty("status", 99);
            requestObject.addProperty("message", "Request Failed");
        } else {
            requestObject.addProperty("status", 0);
            requestObject.addProperty("message", "Request Successful");
        }
        return requestObject;
    }

    public ObjectMapper dateFormattedMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return mapper;
    }

    public String dateISO8601() {
        Date date = new Date();
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.00'Z'");
        System.out.println(formatter1.format(date));
        return formatter1.format(date);
    }

    @SneakyThrows
    public String simpleDateFormat(String dat) {
        return dat;
    }

    public String generateCBSRef() {
        String ref = "";

        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 12;
        Random random = new Random();

        ref = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return ref.toUpperCase();
    }

    public static Date simpleDateFormatting(String date) throws ParseException {
        Date dateFormatted = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        return dateFormatted;
    }

//    public JsonObject parseXmlResponse(String response) {
//        JsonObject swiftObject = converter.convertXmlToJson(response);
//
//        return swiftObject;
//    }
    public ResponseEntity<String> SendJsonHttpRequest(JsonObject requestObject, String url, RestTemplate restTemplate) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("accept", "*/*");
        HttpEntity<String> httpEntity = new HttpEntity<>(requestObject.toString(), httpHeaders);

        return restTemplate.postForEntity(url, httpEntity, String.class);
    }

    public JsonObject flightLogicRequest(JsonObject ackMessages, String url) throws IOException, MalformedURLException, ParseException {

        ResteasyClient httpClient = new ResteasyClientBuilder().build();
        Response response1 = httpClient.target(url).request().header(HttpHeaders.ACCEPT,
                "*/*").post(Entity.entity(ackMessages.toString(), "application/json"));
        String res = response1.readEntity(String.class);

        //log.info("Flight Logic Responses" + res);
        JsonObject FlightAvailabilityResponse = JsonParser.parseString(res).getAsJsonObject();

        //log.info("Flight Responses" + FlightAvailabilityResponse);
        return FlightAvailabilityResponse;
    }

    public JsonObject tokenRequest(String switchtokenUrl, String username, String password) throws IOException, MalformedURLException, ParseException {
        String token = "";
        JsonObject tokenResponse = new JsonObject();
        try {
            JsonObject request = new JsonObject();
            request.addProperty("username", username);
            request.addProperty("password", password);
            log.info("Request: " + request.toString() + " URL: " + switchtokenUrl);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.set("accept", "*/*");
            HttpEntity<String> httpEntity = new HttpEntity<>(request.toString(), httpHeaders);

            ResponseEntity<String> response = restTemplate.postForEntity(switchtokenUrl, httpEntity, String.class);
            String res = response.getBody();
            JsonObject tokenRes = JsonParser.parseString(res).getAsJsonObject();
            log.info("Response: " + res);

            if (tokenRes.has("token") && tokenRes.get("response").getAsString().equalsIgnoreCase("000")) {
                tokenResponse.addProperty("token", tokenRes.get("token").getAsString());
                tokenResponse.addProperty("userId", tokenRes.get("userId").getAsInt());
                //token = tokenRes.get("token").getAsString();
            }
        } catch (JsonSyntaxException | IllegalArgumentException | NullPointerException ex) {
            log.info("Exception: " + ex.getMessage());
        }

        return tokenResponse;
    }
//    public String tokenRequest(String switchtokenUrl, String username, String password) throws IOException, MalformedURLException, ParseException {
//        String token = "";
//        try {
//            JsonObject request = new JsonObject();
//            request.addProperty("username", username);
//            request.addProperty("password", password);
//            log.info("Request: " + request.toString() + " URL: " + switchtokenUrl);
//
//            HttpHeaders httpHeaders = new HttpHeaders();
//            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//            httpHeaders.set("accept", "*/*");
//            HttpEntity<String> httpEntity = new HttpEntity<>(request.toString(), httpHeaders);
//
//            ResponseEntity<String> response = restTemplate.postForEntity(switchtokenUrl, httpEntity, String.class);
//            String res = response.getBody();
//            JsonObject tokenRes = JsonParser.parseString(res).getAsJsonObject();
//            log.info("Response: " + res);
//
//            if (tokenRes.has("token") && tokenRes.get("response").getAsString().equalsIgnoreCase("000")) {
//                token = tokenRes.get("token").getAsString();
//            }
//        } catch (JsonSyntaxException | IllegalArgumentException | NullPointerException ex) {
//            log.info("Exception: " + ex.getMessage());
//        }
//
//        return token;
//    }

//    public String tokenRequest(String switchtokenUrl, String username, String password) throws IOException, MalformedURLException, ParseException {
//        String token = "";
//        try {
//            JsonObject request = new JsonObject();
//
//            request.addProperty("username", username);
//            request.addProperty("password", password);
//            log.info(" REq" + request.toString() + "URL " + switchtokenUrl);
//            ResteasyClient httpClient = new ResteasyClientBuilder().build();
//            Response response1 = httpClient.target(switchtokenUrl).request().header(HttpHeaders.ACCEPT,
//                    "*/*").post(Entity.entity(request.toString(), "application/json"));
//            String res = response1.readEntity(String.class);
//            JsonObject tokenRes = JsonParser.parseString(res).getAsJsonObject();
//            log.info(" Responses" + res);
//            if (tokenRes.has("token") && tokenRes.get("response").getAsString().equalsIgnoreCase("000")) {
//                token = tokenRes.get("token").getAsString();
//
//            }
//        } catch (JsonSyntaxException | IllegalArgumentException | NullPointerException ex) {
//            log.info(" Exception" + ex.getMessage());
//        }
//
//        return token;
//    }
    public JsonObject lookRequest(String accountlookupURL, JsonObject request, String bearerToken) throws IOException, MalformedURLException, ParseException {
        //  JsonObject request = new JsonObject();
//lookuprequest 

        log.info("Request for Lookup: " + request.toString());
        ResteasyClient httpClient = new ResteasyClientBuilder().build();
        Response response1 = httpClient.target(accountlookupURL).request()
                .header(HttpHeaders.ACCEPT, "*/*")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                .post(Entity.entity(request.toString(), "application/json"));
        String res = response1.readEntity(String.class);
        JsonObject lookupRes = JsonParser.parseString(res).getAsJsonObject();

        log.info("Responses for Lookup: " + res);
        return lookupRes;
    }

    public JsonObject paymentReq(String payURL, JsonObject databaseFullRes, JsonObject request, String bearerToken, Integer userId) throws IOException, MalformedURLException, ParseException {
        JsonObject payment = new JsonObject();

        JsonObject databaseObj = databaseFullRes.getAsJsonObject("results");
        log.info("databaseObj   |  " + databaseObj);

        log.info("request   |  " + request);

        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

        String timestamp = now.format(formatter);

        payment.addProperty("institutionId", databaseObj.get("deliveryTypeCode").getAsString());
        payment.addProperty("accountNumber", databaseObj.get("accountNumber").getAsString());
        double amountDouble = request.get("receivingAmount").getAsDouble();
        int amountInt = (int) amountDouble;
        String amountString = Integer.toString(amountInt);
        payment.addProperty("amount", amountString);
        payment.addProperty("transactionType", "AUTO");
        payment.addProperty("partnerReference", databaseObj.get("transactionRef").getAsString());
        payment.addProperty("settlementAccount", databaseObj.get("settlementAccount").getAsString());
        payment.addProperty("comment", request.get("transactionNote").getAsString());
        payment.addProperty("userId", "");
        payment.addProperty("reason", request.get("transactionNote").getAsString());
        payment.addProperty("timestamp", timestamp);

        log.info("reEtswitch request   |  " + payment);
        long connectionTimeout = 40; // Connection timeout in seconds
        long readTimeout = 60;       // Read timeout in seconds

        ResteasyClient httpClient = new ResteasyClientBuilder()
                .establishConnectionTimeout(connectionTimeout, TimeUnit.SECONDS)
                .socketTimeout(readTimeout, TimeUnit.SECONDS)
                .build();

        //   ResteasyClient httpClient = new ResteasyClientBuilder().build();
        Response response1 = httpClient.target(payURL).request()
                .header(HttpHeaders.ACCEPT, "*/*")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                .post(Entity.entity(payment.toString(), "application/json"));
        String res = response1.readEntity(String.class);
        JsonObject paymentRes = JsonParser.parseString(res).getAsJsonObject();

        response1.close();
        httpClient.close();

        log.info("Responses for payment : " + res);
        // {"response":"000","responseDescription":"success","accountNumber":"1000539165015",
        //"amount":"10","currency":"ETB","transactionType":"AUTO","transactionReference":"AMAL-57E6DDF1D",
        //"comment":"Bill Transaction Test","reason":"Bill Transaction Test",
        //"timestamp":"2024-12-31T13:45:14.518+00:00","refNumber":"ESA0BUQVWT9A",
        //"taxAmount":"0.006","transactionCharge":"0.046"}

        Map<String, Object> results = databaseOps.updateTransaction(databaseObj, payment, paymentRes, request);

        log.info("update db : " + results);
        int msgId = (int) results.get("MsgId");
        String resultMsg = (String) results.get("Result");

        JsonObject resultObj = JsonParser.parseString(resultMsg).getAsJsonObject();
        resultObj.addProperty("paymentRes", paymentRes.get("response").getAsString());
        resultObj.addProperty("paymentDesc", paymentRes.get("responseDescription").getAsString());
     
        return resultObj;
    }

    public String maskAccountNumber(String accountNumber) {
        if (accountNumber.length() > 4) {
            String lastFourDigits = accountNumber.substring(accountNumber.length() - 4);
            String maskedPart = accountNumber.substring(0, accountNumber.length() - 4).replaceAll(".", "*");
            return maskedPart + lastFourDigits;
        } else {
            return accountNumber.replaceAll(".", "*");
        }
    }

}
