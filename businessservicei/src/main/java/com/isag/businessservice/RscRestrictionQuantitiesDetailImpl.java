/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.RscContractRegisterDTO;
import com.isag.dto.RscQotationPriceTenderDTO;
import com.isag.dto.RscRestrictionQuantitiesDTO;
import com.isag.dto.RscRestrictionQuantitiesDetailDTO;
import com.isag.dto.RscWorkUnitsGuideDTO;
import com.isag.entity.IsagUser;
import com.isag.entity.RscRestrictionQuantities;
import com.isag.entity.RscRestrictionQuantitiesDetail;
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
public class RscRestrictionQuantitiesDetailImpl implements RscRestrictionQuantitiesDetailService {

    @EJB
    private GenericDAO dao;
    @EJB
    private GenericService genericService;

    @Override
    public List<RscRestrictionQuantitiesDetailDTO> getBranchRscRestrictionQuantitiesDetailByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<RscRestrictionQuantitiesDetailDTO> dTOList = new ArrayList<>();
        List<RscRestrictionQuantitiesDetail> list = dao.findListByQuery("SELECT acc FROM RscRestrictionQuantitiesDetail acc WHERE acc.branchId.id=:branchId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public RscRestrictionQuantitiesDetailDTO findRestrictionQuantitiesDetail(Integer workUnitsGuide) {
        if (workUnitsGuide != null) {
            RscRestrictionQuantitiesDetail quantitiesDetail = dao.findEntityById(RscRestrictionQuantitiesDetail.class, workUnitsGuide);
            return mapTODTO(quantitiesDetail);
        } else {
            return null;
        }
    }

    @Override
    public void addRestrictionQuantitiesDetailList(List<RscRestrictionQuantitiesDetailDTO> rscRestrictionQuantitiesDetailDTOlist, RscRestrictionQuantities restrictionQuantities, IsagUser isagUser) {

        for (RscRestrictionQuantitiesDetailDTO detailDTO : rscRestrictionQuantitiesDetailDTOlist) {

            RscRestrictionQuantitiesDetail detail = mapFromDTO(detailDTO, restrictionQuantities, isagUser);

            if (detail.getId() == null) {
                Integer serial = genericService.getMaxSerialForInvoice(RscRestrictionQuantitiesDetail.class, detail.getBranchId().getId(), null, null);
                detail.setSerial(serial);
                dao.saveEntity(detail);
            } else {
                dao.updateEntity(detail);
            }
        }

    }

    @Override
    public List<RscRestrictionQuantitiesDetailDTO> getRestrictionQuantitiesDetailByRestrictionId(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        List<RscRestrictionQuantitiesDetailDTO> dTOList = new ArrayList<>();
        List<RscRestrictionQuantitiesDetail> list = dao.findListByQuery("SELECT acc FROM RscRestrictionQuantitiesDetail acc WHERE acc.rscRestrictionQuantities.id = :id", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public void updateRestrictionQuantitiesDetailList(List<RscRestrictionQuantitiesDetail> quantitiesDetailList) {
        for (RscRestrictionQuantitiesDetail detail : quantitiesDetailList) {
            dao.updateEntity(detail);
        }
    }

    @Override
    public void deleteRestrictionQuantitiesDetailList(List<RscRestrictionQuantitiesDetailDTO> detailDTOList) {
        for (RscRestrictionQuantitiesDetailDTO detailDTO : detailDTOList) {
            RscRestrictionQuantitiesDetail detail = new RscRestrictionQuantitiesDetail();
            detail.setId(detailDTO.getId());
            dao.deleteEntity(detail);
        }

    }
    
    
       @Override
    public void deleteRestrictionQuantitiesDetailListByExtractID(Integer RestrictionQuantitiesID, Integer Branchid) {
          Map<String, Object> params = new HashMap();
        params.put("RestrictionQuantitiesID", RestrictionQuantitiesID);
         params.put("Branchid", Branchid);
       dao.executeDeleteQuery("Delete From RscRestrictionQuantitiesDetail acc WHERE acc.rscRestrictionQuantities.id = "+RestrictionQuantitiesID+" AND acc.branchId.id = "+Branchid);

    }

    @Override
    public void deleteRestrictionQuantitiesDetail(RscRestrictionQuantitiesDetailDTO unit, IsagUser isagUser) {
        RscRestrictionQuantitiesDetail detail = mapFromDTO(unit, null, isagUser);
        dao.deleteEntity(detail);
    }

    @Override
    public List<RscRestrictionQuantitiesDetailDTO> getAllRscRestrictionQuantitiesDetailByBranchIdAndserial(Integer branchId, Integer serial) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        params.put("serial", serial);

        List<RscRestrictionQuantitiesDetailDTO> dTOList = new ArrayList<>();
        List<RscRestrictionQuantitiesDetail> list = dao.findListByQuery("SELECT acc FROM RscRestrictionQuantitiesDetail acc WHERE acc.branchId.id = :id AND acc.serial = :serial ", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    public RscRestrictionQuantitiesDetailDTO mapTODTO(RscRestrictionQuantitiesDetail detail) {
        RscRestrictionQuantitiesDetailDTO dTO = new RscRestrictionQuantitiesDetailDTO();
        initMapToDTO(dTO, detail);
        return dTO;
    }

    private void initMapToDTO(RscRestrictionQuantitiesDetailDTO dTO, RscRestrictionQuantitiesDetail detailRestriction) {
        dTO.setId(detailRestriction.getId());
        dTO.setSerial(detailRestriction.getSerial());
        dTO.setIndex(detailRestriction.getId());
        dTO.setCreatedDate(detailRestriction.getCreationDate());
        dTO.setCreatedBy(detailRestriction.getCreatedBy().getId());
        dTO.setExcuteQty(detailRestriction.getContractQty());
        dTO.setContractPrice(detailRestriction.getContractPrice());

        // dTO.setRscExtractRegister(detailextract.getRscExtractRegister()== null ? null : detailextract.getRscExtractRegister());
        if (detailRestriction.getWorkUnitId() != null) {
            RscWorkUnitsGuideDTO guideDTO = new RscWorkUnitsGuideDTO();
            guideDTO.setId(detailRestriction.getWorkUnitId().getId());
            guideDTO.setCode(detailRestriction.getWorkUnitId().getCode());
            guideDTO.setName(detailRestriction.getWorkUnitId().getName());
            guideDTO.setUnitName(detailRestriction.getWorkUnitId().getUnitId());
            guideDTO.setQuantity(detailRestriction.getWorkUnitId().getQuantity());
            guideDTO.setValue(detailRestriction.getWorkUnitId().getValue());
            dTO.setGuideDTO(guideDTO);
            dTO.setGuideDTOBak(guideDTO);
        }

        dTO.setWorkUnitId(detailRestriction.getWorkUnitId() == null ? null : detailRestriction.getWorkUnitId().getId());
        if (detailRestriction.getRscRestrictionQuantities() != null) {
            RscRestrictionQuantitiesDTO quantitiesDTO = new RscRestrictionQuantitiesDTO();
            quantitiesDTO.setId(detailRestriction.getRscRestrictionQuantities().getId());

            dTO.setRscRestrictionQuantities(quantitiesDTO);

        }
        
      if(detailRestriction.getRscRestrictionQuantities() != null && detailRestriction.getRscRestrictionQuantities().getContractId() != null  ) { 
          RscContractRegisterDTO rscContractRegisterDTO = new RscContractRegisterDTO();
          rscContractRegisterDTO.setId( detailRestriction.getRscRestrictionQuantities().getContractId().getId());
          if( detailRestriction.getRscRestrictionQuantities().getContractId().getRscQotationPriceTenderId() != null){
              RscQotationPriceTenderDTO qotationPriceTenderDTO = new RscQotationPriceTenderDTO();
              qotationPriceTenderDTO.setId( detailRestriction.getRscRestrictionQuantities().getContractId().getRscQotationPriceTenderId().getId());
             rscContractRegisterDTO.setRscQotationPriceTenderId(qotationPriceTenderDTO);
          } 
          dTO.getRscRestrictionQuantities().setContractId(rscContractRegisterDTO);
      }
    }

    public RscRestrictionQuantitiesDetail mapFromDTO(RscRestrictionQuantitiesDetailDTO dTO, RscRestrictionQuantities restrictionQuantities, IsagUser isagUser) {
        RscRestrictionQuantitiesDetail detail = new RscRestrictionQuantitiesDetail();

        detail.setId(dTO.getId());
        detail.setContractQty(dTO.getExcuteQty());
        detail.setContractPrice(dTO.getContractPrice());
        detail.setSerial(dTO.getSerial());

        if (detail.getRscRestrictionQuantities() == null) {
            detail.setRscRestrictionQuantities(restrictionQuantities);
        }
        if (dTO.getGuideDTO() != null && dTO.getGuideDTO().getId() != null) {
            RscWorkUnitsGuide rscWorkUnitsGuide = new RscWorkUnitsGuide();
            rscWorkUnitsGuide.setId(dTO.getGuideDTO().getId());
            rscWorkUnitsGuide.setName(dTO.getGuideDTO().getName());
            rscWorkUnitsGuide.setCode(dTO.getGuideDTO().getCode());
            rscWorkUnitsGuide.setQuantity(dTO.getGuideDTO().getQuantity());
            rscWorkUnitsGuide.setValue(dTO.getGuideDTO().getValue());
            rscWorkUnitsGuide.setUnitId(dTO.getGuideDTO().getUnitName());
            detail.setWorkUnitId(rscWorkUnitsGuide);

        }

        if (dTO.getRscRestrictionQuantities() != null) {
            RscRestrictionQuantities quantities = new RscRestrictionQuantities();
            quantities.setId(dTO.getRscRestrictionQuantities().getId());
            quantities.setSerial(dTO.getRscRestrictionQuantities().getSerial());
            detail.setRscRestrictionQuantities(quantities);

        }

        if (dTO.getId() == null) {
            IsagUser isagUser1 = new IsagUser();
            isagUser1.setId(dTO.getCreatedBy());
            detail.setCreatedBy(isagUser1);
            detail.setCreationDate(dTO.getCreatedDate());
            detail.setCreatedBy(isagUser);
            detail.setCreationDate(new Date());
        } else {
            detail.setModificationDate(new Date());
            detail.setModifiedBy(isagUser);
        }

        if (isagUser != null) {
            detail.setBranchId(isagUser.getBranchId());
            detail.setCompanyId(isagUser.getCompanyId());
        }
        return detail;
    }

    private void returnListDTO(List<RscRestrictionQuantitiesDetail> list, List<RscRestrictionQuantitiesDetailDTO> dTOList) {
        for (RscRestrictionQuantitiesDetail quantitiesDetail : list) {
            dTOList.add(mapTODTO(quantitiesDetail));
        }
    }

}
