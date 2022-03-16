/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.RscGenaricLoad;
import com.isag.dto.RscPriceTenderDTO;
import com.isag.dto.RscPriceTenderDetailWorkUnitDTO;
import com.isag.dto.RscQotationPriceTenderDTO;
import com.isag.dto.RscQotationPriceTenderDetailWorkUnitDTO;
import com.isag.dto.RscTenderDTO;
import com.isag.dto.RscTenderDetailWorkUnitDTO;

import com.isag.entity.InvOrganizationSite;
import com.isag.entity.IsagUser;
import com.isag.entity.RscPriceTender;
import com.isag.entity.RscQotationPriceTender;
import com.isag.entity.RscTender;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq004
 */
@Stateless
public class RscQotationPriceTenderServiceImpl implements RscQotationPriceTenderService {

    @EJB
    private GenericDAO dao;
    @EJB
    private RscQotationPriceTenderDetailWorkUnitService tenderDetailWorkUnitService;
    @EJB
    private RscPriceTenderDetailWorkUnitService rscPriceTenderDetailWorkUnitService;
    @EJB
    private RscTenderDetailWorkUnitService rscTenderDetailWorkUnitService;
    @EJB
    private GenericService genericService;
    @EJB
    private RscContractRegisterService registerService;
    private Set<Integer> rscQotationPriceTenderSet = new HashSet<Integer>();

    @Override
    public List<RscQotationPriceTenderDTO> getBranchRscQotationPriceTenderActiveByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<RscQotationPriceTenderDTO> dTOList = new ArrayList<>();
        List<RscQotationPriceTender> list = dao.findListByQuery("SELECT acc FROM RscQotationPriceTender acc WHERE acc.branchId.id=:branchId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public Set<Integer> getrscPriceTenderSetByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        Set<Integer> rscPriceTenderSet = new HashSet<>();
        List<Integer> list = dao.findListByQuery("SELECT acc.rscPriceTenderId.id FROM RscQotationPriceTender acc WHERE acc.branchId.id=:branchId", params);
        rscPriceTenderSet.addAll(list);
        return rscPriceTenderSet;
    }

    @Override
    public Set<Integer> getrscTenderSetByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        Set<Integer> rscPriceTenderSet = new HashSet<>();
        List<Integer> list = dao.findListByQuery("SELECT acc.tenderId.id FROM RscQotationPriceTender acc WHERE acc.branchId.id=:branchId", params);
        rscPriceTenderSet.addAll(list);
        return rscPriceTenderSet;
    }

    @Override
    public List<RscQotationPriceTenderDTO> getRscQotationPriceTenderByTenderId(Integer tenderId) {
        Map<String, Object> params = new HashMap();
        params.put("tenderId", tenderId);
        List<RscQotationPriceTenderDTO> dTOList = new ArrayList<>();
        List<RscQotationPriceTender> list = dao.findListByQuery("SELECT acc FROM RscQotationPriceTender acc WHERE acc.tenderId.id=:tenderId", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public RscQotationPriceTenderDTO findRscQotationPriceTender(Integer tenderId) {
        if (tenderId != null) {
            RscQotationPriceTender tender = dao.findEntityById(RscQotationPriceTender.class, tenderId);
            return mapTODTO(tender, null, true);
        } else {
            return null;
        }
    }

    @Override
    public RscQotationPriceTenderDTO addRscQotationPriceTender(RscQotationPriceTenderDTO tenderDTO, List<RscQotationPriceTenderDetailWorkUnitDTO> RscQotationPriceTenderDetailWorkUnitDTOList, List<RscQotationPriceTenderDetailWorkUnitDTO> RscQotationPriceTenderDetailWorkUnitDTODeletedList, IsagUser isagUser) {
        RscQotationPriceTender tender = mapFromDTO(tenderDTO, isagUser);

        if (tender.getId() == null) {
            Integer serial = genericService.getMaxSerialForInvoice(RscQotationPriceTender.class, tender.getBranchId().getId(), null, null);
            tender.setSerial(serial);
        }

        tender = dao.updateEntity(tender);

        if (RscQotationPriceTenderDetailWorkUnitDTOList != null && !RscQotationPriceTenderDetailWorkUnitDTOList.isEmpty()) {
            tenderDetailWorkUnitService.addTenderDetailWorkUnitList(RscQotationPriceTenderDetailWorkUnitDTOList, tender, isagUser);

        }
        if (RscQotationPriceTenderDetailWorkUnitDTODeletedList != null && !RscQotationPriceTenderDetailWorkUnitDTODeletedList.isEmpty()) {
            tenderDetailWorkUnitService.deleteTenderDetailWorkUnitList(RscQotationPriceTenderDetailWorkUnitDTODeletedList);
        }

        tenderDTO = mapTODTO(tender, tenderDTO, true);
        return tenderDTO;

    }

    @Override
    public void deleteRscQotationPriceTender(RscQotationPriceTenderDTO tenderDTO, IsagUser isagUser) {
        RscQotationPriceTender tender = new RscQotationPriceTender();
        tender.setId(tenderDTO.getId());
        tenderDetailWorkUnitService.deleteRscQotationPriceTenderDetailWorkUnitByTenderId(tenderDTO.getId());
//        List<RscQotationPriceTenderDetailWorkUnitDTO> tenderDetailWorkUnitDTOlist = tenderDetailWorkUnitService.getRscQotationPriceTenderDetailWorkUnitByTenderId(tenderDTO.getId());
//        tenderDetailWorkUnitService.deleteTenderDetailWorkUnitList(tenderDetailWorkUnitDTOlist);
        dao.deleteEntity(tender);
    }

    @Override
    public List<RscQotationPriceTenderDTO> getAllRscQotationPriceTenderByBranchIdAndserial(Integer branchId, Integer serial) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        params.put("serial", serial);

        List<RscQotationPriceTenderDTO> dTOList = new ArrayList<>();
        List<RscQotationPriceTender> list = dao.findListByQuery("SELECT acc FROM RscQotationPriceTender acc WHERE acc.branchId.id = :id AND acc.serial = :serial ", params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    @Override
    public List<RscQotationPriceTenderDTO> getAllRscQotationPriceTenderByBranchIdAndserialAndID(Integer branchId, Integer serial, Integer selectedId) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        params.put("serial", serial);
        params.put("selectedId", selectedId);
        String sql = "SELECT acc FROM RscQotationPriceTender acc WHERE acc.id != :selectedId and acc.branchId.id = :id AND acc.serial = :serial ";
        List<RscQotationPriceTenderDTO> dTOList = new ArrayList<>();
        List<RscQotationPriceTender> list = dao.findListByQuery(sql, params);
        returnListDTO(list, dTOList);
        return dTOList;
    }

    public RscQotationPriceTenderDTO mapTODTO(RscQotationPriceTender tender, RscQotationPriceTenderDTO dTO, Boolean check) {
        if (dTO == null) {
            dTO = new RscQotationPriceTenderDTO();
        }
        if (check) {
            dTO.setRscQotationPriceTenderDetailWorkUnitDTOList(tenderDetailWorkUnitService.getRscQotationPriceTenderDetailWorkUnitByTenderId(tender.getId()));

        }
        initMapToDTO(dTO, tender);
        return dTO;
    }

    private void initMapToDTO(RscQotationPriceTenderDTO dTO, RscQotationPriceTender tender) {

        if (tender.getCustomerId() != null && tender.getCustomerId().getId() != null) {
            InvOrganizationSiteDTO siteDTO = new InvOrganizationSiteDTO();
            siteDTO.setId(tender.getCustomerId().getId());
            siteDTO.setCode(tender.getCustomerId().getCode());
            siteDTO.setName(tender.getCustomerId().getName());
            dTO.setCustomerId(siteDTO);
        }
        if (tender.getSubContractor() != null && tender.getSubContractor().getId() != null) {
            InvOrganizationSiteDTO siteDTO = new InvOrganizationSiteDTO();
            siteDTO.setId(tender.getSubContractor().getId());
            siteDTO.setCode(tender.getSubContractor().getCode());
            siteDTO.setName(tender.getSubContractor().getName());
            dTO.setSubContractor(siteDTO);
        }
        if (tender.getRscPriceTenderId() != null && tender.getRscPriceTenderId().getId() != null) {
            RscPriceTenderDTO rscPriceTenderDTO = new RscPriceTenderDTO();
            rscPriceTenderDTO.setId(tender.getRscPriceTenderId().getId());
            rscPriceTenderDTO.setSerial(tender.getRscPriceTenderId().getSerial());
            dTO.setRscPriceTenderId(rscPriceTenderDTO);
        }

        if (tender.getTenderId() != null && tender.getTenderId().getId() != null) {
            RscTenderDTO rscTenderDTO = new RscTenderDTO();
            rscTenderDTO.setId(tender.getTenderId().getId());
            rscTenderDTO.setSerial(tender.getTenderId().getSerial());
            rscTenderDTO.setRemark(tender.getTenderId().getRemark());
            rscTenderDTO.setType(tender.getTenderId().getType());
            if (tender.getTenderId().getCustomerId() != null && tender.getTenderId().getCustomerId().getId() != null) {
                InvOrganizationSiteDTO customer = new InvOrganizationSiteDTO();
                customer.setId(tender.getTenderId().getCustomerId().getId());
                customer.setCode(tender.getTenderId().getCustomerId().getCode());
                customer.setName(tender.getTenderId().getCustomerId().getName());
                dTO.setCustomerId(customer);
                rscTenderDTO.setCustomerId(customer);
            }
            dTO.setTenderId(rscTenderDTO);
        }

        if (tender.getTenderId() != null && tender.getTenderId().getId() != null) {
            RscGenaricLoad genaricLoad = new RscGenaricLoad();
            genaricLoad.setCustomerName(tender.getTenderId().getCustomerId() != null ? tender.getTenderId().getCustomerId().getName() : null);
            genaricLoad.setSerial(tender.getTenderId() != null ? tender.getTenderId().getSerial() : null);
            genaricLoad.setRemark(tender.getTenderId() != null ? tender.getTenderId().getRemark() : null);
            genaricLoad.setTenderType(tender.getTenderId() != null ? tender.getTenderId().getType() : null);
            genaricLoad.setPriceTenderSerial(tender.getSerial());
            genaricLoad.setTenderSerial(tender.getRscPriceTenderId() != null ? tender.getRscPriceTenderId().getSerial() : null);
            genaricLoad.setPriceTenderSerial(tender.getRscPriceTenderId() != null ? tender.getRscPriceTenderId().getSerial() : null);
            dTO.setRscGenaricLoad(genaricLoad);
        }

        dTO.setDate(tender.getDate());
        dTO.setDocNumber(tender.getDocNumber());
        dTO.setRemark(tender.getRemark());
        dTO.setSerial(tender.getSerial());
        dTO.setStatus(tender.getStatus() != null ? tender.getStatus() : 0);
        if (tender.getStatus() != null) {
            if (tender.getStatus() == 0) {
                dTO.setQotationStatus("لم يتم");
            } else if (tender.getStatus() == 1) {
                dTO.setQotationStatus("تم");
            }
        } else {
            dTO.setQotationStatus("لم يتم");
        }
        dTO.setId(tender.getId());

        dTO.setCreatedDate(tender.getCreationDate());
        dTO.setCreatedBy(tender.getCreatedBy().getId());

        if (!rscQotationPriceTenderSet.isEmpty() && rscQotationPriceTenderSet.contains(tender.getId())) {
            dTO.setActive(Boolean.TRUE);
        } else {
            dTO.setActive(Boolean.FALSE);
        }
    }

    public RscQotationPriceTender mapFromDTO(RscQotationPriceTenderDTO dTO, IsagUser isagUser) {
        RscQotationPriceTender RscPriceTender = new RscQotationPriceTender();
        if (dTO.getCustomerId() != null) {
            InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
            invOrganizationSite.setId(dTO.getCustomerId().getId());
//            invOrganizationSite.setCode(dTO.getCustomerId().getCode());
//            invOrganizationSite.setName(dTO.getCustomerId().getName());
            RscPriceTender.setCustomerId(invOrganizationSite);
        }
        if (dTO.getRscGenaricLoad() != null && dTO.getRscGenaricLoad().getPriceTenderId() != null) {
            RscPriceTender rscPriceTender = new RscPriceTender();
//            rscPriceTender.setId(dTO.getRscPriceTenderId().getId());
//            rscPriceTender.setDate(dTO.getRscPriceTenderId().getDate());
//            rscPriceTender.setDocNumber(dTO.getRscPriceTenderId().getDocNumber());
//
//            rscPriceTender.setSerial(dTO.getRscPriceTenderId().getSerial());
//            rscPriceTender.setStatus(dTO.getRscPriceTenderId().getStatus());
//            if (rscPriceTender.getCustomerId() != null && rscPriceTender.getCustomerId().getId() != null) {
//                InvOrganizationSite site = new InvOrganizationSite();
//                site.setId(rscPriceTender.getCustomerId().getId());
//                site.setCode(rscPriceTender.getCustomerId().getCode());
//                site.setName(rscPriceTender.getCustomerId().getName());
//                rscPriceTender.setCustomerId(site);
//            }
//            if (dTO.getRscPriceTenderId().getRscTenderDTO() != null && dTO.getRscPriceTenderId().getRscTenderDTO().getId() != null) {
//                RscTender rscTender = new RscTender();
//                rscTender.setId(dTO.getRscPriceTenderId().getRscTenderDTO().getId());
//                rscTender.setDate(dTO.getRscPriceTenderId().getRscTenderDTO().getDate());
//                rscTender.setDocNumber(dTO.getRscPriceTenderId().getRscTenderDTO().getDocNumber());
//                rscTender.setRemark(dTO.getRscPriceTenderId().getRscTenderDTO().getRemark());
//                rscTender.setSerial(dTO.getRscPriceTenderId().getRscTenderDTO().getSerial());
//                if (rscTender.getCustomerId() != null && rscTender.getCustomerId().getId() != null) {
//                    InvOrganizationSite site = new InvOrganizationSite();
//                    site.setId(rscTender.getCustomerId().getId());
//                    site.setCode(rscTender.getCustomerId().getCode());
//                    site.setName(rscTender.getCustomerId().getName());
//                    rscTender.setCustomerId(site);
//                }
//                rscPriceTender.setTenderId(rscTender);

//            }
            rscPriceTender.setId(dTO.getRscGenaricLoad().getPriceTenderId());
            RscPriceTender.setRscPriceTenderId(rscPriceTender);

        }
        if (dTO.getTenderId() != null && dTO.getTenderId().getId() != null) {
            RscTender parent = new RscTender();
            parent.setId(dTO.getTenderId().getId());
            RscPriceTender.setTenderId(parent);
        }
        if (dTO.getRscGenaricLoad() != null && dTO.getRscGenaricLoad().getTenderId() != null) {
            RscTender parent = new RscTender();
            parent.setId(dTO.getRscGenaricLoad().getTenderId());
            RscPriceTender.setTenderId(parent);
        }

        if (dTO.getSubContractor() != null && dTO.getSubContractor().getId() != null) {
            InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
            invOrganizationSite.setId(dTO.getSubContractor().getId());
            RscPriceTender.setSubContractor(invOrganizationSite);
        }

        RscPriceTender.setDate(dTO.getDate());
        RscPriceTender.setDocNumber(dTO.getDocNumber());
        RscPriceTender.setRemark(dTO.getRemark());
        RscPriceTender.setSerial(dTO.getSerial());
        RscPriceTender.setStatus(dTO.getStatus());

        if (dTO.getId() == null) {
            RscPriceTender.setCreatedBy(isagUser);
            RscPriceTender.setCreationDate(new Date());
        } else {
            RscPriceTender.setId(dTO.getId());
            RscPriceTender.setModificationDate(new Date());
            RscPriceTender.setModifiedBy(isagUser);
            IsagUser user = new IsagUser();
            user.setId(dTO.getCreatedBy());
            RscPriceTender.setCreatedBy(user);
            RscPriceTender.setCreationDate(dTO.getCreatedDate());
            RscPriceTender.setModificationDate(new Date());
            RscPriceTender.setModifiedBy(isagUser);
        }

        if (isagUser != null) {
            RscPriceTender.setBranchId(isagUser.getBranchId());
            RscPriceTender.setCompanyId(isagUser.getCompanyId());
        }
        return RscPriceTender;
    }

    private void returnListDTO(List<RscQotationPriceTender> list, List<RscQotationPriceTenderDTO> dTOList) {
        if (!list.isEmpty() && list.get(0) != null && list.get(0).getBranchId() != null) {
            rscQotationPriceTenderSet = registerService.getRscQotationPriceTenderIDByBranch(list.get(0).getBranchId().getId());
        }
        for (RscQotationPriceTender tender : list) {
            dTOList.add(mapTODTO(tender, null, false));
        }
    }

    @Override
    public List<RscQotationPriceTenderDetailWorkUnitDTO> loadrscGenaric(RscGenaricLoad rscGenaricLoad) {
        List<RscQotationPriceTenderDetailWorkUnitDTO> list = new ArrayList<>();
        RscQotationPriceTenderDetailWorkUnitDTO dTO;
        if (rscGenaricLoad.getTenderType() == 0) {
            List<RscPriceTenderDetailWorkUnitDTO> listRscPriceTenderDetailWorkUnitDTO = rscPriceTenderDetailWorkUnitService.getPriceTenderDetailWorkUnitByTenderId(rscGenaricLoad.getPriceTenderId());
            for (RscPriceTenderDetailWorkUnitDTO rscPriceTenderDetailWorkUnitDTO : listRscPriceTenderDetailWorkUnitDTO) {
                dTO = new RscQotationPriceTenderDetailWorkUnitDTO();
                dTO.setGuideDTO(rscPriceTenderDetailWorkUnitDTO.getGuideDTO());
                dTO.setGuideDTOBak(rscPriceTenderDetailWorkUnitDTO.getGuideDTO());
                dTO.setIndex(rscPriceTenderDetailWorkUnitDTO.getIndex());
                dTO.setPrice(rscPriceTenderDetailWorkUnitDTO.getTotalSell());
                dTO.setQuantity(rscPriceTenderDetailWorkUnitDTO.getTotalQuantityUnit());
                dTO.setRemark(rscPriceTenderDetailWorkUnitDTO.getRemark());
                dTO.setSerial(rscPriceTenderDetailWorkUnitDTO.getSerial());
                if (rscPriceTenderDetailWorkUnitDTO.getTotalSell() != null && rscPriceTenderDetailWorkUnitDTO.getTotalSell().compareTo(BigDecimal.ZERO) > 0
                        && rscPriceTenderDetailWorkUnitDTO.getTotalQuantityUnit() != null && rscPriceTenderDetailWorkUnitDTO.getTotalQuantityUnit().compareTo(BigDecimal.ZERO) > 0) {
                    dTO.setTotal(rscPriceTenderDetailWorkUnitDTO.getTotalSell().multiply(rscPriceTenderDetailWorkUnitDTO.getTotalQuantityUnit()));
                } else {
                    dTO.setTotal(BigDecimal.ZERO);
                }
                list.add(dTO);
            }
        } else if (rscGenaricLoad.getTenderType() == 1) {
            List<RscTenderDetailWorkUnitDTO> listRscTenderDetailWorkUnitDTO = rscTenderDetailWorkUnitService.getTenderDetailWorkUnitByTenderId(rscGenaricLoad.getTenderId());
            for (RscTenderDetailWorkUnitDTO rscTenderDetailWorkUnitDTO : listRscTenderDetailWorkUnitDTO) {
                dTO = new RscQotationPriceTenderDetailWorkUnitDTO();
                dTO.setGuideDTO(rscTenderDetailWorkUnitDTO.getGuideDTO());
                dTO.setGuideDTOBak(rscTenderDetailWorkUnitDTO.getGuideDTO());
                dTO.setIndex(rscTenderDetailWorkUnitDTO.getIndex());
                dTO.setPrice(BigDecimal.ZERO);
                dTO.setQuantity(rscTenderDetailWorkUnitDTO.getQuantity());
                dTO.setRemark(rscTenderDetailWorkUnitDTO.getRemark());
                dTO.setSerial(rscTenderDetailWorkUnitDTO.getSerial());
                dTO.setTotal(BigDecimal.ZERO);
                list.add(dTO);
            }
        }

        return list;
    }
}
