/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.general;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;


/**
 *
 * @author ahmedEssam
 */
@Stateless
public class GeneralImp implements GeneralService {

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String secret = "remas@2019@Secret";



    private String createJWT() throws UnsupportedEncodingException {
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(1593872976, 0, ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE,MMMM d,yyyy h:mm,a", Locale.ENGLISH);
        String formattedDate = dateTime.format(formatter);
        System.out.println(formattedDate);
        String tokenid = tokenId();
        String jwt = Jwts.builder()
                .setIssuer("http://www.erp_isag.com")
                .setSubject("isag")
                .setId(tokenid)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, secret.getBytes("UTF-8"))
                .compact();
        return jwt;
    }

    @Override
    public Boolean verifyToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secret.getBytes("UTF-8"))
                    .parseClaimsJws(token).getBody();
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }

    }

    @Override
    public String DecodeJWT(String token) throws UnsupportedEncodingException {
        Claims claims = Jwts.parser()
                .setSigningKey(secret.getBytes("UTF-8"))
                .parseClaimsJws(token).getBody();
        return claims.getId();
    }

    private String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    private String tokenId() {
        StringBuilder builder = new StringBuilder();
        builder.append(randomAlphaNumeric(8));
        builder.append("-");
        builder.append(randomAlphaNumeric(4));
        builder.append("-");
        builder.append(randomAlphaNumeric(4));
        builder.append("-");
        builder.append(randomAlphaNumeric(4));
        builder.append("-");
        builder.append(randomAlphaNumeric(4));
        builder.append("-");
        builder.append(randomAlphaNumeric(12));
        builder.append("-");
        builder.append(new Date().toString());
        return builder.toString();
    }

}
