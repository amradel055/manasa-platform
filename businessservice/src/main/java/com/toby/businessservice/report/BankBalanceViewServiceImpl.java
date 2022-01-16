/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.bean.BankBalanceReportBean;
import com.toby.bean.BankBalanceReportSearchBean;
import com.toby.core.GenericDAO;
import com.toby.dto.GlBankDTO;
import com.toby.entity.GlYear;
import com.toby.views.BankBalanceView;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq002
 */
@Stateless
public class BankBalanceViewServiceImpl implements BankBalanceViewService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<BankBalanceView> getAllBankBalancesTransactions(Integer branchId, Date dateFrom, Date dateTo, Integer bankIdFrom, Integer bankIdTo) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT bbv FROM BankBalanceView bbv WHERE bbv.branchId = :branchId ");
        appendDateForQueryBuilder(queryBuilder, dateFrom, dateTo);
        addGlBankFromAndTo(queryBuilder, params, bankIdFrom, bankIdTo);
        List<BankBalanceView> data = dao.findListByQuery(queryBuilder.toString(), params);

        return data;
    }

    private void appendDateForQueryBuilder(StringBuilder queryBuilder, Date dateFrom, Date dateTo) {
        if (dateFrom != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(dateFrom);
            queryBuilder.append(" and bbv.date >= '").append(formatDateFrom).append(" 00-00-00'");
        }

        if (dateTo != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(dateTo);
            queryBuilder.append(" and bbv.date <= '").append(formatDateTo).append(" 23-59-59'");
        }
    }

    private void addGlBankFromAndTo(StringBuilder sql, Map<String, Object> params, Integer bankIdFrom, Integer bankIdTo) {
        if (bankIdFrom != null && bankIdFrom > 0) {
            sql.append(" and bbv.bankIdFrom >= :bankIdFrom");
            params.put("bankIdFrom", bankIdFrom);
        }

        if (bankIdTo != null && bankIdTo > 0) {
            sql.append(" and bbv.bankIdTo <= :bankIdTo");
            params.put("bankIdTo", bankIdTo);
        }
    }

    @Override
    public List<BankBalanceView> getAllBankBalancesTransactionsByBankId(Integer branchId, Date dateTo, StringBuilder builder, Date dateFrom) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
//        params.put("glYear", glYear.getYear());
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT bbv FROM BankBalanceView bbv WHERE bbv.branchId = :branchId ");
//        appendDateForQueryBuilder(queryBuilder, glYear.getDateFrom(), dateTo);
        appendDateForQueryBuilder(queryBuilder, dateFrom, dateTo);
        appendBanksIdFromAndTo(queryBuilder, params, builder);
//        queryBuilder.append(" and bbv.glYear = :glYear");
        List<BankBalanceView> data = dao.findListByQuery(queryBuilder.toString(), params);

        return data;
    }

    public List<BankBalanceView> getAllBankBalancesTransactionsByBankIdNew(BankBalanceReportSearchBean bankBalanceReportSearchBean) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", bankBalanceReportSearchBean.getBranchId());
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT bbv FROM BankBalanceView bbv WHERE bbv.branchId = :branchId ");
//        appendDateForQueryBuilder(queryBuilder, glYear.getDateFrom(), dateTo);
        appendDateForQueryBuilder(queryBuilder, bankBalanceReportSearchBean.getDateFrom(), bankBalanceReportSearchBean.getDateTo());
        appendBanksIdFromAndToNew(queryBuilder, params, bankBalanceReportSearchBean.getBankBuilder());
//        queryBuilder.append(" and bbv.glYear = :glYear");
        List<BankBalanceView> data = dao.findListByQuery(queryBuilder.toString(), params);

        return data;
    }

    private void appendBanksIdFromAndTo(StringBuilder sql, Map<String, Object> params, StringBuilder builder) {
        if (builder != null && builder.length() > 0) {
            sql.append(" and (bbv.bankIdFrom in (").append(builder).append(")");
            sql.append(" or bbv.bankIdTo in (").append(builder).append("))");
        }
    }
    
    private void appendBanksIdFromAndToNew(StringBuilder sql, Map<String, Object> params, String bankBuilder) {
        if (bankBuilder != null && !bankBuilder.isEmpty()) {
            sql.append(" and (bbv.bankIdFrom in (").append(bankBuilder).append(")");
            sql.append(" or bbv.bankIdTo in (").append(bankBuilder).append("))");
        }
    }

    private List<BankBalanceReportBean> test(List<BankBalanceView> bankBalanceViewList, BankBalanceReportSearchBean bankBalanceReportSearchBean) {
        List<BankBalanceReportBean> bankBalanceReportBeanList = new ArrayList<>();
        Map<Integer, BankBalanceReportBean> balanceViewMap = new HashMap<>();
        if (bankBalanceViewList != null && !bankBalanceViewList.isEmpty()) {

            for (BankBalanceView balanceReportView : bankBalanceViewList) {
                BankBalanceReportBean balanceReportBean;
                if (balanceReportView.getBankIdTo() != null && bankBalanceReportSearchBean.getSerchedBanksSet().contains(balanceReportView.getBankIdTo())) {
                    balanceReportBean = new BankBalanceReportBean();
                    if (!balanceViewMap.containsKey(balanceReportView.getBankIdTo())) {
                        balanceReportBean.setBankId(balanceReportView.getBankIdTo());
                        balanceReportBean.setSerial(balanceReportView.getSerial());
                        balanceReportBean.setBankName(balanceReportView.getBankNameTo() == null ? balanceReportBean.getBankName() : balanceReportView.getBankNameTo());
                        balanceReportBean.setAccountIdTo(balanceReportView.getAccountIdTo());
                        if (bankBalanceReportSearchBean.getDateFrom() != null &&balanceReportView.getDate().before(bankBalanceReportSearchBean.getDateFrom())) {
                            balanceReportBean.setBankToBalance(balanceReportView.getValueLocal() == null ? BigDecimal.ZERO : balanceReportView.getValueLocal().setScale(2, RoundingMode.UP));
                            balanceReportBean.setPreviousBalance((balanceReportBean.getBankToBalance().subtract(balanceReportBean.getBankFromBalance())).setScale(2, RoundingMode.UP));
                        } else {
                            balanceReportBean.setCredit(BigDecimal.ZERO);
                            balanceReportBean.setDebit(balanceReportView.getValueLocal() != null ? balanceReportView.getValueLocal().setScale(2, RoundingMode.UP) : BigDecimal.ZERO);
                        }
                    } else {
                        balanceReportBean.setBankId(balanceReportView.getBankIdTo());
                        balanceReportBean.setSerial(balanceReportView.getSerial());
                        balanceReportBean.setBankName(balanceReportView.getBankNameTo() == null ? balanceReportBean.getBankName() : balanceReportView.getBankNameTo());
                        balanceReportBean.setAccountIdTo(balanceReportView.getAccountIdTo());
                        balanceReportBean = balanceViewMap.get(balanceReportView.getBankIdTo());
                        if (bankBalanceReportSearchBean.getDateFrom() != null &&balanceReportView.getDate().before(bankBalanceReportSearchBean.getDateFrom())) {
                            balanceReportBean.setBankToBalance(balanceReportView.getValueLocal() == null ? balanceReportBean.getBankToBalance().setScale(2, RoundingMode.UP) : (balanceReportView.getValueLocal().add(balanceReportBean.getBankToBalance())).setScale(2, RoundingMode.UP));
                            balanceReportBean.setPreviousBalance((balanceReportBean.getBankToBalance().subtract(balanceReportBean.getBankFromBalance())).setScale(2, RoundingMode.UP));
                        } else {
                            balanceReportBean.setDebit(balanceReportView.getValueLocal() != null ? (balanceReportBean.getDebit().add(balanceReportView.getValueLocal())).setScale(2, RoundingMode.UP) : balanceReportBean.getDebit().setScale(2, RoundingMode.UP));
                        }
                    }

                    balanceReportBean.setBalance((balanceReportBean.getDebit().subtract(balanceReportBean.getCredit()).add(balanceReportBean.getPreviousBalance() != null ? balanceReportBean.getPreviousBalance() : BigDecimal.ZERO)).setScale(2, RoundingMode.UP));
                    //  addOpeneingBalanceToPreviousBalance(balanceReportBean.getAccountIdTo(), balanceReportBean);
                    balanceViewMap.put(balanceReportView.getBankIdTo(), balanceReportBean);

                }

                if (balanceReportView.getBankIdFrom() != null && bankBalanceReportSearchBean.getSerchedBanksSet().contains(balanceReportView.getBankIdFrom())) {
                    balanceReportBean = new BankBalanceReportBean();
                    if (!balanceViewMap.containsKey(balanceReportView.getBankIdFrom())) {
                        balanceReportBean.setBankId(balanceReportView.getBankIdFrom());
                        balanceReportBean.setSerial(balanceReportView.getSerial());
                        balanceReportBean.setBankName(balanceReportView.getBankNameFrom() == null ? balanceReportBean.getBankName() : balanceReportView.getBankNameFrom());
                        balanceReportBean.setAccountIdFrom(balanceReportView.getAccountIdFrom());
                        if (bankBalanceReportSearchBean.getDateFrom() != null && balanceReportView.getDate().before(bankBalanceReportSearchBean.getDateFrom())) {
                            balanceReportBean.setBankFromBalance(balanceReportView.getValueLocal() == null ? BigDecimal.ZERO : balanceReportView.getValueLocal().setScale(2, RoundingMode.UP));
                            balanceReportBean.setPreviousBalance((balanceReportBean.getBankToBalance().subtract(balanceReportBean.getBankFromBalance())).setScale(2, RoundingMode.UP));
                        } else {
                            balanceReportBean.setCredit(balanceReportView.getValueLocal() != null ? balanceReportView.getValueLocal().setScale(2, RoundingMode.UP) : BigDecimal.ZERO);
                            balanceReportBean.setDebit(BigDecimal.ZERO);
                        }
                    } else {
                        balanceReportBean.setBankId(balanceReportView.getBankIdFrom());
                        balanceReportBean.setSerial(balanceReportView.getSerial());
                        balanceReportBean.setBankName(balanceReportView.getBankNameFrom() == null ? balanceReportBean.getBankName() : balanceReportView.getBankNameFrom());
                        balanceReportBean.setAccountIdFrom(balanceReportView.getAccountIdFrom());
                        balanceReportBean = balanceViewMap.get(balanceReportView.getBankIdFrom());
                        if (bankBalanceReportSearchBean.getDateFrom() != null &&balanceReportView.getDate().before(bankBalanceReportSearchBean.getDateFrom())) {
                            balanceReportBean.setBankFromBalance(balanceReportView.getValueLocal() == null ? balanceReportBean.getBankFromBalance().setScale(2, RoundingMode.UP) : (balanceReportView.getValueLocal().add(balanceReportBean.getBankFromBalance())).setScale(2, RoundingMode.UP));
                            balanceReportBean.setPreviousBalance((balanceReportBean.getBankToBalance().subtract(balanceReportBean.getBankFromBalance())).setScale(2, RoundingMode.UP));

                        } else {
                            balanceReportBean.setCredit(balanceReportView.getValueLocal() != null ? (balanceReportBean.getCredit().add(balanceReportView.getValueLocal()).setScale(2, RoundingMode.UP)) : balanceReportBean.getCredit().setScale(2, RoundingMode.UP));
                        }
                    }
                    balanceReportBean.setBalance((balanceReportBean.getDebit().subtract(balanceReportBean.getCredit()).add(balanceReportBean.getPreviousBalance() != null ? balanceReportBean.getPreviousBalance() : BigDecimal.ZERO)).setScale(2, RoundingMode.UP));
                    //  addOpeneingBalanceToPreviousBalance(balanceReportBean.getAccountIdFrom(), balanceReportBean);
                    balanceViewMap.put(balanceReportView.getBankIdFrom(), balanceReportBean);

                }
            }
        }
        bankBalanceReportBeanList = new ArrayList<>(balanceViewMap.values());
        return bankBalanceReportBeanList;
    }

    public List<BankBalanceReportBean> search(BankBalanceReportSearchBean bankBalanceReportSearchBean) {
        List<BankBalanceReportBean> list = null;
        if (bankBalanceReportSearchBean.getGlBankList() != null && !bankBalanceReportSearchBean.getGlBankList().isEmpty()) {
            StringBuilder bankBuilder = new StringBuilder();
            for (GlBankDTO glBankDTO : bankBalanceReportSearchBean.getGlBankList()) {
                if (bankBuilder != null && bankBuilder.length() == 0) {
                        bankBuilder.append(glBankDTO.getId());
                    } else {
                        bankBuilder.append(",");
                        bankBuilder.append(glBankDTO.getId());
                    }
                bankBalanceReportSearchBean.getSerchedBanksSet().add(glBankDTO.getId());
                bankBalanceReportSearchBean.setBankBuilder(bankBuilder.toString());
            }
            List<BankBalanceView> bankBalanceViewList = getAllBankBalancesTransactionsByBankIdNew(bankBalanceReportSearchBean);
            list = test(bankBalanceViewList, bankBalanceReportSearchBean);
        }
        return list;
    }

}
