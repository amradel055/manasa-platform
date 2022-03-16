/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.RscQotationPriceTenderDetailWorkUnitDTO;
import com.isag.dto.RscWorkUnitsGuideDTO;
import com.isag.entity.IsagUser;
import com.isag.entity.RscPriceTenderDetailWorkUnit;
import com.isag.entity.RscQotationPriceTender;
import com.isag.entity.RscQotationPriceTenderDetailWorkUnit;
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
public class RscQotationPriceTenderDetailWorkUnitServiceImpl implements RscQotationPriceTenderDetailWorkUnitService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<RscQotationPriceTenderDetailWorkUnitDTO> getBranchQotationPriceTenderWorkUnitByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<RscQotationPriceTenderDetailWorkUnitDTO> dTOList = new ArrayList<>();
        List<RscQotationPriceTenderDetailWorkUnit> list = dao.findListByQuery("SELECT acc FROM RscQotationPriceTenderDetailWorkUnit acc WHERE acc.branchId.id=:branchId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public RscQotationPriceTenderDetailWorkUnitDTO findTenderDetailWorkUnit(Integer workUnitsGuide) {
        if (workUnitsGuide != null) {
            RscQotationPriceTenderDetailWorkUnit guide = dao.findEntityById(RscQotationPriceTenderDetailWorkUnit.class, workUnitsGuide);
            return mapTODTO(guide);
        } else {
            return null;
        }
    }

    @Override
    public void addTenderDetailWorkUnitList(List<RscQotationPriceTenderDetailWorkUnitDTO> tenderDetailWorkUnitDTOlist, RscQotationPriceTender tenderId, IsagUser isagUser) {
        for (RscQotationPriceTenderDetailWorkUnitDTO detailWorkUnitDTO : tenderDetailWorkUnitDTOlist) {
            RscQotationPriceTenderDetailWorkUnit detailWorkUnit = mapFromDTO(detailWorkUnitDTO, tenderId, isagUser);

            if (detailWorkUnit.getId() == null) {
                dao.saveEntity(detailWorkUnit);
            } else {
                dao.updateEntity(detailWorkUnit);
            }
        }

    }

    @Override
    public void deleteTenderDetailWorkUnitList(List<RscQotationPriceTenderDetailWorkUnitDTO> tenderDetailWorkUnitDTOlist) {
        for (RscQotationPriceTenderDetailWorkUnitDTO detailWorkUnitDTO : tenderDetailWorkUnitDTOlist) {
            RscPriceTenderDetailWorkUnit detailWorkUnit = new RscPriceTenderDetailWorkUnit();
            detailWorkUnit.setId(detailWorkUnitDTO.getId());
            dao.deleteEntity(detailWorkUnit);
        }

    }

    @Override
    public void deleteTenderDetailWorkUnit(RscQotationPriceTenderDetailWorkUnitDTO unit, IsagUser isagUser) {
        RscQotationPriceTenderDetailWorkUnit rscTender = mapFromDTO(unit, null, isagUser);
        dao.deleteEntity(rscTender);
    }

    @Override
    public List<RscQotationPriceTenderDetailWorkUnitDTO> getRscQotationPriceTenderDetailWorkUnitByTenderId(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        List<RscQotationPriceTenderDetailWorkUnitDTO> dTOList = new ArrayList<>();
        List<RscQotationPriceTenderDetailWorkUnit> list = dao.findListByQuery("SELECT acc FROM RscQotationPriceTenderDetailWorkUnit acc WHERE  acc.qotationPriceTenderId.id = :id", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }
    
    @Override
    public void deleteRscQotationPriceTenderDetailWorkUnitByTenderId(Integer TenderId) { 
         dao.executeDeleteQuery("DELETE  FROM RscQotationPriceTenderDetailWorkUnit acc WHERE  acc.qotationPriceTenderId.id = "+TenderId);
    }
    
    @Override
    public List<RscQotationPriceTenderDetailWorkUnitDTO> getAllTenderRoots(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        List<RscQotationPriceTenderDetailWorkUnitDTO> dTOList = new ArrayList<>();
        List<RscQotationPriceTenderDetailWorkUnit> list = dao.findListByQuery("SELECT acc FROM RscQotationPriceTenderDetailWorkUnit acc WHERE acc.branchId.id = :id");
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<RscQotationPriceTenderDetailWorkUnitDTO> getAllQotationPriceTenderWorkUnitByBranchIdAndserial(Integer branchId, Integer serial) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        params.put("serial", serial);

        List<RscQotationPriceTenderDetailWorkUnitDTO> dTOList = new ArrayList<>();
        List<RscQotationPriceTenderDetailWorkUnit> list = dao.findListByQuery("SELECT acc FROM RscPriceTenderDetailWorkUnit acc WHERE acc.branchId.id = :id AND acc.serial = :serial ", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<RscQotationPriceTenderDetailWorkUnitDTO> getAllQotationPriceTenderWorkUnitByBranchIdAndserialAndParent(Integer branchId, Integer serial, Integer parentId) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        params.put("serial", serial);

        String sql = "SELECT acc FROM RscQotationPriceTenderDetailWorkUnit acc WHERE acc.branchId.id = :id AND acc.serial = :serial ";
        List<RscQotationPriceTenderDetailWorkUnitDTO> dTOList = new ArrayList<>();
        List<RscQotationPriceTenderDetailWorkUnit> list = dao.findListByQuery(sql, params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    public RscQotationPriceTenderDetailWorkUnitDTO mapTODTO(RscQotationPriceTenderDetailWorkUnit guide) {
        RscQotationPriceTenderDetailWorkUnitDTO dTO = new RscQotationPriceTenderDetailWorkUnitDTO();
        initMapToDTO(dTO, guide);

        return dTO;
    }

    private void initMapToDTO(RscQotationPriceTenderDetailWorkUnitDTO dTO, RscQotationPriceTenderDetailWorkUnit guide) {
        dTO.setId(guide.getId());
        dTO.setQuantity(guide.getQuantity());
        dTO.setRemark(guide.getRemark());
        dTO.setSerial(guide.getSerial());
        dTO.setStatus(guide.getStatus());
        dTO.setPrice(guide.getPrice());
        dTO.setTotal(guide.getTotal());
        dTO.setIndex(guide.getId());
        dTO.setQotationPriceTenderId(guide.getQotationPriceTenderId() == null ? null : guide.getQotationPriceTenderId().getId());
        dTO.setCreatedBy(guide.getCreatedBy().getId());
        dTO.setCreatedDate(guide.getCreationDate());
        if (guide.getWorkUnitId() != null) {
            RscWorkUnitsGuideDTO guideDTO = new RscWorkUnitsGuideDTO();
            guideDTO.setId(guide.getWorkUnitId().getId());
            guideDTO.setCode(guide.getWorkUnitId().getCode());
            guideDTO.setName(guide.getWorkUnitId().getName());
            guideDTO.setUnitName(guide.getWorkUnitId().getUnitId());
            guideDTO.setQuantity(guide.getWorkUnitId().getQuantity());
            guideDTO.setValue(guide.getWorkUnitId().getValue());
            dTO.setGuideDTO(guideDTO);
            dTO.setGuideDTOBak(guideDTO);

        }
        dTO.setWorkUnitId(guide.getWorkUnitId() == null ? null : guide.getWorkUnitId().getId());

    }

    public RscQotationPriceTenderDetailWorkUnit mapFromDTO(RscQotationPriceTenderDetailWorkUnitDTO dTO, RscQotationPriceTender tenderId, IsagUser isagUser) {
        RscQotationPriceTenderDetailWorkUnit tenderDetail = new RscQotationPriceTenderDetailWorkUnit();

        tenderDetail.setId(dTO.getId());
        tenderDetail.setQuantity(dTO.getQuantity());
        tenderDetail.setRemark(dTO.getRemark());
        tenderDetail.setSerial(dTO.getSerial());
        tenderDetail.setStatus(dTO.getStatus());
        tenderDetail.setPrice(dTO.getPrice());
        tenderDetail.setTotal(dTO.getTotal());

        if (tenderDetail.getQotationPriceTenderId() == null) {
            tenderDetail.setQotationPriceTenderId(tenderId);
        }
        if (dTO.getGuideDTO() != null && dTO.getGuideDTO().getId() != null) {
            RscWorkUnitsGuide rscWorkUnitsGuide = new RscWorkUnitsGuide();
            rscWorkUnitsGuide.setId(dTO.getGuideDTO().getId());
            rscWorkUnitsGuide.setCode(dTO.getGuideDTO().getCode());
            rscWorkUnitsGuide.setName(dTO.getGuideDTO().getName());
            rscWorkUnitsGuide.setUnitId(dTO.getGuideDTO().getUnitName());
            tenderDetail.setWorkUnitId(rscWorkUnitsGuide);
        }

        if (dTO.getId() == null) {
            tenderDetail.setCreatedBy(isagUser);
            tenderDetail.setCreationDate(new Date());
        } else {
            IsagUser user = new IsagUser();
            user.setId(dTO.getCreatedBy());
            tenderDetail.setCreatedBy(user);
            tenderDetail.setCreationDate(dTO.getCreatedDate());
            tenderDetail.setModificationDate(new Date());
            tenderDetail.setModifiedBy(isagUser);
        }

        if (isagUser != null) {
            tenderDetail.setBranchId(isagUser.getBranchId());
            tenderDetail.setCompanyId(isagUser.getCompanyId());
        }
        return tenderDetail;
    }

    private void returnListDTO(List<RscQotationPriceTenderDetailWorkUnit> list, List<RscQotationPriceTenderDetailWorkUnitDTO> dTOList) {
        for (RscQotationPriceTenderDetailWorkUnit guide : list) {
            dTOList.add(mapTODTO(guide));
        }
    }
}
