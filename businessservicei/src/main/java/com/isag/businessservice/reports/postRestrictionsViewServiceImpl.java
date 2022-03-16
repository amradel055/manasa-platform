/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.RscExtractRegisterService;
import com.isag.core.GenericDAO;
import com.isag.entity.GeneralJournal;
import com.isag.entity.GlAccount;
import com.isag.entity.GlBankTransaction;
import com.isag.entity.InstContract;
import com.isag.entity.InvNotice;
import com.isag.entity.InvPurchaseInvoice;
import com.isag.entity.RscExtractRegister;
import com.isag.views.PostRestrictionsView;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author ahmed
 */
@Stateless
public class postRestrictionsViewServiceImpl implements postRestrictionsViewService {

    @EJB
    private GenericDAO dao;
    @EJB
    private RscExtractRegisterService rscExtractRegisterService;

    @Override
    public List<PostRestrictionsView> getPostRestrictionByScreenCode(Integer branchId, Date dateFrom, Date dateTo, Integer DocumentId, Integer postFlag) {
        dao.executeNativeQuery("select sql_mode_function()");
        if (branchId != null && branchId != 179) {
            Map<String, Object> params = new HashMap<>();
            String query = "SELECT glb FROM PostRestrictionsView glb WHERE glb.branchId = :branchId AND glb.screenCode = :DocumentId AND glb.postFlag = :postFlag";
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append(query);
            appendDateForQueryBuilder(queryBuilder, dateFrom, dateTo);
            params.put("branchId", branchId);
            params.put("DocumentId", DocumentId);
            params.put("postFlag", postFlag);
            List<PostRestrictionsView> PostRestrictionsViewList = dao.findListByQuery(queryBuilder.toString(), params);
            return PostRestrictionsViewList;
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("branchId", branchId);
            params.put("postFlag", postFlag);

            Long newScreenCode = 0l;
            String accountId = "";
            switch (DocumentId) {
                case 1001:
                    params.put("transactionType", 0);
                    params.put("paymentType", 0);
                    accountId = "glbd.accountCreditId";
                    newScreenCode = -2000L;
                    break;
                case 1002:
                    params.put("transactionType", 0);
                    params.put("paymentType", 1);
                    accountId = "glbd.accountCreditId";
                    newScreenCode = -2000L;
                    break;
                case 1003:
                    params.put("transactionType", 0);
                    params.put("paymentType", 2);
                    accountId = "glbd.accountCreditId";
                    newScreenCode = -2000L;
                    break;
                case 1004:
                    params.put("transactionType", 1);
                    params.put("paymentType", 0);
                    accountId = "glbd.accountDebitId";
                    newScreenCode = -1000L;
                    break;
                case 1005:
                    params.put("transactionType", 1);
                    params.put("paymentType", 1);
                    accountId = "glbd.accountDebitId";
                    newScreenCode = -1000L;
                    break;
                case 1006:
                    params.put("transactionType", 1);
                    params.put("paymentType", 2);
                    accountId = "glbd.accountDebitId";
                    newScreenCode = -1000L;
                    break;
                case 1007:
                    params.put("transactionType", 2);
                    params.put("paymentType", 0);
                    break;
                case 1008:
                    params.put("transactionType", 2);
                    params.put("paymentType", 2);
                    break;
            }

//TODO add another 3
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("select glb.id  , glb.date ,").append(accountId).append(" ,sum(COALESCE(glbd.value, 0)),glb.generalJournalId ,glb.glYear ,glb.serial from GlBankTransactionDetail glbd"
                    + " LEFT JOIN glbd.glBankTransactionId glb where glb.branchId.id = :branchId and glb.transactionType = :transactionType and glb.postFlag = :postFlag "
                    + " and glb.paymentType = :paymentType ");
            appendDateForQueryBuilder(stringBuilder, dateFrom, dateTo);
            stringBuilder.append(" group by glb.date , ").append(accountId);
            List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);
            List<PostRestrictionsView> PostRestrictionsViewList = getPostRestrictionViewList(res, newScreenCode);
            return PostRestrictionsViewList;
        }
    }

    private void appendDateForQueryBuilder(StringBuilder queryBuilder, Date dateFrom, Date dateTo) {
        if (dateFrom != null && dateTo != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(dateFrom);
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(dateTo);
            queryBuilder.append(" and glb.date between '").append(formatDateFrom).append("' AND '").append(formatDateTo).append("'");

        } else {
            if (dateFrom != null) {
                String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(dateFrom);
                queryBuilder.append(" and glb.date between '").append(formatDateFrom).append("' AND ").append(" glb.date ");
            }

            if (dateTo != null) {
                String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(dateTo);
                queryBuilder.append(" and glb.date between glb.date AND '").append(formatDateTo).append("'");
            }
        }
    }

    public List<PostRestrictionsView> getPostRestrictionViewList(List<Object[]> res, Long newScreenCode) {

        List<PostRestrictionsView> postRestrictionsViewList = new ArrayList<>();

        Iterator it = res.iterator();
        while (it.hasNext()) {
            Object[] line = (Object[]) it.next();
            PostRestrictionsView postRestrictionsView = new PostRestrictionsView();
            postRestrictionsView.setId((Integer) line[0]);
            postRestrictionsView.setDate((Date) line[1]);
            GlAccount account = new GlAccount();
            account = (GlAccount) line[2];
            postRestrictionsView.setAccountId(account != null ? account.getId() : null);
            postRestrictionsView.setValue((BigDecimal) line[3]);
            GeneralJournal generalJournal = new GeneralJournal();
            generalJournal = (GeneralJournal) line[4];
            postRestrictionsView.setGeneralJournalId(generalJournal != null ? generalJournal.getId() : null);
            postRestrictionsView.setGlYear((Integer) line[5]);
            postRestrictionsView.setScreenCode(newScreenCode);
            postRestrictionsView.setSerial((Integer) line[6]);
            postRestrictionsViewList.add(postRestrictionsView);
        }
        return postRestrictionsViewList;
    }

    @Override
    public List<PostRestrictionsView> getTransactionsIdForUpdatePostFlag(Integer BankIdAccount, Date documentDate, Long screenCode) {
        dao.executeNativeQuery("select sql_mode_function()");
        Map<String, Object> params = new HashMap<>();
        String accountClass = "";

        switch (screenCode.intValue()) {
            case -1000:
                params.put("transactionType", 1);
                accountClass = "glbd.accountDebitId.id";
                break;
            case -2000:
                params.put("transactionType", 0);
                accountClass = "glbd.accountCreditId.id";
                break;
        }
        params.put("BankIdAccount", BankIdAccount);
        params.put("documentDate", documentDate);
        // params.put("transactionType", 1);
        // params.put("paymentType", 0);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select glbd.glBankTransactionId from GlBankTransactionDetail glbd"
                + " LEFT JOIN glbd.glBankTransactionId glb where ").append(accountClass).append(" = :BankIdAccount and glb.date = :documentDate and glb.transactionType = :transactionType  ");
        stringBuilder.append(" group by glbd.glBankTransactionId.id");
        List<GlBankTransaction> res = dao.findListByQuery(stringBuilder.toString(), params);
        List<PostRestrictionsView> PostRestrictionsViewList = getglBankTransactionIdList(res);
        return PostRestrictionsViewList;
    }

    public List<PostRestrictionsView> getglBankTransactionIdList(List<GlBankTransaction> res) {

        List<PostRestrictionsView> postRestrictionsViewList = new ArrayList<>();

        for (GlBankTransaction bankTransaction : res) {
            PostRestrictionsView postRestrictionsView = new PostRestrictionsView();
            postRestrictionsView.setId(bankTransaction.getId());
            postRestrictionsViewList.add(postRestrictionsView);
        }
        return postRestrictionsViewList;
    }

    @Override
    public List<PostRestrictionsView> getBankTransactions(Integer branchId, Date dateFrom, Date dateTo, Integer DocumentId, Integer postFlag) {
        //set sql mode
        dao.executeNativeQuery("select isag.sql_mode_function()");
        List<PostRestrictionsView> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("postFlag", postFlag);
        StringBuilder queryBuilder = new StringBuilder();
        if (DocumentId >= 1001 && DocumentId < 1009) {
            String query = "SELECT glb FROM GlBankTransaction glb WHERE glb.branchId.id = :branchId  AND glb.postFlag = :postFlag AND glb.transactionType =:transactionType AND glb.paymentType =:paymentType";
            queryBuilder.append(query);
            appendDateForQueryBuilder(queryBuilder, dateFrom, dateTo);
            transaction(DocumentId, params, 1);
            List<GlBankTransaction> bankTransactions = dao.findListByQuery(queryBuilder.toString(), params);
            list = returnListView(bankTransactions, DocumentId);
        } else if (DocumentId >= 1009 && DocumentId < 1021) {
            String query;
            if (params.get("postFlag").equals(0)) {
                query = "SELECT i FROM InvPurchaseInvoice i WHERE i.branchId.id = :branchId  AND (i.postFlag = :postFlag OR i.postFlag is null) AND i.type =:type AND i.paymentType =:paymentType";
            } else {
                query = "SELECT i FROM InvPurchaseInvoice i WHERE i.branchId.id = :branchId  AND i.postFlag = :postFlag AND i.type =:type AND i.paymentType =:paymentType";
            }
            queryBuilder.append(query);
            transaction(DocumentId, params, 2);
            if (dateFrom != null && dateTo != null) {
                String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(dateFrom);
                String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(dateTo);
                queryBuilder.append(" and i.date between '").append(formatDateFrom).append("' AND '").append(formatDateTo).append("'");

            } else {
                if (dateFrom != null) {
                    String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(dateFrom);
                    queryBuilder.append(" and i.date between '").append(formatDateFrom).append("' AND ").append(" i.date ");
                }

                if (dateTo != null) {
                    String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(dateTo);
                    queryBuilder.append(" and i.date between i.date AND '").append(formatDateTo).append("'");
                }
            }

            List<InvPurchaseInvoice> invPurchaseInvoice = dao.findListByQuery(queryBuilder.toString(), params);
            list = returnListView(invPurchaseInvoice, DocumentId);
        } else if (DocumentId >= 1021 && DocumentId < 1022) {
            String query;
            if (params.get("postFlag").equals(0)) {
                query = "SELECT i FROM InstContract i WHERE i.branchId.id = :branchId  AND (i.postFlag = :postFlag OR i.postFlag is null)";
            } else {
                query = "SELECT i FROM InstContract i WHERE i.branchId.id = :branchId  AND i.postFlag = :postFlag";
            }
            queryBuilder.append(query);
            if (dateFrom != null && dateTo != null) {
                String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(dateFrom);
                String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(dateTo);
                queryBuilder.append(" and i.date between '").append(formatDateFrom).append("' AND '").append(formatDateTo).append("'");

            } else {
                if (dateFrom != null) {
                    String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(dateFrom);
                    queryBuilder.append(" and i.date between '").append(formatDateFrom).append("' AND ").append(" i.date ");
                }

                if (dateTo != null) {
                    String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(dateTo);
                    queryBuilder.append(" and i.date between i.date AND '").append(formatDateTo).append("'");
                }
            }
            List<InstContract> instContract = dao.findListByQuery(queryBuilder.toString(), params);
            list = returnListView(instContract, DocumentId);
        } else if (DocumentId >= 1022 && DocumentId < 1030) {
            String query;
            if (params.get("postFlag").equals(0)) {
                query = "SELECT i FROM RscExtractRegister i WHERE i.branchId.id = :branchId  AND (i.postFlag = :postFlag OR i.postFlag is null)";

            } else {
                query = "SELECT i FROM RscExtractRegister i WHERE i.branchId.id = :branchId  AND i.postFlag = :postFlag";

            }
            queryBuilder.append(query);
            if (dateFrom != null && dateTo != null) {
                String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(dateFrom);
                String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(dateTo);
                queryBuilder.append(" and i.date between '").append(formatDateFrom).append("' AND '").append(formatDateTo).append("'");

            } else {
                if (dateFrom != null) {
                    String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(dateFrom);
                    queryBuilder.append(" and i.date between '").append(formatDateFrom).append("' AND ").append(" i.date ");
                }

                if (dateTo != null) {
                    String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(dateTo);
                    queryBuilder.append(" and i.date between i.date AND '").append(formatDateTo).append("'");
                }
            }
            List<RscExtractRegister> rscExtractRegisters = dao.findListByQuery(queryBuilder.toString(), params);
            list = returnListView(rscExtractRegisters, DocumentId);

        } else if (DocumentId >= 1030 && DocumentId < 1039) {
            String query;
            if (params.get("postFlag").equals(0)) {
                query = "SELECT i FROM InvNotice i WHERE i.branchId.id = :branchId AND i.type =:type AND (i.postFlag = :postFlag OR i.postFlag is null)";

            } else {
                query = "SELECT i FROM InvNotice i WHERE i.branchId.id = :branchId AND i.type =:type AND i.postFlag = :postFlag";

            }
            queryBuilder.append(query);
            transaction(DocumentId, params, 5);
            if (dateFrom != null && dateTo != null) {
                String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(dateFrom);
                String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(dateTo);
                queryBuilder.append(" and i.date between '").append(formatDateFrom).append("' AND '").append(formatDateTo).append("'");

            } else {
                if (dateFrom != null) {
                    String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(dateFrom);
                    queryBuilder.append(" and i.date between '").append(formatDateFrom).append("' AND ").append(" i.date ");
                }

                if (dateTo != null) {
                    String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(dateTo);
                    queryBuilder.append(" and i.date between i.date AND '").append(formatDateTo).append("'");
                }
            }
            List<InvNotice> invNoticeList = dao.findListByQuery(queryBuilder.toString(), params);
            list = returnListView(invNoticeList, DocumentId);
        }
        return list;

    }
    int index = 0;

    private <T> PostRestrictionsView initMapToView(T Entity, Integer DocumentId) {
        PostRestrictionsView view = new PostRestrictionsView();
        index++;
        if (DocumentId >= 1001 && DocumentId < 1009) {
            //الخزائن
            GlBankTransaction entity = (GlBankTransaction) Entity;
            view.setRowNum(index);
            view.setSerial(entity.getSerial());
            view.setId(entity.getId());
            view.setDate(entity.getDate());
            view.setGlYear(entity.getGlYear());
            view.setPostFlag(entity.getPostFlag());
            view.setValue(entity.getValue());
            view.setValueLocal(entity.getValueLocal());
            view.setInstallmentValue(entity.getInstallmentId() != null ? entity.getInstallmentId().getValue() : null);
            view.setGeneralJournalId(entity.getGeneralJournalId() != null ? entity.getGeneralJournalId().getId() : null);
            view.setBranchId(entity.getBranchId().getId());
            view.setRate(entity.getRate() != null ? entity.getRate() : BigDecimal.ONE);
            if (entity.getTransactionType() == 0 && entity.getPaymentType() == 0) {
                view.setScreenCode(1001);
                view.setScreenName("صرف نقدي جزينة");
            } else if (entity.getTransactionType() == 0 && entity.getPaymentType() == 1) {
                view.setScreenCode(1002);
                view.setScreenName("صرف شيك خزينة");
            } else if (entity.getTransactionType() == 0 && entity.getPaymentType() == 2) {
                view.setScreenCode(1003);
                view.setScreenName("صرف شيك خطي خزينة");
            } else if (entity.getTransactionType() == 1 && entity.getPaymentType() == 0) {
                view.setScreenCode(1004);
                view.setScreenName("قبض نقدي خزينة");
            } else if (entity.getTransactionType() == 1 && entity.getPaymentType() == 1) {
                view.setScreenCode(1005);
                view.setScreenName("قبض شيك خزينة");
            } else if (entity.getTransactionType() == 1 && entity.getPaymentType() == 2) {
                view.setScreenCode(1006);
                view.setScreenName("قبض شيك خطي خزينة");
            } else if (entity.getTransactionType() == 2 && entity.getPaymentType() == 0) {
                view.setScreenCode(1007);
                view.setScreenName("تحويل نقدي خزينة");
            } else if (entity.getTransactionType() == 2 && entity.getPaymentType() == 2) {
                view.setScreenCode(1008);
                view.setScreenName("تحويل شيك خطي خزينة");
            }
            view.setRemark(entity.getRemark() != null && !entity.getRemark().isEmpty() ? view.getScreenName().concat(" لسند ").concat(entity.getSerial().toString()).concat(" + ").concat(entity.getRemark()) : view.getScreenName().concat(" لسند ").concat(entity.getSerial().toString()).concat(entity.getRemark()));

        } else if (DocumentId >= 1009 && DocumentId < 1021) {
            //المخازن

            InvPurchaseInvoice entity = (InvPurchaseInvoice) Entity;
            view.setRowNum(index);
            view.setSerial(entity.getSerial());
            view.setId(entity.getId());
            view.setDate(entity.getDate());
            view.setGlYear(null);
            view.setPostFlag(entity.getPostFlag());
            view.setValue(null);
            view.setValueLocal(null);
            view.setInstallmentValue(null);
            view.setGeneralJournalId(entity.getGeneralJournalId() != null ? entity.getGeneralJournalId().getId() : null);
            view.setBranchId(entity.getBranchId().getId());
            if (entity.getType() == 0 && entity.getPaymentType() == 0) {
                view.setScreenCode(1009);
                view.setScreenName("مشتريات نقدي مخازن");
            } else if (entity.getType() == 0 && entity.getPaymentType() == 1) {
                view.setScreenCode(1010);
                view.setScreenName("مشتريات اجل مخازن");
            } else if (entity.getType() == 0 && entity.getPaymentType() == 2) {
                view.setScreenCode(1011);
                view.setScreenName("مشتريات اعتماد مستندي مخازن");
            } else if (entity.getType() == 1 && entity.getPaymentType() == 0) {
                view.setScreenCode(1012);
                view.setScreenName("مبيعات نقدي مخازن");
            } else if (entity.getType() == 1 && entity.getPaymentType() == 1) {
                view.setScreenCode(1013);
                view.setScreenName("مبيعات اجل مخازن");
            } else if (entity.getType() == 1 && entity.getPaymentType() == 2) {
                view.setScreenCode(1014);
                view.setScreenName("مردودات مشتريات نقدي مخازن");
            } else if (entity.getType() == 2 && entity.getPaymentType() == 0) {
                view.setScreenCode(1015);
                view.setScreenName("مردودات مشتريات اجل مخازن");
            } else if (entity.getType() == 2 && entity.getPaymentType() == 1) {
                view.setScreenCode(1016);
                view.setScreenName("مرودات مشتريات اعتماد مستندي مخازن");
            } else if (entity.getType() == 3 && entity.getPaymentType() == 0) {
                view.setScreenCode(1018);
                view.setScreenName("مردودات مبيعات نقدي مخازن");
            } else if (entity.getType() == 3 && entity.getPaymentType() == 1) {
                view.setScreenCode(1019);
                view.setScreenName("مردودات مبيعات اجل مخازن");
            }
            String serial = entity.getSerial() != null ? entity.getSerial().toString() : " ";
            view.setRemark(entity.getRemarks() != null && !entity.getRemarks().isEmpty() ? view.getScreenName().concat(" لرقم ").concat(serial).concat(" + ").concat(entity.getRemarks()) : view.getScreenName().concat(" لرقم ").concat(serial).concat(entity.getRemarks()));

        } else if (DocumentId >= 1021 && DocumentId < 1022) {
            //التقسيط
            InstContract entity = (InstContract) Entity;
            view.setRowNum(index);
            view.setId(entity.getId());
            view.setSerial(entity.getSerial());
            view.setDate(entity.getDate());
            view.setBranchId(entity.getBranchId().getId());
            view.setCompanyId(entity.getCompanyId().getId());
            view.setRemark("التقسيط لرقم " + entity.getSerial() + " " + entity.getRemark());
            view.setGlYear(null);
            view.setPostFlag(entity.getPostFlag());
            view.setValue(entity.getTotalValue());
            view.setValueLocal(entity.getTotalValue());
            view.setInstallmentValue(entity.getInstallmentValue());
            view.setGeneralJournalId(entity.getGeneralJournalId() != null ? entity.getGeneralJournalId().getId() : null);
            view.setScreenCode(1021);
            view.setDocumentTypeId(null);
            view.setBranchId(entity.getBranchId().getId());

        } else if (DocumentId >= 1022 && DocumentId < 1030) {
            //المشاريع 
            RscExtractRegister entity = (RscExtractRegister) Entity;
            BigDecimal value = rscExtractRegisterService.getTotalNetCost(entity.getBranchId().getId(), entity.getId());
            view.setRowNum(index);
            view.setId(entity.getId());
            view.setSerial(entity.getSerial());
            view.setDate(entity.getDate());
            view.setBranchId(entity.getBranchId().getId());
            view.setCompanyId(entity.getCompanyId().getId());
            view.setRemark("المستخلص لسند " + entity.getSerial());
            view.setGlYear(null);
            view.setPostFlag(entity.getPostFlag());
            view.setValue(value);
            view.setValueLocal(value);
            view.setInstallmentValue(null);
            view.setGeneralJournalId(entity.getGeneralJournalId() != null ? entity.getGeneralJournalId().getId() : null);
            view.setScreenCode(1022);
            view.setDocumentTypeId(null);
            view.setBranchId(entity.getBranchId().getId());

        } else if (DocumentId >= 1030 && DocumentId < 1040) {
            //الاشعارات 
            InvNotice entity = (InvNotice) Entity;
            view.setRowNum(index);
            view.setId(entity.getId());
            view.setSerial(entity.getSerial());
            view.setDate(entity.getDate());
            view.setBranchId(entity.getBranchId().getId());
            view.setCompanyId(entity.getCompanyId().getId());
            view.setRemark("الاشعار  لسند" + entity.getSerial());
            view.setGlYear(null);
            view.setPostFlag(entity.getPostFlag());
            view.setValue(entity.getValue());
            view.setValueLocal(entity.getValue());
            view.setInstallmentValue(null);
            view.setGeneralJournalId(entity.getGeneralJournalId() != null ? entity.getGeneralJournalId().getId() : null);
            if (entity.getType() == 0) {
                view.setScreenCode(1030);
                view.setScreenName("اشعار اضافة عميل");
            } else if (entity.getType() == 1) {
                view.setScreenCode(1031);
                view.setScreenName("اشعار خصم عميل");
            } else if (entity.getType() == 2) {
                view.setScreenCode(1032);
                view.setScreenName("اشعار اضافة مورد");
            } else if (entity.getType() == 3) {
                view.setScreenCode(1033);
                view.setScreenName("اشعار خصم مورد");
            }

            view.setDocumentTypeId(null);
            view.setBranchId(entity.getBranchId().getId());

        }
        return view;
    }

    public <T> List<PostRestrictionsView> returnListView(List<T> list, Integer DocumentId) {
        List<PostRestrictionsView> viewList = new ArrayList<>();
        for (T entity : list) {
            viewList.add(mapTOView(entity, DocumentId));
        }
        return viewList;
    }

    public <T> PostRestrictionsView mapTOView(T entity, Integer DocumentId) {
        PostRestrictionsView view = initMapToView(entity, DocumentId);
        return view;
    }

    public void transaction(Integer screenCode, Map<String, Object> params, int moduleCode) {
        //الخزائن
        if (moduleCode == 1) {
            switch (screenCode) {
                case 1001:
                    params.put("transactionType", 0);
                    params.put("paymentType", 0);
                    break;
                case 1002:
                    params.put("transactionType", 0);
                    params.put("paymentType", 1);
                    break;
                case 1003:
                    params.put("transactionType", 0);
                    params.put("paymentType", 2);
                    break;
                case 1004:
                    params.put("transactionType", 1);
                    params.put("paymentType", 0);
                    break;
                case 1005:
                    params.put("transactionType", 1);
                    params.put("paymentType", 1);
                    break;
                case 1006:
                    params.put("transactionType", 1);
                    params.put("paymentType", 2);
                    break;
                case 1007:
                    params.put("transactionType", 2);
                    params.put("paymentType", 0);
                    break;
                case 1008:
                    params.put("transactionType", 2);
                    params.put("paymentType", 2);
                    break;
            }
        }

        //المخازن    
        if (moduleCode == 2) {
            switch (screenCode) {
                case 1009:
                    params.put("type", 0);
                    params.put("paymentType", 0);
                    break;
                case 1010:
                    params.put("type", 0);
                    params.put("paymentType", 1);
                    break;
                case 1011:
                    params.put("type", 0);
                    params.put("paymentType", 2);
                    break;
                case 1012:
                    params.put("type", 1);
                    params.put("paymentType", 0);
                    break;
                case 1013:
                    params.put("type", 1);
                    params.put("paymentType", 1);
                    break;
                case 1014:
                    params.put("type", 1);
                    params.put("paymentType", 2);
                    break;
                case 1015:
                    params.put("type", 2);
                    params.put("paymentType", 0);
                    break;
                case 1016:
                    params.put("type", 2);
                    params.put("paymentType", 1);
                    break;
                case 1018:
                    params.put("type", 3);
                    params.put("paymentType", 0);
                    break;
                case 1019:
                    params.put("type", 3);
                    params.put("paymentType", 1);
                    break;
            }
        }

        if (moduleCode == 5) {
            switch (screenCode) {
                case 1030:
                    params.put("type", 0);
                    break;
                case 1031:
                    params.put("type", 1);
                    break;
                case 1032:
                    params.put("type", 2);
                    break;
                case 1033:
                    params.put("type", 3);
                    break;
            }
        }

    }

    @Override
    public PostRestrictionsView getOneInstBankTransaction(Integer instContractId) {

        InstContract instContract;
        instContract = dao.findEntityById(InstContract.class, instContractId);
        List<InstContract> instContractList = new ArrayList<>();
        instContractList.add(instContract);
        Integer InstallmentDocumentID = 1021;
        List<PostRestrictionsView> restrictionsViewList = returnListView(instContractList, InstallmentDocumentID);
        if (restrictionsViewList != null && !restrictionsViewList.isEmpty()) {
            return restrictionsViewList.get(0);
        } else {
            return null;
        }

    }
}
