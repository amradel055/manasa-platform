/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.general;

import com.isag.businessservice.BranchService;
import com.isag.businessservice.IsagApiTokenService;
import com.isag.dto.APIBranchDTO;
import com.isag.dto.APIDataDTO;
import com.isag.dto.IsagApiTokenDTO;
import com.isag.dto.APIloginDTO;
import com.isag.entity.Branch;
import com.isag.entity.IsagUser;
import com.isag.root.LoginRoot;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import com.isag.businessservice.IsagUserService;

/**
 *
 * @author ahmedEssam
 */
@Stateless
public class GeneralImp implements GeneralService {

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String secret = "isag@2019@Secret";

    @EJB
    private IsagUserService userService;
    @EJB
    private IsagApiTokenService isagApiTokenService;
    @EJB
    private BranchService branchService;

    private IsagUser isagUser;

    private List<Branch> branches = new ArrayList<>();

    @Override
    public APIloginDTO authenticate(LoginRoot loginRoot) {

        String userName = loginRoot.userName;
        String password = loginRoot.password;
        IsagUser loggedInUser = userService.authenticateUser(userName, password);
        //loggedInUser.getMaster() دي معناها لازم يكون مدير
        //if (loggedInUser != null && loggedInUser.getMaster()  == 1) {
        if (loggedInUser != null) {
            System.out.println("Logged In Successfully");
            isagUser = loggedInUser;
            branches = branchService.getBranchesForUser(loggedInUser.getId());
            if (branches != null && branches.size() > 0) {
                isagUser.setBranchId(branches.get(0));
            }
            return issueToken();
        } else {
            return jsonToken(null, Boolean.FALSE, "عفوا اسم المستخدم او كلمه المرور غير صحيحه");
        }
    }

    @Override
    public APIloginDTO issueToken() {
        try {
            String token = createJWT();
            String tokenId = DecodeJWT(token);
            IsagApiTokenDTO isagApiTokenDTO = new IsagApiTokenDTO();
            isagApiTokenDTO.setToken(token);
            isagApiTokenDTO.setTokenId(tokenId);
            isagApiTokenService.addIsagApiToken(isagApiTokenDTO, isagUser);
            return jsonToken(token, Boolean.TRUE, null);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(GeneralImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jsonToken(null, Boolean.FALSE, null);

    }

    private APIloginDTO jsonToken(String token, Boolean success, String msg) {
        APIloginDTO dTO = new APIloginDTO();
        dTO.setSuccess(success);
        dTO.setMsg(msg);
        dTO.setToken(token);
        if (success.equals(Boolean.TRUE)) {
            APIDataDTO data = new APIDataDTO();
            data.setName(isagUser.getName());
            data.setBranchSelected(isagUser.getBranchId().getSerial());
            if (branches != null && !branches.isEmpty()) {
                List<APIBranchDTO> branchDTOList = new ArrayList<>();
                for (Branch branche : branches) {
                    APIBranchDTO branchDto = new APIBranchDTO();
                    branchDto.setId(branche.getSerial());
                    branchDto.setNameAr(branche.getNameAr());
                    branchDto.setNameEn(branche.getNameEn());
                    branchDTOList.add(branchDto);
                }
                data.setBranchList(branchDTOList);
            }
            dTO.setData(data);
        }
        return dTO;
    }

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
