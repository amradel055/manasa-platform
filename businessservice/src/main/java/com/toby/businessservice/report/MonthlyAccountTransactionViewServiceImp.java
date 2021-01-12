package com.toby.businessservice.report;

import com.toby.businessservice.reports.entityBean.MonthlyAccountTransactionBean;
import com.toby.businessservice.reports.searchBean.MonthlyAccountTransactionSearchBean;
import com.toby.core.GenericDAO;
import com.toby.entity.GlAccount;
import com.toby.views.MonthlyAccountTransactionView;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Stateless
public class MonthlyAccountTransactionViewServiceImp implements MonthlyAccountTransactionViewService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<MonthlyAccountTransactionBean> getMonthlyAccountTransactionViewList(MonthlyAccountTransactionSearchBean monthlyAccountTransactionSearchBean, List<GlAccount> glAccountForSummaryList) {

        Map<String, Object> params = new HashMap<>();
        params.put("branchId", monthlyAccountTransactionSearchBean.getBranchId());
        StringBuilder queryBuilder = new StringBuilder();

        appendAccountForQueryBuilder(queryBuilder, params, monthlyAccountTransactionSearchBean);
        appendAdminUnitForQueryBuilder(queryBuilder, params, monthlyAccountTransactionSearchBean);
        appendCostCenterForQueryBuilder(queryBuilder, params, monthlyAccountTransactionSearchBean);
        queryBuilder.append(" order by mat.monthDate");
        //queryBuilder.append("order by mat.monthDate");
        List<MonthlyAccountTransactionView> monthlyAccountTransactionViewList
                = dao.findListByQuery("SELECT mat FROM MonthlyAccountTransactionView mat WHERE mat.glBranchId = :branchId" + queryBuilder, params);

        List<MonthlyAccountTransactionBean> monthlyAccountTransactionBeanList = new ArrayList<>(0);
        for (MonthlyAccountTransactionView monthlyAccountTransactionView : monthlyAccountTransactionViewList) {
            MonthlyAccountTransactionBean monthlyAccountTransactionBean = new MonthlyAccountTransactionBean();
            monthlyAccountTransactionBean.setRowNum(monthlyAccountTransactionView.getRowNum());
            monthlyAccountTransactionBean.setMonthDate(monthlyAccountTransactionView.getMonthDate());
            monthlyAccountTransactionBean.setDebitAmount(monthlyAccountTransactionView.getDebitAmount());
            monthlyAccountTransactionBean.setCreditAmount(monthlyAccountTransactionView.getCreditAmount());
            monthlyAccountTransactionBean.setAccClass(monthlyAccountTransactionView.getAccClass());
            monthlyAccountTransactionBeanList.add(monthlyAccountTransactionBean);
        }

        /**
         * Make HashMap to collect debit and credit amount group by month date
         */
        HashMap<String, MonthlyAccountTransactionBean> monthlyAccountTransactionHashMap = new HashMap<>();
        for (MonthlyAccountTransactionBean monthlyAccountTransactionBean : monthlyAccountTransactionBeanList) {
            if (monthlyAccountTransactionBean.getDebitAmount() == null) {
                monthlyAccountTransactionBean.setDebitAmount(BigDecimal.ZERO);
            }
            if (monthlyAccountTransactionBean.getCreditAmount() == null) {
                monthlyAccountTransactionBean.setCreditAmount(BigDecimal.ZERO);
            }
            if (monthlyAccountTransactionHashMap.containsKey(monthlyAccountTransactionBean.getMonthDate())) {
                MonthlyAccountTransactionBean monthlyAccountTransactionBean1
                        = monthlyAccountTransactionHashMap.get(monthlyAccountTransactionBean.getMonthDate());
                monthlyAccountTransactionBean1.setDebitAmount(
                        monthlyAccountTransactionBean1.getDebitAmount().add(monthlyAccountTransactionBean.getDebitAmount()));
                monthlyAccountTransactionBean1.setCreditAmount(
                        monthlyAccountTransactionBean1.getCreditAmount().add(monthlyAccountTransactionBean.getCreditAmount()));
                monthlyAccountTransactionHashMap.put(monthlyAccountTransactionBean.getMonthDate(), monthlyAccountTransactionBean1);

            } else {
                monthlyAccountTransactionHashMap.put(monthlyAccountTransactionBean.getMonthDate(), monthlyAccountTransactionBean);
            }
        }
        int count = 0;
        for (MonthlyAccountTransactionBean monthlyAccountTransactionBean : monthlyAccountTransactionHashMap.values()) {
            if (count == 0) {
                if (monthlyAccountTransactionBean.getAccClass() != null && monthlyAccountTransactionBean.getAccClass().equals("CREDIT")) {
                    monthlyAccountTransactionBean.setBalance(monthlyAccountTransactionBean.getCreditAmount().subtract(monthlyAccountTransactionBean.getDebitAmount()));
                } else {
                    monthlyAccountTransactionBean.setBalance(monthlyAccountTransactionBean.getDebitAmount().subtract(monthlyAccountTransactionBean.getCreditAmount()));
                }
            } else {
                if (monthlyAccountTransactionBean.getAccClass() != null && monthlyAccountTransactionBean.getAccClass().equals("CREDIT")) {
                    monthlyAccountTransactionBean.setBalance(new ArrayList<>(monthlyAccountTransactionHashMap.values()).get(count - 1).getBalance()
                            .add(monthlyAccountTransactionBean.getCreditAmount().subtract(monthlyAccountTransactionBean.getDebitAmount())));
                } else {

                    monthlyAccountTransactionBean.setBalance(new ArrayList<>(monthlyAccountTransactionHashMap.values()).get(count - 1).getBalance()
                            .add(monthlyAccountTransactionBean.getDebitAmount().subtract(monthlyAccountTransactionBean.getCreditAmount())));
                }
            }
            count++;
        }
        Map<String, MonthlyAccountTransactionBean> sortedMapAsc = sortByComparator(monthlyAccountTransactionHashMap);

        return new ArrayList<>(sortedMapAsc.values());
    }

    private void appendCostCenterForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, MonthlyAccountTransactionSearchBean monthlyAccountTransactionSearchBean) {
        if (monthlyAccountTransactionSearchBean.getCostCenterId() != null && monthlyAccountTransactionSearchBean.getCostCenterId() != 0) {
            params.put("costCenterId", monthlyAccountTransactionSearchBean.getCostCenterId());
            dao.checkAndQuery(queryBuilder);
            queryBuilder.append(" mat.costCenterId = :costCenterId ");
        }
    }

    private void appendAdminUnitForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, MonthlyAccountTransactionSearchBean monthlyAccountTransactionSearchBean) {
        if (monthlyAccountTransactionSearchBean.getAdminUnitId() != null && monthlyAccountTransactionSearchBean.getAdminUnitId() != 0) {
            params.put("adminUnitId", monthlyAccountTransactionSearchBean.getAdminUnitId());
            dao.checkAndQuery(queryBuilder);
            queryBuilder.append(" mat.adminUnitId = :adminUnitId ");
        }
    }

    private void appendAccountForQueryBuilder(StringBuilder queryBuilder, Map<String, Object> params, MonthlyAccountTransactionSearchBean monthlyAccountTransactionSearchBean) {
        if (monthlyAccountTransactionSearchBean.getAccountCodeFrom() != null && monthlyAccountTransactionSearchBean.getAccountCodeFrom() > 0) {
            params.put("accountFrom", monthlyAccountTransactionSearchBean.getAccountCodeFrom());
            queryBuilder.append(" and mat.glAccountCode = :accountFrom");
        }
        /*if (monthlyAccountTransactionSearchBean.getAccountCodeTo()!= null && monthlyAccountTransactionSearchBean.getAccountCodeTo()< 0) {
         params.put("accountTo", monthlyAccountTransactionSearchBean.getAccountCodeTo());
         queryBuilder.append(" and mat.glAccountCode >= :accountTo ");
         }*/
    }

    private static Map<String, MonthlyAccountTransactionBean> sortByComparator(Map<String, MonthlyAccountTransactionBean> unsortMap) {

        List<Map.Entry<String, MonthlyAccountTransactionBean>> list = new LinkedList<Map.Entry<String, MonthlyAccountTransactionBean>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<String, MonthlyAccountTransactionBean>>() {
            @Override
            public int compare(Map.Entry<String, MonthlyAccountTransactionBean> o1,
                    Map.Entry<String, MonthlyAccountTransactionBean> o2) {
                return o1.getValue().getRowNum().compareTo(o2.getValue().getRowNum());
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, MonthlyAccountTransactionBean> sortedMap = new LinkedHashMap<String, MonthlyAccountTransactionBean>();
        for (Map.Entry<String, MonthlyAccountTransactionBean> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}
