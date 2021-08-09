/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.RscTenderDTO;
import com.isag.dto.RscTenderDetailWorkUnitDTO;
import com.isag.dto.RscTenderRscAccrualDeductionDTO;
import com.isag.dto.SymbolDTO;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.IsagUser;
import com.isag.entity.RscContractRegister;
import com.isag.entity.RscTender;
import com.isag.entity.Symbol;
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
public class RscTenderServiceImpl implements RscTenderService {

    @EJB
    private GenericDAO dao;
    @EJB
    private RscTenderDetailWorkUnitService rscTenderDetailWorkUnitService;
    @EJB
    private GenericService genericService;
    @EJB
    private RscTenderRscAccrualDeductionService rscTenderRscAccrualDeductionService;
    @EJB
    private RscPriceTenderService priceTenderService;
    @EJB
    private RscQotationPriceTenderService rscQotationPriceTenderService;
    private Set<Integer> tenderSet = new HashSet<>();

    @Override
    public List<RscTenderDTO> getBranchRscTenderActiveByBranch(Integer branchId, IsagUser isagUser) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<RscTenderDTO> dTOList = new ArrayList<>();
        List<RscTender> list = dao.findListByQuery("SELECT acc FROM RscTender acc WHERE acc.branchId.id=:branchId", params);
        returnListDTO(list, dTOList, isagUser);
        return dTOList;
    }

    @Override
    public List<RscTenderDTO> getRscTenderActiveByBranch(Integer branchId, IsagUser isagUser) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<RscTenderDTO> dTOList = new ArrayList<>();
        List<RscTender> list = dao.findListByQuery("SELECT acc FROM RscTender acc WHERE acc.branchId.id=:branchId AND acc.type = 0 AND acc.id not in (select r.tenderId.id  FROM RscPriceTender r where  r.branchId.id=:branchId )", params);
        returnListDTO(list, dTOList, isagUser);
        return dTOList;
    }

    @Override
    public RscTenderDTO findRscTender(Integer tenderId, IsagUser isagUser) {
        if (tenderId != null) {
            RscTenderDTO rscTenderDTO = new RscTenderDTO();
            RscTender tender = dao.findEntityById(RscTender.class, tenderId);
            rscTenderDTO = mapTODTO(tender, true, isagUser);
            return rscTenderDTO;
        } else {
            return null;
        }
    }

    @Override
    public void updateSummary(RscTenderDTO rscTenderDTO) {

        if (rscTenderDTO != null) {
            updateWorkUnitSummary(rscTenderDTO);

            updateAccuralSummary(rscTenderDTO);

            updateDeductionAccural(rscTenderDTO);

        }
    }

    @Override
    public RscTenderDTO updateWorkUnitSummary(RscTenderDTO rscTenderDTO) {
        if (rscTenderDTO.getRscTenderDetailWorkUnitDTOList() != null && !rscTenderDTO.getRscTenderDetailWorkUnitDTOList().isEmpty()) {
            rscTenderDTO.setTotalAmount(BigDecimal.ZERO);
            rscTenderDTO.setTotalValue(BigDecimal.ZERO);
            for (RscTenderDetailWorkUnitDTO detail : rscTenderDTO.getRscTenderDetailWorkUnitDTOList()) {
                if (detail.getGuideDTO() == null) {
                    detail.setGuideDTO(detail.getGuideDTOBak());
                }
                rscTenderDTO.setTotalAmount((rscTenderDTO.getTotalAmount() == null ? BigDecimal.ZERO : rscTenderDTO.getTotalAmount()).add(detail.getQuantity() == null ? BigDecimal.ZERO : detail.getQuantity()));
                rscTenderDTO.setTotalValue((rscTenderDTO.getTotalValue() == null ? BigDecimal.ZERO : rscTenderDTO.getTotalValue()).add(detail.getTotal() == null ? BigDecimal.ZERO : detail.getTotal()));
            }
        }
        return rscTenderDTO;
    }

    @Override
    public RscTenderDTO updateDeductionAccural(RscTenderDTO rscTenderDTO) {
        if (rscTenderDTO.getRscTenderRscAccrualDeductionDTODeductionList() != null && !rscTenderDTO.getRscTenderRscAccrualDeductionDTODeductionList().isEmpty()) {
            rscTenderDTO.setTotalAccural(BigDecimal.ZERO);
            for (RscTenderRscAccrualDeductionDTO detail : rscTenderDTO.getRscTenderRscAccrualDeductionDTODeductionList()) {
                if (detail.getRscAccrualDeductionId() == null) {
                    detail.setRscAccrualDeductionId(detail.getRscAccrualDeductionIdBak());
                }
                rscTenderDTO.setTotalDeduction((rscTenderDTO.getTotalDeduction() == null ? BigDecimal.ZERO : rscTenderDTO.getTotalDeduction()).add(detail.getPercentage() == null ? BigDecimal.ZERO : detail.getPercentage()));
            }
        }
        return rscTenderDTO;
    }

    @Override
    public RscTenderDTO updateAccuralSummary(RscTenderDTO rscTenderDTO) {
        rscTenderDTO.setTotalDeduction(BigDecimal.ZERO);
        if (rscTenderDTO.getRscTenderRscAccrualDeductionDTOAccruaList() != null && !rscTenderDTO.getRscTenderRscAccrualDeductionDTOAccruaList().isEmpty()) {
            for (RscTenderRscAccrualDeductionDTO detail : rscTenderDTO.getRscTenderRscAccrualDeductionDTOAccruaList()) {
                if (detail.getRscAccrualDeductionId() == null) {
                    detail.setRscAccrualDeductionId(detail.getRscAccrualDeductionIdBak());
                }
                rscTenderDTO.setTotalAccural((rscTenderDTO.getTotalAccural() == null ? BigDecimal.ZERO : rscTenderDTO.getTotalAccural()).add(detail.getPercentage() == null ? BigDecimal.ZERO : detail.getPercentage()));
            }
        }
        return rscTenderDTO;
    }

    @Override
    public RscTenderDTO addRscTender(RscTenderDTO tenderDTO, IsagUser isagUser) {
        RscTender tender = mapFromDTO(tenderDTO, isagUser);
        StringBuilder error = new StringBuilder();

        error = validateHead(tender, error);
        if (error != null && !error.toString().isEmpty()) {
            tenderDTO.setMsg(error.toString());
        } else {
            if (tender.getId() == null) {
                Integer serial = genericService.getMaxSerialForInvoice(RscTender.class, tender.getBranchId().getId(), null, null);
                tender.setSerial(serial);
            }
            tender = dao.updateEntity(tender);

            if (tenderDTO.getRscTenderDetailWorkUnitDTOList() != null && !tenderDTO.getRscTenderDetailWorkUnitDTOList().isEmpty()) {
                String msg = rscTenderDetailWorkUnitService.addTenderDetailWorkUnitList(tenderDTO.getRscTenderDetailWorkUnitDTOList(), tender.getId(), isagUser);
                if (msg != null && !msg.isEmpty()) {
                    error.append(msg);
                }
            }

            if (tenderDTO.getRscTenderRscAccrualDeductionDTOAccruaList() != null && !tenderDTO.getRscTenderRscAccrualDeductionDTOAccruaList().isEmpty()) {
                String msg = rscTenderRscAccrualDeductionService.addRscTenderRscAccrualDeductionDList(tenderDTO.getRscTenderRscAccrualDeductionDTOAccruaList(), tender.getId(), isagUser);
                if (msg != null && !msg.isEmpty()) {
                    error.append(msg);
                }
            }

            if (tenderDTO.getRscTenderRscAccrualDeductionDTODeductionList() != null && !tenderDTO.getRscTenderRscAccrualDeductionDTODeductionList().isEmpty()) {
                String msg = rscTenderRscAccrualDeductionService.addRscTenderRscAccrualDeductionDList(tenderDTO.getRscTenderRscAccrualDeductionDTODeductionList(), tender.getId(), isagUser);
                if (msg != null && !msg.isEmpty()) {
                    error.append(msg);
                }
            }

            if (tenderDTO.getRscTenderDetailWorkUnitDTODeletedList() != null && !tenderDTO.getRscTenderDetailWorkUnitDTODeletedList().isEmpty()) {
                rscTenderDetailWorkUnitService.deleteTenderDetailWorkUnitList(tenderDTO.getRscTenderDetailWorkUnitDTODeletedList(), isagUser);
            }

            if (tenderDTO.getRscTenderRscAccrualDeductionDTODeletedList() != null && !tenderDTO.getRscTenderRscAccrualDeductionDTODeletedList().isEmpty()) {
                rscTenderRscAccrualDeductionService.deleteRscTenderRscAccrualDeductionDList(tenderDTO.getRscTenderRscAccrualDeductionDTODeletedList(), isagUser);
            }
            if (error != null && !error.toString().isEmpty()) {
                dao.rollbackQuery();
                tenderDTO.setMsg(error.toString());
                return tenderDTO;
            }
            tenderDTO = mapTODTO(tender, true, isagUser);
        }
        return tenderDTO;

    }

    private StringBuilder validateHead(RscTender tender, StringBuilder error) {
        if (tender.getDate() == null) {
            error.append("يجب ادخال تاريخ");
        }

        if (tender.getCustomerId() == null) {
            error.append("يجب ادخال مالك");
        }

        if (tender.getType() == null) {
            error.append("يجب ادخال نوع المناقصه");
        }

        if (tender.getDocNumber() == null || tender.getDocNumber().isEmpty()) {
            error.append("يجب ادخال الرقم الدفتري");
        }

        return error;
    }

    @Override
    public void deleteRscTender(RscTenderDTO tenderDTO, IsagUser isagUser) {
        RscTender tender = new RscTender();
        tender.setId(tenderDTO.getId());
        List<RscTenderDetailWorkUnitDTO> tenderDetailWorkUnitDTOlist = rscTenderDetailWorkUnitService.getTenderDetailWorkUnitByTenderId(tenderDTO.getId());
        rscTenderDetailWorkUnitService.deleteTenderDetailWorkUnitList(tenderDetailWorkUnitDTOlist, isagUser);
        priceTenderService.deleteByTenderId(tenderDTO.getId());
        rscTenderRscAccrualDeductionService.deleteByTenderId(tenderDTO.getId());

        dao.deleteEntity(tender);
    }

    @Override
    public List<RscTenderDTO> getAllRscTenderByBranchIdAndserial(Integer branchId, Integer serial, IsagUser isagUser) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        params.put("serial", serial);

        List<RscTenderDTO> dTOList = new ArrayList<>();
        List<RscTender> list = dao.findListByQuery("SELECT acc FROM RscTender acc WHERE acc.branchId.id = :id AND acc.serial = :serial ", params);
        returnListDTO(list, dTOList, isagUser);
        return dTOList;
    }

    @Override
    public List<RscTenderDTO> getAllRscTenderByBranchIdAndserialAndID(Integer branchId, Integer serial, Integer selectedId, IsagUser isagUser) {
        Map<String, Object> params = new HashMap();
        params.put("id", branchId);
        params.put("serial", serial);
        params.put("selectedId", selectedId);
        String sql = "SELECT acc FROM RscTender acc WHERE acc.id != :selectedId and acc.branchId.id = :id AND acc.serial = :serial ";
        List<RscTenderDTO> dTOList = new ArrayList<>();
        List<RscTender> list = dao.findListByQuery(sql, params);
        returnListDTO(list, dTOList, isagUser);
        return dTOList;
    }

    public RscTenderDTO mapTODTO(RscTender tender, Boolean check, IsagUser isagUser) {
        RscTenderDTO dTO = new RscTenderDTO();
        initMapToDTO(dTO, tender);
        if (check) {
            dTO.setRscTenderDetailWorkUnitDTOList(rscTenderDetailWorkUnitService.getTenderDetailWorkUnitByTenderId(tender.getId()));
            dTO.setRscTenderRscAccrualDeductionDTOAccruaList(rscTenderRscAccrualDeductionService.getAllRscTenderRscAccrualDeductionList(tender.getId(), 2)); // Accrual
            dTO.setRscTenderRscAccrualDeductionDTODeductionList(rscTenderRscAccrualDeductionService.getAllRscTenderRscAccrualDeductionList(tender.getId(), 3)); // Deduction
            updateSummary(dTO);
        }
        return dTO;
    }

    private void initMapToDTO(RscTenderDTO dTO, RscTender tender) {

        if (tender.getCustomerId() != null && tender.getCustomerId().getId() != null) {
            InvOrganizationSiteDTO siteDTO = new InvOrganizationSiteDTO();
            siteDTO.setId(tender.getCustomerId().getId());
            siteDTO.setCode(tender.getCustomerId().getCode());
            siteDTO.setName(tender.getCustomerId().getName());
            dTO.setCustomerId(siteDTO);
        }

        dTO.setDate(tender.getDate());
        dTO.setDocNumber(tender.getDocNumber());
        if (tender.getGeneralConsultant() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(tender.getGeneralConsultant().getId());
            symbolDTO.setSerial(tender.getGeneralConsultant().getSerial());
            symbolDTO.setName(tender.getGeneralConsultant().getName());
            dTO.setGeneralConsultant(symbolDTO);
        }

        if (tender.getProfissionalBussinessConsultant() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(tender.getProfissionalBussinessConsultant().getId());
            symbolDTO.setSerial(tender.getProfissionalBussinessConsultant().getSerial());
            symbolDTO.setName(tender.getProfissionalBussinessConsultant().getName());
            dTO.setProfissionalBussinessConsultant(symbolDTO);
        }

        if (tender.getProjectManager() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(tender.getProjectManager().getId());
            symbolDTO.setSerial(tender.getProjectManager().getSerial());
            symbolDTO.setName(tender.getProjectManager().getName());
            dTO.setProjectManager(symbolDTO);
        }

        dTO.setRemark(tender.getRemark());
        dTO.setSerial(tender.getSerial());
        dTO.setType(tender.getType());

        Map<String, Object> params = new HashMap();
        params.put("tenderId", tender.getId());
        String sql = "SELECT r FROM RscContractRegister r WHERE r.tenderId.id = :tenderId ";
        List<RscContractRegister> list = dao.findListByQuery(sql, params);

        if (list != null && !list.isEmpty()) {
            dTO.setStatusText("تم الترسية");
            if (tender.getType() != null && "1".equals(tender.getType())) {
                if (list.get(0).getSiteId() != null) {
                    dTO.setContractorName(list.get(0).getSiteId().getName());
                }
            }
        } else {
            dTO.setStatusText("لم يتم الترسية");
        }
        dTO.setId(tender.getId());
        dTO.setCreatedDate(tender.getCreationDate());
        dTO.setCreatedBy(tender.getCreatedBy().getId());
        if (!tenderSet.isEmpty() && tenderSet.contains(tender.getId())) {
            dTO.setActive(Boolean.TRUE);
        } else {
            dTO.setActive(Boolean.FALSE);
        }

    }

    public RscTender mapFromDTO(RscTenderDTO dTO, IsagUser isagUser) {
        RscTender rscTender = new RscTender();
        if (dTO.getCustomerId() != null) {
            InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
            invOrganizationSite.setId(dTO.getCustomerId().getId());
            rscTender.setCustomerId(invOrganizationSite);
        }

        if (dTO.getGeneralConsultant() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(dTO.getGeneralConsultant().getId());
            rscTender.setGeneralConsultant(symbol);
        }
        if (dTO.getProfissionalBussinessConsultant() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(dTO.getProfissionalBussinessConsultant().getId());
            rscTender.setProfissionalBussinessConsultant(symbol);
        }

        if (dTO.getProjectManager() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(dTO.getProjectManager().getId());
            rscTender.setProjectManager(symbol);
        }

        rscTender.setDate(dTO.getDate());
        rscTender.setDocNumber(dTO.getDocNumber());
        rscTender.setRemark(dTO.getRemark());
        rscTender.setSerial(dTO.getSerial());
        rscTender.setType(dTO.getType());

        rscTender.setBranchId(isagUser.getBranchId());
        rscTender.setCompanyId(isagUser.getCompanyId());

        if (dTO.getId() == null) {
            rscTender.setCreatedBy(isagUser);
            rscTender.setCreationDate(new Date());
        } else {
            rscTender.setId(dTO.getId());
            rscTender.setModificationDate(new Date());
            rscTender.setModifiedBy(isagUser);
            IsagUser user = new IsagUser();
            user.setId(dTO.getCreatedBy());
            rscTender.setCreatedBy(user);
            rscTender.setCreationDate(dTO.getCreatedDate());
        }
        return rscTender;
    }

    private void returnListDTO(List<RscTender> list, List<RscTenderDTO> dTOList, IsagUser isagUser) {
        if (!list.isEmpty() && list.get(0) != null && list.get(0).getBranchId() != null) {
            tenderSet = priceTenderService.getAllTenderByBranchId(list.get(0).getBranchId().getId());
            tenderSet.addAll(rscQotationPriceTenderService.getrscTenderSetByBranch(list.get(0).getBranchId().getId()));
        }
        for (RscTender tender : list) {
            dTOList.add(mapTODTO(tender, false, isagUser));
        }
    }

    @Override
    public List<RscTenderDTO> getAllRscTenderByBranchId(IsagUser isagUser) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", isagUser.getBranchId().getId());

        List<RscTenderDTO> dTOList = new ArrayList<>();
        List<RscTender> list = dao.findListByQuery("SELECT acc FROM RscTender acc WHERE acc.branchId.id=:branchId", params);
        returnListDTO(list, dTOList, isagUser);
        return dTOList;
    }
}
