/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.remitxpress.co.ke.main.services;

import com.google.gson.JsonObject;
import com.remitxpress.co.ke.main.utilities.Utils;
import io.jsonwebtoken.SignatureAlgorithm;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.springframework.dao.DataAccessException;

/**
 *
 * @author jgithu
 */
@Service
public class DatabaseOps {

    private final org.apache.logging.log4j.Logger log = LogManager.getLogger(Utils.class);

    private final JdbcTemplate jdbcTemplate;

    public DatabaseOps(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, Object> getCountries() {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GetCountry")
                .declareParameters(
                        new SqlOutParameter("MsgId", Types.INTEGER),
                        new SqlOutParameter("Result", Types.NVARCHAR)
                );

        Map<String, Object> result = jdbcCall.execute();
        int msgId = (int) result.get("MsgId");
        String resultMsg = (String) result.get("Result");

        if (msgId == 0) {
            return result;
        } else {
            return result;
        }
    }

    public Map<String, Object> getCurrencies() {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GetCurrencies")
                .declareParameters(
                        new SqlOutParameter("MsgId", Types.INTEGER),
                        new SqlOutParameter("Result", Types.NVARCHAR)
                );

        Map<String, Object> result = jdbcCall.execute();
        int msgId = (int) result.get("MsgId");
        String resultMsg = (String) result.get("Result");

        if (msgId == 0) {
            return result;
        } else {
            return result;
        }
    }

    public Map<String, Object> getDeliveryMethod(Integer countryId) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GetDeliveryMethod")
                .declareParameters(
                        new SqlParameter("CountryId", Types.INTEGER),
                        new SqlOutParameter("MsgId", Types.INTEGER),
                        new SqlOutParameter("Result", Types.NVARCHAR)
                );

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("CountryId", countryId);
        Map<String, Object> result = jdbcCall.execute(params);

        int msgId = (int) result.get("MsgId");
        String resultMsg = (String) result.get("Result");

        if (msgId == 0) {
            return result;
        } else {
            return result;
        }
    }

    public Map<String, Object> getDeliveryType(Integer CountryId, Integer DeliveryMethodId) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GetDeliveryType")
                .declareParameters(
                        new SqlParameter("CountryId", Types.INTEGER),
                        new SqlParameter("DeliveryMethodId", Types.INTEGER),
                        new SqlOutParameter("MsgId", Types.INTEGER),
                        new SqlOutParameter("Result", Types.NVARCHAR)
                );

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("CountryId", CountryId)
                .addValue("DeliveryMethodId", DeliveryMethodId);
        Map<String, Object> result = jdbcCall.execute(params);
        int msgId = (int) result.get("MsgId");
        String resultMsg = (String) result.get("Result");

        if (msgId == 0) {
            return result;
        } else {
            return result;
        }
    }

    public Map<String, Object> getExchangeRate(Integer partnerID, Integer SourceCurrency, Integer DestinationCurrency) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GetExchangeRate")
                .declareParameters(
                        new SqlParameter("PartnerId", Types.INTEGER),
                        new SqlParameter("SourceCurrency", Types.INTEGER),
                        new SqlParameter("DestinationCurrency", Types.INTEGER),
                        new SqlOutParameter("MsgId", Types.INTEGER),
                        new SqlOutParameter("Result", Types.NVARCHAR)
                );

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("PartnerId", partnerID)
                .addValue("SourceCurrency", SourceCurrency)
                .addValue("DestinationCurrency", DestinationCurrency);
        Map<String, Object> result = jdbcCall.execute(params);
        int msgId = (int) result.get("MsgId");
        String resultMsg = (String) result.get("Result");

        if (msgId == 0) {
            return result;
        } else {
            return result;
        }
    }

    public Map<String, Object> getAccountBalance(String account) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GetAccountBalance")
                .declareParameters(
                        new SqlParameter("AccountNumber", Types.NVARCHAR),
                        new SqlOutParameter("MsgId", Types.INTEGER),
                        new SqlOutParameter("Result", Types.NVARCHAR)
                );

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("AccountNumber", account);

        Map<String, Object> result = jdbcCall.execute(params);
        int msgId = (int) result.get("MsgId");
        String resultMsg = (String) result.get("Result");

        if (msgId == 0) {
            return result;
        } else {
            return result;
        }
    }

    public Map<String, Object> GetTransactionStatus(String PartnerId, String transRef, String partnerRef) {

        if (transRef.equalsIgnoreCase("") || transRef.isEmpty()) {
            transRef = null;
        }

        if (partnerRef.equalsIgnoreCase("") || partnerRef.isEmpty()) {
            partnerRef = null;
        }

        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GetTransactionStatus")
                .declareParameters(
                        new SqlParameter("TransactionReference ", Types.NVARCHAR),
                        new SqlParameter("PartnerReference ", Types.NVARCHAR),
                        new SqlParameter("PartnerId ", Types.NVARCHAR),
                        new SqlOutParameter("msgId", Types.INTEGER),
                        new SqlOutParameter("result", Types.NVARCHAR)
                );

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("TransactionReference", transRef)
                .addValue("PartnerReference", partnerRef)
                .addValue("PartnerId", PartnerId);

        Map<String, Object> result = jdbcCall.execute(params);
        log.info("TRANSACTIONS RESPONSE: " + result);
//        String msgId = (String) result.get("msgId");
//        String resultMsg = (String) result.get("result");
        return result;
//        if (msgId == 0) {
//          
//        } else {
//            return result;
//        }
    }

    public Map<String, Object> initiateTransaction(JsonObject transactionDetails, String partner) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("InitiateRemitTransaction")
                .declareParameters(
                        new SqlParameter("InitiatorPartnerId", Types.INTEGER),
                        new SqlParameter("DeliveryMethodId", Types.INTEGER),
                        new SqlParameter("DeliveryTypeId", Types.INTEGER),
                        new SqlParameter("BankAccountNumber", Types.NVARCHAR),
                        new SqlParameter("BankAccountName", Types.NVARCHAR),
                        new SqlParameter("WalletId", Types.INTEGER),
                        new SqlParameter("WalletAccountNumber", Types.NVARCHAR),
                        new SqlParameter("WalletAccountName", Types.NVARCHAR),
                        new SqlParameter("SendingCurrencyId", Types.INTEGER),
                        new SqlParameter("SendingAmount", Types.INTEGER),
                        new SqlParameter("ReceivingAmount", Types.INTEGER),
                        new SqlParameter("ReceivingCurrencyId", Types.INTEGER),
                        new SqlParameter("InitiatorAgentId", Types.INTEGER),
                        new SqlParameter("InitiatorSubAgentId", Types.INTEGER),
                        new SqlParameter("PartnerReference", Types.NVARCHAR),
                        new SqlParameter("PartnerRequest", Types.NVARCHAR),
                        new SqlParameter("TransactionNote", Types.NVARCHAR),
                        new SqlParameter("PartnerTimestamp", Types.NVARCHAR),
                        new SqlOutParameter("msgId", Types.INTEGER),
                        new SqlOutParameter("results", Types.NVARCHAR)
                );

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("InitiatorPartnerId", Integer.parseInt(partner))
                .addValue("DeliveryMethodId", transactionDetails.get("deliveryMethodId").getAsInt())
                .addValue("DeliveryTypeId", transactionDetails.get("deliveryTypeId").getAsInt())
                .addValue("BankAccountNumber", transactionDetails.get("accountNumber").getAsString())
                .addValue("BankAccountName", transactionDetails.get("accountName").getAsString())
                .addValue("WalletId", null)
                .addValue("WalletAccountNumber", null)
                .addValue("WalletAccountName", null)
                .addValue("SendingCurrencyId", transactionDetails.get("sendingCurrencyId").getAsInt())
                .addValue("SendingAmount", transactionDetails.get("sendingAmount").getAsInt())
                .addValue("ReceivingAmount", transactionDetails.get("receivingAmount").getAsInt())
                .addValue("ReceivingCurrencyId", transactionDetails.get("receivingCurrencyId").getAsInt())
                .addValue("InitiatorAgentId", null)
                .addValue("InitiatorSubAgentId", null)
                .addValue("PartnerReference", transactionDetails.get("partnerReference").getAsString())
                .addValue("PartnerRequest", transactionDetails.toString())
                .addValue("TransactionNote", transactionDetails.get("transactionNote").getAsString())
                .addValue("PartnerTimestamp", transactionDetails.get("partnerTimestamp").getAsString());

        Map<String, Object> result = jdbcCall.execute(params);

        log.info("INSERT DB RESULT: " + result);

//        int msgId = (int) result.get("MsgId");
//    String resultMsg = (String) result.get("Result");
//
//        if (msgId == 0) {
//            return result;
//        } else {
//            
//        }
//        
        return result;
    }

//EXEC	@return_value = [dbo].[UpdateRemittanceTransaction]
//		@TransactionRef = N'1AFCA2C5F',
//		@ThirdPartyReference = N'1AFCA2C5F',
//		@ThirdPartyRequest = N'{req_to_eswitch}',
//		@ThirdPartyResponse = N'{res_from_eswitch}',
//		@ThirdPartyTrxDate = N'2024-05-23 20:23:28.000',
//		@ThirdPartyStatus = N'success',
//		@MsgId = @MsgId OUTPUT,
//		@Result = @Result OUTPUT
    public Map<String, Object> updateTransaction(JsonObject databaseObj, JsonObject request, JsonObject response, JsonObject thirdpartyrequest) {
        String transStatus = "";

        String refNumber = "";
        BigDecimal taxAmount = BigDecimal.ZERO;
        BigDecimal transactionCharge = BigDecimal.ZERO;

        if (response.has("refNumber")) {
            refNumber = response.get("refNumber").getAsString();
        }
        if (response.has("taxAmount")) {
            try {
                taxAmount = new BigDecimal(response.get("taxAmount").getAsString());
            } catch (NumberFormatException e) {
                log.error("Invalid taxAmount: " + response.get("taxAmount").getAsString(), e);
            }
        }
        if (response.has("transactionCharge")) {
            try {
                transactionCharge = new BigDecimal(response.get("transactionCharge").getAsString());
            } catch (NumberFormatException e) {
                log.error("Invalid transactionCharge: " + response.get("transactionCharge").getAsString(), e);
            }
        }

        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("UpdateRemittanceTransaction")
                .declareParameters(
                        new SqlParameter("TransactionRef", Types.NVARCHAR),
                        new SqlParameter("ThirdPartyReference", Types.NVARCHAR),
                        new SqlParameter("ThirdPartyRequest", Types.NVARCHAR),
                        new SqlParameter("ThirdPartyResponse", Types.NVARCHAR),
                        new SqlParameter("ThirdPartyTrxDate", Types.NVARCHAR),
                        new SqlParameter("ThirdPartyStatus", Types.NVARCHAR),
                        new SqlParameter("EsbReference", Types.NVARCHAR),
                        new SqlParameter("VatAmount", Types.NUMERIC),
                        new SqlParameter("TransactionCharge", Types.NUMERIC),
                        new SqlOutParameter("MsgId", Types.INTEGER),
                        new SqlOutParameter("Result", Types.NVARCHAR)
                );

        if (response.has("response") && response.get("response").getAsString().equalsIgnoreCase("000")) {
            transStatus = "success ";
        } else {
            transStatus = "failed";
        }
        // Parse the timestamp string to LocalDateTime
        String timestampStr = request.get("timestamp").getAsString();
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        LocalDateTime localDateTime = LocalDateTime.parse(timestampStr, inputFormatter);

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTimestamp = localDateTime.format(outputFormatter);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("TransactionRef", databaseObj.get("transactionRef").getAsString())
                .addValue("ThirdPartyReference", databaseObj.get("transactionRef").getAsString())
                .addValue("ThirdPartyRequest", request.toString())
                .addValue("ThirdPartyResponse", response.toString())
                .addValue("ThirdPartyTrxDate", formattedTimestamp)
                .addValue("ThirdPartyStatus", transStatus)
                .addValue("EsbReference", refNumber)
                .addValue("VatAmount", taxAmount)
                .addValue("TransactionCharge", transactionCharge);
        Map<String, Object> result = jdbcCall.execute(params);
        log.info("UPDATE DB RESULT: " + result);

        int msgId = (int) result.get("MsgId");
        String resultMsg = (String) result.get("Result");

        if (msgId == 0) {
            return result;
        } else {
            return result;
        }
    }

    public String getDeliveryTypeCode(int deliveryTypeId) {
        String sql = "SELECT DeliveryTypeCode FROM DeliveryType WHERE DeliveryTypeId = ?";
        try {
            String deliveryTypeCode = jdbcTemplate.queryForObject(sql, new Object[]{deliveryTypeId}, String.class);
            log.info("Fetched DeliveryTypeCode: {}", deliveryTypeCode);
            return deliveryTypeCode;
        } catch (DataAccessException e) {
            log.error("Error fetching DeliveryTypeCode for DeliveryTypeId {}: {}", deliveryTypeId, e.getMessage());
            return null;
        }
    }
}
