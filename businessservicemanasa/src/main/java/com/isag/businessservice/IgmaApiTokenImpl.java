/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.IgmaApiTokenDTO;
import com.isag.dto.StudentDTO;
import com.isag.entity.Employee;
import com.isag.entity.IgmaApiToken;
import com.isag.entity.IgmaUser;
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
public class IgmaApiTokenImpl implements IgmaApiTokenService {

    @EJB
    private GenericDAO dao;

    @Override
    public IgmaApiTokenDTO addIsagApiToken(IgmaApiTokenDTO isagApiTokenDTO, Employee employee) {
        IgmaApiToken entity = mapFromDTO(isagApiTokenDTO, employee);
        entity = dao.updateEntity(entity);
        return mapTODTO(entity, null);
    }
    
    @Override
    public IgmaApiTokenDTO addIsagApiToken(IgmaApiTokenDTO isagApiTokenDTO, StudentDTO studentDTO) {
        IgmaApiToken entity = mapFromDTOStudent(isagApiTokenDTO, studentDTO);
        entity = dao.updateEntity(entity);
        return mapTODTO(entity, null);
    }
    
    @Override
    public IgmaApiTokenDTO addIsagApiToken(IgmaApiTokenDTO isagApiTokenDTO, IgmaUser isagUser) {
        IgmaApiToken entity = mapToEntity(isagApiTokenDTO);
        entity = dao.updateEntity(entity);
        return mapTODTO(entity, null);
    }

    @Override
    public IgmaApiTokenDTO getIsagApiTokenDTOByTokenAndTokenID(IgmaApiTokenDTO isagApiTokenDTO) {
        Map<String, Object> params = new HashMap<>();
        params.put("tokenId", isagApiTokenDTO.getTokenId());
        params.put("token", isagApiTokenDTO.getToken());
        IgmaApiToken isagApiToken = dao.findEntityByQuery("SELECT t FROM IsagApiToken t WHERE t.tokenId  = :tokenId  AND t.token = :token ", params);
        IgmaApiTokenDTO apiTokenDTO = new IgmaApiTokenDTO();
        mapTODTO(isagApiToken, apiTokenDTO);
        return apiTokenDTO;
    }

    private List<IgmaApiTokenDTO> returnListDTO(List<IgmaApiToken> list) {
        List<IgmaApiTokenDTO> dTOList = new ArrayList<>();
        for (IgmaApiToken entity : list) {
            dTOList.add(mapTODTO(entity, null));
        }
        return dTOList;
    }

    public IgmaApiTokenDTO mapTODTO(IgmaApiToken entity, IgmaApiTokenDTO dTO) {
        if (dTO == null) {
            dTO = new IgmaApiTokenDTO();
        }
        initMapToDTO(dTO, entity);
        return dTO;
    }
    
    @Override
    public void deleteByStudentId(Integer studentId) {
        String sql = "delete from IsagApiToken d where d.id > 0 AND d.studentId.id = " + studentId;
        dao.executeDeleteQuery(sql);
    }

    private void initMapToDTO(IgmaApiTokenDTO dTO, IgmaApiToken entity) {
        dTO.setToken(entity.getToken());
        dTO.setTokenId(entity.getTokenId());
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy()==null?null:entity.getCreatedBy().getId());
        dTO.setCompanyId(entity.getCompanyId() != null ? entity.getCompanyId().getId() : null);
    }

    private IgmaApiToken mapFromDTO(IgmaApiTokenDTO dTO, Employee employee) {
        IgmaApiToken entity = new IgmaApiToken();
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

    private IgmaApiToken mapFromDTOStudent(IgmaApiTokenDTO dTO, StudentDTO studentDTO) {
        IgmaApiToken entity = new IgmaApiToken();
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
    
    private IgmaApiToken mapToEntity(IgmaApiTokenDTO dTO) {
        IgmaApiToken entity = new IgmaApiToken();
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
