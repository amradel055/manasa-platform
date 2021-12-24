/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.GeneralJournalDTO;
import com.toby.dto.GlYearDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.SymbolDTO;
import com.toby.entity.CostCenter;
import com.toby.entity.Currency;
import com.toby.entity.GeneralJournal;
import com.toby.entity.GeneralJournalDetails;
import com.toby.entity.GlAccount;
import com.toby.entity.GlAdminUnit;
import com.toby.entity.GlBank;
import com.toby.entity.GlBankTransaction;
import com.toby.entity.GlBankTransactionDetail;
import com.toby.entity.GlYear;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.Symbol;
import com.toby.entity.TobyUser;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
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
public class GlBankTransactionDetailsServiceImpl implements GlBankTransactionDetailsService {

    @EJB
    private GenericDAO dao;
    @EJB
    private GeneraljournalService generaljournalService;
    @EJB
    private GeneraljournalDetailsService generaljournalDetailsService;
    @EJB
    private GlBankService glBankService;
    @EJB
    private GeneraljournalServiceDTO generaljournalServiceDTO;

    @Override
    public void addGlBankTransactionDetails(GlBankTransactionDetail glBankTransactionDetail) {
        dao.saveEntity(glBankTransactionDetail);
    }

    @Override
    public void deleteGlBankTransactionDetails(GlBankTransactionDetail glBankTransactionDetail) {
        dao.deleteEntity(glBankTransactionDetail);
    }

    @Override
    public GlBankTransactionDetail updateGlBankTransactionDetails(GlBankTransactionDetail glBankTransactionDetail, TobyUser tobyUser) {
        GlBankTransactionDetail bankTransactionDetail = dao.updateEntity(glBankTransactionDetail);
        if (glBankTransactionDetail.getGlBankTransactionId() != null
                && glBankTransactionDetail.getGlBankTransactionId().getGeneralJournalId() != null
                && glBankTransactionDetail.getGlBankTransactionId().getGeneralJournalId().getId() != null) {
            generaljournalService.deleteGeneralJournal(glBankTransactionDetail.getGlBankTransactionId().getGeneralJournalId().getId());
        }
        tarhil(bankTransactionDetail, tobyUser);
        return bankTransactionDetail;
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
    public Integer addCashDetail(InvInvoiceDTO dTO, Integer glBankTransactionId, Integer glBankTransactionSerial, TobyUser tobyUser) {

        GlBankTransactionDetail bankTransactionDetail = mapToEntityDetailFromInvSales(dTO, glBankTransactionId, glBankTransactionSerial, tobyUser);

        bankTransactionDetail = dao.updateEntity(bankTransactionDetail);
        bankTransactionDetail.getGlBankTransactionId().setGlYear(dTO.getGlYearSelection().getId());
        return tarhil(bankTransactionDetail, tobyUser);
    }

    public GlBankTransactionDetail mapToEntityDetailFromInvSales(InvInvoiceDTO dTO, Integer glBankTransactionId, Integer glBankTransactionSerial, TobyUser tobyUser) {
        GlBankTransactionDetail entity = new GlBankTransactionDetail();

        if (dTO.getGlBankId() != null && dTO.getGlBankId().getId() != null) {
            GlBank glbank = glBankService.findGlBank(dTO.getGlBankId().getId());
            if (glbank.getAccountId() != null) {
                GlAccount accountDebitId = new GlAccount();
                accountDebitId.setId(glbank.getAccountId().getId());
                entity.setAccountDebitId(accountDebitId);
                entity.setBankIdFrom(glbank);
            }
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

        entity.setRemarks(dTO.getRemarks());
        entity.setValue(dTO.getPayed() != null ? dTO.getPayed() : BigDecimal.ZERO);
        entity.setValueLocal(dTO.getPayed() != null ? dTO.getPayed() : BigDecimal.ZERO);

        entity.setSerial(finMaxSerial(glBankTransactionId));
        GlBankTransaction bankTransaction = new GlBankTransaction();
        bankTransaction.setId(glBankTransactionId);
        bankTransaction.setTransactionType(1);
        bankTransaction.setDate(dTO.getDate());
        bankTransaction.setSerial(glBankTransactionSerial);

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
            journal = generaljournalService.addGeneralJournalGlobal(dto.getGlBankTransactionId().getGlYear(), 1001, dto.getSerial(), dto.getGlBankTransactionId().getDate(),
                    dto.getGlBankTransactionId().getRemark(), tobyUser);

            // يتبقى ادخالى العموله يدويا
            if (dto.getOrganizationSiteId() != null
                    && dto.getBankIdTo().getGallaryId() != null
                    && dto.getBankIdTo().getGallaryId().getCostCenterId() != null
                    && dto.getBankIdTo().getGallaryId().getCostCenterId().getId() != null) {
                BigDecimal finalValue = dto.getValue();
                if (dto.getBankCommition().compareTo(BigDecimal.ZERO) == 1) {
                    // كيفية ادخال عمولة البنك
                    Integer accountId_3 = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"BANK_COMMITION\" AND a.accountId IS NOT NULL", params);
//                    BigDecimal commitionValue = dto.getValue().multiply(dto.getBankCommition().divide(new BigDecimal(100)));
                    finalValue = finalValue.subtract(dto.getBankCommition());
                    generaljournalDetailsService.addGenDetalil(true, dto.getBankCommition(), journal.getId(), accountId_3,
                            dto.getBankIdTo().getGallaryId().getCostCenterId().getId(),
                            dto.getRemarks(), tobyUser);
                }
                generaljournalDetailsService.addGenDetalil(true, finalValue, journal.getId(), dto.getAccountDebitId().getId(),
                        dto.getBankIdTo().getGallaryId().getCostCenterId().getId(),
                        dto.getRemarks(), tobyUser);
                generaljournalDetailsService.addGenDetalil(false, finalValue, journal.getId(), dto.getAccountCreditId().getId(),
                        dto.getBankIdTo().getGallaryId().getCostCenterId().getId(),
                        dto.getRemarks(), tobyUser);
                if (dto.getBankCommition().compareTo(BigDecimal.ZERO) == 1) {
                    generaljournalDetailsService.addGenDetalil(false, dto.getBankCommition(), journal.getId(), dto.getAccountCreditId().getId(),
                            dto.getBankIdTo().getGallaryId().getCostCenterId().getId(),
                            dto.getRemarks(), tobyUser);
                }

            }

        } else if (dto.getGlBankTransactionId().getTransactionType() == 1) {
            journal = generaljournalService.addGeneralJournalGlobal(dto.getGlBankTransactionId().getGlYear(), 1004, dto.getGlBankTransactionId().getSerial(), dto.getGlBankTransactionId().getDate(),
                    dto.getGlBankTransactionId().getRemark(), tobyUser);

            BigDecimal valueCommition = dto.getValue().multiply(dto.getBankIdFrom().getCommition().divide(new BigDecimal(100)));
            BigDecimal valueCommitionTax = (((dto.getValue().multiply(dto.getBankIdFrom().getCommition().divide(new BigDecimal(100)))).multiply(new BigDecimal(0.15))).setScale(5, RoundingMode.HALF_UP));
            BigDecimal valueAfterCommition = ((dto.getValue().subtract(valueCommition)).setScale(5, RoundingMode.HALF_UP));

            //  القيمة مطروح منها عمولة البنك
            if (dto.getOrganizationSiteId() != null
                    && dto.getBankIdFrom().getGallaryId() != null
                    && dto.getBankIdFrom().getGallaryId().getCostCenterId() != null
                    && dto.getBankIdFrom().getGallaryId().getCostCenterId().getId() != null) {

                generaljournalDetailsService.addGenDetalil(false, dto.getValue(), journal.getId(), dto.getAccountCreditId().getId(),
                        dto.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                        dto.getRemarks(), tobyUser);
                Integer bankCommition = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"BANK_COMMITION\" AND a.accountId IS NOT NULL", params);

                if (dto.getBankIdFrom().getCommition().compareTo(BigDecimal.ZERO) == 1) {

                    generaljournalDetailsService.addGenDetalil(true, valueCommition, journal.getId(), bankCommition,
                            dto.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                            dto.getRemarks(), tobyUser);
                }
                if (dto.getBankIdFrom() != null && dto.getBankIdFrom().getCommition() != null
                        && dto.getBankIdFrom().getCommition().compareTo(BigDecimal.ZERO) == 1) {

                    Integer bankTax = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"BANK_TAX\" AND a.accountId IS NOT NULL", params);

                    generaljournalDetailsService.addGenDetalil(true, valueCommitionTax, journal.getId(), bankTax,
                            dto.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                            dto.getRemarks(), tobyUser);
                    generaljournalDetailsService.addGenDetalil(false, valueCommitionTax, journal.getId(), bankCommition,
                            dto.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                            dto.getRemarks(), tobyUser);

                }
//                BigDecimal valueAfterCommitionANDTax = valueAfterCommition.add(valueCommitionTax);
                generaljournalDetailsService.addGenDetalil(true, valueAfterCommition, journal.getId(), dto.getAccountDebitId().getId(),
                        dto.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                        dto.getRemarks(), tobyUser);
            }
        }
        return journal == null ? null : journal.getId();
    }

    public List<GeneralJournalDTO> tarhilOne(TobyUser tobyUser) {

        List<GeneralJournalDTO> generalJournalDTOList = new ArrayList<>();

        Map<String, Object> params20 = new HashMap<>();
        params20.put("companyId", 125); 
        StringBuilder builder = new StringBuilder();
//        builder.append("SELECT s FROM GlBankTransactionDetail s WHERE  s.companyId.id = :companyId AND s.glBankTransactionId.transactionType = 1 AND s.bankIdFrom IS NOT NULL AND s.id > 3641 and s.id <= 18650 ");
        builder.append("SELECT s FROM GlBankTransactionDetail s WHERE  s.companyId.id = :companyId AND s.glBankTransactionId.transactionType = 1 AND s.bankIdFrom IS NOT NULL AND s.id > 18650  ");
//        builder.append("SELECT s FROM GlBankTransactionDetail s WHERE  s.companyId.id = :companyId AND s.glBankTransactionId.transactionType = 1 AND s.bankIdFrom IS NOT NULL AND id > 33650 and id <= 48650 ");
//        builder.append("SELECT s FROM GlBankTransactionDetail s WHERE  s.companyId.id = :companyId AND s.glBankTransactionId.transactionType = 1 AND s.bankIdFrom IS NOT NULL AND id > 48650 ");
        List<GlBankTransactionDetail> details = dao.findListByQuery(builder.toString(), params20);

        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        Integer bankCommition = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"BANK_COMMITION\" AND a.accountId IS NOT NULL", params);
        Integer bankTax = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"BANK_TAX\" AND a.accountId IS NOT NULL", params);
        Integer accountId_3 = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"BANK_COMMITION\" AND a.accountId IS NOT NULL", params);

        Integer xx = 1;
        for (GlBankTransactionDetail entity : details) {
            if (xx == 19773) {
                System.out.println("com.toby.businessservice.GlBankTransactionDetailsServiceImpl.tarhilOne()");
            }
            System.err.println("------------ > " + new Date() + " ----------> Number " + xx++);
            GeneralJournalDTO journal = null;

            if (entity.getGlBankTransactionId().getTransactionType() == 0) {
                journal = addGeneralJournalGlobal(73, 1001, entity.getSerial(), entity.getGlBankTransactionId().getDate(),
                        entity.getGlBankTransactionId().getRemark(), tobyUser, entity.getGlBankTransactionId().getId());

                // يتبقى ادخالى العموله يدويا
                if (entity.getOrganizationSiteId() != null
                        && entity.getBankIdTo().getGallaryId() != null
                        && entity.getBankIdTo().getGallaryId().getCostCenterId() != null
                        && entity.getBankIdTo().getGallaryId().getCostCenterId().getId() != null) {
                    BigDecimal finalValue = entity.getValue();
                    if (entity.getBankCommition().compareTo(BigDecimal.ZERO) == 1) {
                        // كيفية ادخال عمولة البنك
//                    BigDecimal commitionValue = dto.getValue().multiply(dto.getBankCommition().divide(new BigDecimal(100)));
                        finalValue = finalValue.subtract(entity.getBankCommition());
                        journal.getDetailList().add(addGenDetalil(1, true, entity.getBankCommition(), journal.getId(), accountId_3,
                                entity.getBankIdTo().getGallaryId().getCostCenterId().getId(),
                                entity.getRemarks(), tobyUser));
                    }
                    journal.getDetailList().add(addGenDetalil(2, true, finalValue, journal.getId(), entity.getAccountDebitId().getId(),
                            entity.getBankIdTo().getGallaryId().getCostCenterId().getId(),
                            entity.getRemarks(), tobyUser));
                    journal.getDetailList().add(addGenDetalil(3, false, finalValue, journal.getId(), entity.getAccountCreditId().getId(),
                            entity.getBankIdTo().getGallaryId().getCostCenterId().getId(),
                            entity.getRemarks(), tobyUser));
                    if (entity.getBankCommition().compareTo(BigDecimal.ZERO) == 1) {
                        journal.getDetailList().add(addGenDetalil(4, false, entity.getBankCommition(), journal.getId(), entity.getAccountCreditId().getId(),
                                entity.getBankIdTo().getGallaryId().getCostCenterId().getId(),
                                entity.getRemarks(), tobyUser));
                    }

                }

            } else if (entity.getGlBankTransactionId().getTransactionType() == 1) {
                journal = addGeneralJournalGlobal(73, 1004, entity.getGlBankTransactionId().getSerial(), entity.getGlBankTransactionId().getDate(),
                        entity.getGlBankTransactionId().getRemark(), tobyUser, entity.getGlBankTransactionId().getId());

                BigDecimal valueCommition = entity.getValue().multiply(entity.getBankIdFrom().getCommition().divide(new BigDecimal(100)));
                BigDecimal valueCommitionTax = (((entity.getValue().multiply(entity.getBankIdFrom().getCommition().divide(new BigDecimal(100)))).multiply(new BigDecimal(0.15))).setScale(5, RoundingMode.HALF_UP));
                BigDecimal valueAfterCommition = ((entity.getValue().subtract(valueCommition)).setScale(5, RoundingMode.HALF_UP));

                //  القيمة مطروح منها عمولة البنك
                if (entity.getOrganizationSiteId() != null
                        && entity.getBankIdFrom().getGallaryId() != null
                        && entity.getBankIdFrom().getGallaryId().getCostCenterId() != null
                        && entity.getBankIdFrom().getGallaryId().getCostCenterId().getId() != null) {

                    journal.getDetailList().add(addGenDetalil(1, false, entity.getValue(), journal.getId(), entity.getAccountCreditId().getId(),
                            entity.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                            entity.getRemarks(), tobyUser));

                    if (entity.getBankIdFrom().getCommition().compareTo(BigDecimal.ZERO) == 1) {

                        journal.getDetailList().add(addGenDetalil(2, true, valueCommition, journal.getId(), bankCommition,
                                entity.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                                entity.getRemarks(), tobyUser));
                    }
                    if (entity.getBankIdFrom() != null && entity.getBankIdFrom().getCommition() != null
                            && entity.getBankIdFrom().getCommition().compareTo(BigDecimal.ZERO) == 1) {

                        journal.getDetailList().add(addGenDetalil(3, true, valueCommitionTax, journal.getId(), bankTax,
                                entity.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                                entity.getRemarks(), tobyUser));
                        journal.getDetailList().add(addGenDetalil(4, false, valueCommitionTax, journal.getId(), bankCommition,
                                entity.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                                entity.getRemarks(), tobyUser));

                    }
                    if (entity.getAccountDebitId() != null && entity.getAccountDebitId().getId() != null
                            && entity.getBankIdFrom() != null && entity.getBankIdFrom().getGallaryId() != null
                            && entity.getBankIdFrom().getGallaryId().getCostCenterId() != null && entity.getBankIdFrom().getGallaryId().getCostCenterId().getId() != null) {
                        journal.getDetailList().add(addGenDetalil(5, true, valueAfterCommition, journal.getId(), entity.getAccountDebitId().getId(),
                                entity.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                                entity.getRemarks(), tobyUser));
                    }else{
                        System.out.println("----------> ERROR <-------------------");
                    }
                }
            }
            generalJournalDTOList.add(journal);
        }
        return generalJournalDTOList;
    }

    public GeneralJournalDTO addGeneralJournalGlobal(Integer yearSelectionId, Integer specialDocumentsTypes, Integer serial, Date date, String remark, TobyUser tobyUser, Integer id) {

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
//        generalJournal.setGeneralType(symbol != null ? symbol : null);
        generalJournal.setBranchId(tobyUser.getBranchId().getId());
        generalJournal.setCompanyId(tobyUser.getCompanyId().getId());
        generalJournal.setCreatedBy(tobyUser.getId());
        generalJournal.setCreatedDate(new Date());
        Map<String, Object> params = new HashMap<>();
        params.put("glYear", yearSelectionId == null ? null : yearSelectionId);

        return generalJournal;
    }

    public GeneralJournalDetails addGenDetalil(Integer serial, Boolean isDebit, BigDecimal value, Integer generalJournalId, Integer accountId, Integer costCenterId, String remark, TobyUser tobyUser) {

        GeneralJournalDetails generalJournalDetail = new GeneralJournalDetails();

//        if (generalJournalId != null) {
//            GeneralJournal journal = new GeneralJournal();
//            journal.setId(generalJournalId);
//            generalJournalDetail.setGeneralJournalId(journal);
//        }
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

        return generalJournalDetail;
    }

}
