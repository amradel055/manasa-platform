/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.core.GenericDAO;
import com.isag.dto.BankBalanceReportBeanNew;
import com.isag.dto.BankBalanceReportSearchBean;
import com.isag.dto.BankBalanceViewNew;
import com.isag.dto.GlBankDTO;
import com.isag.entity.GlBank;
import com.isag.entity.GlYear;
import com.isag.views.BankBalanceView;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
//        if (dateFrom != null) {
//            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(dateFrom);
//            queryBuilder.append(" and bbv.date >= '").append(formatDateFrom).append(" 00-00-00'");
//        }

        if (dateTo != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd   HH:mm:ss").format(dateTo);
            queryBuilder.append(" and bbv.date <= '").append(formatDateTo).append("' ");
//            queryBuilder.append(" and bbv.date <= '").append(formatDateTo).append(" 23-59-59'");
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
    public List<BankBalanceView> getAllBankBalancesTransactionsByBankId(Integer branchId, GlYear glYear, Date dateTo, StringBuilder builder) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
//        params.put("glYear", glYear.getYear());
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT bbv FROM BankBalanceView bbv WHERE bbv.branchId = :branchId ");
        appendDateForQueryBuilder(queryBuilder, glYear.getDateFrom(), dateTo);
        appendBanksIdFromAndTo(queryBuilder, params, builder);
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

    @Override
    public List<BankBalanceReportBeanNew> searchNative(BankBalanceReportSearchBean bankBalanceReportSearchBean) {
        List<BankBalanceReportBeanNew> list = null;
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

    private List<BankBalanceReportBeanNew> testNative(List<BankBalanceViewNew> bankBalanceViewList, BankBalanceReportSearchBean bankBalanceReportSearchBean) {
        List<BankBalanceReportBeanNew> bankBalanceReportBeanList = new ArrayList<>();
        Map<Integer, BankBalanceReportBeanNew> balanceViewMap = new HashMap<>();
        if (bankBalanceViewList != null && !bankBalanceViewList.isEmpty()) {

            for (BankBalanceViewNew balanceReportView : bankBalanceViewList) {
                BankBalanceReportBeanNew balanceReportBeanNew;
                if (balanceReportView.getBankIdTo() != null && bankBalanceReportSearchBean.getSerchedBanksSet().contains(balanceReportView.getBankIdTo())) {
                    balanceReportBeanNew = new BankBalanceReportBeanNew();
                    if (!balanceViewMap.containsKey(balanceReportView.getBankIdTo())) {
                        balanceReportBeanNew.setBankId(balanceReportView.getBankIdTo());
                        balanceReportBeanNew.setSerial(balanceReportView.getSerial());
                        balanceReportBeanNew.setBankName(balanceReportView.getBankNameTo() == null ? balanceReportBeanNew.getBankName() : balanceReportView.getBankNameTo());
//                        balanceReportBean.setAccountIdTo(balanceReportView.getAccountIdTo());
                        if (bankBalanceReportSearchBean.getDateFrom() != null && balanceReportView.getDate().before(bankBalanceReportSearchBean.getDateFrom())) {
                            balanceReportBeanNew.setBankToBalance(balanceReportView.getValueLocal() == null ? BigDecimal.ZERO : balanceReportView.getValueLocal().setScale(2, RoundingMode.UP));
                            balanceReportBeanNew.setPreviousBalance((balanceReportBeanNew.getBankToBalance().subtract(balanceReportBeanNew.getBankFromBalance())).setScale(2, RoundingMode.UP));
                        } else {
                            balanceReportBeanNew.setCredit(BigDecimal.ZERO);
                            balanceReportBeanNew.setDebit(balanceReportView.getValueLocal() != null ? balanceReportView.getValueLocal().setScale(2, RoundingMode.UP) : BigDecimal.ZERO);
                        }
                    } else {
                        balanceReportBeanNew.setBankId(balanceReportView.getBankIdTo());
                        balanceReportBeanNew.setSerial(balanceReportView.getSerial());
                        balanceReportBeanNew.setBankName(balanceReportView.getBankNameTo() == null ? balanceReportBeanNew.getBankName() : balanceReportView.getBankNameTo());
//                        balanceReportBean.setAccountIdTo(balanceReportView.getAccountIdTo());
                        balanceReportBeanNew = balanceViewMap.get(balanceReportView.getBankIdTo());
                        if (bankBalanceReportSearchBean.getDateFrom() != null && balanceReportView.getDate().before(bankBalanceReportSearchBean.getDateFrom())) {
                            balanceReportBeanNew.setBankToBalance(balanceReportView.getValueLocal() == null ? balanceReportBeanNew.getBankToBalance().setScale(2, RoundingMode.UP) : (balanceReportView.getValueLocal().add(balanceReportBeanNew.getBankToBalance())).setScale(2, RoundingMode.UP));
                            balanceReportBeanNew.setPreviousBalance((balanceReportBeanNew.getBankToBalance().subtract(balanceReportBeanNew.getBankFromBalance())).setScale(2, RoundingMode.UP));
                        } else {
                            balanceReportBeanNew.setDebit(balanceReportView.getValueLocal() != null ? (balanceReportBeanNew.getDebit().add(balanceReportView.getValueLocal())).setScale(2, RoundingMode.UP) : balanceReportBeanNew.getDebit().setScale(2, RoundingMode.UP));
                        }
                    }

                    balanceReportBeanNew.setBalance((balanceReportBeanNew.getDebit().subtract(balanceReportBeanNew.getCredit()).add(balanceReportBeanNew.getPreviousBalance() != null ? balanceReportBeanNew.getPreviousBalance() : BigDecimal.ZERO)).setScale(2, RoundingMode.UP));
                    //  addOpeneingBalanceToPreviousBalance(balanceReportBean.getAccountIdTo(), balanceReportBean);
                    balanceViewMap.put(balanceReportView.getBankIdTo(), balanceReportBeanNew);

                }

                if (balanceReportView.getBankIdFrom() != null && bankBalanceReportSearchBean.getSerchedBanksSet().contains(balanceReportView.getBankIdFrom())) {
                    balanceReportBeanNew = new BankBalanceReportBeanNew();
                    if (!balanceViewMap.containsKey(balanceReportView.getBankIdFrom())) {
                        balanceReportBeanNew.setBankId(balanceReportView.getBankIdFrom());
                        balanceReportBeanNew.setSerial(balanceReportView.getSerial());
                        balanceReportBeanNew.setBankName(balanceReportView.getBankNameFrom() == null ? balanceReportBeanNew.getBankName() : balanceReportView.getBankNameFrom());
//                        balanceReportBean.setAccountIdFrom(balanceReportView.getAccountIdFrom());
                        if (bankBalanceReportSearchBean.getDateFrom() != null && balanceReportView.getDate().before(bankBalanceReportSearchBean.getDateFrom())) {
                            balanceReportBeanNew.setBankFromBalance(balanceReportView.getValueLocal() == null ? BigDecimal.ZERO : balanceReportView.getValueLocal().setScale(2, RoundingMode.UP));
                            balanceReportBeanNew.setPreviousBalance((balanceReportBeanNew.getBankToBalance().subtract(balanceReportBeanNew.getBankFromBalance())).setScale(2, RoundingMode.UP));
                        } else {
                            balanceReportBeanNew.setCredit(balanceReportView.getValueLocal() != null ? balanceReportView.getValueLocal().setScale(2, RoundingMode.UP) : BigDecimal.ZERO);
                            balanceReportBeanNew.setDebit(BigDecimal.ZERO);
                        }
                    } else {
                        balanceReportBeanNew.setBankId(balanceReportView.getBankIdFrom());
                        balanceReportBeanNew.setSerial(balanceReportView.getSerial());
                        balanceReportBeanNew.setBankName(balanceReportView.getBankNameFrom() == null ? balanceReportBeanNew.getBankName() : balanceReportView.getBankNameFrom());
//                        balanceReportBean.setAccountIdFrom(balanceReportView.getAccountIdFrom());
                        balanceReportBeanNew = balanceViewMap.get(balanceReportView.getBankIdFrom());
                        if (bankBalanceReportSearchBean.getDateFrom() != null && balanceReportView.getDate().before(bankBalanceReportSearchBean.getDateFrom())) {
                            balanceReportBeanNew.setBankFromBalance(balanceReportView.getValueLocal() == null ? balanceReportBeanNew.getBankFromBalance().setScale(2, RoundingMode.UP) : (balanceReportView.getValueLocal().add(balanceReportBeanNew.getBankFromBalance())).setScale(2, RoundingMode.UP));
                            balanceReportBeanNew.setPreviousBalance((balanceReportBeanNew.getBankToBalance().subtract(balanceReportBeanNew.getBankFromBalance())).setScale(2, RoundingMode.UP));

                        } else {
                            balanceReportBeanNew.setCredit(balanceReportView.getValueLocal() != null ? (balanceReportBeanNew.getCredit().add(balanceReportView.getValueLocal()).setScale(2, RoundingMode.UP)) : balanceReportBeanNew.getCredit().setScale(2, RoundingMode.UP));
                        }
                    }
                    balanceReportBeanNew.setBalance((balanceReportBeanNew.getDebit().subtract(balanceReportBeanNew.getCredit()).add(balanceReportBeanNew.getPreviousBalance() != null ? balanceReportBeanNew.getPreviousBalance() : BigDecimal.ZERO)).setScale(2, RoundingMode.UP));
                    //  addOpeneingBalanceToPreviousBalance(balanceReportBean.getAccountIdFrom(), balanceReportBean);
                    balanceViewMap.put(balanceReportView.getBankIdFrom(), balanceReportBeanNew);

                }
            }
        }
        bankBalanceReportBeanList = new ArrayList<>(balanceViewMap.values());
        return bankBalanceReportBeanList;
    }

    public List<BankBalanceViewNew> getAllBankBalancesTransactionsByBankIdNativ(BankBalanceReportSearchBean bankBalanceReportSearchBean) {

        dao.executeNativeQuery("select sql_mode_function()");
        Integer branchId = bankBalanceReportSearchBean.getBranchId();
        String bankBuilder = bankBalanceReportSearchBean.getBankBuilder();
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
                + "                IFNULL(`ipi`.`tax_flag`, 0.0),15,IFNULL(0, 0.0)),0.0) AS `glValue`,\n"
                + "        IFNULL(NET(IFNULL(SUM(`ipid`.`net`), 0.0),IFNULL(`ipi`.`discount_type`, 0.0),IFNULL(`ipi`.`discount`, 0.0),\n"
                + "                IFNULL(`ipi`.`tax_flag`, 0.0),15,IFNULL(0, 0.0)),0.0)  AS `value_local`,\n"
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
            list.add(bankBalanceViewNew);
        }
        return list;
    }

    @Override
    public BigDecimal findBalanceForBankId(Integer bankId) {

        if (bankId != null) {
            dao.executeNativeQuery("select sql_mode_function()");
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append(" select   sum(s.value_local) from (");
            findOpenningBalance(queryBuilder, bankId);
            queryBuilder.append(" UNION ALL  ");
            findCash(queryBuilder, bankId, null, null);
            queryBuilder.append(" UNION ALL  ");
            findSettelment(queryBuilder, bankId, null, null);
            queryBuilder.append(" UNION ALL  ");
            findTransfer(queryBuilder, bankId, null, null);
            queryBuilder.append(" UNION ALL  ");
            queryBuilder.append(" \n"
                    + "    SELECT \n"
                    + "      \n"
                    + "        IFNULL(NET(IFNULL(SUM(`ipid`.`net`), 0.0),IFNULL(`ipi`.`discount_type`, 0.0),IFNULL(`ipi`.`discount`, 0.0),\n"
                    + "                IFNULL(`ipi`.`tax_flag`, 0.0),15,IFNULL(0, 0.0)),0.0)  AS `value_local`  \n"
                    + "    FROM\n"
                    + "       `inv_purchase_invoice` `ipi`  LEFT JOIN  `inv_purchase_invoice_detail` `ipid`  ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
                    + "    WHERE\n"
                    + "       (`ipi`.`type` = 1 or `ipi`.`type` = 2) AND (`ipi`.`payment_type` = 0) ").append("  And (`ipi`.`gl_bank_id` = ").append(bankId).append(")\n"
                    + "        group by ipi.id    UNION ALL SELECT \n"
                    + "      \n"
                    + "        ((IFNULL(NET(IFNULL(SUM(`ipid`.`net`), 0.0),IFNULL(`ipi`.`discount_type`, 0.0),IFNULL(`ipi`.`discount`, 0.0),\n"
                    + "                IFNULL(`ipi`.`tax_flag`, 0.0),15,IFNULL(0, 0.0)),0.0)) * -1 )  AS `value_local`  \n"
                    + "    FROM\n"
                    + "       `inv_purchase_invoice` `ipi`  LEFT JOIN  `inv_purchase_invoice_detail` `ipid`  ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
                    + "    WHERE\n"
                    + "       (`ipi`.`type` = 0 or `ipi`.`type` = 3) AND (`ipi`.`payment_type` = 0)  ").append("  And (`ipi`.`gl_bank_id` = ").append(bankId).append(")\n"
                    + "        group by ipi.id union all SELECT sum(`gs`.`finalNet`)  AS `value_local`\n"
                    + "    FROM `gm_subscriptions` `gs` \n"
                    + "    WHERE   gl_bank_id = ").append(bankId).append("  and `gs`.deleted_by is null )s ");
            List<BigDecimal> data = dao.executeNativeQuery(queryBuilder.toString());
            if (data == null) {
                return BigDecimal.ZERO;
            } else {
                if (data.get(0) == null) {
                    return BigDecimal.ZERO;
                } else {
                    return data.get(0);
                }
            }

        } else {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal findCashBalanceByDate(Integer bankId) {

        if (bankId != null) {
            dao.executeNativeQuery("select sql_mode_function()");
            StringBuilder queryBuilder = new StringBuilder();
            String dateFrom = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(new Date());
            String dateTo = new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(new Date());
            queryBuilder.append(" select   sum(s.value_local) from (");
            findCash(queryBuilder, bankId, dateFrom, dateTo);
            queryBuilder.append(") s ");
            List<BigDecimal> data = dao.executeNativeQuery(queryBuilder.toString());
            if (data == null) {
                return BigDecimal.ZERO;
            } else {
                if (data.get(0) == null) {
                    return BigDecimal.ZERO;
                } else {
                    return data.get(0);
                }
            }

        } else {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal findCashBalanceByMonth(Integer bankId) {

        if (bankId != null) {
            dao.executeNativeQuery("select sql_mode_function()");
            StringBuilder queryBuilder = new StringBuilder();
            String dateFrom = new SimpleDateFormat("yyyy-MM-01 00:00:00").format(new Date());
            String dateTo = new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(new Date());
            queryBuilder.append(" select   sum(s.value_local) from (");
            findCash(queryBuilder, bankId, dateFrom, dateTo);
            queryBuilder.append(")s ");
            List<BigDecimal> data = dao.executeNativeQuery(queryBuilder.toString());
            if (data == null) {
                return BigDecimal.ZERO;
            } else {
                if (data.get(0) == null) {
                    return BigDecimal.ZERO;
                } else {
                    return data.get(0);
                }
            }

        } else {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal findSettelmentBalanceByDate(Integer bankId) {

        if (bankId != null) {
            dao.executeNativeQuery("select sql_mode_function()");
            StringBuilder queryBuilder = new StringBuilder();
            String dateFrom = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(new Date());
            String dateTo = new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(new Date());
            queryBuilder.append(" select   sum(s.value_local) from (");
            findSettelment(queryBuilder, bankId, dateFrom, dateTo);
            queryBuilder.append(")s ");
            List<BigDecimal> data = dao.executeNativeQuery(queryBuilder.toString());
            if (data == null) {
                return BigDecimal.ZERO;
            } else {
                if (data.get(0) == null) {
                    return BigDecimal.ZERO;
                } else {
                    return data.get(0);
                }
            }

        } else {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal findSettelmentBalanceByMonth(Integer bankId) {

        if (bankId != null) {
            dao.executeNativeQuery("select sql_mode_function()");
            StringBuilder queryBuilder = new StringBuilder();
            String dateFrom = new SimpleDateFormat("yyyy-MM-01 00:00:00").format(new Date());
            String dateTo = new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(new Date());
            queryBuilder.append(" select   sum(s.value_local) from (");
            findSettelment(queryBuilder, bankId, dateFrom, dateTo);
            queryBuilder.append(")s ");
            List<BigDecimal> data = dao.executeNativeQuery(queryBuilder.toString());
            if (data == null) {
                return BigDecimal.ZERO;
            } else {
                if (data.get(0) == null) {
                    return BigDecimal.ZERO;
                } else {
                    return data.get(0);
                }
            }

        } else {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal findTransferBalanceByDate(Integer bankId) {

        if (bankId != null) {
            dao.executeNativeQuery("select sql_mode_function()");
            StringBuilder queryBuilder = new StringBuilder();
            String dateFrom = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(new Date());
            String dateTo = new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(new Date());
            queryBuilder.append(" select   sum(s.value_local) from (");
            findTransfer(queryBuilder, bankId, dateFrom, dateTo);
            queryBuilder.append(")s ");
            List<BigDecimal> data = dao.executeNativeQuery(queryBuilder.toString());
            if (data == null) {
                return BigDecimal.ZERO;
            } else {
                if (data.get(0) == null) {
                    return BigDecimal.ZERO;
                } else {
                    return data.get(0);
                }
            }

        } else {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal findTransferBalanceByMonth(Integer bankId) {

        if (bankId != null) {
            dao.executeNativeQuery("select sql_mode_function()");
            StringBuilder queryBuilder = new StringBuilder();
            String dateFrom = new SimpleDateFormat("yyyy-MM-01 00:00:00").format(new Date());
            String dateTo = new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(new Date());
            queryBuilder.append(" select   sum(s.value_local) from (");
            findTransfer(queryBuilder, bankId, dateFrom, dateTo);
            queryBuilder.append(")s ");
            List<BigDecimal> data = dao.executeNativeQuery(queryBuilder.toString());
            if (data == null) {
                return BigDecimal.ZERO;
            } else {
                if (data.get(0) == null) {
                    return BigDecimal.ZERO;
                } else {
                    return data.get(0);
                }
            }

        } else {
            return BigDecimal.ZERO;
        }
    }

    private void findTransfer(StringBuilder queryBuilder, Integer bankId, String dateFrom, String dateTo) {
        queryBuilder.append(" \n"
                + "    SELECT sum(`glbt`.`value_local`) AS `value_local`\n"
                + "         FROM `gl_bank_transaction` `glb` LEFT JOIN `gl_bank_transaction_detail` `glbt` ON `glb`.`id` = `glbt`.`gl_bank_transaction_id` \n"
                + "    WHERE\n"
                + "        (`glb`.`transaction_type` = 2)  AND  bank_id_to = ").append(bankId).append(
                " And `glb`.`gl_bank_id` =  ").append(bankId);
        if (dateFrom != null && dateTo != null) {
            queryBuilder.append(" AND glb.date >= '").append(dateFrom).append("' AND glb.date <= '").append(dateTo).append("'");
        }
        queryBuilder.append(" UNION ALL  ");
        queryBuilder.append(" \n"
                + "     SELECT (sum(`glbt`.`value_local`) * -1 ) AS `value_local`\n"
                + "         FROM `gl_bank_transaction` `glb` LEFT JOIN `gl_bank_transaction_detail` `glbt` ON `glb`.`id` = `glbt`.`gl_bank_transaction_id` \n"
                + "    WHERE\n"
                + "        (`glb`.`transaction_type` = 2)  AND  bank_id_from = ").append(bankId).append(
                " And `glb`.`gl_bank_id` =  ").append(bankId);
        if (dateFrom != null && dateTo != null) {
            queryBuilder.append(" AND glb.date >= '").append(dateFrom).append("' AND glb.date <= '").append(dateTo).append("'");
        }
    }

    private void findSettelment(StringBuilder queryBuilder, Integer bankId, String dateFrom, String dateTo) {
        queryBuilder.append(" \n"
                + "     SELECT (sum(`glbt`.`value_local`) * -1 ) AS `value_local`\n"
                + "         FROM `gl_bank_transaction` `glb` LEFT JOIN `gl_bank_transaction_detail` `glbt` ON `glb`.`id` = `glbt`.`gl_bank_transaction_id` \n"
                + "    WHERE\n"
                + "        (`glb`.`transaction_type` = 0)  AND  bank_id_to = ").append(bankId).append(
                " And `glb`.`gl_bank_id` =  ").append(bankId);
        if (dateFrom != null && dateTo != null) {
            queryBuilder.append(" AND glb.date >= '").append(dateFrom).append("' AND glb.date <= '").append(dateTo).append("'");
        }
    }

    private void findCash(StringBuilder queryBuilder, Integer bankId, String dateFrom, String dateTo) {
        queryBuilder.append(
                "      SELECT sum(`glbt`.`value_local`) AS `value_local`\n"
                + "         FROM `gl_bank_transaction` `glb` LEFT JOIN `gl_bank_transaction_detail` `glbt` ON `glb`.`id` = `glbt`.`gl_bank_transaction_id` \n"
                + "    WHERE\n"
                + "        (`glb`.`transaction_type` = 1)  AND  bank_id_from = ").append(bankId).append(
                " And `glb`.`gl_bank_id` =  ").append(bankId);
        if (dateFrom != null && dateTo != null) {
            queryBuilder.append(" AND glb.date >= '").append(dateFrom).append("' AND glb.date <= '").append(dateTo).append("'");
        }
    }

    private void findOpenningBalance(StringBuilder queryBuilder, Integer bankId) {
        queryBuilder.append(" SELECT  `gb`.`openinng_balance` AS `value_local` \n"
                + "	FROM `gl_bank` `gb` \n"
                + "    WHERE\n"
                + "        ((`gb`.`date_openinng_balance` IS NOT NULL)\n"
                + "            AND (`gb`.`openinng_balance` > 0)) ").append(
                        " And `gb`.`id` =  ").append(bankId);
    }

    @Override
    public Integer FindInvoiceNumCashSettTranByTypeByDate(List<GlBank> glbankIdList, Integer branchId, Integer transactionType) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("transactionType", transactionType);

        String inv = updateinventoryIdList(glbankIdList);
        String dateFrom = new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(new Date());
        String dateTo = new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(new Date());
        StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT(ipid.id) FROM GlBankTransaction ipid WHERE ipid.branchId.id = :branchId AND ipid.transactionType = :transactionType AND ipid.glBankId.id in (").append(inv).append(") AND ipid.date >= '").append(dateFrom).append("' AND ipid.date <= '").append(dateTo).append("'");
        List<Long> list = dao.findListByQuery(query.toString(), params);
        return list == null ? 0 : list.get(0).intValue();
    }
    
    @Override
    public Integer FindInvoiceNumCashSettTranByTypeByMonth(List<GlBank> glbankIdList, Integer branchId, Integer transactionType) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("transactionType", transactionType);

        String inv = updateinventoryIdList(glbankIdList);
        String dateFrom = new SimpleDateFormat("yyyy-MM-01 00:00:00").format(new Date());
        String dateTo = new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(new Date());
        StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT(ipid.id) FROM GlBankTransaction ipid WHERE ipid.branchId.id = :branchId AND ipid.transactionType = :transactionType AND ipid.glBankId.id in (").append(inv).append(") AND ipid.date >= '").append(dateFrom).append("' AND ipid.date <= '").append(dateTo).append("'");
        List<Long> list = dao.findListByQuery(query.toString(), params);
        return list == null ? 0 : list.get(0).intValue();
    }
    

    public String updateinventoryIdList(List<GlBank> glBanksList) {
        StringBuilder stringBuilder = null;
        for (GlBank glBank : glBanksList) {
            if (stringBuilder == null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(glBank.getId());
            } else {
                stringBuilder.append(",").append(glBank.getId());

            }
            return stringBuilder.toString();
        }
        return stringBuilder.toString();

    }

}
