/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.GlBankService;
import com.isag.businessservice.GlBankTransactionService;
import com.isag.businessservice.InvPurchaseInvoiceService_1;
import com.isag.businessservice.reports.searchBean.SubAccountSummarySearchBean;
import com.isag.comparatorsVars.CashAccountSettlmentComparator1;
import com.isag.comparatorsVars.CashAccountSettlmentViewComparator;
import com.isag.core.GenericDAO;
import com.isag.dto.GlBankDTO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.GlBank;
import com.isag.entity.GlBankTransaction;
import com.isag.entity.GlBankTransactionDetail;
import com.isag.entity.GlYear;
import com.isag.entity.InvPurchaseInvoiceDetail;
import com.isag.entity.InvReturnPurchase;
import com.isag.views.CashAccountSettlementBean;
import com.isag.views.CashAccountSettlment;
import com.isag.views.CashAccountSettlmentView;
import com.isag.views.NetView;
import com.isag.views.SubAccountSummaryReport;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
    @EJB
    private GlBankTransactionService glBankTransactionService;
    @EJB
    private GlBankService glBankService;

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
        if (cashAccountSettlementSearchBean.getGlBankFrom() != null && cashAccountSettlementSearchBean.getGlBankFrom().getId() > 0) {
            builder.append(" and g.id >= :glBankFrom");
            params.put("glBankFrom", cashAccountSettlementSearchBean.getGlBankFrom().getId());
        }

        if (cashAccountSettlementSearchBean.getGlBankTo() != null && cashAccountSettlementSearchBean.getGlBankTo().getId() > 0) {
            builder.append(" and g.id <= :glBankTo");
            params.put("glBankTo", cashAccountSettlementSearchBean.getGlBankTo().getId());
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

        if (cashAccountSettlementSearchBean.getIsagUserFrom() != null) {
            builder.append(" and g.createdBy.name >= :createdByFrom");
            params.put("createdByFrom", cashAccountSettlementSearchBean.getIsagUserFrom());
        }

        if (cashAccountSettlementSearchBean.getIsagUserTo() != null) {
            builder.append(" and g.createdBy.name <= :createdByTo");
            params.put("createdByTo", cashAccountSettlementSearchBean.getIsagUserTo());
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

        if (cashAccountSettlementSearchBean.getIsagUserFrom() != null) {
            builder.append(" and g.createdBy.name >= :createdByFrom");
            params.put("createdByFrom", cashAccountSettlementSearchBean.getIsagUserFrom());
        }

        if (cashAccountSettlementSearchBean.getIsagUserTo() != null) {
            builder.append(" and g.createdBy.name <= :createdByTo");
            params.put("createdByTo", cashAccountSettlementSearchBean.getIsagUserTo());
        }
//        if (cashAccountSettlementSearchBean.getOrderBy() == 0) {
//            builder.append(" order by g.glBankId.id  asc , g.date  ");
//        } else if (cashAccountSettlementSearchBean.getOrderBy() == 1 /*&& getTransactionType() == 1*/) {
//            builder.append(" order by g.transactionType desc , g.value asc ,  g.glBankId.id asc ,g.date ");
//        }

        List<GlBankTransaction> glBankTransactionList = dao.findListByQuery(builder.toString(), params);
        return returnListView(glBankTransactionList, 0);
    }

    private List<CashAccountSettlmentView> findReceiptWithDocumentSerial(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
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
        if (cashAccountSettlementSearchBean.getFromDocumentSerial() == null && cashAccountSettlementSearchBean.getToDocumentSerial() == null) {
            builder.append(" and g.documentSerial is null");
        } else {
            if (cashAccountSettlementSearchBean.getFromDocumentSerial() != null) {
                builder.append(" and g.documentSerial >= :documentSerialFrom");
                params.put("documentSerialFrom", cashAccountSettlementSearchBean.getFromDocumentSerial());
            }

            if (cashAccountSettlementSearchBean.getToDocumentSerial() != null) {
                builder.append(" and g.documentSerial <= :documentSerialTo");
                params.put("documentSerialTo", cashAccountSettlementSearchBean.getToDocumentSerial());
            }
        }
        if (cashAccountSettlementSearchBean.getIsagUserFrom() != null) {
            builder.append(" and g.createdBy.name >= :createdByFrom");
            params.put("createdByFrom", cashAccountSettlementSearchBean.getIsagUserFrom());
        }

        if (cashAccountSettlementSearchBean.getIsagUserTo() != null) {
            builder.append(" and g.createdBy.name <= :createdByTo");
            params.put("createdByTo", cashAccountSettlementSearchBean.getIsagUserTo());
        }
//        if (cashAccountSettlementSearchBean.getOrderBy() == 0) {
//            builder.append(" order by g.glBankId.id  asc , g.date  ");
//        } else if (cashAccountSettlementSearchBean.getOrderBy() == 1 /*&& getTransactionType() == 1*/) {
//            builder.append(" order by g.transactionType desc , g.value asc ,  g.glBankId.id asc ,g.date ");
//        }

        List<GlBankTransaction> glBankTransactionList = dao.findListByQuery(builder.toString(), params);
        return returnListView(glBankTransactionList, 0);
    }

    @Override
    public List<Object[]> findDateWithDocumentSerial(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT  g.documentSerial , max(g.date) , min(g.date) FROM GlBankTransaction g WHERE g.branchId.id = :branchId AND g.transactionType <> 2 AND  g.transactionType <> 3 ");
        if (cashAccountSettlementSearchBean.getGlBankFrom() != null && cashAccountSettlementSearchBean.getGlBankFrom().getId() > 0) {
            builder.append(" and g.glBankId.id >= :glBankFrom");
            params.put("glBankFrom", cashAccountSettlementSearchBean.getGlBankFrom().getId());
        }

        if (cashAccountSettlementSearchBean.getGlBankTo() != null && cashAccountSettlementSearchBean.getGlBankTo().getId() > 0) {
            builder.append(" and g.glBankId.id <= :glBankTo");
            params.put("glBankTo", cashAccountSettlementSearchBean.getGlBankTo().getId());
        }

        if (cashAccountSettlementSearchBean.getFromDocumentSerial() == null && cashAccountSettlementSearchBean.getToDocumentSerial() == null) {
            builder.append(" and g.documentSerial is null");
        } else {
            if (cashAccountSettlementSearchBean.getFromDocumentSerial() != null) {
                builder.append(" and g.documentSerial >= :documentSerialFrom");
                params.put("documentSerialFrom", cashAccountSettlementSearchBean.getFromDocumentSerial());
            }

            if (cashAccountSettlementSearchBean.getToDocumentSerial() != null) {
                builder.append(" and g.documentSerial <= :documentSerialTo");
                params.put("documentSerialTo", cashAccountSettlementSearchBean.getToDocumentSerial());
            }
        }

        builder.append(" group by g.documentSerial ");

        List<Object[]> glBankTransactionList = dao.findListByQuery(builder.toString(), params);
        return glBankTransactionList;
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

        if (cashAccountSettlementSearchBean.getIsagUserFrom() != null) {
            builder.append(" and g.glBankTransactionId.createdBy.name >= :createdByFrom");
            params.put("createdByFrom", cashAccountSettlementSearchBean.getIsagUserFrom());
        }

        if (cashAccountSettlementSearchBean.getIsagUserTo() != null) {
            builder.append(" and g.glBankTransactionId.createdBy.name <= :createdByTo");
            params.put("createdByTo", cashAccountSettlementSearchBean.getIsagUserTo());
        }
//        if (cashAccountSettlementSearchBean.getOrderBy() == 0) {
//            builder.append(" order by g.bankIdFrom.id  asc , g.glBankTransactionId.date  ");
//        }
        List<GlBankTransactionDetail> glBankTransactionDetailList = dao.findListByQuery(builder.toString(), params);
        return returnListView(glBankTransactionDetailList, 0);
    }

    private List<CashAccountSettlmentView> findTransferfromWithDocumentSerial(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
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

        if (cashAccountSettlementSearchBean.getFromDocumentSerial() == null && cashAccountSettlementSearchBean.getToDocumentSerial() == null) {
            builder.append(" and g.glBankTransactionId.documentSerial is null");
        } else {
            if (cashAccountSettlementSearchBean.getFromDocumentSerial() != null) {
                builder.append(" and g.glBankTransactionId.documentSerial >= :documentSerialFrom");
                params.put("documentSerialFrom", cashAccountSettlementSearchBean.getFromDocumentSerial());
            }

            if (cashAccountSettlementSearchBean.getToDocumentSerial() != null) {
                builder.append(" and g.glBankTransactionId.documentSerial <= :documentSerialTo");
                params.put("documentSerialTo", cashAccountSettlementSearchBean.getToDocumentSerial());
            }
        }

        if (cashAccountSettlementSearchBean.getIsagUserFrom() != null) {
            builder.append(" and g.glBankTransactionId.createdBy.name >= :createdByFrom");
            params.put("createdByFrom", cashAccountSettlementSearchBean.getIsagUserFrom());
        }

        if (cashAccountSettlementSearchBean.getIsagUserTo() != null) {
            builder.append(" and g.glBankTransactionId.createdBy.name <= :createdByTo");
            params.put("createdByTo", cashAccountSettlementSearchBean.getIsagUserTo());
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

        if (cashAccountSettlementSearchBean.getIsagUserFrom() != null) {
            builder.append(" and g.glBankTransactionId.createdBy.name >= :createdByFrom");
            params.put("createdByFrom", cashAccountSettlementSearchBean.getIsagUserFrom());
        }

        if (cashAccountSettlementSearchBean.getIsagUserTo() != null) {
            builder.append(" and g.glBankTransactionId.createdBy.name <= :createdByTo");
            params.put("createdByTo", cashAccountSettlementSearchBean.getIsagUserTo());
        }
//        if (cashAccountSettlementSearchBean.getOrderBy() == 0) {
//            builder.append(" order by g.bankIdTo.id  asc , g.glBankTransactionId.date  ");
//        }
        List<GlBankTransactionDetail> glBankTransactionDetailList = dao.findListByQuery(builder.toString(), params);
        return returnListView(glBankTransactionDetailList, 1);
    }

    private List<CashAccountSettlmentView> findTransfertoWithDocumentSerial(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
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
        if (cashAccountSettlementSearchBean.getFromDocumentSerial() == null && cashAccountSettlementSearchBean.getToDocumentSerial() == null) {
            builder.append(" and g.glBankTransactionId.documentSerial is null");
        } else {
            if (cashAccountSettlementSearchBean.getFromDocumentSerial() != null) {
                builder.append(" and g.glBankTransactionId.documentSerial >= :documentSerialFrom");
                params.put("documentSerialFrom", cashAccountSettlementSearchBean.getFromDocumentSerial());
            }

            if (cashAccountSettlementSearchBean.getToDocumentSerial() != null) {
                builder.append(" and g.glBankTransactionId.documentSerial <= :documentSerialTo");
                params.put("documentSerialTo", cashAccountSettlementSearchBean.getToDocumentSerial());
            }
        }

        if (cashAccountSettlementSearchBean.getIsagUserFrom() != null) {
            builder.append(" and g.glBankTransactionId.createdBy.name >= :createdByFrom");
            params.put("createdByFrom", cashAccountSettlementSearchBean.getIsagUserFrom());
        }

        if (cashAccountSettlementSearchBean.getIsagUserTo() != null) {
            builder.append(" and g.glBankTransactionId.createdBy.name <= :createdByTo");
            params.put("createdByTo", cashAccountSettlementSearchBean.getIsagUserTo());
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

        if (cashAccountSettlementSearchBean.getIsagUserFrom() != null) {
            builder.append(" and g.invPurchaseInvoiceId.createdBy.name >= :createdByFrom");
            params.put("createdByFrom", cashAccountSettlementSearchBean.getIsagUserFrom());
        }

        if (cashAccountSettlementSearchBean.getIsagUserTo() != null) {
            builder.append(" and g.invPurchaseInvoiceId.createdBy.name <= :createdByTo");
            params.put("createdByTo", cashAccountSettlementSearchBean.getIsagUserTo());
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

        if (cashAccountSettlementSearchBean.getIsagUserFrom() != null) {
            builder.append(" and g.createdBy.name >= :createdByFrom");
            params.put("createdByFrom", cashAccountSettlementSearchBean.getIsagUserFrom());
        }

        if (cashAccountSettlementSearchBean.getIsagUserTo() != null) {
            builder.append(" and g.createdBy.name <= :createdByTo");
            params.put("createdByTo", cashAccountSettlementSearchBean.getIsagUserTo());
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

            Collections.sort(accountSettlmentViews, compare);
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

            Collections.sort(accountSettlmentViews, compare);
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

            Collections.sort(accountSettlmentViews, compare);
        }

        return accountSettlmentViews;

    }

    @Override
    public List<CashAccountSettlmentView> findAllCashAccountSettlmentWithDocumentSerial(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        List<CashAccountSettlmentView> accountSettlmentViews = new ArrayList<>();
        if (cashAccountSettlementSearchBean.getTransactionType() != null && cashAccountSettlementSearchBean.getTransactionType() == 0) {
            accountSettlmentViews.addAll(findOpenBalance(cashAccountSettlementSearchBean));
        } else if (cashAccountSettlementSearchBean.getTransactionType() != null && (cashAccountSettlementSearchBean.getTransactionType() == 1 || cashAccountSettlementSearchBean.getTransactionType() == 2)) {
            accountSettlmentViews.addAll(findReceiptWithDocumentSerial(cashAccountSettlementSearchBean));
        } else if (cashAccountSettlementSearchBean.getTransactionType() != null && cashAccountSettlementSearchBean.getTransactionType() == 3) {
            accountSettlmentViews.addAll(findTransferfromWithDocumentSerial(cashAccountSettlementSearchBean));
        } else if (cashAccountSettlementSearchBean.getTransactionType() != null && cashAccountSettlementSearchBean.getTransactionType() == 4) {
            accountSettlmentViews.addAll(findTransfertoWithDocumentSerial(cashAccountSettlementSearchBean));
        } else if (cashAccountSettlementSearchBean.getTransactionType() != null && (cashAccountSettlementSearchBean.getTransactionType() == 5 || cashAccountSettlementSearchBean.getTransactionType() == 6 || cashAccountSettlementSearchBean.getTransactionType() == 7 || cashAccountSettlementSearchBean.getTransactionType() == 8)) {
            accountSettlmentViews.addAll(findSales(cashAccountSettlementSearchBean));
//        } else if (cashAccountSettlementSearchBean.getTransactionType() != null && (cashAccountSettlementSearchBean.getTransactionType() == 7 || cashAccountSettlementSearchBean.getTransactionType() == 8)) {
//         //   accountSettlmentViews.addAll(findReturnSales(cashAccountSettlementSearchBean));
        } else {
            Boolean documentSerialIsExist = glBankTransactionService.checkDocumentSerial(cashAccountSettlementSearchBean.getBranchId(), cashAccountSettlementSearchBean.getGlBankFrom().getId());
            if (!documentSerialIsExist) {
                accountSettlmentViews.addAll(findOpenBalance(cashAccountSettlementSearchBean));
            }
            accountSettlmentViews.addAll(findReceiptWithDocumentSerial(cashAccountSettlementSearchBean));
            accountSettlmentViews.addAll(findTransferfromWithDocumentSerial(cashAccountSettlementSearchBean));
            accountSettlmentViews.addAll(findTransfertoWithDocumentSerial(cashAccountSettlementSearchBean));
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
        List<CashAccountSettlmentView> cashAccountSettlementList = dao.findListByQuery(sql, params);

        return cashAccountSettlementList;
    }

    @Override
    public CashAccountSettlementBean search(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        CashAccountSettlementBean cashAccountSettlementBean = new CashAccountSettlementBean();
        cashAccountSettlementBean.setGlBankCopyMap(new HashMap<>());
        if (cashAccountSettlementSearchBean.getGlBankDTOListSelected() != null && !cashAccountSettlementSearchBean.getGlBankDTOListSelected().isEmpty()) {
            for (GlBankDTO gb : cashAccountSettlementSearchBean.getGlBankDTOListSelected()) {
                cashAccountSettlementBean.getGlBankMap().put(gb.getId(), gb);
                cashAccountSettlementBean.getTotalForDebitMap().put(gb.getId(), BigDecimal.ZERO);
                cashAccountSettlementBean.getTotalForCreditMap().put(gb.getId(), BigDecimal.ZERO);
                cashAccountSettlementBean.getTotalForBalanceMap().put(gb.getId(), BigDecimal.ZERO);
                cashAccountSettlementBean.getOpeningBalanceMap().put(gb.getId(), BigDecimal.ZERO);
                cashAccountSettlementBean.getGlBankCopyMap().put(gb.getId(), gb);
                System.out.println("Key = " + gb.getId());
            }
        }

        cashAccountSettlementBean.setCashAccountSettlementReports(new ArrayList<>());
        cashAccountSettlementBean.setCashAccountSettlementOpeningBalanceList(new ArrayList<>());
        cashAccountSettlementBean.setCashAccountSettlementList(findAllCashAccountSettlment2(cashAccountSettlementSearchBean)); // here
        if (cashAccountSettlementSearchBean.getOrderBy() == 1) {
            cashAccountSettlementBean.getCashAccountSettlementList().sort(new CashAccountSettlmentViewComparator());
        }

        cashAccountSettlementBean.setTotalDebit(BigDecimal.ZERO);
        cashAccountSettlementBean.setTotalCredit(BigDecimal.ZERO);

        BigDecimal debit = BigDecimal.ZERO;
        BigDecimal credit = BigDecimal.ZERO;
        BigDecimal balance = BigDecimal.ZERO;
        SubAccountSummaryReport cashAccountSettlment;
        cashAccountSettlementBean.setDistinctBankId(0);
        for (CashAccountSettlmentView cashAccountSettlmentView : cashAccountSettlementBean.getCashAccountSettlementList()) {

            cashAccountSettlment = new SubAccountSummaryReport();
            cashAccountSettlment.setId(cashAccountSettlmentView.getBanktransactionId());
            cashAccountSettlment.setBankId(cashAccountSettlmentView.getBankId());
            cashAccountSettlment.setBankName(cashAccountSettlmentView.getName());
            cashAccountSettlment.setDate(cashAccountSettlmentView.getDate() != null ? cashAccountSettlmentView.getDate() : null);
            cashAccountSettlment.setRemark(cashAccountSettlmentView.getRemark() != null ? cashAccountSettlmentView.getRemark() : null);
            cashAccountSettlment.setRemark2(cashAccountSettlmentView.getRemark2() != null ? cashAccountSettlmentView.getRemark2() : null);
            cashAccountSettlment.setCode(cashAccountSettlmentView.getCode() != null ? cashAccountSettlmentView.getCode() : null);
            cashAccountSettlment.setCurrencyName(cashAccountSettlmentView.getCurrencyName() != null ? cashAccountSettlmentView.getCurrencyName() : null);
            cashAccountSettlment.setCurrencyCode(cashAccountSettlmentView.getCurrencyCode() != null ? cashAccountSettlmentView.getCurrencyCode() : null);
            cashAccountSettlment.setSerial(cashAccountSettlmentView.getSerial() != null ? cashAccountSettlmentView.getSerial() + "" : null);
            cashAccountSettlment.setTransactionTypeNumber(cashAccountSettlmentView.getTypeId());
            cashAccountSettlment.setCreatedBy(cashAccountSettlmentView.getCreatedBy());

            if (cashAccountSettlmentView.getStatus() != null) {
                if (cashAccountSettlementSearchBean.getCurrencyType() == 1) {

                    if (cashAccountSettlmentView.getStatus() == 1) {
                        cashAccountSettlment.setDebitAmount(cashAccountSettlmentView.getValue().setScale(2, RoundingMode.UP));
                        cashAccountSettlment.setTransactionType(cashAccountSettlmentView.getTypeName());

                    } else if (cashAccountSettlmentView.getStatus() == 0) {
                        cashAccountSettlment.setCreditAmount(cashAccountSettlmentView.getValue().setScale(2, RoundingMode.UP));
                        cashAccountSettlment.setTransactionType(cashAccountSettlmentView.getTypeName());

                    }
                } else {
                    if (cashAccountSettlmentView.getStatus() == 1) {
                        cashAccountSettlment.setDebitAmount(cashAccountSettlmentView.getValueLocal().setScale(2, RoundingMode.UP));
                        cashAccountSettlment.setTransactionType(cashAccountSettlmentView.getTypeName());
                    } else if (cashAccountSettlmentView.getStatus() == 0) {
                        cashAccountSettlment.setCreditAmount(cashAccountSettlmentView.getValueLocal().setScale(2, RoundingMode.UP));
                        cashAccountSettlment.setTransactionType(cashAccountSettlmentView.getTypeName());

                    }
                }
            }
            addOpeningBalance2(cashAccountSettlment, cashAccountSettlementBean, cashAccountSettlementSearchBean);
            debit = cashAccountSettlment.getDebitAmount() != null ? cashAccountSettlment.getDebitAmount().setScale(2, RoundingMode.UP) : BigDecimal.ZERO;
            credit = cashAccountSettlment.getCreditAmount() != null ? cashAccountSettlment.getCreditAmount().setScale(2, RoundingMode.UP) : BigDecimal.ZERO;
            balance = cashAccountSettlementBean.getTotalForBalanceMap().get(cashAccountSettlment.getBankId());
            if (balance == null) {
                balance = BigDecimal.ZERO;
            }
            balance = balance.add(debit.subtract(credit));
            cashAccountSettlementBean.getTotalForBalanceMap().put(cashAccountSettlment.getBankId(), balance.setScale(2, RoundingMode.UP));
            cashAccountSettlment.setBalance(balance.setScale(2, RoundingMode.UP));

            fillDebitAndCreditTotals(cashAccountSettlment, cashAccountSettlementBean, debit, credit);
            cashAccountSettlementBean.getCashAccountSettlementReports().add(cashAccountSettlment);

        }
        calculateExceedDateOpeningBalance(cashAccountSettlementBean, cashAccountSettlementSearchBean);
        if (cashAccountSettlementBean.getCashAccountSettlementReports() != null && !cashAccountSettlementBean.getCashAccountSettlementReports().isEmpty()) {
            for (SubAccountSummaryReport cash : cashAccountSettlementBean.getCashAccountSettlementReports()) {
                cash.setTotalCredit(cashAccountSettlementBean.getTotalForCreditMap().get(cash.getBankId()).setScale(2, RoundingMode.UP));
                cash.setTotalDebit(cashAccountSettlementBean.getTotalForDebitMap().get(cash.getBankId()).setScale(2, RoundingMode.UP));
                cash.setOpeningBalance(cashAccountSettlementBean.getOpeningBalanceMap().get(cash.getBankId()).setScale(2, RoundingMode.UP));
            }
        }
        cashAccountSettlementBean.setCashAccountSettlementList(null);
        cashAccountSettlementBean.setOpeningBalanceMap(null);
        cashAccountSettlementBean.setTotalForBalanceMap(null);
        cashAccountSettlementBean.setTotalForCreditMap(null);
        cashAccountSettlementBean.setTotalForDebitMap(null);
        return cashAccountSettlementBean;
    }

    public void addOpeningBalance2(SubAccountSummaryReport cashAccountSettlment, CashAccountSettlementBean cashAccountSettlementBean,
            SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        cashAccountSettlementBean.setOpeningBalance(BigDecimal.ZERO);
        if (cashAccountSettlment.getBankId() != null && cashAccountSettlementBean.getDistinctBankId().compareTo(cashAccountSettlment.getBankId()) != 0) {
            cashAccountSettlementBean.setDistinctBankId(cashAccountSettlment.getBankId());
            BigDecimal debitOpenBalance = findOpeningBalancesForSpecificCashAccount2(cashAccountSettlementSearchBean, cashAccountSettlment.getBankId(), 1);
            BigDecimal creditOpenBalance = findOpeningBalancesForSpecificCashAccount2(cashAccountSettlementSearchBean, cashAccountSettlment.getBankId(), 0);
            cashAccountSettlementBean.setOpeningBalance((debitOpenBalance != null ? debitOpenBalance : BigDecimal.ZERO).subtract(creditOpenBalance != null ? creditOpenBalance : BigDecimal.ZERO));
            cashAccountSettlementBean.getOpeningBalanceMap().put(cashAccountSettlment.getBankId(), cashAccountSettlementBean.getOpeningBalance().setScale(2, RoundingMode.UP));
            /*if (openingBalance.signum() == -1 && openingBalance.compareTo(BigDecimal.ZERO) != 0) {
             cashAccountSettlment.setCreditAmount(cashAccountSettlment.getCreditAmount() != null ? cashAccountSettlment.getCreditAmount() : BigDecimal.ZERO);
             } else if ((openingBalance.signum() == 1 && openingBalance.compareTo(BigDecimal.ZERO) != 0)) {
             cashAccountSettlment.setDebitAmount(cashAccountSettlment.getDebitAmount() != null ? cashAccountSettlment.getDebitAmount() : BigDecimal.ZERO);
             }*/
            BigDecimal bala = cashAccountSettlementBean.getTotalForBalanceMap().get(cashAccountSettlment.getBankId());
            if (bala == null) {
                bala = BigDecimal.ZERO;
            }
            bala = bala.add(cashAccountSettlementBean.getOpeningBalance());
            cashAccountSettlementBean.getTotalForBalanceMap().put(cashAccountSettlment.getBankId(), bala.setScale(2, RoundingMode.UP));
        }
    }

    public void fillDebitAndCreditTotals(SubAccountSummaryReport cashAccountSettlment, CashAccountSettlementBean cashAccountSettlementBean,
            BigDecimal debit, BigDecimal credit) {
        BigDecimal catchDebit = cashAccountSettlementBean.getTotalForDebitMap().get(cashAccountSettlment.getBankId());
        BigDecimal catchCredit = cashAccountSettlementBean.getTotalForCreditMap().get(cashAccountSettlment.getBankId());
        if (catchDebit == null) {
            catchDebit = BigDecimal.ZERO;
        }
        if (catchCredit == null) {
            catchCredit = BigDecimal.ZERO;
        }
        catchDebit = catchDebit.add(debit);
        catchCredit = catchCredit.add(credit);
        cashAccountSettlementBean.getTotalForDebitMap().put(cashAccountSettlment.getBankId(), catchDebit.setScale(2, RoundingMode.UP));
        cashAccountSettlementBean.getTotalForCreditMap().put(cashAccountSettlment.getBankId(), catchCredit.setScale(2, RoundingMode.UP));
    }

    public void calculateExceedDateOpeningBalance(CashAccountSettlementBean cashAccountSettlementBean, SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        cashAccountSettlementBean.setBuilder(new StringBuilder());
        for (Integer key : cashAccountSettlementBean.getGlBankCopyMap().keySet()) {
            if (cashAccountSettlementBean.getBuilder().length() == 0 && cashAccountSettlementBean.getBuilder() != null) {
                cashAccountSettlementBean.getBuilder().append(key);
            } else {
                cashAccountSettlementBean.getBuilder().append(",").append(key);
            }
        }
        if (cashAccountSettlementBean.getBuilder().length() != 0 && cashAccountSettlementBean.getBuilder() != null) {
            List<GlBankDTO> glBankList = glBankService.findGlBankDTOList(cashAccountSettlementBean.getBuilder());

            if (glBankList != null && !glBankList.isEmpty()) {

                for (GlBankDTO bankDTO : glBankList) {
                    BigDecimal debitOpenBalance = findOpeningBalancesForSpecificCashAccount(cashAccountSettlementSearchBean, bankDTO.getId(), 1);
                    BigDecimal creditOpenBalance = findOpeningBalancesForSpecificCashAccount(cashAccountSettlementSearchBean, bankDTO.getId(), 0);
                    cashAccountSettlementBean.setOpeningBalance((debitOpenBalance != null ? debitOpenBalance : BigDecimal.ZERO).subtract(creditOpenBalance != null ? creditOpenBalance : BigDecimal.ZERO));
                    cashAccountSettlementBean.getOpeningBalanceMap().put(bankDTO.getId(), cashAccountSettlementBean.getOpeningBalance().setScale(2, RoundingMode.UP));
                    cashAccountSettlementBean.getTotalForBalanceMap().put(bankDTO.getId(), cashAccountSettlementBean.getOpeningBalance().setScale(2, RoundingMode.UP));
                    if (cashAccountSettlementBean.getOpeningBalance().compareTo(BigDecimal.ZERO) == 1) {
                        SubAccountSummaryReport summaryReport = new SubAccountSummaryReport();
                        summaryReport.setBankId(bankDTO.getId());
                        summaryReport.setBankName(bankDTO.getName());
                        summaryReport.setCurrencyCode(bankDTO.getCurrency().getCode());
                        summaryReport.setCurrencyName(bankDTO.getCurrency().getName());
                        summaryReport.setCode(bankDTO.getCode());
                        summaryReport.setBalance(cashAccountSettlementBean.getOpeningBalance().setScale(2, RoundingMode.UP));
                        cashAccountSettlementBean.getCashAccountSettlementReports().add(summaryReport);
                    }
                }
            }
        }
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
    public BigDecimal findOpeningBalancesForSpecificCashAccount2(SubAccountSummarySearchBean cashAccountSettlementSearchBean, Integer bankId, Integer type) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        params.put("bankId", bankId);
        params.put("type", type);
        StringBuilder builder = new StringBuilder();
        String sql = "SELECT sum(COALESCE(cas.valueLocal,0)) FROM CashAccountSettlmentView cas WHERE cas.branchId = :branchId and cas.status = :type and cas.bankId= :bankId";
        builder.append(sql);
        addDateFromAndToForOpeningBalance(builder, cashAccountSettlementSearchBean);
        BigDecimal value = dao.findEntityByQuery(builder.toString(), params);
        return value;
    }

    private BigDecimal openinngBalance(SubAccountSummarySearchBean cashAccountSettlementSearchBean, Integer bankId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT g.openinngBalance FROM GlBank g WHERE g.branchId.id = :branchId AND g.dateOpeninngBalance is not null AND g.openinngBalance > 0 ");
        builder.append(" and g.id = :glBankFrom");
        params.put("glBankFrom", bankId);

        BigDecimal openinngBalance = dao.findEntityByQuery(builder.toString(), params);
        return openinngBalance == null ? BigDecimal.ZERO : openinngBalance;
    }

    private BigDecimal glbankTransaction(SubAccountSummarySearchBean cashAccountSettlementSearchBean, Integer bankId) {
        Map<String, Object> paramsGlbankTrans = new HashMap();
        paramsGlbankTrans.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        StringBuilder builder2 = new StringBuilder();
        builder2.append("SELECT sum(COALESCE(g.valueLocal,0)) FROM GlBankTransaction g WHERE g.branchId.id = :branchId AND g.transactionType <> 2 AND  g.transactionType <> 3 ");
        builder2.append(" and g.glBankId.id = :glBankFrom");
        paramsGlbankTrans.put("glBankFrom", bankId);
//        String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateFrom());
//        builder2.append(" and g.date = '").append(d3).append("'");
        builder2.append(" and g.transactionType = 0");
        builder2.append(" and g.documentSerial is not null");
        BigDecimal receipt = dao.findEntityByQuery(builder2.toString(), paramsGlbankTrans);
        return receipt == null ? BigDecimal.ZERO : receipt;
    }

    private BigDecimal glbankTransactionCash(SubAccountSummarySearchBean cashAccountSettlementSearchBean, Integer bankId) {
        Map<String, Object> paramsCash = new HashMap();
        paramsCash.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        StringBuilder builder3 = new StringBuilder();
        builder3.append("SELECT sum(COALESCE(g.valueLocal,0)) FROM GlBankTransaction g WHERE g.branchId.id = :branchId AND g.transactionType <> 2 AND  g.transactionType <> 3 ");
        builder3.append(" and g.glBankId.id = :glBankFrom");
        paramsCash.put("glBankFrom", bankId);
//        String d4 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateFrom());
//        builder3.append(" and g.date = '").append(d4).append("'");
        builder3.append(" and g.transactionType = 1");
        builder3.append(" and g.documentSerial is not null");
        BigDecimal Cash = dao.findEntityByQuery(builder3.toString(), paramsCash);

        return Cash == null ? BigDecimal.ZERO : Cash;
    }

    private BigDecimal transferfrom(SubAccountSummarySearchBean cashAccountSettlementSearchBean, Integer bankId) {
        Map<String, Object> paramsTransferfrom = new HashMap();
        paramsTransferfrom.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        StringBuilder builder4 = new StringBuilder();
        builder4.append("SELECT sum(COALESCE(g.valueLocal,0)) FROM GlBankTransactionDetail g WHERE g.glBankTransactionId.branchId.id = :branchId AND g.glBankTransactionId.transactionType = 2 ");
        builder4.append(" and g.bankIdFrom.id = :glBankFrom");
        paramsTransferfrom.put("glBankFrom", bankId);
//        String d5 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateFrom());
//        builder4.append(" and g.glBankTransactionId.date = '").append(d5).append("'");
        builder4.append(" and g.glBankTransactionId.documentSerial is not null");

        BigDecimal transferfrom = dao.findEntityByQuery(builder4.toString(), paramsTransferfrom);

        return transferfrom == null ? BigDecimal.ZERO : transferfrom;
    }

    private BigDecimal transferTo(SubAccountSummarySearchBean cashAccountSettlementSearchBean, Integer bankId) {
        Map<String, Object> paramsTransferfrom = new HashMap();
        paramsTransferfrom.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        StringBuilder builder4 = new StringBuilder();
        builder4.append("SELECT sum(COALESCE(g.valueLocal,0)) FROM GlBankTransactionDetail g WHERE g.glBankTransactionId.branchId.id = :branchId AND g.glBankTransactionId.transactionType = 2 ");
        builder4.append(" and g.bankIdTo.id = :glBankFrom");
        paramsTransferfrom.put("glBankFrom", bankId);
//        String d5 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateFrom());
//        builder4.append(" and g.glBankTransactionId.date = '").append(d5).append("'");
        builder4.append(" and g.glBankTransactionId.documentSerial is not null");

        BigDecimal transferTo = dao.findEntityByQuery(builder4.toString(), paramsTransferfrom);

        return transferTo == null ? BigDecimal.ZERO : transferTo;
    }

    @Override
    public BigDecimal findOpeningBalancesForDocumentSerial(SubAccountSummarySearchBean cashAccountSettlementSearchBean, Integer bankId, Integer type) {
        BigDecimal debit = new BigDecimal(0);
        BigDecimal credit = new BigDecimal(0);

        // debit = debit.add(openinngBalance(cashAccountSettlementSearchBean, bankId, glYear));
        debit = debit.add(glbankTransaction(cashAccountSettlementSearchBean, bankId));
        credit = credit.add(glbankTransactionCash(cashAccountSettlementSearchBean, bankId));

        credit = credit.add(transferfrom(cashAccountSettlementSearchBean, bankId));
        debit = debit.add(transferTo(cashAccountSettlementSearchBean, bankId));

        return credit.subtract(debit);
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

    private BigDecimal findOpenBalanceSumwithDocumentSerial(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        params.put("bankId", cashAccountSettlementSearchBean.getBankId());
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT sum(COALESCE(g.openinngBalance,0)) FROM GlBank g WHERE g.branchId.id = :branchId AND g.id = :bankId ");

//        if (cashAccountSettlementSearchBean.getDateTo() != null) {
//            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateFrom());
//            builder.append(" and g.dateOpeninngBalance < '").append(d3).append("'");
//        }
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

    private BigDecimal findReceiptSumWithDocumentSerial(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        params.put("bankId", cashAccountSettlementSearchBean.getBankId());
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT sum(COALESCE(g.valueLocal,0)) FROM GlBankTransaction g WHERE g.branchId.id = :branchId AND g.transactionType <> 2 AND  g.transactionType <> 3 AND g.glBankId.id = :bankId");

//        if (cashAccountSettlementSearchBean.getDateTo() != null) {
//            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateFrom());
//            builder.append(" and g.date < '").append(d3).append("'");
//        }
        if (cashAccountSettlementSearchBean.getType() != null) {
            builder.append(" and g.transactionType = :type");
            params.put("type", cashAccountSettlementSearchBean.getType());
        }

        if (cashAccountSettlementSearchBean.getFromDocumentSerial() == null && cashAccountSettlementSearchBean.getToDocumentSerial() == null) {
            builder.append(" and g.documentSerial is not null ");
        } else {
            if (cashAccountSettlementSearchBean.getFromDocumentSerial() != null) {
                builder.append(" and g.documentSerial < :documentSerialFrom");
                params.put("documentSerialFrom", cashAccountSettlementSearchBean.getFromDocumentSerial());
            }

//            if (cashAccountSettlementSearchBean.getToDocumentSerial() != null) {
//                builder.append(" and g.documentSerial <= :documentSerialTo");
//                params.put("documentSerialTo", cashAccountSettlementSearchBean.getToDocumentSerial());
//            }
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

    private BigDecimal findTransferfromSumWithDocumentSerial(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        params.put("bankId", cashAccountSettlementSearchBean.getBankId());
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT sum(COALESCE(g.valueLocal,0)) FROM GlBankTransactionDetail g WHERE g.glBankTransactionId.branchId.id = :branchId AND g.glBankTransactionId.transactionType = 2 AND g.bankIdFrom.id IS NOT NULL AND g.bankIdFrom.id = :bankId");

//        if (cashAccountSettlementSearchBean.getDateTo() != null) {
//            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateFrom());
//            builder.append(" and g.glBankTransactionId.date < '").append(d3).append("'");
//        }
        if (cashAccountSettlementSearchBean.getFromDocumentSerial() == null && cashAccountSettlementSearchBean.getToDocumentSerial() == null) {
            builder.append(" and g.glBankTransactionId.documentSerial is not null ");
        } else {
            if (cashAccountSettlementSearchBean.getFromDocumentSerial() != null) {
                builder.append(" and g.glBankTransactionId.documentSerial < :documentSerialFrom");
                params.put("documentSerialFrom", cashAccountSettlementSearchBean.getFromDocumentSerial());
            }

//            if (cashAccountSettlementSearchBean.getToDocumentSerial() != null) {
//                builder.append(" and g.glBankTransactionId.documentSerial <= :documentSerialTo");
//                params.put("documentSerialTo", cashAccountSettlementSearchBean.getToDocumentSerial());
//            }
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

    private BigDecimal findTransfertoSumWithDocumentSerial(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        params.put("bankId", cashAccountSettlementSearchBean.getBankId());
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT sum(COALESCE(g.valueLocal,0)) FROM GlBankTransactionDetail g WHERE g.glBankTransactionId.branchId.id = :branchId AND g.glBankTransactionId.transactionType = 2 AND g.bankIdTo.id IS NOT NULL AND g.bankIdTo.id = :bankId");
//        if (cashAccountSettlementSearchBean.getDateTo() != null) {
//            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateFrom());
//            builder.append(" and g.glBankTransactionId.date < '").append(d3).append("'");
//        }
        if (cashAccountSettlementSearchBean.getFromDocumentSerial() == null && cashAccountSettlementSearchBean.getToDocumentSerial() == null) {
            builder.append(" and g.glBankTransactionId.documentSerial is not null");
        } else {
            if (cashAccountSettlementSearchBean.getFromDocumentSerial() != null) {
                builder.append(" and g.glBankTransactionId.documentSerial < :documentSerialFrom");
                params.put("documentSerialFrom", cashAccountSettlementSearchBean.getFromDocumentSerial());
            }

//            if (cashAccountSettlementSearchBean.getToDocumentSerial() != null) {
//                builder.append(" and g.glBankTransactionId.documentSerial <= :documentSerialTo");
//                params.put("documentSerialTo", cashAccountSettlementSearchBean.getToDocumentSerial());
//            }
        }
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

            builder.append("select  IFNULL(NET(IFNULL(SUM(`ipd`.`net`), 0.0), IFNULL(`ip`.`discount_type`, 0.0), IFNULL(`ip`.`discount`, 0.0), IFNULL(`ip`.`tax_flag`, 0.0), "
                    + " 14, 0.0, 0.0, 0.0), 0.0) AS `net` from inv_purchase_invoice_detail ipd  left join inv_purchase_invoice ip on ip.id = ipd.inv_purchase_invoice_id ");

            if (cashAccountSettlementSearchBean.getDateTo() != null) {
                String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateFrom());
                builder.append(" and ip.date < '").append(d3).append("'");
            }

            if (cashAccountSettlementSearchBean.getType() != null) {
                builder.append(" and ip.type = ").append(cashAccountSettlementSearchBean.getType());
            }
            List<Object[]> objectlList = dao.executeNativeQuery(builder.toString());
            selesValue = (BigDecimal) objectlList.get(0)[0];
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
//        if (cashAccountSettlementSearchBean.getIsagUserFrom() != null) {
//            builder.append(" and g.createdBy.name >= :createdByFrom");
//            params.put("createdByFrom", cashAccountSettlementSearchBean.getIsagUserFrom());
//        }
//
//        if (cashAccountSettlementSearchBean.getIsagUserTo() != null) {
//            builder.append(" and g.createdBy.name <= :createdByTo");
//            params.put("createdByTo", cashAccountSettlementSearchBean.getIsagUserTo());
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
    public BigDecimal findOpeningBalancesForSpecificCashAccount(SubAccountSummarySearchBean cashAccountSettlementSearchBean, Integer bankId, Integer type) {
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
            //debit
            OpenBalanceSum = findOpenBalanceSum(cashAccountSettlementSearchBean);
            TransfertoSum = findTransfertoSum(cashAccountSettlementSearchBean);
            Openingvalue = Openingvalue.add(OpenBalanceSum);
            Openingvalue = Openingvalue.add(TransfertoSum);

        } else if (type == 0) {
            //credit
            TransferfromSum = findTransferfromSum(cashAccountSettlementSearchBean);
            Openingvalue = Openingvalue.add(TransferfromSum);
        }
        Openingvalue = Openingvalue.add(SalesSum);
        Openingvalue = Openingvalue.add(returnSalesSum);
        Openingvalue = Openingvalue.add(ReceiptSum);
        return Openingvalue;
    }

    @Override
    public BigDecimal findOpeningBalancesForSpecificCashAccountWithDocumentSerial(SubAccountSummarySearchBean cashAccountSettlementSearchBean, Integer bankId, Integer type) {
        Openingvalue = BigDecimal.ZERO;
        cashAccountSettlementSearchBean.setBankId(bankId);
        cashAccountSettlementSearchBean.setType(type);
        SalesSum = BigDecimal.ZERO;
        returnSalesSum = BigDecimal.ZERO;

        SalesSum = findSalesSum(cashAccountSettlementSearchBean);
        ReceiptSum = findReceiptSumWithDocumentSerial(cashAccountSettlementSearchBean);
        if (type == 1) {
            cashAccountSettlementSearchBean.setType(2);
            returnSalesSum = findSalesSum(cashAccountSettlementSearchBean);
        } else if (type == 0) {
            cashAccountSettlementSearchBean.setType(3);
            returnSalesSum = findSalesSum(cashAccountSettlementSearchBean);
        }
        if (type == 1) {
            //debit
            OpenBalanceSum = findOpenBalanceSumwithDocumentSerial(cashAccountSettlementSearchBean);
            TransfertoSum = findTransfertoSumWithDocumentSerial(cashAccountSettlementSearchBean);
            Openingvalue = Openingvalue.add(OpenBalanceSum);
            Openingvalue = Openingvalue.add(TransfertoSum);

        } else if (type == 0) {
            //credit
            TransferfromSum = findTransferfromSumWithDocumentSerial(cashAccountSettlementSearchBean);
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

    private void addGlBankFromAndToNew(StringBuilder sql, Map<String, Object> params, SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        if (cashAccountSettlementSearchBean.getGlBankIdSelected() != null) {
            sql.append(" and cas.bankId in ( :glBankFrom )");
            params.put("glBankFrom", cashAccountSettlementSearchBean.getGlBankIdSelected());
        }

    }

    public void addDateFromAndTo(StringBuilder sql, SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        //        if (salesReportByValueAded.getDateFrom() != null) {
//            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
//            query.append(" and ip.date >=  '").append(dateFormat.format(salesReportByValueAded.getDateFrom())).append("' ");
//        }
        if (cashAccountSettlementSearchBean.getDateFrom() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateFrom());
            sql.append(" and cas.date >= '").append(d3).append(" 00:00:00").append("'");
        }

        if (cashAccountSettlementSearchBean.getDateTo() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateTo());
            sql.append(" and cas.date <= '").append(d3).append(" 59:59:59").append("'");
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
        if (cashAccountSettlementSearchBean.getIsagUserFrom() != null) {
            sql.append(" and cas.createdBy >= :createdByFrom");
            params.put("createdByFrom", cashAccountSettlementSearchBean.getIsagUserFrom());
        }

        if (cashAccountSettlementSearchBean.getIsagUserTo() != null) {
            sql.append(" and cas.createdBy <= :createdByTo");
            params.put("createdByTo", cashAccountSettlementSearchBean.getIsagUserTo());
        }
    }

    private void addOrderBy(StringBuilder sql, SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        if (cashAccountSettlementSearchBean.getOrderBy() != null && cashAccountSettlementSearchBean.getOrderBy() == 0) {
            sql.append(" order by cas.date");
        } else {
            sql.append(" order by cas.value");
        }
    }

    public void addDateFromAndToForOpeningBalance(StringBuilder sql, SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
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
        addDateFromAndToForOpeningBalance(builder, cashAccountSettlementSearchBean);
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
            view.setDocumentSerial(entity.getDocumentSerial());
            if (entity.getGlBankId() != null) {
                view.setName(entity.getGlBankId().getName());
                view.setCode(entity.getGlBankId().getCode());
                if (entity.getGlBankId().getCurrencyId() != null) {
                    view.setCurrencyCode(entity.getGlBankId().getCurrencyId().getCode());
                    view.setCurrencyName(entity.getGlBankId().getCurrencyId().getName());
                }
            }
            view.setStatus(entity.getTransactionType());
            if (entity.getInstallmentId() != null && entity.getInstallmentId().getInstContractId() != null) {
                view.setContractSerial(entity.getInstallmentId().getInstContractId().getSerial().toString());
            }

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
                    view.setDocumentSerial(entity.getGlBankTransactionId().getDocumentSerial());
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
                    view.setDocumentSerial(entity.getGlBankTransactionId().getDocumentSerial());

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

    @Override
    public CashAccountSettlementBean searchNew(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        if (cashAccountSettlementSearchBean.getGlBankIdSelected() != null && !cashAccountSettlementSearchBean.getGlBankIdSelected().isEmpty()) {
            CashAccountSettlementBean cashAccountSettlementBean = new CashAccountSettlementBean();
            cashAccountSettlementBean.setGlBankCopyMap(new HashMap<>());
            if (cashAccountSettlementSearchBean.getGlBankDTOListSelected() != null && !cashAccountSettlementSearchBean.getGlBankDTOListSelected().isEmpty()) {
                for (GlBankDTO gb : cashAccountSettlementSearchBean.getGlBankDTOListSelected()) {
                    cashAccountSettlementBean.getGlBankMap().put(gb.getId(), gb);
                    cashAccountSettlementBean.getTotalForDebitMap().put(gb.getId(), BigDecimal.ZERO);
                    cashAccountSettlementBean.getTotalForCreditMap().put(gb.getId(), BigDecimal.ZERO);
                    cashAccountSettlementBean.getTotalForBalanceMap().put(gb.getId(), BigDecimal.ZERO);
                    cashAccountSettlementBean.getOpeningBalanceMap().put(gb.getId(), BigDecimal.ZERO);
                    cashAccountSettlementBean.getGlBankCopyMap().put(gb.getId(), gb);
                    System.out.println("Key = " + gb.getId());
                }
            }

            cashAccountSettlementBean.setCashAccountSettlementReports(new ArrayList<>());
            cashAccountSettlementBean.setCashAccountSettlementOpeningBalanceList(new ArrayList<>());
            cashAccountSettlementBean.setCashAccountSettlementListNew(findAllCashAccountSettlmentNew(cashAccountSettlementSearchBean)); // here
            if (cashAccountSettlementSearchBean.getOrderBy() == 1) {
                cashAccountSettlementBean.getCashAccountSettlementListNew().sort(new CashAccountSettlmentComparator1());
            }

            cashAccountSettlementBean.setTotalDebit(BigDecimal.ZERO);
            cashAccountSettlementBean.setTotalCredit(BigDecimal.ZERO);

            BigDecimal debit = BigDecimal.ZERO;
            BigDecimal credit = BigDecimal.ZERO;
            BigDecimal balance = BigDecimal.ZERO;
            SubAccountSummaryReport cashAccountSettlment;
            cashAccountSettlementBean.setDistinctBankId(0);
            for (CashAccountSettlment cashAccountSettlmentView : cashAccountSettlementBean.getCashAccountSettlementListNew()) {

                cashAccountSettlment = new SubAccountSummaryReport();
                cashAccountSettlment.setId(cashAccountSettlmentView.getBanktransactionId());
                cashAccountSettlment.setBankId(cashAccountSettlmentView.getBankId());
                cashAccountSettlment.setBankName(cashAccountSettlmentView.getName());
                cashAccountSettlment.setDate(cashAccountSettlmentView.getDate() != null ? cashAccountSettlmentView.getDate() : null);
                cashAccountSettlment.setRemark(cashAccountSettlmentView.getRemark() != null ? cashAccountSettlmentView.getRemark() : null);
                cashAccountSettlment.setRemark2(cashAccountSettlmentView.getRemark2() != null ? cashAccountSettlmentView.getRemark2() : null);
                cashAccountSettlment.setCode(cashAccountSettlmentView.getCode() != null ? cashAccountSettlmentView.getCode() : null);
                cashAccountSettlment.setCurrencyName(cashAccountSettlmentView.getCurrencyName() != null ? cashAccountSettlmentView.getCurrencyName() : null);
                cashAccountSettlment.setCurrencyCode(cashAccountSettlmentView.getCurrencyCode() != null ? cashAccountSettlmentView.getCurrencyCode() : null);
                cashAccountSettlment.setSerial(cashAccountSettlmentView.getSerial() != null ? cashAccountSettlmentView.getSerial() + "" : null);
//                cashAccountSettlment.setTransactionTypeNumber(cashAccountSettlmentView.getTypeId());
                cashAccountSettlment.setCreatedBy(cashAccountSettlmentView.getCreatedBy());
//                cashAccountSettlment.setCustomerName(cashAccountSettlmentView.getCustomerName());
//                cashAccountSettlment.setCustomerCode(cashAccountSettlmentView.getCustomerCode());
//                cashAccountSettlment.setInvoiceNumber(cashAccountSettlmentView.getInvoiceNumber());

                if (cashAccountSettlmentView.getStatus() != null) {
                    if (cashAccountSettlementSearchBean.getCurrencyType() == 1) {

                        if (cashAccountSettlmentView.getStatus() == 1) {
                            cashAccountSettlment.setDebitAmount(cashAccountSettlmentView.getValue().setScale(2, RoundingMode.UP));
                            cashAccountSettlment.setTransactionType(cashAccountSettlmentView.getTypeName());

                        } else if (cashAccountSettlmentView.getStatus() == 0) {
                            cashAccountSettlment.setCreditAmount(cashAccountSettlmentView.getValue().setScale(2, RoundingMode.UP));
                            cashAccountSettlment.setTransactionType(cashAccountSettlmentView.getTypeName());

                        }
                    } else {
                        if (cashAccountSettlmentView.getStatus() == 1) {
                            cashAccountSettlment.setDebitAmount(cashAccountSettlmentView.getValueLocal().setScale(2, RoundingMode.UP));
                            cashAccountSettlment.setTransactionType(cashAccountSettlmentView.getTypeName());
                        } else if (cashAccountSettlmentView.getStatus() == 0) {
                            cashAccountSettlment.setCreditAmount(cashAccountSettlmentView.getValueLocal().setScale(2, RoundingMode.UP));
                            cashAccountSettlment.setTransactionType(cashAccountSettlmentView.getTypeName());

                        }
                    }
                }
                addOpeningBalanceNew(cashAccountSettlment, cashAccountSettlementBean, cashAccountSettlementSearchBean);
                debit = cashAccountSettlment.getDebitAmount() != null ? cashAccountSettlment.getDebitAmount().setScale(2, RoundingMode.UP) : BigDecimal.ZERO;
                credit = cashAccountSettlment.getCreditAmount() != null ? cashAccountSettlment.getCreditAmount().setScale(2, RoundingMode.UP) : BigDecimal.ZERO;
                balance = cashAccountSettlementBean.getTotalForBalanceMap().get(cashAccountSettlment.getBankId());
                if (balance == null) {
                    balance = BigDecimal.ZERO;
                }
                balance = balance.add(debit.subtract(credit));
                cashAccountSettlementBean.getTotalForBalanceMap().put(cashAccountSettlment.getBankId(), balance.setScale(2, RoundingMode.UP));
                cashAccountSettlment.setBalance(balance.setScale(2, RoundingMode.UP));

                fillDebitAndCreditTotalsNew(cashAccountSettlment, cashAccountSettlementBean, debit, credit);
                cashAccountSettlementBean.getCashAccountSettlementReports().add(cashAccountSettlment);

            }
            calculateExceedDateOpeningBalanceNew(cashAccountSettlementBean, cashAccountSettlementSearchBean);
            if (cashAccountSettlementBean.getCashAccountSettlementReports() != null && !cashAccountSettlementBean.getCashAccountSettlementReports().isEmpty()) {
                for (SubAccountSummaryReport cash : cashAccountSettlementBean.getCashAccountSettlementReports()) {
                    cash.setTotalCredit(cashAccountSettlementBean.getTotalForCreditMap() == null ? BigDecimal.ZERO : cashAccountSettlementBean.getTotalForCreditMap().get(cash.getBankId()).setScale(2, RoundingMode.UP));
                    cash.setTotalDebit(cashAccountSettlementBean.getTotalForDebitMap() == null ? BigDecimal.ZERO : cashAccountSettlementBean.getTotalForDebitMap().get(cash.getBankId()).setScale(2, RoundingMode.UP));
                    cash.setOpeningBalance(cashAccountSettlementBean.getOpeningBalanceMap() == null ? BigDecimal.ZERO : cashAccountSettlementBean.getOpeningBalanceMap().get(cash.getBankId()).setScale(2, RoundingMode.UP));
                }
            }
            cashAccountSettlementBean.setCashAccountSettlementList(null);
            cashAccountSettlementBean.setCashAccountSettlementListNew(null);
            cashAccountSettlementBean.setOpeningBalanceMap(null);
            cashAccountSettlementBean.setTotalForBalanceMap(null);
            cashAccountSettlementBean.setTotalForCreditMap(null);
            cashAccountSettlementBean.setTotalForDebitMap(null);
            return cashAccountSettlementBean;
        }
        return null;
    }

    public void addOpeningBalanceNew(SubAccountSummaryReport cashAccountSettlment, CashAccountSettlementBean cashAccountSettlementBean,
            SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        cashAccountSettlementBean.setOpeningBalance(BigDecimal.ZERO);
        if (cashAccountSettlment.getBankId() != null && cashAccountSettlementBean.getDistinctBankId().compareTo(cashAccountSettlment.getBankId()) != 0) {
            cashAccountSettlementBean.setDistinctBankId(cashAccountSettlment.getBankId());
            BigDecimal debitOpenBalance = findOpeningBalancesForSpecificCashAccountNew(cashAccountSettlementSearchBean, cashAccountSettlment.getBankId(), 1);
            BigDecimal creditOpenBalance = findOpeningBalancesForSpecificCashAccountNew(cashAccountSettlementSearchBean, cashAccountSettlment.getBankId(), 0);
            cashAccountSettlementBean.setOpeningBalance((debitOpenBalance != null ? debitOpenBalance : BigDecimal.ZERO).subtract(creditOpenBalance != null ? creditOpenBalance : BigDecimal.ZERO));
            cashAccountSettlementBean.getOpeningBalanceMap().put(cashAccountSettlment.getBankId(), cashAccountSettlementBean.getOpeningBalance().setScale(2, RoundingMode.UP));
            /*if (openingBalance.signum() == -1 && openingBalance.compareTo(BigDecimal.ZERO) != 0) {
             cashAccountSettlment.setCreditAmount(cashAccountSettlment.getCreditAmount() != null ? cashAccountSettlment.getCreditAmount() : BigDecimal.ZERO);
             } else if ((openingBalance.signum() == 1 && openingBalance.compareTo(BigDecimal.ZERO) != 0)) {
             cashAccountSettlment.setDebitAmount(cashAccountSettlment.getDebitAmount() != null ? cashAccountSettlment.getDebitAmount() : BigDecimal.ZERO);
             }*/
            BigDecimal bala = cashAccountSettlementBean.getTotalForBalanceMap().get(cashAccountSettlment.getBankId());
            if (bala == null) {
                bala = BigDecimal.ZERO;
            }
            bala = bala.add(cashAccountSettlementBean.getOpeningBalance());
            cashAccountSettlementBean.getTotalForBalanceMap().put(cashAccountSettlment.getBankId(), bala.setScale(2, RoundingMode.UP));
        }
    }

    public void fillDebitAndCreditTotalsNew(SubAccountSummaryReport cashAccountSettlment, CashAccountSettlementBean cashAccountSettlementBean,
            BigDecimal debit, BigDecimal credit) {
        BigDecimal catchDebit = cashAccountSettlementBean.getTotalForDebitMap().get(cashAccountSettlment.getBankId());
        BigDecimal catchCredit = cashAccountSettlementBean.getTotalForCreditMap().get(cashAccountSettlment.getBankId());
        if (catchDebit == null) {
            catchDebit = BigDecimal.ZERO;
        }
        if (catchCredit == null) {
            catchCredit = BigDecimal.ZERO;
        }
        catchDebit = catchDebit.add(debit);
        catchCredit = catchCredit.add(credit);
        cashAccountSettlementBean.getTotalForDebitMap().put(cashAccountSettlment.getBankId(), catchDebit.setScale(2, RoundingMode.UP));
        cashAccountSettlementBean.getTotalForCreditMap().put(cashAccountSettlment.getBankId(), catchCredit.setScale(2, RoundingMode.UP));
    }

    public void calculateExceedDateOpeningBalanceNew(CashAccountSettlementBean cashAccountSettlementBean, SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        cashAccountSettlementBean.setBuilder(new StringBuilder());
        for (Integer key : cashAccountSettlementBean.getGlBankCopyMap().keySet()) {
            if (cashAccountSettlementBean.getBuilder().length() == 0 && cashAccountSettlementBean.getBuilder() != null) {
                cashAccountSettlementBean.getBuilder().append(key);
            } else {
                cashAccountSettlementBean.getBuilder().append(",").append(key);
            }
        }
        if (cashAccountSettlementBean.getBuilder().length() != 0 && cashAccountSettlementBean.getBuilder() != null) {
            List<GlBankDTO> glBankList = glBankService.findGlBankDTOList(cashAccountSettlementBean.getBuilder());

            if (glBankList != null && !glBankList.isEmpty()) {

                for (GlBankDTO bankDTO : glBankList) {
                    BigDecimal debitOpenBalance = findOpeningBalancesForSpecificCashAccountNew(cashAccountSettlementSearchBean, bankDTO.getId(), 1);
                    BigDecimal creditOpenBalance = findOpeningBalancesForSpecificCashAccountNew(cashAccountSettlementSearchBean, bankDTO.getId(), 0);
                    cashAccountSettlementBean.setOpeningBalance((debitOpenBalance != null ? debitOpenBalance : BigDecimal.ZERO).subtract(creditOpenBalance != null ? creditOpenBalance : BigDecimal.ZERO));
                    cashAccountSettlementBean.getOpeningBalanceMap().put(bankDTO.getId(), cashAccountSettlementBean.getOpeningBalance().setScale(2, RoundingMode.UP));
                    cashAccountSettlementBean.getTotalForBalanceMap().put(bankDTO.getId(), cashAccountSettlementBean.getOpeningBalance().setScale(2, RoundingMode.UP));
                    if (cashAccountSettlementBean.getOpeningBalance().compareTo(BigDecimal.ZERO) == 1) {
                        SubAccountSummaryReport summaryReport = new SubAccountSummaryReport();
                        summaryReport.setBankId(bankDTO.getId());
                        summaryReport.setBankName(bankDTO.getName());
                        summaryReport.setCode(bankDTO.getCode());
                        summaryReport.setBalance(cashAccountSettlementBean.getOpeningBalance().setScale(2, RoundingMode.UP));
                        cashAccountSettlementBean.getCashAccountSettlementReports().add(summaryReport);
                    }
                }
            }
        }
    }

    public String createSQL(SubAccountSummarySearchBean cashAccountSettlementSearchBean, boolean isOpenningBalance) {
        StringBuilder query = null;
        String dateFrom = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateFrom());
        String dateTo = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateTo());

        if (cashAccountSettlementSearchBean.getGlBankIdSelected() != null && !cashAccountSettlementSearchBean.getGlBankIdSelected().isEmpty()) {
            if ((cashAccountSettlementSearchBean.getTransactionType() == null || cashAccountSettlementSearchBean.getTransactionType() == 0) && cashAccountSettlementSearchBean.getType() == 3
                    && cashAccountSettlementSearchBean.getSerialFrom() == null && cashAccountSettlementSearchBean.getSerialTo() == null) {
                query = new StringBuilder();
                query.append("SELECT \n"
                        + "        `i`.`name` AS `createdBy`,\n"
                        + "        `gb`.`id` AS `bankId`,\n"
                        + "        0 AS `paymentType`,\n"
                        + "        `gb`.`date_openinng_balance` AS `date`,\n"
                        + "        CAST(`gb`.`code` AS SIGNED) AS `serial`,\n"
                        + "        `gb`.`openinng_balance` AS `value`,\n"
                        + "        `gb`.`openinng_balance` AS `value_local`,\n"
                        + "        '' AS `remark`,\n"
                        + "        '' AS `remark2`,\n"
                        + "        `gb`.`name` AS `name`,\n"
                        + "        `gb`.`code` AS `code`,\n"
                        + "        `c`.`name` AS `currencyName`,\n"
                        + "        `c`.`code` AS `currencyCode`,\n"
                        + "        CAST(1 AS SIGNED) AS `status`,\n"
                        + "        '' AS `contractSerial`,\n"
                        + "        'رصيد افتتاحي' AS `type_name`,\n"
                        + "        NULL AS `documentSerial`\n"
                        + "    FROM\n"
                        + "        ((`isag`.`gl_bank` `gb`\n"
                        + "        LEFT JOIN `isag`.`currency` `c` ON ((`c`.`id` = `gb`.`currency_id`)))\n"
                        + "        LEFT JOIN `isag`.`isag_user` `i` ON ((`i`.`id` = `gb`.`created_by`)))\n"
                        + "    WHERE\n"
                        + "        ((`gb`.`date_openinng_balance` IS NOT NULL)\n"
                        + "            AND (`gb`.`openinng_balance` > 0)) \n"
                        + "            AND `gb`.`id` in (").append(cashAccountSettlementSearchBean.getGlBankIdSelected()).append(")");
                if (isOpenningBalance) {
                    query.append("            AND `gb`.`date_openinng_balance` < '").append(dateFrom).append("'");
                } else {
                    query.append("            AND `gb`.`date_openinng_balance` >= '").append(dateFrom).append("' AND `gb`.`date_openinng_balance` <= '").append(dateTo).append("'");
                }
            }
            if (cashAccountSettlementSearchBean.getTransactionType() == null || cashAccountSettlementSearchBean.getTransactionType() == 1 || cashAccountSettlementSearchBean.getTransactionType() == 2) {
                if (query == null) {
                    query = new StringBuilder();

                } else {
                    query.append("    UNION ALL ");
                }

                query.append(" SELECT \n"
                        + "        `i`.`name` AS `createdBy`,\n"
                        + "        `bt`.`gl_bank_id` AS `bankId`,\n"
                        + "        `bt`.`payment_type` AS `paymentType`,\n"
                        + "        `bt`.`date` AS `date`,\n"
                        + "        `bt`.`serial` AS `serial`,\n"
                        + "        `bt`.`value` AS `value`,\n"
                        + "        `bt`.`value_local` AS `value_local`,\n"
                        + "        `bt`.`remark` AS `remark`,\n"
                        + "        `bt`.`remark2` AS `remark2`,\n"
                        + "        `gb`.`name` AS `name`,\n"
                        + "        `gb`.`code` AS `code`,\n"
                        + "        `c`.`name` AS `currencyName`,\n"
                        + "        `c`.`code` AS `currencyCode`,\n"
                        + "        `bt`.`transaction_type` AS `status`,\n"
                        + "        `ic`.`serial` AS `contractSerial`,\n"
                        + "        (CASE\n"
                        + "            WHEN (`bt`.`transaction_type` = 0) THEN 'سند صرف'\n"
                        + "            WHEN (`bt`.`transaction_type` = 1) THEN 'سند قبض'\n"
                        + "        END) AS `type_name`,\n"
                        + "        `bt`.`document_serial` AS `documentSerial`\n"
                        + "    FROM\n"
                        + "        (((((`isag`.`gl_bank_transaction` `bt`\n"
                        + "        LEFT JOIN `isag`.`gl_bank` `gb` ON ((`gb`.`id` = `bt`.`gl_bank_id`)))\n"
                        + "        LEFT JOIN `isag`.`inst_contract_detail` `icd` ON ((`icd`.`id` = `bt`.`installment_id`)))\n"
                        + "        LEFT JOIN `isag`.`inst_contract` `ic` ON ((`ic`.`id` = `icd`.`inst_contract_id`)))\n"
                        + "        LEFT JOIN `isag`.`currency` `c` ON ((`c`.`id` = `gb`.`currency_id`)))\n"
                        + "        LEFT JOIN `isag`.`isag_user` `i` ON ((`i`.`id` = `bt`.`created_by`)))\n"
                        + "    WHERE\n"
                        + "        ((`bt`.`transaction_type` <> 2)\n"
                        + "            AND (`bt`.`transaction_type` <> 3)) "
                        + "            AND `bt`.`gl_bank_id` in (").append(cashAccountSettlementSearchBean.getGlBankIdSelected()).append(")");
                if (isOpenningBalance) {
                    query.append("            AND `bt`.`date` < '").append(dateFrom).append("'");
                } else {
                    query.append("            AND `bt`.`date` >= '").append(dateFrom).append("' AND `bt`.`date` <= '").append(dateTo).append("'");
                }
                if (cashAccountSettlementSearchBean.getType() != null && cashAccountSettlementSearchBean.getType() != 3) {
                    query.append(" AND `bt`.`transaction_type` = ").append(cashAccountSettlementSearchBean.getType());
                }

                if (cashAccountSettlementSearchBean.getTransactionType() != null) {
                    query.append(" AND `bt`.`transaction_type` = ").append(cashAccountSettlementSearchBean.getTransactionType());
                }

                if (cashAccountSettlementSearchBean.getSerialFrom() != null) {
                    query.append(" AND `bt`.`serial` >= ").append(cashAccountSettlementSearchBean.getSerialFrom());
                }

                if (cashAccountSettlementSearchBean.getSerialTo() != null) {
                    query.append(" AND `bt`.`serial` <= ").append(cashAccountSettlementSearchBean.getSerialTo());
                }

            }

            if ((cashAccountSettlementSearchBean.getTransactionType() == null || cashAccountSettlementSearchBean.getTransactionType() == 3)
                    && cashAccountSettlementSearchBean.getType() != null && (cashAccountSettlementSearchBean.getType() == 3 || cashAccountSettlementSearchBean.getType() == 0)) {
                if (query == null) {
                    query = new StringBuilder();

                } else {
                    query.append("    UNION ALL ");
                }

                query.append(" SELECT \n"
                        + "        `i`.`name` AS `createdBy`,\n"
                        + "        `btd`.`bank_id_from` AS `bankId`,\n"
                        + "        `bt`.`payment_type` AS `paymentType`,\n"
                        + "        `bt`.`date` AS `date`,\n"
                        + "        `bt`.`serial` AS `serial`,\n"
                        + "        `btd`.`value` AS `value`,\n"
                        + "        `btd`.`value_local` AS `value_local`,\n"
                        + "        `btd`.`remarks` AS `remark`,\n"
                        + "        `bt`.`remark2` AS `remark2`,\n"
                        + "        `gb`.`name` AS `name`,\n"
                        + "        `gb`.`code` AS `code`,\n"
                        + "        `c`.`name` AS `currencyName`,\n"
                        + "        `c`.`code` AS `currencyCode`,\n"
                        + "        0 AS `status`,\n"
                        + "        '' AS `contractSerial`,\n"
                        + "        'تحويل صادر' AS `type_name`,\n"
                        + "        `bt`.`document_serial` AS `documentSerial`\n"
                        + "    FROM\n"
                        + "        ((((`isag`.`gl_bank_transaction` `bt`\n"
                        + "        LEFT JOIN `isag`.`gl_bank_transaction_detail` `btd` ON ((`bt`.`id` = `btd`.`gl_bank_transaction_id`)))\n"
                        + "        LEFT JOIN `isag`.`gl_bank` `gb` ON ((`gb`.`id` = `btd`.`bank_id_from`)))\n"
                        + "        LEFT JOIN `isag`.`currency` `c` ON ((`c`.`id` = `gb`.`currency_id`)))\n"
                        + "        LEFT JOIN `isag`.`isag_user` `i` ON ((`i`.`id` = `bt`.`created_by`)))\n"
                        + "    WHERE\n"
                        + "        ((`bt`.`transaction_type` = 2)\n"
                        + "            AND (`btd`.`bank_id_from` IS NOT NULL))  "
                        + "            AND `btd`.`bank_id_from` in (").append(cashAccountSettlementSearchBean.getGlBankIdSelected()).append(")");
                if (isOpenningBalance) {
                    query.append("            AND `bt`.`date` < '").append(dateFrom).append("'");
                } else {
                    query.append("            AND `bt`.`date` >= '").append(dateFrom).append("' AND `bt`.`date` <= '").append(dateTo).append("'");
                }
                if (cashAccountSettlementSearchBean.getSerialFrom() != null) {
                    query.append(" AND `bt`.`serial` >= ").append(cashAccountSettlementSearchBean.getSerialFrom());
                }

                if (cashAccountSettlementSearchBean.getSerialTo() != null) {
                    query.append(" AND `bt`.`serial` <= ").append(cashAccountSettlementSearchBean.getSerialTo());
                }

            }

            if ((cashAccountSettlementSearchBean.getTransactionType() == null || cashAccountSettlementSearchBean.getTransactionType() == 4)
                    && cashAccountSettlementSearchBean.getType() != null && (cashAccountSettlementSearchBean.getType() == 3 || cashAccountSettlementSearchBean.getType() == 1)) {
                if (query == null) {
                    query = new StringBuilder();

                } else {
                    query.append("    UNION ALL ");
                }

                query.append(" SELECT \n"
                        + "        `i`.`name` AS `createdBy`,\n"
                        + "        `btd`.`bank_id_to` AS `bankId`,\n"
                        + "        `bt`.`payment_type` AS `paymentType`,\n"
                        + "        `bt`.`date` AS `date`,\n"
                        + "        `bt`.`serial` AS `serial`,\n"
                        + "        ((`btd`.`value` * IFNULL(`btd`.`rate_bank_from`, 1)) / IFNULL(`btd`.`rate_bank_to`, 1)) AS `value`,\n"
                        + "        `btd`.`value_local` AS `value_local`,\n"
                        + "        `btd`.`remarks` AS `remark`,\n"
                        + "        `bt`.`remark2` AS `remark2`,\n"
                        + "        `gb`.`name` AS `name`,\n"
                        + "        `gb`.`code` AS `code`,\n"
                        + "        `c`.`name` AS `currencyName`,\n"
                        + "        `c`.`code` AS `currencyCode`,\n"
                        + "        1 AS `status`,\n"
                        + "        '' AS `contractSerial`,\n"
                        + "        'تحويل وارد' AS `type_name`,\n"
                        + "        `bt`.`document_serial` AS `documentSerial`\n"
                        + "    FROM\n"
                        + "        ((((`isag`.`gl_bank_transaction` `bt`\n"
                        + "        LEFT JOIN `isag`.`gl_bank_transaction_detail` `btd` ON ((`bt`.`id` = `btd`.`gl_bank_transaction_id`)))\n"
                        + "        LEFT JOIN `isag`.`gl_bank` `gb` ON ((`gb`.`id` = `btd`.`bank_id_to`)))\n"
                        + "        LEFT JOIN `isag`.`currency` `c` ON ((`c`.`id` = `gb`.`currency_id`)))\n"
                        + "        LEFT JOIN `isag_user` `i` ON ((`i`.`id` = `bt`.`created_by`)))\n"
                        + "    WHERE\n"
                        + "        ((`bt`.`transaction_type` = 2)\n"
                        + "            AND (`btd`.`bank_id_to` IS NOT NULL))   "
                        + "            AND `btd`.`bank_id_to` in (").append(cashAccountSettlementSearchBean.getGlBankIdSelected()).append(")");
                if (isOpenningBalance) {
                    query.append("            AND `bt`.`date` < '").append(dateFrom).append("'");
                } else {
                    query.append("            AND `bt`.`date` >= '").append(dateFrom).append("' AND `bt`.`date` <= '").append(dateTo).append("'");
                }
                if (cashAccountSettlementSearchBean.getSerialFrom() != null) {
                    query.append(" AND `bt`.`serial` >= ").append(cashAccountSettlementSearchBean.getSerialFrom());
                }

                if (cashAccountSettlementSearchBean.getSerialTo() != null) {
                    query.append(" AND `bt`.`serial` <= ").append(cashAccountSettlementSearchBean.getSerialTo());
                }

            }

            if ((cashAccountSettlementSearchBean.getTransactionType() == null || cashAccountSettlementSearchBean.getTransactionType() == 5)
                    && cashAccountSettlementSearchBean.getType() != null && (cashAccountSettlementSearchBean.getType() == 3 || cashAccountSettlementSearchBean.getType() == 0)) {
                if (query == null) {
                    query = new StringBuilder();

                } else {
                    query.append("    UNION ALL ");
                }

                query.append(" SELECT \n"
                        + "        `i`.`name` AS `createdBy`,\n"
                        + "        `ipi`.`gl_bank_id` AS `bankId`,\n"
                        + "        `ipi`.`payment_type` AS `paymentType`,\n"
                        + "        `ipi`.`date` AS `date`,\n"
                        + "        `ipi`.`serial` AS `serial`,\n"
                        + "        IFNULL(NET(IFNULL(SUM(`ipid`.`net`), 0.0), IFNULL(`ipi`.`discount_type`, 0.0), IFNULL(`ipi`.`discount`, 0.0), IFNULL(`ipi`.`tax_flag`, 0.0), \n"
                        + "        (CASE\n"
                        + "            WHEN (`ipi`.`tax_flag` = 0) THEN 0\n"
                        + "            WHEN (`ipi`.`tax_flag` = 1) THEN 15\n"
                        + "        END)\n"
                        + ", 0.0, 0.0, 0.0), 0.0) AS `value`,\n"
                        + "        IFNULL(NET(IFNULL(SUM(`ipid`.`net`), 0.0), IFNULL(`ipi`.`discount_type`, 0.0), IFNULL(`ipi`.`discount`, 0.0), IFNULL(`ipi`.`tax_flag`, 0.0), \n"
                        + "        (CASE\n"
                        + "            WHEN (`ipi`.`tax_flag` = 0) THEN 0\n"
                        + "            WHEN (`ipi`.`tax_flag` = 1) THEN 15\n"
                        + "        END)\n"
                        + ", 0.0, 0.0, 0.0), 0.0) AS `value_local`,\n"
                        + "        `ipi`.`remarks` AS `remark`,\n"
                        + "        '' AS `remark2`,\n"
                        + "        `gb`.`name` AS `name`,\n"
                        + "        `gb`.`code` AS `code`,\n"
                        + "        `c`.`name` AS `currencyName`,\n"
                        + "        `c`.`code` AS `currencyCode`,\n"
                        + "        `ipi`.`type` AS `status`,\n"
                        + "        '' AS `contractSerial`,\n"
                        + "        'فاتورة مشتريات' AS `type_name`,\n"
                        + "        NULL AS `documentSerial`\n"
                        + "    FROM\n"
                        + "        (((((`inv_purchase_invoice` `ipi` )\n"
                        + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON ((`ipi`.`id` = `ipid`.`inv_purchase_invoice_id`)))\n"
                        + "        LEFT JOIN `gl_bank` `gb` ON ((`gb`.`id` = `ipi`.`gl_bank_id`)))\n"
                        + "        LEFT JOIN `currency` `c` ON ((`c`.`id` = `ipi`.`currency_id`)))\n"
                        + "        LEFT JOIN `isag_user` `i` ON ((`i`.`id` = `ipi`.`created_by`)))\n"
                        + "    WHERE\n"
                        + "        (`ipi`.`payment_type` = 0)    "
                        + "            AND `ipi`.`gl_bank_id` in (").append(cashAccountSettlementSearchBean.getGlBankIdSelected()).append(")");
                if (isOpenningBalance) {
                    query.append("            AND `ipi`.`date` < '").append(dateFrom).append("'");
                } else {
                    query.append("            AND `ipi`.`date` >= '").append(dateFrom).append("' AND `ipi`.`date` <= '").append(dateTo).append("'");
                }
                if (cashAccountSettlementSearchBean.getSerialFrom() != null) {
                    query.append(" AND `ipi`.`serial` >= ").append(cashAccountSettlementSearchBean.getSerialFrom());
                }

                if (cashAccountSettlementSearchBean.getSerialTo() != null) {
                    query.append(" AND `ipi`.`serial` <= ").append(cashAccountSettlementSearchBean.getSerialTo());
                }

                query.append(" AND `ipi`.`type` = 0  group by ipi.id ");
            }

            if ((cashAccountSettlementSearchBean.getTransactionType() == null || cashAccountSettlementSearchBean.getTransactionType() == 6)
                    && cashAccountSettlementSearchBean.getType() != null && (cashAccountSettlementSearchBean.getType() == 3 || cashAccountSettlementSearchBean.getType() == 1)) {
                if (query == null) {
                    query = new StringBuilder();

                } else {
                    query.append("    UNION ALL ");
                }

                query.append(" SELECT \n"
                        + "        `i`.`name` AS `createdBy`,\n"
                        + "        `ipi`.`gl_bank_id` AS `bankId`,\n"
                        + "        `ipi`.`payment_type` AS `paymentType`,\n"
                        + "        `ipi`.`date` AS `date`,\n"
                        + "        `ipi`.`serial` AS `serial`,\n"
                        + "        IFNULL(NET(IFNULL(SUM(`ipid`.`net`), 0.0), IFNULL(`ipi`.`discount_type`, 0.0), IFNULL(`ipi`.`discount`, 0.0), IFNULL(`ipi`.`tax_flag`, 0.0), \n"
                        + "        (CASE\n"
                        + "            WHEN (`ipi`.`tax_flag` = 0) THEN 0\n"
                        + "            WHEN (`ipi`.`tax_flag` = 1) THEN 15\n"
                        + "        END)\n"
                        + ", 0.0, 0.0, 0.0), 0.0) AS `value`,\n"
                        + "        IFNULL(NET(IFNULL(SUM(`ipid`.`net`), 0.0), IFNULL(`ipi`.`discount_type`, 0.0), IFNULL(`ipi`.`discount`, 0.0), IFNULL(`ipi`.`tax_flag`, 0.0), \n"
                        + "        (CASE\n"
                        + "            WHEN (`ipi`.`tax_flag` = 0) THEN 0\n"
                        + "            WHEN (`ipi`.`tax_flag` = 1) THEN 15\n"
                        + "        END)\n"
                        + ", 0.0, 0.0, 0.0), 0.0) AS `value_local`,\n"
                        + "        `ipi`.`remarks` AS `remark`,\n"
                        + "        '' AS `remark2`,\n"
                        + "        `gb`.`name` AS `name`,\n"
                        + "        `gb`.`code` AS `code`,\n"
                        + "        `c`.`name` AS `currencyName`,\n"
                        + "        `c`.`code` AS `currencyCode`,\n"
                        + "        `ipi`.`type` AS `status`,\n"
                        + "        '' AS `contractSerial`,\n"
                        + "        'فاتورة مبيعات' AS `type_name`,\n"
                        + "        NULL AS `documentSerial`\n"
                        + "    FROM\n"
                        + "        (((((`inv_purchase_invoice` `ipi` )\n"
                        + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON ((`ipi`.`id` = `ipid`.`inv_purchase_invoice_id`)))\n"
                        + "        LEFT JOIN `gl_bank` `gb` ON ((`gb`.`id` = `ipi`.`gl_bank_id`)))\n"
                        + "        LEFT JOIN `currency` `c` ON ((`c`.`id` = `ipi`.`currency_id`)))\n"
                        + "        LEFT JOIN `isag_user` `i` ON ((`i`.`id` = `ipi`.`created_by`)))\n"
                        + "    WHERE\n"
                        + "        (`ipi`.`payment_type` = 0)    "
                        + "            AND `ipi`.`gl_bank_id` in (").append(cashAccountSettlementSearchBean.getGlBankIdSelected()).append(")");
                if (isOpenningBalance) {
                    query.append("            AND `ipi`.`date` < '").append(dateFrom).append("'");
                } else {
                    query.append("            AND `ipi`.`date` >= '").append(dateFrom).append("' AND `ipi`.`date` <= '").append(dateTo).append("'");
                }
                if (cashAccountSettlementSearchBean.getSerialFrom() != null) {
                    query.append(" AND `ipi`.`serial` >= ").append(cashAccountSettlementSearchBean.getSerialFrom());
                }

                if (cashAccountSettlementSearchBean.getSerialTo() != null) {
                    query.append(" AND `ipi`.`serial` <= ").append(cashAccountSettlementSearchBean.getSerialTo());
                }

                query.append(" AND `ipi`.`type` = 1  group by ipi.id ");
            }

            if ((cashAccountSettlementSearchBean.getTransactionType() == null || cashAccountSettlementSearchBean.getTransactionType() == 7)
                    && cashAccountSettlementSearchBean.getType() != null && (cashAccountSettlementSearchBean.getType() == 3 || cashAccountSettlementSearchBean.getType() == 1)) {
                if (query == null) {
                    query = new StringBuilder();

                } else {
                    query.append("    UNION ALL ");
                }

                query.append(" SELECT \n"
                        + "        `i`.`name` AS `createdBy`,\n"
                        + "        `ipi`.`gl_bank_id` AS `bankId`,\n"
                        + "        `ipi`.`payment_type` AS `paymentType`,\n"
                        + "        `ipi`.`date` AS `date`,\n"
                        + "        `ipi`.`serial` AS `serial`,\n"
                        + "        IFNULL(NET(IFNULL(SUM(`ipid`.`net`), 0.0), IFNULL(`ipi`.`discount_type`, 0.0), IFNULL(`ipi`.`discount`, 0.0), IFNULL(`ipi`.`tax_flag`, 0.0), \n"
                        + "        (CASE\n"
                        + "            WHEN (`ipi`.`tax_flag` = 0) THEN 0\n"
                        + "            WHEN (`ipi`.`tax_flag` = 1) THEN 15\n"
                        + "        END)\n"
                        + ", 0.0, 0.0, 0.0), 0.0) AS `value`,\n"
                        + "        IFNULL(NET(IFNULL(SUM(`ipid`.`net`), 0.0), IFNULL(`ipi`.`discount_type`, 0.0), IFNULL(`ipi`.`discount`, 0.0), IFNULL(`ipi`.`tax_flag`, 0.0), \n"
                        + "        (CASE\n"
                        + "            WHEN (`ipi`.`tax_flag` = 0) THEN 0\n"
                        + "            WHEN (`ipi`.`tax_flag` = 1) THEN 15\n"
                        + "        END)\n"
                        + ", 0.0, 0.0, 0.0), 0.0) AS `value_local`,\n"
                        + "        `ipi`.`remarks` AS `remark`,\n"
                        + "        '' AS `remark2`,\n"
                        + "        `gb`.`name` AS `name`,\n"
                        + "        `gb`.`code` AS `code`,\n"
                        + "        `c`.`name` AS `currencyName`,\n"
                        + "        `c`.`code` AS `currencyCode`,\n"
                        + "        `ipi`.`type` AS `status`,\n"
                        + "        '' AS `contractSerial`,\n"
                        + "        ' مردود مشتريات' AS `type_name`,\n"
                        + "        NULL AS `documentSerial`\n"
                        + "    FROM\n"
                        + "        (((((`inv_purchase_invoice` `ipi` )\n"
                        + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON ((`ipi`.`id` = `ipid`.`inv_purchase_invoice_id`)))\n"
                        + "        LEFT JOIN `gl_bank` `gb` ON ((`gb`.`id` = `ipi`.`gl_bank_id`)))\n"
                        + "        LEFT JOIN `currency` `c` ON ((`c`.`id` = `ipi`.`currency_id`)))\n"
                        + "        LEFT JOIN `isag_user` `i` ON ((`i`.`id` = `ipi`.`created_by`)))\n"
                        + "    WHERE\n"
                        + "        (`ipi`.`payment_type` = 0)    "
                        + "            AND `ipi`.`gl_bank_id` in (").append(cashAccountSettlementSearchBean.getGlBankIdSelected()).append(")");
                if (isOpenningBalance) {
                    query.append("            AND `ipi`.`date` < '").append(dateFrom).append("'");
                } else {
                    query.append("            AND `ipi`.`date` >= '").append(dateFrom).append("' AND `ipi`.`date` <= '").append(dateTo).append("'");
                }
                if (cashAccountSettlementSearchBean.getSerialFrom() != null) {
                    query.append(" AND `ipi`.`serial` >= ").append(cashAccountSettlementSearchBean.getSerialFrom());
                }

                if (cashAccountSettlementSearchBean.getSerialTo() != null) {
                    query.append(" AND `ipi`.`serial` <= ").append(cashAccountSettlementSearchBean.getSerialTo());
                }

                query.append(" AND `ipi`.`type` = 2  group by ipi.id ");
            }

            if ((cashAccountSettlementSearchBean.getTransactionType() == null || cashAccountSettlementSearchBean.getTransactionType() == 8)
                    && cashAccountSettlementSearchBean.getType() != null && (cashAccountSettlementSearchBean.getType() == 3 || cashAccountSettlementSearchBean.getType() == 0)) {
                if (query == null) {
                    query = new StringBuilder();

                } else {
                    query.append("    UNION ALL ");
                }

                query.append(" SELECT \n"
                        + "        `i`.`name` AS `createdBy`,\n"
                        + "        `ipi`.`gl_bank_id` AS `bankId`,\n"
                        + "        `ipi`.`payment_type` AS `paymentType`,\n"
                        + "        `ipi`.`date` AS `date`,\n"
                        + "        `ipi`.`serial` AS `serial`,\n"
                        + "        IFNULL(NET(IFNULL(SUM(`ipid`.`net`), 0.0), IFNULL(`ipi`.`discount_type`, 0.0), IFNULL(`ipi`.`discount`, 0.0), IFNULL(`ipi`.`tax_flag`, 0.0), \n"
                        + "        (CASE\n"
                        + "            WHEN (`ipi`.`tax_flag` = 0) THEN 0\n"
                        + "            WHEN (`ipi`.`tax_flag` = 1) THEN 15\n"
                        + "        END)\n"
                        + ", 0.0, 0.0, 0.0), 0.0) AS `value`,\n"
                        + "        IFNULL(NET(IFNULL(SUM(`ipid`.`net`), 0.0), IFNULL(`ipi`.`discount_type`, 0.0), IFNULL(`ipi`.`discount`, 0.0), IFNULL(`ipi`.`tax_flag`, 0.0), \n"
                        + "        (CASE\n"
                        + "            WHEN (`ipi`.`tax_flag` = 0) THEN 0\n"
                        + "            WHEN (`ipi`.`tax_flag` = 1) THEN 15\n"
                        + "        END)\n"
                        + ", 0.0, 0.0, 0.0), 0.0) AS `value_local`,\n"
                        + "        `ipi`.`remarks` AS `remark`,\n"
                        + "        '' AS `remark2`,\n"
                        + "        `gb`.`name` AS `name`,\n"
                        + "        `gb`.`code` AS `code`,\n"
                        + "        `c`.`name` AS `currencyName`,\n"
                        + "        `c`.`code` AS `currencyCode`,\n"
                        + "        `ipi`.`type` AS `status`,\n"
                        + "        '' AS `contractSerial`,\n"
                        + "        'مردود مبيعات' AS `type_name`,\n"
                        + "        NULL AS `documentSerial`\n"
                        + "    FROM\n"
                        + "        ((((`inv_purchase_invoice` `ipi` \n"
                        + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON ((`ipi`.`id` = `ipid`.`inv_purchase_invoice_id`)))\n"
                        + "        LEFT JOIN `gl_bank` `gb` ON ((`gb`.`id` = `ipi`.`gl_bank_id`)))\n"
                        + "        LEFT JOIN `currency` `c` ON ((`c`.`id` = `ipi`.`currency_id`)))\n"
                        + "        LEFT JOIN `isag_user` `i` ON ((`i`.`id` = `ipi`.`created_by`)))\n"
                        + "    WHERE\n"
                        + "        (`ipi`.`payment_type` = 0)    "
                        + "            AND `ipi`.`gl_bank_id` in (").append(cashAccountSettlementSearchBean.getGlBankIdSelected()).append(")");
                if (isOpenningBalance) {
                    query.append("            AND `ipi`.`date` < '").append(dateFrom).append("'");
                } else {
                    query.append("            AND `ipi`.`date` >= '").append(dateFrom).append("' AND `ipi`.`date` <= '").append(dateTo).append("'");
                }
                if (cashAccountSettlementSearchBean.getSerialFrom() != null) {
                    query.append(" AND `ipi`.`serial` >= ").append(cashAccountSettlementSearchBean.getSerialFrom());
                }

                if (cashAccountSettlementSearchBean.getSerialTo() != null) {
                    query.append(" AND `ipi`.`serial` <= ").append(cashAccountSettlementSearchBean.getSerialTo());
                }

                query.append(" AND `ipi`.`type` = 3  group by ipi.id  ");
            }

            if ((cashAccountSettlementSearchBean.getTransactionType() == null || cashAccountSettlementSearchBean.getTransactionType() == 9)
                    && cashAccountSettlementSearchBean.getType() != null && (cashAccountSettlementSearchBean.getType() == 3 || cashAccountSettlementSearchBean.getType() == 1)) {
                Integer status = 1;
                if (query == null) {
                    query = new StringBuilder();

                } else {
                    query.append("    UNION ALL ");
                }

                query.append(" SELECT \n"
                        + "        `i`.`name` AS `createdBy`,\n"
                        + "        `gs`.`gl_bank_id` AS `bankId`,\n"
                        + "        0 AS `paymentType`,\n"
                        + "        `gs`.`date` AS `date`,\n"
                        + "        `gs`.`serial` AS `serial`,\n"
                        + "        `gs`.`finalNet` AS `value`,\n"
                        + "        `gs`.`finalNet` AS `value_local`,\n"
                        + "        `gs`.`remarks` AS `remark`,\n"
                        + "        '' AS `remark2`,\n"
                        + "        `gb`.`name` AS `name`,\n"
                        + "        `gb`.`code` AS `code`,\n"
                        + "        NULL AS `currencyName`,\n"
                        + "        NULL AS `currencyCode`,\n").append(status).append(
                                "  AS `status`,\n"
                                + "        '' AS `contractSerial`,\n"
                                + "        'اشتراكات جيم' AS `type_name`,\n"
                                + "        NULL AS `documentSerial`\n"
                                + "    FROM\n"
                                + "        (((`isag`.`gm_subscriptions` `gs`\n"
                                + "        LEFT JOIN `isag`.`gm_subscriptions_detail` `gsd` ON ((`gs`.`id` = `gsd`.`gm_subscriptions_id`)))\n"
                                + "        LEFT JOIN `isag`.`gl_bank` `gb` ON ((`gb`.`id` = `gs`.`gl_bank_id`)))\n"
                                + "        LEFT JOIN `isag`.`isag_user` `i` ON ((`i`.`id` = `gs`.`created_by`))) where  gs.deleted_by IS NULL  "
                                + "            AND `gs`.`gl_bank_id` in (").append(cashAccountSettlementSearchBean.getGlBankIdSelected()).append(")");
                if (isOpenningBalance) {
                    query.append("            AND `gs`.`date` < '").append(dateFrom).append("'");
                } else {
                    query.append("            AND `gs`.`date` >= '").append(dateFrom).append("' AND `gs`.`date` <= '").append(dateTo).append("'");
                }
                if (cashAccountSettlementSearchBean.getSerialFrom() != null) {
                    query.append(" AND `gs`.`serial` >= ").append(cashAccountSettlementSearchBean.getSerialFrom());
                }

                if (cashAccountSettlementSearchBean.getSerialTo() != null) {
                    query.append(" AND `gs`.`serial` <= ").append(cashAccountSettlementSearchBean.getSerialTo());
                }

                query.append(" AND `gs`.`type` = 0 ");
            }
        }
        return query == null ? null : query.toString();
    }

    public List<CashAccountSettlment> findAllCashAccountSettlmentNew(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        List<CashAccountSettlment> list = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        String sql = createSQL(cashAccountSettlementSearchBean, false);
        List<Object[]> res = dao.executeNativeQuery(sql.toString());
        for (Object[] object : res) {
            CashAccountSettlment cashAccountSettlmentView = new CashAccountSettlment();
            cashAccountSettlmentView.setCreatedBy(object[0] != null ? (String) object[0] : null);
            cashAccountSettlmentView.setBankId(object[1] != null ? (Integer) object[1] : null);
//            cashAccountSettlmentView.setPaymentType(object[2] != null ? (Integer) object[2] : null);
            cashAccountSettlmentView.setDate(object[3] != null ? (Date) object[3] : null);
            cashAccountSettlmentView.setSerial(object[4] != null ? BigInteger.valueOf(Long.valueOf(object[4].toString())) : null);
            cashAccountSettlmentView.setValue(object[5] != null ? (BigDecimal) object[5] : null);
            cashAccountSettlmentView.setValueLocal(object[6] != null ? (BigDecimal) object[6] : null);
            cashAccountSettlmentView.setRemark(object[7] != null ? (String) object[7] : null);
            cashAccountSettlmentView.setRemark2(object[8] != null ? (String) object[8] : null); 
            cashAccountSettlmentView.setName(object[9] != null ? (String) object[9] : null);
            cashAccountSettlmentView.setCode(object[10] != null ? (String) object[10] : null);
            cashAccountSettlmentView.setCurrencyName(object[11] != null ? (String) object[11] : null);
            cashAccountSettlmentView.setCurrencyCode(object[12] != null ? (String) object[12] : null);
            cashAccountSettlmentView.setStatus(object[13] != null ? Integer.valueOf(object[13].toString())  : null);
            cashAccountSettlmentView.setContractSerial(object[14] != null ? (String) object[14] : null);
            cashAccountSettlmentView.setTypeName(object[15] != null ? (String) object[15] : null);
            cashAccountSettlmentView.setDocumentSerial(object[16] != null ? (Integer) object[16] : null);
            list.add(cashAccountSettlmentView);
        }
        return list;
    }

    public BigDecimal findOpeningBalancesForSpecificCashAccountNew(SubAccountSummarySearchBean cashAccountSettlementSearchBean, Integer bankId, Integer type) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
        params.put("bankId", bankId);
        params.put("type", type);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT sum(COALESCE(cas.value_local,0)) FROM (").append(createSQL(cashAccountSettlementSearchBean, true)).append(") cas where cas.status = ").append(type).append(" AND cas.bankId = ").append(bankId);
        List<Object[]> res = dao.executeNativeQuery(builder.toString());
        BigDecimal value = BigDecimal.ZERO;
        if (res != null && !res.isEmpty()) {
            for (Object[] object : res) {
                if (object != null) {
                    value = object[0] != null ? (BigDecimal) object[0] : BigDecimal.ZERO;
                }
            }
        }

        return value;
    }
}
