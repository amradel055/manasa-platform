/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.RscAccrualDeductionDTO;
import com.isag.dto.RscTenderDTO;
import com.isag.dto.RscTenderRscAccrualDeductionDTO;
import com.isag.entity.IsagUser;
import com.isag.entity.RscAccrualDeduction;
import com.isag.entity.RscTender;
import com.isag.entity.RscTenderRscAccrualDeduction;
import java.math.BigDecimal;
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
public class RscTenderRscAccrualDeductionServiceImpl implements RscTenderRscAccrualDeductionService {

    @EJB
    private GenericDAO dao;
    @EJB
    private GenericService genericService;

    @Override
    public RscTenderRscAccrualDeductionDTO addRscTenderRscAccrualDeductionD(RscTenderRscAccrualDeductionDTO rscTenderRscAccrualDeductionDTO, Integer tenderId, IsagUser isagUser) {
        RscTenderRscAccrualDeduction entity = mapFromDTO(rscTenderRscAccrualDeductionDTO, tenderId, isagUser);
        entity = dao.updateEntity(entity);
        rscTenderRscAccrualDeductionDTO = mapTODTO(entity, rscTenderRscAccrualDeductionDTO);
        return rscTenderRscAccrualDeductionDTO;
    }

    @Override
    public String addRscTenderRscAccrualDeductionDList(List<RscTenderRscAccrualDeductionDTO> rscTenderRscAccrualDeductionDTOList, Integer tenderId, IsagUser isagUser) {
//        Map<Integer, RscTenderRscAccrualDeduction> map = new HashMap<>();
        for (RscTenderRscAccrualDeductionDTO detailRscTenderRscAccrualDeductionDTO : rscTenderRscAccrualDeductionDTOList) {
            RscTenderRscAccrualDeduction detailentity = mapFromDTO(detailRscTenderRscAccrualDeductionDTO, tenderId, isagUser);
            if (detailentity.getMsg() != null && !detailentity.getMsg().isEmpty()) {
                return detailentity.getMsg();
            } else {
                if (detailRscTenderRscAccrualDeductionDTO.getId() == null) {
//                Integer serial = genericService.getMaxSerialForInvoice(RscTenderRscAccrualDeduction.class, detailRscTenderRscAccrualDeductionDTO.getBranchId().getId(), null, null);
//                detailWorkUnit.setSerial(serial);
                    dao.saveEntity(detailentity);
                } else {
                    dao.updateEntity(detailentity);
                }
//            map.put(detailWorkUnit.getSerial(), detailWorkUnit);
            }
        }
        return null;
    }

    @Override
    public List<RscTenderRscAccrualDeductionDTO> getAllRscTenderRscAccrualDeductionList(Integer tenderId, Integer type) {
        Map<String, Object> params = new HashMap();
        params.put("tenderId", tenderId);
        params.put("type", type);
        List<RscTenderRscAccrualDeductionDTO> dTOList = new ArrayList<>();
        List<RscTenderRscAccrualDeduction> list = dao.findListByQuery("SELECT acc FROM RscTenderRscAccrualDeduction acc WHERE acc.rscTenderId.id = :tenderId AND acc.rscAccrualDeductionId.type = :type ", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public void deleteRscTenderRscAccrualDeductionDList(List<RscTenderRscAccrualDeductionDTO> rscTenderRscAccrualDeductionDTOList, IsagUser isagUser) {
        for (RscTenderRscAccrualDeductionDTO detailRscTenderRscAccrualDeductionDTO : rscTenderRscAccrualDeductionDTOList) {
            RscTenderRscAccrualDeduction detailentity = mapFromDTO(detailRscTenderRscAccrualDeductionDTO, null, isagUser);
            dao.deleteEntity(detailentity);
        }

    }

    private RscTenderRscAccrualDeductionDTO mapTODTO(RscTenderRscAccrualDeduction entity, RscTenderRscAccrualDeductionDTO dTO) {
        if (dTO == null) {
            dTO = new RscTenderRscAccrualDeductionDTO();
        }
        initMapToDTO(dTO, entity);
        return dTO;
    }

    private void initMapToDTO(RscTenderRscAccrualDeductionDTO dTO, RscTenderRscAccrualDeduction entity) {

        if (entity.getRscAccrualDeductionId() != null && entity.getRscAccrualDeductionId().getId() != null) {
            RscAccrualDeductionDTO parentDTO = new RscAccrualDeductionDTO();
            parentDTO.setId(entity.getRscAccrualDeductionId().getId());
            parentDTO.setRscAccrualDeductionCode(entity.getRscAccrualDeductionId().getCode());
            parentDTO.setRscAccrualDeductionName(entity.getRscAccrualDeductionId().getName());
            parentDTO.setRscAccrualDeductionType(entity.getRscAccrualDeductionId().getType());
            parentDTO.setSerial(entity.getRscAccrualDeductionId().getSerial());
            dTO.setRscAccrualDeductionId(parentDTO);
            dTO.setRscAccrualDeductionIdBak(parentDTO);
        }

        if (entity.getRscTenderId() != null && entity.getRscTenderId().getId() != null) {
            RscTenderDTO parentDTO = new RscTenderDTO();
            parentDTO.setId(entity.getRscTenderId().getId());
            parentDTO.setDocNumber(entity.getRscTenderId().getDocNumber());
            parentDTO.setDate(entity.getRscTenderId().getDate());
            parentDTO.setRemark(entity.getRscTenderId().getRemark());
            parentDTO.setSerial(entity.getRscTenderId().getSerial());
            dTO.setRscTenderId(parentDTO);
        }

        dTO.setPercentage(entity.getPercentage());
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
    }

    private RscTenderRscAccrualDeduction mapFromDTO(RscTenderRscAccrualDeductionDTO dTO, Integer tenderId, IsagUser isagUser) {
        RscTenderRscAccrualDeduction entity = new RscTenderRscAccrualDeduction();

        if (dTO.getRscAccrualDeductionId() != null && dTO.getRscAccrualDeductionId().getId() != null) {
            RscAccrualDeduction parent = new RscAccrualDeduction();
            parent.setId(dTO.getRscAccrualDeductionId().getId());
            parent.setName(dTO.getRscAccrualDeductionId().getRscAccrualDeductionName());
            parent.setCode(dTO.getRscAccrualDeductionId().getRscAccrualDeductionCode());
            parent.setSerial(dTO.getRscAccrualDeductionId().getSerial());
            entity.setRscAccrualDeductionId(parent);
        } else {
            entity.setMsg("\n يجب اختيار استحقاق / استقطاع");
        }

        if (entity.getRscTenderId() == null && tenderId != null) {
            RscTender parent = new RscTender();
            parent.setId(tenderId);
            entity.setRscTenderId(parent);
        }

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

        if (dTO.getPercentage() != null && dTO.getPercentage().compareTo(BigDecimal.ZERO) > 0) {
            entity.setPercentage(dTO.getPercentage());
        } else {
            entity.setMsg("\n يجب ادخال قيمة نسبة الاستحقاق / الاستقطاع");
        }

        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }

    private void returnListDTO(List<RscTenderRscAccrualDeduction> list, List<RscTenderRscAccrualDeductionDTO> dTOList) {
        for (RscTenderRscAccrualDeduction rscTenderRscAccrualDeduction : list) {
            dTOList.add(mapTODTO(rscTenderRscAccrualDeduction, null));
        }
    }

    @Override
    public void deleteByTenderId(Integer tenderId) {

        Map<String, Object> params = new HashMap();
        params.put("tenderId", tenderId);

        dao.executeDeleteQuery("delete from RscTenderRscAccrualDeduction d WHERE d.rscTenderId.id=" + tenderId);

    }

}
