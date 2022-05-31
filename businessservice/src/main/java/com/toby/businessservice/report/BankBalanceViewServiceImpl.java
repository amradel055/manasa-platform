/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.bean.BankBalanceReportBean;
import com.toby.bean.BankBalanceReportSearchBean;
import com.toby.businessservice.reports.searchBean.SubAccountSummarySearchBean;
import com.toby.core.GenericDAO;
import com.toby.dto.GlBankDTO;
import com.toby.entity.GlYear;
import com.toby.views.BankBalanceView;
import com.toby.views.BankBalanceViewNew;
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

    public List<BankBalanceViewNew> getAllBankBalancesTransactionsByBankIdNativ(BankBalanceReportSearchBean bankBalanceReportSearchBean) {

        dao.executeNativeQuery("select toby.sql_mode_function()");
        Integer branchId = bankBalanceReportSearchBean.getBranchId();
        String bankBuilder = bankBalanceReportSearchBean.getBankBuilder();
        String dateFrom = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(bankBalanceReportSearchBean.getDateFrom());
        String dateTo = new SimpleDateFormat("yyyy-MM-dd  23:59:59").format(bankBalanceReportSearchBean.getDateTo());

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("    SELECT \n"
                + "\n"
                + "        `gb`.`date_openinng_balance` AS `date`,\n"
                + "        '' AS `remark`,\n"
                + "        NULL AS `gl_year`,\n"
                + "        `gb`.`code` AS `serial`,\n"
                + "        `gb`.`openinng_balance` AS `glValue`,\n"
                + "        `gb`.`openinng_balance` AS `value_local`,\n"
                + "        NULL AS `transactionType`,\n"
                + "        `gb`.`id` AS `bankIdTo`,\n"
                + "        NULL AS `bankIdfrom`,\n"
                + "        `gb`.`name` AS `bankNameTo`,\n"
                + "        '' AS `bankNameFrom`,\n"
                + "        `gb`.`account_id` AS `accountIdTo`,\n"
                + "        NULL AS `accountIdFrom`,\n"
                + "        `gb`.`branch_id` AS `branchId`,\n"
                + "        `gb`.`code` AS `bankCodeto`,\n"
                + "        `gb`.`code` AS `bankCodeFrom`\n"
                + "    FROM\n"
                + "        `gl_bank` `gb`\n"
                + "    WHERE\n"
                + "        ((`gb`.`date_openinng_balance` IS NOT NULL)\n"
                + "            AND (`gb`.`openinng_balance` > 0)) AND  `gb`.`branch_id` = ").append(branchId).append(
                " And `gb`.`id` IN ( ").append(bankBuilder).append(
                "  )          UNION ALL SELECT \n"
                + "        `glb`.`date` AS `date`,\n"
                + "        `glb`.`remark` AS `remark`,\n"
                + "        `glb`.`gl_year` AS `gl_year`,\n"
                + "        `glb`.`serial` AS `serial`,\n"
                + "        `glbt`.`value` AS `glValue`,\n"
                + "        `glbt`.`value_local` AS `value_local`,\n"
                + "        `glb`.`transaction_type` AS `transactionType`,\n"
                + "        `glbt`.`bank_id_to` AS `bankIdTo`,\n"
                + "        `glbt`.`bank_id_from` AS `bankIdfrom`,\n"
                + "        `glb1`.`name` AS `bankNameTo`,\n"
                + "        `glb2`.`name` AS `bankNameFrom`,\n"
                + "        `glb1`.`account_id` AS `accountIdTo`,\n"
                + "        `glb2`.`account_id` AS `accountIdFrom`,\n"
                + "        `glb`.`branch_id` AS `branchId`,\n"
                + "        `glb1`.`code` AS `bankCodeto`,\n"
                + "        `glb2`.`code` AS `bankCodeFrom`\n"
                + "    FROM\n"
                + "        (((`gl_bank_transaction` `glb`\n"
                + "        LEFT JOIN `gl_bank_transaction_detail` `glbt` ON ((`glb`.`id` = `glbt`.`gl_bank_transaction_id`)))\n"
                + "        LEFT JOIN `gl_bank` `glb1` ON ((`glb1`.`id` = `glbt`.`bank_id_to`)))\n"
                + "        LEFT JOIN `gl_bank` `glb2` ON ((`glb2`.`id` = `glbt`.`bank_id_from`)))\n"
                + "    WHERE\n"
                + "        (`glb`.`transaction_type` = 2)  AND  `glb`.`branch_id` = ").append(branchId).append(" And  (`glb1`.`id` IN (").append(bankBuilder).append(") OR `glb2`.`id` IN (").append(bankBuilder).append("))\n"
                + "    UNION ALL SELECT \n"
                + "      \n"
                + "        `glb`.`date` AS `date`,\n"
                + "        `glb`.`remark` AS `remark`,\n"
                + "        `glb`.`gl_year` AS `gl_year`,\n"
                + "        `glb`.`serial` AS `serial`,\n"
                + "        `glbt`.`value` AS `glValue`,\n"
                + "        `glbt`.`value_local` AS `value_local`,\n"
                + "        `glb`.`transaction_type` AS `transactionType`,\n"
                + "        `glbt`.`bank_id_to` AS `bankIdTo`,\n"
                + "        `glbt`.`bank_id_from` AS `bankIdfrom`,\n"
                + "        `glb1`.`name` AS `bankNameTo`,\n"
                + "        `glb2`.`name` AS `bankNameFrom`,\n"
                + "        `glb1`.`account_id` AS `accountIdTo`,\n"
                + "        `glb2`.`account_id` AS `accountIdFrom`,\n"
                + "        `glb`.`branch_id` AS `branchId`,\n"
                + "        `glb1`.`code` AS `bankCodeto`,\n"
                + "        `glb2`.`code` AS `bankCodeFrom`\n"
                + "    FROM\n"
                + "        (((`gl_bank_transaction` `glb`\n"
                + "        LEFT JOIN `gl_bank_transaction_detail` `glbt` ON ((`glb`.`id` = `glbt`.`gl_bank_transaction_id`)))\n"
                + "        LEFT JOIN `gl_bank` `glb1` ON ((`glb1`.`id` = `glbt`.`bank_id_to`)))\n"
                + "        LEFT JOIN `gl_bank` `glb2` ON ((`glb2`.`id` = `glbt`.`bank_id_from`)))\n"
                + "    WHERE\n"
                + "        ((`glb`.`transaction_type` = 1)\n"
                + "            OR (`glb`.`transaction_type` = 0))  AND  `glb`.`branch_id` = ").append(branchId).append("  And (`glb1`.`id` IN (").append(bankBuilder).append(") OR `glb2`.`id` IN (").append(bankBuilder).append(")and `glb`.`date` <= '").append(dateTo).append("'").append(")\n"
                + "            \n"
                + "            \n"
                + "    UNION ALL SELECT \n"
                + "      \n"
                + "        `ipi`.`date` AS `date`,\n"
                + "        `ipi`.`remarks` AS `remark`,\n"
                + "        NULL AS `gl_year`,\n"
                + "        `ipi`.`serial` AS `serial`,\n"
                + "        IFNULL(NET(IFNULL(SUM(`ipid`.`net`), 0.0),IFNULL(`ipi`.`discount_type`, 0.0),IFNULL(`ipi`.`discount`, 0.0),\n"
                + "                IFNULL(`ipi`.`tax_flag`, 0.0),15,IFNULL(`ipi`.`disc_halala`, 0.0)),0.0) AS `glValue`,\n"
                + "        IFNULL(NET(IFNULL(SUM(`ipid`.`net`), 0.0),IFNULL(`ipi`.`discount_type`, 0.0),IFNULL(`ipi`.`discount`, 0.0),\n"
                + "                IFNULL(`ipi`.`tax_flag`, 0.0),15,IFNULL(`ipi`.`disc_halala`, 0.0)),0.0)  AS `value_local`,\n"
                + "        `ipi`.`type` AS `transactionType`,\n"
                + "        (CASE\n"
                + "            WHEN (`ipi`.`type` = 0) THEN NULL\n"
                + "            WHEN (`ipi`.`type` = 1) THEN `ipi`.`gl_bank_id`\n"
                + "        END) AS `bankIdTo`,\n"
                + "        (CASE\n"
                + "            WHEN (`ipi`.`type` = 0) THEN `ipi`.`gl_bank_id`\n"
                + "            WHEN (`ipi`.`type` = 1) THEN NULL\n"
                + "        END) AS `bankIdfrom`,\n"
                + "        (CASE\n"
                + "            WHEN (`ipi`.`type` = 0) THEN NULL\n"
                + "            WHEN (`ipi`.`type` = 1) THEN `glb1`.`name`\n"
                + "        END) AS `bankNameTo`,\n"
                + "        (CASE\n"
                + "            WHEN (`ipi`.`type` = 0) THEN `glb1`.`name`\n"
                + "            WHEN (`ipi`.`type` = 1) THEN NULL\n"
                + "        END) AS `bankNameFrom`,\n"
                + "        `glb1`.`account_id` AS `accountIdTo`,\n"
                + "        `glb1`.`account_id` AS `accountIdFrom`,\n"
                + "        `ipi`.`branch_id` AS `branchId`,\n"
                + "        (CASE\n"
                + "            WHEN (`ipi`.`type` = 0) THEN NULL\n"
                + "            WHEN (`ipi`.`type` = 1) THEN `glb1`.`code`\n"
                + "        END) AS `bankCodeto`,\n"
                + "        (CASE\n"
                + "            WHEN (`ipi`.`type` = 0) THEN `glb1`.`code`\n"
                + "            WHEN (`ipi`.`type` = 1) THEN NULL\n"
                + "        END) AS `bankCodeFrom`\n"
                + "    FROM\n"
                + "       `inv_purchase_invoice` `ipi`  LEFT JOIN  `inv_purchase_invoice_detail` `ipid`  ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
                + "        LEFT JOIN `gl_bank` `glb1` ON `glb1`.`id` = `ipi`.`gl_bank_id`\n"
                + "    WHERE\n"
                + "        (`ipi`.`payment_type` = 0)  AND  `ipi`.`branch_id` = ").append(branchId).append("  And (`ipi`.`gl_bank_id` IN (").append(bankBuilder).append("))\n"
                + "        group by ipi.id");
//        appendDateForQueryBuilder(queryBuilder, glYear.getDateFrom(), dateTo);
//        appendDateForQueryBuilder(queryBuilder, bankBalanceReportSearchBean.getDateFrom(), bankBalanceReportSearchBean.getDateTo());
//        appendBanksIdFromAndToNew(queryBuilder, params,);
//        queryBuilder.append(" and bbv.glYear = :glYear");

        List<Object[]> data = dao.executeNativeQuery(queryBuilder.toString());
        return returnBankBalanceData(data);
    }

    public List<BankBalanceViewNew> getAllBankBalancesTransactionsByBankIdNativNew(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {

        dao.executeNativeQuery("select toby.sql_mode_function()");
        Integer branchId = cashAccountSettlementSearchBean.getBranchId();
        String bankBuilder = cashAccountSettlementSearchBean.getBankBuilder();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("    SELECT \n"
                + "\n"
                + "        `gb`.`date_openinng_balance` AS `date`,\n"
                + "        '' AS `remark`,\n"
                + "        NULL AS `gl_year`,\n"
                + "        `gb`.`code` AS `serial`,\n"
                + "        `gb`.`openinng_balance` AS `glValue`,\n"
                + "        `gb`.`openinng_balance` AS `value_local`,\n"
                + "        NULL AS `transactionType`,\n"
                + "        `gb`.`id` AS `bankIdTo`,\n"
                + "        NULL AS `bankIdfrom`,\n"
                + "        `gb`.`name` AS `bankNameTo`,\n"
                + "        '' AS `bankNameFrom`,\n"
                + "        `gb`.`account_id` AS `accountIdTo`,\n"
                + "        NULL AS `accountIdFrom`,\n"
                + "        `gb`.`branch_id` AS `branchId`,\n"
                + "        `gb`.`code` AS `bankCodeto`,\n"
                + "        `gb`.`code` AS `bankCodeFrom`,\n"
                + "        `gb`.`cash` AS `cashto`,\n"
                + "        `gb`.`cash` AS `cashfrom`\n"
                + "    FROM\n"
                + "        `gl_bank` `gb`\n"
                + "    WHERE\n"
                + "        ((`gb`.`date_openinng_balance` IS NOT NULL)\n"
                + "            AND (`gb`.`openinng_balance` > 0)) AND  `gb`.`branch_id` = ").append(branchId).append(
                " And `gb`.`id` IN ( ").append(bankBuilder).append(
                "  )          UNION ALL SELECT \n"
                + "        `glb`.`date` AS `date`,\n"
                + "        `glb`.`remark` AS `remark`,\n"
                + "        `glb`.`gl_year` AS `gl_year`,\n"
                + "        `glb`.`serial` AS `serial`,\n"
                + "        `glbt`.`value` AS `glValue`,\n"
                + "        `glbt`.`value_local` AS `value_local`,\n"
                + "        `glb`.`transaction_type` AS `transactionType`,\n"
                + "        `glbt`.`bank_id_to` AS `bankIdTo`,\n"
                + "        `glbt`.`bank_id_from` AS `bankIdfrom`,\n"
                + "        `glb1`.`name` AS `bankNameTo`,\n"
                + "        `glb2`.`name` AS `bankNameFrom`,\n"
                + "        `glb1`.`account_id` AS `accountIdTo`,\n"
                + "        `glb2`.`account_id` AS `accountIdFrom`,\n"
                + "        `glb`.`branch_id` AS `branchId`,\n"
                + "        `glb1`.`code` AS `bankCodeto`,\n"
                + "        `glb2`.`code` AS `bankCodeFrom`,\n"
                + "        `glb1`.`cash` AS `cashto`,\n"
                + "        `glb2`.`cash` AS `cashfrom`\n"
                + "    FROM\n"
                + "        (((`gl_bank_transaction` `glb`\n"
                + "        LEFT JOIN `gl_bank_transaction_detail` `glbt` ON ((`glb`.`id` = `glbt`.`gl_bank_transaction_id`)))\n"
                + "        LEFT JOIN `gl_bank` `glb1` ON ((`glb1`.`id` = `glbt`.`bank_id_to`)))\n"
                + "        LEFT JOIN `gl_bank` `glb2` ON ((`glb2`.`id` = `glbt`.`bank_id_from`)))\n"
                + "    WHERE\n"
                + "        (`glb`.`transaction_type` = 2)  AND  `glb`.`branch_id` = ").append(branchId).append(" And  (`glb1`.`id` IN (").append(bankBuilder).append(") OR `glb2`.`id` IN (").append(bankBuilder).append("))\n"
                + "    UNION ALL SELECT \n"
                + "      \n"
                + "        `glb`.`date` AS `date`,\n"
                + "        `glb`.`remark` AS `remark`,\n"
                + "        `glb`.`gl_year` AS `gl_year`,\n"
                + "        `glb`.`serial` AS `serial`,\n"
                + "        `glbt`.`value` AS `glValue`,\n"
                + "        `glbt`.`value_local` AS `value_local`,\n"
                + "        `glb`.`transaction_type` AS `transactionType`,\n"
                + "        `glbt`.`bank_id_to` AS `bankIdTo`,\n"
                + "        `glbt`.`bank_id_from` AS `bankIdfrom`,\n"
                + "        `glb1`.`name` AS `bankNameTo`,\n"
                + "        `glb2`.`name` AS `bankNameFrom`,\n"
                + "        `glb1`.`account_id` AS `accountIdTo`,\n"
                + "        `glb2`.`account_id` AS `accountIdFrom`,\n"
                + "        `glb`.`branch_id` AS `branchId`,\n"
                + "        `glb1`.`code` AS `bankCodeto`,\n"
                + "        `glb2`.`code` AS `bankCodeFrom`,\n"
                + "        `glb1`.`cash` AS `cashto`,\n"
                + "        `glb2`.`cash` AS `cashfrom`\n"
                + "    FROM\n"
                + "        (((`gl_bank_transaction` `glb`\n"
                + "        LEFT JOIN `gl_bank_transaction_detail` `glbt` ON ((`glb`.`id` = `glbt`.`gl_bank_transaction_id`)))\n"
                + "        LEFT JOIN `gl_bank` `glb1` ON ((`glb1`.`id` = `glbt`.`bank_id_to`)))\n"
                + "        LEFT JOIN `gl_bank` `glb2` ON ((`glb2`.`id` = `glbt`.`bank_id_from`)))\n"
                + "    WHERE\n"
                + "        ((`glb`.`transaction_type` = 1)\n"
                + "            OR (`glb`.`transaction_type` = 0))  AND  `glb`.`branch_id` = ").append(branchId).append("  And (`glb1`.`id` IN (").append(bankBuilder).append(") OR `glb2`.`id` IN (").append(bankBuilder).append("))\n"
                + "            \n"
                + "            \n"
                + "    UNION ALL SELECT \n"
                + "      \n"
                + "        `ipi`.`date` AS `date`,\n"
                + "        `ipi`.`remarks` AS `remark`,\n"
                + "        NULL AS `gl_year`,\n"
                + "        `ipi`.`serial` AS `serial`,\n"
                + "        IFNULL(NET(IFNULL(SUM(`ipid`.`net`), 0.0),IFNULL(`ipi`.`discount_type`, 0.0),IFNULL(`ipi`.`discount`, 0.0),\n"
                + "                IFNULL(`ipi`.`tax_flag`, 0.0),15,IFNULL(`ipi`.`disc_halala`, 0.0)),0.0) AS `glValue`,\n"
                + "        IFNULL(NET(IFNULL(SUM(`ipid`.`net`), 0.0),IFNULL(`ipi`.`discount_type`, 0.0),IFNULL(`ipi`.`discount`, 0.0),\n"
                + "                IFNULL(`ipi`.`tax_flag`, 0.0),15,IFNULL(`ipi`.`disc_halala`, 0.0)),0.0)  AS `value_local`,\n"
                + "        `ipi`.`type` AS `transactionType`,\n"
                + "        (CASE\n"
                + "            WHEN (`ipi`.`type` = 0) THEN NULL\n"
                + "            WHEN (`ipi`.`type` = 1) THEN `ipi`.`gl_bank_id`\n"
                + "        END) AS `bankIdTo`,\n"
                + "        (CASE\n"
                + "            WHEN (`ipi`.`type` = 0) THEN `ipi`.`gl_bank_id`\n"
                + "            WHEN (`ipi`.`type` = 1) THEN NULL\n"
                + "        END) AS `bankIdfrom`,\n"
                + "        (CASE\n"
                + "            WHEN (`ipi`.`type` = 0) THEN NULL\n"
                + "            WHEN (`ipi`.`type` = 1) THEN `glb1`.`name`\n"
                + "        END) AS `bankNameTo`,\n"
                + "        (CASE\n"
                + "            WHEN (`ipi`.`type` = 0) THEN `glb1`.`name`\n"
                + "            WHEN (`ipi`.`type` = 1) THEN NULL\n"
                + "        END) AS `bankNameFrom`,\n"
                + "        `glb1`.`account_id` AS `accountIdTo`,\n"
                + "        `glb1`.`account_id` AS `accountIdFrom`,\n"
                + "        `ipi`.`branch_id` AS `branchId`,\n"
                + "        (CASE\n"
                + "            WHEN (`ipi`.`type` = 0) THEN NULL\n"
                + "            WHEN (`ipi`.`type` = 1) THEN `glb1`.`code`\n"
                + "        END) AS `bankCodeto`,\n"
                + "        (CASE\n"
                + "            WHEN (`ipi`.`type` = 0) THEN `glb1`.`code`\n"
                + "            WHEN (`ipi`.`type` = 1) THEN NULL\n"
                + "        END) AS `bankCodeFrom`,\n"
                + "        (CASE\n"
                + "            WHEN (`ipi`.`type` = 0) THEN NULL\n"
                + "            WHEN (`ipi`.`type` = 1) THEN `glb1`.`cash`\n"
                + "        END) AS `cashto`,\n"
                + "        (CASE\n"
                + "            WHEN (`ipi`.`type` = 0) THEN `glb1`.`cash`\n"
                + "            WHEN (`ipi`.`type` = 1) THEN NULL\n"
                + "        END) AS `cashfrom`\n"
                + "    FROM\n"
                + "       `inv_purchase_invoice` `ipi`  LEFT JOIN  `inv_purchase_invoice_detail` `ipid`  ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
                + "        LEFT JOIN `gl_bank` `glb1` ON `glb1`.`id` = `ipi`.`gl_bank_id`\n"
                + "    WHERE\n"
                + "        (`ipi`.`payment_type` = 0)  AND  `ipi`.`branch_id` = ").append(branchId).append("  And (`ipi`.`gl_bank_id` IN (").append(bankBuilder).append("))\n"
                + "        group by ipi.id");
//        appendDateForQueryBuilder(queryBuilder, glYear.getDateFrom(), dateTo);
//        appendDateForQueryBuilder(queryBuilder, bankBalanceReportSearchBean.getDateFrom(), bankBalanceReportSearchBean.getDateTo());
//        appendBanksIdFromAndToNew(queryBuilder, params,);
//        queryBuilder.append(" and bbv.glYear = :glYear");

        List<Object[]> data = dao.executeNativeQuery(queryBuilder.toString());
        return returnBankBalanceData(data);
    }

    private List<BankBalanceViewNew> returnBankBalanceData(List<Object[]> res) {
        List<BankBalanceViewNew> list = new ArrayList<>();
        BankBalanceViewNew bankBalanceViewNew = new BankBalanceViewNew();
        for (Object[] object : res) {

            bankBalanceViewNew = new BankBalanceViewNew();
            bankBalanceViewNew.setDate((Date) object[0]);
            bankBalanceViewNew.setRemark(object[1] != null ? object[1].toString() : null);
            bankBalanceViewNew.setGlYear(object[2] != null ? Integer.valueOf(object[2].toString()) : null);
            bankBalanceViewNew.setSerial(object[3] != null ? object[3].toString() : null);
            bankBalanceViewNew.setGlValue((BigDecimal) object[4]);
            bankBalanceViewNew.setValueLocal((BigDecimal) object[5]);
            bankBalanceViewNew.setTransactionType(object[6] != null ? Integer.valueOf(object[6].toString()) : null);
            bankBalanceViewNew.setBankIdTo(object[7] != null ? Integer.valueOf(object[7].toString()) : null);
            bankBalanceViewNew.setBankIdFrom(object[8] != null ? Integer.valueOf(object[8].toString()) : null);
            bankBalanceViewNew.setBankNameTo(object[9] != null ? object[9].toString() : null);
            bankBalanceViewNew.setBankNameFrom(object[10] != null ? object[10].toString() : null);
            bankBalanceViewNew.setAccountIdTo(object[11] != null ? object[11].toString() : null);
            bankBalanceViewNew.setAccountIdFrom(object[12] != null ? object[12].toString() : null);
            bankBalanceViewNew.setBranchId(object[13] != null ? Integer.valueOf(object[13].toString()) : null);
            bankBalanceViewNew.setBankCodeto(object[14] != null ? object[14].toString() : null);
            bankBalanceViewNew.setBankCodeFrom(object[15] != null ? object[13].toString() : null);
            bankBalanceViewNew.setCashTo(object[16] != null ? (Integer) object[16] : null);
            bankBalanceViewNew.setCashFrom(object[17] != null ? (Integer) object[17] : null);
            list.add(bankBalanceViewNew);
        }
        return list;
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
                        if (bankBalanceReportSearchBean.getDateFrom() != null && balanceReportView.getDate().before(bankBalanceReportSearchBean.getDateFrom())) {
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
                        if (bankBalanceReportSearchBean.getDateFrom() != null && balanceReportView.getDate().before(bankBalanceReportSearchBean.getDateFrom())) {
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
                        if (bankBalanceReportSearchBean.getDateFrom() != null && balanceReportView.getDate().before(bankBalanceReportSearchBean.getDateFrom())) {
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

    private List<BankBalanceReportBean> testNative(List<BankBalanceViewNew> bankBalanceViewList, BankBalanceReportSearchBean bankBalanceReportSearchBean) {
        List<BankBalanceReportBean> bankBalanceReportBeanList = new ArrayList<>();
        Map<Integer, BankBalanceReportBean> balanceViewMap = new HashMap<>();
        if (bankBalanceViewList != null && !bankBalanceViewList.isEmpty()) {

            for (BankBalanceViewNew balanceReportView : bankBalanceViewList) {
                BankBalanceReportBean balanceReportBean;
                if (balanceReportView.getBankIdTo() != null && bankBalanceReportSearchBean.getSerchedBanksSet().contains(balanceReportView.getBankIdTo())) {
                    balanceReportBean = new BankBalanceReportBean();
                    if (!balanceViewMap.containsKey(balanceReportView.getBankIdTo())) {
                        balanceReportBean.setBankId(balanceReportView.getBankIdTo());
                        balanceReportBean.setSerial(balanceReportView.getSerial());
                        balanceReportBean.setBankName(balanceReportView.getBankNameTo() == null ? balanceReportBean.getBankName() : balanceReportView.getBankNameTo());
//                        balanceReportBean.setAccountIdTo(balanceReportView.getAccountIdTo());
                        if (bankBalanceReportSearchBean.getDateFrom() != null && balanceReportView.getDate().before(bankBalanceReportSearchBean.getDateFrom())) {
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
//                        balanceReportBean.setAccountIdTo(balanceReportView.getAccountIdTo());
                        balanceReportBean = balanceViewMap.get(balanceReportView.getBankIdTo());
                        if (bankBalanceReportSearchBean.getDateFrom() != null && balanceReportView.getDate().before(bankBalanceReportSearchBean.getDateFrom())) {
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
//                        balanceReportBean.setAccountIdFrom(balanceReportView.getAccountIdFrom());
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
//                        balanceReportBean.setAccountIdFrom(balanceReportView.getAccountIdFrom());
                        balanceReportBean = balanceViewMap.get(balanceReportView.getBankIdFrom());
                        if (bankBalanceReportSearchBean.getDateFrom() != null && balanceReportView.getDate().before(bankBalanceReportSearchBean.getDateFrom())) {
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

    private List<BankBalanceReportBean> testNativeNew(List<BankBalanceViewNew> bankBalanceViewList, SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        List<BankBalanceReportBean> bankBalanceReportBeanList = new ArrayList<>();
        Map<Integer, BankBalanceReportBean> balanceViewMap = new HashMap<>();
        if (bankBalanceViewList != null && !bankBalanceViewList.isEmpty()) {

            for (BankBalanceViewNew balanceReportView : bankBalanceViewList) {
                BankBalanceReportBean balanceReportBean;
                if (balanceReportView.getBankIdTo() != null && cashAccountSettlementSearchBean.getSerchedBanksSet().contains(balanceReportView.getBankIdTo())) {
                    balanceReportBean = new BankBalanceReportBean();
                    if (!balanceViewMap.containsKey(balanceReportView.getBankIdTo())) {
                        balanceReportBean.setBankId(balanceReportView.getBankIdTo());
                        balanceReportBean.setSerial(balanceReportView.getSerial());
                        balanceReportBean.setCashType(balanceReportView.getCashTo());
                        balanceReportBean.setBankName(balanceReportView.getBankNameTo() == null ? balanceReportBean.getBankName() : balanceReportView.getBankNameTo());
//                        balanceReportBean.setAccountIdTo(balanceReportView.getAccountIdTo());
                        if (cashAccountSettlementSearchBean.getDateFrom() != null && balanceReportView.getDate().before(cashAccountSettlementSearchBean.getDateFrom())) {
                            balanceReportBean.setBankToBalance(balanceReportView.getValueLocal() == null ? BigDecimal.ZERO : balanceReportView.getValueLocal().setScale(2, RoundingMode.UP));
                            balanceReportBean.setPreviousBalance((balanceReportBean.getBankToBalance().subtract(balanceReportBean.getBankFromBalance())).setScale(2, RoundingMode.UP));
                        } else {
                            balanceReportBean.setCredit(BigDecimal.ZERO);
                            balanceReportBean.setDebit(balanceReportView.getValueLocal() != null ? balanceReportView.getValueLocal().setScale(2, RoundingMode.UP) : BigDecimal.ZERO);
                        }
                    } else {
                        balanceReportBean.setBankId(balanceReportView.getBankIdTo());
                        balanceReportBean.setSerial(balanceReportView.getSerial());
                        balanceReportBean.setCashType(balanceReportView.getCashTo());
                        balanceReportBean.setBankName(balanceReportView.getBankNameTo() == null ? balanceReportBean.getBankName() : balanceReportView.getBankNameTo());
//                        balanceReportBean.setAccountIdTo(balanceReportView.getAccountIdTo());
                        balanceReportBean = balanceViewMap.get(balanceReportView.getBankIdTo());
                        if (cashAccountSettlementSearchBean.getDateFrom() != null && balanceReportView.getDate().before(cashAccountSettlementSearchBean.getDateFrom())) {
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

                if (balanceReportView.getBankIdFrom() != null && cashAccountSettlementSearchBean.getSerchedBanksSet().contains(balanceReportView.getBankIdFrom())) {
                    balanceReportBean = new BankBalanceReportBean();
                    if (!balanceViewMap.containsKey(balanceReportView.getBankIdFrom())) {
                        balanceReportBean.setBankId(balanceReportView.getBankIdFrom());
                        balanceReportBean.setSerial(balanceReportView.getSerial());
                        balanceReportBean.setCashType(balanceReportView.getCashFrom());
                        balanceReportBean.setBankName(balanceReportView.getBankNameFrom() == null ? balanceReportBean.getBankName() : balanceReportView.getBankNameFrom());
//                        balanceReportBean.setAccountIdFrom(balanceReportView.getAccountIdFrom());
                        if (cashAccountSettlementSearchBean.getDateFrom() != null && balanceReportView.getDate().before(cashAccountSettlementSearchBean.getDateFrom())) {
                            balanceReportBean.setBankFromBalance(balanceReportView.getValueLocal() == null ? BigDecimal.ZERO : balanceReportView.getValueLocal().setScale(2, RoundingMode.UP));
                            balanceReportBean.setPreviousBalance((balanceReportBean.getBankToBalance().subtract(balanceReportBean.getBankFromBalance())).setScale(2, RoundingMode.UP));
                        } else {
                            balanceReportBean.setCredit(balanceReportView.getValueLocal() != null ? balanceReportView.getValueLocal().setScale(2, RoundingMode.UP) : BigDecimal.ZERO);
                            balanceReportBean.setDebit(BigDecimal.ZERO);
                        }
                    } else {
                        balanceReportBean.setBankId(balanceReportView.getBankIdFrom());
                        balanceReportBean.setSerial(balanceReportView.getSerial());
                        balanceReportBean.setCashType(balanceReportView.getCashFrom());
                        balanceReportBean.setBankName(balanceReportView.getBankNameFrom() == null ? balanceReportBean.getBankName() : balanceReportView.getBankNameFrom());
//                        balanceReportBean.setAccountIdFrom(balanceReportView.getAccountIdFrom());
                        balanceReportBean = balanceViewMap.get(balanceReportView.getBankIdFrom());
                        if (cashAccountSettlementSearchBean.getDateFrom() != null && balanceReportView.getDate().before(cashAccountSettlementSearchBean.getDateFrom())) {
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

    @Override
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

    @Override
    public List<BankBalanceReportBean> searchNative(BankBalanceReportSearchBean bankBalanceReportSearchBean) {
        List<BankBalanceReportBean> list = null;
        if (bankBalanceReportSearchBean.getGlBankList() != null && !bankBalanceReportSearchBean.getGlBankList().isEmpty()) {
            StringBuilder bankBuilder = new StringBuilder();
            for (GlBankDTO glBankDTO : bankBalanceReportSearchBean.getGlBankList()) {
                if (bankBuilder.length() == 0) {
                    bankBuilder.append(glBankDTO.getId());
                } else {
                    bankBuilder.append(",");
                    bankBuilder.append(glBankDTO.getId());
                }
                bankBalanceReportSearchBean.getSerchedBanksSet().add(glBankDTO.getId());
                bankBalanceReportSearchBean.setBankBuilder(bankBuilder.toString());
            }
            List<BankBalanceViewNew> bankBalanceViewList = getAllBankBalancesTransactionsByBankIdNativ(bankBalanceReportSearchBean);
            list = testNative(bankBalanceViewList, bankBalanceReportSearchBean);
        }
        return list;
    }

    @Override
    public List<BankBalanceReportBean> searchNativeNew(SubAccountSummarySearchBean cashAccountSettlementSearchBean) {
        List<BankBalanceReportBean> list = null;
        if (cashAccountSettlementSearchBean.getGlBankDTOListSelected() != null && !cashAccountSettlementSearchBean.getGlBankDTOListSelected().isEmpty()) {
            StringBuilder bankBuilder = new StringBuilder();
            for (GlBankDTO glBankDTO : cashAccountSettlementSearchBean.getGlBankDTOListSelected()) {
                if (bankBuilder.length() == 0) {
                    bankBuilder.append(glBankDTO.getId());
                } else {
                    bankBuilder.append(",");
                    bankBuilder.append(glBankDTO.getId());
                }
                cashAccountSettlementSearchBean.getSerchedBanksSet().add(glBankDTO.getId());
                cashAccountSettlementSearchBean.setBankBuilder(bankBuilder.toString());
            }
            List<BankBalanceViewNew> bankBalanceViewList = getAllBankBalancesTransactionsByBankIdNativNew(cashAccountSettlementSearchBean);
            list = testNativeNew(bankBalanceViewList, cashAccountSettlementSearchBean);
        }
        return list;
    }

}
