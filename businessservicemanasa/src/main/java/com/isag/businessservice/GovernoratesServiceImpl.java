/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.GovernoratesDTO;
import com.isag.entity.Governorates;
import com.isag.entity.IsagUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq003
 */
@Stateless
public class GovernoratesServiceImpl implements GovernoratesService {

    @EJB
    private GenericDAO dao;

    @Override
    public GovernoratesDTO findGovernorate(Integer governorateId) {
        if (governorateId != null) {
            Governorates entity = dao.findEntityById(Governorates.class, governorateId);
            return mapTODTO(entity);
        } else {
            return null;
        }
    }

    @Override
    public List<GovernoratesDTO> findGovernorate() {
        List<Governorates> list = dao.findAll(Governorates.class);
        return returnListDTO(list);
    }

    private List<GovernoratesDTO> returnListDTO(List<Governorates> list) {
        List<GovernoratesDTO> dTOList = new ArrayList<>();
        for (Governorates entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private GovernoratesDTO mapTODTO(Governorates entity) {
        GovernoratesDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    private GovernoratesDTO initMapToDTO(Governorates entity) {

        GovernoratesDTO dTO = new GovernoratesDTO();
        dTO.setId(entity.getId());
        dTO.setName(entity.getName());

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private Governorates mapToEntity(GovernoratesDTO dTO, IsagUser isagUser) {
        Governorates entity = new Governorates();

        entity.setName(dTO.getName());

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
