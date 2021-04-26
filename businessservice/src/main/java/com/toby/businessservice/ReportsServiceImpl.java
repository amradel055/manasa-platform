/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.views.CustomerBalances;
import com.toby.views.SalesReportByValueAded;
import com.toby.views.SalesReportByValueAdedDetails;
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
}
