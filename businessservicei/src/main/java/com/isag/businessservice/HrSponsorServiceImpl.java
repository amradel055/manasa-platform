/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.HrSponsorDTO;
import com.isag.entity.HrSponsor;
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
 * @author `*"<:ahmedShaaban:>"*`
 */
@Stateless
public class HrSponsorServiceImpl implements HrSponsorService {

    @EJB
    private GenericDAO dao;
    @EJB
    private GenericService genericService;

    @Override
    public HrSponsorDTO findHrSponsorByEmployeeId(Integer employeeId) {
        HrSponsor hrSponsor = dao.findEntityById(HrSponsor.class, employeeId);
        HrSponsorDTO hrSponsorDTO = mapTODTO(hrSponsor, null);
        return hrSponsorDTO;
    }

    @Override
    public void deleteHrSponsor(HrSponsorDTO hrSponsorDTO, IsagUser isagUser) {
        HrSponsor entity = new HrSponsor();
        entity.setId(hrSponsorDTO.getId());
        dao.deleteEntity(entity);
    }

    @Override
    public HrSponsorDTO addHrSponsor(HrSponsorDTO hrSponsorDTO, IsagUser isagUser) {

        HrSponsor entity = mapFromDTO(hrSponsorDTO, isagUser);

        entity = dao.updateEntity(entity);
        hrSponsorDTO = mapTODTO(entity, hrSponsorDTO);
        return hrSponsorDTO;
    }

    public HrSponsorDTO mapTODTO(HrSponsor entity, HrSponsorDTO dTO) {
        if (dTO == null) {
            dTO = new HrSponsorDTO();
        }
        initMapToDTO(dTO, entity);
        return dTO;
    }

    private void initMapToDTO(HrSponsorDTO dTO, HrSponsor entity) {
        dTO.setName(entity.getName());
        dTO.setIndex(entity.getId());
        dTO.setId(entity.getId());
        dTO.setCode(entity.getCode());
        dTO.setCreatedDate(entity.getCreationDate() != null ? entity.getCreationDate() : new Date());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
    }

    public HrSponsor mapFromDTO(HrSponsorDTO dTO, IsagUser isagUser) {
        HrSponsor entity = new HrSponsor();
        entity.setName(dTO.getName());
        entity.setId(dTO.getId());
        entity.setCode(dTO.getCode());
        if (dTO.getId() == null) {
            entity.setCreatedBy(isagUser);
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

    private void returnListDTO(List<HrSponsor> list, List<HrSponsorDTO> dTOList) {
        for (HrSponsor entity : list) {
            dTOList.add(mapTODTO(entity, null));
        }
    }

    @Override
    public List<HrSponsorDTO> getSponsorsByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        List<HrSponsorDTO> dTOList = new ArrayList<>();
        params.put("companyId", companyId);
        List<HrSponsor> list = dao.findListByQuery("SELECT h FROM HrSponsor h WHERE h.companyId.id=:companyId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

}
