/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.general;

import com.isag.businessservice.StudentPeriodsService;
import com.isag.businessservice.StudentService;
import com.isag.dto.APIDataDTO;
import com.isag.dto.APIStudentDataDTO;
import com.isag.dto.IgmaApiTokenDTO;
import com.isag.dto.APIloginDTO;
import com.isag.dto.StudentDTO;
import com.isag.root.LoginRoot;
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
import com.isag.entity.IgmaUser;
import com.isag.entity.IgmaUserLogin;
import java.util.List;
import com.isag.businessservice.IgmaApiTokenService;
import com.isag.businessservice.IgmaUserLoginService;
import com.isag.businessservice.IgmaUserService;
import com.isag.dto.StudentPeriodsDTO;
import com.isag.views.TeachersByStudentId;

/**
 *
 * @author ahmedEssam
 */
@Stateless
public class GeneralImp implements GeneralService {

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String secret = "remas@2019@Secret";

    @EJB
    private StudentService studentService;
    @EJB
    private IgmaApiTokenService isagApiTokenService;
    @EJB
    StudentPeriodsService periodsService;
    @EJB
    private IgmaUserService userService;
    @EJB
    private IgmaUserLoginService isagUserLoginService;
    @EJB
    private StudentPeriodsService studentPeriodsService;

//    private Employee emp;
    private StudentDTO studentDTO;
//    private IgmaUser isagUser;

    @Override
    public APIloginDTO authenticateStudent(LoginRoot loginRoot) {

        String userName = loginRoot.userName;
        String password = loginRoot.password;
        studentDTO = studentService.authenticateUser(userName, password, loginRoot.branchId, loginRoot.companyId);
        IgmaUserLogin isagUserLogin = isagUserLoginService.findIsagUserLoginByStudentIdLast(studentDTO.getId());

        if (isagUserLogin != null && isagUserLogin.getState() == 1) {
            if (isagUserLogin.getMacId() != null && loginRoot.macId != null && !isagUserLogin.getMacId().equals(loginRoot.macId) && studentDTO.getMacId() != null) {
                return jsonTokenStudent(null, Boolean.FALSE, "يجب الخروج أولا من الجهاز الاخر ");
            }
        }

        studentDTO.setMacId(loginRoot.macId);
        if (studentDTO.getId() != null) {
            studentService.updateStudentMacId(studentDTO);
        }
        if (studentDTO != null && studentDTO.getBranchId() != null) {
            System.out.println("Logged In Successfully");
            isagUserLoginService.saveUserData(loginRoot.branchId, loginRoot.companyId, loginRoot.macId, studentDTO.getId(), 1);
            return issueTokenStudent();
        } else {
            return jsonTokenStudent(null, Boolean.FALSE, "عفوا اسم المستخدم او كلمه المرور غير صحيحه");
        }
    }

    @Override
    public APIloginDTO authenticateManasa(LoginRoot loginRoot) {

        String userName = loginRoot.userName;
        String password = loginRoot.password;
        IgmaUser loggedInUser = userService.authenticateUser(userName, password);
        //loggedInUser.getMaster() دي معناها لازم يكون مدير
        if (loggedInUser != null) {
            System.out.println("Logged In Successfully");
//            isagUser = loggedInUser;
            return issueTokenManasa(loggedInUser);
        } else {
            return jsonTokenManasa(null, Boolean.FALSE, "عفوا اسم المستخدم او كلمه المرور غير صحيحه", loggedInUser);
        }
    }

    public APIloginDTO issueTokenStudent() {
        try {
            String token = createJWT();
            String tokenId = DecodeJWT(token);
            IgmaApiTokenDTO isagApiTokenDTO = new IgmaApiTokenDTO();
            isagApiTokenDTO.setToken(token);
            isagApiTokenDTO.setTokenId(tokenId);
            isagApiTokenService.addIsagApiToken(isagApiTokenDTO, studentDTO);
            return jsonTokenStudent(token, Boolean.TRUE, null);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(GeneralImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jsonTokenStudent(null, Boolean.FALSE, null);

    }

    @Override
    public APIloginDTO issueTokenManasa(IgmaUser isagUser) {
        try {
            String token = createJWT();
            String tokenId = DecodeJWT(token);
            IgmaApiTokenDTO isagApiTokenDTO = new IgmaApiTokenDTO();
            isagApiTokenDTO.setToken(token);
            isagApiTokenDTO.setTokenId(tokenId);
            isagApiTokenService.addIsagApiToken(isagApiTokenDTO, isagUser);
            return jsonTokenManasa(token, Boolean.TRUE, null, isagUser);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(GeneralImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jsonTokenManasa(null, Boolean.FALSE, null, isagUser);

    }

    private APIloginDTO jsonTokenManasa(String token, Boolean success, String msg, IgmaUser isagUser) {
        APIloginDTO dTO = new APIloginDTO();
        dTO.setSuccess(success);
        dTO.setMsg(msg);
        dTO.setToken(token);
        if (isagUser.getParent() != null) {
            dTO.setTeacherId(isagUser.getParent().getId());
        } else {
            dTO.setTeacherId(isagUser.getId());
        }
        if (success.equals(Boolean.TRUE)) {
            APIDataDTO data = new APIDataDTO();
            data.setName(isagUser == null ? "" : isagUser.getName());
            data.setUserId(isagUser == null ? null : isagUser.getId());
            dTO.setData(data);
        }
        return dTO;
    }

    private APIloginDTO jsonTokenStudent(String token, Boolean success, String msg) {
        APIloginDTO dTO = new APIloginDTO();
        dTO.setSuccess(success);
        dTO.setMsg(msg);
        dTO.setToken(token);
        if (success.equals(Boolean.TRUE)) {
            APIStudentDataDTO data = new APIStudentDataDTO();
            data.setCode(studentDTO.getCode());
            data.setId(studentDTO.getId());
            data.setFirstName(studentDTO.getFirstName());
            data.setFatherName(studentDTO.getFatherName());
            data.setGrandFatherName(studentDTO.getGrandFatherName());
            data.setFamilyName(studentDTO.getFamilyName());
            data.setEmail(studentDTO.getEmail());
            data.setMobile(studentDTO.getMobile());
            data.setSchoolStagesId(studentDTO.getSchoolStagesId().getId());
            data.setImagePath(studentDTO.getImagePath());
            List<Integer> periodList = periodsService.findPeriodsByStudentId(studentDTO.getId());
            List<TeachersByStudentId> teachersList = periodsService.findteachersByStudentId(studentDTO.getId());
            data.setPeriodList(periodList);
            data.setTeacherList(teachersList);
            dTO.setTeacherList(studentPeriodsService.findteachersByStudentIdLogin(studentDTO.getId()));
            dTO.setStudentData(data);
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
