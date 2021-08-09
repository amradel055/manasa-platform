/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.RemBusesDTO;
import com.isag.entity.RemBuses;
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
public class RemBusesServiceImpl implements RemBusesService {

    @EJB
    private GenericDAO dao;

    @Override
    public RemBusesDTO findBus(Integer busId) {
        if (busId != null) {
            RemBuses entity = dao.findEntityById(RemBuses.class, busId);
            return mapTODTO(entity);
        } else {
            return null;
        }
    }

    @Override
    public void deleteBuses(RemBusesDTO busesDTO) {
        RemBuses buses = new RemBuses();
        buses.setId(busesDTO.getId());
        dao.deleteEntity(buses);
    }

    @Override
    public RemBusesDTO save(RemBusesDTO busesDTO, IsagUser isagUser) {
        RemBuses bus = mapToEntity(busesDTO, isagUser);
        bus = dao.updateEntity(bus);
        busesDTO = mapTODTO(bus);
        return busesDTO;
    }

    @Override
    public List<RemBusesDTO> findBuses(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        String query = "";
        if (branchId != null) {
            params.put("branchId", branchId);
            query = "SELECT b FROM RemBuses b WHERE b.branchId.id = :branchId";
        } 
        List<RemBuses> buseseList = dao.findListByQuery(query, params);
        List<RemBusesDTO> listDTO = returnListDTO(buseseList);
        return listDTO;
    }

    private List<RemBusesDTO> returnListDTO(List<RemBuses> list) {
        List<RemBusesDTO> dTOList = new ArrayList<>();
        for (RemBuses entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private RemBusesDTO mapTODTO(RemBuses entity) {
        RemBusesDTO dTO = initMapToDTO(entity);
        return dTO;
    }

    private RemBusesDTO initMapToDTO(RemBuses entity) {

        RemBusesDTO dTO = new RemBusesDTO();
        dTO.setId(entity.getId());

        dTO.setName(entity.getName());
        dTO.setChairNumber(entity.getChairNumber());
        dTO.setCode(entity.getCode());

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private RemBuses mapToEntity(RemBusesDTO dTO, IsagUser isagUser) {
        RemBuses entity = new RemBuses();

        entity.setName(dTO.getName());
        entity.setCode(dTO.getCode());
        entity.setChairNumber(dTO.getChairNumber());

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
