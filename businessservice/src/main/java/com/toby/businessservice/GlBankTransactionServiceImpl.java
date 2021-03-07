/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.GlBankDTO;
import com.toby.dto.GlBankTransactionDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.entity.AccountsSystemSettings;
import com.toby.entity.GlBank;
import com.toby.entity.GlBankTransaction;
import com.toby.entity.GlBankTransactionDetail;
import com.toby.entity.InvPurchaseInvoice;
import com.toby.entity.Symbol;
import com.toby.entity.TobyUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author hq002
 */
@Stateless
public class GlBankTransactionServiceImpl implements GlBankTransactionService {

    @EJB
    private GenericDAO dao;
    private EntityManager em;

    @EJB
    GlBankTransactionDetailsService glBankTransactionDetailsService;

    @EJB
    AccountsSystemSettingsService accountsSystemSettingsService;

    @Override
    public GlBankTransaction addGlBankTransaction(GlBankTransaction glBankTransaction, List<GlBankTransactionDetail> glBankTransactionDetailList, List<GlBankTransactionDetail> glBankTransactionDetailListDeleted, Integer companyId) {

        addingForGlBank(glBankTransaction, glBankTransactionDetailListDeleted, glBankTransactionDetailList);
        if (glBankTransaction.getTransactionType() != null && glBankTransaction.getTransactionType() == 1) {

            AccountsSystemSettings accountsSystemSettings = new AccountsSystemSettings();
            accountsSystemSettings = accountsSystemSettingsService.getInventoryByCompanyId(companyId);

            if (accountsSystemSettings != null && accountsSystemSettings.getWorkWithAccounts() != null && accountsSystemSettings.getNoteSreceivablesType().equals("NOT_ALLOW_DETAIL")) {
                checkDuplicateDetails(glBankTransaction);
            }
        }
        return glBankTransaction;
    }

    public void addingForGlBank(GlBankTransaction glBankTransaction, List<GlBankTransactionDetail> glBankTransactionDetailListDeleted, List<GlBankTransactionDetail> glBankTransactionDetailList) {
        if (glBankTransaction.getId() != null) {
            dao.updateEntity(glBankTransaction);
        } else {

            glBankTransaction.setSerial(getMaxSerail(glBankTransaction));
            dao.saveEntity(glBankTransaction);
        }

        if (glBankTransactionDetailListDeleted != null && !glBankTransactionDetailListDeleted.isEmpty()) {
            for (GlBankTransactionDetail deleted : glBankTransactionDetailListDeleted) {
                glBankTransactionDetailsService.deleteGlBankTransactionDetails(deleted);
            }
        }

        Integer maxSerial = 0;
        if (glBankTransaction != null && glBankTransaction.getId() != null) {
            maxSerial = glBankTransactionDetailsService.finMaxSerial(glBankTransaction.getId());
        } else {
            maxSerial++;
        }

        for (GlBankTransactionDetail glBankTransactionDetail : glBankTransactionDetailList) {
            glBankTransactionDetail.setGlBankTransactionId(glBankTransaction);

            if (glBankTransactionDetail.getId() != null) {
                glBankTransactionDetailsService.updateGlBankTransactionDetails(glBankTransactionDetail);
            } else {
                glBankTransactionDetail.setSerial(maxSerial);
                glBankTransactionDetailsService.addGlBankTransactionDetails(glBankTransactionDetail);
                maxSerial++;
            }
        }
    }

    @Override
    public synchronized Integer getMaxSerail(GlBankTransaction glBankTransaction) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", glBankTransaction.getBranchId().getId());
        params.put("companyId", glBankTransaction.getCompanyId().getId());
        params.put("transactionType", glBankTransaction.getTransactionType());
        //params.put("glYear", glBankTransaction.getGlYear());

        Integer max = dao.findEntityByQuery("SELECT COALESCE(MAX(g.serial),0) +1  FROM GlBankTransaction g WHERE g.branchId.id =:branchId AND g.companyId.id =:companyId AND g.transactionType =:transactionType ", params);

        return max;
    }

    public void checkDuplicateDetails(GlBankTransaction glBankTransaction) {
        List<GlBankTransactionDetail> glBankTransactionDetailList = glBankTransactionDetailsService.getAllGlBankTransactionDetailsByGlBankTransactionId(glBankTransaction.getId());
        if (glBankTransactionDetailList.size() > 1) {
            glBankTransactionDetailsService.deleteGlBankTransactionDetails(glBankTransactionDetailList.get(glBankTransactionDetailList.size() - 1));
        }
    }

    @Override
    public void deleteGlBankTransaction(GlBankTransaction glBankTransaction) {
        if (glBankTransaction.getId() != null && glBankTransaction.getId() > 0) {
            dao.executeDeleteQuery("delete from GlBankTransactionDetail e WHERE e.id > 0 AND e.glBankTransactionId.id = " + glBankTransaction.getId());
        }
        dao.deleteEntity(glBankTransaction);
    }

    @Override
    public void deleteGlBankTransactionByInvoiceId(Integer invoiceId) {
        List<GlBankTransaction> list = getALLGlBankTransactionByInvoiceId(invoiceId);
        for (GlBankTransaction glBankTransaction : list) {
            if (glBankTransaction.getId() != null && glBankTransaction.getId() > 0) {
                dao.executeDeleteQuery("delete from GlBankTransactionDetail e WHERE e.id > 0 AND e.glBankTransactionId.id = " + glBankTransaction.getId());
            }
            dao.deleteEntity(glBankTransaction);
        }

    }

    @Override
    public List<GlBankTransaction> getALLGlBankTransactionByCompanyId(Integer companyId) {
        return dao.findEntityListByCompanyId(GlBankTransaction.class, companyId);
    }

    @Override
    public GlBankTransaction findGlBankTransactionById(Integer glBankTransactionId) {
        return dao.findEntityById(GlBankTransaction.class, glBankTransactionId);
    }

    @Override
    public GlBankTransaction addOneGlBankTransaction(GlBankTransaction glBankTransaction, GlBankTransactionDetail bankTransactionDetail) {
        if (glBankTransaction.getId() != null) {
            glBankTransaction = dao.updateEntity(glBankTransaction);
        } else {

            glBankTransaction.setSerial(getMaxSerail(glBankTransaction));
            dao.saveEntity(glBankTransaction);
        }

        bankTransactionDetail.setGlBankTransactionId(glBankTransaction);
        glBankTransactionDetailsService.updateGlBankTransactionDetails(bankTransactionDetail);
        return glBankTransaction;
    }

    @Override
    public List<GlBankTransaction> getALLGlBankTransactionSettlmentByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("transactionType", 0);
        List<GlBankTransaction> glBankTransactionList = dao.findListByQuery("SELECT e FROM GlBankTransaction e WHERE e.branchId.id = :branchId AND e.transactionType =:transactionType order by e.serial DESC ", params);
        return glBankTransactionList;
    }

    @Override
    public List<GlBankTransaction> getALLGlBankTransactionRecievableByBranchId(Integer branchId, Integer transactionType) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("transactionType", transactionType);
        return dao.findListByQuery("SELECT e FROM GlBankTransaction e WHERE e.branchId.id = :branchId AND e.transactionType =:transactionType order by e.serial DESC ", params);
    }

    @Override
    public List<GlBankTransaction> getALLGlBankTransactionRecievableNotloadByBranchId(Integer branchId, Integer transactionType) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("transactionType", transactionType);
        return dao.findListByQuery("SELECT e FROM GlBankTransaction e WHERE e.branchId.id = :branchId AND e.transactionType =:transactionType AND e.id not in ( select g.parent.id FROM GlBankTransaction g WHERE g.branchId.id = :branchId and g.parent is not null )  order by e.serial DESC ", params);
    }

    @Override
    public GlBankTransaction updateGlBankTransaction(GlBankTransaction glBankTransaction) {

        GlBankTransaction bankTransactionupdate = new GlBankTransaction();

        bankTransactionupdate = dao.findEntityById(GlBankTransaction.class, glBankTransaction.getId());
        bankTransactionupdate.setPostFlag(glBankTransaction.getPostFlag());
        if (glBankTransaction.getPostFlag().compareTo(1) == 0) {
            bankTransactionupdate.setGeneralJournalId(glBankTransaction.getGeneralJournalId());
        } else {
            bankTransactionupdate.setGeneralJournalId(null);
        }

        return dao.updateEntity(bankTransactionupdate);

    }

    @Override
    public GlBankTransaction addGlBankTransactionForInstallment(GlBankTransaction glBankTransaction, List<GlBankTransactionDetail> glBankTransactionDetailList, List<GlBankTransactionDetail> glBankTransactionDetailListDeleted) {
        addingForGlBank(glBankTransaction, glBankTransactionDetailListDeleted, glBankTransactionDetailList);
        /*if (glBankTransaction.getInstallmentId() != null) {
         InstContractDetail contractDetail = instContractDetailsService.findInstContractDetailsById(glBankTransaction.getInstallmentId().getId());
         contractDetail.setStatus(glBankTransaction.getFullPayment());
         instContractDetailsService.updateInstContractDetails(contractDetail);
         }*/
        return glBankTransaction;
    }

    @Override
    public Long getTotalRegistors(Integer transactionType, Integer branchId, Map<String, Object> filters) {
        StringBuilder builder = new StringBuilder();
        filters.put("transactionType", transactionType);
        builder.append("SELECT count(s) FROM GlBankTransaction s WHERE s.branchId.id = :branchId AND s.transactionType =:transactionType ");
        appendFilter(filters, branchId, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);

        return details;
    }

    @Override
    public Long getTotalRegistorsPre(Integer transactionType, Integer branchId, Map<String, Object> filters) {
        StringBuilder builder = new StringBuilder();
        filters.put("transactionType", transactionType);
        builder.append("SELECT count(s) FROM GlBankTransaction s WHERE s.branchId.id = :branchId AND s.transactionType =:transactionType AND s.id not in ( select g.parent.id FROM GlBankTransaction g WHERE g.branchId.id = :branchId and g.parent is not null )  ");
        appendFilter(filters, branchId, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);

        return details;
    }

    public void appendFilter(Map<String, Object> filters, Integer branchId, StringBuilder builder) {
        filters.put("branchId", branchId);
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and (s.serial like CONCAT('%',:globalFilter ,'%')  OR s.glBankId.name  like CONCAT('%',:globalFilter ,'%') OR s.valueLocal like CONCAT('%',:globalFilter ,'%') OR s.createdBy.name like CONCAT('%',:globalFilter ,'%')  OR s.remark like CONCAT('%',:globalFilter ,'%') OR s.remark2 like CONCAT('%',:globalFilter ,'%') OR s.value like CONCAT('%',:globalFilter ,'%') ) ";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }

    @Override
    public List<GlBankTransactionDTO> getALLGlBankTransactionRecievableByBranchId(Integer transactionType, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters) {
        filters.put("transactionType", transactionType);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT s FROM GlBankTransaction s WHERE s.branchId.id = :branchId AND s.transactionType =:transactionType  ");
        appendFilter(filters, branchId, builder);
        builder.append(" order by s.serial DESC");
        List<GlBankTransaction> details = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        return returnListDTO(details);
    }

    @Override
    public List<GlBankTransactionDTO> getALLGlBankTransactionRecievableNotloadByBranchId(Integer transactionType, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters) {
        filters.put("transactionType", transactionType);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT s FROM GlBankTransaction s WHERE s.branchId.id = :branchId AND s.transactionType =:transactionType AND s.id not in ( select g.parent.id FROM GlBankTransaction g WHERE g.branchId.id = :branchId and g.parent is not null )  ");
        appendFilter(filters, branchId, builder);
        builder.append(" order by s.serial DESC");
        List<GlBankTransaction> details = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        return returnListDTO(details);
    }

    public GlBankTransactionDTO mapTODTO(GlBankTransaction entity, Boolean needDetail) {

        GlBankTransactionDTO dTO = initMapToDTO(entity);

        if (needDetail) {

        }

        return dTO;
    }

    private GlBankTransactionDTO initMapToDTO(GlBankTransaction entity) {

        GlBankTransactionDTO dTO = new GlBankTransactionDTO();

        dTO.setId(entity.getId());
        dTO.setSerial(entity.getSerial());
        if (entity.getGlBankId() != null) {
            GlBankDTO bankDTO = new GlBankDTO();
            bankDTO.setId(entity.getGlBankId().getId());
            bankDTO.setName(entity.getGlBankId().getName());
            dTO.setGlBankId(bankDTO);
        }

        dTO.setDate(entity.getDate());
        dTO.setRemark(entity.getRemark());
        dTO.setRemark2(entity.getRemark2());
        dTO.setValue(entity.getValue());
        dTO.setValueLocal(entity.getValueLocal());
        dTO.setCreatedByName(entity.getCreatedBy().getName());
        dTO.setOrganizationType(entity.getOrganizationType());

        if (entity.getPaymentType() != null) {
            if (entity.getPaymentType() == 0) {
                dTO.setPaymentTypeName("نقدي");
            } else if (entity.getPaymentType() == 1) {
                dTO.setPaymentTypeName("الشيك");
            } else {
                dTO.setPaymentTypeName("الشيك الخطي");

            }
        }
        dTO.setPostFlag(entity.getPostFlag());
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());

        return dTO;
    }

    public GlBankTransaction mapToEntity(GlBankTransactionDTO dTO, TobyUser tobyUser) {
        GlBankTransaction entity = new GlBankTransaction();

        entity.setChequeDate(dTO.getChequeDate());
        entity.setChequeDueDate(dTO.getChequeDueDate());
        entity.setChequeNumber(dTO.getChequeNumber());
        entity.setChequeStatus(dTO.getChequeStatus());
        entity.setDate(dTO.getDate());
        if (dTO.getDocumentTypeId() != null && dTO.getDocumentTypeId().getId() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(dTO.getDocumentTypeId().getId());
            entity.setDocumentTypeId(symbol);
        }
        if (dTO.getGlBankId() != null && dTO.getGlBankId().getId() != null) {
            GlBank glBank = new GlBank();
            glBank.setId(dTO.getGlBankId().getId());
            entity.setGlBankId(glBank);
        }
        if (dTO.getInvoiceId() != null) {
            InvPurchaseInvoice invPurchaseInvoice = new InvPurchaseInvoice();
            invPurchaseInvoice.setId(dTO.getInvoiceId());
            entity.setInvoiceId(invPurchaseInvoice);
        }
        entity.setGlYear(dTO.getGlYear());
        entity.setOrganizationType(dTO.getOrganizationType());
        entity.setPaymentType(dTO.getPaymentType());
        entity.setPostFlag(dTO.getPostFlag());
        entity.setRate(dTO.getRate());
        entity.setRemark(dTO.getRemark());
        entity.setRemark2(dTO.getRemark2());
        entity.setSerial(dTO.getSerial());
        entity.setTransactionType(dTO.getTransactionType());
        entity.setValue(dTO.getValue());
        entity.setValueLocal(dTO.getValueLocal());

        if (dTO.getId() == null) {
            entity.setCreatedBy(tobyUser);
            entity.setCreationDate(new Date());

        } else {
            TobyUser tobyUser1 = new TobyUser();
            tobyUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(tobyUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(tobyUser);
        }

        entity.setBranchId(tobyUser.getBranchId());
        entity.setCompanyId(tobyUser.getCompanyId());
        return entity;
    }

    private List<GlBankTransactionDTO> returnListDTO(List<GlBankTransaction> list) {
        List<GlBankTransactionDTO> dTOList = new ArrayList<>();
        for (GlBankTransaction entity : list) {
            dTOList.add(mapTODTO(entity, false));
        }
        return dTOList;
    }

    @Override
    public List<GlBankTransaction> getALLGlBankTransactionByGeneralJournalId(Integer generalJournalId) {
        Map<String, Object> params = new HashMap<>();
        params.put("generalJournalId", generalJournalId);
        List<GlBankTransaction> glBankTransactionList = dao.findListByQuery("SELECT e FROM GlBankTransaction e WHERE e.generalJournalId.id = :generalJournalId ", params);
        return glBankTransactionList;
    }

    @Override
    public List<GlBankTransaction> getALLGlBankTransactionByInvoiceId(Integer invoiceId) {
        Map<String, Object> params = new HashMap<>();
        params.put("invoiceId", invoiceId);
        List<GlBankTransaction> glBankTransactionList = dao.findListByQuery("SELECT e FROM GlBankTransaction e WHERE e.invoiceId.id = :invoiceId ", params);
        return glBankTransactionList;
    }

    @Override
    public void addCash(InvInvoiceDTO dTO, TobyUser tobyUser) {

        GlBankTransaction glBankTransaction = mapToEntityFromInvSales(dTO, tobyUser);

        glBankTransaction = dao.updateEntity(glBankTransaction);

        glBankTransactionDetailsService.addCashDetail(dTO, glBankTransaction.getId(), tobyUser);
    }

    public GlBankTransaction mapToEntityFromInvSales(InvInvoiceDTO dTO, TobyUser tobyUser) {
        GlBankTransaction entity = new GlBankTransaction();

        entity.setDate(dTO.getDate());

        if (dTO.getGlBankId() != null && dTO.getGlBankId().getId() != null) {
            GlBank glBank = new GlBank();
            glBank.setId(dTO.getGlBankId().getId());
            entity.setGlBankId(glBank);
        }
        if (dTO.getId() != null) {
            InvPurchaseInvoice invPurchaseInvoice = new InvPurchaseInvoice();
            invPurchaseInvoice.setId(dTO.getId());
            entity.setInvoiceId(invPurchaseInvoice);
        }
        entity.setGlYear(dTO.getYear());
        entity.setOrganizationType(0);
        entity.setPaymentType(dTO.getPaymentType());
        entity.setPostFlag(0);
        entity.setRate(dTO.getRate());
        entity.setRemark(dTO.getRemarks());
        entity.setRemark2(dTO.getCreatedByName());
        entity.setSerial(dTO.getSerial());
        entity.setTransactionType(1);
        entity.setValue(dTO.getPayed());
        entity.setValueLocal(dTO.getPayed());
        entity.setCreatedBy(tobyUser);
        entity.setCreationDate(new Date());

        entity.setBranchId(tobyUser.getBranchId());
        entity.setCompanyId(tobyUser.getCompanyId());

        entity.setSerial(getMaxSerail(entity));

        return entity;
    }

}
