package com.flightlogic.co.ke.main.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flightlogic.co.ke.main.services.FlightAvailability;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hazelcast.json.internal.JsonSchemaNode;
import java.io.IOException;
import java.net.MalformedURLException;
//import lombok.SneakyThrows;
import org.jboss.resteasy.client.jaxrs.BasicAuthentication;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import org.apache.commons.codec.binary.Base64;
import java.util.List;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;

@Service
public class Utils {

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

        log.info("Flight Logic Responses" + res);
        JsonObject FlightAvailabilityResponse = JsonParser.parseString(res).getAsJsonObject();

        log.info("Flight Responses" + FlightAvailabilityResponse);
        return FlightAvailabilityResponse;
    }

    public JsonArray airportRequest(JsonObject ackMessages, String url) throws IOException, MalformedURLException, ParseException {

        ResteasyClient httpClient = new ResteasyClientBuilder().build();
        Response response1 = httpClient.target(url).request().header(HttpHeaders.ACCEPT,
                "*/*").post(Entity.entity(ackMessages.toString(), "application/json"));
        String res = response1.readEntity(String.class);

        log.info("Airport List Responses" + res);
        JsonArray FlightAvailabilityResponse = JsonParser.parseString(res).getAsJsonArray();

        log.info("Airport List Responses" + FlightAvailabilityResponse);
        return FlightAvailabilityResponse;
    }

}
