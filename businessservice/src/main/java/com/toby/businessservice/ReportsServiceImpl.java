/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.API.BankBalanceReportAPI;
import com.toby.bussinessservice.global.Utility;
import com.toby.core.GenericDAO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.OfferCompanyDTO;
import com.toby.dto.ProGroupDTO;
import com.toby.entity.ProProductionItemsTransaction;
import com.toby.views.ClientsByInvInventory;
import com.toby.views.CompanyInvoicesWithoutSewing;
import com.toby.views.CustomerBalances;
import com.toby.views.CustomerBalancesDebtAge;
import com.toby.views.CustomerBalancesDebtAgeSearch;
import com.toby.views.CustomersHaveNoMovementFromTheDate;
import com.toby.views.InvoicesIssuedWithinAperiod;
import com.toby.views.InvoicesRegisteredMoreThanOnce;
import com.toby.views.ItemBalancesStatement;
import com.toby.views.ProfitOfItemsSold;
import com.toby.views.SalesReportByValueAded;
import com.toby.views.SalesReportByValueAdedDetails;
import com.toby.views.SalesStatementForThePeriod;
import com.toby.views.StatementOfBondsByBranche;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
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
public class ReportsServiceImpl implements ReportsService {

    @EJB
    private GenericDAO dao;
    @EJB
    private InvPurchaseInvoiceService_1 invPurchaseInvoiceService;

    @Override  // بيان الفواتير حسب الضريبة 
    public List<SalesReportByValueAded> getSalesReportByValueAded(SalesReportByValueAded salesReportByValueAded, Integer branchId) {

        dao.executeNativeQuery("select toby.sql_mode_function()");
//        dao.executeNativeQuery("select toby.net_after_discount_head()");
        List<SalesReportByValueAded> salesReportByValueAdeds = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select sum(s.net) , sum(s.netBeforTax) , s.gallary_name  from ( ");

        daribaSql(sql, branchId, salesReportByValueAded);

        sql.append(" ) as s group by s.gallary_name ");
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

    private StringBuilder daribaSql(StringBuilder sql, Integer branchId, SalesReportByValueAded salesReportByValueAded) {
        sql.append(" SELECT \n"
                + "        `ip`.`serial` AS `serial`, `ip`.`type` AS `type`,`ip`.`date` AS `date`,`inv`.`name` AS `gallary_name`,\n"
                + "        `ios`.`name` AS `organization_site_name`,`ip`.`discount` AS `head_discount`,\n"
                + "        ( IFNULL(SUM(`ipd`.`net_after_discount_head`), 0.0) * 1.15) AS `net`,\n"
                + "        IFNULL(SUM(`ipd`.`net_after_discount_head`), 0.0) AS `netBeforTax`\n"
                + "    FROM\n"
                + "        `inv_purchase_invoice` `ip` LEFT JOIN `inv_purchase_invoice_detail` `ipd` ON `ip`.`id` = `ipd`.`inv_purchase_invoice_id`\n"
                + "        LEFT JOIN `inv_inventory` `inv` ON `inv`.`id` = `ip`.`galary_Id` LEFT JOIN `inv_organization_site` `ios` ON `ios`.`id` = `ip`.`organization_site_id`\n"
                + " ");
        sql.append("  where  ip.type = 4  AND ip.invoice_type IS NULL and ip.branch_id = ").append(branchId);
        appendGallery(salesReportByValueAded, sql);
        appendDate(salesReportByValueAded, sql);
        sql.append(" GROUP BY `ip`.`id` ");

        sql.append(" union all ");
        sql.append(" SELECT \n"
                + "        `ip`.`serial` AS `serial`, `ip`.`type` AS `type`,`ip`.`date` AS `date`,`inv`.`name` AS `gallary_name`,\n"
                + "        `ios`.`name` AS `organization_site_name`,`ip`.`discount` AS `head_discount`,\n"
                + "        ((IFNULL(SUM(`ipd`.`net_after_discount_head`), 0.0) * 1.15)*-1) AS `net`,\n"
                + "        (IFNULL(SUM(`ipd`.`net_after_discount_head`), 0.0) *-1)  AS `netBeforTax`\n"
                + "    FROM\n"
                + "        `inv_purchase_invoice` `ip` LEFT JOIN `inv_purchase_invoice_detail` `ipd` ON `ip`.`id` = `ipd`.`inv_purchase_invoice_id`\n"
                + "        LEFT JOIN `inv_inventory` `inv` ON `inv`.`id` = `ip`.`galary_Id` LEFT JOIN `inv_organization_site` `ios` ON `ios`.`id` = `ip`.`organization_site_id`\n"
                + " ");
        sql.append("  where ip.type = 3 and ip.branch_id = ").append(branchId);
        appendGallery(salesReportByValueAded, sql);
        appendDate(salesReportByValueAded, sql);
        sql.append(" GROUP BY `ip`.`id` ");

        sql.append(" union all ");
        sql.append(" SELECT \n"
                + "        `invt`.`serial` AS `serial`, `invt`.`type` AS `type`,`invt`.`date` AS `date`,`inv`.`name` AS `gallary_name`,\n"
                + "        `ios`.`name` AS `organization_site_name`,null AS `head_discount`,\n"
                + "        (invt.value * -1) AS `net`,\n"
                + "                (((invt.value / 1.15) )* -1) AS `netBeforTax`\n"
                + "    FROM\n"
                + "       `inv_notice` `invt` \n"
                + "        LEFT JOIN `inv_inventory` `inv` ON `inv`.`id` = `invt`.`inv_inventory_id` LEFT JOIN `inv_organization_site` `ios` ON `ios`.`id` = `invt`.`organization_site_id`\n"
                + " ");
        sql.append("  where ( invt.type = 1 or invt.type = 4 ) and invt.branch_id = ").append(branchId);
        appendGalleryInvNotice(salesReportByValueAded, sql);
        appendDateInvoice(salesReportByValueAded, sql);
        sql.append(" GROUP BY `invt`.`id` ");

        return sql;
    }

    @Override
    public List<SalesReportByValueAdedDetails> getSalesReportByValueAdedDetails(SalesReportByValueAded salesReportByValueAded, Integer branchId) {

        List<SalesReportByValueAdedDetails> salesReportByValueAdeds = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql = daribaSql(sql, branchId, salesReportByValueAded);
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
            reportByValueAdedDetails.setTax(reportByValueAdedDetails.getTotalAfterTax().subtract(reportByValueAdedDetails.getTotalBeforeTax()));
            salesReportByValueAdeds.add(reportByValueAdedDetails);
        }
        return salesReportByValueAdeds;
    }

    @Override
    public List<CustomerBalances> getCustomerBalances(CustomerBalances customerBalances, Integer branchId) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        List<CustomerBalances> customerBalanceses = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT organization_code , organization_name , sum(exitt) as exitt , sum(adding) as adding , openning_balance , organization_site_id \n"
                + "FROM toby.organization_site_statement_view \n"
                + " ");
        sql.append(" where organization_type = 0 and branch_id = ").append(branchId);
        appendGallery2(customerBalances, sql);
        sql.append(" group by organization_site_id\n");
        sql.append(" having (sum(exitt) - sum(adding) ) != 0 ");
        List<Object[]> res = dao.executeNativeQuery(sql.toString());
        for (Object[] object : res) {
            CustomerBalances customerBalances1 = new CustomerBalances();
            customerBalances1.setClientCode((String) object[0]);
            customerBalances1.setClientName((String) object[1]);
            customerBalances1.setDebit((BigDecimal) object[2]);
            customerBalances1.setCreditor((BigDecimal) object[3]);
            customerBalances1.setOpenningBalance((BigDecimal) object[4]);
            customerBalances1.setOrganizationsiteId((Integer) object[5]);
            customerBalanceses.add(customerBalances1);
        }
        return customerBalanceses;
    }

    @Override
    public List<CustomerBalancesDebtAge> getCustomerBalancesDebtAge(CustomerBalancesDebtAgeSearch customerBalancesDebtAge) {
        if (customerBalancesDebtAge.getGallary() != null && customerBalancesDebtAge.getGallary().getId() != null && customerBalancesDebtAge.getDateToSearch() != null) {
            dao.executeNativeQuery("select toby.sql_mode_function()");
            customerBalancesDebtAge.setDatefromTotal(customerBalancesDebtAge.getDateToSearch());
            for (int i = 1; i <= 12; i++) {
                switch (i) {
                    case 1:
                        customerBalancesDebtAge.setDateto1(Utility.subDateAndNumer(customerBalancesDebtAge.getDateToSearch(), ((i - 1) * 30)));
                        customerBalancesDebtAge.setDatefrom1(Utility.subDateAndNumer(customerBalancesDebtAge.getDateToSearch(), (i * 30)));
                        break;
                    case 2:
                        customerBalancesDebtAge.setDateto2(Utility.subDateAndNumer(customerBalancesDebtAge.getDateToSearch(), ((i - 1) * 30)));
                        customerBalancesDebtAge.setDatefrom2(Utility.subDateAndNumer(customerBalancesDebtAge.getDateToSearch(), (i * 30)));
                        break;
                    case 3:
                        customerBalancesDebtAge.setDateto3(Utility.subDateAndNumer(customerBalancesDebtAge.getDateToSearch(), ((i - 1) * 30)));
                        customerBalancesDebtAge.setDatefrom3(Utility.subDateAndNumer(customerBalancesDebtAge.getDateToSearch(), (i * 30)));
                        break;
                    case 4:
                        customerBalancesDebtAge.setDateto4(Utility.subDateAndNumer(customerBalancesDebtAge.getDateToSearch(), ((i - 1) * 30)));
                        customerBalancesDebtAge.setDatefrom4(Utility.subDateAndNumer(customerBalancesDebtAge.getDateToSearch(), (i * 30)));
                        break;
                    case 5:
                        customerBalancesDebtAge.setDateto5(Utility.subDateAndNumer(customerBalancesDebtAge.getDateToSearch(), ((i - 1) * 30)));
                        customerBalancesDebtAge.setDatefrom5(Utility.subDateAndNumer(customerBalancesDebtAge.getDateToSearch(), (i * 30)));
                        break;
                    case 6:
                        customerBalancesDebtAge.setDateto6(Utility.subDateAndNumer(customerBalancesDebtAge.getDateToSearch(), ((i - 1) * 30)));
                        customerBalancesDebtAge.setDatefrom6(Utility.subDateAndNumer(customerBalancesDebtAge.getDateToSearch(), (i * 30)));
                        break;
                    case 7:
                        customerBalancesDebtAge.setDateto7(Utility.subDateAndNumer(customerBalancesDebtAge.getDateToSearch(), ((i - 1) * 30)));
                        customerBalancesDebtAge.setDatefrom7(Utility.subDateAndNumer(customerBalancesDebtAge.getDateToSearch(), (i * 30)));
                        break;
                    case 8:
                        customerBalancesDebtAge.setDateto8(Utility.subDateAndNumer(customerBalancesDebtAge.getDateToSearch(), ((i - 1) * 30)));
                        customerBalancesDebtAge.setDatefrom8(Utility.subDateAndNumer(customerBalancesDebtAge.getDateToSearch(), (i * 30)));
                        break;
                    case 9:
                        customerBalancesDebtAge.setDateto9(Utility.subDateAndNumer(customerBalancesDebtAge.getDateToSearch(), ((i - 1) * 30)));
                        customerBalancesDebtAge.setDatefrom9(Utility.subDateAndNumer(customerBalancesDebtAge.getDateToSearch(), (i * 30)));
                        break;
                    case 10:
                        customerBalancesDebtAge.setDateto10(Utility.subDateAndNumer(customerBalancesDebtAge.getDateToSearch(), ((i - 1) * 30)));
                        customerBalancesDebtAge.setDatefrom10(Utility.subDateAndNumer(customerBalancesDebtAge.getDateToSearch(), (i * 30)));
                        break;
                    case 11:
                        customerBalancesDebtAge.setDateto11(Utility.subDateAndNumer(customerBalancesDebtAge.getDateToSearch(), ((i - 1) * 30)));
                        customerBalancesDebtAge.setDatefrom11(Utility.subDateAndNumer(customerBalancesDebtAge.getDateToSearch(), (i * 30)));
                        break;
                    case 12:
                        customerBalancesDebtAge.setDateto12(Utility.subDateAndNumer(customerBalancesDebtAge.getDateToSearch(), ((i - 1) * 30)));
                        customerBalancesDebtAge.setDatefrom12(Utility.subDateAndNumer(customerBalancesDebtAge.getDateToSearch(), (i * 30)));
                        break;
                    default:
                        break;
                }
            }

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

            customerBalancesDebtAge.setDatefromBefore(Utility.subDateAndNumer(customerBalancesDebtAge.getDatefrom12(), 1));
            List<CustomerBalancesDebtAge> customerBalancesDebtAgeList = new ArrayList<>();
            StringBuilder sql = new StringBuilder();
            sql.append(" select ios.code , ios.name \n"
                    + " , balance_customer_date(ios.id ,'").append(dateFormat.format(customerBalancesDebtAge.getDatefromTotal())).append("' ) as balanceTotal , balance_customer_date(ios.id ,'").append(dateFormat.format(customerBalancesDebtAge.getDatefromBefore())).append("' ) as balanceBefore \n"
                    + " , balance_customer(ios.id ,'").append(dateFormat.format(customerBalancesDebtAge.getDatefrom1())).append("','").append(dateFormat.format(customerBalancesDebtAge.getDateto1())).append("' ) as m1 , balance_customer(ios.id ,'").append(dateFormat.format(customerBalancesDebtAge.getDatefrom2())).append("','").append(dateFormat.format(customerBalancesDebtAge.getDateto2())).append("' ) as m2 , balance_customer(ios.id ,'").append(dateFormat.format(customerBalancesDebtAge.getDatefrom3())).append("','").append(dateFormat.format(customerBalancesDebtAge.getDateto3())).append("' ) as m3\n"
                    + " , balance_customer(ios.id ,'").append(dateFormat.format(customerBalancesDebtAge.getDatefrom4())).append("','").append(dateFormat.format(customerBalancesDebtAge.getDateto4())).append("' ) as m4 , balance_customer(ios.id ,'").append(dateFormat.format(customerBalancesDebtAge.getDatefrom5())).append("','").append(dateFormat.format(customerBalancesDebtAge.getDateto5())).append("' ) as m5 , balance_customer(ios.id ,'").append(dateFormat.format(customerBalancesDebtAge.getDatefrom6())).append("','").append(dateFormat.format(customerBalancesDebtAge.getDateto6())).append("' ) as m6\n"
                    + " , balance_customer(ios.id ,'").append(dateFormat.format(customerBalancesDebtAge.getDatefrom7())).append("','").append(dateFormat.format(customerBalancesDebtAge.getDateto7())).append("' ) as m7 , balance_customer(ios.id ,'").append(dateFormat.format(customerBalancesDebtAge.getDatefrom8())).append("','").append(dateFormat.format(customerBalancesDebtAge.getDateto8())).append("' ) as m8 , balance_customer(ios.id ,'").append(dateFormat.format(customerBalancesDebtAge.getDatefrom9())).append("','").append(dateFormat.format(customerBalancesDebtAge.getDateto9())).append("' ) as m9\n"
                    + " , balance_customer(ios.id ,'").append(dateFormat.format(customerBalancesDebtAge.getDatefrom10())).append("','").append(dateFormat.format(customerBalancesDebtAge.getDateto10())).append("' ) as m10, balance_customer(ios.id ,'").append(dateFormat.format(customerBalancesDebtAge.getDatefrom11())).append("','").append(dateFormat.format(customerBalancesDebtAge.getDateto11())).append("' ) as m11, balance_customer(ios.id ,'").append(dateFormat.format(customerBalancesDebtAge.getDatefrom12())).append("','").append(dateFormat.format(customerBalancesDebtAge.getDateto12())).append("' ) as m12\n"
                    + " from inv_organization_site ios where ios.id in (select distinct(ipi.organization_site_id) from inv_purchase_invoice ipi where ipi.branch_id = 182 ");
            sql.append(" and ipi.galary_Id = ").append(customerBalancesDebtAge.getGallary().getId());
            sql.append(" )");
            List<Object[]> res = dao.executeNativeQuery(sql.toString());
            for (Object[] object : res) {
                CustomerBalancesDebtAge customerBalances1 = new CustomerBalancesDebtAge();
                customerBalances1.setCode((String) object[0]);
                customerBalances1.setName((String) object[1]);
                customerBalances1.setBalanceTotal((BigDecimal) object[2]);
                customerBalances1.setBalanceBefore((BigDecimal) object[3]);
                customerBalances1.setM1((BigDecimal) object[4]);
                customerBalances1.setM2((BigDecimal) object[5]);
                customerBalances1.setM3((BigDecimal) object[6]);
                customerBalances1.setM4((BigDecimal) object[7]);
                customerBalances1.setM5((BigDecimal) object[8]);
                customerBalances1.setM6((BigDecimal) object[9]);
                customerBalances1.setM7((BigDecimal) object[10]);
                customerBalances1.setM8((BigDecimal) object[11]);
                customerBalances1.setM9((BigDecimal) object[12]);
                customerBalances1.setM10((BigDecimal) object[13]);
                customerBalances1.setM11((BigDecimal) object[14]);
                customerBalances1.setM12((BigDecimal) object[15]);
                customerBalancesDebtAgeList.add(customerBalances1);
            }

            return customerBalancesDebtAgeList;
        }
        return null;
    }

    @Override
    public List<StatementOfBondsByBranche> getStatementOfBondsByBranch(StatementOfBondsByBranche statementOfBondsByBranche, Integer branchId) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
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
                + "        (CASE WHEN (SUM(IFNULL(`ipid`.`net`, 0)) = 0) THEN 0 ELSE NET(TRUNCATE(SUM(IFNULL(`ipid`.`net`, 0)),2),0,0,`ipi`.`tax_flag`,15,IFNULL(`ip`.`disc_halala`, 0.0))END) AS `total`,\n"
                + "        (CASE WHEN (SUM(IFNULL(`ipid`.`net`, 0)) = 0) THEN 0 ELSE NET(TRUNCATE(SUM(IFNULL(`ipid`.`net`, 0)),2),`ipi`.`discount_type`,`ipi`.`discount`,`ipi`.`tax_flag`,15,IFNULL(`ip`.`disc_halala`, 0.0)) END) AS `net`,\n"
                + "        (SELECT sum(IFNULL(`gbtd`.`value_local`, 0)) AS `adding` FROM  `gl_bank_transaction` `gbt` LEFT JOIN `gl_bank_transaction_detail` `gbtd` \n"
                + "        ON `gbt`.`id` = `gbtd`.`gl_bank_transaction_id` WHERE gbt.invoice_id = ipi.id) as payed,\n"
                + "        IFNULL((SELECT (CASE WHEN (SUM(IFNULL(`ipid2`.`net`, 0)) = 0) THEN 0 ELSE NET(TRUNCATE(SUM(IFNULL(`ipid2`.`net`, 0)),2),\n"
                + "       `ipi2`.`discount_type`,`ipi2`.`discount`,`ipi2`.`tax_flag`,15,IFNULL(`ip`.`disc_halala`, 0.0)) END) AS `net` FROM `inv_purchase_invoice` `ipi2` \n"
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
        sql.append(" SELECT inv.id , inv.name , oc.name as name , oc.code code , sum(ipid.net_after_discount_head) as sum \n"
                + " FROM `inv_purchase_invoice` `ipi`\n"
                + " LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
                + " LEFT JOIN inv_organization_site ios on ios.id = ipi.organization_site_id\n"
                + " LEFT JOIN offer_company oc on oc.id = ios.offer_company_id\n"
                + " LEFT JOIN inv_inventory inv on inv.id = ipi.galary_Id\n"
                + " LEFT JOIN inv_item ii on ii.id = ipid.item_id\n"
                + " LEFT JOIN pro_group pg on pg.id = ii.pro_group_id\n"
                + " WHERE `ipid`.`item_id` IS NOT NULL AND ipi.type = 4 \n"
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

    @Override
    public List<ItemBalancesStatement> getItemBalancesStatement(ItemBalancesStatement itemBalancesStatement, Integer branchId) {

        List<ItemBalancesStatement> itemBalancesStatementList = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select ii.code , ii.name  ,\n"
                + "( select  ipi2.date \n"
                + " from inv_purchase_invoice_detail ipid2 left join inv_purchase_invoice ipi2 \n"
                + " on ipi2.id = ipid2.inv_purchase_invoice_id where ipid2.item_id = ii.id "
                + "");
        if (itemBalancesStatement.getFromDate() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            sql.append(" and ipi2.date <  '").append(dateFormat.format(itemBalancesStatement.getFromDate())).append("' ");
        }
        sql.append(" order by ipi2.id desc limit 1) as last_date,ii.max_price_men as sell_price , ii.cost_average as cost_average");
        sql.append(" from inv_item ii left join pro_group pg on pg.id = ii.pro_group_id "
                + " ");
        sql.append("  where ii.branch_id = ").append(branchId);
        sql.append(" and pg.sort = 1 and ii.id not in (select item_id \n"
                + " from inv_purchase_invoice_detail ipid \n"
                + " left join inv_purchase_invoice ipi on ipi.id = ipid.inv_purchase_invoice_id\n"
                + " ");
        sql.append("  where ii.branch_id = ").append(branchId);
        appendDate6(itemBalancesStatement, sql);
        sql.append(" ) ");
        List<Object[]> res = dao.executeNativeQuery(sql.toString());
        for (Object[] object : res) {
            itemBalancesStatement = new ItemBalancesStatement();
            itemBalancesStatement.setCode((String) object[0]);
            itemBalancesStatement.setName((String) object[1]);
            itemBalancesStatement.setDate((Date) object[2]);
            itemBalancesStatement.setSallary((BigDecimal) object[3]);
            itemBalancesStatement.setCostAverage((BigDecimal) object[4]);
            itemBalancesStatementList.add(itemBalancesStatement);
        }
        return itemBalancesStatementList;
    }

    @Override
    public List<ProfitOfItemsSold> getProfitOfItemsSold(ProfitOfItemsSold profitOfItemsSold, Integer branchId, Integer type, Integer invoiceType) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        List<ProfitOfItemsSold> profitOfItemsSoldList = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select ii.id , ii.code , ii.name , IFNULL(ii.cost_average,0) as cost_average , sum(IFNULL(ipid.quantity,0)) as totalSell , \n"
                + "sum(IFNULL(ipid.discount,0)) as totalDiscount  , sum(IFNULL(ipid.net_after_discount_head,0)) as totalNet , sum(IFNULL(ipid.number,0)) as totalNumber\n"
                + "from inv_purchase_invoice_detail ipid \n"
                + "left join inv_purchase_invoice ipi on ipi.id = ipid.inv_purchase_invoice_id left join inv_item ii on ii.id = ipid.item_id\n"
                + "left join pro_group p on p.id = ii.pro_group_id");
        sql.append(" where ipid.branch_id = ").append(branchId);
        sql.append(" and ipi.type = ").append(type);

        if (invoiceType != null) {
            sql.append(" and ipi.invoice_type =  ").append(invoiceType);
        }

        if (profitOfItemsSold.getItemCode() != null && !profitOfItemsSold.getItemCode().isEmpty()) {
            sql.append(" and ii.code =  ").append(profitOfItemsSold.getItemCode());
        }

        appendDate7(profitOfItemsSold, sql);
        StringBuilder gallaryIds = null;
        StringBuilder groupId = null;
        for (InvInventoryDTO invInventoryDTO : profitOfItemsSold.getGallarySelected()) {
            if (gallaryIds == null) {
                gallaryIds = new StringBuilder();
                gallaryIds.append(invInventoryDTO.getId());
            } else {
                gallaryIds.append(",").append(invInventoryDTO.getId());
            }
        }
        for (ProGroupDTO proGroupDTO : profitOfItemsSold.getGroupDTOSelected()) {
            if (groupId == null) {
                groupId = new StringBuilder();
                groupId.append(proGroupDTO.getId());
            } else {
                groupId.append(",").append(proGroupDTO.getId());
            }
        }
        appendGallery7(gallaryIds, sql);
        appendProGroup(groupId, sql);
        sql.append(" group by ii.id");
        sql.append(" order by sum(ipid.number) desc ");
        List<Object[]> res = dao.executeNativeQuery(sql.toString());
        for (Object[] object : res) {
            profitOfItemsSold = new ProfitOfItemsSold();
            profitOfItemsSold.setCode((String) object[1]);
            profitOfItemsSold.setName((String) object[2]);
            profitOfItemsSold.setCostAverage((BigDecimal) object[3]);
            profitOfItemsSold.setTotallSell((BigDecimal) object[4]);
            profitOfItemsSold.setTotallDiscount((BigDecimal) object[5]);
            profitOfItemsSold.setTotallNet((BigDecimal) object[6]);
            profitOfItemsSold.setTotallNumber((BigDecimal) object[7]);
            profitOfItemsSoldList.add(profitOfItemsSold);
        }
        return profitOfItemsSoldList;
    }

    @Override
    public List<SalesStatementForThePeriod> getSalesStatementForThePeriod(SalesStatementForThePeriod statementForThePeriod) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        List<SalesStatementForThePeriod> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select ipi.serial , ipi.date , ios.code , ios.name as iosName ,\n"
                + "IFNULL(SUM(`ipid`.`net_after_discount_head`), 0) as total_net,\n"
                + " ipi.id ,iinv.name as gallaryName ,IFNULL(NET(TRUNCATE(SUM(IFNULL(`net`, 0)), 2), ipi.`discount_type`, ipi.`discount`, `tax_flag`, 15, IFNULL(`disc_halala`, 0)),0) as net\n"
                + "from inv_purchase_invoice ipi\n"
                + "left join  inv_purchase_invoice_detail ipid on ipi.id = ipid.inv_purchase_invoice_id\n"
                + "left join inv_organization_site ios on ios.id = ipi.organization_site_id left join inv_inventory iinv on iinv.id = ipi.galary_Id  \n"
                + " where ipi.type = 4  ");
        appendGallery5(statementForThePeriod, sql);
        appendOrganizationId(statementForThePeriod, sql);
        appendDate8(statementForThePeriod, sql);
        sql.append(" group by ipi.id order by ipi.galary_Id ");
        List<Object[]> res = dao.executeNativeQuery(sql.toString());
        for (Object[] object : res) {
            if (object[0] != null) {
                statementForThePeriod = new SalesStatementForThePeriod();
                statementForThePeriod.setSerial((Integer) object[0]);
                statementForThePeriod.setInvoiceDate((Date) object[1]);
                statementForThePeriod.setCode((String) object[2]);
                statementForThePeriod.setName((String) object[3]);
                statementForThePeriod.setTotalNet((BigDecimal) object[4]);
                statementForThePeriod.setNet((BigDecimal) object[7]);
//                statementForThePeriod.setNet((statementForThePeriod.getTotalNet().multiply(new BigDecimal(1.15))).setScale(3, RoundingMode.HALF_UP));
                statementForThePeriod.setInvoiceId((Integer) object[5]);
                statementForThePeriod.setInvoiceStatus(invPurchaseInvoiceService.findInvoiceStatus(statementForThePeriod.getInvoiceId()));
//                statementForThePeriod.setPaid(invPurchaseInvoiceService.findPayed(statementForThePeriod.getInvoiceId()));
//                statementForThePeriod.setGalleryName((String) object[6]);

                statementForThePeriod.setReturnPurchaseValue(findInvReturnValue(statementForThePeriod.getInvoiceId()));
                statementForThePeriod.setGalleryName((String) object[6]);

                Map<String, Object> params = new HashMap<>();
                params.put("invPurchaseInvoiceId", statementForThePeriod.getInvoiceId());
                List<BigDecimal> noticeDebit = dao.findListByQuery("SELECT  sum(i.value)    FROM InvNotice i WHERE i.invInvoice.id = :invPurchaseInvoiceId AND i.type = 0 Group by i.invInvoice.id", params);
                List<BigDecimal> noticeCredit = dao.findListByQuery("SELECT  sum(i.value)    FROM InvNotice i WHERE i.invInvoice.id = :invPurchaseInvoiceId AND i.type in (1,4)  Group by i.invInvoice.id", params);

                List<BigDecimal> details = dao.findListByQuery("SELECT  sum(i.value)    FROM GlBankTransaction i WHERE i.invoiceId.id = :invPurchaseInvoiceId Group by i.invoiceId.id", params);
                List<BigDecimal> detailsReturn = dao.findListByQuery("SELECT  sum(i.value)    FROM GlBankTransactionDetail i WHERE i.invoiceId.id = :invPurchaseInvoiceId Group by i.invoiceId.id", params);
                BigDecimal payment = (details != null && !details.isEmpty()) ? details.get(0).subtract((detailsReturn != null && !detailsReturn.isEmpty()) ? detailsReturn.get(0) : BigDecimal.ZERO) : BigDecimal.ZERO;

                BigDecimal noticeDebitValue = (noticeDebit != null && !noticeDebit.isEmpty()) ? noticeDebit.get(0) : BigDecimal.ZERO;
                BigDecimal noticeCreditValue = (noticeCredit != null && !noticeCredit.isEmpty()) ? noticeCredit.get(0) : BigDecimal.ZERO;

                statementForThePeriod.setInvNoticeValue(noticeCreditValue.subtract(noticeDebitValue));
                statementForThePeriod.setNoticeDebit(noticeDebitValue);
                statementForThePeriod.setNoticeCredit(noticeCreditValue);
                statementForThePeriod.setCash(payment);

                list.add(statementForThePeriod);
            }
        }
        return list;
    }

    public BigDecimal findInvNoticeValue(Integer invPurchaseInvoiceId) {
        List<BigDecimal> value = null;
        if (invPurchaseInvoiceId != null && invPurchaseInvoiceId > 0) {
            value = dao.executeNativeQuery("SELECT sum(value) from inv_notice where branch_id = 182 and inv_invoice =  " + invPurchaseInvoiceId);
        }
        return (value != null && !value.isEmpty()) ? value.get(0) : BigDecimal.ZERO;
    }

    public BigDecimal findInvReturnValue(Integer invPurchaseInvoiceId) {
        List<BigDecimal> value = null;
        if (invPurchaseInvoiceId != null && invPurchaseInvoiceId > 0) {
            value = dao.executeNativeQuery("SELECT IFNULL(SUM(ipid.net_after_discount_head), 0) from inv_purchase_invoice ipi left join  inv_purchase_invoice_detail ipid on ipi.id = ipid.inv_purchase_invoice_id where ipi.branch_id = 182 and ipi.type = 3 and ipi.parent =  " + invPurchaseInvoiceId);
        }
        return (value != null && !value.isEmpty()) ? (value.get(0).multiply(new BigDecimal(1.15))).setScale(3, RoundingMode.HALF_UP) : BigDecimal.ZERO;
    }

    @Override
    public List<SalesStatementForThePeriod> getSalesStatementForThePeriodForPay(SalesStatementForThePeriod statementForThePeriod) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        List<SalesStatementForThePeriod> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select ipi.serial , ipi.date , ios.code , ios.name as iosName ,\n"
                + "IFNULL(SUM(`ipid`.`net_after_discount_head`), 0) as total_net,\n"
                + " ipi.id ,iinv.name as gallaryName ,IFNULL(NET(TRUNCATE(SUM(IFNULL(`net`, 0)), 2), ipi.`discount_type`, ipi.`discount`, `tax_flag`, 15, IFNULL(`disc_halala`, 0)),0) as net\n"
                + "from inv_purchase_invoice ipi\n"
                + "left join  inv_purchase_invoice_detail ipid on ipi.id = ipid.inv_purchase_invoice_id\n"
                + "left join inv_organization_site ios on ios.id = ipi.organization_site_id "
                + "left join inv_inventory iinv on iinv.id = ipi.galary_Id  	"
                + "left join gl_bank_transaction gb on ipi.id = gb.invoice_id  \n"
                + " where ipi.type = 4 ");
        appendGallery5(statementForThePeriod, sql);
        appendDateForPay(statementForThePeriod, sql);
        sql.append(" group by ipi.id order by ipi.galary_Id ");
        List<Object[]> res = dao.executeNativeQuery(sql.toString());
        for (Object[] object : res) {
            if (object[0] != null) {
                statementForThePeriod = new SalesStatementForThePeriod();
                statementForThePeriod.setSerial((Integer) object[0]);
                statementForThePeriod.setInvoiceDate((Date) object[1]);
                statementForThePeriod.setCode((String) object[2]);
                statementForThePeriod.setName((String) object[3]);
                statementForThePeriod.setTotalNet((BigDecimal) object[4]);
                statementForThePeriod.setNet((BigDecimal) object[7]);
                statementForThePeriod.setInvoiceId((Integer) object[5]);
                statementForThePeriod.setInvoiceStatus(invPurchaseInvoiceService.findInvoiceStatus(statementForThePeriod.getInvoiceId()));
                statementForThePeriod.setPaid(invPurchaseInvoiceService.findPayed(statementForThePeriod.getInvoiceId()));
                statementForThePeriod.setCash(statementForThePeriod.getPaid());
                statementForThePeriod.setGalleryName((String) object[6]);
                list.add(statementForThePeriod);
            }
        }
        return list;
    }

    @Override
    public List<InvoicesRegisteredMoreThanOnce> getInvoicesRegisteredMoreThanOnce(InvoicesRegisteredMoreThanOnce registeredMoreThanOnce, Integer serial, Date date, Integer branchId) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        List<InvoicesRegisteredMoreThanOnce> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ipi.id ,pps.name,ii.name , ifnull(sum(ppit.number),0) as workNumber , ifnull(ipid.number,0) as originNumber, pps.id , ii.id ,pfdd.id,\n"
                + "sum(ppit.number)-(ipid.number) as subtraction ,pfd.date,ipi.serial\n"
                + "FROM toby.pro_production_items_transaction ppit\n"
                + "left join pro_production_stages pps on pps.id = ppit.pro_production_stages_id\n"
                + "left join pro_factory_delivery_detail pfdd on pfdd.id = ppit.pro_factory_delivery_detail_id\n"
                + "left join pro_factory_delivery pfd on pfd.id = pfdd.pro_factory_delivery_id\n"
                + "left join inv_purchase_invoice_detail ipid on ipid.id = pfdd.inv_purchase_invoice_detail_id\n"
                + "left join inv_item ii on ii.id = ipid.item_id\n"
                + "left join inv_purchase_invoice ipi on ipi.id = ipid.inv_purchase_invoice_id\n");
        sql.append(" where ");
        sql.append(" ipi.branch_id = ").append(branchId);

        if (serial != null) {
            sql.append(" AND ipi.serial = ").append(serial);
        }
        appendDate9(registeredMoreThanOnce, sql, serial);
        sql.append(" group by pps.id , ii.id , ipi.id\n");
        if (serial == null) {
            sql.append(" having workNumber > originNumber ");
        }
        List<Object[]> res = dao.executeNativeQuery(sql.toString());
        for (Object[] object : res) {
            registeredMoreThanOnce = new InvoicesRegisteredMoreThanOnce();
            registeredMoreThanOnce.setStageName((String) object[1]);
            registeredMoreThanOnce.setItemName((String) object[2]);
            registeredMoreThanOnce.setWorkNumber((BigDecimal) object[3]);
            registeredMoreThanOnce.setOriginNumber((BigDecimal) object[4]);
            registeredMoreThanOnce.setStageId((Integer) object[5]);
            registeredMoreThanOnce.setFactoryId((Integer) object[7]);
            registeredMoreThanOnce.setSubtraction((BigDecimal) object[8]);
            registeredMoreThanOnce.setReceivedDate((Date) object[9]);
            registeredMoreThanOnce.setSerial((Integer) object[10]);
            list.add(registeredMoreThanOnce);
        }

        return list;
    }

    @Override
    public void deleteIncrease(Integer proProductionStagesId, Integer proFactoryDeliveryDetailId, BigDecimal workNumber, BigDecimal originNumber) {
        StringBuilder sql = new StringBuilder();
        sql.append("select  id , number from pro_production_items_transaction where pro_production_stages_id = ").append(proProductionStagesId);
        sql.append(" AND pro_factory_delivery_detail_id = ").append(proFactoryDeliveryDetailId);
        List<Object[]> res = dao.executeNativeQuery(sql.toString());
        for (Object[] object : res) {
            BigDecimal number = (BigDecimal) object[1];
            if (workNumber.compareTo(originNumber) == 1) {
                ProProductionItemsTransaction itemsTransaction = new ProProductionItemsTransaction();
                itemsTransaction.setId((Integer) object[0]);
                dao.deleteEntity(itemsTransaction);
                workNumber = workNumber.subtract(number);
            }
        }
    }

    @Override
    public List<CustomersHaveNoMovementFromTheDate> getCustomersHaveNoMovementFromTheDate(CustomersHaveNoMovementFromTheDate noMovementFromTheDate, Integer branchId) {

        List<CustomersHaveNoMovementFromTheDate> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select ios.name,ios.mobile from inv_organization_site ios\n"
                + " where ios.branch_id = ").append(branchId);
        appendGallery6(noMovementFromTheDate, sql);
        sql.append(" and ios.id not in( "
                + " select ipi.organization_site_id from inv_purchase_invoice ipi\n"
                + " where ipi.branch_id = ").append(branchId);
        appendDate10(noMovementFromTheDate, sql);
        sql.append(" ) ");
        List<Object[]> res = dao.executeNativeQuery(sql.toString());
        for (Object[] object : res) {
            noMovementFromTheDate = new CustomersHaveNoMovementFromTheDate();
            noMovementFromTheDate.setName((String) object[0]);
            noMovementFromTheDate.setMobile((String) object[1]);
            list.add(noMovementFromTheDate);
        }
        return list;
    }

    @Override
    public List<ClientsByInvInventory> getClientsByInvInventory(ClientsByInvInventory invInventory, Integer branchId) {

        List<ClientsByInvInventory> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ios.name,ios.code,ios.mobile FROM toby.inv_organization_site ios\n"
                //                + "left join inv_inventory ii on ii.id = ios.gallary_id\n"
                //                + "left join offer_company oc on oc.id = ios.offer_company_id\n"
                + " where ios.branch_id = ").append(branchId);

        StringBuilder gallaryIds = null;
        for (InvInventoryDTO invInventoryDTO : invInventory.getGallaryListSelected()) {
            if (gallaryIds == null) {
                gallaryIds = new StringBuilder();
                gallaryIds.append(invInventoryDTO.getId());
            } else {
                gallaryIds.append(",").append(invInventoryDTO.getId());
            }
        }
        StringBuilder offerCompanyId = null;
        for (OfferCompanyDTO offerCompanyDTO : invInventory.getOfferCompanyListSelected()) {
            if (offerCompanyId == null) {
                offerCompanyId = new StringBuilder();
                offerCompanyId.append(offerCompanyDTO.getId());
            } else {
                offerCompanyId.append(",").append(offerCompanyDTO.getId());
            }
        }
        appendGallery9(gallaryIds, sql);
        appendOfferCompany(offerCompanyId, sql);
        List<Object[]> res = dao.executeNativeQuery(sql.toString());
        for (Object[] object : res) {
            invInventory = new ClientsByInvInventory();
            invInventory.setClientName((String) object[0]);
            invInventory.setClientCode((String) object[1]);
            invInventory.setClientMobile((String) object[2]);
            list.add(invInventory);
        }

        return list;
    }

    private void appendGallery(SalesReportByValueAded salesReportByValueAded, StringBuilder query) {
        if (salesReportByValueAded.getGallerySelected() != null) {
            query.append(" and ip.galary_Id =  ").append(salesReportByValueAded.getGallerySelected().getId());
        }
    }

    private void appendGalleryInvNotice(SalesReportByValueAded salesReportByValueAded, StringBuilder query) {
        if (salesReportByValueAded.getGallerySelected() != null) {
            query.append(" and invt.inv_inventory_id =  ").append(salesReportByValueAded.getGallerySelected().getId());
        }
    }

    private void appendDateInvoice(SalesReportByValueAded salesReportByValueAded, StringBuilder query) {
        if (salesReportByValueAded.getDateFrom() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            query.append(" and invt.date >=  '").append(dateFormat.format(salesReportByValueAded.getDateFrom())).append("' ");
        }
        if (salesReportByValueAded.getDateTo() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            query.append(" and invt.date <=  '").append(dateFormat.format(salesReportByValueAded.getDateTo())).append("' ");
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
            query.append(" and ip.galary_Id =  ").append(salesReportByValueAded.getGallerySelected().getId());
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
            query.append(" and organization_gallary_id =  ").append(customerBalances.getGallerySelected().getId());
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

    private void appendDate6(ItemBalancesStatement itemBalancesStatement, StringBuilder query) {
        if (itemBalancesStatement.getDateFrom() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            query.append(" AND ipi.date >=  '").append(dateFormat.format(itemBalancesStatement.getDateFrom())).append("' ");
        }
        if (itemBalancesStatement.getDateTo() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            query.append(" and ipi.date <=   '").append(dateFormat.format(itemBalancesStatement.getDateTo())).append("' ");
        }
    }

    private void appendDate7(ProfitOfItemsSold profitOfItemsSold, StringBuilder sql) {
        if (profitOfItemsSold.getDateFrom() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            sql.append(" AND ipi.date >=  '").append(dateFormat.format(profitOfItemsSold.getDateFrom())).append("' ");
        }
        if (profitOfItemsSold.getDateTo() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            sql.append(" and ipi.date <=   '").append(dateFormat.format(profitOfItemsSold.getDateTo())).append("' ");
        }
    }

    private void appendGallery7(StringBuilder gallaryIds, StringBuilder sql) {
        if (gallaryIds != null) {
            sql.append(" and ipi.galary_Id in ( ").append(gallaryIds.toString()).append(" )");
        }
    }

    private void appendProGroup(StringBuilder groupId, StringBuilder sql) {
        if (groupId != null) {
            sql.append(" and ii.pro_group_id in(  ").append(groupId).append(" )");
        }
    }

    private void appendDate8(SalesStatementForThePeriod statementForThePeriod, StringBuilder sql) {
        if (statementForThePeriod.getDateFrom() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            sql.append(" AND ipi.date >=  '").append(dateFormat.format(statementForThePeriod.getDateFrom())).append("' ");
        }
        if (statementForThePeriod.getDateTo() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            sql.append(" and ipi.date <=   '").append(dateFormat.format(statementForThePeriod.getDateTo())).append("' ");
        }
    }

    private void appendDateForPay(SalesStatementForThePeriod statementForThePeriod, StringBuilder sql) {
        if (statementForThePeriod.getDateFrom() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            sql.append(" AND ipi.date <  '").append(dateFormat.format(statementForThePeriod.getDateFrom())).append("' ");

//            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            sql.append(" and gb.date >=   '").append(dateFormat.format(statementForThePeriod.getDateFrom())).append("' ");
        }
    }

    private void appendGallery5(SalesStatementForThePeriod statementForThePeriod, StringBuilder query) {
        if (statementForThePeriod.getGallerySelected() != null) {
            query.append(" and ipi.galary_Id =  ").append(statementForThePeriod.getGallerySelected().getId());
        }
    }

    private void appendOrganizationId(SalesStatementForThePeriod statementForThePeriod, StringBuilder query) {
        if (statementForThePeriod.getOrganizationId() != null) {
            query.append(" and ipi.organization_site_id =  ").append(statementForThePeriod.getOrganizationId());
        }
    }

    private void appendSerial(InvoicesRegisteredMoreThanOnce registeredMoreThanOnce, StringBuilder query) {
        if (registeredMoreThanOnce.getSerialSelected() != null) {
            query.append(" where ipi.serial = ").append(registeredMoreThanOnce.getSerialSelected().getSerial());
        }
    }

    private void appendDate9(InvoicesRegisteredMoreThanOnce registeredMoreThanOnce, StringBuilder sql, Integer serial) {
        if (registeredMoreThanOnce.getDate() != null && serial == null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            sql.append("  and pfd.date =  '").append(dateFormat.format(registeredMoreThanOnce.getDate())).append("' ");
        }

    }

    private void appendDate10(CustomersHaveNoMovementFromTheDate noMovementFromTheDate, StringBuilder sql) {
        if (noMovementFromTheDate.getDate() != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            sql.append("  and ipi.date >=  '").append(dateFormat.format(noMovementFromTheDate.getDate())).append("' ");
        }

    }

    private void appendGallery6(CustomersHaveNoMovementFromTheDate noMovementFromTheDate, StringBuilder query) {
        if (noMovementFromTheDate.getGallerySelected() != null) {
            query.append(" and ios.gallary_id =  ").append(noMovementFromTheDate.getGallerySelected().getId());
        }
    }

    private void appendGallery9(StringBuilder gallaryIds, StringBuilder sql) {
        if (gallaryIds != null) {
            sql.append("  and ios.gallary_id in ( ").append(gallaryIds.toString()).append(" )");
        }
    }

    private void appendOfferCompany(StringBuilder offerCompanyId, StringBuilder sql) {
        if (offerCompanyId != null) {
            sql.append("  and ios.offer_company_id in ( ").append(offerCompanyId.toString()).append(" )");
        } else {
            sql.append("  and ios.offer_company_id IS NULL");
        }
    }

    @Override
    public BigDecimal getValueOfInvoice(Integer invoiceId) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        List<BigDecimal> value = null;
        if (invoiceId != null && invoiceId > 0) {
            value = dao.executeNativeQuery("SELECT IFNULL(NET(TRUNCATE(SUM(IFNULL(`net`, 0)), 2), ipi.`discount_type`, ipi.`discount`, `tax_flag`, 15, IFNULL(`disc_halala`, 0)),0) from inv_purchase_invoice ipi left join  inv_purchase_invoice_detail ipid on ipi.id = ipid.inv_purchase_invoice_id where ipi.id =  " + invoiceId);
        }
        return (value != null && !value.isEmpty()) ? (value.get(0)) : BigDecimal.ZERO;
//        StringBuilder sql = new StringBuilder();
//        sql.append("select IFNULL(NET(TRUNCATE(SUM(IFNULL(`net`, 0)), 2), ipi.`discount_type`, ipi.`discount`, `tax_flag`, 15, IFNULL(`disc_halala`, 0)),0) as net\n"
//                + "from inv_purchase_invoice ipi\n"
//                + " where ipi.id =   " + invoiceId);
//
//        sql.append(" group by ipi.id ");
//        List<BigDecimal> res = dao.executeNativeQuery(sql.toString());
//        if (res != null && !res.isEmpty() && res.get(0) != null) {
//            return res.get(0);
//        }
//        return BigDecimal.ZERO;
//    }
    }

    @Override  // بيان الفواتير حسب الضريبة 
    public List<SalesReportByValueAded> getSalesReportByValueAdedAPI(BankBalanceReportAPI bankBalanceReportAPI) {
        List<SalesReportByValueAded> salesReportByValueAdedList = new ArrayList<>();

        SalesReportByValueAded salesReportByValueAded = new SalesReportByValueAded();
        for (InvInventoryDTO inventoryDTO : bankBalanceReportAPI.getInvInventoryDTOList()) {
            salesReportByValueAded.setDateFrom(bankBalanceReportAPI.getDateFrom());
            salesReportByValueAded.setDateTo(bankBalanceReportAPI.getDateTo());
            salesReportByValueAded.setGallerySelected(inventoryDTO);
            List<SalesReportByValueAded> valueAdedList = getSalesReportByValueAded(salesReportByValueAded, bankBalanceReportAPI.getBranchId());
            salesReportByValueAdedList.addAll(valueAdedList);
        }

        return salesReportByValueAdedList;
    }

    @Override
    public List<SalesReportByValueAdedDetails> getSalesReportByValueAdedDetailsAPI(BankBalanceReportAPI bankBalanceReportAPI) {
        List<SalesReportByValueAdedDetails> salesReportByValueAdedDetailseList = new ArrayList<>();
        SalesReportByValueAded salesReportByValueAded = new SalesReportByValueAded();
        salesReportByValueAded.setDateFrom(bankBalanceReportAPI.getDateFrom());
        salesReportByValueAded.setDateTo(bankBalanceReportAPI.getDateTo());
        for (InvInventoryDTO invInventoryDTO : bankBalanceReportAPI.getInvInventoryDTOList()) {
            salesReportByValueAded.setGallerySelected(invInventoryDTO);
            List<SalesReportByValueAdedDetails> list = getSalesReportByValueAdedDetails(salesReportByValueAded, bankBalanceReportAPI.getBranchId());
            salesReportByValueAdedDetailseList.addAll(list);
        }

        return salesReportByValueAdedDetailseList;
    }

    @Override
    public List<CustomerBalances> getCustomerBalancesAPI(BankBalanceReportAPI bankBalanceReportAPI) {
        CustomerBalances customerBalances = new CustomerBalances();
        if (bankBalanceReportAPI != null) {
            customerBalances.setGallerySelected(bankBalanceReportAPI.getGallarySellected());
        }
        return getCustomerBalances(customerBalances, bankBalanceReportAPI.getBranchId());
    }

    @Override
    public List<CustomerBalancesDebtAge> getCustomerBalancesDebtAgeAPI(BankBalanceReportAPI bankBalanceReportAPI) {
        CustomerBalancesDebtAgeSearch customerBalancesDebtAgeSearch = new CustomerBalancesDebtAgeSearch();
        if (bankBalanceReportAPI != null) {
            customerBalancesDebtAgeSearch.setDateToSearch(bankBalanceReportAPI.getDateFrom());
            customerBalancesDebtAgeSearch.setGallary(bankBalanceReportAPI.getGallarySellected());
        }
        return getCustomerBalancesDebtAge(customerBalancesDebtAgeSearch);
    }

    @Override
    public List<CompanyInvoicesWithoutSewing> getCompanyInvoicesWithoutSewingAPI(BankBalanceReportAPI bankBalanceReportAPI) {
        CompanyInvoicesWithoutSewing companyInvoicesWithoutSewing = new CompanyInvoicesWithoutSewing();
        if (bankBalanceReportAPI != null) {
            companyInvoicesWithoutSewing.setDateFrom(bankBalanceReportAPI.getDateFrom());
            companyInvoicesWithoutSewing.setDateTo(bankBalanceReportAPI.getDateTo());
        }
        return getCompanyInvoicesWithoutSewing(companyInvoicesWithoutSewing, bankBalanceReportAPI.getBranchId());
    }

    @Override
    public List<ItemBalancesStatement> getItemBalancesStatementAPI(BankBalanceReportAPI bankBalanceReportAPI) {

        ItemBalancesStatement itemBalancesStatement = new ItemBalancesStatement();

        if (itemBalancesStatement != null) {
            itemBalancesStatement.setDateFrom(bankBalanceReportAPI.getDateFrom());
            itemBalancesStatement.setDateTo(bankBalanceReportAPI.getDateTo());

        }
        return getItemBalancesStatement(itemBalancesStatement, bankBalanceReportAPI.getBranchId());
    }

}
