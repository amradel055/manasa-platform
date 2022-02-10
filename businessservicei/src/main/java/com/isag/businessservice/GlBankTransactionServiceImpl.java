/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.SubAccountSummarySearchBean;
import com.isag.core.GenericDAO;
import com.isag.dto.GlBankDTO;
import com.isag.dto.GlBankTransactionDTO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.AccountsSystemSettings;
import com.isag.entity.GlBank;
import com.isag.entity.GlBankTransaction;
import com.isag.entity.GlBankTransactionDetail;
import com.isag.entity.InvPurchaseInvoice;
import com.isag.entity.IsagUser;
import com.isag.views.CashAccountSettlmentView;
import com.isag.views.SupplierAccountViewBean;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
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
    private BigDecimal value;
    private Map<Integer, BigDecimal> balance = new HashMap<>();
    private Map<Integer, BigDecimal> openingBalance = new HashMap<>();
    GlBankDTO bankDTOTo = new GlBankDTO();

    @EJB
    GlBankTransactionDetailsService glBankTransactionDetailsService;
    @EJB
    InstContractDetailsService instContractDetailsService;

    @EJB
    AccountsSystemSettingsService accountsSystemSettingsService;
    @EJB
    private IsagUserBankService isagUserBankService;
    @EJB
    private GlBankService bankService;

    @Override
    public GlBankTransaction addGlBankTransaction(GlBankTransaction glBankTransaction, List<GlBankTransactionDetail> glBankTransactionDetailList, List<GlBankTransactionDetail> glBankTransactionDetailListDeleted, Integer companyId) {

        addingForGlBank(glBankTransaction, glBankTransactionDetailListDeleted, glBankTransactionDetailList);
        if (glBankTransaction.getTransactionType() != null && glBankTransaction.getTransactionType() == 1) {

            AccountsSystemSettings accountsSystemSettings = accountsSystemSettingsService.getInventoryByCompanyId(companyId);

            if (accountsSystemSettings != null && accountsSystemSettings.getWorkWithAccounts() != null && accountsSystemSettings.getNoteSreceivablesType().equals("NOT_ALLOW_DETAIL")) {
                checkDuplicateDetails(glBankTransaction);
            }
        }
        return glBankTransaction;
    }

    public GlBankTransaction addingForGlBank(GlBankTransaction glBankTransaction, List<GlBankTransactionDetail> glBankTransactionDetailListDeleted, List<GlBankTransactionDetail> glBankTransactionDetailList) {
        String error = null;
        try {
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
        } catch (Exception e) {
//            e.printStackTrace();
            error = "هذا الشيت تم ادخاله من قبل";
            glBankTransaction.setMsg(error);
        }
        return glBankTransaction;
    }

    @Override
    public synchronized Integer getMaxSerail(GlBankTransaction glBankTransaction) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", glBankTransaction.getBranchId().getId());
        params.put("companyId", glBankTransaction.getCompanyId().getId());
        params.put("transactionType", glBankTransaction.getTransactionType());
        //params.put("glYear", glBankTransaction.getGlYear());
        Integer max = null;
        AccountsSystemSettings accountsSystemSettings = new AccountsSystemSettings();
        accountsSystemSettings = accountsSystemSettingsService.getInventoryByCompanyId(glBankTransaction.getCompanyId().getId());

        if ("ALLOW_SERIAL".equals(accountsSystemSettings.getBankSerial())) {
            params.put("bankid", glBankTransaction.getGlBankId().getId());
            if (glBankTransaction.getTransactionType() == 2) {
                //في حاله تحويل الخزائن
                max = dao.findEntityByQuery("SELECT COALESCE(MAX(g.glBankTransactionId.serial),0) +1  FROM GlBankTransactionDetail g WHERE g.glBankTransactionId.branchId.id =:branchId AND g.glBankTransactionId.companyId.id =:companyId AND g.glBankTransactionId.transactionType =:transactionType AND g.bankIdFrom.id = :bankid", params);
            } else {
                max = dao.findEntityByQuery("SELECT COALESCE(MAX(g.serial),0) +1  FROM GlBankTransaction g WHERE g.branchId.id =:branchId AND g.companyId.id =:companyId AND g.transactionType =:transactionType AND g.glBankId.id = :bankid", params);
            }
            return max;
        } else {
            max = dao.findEntityByQuery("SELECT COALESCE(MAX(g.serial),0) +1  FROM GlBankTransaction g WHERE g.branchId.id =:branchId AND g.companyId.id =:companyId AND g.transactionType =:transactionType ", params);
        }

        return max;
    }

    @Override
    public synchronized Integer getMaxDocumentSerial(IsagUser isagUser, Integer glBankid) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", isagUser.getBranchId().getId());
        params.put("companyId", isagUser.getCompanyId().getId());
        params.put("glBankId", glBankid);
        Integer max = dao.findEntityByQuery("SELECT COALESCE(MAX(g.documentSerial),0) +1  FROM GlBankTransactionDetail e LEFT JOIN e.glBankTransactionId g Left JOIN e.bankIdTo  b  WHERE g.branchId.id =:branchId AND g.companyId.id =:companyId AND ( (g.glBankId.id = :glBankId) or (e.bankIdFrom  is not null  and b.id = :glBankId))", params);
        return max;
    }

    @Override
    public Boolean checkDocumentSerial(Integer branchId, Integer glBankid) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        //    params.put("companyId", isagUser.getCompanyId().getId());
        params.put("glBankId", glBankid);
        Long max = dao.findEntityByQuery("SELECT COALESCE(MAX(g.documentSerial),0)   FROM GlBankTransactionDetail e LEFT JOIN e.glBankTransactionId g Left JOIN e.bankIdTo  b  WHERE g.branchId.id =:branchId  AND ( (g.glBankId.id = :glBankId) or (e.bankIdFrom  is not null  and b.id = :glBankId))", params);
        return max != null && max > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public synchronized Integer closePeriod(List<CashAccountSettlmentView> cashAccountSettlmentViews, IsagUser isagUser) {

        if (cashAccountSettlmentViews != null && !cashAccountSettlmentViews.isEmpty()) {
            List<Integer> idList = new ArrayList();
            for (CashAccountSettlmentView cashAccountSettlmentView : cashAccountSettlmentViews) {
                idList.add(cashAccountSettlmentView.getBanktransactionId());
            }
            if (idList != null && !idList.isEmpty()) {
                Integer max = getMaxDocumentSerial(isagUser, cashAccountSettlmentViews.get(0).getBankId());
                Map<String, Object> params = new HashMap<>();
                params.put("id", idList);
                params.put("documentSerial", max);
                params.put("modifiedBy", isagUser);
                params.put("modificationDate", new Date());
                params.put("branchId", isagUser.getBranchId().getId());
                String query = "UPDATE GlBankTransaction g SET g.documentSerial = :documentSerial  , g.modificationDate = :modificationDate , g.modifiedBy = :modifiedBy  WHERE g.id IN :id AND g.documentSerial is null AND g.branchId.id = :branchId";
                dao.executeUpdateQuery(query, params);
                return max;
            }
        }
        return null;
    }

    @Override
    public List<GlBankTransactionDTO> getALLGlBankTransactionIsClosedPeriod(SubAccountSummarySearchBean subAccountSummarySearchBean) {
        balance.clear();
        if (subAccountSummarySearchBean != null && subAccountSummarySearchBean.getBranchId() != null) {
            //set opening balance
            getALLGlBankTransactionOpeningBalance(subAccountSummarySearchBean);
            //
            Map<String, Object> params = new HashMap<>();
            params.put("branchId", subAccountSummarySearchBean.getBranchId());
            params.put("glBankId", subAccountSummarySearchBean.getBankDTO().getId());
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT e FROM GlBankTransactionDetail g LEFT JOIN g.glBankTransactionId e Left JOIN  e.glBankId gbank Left JOIN g.bankIdTo b Left JOIN g.bankIdFrom bf WHERE e.branchId.id = :branchId AND e.documentSerial is not null and ( (gbank is not null and  gbank.id = :glBankId) or (gbank  is  null  and (b.id = :glBankId or bf.id = :glBankId)))");
            if (subAccountSummarySearchBean.getFromDocumentSerial() != null) {
                builder.append(" AND e.documentSerial >= :fromDocumentSerial ");
                params.put("fromDocumentSerial", subAccountSummarySearchBean.getFromDocumentSerial());
            }
            if (subAccountSummarySearchBean.getToDocumentSerial() != null) {
                builder.append(" AND e.documentSerial <= :toDocumentSerial ");
                params.put("toDocumentSerial", subAccountSummarySearchBean.getToDocumentSerial());
            }
            if (subAccountSummarySearchBean.getDateFrom() != null && subAccountSummarySearchBean.getDateTo() != null) {
                builder.append(" AND e.date between :fromDate AND :toDate ");
                params.put("fromDate", subAccountSummarySearchBean.getDateFrom());
                params.put("toDate", subAccountSummarySearchBean.getDateTo());
            } else {
                if (subAccountSummarySearchBean.getDateFrom() != null) {
                    builder.append(" AND e.date between :fromDate AND e.date ");
                    params.put("fromDate", subAccountSummarySearchBean.getDateFrom());
                }
                if (subAccountSummarySearchBean.getDateTo() != null) {
                    builder.append(" AND e.date between e.date AND :toDate ");
                    params.put("toDate", subAccountSummarySearchBean.getDateTo());
                }
            }
            builder.append(" GROUP BY e.id order by  CASE  WHEN (e.transactionType = 0) then 1 WHEN (e.transactionType = 1)then 0 WHEN (e.transactionType = 2) then 2 else e.transactionType end ASC,e.serial ASC , e.documentSerial DESC  ");
            List<GlBankTransaction> glBankTransactionList = dao.findListByQuery(builder.toString(), params);
            List<GlBankTransactionDTO> bankTransactionDTOList = returnListDTO(glBankTransactionList, true, subAccountSummarySearchBean.getBankDTO().getId());

//            Comparator<GlBankTransactionDTO> compare = (GlBankTransactionDTO o1, GlBankTransactionDTO o2)
//                    -> { 
//                int value1 = o1.getListOrder().compareTo(o2.getListOrder());
//                if (value1 == 0) {
//                    int value2 = o1.getSerial().compareTo(o2.getSerial());
//                    if (value2 == 0) {
//                        return o1.getDate().compareTo(o2.getDate());
//                    } else {
//                        return value2;
//                    }
//                }
//                return value1;
//            };
//            Collections.sort(bankTransactionDTOList, compare);
            return bankTransactionDTOList;

        }
        return new ArrayList<>();
    }

    public void getALLGlBankTransactionOpeningBalance(SubAccountSummarySearchBean subAccountSummarySearchBean) {
        balance.clear();
        openingBalance.clear();
        if (subAccountSummarySearchBean != null && subAccountSummarySearchBean.getBranchId() != null) {
            Integer branchId = subAccountSummarySearchBean.getBranchId();
            Integer bankId = subAccountSummarySearchBean.getBankDTO().getId();
            StringBuilder builder = new StringBuilder();
            builder.append("select b.document_serial  , (sum(b.Debit) - sum(b.Credit)) balance  from ");
            builder.append("((SELECT openinng_balance Debit , 0 Credit ,g.document_serial FROM isag.gl_bank , (select min(document_serial) as document_serial from gl_bank_transaction gb  where gb.branch_id = " + branchId + " and  gb.document_serial is not null ) g where branch_id = " + branchId + " and id = " + bankId + ")");
            builder.append(" union ");
            builder.append("(select sum(value_local) Debit , 0 Credit ,document_serial from  gl_bank_transaction where transaction_type = 1 and branch_id = " + branchId + " and gl_bank_id = " + bankId + " and  document_serial is not null group by document_serial)");
            builder.append(" union ");
            builder.append("(select 0 Debit , sum(value_local) Credit,document_serial from  gl_bank_transaction where transaction_type = 0 and branch_id = " + branchId + " and gl_bank_id = " + bankId + " and document_serial is not null group by document_serial) ");
            builder.append(" union ");
            builder.append("(select  sum(ifnull(bt.value_local,0)) Debit , 0 Credit ,document_serial from  gl_bank_transaction bt left join gl_bank_transaction_detail btd on (bt.id = btd.gl_bank_transaction_id) where bt.branch_id = " + branchId + " and bt.transaction_type = 2 and btd.bank_id_to = " + bankId + " and bt.document_serial is not null group by document_serial)");
            builder.append(" union ");
            builder.append("(select 0 Debit , sum(ifnull(bt.value_local,0)) Credit ,document_serial from  gl_bank_transaction bt left join gl_bank_transaction_detail btd on (bt.id = btd.gl_bank_transaction_id) where bt.branch_id = " + branchId + " and bt.transaction_type = 2 and btd.bank_id_from = " + bankId + " and bt.document_serial is not null group by document_serial)) b ");
            builder.append("group by b.document_serial order by b.document_serial");
            List<Object[]> objectList = dao.executeNativeQuery(builder.toString());
            BigDecimal oldBalance = BigDecimal.ZERO;
            for (int i = 1; i < objectList.size(); i++) {
                if (i == 1) {
                    balance.put((Integer) objectList.get(i)[0], (BigDecimal) objectList.get(i - 1)[1]);
                } else {
                    oldBalance = balance.get(objectList.get(i - 1)[0]);
                    balance.put((Integer) objectList.get(i)[0], oldBalance.add((BigDecimal) objectList.get(i - 1)[1]));
                }
            }
            openingBalance = balance;
        }

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
    public List<GlBankTransaction> getALLGlBankTransactionByCompanyId(Integer companyId) {
        return dao.findEntityListByCompanyId(GlBankTransaction.class, companyId);
    }

    @Override
    public GlBankTransaction findGlBankTransactionById(Integer glBankTransactionId) {
        return dao.findEntityById(GlBankTransaction.class , glBankTransactionId);
    }

    @Override
    public Integer findGlBankTransactionBySerial(Integer serial, Integer branchId, Integer transactionType) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("serial", serial);
        params.put("transactionType", transactionType);
        List<Integer>GlBankTransactionList= dao.findListByQuery("SELECT e.id FROM GlBankTransaction e WHERE  e.branchId.id = :branchId  AND e.serial = :serial AND e.transactionType = :transactionType ", params);
        if(GlBankTransactionList!=null&&!GlBankTransactionList.isEmpty()&&GlBankTransactionList.get(0)!=null){
            Integer  GlBankTransactionId =GlBankTransactionList.get(0);
            return GlBankTransactionId;
        
        }
                return null;

    }

    @Override
    public GlBankTransaction addOneGlBankTransaction(GlBankTransaction glBankTransaction, GlBankTransactionDetail bankTransactionDetail) {
        if (glBankTransaction.getId() != null) {
            glBankTransaction = dao.updateEntity(glBankTransaction);
        } else {
            if (glBankTransaction.getTransactionType() == 2) {
                glBankTransaction.setGlBankId(bankTransactionDetail.getBankIdFrom());
                glBankTransaction.setSerial(getMaxSerail(glBankTransaction));
                glBankTransaction.setGlBankId(null);
            } else {
                glBankTransaction.setSerial(getMaxSerail(glBankTransaction));
            }
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
        return glBankTransaction;
    }

    @Override
    public Long getTotalRegistors(Integer transactionType, Integer branchId, Integer userId, Map<String, Object> filters, Integer selectGlBankId) {
        StringBuilder builder = new StringBuilder();
        filters.put("transactionType", transactionType);
        builder.append("SELECT count(s) FROM GlBankTransaction s WHERE s.branchId.id = :branchId AND s.transactionType =:transactionType ");
        appendFilter(filters, branchId, userId, builder, selectGlBankId, transactionType);
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

    public void appendFilter(Map<String, Object> filters, Integer branchId, Integer userId, StringBuilder builder, Integer selectGlBankId, Integer transactionType) {
        filters.put("branchId", branchId);
        if (selectGlBankId != null) {
            filters.put("selectGlBankId", selectGlBankId);
            builder.append(" AND s.glBankId.id = :selectGlBankId ");
        }
        List<Integer> glBankIdList = isagUserBankService.getAllGlBankIdForUserByTypeAndBranchId(userId, branchId, transactionType);
        filters.put("glBankIdList", glBankIdList);
        builder.append(" AND s.glBankId.id in :glBankIdList ");

        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and (s.serial like CONCAT('%',:globalFilter ,'%')  OR s.glBankId.name  like CONCAT('%',:globalFilter ,'%') OR s.valueLocal like CONCAT('%',:globalFilter ,'%') OR s.createdBy.name like CONCAT('%',:globalFilter ,'%')  OR s.remark like CONCAT('%',:globalFilter ,'%') OR s.remark2 like CONCAT('%',:globalFilter ,'%') OR s.value like CONCAT('%',:globalFilter ,'%') ) ";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
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
    public List<GlBankTransactionDTO> getALLGlBankTransactionRecievableByBranchId(Integer transactionType, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters, Integer selectGlBankId, Integer userId) {
        filters.put("transactionType", transactionType);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT s FROM GlBankTransaction s WHERE s.branchId.id = :branchId AND s.transactionType =:transactionType  ");
        appendFilter(filters, branchId, userId, builder, selectGlBankId, transactionType);
        builder.append(" order by s.serial DESC");
        List<GlBankTransaction> details = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        return returnListDTO(details, false);
    }

    @Override
    public List<GlBankTransactionDTO> getALLGlBankTransactionRecievableNotloadByBranchId(Integer transactionType, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters) {
        filters.put("transactionType", transactionType);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT s FROM GlBankTransaction s WHERE s.branchId.id = :branchId AND s.transactionType =:transactionType AND s.id not in ( select g.parent.id FROM GlBankTransaction g WHERE g.branchId.id = :branchId and g.parent is not null )  ");
        appendFilter(filters, branchId, builder);
        builder.append(" order by s.serial DESC");
        List<GlBankTransaction> details = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        return returnListDTO(details, false);
    }

    public GlBankTransactionDTO mapTODTO(GlBankTransaction entity, Boolean needDetail, Integer brankId) {

        GlBankTransactionDTO dTO = initMapToDTO(entity, brankId);

        if (needDetail) {

        }

        return dTO;
    }

    private GlBankTransactionDTO initMapToDTO(GlBankTransaction entity, Integer brankId) {

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
        dTO.setDocumentSerial(entity.getDocumentSerial());
        dTO.setIsDone(entity.getIsDone());
        dTO.setLook(entity.getLook());

        if (entity.getPaymentType() != null) {
            if (entity.getPaymentType() == 0) {
                dTO.setPaymentTypeName("نقدي");
            } else if (entity.getPaymentType() == 1) {
                dTO.setPaymentTypeName("الشيك");
            } else {
                dTO.setPaymentTypeName("الشيك الخطي");

            }
        }

        if (entity.getTransactionType() != null && !entity.getDublara()) {
            dTO.setTransactionType(entity.getTransactionType());
            if (openingBalance.containsKey(entity.getDocumentSerial())) {
                dTO.setOpeningBalance(openingBalance.get(entity.getDocumentSerial()));
            } else {
                if (entity.getGlBankId() == null) {
                    BigDecimal bankOpeningBalance = bankService.findGlBankOpeninngBalance(entity.getBranchId().getId(), brankId);
                    dTO.setOpeningBalance(bankOpeningBalance);
                    setValue(bankOpeningBalance);
                } else {
                    dTO.setOpeningBalance(entity.getGlBankId().getOpeninngBalance());
                    setValue(entity.getGlBankId().getOpeninngBalance());
                }
            }

            if (balance.containsKey(entity.getDocumentSerial())) {
                setValue(balance.get(entity.getDocumentSerial()));
            }

            if (entity.getTransactionType() == 0) {
                dTO.setTransactionTypeName("سند صرف	");
                dTO.setCredit(entity.getValueLocal());
                dTO.setDebit(BigDecimal.ZERO);
                setValue(getValue().subtract(entity.getValueLocal()));
                dTO.setListOrder(1);
            } else if (entity.getTransactionType() == 1) {
                dTO.setTransactionTypeName("سند قبض	");
                dTO.setDebit(entity.getValueLocal());
                dTO.setCredit(BigDecimal.ZERO);
                setValue(getValue().add(entity.getValueLocal()));
                dTO.setListOrder(0);
            } else if (entity.getTransactionType() == 2) {
                //fromGlBank
                dTO.setTransactionTypeName("تحويل صادر");
                dTO.setCredit(entity.getValueLocal());
                dTO.setDebit(BigDecimal.ZERO);
                dTO.setListOrder(2);
                GlBankDTO bankDTOFrom = new GlBankDTO();
                for (Iterator iterator = entity.getGlBankTransactionDetailCollection().iterator(); iterator.hasNext();) {
                    GlBankTransactionDetail bankTransactionDetail = (GlBankTransactionDetail) iterator.next();
                    //fromGlBank 
                    bankDTOFrom.setId(bankTransactionDetail.getBankIdFrom().getId());
                    bankDTOFrom.setName(bankTransactionDetail.getBankIdFrom().getName());
                    dTO.setGlBankId(bankDTOFrom);
                    //toGlBank
                    bankDTOTo.setId(bankTransactionDetail.getBankIdTo().getId());
                    bankDTOTo.setName(bankTransactionDetail.getBankIdTo().getName());
                }
                if (bankDTOFrom.getId().equals(brankId)) {
                    setValue(getValue().subtract(entity.getValueLocal()));
                }
            }
            if (balance.containsKey(entity.getDocumentSerial())) {
                balance.replace(entity.getDocumentSerial(), getValue());
            } else {
                balance.put(entity.getDocumentSerial(), getValue());
            }
            dTO.setBalance(getValue());
            setValue(BigDecimal.ZERO);
        }
        dTO.setPostFlag(entity.getPostFlag());
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());

        return dTO;
    }

    private GlBankTransactionDTO transfareTo(GlBankTransaction entity, Integer brankId) {

        if (entity.getTransactionType() != null && entity.getTransactionType() == 2) {
            entity.setDublara(Boolean.TRUE);
            GlBankTransactionDTO dTO = initMapToDTO(entity, brankId);
            if (openingBalance.containsKey(entity.getDocumentSerial())) {
                dTO.setOpeningBalance(openingBalance.get(entity.getDocumentSerial()));
            } else {
                dTO.setOpeningBalance(BigDecimal.ZERO);
            }
            if (balance.containsKey(dTO.getDocumentSerial())) {
                setValue(balance.get(dTO.getDocumentSerial()));
            }
            //toGlbank
            GlBankTransactionDTO dTO2 = dTO;
            dTO2.setTransactionTypeName("تحويل وارد");
            dTO2.setDebit(dTO.getValueLocal());
            dTO2.setCredit(BigDecimal.ZERO);
            dTO.setListOrder(2);
            if (bankDTOTo.getId().equals(brankId)) {
                setValue(getValue().add(dTO.getValueLocal()));
            }
            if (balance.containsKey(dTO.getDocumentSerial())) {
                balance.replace(dTO.getDocumentSerial(), getValue());
            } else {
                balance.put(dTO.getDocumentSerial(), getValue());
            }
            dTO2.setBalance(getValue());
            setValue(BigDecimal.ZERO);
            dTO2.setIndex(dTO.getIndex() + 1);
            //toGlbank 
            dTO2.setGlBankId(bankDTOTo);
            bankDTOTo = new GlBankDTO();
            dTO = null;
            return dTO2;
        }
        return null;
    }

    public GlBankTransaction mapFromDTO(GlBankTransactionDTO dTO, IsagUser isagUser) {
        GlBankTransaction entity = new GlBankTransaction();

        if (dTO.getId() == null) {
            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());

        } else {
            IsagUser isagUser1 = new IsagUser();
            isagUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(isagUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);
        }

        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }

    public List<GlBankTransactionDTO> returnListDTO(List<GlBankTransaction> list, Boolean duplara) {
        List<GlBankTransactionDTO> dTOList = new ArrayList<>();
        for (GlBankTransaction entity : list) {
            dTOList.add(mapTODTO(entity, false, null));

            if (duplara) {
                GlBankTransactionDTO DTO2 = transfareTo(entity, null);
                if (DTO2 != null) {
                    dTOList.add(DTO2);
                }
            }
        }
        return dTOList;
    }

    public List<GlBankTransactionDTO> returnListDTO(List<GlBankTransaction> list, Boolean duplara, Integer brankId) {
        List<GlBankTransactionDTO> dTOList = new ArrayList<>();
        for (GlBankTransaction entity : list) {
            dTOList.add(mapTODTO(entity, false, brankId));

            if (duplara) {
                GlBankTransactionDTO DTO2 = transfareTo(entity, brankId);
                if (DTO2 != null) {
                    dTOList.add(DTO2);
                }
            }
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
    public void updatelookAndIsDone(SupplierAccountViewBean supplierAccountViewBean) {
        if (supplierAccountViewBean != null && supplierAccountViewBean.getIdNumber() != null) {
            GlBankTransaction bankTransaction = dao.findEntityById(GlBankTransaction.class, supplierAccountViewBean.getIdNumber());
            bankTransaction.setLook(supplierAccountViewBean.getLook() ? 1 : 0);
            bankTransaction.setIsDone(supplierAccountViewBean.getIsDone() ? 1 : 0);
            dao.updateEntity(bankTransaction);
        }
    }

    @Override
    public void updatelookAndIsDone(List<SupplierAccountViewBean> supplierAccountViewBeanList) {
        for (SupplierAccountViewBean supplierAccountViewBean : supplierAccountViewBeanList) {
            updatelookAndIsDone(supplierAccountViewBean);
        }
    }

    public GlBankTransaction mapToEntityFromInvSales(InvInvoiceDTO dTO, IsagUser isagUser) {
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
        entity.setOrganizationType(0);
        entity.setPaymentType(0);
        entity.setPostFlag(0);
        entity.setRate(dTO.getRate());
        entity.setRemark(dTO.getRemarks());
        entity.setRemark2(dTO.getCreatedByName());
        entity.setSerial(dTO.getSerial());
        entity.setTransactionType(1);
        entity.setValue(dTO.getPayed());
        entity.setValueLocal(dTO.getPayed());
        entity.setCreatedBy(isagUser);
        entity.setCreationDate(new Date());

        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());

        entity.setSerial(getMaxSerail(entity));

        return entity;
    }

    @Override
    public void addCash(InvInvoiceDTO dTO, IsagUser tobyUser) {

        GlBankTransaction glBankTransaction = mapToEntityFromInvSales(dTO, tobyUser);

        glBankTransaction = dao.updateEntity(glBankTransaction);

        glBankTransactionDetailsService.addCashDetail(dTO, glBankTransaction.getId(), tobyUser);
    }

    /**
     * @return the value
     */
    public BigDecimal getValue() {
        if (value == null) {
            value = BigDecimal.ZERO;
        }
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public Map<Integer, Vector<Object>> getAllinstallmentIdAndPayedValue(Integer branchId, List<Integer> installmentIdList) {
        Map<String, Object> param = new HashMap<>();
        param.put("branchId", branchId);
        param.put("installmentIdList", installmentIdList);
        String query = "SELECT g.installmentId.id , g.value , g.date , g.glBankId.name FROM GlBankTransaction g WHERE g.branchId.id = :branchId AND g.installmentId.id in :installmentIdList ";
        List<Object[]> result = dao.findListByQuery(query, param);
        Map<Integer, Vector<Object>> map = new HashMap<>();
        for (Object[] objects : result) {
            Vector<Object> vector;
            Integer installmentId = (Integer) objects[0];
            BigDecimal value = (BigDecimal) objects[1];
            Date date = (Date) objects[2];
            String bankName = (String) objects[3];
            if (map.containsKey(installmentId)) {
                vector = map.get(installmentId);
                vector.add(0, ((BigDecimal) vector.firstElement()).add(value != null ? value : BigDecimal.ZERO));
            } else {
                vector = new Vector<>();
                vector.add(0, value);
            }
            vector.add(1, date);
            vector.add(2, bankName);
            map.put(installmentId, vector);
        }
        return map;
    }

}
