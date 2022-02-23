/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.RscAccrualDeductionDTO;
import com.isag.dto.RscExtractAccuralDeductionDTO;
import com.isag.dto.RscExtractRegisterDTO;
import com.isag.entity.IsagUser;
import com.isag.entity.RscAccrualDeduction;
import com.isag.entity.RscExtractAccuralDeduction;
import com.isag.entity.RscExtractRegister;
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
public class RscExtractAccrualDeductionServiceImpl implements RscExtractAccrualDeductionService {

    @EJB
    private GenericDAO dao;
    @EJB
    private GenericService genericService;

    @Override
    public List<RscExtractAccuralDeductionDTO> getBranchRscExtractAccuralDeductionByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<RscExtractAccuralDeductionDTO> dTOList = new ArrayList<>();
        List<RscExtractAccuralDeduction> list = dao.findListByQuery("SELECT acc FROM RscExtractAccuralDeduction acc WHERE acc.branchId.id=:branchId ", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<RscExtractAccuralDeductionDTO> getRscExtractAccuralOrDeductionByBranchAndTypeAndExtract(Integer ExtractId, Integer type) {
        Map<String, Object> params = new HashMap();
        params.put("type", type);
        params.put("extractId", ExtractId);
        List<RscExtractAccuralDeductionDTO> dTOList = new ArrayList<>();
        List<RscExtractAccuralDeduction> list = dao.findListByQuery("SELECT acc FROM RscExtractAccuralDeduction acc WHERE acc.rscExtractRegisterId.id=:extractId AND acc.rscAccrualDeductionId.type=:type", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }
     @Override
    public List<RscExtractAccuralDeductionDTO> getRscExtractAccuralOrDeductionByExtractForDelete(Integer ExtractId) {
        Map<String, Object> params = new HashMap();
       
        params.put("extractId", ExtractId);
        List<RscExtractAccuralDeductionDTO> dTOList = new ArrayList<>();
        List<RscExtractAccuralDeduction> list = dao.findListByQuery("SELECT acc FROM RscExtractAccuralDeduction acc WHERE acc.rscExtractRegisterId.id=:extractId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public RscExtractAccuralDeductionDTO findExtractAccuralDeduction(Integer deduction) {
        if (deduction != null) {
            RscExtractAccuralDeduction rscdeduction = dao.findEntityById(RscExtractAccuralDeduction.class, deduction);
            return mapTODTO(rscdeduction);
        } else {
            return null;
        }
    }

    @Override
    public void addExtractAccuralDeductionList(List<RscExtractAccuralDeductionDTO> deductionDTOlist, RscExtractRegister extractRegister, IsagUser isagUser) {

        for (RscExtractAccuralDeductionDTO deductionDTO : deductionDTOlist) {
            RscExtractAccuralDeduction rscDeduction = mapFromDTO(deductionDTO, extractRegister, isagUser);

            if (rscDeduction.getId() == null) {
                Integer serial = genericService.getMaxSerialForInvoice(RscExtractAccuralDeduction.class, rscDeduction.getBranchId().getId(), null, null);
                rscDeduction.setSerial(serial);
              
            }  
                dao.updateEntity(rscDeduction);
            

        }

    }

    @Override
    public void addExtractDeductionList(List<RscExtractAccuralDeductionDTO> accrualDTOlist, RscExtractRegister extractRegister, IsagUser isagUser) {

        for (RscExtractAccuralDeductionDTO accrualDTO : accrualDTOlist) {
            RscExtractAccuralDeduction rscAccrual = mapFromDTO(accrualDTO, extractRegister, isagUser);

            if (rscAccrual.getId() == null) {
                Integer serial = genericService.getMaxSerialForInvoice(RscExtractAccuralDeduction.class, rscAccrual.getBranchId().getId(), null, null);
                rscAccrual.setSerial(serial);
                dao.saveEntity(rscAccrual);
            } else {
                dao.updateEntity(rscAccrual);
            }

        }

    }

    @Override
    public void deleteExtractAccuralDeductionList(List<RscExtractAccuralDeductionDTO> deductionDTOlist, IsagUser isagUser) {
        for (RscExtractAccuralDeductionDTO rscDeductionDTO : deductionDTOlist) {
            RscExtractAccuralDeduction rscDeduction = mapFromDTO(rscDeductionDTO, null, isagUser);
            dao.deleteEntity(rscDeduction);
        }

    }

    @Override
    public void deleteExtractAccuralDeduction(RscExtractAccuralDeductionDTO deduction, IsagUser isagUser) {
        RscExtractAccuralDeduction rscDeduction = mapFromDTO(deduction, null, isagUser);
        dao.deleteEntity(rscDeduction);
    }

    @Override
    public List<RscExtractAccuralDeductionDTO> getRscExtractAccuralDeductionByExtractId(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        List<RscExtractAccuralDeductionDTO> dTOList = new ArrayList<>();
        List<RscExtractAccuralDeduction> list = dao.findListByQuery("SELECT acc FROM RscExtractAccuralDeduction acc WHERE acc.rscExtractRegisterId.id = :id", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<RscExtractAccuralDeductionDTO> getAllRscExtractAccuralDeductionByBranchIdAndserial(Integer branchId, Integer serial) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        params.put("serial", serial);

        List<RscExtractAccuralDeductionDTO> dTOList = new ArrayList<>();
        List<RscExtractAccuralDeduction> list = dao.findListByQuery("SELECT acc FROM RscExtractAccuralDeduction acc WHERE acc.branchId.id = :id AND acc.serial = :serial ", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    public RscExtractAccuralDeductionDTO mapTODTO(RscExtractAccuralDeduction rscDeduction) {
        RscExtractAccuralDeductionDTO dTO = new RscExtractAccuralDeductionDTO();
        initMapToDTO(dTO, rscDeduction);

        return dTO;
    }

    private void initMapToDTO(RscExtractAccuralDeductionDTO dTO, RscExtractAccuralDeduction deduction) {
        dTO.setId(deduction.getId());
        dTO.setIndex(deduction.getId());
        dTO.setBranchId(deduction.getBranchId().getId());
        dTO.setCreatedDate(deduction.getCreationDate());
        dTO.setCreatedBy(deduction.getCreatedBy().getId());
        dTO.setRscAccuralDeductionPercentage(deduction.getRscAccuralDeductionPercentage());
        dTO.setRscAccuralDeductionValue(deduction.getRscAccuralDeductionValue());
        dTO.setSerial(deduction.getSerial());
        dTO.setCreatedDate(deduction.getCreationDate());
        dTO.setCreatedBy(deduction.getCreatedBy().getId());

        if (deduction.getRscAccrualDeductionId() != null) {
            RscAccrualDeductionDTO accrualDTO = new RscAccrualDeductionDTO();
            accrualDTO.setId(deduction.getRscAccrualDeductionId().getId());
            accrualDTO.setRscAccrualDeductionCode(deduction.getRscAccrualDeductionId().getCode());
            accrualDTO.setRscAccrualDeductionName(deduction.getRscAccrualDeductionId().getName());
            accrualDTO.setRscAccrualDeductionType(deduction.getRscAccrualDeductionId().getType());
            accrualDTO.setSerial(deduction.getRscAccrualDeductionId().getSerial());
            dTO.setRscAccrualDeductionDTO(accrualDTO);
            dTO.setRscAccrualDeductionDTObak(accrualDTO);

        }

        if (deduction.getRscExtractRegisterId() != null) {
            RscExtractRegisterDTO extractDTO = new RscExtractRegisterDTO();
            extractDTO.setId(deduction.getRscExtractRegisterId().getId());
            extractDTO.setRemain(deduction.getRscExtractRegisterId().getRemain());
            extractDTO.setValid(deduction.getRscExtractRegisterId().getValid());
            extractDTO.setClosed(deduction.getRscExtractRegisterId().getClosed());

            dTO.setRscExtractRegisterId(extractDTO);

        }

    }

    public RscExtractAccuralDeduction mapFromDTO(RscExtractAccuralDeductionDTO dTO, RscExtractRegister extractRegister, IsagUser isagUser) {
        RscExtractAccuralDeduction rscDeduction = new RscExtractAccuralDeduction();

        rscDeduction.setId(dTO.getId());

        rscDeduction.setRscAccuralDeductionPercentage(dTO.getRscAccuralDeductionPercentage());
        rscDeduction.setRscAccuralDeductionValue(dTO.getRscAccuralDeductionValue());
        rscDeduction.setSerial(dTO.getSerial());

        if (rscDeduction.getRscExtractRegisterId() == null) {

            rscDeduction.setRscExtractRegisterId(extractRegister);
        }
        if (dTO.getRscExtractRegisterId() != null) {
            RscExtractRegister extract = new RscExtractRegister();
            extract.setId(dTO.getRscExtractRegisterId().getId());
            extract.setRemain(dTO.getRscExtractRegisterId().getRemain());
            extract.setValid(dTO.getRscExtractRegisterId().getValid());
            extract.setClosed(dTO.getRscExtractRegisterId().getClosed());

            rscDeduction.setRscExtractRegisterId(extract);
        }

        if (dTO.getRscAccrualDeductionDTO() != null && dTO.getRscAccrualDeductionDTO().getId() != null) {
            RscAccrualDeduction rscAccrual = new RscAccrualDeduction();
            rscAccrual.setId(dTO.getRscAccrualDeductionDTO().getId());
            rscAccrual.setCode(dTO.getRscAccrualDeductionDTO().getRscAccrualDeductionCode());
            rscAccrual.setName(dTO.getRscAccrualDeductionDTO().getRscAccrualDeductionName());
            rscAccrual.setType(dTO.getRscAccrualDeductionDTO().getRscAccrualDeductionType());

            rscDeduction.setRscAccrualDeductionId(rscAccrual);
        }

        if (dTO.getId() == null) {
            IsagUser isagUser1 = new IsagUser();
            isagUser1.setId(dTO.getCreatedBy());
            rscDeduction.setCreatedBy(isagUser1);
            rscDeduction.setCreationDate(dTO.getCreatedDate());
            rscDeduction.setCreatedBy(isagUser);
            rscDeduction.setCreationDate(new Date());

        } else {
            rscDeduction.setModificationDate(new Date());
            rscDeduction.setModifiedBy(isagUser);
        }

        if (isagUser != null) {
            rscDeduction.setBranchId(isagUser.getBranchId());
            rscDeduction.setCompanyId(isagUser.getCompanyId());
        }
        return rscDeduction;
    }

    private void returnListDTO(List<RscExtractAccuralDeduction> list, List<RscExtractAccuralDeductionDTO> dTOList) {
        for (RscExtractAccuralDeduction deduction : list) {
            dTOList.add(mapTODTO(deduction));
        }
    }
}
