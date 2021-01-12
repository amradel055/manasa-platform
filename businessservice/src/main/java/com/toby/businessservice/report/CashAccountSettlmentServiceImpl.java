/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.businessservice.InvPurchaseInvoiceService_1;
import com.toby.businessservice.reports.searchBean.SubAccountSummarySearchBean;
import com.toby.core.GenericDAO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.entity.GlBank;
import com.toby.entity.GlBankTransaction;
import com.toby.entity.GlBankTransactionDetail;
import com.toby.entity.GlYear;
import com.toby.entity.InvPurchaseInvoiceDetail;
import com.toby.entity.InvReturnPurchase;
import com.toby.views.CashAccountSettlmentView;
import com.toby.views.NetView;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author WIN7
 */
@Stateless
public class CashAccountSettlmentServiceImpl implements CashAccountSettlmentService {

    @EJB
    private GenericDAO dao;
    @EJB
    private InvPurchaseInvoiceService_1 invPurchaseInvoiceService_1;

    private NetView netviewList(Integer branchId, Integer headId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("headId", headId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT n FROM NetView n WHERE n.branchId = :branchId AND n.headId = :headId");
        NetView netview = dao.findEntityByQuery(builder.toString(), params);
        return netview;
    }

    private List<CashAccountSettlmentView> findOpenBalance(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT g FROM GlBank g WHERE g.branchId.id = :branchId AND g.dateOpeninngBalance is not null AND g.openinngBalance > 0  ");
        if (cashAccountSettlementSearchBean.getGlBankFrom() != null) {
            builder.append(" and g.code >= :glBankFrom");
            params.put("glBankFrom", cashAccountSettlementSearchBean.getGlBankFrom().getCode());
        }

        if (cashAccountSettlementSearchBean.getGlBankTo() != null) {
            builder.append(" and g.code <= :glBankTo");
            params.put("glBankTo", cashAccountSettlementSearchBean.getGlBankTo().getCode());
        }

        if (cashAccountSettlementSearchBean.getDateFrom() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateFrom());
            builder.append(" and g.dateOpeninngBalance >= '").append(d3).append("'");
        }

        if (cashAccountSettlementSearchBean.getDateTo() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateTo());
            builder.append(" and g.dateOpeninngBalance <= '").append(d3).append("'");
        }

//         if (cashAccountSettlementSearchBean.getTransactionType() != null) {
//            builder.append(" and g.typeId = :transactionType");
//            params.put("transactionType", cashAccountSettlementSearchBean.getTransactionType());
//        }
        if (cashAccountSettlementSearchBean.getSerialFrom() != null) {
            builder.append(" and g.code >= :serialFrom");
            params.put("serialFrom", cashAccountSettlementSearchBean.getSerialFrom().toString());
        }

        if (cashAccountSettlementSearchBean.getSerialTo() != null) {
            builder.append(" and g.code <= :serialTo");
            params.put("serialTo", cashAccountSettlementSearchBean.getSerialTo().toString());
        }

        if (cashAccountSettlementSearchBean.getTobyUserFrom() != null) {
            builder.append(" and g.createdBy.name >= :createdByFrom");
            params.put("createdByFrom", cashAccountSettlementSearchBean.getTobyUserFrom());
        }

        if (cashAccountSettlementSearchBean.getTobyUserTo() != null) {
            builder.append(" and g.createdBy.name <= :createdByTo");
            params.put("createdByTo", cashAccountSettlementSearchBean.getTobyUserTo());
        }
//        if (cashAccountSettlementSearchBean.getOrderBy() == 0) {
//            builder.append(" order by g.id  asc ,g.dateOpeninngBalance  ");
//        } else if (cashAccountSettlementSearchBean.getOrderBy() == 1) {
//            builder.append(" order by g.openinngBalance asc , g.id asc ,g.dateOpeninngBalance  ");
//        } else if (cashAccountSettlementSearchBean.getOrderBy() == 2) {
//            builder.append(" order by g.code asc ,g.id asc ,g.dateOpeninngBalance  ");
//        }
        List<GlBank> glBankList = dao.findListByQuery(builder.toString(), params);
        return returnListView(glBankList, 0);
    }

    private List<CashAccountSettlmentView> findReceipt(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT g FROM GlBankTransaction g WHERE g.branchId.id = :branchId AND g.transactionType <> 2 AND  g.transactionType <> 3 ");
        if (cashAccountSettlementSearchBean.getGlBankFrom() != null && cashAccountSettlementSearchBean.getGlBankFrom().getId() > 0) {
            builder.append(" and g.glBankId.id >= :glBankFrom");
            params.put("glBankFrom", cashAccountSettlementSearchBean.getGlBankFrom().getId());
        }

        if (cashAccountSettlementSearchBean.getGlBankTo() != null && cashAccountSettlementSearchBean.getGlBankTo().getId() > 0) {
            builder.append(" and g.glBankId.id <= :glBankTo");
            params.put("glBankTo", cashAccountSettlementSearchBean.getGlBankTo().getId());
        }

        if (cashAccountSettlementSearchBean.getDateFrom() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateFrom());
            builder.append(" and g.date >= '").append(d3).append("'");
        }

        if (cashAccountSettlementSearchBean.getDateTo() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateTo());
            builder.append(" and g.date <= '").append(d3).append("'");
        }
        if (cashAccountSettlementSearchBean.getType() != null && cashAccountSettlementSearchBean.getType() != 3) {
            builder.append(" and g.transactionType = :type");
            params.put("type", cashAccountSettlementSearchBean.getType());
        }
        if (cashAccountSettlementSearchBean.getTransactionType() != null) {
            builder.append(" and g.transactionType = :transactionType-1");
            params.put("transactionType", cashAccountSettlementSearchBean.getTransactionType());
        }
        if (cashAccountSettlementSearchBean.getSerialFrom() != null) {
            builder.append(" and g.serial >= :serialFrom");
            params.put("serialFrom", cashAccountSettlementSearchBean.getSerialFrom());
        }

        if (cashAccountSettlementSearchBean.getSerialTo() != null) {
            builder.append(" and g.serial <= :serialTo");
            params.put("serialTo", cashAccountSettlementSearchBean.getSerialTo());
        }

        if (cashAccountSettlementSearchBean.getTobyUserFrom() != null) {
            builder.append(" and g.createdBy.name >= :createdByFrom");
            params.put("createdByFrom", cashAccountSettlementSearchBean.getTobyUserFrom());
        }

        if (cashAccountSettlementSearchBean.getTobyUserTo() != null) {
            builder.append(" and g.createdBy.name <= :createdByTo");
            params.put("createdByTo", cashAccountSettlementSearchBean.getTobyUserTo());
        }
//        if (cashAccountSettlementSearchBean.getOrderBy() == 0) {
//            builder.append(" order by g.glBankId.id  asc , g.date  ");
//        } else if (cashAccountSettlementSearchBean.getOrderBy() == 1 /*&& getTransactionType() == 1*/) {
//            builder.append(" order by g.transactionType desc , g.value asc ,  g.glBankId.id asc ,g.date ");
//        }

        List<GlBankTransaction> glBankTransactionList = dao.findListByQuery(builder.toString(), params);
        return returnListView(glBankTransactionList, 0);
    }

    private List<CashAccountSettlmentView> findTransferfrom(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT g FROM GlBankTransactionDetail g WHERE g.glBankTransactionId.branchId.id = :branchId AND g.glBankTransactionId.transactionType = 2 ");
        if (cashAccountSettlementSearchBean.getGlBankFrom() != null && cashAccountSettlementSearchBean.getGlBankFrom().getId() > 0) {
            builder.append(" and g.bankIdFrom.id >= :glBankFrom");
            params.put("glBankFrom", cashAccountSettlementSearchBean.getGlBankFrom().getId());
        }

        if (cashAccountSettlementSearchBean.getGlBankTo() != null && cashAccountSettlementSearchBean.getGlBankTo().getId() > 0) {
            builder.append(" and g.bankIdFrom.id <= :glBankTo");
            params.put("glBankTo", cashAccountSettlementSearchBean.getGlBankTo().getId());
        }

        if (cashAccountSettlementSearchBean.getDateFrom() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateFrom());
            builder.append(" and g.glBankTransactionId.date >= '").append(d3).append("'");
        }

        if (cashAccountSettlementSearchBean.getDateTo() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateTo());
            builder.append(" and g.glBankTransactionId.date <= '").append(d3).append("'");
        }
        if (cashAccountSettlementSearchBean.getType() != null && cashAccountSettlementSearchBean.getType() != 3) {
            builder.append(" and g.glBankTransactionId.transactionType = :type");
            params.put("type", cashAccountSettlementSearchBean.getType());
        }
        if (cashAccountSettlementSearchBean.getSerialFrom() != null) {
            builder.append(" and g.glBankTransactionId.serial >= :serialFrom");
            params.put("serialFrom", cashAccountSettlementSearchBean.getSerialFrom());
        }

        if (cashAccountSettlementSearchBean.getSerialTo() != null) {
            builder.append(" and g.glBankTransactionId.serial <= :serialTo");
            params.put("serialTo", cashAccountSettlementSearchBean.getSerialTo());
        }

        if (cashAccountSettlementSearchBean.getTobyUserFrom() != null) {
            builder.append(" and g.glBankTransactionId.createdBy.name >= :createdByFrom");
            params.put("createdByFrom", cashAccountSettlementSearchBean.getTobyUserFrom());
        }

        if (cashAccountSettlementSearchBean.getTobyUserTo() != null) {
            builder.append(" and g.glBankTransactionId.createdBy.name <= :createdByTo");
            params.put("createdByTo", cashAccountSettlementSearchBean.getTobyUserTo());
        }
//        if (cashAccountSettlementSearchBean.getOrderBy() == 0) {
//            builder.append(" order by g.bankIdFrom.id  asc , g.glBankTransactionId.date  ");
//        }
        List<GlBankTransactionDetail> glBankTransactionDetailList = dao.findListByQuery(builder.toString(), params);
        return returnListView(glBankTransactionDetailList, 0);
    }

    private List<CashAccountSettlmentView> findTransferto(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT g FROM GlBankTransactionDetail g WHERE g.glBankTransactionId.branchId.id = :branchId AND g.glBankTransactionId.transactionType = 2 ");
        if (cashAccountSettlementSearchBean.getGlBankFrom() != null && cashAccountSettlementSearchBean.getGlBankFrom().getId() > 0) {
            builder.append(" and g.bankIdTo.id >= :glBankFrom");
            params.put("glBankFrom", cashAccountSettlementSearchBean.getGlBankFrom().getId());
        }

        if (cashAccountSettlementSearchBean.getGlBankTo() != null && cashAccountSettlementSearchBean.getGlBankTo().getId() > 0) {
            builder.append(" and g.bankIdTo.id <= :glBankTo");
            params.put("glBankTo", cashAccountSettlementSearchBean.getGlBankTo().getId());
        }

        if (cashAccountSettlementSearchBean.getDateFrom() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateFrom());
            builder.append(" and g.glBankTransactionId.date >= '").append(d3).append("'");
        }

        if (cashAccountSettlementSearchBean.getDateTo() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateTo());
            builder.append(" and g.glBankTransactionId.date <= '").append(d3).append("'");
        }
        if (cashAccountSettlementSearchBean.getType() != null && cashAccountSettlementSearchBean.getType() != 3) {
            builder.append(" and g.glBankTransactionId.transactionType = :type");
            params.put("type", cashAccountSettlementSearchBean.getType());
        }
        if (cashAccountSettlementSearchBean.getSerialFrom() != null) {
            builder.append(" and g.glBankTransactionId.serial >= :serialFrom");
            params.put("serialFrom", cashAccountSettlementSearchBean.getSerialFrom());
        }

        if (cashAccountSettlementSearchBean.getSerialTo() != null) {
            builder.append(" and g.glBankTransactionId.serial <= :serialTo");
            params.put("serialTo", cashAccountSettlementSearchBean.getSerialTo());
        }

        if (cashAccountSettlementSearchBean.getTobyUserFrom() != null) {
            builder.append(" and g.glBankTransactionId.createdBy.name >= :createdByFrom");
            params.put("createdByFrom", cashAccountSettlementSearchBean.getTobyUserFrom());
        }

        if (cashAccountSettlementSearchBean.getTobyUserTo() != null) {
            builder.append(" and g.glBankTransactionId.createdBy.name <= :createdByTo");
            params.put("createdByTo", cashAccountSettlementSearchBean.getTobyUserTo());
        }
//        if (cashAccountSettlementSearchBean.getOrderBy() == 0) {
//            builder.append(" order by g.bankIdTo.id  asc , g.glBankTransactionId.date  ");
//        }
        List<GlBankTransactionDetail> glBankTransactionDetailList = dao.findListByQuery(builder.toString(), params);
        return returnListView(glBankTransactionDetailList, 1);
    }

    private List<CashAccountSettlmentView> findSales(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        StringBuilder builder = new StringBuilder();

        builder.append("SELECT g , sum(g.net) FROM InvPurchaseInvoiceDetail g WHERE g.invPurchaseInvoiceId.branchId.id = :branchId AND ( g.invPurchaseInvoiceId.paymentType = 0 OR g.invPurchaseInvoiceId.paymentType = 3 )");

        if (cashAccountSettlementSearchBean.getGlBankFrom() != null && cashAccountSettlementSearchBean.getGlBankFrom().getId() > 0) {
            builder.append(" and g.invPurchaseInvoiceId.glBankId.id >= :glBankFrom");
            params.put("glBankFrom", cashAccountSettlementSearchBean.getGlBankFrom().getId());
        }

        if (cashAccountSettlementSearchBean.getGlBankTo() != null && cashAccountSettlementSearchBean.getGlBankTo().getId() > 0) {
            builder.append(" and g.invPurchaseInvoiceId.glBankId.id <= :glBankTo");
            params.put("glBankTo", cashAccountSettlementSearchBean.getGlBankTo().getId());
        }

        if (cashAccountSettlementSearchBean.getDateFrom() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateFrom());
            builder.append(" and g.invPurchaseInvoiceId.date >= '").append(d3).append("'");
        }

        if (cashAccountSettlementSearchBean.getDateTo() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateTo());
            builder.append(" and g.invPurchaseInvoiceId.date <= '").append(d3).append("'");
        }
        if (cashAccountSettlementSearchBean.getType() != null && cashAccountSettlementSearchBean.getType() != 3) {
            builder.append(" and g.invPurchaseInvoiceId.type = :type");
            params.put("type", cashAccountSettlementSearchBean.getType());
        }
        if (cashAccountSettlementSearchBean.getTransactionType() != null) {
            if (cashAccountSettlementSearchBean.getTransactionType() == 5) {
                params.put("transactionType", 0);
            } else if (cashAccountSettlementSearchBean.getTransactionType() == 6) {
                params.put("transactionType", 1);
            } else if (cashAccountSettlementSearchBean.getTransactionType() == 7) {
                params.put("transactionType", 2);
            } else if (cashAccountSettlementSearchBean.getTransactionType() == 8) {
                params.put("transactionType", 3);
            }
            builder.append(" and g.invPurchaseInvoiceId.type = :transactionType");
        }
        if (cashAccountSettlementSearchBean.getSerialFrom() != null) {
            builder.append(" and g.invPurchaseInvoiceId.serial >= :serialFrom");
            params.put("serialFrom", cashAccountSettlementSearchBean.getSerialFrom());
        }

        if (cashAccountSettlementSearchBean.getSerialTo() != null) {
            builder.append(" and g.invPurchaseInvoiceId.serial <= :serialTo");
            params.put("serialTo", cashAccountSettlementSearchBean.getSerialTo());
        }

        if (cashAccountSettlementSearchBean.getTobyUserFrom() != null) {
            builder.append(" and g.invPurchaseInvoiceId.createdBy.name >= :createdByFrom");
            params.put("createdByFrom", cashAccountSettlementSearchBean.getTobyUserFrom());
        }

        if (cashAccountSettlementSearchBean.getTobyUserTo() != null) {
            builder.append(" and g.invPurchaseInvoiceId.createdBy.name <= :createdByTo");
            params.put("createdByTo", cashAccountSettlementSearchBean.getTobyUserTo());
        }
//        if (cashAccountSettlementSearchBean.getOrderBy() == 0) {
//            builder.append(" order by g.invPurchaseInvoiceId.glBankId.id  asc ,  g.invPurchaseInvoiceId.date  ");
//        }
        builder.append(" Group by g.invPurchaseInvoiceId.id");
        List<Object[]> invPurchaseInvoiceDetailList = dao.findListByQuery(builder.toString(), params);
        return returnListView(invPurchaseInvoiceDetailList, 0);
    }

    private List<CashAccountSettlmentView> findReturnSales(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT g FROM InvReturnPurchase g WHERE g.branchId.id = :branchId AND g.paymentType = 0 ");
        if (cashAccountSettlementSearchBean.getGlBankFrom() != null && cashAccountSettlementSearchBean.getGlBankFrom().getId() > 0) {
            builder.append(" and g.glBankId.id >= :glBankFrom");
            params.put("glBankFrom", cashAccountSettlementSearchBean.getGlBankFrom().getId());
        }

        if (cashAccountSettlementSearchBean.getGlBankTo() != null && cashAccountSettlementSearchBean.getGlBankTo().getId() > 0) {
            builder.append(" and g.glBankId.id <= :glBankTo");
            params.put("glBankTo", cashAccountSettlementSearchBean.getGlBankTo().getId());
        }

        if (cashAccountSettlementSearchBean.getDateFrom() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateFrom());
            builder.append(" and g.date >= '").append(d3).append("'");
        }

        if (cashAccountSettlementSearchBean.getDateTo() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateTo());
            builder.append(" and g.date <= '").append(d3).append("'");
        }
        if (cashAccountSettlementSearchBean.getType() != null && cashAccountSettlementSearchBean.getType() != 3) {
            builder.append(" and g.type = :type");
            params.put("type", cashAccountSettlementSearchBean.getType());
        }
        if (cashAccountSettlementSearchBean.getTransactionType() != null) {
            builder.append(" and g.type = :transactionType");
            int type = 0;
            if (cashAccountSettlementSearchBean.getTransactionType() == 7) {
                type = 0;
            } else if (cashAccountSettlementSearchBean.getTransactionType() == 8) {
                type = 1;
            }
            params.put("transactionType", type);
        }
        if (cashAccountSettlementSearchBean.getSerialFrom() != null) {
            builder.append(" and g.serial >= :serialFrom");
            params.put("serialFrom", cashAccountSettlementSearchBean.getSerialFrom());
        }

        if (cashAccountSettlementSearchBean.getSerialTo() != null) {
            builder.append(" and g.serial <= :serialTo");
            params.put("serialTo", cashAccountSettlementSearchBean.getSerialTo());
        }

        if (cashAccountSettlementSearchBean.getTobyUserFrom() != null) {
            builder.append(" and g.createdBy.name >= :createdByFrom");
            params.put("createdByFrom", cashAccountSettlementSearchBean.getTobyUserFrom());
        }

        if (cashAccountSettlementSearchBean.getTobyUserTo() != null) {
            builder.append(" and g.createdBy.name <= :createdByTo");
            params.put("createdByTo", cashAccountSettlementSearchBean.getTobyUserTo());
        }
        List<InvReturnPurchase> invReturnPurchaseList = dao.findListByQuery(builder.toString(), params);
        return returnListView(invReturnPurchaseList, 0);
    }

    @Override
    public List<CashAccountSettlmentView> findAllCashAccountSettlment(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        List<CashAccountSettlmentView> accountSettlmentViews = new ArrayList<>();
        if (cashAccountSettlementSearchBean.getTransactionType() != null && cashAccountSettlementSearchBean.getTransactionType() == 0) {
            accountSettlmentViews.addAll(findOpenBalance(cashAccountSettlementSearchBean));
        } else if (cashAccountSettlementSearchBean.getTransactionType() != null && (cashAccountSettlementSearchBean.getTransactionType() == 1 || cashAccountSettlementSearchBean.getTransactionType() == 2)) {
            accountSettlmentViews.addAll(findReceipt(cashAccountSettlementSearchBean));
        } else if (cashAccountSettlementSearchBean.getTransactionType() != null && cashAccountSettlementSearchBean.getTransactionType() == 3) {
            accountSettlmentViews.addAll(findTransferfrom(cashAccountSettlementSearchBean));
        } else if (cashAccountSettlementSearchBean.getTransactionType() != null && cashAccountSettlementSearchBean.getTransactionType() == 4) {
            accountSettlmentViews.addAll(findTransferto(cashAccountSettlementSearchBean));
        } else if (cashAccountSettlementSearchBean.getTransactionType() != null && (cashAccountSettlementSearchBean.getTransactionType() == 5 || cashAccountSettlementSearchBean.getTransactionType() == 6 || cashAccountSettlementSearchBean.getTransactionType() == 7 || cashAccountSettlementSearchBean.getTransactionType() == 8)) {
            accountSettlmentViews.addAll(findSales(cashAccountSettlementSearchBean));
//        } else if (cashAccountSettlementSearchBean.getTransactionType() != null && (cashAccountSettlementSearchBean.getTransactionType() == 7 || cashAccountSettlementSearchBean.getTransactionType() == 8)) {
//         //   accountSettlmentViews.addAll(findReturnSales(cashAccountSettlementSearchBean));
        } else {
            accountSettlmentViews.addAll(findOpenBalance(cashAccountSettlementSearchBean));
            accountSettlmentViews.addAll(findReceipt(cashAccountSettlementSearchBean));
            accountSettlmentViews.addAll(findTransferfrom(cashAccountSettlementSearchBean));
            accountSettlmentViews.addAll(findTransferto(cashAccountSettlementSearchBean));
            accountSettlmentViews.addAll(findSales(cashAccountSettlementSearchBean));
        }

//        if (cashAccountSettlementSearchBean.getOrderBy() == 0) {
//            Comparator<CashAccountSettlmentView> compareByOrder = (CashAccountSettlmentView o1, CashAccountSettlmentView o2) -> o1.getOrdered().compareTo(o2.getOrdered());
//            Collections.sort(accountSettlmentViews, compareByOrder);
//        } else if (cashAccountSettlementSearchBean.getOrderBy() == 1) {
//            Comparator<CashAccountSettlmentView> compareByOrder = (CashAccountSettlmentView o1, CashAccountSettlmentView o2) -> o1.getOrdered().compareTo(o2.getOrdered());
//            Collections.sort(accountSettlmentViews, compareByOrder);
//            //    sql = sql + " order by cas.value asc , cas.bankId asc ,cas.date ,cas.ordered ";
//        } else if (cashAccountSettlementSearchBean.getOrderBy() == 2) {
//            //      sql = sql + " order by cas.serial asc , cas.bankId asc ,cas.date ,cas.ordered ";
//        }
        if (cashAccountSettlementSearchBean.getOrderBy() == 0) {
            Comparator<CashAccountSettlmentView> compare = (CashAccountSettlmentView o1, CashAccountSettlmentView o2)
                    -> {
                        int value1 = o1.getBankId().compareTo(o2.getBankId());
                        if (value1 == 0) {
                            int value2 = o1.getDate().compareTo(o2.getDate());
                            if (value2 == 0) {
                                return o1.getOrdered().compareTo(o2.getOrdered());
                            } else {
                                return value2;
                            }
                        }
                        return value1;
                    };

//            Collections.sort(accountSettlmentViews, compare);
        } else if (cashAccountSettlementSearchBean.getOrderBy() == 1) {
            Comparator<CashAccountSettlmentView> compare = (CashAccountSettlmentView o1, CashAccountSettlmentView o2)
                    -> {
                        int value1 = o1.getValue().compareTo(o2.getValue());
                        if (value1 == 0) {
                            int value2 = o1.getBankId().compareTo(o2.getBankId());
                            if (value2 == 0) {
                                int value3 = o1.getDate().compareTo(o2.getDate());
                                if (value3 == 0) {
                                    return o1.getOrdered().compareTo(o2.getOrdered());
                                } else {
                                    return value3;
                                }
                            } else {
                                return value2;
                            }
                        }
                        return value1;
                    };

//            Collections.sort(accountSettlmentViews, compare);
        } else if (cashAccountSettlementSearchBean.getOrderBy() == 2) {

            Comparator<CashAccountSettlmentView> compare = (CashAccountSettlmentView o1, CashAccountSettlmentView o2)
                    -> {
                        int value1 = o1.getSerial().compareTo(o2.getSerial());
                        if (value1 == 0) {
                            int value2 = o1.getBankId().compareTo(o2.getBankId());
                            if (value2 == 0) {
                                int value3 = o1.getDate().compareTo(o2.getDate());
                                if (value3 == 0) {
                                    return o1.getOrdered().compareTo(o2.getOrdered());
                                } else {
                                    return value3;
                                }
                            } else {
                                return value2;
                            }
                        }
                        return value1;
                    };

//            Collections.sort(accountSettlmentViews, compare);
        }

        return accountSettlmentViews;
    }

    @Override
    public List<CashAccountSettlmentView> findAllCashAccountSettlment2(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        String sql = "SELECT cas FROM CashAccountSettlmentView cas WHERE cas.branchId = :branchId " + createSql(cashAccountSettlementSearchBean, params);
        if (cashAccountSettlementSearchBean.getOrderBy() == 0) {
            sql = sql + " order by cas.bankId asc ,cas.date ,cas.ordered ";
        } else if (cashAccountSettlementSearchBean.getOrderBy() == 1) {
            sql = sql + " order by cas.value asc , cas.bankId asc ,cas.date ,cas.ordered ";
        } else if (cashAccountSettlementSearchBean.getOrderBy() == 2) {
            sql = sql + " order by cas.serial asc , cas.bankId asc ,cas.date ,cas.ordered ";
        }
        return dao.findListByQuery(sql, params);
    }

    @Override
    public List<CashAccountSettlmentView> findAllRemainingCashAccountSettlment(SubAccountSummarySearchBean cashAccountSettlementSearchBean, StringBuilder stringBuilder) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        String sql = "SELECT cas FROM CashAccountSettlmentView cas WHERE cas.branchId = :branchId";
        StringBuilder sb = new StringBuilder();
        sb.append(sql);
        sb.append(" and cas.bankId in ( ");
        sb.append(stringBuilder);
        sb.append(" )");
        //  sb.append(" group by cas.bankId ");

        return dao.findListByQuery(sb.toString(), params);
    }

    @Override
    public List<CashAccountSettlmentView> findAllCashAccountSettlmentForLineCheck(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        String sql = "SELECT cas FROM CashAccountSettlmentView cas WHERE cas.branchId = :branchId and cas.paymentType = 2" + createSql(cashAccountSettlementSearchBean, params);

        return dao.findListByQuery(sql, params);
    }

    @Override
    public BigDecimal findOpeningBalancesForSpecificCashAccount2(SubAccountSummarySearchBean cashAccountSettlementSearchBean, Integer bankId, Integer type, GlYear glYear) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        params.put("bankId", bankId);
        params.put("type", type);
        StringBuilder builder = new StringBuilder();
        String sql = "SELECT sum(COALESCE(cas.valueLocal,0)) FROM CashAccountSettlmentView cas WHERE cas.branchId = :branchId and cas.status = :type and cas.bankId= :bankId";
        builder.append(sql);
        addDateFromAndToForOpeningBalance(builder, cashAccountSettlementSearchBean, glYear);
        BigDecimal value = dao.findEntityByQuery(builder.toString(), params);
        return value;
    }

    private BigDecimal findOpenBalanceSum(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        params.put("bankId", cashAccountSettlementSearchBean.getBankId());
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT sum(COALESCE(g.openinngBalance,0)) FROM GlBank g WHERE g.branchId.id = :branchId AND g.id = :bankId ");

        if (cashAccountSettlementSearchBean.getDateTo() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateFrom());
            builder.append(" and g.dateOpeninngBalance < '").append(d3).append("'");
        }

        BigDecimal value = dao.findEntityByQuery(builder.toString(), params);
        return value == null ? BigDecimal.ZERO : value;
    }

    private BigDecimal findReceiptSum(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        params.put("bankId", cashAccountSettlementSearchBean.getBankId());
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT sum(COALESCE(g.valueLocal,0)) FROM GlBankTransaction g WHERE g.branchId.id = :branchId AND g.transactionType <> 2 AND  g.transactionType <> 3 AND g.glBankId.id = :bankId");

        if (cashAccountSettlementSearchBean.getDateTo() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateFrom());
            builder.append(" and g.date < '").append(d3).append("'");
        }
        if (cashAccountSettlementSearchBean.getType() != null) {
            builder.append(" and g.transactionType = :type");
            params.put("type", cashAccountSettlementSearchBean.getType());
        }
        BigDecimal value = dao.findEntityByQuery(builder.toString(), params);
        return value == null ? BigDecimal.ZERO : value;
    }

    private BigDecimal findTransferfromSum(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        params.put("bankId", cashAccountSettlementSearchBean.getBankId());
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT sum(COALESCE(g.valueLocal,0)) FROM GlBankTransactionDetail g WHERE g.glBankTransactionId.branchId.id = :branchId AND g.glBankTransactionId.transactionType = 2 AND g.bankIdFrom.id IS NOT NULL AND g.bankIdFrom.id = :bankId");

        if (cashAccountSettlementSearchBean.getDateTo() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateFrom());
            builder.append(" and g.glBankTransactionId.date < '").append(d3).append("'");
        }

        BigDecimal value = dao.findEntityByQuery(builder.toString(), params);
        return value == null ? BigDecimal.ZERO : value;

    }

    private BigDecimal findTransfertoSum(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        params.put("bankId", cashAccountSettlementSearchBean.getBankId());
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT sum(COALESCE(g.valueLocal,0)) FROM GlBankTransactionDetail g WHERE g.glBankTransactionId.branchId.id = :branchId AND g.glBankTransactionId.transactionType = 2 AND g.bankIdTo.id IS NOT NULL AND g.bankIdTo.id = :bankId");
        if (cashAccountSettlementSearchBean.getDateTo() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateFrom());
            builder.append(" and g.glBankTransactionId.date < '").append(d3).append("'");
        }
        builder.append(" ");
        BigDecimal value = dao.findEntityByQuery(builder.toString(), params);
        return value == null ? BigDecimal.ZERO : value;
    }
    BigDecimal selesValue = BigDecimal.ZERO;

    private BigDecimal findSalesSum(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        try {
            selesValue = BigDecimal.ZERO;
            Map<String, Object> params = new HashMap();
            params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
            params.put("bankId", cashAccountSettlementSearchBean.getBankId());
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT g , sum(g.net) FROM InvPurchaseInvoiceDetail g WHERE g.invPurchaseInvoiceId.glBankId.id  = :bankId  AND g.invPurchaseInvoiceId.paymentType = 0  AND g.invPurchaseInvoiceId.branchId.id = :branchId ");

            if (cashAccountSettlementSearchBean.getDateTo() != null) {
                String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateFrom());
                builder.append(" and g.invPurchaseInvoiceId.date < '").append(d3).append("'");
            }

            if (cashAccountSettlementSearchBean.getType() != null) {
                builder.append(" and g.invPurchaseInvoiceId.type = :type");
                params.put("type", cashAccountSettlementSearchBean.getType());
            }
            builder.append(" Group by g.invPurchaseInvoiceId.id");
            List<Object[]> objectlList = dao.findListByQuery(builder.toString(), params);
            for (Object[] objects : objectlList) {
                InvPurchaseInvoiceDetail entity = (InvPurchaseInvoiceDetail) objects[0];
                entity.setSumDetailNet((BigDecimal) objects[objects.length - 1]);
                InvInvoiceDTO invInvoiceDTO = invPurchaseInvoiceService_1.returnInvInvoiceSummarySumation(entity.getInvPurchaseInvoiceId(), entity.getSumDetailNet());
                selesValue = selesValue.add(invInvoiceDTO.getFinalNet().multiply(invInvoiceDTO.getRate()));
            }

            return selesValue;
        } catch (Exception ex) {
            return BigDecimal.ZERO;
        }
    }

//   private List<CashAccountSettlmentView> findReturnSales(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
//        Map<String, Object> params = new HashMap();
//        params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
//        StringBuilder builder = new StringBuilder();
//        builder.append("SELECT g FROM InvReturnPurchase g WHERE g.branchId.id = :branchId AND g.paymentType = 0 ");
//        if (cashAccountSettlementSearchBean.getGlBankFrom() != null && cashAccountSettlementSearchBean.getGlBankFrom().getId() > 0) {
//            builder.append(" and g.glBankId.id >= :glBankFrom");
//            params.put("glBankFrom", cashAccountSettlementSearchBean.getGlBankFrom().getId());
//        }
//
//        if (cashAccountSettlementSearchBean.getGlBankTo() != null && cashAccountSettlementSearchBean.getGlBankTo().getId() > 0) {
//            builder.append(" and g.glBankId.id <= :glBankTo");
//            params.put("glBankTo", cashAccountSettlementSearchBean.getGlBankTo().getId());
//        }
//
//        if (cashAccountSettlementSearchBean.getDateFrom() != null) {
//            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateFrom());
//            builder.append(" and g.date >= '").append(d3).append("'");
//        }
//
//        if (cashAccountSettlementSearchBean.getDateTo() != null) {
//            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateTo());
//            builder.append(" and g.date <= '").append(d3).append("'");
//        }
//        if (cashAccountSettlementSearchBean.getType() != null && cashAccountSettlementSearchBean.getType() != 3) {
//            builder.append(" and g.type = :type");
//            params.put("type", cashAccountSettlementSearchBean.getType());
//        }
//        if (cashAccountSettlementSearchBean.getTransactionType() != null) {
//            builder.append(" and g.type = :transactionType");
//            int type = 0;
//            if (cashAccountSettlementSearchBean.getTransactionType() == 7) {
//                type = 0;
//            } else if (cashAccountSettlementSearchBean.getTransactionType() == 8) {
//                type = 1;
//            }
//            params.put("transactionType", type);
//        }
//        if (cashAccountSettlementSearchBean.getSerialFrom() != null) {
//            builder.append(" and g.serial >= :serialFrom");
//            params.put("serialFrom", cashAccountSettlementSearchBean.getSerialFrom());
//        }
//
//        if (cashAccountSettlementSearchBean.getSerialTo() != null) {
//            builder.append(" and g.serial <= :serialTo");
//            params.put("serialTo", cashAccountSettlementSearchBean.getSerialTo());
//        }
//
//        if (cashAccountSettlementSearchBean.getTobyUserFrom() != null) {
//            builder.append(" and g.createdBy.name >= :createdByFrom");
//            params.put("createdByFrom", cashAccountSettlementSearchBean.getTobyUserFrom());
//        }
//
//        if (cashAccountSettlementSearchBean.getTobyUserTo() != null) {
//            builder.append(" and g.createdBy.name <= :createdByTo");
//            params.put("createdByTo", cashAccountSettlementSearchBean.getTobyUserTo());
//        }
//        List<InvReturnPurchase> invReturnPurchaseList = dao.findListByQuery(builder.toString(), params);
//        return returnListView(invReturnPurchaseList, 0);
//    }
    BigDecimal Openingvalue = BigDecimal.ZERO;
    BigDecimal SalesSum = BigDecimal.ZERO;
    BigDecimal returnSalesSum = BigDecimal.ZERO;
    BigDecimal ReceiptSum = BigDecimal.ZERO;
    BigDecimal OpenBalanceSum = BigDecimal.ZERO;
    BigDecimal TransfertoSum = BigDecimal.ZERO;
    BigDecimal TransferfromSum = BigDecimal.ZERO;

    @Override
    public BigDecimal findOpeningBalancesForSpecificCashAccount(SubAccountSummarySearchBean cashAccountSettlementSearchBean, Integer bankId, Integer type, GlYear glYear) {
        Openingvalue = BigDecimal.ZERO;
        cashAccountSettlementSearchBean.setBankId(bankId);
        cashAccountSettlementSearchBean.setType(type);
        SalesSum = BigDecimal.ZERO;
        returnSalesSum = BigDecimal.ZERO;

        SalesSum = findSalesSum(cashAccountSettlementSearchBean);
        ReceiptSum = findReceiptSum(cashAccountSettlementSearchBean);
        if (type == 1) {
            cashAccountSettlementSearchBean.setType(2);
            returnSalesSum = findSalesSum(cashAccountSettlementSearchBean);
        } else if (type == 0) {
            cashAccountSettlementSearchBean.setType(3);
            returnSalesSum = findSalesSum(cashAccountSettlementSearchBean);
        }
        if (type == 1) {
            OpenBalanceSum = findOpenBalanceSum(cashAccountSettlementSearchBean);
            TransfertoSum = findTransfertoSum(cashAccountSettlementSearchBean);
            Openingvalue = Openingvalue.add(OpenBalanceSum);
            Openingvalue = Openingvalue.add(TransfertoSum);
        } else if (type == 0) {
            TransferfromSum = findTransferfromSum(cashAccountSettlementSearchBean);
            Openingvalue = Openingvalue.add(TransferfromSum);
        }
        Openingvalue = Openingvalue.add(SalesSum);
        Openingvalue = Openingvalue.add(returnSalesSum);
        Openingvalue = Openingvalue.add(ReceiptSum);
        return Openingvalue;
    }

    public String createSql(SubAccountSummarySearchBean cashAccountSettlementSearchBean, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        addGlBankFromAndTo(sql, params, cashAccountSettlementSearchBean);
        addDateFromAndTo(sql, cashAccountSettlementSearchBean);
        addType(sql, params, cashAccountSettlementSearchBean);
        addtransactionType(sql, params, cashAccountSettlementSearchBean);
        addserialFromAndTo(sql, params, cashAccountSettlementSearchBean);
        addUserFromAndTo(sql, params, cashAccountSettlementSearchBean);
        // addOrderBy(sql, cashAccountSettlementSearchBean);
        return sql.toString();
    }

    private void addGlBankFromAndTo(StringBuilder sql, Map<String, Object> params, SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        if (cashAccountSettlementSearchBean.getGlBankFrom() != null && cashAccountSettlementSearchBean.getGlBankFrom().getId() > 0) {
            sql.append(" and cas.bankId >= :glBankFrom");
            params.put("glBankFrom", cashAccountSettlementSearchBean.getGlBankFrom().getId());
        }

        if (cashAccountSettlementSearchBean.getGlBankTo() != null && cashAccountSettlementSearchBean.getGlBankTo().getId() > 0) {
            sql.append(" and cas.bankId <= :glBankTo");
            params.put("glBankTo", cashAccountSettlementSearchBean.getGlBankTo().getId());
        }
    }

    public void addDateFromAndTo(StringBuilder sql, SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        if (cashAccountSettlementSearchBean.getDateFrom() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateFrom());
            sql.append(" and cas.date >= '").append(d3).append("'");
        }

        if (cashAccountSettlementSearchBean.getDateTo() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateTo());
            sql.append(" and cas.date <= '").append(d3).append("'");
        }
    }

    private void addType(StringBuilder sql, Map<String, Object> params, SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        if (cashAccountSettlementSearchBean.getType() != null && cashAccountSettlementSearchBean.getType() != 3) {
            sql.append(" and cas.type = :type");
            params.put("type", cashAccountSettlementSearchBean.getType());
        }
    }

    private void addtransactionType(StringBuilder sql, Map<String, Object> params, SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        if (cashAccountSettlementSearchBean.getTransactionType() != null) {
            sql.append(" and cas.typeId = :transactionType");
            params.put("transactionType", cashAccountSettlementSearchBean.getTransactionType());
        }
    }

    private void addserialFromAndTo(StringBuilder sql, Map<String, Object> params, SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        if (cashAccountSettlementSearchBean.getSerialFrom() != null) {
            sql.append(" and cas.serial >= :serialFrom");
            params.put("serialFrom", cashAccountSettlementSearchBean.getSerialFrom());
        }

        if (cashAccountSettlementSearchBean.getSerialTo() != null) {
            sql.append(" and cas.serial <= :serialTo");
            params.put("serialTo", cashAccountSettlementSearchBean.getSerialTo());
        }
    }

    private void addUserFromAndTo(StringBuilder sql, Map<String, Object> params, SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        if (cashAccountSettlementSearchBean.getTobyUserFrom() != null) {
            sql.append(" and cas.createdBy >= :createdByFrom");
            params.put("createdByFrom", cashAccountSettlementSearchBean.getTobyUserFrom());
        }

        if (cashAccountSettlementSearchBean.getTobyUserTo() != null) {
            sql.append(" and cas.createdBy <= :createdByTo");
            params.put("createdByTo", cashAccountSettlementSearchBean.getTobyUserTo());
        }
    }

    private void addOrderBy(StringBuilder sql, SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        if (cashAccountSettlementSearchBean.getOrderBy() != null && cashAccountSettlementSearchBean.getOrderBy() == 0) {
            sql.append(" order by cas.date");
        } else {
            sql.append(" order by cas.value");
        }
    }

    public void addDateFromAndToForOpeningBalance(StringBuilder sql, SubAccountSummarySearchBean cashAccountSettlementSearchBean, GlYear glYear) {
//        if (glYear.getDateFrom() != null) {
//            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(glYear.getDateFrom());
//            sql.append(" and cas.date >= '").append(d3).append("'");
//        }

        if (cashAccountSettlementSearchBean.getDateTo() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateFrom());
            sql.append(" and cas.date ").append("< '").append(d3).append("'");
        }
    }

    @Override
    public List<CashAccountSettlmentView> findOpeningBalancesForSpecificCashAccountByGroups(SubAccountSummarySearchBean cashAccountSettlementSearchBean, Integer type, GlYear glYear) {
        List<CashAccountSettlmentView> cashAccountSettlmentViewReturnedList = new ArrayList<>();

        Map<String, Object> params = new HashMap();
        params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        params.put("type", type);
        StringBuilder builder = new StringBuilder();
        String sql = "SELECT sum(COALESCE(cas.valueLocal,0)) , cas.bankId FROM CashAccountSettlmentView cas WHERE cas.branchId = :branchId and cas.type = :type ";
        builder.append(sql);
        addDateFromAndToForOpeningBalance(builder, cashAccountSettlementSearchBean, glYear);
        builder.append(" group by cas.bankId");

        List<Object[]> res = dao.findListByQuery(builder.toString(), params);
        cashAccountSettlmentViewReturnedList = getTheListBasedOnBankIdGroup(res);
        return cashAccountSettlmentViewReturnedList;
    }

    public List<CashAccountSettlmentView> getTheListBasedOnBankIdGroup(List<Object[]> res) {

        List<CashAccountSettlmentView> cashAccountSettlmentViewList = new ArrayList<>();

        Iterator it = res.iterator();
        while (it.hasNext()) {
            Object[] line = (Object[]) it.next();
            CashAccountSettlmentView cashsIterated = new CashAccountSettlmentView();
            cashsIterated.setValue((BigDecimal) line[0]);
            cashsIterated.setBankId((Integer) line[1]);
            cashAccountSettlmentViewList.add(cashsIterated);
        }
        return cashAccountSettlmentViewList;
    }

    public <T> List<CashAccountSettlmentView> returnListView(List<T> list, int type) {
        List<CashAccountSettlmentView> viewList = new ArrayList<>();
        for (T entity : list) {
            viewList.add(mapTOView(entity, type));
        }
        return viewList;
    }

    public <T> CashAccountSettlmentView mapTOView(T entity, int type) {
        CashAccountSettlmentView view = initMapToView(entity, type);
        return view;
    }

    int index = 0;

    private <T> CashAccountSettlmentView initMapToView(T Entity, int type) {
        CashAccountSettlmentView view = new CashAccountSettlmentView();
        index++;
        if (Entity.getClass().getSimpleName().equals("GlBank")) {
            GlBank entity = (GlBank) Entity;
            view.setRowNum(index);
            view.setBanktransactionId(entity.getId());
            view.setBankId(entity.getId());
            view.setCreatedBy(entity.getCreatedBy().getName());
            view.setPaymentType(0);
            view.setDate(entity.getDateOpeninngBalance());
            view.setSerial(new BigInteger(entity.getCode()));
            view.setValue(entity.getOpeninngBalance());
            view.setValueLocal(entity.getOpeninngBalance());
            view.setName(entity.getName());
            view.setCode(entity.getCode());
            if (entity.getCurrencyId() != null) {
                view.setCurrencyCode(entity.getCurrencyId().getCode());
                view.setCurrencyName(entity.getCurrencyId().getName());
            }
            view.setStatus(1);
            view.setTypeId(0);
            view.setTypeName("رصيد افتتاحي");
            view.setOrdered(0);
        } else if (Entity.getClass().getSimpleName().equals("GlBankTransaction")) {
            GlBankTransaction entity = (GlBankTransaction) Entity;
            view.setRowNum(index);
            view.setBanktransactionId(entity.getId());
            view.setCreatedBy(entity.getCreatedBy().getName());
            view.setBankId(entity.getGlBankId().getId());
            view.setType(entity.getTransactionType());
            view.setPaymentType(entity.getPaymentType());
            view.setDate(entity.getDate());
            view.setSerial(new BigInteger(entity.getSerial().toString()));
            view.setValue(entity.getValue());
            view.setValueLocal(entity.getValueLocal());
            view.setRemark(entity.getRemark());
            view.setRemark2(entity.getRemark2());
            if (entity.getGlBankId() != null) {
                view.setName(entity.getGlBankId().getName());
                view.setCode(entity.getGlBankId().getCode());
                if (entity.getGlBankId().getCurrencyId() != null) {
                    view.setCurrencyCode(entity.getGlBankId().getCurrencyId().getCode());
                    view.setCurrencyName(entity.getGlBankId().getCurrencyId().getName());
                }
            }
            view.setStatus(entity.getTransactionType());
//            if (entity.getInstallmentId() != null && entity.getInstallmentId().getInstContractId() != null) {
//                view.setContractSerial(entity.getInstallmentId().getInstContractId().getSerial().toString());
//            }

            if (entity.getTransactionType() == 0) {
                view.setTypeId(1);
                view.setTypeName("سند صرف");
                view.setOrdered(2);
            }
            if (entity.getTransactionType() == 1) {
                view.setTypeId(2);
                view.setTypeName("سند قبض");
                view.setOrdered(1);
            }

        } else if (Entity.getClass().getSimpleName().equals("GlBankTransactionDetail")) {
            if (type == 0) {
                GlBankTransactionDetail entity = (GlBankTransactionDetail) Entity;
                view.setRowNum(index);
                if (entity.getGlBankTransactionId() != null) {
                    view.setBanktransactionId(entity.getGlBankTransactionId().getId());
                    view.setType(entity.getGlBankTransactionId().getTransactionType());
                    view.setPaymentType(entity.getGlBankTransactionId().getPaymentType());
                    view.setDate(entity.getGlBankTransactionId().getDate());
                    view.setSerial(new BigInteger(entity.getGlBankTransactionId().getSerial().toString()));
                    view.setRemark2(entity.getGlBankTransactionId().getRemark2());

                    if (entity.getGlBankTransactionId().getCreatedBy() != null) {
                        view.setCreatedBy(entity.getGlBankTransactionId().getCreatedBy().getName());
                    }
                }
                view.setBankId(entity.getBankIdFrom().getId());
                view.setValue(entity.getValue());
                view.setValueLocal(entity.getValueLocal());
                view.setRemark(entity.getRemarks());
                if (entity.getBankIdFrom() != null) {
                    view.setName(entity.getBankIdFrom().getName());
                    view.setCode(entity.getBankIdFrom().getCode());
                    if (entity.getBankIdFrom().getCurrencyId() != null) {
                        view.setCurrencyCode(entity.getBankIdFrom().getCurrencyId().getCode());
                        view.setCurrencyName(entity.getBankIdFrom().getCurrencyId().getName());
                    }
                }
                view.setStatus(0);
                view.setTypeId(3);
                view.setTypeName("تحويل صادر");
                view.setOrdered(4);
            } else if (type == 1) {
                GlBankTransactionDetail entity = (GlBankTransactionDetail) Entity;
                view.setRowNum(index);
                if (entity.getGlBankTransactionId() != null) {
                    view.setBanktransactionId(entity.getGlBankTransactionId().getId());
                    view.setType(entity.getGlBankTransactionId().getTransactionType());
                    view.setPaymentType(entity.getGlBankTransactionId().getPaymentType());
                    view.setDate(entity.getGlBankTransactionId().getDate());
                    view.setSerial(new BigInteger(entity.getGlBankTransactionId().getSerial().toString()));
                    view.setRemark2(entity.getGlBankTransactionId().getRemark2());

                    if (entity.getGlBankTransactionId().getCreatedBy() != null) {
                        view.setCreatedBy(entity.getGlBankTransactionId().getCreatedBy().getName());
                    }
                }
                view.setBankId(entity.getBankIdTo().getId());
                view.setValue((entity.getValue().multiply(entity.getRateBankFrom() != null ? entity.getRateBankFrom() : BigDecimal.ONE)).divide(entity.getRateBankTo() != null ? entity.getRateBankTo() : BigDecimal.ONE));
                view.setValueLocal(entity.getValueLocal());
                view.setRemark(entity.getRemarks());
                if (entity.getBankIdTo() != null) {
                    view.setName(entity.getBankIdTo().getName());
                    view.setCode(entity.getBankIdTo().getCode());
                    if (entity.getBankIdTo().getCurrencyId() != null) {
                        view.setCurrencyCode(entity.getBankIdTo().getCurrencyId().getCode());
                        view.setCurrencyName(entity.getBankIdTo().getCurrencyId().getName());
                    }
                }
                view.setStatus(1);
                view.setTypeId(4);
                view.setTypeName("تحويل وارد");
                view.setOrdered(3);
            }
        } else if (Entity.getClass().getSimpleName().equals("Object[]")) {
            Object[] _Entity = (Object[]) Entity;
            InvPurchaseInvoiceDetail entity = (InvPurchaseInvoiceDetail) _Entity[0];
            entity.setSumDetailNet((BigDecimal) _Entity[_Entity.length - 1]);
            InvInvoiceDTO invInvoiceDTO = invPurchaseInvoiceService_1.returnInvInvoiceSummarySumation(entity.getInvPurchaseInvoiceId(), entity.getSumDetailNet());
            view.setRowNum(index);
            if (entity.getCreatedBy() != null) {
                view.setCreatedBy(entity.getCreatedBy().getName());
            }
            if (entity.getInvPurchaseInvoiceId() != null) {
                if (entity.getInvPurchaseInvoiceId().getGlBankId() != null) {
                    view.setBankId(entity.getInvPurchaseInvoiceId().getGlBankId().getId());

                }
                view.setType(entity.getInvPurchaseInvoiceId().getType());
                view.setPaymentType(entity.getInvPurchaseInvoiceId().getPaymentType());
                view.setDate(entity.getInvPurchaseInvoiceId().getDate());
                view.setSerial(new BigInteger(entity.getInvPurchaseInvoiceId().getSerial() != null ? entity.getInvPurchaseInvoiceId().getSerial().toString() : "0"));
                if (entity.getInvPurchaseInvoiceId().getGlBankId() != null) {
                    view.setName(entity.getInvPurchaseInvoiceId().getGlBankId().getName());
                    view.setCode(entity.getInvPurchaseInvoiceId().getGlBankId().getCode());
                }
                if (entity.getInvPurchaseInvoiceId().getCurrencyId() != null) {
                    view.setCurrencyName(entity.getInvPurchaseInvoiceId().getCurrencyId().getName());
                    view.setCurrencyCode(entity.getInvPurchaseInvoiceId().getCurrencyId().getCode());
                }
                view.setStatus(entity.getInvPurchaseInvoiceId().getType());
                view.setBanktransactionId(entity.getInvPurchaseInvoiceId().getId());
            }
            view.setValue(invInvoiceDTO.getFinalNet());
            view.setValueLocal(invInvoiceDTO.getFinalNet().multiply(invInvoiceDTO.getRate()));

            view.setRemark(entity.getInvPurchaseInvoiceId().getRemarks());
            if (entity.getInvPurchaseInvoiceId().getType() == 0) {
                view.setTypeId(5);
                view.setTypeName("فاتورة مشتريات");
                view.setOrdered(6);
            } else if (entity.getInvPurchaseInvoiceId().getType() == 1) {
                view.setTypeId(6);
                view.setTypeName("فاتورة مبيعات");
                view.setOrdered(5);
            } else if (entity.getInvPurchaseInvoiceId().getType() == 2) {
                view.setTypeId(7);
                view.setTypeName("مردود مشتريات");
                view.setOrdered(7);
            } else if (entity.getInvPurchaseInvoiceId().getType() == 3) {
                view.setTypeId(8);
                view.setTypeName("مردود مبيعات");
                view.setOrdered(8);
            }
        }

        return view;
    }

}
