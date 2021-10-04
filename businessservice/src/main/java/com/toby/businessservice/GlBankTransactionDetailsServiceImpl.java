/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.entity.CostCenter;
import com.toby.entity.GeneralJournal;
import com.toby.entity.GlAccount;
import com.toby.entity.GlAdminUnit;
import com.toby.entity.GlBank;
import com.toby.entity.GlBankTransaction;
import com.toby.entity.GlBankTransactionDetail;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.TobyUser;
import java.math.BigDecimal;
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
public class GlBankTransactionDetailsServiceImpl implements GlBankTransactionDetailsService {

    @EJB
    private GenericDAO dao;
    @EJB
    private GeneraljournalService generaljournalService;
    @EJB
    private GeneraljournalDetailsService generaljournalDetailsService;

    @Override
    public void addGlBankTransactionDetails(GlBankTransactionDetail glBankTransactionDetail) {
        dao.saveEntity(glBankTransactionDetail);
    }

    @Override
    public void deleteGlBankTransactionDetails(GlBankTransactionDetail glBankTransactionDetail) {
        dao.deleteEntity(glBankTransactionDetail);
    }

    @Override
    public GlBankTransactionDetail updateGlBankTransactionDetails(GlBankTransactionDetail glBankTransactionDetail) {
        return dao.updateEntity(glBankTransactionDetail);
    }

    @Override
    public List<GlBankTransactionDetail> getAllGlBankTransactionDetailsByGlBankTransactionId(Integer glBankTransactionId) {
        Map<String, Object> params = new HashMap<>();
        params.put("glBankTransactionId", glBankTransactionId);
        List<GlBankTransactionDetail> details = dao.findListByQuery("SELECT e FROM GlBankTransactionDetail e WHERE e.glBankTransactionId.id = :glBankTransactionId", params);
        return details;
    }

    @Override
    public GlBankTransactionDetail findGlBankTransactionDetailsById(Integer glBankTransactionDetailsId) {
        return dao.findEntityById(GlBankTransactionDetail.class, glBankTransactionDetailsId);
    }

    @Override
    public GlBankTransactionDetail addOneGlBankTransactionDetail(GlBankTransactionDetail glBankTransactionDetail) {
        return dao.updateEntity(glBankTransactionDetail);
    }

    @Override
    public GlBankTransactionDetail getGlBankTransactionDetailByGlBankTransactionId(Integer glBankTransactionId) {
        Map<String, Object> params = new HashMap<>();
        params.put("glBankTransactionId", glBankTransactionId);
        return dao.findEntityByQuery("SELECT e FROM GlBankTransactionDetail e WHERE e.glBankTransactionId.id = :glBankTransactionId", params);

    }

    @Override
    public Integer finMaxSerial(Integer glBankTransactionId) {
        Map<String, Object> params = new HashMap<>();
        params.put("glBankTransactionId", glBankTransactionId);
        Integer max = dao.findEntityByQuery("SELECT COALESCE(MAX(g.serial),0) +1  FROM GlBankTransactionDetail g WHERE g.glBankTransactionId.id =:glBankTransactionId  ", params);
        return max;
    }

    @Override
    public void addCashDetail(InvInvoiceDTO dTO, Integer glBankTransactionId, TobyUser tobyUser) {

        GlBankTransactionDetail bankTransactionDetail = mapToEntityDetailFromInvSales(dTO, glBankTransactionId, tobyUser);

        dao.updateEntity(bankTransactionDetail);
    }

    public GlBankTransactionDetail mapToEntityDetailFromInvSales(InvInvoiceDTO dTO, Integer glBankTransactionId, TobyUser tobyUser) {
        GlBankTransactionDetail entity = new GlBankTransactionDetail();

        if (dTO.getGlBankId() != null && dTO.getGlBankId().getAccountId() != null) {
            GlAccount accountDebitId = new GlAccount();
            accountDebitId.setId(dTO.getGlBankId().getAccountId());
            entity.setAccountDebitId(accountDebitId);
        }

        if (dTO.getOrganizationSiteId() != null) {
            InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
            invOrganizationSite.setId(dTO.getOrganizationSiteId().getId());
            entity.setOrganizationSiteId(invOrganizationSite);
            GlAccount accountCreditId = new GlAccount();
            if (dTO.getOrganizationSiteId().getAccountId() != null) {
                accountCreditId.setId(dTO.getOrganizationSiteId().getAccountId().getId());
                entity.setAccountCreditId(accountCreditId);
            }
        }

        if (dTO.getAdminUnitId() != null && dTO.getAdminUnitId().getId() != null) {
            GlAdminUnit glAdminUnit = new GlAdminUnit();
            glAdminUnit.setId(dTO.getAdminUnitId().getId());
            entity.setAdminUnitId(glAdminUnit);
        }

        if (dTO.getCostCenterId() != null) {
            CostCenter costCenter = new CostCenter();
            costCenter.setId(dTO.getCostCenterId().getId());
            entity.setCostCenterId(costCenter);
        }

        if (dTO.getGlBankId() != null && dTO.getGlBankId().getId() != null) {
            GlBank glBank = new GlBank();
            glBank.setId(dTO.getGlBankId().getId());
            entity.setBankIdFrom(glBank);
        }

        entity.setRemarks(dTO.getRemarks());
        entity.setValue(dTO.getPayed() != null ? dTO.getPayed() : BigDecimal.ZERO);
        entity.setValueLocal(dTO.getPayed() != null ? dTO.getPayed() : BigDecimal.ZERO);

        entity.setSerial(finMaxSerial(glBankTransactionId));
        GlBankTransaction bankTransaction = new GlBankTransaction();
        bankTransaction.setId(glBankTransactionId);
        entity.setGlBankTransactionId(bankTransaction);

        entity.setCreatedBy(tobyUser);
        entity.setCreationDate(new Date());
        entity.setCompanyId(tobyUser.getCompanyId());

        return entity;
    }

    public Integer tarhil(GlBankTransactionDetail dto, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        GeneralJournal journal = null;

        if (dto.getGlBankTransactionId().getTransactionType() == 0) {
            journal = generaljournalService.addGeneralJournalGlobal(null, 1001, dto.getSerial(), dto.getGlBankTransactionId().getDate(),
                    dto.getGlBankTransactionId().getRemark(), tobyUser);

            if (dto.getOrganizationSiteId() != null
                    && dto.getBankIdTo().getGallaryId() != null
                    && dto.getBankIdTo().getGallaryId().getCostCenterId() != null
                    && dto.getBankIdTo().getGallaryId().getCostCenterId().getId() != null) {
                generaljournalDetailsService.addGenDetalil(true, dto.getValue(), journal.getId(), dto.getAccountDebitId().getId(),
                        dto.getBankIdTo().getGallaryId().getCostCenterId().getId(),
                        dto.getRemarks(), tobyUser);
                generaljournalDetailsService.addGenDetalil(false, dto.getValue(), journal.getId(), dto.getAccountCreditId().getId(),
                        dto.getBankIdTo().getGallaryId().getCostCenterId().getId(),
                        dto.getRemarks(), tobyUser);
                if (dto.getBankIdTo().getCommition().compareTo(BigDecimal.ZERO) == 1) {
                   //  القيمة تدخل يدويا من الشاشة
                    
                    Integer accountId_3 = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"BANK_COMMITION\" AND a.accountId IS NOT NULL", params);
                    BigDecimal value = dto.getValue().multiply(dto.getBankIdTo().getCommition().divide(new BigDecimal(100)));
                    generaljournalDetailsService.addGenDetalil(true, value, journal.getId(), accountId_3,
                            dto.getBankIdTo().getGallaryId().getCostCenterId().getId(),
                            dto.getRemarks(), tobyUser);
                    generaljournalDetailsService.addGenDetalil(false, dto.getValue(), journal.getId(), dto.getAccountCreditId().getId(),
                        dto.getBankIdTo().getGallaryId().getCostCenterId().getId(),
                        dto.getRemarks(), tobyUser);
                }
            }

        } else if (dto.getGlBankTransactionId().getTransactionType() == 1) {
            journal = generaljournalService.addGeneralJournalGlobal(null, 1004, dto.getSerial(), dto.getGlBankTransactionId().getDate(),
                    dto.getGlBankTransactionId().getRemark(), tobyUser);

            
            //  القيمة مطروح منها عمولة البنك
            if (dto.getOrganizationSiteId() != null
                    && dto.getBankIdFrom().getGallaryId() != null
                    && dto.getBankIdFrom().getGallaryId().getCostCenterId() != null
                    && dto.getBankIdFrom().getGallaryId().getCostCenterId().getId() != null) {
                generaljournalDetailsService.addGenDetalil(true, dto.getValue(), journal.getId(), dto.getAccountDebitId().getId(),
                        dto.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                        dto.getRemarks(), tobyUser);
                generaljournalDetailsService.addGenDetalil(false, dto.getValue(), journal.getId(), dto.getAccountCreditId().getId(),
                        dto.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                        dto.getRemarks(), tobyUser);
                
                
                if (dto.getBankIdFrom().getCommition().compareTo(BigDecimal.ZERO) == 1) {
                    
                    Integer accountId_3 = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"BANK_COMMITION\" AND a.accountId IS NOT NULL", params);
                    BigDecimal value = dto.getValue().multiply(dto.getBankIdFrom().getCommition().divide(new BigDecimal(100)));
                    generaljournalDetailsService.addGenDetalil(true, value, journal.getId(), accountId_3,
                            dto.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                            dto.getRemarks(), tobyUser);
                }
                if (dto.getBankIdFrom() != null && dto.getBankIdFrom().getCommition() != null
                        && dto.getBankIdFrom().getCommition().compareTo(BigDecimal.ZERO) == 1) {

                    Integer accountId_3 = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"BANK_COMMITION\" AND a.accountId IS NOT NULL", params);
                    Integer accountId_2 = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where  a.branchId.id =  :branchId AND a.code = \"BANK_TAX\" AND a.accountId IS NOT NULL", params);
                    
                    if (accountId_3 != null && accountId_2 != null) {

                        generaljournalDetailsService.addGenDetalil(false, dto.getValue(), journal.getId(), dto.getAccountCreditId().getId(),
                        dto.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                        dto.getRemarks(), tobyUser);
                        
                        
//                        view.setSecondValue(entity.getValue().multiply(entity.getBankIdFrom().getCommition().divide(new BigDecimal(100))));
//                        view.setTaxValue(view.getSecondValue().multiply(new BigDecimal(0.15)));
//                        view.setTaxValueLocal(view.getTaxValue());
//
//                        view.setSecondAccountDebitId(gla2.getId());
//                        view.setSecondAccNumberDebit(gla2.getAccNumber());
//                        view.setSecondAccNameDebit(gla2.getName());
//
//                        view.setThirdAccountDebitId(gla3.getId());
//                        view.setThirdAccNumberDebit(gla3.getAccNumber());
//                        view.setThirdAccNameDebit(gla3.getName());
                    } else {
//                        String ErrorMsg = "";
//                        if (gla3 == null) {
//                            ErrorMsg += "يرجي ربط حساب العمولة ;";
//                        }
//                        if (gla2 == null) {
//                            ErrorMsg += ";يرجي ربط حساب الضريبة;";
//                        }
//                        view.setErrorMsg(ErrorMsg);
                    }

                }
            }
        }
        return journal == null ? null : journal.getId();
    }

}
