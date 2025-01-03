/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.remitxpress.co.ke.main.services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.Date;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 *
 * @author jgithu
 */
@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    private final JdbcTemplate jdbcTemplate;

    public TokenService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Generates a JWT token for the given username.
     *
     * @param username the username for which to generate the token
     * @return the generated JWT token
     */
    private String generateToken(Integer partner) {
        return Jwts.builder()
                .setSubject(String.valueOf(partner))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * Validates the provided username and password by calling a stored
     * procedure. If the credentials are valid, generates and returns a JWT
     * token.
     *
     * @param username the username to validate
     * @param password the password to validate
     * @return the generated JWT token if validation is successful
     * @throws RuntimeException if the validation fails
     */
    public String validateUserAndGenerateToken(String username, String password) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("ValidatePartner")
                .declareParameters(
                        new SqlParameter("Username", Types.NVARCHAR),
                        new SqlParameter("Password", Types.NVARCHAR),
                        new SqlOutParameter("msgId", Types.INTEGER),
                        new SqlOutParameter("result", Types.NVARCHAR)
                );

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("Username", username)
                .addValue("Password", password);

        Map<String, Object> result = jdbcCall.execute(params);

        int msgId = (int) result.get("msgId");
        String resultMsg = (String) result.get("result");

        JsonObject resultObj = JsonParser.parseString(resultMsg).getAsJsonObject();

        JsonObject validateObj = resultObj.getAsJsonObject("result");

        //  {"msgId": 0, "result": {"PartnerId":1,"PartnerName":"TalkRemit","AccountNumber":"2396225"}}
        Integer partner = validateObj.get("PartnerId").getAsInt();

        if (msgId == 0) { // Assuming msgId 0 means success
            return generateToken(partner);
        } else {
            throw new RuntimeException("Invalid username or password: " + resultMsg);
        }
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid or expired token", e);
        }
    }

}
