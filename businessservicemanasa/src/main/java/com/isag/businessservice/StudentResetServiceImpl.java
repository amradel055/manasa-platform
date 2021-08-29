/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.StudentResetDTO;
import com.isag.entity.StudentReset;
import com.isag.entity.IgmaUser;
import com.isag.entity.Student;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author hq003
 */
@Stateless
public class StudentResetServiceImpl implements StudentResetService {

    @EJB
    private GenericDAO dao;

    public Boolean validateStudentReset(StudentResetDTO studentResetDTO) {
        Map<String, Object> params = new HashMap();
        params.put("studentCode", studentResetDTO.getStudentCode());
        List<StudentReset> studentMonth = dao.findListByQuery(" SELECT e FROM StudentReset e WHERE e.studentCode = :studentCode  ", params);
        if (studentMonth != null && !studentMonth.isEmpty()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public void deleteStudentReset(Integer studentResetDTO) {
        StudentReset entity = new StudentReset();
        entity.setId(studentResetDTO);
        dao.deleteEntity(entity);
    }

    public void sentSMS(String code) {
        String to = "amradel055@gmail.com";
        String from = "amradel@isag-it.com";
        String host = "mail.isag-it.com"; //or IP address  

        //Get the session object  
        Properties properties = System.getProperties();
        properties.setProperty(" mail.smtp.host", host);
        Session session = Session.getDefaultInstance(properties);

        //compose the message  
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            //message.setSubject("Ping");
            message.setSubject(" verification code ");
            //message.setText(" Hello, this is example of sending email  ");
            message.setText(code);

            // Send message  
            Transport.send(message);
            System.out.println(" message sent successfully.... ");

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public String GenerateRandomCode() {
        String AlphaNumericString = "0123456789" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int codeLength = 6;
        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(codeLength);

        for (int i = 0; i < codeLength; i++) {
            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int) (AlphaNumericString.length() * Math.random());
            // add Character one by one in end of sb
            sb.append(AlphaNumericString.charAt(index));
        }
        String finalCode = sb.toString();
        return finalCode;
    }

    @Override
    public StudentResetDTO saveGeneratorCode(StudentResetDTO studentResetDTO, IgmaUser isagUser) {
        String code = GenerateRandomCode();
        sentSMS(code);
        studentResetDTO.setStudentCode(code);
        if (studentResetDTO.getId() != null) {
            StudentReset reset = mapToEntity(studentResetDTO, isagUser);
            reset = dao.updateEntity(reset);
            studentResetDTO = mapTODTO(reset);
        }
        return studentResetDTO;
    }

    @Override
    public StudentResetDTO save(StudentResetDTO studentResetDTO, IgmaUser isagUser) {
        if (studentResetDTO.getStudentCode() != null) {
            StudentReset reset = mapToEntity(studentResetDTO, isagUser);
            reset = dao.updateEntity(reset);
            studentResetDTO = mapTODTO(reset);
        }
        return studentResetDTO;
    }

    private List<StudentResetDTO> returnListDTO(List<StudentReset> list) {
        List<StudentResetDTO> dTOList = new ArrayList<>();
        for (StudentReset entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private StudentResetDTO mapTODTO(StudentReset entity) {
        StudentResetDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    private StudentResetDTO initMapToDTO(StudentReset entity) {

        StudentResetDTO dTO = new StudentResetDTO();
        dTO.setId(entity.getId());
        dTO.setStudentCode(entity.getStudentCode());

        if (entity.getStudentId() != null) {
            dTO.setStudentId(entity.getStudentId().getId());
        }

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private StudentReset mapToEntity(StudentResetDTO dTO, IgmaUser isagUser) {

        StudentReset entity = new StudentReset();

        entity.setId(dTO.getId());
        entity.setStudentCode(dTO.getStudentCode());

        if (dTO.getStudentId() != null) {
            Student student = new Student();
            student.setId(dTO.getStudentId());
            entity.setStudentId(student);
        }

        if (dTO.getId() == null) {

            entity.setCreatedBy(isagUser);
            entity.setBranchId(isagUser.getBranchId());
            entity.setCreationDate(new Date());
        } else {
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);
        }

        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }

}
