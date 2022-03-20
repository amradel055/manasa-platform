/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.GeneralJournalDTO;
import com.toby.dto.GlAccountDTO;
import com.toby.dto.GlYearDTO;
import com.toby.dto.InvNoticeDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.SymbolDTO;
import com.toby.entity.CostCenter;
import com.toby.entity.Currency;
import com.toby.entity.GeneralJournal;
import com.toby.entity.GeneralJournalDetails;
import com.toby.entity.GlAccount;
import com.toby.entity.InvNotice;
import com.toby.entity.TobyUser;
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
 * @author hq002
 */
@Stateless
public class InvNoticeServiceImpl implements InvNoticeService {

    @EJB
    GenericDAO dao;
    @EJB
    GenericService genericService;
    @EJB
    OrganizationSiteService organizationSiteService;
    @EJB
    private GeneraljournalService generaljournalService;
    @EJB
    private GeneraljournalDetailsService generaljournalDetailsService;

    @Override
    public void deleteInvNotice(InvNotice invNotice) {
        dao.deleteEntity(invNotice);
    }

    @Override
    public List<InvNotice> getALLInvNotice() {
        return dao.findAll(InvNotice.class);
    }

    @Override
    public List<InvNotice> getALLInvNoticeByCompanyId(Integer Id) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", Id);
        List<InvNotice> invNoticeList = dao.findListByQuery("SELECT i FROM InvNotice i WHERE i.companyId.id = :companyId", params);
        return invNoticeList;
    }

    @Override
    public List<InvNotice> getALLInvNoticeByBranchIdAndType(Integer Id, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", Id);
        StringBuilder query = new StringBuilder();
        query.append("SELECT i FROM InvNotice i WHERE i.branchId.id = :branchId ");

        if (type != null) {
            if (type == 2) {
                query.append(" AND i.type >= :type order by i.id desc");
            } else {
                query.append(" AND i.type <= :type order by i.id desc");
            }
            params.put("type", type);
        }

        List<InvNotice> invNoticeList = dao.findListByQuery(query.toString(), params);
        return invNoticeList;
    }

    @Override
    public List<InvNoticeDTO> getALLInvNoticeByBranchIdAndTypeDTO(Integer Id, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", Id);
        StringBuilder query = new StringBuilder();
        query.append("SELECT i FROM InvNotice i WHERE i.branchId.id = :branchId ");

        if (type != null) {
            if (type == 2) {
                query.append(" AND i.type in (2,3,5) order by i.id desc");
            } else {
                query.append(" AND i.type in (0,1,4) order by i.id desc");
            }
//            params.put("type", type);
        }

        List<InvNotice> invNoticeList = dao.findListByQuery(query.toString(), params);

        return returnDTOList(invNoticeList);
    }

    @Override
    public List<InvNoticeDTO> getALLInvNoticeByBranchIdAndTypeAndSerialAndOrgan_idDTO(Integer Id, Integer type, InvOrganizationSiteDTO organizationSiteId, Integer serial) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", Id);

        StringBuilder query = new StringBuilder();
        query.append("SELECT i FROM InvNotice i WHERE i.branchId.id = :branchId ");

        if (type != null) {
            if (type == 2) {
                query.append(" AND i.type in (2,3,5)");
            } else {
                query.append(" AND i.type in (0,1,4)");
            }
            if (serial != null) {
                params.put("serial", serial);
                query.append(" AND i.serial = :serial");
            }
            if (organizationSiteId != null) {
                params.put("organizationSiteId", organizationSiteId.getId());
                query.append(" AND i.organizationSiteId.id = :organizationSiteId");
            }
            query.append(" order by i.id desc ");
//            params.put("type", type);
            {

            }
        }
        List<InvNotice> invNoticeList = dao.findListByQuery(query.toString(), params);

        return returnDTOList(invNoticeList);
    }

    public List<InvNoticeDTO> returnDTOList(List<InvNotice> invNoticeList) {
        if (invNoticeList != null && !invNoticeList.isEmpty()) {
            List<InvNoticeDTO> list = new ArrayList<>();
            for (InvNotice invNotice : invNoticeList) {
                list.add(mapToDto(invNotice));
            }
            return list;
        }
        return null;
    }

    public InvNoticeDTO mapToDto(InvNotice entity) {
        InvNoticeDTO dTO = new InvNoticeDTO();
        dTO.setId(entity.getId());
        dTO.setSerial(entity.getSerial());

        if (entity.getContrastAccountId() != null) {
            GlAccountDTO accountDTO = new GlAccountDTO();
            accountDTO.setId(entity.getContrastAccountId().getId());
            accountDTO.setAccNumber(entity.getContrastAccountId().getAccNumber());
            dTO.setContrastAccountId(accountDTO);
        }
        dTO.setDate(entity.getDate());
        if (entity.getGeneralJournalId() != null) {
            GeneralJournalDTO generalJournalDTO = new GeneralJournalDTO();
            generalJournalDTO.setId(entity.getGeneralJournalId().getId());
            dTO.setGeneralJournalId(generalJournalDTO);
        }

        dTO.setValue(entity.getValue());
        dTO.setRemark(entity.getRemark());

        if (entity.getOrganizationSiteId() != null) {
            dTO.setOrganizationSiteId(organizationSiteService.mapToDTOSmall(entity.getOrganizationSiteId()));
        }

        if (entity.getType() != null) {
            if (entity.getType() == 0 || entity.getType() == 2) {
                dTO.setTypeName("اشعار اضافة");
            } else if (entity.getType() == 1 || entity.getType() == 3) {
                dTO.setTypeName("اشعار خصم");
            }

            dTO.setType(entity.getType());
        }

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());

        return dTO;
    }

    @Override
    public InvNotice findInvNoticeByInvNoticeId(Integer invNoticeId) {
        return dao.findEntityById(InvNotice.class, invNoticeId);
    }

    @Override
    public Integer findMaxSerialNoByBranch() {
        Integer maxId = dao.findEntityByQuery("SELECT  MAX(invNotice.id) FROM InvNotice invNotice");
        return maxId;
    }

    @Override
    public void updateInvNotice(InvNotice invNotice) {
        dao.updateEntity(invNotice);
    }

    @Override
    public InvNotice addInvNotice(InvNotice invNotice, TobyUser tobyUser, Integer yearSelection) {
        String error = null;
        try {
            if (invNotice.getId() == null) {
                invNotice.setSerial(getMaxGenericSerialByType(InvNotice.class, invNotice.getBranchId().getId(), invNotice.getType()));

            } else {
                Integer tarhilIdOld = invNotice.getGeneralJournalId() == null ? null : invNotice.getGeneralJournalId().getId();
                invNotice.setPostFlag(0);
                invNotice.setGeneralJournalId(null);
                invNotice = dao.updateEntity(invNotice);
                if (tarhilIdOld != null) {
                    generaljournalService.deleteGeneralJournal(tarhilIdOld);
                }
            }
            ///   الترحيل
            GeneralJournal generalJournal = tarhil(invNotice, tobyUser, yearSelection);
            if (generalJournal != null) {
                invNotice.setPostFlag(1);
                invNotice.setGeneralJournalId(generalJournal);
                dao.updateEntity(invNotice);
            }

            return invNotice;
        } catch (Exception e) {
//            e.printStackTrace();
            error = "هذا الشيت تم ادخاله من قبل";
            invNotice.setMsg(error);
        }
        return invNotice;
    }

    public synchronized <T> Integer getMaxGenericSerialByType(Class<T> entityClass, Integer branchId, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);

        StringBuilder query = new StringBuilder();
        query.append("SELECT max(e.serial) FROM ").append(entityClass.getSimpleName()).append(" e WHERE e.branchId.id =:branchId");

        if (type != null) {
            if (type == 2 || type == 3) {
                query.append(" AND e.type = 2 OR e.type = 3");
            } else {
                query.append(" AND e.type = 0 OR e.type = 1 OR e.type = 4");
            }
        }

        Integer serial = dao.findEntityByQuery(query.toString(), params);

        if (serial == null) {
            return 1;
        } else {
            return serial + 1;
        }
    }

    @Override
    public InvNotice updateInvNoticeForShifting(InvNotice invNotice) {
        InvNotice invNoticeReq = new InvNotice();

        invNoticeReq = dao.findEntityById(InvNotice.class, invNotice.getId());
        invNoticeReq.setPostFlag(invNotice.getPostFlag());
        if (invNoticeReq.getPostFlag().compareTo(1) == 0) {
            invNoticeReq.setGeneralJournalId(invNotice.getGeneralJournalId());
        } else {
            invNoticeReq.setGeneralJournalId(null);
        }

        return dao.updateEntity(invNoticeReq);
    }

    @Override
    public List<InvNoticeDTO> getInvNoticeListDTO(Integer branchId, Integer type, Date dateFrom,
            Date dateTo, Integer noticeFrom, Integer noticeTo) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT i FROM InvNotice i WHERE i.branchId.id = :branchId AND i.type = :type");
        params.put("branchId", branchId);
        params.put("type", type);
        if (dateFrom != null) {
            params.put("dateFrom", dateFrom);
            query.append(" AND i.date >=:dateFrom ");
        }
        if (dateTo != null) {
            params.put("dateTo", dateTo);
            query.append(" AND i.date <= :dateTo ");
        }
        if (noticeFrom != null) {
            params.put("noticeFrom", noticeFrom);
            query.append(" AND i.serial >= :noticeFrom ");
        }
        if (noticeTo != null) {
            params.put("noticeTo", noticeTo);
            query.append(" AND i.serial <= :noticeTo ");
        }

        List<InvNotice> invNoticeList = dao.findListByQuery(query.toString(), params);

        return returnDTOList(invNoticeList);
    }

    @Override
    public GeneralJournal tarhil(InvNotice dto, TobyUser tobyUser, Integer yearSelection) throws CloneNotSupportedException {

        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        GeneralJournal journal = null;
        StringBuilder remark = new StringBuilder();
        remark.append("اشعار الخصم ");
        remark.append(dto.getOrganizationSiteId().getName());
        BigDecimal mainValue = dto.getValue().divide(new BigDecimal(1.15));
        BigDecimal taxValue = dto.getValue().subtract(mainValue);
        if (dto.getInvInventory() != null && dto.getInvInventory().getCostCenterId() != null && dto.getInvInventory().getCostCenterId().getId() != null) {

            journal = generaljournalService.addGeneralJournalGlobal(yearSelection, 1031, dto.getSerial(), dto.getDate(), remark.toString(), tobyUser, 0, true, dto.getId());

            Integer salesTaxAccount = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"SALES_TAX_ACCOUNT\" AND a.accountId IS NOT NULL", params);
            Integer DISCOUNT_TAX_ACCOUNT = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"DISCOUNT_TAX_ACCOUNT\" AND a.accountId IS NOT NULL", params);

            generaljournalDetailsService.addGenDetalil(false, dto.getValue(), journal.getId(),
                    (dto.getOrganizationSiteId() == null || dto.getOrganizationSiteId().getAccountId() == null || dto.getOrganizationSiteId().getAccountId().getId() == null) ? null : dto.getOrganizationSiteId().getAccountId().getId(),
                    (dto.getInvInventory() == null || dto.getInvInventory().getCostCenterId() == null || dto.getInvInventory().getCostCenterId().getId() == null) ? null : dto.getInvInventory().getCostCenterId().getId(),
                    remark.toString(), tobyUser);

            generaljournalDetailsService.addGenDetalil(true, mainValue, journal.getId(), DISCOUNT_TAX_ACCOUNT,
                    (dto.getInvInventory() == null || dto.getInvInventory().getCostCenterId() == null || dto.getInvInventory().getCostCenterId().getId() == null) ? null : dto.getInvInventory().getCostCenterId().getId(),
                    remark.toString(), tobyUser);
            generaljournalDetailsService.addGenDetalil(true, taxValue, journal.getId(), salesTaxAccount,
                    (dto.getInvInventory() == null || dto.getInvInventory().getCostCenterId() == null || dto.getInvInventory().getCostCenterId().getId() == null) ? null : dto.getInvInventory().getCostCenterId().getId(),
                    remark.toString(), tobyUser);

        } else if (dto.getOrganizationSiteId() != null && dto.getOrganizationSiteId().getGallaryId() != null && dto.getOrganizationSiteId().getGallaryId().getCostCenterId() != null && dto.getOrganizationSiteId().getGallaryId().getCostCenterId().getId() != null) {

            journal = generaljournalService.addGeneralJournalGlobal(yearSelection, 1031, dto.getSerial(), dto.getDate(), remark.toString(), tobyUser, 0, true, dto.getId());

            Integer salesTaxAccount = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"SALES_TAX_ACCOUNT\" AND a.accountId IS NOT NULL", params);
            Integer DISCOUNT_TAX_ACCOUNT = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"DISCOUNT_TAX_ACCOUNT\" AND a.accountId IS NOT NULL", params);
            // حساب القيمة المضافة 
            generaljournalDetailsService.addGenDetalil(false, dto.getValue(), journal.getId(),
                    (dto.getOrganizationSiteId() == null || dto.getOrganizationSiteId().getAccountId() == null || dto.getOrganizationSiteId().getAccountId().getId() == null) ? null : dto.getOrganizationSiteId().getAccountId().getId(),
                    dto.getOrganizationSiteId().getGallaryId().getCostCenterId().getId(),
                    remark.toString(), tobyUser);

            generaljournalDetailsService.addGenDetalil(true, mainValue, journal.getId(), DISCOUNT_TAX_ACCOUNT,
                    dto.getOrganizationSiteId().getGallaryId().getCostCenterId().getId(),
                    remark.toString(), tobyUser);
            generaljournalDetailsService.addGenDetalil(true, taxValue, journal.getId(), salesTaxAccount,
                    dto.getOrganizationSiteId().getGallaryId().getCostCenterId().getId(),
                    remark.toString(), tobyUser);
        }
        return journal == null ? null : journal;
    }

    @Override
    public void getALLInvNoticeForToby(TobyUser tobyUser) throws CloneNotSupportedException {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", 182);
        StringBuilder query = new StringBuilder();
        query.append("SELECT i FROM InvNotice i WHERE i.branchId.id = :branchId ");

        query.append(" AND i.type in (1,4) ");

        List<InvNotice> invNoticeList = dao.findListByQuery(query.toString(), params);
        int i = 0;
        for (InvNotice dto : invNoticeList) {
            System.err.println(i++);
            tarhil(dto, tobyUser, 73);
        }
    }

    @Override
    public List<GeneralJournalDTO> getALLInvNoticeForToby2(TobyUser tobyUser) throws CloneNotSupportedException {
        List<GeneralJournalDTO> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", 182);
        StringBuilder query = new StringBuilder();
        query.append("SELECT i FROM InvNotice i WHERE i.branchId.id = :branchId ");

        query.append(" AND i.type in (1,4) ");

        List<InvNotice> invNoticeList = dao.findListByQuery(query.toString(), params);
        int i = 0;
        for (InvNotice dto : invNoticeList) {
            System.err.println(i++);
            list.add(tarhilOne(dto, tobyUser, 73));
        }
        return list;
    }

    public GeneralJournalDTO tarhilOne(InvNotice dto, TobyUser tobyUser, Integer yearSelection) throws CloneNotSupportedException {
//        dao.executeNativeQuery("select toby.sql_mode_function()");
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        GeneralJournalDTO journal = null;
        StringBuilder remark = new StringBuilder();
        remark.append("اشعار الخصم ");
        remark.append(dto.getOrganizationSiteId().getName());
        BigDecimal mainValue = dto.getValue().divide(new BigDecimal(1.15));
        BigDecimal taxValue = dto.getValue().subtract(mainValue);

        if (dto.getInvInventory() != null && dto.getInvInventory().getCostCenterId() != null && dto.getInvInventory().getCostCenterId().getId() != null) {

            journal = addGeneralJournalGlobal(yearSelection, 1031, dto.getSerial(), dto.getDate(), remark.toString(), tobyUser, null);

            journal.setDocumentId(dto.getId());

            Integer salesTaxAccount = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"SALES_TAX_ACCOUNT\" AND a.accountId IS NOT NULL", params);
            Integer DISCOUNT_TAX_ACCOUNT = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"DISCOUNT_TAX_ACCOUNT\" AND a.accountId IS NOT NULL", params);

            journal.getDetailList().add(addGenDetalil(false, dto.getValue(), journal.getId(),
                    (dto.getOrganizationSiteId() == null || dto.getOrganizationSiteId().getAccountId() == null || dto.getOrganizationSiteId().getAccountId().getId() == null) ? null : dto.getOrganizationSiteId().getAccountId().getId(),
                    (dto.getInvInventory() == null || dto.getInvInventory().getCostCenterId() == null || dto.getInvInventory().getCostCenterId().getId() == null) ? null : dto.getInvInventory().getCostCenterId().getId(),
                    remark.toString(), tobyUser, null, null));

            journal.getDetailList().add(addGenDetalil(true, mainValue, journal.getId(), DISCOUNT_TAX_ACCOUNT,
                    (dto.getInvInventory() == null || dto.getInvInventory().getCostCenterId() == null || dto.getInvInventory().getCostCenterId().getId() == null) ? null : dto.getInvInventory().getCostCenterId().getId(),
                    remark.toString(), tobyUser, null, null));
            journal.getDetailList().add(addGenDetalil(true, taxValue, journal.getId(), salesTaxAccount,
                    (dto.getInvInventory() == null || dto.getInvInventory().getCostCenterId() == null || dto.getInvInventory().getCostCenterId().getId() == null) ? null : dto.getInvInventory().getCostCenterId().getId(),
                    remark.toString(), tobyUser, null, null));

        } else if (dto.getOrganizationSiteId() != null && dto.getOrganizationSiteId().getGallaryId() != null && dto.getOrganizationSiteId().getGallaryId().getCostCenterId() != null && dto.getOrganizationSiteId().getGallaryId().getCostCenterId().getId() != null) {
            journal = addGeneralJournalGlobal(yearSelection, 1031, dto.getSerial(), dto.getDate(), remark.toString(), tobyUser, null);

            journal.setDocumentId(dto.getId());

            Integer salesTaxAccount = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"SALES_TAX_ACCOUNT\" AND a.accountId IS NOT NULL", params);
            Integer DISCOUNT_TAX_ACCOUNT = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"DISCOUNT_TAX_ACCOUNT\" AND a.accountId IS NOT NULL", params);

            journal.getDetailList().add(addGenDetalil(false, dto.getValue(), journal.getId(),
                    (dto.getOrganizationSiteId() == null || dto.getOrganizationSiteId().getAccountId() == null || dto.getOrganizationSiteId().getAccountId().getId() == null) ? null : dto.getOrganizationSiteId().getAccountId().getId(),
                    dto.getOrganizationSiteId().getGallaryId().getCostCenterId().getId(),
                    remark.toString(), tobyUser, null, null));

            journal.getDetailList().add(addGenDetalil(true, mainValue, journal.getId(), DISCOUNT_TAX_ACCOUNT,
                    dto.getOrganizationSiteId().getGallaryId().getCostCenterId().getId(),
                    remark.toString(), tobyUser, null, null));
            journal.getDetailList().add(addGenDetalil(true, taxValue, journal.getId(), salesTaxAccount,
                    dto.getOrganizationSiteId().getGallaryId().getCostCenterId().getId(),
                    remark.toString(), tobyUser, null, null));
        }
        return journal == null ? null : journal;
    }

    public GeneralJournalDTO addGeneralJournalGlobal(Integer yearSelectionId, Integer specialDocumentsTypes,
            Integer serial, Date date, String remark, TobyUser tobyUser, Integer id) {

        GeneralJournalDTO generalJournal = new GeneralJournalDTO();
        generalJournal.setGeneralStatement(remark);
        generalJournal.setGeneralData(date);
        generalJournal.setGeneralDecument(serial);
        generalJournal.setSerial(serial);

        generalJournal.setPost_flag(true);
        generalJournal.setId(id);
//        Symbol symbol = symbolService.getSpecialDocumentsTypes(specialDocumentsTypes);
        GlYearDTO glYear = new GlYearDTO();
        glYear.setId(73);
        generalJournal.setGlYear(glYear);
        SymbolDTO symbol = new SymbolDTO();
        symbol.setId(611);
        generalJournal.setGeneralType(symbol != null ? symbol : null);

        generalJournal.setBranchId(tobyUser.getBranchId().getId());
        generalJournal.setCompanyId(tobyUser.getCompanyId().getId());
        generalJournal.setCreatedBy(tobyUser.getId());
        generalJournal.setCreatedDate(new Date());
        Map<String, Object> params = new HashMap<>();
        params.put("glYear", yearSelectionId == null ? null : yearSelectionId);

        return generalJournal;
    }

    public GeneralJournalDetails addGenDetalil(Boolean isDebit, BigDecimal value, Integer generalJournalId, Integer accountId,
            Integer costCenterId, String remark, TobyUser tobyUser, Integer serial, Integer id) {

        GeneralJournalDetails generalJournalDetail = new GeneralJournalDetails();

        if (generalJournalId != null) {
            GeneralJournal journal = new GeneralJournal();
            journal.setId(generalJournalId);
            generalJournalDetail.setGeneralJournalId(journal);
        }
        if (isDebit) {
            generalJournalDetail.setDebitAmount(value);
            generalJournalDetail.setDebitAmountLocal(value);
        } else {
            generalJournalDetail.setCreditamount(value);
            generalJournalDetail.setCreditamountLocal(value);
        }

        if (accountId != null) {
            GlAccount glAccount = new GlAccount();
            glAccount.setId(accountId);
            generalJournalDetail.setGlACCOUNTId(glAccount);
        }
        generalJournalDetail.setDiscribtion(remark);
        generalJournalDetail.setRate(BigDecimal.ONE);
        Currency currency = new Currency();
        currency.setId(110);
        generalJournalDetail.setCurrencyId(currency);

        if (costCenterId != null) {
            CostCenter costCenter = new CostCenter();
            costCenter.setId(costCenterId);
            generalJournalDetail.setCostCenterId(costCenter);
        }
        generalJournalDetail.setBranchId(tobyUser.getBranchId());
        generalJournalDetail.setCompanyId(tobyUser.getCompanyId());
        generalJournalDetail.setCreatedBy(tobyUser);
        generalJournalDetail.setCreationDate(new Date());
        generalJournalDetail.setSerial(serial);
        generalJournalDetail.setId(id);

        return generalJournalDetail;
    }

}
