package com.toby.businessservice.report;

import com.toby.businessservice.GlYearService;
import com.toby.businessservice.reports.searchBean.SubAccountSummarySearchBean;
import com.toby.core.GenericDAO;
import com.toby.entity.GlYear;
import com.toby.views.SubAccountSummary;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class SubAccountSummaryServiceImpl implements SubAccountSummaryService {

    @EJB
    private GenericDAO dao;
    @EJB
    private GlYearService glYearService;

    @Override
    public List<SubAccountSummary> getSubAccountSummary(SubAccountSummarySearchBean subAccountSummarySearchBean, GlYear glYear, boolean extended) {
        Map<String, Object> params = new HashMap();
//        params.put("id", companyId);
        params.put("branchId", subAccountSummarySearchBean.getBranchId());
        StringBuilder builder = new StringBuilder();
        String sql = "";
        if (extended) {
            sql = "SELECT sas FROM SubAccountSummary sas WHERE sas.glbranchId = :branchId and sas.symbolSerial > 0 " + createSql(subAccountSummarySearchBean);
        } else {
            sql = "SELECT sas FROM SubAccountSummary sas WHERE sas.glbranchId = :branchId " + createSql(subAccountSummarySearchBean);
        }
        builder.append(sql);

        if (!extended) {
            if (glYear.getDateFrom() != null) {
                String d3 = new SimpleDateFormat("yyyy-MM-dd").format(glYear.getDateFrom());
                builder.append(" and sas.date >= '").append(d3).append("'");
            }
            if (glYear.getDateTo() != null) {
                String d4 = new SimpleDateFormat("yyyy-MM-dd").format(glYear.getDateTo());
                builder.append(" and sas.date <= '").append(d4).append("'");
            }
        }
        List<SubAccountSummary> data = dao.findListByQuery(builder.toString(), params);

        return data;
    }

    @Override
    public BigDecimal getBeforeBalance(SubAccountSummarySearchBean subAccountSummarySearchBean, GlYear glYear, boolean extended) {

        Map<String, Object> params = new HashMap();
        params.put("branchId", subAccountSummarySearchBean.getBranchId());
        subAccountSummarySearchBean.setDateTo(subAccountSummarySearchBean.getDateFrom());
        subAccountSummarySearchBean.setDateFrom(glYear.getDateFrom());
        StringBuilder builder = new StringBuilder();
        String sql = "";
        if ("DEBIT".equalsIgnoreCase(subAccountSummarySearchBean.getAccClass())) {
            sql = "SELECT  SUM(COALESCE(sas.debitAmount , 0)) - SUM(COALESCE(sas.creditAmount , 0)) FROM SubAccountSummary sas WHERE sas.glbranchId = :branchId " + createBeforeSql(subAccountSummarySearchBean).toString();
        } else if ("Credit".equalsIgnoreCase(subAccountSummarySearchBean.getAccClass())) {
            sql = "SELECT  SUM(COALESCE(sas.creditAmount , 0)) - SUM(COALESCE(sas.debitAmount , 0)) FROM SubAccountSummary sas WHERE sas.glbranchId = :branchId " + createBeforeSql(subAccountSummarySearchBean).toString();
        }

        builder.append(sql);
        if (!extended) {
            if (glYear.getDateFrom() != null) {
                String d3 = new SimpleDateFormat("yyyy-MM-dd").format(glYear.getDateFrom());
                builder.append(" and sas.date >= '").append(d3).append("'");
            }
            if (glYear.getDateTo() != null) {
                String d4 = new SimpleDateFormat("yyyy-MM-dd").format(glYear.getDateTo());
                builder.append(" and sas.date <= '").append(d4).append("'");
            }
        }
        BigDecimal beforeBalance = dao.findEntityByQuery(builder.toString(), params);
        beforeBalance = beforeBalance != null ? beforeBalance : BigDecimal.ZERO;
        return beforeBalance;
    }

    public StringBuilder createSql(SubAccountSummarySearchBean subAccountSummarySearchBean) {
        StringBuilder sql = new StringBuilder();
        addCostCenterFrom(sql, subAccountSummarySearchBean);
        addCostCenterTo(sql, subAccountSummarySearchBean);
        addAdminUnitIdFrom(sql, subAccountSummarySearchBean);
        addAdminUnitIdTo(sql, subAccountSummarySearchBean);
        addAccountId(sql, subAccountSummarySearchBean);
        addDateFrom(sql, subAccountSummarySearchBean);
        addDateTo(sql, subAccountSummarySearchBean);
        return sql;
    }

    public StringBuilder createBeforeSql(SubAccountSummarySearchBean subAccountSummarySearchBean) {
        StringBuilder sql = new StringBuilder();
        addCostCenterFrom(sql, subAccountSummarySearchBean);
        addCostCenterTo(sql, subAccountSummarySearchBean);
        addAdminUnitIdFrom(sql, subAccountSummarySearchBean);
        addAdminUnitIdTo(sql, subAccountSummarySearchBean);
        addAccountId(sql, subAccountSummarySearchBean);
        addDateFrom(sql, subAccountSummarySearchBean);
        addDateToBefore(sql, subAccountSummarySearchBean);
        return sql;
    }

    public void addCostCenterFrom(StringBuilder sql, SubAccountSummarySearchBean subAccountSummarySearchBean) {
        if (subAccountSummarySearchBean.getCostCenterFrom() != null && subAccountSummarySearchBean.getCostCenterFrom() > 0) {
            sql.append(" and sas.costCenterId >= ").append(subAccountSummarySearchBean.getCostCenterFrom());
        }
    }

    public void addCostCenterTo(StringBuilder sql, SubAccountSummarySearchBean subAccountSummarySearchBean) {
        if (subAccountSummarySearchBean.getCostCenterTo() != null && subAccountSummarySearchBean.getCostCenterTo() > 0) {
            sql.append(" and sas.costCenterId <= ").append(subAccountSummarySearchBean.getCostCenterTo());
        }
    }

    public void addAdminUnitIdFrom(StringBuilder sql, SubAccountSummarySearchBean subAccountSummarySearchBean) {
        if (subAccountSummarySearchBean.getAdminUnitFrom() != null && subAccountSummarySearchBean.getAdminUnitFrom() > 0) {
            sql.append(" and sas.adminUnitId >= ").append(subAccountSummarySearchBean.getAdminUnitFrom());
        }
    }

    public void addAdminUnitIdTo(StringBuilder sql, SubAccountSummarySearchBean subAccountSummarySearchBean) {
        if (subAccountSummarySearchBean.getAdminUnitTo() != null && subAccountSummarySearchBean.getAdminUnitTo() > 0) {
            sql.append(" and sas.adminUnitId <= ").append(subAccountSummarySearchBean.getAdminUnitTo());
        }
    }

    public void addAccountId(StringBuilder sql, SubAccountSummarySearchBean subAccountSummarySearchBean) {
        if (subAccountSummarySearchBean.getAccountId() != null && subAccountSummarySearchBean.getAccountId() > 0) {

            sql.append(" and sas.glaccountId = ").append(subAccountSummarySearchBean.getAccountId());
        }
    }

    public void addDateFrom(StringBuilder sql, SubAccountSummarySearchBean subAccountSummarySearchBean) {
        if (subAccountSummarySearchBean.getDateFrom() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(subAccountSummarySearchBean.getDateFrom());

            sql.append(" and sas.date >= '").append(d3).append("'");
        }
//        else{
//            GlYear glYear = glYearService.findYear(subAccountSummarySearchBean.getFinancailYear());
//            sql.append(" sas.date >= '").append(glYear.getDateFrom()).append("'");
//        }
    }

    public void addDateTo(StringBuilder sql, SubAccountSummarySearchBean subAccountSummarySearchBean) {
        if (subAccountSummarySearchBean.getDateTo() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(subAccountSummarySearchBean.getDateTo());

            sql.append(" and sas.date <= '").append(d3).append("'");
        }
//        else{
//            GlYear glYear = glYearService.findYear(subAccountSummarySearchBean.getFinancailYear());
//            sql.append(" sas.date <= '").append(glYear.getDateTo()).append("'");
//        }
    }

    public void addDateToBefore(StringBuilder sql, SubAccountSummarySearchBean subAccountSummarySearchBean) {
        if (subAccountSummarySearchBean.getDateTo() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(subAccountSummarySearchBean.getDateTo());

            sql.append(" and sas.date < '").append(d3).append("'");
        }
    }

}
