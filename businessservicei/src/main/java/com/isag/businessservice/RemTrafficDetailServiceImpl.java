/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.RemPointsDTO;
import com.isag.dto.RemTrafficDTO;
import com.isag.dto.RemTrafficDetailDTO;
import com.isag.entity.RemTrafficDetail;
import com.isag.entity.IsagUser;
import com.isag.entity.RemPoints;
import com.isag.entity.RemTraffic;
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
public class RemTrafficDetailServiceImpl implements RemTrafficDetailService {

    @EJB
    private GenericDAO dao;

    @Override
    public RemTrafficDetailDTO findTrafficDetail(Integer trafficDetailId) {
        if (trafficDetailId != null) {
            RemTrafficDetail entity = dao.findEntityById(RemTrafficDetail.class, trafficDetailId);
            return mapTODTO(entity);
        } else {
            return null;
        }
    }

    @Override
    public List<RemTrafficDetailDTO> findAllTrafficDetail(Integer trafficId) {
        if (trafficId != null) {

            Map<String, Object> params = new HashMap<>();
            String query = "";
            params.put("trafficId", trafficId);
            query = "SELECT b FROM RemTrafficDetail b WHERE b.remTrafficId.id = :trafficId";
            List<RemTrafficDetail> trafficDetaileseList = dao.findListByQuery(query, params);
            return returnListDTO(trafficDetaileseList);
        } else {
            return null;
        }
    }

    @Override
    public void deleteTrafficDetail(RemTrafficDetailDTO trafficDetailesDTO) {
        RemTrafficDetail trafficDetailes = new RemTrafficDetail();
        trafficDetailes.setId(trafficDetailesDTO.getId());
        dao.deleteEntity(trafficDetailes);
    }

    @Override
    public void deleteAllTrafficDetail(List<RemTrafficDetailDTO> trafficDetailesDTOList) {
        if (trafficDetailesDTOList != null) {
            for (RemTrafficDetailDTO detail : trafficDetailesDTOList) {
                if (detail.getId() != null) {
                    deleteTrafficDetail(detail);
                }
            }
        }
    }

    @Override
    public RemTrafficDetailDTO save(RemTrafficDetailDTO trafficDetailesDTO, IsagUser isagUser, Integer remTrafficId) {
        RemTrafficDetail trafficDetail = mapToEntity(trafficDetailesDTO, isagUser, remTrafficId);
        trafficDetail = dao.updateEntity(trafficDetail);
        trafficDetailesDTO = mapTODTO(trafficDetail);
        return trafficDetailesDTO;
    }

    @Override
    public List<RemTrafficDetailDTO> saveAll(List<RemTrafficDetailDTO> trafficDetailesDTOList, IsagUser isagUser, Integer remTrafficId) {
        List<RemTrafficDetailDTO> dTOList = new ArrayList<>();
        for (RemTrafficDetailDTO detailDTO : trafficDetailesDTOList) {
            dTOList.add(save(detailDTO, isagUser, remTrafficId));
        }
        return dTOList;
    }

    public RemTrafficDTO addDetail(RemTrafficDTO remTrafficDTO, Integer index) {
        if (remTrafficDTO != null) {
            RemTrafficDetailDTO remTrafficDetailDTO = new RemTrafficDetailDTO();
            remTrafficDetailDTO.setIndex(index);
            remTrafficDTO.getRemTrafficDetailDTOList().add(remTrafficDetailDTO);
        }
        return remTrafficDTO;
    }

    private List<RemTrafficDetailDTO> returnListDTO(List<RemTrafficDetail> list) {
        List<RemTrafficDetailDTO> dTOList = new ArrayList<>();
        for (RemTrafficDetail entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private RemTrafficDetailDTO mapTODTO(RemTrafficDetail entity) {
        RemTrafficDetailDTO dTO = initMapToDTO(entity);
        return dTO;
    }

    private RemTrafficDetailDTO initMapToDTO(RemTrafficDetail entity) {

        RemTrafficDetailDTO dTO = new RemTrafficDetailDTO();
        dTO.setId(entity.getId());

        dTO.setRemTrafficId(entity.getRemTrafficId().getId());
        if (entity.getPointId() != null) {
            RemPointsDTO remPointsDTO = new RemPointsDTO();
            remPointsDTO.setName(entity.getPointId().getName());
            remPointsDTO.setLat(entity.getPointId().getLat());
            remPointsDTO.setLng(entity.getPointId().getLng());
            dTO.setPointId(remPointsDTO);
            dTO.setPointIdBakUp(remPointsDTO);
        }

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private RemTrafficDetail mapToEntity(RemTrafficDetailDTO dTO, IsagUser isagUser, Integer remTrafficId) {
        RemTrafficDetail entity = new RemTrafficDetail();

        if (remTrafficId != null) {
            RemTraffic remTraffic = new RemTraffic();
            remTraffic.setId(remTrafficId);
            entity.setRemTrafficId(remTraffic);
        }

        if (dTO.getPointId() != null) {
            RemPoints remPoints = new RemPoints();
            remPoints.setId(dTO.getPointId().getId());
            entity.setPointId(remPoints);
        }

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
