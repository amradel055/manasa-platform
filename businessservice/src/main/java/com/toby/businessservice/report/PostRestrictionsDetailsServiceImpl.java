/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.businessservice.InvPurchaseInvoiceService_1;
import com.toby.core.GenericDAO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.entity.GlAccount;
import com.toby.entity.GlBankTransactionDetail;
import com.toby.entity.InvNotice;
import com.toby.entity.InvPurchaseInvoiceDetail;
import com.toby.views.PostRestrictionsDetailsView;
import com.toby.views.PostRestrictionsView;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
public class PostRestrictionsDetailsServiceImpl implements PostRestrictionsDetailsService {

    @EJB
    private GenericDAO dao;
    @EJB
    private InvPurchaseInvoiceService_1 invPurchaseInvoiceService_1;

    @Override
    public List<PostRestrictionsDetailsView> getPostRestrictionsDetailsByPostRestrictionsId(Integer glBankTransactionId, Integer moduleCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("glBankTransactionId", glBankTransactionId);
        params.put("moduleCode", moduleCode);
        List<PostRestrictionsDetailsView> details = dao.findListByQuery("SELECT e FROM PostRestrictionsDetailsView e WHERE e.glBankTransactionId = :glBankTransactionId and e.moduleCode= :moduleCode ", params);
        return details;
    }

    @Override
    public List<PostRestrictionsDetailsView> getPostRestrictionsDetailsByBankIdDebitAccountAndDate(PostRestrictionsView postRestrictionsView, Date documentDate) {

        dao.executeNativeQuery("select toby.sql_mode_function()");
        Map<String, Object> params = new HashMap<>();
        params.put("accountId", postRestrictionsView.getAccountId());

        String accountIdClass = "";
        if (postRestrictionsView.getScreenCode() == -1000l) {
            accountIdClass = "e.accountDebitId";
            params.put("transactionType", 1);
        } else {
            accountIdClass = "e.accountCreditId";
            params.put("transactionType", 0);
        }
        params.put("documentDate", documentDate);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select sum(COALESCE(e.value,0)) , e.date , e.accountCreditId , e.accountDebitId ,e.accNumberDebit , e.accNumberCredit ,e.accNameDebit , e.accNameCredit ,e.branchId FROM PostRestrictionsDetailsView e "
                + "WHERE ").append(accountIdClass).append(" = :accountId and e.date = :documentDate and e.transactionType = :transactionType group by e.accountCreditId ,e.accountDebitId,e.date ");
        List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);
        List<PostRestrictionsDetailsView> PostRestrictionsViewDetailList = getPostRestrictionViewDetailList(res);
        return PostRestrictionsViewDetailList;
    }

    public List<PostRestrictionsDetailsView> getPostRestrictionViewDetailList(List<Object[]> res) {

        List<PostRestrictionsDetailsView> postRestrictionsDetailsViewList = new ArrayList<>();

        Iterator it = res.iterator();
        while (it.hasNext()) {
            Object[] line = (Object[]) it.next();
            PostRestrictionsDetailsView postRestrictionsDetailsView = new PostRestrictionsDetailsView();
            postRestrictionsDetailsView.setValue((BigDecimal) line[0]);
            postRestrictionsDetailsView.setDate((Date) line[1]);
            postRestrictionsDetailsView.setAccountCreditId((Integer) line[2]);
            postRestrictionsDetailsView.setAccountDebitId((Integer) line[3]);
            postRestrictionsDetailsView.setAccNumberDebit((Integer) line[4]);
            postRestrictionsDetailsView.setAccNumberCredit((Integer) line[5]);
            postRestrictionsDetailsView.setAccNameDebit((String) line[6]);
            postRestrictionsDetailsView.setAccNameCredit((String) line[7]);
            postRestrictionsDetailsView.setBranchId((Integer) line[8]);

            postRestrictionsDetailsViewList.add(postRestrictionsDetailsView);
        }
        return postRestrictionsDetailsViewList;
    }

    @Override
    public List<PostRestrictionsDetailsView> getPostRestrictionsDetailsByPostRestrictionsId(Integer branchId, Integer glBankTransactionId, Integer moduleCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("glBankTransactionId", glBankTransactionId);
        /*  params.put("moduleCode", moduleCode);*/
        params.put("branchId", branchId);
        List<PostRestrictionsDetailsView> list = new ArrayList();
        index = 0;
        if (moduleCode == 1) { // here قبض نقدي
            List<GlBankTransactionDetail> details = dao.findListByQuery("SELECT g FROM GlBankTransactionDetail g WHERE g.glBankTransactionId.id = :glBankTransactionId AND g.glBankTransactionId.branchId.id = :branchId ", params);
            list = returnListView(details, 1);
        } else if (moduleCode == 2) {
            List<InvPurchaseInvoiceDetail> details = dao.findListByQuery("SELECT i , sum(i.net)    FROM InvPurchaseInvoiceDetail i WHERE i.invPurchaseInvoiceId.id = :glBankTransactionId AND i.invPurchaseInvoiceId.branchId.id = :branchId Group by i.invPurchaseInvoiceId.id", params);
            list = returnListView(details, 2);
        } else if (moduleCode == 3) {
//            List<InstContract> details = dao.findListByQuery("SELECT i FROM InstContract i WHERE i.id = :glBankTransactionId AND i.branchId.id = :branchId ", params);
//            list = returnListView(details, 3);
        } else if (moduleCode == 4) {
//            List<RscExtractRegister> details = dao.findListByQuery("SELECT i FROM RscExtractRegister i WHERE i.id = :glBankTransactionId AND i.branchId.id = :branchId ", params);
//            list = returnListView(details, 4);
        } else if (moduleCode == 5) {
            List<InvNotice> details = dao.findListByQuery("SELECT i FROM InvNotice i WHERE i.id = :glBankTransactionId AND i.branchId.id = :branchId ", params);
            list = returnListView(details, 5);
        }
        return list;
    }

    int index = 0;

    private <T> PostRestrictionsDetailsView initMapToView(T Entity, Integer moduleCode) {
        StringBuilder error = new StringBuilder();
        PostRestrictionsDetailsView view = new PostRestrictionsDetailsView();
        index++;
        if (moduleCode == 1) { // قبض نقدي
            GlBankTransactionDetail entity = (GlBankTransactionDetail) Entity;
            view.setRowNum(index);
            view.setGlBankTransactionId(entity.getId());

//            if (entity.getBankIdFrom() != null && entity.getBankIdFrom().getAccountId() != null) {
//                view.setAccountDebitId(entity.getBankIdFrom().getAccountId() != null ? entity.getBankIdFrom().getAccountId().getId() : null);
//                view.setAccNumberDebit(entity.getBankIdFrom().getAccountId() != null ? entity.getBankIdFrom().getAccountId().getAccNumber() : null);
//                view.setAccNameDebit(entity.getBankIdFrom().getAccountId() != null ? entity.getBankIdFrom().getAccountId().getName() : null);
//            } else {
//                error.append("\n المسلسل رقم ليس لديه حساب مدين").append(index);
//                view.setErrorMsg(error.toString());
//                return view;
//            }
//
//
//            if (entity.getOrganizationSiteId() != null && entity.getOrganizationSiteId().getAccountId() != null) {
//                view.setAccountCreditId(entity.getOrganizationSiteId().getAccountId() != null ? entity.getOrganizationSiteId().getAccountId().getId() : null);
//                view.setAccNumberCredit(entity.getOrganizationSiteId().getAccountId() != null ? entity.getOrganizationSiteId().getAccountId().getAccNumber() : null);
//                view.setAccNameCredit(entity.getOrganizationSiteId().getAccountId() != null ? entity.getOrganizationSiteId().getAccountId().getName() : null);
//            } else {
//                error.append("\n المسلسل رقم ليس لديه حساب دائن").append(index);
//                view.setErrorMsg(error.toString());
//                return view;
//            }
            if (entity.getAccountDebitId() != null) {
                view.setAccountDebitId(entity.getAccountDebitId() != null ? entity.getAccountDebitId().getId() : null);
                view.setAccNumberDebit(entity.getAccountDebitId() != null ? entity.getAccountDebitId().getAccNumber() : null);
                view.setAccNameDebit(entity.getAccountDebitId() != null ? entity.getAccountDebitId().getName() : null);
            } else {
                error.append("\n المسلسل رقم ليس لديه حساب مدين").append(index);
                view.setErrorMsg(error.toString());
                return view;
            }
            if (entity.getAccountCreditId() != null) {
                view.setAccountCreditId(entity.getAccountCreditId() != null ? entity.getAccountCreditId().getId() : null);
                view.setAccNumberCredit(entity.getAccountCreditId() != null ? entity.getAccountCreditId().getAccNumber() : null);
                view.setAccNameCredit(entity.getAccountCreditId() != null ? entity.getAccountCreditId().getName() : null);
            } else {
                error.append("\n المسلسل رقم ليس لديه حساب دائن").append(index);
                view.setErrorMsg(error.toString());
                return view;
            }
            view.setValue(entity.getValue());
            view.setValueLocal(entity.getValueLocal());

            view.setSecondAccountCreditId(null);
            view.setSecondAccNumberCredit(null);
            view.setSecondAccNameCredit(null);

            view.setCostCenterId(entity.getCostCenterId() != null ? entity.getCostCenterId().getId() : null);
            view.setAdminUnitId(entity.getAdminUnitId() != null ? entity.getAdminUnitId().getId() : null);

            view.setSecondValue(null);
            view.setTaxValue(BigDecimal.ZERO);
            view.setTaxValueLocal(BigDecimal.ZERO);
            view.setRemarks(entity.getRemarks());
            view.setModuleCode(1);
            view.setBranchId(entity.getGlBankTransactionId() != null ? entity.getGlBankTransactionId().getBranchId().getId() : null);
            view.setDate(entity.getGlBankTransactionId().getDate());
            view.setTransactionType(entity.getGlBankTransactionId() != null ? entity.getGlBankTransactionId().getTransactionType() : null);

            Map<String, Object> params = new HashMap<>();
            params.put("branchId", entity.getGlBankTransactionId().getBranchId().getId());

            if (entity.getBankIdTo() != null && entity.getBankIdFrom() == null) {
                Integer accountId_2 = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"BANK_COMMITION\" AND a.accountId IS NOT NULL", params);
                GlAccount gla2 = dao.findEntityByQuery("SELECT g FROM GlAccount g where g.id = " + accountId_2);
                if (gla2 != null) {
                    view.setSecondAccountDebitId(gla2.getId());
                    view.setSecondAccNumberDebit(gla2.getAccNumber());
                    view.setSecondAccNameDebit(gla2.getName());
                }else{
                    view.setErrorMsg( ";يرجي ربط حساب العمولة;");
                }
            }

            if (entity.getBankIdFrom() != null && entity.getBankIdFrom().getCommition() != null
                    && entity.getBankIdFrom().getCommition().compareTo(BigDecimal.ZERO) == 1) {

                Integer accountId_3 = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"BANK_COMMITION\" AND a.accountId IS NOT NULL", params);
                Integer accountId_2 = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where  a.branchId.id =  :branchId AND a.code = \"BANK_TAX\" AND a.accountId IS NOT NULL", params);
                GlAccount gla2 = dao.findEntityByQuery("SELECT g FROM GlAccount g where g.id = " + accountId_2);
                GlAccount gla3 = dao.findEntityByQuery("SELECT g FROM GlAccount g where g.id = " + accountId_3);
                if (gla3 != null && gla2 != null) {

                    view.setSecondValue(entity.getValue().multiply(entity.getBankIdFrom().getCommition().divide(new BigDecimal(100))));
                    view.setTaxValue(view.getSecondValue().multiply(new BigDecimal(0.15)));
                    view.setTaxValueLocal(view.getTaxValue());

                    view.setSecondAccountDebitId(gla2.getId());
                    view.setSecondAccNumberDebit(gla2.getAccNumber());
                    view.setSecondAccNameDebit(gla2.getName());

                    view.setThirdAccountDebitId(gla3.getId());
                    view.setThirdAccNumberDebit(gla3.getAccNumber());
                    view.setThirdAccNameDebit(gla3.getName());
                } else {
                    String ErrorMsg = "";
                    if (gla3 == null) {
                        ErrorMsg += "يرجي ربط حساب العمولة ;";
                    }
                    if (gla2 == null) {
                        ErrorMsg += ";يرجي ربط حساب الضريبة;";
                    }
                    view.setErrorMsg(ErrorMsg);
                }

            }
        } else if (moduleCode == 2) {
            Object[] _Entity = (Object[]) Entity;
            InvPurchaseInvoiceDetail entity = (InvPurchaseInvoiceDetail) _Entity[0];
            entity.setSumDetailNet((BigDecimal) _Entity[_Entity.length - 1]);
            view.setRowNum(index);
            if (entity.getInvPurchaseInvoiceId().getType() == 1 || entity.getInvPurchaseInvoiceId().getType() == 2) {
                if (entity.getInvPurchaseInvoiceId().getPaymentType().equals(0) && entity.getInvPurchaseInvoiceId().getGlBankId() != null
                        && entity.getInvPurchaseInvoiceId().getGlBankId().getAccountId() != null) {
                    view.setAccountDebitId(entity.getInvPurchaseInvoiceId().getGlBankId().getAccountId().getId());
                    view.setAccNumberDebit(entity.getInvPurchaseInvoiceId().getGlBankId().getAccountId().getAccNumber());
                    view.setAccNameDebit(entity.getInvPurchaseInvoiceId().getGlBankId().getAccountId().getName());

                } else if ((entity.getInvPurchaseInvoiceId().getPaymentType().equals(1) || entity.getInvPurchaseInvoiceId().getPaymentType().equals(2))
                        && entity.getInvPurchaseInvoiceId().getOrganizationSiteId() != null
                        && entity.getInvPurchaseInvoiceId().getOrganizationSiteId().getAccountId() != null) {
                    view.setAccountDebitId(entity.getInvPurchaseInvoiceId().getOrganizationSiteId().getAccountId().getId());
                    view.setAccNumberDebit(entity.getInvPurchaseInvoiceId().getOrganizationSiteId().getAccountId().getAccNumber());
                    view.setAccNameDebit(entity.getInvPurchaseInvoiceId().getOrganizationSiteId().getAccountId().getName());

                }

                if (view.getAccountDebitId() == null) {
                    error.append("\n المسلسل رقم ليس لديه حساب مدين").append(index);
                    view.setErrorMsg(error.toString());
                    return view;
                }
            } else if (entity.getInvPurchaseInvoiceId().getType() == 0 || entity.getInvPurchaseInvoiceId().getType() == 3) {
                if (entity.getInvPurchaseInvoiceId().getPaymentType().equals(0) && entity.getInvPurchaseInvoiceId().getGlBankId() != null
                        && entity.getInvPurchaseInvoiceId().getGlBankId().getAccountId() != null) {
                    view.setAccountCreditId(entity.getInvPurchaseInvoiceId().getGlBankId().getAccountId().getId());
                    view.setAccNumberCredit(entity.getInvPurchaseInvoiceId().getGlBankId().getAccountId().getAccNumber());
                    view.setAccNameCredit(entity.getInvPurchaseInvoiceId().getGlBankId().getAccountId().getName());

                } else if ((entity.getInvPurchaseInvoiceId().getPaymentType().equals(1) || entity.getInvPurchaseInvoiceId().getPaymentType().equals(2))
                        && entity.getInvPurchaseInvoiceId().getOrganizationSiteId() != null
                        && entity.getInvPurchaseInvoiceId().getOrganizationSiteId().getAccountId() != null) {
                    view.setAccountCreditId(entity.getInvPurchaseInvoiceId().getOrganizationSiteId().getAccountId().getId());
                    view.setAccNumberCredit(entity.getInvPurchaseInvoiceId().getOrganizationSiteId().getAccountId().getAccNumber());
                    view.setAccNameCredit(entity.getInvPurchaseInvoiceId().getOrganizationSiteId().getAccountId().getName());

                }

                if (view.getAccountCreditId() == null) {
                    error.append("\n المسلسل رقم ليس لديه حساب دائن").append(index);
                    view.setErrorMsg(error.toString());
                    return view;
                }
            }
            InvInvoiceDTO invInvoiceDTO = invPurchaseInvoiceService_1.findInvPurchaseInvoiceById(entity.getInvPurchaseInvoiceId().getId());
            view.setValue(invInvoiceDTO.getValue());
            view.setValueLocal(invInvoiceDTO.getValueLocal());
            view.setTaxValue(invInvoiceDTO.getTaxValue());
            view.setTaxValueLocal(invInvoiceDTO.getTaxValueLocal());
            view.setTaxDiscValue(invInvoiceDTO.getTaxdiscvalue());
            view.setTaxDiscValueLocal(invInvoiceDTO.getTaxDiscValueLocal());

//            BigDecimal valueAfterDiscount = BigDecimal.ZERO;
//            if (entity.getInvPurchaseInvoiceId() != null && entity.getInvPurchaseInvoiceId().getDiscountType() == 0) {
//                valueAfterDiscount = entity.getSumDetailNet().subtract(entity.getInvPurchaseInvoiceId().getDiscount());
//            } else if (entity.getInvPurchaseInvoiceId() != null && entity.getInvPurchaseInvoiceId().getDiscountType() == 1) {
//                valueAfterDiscount = entity.getSumDetailNet().subtract((entity.getInvPurchaseInvoiceId().getDiscount().multiply(entity.getSumDetailNet())).divide(new BigDecimal(100)));
//            }
//            view.setValue(valueAfterDiscount);
//            view.setValueLocal(view.getValue().multiply(entity.getInvPurchaseInvoiceId().getRate()));
//
//            if (entity.getInvPurchaseInvoiceId() != null && entity.getInvPurchaseInvoiceId().getTaxflag()) {
//                view.setTaxValue(view.getValue().multiply(new BigDecimal(0.14)).setScale(3, BigDecimal.ROUND_DOWN));
//
//            } else if (entity.getInvPurchaseInvoiceId() != null && !entity.getInvPurchaseInvoiceId().getTaxflag()) {
//                view.setTaxValue(BigDecimal.ZERO);
//            }
//            view.setTaxValueLocal(view.getTaxValue().multiply(entity.getInvPurchaseInvoiceId().getRate()));
//
//            if (entity.getInvPurchaseInvoiceId() != null && entity.getInvPurchaseInvoiceId().getTaxdiscflag()) {
//                view.setTaxDiscValue(view.getValue().multiply(new BigDecimal(0.01)).setScale(3, BigDecimal.ROUND_DOWN));
//
//            } else if (entity.getInvPurchaseInvoiceId() != null && !entity.getInvPurchaseInvoiceId().getTaxdiscflag()) {
//                view.setTaxDiscValue(BigDecimal.ZERO);
//            }
//            view.setTaxDiscValueLocal(view.getTaxDiscValue().multiply(entity.getInvPurchaseInvoiceId().getRate()));
            //  view.setValueLocal(valueLocal);
            view.setRemarks(entity.getInvPurchaseInvoiceId() != null ? entity.getInvPurchaseInvoiceId().getRemarks() : null);
            view.setModuleCode(2);
            view.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
            view.setDate(entity.getInvPurchaseInvoiceId().getDate());
            //  view.setTransactionType(entity.getInvPurchaseInvoiceId() != null ? entity.getInvPurchaseInvoiceId().getTransactionType() : null);

        } else if (moduleCode == 3) {
//            InstContract entity = (InstContract) Entity;
//            Map<String, Object> params = new HashMap<>();
//            params.put("branchId", entity.getBranchId().getId());
//            Integer accountId_3 = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"MURABAHA\" AND a.accountId IS NOT NULL", params);
//            Integer accountId_2 = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where  a.branchId.id =  :branchId AND a.code = \"INSTALLMENT_SALES_ACCOUNT_NUMBER\" AND a.accountId IS NOT NULL", params);
//            GlAccount gla3 = dao.findEntityByQuery("SELECT g FROM GlAccount g where g.id = " + accountId_3);
//            GlAccount gla2 = dao.findEntityByQuery("SELECT g FROM GlAccount g where g.id = " + accountId_2);
//            if (gla3 != null && gla2 != null) {
//                view.setRowNum(index);
//                view.setGlBankTransactionId(entity.getId());
//                if (entity.getOrganizationSiteId() != null && entity.getOrganizationSiteId().getAccountId() != null) {
//                    view.setAccountDebitId(entity.getOrganizationSiteId() != null ? entity.getOrganizationSiteId().getAccountId().getId() : null);
//                } else {
//                    error.append("\n المسلسل رقم ليس لديه حساب مدين").append(index);
//                    view.setErrorMsg(error.toString());
//                    return view;
//                }
//                view.setAccountCreditId(accountId_2);
//                view.setSecondAccountCreditId(gla3 != null ? gla3.getId() : null);
//                view.setCostCenterId(null);
//                view.setAdminUnitId(null);
//                view.setAccNumberDebit(entity.getOrganizationSiteId().getAccountId() != null ? entity.getOrganizationSiteId().getAccountId().getAccNumber() : null);
//                view.setAccNumberCredit(gla2 != null ? gla2.getAccNumber() : null);
//                view.setSecondAccNumberCredit(gla3 != null ? gla3.getAccNumber() : null);
//                view.setAccNameDebit(entity.getOrganizationSiteId().getAccountId() != null ? entity.getOrganizationSiteId().getAccountId().getName() : null);
//                view.setAccNameCredit(gla2 != null ? gla2.getName() : null);
//                view.setSecondAccNameCredit(gla3 != null ? gla3.getName() : null);
//                view.setValue(entity.getTotalValue());
//                view.setValueLocal(BigDecimal.ZERO);
//                view.setSecondValue(entity.getInstallmentValue());
//                view.setTaxValue(entity.getMurabahaRate());
//                view.setTaxValueLocal(BigDecimal.ZERO);
//                view.setRemarks(null);
//                view.setModuleCode(3);
//                view.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
//                view.setDate(null);
//                view.setTransactionType(null);
//            } else {
//                String ErrorMsg = "";
//                if (gla3 == null) {
//                    ErrorMsg += "يرجي ربط حساب المرابحه ;";
//                }
//                if (gla2 == null) {
//                    ErrorMsg += "يرجي ربط حساب  مبيعات التقسيط ;";
//                }
//                view.setErrorMsg(ErrorMsg);
//            }

        } else if (moduleCode == 4) {
//            RscExtractRegister entity = (RscExtractRegister) Entity;
//            BigDecimal value = rscExtractRegisterService.getTotalNetCost(entity.getBranchId().getId(), entity.getId());
//            Map<String, Object> params = new HashMap<>();
//            params.put("branchId", entity.getBranchId().getId());
//
//            if (entity.getCustomerId() != null) {
//                Integer accountId_3 = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"PROJECT_REVENUE\" AND a.accountId IS NOT NULL", params);
//                GlAccount gla3 = dao.findEntityByQuery("SELECT g FROM GlAccount g where g.id = " + accountId_3);
//                if (entity.getInstProjectGuide() != null && entity.getInstProjectGuide().getAccountId() != null
//                        && entity.getCustomerId() != null && entity.getCustomerId().getAccountId() != null && gla3 != null) {
//
//                    view.setAccountDebitId(entity.getCustomerId() != null ? entity.getCustomerId().getAccountId().getId() : null);
//                    view.setAccNumberDebit(entity.getCustomerId().getAccountId().getAccNumber());
//                    view.setAccNameDebit(entity.getCustomerId().getAccountId().getName());
//
//                    view.setAccountCreditId(entity.getInstProjectGuide() != null ? entity.getInstProjectGuide().getAccountId().getId() : null);
//                    view.setAccNumberCredit(entity.getInstProjectGuide().getAccountId().getAccNumber());
//                    view.setAccNameCredit(entity.getInstProjectGuide().getAccountId().getName());
//
//                    view.setSecondAccountCreditId(gla3.getId());
//                    view.setSecondAccNumberCredit(gla3.getAccNumber());
//                    view.setSecondAccNameCredit(gla3.getName());
//
//                    view.setThirdAccountCreditId(entity.getSiteId() != null ? entity.getSiteId().getAccountId().getId() : null);
//                    view.setThirdAccNumberCredit(entity.getSiteId().getAccountId().getAccNumber());
//                    view.setThirdAccNameCredit(entity.getSiteId().getAccountId().getName());
//
//                    view.setSecondAccountDebitId(entity.getInstProjectGuide() != null ? entity.getInstProjectGuide().getAccountId().getId() : null);
//                    view.setSecondAccNumberDebit(entity.getInstProjectGuide().getAccountId().getAccNumber());
//                    view.setSecondAccNameDebit(entity.getInstProjectGuide().getAccountId().getName());
//                } else {
//                    error.append("\n المسلسل رقم" + index + " ليس لديه حساب مشروعات او العميل او حساب الايرادات");
//                    view.setErrorMsg(error.toString());
//                    return view;
//
//                }
//            } else {
//                if (entity.getInstProjectGuide() != null && entity.getInstProjectGuide().getAccountId() != null && entity.getSiteId() != null && entity.getSiteId().getAccountId() != null) {
//                    view.setAccountDebitId(entity.getInstProjectGuide() != null ? entity.getInstProjectGuide().getAccountId().getId() : null);
//                    view.setAccNumberDebit(entity.getInstProjectGuide().getAccountId().getAccNumber());
//                    view.setAccNameDebit(entity.getInstProjectGuide().getAccountId().getName());
//                    view.setAccountCreditId(entity.getSiteId() != null ? entity.getSiteId().getAccountId().getId() : null);
//                    view.setAccNumberCredit(entity.getSiteId().getAccountId().getAccNumber());
//                    view.setAccNameCredit(entity.getSiteId().getAccountId().getName());
//
//                } else {
//                    error.append("\n المسلسل رقم" + index + " ليس لديه حساب مشروعات او العميل ");
//                    view.setErrorMsg(error.toString());
//                    return view;
//                }
//
//            }

//            view.setRowNum(index);
//            view.setGlBankTransactionId(entity.getId());
//            view.setCostCenterId(null);
//            view.setAdminUnitId(null);
//            view.setValue(value);
//            view.setValueLocal(BigDecimal.ZERO);
//            view.setSecondValue(BigDecimal.ZERO);
//            view.setTaxValue(entity.getPercentage());
//            view.setTaxValueLocal(BigDecimal.ZERO);
//            view.setRemarks(null);
//            view.setModuleCode(4);
//            view.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
//            view.setDate(null);
//            view.setTransactionType(null);
        } else if (moduleCode == 5) {
            InvNotice entity = (InvNotice) Entity;
            if (entity.getOrganizationSiteId() != null && entity.getOrganizationSiteId().getAccountId() != null) {

                String accountName = "";
                if (entity.getType() == 0) {
                    accountName = "ADD_NOTICE_CUSTOMER";
                } else if (entity.getType() == 1) {
                    accountName = "DISCOUNT_NOTICE_CUSTOMER";
                } else if (entity.getType() == 2) {
                    accountName = "ADD_NOTICE_SUPPLIER";
                } else if (entity.getType() == 3) {
                    accountName = "DISCOUNT_NOTICE_SUPPLIER";
                }

                Map<String, Object> params = new HashMap<>();
                params.put("branchId", entity.getBranchId().getId());
                params.put("accountName", accountName);

                Integer addNoticeAccountNumber = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = :accountName AND a.accountId IS NOT NULL", params);
                GlAccount addNoticeAccount = dao.findEntityByQuery("SELECT g FROM GlAccount g where g.id = " + addNoticeAccountNumber);
                if (addNoticeAccount != null) {
                    view.setRowNum(index);
                    view.setGlBankTransactionId(entity.getId());
                    view.setCostCenterId(null);
                    view.setAdminUnitId(null);

                    if (entity.getType() == 0 || entity.getType() == 3) {
                        accountName = "ADD_NOTICE_CUSTOMER";
                        view.setAccountDebitId(entity.getOrganizationSiteId() != null ? entity.getOrganizationSiteId().getAccountId().getId() : null);
                        view.setAccNumberDebit(entity.getOrganizationSiteId().getAccountId() != null ? entity.getOrganizationSiteId().getAccountId().getAccNumber() : null);
                        view.setAccNameDebit(entity.getOrganizationSiteId().getAccountId() != null ? entity.getOrganizationSiteId().getAccountId().getName() : null);

                        view.setAccountCreditId(addNoticeAccountNumber);
                        view.setAccNumberCredit(addNoticeAccount != null ? addNoticeAccount.getAccNumber() : null);
                        view.setAccNameCredit(addNoticeAccount != null ? addNoticeAccount.getName() : null);
                    } else if (entity.getType() == 1 || entity.getType() == 2) {
                        view.setAccountDebitId(addNoticeAccountNumber);
                        view.setAccNumberDebit(addNoticeAccount != null ? addNoticeAccount.getAccNumber() : null);
                        view.setAccNameDebit(addNoticeAccount != null ? addNoticeAccount.getName() : null);

                        view.setAccountCreditId(entity.getOrganizationSiteId() != null ? entity.getOrganizationSiteId().getAccountId().getId() : null);
                        view.setAccNumberCredit(entity.getOrganizationSiteId().getAccountId() != null ? entity.getOrganizationSiteId().getAccountId().getAccNumber() : null);
                        view.setAccNameCredit(entity.getOrganizationSiteId().getAccountId() != null ? entity.getOrganizationSiteId().getAccountId().getName() : null);
                    }

                    view.setValue(entity.getValue());
                    view.setValueLocal(entity.getValue());
                    view.setRemarks(entity.getRemark());
                    view.setModuleCode(5);
                    view.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
                    view.setDate(null);
                    view.setTransactionType(null);
                } else {
                    String ErrorMsg = "";
                    if (addNoticeAccount == null) {
                        ErrorMsg += "يرجي ربط حساب الاشعار ;";
                    }
                    view.setErrorMsg(ErrorMsg);
                }
            } else {
                error.append("\n المسلسل رقم ليس لديه حساب مدين").append(index);
                view.setErrorMsg(error.toString());
                return view;
            }
        }
        return view;
    }

    public <T> List<PostRestrictionsDetailsView> returnListView(List<T> list, Integer moduleCode) {
        List<PostRestrictionsDetailsView> viewList = new ArrayList<>();
        for (T entity : list) {
            viewList.add(mapTOView(entity, moduleCode));
        }
        return viewList;
    }

    public <T> PostRestrictionsDetailsView mapTOView(T entity, Integer moduleCode) {
        PostRestrictionsDetailsView view = initMapToView(entity, moduleCode);
        return view;
    }

}
