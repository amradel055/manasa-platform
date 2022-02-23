/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.RscContractRegisterDetailWorkUnitDTO;
import com.isag.dto.RscWorkUnitsGuideDTO;
import com.isag.entity.IsagUser;
import com.isag.entity.RscContractRegister;
import com.isag.entity.RscContractRegisterDetailWorkUnit;
import com.isag.entity.RscWorkUnitsGuide;
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
public class RscContractRegisterDetailWorkUnitImpl implements RscContractRegisterDetailWorkUnitService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<RscContractRegisterDetailWorkUnitDTO> getBranchContractRegisterDetailWorkUnitByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<RscContractRegisterDetailWorkUnitDTO> dTOList = new ArrayList<>();
        List<RscContractRegisterDetailWorkUnit> list = dao.findListByQuery("SELECT acc FROM RscContractRegisterDetailWorkUnit acc WHERE acc.branchId.id=:branchId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public RscContractRegisterDetailWorkUnitDTO findContractDetailWorkUnit(Integer workUnitsGuide) {
        if (workUnitsGuide != null) {
            RscContractRegisterDetailWorkUnit guide = dao.findEntityById(RscContractRegisterDetailWorkUnit.class, workUnitsGuide);
            return mapTODTO(guide);
        } else {
            return null;
        }
    }

    @Override
    public void addContractDetailWorkUnitList(List<RscContractRegisterDetailWorkUnitDTO> contractDetailWorkUnitDTOlist, RscContractRegister contractId, IsagUser isagUser) {
        for (RscContractRegisterDetailWorkUnitDTO detailWorkUnitDTO : contractDetailWorkUnitDTOlist) {
            RscContractRegisterDetailWorkUnit detailWorkUnit = mapFromDTO(detailWorkUnitDTO, contractId, isagUser);

            if (detailWorkUnit.getId() == null) {
                dao.saveEntity(detailWorkUnit);
            } else {
                dao.updateEntity(detailWorkUnit);
            }
        }

    }

    @Override
    public void deleteContractDetailWorkUnitList(List<RscContractRegisterDetailWorkUnitDTO> contractDetailWorkUnitDTOlist) {
        for (RscContractRegisterDetailWorkUnitDTO detailWorkUnitDTO : contractDetailWorkUnitDTOlist) {
            RscContractRegisterDetailWorkUnit detailWorkUnit = new RscContractRegisterDetailWorkUnit();
            detailWorkUnit.setId(detailWorkUnitDTO.getId());
            dao.deleteEntity(detailWorkUnit);
        }

    }

    @Override
    public void deleteContractDetailWorkUnit(RscContractRegisterDetailWorkUnitDTO unit, IsagUser isagUser) {
        RscContractRegisterDetailWorkUnit rscContract = mapFromDTO(unit, null, isagUser);
        dao.deleteEntity(rscContract);
    }

    @Override
    public List<RscContractRegisterDetailWorkUnitDTO> getContractDetailWorkUnitByContractId(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        List<RscContractRegisterDetailWorkUnitDTO> dTOList = new ArrayList<>();
        List<RscContractRegisterDetailWorkUnit> list = dao.findListByQuery("SELECT acc FROM RscContractRegisterDetailWorkUnit acc WHERE  acc.rscContractRegisterId.id = :id", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<RscContractRegisterDetailWorkUnitDTO> getAllContractRoots(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        List<RscContractRegisterDetailWorkUnitDTO> dTOList = new ArrayList<>();
        List<RscContractRegisterDetailWorkUnit> list = dao.findListByQuery("SELECT acc FROM RscContractRegisterDetailWorkUnit acc WHERE acc.branchId.id = :id");
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<RscContractRegisterDetailWorkUnitDTO> getAllContractRegisterDetailWorkUnitByBranchIdAndserial(Integer branchId, Integer serial) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        params.put("serial", serial);

        List<RscContractRegisterDetailWorkUnitDTO> dTOList = new ArrayList<>();
        List<RscContractRegisterDetailWorkUnit> list = dao.findListByQuery("SELECT acc FROM RscPriceTenderDetailWorkUnit acc WHERE acc.branchId.id = :id AND acc.serial = :serial ", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<RscContractRegisterDetailWorkUnitDTO> getAllContractRegisterDetailWorkUnitByBranchIdAndserialAndParent(Integer branchId, Integer serial, Integer parentId) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        params.put("serial", serial);

        String sql = "SELECT acc FROM RscContractRegisterDetailWorkUnit acc WHERE acc.branchId.id = :id AND acc.serial = :serial ";
        List<RscContractRegisterDetailWorkUnitDTO> dTOList = new ArrayList<>();
        List<RscContractRegisterDetailWorkUnit> list = dao.findListByQuery(sql, params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    public RscContractRegisterDetailWorkUnitDTO mapTODTO(RscContractRegisterDetailWorkUnit guide) {
        RscContractRegisterDetailWorkUnitDTO dTO = new RscContractRegisterDetailWorkUnitDTO();
        initMapToDTO(dTO, guide);

        return dTO;
    }

    private void initMapToDTO(RscContractRegisterDetailWorkUnitDTO dTO, RscContractRegisterDetailWorkUnit contract) {
        dTO.setId(contract.getId());
        dTO.setQuantity(contract.getQuantity());
        dTO.setRemark(contract.getRemark());
        dTO.setSerial(contract.getSerial());
        dTO.setTotal(contract.getTotal());
        dTO.setPrice(contract.getPrice());
        dTO.setIndex(contract.getId());
        dTO.setCreatedBy(contract.getCreatedBy().getId());
        dTO.setCreatedDate(contract.getCreationDate());

        if (contract.getWorkUnitId() != null) {
            RscWorkUnitsGuideDTO guideDTO = new RscWorkUnitsGuideDTO();
            guideDTO.setId(contract.getWorkUnitId().getId());
            guideDTO.setCode(contract.getWorkUnitId().getCode());
            guideDTO.setName(contract.getWorkUnitId().getName());
            guideDTO.setUnitName(contract.getWorkUnitId().getUnitId());
            guideDTO.setQuantity(contract.getWorkUnitId().getQuantity());
            guideDTO.setValue(contract.getWorkUnitId().getValue());
            dTO.setGuideDTO(guideDTO);
            dTO.setGuideDTOBak(guideDTO);
        }
          dTO.setWorkUnitId(contract.getWorkUnitId() == null ? null : contract.getWorkUnitId().getId());

    }

    public RscContractRegisterDetailWorkUnit mapFromDTO(RscContractRegisterDetailWorkUnitDTO dTO, RscContractRegister contractId, IsagUser isagUser) {
        RscContractRegisterDetailWorkUnit contractDetail = new RscContractRegisterDetailWorkUnit();

        contractDetail.setId(dTO.getId());
        contractDetail.setQuantity(dTO.getQuantity());
        contractDetail.setRemark(dTO.getRemark());
        contractDetail.setSerial(dTO.getSerial());
        contractDetail.setTotal(dTO.getTotal());
        contractDetail.setPrice(dTO.getPrice());
        if (contractDetail.getRscContractRegisterId() == null) {
            contractDetail.setRscContractRegisterId(contractId);
        }
        if (dTO.getGuideDTO() != null && dTO.getGuideDTO().getId()!= null) {
            RscWorkUnitsGuide rscWorkUnitsGuide = new RscWorkUnitsGuide();
            rscWorkUnitsGuide.setId(dTO.getGuideDTO().getId());
            contractDetail.setWorkUnitId(rscWorkUnitsGuide);
            
        }
        
        
        
        if (dTO.getId() == null) {
            contractDetail.setCreatedBy(isagUser);
            contractDetail.setCreationDate(new Date());
        } else {
            IsagUser user = new IsagUser();
            user.setId(dTO.getCreatedBy());
            contractDetail.setCreatedBy(user);
            contractDetail.setCreationDate(dTO.getCreatedDate());
            contractDetail.setModificationDate(new Date());
            contractDetail.setModifiedBy(isagUser);
        }

        if (isagUser != null) {
            contractDetail.setBranchId(isagUser.getBranchId());
            contractDetail.setCompanyId(isagUser.getCompanyId());
        }
        return contractDetail;
    }

    private void returnListDTO(List<RscContractRegisterDetailWorkUnit> list, List<RscContractRegisterDetailWorkUnitDTO> dTOList) {
        for (RscContractRegisterDetailWorkUnit contract : list) {
            dTOList.add(mapTODTO(contract));
        }
    }
}
