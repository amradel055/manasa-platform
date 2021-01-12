/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.entity.CostCenter;
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

}
