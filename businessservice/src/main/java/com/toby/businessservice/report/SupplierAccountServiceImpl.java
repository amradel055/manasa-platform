/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.businessservice.reports.searchBean.SupplierAccountSearchBean;
import com.toby.core.GenericDAO;
import com.toby.views.OrganizationSiteStatementView;
import java.math.BigDecimal;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hhhh
 */
@Stateless
public class SupplierAccountServiceImpl implements SupplierAccountService {

    @EJB
    private GenericDAO dao;
    StringBuilder appendQuery;

    @Override
    public List<OrganizationSiteStatementView> getAllOrganizationSiteStatementSearchBean(SupplierAccountSearchBean supplierAccountSearchBean) {
        Map<String, Object> params = new HashMap<>();

        params.put("branchId", supplierAccountSearchBean.getBranchId());
//        params.put("type", supplierAccountSearchBean.getOrganizationType());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT RIV FROM OrganizationSiteStatementView RIV WHERE RIV.branchId = :branchId and   ");

        int type = supplierAccountSearchBean.getOrganizationType();
        if (type == 0) {
            stringBuilder.append("(RIV.organizationType = 0 or RIV.organizationType = 2)");
        } else if (type == 1) {
            stringBuilder.append(" (RIV.organizationType = 1 or RIV.organizationType = 2)");
        } else if (type == 4) {
            stringBuilder.append(" RIV.organizationType = 4");
        }

        appendDate(supplierAccountSearchBean, stringBuilder, params);
        appendOrganization(supplierAccountSearchBean, stringBuilder, params);
        stringBuilder.append(" order by  RIV.date");

        List<OrganizationSiteStatementView> details = dao.findListByQuery(stringBuilder.toString(), params);
        return details;
    }

    @Override
    public List<OrganizationSiteStatementView> getAllOrganizationSiteStatementSearchBeanById(SupplierAccountSearchBean supplierAccountSearchBean) {
        Map<String, Object> params = new HashMap<>();

        params.put("branchId", supplierAccountSearchBean.getBranchId());
//        params.put("type", supplierAccountSearchBean.getOrganizationType());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT RIV FROM OrganizationSiteStatementView RIV WHERE RIV.branchId = :branchId and   ");

        int type = supplierAccountSearchBean.getOrganizationType();
        if (type == 0) {
            stringBuilder.append("(RIV.organizationType = 0 or RIV.organizationType = 2)");
        } else if (type == 1) {
            stringBuilder.append(" (RIV.organizationType = 1 or RIV.organizationType = 2)");
        } else if (type == 4) {
            stringBuilder.append(" RIV.organizationType = 4");
        }

        appendDate(supplierAccountSearchBean, stringBuilder, params);
        if (supplierAccountSearchBean.getFromorganizationNameDTO() != null) {
            params.put("FromOrganization", supplierAccountSearchBean.getFromorganizationNameDTO().getId());
            stringBuilder.append(" AND RIV.organizationSiteId = :FromOrganization ");
        }
        stringBuilder.append(" order by  RIV.date");

        List<OrganizationSiteStatementView> details = dao.findListByQuery(stringBuilder.toString(), params);
        return details;
    }

    private void appendOrganization(SupplierAccountSearchBean supplierAccountSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (supplierAccountSearchBean.getFromorganizationNameDTO() != null) {
            params.put("FromOrganization", supplierAccountSearchBean.getFromorganizationNameDTO().getCode());
            stringBuilder.append(" AND RIV.organizationCode >= :FromOrganization ");
        }

        if (supplierAccountSearchBean.getFromorganizationNameDTO() != null) {
            params.put("ToOrganization", supplierAccountSearchBean.getFromorganizationNameDTO().getCode());
            stringBuilder.append(" AND RIV.organizationCode <= :ToOrganization ");
        }

        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void appendDate(SupplierAccountSearchBean supplierAccountSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (supplierAccountSearchBean.getDateFrom() != null) {
            params.put("fromDate", supplierAccountSearchBean.getDateFrom());
            stringBuilder.append(" AND RIV.date >= :fromDate");
        }

        if (supplierAccountSearchBean.getDateTo() != null) {
            params.put("toDate", supplierAccountSearchBean.getDateTo());
            stringBuilder.append(" AND RIV.date <= :toDate");
        }
    }

    @Override
    public List<OrganizationSiteStatementView> getAllOpeningBalanceBeforeBalanceBySearchBean(SupplierAccountSearchBean supplierAccountSearchBean) {

        Map<String, Object> params = new HashMap<>();
        params.put("branchId", supplierAccountSearchBean.getBranchId());
        params.put("type", supplierAccountSearchBean.getOrganizationType());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT RIV.organizationSiteId , (SUM(RIV.adding) - SUM(RIV.exitt)) FROM OrganizationSiteStatementView RIV WHERE RIV.branchId = :branchId and RIV.organizationType != :type ");
        appendOrganization(supplierAccountSearchBean, stringBuilder, params);
        appendDateBefore(supplierAccountSearchBean, stringBuilder, params);
        stringBuilder.append(" group by RIV.organizationSiteId");
        List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);
        List<OrganizationSiteStatementView> statementViews = prepareList(res);
        return statementViews;
    }

    private List<OrganizationSiteStatementView> prepareList(List<Object[]> res) {
        List<OrganizationSiteStatementView> list = new ArrayList<>();
        OrganizationSiteStatementView siteStatementView;
        for (Object[] object : res) {
            siteStatementView = new OrganizationSiteStatementView();
            siteStatementView.setOrganizationSiteId((Integer) object[0]);
            siteStatementView.setOpenningBalance((BigDecimal) object[1]);

            list.add(siteStatementView);
        }

        return list;
    }

    private void appendDateBefore(SupplierAccountSearchBean supplierAccountSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (supplierAccountSearchBean.getDateFrom() != null) {
            params.put("fromDate", supplierAccountSearchBean.getDateFrom());
            stringBuilder.append(" AND RIV.date < :fromDate");
        }
    }

    @Override
    public BigDecimal getAccountForOrganizationSite(Integer organizationSiteId) {
        Map<String, Object> params = new HashMap<>();

        params.put("organizationSiteId", organizationSiteId);
//        params.put("type", supplierAccountSearchBean.getOrganizationType());

        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("SELECT sum(RIV.exitt) - sum(RIV.adding) FROM OrganizationSiteStatementView RIV WHERE RIV.organizationSiteId = :organizationSiteId  ");
        stringBuilder.append("SELECT (sum(s.exitt) - sum(s.adding)) as balance from (SELECT 0 AS `exitt`, IFNULL(NET(TRUNCATE(SUM(IFNULL(`net`, 0)), 2), ipi.`discount_type`, ipi.`discount`, `tax_flag`, 15, IFNULL(`disc_halala`, 0)),0) AS `adding`\n"
                + "	FROM `inv_purchase_invoice` `ipi` LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
                + "	WHERE ((`ipi`.`type` = 0) AND (`ipi`.`date` IS NOT NULL)) "
                + "     AND ipi.organization_site_id = " + organizationSiteId + "\n"
                + "    group by ipi.id\n"
                + "UNION ALL SELECT IFNULL(NET(TRUNCATE(SUM(IFNULL(`net`, 0)), 2), ipi.`discount_type`, ipi.`discount`, `tax_flag`, 15, IFNULL(`disc_halala`, 0)),0)  AS `exitt`, 0 AS `adding`\n"
                + "    FROM `inv_purchase_invoice` `ipi` LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
                + "    WHERE ((`ipi`.`type` = 4)  AND (`ipi`.`date` IS NOT NULL)) "
                + "     AND ipi.organization_site_id = " + organizationSiteId + "\n"
                + "    group by ipi.id\n"
                + "UNION ALL SELECT IFNULL(NET(TRUNCATE(SUM(IFNULL(`net`, 0)), 2), ipi.`discount_type`, ipi.`discount`, `tax_flag`, 15, IFNULL(`disc_halala`, 0)),0)  AS `exitt`, 0 AS `adding`\n"
                + "    FROM  `inv_purchase_invoice` `ipi` LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
                + "    WHERE ((`ipi`.`type` = 2)  AND (`ipi`.`date` IS NOT NULL)) "
                + "     AND ipi.organization_site_id = " + organizationSiteId + "\n"
                + "    group by ipi.id\n"
                + "UNION ALL SELECT 0 AS `exitt`, IFNULL(NET(TRUNCATE(SUM(IFNULL(`net`, 0)), 2), ipi.`discount_type`, ipi.`discount`, `tax_flag`, 15, IFNULL(`disc_halala`, 0)),0) AS `adding`\n"
                + "    FROM `inv_purchase_invoice` `ipi` LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
                + "    WHERE ((`ipi`.`type` = 3) AND (`ipi`.`date` IS NOT NULL)) "
                + "     AND ipi.organization_site_id = " + organizationSiteId + "\n"
                + "    group by ipi.id\n"
                + "UNION ALL SELECT 0 AS `exitt`, IFNULL(`gbtd`.`value_local`, 0) AS `adding`\n"
                + "    FROM `gl_bank_transaction` `gbt`  LEFT JOIN `gl_bank_transaction_detail` `gbtd` ON `gbt`.`id` = `gbtd`.`gl_bank_transaction_id`\n"
                + "    WHERE ((`gbt`.`payment_type` = 0) "
                + "     AND gbtd.organization_site_id = " + organizationSiteId + "\n"
                + "AND (`gbt`.`transaction_type` = 1)) \n"
                + "    GROUP BY  `gbt`.`id`\n"
                + "UNION ALL SELECT 0 AS `exitt`, IFNULL(`gbtd`.`value_local`, 0) AS `adding`\n"
                + "	FROM `gl_bank_transaction` `gbt` LEFT JOIN `gl_bank_transaction_detail` `gbtd` ON `gbt`.`id` = `gbtd`.`gl_bank_transaction_id`\n"
                + "    WHERE (`gbt`.`payment_type` = 1) "
                + "     AND gbtd.organization_site_id = " + organizationSiteId + "\n"
                + "AND (`gbt`.`transaction_type` = 1)\n"
                + "    GROUP BY  `gbt`.`id`\n"
                + "UNION ALL SELECT 0 AS `exitt`, IFNULL(`gbtd`.`value_local`, 0) AS `adding`\n"
                + "    FROM `gl_bank_transaction` `gbt`  LEFT JOIN `gl_bank_transaction_detail` `gbtd` ON `gbt`.`id` = `gbtd`.`gl_bank_transaction_id`\n"
                + "    WHERE (`gbt`.`payment_type` = 2)  "
                + "     AND gbtd.organization_site_id = " + organizationSiteId + "\n"
                + "AND (`gbt`.`transaction_type` = 1)\n"
                + "    GROUP BY  `gbt`.`id`\n"
                + "UNION ALL SELECT   SUM(IFNULL(`gbtd`.`value_local`, 0)) AS `exitt`, 0 AS `adding`\n"
                + "    FROM `gl_bank_transaction` `gbt`  LEFT JOIN `gl_bank_transaction_detail` `gbtd` ON `gbt`.`id` = `gbtd`.`gl_bank_transaction_id`\n"
                + "    WHERE (`gbt`.`payment_type` = 0)  "
                + "     AND gbtd.organization_site_id = " + organizationSiteId + "\n"
                + "AND (`gbt`.`transaction_type` = 0)\n"
                + "    GROUP BY  `gbt`.`id`\n"
                + "UNION ALL SELECT     SUM(IFNULL(`gbtd`.`value_local`, 0)) AS `exitt`, 0 AS `adding`\n"
                + "    FROM `gl_bank_transaction` `gbt` LEFT JOIN `gl_bank_transaction_detail` `gbtd` ON `gbt`.`id` = `gbtd`.`gl_bank_transaction_id`\n"
                + "    WHERE (`gbt`.`payment_type` = 1) "
                + "     AND gbtd.organization_site_id = " + organizationSiteId + "\n"
                + "AND (`gbt`.`transaction_type` = 0)\n"
                + "    GROUP BY  `gbt`.`id` \n"
                + "UNION ALL SELECT  SUM(IFNULL(`gbtd`.`value_local`, 0)) AS `exitt`,  0 AS `adding` \n"
                + "    FROM `gl_bank_transaction` `gbt` LEFT JOIN `gl_bank_transaction_detail` `gbtd` ON `gbt`.`id` = `gbtd`.`gl_bank_transaction_id`\n"
                + "    WHERE  (`gbt`.`payment_type` = 2) "
                + "     AND gbtd.organization_site_id = " + organizationSiteId + "\n"
                + "AND (`gbt`.`transaction_type` = 0)\n"
                + "    GROUP BY `gbt`.`id`\n"
                + "UNION ALL SELECT IFNULL(`ios`.`open_balance_debit`, 0) AS `exitt`, IFNULL(`ios`.`open_balance_credit`, 0) AS `adding`\n"
                + "    FROM  `inv_organization_site` `ios` WHERE  "
                + " `ios`.id = " + organizationSiteId + "\n"
                + "UNION ALL SELECT  IFNULL(`in`.`value`, 0) AS `exitt`,  0 AS `adding`\n"
                + "    FROM `inv_notice` `in`   WHERE (`in`.`type` = 0)  "
                + "and `in`.organization_site_id =  " + organizationSiteId + "\n"
                + "UNION ALL SELECT   0 AS `exitt`, IFNULL(`in`.`value`, 0) AS `adding`\n"
                + "    FROM `inv_notice` `in` WHERE  (`in`.`type` = 1 or `in`.`type` = 4 )  "
                + "and `in`.organization_site_id =  " + organizationSiteId + "\n"
                + "UNION ALL SELECT  0 AS `exitt`,IFNULL(`in`.`value`, 0) AS `adding`\n"
                + "    FROM `inv_notice` `in` WHERE  (`in`.`type` = 2) "
                + "and `in`.organization_site_id =  " + organizationSiteId + "\n"
                + "UNION ALL SELECT  IFNULL(`in`.`value`, 0) AS `exitt`,  0 AS `adding`\n"
                + "    FROM  `inv_notice` `in`   WHERE   (`in`.`type` = 3) "
                + "and `in`.organization_site_id =  " + organizationSiteId + "\n"
                + "    ) s ");

        List<BigDecimal> details = dao.executeNativeQuery(stringBuilder.toString());
        return (details != null && !details.isEmpty()) ? (details.get(0) == null ? BigDecimal.ZERO : details.get(0).setScale(3, BigDecimal.ROUND_UP)) : BigDecimal.ZERO;
    }
}
