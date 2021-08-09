/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.RscExtractRegisterDTO;
import com.isag.dto.RscExtractRegisterDetailDTO;
import com.isag.dto.RscWorkUnitsGuideDTO;
import com.isag.entity.IsagUser;
import com.isag.entity.RscExtractRegister;
import com.isag.entity.RscExtractRegisterDetail;
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
public class RscExtractRegisterDetailImpl implements RscExtractRegisterDetailService {

    @EJB
    private GenericDAO dao;
    @EJB
    private GenericService genericService;

    @Override
    public List<RscExtractRegisterDetailDTO> getBranchRscExtractRegisterDetailByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<RscExtractRegisterDetailDTO> dTOList = new ArrayList<>();
        List<RscExtractRegisterDetail> list = dao.findListByQuery("SELECT acc FROM RscPriceTenderDetailWorkUnit acc WHERE acc.branchId.id=:branchId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public RscExtractRegisterDetailDTO findExtractRegisterDetail(Integer workUnitsGuide) {
        if (workUnitsGuide != null) {
            RscExtractRegisterDetail registerDetail = dao.findEntityById(RscExtractRegisterDetail.class, workUnitsGuide);
            return mapTODTO(registerDetail);
        } else {
            return null;
        }
    }

    @Override
    public void addExtractRegisterDetailList(List<RscExtractRegisterDetailDTO> rscExtractRegisterDetailDTOlist, RscExtractRegister extractRegister, IsagUser isagUser) {

        for (RscExtractRegisterDetailDTO detailDTO : rscExtractRegisterDetailDTOlist) {

            RscExtractRegisterDetail registerDetail = mapFromDTO(detailDTO, extractRegister, isagUser);

            if (registerDetail.getId() == null) {
                Integer serial = genericService.getMaxSerialForInvoice(RscExtractRegisterDetail.class, registerDetail.getBranchId().getId(), null, null);
                registerDetail.setSerial(serial);

            }
            dao.updateEntity(registerDetail);

        }

    }

    @Override
    public void updateExtractRegisterDetailList(List<RscExtractRegisterDetail> rscExtractRegisterDetailList) {
        for (RscExtractRegisterDetail detail : rscExtractRegisterDetailList) {
            dao.updateEntity(detail);
        }
    }

    @Override
    public void deleteExtractRegisterDetailList(List<RscExtractRegisterDetailDTO> detailDTOList) {
        for (RscExtractRegisterDetailDTO registerDetailDTO : detailDTOList) {
            RscExtractRegisterDetail detail = new RscExtractRegisterDetail();
            detail.setId(registerDetailDTO.getId());
            dao.deleteEntity(detail);
        }

    }

    @Override
    public void deleteExtractRegisterDetail(RscExtractRegisterDetailDTO unit, IsagUser isagUser) {
        RscExtractRegisterDetail detail = mapFromDTO(unit, null, isagUser);
        dao.deleteEntity(detail);
    }

    @Override
    public List<RscExtractRegisterDetailDTO> getPriceExtractRegisterDetailByExtractId(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        List<RscExtractRegisterDetailDTO> dTOList = new ArrayList<>();
        List<RscExtractRegisterDetail> list = dao.findListByQuery("SELECT acc FROM RscExtractRegisterDetail acc WHERE acc.rscExtractRegister.id = :id", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public BigDecimal getRampValue(Integer id, Integer unitId) {
        Map<String, Object> params = new HashMap();
        params.put("contractId", id);
        params.put("unitId", unitId);
        List<RscExtractRegisterDetail> list = dao.findListByQuery("SELECT acc FROM RscExtractRegisterDetail acc LEFT JOIN RscExtractRegister ac where acc.rscExtractRegister.id=ac.id and ac.restrictionId.contractId.id = :contractId and ac.closed=0  and acc.workUnitId.id=:unitId ", params);
        BigDecimal rampValue = BigDecimal.ZERO;
        if (list != null && !list.isEmpty()) {
            if (list.get(0) == null) {
                rampValue = BigDecimal.ZERO;
            } else {
                rampValue = list.get(0).getNetTotalReturnRamp();

            }
        }
        return rampValue;
    }

    @Override
    public BigDecimal getContractQty(Integer id, Integer unitId) {
        Map<String, Object> params = new HashMap();
        params.put("contractId", id);
        params.put("unitId", unitId);
        List<RscExtractRegisterDetail> list = dao.findListByQuery("SELECT acc FROM RscExtractRegisterDetail acc LEFT JOIN RscExtractRegister ac where acc.rscExtractRegister.id=ac.id and ac.restrictionId.contractId.id = :contractId and ac.closed=0  and acc.workUnitId.id=:unitId ", params);
        BigDecimal contractQty = BigDecimal.ZERO;
        if (list != null && !list.isEmpty()) {
            if (list.get(0) == null) {
                contractQty = BigDecimal.ZERO;
            } else {
                contractQty = list.get(0).getContractQty();
            }
        }

        return contractQty;
    }

    @Override
    public BigDecimal getPriceSell(Integer id, Integer unitId) {
        Map<String, Object> params = new HashMap();
        params.put("contractId", id);
        params.put("unitId", unitId);
        List<RscExtractRegisterDetail> list = dao.findListByQuery("SELECT acc FROM RscExtractRegisterDetail acc LEFT JOIN RscExtractRegister ac where acc.rscExtractRegister.id=ac.id and ac.restrictionId.contractId.id = :contractId and ac.closed=0  and acc.workUnitId.id=:unitId ", params);
        BigDecimal priceSell = BigDecimal.ZERO;
        if (list != null && !list.isEmpty()) {
            if (list.get(0) == null) {
                priceSell = BigDecimal.ZERO;
            } else {
                priceSell = list.get(0).getPriceSell();
            }
        }

        return priceSell;
    }

    @Override
    public BigDecimal getOldQty(Integer tenderId, Integer unitId) {
        Map<String, Object> params = new HashMap();
        params.put("tenderId", tenderId);
        params.put("unitId", unitId);
        List<BigDecimal> list = dao.findListByQuery("SELECT acc.currentQty FROM RscExtractRegisterDetail acc LEFT JOIN RscExtractRegister ac where acc.rscExtractRegister.id = ac.id and ac.tenderId.id = :tenderId and ac.closed=0  and acc.workUnitId.id=:unitId order by ac.id desc ", params);
        BigDecimal oldQty = BigDecimal.ZERO;
        if (list != null && !list.isEmpty()) {
            if (list.get(0) == null) {
                oldQty = BigDecimal.ZERO;
            } else {
                oldQty = list.get(0);
            }

        }
        return oldQty;
    }

    @Override
    public RscExtractRegisterDetail getdetail(Integer tenderId, Integer unitId) {
        Map<String, Object> params = new HashMap();
        params.put("tenderId", tenderId);
        params.put("unitId", unitId);
        List<RscExtractRegisterDetail> list = dao.findListByQuery("SELECT acc FROM RscExtractRegisterDetail acc LEFT JOIN RscExtractRegister ac where acc.rscExtractRegister.id=ac.id and ac.tenderId.id = :tenderId and ac.closed=0  and acc.workUnitId.id=:unitId ", params);
        RscExtractRegisterDetail rampValue = null;
        if (list != null && !list.isEmpty()) {
            rampValue = list.get(0);
        }
        return rampValue;
    }

    @Override
    public List<RscExtractRegisterDetailDTO> getAllRscExtractRegisterDetailByBranchIdAndserial(Integer branchId, Integer serial
    ) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        params.put("serial", serial);

        List<RscExtractRegisterDetailDTO> dTOList = new ArrayList<>();
        List<RscExtractRegisterDetail> list = dao.findListByQuery("SELECT acc FROM RscExtractRegisterDetail acc WHERE acc.branchId.id = :id AND acc.serial = :serial ", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    public RscExtractRegisterDetailDTO mapTODTO(RscExtractRegisterDetail detail) {
        RscExtractRegisterDetailDTO dTO = new RscExtractRegisterDetailDTO();
        initMapToDTO(dTO, detail);
        return dTO;
    }

    private void initMapToDTO(RscExtractRegisterDetailDTO dTO, RscExtractRegisterDetail detailextract) {
        dTO.setIndex(detailextract.getId());
        dTO.setId(detailextract.getId());
        dTO.setContractQty(detailextract.getContractQty());
        dTO.setCurrentQty(detailextract.getCurrentQty());
        dTO.setCurrentValue(detailextract.getCurrentValue());
        dTO.setDiscountPercentage(detailextract.getDiscountPercentage());
        dTO.setDiscountValue(detailextract.getDiscountValue());
        dTO.setNetCost(detailextract.getNetCost());
        dTO.setNetTotalReturnRamp(detailextract.getNetTotalReturnRamp());
        dTO.setOldQty(detailextract.getOldQty());
        dTO.setOldRamp(detailextract.getOldRamp());
        dTO.setPriceSell(detailextract.getPriceSell());
        dTO.setRampPercentage(detailextract.getRampPercentage());
        dTO.setRampValue(detailextract.getRampValue());
        dTO.setRemarks(detailextract.getRemarks());
        dTO.setReturnRamp(detailextract.getReturnRamp());
        dTO.setReturnRampPercentage(detailextract.getReturnRampPercentage());
        dTO.setExcuteQty(detailextract.getExcuteQuantity());
        dTO.setValueAfterDiscount(detailextract.getValueAfterDiscount());
        dTO.setSerial(detailextract.getSerial());
        dTO.setTotalRamp(detailextract.getTotalRamp());
        dTO.setIndex(detailextract.getId());
        dTO.setCreatedDate(detailextract.getCreationDate());
        dTO.setCreatedBy(detailextract.getCreatedBy().getId());
        dTO.setMaster(Boolean.TRUE);

        // dTO.setRscExtractRegister(detailextract.getRscExtractRegister()== null ? null : detailextract.getRscExtractRegister());
        if (detailextract.getWorkUnitId() != null) {
            RscWorkUnitsGuideDTO guideDTO = new RscWorkUnitsGuideDTO();
            guideDTO.setId(detailextract.getWorkUnitId().getId());
            guideDTO.setCode(detailextract.getWorkUnitId().getCode());
            guideDTO.setName(detailextract.getWorkUnitId().getName());
            guideDTO.setUnitName(detailextract.getWorkUnitId().getUnitId());
            guideDTO.setQuantity(detailextract.getWorkUnitId().getQuantity());
            guideDTO.setValue(detailextract.getWorkUnitId().getValue());
            dTO.setGuideDTO(guideDTO);
            dTO.setGuideDTOBak(guideDTO);

        }
        dTO.setWorkUnitId(detailextract.getWorkUnitId() == null ? null : detailextract.getWorkUnitId().getId());
        if (detailextract.getRscExtractRegister() != null) {
            RscExtractRegisterDTO extractDTO = new RscExtractRegisterDTO();
            extractDTO.setId(detailextract.getRscExtractRegister().getId());
            extractDTO.setRemain(detailextract.getRscExtractRegister().getRemain());
            extractDTO.setValid(detailextract.getRscExtractRegister().getValid());
            extractDTO.setClosed(detailextract.getRscExtractRegister().getClosed());

            dTO.setRscExtractRegister(extractDTO);

        }

    }

    public RscExtractRegisterDetail mapFromDTO(RscExtractRegisterDetailDTO dTO, RscExtractRegister extractRegister, IsagUser isagUser) {
        RscExtractRegisterDetail detail = new RscExtractRegisterDetail();

        detail.setId(dTO.getId());
        detail.setContractQty(dTO.getContractQty());
        detail.setCurrentQty(dTO.getCurrentQty());
        detail.setCurrentValue(dTO.getCurrentValue());
        detail.setDiscountPercentage(dTO.getDiscountPercentage());
        detail.setDiscountValue(dTO.getDiscountValue());
        detail.setNetCost(dTO.getNetCost());
        detail.setNetTotalReturnRamp(dTO.getNetTotalReturnRamp());
        detail.setOldQty(dTO.getOldQty());
        detail.setOldRamp(dTO.getOldRamp());
        detail.setPriceSell(dTO.getPriceSell());
        detail.setRampPercentage(dTO.getRampPercentage());
        detail.setRampValue(dTO.getRampValue());
        detail.setRemarks(dTO.getRemarks());
        detail.setReturnRamp(dTO.getReturnRamp());
        detail.setReturnRampPercentage(dTO.getReturnRampPercentage());
        detail.setExcuteQuantity(dTO.getExcuteQty());

        detail.setValueAfterDiscount(dTO.getValueAfterDiscount());
        detail.setSerial(dTO.getSerial());
        detail.setTotalRamp(dTO.getTotalRamp());

        if (detail.getRscExtractRegister() == null) {
            detail.setRscExtractRegister(extractRegister);
        }
        if (dTO.getGuideDTO() != null && dTO.getGuideDTO().getId() != null) {
            RscWorkUnitsGuide rscWorkUnitsGuide = new RscWorkUnitsGuide();
            rscWorkUnitsGuide.setId(dTO.getGuideDTO().getId());
            rscWorkUnitsGuide.setName(dTO.getGuideDTO().getName());
            rscWorkUnitsGuide.setCode(dTO.getGuideDTO().getCode());
            rscWorkUnitsGuide.setQuantity(dTO.getGuideDTO().getQuantity());
            rscWorkUnitsGuide.setValue(dTO.getGuideDTO().getValue());
            detail.setWorkUnitId(rscWorkUnitsGuide);
        }

        
        if (dTO.getRscExtractRegister() != null) {
            RscExtractRegister extract = new RscExtractRegister();
            extract.setId(dTO.getRscExtractRegister().getId());
            extract.setRemain(dTO.getRscExtractRegister().getRemain());
            extract.setValid(dTO.getRscExtractRegister().getValid());
            extract.setClosed(dTO.getRscExtractRegister().getClosed());

            detail.setRscExtractRegister(extract);

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

    private void returnListDTO(List<RscExtractRegisterDetail> list, List<RscExtractRegisterDetailDTO> dTOList) {
        for (RscExtractRegisterDetail extractRegisterDetail : list) {
            dTOList.add(mapTODTO(extractRegisterDetail));
        }
    }

}
