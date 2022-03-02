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
    public Integer updateGlBankTransactionDetails(GlBankTransactionDetail glBankTransactionDetail, TobyUser tobyUser) {
        GlBankTransactionDetail bankTransactionDetail = dao.updateEntity(glBankTransactionDetail);
        Integer id = null;
//        if (glBankTransactionDetail.getGlBankTransactionId() != null
//                && glBankTransactionDetail.getGlBankTransactionId().getGeneralJournalId() != null
//                && glBankTransactionDetail.getGlBankTransactionId().getGeneralJournalId().getId() != null) {
//            generaljournalService.deleteGeneralJournal(glBankTransactionDetail.getGlBankTransactionId().getGeneralJournalId().getId());
//        }
        id = tarhil(bankTransactionDetail, tobyUser);
        return id;
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

        // إدخال سندات الصرف
        if (dto.getGlBankTransactionId().getTransactionType() == 0) {
            journal = generaljournalService.addGeneralJournalGlobal(dto.getGlBankTransactionId().getGlYear(), 1001, dto.getGlBankTransactionId().getSerial(), dto.getGlBankTransactionId().getDate(),
                    dto.getGlBankTransactionId().getRemark(), tobyUser, 0, true);

            // يتبقى ادخالى العموله يدويا
            if (dto.getBankIdTo().getGallaryId() != null
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
                            dto.getGlBankTransactionId().getRemark(), tobyUser);
                }
                generaljournalDetailsService.addGenDetalil(true, finalValue, journal.getId(), dto.getAccountDebitId().getId(),
                        dto.getBankIdTo().getGallaryId().getCostCenterId().getId(),
                        dto.getGlBankTransactionId().getRemark(), tobyUser);
                generaljournalDetailsService.addGenDetalil(false, finalValue, journal.getId(), dto.getAccountCreditId().getId(),
                        dto.getBankIdTo().getGallaryId().getCostCenterId().getId(),
                        dto.getGlBankTransactionId().getRemark(), tobyUser);
                if (dto.getBankCommition().compareTo(BigDecimal.ZERO) == 1) {
                    generaljournalDetailsService.addGenDetalil(false, dto.getBankCommition(), journal.getId(), dto.getAccountCreditId().getId(),
                            dto.getBankIdTo().getGallaryId().getCostCenterId().getId(),
                            dto.getGlBankTransactionId().getRemark(), tobyUser);
                }

            }

            // ادخال سندات القبض
        } else if (dto.getGlBankTransactionId().getTransactionType() == 1) {
            journal = tarhilCash(journal, dto, tobyUser, params);
        } else if (dto.getGlBankTransactionId().getTransactionType() == 2) {
            journal = generaljournalService.addGeneralJournalGlobal(dto.getGlBankTransactionId().getGlYear(), 1007, dto.getGlBankTransactionId().getSerial(), dto.getGlBankTransactionId().getDate(),
                    dto.getGlBankTransactionId().getRemark(), tobyUser, 1, true);

            generaljournalDetailsService.addGenDetalil(true, dto.getGlBankTransactionId().getValueLocal(), journal.getId(), dto.getAccountDebitId().getId(),
                    dto.getAccountDebitId().getCostCenterId().getId(),
                    dto.getRemarks(), tobyUser);

            generaljournalDetailsService.addGenDetalil(false, dto.getGlBankTransactionId().getValueLocal(), journal.getId(), dto.getAccountCreditId().getId(),
                    dto.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                    dto.getRemarks(), tobyUser);
        }
        return journal == null ? null : journal.getId();
    }

    private GeneralJournal tarhilCash(GeneralJournal journal, GlBankTransactionDetail dto, TobyUser tobyUser, Map<String, Object> params) {
        journal = generaljournalService.addGeneralJournalGlobal(dto.getGlBankTransactionId().getGlYear(), 1004, dto.getGlBankTransactionId().getSerial(), dto.getGlBankTransactionId().getDate(),
                dto.getGlBankTransactionId().getRemark(), tobyUser, 1, true);
        String remark = "قبض نقدي رقم  " + dto.getGlBankTransactionId().getSerial() + " " + dto.getGlBankTransactionId().getGlBankId().getName();
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
                    remark, tobyUser);
            Integer bankCommition = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"BANK_COMMITION\" AND a.accountId IS NOT NULL", params);

            if (dto.getBankIdFrom().getCommition().compareTo(BigDecimal.ZERO) == 1) {

                generaljournalDetailsService.addGenDetalil(true, valueCommition.add(valueCommitionTax), journal.getId(), bankCommition,
                        dto.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                        remark, tobyUser);
            }
            if (dto.getBankIdFrom() != null && dto.getBankIdFrom().getCommition() != null
                    && dto.getBankIdFrom().getCommition().compareTo(BigDecimal.ZERO) == 1) {

                Integer bankTax = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"BANK_TAX\" AND a.accountId IS NOT NULL", params);

//                    generaljournalDetailsService.addGenDetalil(true, valueCommitionTax, journal.getId(), bankTax,
//                            dto.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
//                            dto.getRemarks(), tobyUser);
                generaljournalDetailsService.addGenDetalil(false, valueCommitionTax, journal.getId(), dto.getAccountDebitId().getId(),
                        dto.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                        remark, tobyUser);

            }
//                BigDecimal valueAfterCommitionANDTax = valueAfterCommition.add(valueCommitionTax);
            generaljournalDetailsService.addGenDetalil(true, valueAfterCommition, journal.getId(), dto.getAccountDebitId().getId(),
                    dto.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                    remark, tobyUser);
        }
        return journal;
    }

    @Override
    public List<GeneralJournalDTO> tarhilOne(TobyUser tobyUser, Integer type) {

        List<GeneralJournalDTO> generalJournalDTOList = new ArrayList<>();

        Map<String, Object> params20 = new HashMap<>();
        params20.put("companyId", 125);
        params20.put("type", type);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT s FROM GlBankTransactionDetail s WHERE  s.companyId.id = :companyId AND s.glBankTransactionId.transactionType = :type");
        if (type == 1) {
            builder.append(" AND s.bankIdFrom IS NOT NULL   ");
        } else {
            builder.append(" AND s.bankIdTo IS NOT NULL   ");
        }

        List<GlBankTransactionDetail> details = dao.findListByQuery(builder.toString(), params20);

        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        Integer bankCommition = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"BANK_COMMITION\" AND a.accountId IS NOT NULL", params);
        Integer bankTax = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"BANK_TAX\" AND a.accountId IS NOT NULL", params);
        Integer accountId_3 = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"BANK_COMMITION\" AND a.accountId IS NOT NULL", params);

        Integer xx = 1;
        Integer cashId = 100001;
        Integer SettelMentId = 90000;
        StringBuilder remark = new StringBuilder();
        for (GlBankTransactionDetail entity : details) {
            remark = new StringBuilder();
            remark.append("سند صرف  رقم  ").append(entity.getGlBankTransactionId().getSerial());
            if (entity.getOrganizationSiteId() != null) {
                remark.append(" عميل رقم ").append(entity.getOrganizationSiteId().getCode()).append(" ").append(entity.getOrganizationSiteId().getName());
            }
            if (entity.getInvoiceId() != null) {
                remark.append(" فاتورة رقم ");
                remark.append(entity.getInvoiceId().getSerial());
            }

            cashId++;
            SettelMentId++;
            System.err.println("------------ > " + entity.getGlBankTransactionId().getDate() + " ----------> Number " + xx++);
            GeneralJournalDTO journal = null;

            if (entity.getGlBankTransactionId().getTransactionType() == 0) {
                journal = addGeneralJournalGlobal(73, 1001, SettelMentId, entity.getGlBankTransactionId().getDate(),
                        remark.toString(), tobyUser, SettelMentId, 175);

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
                                remark.toString(), tobyUser));
                    }
                    journal.getDetailList().add(addGenDetalil(2, true, finalValue, journal.getId(), entity.getAccountDebitId().getId(),
                            entity.getBankIdTo().getGallaryId().getCostCenterId().getId(),
                            remark.toString(), tobyUser));
                    journal.getDetailList().add(addGenDetalil(3, false, finalValue, journal.getId(), entity.getAccountCreditId().getId(),
                            entity.getBankIdTo().getGallaryId().getCostCenterId().getId(),
                            remark.toString(), tobyUser));
                    if (entity.getBankCommition().compareTo(BigDecimal.ZERO) == 1) {
                        journal.getDetailList().add(addGenDetalil(4, false, entity.getBankCommition(), journal.getId(), entity.getAccountCreditId().getId(),
                                entity.getBankIdTo().getGallaryId().getCostCenterId().getId(),
                                remark.toString(), tobyUser));
                    }

                }

            } else if (entity.getGlBankTransactionId().getTransactionType() == 1) {
                journal = yarabFinal2(entity,
                        journal, cashId, tobyUser, bankCommition, bankTax);
            }
            generalJournalDTOList.add(journal);
        }
        return generalJournalDTOList;
    }

    private GeneralJournalDTO yarabFinal(GlBankTransactionDetail entity, GeneralJournalDTO journal, Integer cashId, TobyUser tobyUser, Integer bankCommition, Integer bankTax) {
        String remark2 = "قبض نقدي رقم  " + entity.getGlBankTransactionId().getSerial() + " " + entity.getGlBankTransactionId().getGlBankId().getName();
        journal = addGeneralJournalGlobal(73, 1004, cashId, entity.getGlBankTransactionId().getDate(),
                remark2, tobyUser, cashId, 178);
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
                    remark2, tobyUser));

            if (entity.getBankIdFrom().getCommition().compareTo(BigDecimal.ZERO) == 1) {

                journal.getDetailList().add(addGenDetalil(2, true, valueCommition, journal.getId(), bankCommition,
                        entity.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                        remark2, tobyUser));
            }
            if (entity.getBankIdFrom() != null && entity.getBankIdFrom().getCommition() != null
                    && entity.getBankIdFrom().getCommition().compareTo(BigDecimal.ZERO) == 1) {

                journal.getDetailList().add(addGenDetalil(3, true, valueCommitionTax, journal.getId(), bankTax,
                        entity.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                        remark2, tobyUser));
                journal.getDetailList().add(addGenDetalil(4, false, valueCommitionTax, journal.getId(), bankCommition,
                        entity.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                        remark2, tobyUser));

            }
            if (entity.getAccountDebitId() != null && entity.getAccountDebitId().getId() != null
                    && entity.getBankIdFrom() != null && entity.getBankIdFrom().getGallaryId() != null
                    && entity.getBankIdFrom().getGallaryId().getCostCenterId() != null && entity.getBankIdFrom().getGallaryId().getCostCenterId().getId() != null) {
                journal.getDetailList().add(addGenDetalil(5, true, valueAfterCommition, journal.getId(), entity.getAccountDebitId().getId(),
                        entity.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                        remark2, tobyUser));
            } else {
                System.out.println("----------> ERROR <-------------------");
            }
        }
        return journal;
    }

    private GeneralJournalDTO yarabFinal2(GlBankTransactionDetail dto, GeneralJournalDTO journal, Integer cashId, TobyUser tobyUser, Integer bankCommition, Integer bankTax) {
        String remark2 = "قبض نقدي رقم  " + dto.getGlBankTransactionId().getSerial() + " " + dto.getGlBankTransactionId().getGlBankId().getName();
        journal = addGeneralJournalGlobal(73, 1004, dto.getGlBankTransactionId().getSerial(), dto.getGlBankTransactionId().getDate(),
                remark2, tobyUser, null, 178);
        journal.setDocumentId(dto.getGlBankTransactionId().getId());
        BigDecimal valueCommition = dto.getValue().multiply(dto.getBankIdFrom().getCommition().divide(new BigDecimal(100)));
        BigDecimal valueCommitionTax = (((dto.getValue().multiply(dto.getBankIdFrom().getCommition().divide(new BigDecimal(100)))).multiply(new BigDecimal(0.15))).setScale(5, RoundingMode.HALF_UP));
        BigDecimal valueAfterCommition = ((dto.getValue().subtract(valueCommition)).setScale(5, RoundingMode.HALF_UP));

        //  القيمة مطروح منها عمولة البنك
        if (dto.getOrganizationSiteId() != null
                && dto.getBankIdFrom().getGallaryId() != null
                && dto.getBankIdFrom().getGallaryId().getCostCenterId() != null
                && dto.getBankIdFrom().getGallaryId().getCostCenterId().getId() != null) {

            journal.getDetailList().add(addGenDetalil(1, false, dto.getValue(), journal.getId(), dto.getAccountCreditId().getId(),
                    dto.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                    remark2, tobyUser));

            if (dto.getBankIdFrom().getCommition().compareTo(BigDecimal.ZERO) == 1) {

                journal.getDetailList().add(addGenDetalil(2, true, valueCommition.add(valueCommitionTax), null, bankCommition,
                        dto.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                        remark2, tobyUser));
            }

            if (dto.getBankIdFrom() != null && dto.getBankIdFrom().getCommition() != null
                    && dto.getBankIdFrom().getCommition().compareTo(BigDecimal.ZERO) == 1) {

                journal.getDetailList().add(addGenDetalil(4, false, valueCommitionTax, null, dto.getAccountDebitId().getId(),
                        dto.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                        remark2, tobyUser));

            }
//            if (dto.getAccountDebitId() != null && dto.getAccountDebitId().getId() != null
//                    && dto.getBankIdFrom() != null && dto.getBankIdFrom().getGallaryId() != null
//                    && dto.getBankIdFrom().getGallaryId().getCostCenterId() != null && dto.getBankIdFrom().getGallaryId().getCostCenterId().getId() != null) {
                journal.getDetailList().add(addGenDetalil(5, true, valueAfterCommition, journal.getId(), dto.getAccountDebitId().getId(),
                        dto.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                        remark2, tobyUser));
//            } else {
//                System.out.println("----------> ERROR <-------------------");
//            }
        }
        return journal;
    }

    public GeneralJournalDTO addGeneralJournalGlobal(Integer yearSelectionId, Integer specialDocumentsTypes, Integer serial,
            Date date, String remark, TobyUser tobyUser, Integer id, Integer symbolId) {

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
        symbol.setId(symbolId);
        generalJournal.setGeneralType(symbol != null ? symbol : null);
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

    @Override
    public Integer updateGlBankTransactionDetails(List<GlBankTransactionDetail> glBankTransactionDetailList, GlBankTransaction glBankTransaction, TobyUser tobyUser) {
        Integer maxSerial = 0;
        Integer id = null;
        if (glBankTransaction != null && glBankTransaction.getId() != null) {
            maxSerial = finMaxSerial(glBankTransaction.getId());
        } else {
            maxSerial++;
        }
        for (GlBankTransactionDetail glBankTransactionDetail : glBankTransactionDetailList) {
            glBankTransactionDetail.setGlBankTransactionId(glBankTransaction);

            if (glBankTransactionDetail.getId() != null) {
                glBankTransactionDetail = dao.updateEntity(glBankTransactionDetail);
            } else {
                glBankTransactionDetail.setSerial(maxSerial);
                glBankTransactionDetail = dao.updateEntity(glBankTransactionDetail);
                maxSerial++;
            }

            if (glBankTransactionDetail.getGlBankTransactionId() != null
                    && glBankTransactionDetail.getGlBankTransactionId().getGeneralJournalId() != null
                    && glBankTransactionDetail.getGlBankTransactionId().getGeneralJournalId().getId() != null) {

            }
            if (glBankTransaction.getOrganizationType() == 2) {
                id = tarhil(glBankTransactionDetail, tobyUser);
            }
        }

        if (glBankTransaction.getOrganizationType() == 0) {
            id = tarhilSettlement(glBankTransactionDetailList, glBankTransaction, tobyUser, 1001, true);
        }

        return id;
    }

    @Override
    public Integer tarhilSettlement(List<GlBankTransactionDetail> list, GlBankTransaction glBankTransaction, TobyUser tobyUser, Integer specialDocumentsTypes, boolean postFlag) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        Integer currentAccountId = null;
        Integer accountId = null;
        Integer costCenterIdMain = null;
        GeneralJournal journal = null;

        StringBuilder remark = new StringBuilder();
        remark.append("سند صرف  رقم  ").append(glBankTransaction.getSerial());

//        BigDecimal finalValue = BigDecimal.ZERO;
        BigDecimal finalValueCurrent = BigDecimal.ZERO;
//        Boolean check = false;
        Integer accountId_3 = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"BANK_COMMITION\" AND a.accountId IS NOT NULL", params);

        // إدخال سندات الصرف
        journal = generaljournalService.addGeneralJournalGlobal(glBankTransaction.getGlYear(), specialDocumentsTypes, glBankTransaction.getSerial(), glBankTransaction.getDate(),
                glBankTransaction.getRemark() + remark.toString(), tobyUser, 0, postFlag);
//        finalValue = glBankTransaction.getValueLocal().subtract(glBankTransaction.getBankCommetion());

        if (glBankTransaction.getGlAccountId() != null) {
            GlAccount account = dao.findEntityById(GlAccount.class, glBankTransaction.getGlAccountId().getId());
            accountId = account.getId();
            if (glBankTransaction.getCostCenter() != null) {
                costCenterIdMain = glBankTransaction.getCostCenter().getId();
                CostCenter costCenter = dao.findEntityById(CostCenter.class, costCenterIdMain);
                currentAccountId = costCenter.getCurrentaccountId().getId();
            } else {
                costCenterIdMain = account.getCostCenterId().getId();
                currentAccountId = account.getCostCenterId().getCurrentaccountId().getId();
            }

        } else {
            accountId = glBankTransaction.getGlBankId().getAccountId().getId();
            costCenterIdMain = glBankTransaction.getGlBankId().getGallaryId().getCostCenterId().getId();
        }

        for (GlBankTransactionDetail dto : list) {

            if (dto.getOrganizationSiteId() != null) {
                remark.append(" عميل رقم ").append(dto.getOrganizationSiteId().getCode()).append(" ").append(dto.getOrganizationSiteId().getName());
            }
            if (dto.getInvoiceId() != null) {
                remark.append(" فاتورة رقم ");
                remark.append(dto.getInvoiceId().getSerial());
            }

            generaljournalDetailsService.addGenDetalil(false, dto.getValue(), journal.getId(), accountId,
                    costCenterIdMain,
                    glBankTransaction.getRemark() + remark.toString(), tobyUser);

            Integer costCeterDetailId = dto.getCostCenterId().getId();

            if (dto.getCostCenterId() != null
                    && dto.getCostCenterId().getId() != null) {
                generaljournalDetailsService.addGenDetalil(true, dto.getValue(), journal.getId(), dto.getAccountDebitId().getId(),
                        costCeterDetailId,
                        dto.getRemarks() + remark.toString(), tobyUser);
            }

            if (dto.getBankCommition().compareTo(BigDecimal.ZERO) == 1) {
                // كيفية ادخال عمولة البنك
                generaljournalDetailsService.addGenDetalil(false, dto.getBankCommition(), journal.getId(), accountId,
                        costCenterIdMain,
                        glBankTransaction.getRemark() + remark.toString(), tobyUser);
                generaljournalDetailsService.addGenDetalil(true, dto.getBankCommition(), journal.getId(), accountId_3,
                        costCeterDetailId,
                        "عمولة البنك" + remark.toString(), tobyUser);
            }

            if (!costCeterDetailId.equals(costCenterIdMain)) {
                finalValueCurrent = finalValueCurrent.add(dto.getValue());
                generaljournalDetailsService.addGenDetalil(true, dto.getValue(), journal.getId(), dto.getCostCenterId().getCurrentaccountId().getId(),
                        //                        dto.getCostCenterId().getId(),
                        costCenterIdMain,
                        dto.getRemarks() + remark.toString(), tobyUser);
                generaljournalDetailsService.addGenDetalil(false,
                        dto.getValue(),
                        journal.getId(),
                        currentAccountId,
                        dto.getCostCenterId().getId(),
                        glBankTransaction.getRemark() + remark.toString(), tobyUser);
                if (dto.getBankCommition().compareTo(BigDecimal.ZERO) == 1) {
                    generaljournalDetailsService.addGenDetalil(true, dto.getBankCommition(),
                            journal.getId(),
                            dto.getCostCenterId().getCurrentaccountId().getId(),
                            //                        dto.getCostCenterId().getId(),
                            costCenterIdMain,
                            dto.getRemarks() + remark.toString(), tobyUser);
                    generaljournalDetailsService.addGenDetalil(false,
                            dto.getBankCommition(),
                            journal.getId(),
                            currentAccountId,
                            dto.getCostCenterId().getId(),
                            glBankTransaction.getRemark() + remark.toString(), tobyUser);
                }
            }
        }

        return journal == null ? null : journal.getId();
    }

    @Override
    public Integer tarhilSettlementVoucher(List<GlBankTransactionDetail> list1, GlBankTransaction glBankTransaction, TobyUser tobyUser,
            Integer specialDocumentsTypes, boolean postFlag) {
        Boolean debit = true;
        Boolean credit = false;
        if (glBankTransaction.getIsDebit() != null && glBankTransaction.getIsDebit()) {
            debit = false;
            credit = true;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());

        GeneralJournal journal = null;

        StringBuilder remark = new StringBuilder();
        remark.append(glBankTransaction.getRemark());

        BigDecimal finalValueCurrent = BigDecimal.ZERO;
        Integer accountId_3 = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"BANK_COMMITION\" AND a.accountId IS NOT NULL", params);

        journal = generaljournalService.addGeneralJournalGlobal(glBankTransaction.getGlYear(), specialDocumentsTypes, glBankTransaction.getSerial(), glBankTransaction.getDate(),
                glBankTransaction.getRemark(), tobyUser, 0, postFlag);

        Map<Integer, List<GlBankTransactionDetail>> map = new HashMap<>();
        for (GlBankTransactionDetail detail : list1) {
            List<GlBankTransactionDetail> transactionDetails = new ArrayList<>();
            if (map.containsKey(detail.getGlAccountCredit().getId())) {
                transactionDetails = map.get(detail.getGlAccountCredit().getId());
            }
            transactionDetails.add(detail);
            map.put(detail.getGlAccountCredit().getId(), transactionDetails);
        }

        List<Integer> keyList = new ArrayList<Integer>(map.keySet());

//        ArrayList<Integer> keyList = new ArrayList<Integer>(map.keySet());
//      ArrayList<String> valueList = new ArrayList<String>(map.values());
        for (Integer accountId : keyList) {
            BigDecimal valueTotal = BigDecimal.ZERO;
            String remarkAshreaf = "";
            List<GlBankTransactionDetail> detailList = map.get(accountId);
//            Integer currentAccountId = null;
//            Integer accountId = null;
//            Integer costCenterIdMain = null;
//            GlAccount account = dao.findEntityById(GlAccount.class, accountId);
//            accountId = account.getId();
            Integer costCenterIdMain = detailList.get(0).getCostCenterCredit().getId();
            CostCenter costCenter = dao.findEntityById(CostCenter.class, costCenterIdMain);
            Integer currentAccountId = costCenter.getCurrentaccountId().getId();

            for (GlBankTransactionDetail dto : detailList) {

                if (dto.getOrganizationSiteId() != null) {
                    remark.append(" عميل رقم ").append(dto.getOrganizationSiteId().getCode()).append(" ").append(dto.getOrganizationSiteId().getName());
                }
                if (dto.getInvoiceId() != null) {
                    remark.append(" فاتورة رقم ");
                    remark.append(dto.getInvoiceId().getSerial());
                }

                Integer costCeterDetailId = dto.getCostCenterId().getId();

                if (dto.getCostCenterId() != null
                        && dto.getCostCenterId().getId() != null) {
                    generaljournalDetailsService.addGenDetalil(debit, dto.getValue(), journal.getId(), dto.getAccountDebitId().getId(),
                            costCeterDetailId,
                            dto.getRemarks(), tobyUser);
                }

                if (dto.getBankCommition().compareTo(BigDecimal.ZERO) == 1) {
                    // كيفية ادخال عمولة البنك
                    generaljournalDetailsService.addGenDetalil(credit, dto.getBankCommition(), journal.getId(), accountId,
                            costCenterIdMain,
                            glBankTransaction.getRemark() + remark.toString(), tobyUser);
                    generaljournalDetailsService.addGenDetalil(debit, dto.getBankCommition(), journal.getId(), accountId_3,
                            costCeterDetailId,
                            " عمولة البنك " + dto.getRemarks(), tobyUser);
                }

                if (!costCeterDetailId.equals(costCenterIdMain)) {
                    finalValueCurrent = finalValueCurrent.add(dto.getValue());
                    generaljournalDetailsService.addGenDetalil(debit, dto.getValue(), journal.getId(), dto.getCostCenterId().getCurrentaccountId().getId(),
                            //                        dto.getCostCenterId().getId(),
                            costCenterIdMain,
                            dto.getRemarks(), tobyUser);
                    generaljournalDetailsService.addGenDetalil(credit,
                            dto.getValue(),
                            journal.getId(),
                            currentAccountId,
                            dto.getCostCenterId().getId(),
                            dto.getRemarks(), tobyUser);
                    if (dto.getBankCommition().compareTo(BigDecimal.ZERO) == 1) {
                        generaljournalDetailsService.addGenDetalil(debit, dto.getBankCommition(),
                                journal.getId(),
                                dto.getCostCenterId().getCurrentaccountId().getId(),
                                //                        dto.getCostCenterId().getId(),
                                costCenterIdMain,
                                dto.getRemarks(), tobyUser);
                        generaljournalDetailsService.addGenDetalil(credit,
                                dto.getBankCommition(),
                                journal.getId(),
                                currentAccountId,
                                dto.getCostCenterId().getId(),
                                dto.getRemarks(), tobyUser);
                    }
                }
                valueTotal = valueTotal.add(dto.getValue());
                remarkAshreaf = dto.getRemarks();
            }
            generaljournalDetailsService.addGenDetalil(credit, valueTotal, journal.getId(), accountId,
                    costCenterIdMain,
                    remarkAshreaf, tobyUser);
        }

        return journal == null ? null : journal.getId();
    }

    @Override
    public List<GeneralJournal> tarhilCashOne(TobyUser tobyUser, Integer type) {

        List<GeneralJournal> generalJournalDTOList = new ArrayList<>();

        Map<String, Object> params20 = new HashMap<>();
        params20.put("companyId", 125);
        params20.put("type", type);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT s FROM GlBankTransactionDetail s WHERE  s.companyId.id = :companyId AND s.glBankTransactionId.transactionType = :type");
        if (type == 1) {
            builder.append(" AND s.bankIdFrom IS NOT NULL   ");
        } else {
            builder.append(" AND s.bankIdTo IS NOT NULL   ");
        }

        List<GlBankTransactionDetail> details = dao.findListByQuery(builder.toString(), params20);

        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        Integer bankCommition = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"BANK_COMMITION\" AND a.accountId IS NOT NULL", params);

        Integer xx = 1;
        StringBuilder remark = new StringBuilder();
        for (GlBankTransactionDetail entity : details) {
            remark = new StringBuilder();
            remark.append("سند صرف  رقم  ").append(entity.getGlBankTransactionId().getSerial());
            if (entity.getOrganizationSiteId() != null) {
                remark.append(" عميل رقم ").append(entity.getOrganizationSiteId().getCode()).append(" ").append(entity.getOrganizationSiteId().getName());
            }
            if (entity.getInvoiceId() != null) {
                remark.append(" فاتورة رقم ");
                remark.append(entity.getInvoiceId().getSerial());
            }

            System.err.println("------------ > " + entity.getGlBankTransactionId().getDate() + " ----------> Number " + xx++);
            GeneralJournal journal = null;

            if (entity.getGlBankTransactionId().getTransactionType() == 1) {
                journal = tarhilCash1(entity, tobyUser, bankCommition);
            }
            generalJournalDTOList.add(journal);
        }
        return generalJournalDTOList;
    }

    private GeneralJournal tarhilCash1(GlBankTransactionDetail dto,
            TobyUser tobyUser, Integer bankCommition) {
        GeneralJournal journal1 = generaljournalService.addGeneralJournalGlobal(dto.getGlBankTransactionId().getGlYear(), 1004, dto.getGlBankTransactionId().getSerial(), dto.getGlBankTransactionId().getDate(),
                dto.getGlBankTransactionId().getRemark(), tobyUser, 1, true);
        String remark = "قبض نقدي رقم  " + dto.getGlBankTransactionId().getSerial() + " " + dto.getGlBankTransactionId().getGlBankId().getName();
        BigDecimal valueCommition = dto.getValue().multiply(dto.getBankIdFrom().getCommition().divide(new BigDecimal(100)));
        BigDecimal valueCommitionTax = (((dto.getValue().multiply(dto.getBankIdFrom().getCommition().divide(new BigDecimal(100)))).multiply(new BigDecimal(0.15))).setScale(5, RoundingMode.HALF_UP));
        BigDecimal valueAfterCommition = ((dto.getValue().subtract(valueCommition)).setScale(5, RoundingMode.HALF_UP));
        //  القيمة مطروح منها عمولة البنك
        if (dto.getOrganizationSiteId() != null
                && dto.getBankIdFrom().getGallaryId() != null
                && dto.getBankIdFrom().getGallaryId().getCostCenterId() != null
                && dto.getBankIdFrom().getGallaryId().getCostCenterId().getId() != null) {

            generaljournalDetailsService.addGenDetalil(false, dto.getValue(), journal1.getId(), dto.getAccountCreditId().getId(),
                    dto.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                    remark, tobyUser);
//            Integer bankCommition = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"BANK_COMMITION\" AND a.accountId IS NOT NULL", params);

            if (dto.getBankIdFrom().getCommition().compareTo(BigDecimal.ZERO) == 1) {

                generaljournalDetailsService.addGenDetalil(true, valueCommition.add(valueCommitionTax), journal1.getId(), bankCommition,
                        dto.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                        remark, tobyUser);
            }
            if (dto.getBankIdFrom() != null && dto.getBankIdFrom().getCommition() != null
                    && dto.getBankIdFrom().getCommition().compareTo(BigDecimal.ZERO) == 1) {

//                Integer bankTax = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"BANK_TAX\" AND a.accountId IS NOT NULL", params);
//                    generaljournalDetailsService.addGenDetalil(true, valueCommitionTax, journal.getId(), bankTax,
//                            dto.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
//                            dto.getRemarks(), tobyUser);
                generaljournalDetailsService.addGenDetalil(false, valueCommitionTax, journal1.getId(), dto.getAccountDebitId().getId(),
                        dto.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                        remark, tobyUser);

            }
//                BigDecimal valueAfterCommitionANDTax = valueAfterCommition.add(valueCommitionTax);
            generaljournalDetailsService.addGenDetalil(true, valueAfterCommition, journal1.getId(), dto.getAccountDebitId().getId(),
                    dto.getBankIdFrom().getGallaryId().getCostCenterId().getId(),
                    remark, tobyUser);
        }
        return journal1;
    }

}
