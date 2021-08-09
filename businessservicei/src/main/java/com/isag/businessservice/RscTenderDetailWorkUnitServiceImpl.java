/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.RscTenderDetailWorkUnitDTO;
import com.isag.dto.RscWorkUnitsGuideDTO;
import com.isag.entity.IsagUser;
import com.isag.entity.RscTender;
import com.isag.entity.RscTenderDetailWorkUnit;
import com.isag.entity.RscWorkUnitsGuide;
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
 * @author hq004
 */
@Stateless
public class RscTenderDetailWorkUnitServiceImpl implements RscTenderDetailWorkUnitService {

    @EJB
    private GenericDAO dao;
    @EJB
    private GenericService genericService;

    @Override
    public List<RscTenderDetailWorkUnitDTO> getBranchRscTenderDetailWorkUnitByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<RscTenderDetailWorkUnitDTO> dTOList = new ArrayList<>();
        List<RscTenderDetailWorkUnit> list = dao.findListByQuery("SELECT acc FROM RscTenderDetailWorkUnit acc WHERE acc.branchId.id=:branchId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public RscTenderDetailWorkUnitDTO findTenderDetailWorkUnit(Integer tenderDetailWorkUnit) {
        if (tenderDetailWorkUnit != null) {
            RscTenderDetailWorkUnit guide = dao.findEntityById(RscTenderDetailWorkUnit.class, tenderDetailWorkUnit);
            return mapTODTO(guide);
        } else {
            return null;
        }
    }

    @Override
    public String addTenderDetailWorkUnitList(List<RscTenderDetailWorkUnitDTO> tenderDetailWorkUnitDTOlist, Integer tenderId, IsagUser isagUser) {
        Map<Integer, RscTenderDetailWorkUnit> map = new HashMap<>();
        for (RscTenderDetailWorkUnitDTO detailWorkUnitDTO : tenderDetailWorkUnitDTOlist) {
            RscTenderDetailWorkUnit detailWorkUnit = mapFromDTO(detailWorkUnitDTO, tenderId, isagUser);
            if (detailWorkUnit.getMsg() != null && !detailWorkUnit.getMsg().isEmpty()) {
                return detailWorkUnit.getMsg();
            } else {
                if (detailWorkUnit.getId() == null) {
                    Integer serial = genericService.getMaxSerialForInvoice(RscTenderDetailWorkUnit.class, detailWorkUnit.getBranchId().getId(), null, null);
                    detailWorkUnit.setSerial(serial);
                    dao.saveEntity(detailWorkUnit);
                } else {
                    dao.updateEntity(detailWorkUnit);
                }
            }
        }
        return null;
    }

    @Override
    public void deleteTenderDetailWorkUnitList(List<RscTenderDetailWorkUnitDTO> tenderDetailWorkUnitDTOlist, IsagUser isagUser) {
        for (RscTenderDetailWorkUnitDTO detailWorkUnitDTO : tenderDetailWorkUnitDTOlist) {
            RscTenderDetailWorkUnit detailWorkUnit = mapFromDTO(detailWorkUnitDTO, null, isagUser);
            dao.deleteEntity(detailWorkUnit);
        }

    }

    @Override
    public void deleteTenderDetailWorkUnit(RscTenderDetailWorkUnitDTO unit, IsagUser isagUser) {
        RscTenderDetailWorkUnit rscTender = mapFromDTO(unit, null, isagUser);
        dao.deleteEntity(rscTender);
    }

    @Override
    public List<RscTenderDetailWorkUnitDTO> getTenderDetailWorkUnitByTenderId(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        List<RscTenderDetailWorkUnitDTO> dTOList = new ArrayList<>();
        List<RscTenderDetailWorkUnit> list = dao.findListByQuery("SELECT acc FROM RscTenderDetailWorkUnit acc WHERE acc.parent IS NULL AND acc.tenderId.id = :id", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }
    
    @Override
    public Map<Integer, RscTenderDetailWorkUnit> getMapTenderDetailWorkUnitByTenderId(Integer id) {
        Map<String, Object> params = new HashMap();
        Map<Integer, RscTenderDetailWorkUnit> mapTender = new HashMap();
        params.put("id", id);
        List<RscTenderDetailWorkUnitDTO> dTOList = new ArrayList<>();
        List<RscTenderDetailWorkUnit> list = dao.findListByQuery("SELECT acc FROM RscTenderDetailWorkUnit acc WHERE acc.parent IS NULL AND acc.tenderId.id = :id", params);
        for(RscTenderDetailWorkUnit detailWorkUnit : list){
            mapTender.put(detailWorkUnit.getWorkUnitId().getId(), detailWorkUnit);
        }
        return mapTender;
    }

    @Override
    public List<RscTenderDetailWorkUnitDTO> getAllTenderRoots(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        List<RscTenderDetailWorkUnitDTO> dTOList = new ArrayList<>();
        List<RscTenderDetailWorkUnit> list = dao.findListByQuery("SELECT acc FROM RscTenderDetailWorkUnit acc WHERE acc.parent IS NULL AND acc.branchId.id = :id");
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<RscTenderDetailWorkUnitDTO> getAllRscTenderDetailWorkUnitByBranchIdAndserial(Integer branchId, Integer serial) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        params.put("serial", serial);

        List<RscTenderDetailWorkUnitDTO> dTOList = new ArrayList<>();
        List<RscTenderDetailWorkUnit> list = dao.findListByQuery("SELECT acc FROM RscTenderDetailWorkUnit acc WHERE acc.branchId.id = :id AND acc.serial = :serial ", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<RscTenderDetailWorkUnitDTO> getAllRscTenderDetailWorkUnitByBranchIdAndserialAndParent(Integer branchId, Integer serial, Integer parentId) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        params.put("serial", serial);
        params.put("parentId", parentId);
        String sql = "SELECT acc FROM RscTenderDetailWorkUnit acc WHERE acc.parent.id != :parentId and acc.branchId.id = :id AND acc.serial = :serial ";
        List<RscTenderDetailWorkUnitDTO> dTOList = new ArrayList<>();
        List<RscTenderDetailWorkUnit> list = dao.findListByQuery(sql, params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    public RscTenderDetailWorkUnitDTO mapTODTO(RscTenderDetailWorkUnit guide) {
        RscTenderDetailWorkUnitDTO dTO = new RscTenderDetailWorkUnitDTO();
        initMapToDTO(dTO, guide);
        if (guide.getParent() != null) {
            dTO.setParent(new RscTenderDetailWorkUnitDTO());
            initMapToDTO(dTO.getParent(), guide.getParent());
        }
        return dTO;
    }

    private void initMapToDTO(RscTenderDetailWorkUnitDTO dTO, RscTenderDetailWorkUnit guide) {
        dTO.setId(guide.getId());
        dTO.setIndex(guide.getId());
        dTO.setQuantity(guide.getQuantity());
        dTO.setRemark(guide.getRemark());
        dTO.setSerial(guide.getSerial());
        dTO.setStatus(guide.getStatus());
        dTO.setTotal(guide.getTotal());
        dTO.setPrice(guide.getPrice());
        dTO.setRemarks(guide.getRemarks());
        dTO.setTenderId(guide.getTenderId() == null ? null : guide.getTenderId().getId());
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
        dTO.setCreatedDate(guide.getCreationDate());
        dTO.setCreatedBy(guide.getCreatedBy().getId());
        dTO.setWorkUnitId(guide.getWorkUnitId() == null ? null : guide.getWorkUnitId().getId());
    }

    public RscTenderDetailWorkUnit mapFromDTO(RscTenderDetailWorkUnitDTO dTO, Integer tenderId, IsagUser isagUser) {
        RscTenderDetailWorkUnit tenderDetail = new RscTenderDetailWorkUnit();

        tenderDetail.setId(dTO.getId());
        if (dTO.getQuantity() != null && dTO.getQuantity().compareTo(BigDecimal.ZERO) > 0) {
            tenderDetail.setQuantity(dTO.getQuantity());
        } else {
            tenderDetail.setMsg("\n يجب ادخال كمية");
        }
        tenderDetail.setRemark(dTO.getRemark());
        tenderDetail.setSerial(dTO.getSerial());
        tenderDetail.setStatus(dTO.getStatus());
        tenderDetail.setRemarks(dTO.getRemarks());
        if (dTO.getPrice() != null && dTO.getPrice().compareTo(BigDecimal.ZERO) > 0) {
            tenderDetail.setPrice(dTO.getPrice());
        } else {
            tenderDetail.setMsg("\n يجب ادخال سعر");
        }
        tenderDetail.setTotal(dTO.getTotal());

        if (dTO.getGuideDTO() != null && dTO.getGuideDTO().getId() != null) {
            RscWorkUnitsGuide rscWorkUnitsGuide = new RscWorkUnitsGuide();
            rscWorkUnitsGuide.setId(dTO.getGuideDTO().getId());
            rscWorkUnitsGuide.setCode(dTO.getGuideDTO().getCode());
            rscWorkUnitsGuide.setName(dTO.getGuideDTO().getName());
            rscWorkUnitsGuide.setUnitId(dTO.getGuideDTO().getUnitName());
            rscWorkUnitsGuide.setValue(dTO.getGuideDTO().getValue());
            rscWorkUnitsGuide.setQuantity(dTO.getGuideDTO().getQuantity());
            tenderDetail.setWorkUnitId(rscWorkUnitsGuide);
        } else if (dTO.getGuideDTOBak() != null && dTO.getGuideDTOBak() != null) {
            RscWorkUnitsGuide rscWorkUnitsGuide = new RscWorkUnitsGuide();
            rscWorkUnitsGuide.setId(dTO.getGuideDTOBak().getId());
            rscWorkUnitsGuide.setCode(dTO.getGuideDTOBak().getCode());
            rscWorkUnitsGuide.setName(dTO.getGuideDTOBak().getName());
            rscWorkUnitsGuide.setUnitId(dTO.getGuideDTOBak().getUnitName());
            tenderDetail.setWorkUnitId(rscWorkUnitsGuide);
        } else {
            tenderDetail.setMsg("\n يوجد بند فارغ يجب اختيار بند");
        }

        if (tenderDetail.getTenderId() == null && tenderId != null) {
            RscTender tender = new RscTender();
            tender.setId(tenderId);
            tenderDetail.setTenderId(tender);
        }

        if (dTO.getId() == null) {
            tenderDetail.setCreatedBy(isagUser);
            tenderDetail.setCreationDate(new Date());
        } else {
            tenderDetail.setModificationDate(new Date());
            tenderDetail.setModifiedBy(isagUser);
            if (isagUser != null) {
                IsagUser user = new IsagUser();
                user.setId(dTO.getCreatedBy());
                tenderDetail.setCreatedBy(user);
                tenderDetail.setCreationDate(dTO.getCreatedDate());
            }
        }

        if (isagUser != null) {
            tenderDetail.setBranchId(isagUser.getBranchId());
            tenderDetail.setCompanyId(isagUser.getCompanyId());
        }
        return tenderDetail;
    }

    private void returnListDTO(List<RscTenderDetailWorkUnit> list, List<RscTenderDetailWorkUnitDTO> dTOList) {
        for (RscTenderDetailWorkUnit guide : list) {
            dTOList.add(mapTODTO(guide));
        }
    }
}
