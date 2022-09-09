/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.rest;

import com.toby.API.InvInvoiceDTOApi;
import com.toby.API.ResponseApi;
import com.toby.businessservice.InvPurchaseInvoiceService_1;
import com.toby.define.HttpMethode;
import com.toby.dto.InvInvoiceDTO;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.StringEscapeUtils;

@Path("/sales/")
public class SalesRest {

    @EJB
    InvPurchaseInvoiceService_1 invPurchaseInvoiceService_1;

    @Transactional
    @Path("/save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(InvInvoiceDTOApi invInvoiceDTOApi) {
        try {
            ResponseApi responseApi = invPurchaseInvoiceService_1.SaveApi(invInvoiceDTOApi);
            return Response.ok().entity(responseApi).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @Transactional
    @Path("/saveDeskTop")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public InvInvoiceDTOApi saveDeskTop(InvInvoiceDTOApi invInvoiceDTOApi) {
        try {
            if (invInvoiceDTOApi.getId() == null && invInvoiceDTOApi.getCustomerId() != null && invInvoiceDTOApi.getGallaryId() != null) {
                InvInvoiceDTOApi invoiceDTO = invPurchaseInvoiceService_1.SaveApiDeskTop(invInvoiceDTOApi);
                return invoiceDTO;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    @Path("/tarhilSalesAPI")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public InvInvoiceDTOApi tarhilSalesAPI(InvInvoiceDTOApi invInvoiceDTOApi) {
        try {
            if (invInvoiceDTOApi.getId() != null && invInvoiceDTOApi.getCustomerId() != null && invInvoiceDTOApi.getGallaryId() != null) {
                InvInvoiceDTO invInvoiceDTO = invPurchaseInvoiceService_1.mapToDTOApiDeskTop(invInvoiceDTOApi, false);
                InvInvoiceDTOApi invInvoiceDTOAPI = invPurchaseInvoiceService_1.tarhilSalesAPI(invInvoiceDTO);
                sendSms(invInvoiceDTOAPI.getCustomerMobile(), "توبى العالمى", "testtt", "20099319", "0564933202");
                return invInvoiceDTOAPI;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public void sendSms(String mobile, String sender, String message, String username, String password) {
        try {
            String language = "2";
            StringBuilder json = new StringBuilder();
            json.append("[{\"user\":\"").append(username).append("\",\"pwd\":\"").append(password).append("\",\"number\":\"").append(mobile).append("\",\"msg\":\"").append(message);
            json.append("\",\"sender\":\"SMSAlert\",\"language\":\"Unicode\"}]");
            callApiPostBasic("http://mshastra.com/sendsms_api_json.aspx", null, null, json.toString(), null, HttpMethode.POST);
        } catch (Exception e) {
            System.out.println("Error SMS " + e);
//            return "Error " + e;
        }
    }

    public String callApiPostBasic(String url, String userName, String password, String jsonInputString, Map<String, Object> LinkParams, HttpMethode httpMethode) {
        try {
            String auth = userName + ":" + password;
            String link = url;
            if (LinkParams != null) {
                link = link + getParamsString(LinkParams);
            }
            URL url1 = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url1.openConnection();

            conn.setRequestMethod(httpMethode.name());
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            conn.setDoOutput(true);
            if (userName != null && password != null) {
                conn.setRequestProperty(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString((auth).getBytes()));
            }
            if (jsonInputString != null && !jsonInputString.isEmpty()) {
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                    os.close();
                }
            }

            BufferedReader br;
            String strCurrentLine;
            StringBuilder json = new StringBuilder();

            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((strCurrentLine = br.readLine()) != null) {
                json.append(strCurrentLine);
            }

            br.close();
            conn.disconnect();
            String result = StringEscapeUtils.unescapeJava(json.toString());
            result = result.replaceAll("null", "\"null\"");
            result.replaceAll("\"\"", "\"null\"");
            return result.replaceAll("(?<=[^{,:])\\\"(?![},:])(?!$)", "\'");//((?<!^)\")(?<!$)
        } catch (Exception ex) {
//            Logger.getLogger(Basic.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private String getParamsString(Map<String, Object> params) {
        try {
            StringBuilder result = new StringBuilder();
            Map<String, Object> param = new LinkedHashMap<>();
            param.putAll(params);

            for (Map.Entry<String, Object> entry : param.entrySet()) {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                result.append("&");
            }

            String resultString = result.toString();
            return resultString.length() > 0
                    ? resultString.substring(0, resultString.length() - 1)
                    : resultString;
        } catch (Exception ex) {
//            Logger.getLogger(Basic.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Transactional
    @Path("findInvPurchaseInvoiceById")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public InvInvoiceDTO findInvPurchaseInvoiceById(InvInvoiceDTOApi dTO) {
        return this.invPurchaseInvoiceService_1.findInvPurchaseInvoiceById(dTO.getId());
    }

    @Transactional
    @Path("findInvPurchaseInvoiceByserial")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public InvInvoiceDTOApi findInvPurchaseInvoiceByserial(InvInvoiceDTOApi dTO) {
        return this.invPurchaseInvoiceService_1.findInvPurchaseInvoiceByserialAPI(dTO.getSerial(), dTO.getGallaryId(), dTO.getBranchId());
    }
}
