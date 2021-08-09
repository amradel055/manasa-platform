/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.RemPointsDTO;
import com.isag.dto.RemTrafficDTO;
import com.isag.dto.RemWorkOrderDTO;
import com.isag.dto.RemWorkOrderDetailDTO;
import com.isag.entity.RemWorkOrderDetail;
import com.isag.entity.IsagUser;
import com.isag.entity.RemPoints;
import com.isag.entity.RemTraffic;
import com.isag.entity.RemWorkOrder;
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
public class RemWorkOrderDetailServiceImpl implements RemWorkOrderDetailService {

    @EJB
    private GenericDAO dao;

    @Override
    public RemWorkOrderDetailDTO findWorkOrderDetail(Integer workOrderDetailId) {
        if (workOrderDetailId != null) {
            RemWorkOrderDetail entity = dao.findEntityById(RemWorkOrderDetail.class, workOrderDetailId);
            return mapTODTO(entity);
        } else {
            return null;
        }
    }

    @Override
    public List<RemWorkOrderDetailDTO> findAllWorkOrderDetail(Integer workOrderId) {
        if (workOrderId != null) {

            Map<String, Object> params = new HashMap<>();
            String query = "";
            params.put("workOrderId", workOrderId);
            query = "SELECT b FROM RemWorkOrderDetail b WHERE b.remWorkOrder.id = :workOrderId";
            List<RemWorkOrderDetail> workOrderDetaileseList = dao.findListByQuery(query, params);
            return returnListDTO(workOrderDetaileseList);
        } else {
            return null;
        }
    }

    @Override
    public void deleteWorkOrderDetail(RemWorkOrderDetailDTO workOrderDetailesDTO) {
        RemWorkOrderDetail workOrderDetailes = new RemWorkOrderDetail();
        workOrderDetailes.setId(workOrderDetailesDTO.getId());
        dao.deleteEntity(workOrderDetailes);
    }

    @Override
    public void deleteAllWorkOrderDetail(List<RemWorkOrderDetailDTO> workOrderDetailesDTOList) {
        if (workOrderDetailesDTOList != null) {
            for (RemWorkOrderDetailDTO detail : workOrderDetailesDTOList) {
                if (detail.getId() != null) {
                    deleteWorkOrderDetail(detail);
                }
            }
        }
    }

    @Override
    public RemWorkOrderDetailDTO save(RemWorkOrderDetailDTO workOrderDetailesDTO, IsagUser isagUser, Integer remWorkOrderId) {
        RemWorkOrderDetail workOrderDetail = mapToEntity(workOrderDetailesDTO, isagUser, remWorkOrderId);
        workOrderDetail = dao.updateEntity(workOrderDetail);
        workOrderDetailesDTO = mapTODTO(workOrderDetail);
        return workOrderDetailesDTO;
    }

    @Override
    public List<RemWorkOrderDetailDTO> saveAll(List<RemWorkOrderDetailDTO> workOrderDetailesDTOList, IsagUser isagUser, Integer remWorkOrderId) {
        List<RemWorkOrderDetailDTO> dTOList = new ArrayList<>();
        for (RemWorkOrderDetailDTO detailDTO : workOrderDetailesDTOList) {
            dTOList.add(save(detailDTO, isagUser, remWorkOrderId));
        }
        return dTOList;
    }

    public RemWorkOrderDTO addDetail(RemWorkOrderDTO remWorkOrderDTO, Integer index) {
        if (remWorkOrderDTO != null) {
            RemWorkOrderDetailDTO remWorkOrderDetailDTO = new RemWorkOrderDetailDTO();
            remWorkOrderDetailDTO.setIndex(index);
            remWorkOrderDTO.getRemWorkOrderDetailDTOList().add(remWorkOrderDetailDTO);
        }
        return remWorkOrderDTO;
    }

    private List<RemWorkOrderDetailDTO> returnListDTO(List<RemWorkOrderDetail> list) {
        List<RemWorkOrderDetailDTO> dTOList = new ArrayList<>();
        for (RemWorkOrderDetail entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private RemWorkOrderDetailDTO mapTODTO(RemWorkOrderDetail entity) {
        RemWorkOrderDetailDTO dTO = initMapToDTO(entity);
        return dTO;
    }

    private RemWorkOrderDetailDTO initMapToDTO(RemWorkOrderDetail entity) {

        RemWorkOrderDetailDTO dTO = new RemWorkOrderDetailDTO();
        dTO.setId(entity.getId());

        dTO.setRemWorkOrder(entity.getRemWorkOrder().getId());
        if (entity.getTrafficId()!= null) {
            RemTrafficDTO remTrafficDTO = new RemTrafficDTO();
            remTrafficDTO.setName(entity.getTrafficId().getName());
            remTrafficDTO.setId(entity.getTrafficId().getId());
            dTO.setTrafficId(remTrafficDTO);
        }

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private RemWorkOrderDetail mapToEntity(RemWorkOrderDetailDTO dTO, IsagUser isagUser, Integer remWorkOrderId) {
        RemWorkOrderDetail entity = new RemWorkOrderDetail();

        if (remWorkOrderId != null) {
            RemWorkOrder remWorkOrder = new RemWorkOrder();
            remWorkOrder.setId(remWorkOrderId);
            entity.setRemWorkOrder(remWorkOrder);
        }

        if (dTO.getTrafficId() != null) {
            RemTraffic remTraffic = new RemTraffic();
            remTraffic.setId(dTO.getTrafficId().getId());
            entity.setTrafficId(remTraffic);
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
