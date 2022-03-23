/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.RscPriceTenderDetailWorkUnitDTO;
import com.isag.dto.RscWorkUnitsGuideDTO;
import com.isag.entity.IsagUser;
import com.isag.entity.RscPriceTender;
import com.isag.entity.RscPriceTenderDetailWorkUnit;
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
public class RscPriceTenderDetailWorkUnitServiceImpl implements RscPriceTenderDetailWorkUnitService {

    @EJB
    private GenericDAO dao;
    @EJB
    private GenericService genericService;

    @Override
    public List<RscPriceTenderDetailWorkUnitDTO> getBranchRscPriceTenderDetailWorkUnitByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<RscPriceTenderDetailWorkUnitDTO> dTOList = new ArrayList<>();
        List<RscPriceTenderDetailWorkUnit> list = dao.findListByQuery("SELECT acc FROM RscPriceTenderDetailWorkUnit acc WHERE acc.branchId.id=:branchId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<RscPriceTenderDetailWorkUnitDTO> getRscPriceTenderDetailWorkUnitChilds(Integer parentId) {
        Map<String, Object> params = new HashMap();
        params.put("parent", parentId);
        List<RscPriceTenderDetailWorkUnitDTO> dTOList = new ArrayList<>();
        List<RscPriceTenderDetailWorkUnit> list = dao.findListByQuery("SELECT acc FROM RscPriceTenderDetailWorkUnit acc WHERE acc.parent.id = :parent", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public RscPriceTenderDetailWorkUnitDTO findTenderDetailWorkUnit(Integer tenderDetailWorkUnit) {
        if (tenderDetailWorkUnit != null) {
            RscPriceTenderDetailWorkUnit guide = dao.findEntityById(RscPriceTenderDetailWorkUnit.class, tenderDetailWorkUnit);
            return mapTODTO(guide);
        } else {
            return null;
        }
    }

    @Override
    public RscPriceTenderDetailWorkUnitDTO addTenderDetailWorkUnitList(List<RscPriceTenderDetailWorkUnitDTO> tenderDetailWorkUnitDTOlist, RscPriceTender tenderId, IsagUser isagUser) {
        Map<Integer, RscPriceTenderDetailWorkUnit> map = new HashMap<>();
        RscPriceTenderDetailWorkUnitDTO detail = null;
        for (RscPriceTenderDetailWorkUnitDTO detailWorkUnitDTO : tenderDetailWorkUnitDTOlist) {
            detail = detailWorkUnitDTO;
            RscPriceTenderDetailWorkUnit detailWorkUnit = mapFromDTO(detailWorkUnitDTO, tenderId, isagUser);
            if (validateSave(detailWorkUnitDTO).isEmpty()) {
                if (detailWorkUnit.getId() == null) {
                    Integer serial = genericService.getMaxSerialForInvoice(RscPriceTenderDetailWorkUnit.class, detailWorkUnit.getBranchId().getId(), null, null);
                    detailWorkUnit.setSerial(serial);
                    dao.saveEntity(detailWorkUnit);
                } else {
                    dao.updateEntity(detailWorkUnit);
                }
                map.put(detailWorkUnit.getSerial(), detailWorkUnit);

            } else {
//             dao.rollbackQuery();
                detailWorkUnitDTO.setMsg("حدث خطا, يجب ادخال تفاصيل تسعير العطاء");
            }
        }
        return detail;
    }

    @Override
    public void deleteTenderDetailWorkUnitList(List<RscPriceTenderDetailWorkUnitDTO> tenderDetailWorkUnitDTOlist) {
        for (RscPriceTenderDetailWorkUnitDTO detailWorkUnitDTO : tenderDetailWorkUnitDTOlist) {
            RscPriceTenderDetailWorkUnit detailWorkUnit = new RscPriceTenderDetailWorkUnit();
            detailWorkUnit.setId(detailWorkUnitDTO.getId());
            dao.deleteEntity(detailWorkUnit);
        }

    }

    @Override
    public void deleteTenderDetailWorkUnit(RscPriceTenderDetailWorkUnitDTO unit, IsagUser isagUser) {
        RscPriceTenderDetailWorkUnit rscTender = mapFromDTO(unit, null, isagUser);
        dao.deleteEntity(rscTender);
    }

    @Override
    public List<RscPriceTenderDetailWorkUnitDTO> getPriceTenderDetailWorkUnitByTenderId(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        List<RscPriceTenderDetailWorkUnitDTO> dTOList = new ArrayList<>();
        List<RscPriceTenderDetailWorkUnit> list = dao.findListByQuery("SELECT acc FROM RscPriceTenderDetailWorkUnit acc WHERE acc.parent IS NULL AND acc.rscPriceTender.id = :id", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<RscPriceTenderDetailWorkUnitDTO> getAllTenderRoots(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        List<RscPriceTenderDetailWorkUnitDTO> dTOList = new ArrayList<>();
        List<RscPriceTenderDetailWorkUnit> list = dao.findListByQuery("SELECT acc FROM RscPriceTenderDetailWorkUnit acc WHERE acc.parent IS NULL AND acc.branchId.id = :id");
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<RscPriceTenderDetailWorkUnitDTO> getAllRscPriceTenderDetailWorkUnitByBranchIdAndserial(Integer branchId, Integer serial) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        params.put("serial", serial);

        List<RscPriceTenderDetailWorkUnitDTO> dTOList = new ArrayList<>();
        List<RscPriceTenderDetailWorkUnit> list = dao.findListByQuery("SELECT acc FROM RscPriceTenderDetailWorkUnit acc WHERE acc.branchId.id = :id AND acc.serial = :serial ", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<RscPriceTenderDetailWorkUnitDTO> getAllRscPriceTenderDetailWorkUnitByBranchIdAndserialAndParent(Integer branchId, Integer serial, Integer parentId) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        params.put("serial", serial);
        params.put("parentId", parentId);
        String sql = "SELECT acc FROM RscPriceTenderDetailWorkUnit acc WHERE acc.parent.id != :parentId and acc.branchId.id = :id AND acc.serial = :serial ";
        List<RscPriceTenderDetailWorkUnitDTO> dTOList = new ArrayList<>();
        List<RscPriceTenderDetailWorkUnit> list = dao.findListByQuery(sql, params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    public RscPriceTenderDetailWorkUnitDTO mapTODTO(RscPriceTenderDetailWorkUnit guide) {
        RscPriceTenderDetailWorkUnitDTO dTO = new RscPriceTenderDetailWorkUnitDTO();
        initMapToDTO(dTO, guide);
        if (guide.getParent() != null) {
            dTO.setParent(new RscPriceTenderDetailWorkUnitDTO());
            initMapToDTO(dTO.getParent(), guide.getParent());
        }
        return dTO;
    }

    private void initMapToDTO(RscPriceTenderDetailWorkUnitDTO dTO, RscPriceTenderDetailWorkUnit guide) {
        dTO.setId(guide.getId());
        dTO.setQuantityUnits(guide.getQuantityUnits());
        dTO.setRemark(guide.getRemark());
        dTO.setSerial(guide.getSerial());
        dTO.setStatus(guide.getStatus());
        dTO.setPriceUnit(guide.getPriceUnit());
        dTO.setPriceUnitSell(guide.getPriceUnitSell());
        dTO.setProfitPercentage(guide.getProfitPercentage());
        dTO.setProfitValue(guide.getProfitValue());
        dTO.setQuantityIn(guide.getQuantityIn());
        dTO.setQuantityUnits(guide.getQuantityUnits());
        dTO.setTotalCost(guide.getTotalCost());
        dTO.setTotalProfit(guide.getTotalProfit());
        dTO.setTotalQuantityUnit(guide.getTotalQuantityUnit());
        dTO.setTotalSell(guide.getTotalSell());
        dTO.setTotalUnits(guide.getTotalUnits());
        dTO.setIndex(guide.getId());
        dTO.setCreatedDate(guide.getCreationDate());
        dTO.setCreatedBy(guide.getCreatedBy().getId());

        dTO.setTenderId(guide.getRscPriceTender() == null ? null : guide.getRscPriceTender().getId());
        if (guide.getWorkUnitId() != null) {
            RscWorkUnitsGuideDTO guideDTO = new RscWorkUnitsGuideDTO();
            guideDTO.setId(guide.getWorkUnitId().getId());
            guideDTO.setCode(guide.getWorkUnitId().getCode());
            guideDTO.setName(guide.getWorkUnitId().getName());
            guideDTO.setUnitName(guide.getWorkUnitId().getUnitId());
            guideDTO.setQuantity(guide.getWorkUnitId().getQuantity());
            if (guide.getPriceUnit() != null) {
                guideDTO.setValue(guide.getPriceUnit());
            } else {
                guideDTO.setValue(guide.getWorkUnitId().getValue());
            }

            dTO.setGuideDTO(guideDTO);
            dTO.setGuideDTOBak(guideDTO);

        }
        dTO.setWorkUnitId(guide.getWorkUnitId() == null ? null : guide.getWorkUnitId().getId());

    }

    public RscPriceTenderDetailWorkUnit mapFromDTO(RscPriceTenderDetailWorkUnitDTO dTO, RscPriceTender tenderId, IsagUser isagUser) {
        RscPriceTenderDetailWorkUnit tenderDetail = new RscPriceTenderDetailWorkUnit();

        tenderDetail.setId(dTO.getId());
        tenderDetail.setQuantityUnits(dTO.getQuantityUnits());
        tenderDetail.setRemark(dTO.getRemark());
        tenderDetail.setSerial(dTO.getSerial());
        tenderDetail.setStatus(dTO.getStatus());
        tenderDetail.setPriceUnit(dTO.getPriceUnit());
        tenderDetail.setPriceUnitSell(dTO.getPriceUnitSell());
        tenderDetail.setProfitPercentage(dTO.getProfitPercentage());
        tenderDetail.setProfitValue(dTO.getProfitValue());
        tenderDetail.setQuantityIn(dTO.getQuantityIn());
        tenderDetail.setQuantityUnits(dTO.getQuantityUnits());
        tenderDetail.setTotalCost(dTO.getTotalCost());
        tenderDetail.setTotalProfit(dTO.getTotalProfit());
        tenderDetail.setTotalQuantityUnit(dTO.getTotalQuantityUnit());
        tenderDetail.setTotalSell(dTO.getTotalSell());
        tenderDetail.setTotalUnits(dTO.getTotalUnits());

        if (tenderDetail.getRscPriceTender() == null) {
            tenderDetail.setRscPriceTender(tenderId);
        }
        if (dTO.getGuideDTO() != null && dTO.getGuideDTO().getId() != null) {
            RscWorkUnitsGuide rscWorkUnitsGuide = new RscWorkUnitsGuide();
            rscWorkUnitsGuide.setId(dTO.getGuideDTO().getId());
            rscWorkUnitsGuide.setName(dTO.getGuideDTO().getName());
            rscWorkUnitsGuide.setCode(dTO.getGuideDTO().getCode());
            rscWorkUnitsGuide.setQuantity(dTO.getGuideDTO().getQuantity());
            rscWorkUnitsGuide.setValue(dTO.getGuideDTO().getValue());
            rscWorkUnitsGuide.setUnitId(dTO.getGuideDTO().getUnitName());
            tenderDetail.setWorkUnitId(rscWorkUnitsGuide);
        }

        if (dTO.getParent() != null && dTO.getParent().getId() != null) {
            RscPriceTenderDetailWorkUnit guideParent = new RscPriceTenderDetailWorkUnit();
            guideParent.setId(dTO.getParent().getId());
            tenderDetail.setParent(guideParent);
        }

        if (dTO.getId() == null) {
            IsagUser isagUser1 = new IsagUser();
            isagUser1.setId(dTO.getCreatedBy());
            tenderDetail.setCreatedBy(isagUser1);
            tenderDetail.setCreationDate(dTO.getCreatedDate());
            tenderDetail.setCreatedBy(isagUser);
            tenderDetail.setCreationDate(new Date());
        } else {
            tenderDetail.setModificationDate(new Date());
            tenderDetail.setModifiedBy(isagUser);
        }

        if (isagUser != null) {
            tenderDetail.setBranchId(isagUser.getBranchId());
            tenderDetail.setCompanyId(isagUser.getCompanyId());
        }
        return tenderDetail;
    }

    private void returnListDTO(List<RscPriceTenderDetailWorkUnit> list, List<RscPriceTenderDetailWorkUnitDTO> dTOList) {
        for (RscPriceTenderDetailWorkUnit guide : list) {
            dTOList.add(mapTODTO(guide));
        }
    }

    private String validateSave(RscPriceTenderDetailWorkUnitDTO detailWorkUnitDTO) {
        if (detailWorkUnitDTO.getGuideDTO() == null) {
            return "يجب ادخال بند";
        } else if (detailWorkUnitDTO.getPriceUnit() == null) {
            return "يجب ادخال قيمة الفئة تحليلى";
        } else if (detailWorkUnitDTO.getPriceUnitSell() == null) {
            return "يجب ادخال سعر البيع";
        } else if (detailWorkUnitDTO.getProfitPercentage() == null) {
            return "يجب ادخال نسبة الربح";
        } else if (detailWorkUnitDTO.getProfitValue() == null) {
            return "يجب ادخال قيمة الربح";
        } else if (detailWorkUnitDTO.getQuantityIn() == null) {
            return "يجب ادخال كمية بند العطاء";
        } else if (detailWorkUnitDTO.getQuantityUnits() == null) {
            return "يجب ادخال الكمية تحليلى";

        } else if (detailWorkUnitDTO.getTotalCost() == null) {
            return "يجب ادخال اجمالى التكلفة تحليلى";
        } else if (detailWorkUnitDTO.getTotalProfit() == null) {
            return "يجب ادخال اجمالى الربح";
        } else if (detailWorkUnitDTO.getTotalQuantityUnit() == null) {
            return "يجب ادخال اجمالى الكمية تحليلى";
        } else if (detailWorkUnitDTO.getTotalSell() == null) {
            return "يجب ادخال اجمالى البيع";
        } else if (detailWorkUnitDTO.getTotalUnits() == null) {
            return "يجب ادخال اجمالى الوحدة";
        }
        return "";
    }

    @Override
    public void deletebyPriceTenderId(Integer priceTenderId) {

        Map<String, Object> params = new HashMap();
        params.put("priceTenderId", priceTenderId);

        dao.executeDeleteQuery("delete from RscPriceTenderDetailWorkUnit d WHERE d.priceTenderId.id=" + priceTenderId);
    }
}
