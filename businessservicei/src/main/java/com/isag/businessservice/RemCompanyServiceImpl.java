/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;


import com.isag.core.GenericDAO;
import com.isag.dto.RemCompanyDTO;
import com.isag.entity.RemCompany;
import com.isag.entity.IsagUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq003
 */
@Stateless
public class RemCompanyServiceImpl implements RemCompanyService {

    @EJB
    private GenericDAO dao;

    @Override
    public RemCompanyDTO findCompany(Integer companyId) {
        if (companyId != null) {
            RemCompany entity = dao.findEntityById(RemCompany.class, companyId);
            return mapTODTO(entity);
        } else {
            return null;
        }
    }

    @Override
    public void deleteCompany(RemCompanyDTO companyesDTO) {
        RemCompany companyes = new RemCompany();
        companyes.setId(companyesDTO.getId());
        dao.deleteEntity(companyes);
    }

    @Override
    public RemCompanyDTO save(RemCompanyDTO companyesDTO, IsagUser isagUser) {
        RemCompany company = mapToEntity(companyesDTO, isagUser);
        company = dao.updateEntity(company);
        companyesDTO = mapTODTO(company);
        return companyesDTO;
    }

    @Override
    public List<RemCompanyDTO> findCompanys(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        String query = "";
        if (branchId != null) {
            params.put("branchId", branchId);
            query = "SELECT b FROM RemCompany b WHERE b.branchId.id = :branchId";
        } 
        List<RemCompany> companyeseList = dao.findListByQuery(query, params);
        List<RemCompanyDTO> listDTO = returnListDTO(companyeseList);
        return listDTO;
    }

    private List<RemCompanyDTO> returnListDTO(List<RemCompany> list) {
        List<RemCompanyDTO> dTOList = new ArrayList<>();
        for (RemCompany entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private RemCompanyDTO mapTODTO(RemCompany entity) {
        RemCompanyDTO dTO = initMapToDTO(entity);
        return dTO;
    }

    private RemCompanyDTO initMapToDTO(RemCompany entity) {

        RemCompanyDTO dTO = new RemCompanyDTO();
        dTO.setId(entity.getId());

        dTO.setName(entity.getName());
        dTO.setPhone(entity.getPhone());
        dTO.setAddress(entity.getAddress());
        dTO.setCode(entity.getCode());

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private RemCompany mapToEntity(RemCompanyDTO dTO, IsagUser isagUser) {
        RemCompany entity = new RemCompany();

        entity.setName(dTO.getName());
        entity.setCode(dTO.getCode());
        entity.setAddress(dTO.getAddress());
        entity.setPhone(dTO.getPhone());

        if (dTO.getId() == null) {

            entity.setCreatedBy(isagUser);
            entity.setBranchId(isagUser.getBranchId());
            entity.setCreationDate(new Date());
        } else {
            IsagUser isagUser1 = new IsagUser();
            isagUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(isagUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);
        }

        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }

}
