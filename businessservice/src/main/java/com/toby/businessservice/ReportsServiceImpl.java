/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.views.CompanyInvoicesWithoutSewing;
import com.toby.views.CustomerBalances;
import com.toby.views.InvoicesIssuedWithinAperiod;
import com.toby.views.SalesReportByValueAded;
import com.toby.views.SalesReportByValueAdedDetails;
import com.toby.views.StatementOfBondsByBranche;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq002
 */
@Stateless
public class ReportsServiceImpl implements ReportsService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<SalesReportByValueAded> getSalesReportByValueAded(SalesReportByValueAded salesReportByValueAded, Integer branchId) {

        List<SalesReportByValueAded> salesReportByValueAdeds = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select sum(s.net) , sum(s.netBeforTax) , s.gallary_name  from ( SELECT \n"
                + "        `ip`.`serial` AS `serial`, `ip`.`type` AS `type`,`ip`.`date` AS `date`,`inv`.`name` AS `gallary_name`,\n"
                + "        `ios`.`name` AS `organization_site_name`,`ip`.`discount` AS `head_discount`,\n"
                + "        IFNULL(NET(IFNULL(SUM(`ipd`.`net`), 0.0),IFNULL(`ip`.`discount_type`, 0.0),IFNULL(`ip`.`discount`, 0.0),\n"
                + "                        IFNULL(`ip`.`tax_flag`, 0.0),15,0.0,0.0,0.0),0.0) AS `net`,\n"
                + "                IFNULL(NET(IFNULL(SUM(`ipd`.`net`), 0.0),IFNULL(`ip`.`discount_type`, 0.0),\n"
                + "                        IFNULL(`ip`.`discount`, 0.0),IFNULL(`ip`.`tax_flag`, 0.0),0.0,0.0,0.0,0.0),0.0) AS `netBeforTax`\n"
                + "    FROM\n"
                + "        `inv_purchase_invoice` `ip` LEFT JOIN `inv_purchase_invoice_detail` `ipd` ON `ip`.`id` = `ipd`.`inv_purchase_invoice_id`\n"
                + "        LEFT JOIN `inv_inventory` `inv` ON `inv`.`id` = `ip`.`galary_Id` LEFT JOIN `inv_organization_site` `ios` ON `ios`.`id` = `ip`.`organization_site_id`\n"
                + " ");
        sql.append("  where ip.branch_id = ").append(branchId);
        appendGallery(salesReportByValueAded, sql);
        appendDate(salesReportByValueAded, sql);
        sql.append(" GROUP BY `ip`.`id` ) as s group by s.gallary_name ");
        List<Object[]> res = dao.executeNativeQuery(sql.toString());
        for (Object[] object : res) {
            SalesReportByValueAded salesReportByValueAded1 = new SalesReportByValueAded();
            salesReportByValueAded1.setTotalAfterTax((BigDecimal) object[0]);
            salesReportByValueAded1.setTotalBeforeTax((BigDecimal) object[1]);
            salesReportByValueAded1.setGalleryName((String) object[2]);
            salesReportByValueAdeds.add(salesReportByValueAded1);

        }
        return salesReportByValueAdeds;
    }

    @Override
    public List<SalesReportByValueAdedDetails> getSalesReportByValueAdedDetails(SalesReportByValueAdedDetails salesReportByValueAdedDetails, Integer branchId) {

        List<SalesReportByValueAdedDetails> salesReportByValueAdeds = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT \n"
                + "        `ip`.`serial` AS `serial`,\n"
                + "        `ip`.`type` AS `type`,\n"
                + "        `ip`.`date` AS `date`,\n"
                + "        `inv`.`name` AS `gallary_name`,\n"
                + "        `ios`.`name` AS `organization_site_name`,\n"
                + "        `ip`.`discount` AS `head_discount`,\n"
                + "        IFNULL(NET(IFNULL(SUM(`ipd`.`net`), 0.0),\n"
                + "                        IFNULL(`ip`.`discount_type`, 0.0),\n"
                + "                        IFNULL(`ip`.`discount`, 0.0),\n"
                + "                        IFNULL(`ip`.`tax_flag`, 0.0),\n"
                + "                        15,\n"
                + "                        0.0,\n"
                + "                        0.0,\n"
                + "                        0.0),\n"
                + "                0.0) AS `net`,\n"
                + "                IFNULL(NET(IFNULL(SUM(`ipd`.`net`), 0.0),\n"
                + "                        IFNULL(`ip`.`discount_type`, 0.0),\n"
                + "                        IFNULL(`ip`.`discount`, 0.0),\n"
                + "                        IFNULL(`ip`.`tax_flag`, 0.0),\n"
                + "                        0.0,\n"
                + "                        0.0,\n"
                + "                        0.0,\n"
                + "                        0.0),\n"
                + "                0.0) AS `netBeforTax`\n"
                + "    FROM\n"
                + "        `inv_purchase_invoice` `ip`\n"
                + "        LEFT JOIN `inv_purchase_invoice_detail` `ipd` ON `ip`.`id` = `ipd`.`inv_purchase_invoice_id`\n"
                + "        LEFT JOIN `inv_inventory` `inv` ON `inv`.`id` = `ip`.`galary_Id`\n"
                + "        LEFT JOIN `inv_organization_site` `ios` ON `ios`.`id` = `ip`.`organization_site_id`\n"
                + " ");
        sql.append(" where ip.branch_id = ").append(branchId);
        appendGallery1(salesReportByValueAdedDetails, sql);
        appendDate1(salesReportByValueAdedDetails, sql);
        sql.append(" GROUP BY `ip`.`id`");
        List<Object[]> res = dao.executeNativeQuery(sql.toString());
        for (Object[] object : res) {
            SalesReportByValueAdedDetails reportByValueAdedDetails = new SalesReportByValueAdedDetails();
            reportByValueAdedDetails.setInvoiceNumber((Integer) object[0]);
            reportByValueAdedDetails.setInvoiceType((Integer) object[1]);
            reportByValueAdedDetails.setInvoiceDate((Date) object[2]);
            reportByValueAdedDetails.setGalleryName((String) object[3]);
            reportByValueAdedDetails.setEmployeeName((String) object[4]);
            reportByValueAdedDetails.setDiscount((BigDecimal) object[5]);
            reportByValueAdedDetails.setTotalAfterTax((BigDecimal) object[6]);
            reportByValueAdedDetails.setTotalBeforeTax((BigDecimal) object[7]);
            salesReportByValueAdeds.add(reportByValueAdedDetails);
        }
        return salesReportByValueAdeds;
    }

    @Override
    public List<CustomerBalances> getCustomerBalances(CustomerBalances customerBalances, Integer branchId) {

        List<CustomerBalances> customerBalanceses = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT organization_code , organization_name , sum(exitt) as exitt , sum(adding) as adding\n"
                + "FROM toby.organization_site_statement_view \n"
                + " ");
        sql.append(" where branch_id = ").append(branchId);
        //appendGallery2(customerBalances, sql);
        sql.append(" group by organization_site_id\n");
        sql.append(" having (sum(exitt) - sum(adding) ) > 2 ");
        List<Object[]> res = dao.executeNativeQuery(sql.toString());
        for (Object[] object : res) {
            CustomerBalances customerBalances1 = new CustomerBalances();
            customerBalances1.setClientCode((String) object[0]);
            customerBalances1.setClientName((String) object[1]);
            customerBalances1.setDebit((BigDecimal) object[2]);
            customerBalances1.setCreditor((BigDecimal) object[3]);
            customerBalanceses.add(customerBalances1);
        }
        return customerBalanceses;
    }

    @Override
    public List<StatementOfBondsByBranche> getStatementOfBondsByBranch(StatementOfBondsByBranche statementOfBondsByBranche, Integer branchId) {

        List<StatementOfBondsByBranche> statementOfBondsByBrancheList = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select s.ScreenName , s.gl_bank_name , s.adding , s.gallary_id , s.gallary_code , s.gallary_name from (SELECT \n"
                + "        'قبض نقدي' AS `ScreenName`, `gb`.`name` AS `gl_bank_name`, sum(IFNULL(`gbtd`.`value_local`, 0) )AS `adding` , inv.id as gallary_id , inv.code as gallary_code , inv.name as gallary_name\n"
                + "    FROM\n"
                + "        `gl_bank_transaction` `gbt`\n"
                + "        LEFT JOIN `gl_bank_transaction_detail` `gbtd` ON `gbt`.`id` = `gbtd`.`gl_bank_transaction_id`\n"
                + "        LEFT JOIN `gl_bank` `gb` ON `gb`.`id` = `gbt`.`gl_bank_id`\n"
                + "        LEFT JOIN inv_inventory inv on inv.id = gb.gallary_id \n"
                + "    WHERE\n"
                + "        `gbt`.`serial` IS NOT NULL AND `gbt`.`payment_type` = 0 AND `gbtd`.`organization_site_id` IS NOT NULL \n"
                + "        AND `gbt`.`transaction_type`"
                + " ");
        sql.append(" AND gbt.branch_id = ").append(branchId);
        sql.append(" AND gb.id is not null AND gbt.transaction_type = 1\n");
        appendDate2(statementOfBondsByBranche, sql);
        sql.append(" group by gb.id , gb.gallary_id \n");
        sql.append(" union all\n"
                + " SELECT \n"
                + "        'قبض شيك' AS `ScreenName`, `gb`.`name` AS `gl_bank_name`, sum(IFNULL(`gbtd`.`value_local`, 0) )AS `adding` , inv.id as gallary_id , inv.code as gallary_code , inv.name as gallary_name\n"
                + "    FROM\n"
                + "        `gl_bank_transaction` `gbt`\n"
                + "        LEFT JOIN `gl_bank_transaction_detail` `gbtd` ON `gbt`.`id` = `gbtd`.`gl_bank_transaction_id`\n"
                + "        LEFT JOIN `gl_bank` `gb` ON `gb`.`id` = `gbt`.`gl_bank_id`\n"
                + "        LEFT JOIN inv_inventory inv on inv.id = gb.gallary_id\n"
                + "    WHERE\n"
                + "        `gbt`.`serial` IS NOT NULL AND `gbt`.`payment_type` = 1 AND `gbtd`.`organization_site_id` IS NOT NULL \n"
                + "        AND `gbt`.`transaction_type` "
                + " ");
        sql.append(" AND gbt.branch_id = ").append(branchId);
        sql.append(" AND gb.id is not null AND gbt.transaction_type = 1\n");
        appendDate2(statementOfBondsByBranche, sql);
        sql.append(" group by gb.id , gb.gallary_id \n");
        sql.append(" union all\n"
                + " SELECT \n"
                + "        'قبض شيك خطي' AS `ScreenName`, `gb`.`name` AS `gl_bank_name`, sum(IFNULL(`gbtd`.`value_local`, 0) )AS `adding` , inv.id as gallary_id , inv.code as gallary_code , inv.name as gallary_name\n"
                + "    FROM\n"
                + "        `gl_bank_transaction` `gbt`\n"
                + "        LEFT JOIN `gl_bank_transaction_detail` `gbtd` ON `gbt`.`id` = `gbtd`.`gl_bank_transaction_id`\n"
                + "        LEFT JOIN `gl_bank` `gb` ON `gb`.`id` = `gbt`.`gl_bank_id`\n"
                + "        LEFT JOIN inv_inventory inv on inv.id = gb.gallary_id\n"
                + "    WHERE\n"
                + "        `gbt`.`serial` IS NOT NULL AND `gbt`.`payment_type` = 2 AND `gbtd`.`organization_site_id` IS NOT NULL \n"
                + "        AND `gbt`.`transaction_type`"
                + " ");
        sql.append(" AND gbt.branch_id = ").append(branchId);
        sql.append(" AND gb.id is not null AND gbt.transaction_type = 1\n");
        appendDate2(statementOfBondsByBranche, sql);
        sql.append(" group by gb.id , gb.gallary_id \n");
        sql.append("  union all \n"
                + "    SELECT \n"
                + "        'صرف نقدي' AS `ScreenName`, `gb`.`name` AS `gl_bank_name`, sum(IFNULL(`gbtd`.`value_local`, 0) )AS `adding` , inv.id as gallary_id , inv.code as gallary_code , inv.name as gallary_name\n"
                + "    FROM\n"
                + "        `gl_bank_transaction` `gbt`\n"
                + "        LEFT JOIN `gl_bank_transaction_detail` `gbtd` ON `gbt`.`id` = `gbtd`.`gl_bank_transaction_id`\n"
                + "        LEFT JOIN `gl_bank` `gb` ON `gb`.`id` = `gbt`.`gl_bank_id`\n"
                + "        LEFT JOIN inv_inventory inv on inv.id = gb.gallary_id\n"
                + "    WHERE\n"
                + "        `gbt`.`serial` IS NOT NULL AND `gbt`.`payment_type` = 0 AND `gbtd`.`organization_site_id` IS NOT NULL \n"
                + "        AND `gbt`.`transaction_type`"
                + " ");
        sql.append(" AND gbt.branch_id = ").append(branchId);
        sql.append(" AND gb.id is not null AND gbt.transaction_type = 0\n");
        appendDate2(statementOfBondsByBranche, sql);
        sql.append(" group by gb.id , gb.gallary_id \n");
        sql.append("  union all\n"
                + " SELECT \n"
                + "        'صرف شيك' AS `ScreenName`, `gb`.`name` AS `gl_bank_name`, sum(IFNULL(`gbtd`.`value_local`, 0) )AS `adding` , inv.id as gallary_id , inv.code as gallary_code , inv.name as gallary_name\n"
                + "    FROM\n"
                + "        `gl_bank_transaction` `gbt`\n"
                + "        LEFT JOIN `gl_bank_transaction_detail` `gbtd` ON `gbt`.`id` = `gbtd`.`gl_bank_transaction_id`\n"
                + "        LEFT JOIN `gl_bank` `gb` ON `gb`.`id` = `gbt`.`gl_bank_id`\n"
                + "        LEFT JOIN inv_inventory inv on inv.id = gb.gallary_id\n"
                + "    WHERE\n"
                + "        `gbt`.`serial` IS NOT NULL AND `gbt`.`payment_type` = 1 AND `gbtd`.`organization_site_id` IS NOT NULL \n"
                + "        AND `gbt`.`transaction_type`"
                + " ");
        sql.append(" AND gbt.branch_id = ").append(branchId);
        sql.append(" AND gb.id is not null AND gbt.transaction_type = 0\n");
        appendDate2(statementOfBondsByBranche, sql);
        sql.append(" group by gb.id , gb.gallary_id \n");
        sql.append(" union all\n"
                + " SELECT \n"
                + "        'صرف شيك خطي' AS `ScreenName`, `gb`.`name` AS `gl_bank_name`, sum(IFNULL(`gbtd`.`value_local`, 0) )AS `adding` , inv.id as gallary_id , inv.code as gallary_code , inv.name as gallary_name\n"
                + "    FROM\n"
                + "        `gl_bank_transaction` `gbt`\n"
                + "        LEFT JOIN `gl_bank_transaction_detail` `gbtd` ON `gbt`.`id` = `gbtd`.`gl_bank_transaction_id`\n"
                + "        LEFT JOIN `gl_bank` `gb` ON `gb`.`id` = `gbt`.`gl_bank_id`\n"
                + "        LEFT JOIN inv_inventory inv on inv.id = gb.gallary_id\n"
                + "    WHERE\n"
                + "        `gbt`.`serial` IS NOT NULL AND `gbt`.`payment_type` = 2 AND `gbtd`.`organization_site_id` IS NOT NULL \n"
                + "        AND `gbt`.`transaction_type`"
                + " ");
        sql.append(" AND gbt.branch_id = ").append(branchId);
        sql.append(" AND gb.id is not null AND gbt.transaction_type = 0\n");
        appendDate2(statementOfBondsByBranche, sql);
        sql.append(" group by gb.id , gb.gallary_id \n");
        sql.append(" union all  \n"
                + " SELECT  'اشعار اضافة للعميل' AS `ScreenName`, 'بدون' AS `gl_bank_name`, sum(IFNULL(`value`, 0))AS `adding` , inv.id as gallary_id , inv.code as gallary_code , inv.name as gallary_name\n"
                + "    FROM `inv_notice` as inc \n"
                + "    left join inv_purchase_invoice as ipi on ipi.organization_site_id = inc.organization_site_id \n"
                + "        LEFT JOIN inv_inventory inv on inv.id = ipi.galary_id \n"
                + "    WHERE (inc.`type` = 0)"
                + " ");
        sql.append(" AND inc.branch_id = ").append(branchId);
        appendDate3(statementOfBondsByBranche, sql);
        sql.append(" group by ipi.galary_Id ");
        sql.append(" UNION ALL \n"
                + "    SELECT  'اشعار خصم عملاء' AS `ScreenName`, 'بدون' AS `gl_bank_name`, sum(IFNULL(`value`, 0))AS `adding`  , inv.id as gallary_id , inv.code as gallary_code , inv.name as gallary_name\n"
                + "    FROM `inv_notice` as inc \n"
                + "    left join inv_purchase_invoice as ipi on ipi.organization_site_id = inc.organization_site_id \n"
                + "        LEFT JOIN inv_inventory inv on inv.id = ipi.galary_id\n"
                + "    WHERE (`inc`.`type` = 1)"
                + " ");
        sql.append(" AND inc.branch_id = ").append(branchId);
        appendDate3(statementOfBondsByBranche, sql);
        sql.append(" group by ipi.galary_Id ");
        sql.append("  UNION ALL \n"
                + "    SELECT  'ااشعار دائن' AS `ScreenName`, 'بدون' AS `gl_bank_name`, sum(IFNULL(`value`, 0))AS `adding`  , inv.id as gallary_id , inv.code as gallary_code , inv.name as gallary_name\n"
                + "    FROM `inv_notice` as inc \n"
                + "    left join inv_purchase_invoice as ipi on ipi.organization_site_id = inc.organization_site_id \n"
                + "        LEFT JOIN inv_inventory inv on inv.id = ipi.galary_id\n"
                + "    WHERE (`inc`.`type` = 4)"
                + " ");
        sql.append(" AND inc.branch_id = ").append(branchId);
        appendDate3(statementOfBondsByBranche, sql);
        sql.append(" group by ipi.galary_Id ");
        sql.append("  UNION ALL\n"
                + "	SELECT  'اشعار اضافة للمورد' AS `ScreenName`, 'بدون' AS `gl_bank_name`, sum(IFNULL(`value`, 0))AS `adding`  , inv.id as gallary_id , inv.code as gallary_code , inv.name as gallary_name\n"
                + "    FROM `inv_notice` as inc \n"
                + "    left join inv_purchase_invoice as ipi on ipi.organization_site_id = inc.organization_site_id \n"
                + "        LEFT JOIN inv_inventory inv on inv.id = ipi.galary_id\n"
                + "    WHERE (inc.`type` = 2)"
                + " ");
        sql.append(" AND inc.branch_id = ").append(branchId);
        appendDate3(statementOfBondsByBranche, sql);
        sql.append(" group by ipi.galary_Id ");
        sql.append(" UNION ALL \n"
                + "    SELECT  'اشعار خصم موردين' AS `ScreenName`, 'بدون' AS `gl_bank_name`, sum(IFNULL(`value`, 0))AS `adding`  , inv.id as gallary_id , inv.code as gallary_code , inv.name as gallary_name\n"
                + "    FROM `inv_notice` as inc \n"
                + "    left join inv_purchase_invoice as ipi on ipi.organization_site_id = inc.organization_site_id\n"
                + "        LEFT JOIN inv_inventory inv on inv.id = ipi.galary_id \n"
                + "    WHERE (`inc`.`type` = 3)"
                + " ");
        sql.append(" AND inc.branch_id = ").append(branchId);
        appendDate3(statementOfBondsByBranche, sql);
        sql.append(" group by ipi.galary_Id) as s ");
        sql.append(" order by s.gallary_id ");
        List<Object[]> res = dao.executeNativeQuery(sql.toString());
        for (Object[] object : res) {
            statementOfBondsByBranche = new StatementOfBondsByBranche();
            statementOfBondsByBranche.setPaymentType((String) object[0]);
            statementOfBondsByBranche.setType((String) object[1]);
            statementOfBondsByBranche.setTotalAmount((BigDecimal) object[2]);
            statementOfBondsByBranche.setGalleryCode((String) object[4]);
            statementOfBondsByBranche.setGalleryName((String) object[5]);
            statementOfBondsByBrancheList.add(statementOfBondsByBranche);
        }
        return statementOfBondsByBrancheList;
    }

    @Override
    public List<InvoicesIssuedWithinAperiod> getInvoiceWithinAperiod(InvoicesIssuedWithinAperiod issuedWithinAperiod, Integer branchId) {

        List<InvoicesIssuedWithinAperiod> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT \n"
                + "        `ipi`.`id` AS `id`, `ipi`.`serial` AS `serial`,  `ipi`.`date` AS `date`, `ios`.`code` AS `organization_code`, `ios`.`name` AS `organization_name`,\n"
                + "        (CASE WHEN (SUM(IFNULL(`ipid`.`net`, 0)) = 0) THEN 0 ELSE NET(TRUNCATE(SUM(IFNULL(`ipid`.`net`, 0)),2),0,0,`ipi`.`tax_flag`,15,0,0,0)END) AS `total`,\n"
                + "        (CASE WHEN (SUM(IFNULL(`ipid`.`net`, 0)) = 0) THEN 0 ELSE NET(TRUNCATE(SUM(IFNULL(`ipid`.`net`, 0)),2),`ipi`.`discount_type`,`ipi`.`discount`,`ipi`.`tax_flag`,15,0,0, 0) END) AS `net`,\n"
                + "        (SELECT sum(IFNULL(`gbtd`.`value_local`, 0)) AS `adding` FROM  `gl_bank_transaction` `gbt` LEFT JOIN `gl_bank_transaction_detail` `gbtd` \n"
                + "        ON `gbt`.`id` = `gbtd`.`gl_bank_transaction_id` WHERE gbt.invoice_id = ipi.id) as payed,\n"
                + "        IFNULL((SELECT (CASE WHEN (SUM(IFNULL(`ipid2`.`net`, 0)) = 0) THEN 0 ELSE NET(TRUNCATE(SUM(IFNULL(`ipid2`.`net`, 0)),2),\n"
                + "       `ipi2`.`discount_type`,`ipi2`.`discount`,`ipi2`.`tax_flag`,15,0,0, 0) END) AS `net` FROM `inv_purchase_invoice` `ipi2` \n"
                + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid2` ON `ipi2`.`id` = `ipid2`.`inv_purchase_invoice_id` WHERE  `ipi2`.`parent` = ipi.id),0) as returnn\n"
                + "\n"
                + "    FROM\n"
                + "        `inv_purchase_invoice` `ipi` LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id` LEFT JOIN `inv_organization_site` `ios` \n"
                + "         ON `ios`.`id` = `ipi`.`organization_site_id`\n"
                + "         WHERE  `ipi`.`type` = 4  AND `ipi`.`date` IS NOT NULL"
                + " ");
        sql.append(" AND ipi.branch_id = ").append(branchId);
        appendDate4(issuedWithinAperiod, sql);
        sql.append(" GROUP BY  `ipi`.`id` , `ipi`.`date` , `ipi`.`organization_site_id` ");
        List<Object[]> res = dao.executeNativeQuery(sql.toString());
        for (Object[] object : res) {
            issuedWithinAperiod = new InvoicesIssuedWithinAperiod();
            issuedWithinAperiod.setInvoiceNumber((Integer) object[0]);
            issuedWithinAperiod.setClientSerial((Integer) object[1]);
            issuedWithinAperiod.setDate((Date) object[2]);
            issuedWithinAperiod.setClientCode((String) object[3]);
            issuedWithinAperiod.setClientName((String) object[4]);
            issuedWithinAperiod.setInvoiceValue((BigDecimal) object[5]);
            issuedWithinAperiod.setInvoiceNet((BigDecimal) object[6]);
            issuedWithinAperiod.setPayed((BigDecimal) object[7]);
            issuedWithinAperiod.setBouncedBack((BigDecimal) object[8]);
            list.add(issuedWithinAperiod);
        }
        return list;
    }

    @Override
    public List<CompanyInvoicesWithoutSewing> getCompanyInvoicesWithoutSewing(CompanyInvoicesWithoutSewing invoicesWithoutSewing, Integer branchId) {

        List<CompanyInvoicesWithoutSewing> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT inv.id , inv.name , oc.name as name , oc.code code , sum(ipid.net) as sum \n"
                + "FROM `inv_purchase_invoice` `ipi`\n"
                + "LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
                + "LEFT JOIN inv_organization_site ios on ios.id = ipi.organization_site_id\n"
                + "LEFT JOIN offer_company oc on oc.id = ios.offer_company_id\n"
                + "LEFT JOIN inv_inventory inv on inv.id = ipi.galary_Id\n"
                + "LEFT JOIN inv_item ii on ii.id = ipid.item_id\n"
                + "LEFT JOIN pro_group pg on pg.id = ii.pro_group_id\n"
                + "WHERE `ipid`.`item_id` IS NOT NULL AND ipi.type = 4 \n"
                + " ");
        sql.append(" AND `ipi`.`branch_id` = ").append(branchId);
        appendDate5(invoicesWithoutSewing, sql);
        sql.append(" and ios.offer_company_id is not null AND pg.id != 5\n ");
        sql.append(" GROUP BY ios.offer_company_id ");
        List<Object[]> res = dao.executeNativeQuery(sql.toString());
        for (Object[] object : res) {
            invoicesWithoutSewing = new CompanyInvoicesWithoutSewing();
            invoicesWithoutSewing.setId((Integer) object[0]);
            invoicesWithoutSewing.setGalleryName((String) object[1]);
            invoicesWithoutSewing.setName((String) object[2]);
            invoicesWithoutSewing.setCode((Integer) object[3]);
            invoicesWithoutSewing.setTotal((BigDecimal) object[4]);
            list.add(invoicesWithoutSewing);

        }
        return list;
    }

    private void appendGallery(SalesReportByValueAded salesReportByValueAded, StringBuilder query) {
        if (salesReportByValueAded.getGallerySelected() != null) {
            query.append(" and ip.gallery =  ").append(salesReportByValueAded.getGallerySelected());
        }
    }

    private void appendDate(SalesReportByValueAded salesReportByValueAded, StringBuilder query) {
        if (salesReportByValueAded.getDateFrom() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            query.append(" and ip.date >=  '").append(dateFormat.format(salesReportByValueAded.getDateFrom())).append("' ");
        }
        if (salesReportByValueAded.getDateTo() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            query.append(" and ip.date <=  '").append(dateFormat.format(salesReportByValueAded.getDateTo())).append("' ");
        }
    }

    private void appendGallery1(SalesReportByValueAdedDetails salesReportByValueAded, StringBuilder query) {
        if (salesReportByValueAded.getGallerySelected() != null) {
            query.append(" and ip.gallery =  ").append(salesReportByValueAded.getGallerySelected());
        }
    }

    private void appendDate1(SalesReportByValueAdedDetails salesReportByValueAded, StringBuilder query) {
        if (salesReportByValueAded.getDateFrom() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            query.append(" and ip.date >=  '").append(dateFormat.format(salesReportByValueAded.getDateFrom())).append("' ");
        }
        if (salesReportByValueAded.getDateTo() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            query.append(" and ip.date <=  '").append(dateFormat.format(salesReportByValueAded.getDateTo())).append("' ");
        }
    }

    private void appendGallery2(CustomerBalances customerBalances, StringBuilder query) {
        if (customerBalances.getGallerySelected() != null) {
            query.append(" and gallery =  ").append(customerBalances.getGallerySelected());
        }
    }

    private void appendDate2(StatementOfBondsByBranche statementOfBondsByBranche, StringBuilder query) {
        if (statementOfBondsByBranche.getDateFrom() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            query.append("  AND `gbt`.`date` >=   '").append(dateFormat.format(statementOfBondsByBranche.getDateFrom())).append("' ");
        }
        if (statementOfBondsByBranche.getDateTo() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            query.append("  AND `gbt`.`date` <=  '").append(dateFormat.format(statementOfBondsByBranche.getDateTo())).append("' ");
        }
    }

    private void appendDate3(StatementOfBondsByBranche statementOfBondsByBranche, StringBuilder query) {
        if (statementOfBondsByBranche.getDateFrom() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            query.append("  AND `inc`.`date` >=  '").append(dateFormat.format(statementOfBondsByBranche.getDateFrom())).append("' ");
        }
        if (statementOfBondsByBranche.getDateTo() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            query.append("  AND `inc`.`date` <=  '").append(dateFormat.format(statementOfBondsByBranche.getDateTo())).append("' ");
        }
    }

    private void appendDate4(InvoicesIssuedWithinAperiod issuedWithinAperiod, StringBuilder query) {
        if (issuedWithinAperiod.getDateFrom() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            query.append(" and `ipi`.`date` >=  '").append(dateFormat.format(issuedWithinAperiod.getDateFrom())).append("' ");
        }
        if (issuedWithinAperiod.getDateTo() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            query.append(" and `ipi`.`date` <=  '").append(dateFormat.format(issuedWithinAperiod.getDateTo())).append("' ");
        }
    }

    private void appendDate5(CompanyInvoicesWithoutSewing invoicesWithoutSewing, StringBuilder query) {
        if (invoicesWithoutSewing.getDateFrom() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            query.append(" and `ipi`.`date` >=  '").append(dateFormat.format(invoicesWithoutSewing.getDateFrom())).append("' ");
        }
        if (invoicesWithoutSewing.getDateTo() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            query.append(" and `ipi`.`date` <=  '").append(dateFormat.format(invoicesWithoutSewing.getDateTo())).append("' ");
        }
    }

}
