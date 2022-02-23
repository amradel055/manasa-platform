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
import com.isag.dto.RscTenderDTO;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.IsagUser;
import com.isag.entity.RscPriceTender;
import com.isag.entity.RscTender;
import com.isag.entity.RscTenderDetailWorkUnit;
import com.isag.entity.Symbol;
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
public class RscPriceTenderServiceImpl implements RscPriceTenderService {

    @EJB
    private GenericDAO dao;
    @EJB
    private RscPriceTenderDetailWorkUnitService tenderDetailWorkUnitService;
    @EJB
    private GenericService genericService;
    @EJB
    private RscQotationPriceTenderService qotationPriceTenderService;
    private Set<Integer> rscPriceTenderSet = new HashSet<>();

    @Override
    public List<RscPriceTenderDTO> getBranchRscPriceTenderActiveByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<RscPriceTenderDTO> dTOList = new ArrayList<>();
        List<RscPriceTender> list = dao.findListByQuery("SELECT acc FROM RscPriceTender acc WHERE acc.branchId.id=:branchId", params);
        returnListDTO(list, dTOList, branchId);
        return dTOList;
    }

    @Override
    public List<RscGenaricLoad> loadRscPriceTenderByBranch(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<RscGenaricLoad> dTOList = new ArrayList<>();
        List<RscPriceTender> list = dao.findListByQuery("SELECT acc FROM RscPriceTender acc WHERE acc.branchId.id=:branchId AND acc.tenderId.type = 0 AND acc.id NOT IN (SELECT r.qotationPriceTenderId.rscPriceTenderId.id FROM RscQotationPriceTenderDetailWorkUnit r WHERE r.branchId.id=:branchId  )", params);
        returnListLoadDTO(list, dTOList);
        List<RscTender> listTender = dao.findListByQuery("SELECT r FROM RscTender r WHERE r.branchId.id=:branchId AND r.type = 1 and r.id not in (select s.tenderId.id from RscContractRegister s where s.branchId.id=:branchId )", params);
        returnListLoadDTO(listTender, dTOList);

        return dTOList;
    }

    @Override
    public RscPriceTenderDTO findRscPriceTender(Integer tenderId) {
        if (tenderId != null) {
            RscPriceTender tender = dao.findEntityById(RscPriceTender.class, tenderId);
            return mapTODTO(tender, null, true);
        } else {
            return null;
        }
    }

    @Override
    public RscPriceTenderDTO addRscPriceTender(RscPriceTenderDTO tenderDTO, List<RscPriceTenderDetailWorkUnitDTO> RscPriceTenderDetailWorkUnitDTOList, List<RscPriceTenderDetailWorkUnitDTO> rscPriceTenderDetailWorkUnitDTODeletedList, IsagUser isagUser) {
        RscPriceTender tender = mapFromDTO(tenderDTO, isagUser);
        String error = validateSave(tenderDTO);
        if (error.isEmpty()) {

            if (tender.getId() == null) {
                Integer serial = genericService.getMaxSerialForInvoice(RscPriceTender.class, tender.getBranchId().getId(), null, null);
                tender.setSerial(serial);
            }

            tender = dao.updateEntity(tender);

            if (RscPriceTenderDetailWorkUnitDTOList != null && !RscPriceTenderDetailWorkUnitDTOList.isEmpty()) {
                RscPriceTenderDetailWorkUnitDTO detail = tenderDetailWorkUnitService.addTenderDetailWorkUnitList(RscPriceTenderDetailWorkUnitDTOList, tender, isagUser);
                if (detail.getMsg() != null && !detail.getMsg().isEmpty()) {
                    tenderDTO.setMsg("حدث خطأ,يجب ادخال بيانات التفصيلى تسعير العطاء");
                    tender.setSerial(null);
                    dao.rollbackQuery();
                }

            }
            if (rscPriceTenderDetailWorkUnitDTODeletedList != null && !rscPriceTenderDetailWorkUnitDTODeletedList.isEmpty()) {
                tenderDetailWorkUnitService.deleteTenderDetailWorkUnitList(rscPriceTenderDetailWorkUnitDTODeletedList);
            }

            tenderDTO = mapTODTO(tender, tenderDTO, true);
        } else {
            tenderDTO.setMsg(error);
            dao.rollbackQuery();
        }

        return tenderDTO;

    }

    @Override
    public void deleteRscPriceTender(RscPriceTenderDTO tenderDTO, IsagUser isagUser) {
        RscPriceTender tender = new RscPriceTender();
        tender.setId(tenderDTO.getId());
        List<RscPriceTenderDetailWorkUnitDTO> tenderDetailWorkUnitDTOlist = tenderDetailWorkUnitService.getPriceTenderDetailWorkUnitByTenderId(tenderDTO.getId());
        tenderDetailWorkUnitService.deleteTenderDetailWorkUnitList(tenderDetailWorkUnitDTOlist);
        dao.deleteEntity(tender);
    }

    @Override
    public List<RscPriceTenderDTO> getAllRscPriceTenderByBranchIdAndserial(Integer branchId, Integer serial) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        params.put("serial", serial);
        List<RscPriceTenderDTO> dTOList = new ArrayList<>();
        List<RscPriceTender> list = dao.findListByQuery("SELECT acc FROM RscPriceTender acc WHERE acc.branchId.id = :id AND acc.serial = :serial ", params);
        returnListDTO(list, dTOList, branchId);
        return dTOList;
    }

    @Override
    public Set<Integer> getAllTenderByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        Set<Integer> tenderSet = new HashSet<>();
        List<Integer> list = dao.findListByQuery("SELECT acc.tenderId.id FROM RscPriceTender acc WHERE acc.branchId.id = :branchId ", params);
        tenderSet.addAll(list);
        return tenderSet;
    }

    @Override
    public List<RscPriceTenderDTO> getAllRscPriceTenderByBranchIdAndserialAndID(Integer branchId, Integer serial, Integer selectedId) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        params.put("serial", serial);
        params.put("selectedId", selectedId);
        String sql = "SELECT acc FROM RscPriceTender acc WHERE acc.id != :selectedId and acc.branchId.id = :id AND acc.serial = :serial ";
        List<RscPriceTenderDTO> dTOList = new ArrayList<>();
        List<RscPriceTender> list = dao.findListByQuery(sql, params);
        returnListDTO(list, dTOList, branchId);
        return dTOList;
    }

    public <T> RscPriceTenderDTO mapTODTO(T tender, RscPriceTenderDTO dTO, Boolean check) {
        if (dTO == null) {
            dTO = new RscPriceTenderDTO();
        }
        initMapToDTO(dTO, tender);
        if (check) {
            RscPriceTender Tender = (RscPriceTender) tender;
            dTO.setRscPriceTenderDetailWorkUnitDTOList(tenderDetailWorkUnitService.getPriceTenderDetailWorkUnitByTenderId(Tender.getId()));
        }
        return dTO;
    }

    public <T> RscGenaricLoad mapTODTO(T tender) {
        RscGenaricLoad dTO = new RscGenaricLoad();
        initMapToDTO(dTO, tender);
        return dTO;
    }

    private <T> void initMapToDTO(RscPriceTenderDTO dTO, T Tender) {
        if (RscPriceTender.class == Tender.getClass()) {
            RscPriceTender tender = (RscPriceTender) Tender;
            if (tender.getCustomerId() != null && tender.getCustomerId().getId() != null) {
                InvOrganizationSiteDTO siteDTO = new InvOrganizationSiteDTO();
                siteDTO.setId(tender.getCustomerId().getId());
                siteDTO.setCode(tender.getCustomerId().getCode());
                siteDTO.setName(tender.getCustomerId().getName());
                dTO.setCustomerId(siteDTO);
            }

            if (tender.getTenderId() != null) {
                RscTenderDTO rscTenderDTO = new RscTenderDTO();
                rscTenderDTO.setId(tender.getTenderId().getId());
                rscTenderDTO.setSerial(tender.getTenderId().getSerial());
                if (tender.getTenderId().getCustomerId() != null && tender.getTenderId().getCustomerId().getId() != null) {
                    InvOrganizationSiteDTO siteDTO = new InvOrganizationSiteDTO();
                    siteDTO.setId(tender.getTenderId().getCustomerId().getId());
                    siteDTO.setCode(tender.getTenderId().getCustomerId().getCode());
                    siteDTO.setName(tender.getTenderId().getCustomerId().getName());
                    rscTenderDTO.setCustomerId(siteDTO);
                }
                rscTenderDTO.setRemark(tender.getTenderId().getRemark());
                rscTenderDTO.setType(tender.getTenderId().getType());
                dTO.setRscTenderDTO(rscTenderDTO);
            }
            dTO.setDate(tender.getDate());
            dTO.setDocNumber(tender.getDocNumber());
            dTO.setGeneralConsultant(tender.getGeneralConsultant() == null ? null : tender.getGeneralConsultant().getId());
            dTO.setProfissionalBussinessConsultant(tender.getProfissionalBussinessConsultant() == null ? null : tender.getProfissionalBussinessConsultant().getId());
            dTO.setProjectManager(tender.getProjectManager() == null ? null : tender.getProjectManager().getId());
            dTO.setRemark(tender.getRemark());
            dTO.setSerial(tender.getSerial());
            dTO.setStatus(tender.getStatus() != null ? tender.getStatus() : 0);
            if (dTO.getStatus() == null || dTO.getStatus() == 0) {
                dTO.setStatusText("لم يتم");
            } else {
                dTO.setStatusText("تم");
            }
            dTO.setId(tender.getId());
            dTO.setIndex(tender.getId());
            dTO.setCreatedDate(tender.getCreationDate());
            dTO.setCreatedBy(tender.getCreatedBy().getId());
            if (!rscPriceTenderSet.isEmpty() && rscPriceTenderSet.contains(tender.getId())) {
                dTO.setActive(Boolean.TRUE);
            } else {
                dTO.setActive(Boolean.FALSE);
            }
        }
    }

    private <T> void initMapToDTO(RscGenaricLoad dTO, T Tender) {
        if (RscPriceTender.class == Tender.getClass()) {
            RscPriceTender priceTender = (RscPriceTender) Tender;
            if (priceTender.getCustomerId() != null && priceTender.getCustomerId().getId() != null) {
                dTO.setCustomerName(priceTender.getCustomerId().getName());
            }
            dTO.setPriceTenderId(priceTender.getId());
            dTO.setPriceTenderSerial(priceTender.getSerial());
            dTO.setSerial(priceTender.getTenderId().getSerial());
            dTO.setIndex(priceTender.getTenderId().getSerial());
            dTO.setTenderId(priceTender.getTenderId().getId());

            dTO.setRemark(priceTender.getTenderId().getRemark());

            if (priceTender.getTenderId() != null) {
                dTO.setTenderType(priceTender.getTenderId().getType());
                if (priceTender.getTenderId().getType() == 0) {
                    dTO.setTenderTypeName("مالك");
                } else if (priceTender.getTenderId().getType() == 1) {
                    dTO.setTenderTypeName("مقاول باطن");
                }
            }
        } else if (RscTender.class == Tender.getClass()) {
            RscTender tender = (RscTender) Tender;
            if (tender.getCustomerId() != null && tender.getCustomerId().getId() != null) {
                dTO.setCustomerName(tender.getCustomerId().getName());
            }
            dTO.setTenderId(tender.getId());
            dTO.setTenderSerial(tender.getSerial());
            dTO.setSerial(tender.getSerial());
            dTO.setIndex(tender.getSerial());

            dTO.setRemark(tender.getRemark());

            dTO.setTenderType(tender.getType());
            if (tender.getType() == 0) {
                dTO.setTenderTypeName("مالك");
            } else if (tender.getType() == 1) {
                dTO.setTenderTypeName("مقاول باطن");
            }
        }
    }

    public RscPriceTender mapFromDTO(RscPriceTenderDTO dTO, IsagUser isagUser) {
        RscPriceTender RscPriceTender = new RscPriceTender();
        if (dTO.getRscTenderDTO() != null && dTO.getRscTenderDTO().getCustomerId() != null
                && dTO.getRscTenderDTO().getCustomerId().getId() != null) {
            InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
            invOrganizationSite.setId(dTO.getRscTenderDTO().getCustomerId().getId());
            invOrganizationSite.setCode(dTO.getRscTenderDTO().getCustomerId().getCode());
            invOrganizationSite.setName(dTO.getRscTenderDTO().getCustomerId().getName());
            RscPriceTender.setCustomerId(invOrganizationSite);
        }
        if (dTO.getGeneralConsultant() != null) {
            Symbol generalConsultant = new Symbol();
            generalConsultant.setId(dTO.getGeneralConsultant());
            RscPriceTender.setGeneralConsultant(generalConsultant);
        }
        if (dTO.getProfissionalBussinessConsultant() != null) {
            Symbol profissionalBussinessConsultant = new Symbol();
            profissionalBussinessConsultant.setId(dTO.getProfissionalBussinessConsultant());
            RscPriceTender.setProfissionalBussinessConsultant(profissionalBussinessConsultant);
        }
        if (dTO.getProjectManager() != null) {
            Symbol projectManager = new Symbol();
            projectManager.setId(dTO.getProjectManager());
            RscPriceTender.setProjectManager(projectManager);
        }

        if (dTO.getCustomerId() == null) {
            if (dTO.getRscTenderDTO() != null
                    && dTO.getRscTenderDTO().getCustomerId() != null
                    && dTO.getRscTenderDTO().getCustomerId().getId() != null) {
                InvOrganizationSite site = new InvOrganizationSite();
                site.setId(dTO.getRscTenderDTO().getCustomerId().getId());
                RscPriceTender.setCustomerId(site);
            } else {
                return null;
            }
        }

        RscPriceTender.setDate(dTO.getDate());
        RscPriceTender.setDocNumber(dTO.getDocNumber());
        RscPriceTender.setRemark(dTO.getRemark());
        RscPriceTender.setSerial(dTO.getSerial());
        RscPriceTender.setStatus(dTO.getStatus());

        if (dTO.getRscTenderDTO() != null && dTO.getRscTenderDTO().getId() != null) {
            RscTender rscTender = new RscTender();
            rscTender.setId(dTO.getRscTenderDTO().getId());
            RscPriceTender.setTenderId(rscTender);
        }

        if (dTO.getId() == null) {
            RscPriceTender.setCreatedBy(isagUser);
            RscPriceTender.setCreationDate(new Date());

        } else {
            IsagUser isagUser1 = new IsagUser();
            isagUser1.setId(dTO.getCreatedBy());
            RscPriceTender.setCreatedBy(isagUser1);
            RscPriceTender.setCreationDate(dTO.getCreatedDate());
            RscPriceTender.setId(dTO.getId());
            RscPriceTender.setModificationDate(new Date());
            RscPriceTender.setModifiedBy(isagUser);
        }

        RscPriceTender.setBranchId(isagUser.getBranchId());
        RscPriceTender.setCompanyId(isagUser.getCompanyId());
        return RscPriceTender;
    }

    private <T> void returnListDTO(List<T> list, List<RscPriceTenderDTO> dTOList, Integer branchId) {
         if (branchId.toString() != null && branchId > 0) {
        rscPriceTenderSet = qotationPriceTenderService.getrscPriceTenderSetByBranch(branchId);
         }
        for (T tender : list) {
            dTOList.add(mapTODTO(tender, null, false));
        }
    }

    private <T> void returnListLoadDTO(List<T> list, List<RscGenaricLoad> dTOList) {
        for (T tender : list) {
            dTOList.add(mapTODTO(tender));
        }
    }

    private String validateSave(RscPriceTenderDTO rscPriceTenderDTO) {
        if (rscPriceTenderDTO.getDate() == null) {
            return "يجب ادخال التاريخ";
        } else if (rscPriceTenderDTO.getStatus() == null) {
            return "يجب ادخال حالة التسعير";
        } else if (rscPriceTenderDTO.getDocNumber() == null) {
            return "يجب ادخال الرقم الدفترى";
        } else if (rscPriceTenderDTO.getCustomerId() == null || rscPriceTenderDTO.getCustomerId().getId() == null) {
            if (rscPriceTenderDTO.getRscTenderDTO().getCustomerId() == null
                    || rscPriceTenderDTO.getRscTenderDTO().getCustomerId().getId() == null) {
                return "يجب ادخال عميل";
            }
        }
        return "";

    }

    @Override
    public void deleteByTenderId(Integer tenderId) {

        Map<String, Object> params = new HashMap();
        params.put("tenderId", tenderId);

        dao.executeDeleteQuery("delete from RscPriceTender d WHERE d.tenderId.id=" + tenderId);

    }

}
