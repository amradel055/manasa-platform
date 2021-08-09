/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.RemPointsDTO;
import com.isag.entity.IsagUser;
import com.isag.entity.RemPoints;
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
public class RemPointsServiceImpl implements RemPointsService {

    @EJB
    private GenericDAO dao;

    @Override
    public RemPointsDTO findPoint(Integer pointId) {
        if (pointId != null) {
            RemPoints entity = dao.findEntityById(RemPoints.class, pointId);
            return mapTODTO(entity);
        } else {
            return null;
        }
    }

    @Override
    public void deletePoint(RemPointsDTO pointsDTO) {
        RemPoints points = new RemPoints();
        points.setId(pointsDTO.getId());
        dao.deleteEntity(points);
    }

    @Override
    public RemPointsDTO save(RemPointsDTO pointsDTO, IsagUser isagUser) {
        RemPoints bus = mapToEntity(pointsDTO, isagUser);
        bus = dao.updateEntity(bus);
        pointsDTO = mapTODTO(bus);
        return pointsDTO;
    }

    @Override
    public List<RemPointsDTO> findPoints(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        String query = "SELECT r from RemPoints r where r.branchId.id = :branchId";
        List<RemPoints> buseseList = dao.findListByQuery(query, params);
        List<RemPointsDTO> listDTO = returnListDTO(buseseList);
        return listDTO;
    }

    private List<RemPointsDTO> returnListDTO(List<RemPoints> list) {
        List<RemPointsDTO> dTOList = new ArrayList<>();
        for (RemPoints entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private RemPointsDTO mapTODTO(RemPoints entity) {
        RemPointsDTO dTO = initMapToDTO(entity);
        return dTO;
    }

    private RemPointsDTO initMapToDTO(RemPoints entity) {

        RemPointsDTO dTO = new RemPointsDTO();
        dTO.setId(entity.getId());

        dTO.setName(entity.getName());
        dTO.setLat(entity.getLat());
        dTO.setLng(entity.getLng());

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private RemPoints mapToEntity(RemPointsDTO dTO, IsagUser isagUser) {
        RemPoints entity = new RemPoints();

        entity.setName(dTO.getName());
        entity.setLat(dTO.getLat());
        entity.setLng(dTO.getLng());

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

}
