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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
    
    public StringBuilder createSqlNew(SubAccountSummarySearchBean subAccountSummarySearchBean) {
        StringBuilder sql = new StringBuilder();
        addCostCenterFromNew(sql, subAccountSummarySearchBean);
        addCostCenterToNew(sql, subAccountSummarySearchBean);
        addAdminUnitIdFromNew(sql, subAccountSummarySearchBean);
        addAdminUnitIdToNew(sql, subAccountSummarySearchBean);
        addAccountIdNew(sql, subAccountSummarySearchBean);
        addDateFromNew(sql, subAccountSummarySearchBean);
        addDateToBeforeNew(sql, subAccountSummarySearchBean);
        return sql;
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

    @Override
    public List<SubAccountSummary> getSubAccountSummaryNew(SubAccountSummarySearchBean subAccountSummarySearchBean, GlYear glYear, boolean extended) {
        Map<String, Object> params = new HashMap();
//        params.put("id", companyId);
        params.put("branchId", subAccountSummarySearchBean.getBranchId());
        StringBuilder builder = new StringBuilder();
        String sql = "";
        builder.append("SELECT \n"
                + "        `gj`.`general_data` AS `date`,\n"
                + "        `d`.`glaccount_id` AS `glaccount_id`,\n"
                + "        `gl`.`name` AS `glAccountName`,\n"
                + "        `gj`.`branch_id` AS `glBranchId`,\n"
                + "        `d`.`admin_unit_id` AS `admin_unit_id`,\n"
                + "        `d`.`cost_center_id` AS `cost_center_id`,\n"
                + "        `gj`.`id` AS `general_Journa_lId`,\n"
                + "        `gj`.`serial` AS `serial`,\n"
                + "        `gj`.`general_decument` AS `general_decument`,\n"
                + "        `gj`.`symbol_id` AS `symbol_id`,\n"
                + "        `s`.`name` AS `symbol_name`,\n"
                + "        `gj`.`general_statement` AS `general_statement`,\n"
                + "        `d`.`discribtion` AS `discribtion`,\n"
                + "        `d`.`debit_amount` AS `debit_amount`,\n"
                + "        `d`.`credit_amount` AS `credit_amount`,\n"
                + "        `d`.`debit_amount_local` AS `debit_amount_foriegn`,\n"
                + "        `d`.`credit_amount_local` AS `credit_amount_foriegn`,\n"
                + "        `s`.`serial` AS `symbol_serial`\n"
                + "    FROM\n"
                + "        (((((`general_journal` `gj`\n"
                + "        LEFT JOIN `general_journal_details` `d` ON ((`gj`.`id` = `d`.`general_journal_id`)))\n"
                + "        LEFT JOIN `gl_account` `gl` ON ((`gl`.`id` = `d`.`glaccount_id`)))\n"
                + "        LEFT JOIN `gl_cost_center` `gcc` ON ((`gcc`.`id` = `d`.`cost_center_id`)))\n"
                + "        LEFT JOIN `gl_admin_unit` `gau` ON ((`gau`.`id` = `d`.`admin_unit_id`)))\n"
                + "        LEFT JOIN `symbol` `s` ON ((`s`.`id` = `gj`.`symbol_id`)))\n"
                + "	where  `gj`.`branch_id` = ").append(subAccountSummarySearchBean.getBranchId());
        if (extended) {
            builder.append(" `s`.`serial` > 0 ").append(createSqlNew(subAccountSummarySearchBean));
        } else {
            builder.append(createSqlNew(subAccountSummarySearchBean));
        }
        builder.append(sql);

        if (!extended) {
            if (glYear.getDateFrom() != null) {
                String d3 = new SimpleDateFormat("yyyy-MM-dd").format(glYear.getDateFrom());
                builder.append(" and gj.general_data >= '").append(d3).append("'");
            }
            if (glYear.getDateTo() != null) {
                String d4 = new SimpleDateFormat("yyyy-MM-dd").format(glYear.getDateTo());
                builder.append(" and gj.general_data <= '").append(d4).append("'");
            }
        }
        
        builder.append(" order by gj.general_data ");
        List<Object[]> res = dao.executeNativeQuery(builder.toString());
        return convertToSubAccountSummary(res);
    }

    public List<SubAccountSummary> convertToSubAccountSummary(List<Object[]> res) {

        List<SubAccountSummary> list = new ArrayList<>();

        Integer index = 1;
        Iterator it = res.iterator();
        while (it.hasNext()) {
            index++;
            Object[] line = (Object[]) it.next();
            SubAccountSummary dto = new SubAccountSummary();
            dto.setRowNum(index);
            dto.setDate((Date) line[0]);
            dto.setGlaccountId((Integer) line[1]);
            dto.setGlAccountName((String) line[2]);
            dto.setGlbranchId((Integer) line[3]);
            dto.setAdminUnitId((Integer) line[4]);
            dto.setCostCenterId((Integer) line[5]);
            dto.setGeneralJournalId((Integer) line[6]);
            dto.setSerial(((Integer) line[7]).toString());
            dto.setGeneralDecument((Integer) line[8]);
            dto.setSymbolId((Integer) line[9]);
            dto.setSymbolName((String) line[10]);
            dto.setGeneralStatement((String) line[11]);
            dto.setDiscribtion((String) line[12]);
            dto.setDebitAmount((BigDecimal) line[13]);
            dto.setCreditAmount((BigDecimal) line[14]);
            dto.setDebitAmountForiegn((BigDecimal) line[15]);
            dto.setCreditAmountForiegn((BigDecimal) line[16]);          

            list.add(dto);
        }
        return list;
    }

    public void addCostCenterFromNew(StringBuilder sql, SubAccountSummarySearchBean subAccountSummarySearchBean) {
        if (subAccountSummarySearchBean.getCostCenterFrom() != null && subAccountSummarySearchBean.getCostCenterFrom() > 0) {
            sql.append(" and d.cost_center_id >= ").append(subAccountSummarySearchBean.getCostCenterFrom());
        }
    }

    public void addCostCenterToNew(StringBuilder sql, SubAccountSummarySearchBean subAccountSummarySearchBean) {
        if (subAccountSummarySearchBean.getCostCenterTo() != null && subAccountSummarySearchBean.getCostCenterTo() > 0) {
            sql.append(" and d.cost_center_id <= ").append(subAccountSummarySearchBean.getCostCenterTo());
        }
    }

    public void addAdminUnitIdFromNew(StringBuilder sql, SubAccountSummarySearchBean subAccountSummarySearchBean) {
        if (subAccountSummarySearchBean.getAdminUnitFrom() != null && subAccountSummarySearchBean.getAdminUnitFrom() > 0) {
            sql.append(" and sas.adminUnitId >= ").append(subAccountSummarySearchBean.getAdminUnitFrom());
        }
    }

    public void addAdminUnitIdToNew(StringBuilder sql, SubAccountSummarySearchBean subAccountSummarySearchBean) {
        if (subAccountSummarySearchBean.getAdminUnitTo() != null && subAccountSummarySearchBean.getAdminUnitTo() > 0) {
            sql.append(" and d.admin_unit_id <= ").append(subAccountSummarySearchBean.getAdminUnitTo());
        }
    }

    public void addAccountIdNew(StringBuilder sql, SubAccountSummarySearchBean subAccountSummarySearchBean) {
        if (subAccountSummarySearchBean.getAccountId() != null && subAccountSummarySearchBean.getAccountId() > 0) {

            sql.append(" and d.glaccount_id = ").append(subAccountSummarySearchBean.getAccountId());
        }
    }

    public void addDateFromNew(StringBuilder sql, SubAccountSummarySearchBean subAccountSummarySearchBean) {
        if (subAccountSummarySearchBean.getDateFrom() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(subAccountSummarySearchBean.getDateFrom());

            sql.append(" and gj.general_data >= '").append(d3).append("'");
        }
    }

    public void addDateToNew(StringBuilder sql, SubAccountSummarySearchBean subAccountSummarySearchBean) {
        if (subAccountSummarySearchBean.getDateTo() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(subAccountSummarySearchBean.getDateTo());

            sql.append(" and gj.general_data <= '").append(d3).append("'");
        }
    }

    public void addDateToBeforeNew(StringBuilder sql, SubAccountSummarySearchBean subAccountSummarySearchBean) {
        if (subAccountSummarySearchBean.getDateTo() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(subAccountSummarySearchBean.getDateTo());

            sql.append(" and gj.general_data < '").append(d3).append("'");
        }
    }
}
