/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.RscAccrualDeductionDTO;
import com.isag.entity.IsagUser;

import com.isag.entity.RscAccrualDeduction;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq004
 */
@Stateless
public class RscAccrualDeductionServiceImpl implements RscAccrualDeductionService {

    @EJB
    private GenericDAO dao;

    @EJB
    private GenericService genericService;

    @Override
    public List<RscAccrualDeductionDTO> getBranchRscAccrualActiveByBranch(Integer branchId,Integer type1,Integer type2) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("type1", type1);
        params.put("type2", type2);
        List<RscAccrualDeductionDTO> dTOList = new ArrayList<>();
        List<RscAccrualDeduction> list = dao.findListByQuery("SELECT acc FROM RscAccrualDeduction acc WHERE acc.branchId.id=:branchId AND acc.type in (:type1,:type2) ", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public RscAccrualDeductionDTO findRscAccrual(Integer accrualId) {
        if (accrualId != null) {
            RscAccrualDeduction accrual = dao.findEntityById(RscAccrualDeduction.class, accrualId);
            return mapTODTO(accrual, null);
        } else {
            return null;
        }
    }

    @Override
    public RscAccrualDeductionDTO addRscAccrual(RscAccrualDeductionDTO accrualDTO, IsagUser isagUser) {

        RscAccrualDeduction accrual = mapFromDTO(accrualDTO, isagUser);

        if (accrual.getId() == null) {
            Integer serial = genericService.getMaxSerialForInvoice(RscAccrualDeduction.class, accrual.getBranchId().getId(), null, null);
            accrual.setSerial(serial);
        }

        //------------------------
        accrual = dao.updateEntity(accrual);

        accrualDTO = mapTODTO(accrual, accrualDTO);

        return accrualDTO;

    }

    @Override
    public void deleteRscAccrual(RscAccrualDeductionDTO accrualDTO, IsagUser isagUser) {
        RscAccrualDeduction accrual = new RscAccrualDeduction();
        accrual.setId(accrualDTO.getId());

        dao.deleteEntity(accrual);
    }

    @Override
    public RscAccrualDeductionDTO getAllRscAccrualByBranchIdAndserial(Integer branchId, Integer serial) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        params.put("serial", serial);

        List<RscAccrualDeductionDTO> dTOList = new ArrayList<>();
        List<RscAccrualDeduction> list = dao.findListByQuery("SELECT acc FROM RscAccrualDeduction acc WHERE acc.branchId.id = :id AND acc.serial = :serial ", params);
        returnListDTO(list, dTOList);
        return dTOList == null ? null : dTOList.get(0);

    }

    @Override
    public List<RscAccrualDeductionDTO> getAllRscAccrualByBranchIdAndserialAndID(Integer branchId, Integer serial, Integer selectedId) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        params.put("serial", serial);
        params.put("selectedId", selectedId);
        String sql = "SELECT acc FROM RscAccrual acc WHERE acc.id != :selectedId and acc.branchId.id = :id AND acc.serial = :serial ";
        List<RscAccrualDeductionDTO> dTOList = new ArrayList<>();
        List<RscAccrualDeduction> list = dao.findListByQuery(sql, params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<RscAccrualDeductionDTO> getRscAccuralDeductionByBranchAndType(Integer branchId, Integer type) {
        Map<String, Object> params = new HashMap();

        params.put("type", type);
        params.put("branchId", branchId);
        List<RscAccrualDeductionDTO> dTOList = new ArrayList<>();
        List<RscAccrualDeduction> list = dao.findListByQuery("SELECT acc FROM RscAccrualDeduction acc WHERE acc.branchId.id=:branchId AND acc.type=:type", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    public RscAccrualDeductionDTO mapTODTO(RscAccrualDeduction accrual, RscAccrualDeductionDTO dTO) {
        if (dTO == null) {
            dTO = new RscAccrualDeductionDTO();
        }
        initMapToDTO(dTO, accrual);

        return dTO;
    }

    private void initMapToDTO(RscAccrualDeductionDTO dTO, RscAccrualDeduction accrual) {

        dTO.setId(accrual.getId());
        dTO.setSerial(accrual.getSerial());
        dTO.setIndex(accrual.getId());
        dTO.setCreatedDate(accrual.getCreationDate());
        dTO.setCreatedBy(accrual.getCreatedBy().getId());
        dTO.setBranchId(accrual.getBranchId().getId());
        dTO.setRscAccrualDeductionCode(accrual.getCode());
        dTO.setRscAccrualDeductionName(accrual.getName());
        dTO.setRscAccrualDeductionType(accrual.getType());

    }

    public RscAccrualDeduction mapFromDTO(RscAccrualDeductionDTO dTO, IsagUser isagUser) {
        RscAccrualDeduction accrual = new RscAccrualDeduction();
        accrual.setCode(dTO.getRscAccrualDeductionCode());
        accrual.setName(dTO.getRscAccrualDeductionName());
        accrual.setType(dTO.getRscAccrualDeductionType());
        if (dTO.getId() == null) {
            accrual.setCreatedBy(isagUser);
            accrual.setCreationDate(new Date());
        } else {
            IsagUser isagUser1 = new IsagUser();
            isagUser1.setId(dTO.getCreatedBy());
            accrual.setCreatedBy(isagUser1);
            accrual.setCreationDate(dTO.getCreatedDate());
            accrual.setId(dTO.getId());
            accrual.setModificationDate(new Date());
            accrual.setModifiedBy(isagUser);
        }

        accrual.setBranchId(isagUser.getBranchId());
        accrual.setCompanyId(isagUser.getCompanyId());
        return accrual;
    }

    private void returnListDTO(List<RscAccrualDeduction> list, List<RscAccrualDeductionDTO> dTOList) {
        for (RscAccrualDeduction rscAccrual : list) {
            dTOList.add(mapTODTO(rscAccrual, null));
        }
    }

}
