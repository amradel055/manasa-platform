/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.IsagApiTokenDTO;
import com.isag.dto.StudentDTO;
import com.isag.entity.Employee;
import com.isag.entity.IsagApiToken;
import com.isag.entity.IsagUser;
import com.isag.entity.Student;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author AhmedEssam
 */
@Stateless
public class IsagApiTokenImpl implements IsagApiTokenService {

    @EJB
    private GenericDAO dao;

    @Override
    public IsagApiTokenDTO addIsagApiToken(IsagApiTokenDTO isagApiTokenDTO, Employee employee) {
        IsagApiToken entity = mapFromDTO(isagApiTokenDTO, employee);
        entity = dao.updateEntity(entity);
        return mapTODTO(entity, null);
    }
    
    @Override
    public IsagApiTokenDTO addIsagApiToken(IsagApiTokenDTO isagApiTokenDTO, StudentDTO studentDTO) {
        IsagApiToken entity = mapFromDTOStudent(isagApiTokenDTO, studentDTO);
        entity = dao.updateEntity(entity);
        return mapTODTO(entity, null);
    }
    
    @Override
    public IsagApiTokenDTO addIsagApiToken(IsagApiTokenDTO isagApiTokenDTO, IsagUser isagUser) {
        IsagApiToken entity = mapToEntity(isagApiTokenDTO);
        entity = dao.updateEntity(entity);
        return mapTODTO(entity, null);
    }

    @Override
    public IsagApiTokenDTO getIsagApiTokenDTOByTokenAndTokenID(IsagApiTokenDTO isagApiTokenDTO) {
        Map<String, Object> params = new HashMap<>();
        params.put("tokenId", isagApiTokenDTO.getTokenId());
        params.put("token", isagApiTokenDTO.getToken());
        IsagApiToken isagApiToken = dao.findEntityByQuery("SELECT t FROM IsagApiToken t WHERE t.tokenId  = :tokenId  AND t.token = :token ", params);
        IsagApiTokenDTO apiTokenDTO = new IsagApiTokenDTO();
        mapTODTO(isagApiToken, apiTokenDTO);
        return apiTokenDTO;
    }

    private List<IsagApiTokenDTO> returnListDTO(List<IsagApiToken> list) {
        List<IsagApiTokenDTO> dTOList = new ArrayList<>();
        for (IsagApiToken entity : list) {
            dTOList.add(mapTODTO(entity, null));
        }
        return dTOList;
    }

    public IsagApiTokenDTO mapTODTO(IsagApiToken entity, IsagApiTokenDTO dTO) {
        if (dTO == null) {
            dTO = new IsagApiTokenDTO();
        }
        initMapToDTO(dTO, entity);
        return dTO;
    }
    
    @Override
    public void deleteByStudentId(Integer studentId) {
        String sql = "delete from IsagApiToken d where d.id > 0 AND d.studentId.id = " + studentId;
        dao.executeDeleteQuery(sql);
    }

    private void initMapToDTO(IsagApiTokenDTO dTO, IsagApiToken entity) {
        dTO.setToken(entity.getToken());
        dTO.setTokenId(entity.getTokenId());
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy()==null?null:entity.getCreatedBy().getId());
        dTO.setCompanyId(entity.getCompanyId() != null ? entity.getCompanyId().getId() : null);
    }

    private IsagApiToken mapFromDTO(IsagApiTokenDTO dTO, Employee employee) {
        IsagApiToken entity = new IsagApiToken();
        entity.setToken(dTO.getToken());
        entity.setTokenId(dTO.getTokenId());
        if (dTO.getId() == null) {
            entity.setCreatedByEmployee(employee);
            entity.setCreationDate(new Date());
        } else {
            Employee employee1 = new Employee();
            employee1.setId(dTO.getCreatedBy());
            entity.setCreatedByEmployee(employee1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
        }
        entity.setCompanyId(employee.getCompanyId()); 
        return entity;
    }

    private IsagApiToken mapFromDTOStudent(IsagApiTokenDTO dTO, StudentDTO studentDTO) {
        IsagApiToken entity = new IsagApiToken();
        entity.setToken(dTO.getToken());
        entity.setTokenId(dTO.getTokenId());
        if (dTO.getId() == null) {
            Student student = new Student();
            student.setId(studentDTO.getId());
            student.setCode(studentDTO.getCode());
            entity.setStudentId(student);
            entity.setCreationDate(new Date());
        } else {
            Student student = new Student();
            student.setId(studentDTO.getId());
            entity.setStudentId(student);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
        }
//        entity.setCompanyId(studentDTO.getRemCompany()); 
        return entity;
    }
    
    private IsagApiToken mapToEntity(IsagApiTokenDTO dTO) {
        IsagApiToken entity = new IsagApiToken();
        entity.setToken(dTO.getToken());
        entity.setTokenId(dTO.getTokenId());
         if (dTO.getId() == null) {
            entity.setCreationDate(new Date());
        } else {
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
        }
//        entity.setCompanyId(studentDTO.getRemCompany()); 
        return entity;
    }
}
