/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.GovernoratesDTO;
import com.isag.dto.StatesDTO;
import com.isag.entity.Governorates;
import com.isag.entity.States;
import com.isag.entity.IgmaUser;
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
public class StatesServiceImpl implements StatesService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<StatesDTO> findStatesBygovernorate(Integer governorateId) {
        Map<String, Object> params = new HashMap<>();
        String query = "";
        if (governorateId != null) {
            params.put("governorateId", governorateId);
            query = "SELECT b FROM States b WHERE b.governorateId.id = :governorateId order by b.id desc";
        }
        List<States> statesList = dao.findListByQuery(query, params);
        List<StatesDTO> listDTO = returnListDTO(statesList);
        return (listDTO != null && !listDTO.isEmpty()) ? listDTO : null;
    }

    private List<StatesDTO> returnListDTO(List<States> list) {
        List<StatesDTO> dTOList = new ArrayList<>();
        for (States entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private StatesDTO mapTODTO(States entity) {
        StatesDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    private StatesDTO initMapToDTO(States entity) {

        StatesDTO dTO = new StatesDTO();
        dTO.setId(entity.getId());

        dTO.setName(entity.getName());

        if (entity.getGovernorateId() != null) {
            GovernoratesDTO governoratesDTO = new GovernoratesDTO();
            governoratesDTO.setId(entity.getGovernorateId().getId());
            governoratesDTO.setName(entity.getGovernorateId().getName());
            dTO.setGovernorateId(governoratesDTO);

        }

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private States mapToEntity(StatesDTO dTO, IgmaUser isagUser) {
        States entity = new States();

        entity.setName(dTO.getName());
        if (dTO.getGovernorateId() != null) {
            Governorates governorates = new Governorates();
            governorates.setId(dTO.getGovernorateId().getId());
            governorates.setName(dTO.getGovernorateId().getName());
            entity.setGovernorateId(governorates);
        }

        if (dTO.getId() == null) {

            entity.setCreatedBy(isagUser);
            entity.setBranchId(isagUser.getBranchId());
            entity.setCreationDate(new Date());
        } else {
            IgmaUser isagUser1 = new IgmaUser();
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
