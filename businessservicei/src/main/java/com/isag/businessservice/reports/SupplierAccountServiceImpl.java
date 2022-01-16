/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.searchBean.SupplierAccountSearchBean;
import com.isag.core.GenericDAO;

import com.isag.views.OrganizationSiteStatementView;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.HashMap;
import java.util.Iterator;
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
        stringBuilder.append(" order by  RIV.date , RIV.supplierInvoiceNumber");

        List<OrganizationSiteStatementView> details = dao.findListByQuery(stringBuilder.toString(), params);
        return details;
    }

    private void appendOrganization(SupplierAccountSearchBean supplierAccountSearchBean, StringBuilder stringBuilder, Map<String, Object> params) {

        if (supplierAccountSearchBean.getFromorganizationNameDTO() != null) {
            params.put("FromOrganization", supplierAccountSearchBean.getFromorganizationNameDTO().getCode());
            stringBuilder.append(" AND RIV.organizationCode >= :FromOrganization ");
        }

        if (supplierAccountSearchBean.getToorganizationNameDTO() != null) {
            params.put("ToOrganization", supplierAccountSearchBean.getToorganizationNameDTO().getCode());
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
//        params.put("type", supplierAccountSearchBean.getOrganizationType());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT RIV.organizationSiteId , (SUM(RIV.adding) - SUM(RIV.exitt)) FROM OrganizationSiteStatementView RIV WHERE RIV.branchId = :branchId  ");
        int type = supplierAccountSearchBean.getOrganizationType();
        if (type == 0) {
            stringBuilder.append(" AND (RIV.organizationType = 0 or RIV.organizationType = 2)");
        } else if (type == 1) {
            stringBuilder.append(" AND (RIV.organizationType = 1 or RIV.organizationType = 2)");
        } else if (type == 4) {
            stringBuilder.append(" AND RIV.organizationType = 4");
        }
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
        stringBuilder.append("SELECT sum(RIV.exitt) - sum(RIV.adding) FROM OrganizationSiteStatementView RIV WHERE RIV.organizationSiteId = :organizationSiteId  ");

        List<BigDecimal> details = dao.findListByQuery(stringBuilder.toString(), params);
        return details.get(0);
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

    @Override
    public BigDecimal getAccountForOrganizationSiteNew(Integer organizationSiteId) {
        Map<String, Object> params = new HashMap<>();

        params.put("organizationSiteId", organizationSiteId);
//        params.put("type", supplierAccountSearchBean.getOrganizationType());

        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("SELECT sum(RIV.exitt) - sum(RIV.adding) FROM OrganizationSiteStatementView RIV WHERE RIV.organizationSiteId = :organizationSiteId  ");
        stringBuilder.append("SELECT (sum(s.exitt) - sum(s.adding)) as balance from (SELECT 0 AS `exitt`, IFNULL(NET(TRUNCATE(SUM(IFNULL(`net`, 0)), 2), ipi.`discount_type`, ipi.`discount`, `tax_flag`, 15,0,0,0),0) AS `adding`\n"
                + "	FROM `inv_purchase_invoice` `ipi` LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
                + "	WHERE ((`ipi`.`type` = 0) AND (`ipi`.`date` IS NOT NULL)) "
                + "     AND ipi.organization_site_id = " + organizationSiteId + "\n"
                + "    group by ipi.id\n"
                + "UNION ALL SELECT IFNULL(NET(TRUNCATE(SUM(IFNULL(`net`, 0)), 2), ipi.`discount_type`, ipi.`discount`, `tax_flag`,  15,0,0,0),0)  AS `exitt`, 0 AS `adding`\n"
                + "    FROM `inv_purchase_invoice` `ipi` LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
                + "    WHERE ((`ipi`.`type` = 1)  AND (`ipi`.`date` IS NOT NULL)) "
                + "     AND ipi.organization_site_id = " + organizationSiteId + "\n"
                + "    group by ipi.id\n"
                + "UNION ALL SELECT IFNULL(NET(TRUNCATE(SUM(IFNULL(`net`, 0)), 2), ipi.`discount_type`, ipi.`discount`, `tax_flag`,  15,0,0,0),0)  AS `exitt`, 0 AS `adding`\n"
                + "    FROM  `inv_purchase_invoice` `ipi` LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
                + "    WHERE ((`ipi`.`type` = 2)  AND (`ipi`.`date` IS NOT NULL)) "
                + "     AND ipi.organization_site_id = " + organizationSiteId + "\n"
                + "    group by ipi.id\n"
                + "UNION ALL SELECT 0 AS `exitt`, IFNULL(NET(TRUNCATE(SUM(IFNULL(`net`, 0)), 2), ipi.`discount_type`, ipi.`discount`, `tax_flag`,  15,0,0,0),0) AS `adding`\n"
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

    @Override
    public List<OrganizationSiteStatementView> getAllOrganizationSiteStatementSearchBeanNew(Integer organizationSiteId, Date dateFrom, Date dateTo) {

        String dateStrFrom = null;
        String dateStrTo = null;
            if (dateFrom != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateStrFrom = dateFormat.format(dateFrom);

            }
            if (dateTo != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateStrTo = dateFormat.format(dateTo);

            }
        

        StringBuilder query = new StringBuilder();

        query.append("select * from (SELECT 0 AS `serial`,\n"
                + "        `ios`.`open_balance_date` AS `date`,\n"
                + "        'رصيد افتتاحي لجهة التعامل' AS `ScreenName`,\n"
                + "        '' AS `gl_bank_name`,\n"
                + "         `ios`.`id` AS `id`,\n"
                + "        `ios`.`code` AS `code`,\n"
                + "        `ios`.`name` AS `name`,\n"
                + "        `ios`.`type` AS `type`,\n"
                + "        NULL AS `inst_contract_serial`,\n"
                + "         `ios`.`remark` AS `remarks`,\n"
                + "        NULL AS `supplier_invoice_number`,\n"
                + "        NULL AS `total_before_fracture`,\n"
                + "        NULL AS `fracture_number`,\n"
                + "        NULL AS `value_fracture`,\n"
                + "        NULL AS `look`,\n"
                + "        NULL AS `is_done`,\n"
                + "        IFNULL(`ios`.`open_balance_debit`, 0) AS `exitt`, IFNULL(`ios`.`open_balance_credit`, 0) AS `adding` ,`ios`.`id` AS `idNumber`\n"
                + "                    FROM  `inv_organization_site` `ios` WHERE  \n"
                + "                    `ios`.id =   \n"+ organizationSiteId + "\n");
                if (dateStrFrom != null) {
                    query.append(" AND IF ( '" ).append(dateStrFrom).append(  "' IS NOT NULL , `ios`.open_balance_date >= '" ).append(dateStrFrom).append(  "' ,true)");
                }
                if (dateStrTo != null) {
                    query.append(" AND IF ( '" ).append(dateStrTo).append(  "' IS NOT NULL , `ios`.open_balance_date <= '" ).append(dateStrTo).append(  "' ,true)");
                }
                query.append( "            UNION ALL     SELECT `ic`.`serial` AS `serial`,\n"
                + "        `ic`.`date` AS `date`,\n"
                + "        'عقد تقسيط' AS `ScreenName`,\n"
                + "        `gb`.`name` AS `gl_bank_name`,\n"
                + "        `ios`.`id` AS `id`,\n"
                + "        `ios`.`code` AS `code`,\n"
                + "        `ios`.`name` AS `name`,\n"
                + "        `ios`.`type` AS `type`,\n"
                + "        `ic`.`serial` AS `inst_contract_serial`, \n"
                + "        `ic`.`remark` AS `remarks`,\n"
                + "        NULL AS `supplier_invoice_number`,\n"
                + "        NULL AS `total_before_fracture`,\n"
                + "        NULL AS `fracture_number`,\n"
                + "        NULL AS `value_fracture`,\n"
                + "        NULL AS `look`,\n"
                + "        NULL AS `is_done`,\n"
                + "        IFNULL(`ic`.`total_value`, 0) AS `exitt`,\n"
                + "        0 AS `adding`  , `ios`.id AS idNumber \n" 
                + "       FROM ((`inst_contract` `ic`\n"
                + "        LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `ic`.`organization_site_id`)))\n"
                + "        LEFT JOIN `gl_bank` `gb` ON ((`gb`.`id` = `ic`.`bank_id`)))\n"
                + "         WHERE  `ic`.`organization_site_id` =   \n"+ organizationSiteId + "\n");
                if (dateStrFrom != null) {
                    query.append(" AND IF ( '" ).append(dateStrFrom).append(  "' IS NOT NULL , `ic`.`date` >= '" ).append(dateStrFrom).append(  "' ,true)");
                }
                if (dateStrTo != null) {
                    query.append(" AND IF ( '" ).append(dateStrTo).append(  "' IS NOT NULL , `ic`.`date` <= '" ).append(dateStrTo).append(  "' ,true)");
                }
                query.append( "                UNION ALL\n"
                + "SELECT  `ic`.`serial` AS `serial`,\n"
                + "        `ic`.`date` AS `date`,\n"
                + "        'دفعة مقدمة من عقد تقسيط' AS `ScreenName`,\n"
                + "        `gb`.`name` AS `gl_bank_name`,\n"
                + "        `ios`.`id` AS `id`,\n"
                + "        `ios`.`code` AS `code`,\n"
                + "        `ios`.`name` AS `name`,\n"
                + "        `ios`.`type` AS `type`,\n"
                + "        `ic`.`serial` AS `inst_contract_serial`, \n"
                + "        `ic`.`remark` AS `remarks`,\n"
                + "        NULL AS `supplier_invoice_number`,\n"
                + "        NULL AS `total_before_fracture`,\n"
                + "        NULL AS `fracture_number`,\n"
                + "        NULL AS `value_fracture`,\n"
                + "        NULL AS `look`,\n"
                + "        NULL AS `is_done`,\n"
                + "        0 AS `exitt`,\n"
                + "        IFNULL(`ic`.`advance_payment`, 0) AS `adding`  , ic.id AS idNumber \n"
                + "       FROM ((`inst_contract` `ic`\n"
                + "        LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `ic`.`organization_site_id`)))\n"
                + "        LEFT JOIN `gl_bank` `gb` ON ((`gb`.`id` = `ic`.`bank_id`)))\n"
                + "         WHERE  `ic`.`organization_site_id` =   \n"+ organizationSiteId + "\n");
                if (dateStrFrom != null) {
                    query.append(" AND IF ( '" ).append(dateStrFrom).append(  "' IS NOT NULL , `ic`.`date` >= '" ).append(dateStrFrom).append(  "' ,true)");
                }
                if (dateStrTo != null) {
                    query.append(" AND IF ( '" ).append(dateStrTo).append(  "' IS NOT NULL , `ic`.`date` <= '" ).append(dateStrTo).append(  "' ,true)");
                }
                query.append(  "                UNION ALL\n"
                + "SELECT `rer`.`serial` AS `serial`,\n"
                + "		`rer`.`date` AS `date`,\n"
                + "        'مستخلص مورد' AS `ScreenName`,\n"
                + "        '' AS `gl_bank_name`,\n"
                + "        `ios`.`id` AS `organization_site_id`,\n"
                + "        `ios`.`code` AS `organization_code`,\n"
                + "        `ios`.`name` AS `organization_name`,\n"
                + "        `ios`.`type` AS `organization_type`,\n"
                + "        NULL AS `inst_contract_serial`, \n"
                + "        `rer`.`remarks` AS `remarks`,\n"
                + "        NULL AS `supplier_invoice_number`,\n"
                + "        NULL AS `total_before_fracture`,\n"
                + "        NULL AS `fracture_number`,\n"
                + "        NULL AS `value_fracture`,\n"
                + "        NULL AS `look`,\n"
                + "        NULL AS `is_done`,\n"
                + "        0 AS `exitt`, \n"
                + "        IFNULL(`rer`.`total_value`, 0) AS `adding`  , rer.id AS idNumber \n"
                + "                	FROM (`rsc_extract_register` `rer`\n"
                + "        LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `rer`.`site_id`))) WHERE\n"
                + "        ((`rer`.`serial` IS NOT NULL)\n"
                + "            AND (`rer`.`site_id` IS NOT NULL)) AND `rer`.`site_id` =   \n"+ organizationSiteId + "\n");
                if (dateStrFrom != null) {
                    query.append(" AND IF ( '" ).append(dateStrFrom).append(  "' IS NOT NULL , `rer`.`date` >= '" ).append(dateStrFrom).append(  "' ,true)");
                }
                if (dateStrTo != null) {
                    query.append(" AND IF ( '" ).append(dateStrTo).append(  "' IS NOT NULL , `rer`.`date` <= '" ).append(dateStrTo).append(  "' ,true)");
                }
                query.append(  "                UNION ALL\n"
                + "SELECT `rer`.`serial` AS `serial`,\n"
                + "		`rer`.`date` AS `date`,\n"
                + "        'مستخلص عميل' AS `ScreenName`,\n"
                + "        '' AS `gl_bank_name`,\n"
                + "        `ios`.`id` AS `organization_site_id`,\n"
                + "        `ios`.`code` AS `organization_code`,\n"
                + "        `ios`.`name` AS `organization_name`,\n"
                + "        `ios`.`type` AS `organization_type`,\n"
                + "        NULL AS `inst_contract_serial`, \n"
                + "        `rer`.`remarks` AS `remarks`,\n"
                + "        NULL AS `supplier_invoice_number`,\n"
                + "        NULL AS `total_before_fracture`,\n"
                + "        NULL AS `fracture_number`,\n"
                + "        NULL AS `value_fracture`,\n"
                + "        NULL AS `look`,\n"
                + "        NULL AS `is_done`,\n"
                + "        IFNULL(`rer`.`total_value`, 0) AS `exitt`,\n"
                + "        0 AS `adding`  , rer.id AS idNumber \n"
                + "                	FROM (`rsc_extract_register` `rer`\n"
                + "        LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `rer`.`customer_id`)))\n"
                + "         WHERE\n"
                + "        ((`rer`.`serial` IS NOT NULL)\n"
                + "            AND (`rer`.`customer_id` IS NOT NULL)) AND `rer`.`customer_id` =   \n"+ organizationSiteId + "\n");
                if (dateStrFrom != null) {
                    query.append(" AND IF ( '" ).append(dateStrFrom).append(  "' IS NOT NULL , `rer`.`date` >= '" ).append(dateStrFrom).append(  "' ,true)");
                }
                if (dateStrTo != null) {
                    query.append(" AND IF ( '" ).append(dateStrTo).append(  "' IS NOT NULL , `rer`.`date` <= '" ).append(dateStrTo).append(  "' ,true)");
                }
                query.append(  "                UNION ALL\n"
                + "SELECT `ipi`.`serial` AS `serial`,\n"
                + "        `ipi`.`date` AS `date`,\n"
                + "        'فاتورة اشتراك' AS `ScreenName`,\n"
                + "        '' AS `gl_bank_name`,\n"
                + "        `ios`.`id` AS `organization_site_id`,\n"
                + "        `ios`.`code` AS `organization_code`,\n"
                + "        `ios`.`name` AS `organization_name`,\n"
                + "        `ios`.`type` AS `organization_type`,\n"
                + "        NULL AS `inst_contract_serial`, \n"
                + "        `ipi`.`remarks` AS `remarks`,\n"
                + "        null AS `supplier_invoice_number`,\n"
                + "        null AS `total_before_fracture`,\n"
                + "        null AS `fracture_number`,\n"
                + "        null AS `value_fracture`,\n"
                + "        null AS `look`,\n"
                + "        null AS `is_done`,\n"
                + "        0 AS `exitt`, "
                + " ipi.finalNet AS `adding`  , ipi.id AS idNumber \n"
                + "                	FROM `gm_subscriptions` `ipi` LEFT JOIN `gm_subscriptions_detail` `ipid` ON `ipi`.`id` = `ipid`.`gm_subscriptions_id`\n"
                + "                    LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `ipi`.`organization_site_id`))\n"
                + "                	WHERE ((`ipi`.`type` = 0) AND (`ipi`.`date` IS NOT NULL)) \n"
                + "                     AND ipi.organization_site_id =   \n"+ organizationSiteId + "\n");
                if (dateStrFrom != null) {
                    query.append(" AND IF ( '" ).append(dateStrFrom).append(  "' IS NOT NULL , `ipi`.`date` >= '" ).append(dateStrFrom).append(  "' ,true)");
                }
                if (dateStrTo != null) {
                    query.append(" AND IF ( '" ).append(dateStrTo).append(  "' IS NOT NULL , `ipi`.`date` <= '" ).append(dateStrTo).append(  "' ,true)");
                }
                query.append(  "                    group by ipi.id\n"
                + "                    \n");
                query.append(  "                UNION ALL\n"
                + "SELECT `ipi`.`serial` AS `serial`,\n"
                + "        `ipi`.`date` AS `date`,\n"
                + "        'فاتورة مشتريات' AS `ScreenName`,\n"
                + "        '' AS `gl_bank_name`,\n"
                + "        `ios`.`id` AS `organization_site_id`,\n"
                + "        `ios`.`code` AS `organization_code`,\n"
                + "        `ios`.`name` AS `organization_name`,\n"
                + "        `ios`.`type` AS `organization_type`,\n"
                + "        NULL AS `inst_contract_serial`, \n"
                + "        `ipi`.`remarks` AS `remarks`,\n"
                + "        ipi.supplier_invoice_number AS `supplier_invoice_number`,\n"
                + "        SUM(IFNULL(`ipid`.`total_before_fracture`, 0))  AS `total_before_fracture`,\n"
                + "        SUM(IFNULL(`ipid`.`fracture_number`, 0)) AS `fracture_number`,\n"
                + "        SUM(IFNULL(`ipid`.`value_fracture`, 0))  AS `value_fracture`,\n"
                + "        `ipi`.`look` AS `look`,\n"
                + "        `ipi`.`is_done` AS `is_done`,\n"
                + "        0 AS `exitt`, IFNULL(NET(TRUNCATE(SUM(IFNULL(`net`, 0)), 2), ipi.`discount_type`, ipi.`discount`, `tax_flag`, 15,0,0,0),0) AS `adding`  , ipi.id AS idNumber \n"
                + "                	FROM `inv_purchase_invoice` `ipi` LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
                + "                    LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `ipi`.`organization_site_id`))\n"
                + "                	WHERE ((`ipi`.`type` = 0) AND (`ipi`.`payment_type` = 1) AND (`ipi`.`date` IS NOT NULL)) \n"
                + "                     AND ipi.organization_site_id =   \n"+ organizationSiteId + "\n");
                if (dateStrFrom != null) {
                    query.append(" AND IF ( '" ).append(dateStrFrom).append(  "' IS NOT NULL , `ipi`.`date` >= '" ).append(dateStrFrom).append(  "' ,true)");
                }
                if (dateStrTo != null) {
                    query.append(" AND IF ( '" ).append(dateStrTo).append(  "' IS NOT NULL , `ipi`.`date` <= '" ).append(dateStrTo).append(  "' ,true)");
                }
                query.append(  "                    group by ipi.id\n"
                + "                    \n"
                + "                UNION ALL SELECT `ipi`.`serial` AS `serial`,\n"
                + "        `ipi`.`date` AS `date`,\n"
                + "        'فاتورة مبيعات' AS `ScreenName`,\n"
                + "        '' AS `gl_bank_name`,\n"
                + "        `ios`.`id` AS `organization_site_id`,\n"
                + "        `ios`.`code` AS `organization_code`,\n"
                + "        `ios`.`name` AS `organization_name`,\n"
                + "        `ios`.`type` AS `organization_type`,\n"
                + "        NULL AS `inst_contract_serial`, \n"
                + "        `ipi`.`remarks` AS `remarks`,\n"
                + "        ipi.supplier_invoice_number AS `supplier_invoice_number`,\n"
                + "        SUM(IFNULL(`ipid`.`total_before_fracture`, 0))  AS `total_before_fracture`,\n"
                + "        SUM(IFNULL(`ipid`.`fracture_number`, 0)) AS `fracture_number`,\n"
                + "        SUM(IFNULL(`ipid`.`value_fracture`, 0))  AS `value_fracture`,\n"
                + "        `ipi`.`look` AS `look`,\n"
                + "        `ipi`.`is_done` AS `is_done`,\n"
                + "        IFNULL(NET(TRUNCATE(SUM(IFNULL(`net`, 0)), 2), ipi.`discount_type`, ipi.`discount`, `tax_flag`,  15,0,0,0),0)  AS `exitt`, 0 AS `adding`  , ipi.id AS idNumber \n"
                + "                    FROM `inv_purchase_invoice` `ipi` LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
                + "                    LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `ipi`.`organization_site_id`))\n"
                + "                    WHERE ((`ipi`.`type` = 1) AND (`ipi`.`payment_type` = 1)  AND (`ipi`.`date` IS NOT NULL)) \n"
                + "                     AND ipi.organization_site_id =   \n"+ organizationSiteId + "\n");
                if (dateStrFrom != null) {
                    query.append(" AND IF ( '" ).append(dateStrFrom).append(  "' IS NOT NULL , `ipi`.`date` >= '" ).append(dateStrFrom).append(  "' ,true)");
                }
                if (dateStrTo != null) {
                    query.append(" AND IF ( '" ).append(dateStrTo).append(  "' IS NOT NULL , `ipi`.`date` <= '" ).append(dateStrTo).append(  "' ,true)");
                }
                query.append(  "                    group by ipi.id\n"
                + "                UNION ALL SELECT `ipi`.`serial` AS `serial`,\n"
                + "        `ipi`.`date` AS `date`,\n"
                + "        'مردود مشتريات' AS `ScreenName`,\n"
                + "        '' AS `gl_bank_name`,\n"
                + "        `ios`.`id` AS `organization_site_id`,\n"
                + "        `ios`.`code` AS `organization_code`,\n"
                + "        `ios`.`name` AS `organization_name`,\n"
                + "        `ios`.`type` AS `organization_type`,\n"
                + "        NULL AS `inst_contract_serial`, \n"
                + "        `ipi`.`remarks` AS `remarks`,\n"
                + "        ipi.supplier_invoice_number AS `supplier_invoice_number`,\n"
                + "        SUM(IFNULL(`ipid`.`total_before_fracture`, 0))  AS `total_before_fracture`,\n"
                + "        SUM(IFNULL(`ipid`.`fracture_number`, 0)) AS `fracture_number`,\n"
                + "        SUM(IFNULL(`ipid`.`value_fracture`, 0))  AS `value_fracture`,\n"
                + "        `ipi`.`look` AS `look`,\n"
                + "        `ipi`.`is_done` AS `is_done`,\n"
                + "        IFNULL(NET(TRUNCATE(SUM(IFNULL(`net`, 0)), 2), ipi.`discount_type`, ipi.`discount`, `tax_flag`,  15,0,0,0),0)  AS `exitt`, 0 AS `adding`  , ipi.id AS idNumber \n"
                + "                    FROM  `inv_purchase_invoice` `ipi` LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
                + "                    LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `ipi`.`organization_site_id`))\n"
                + "                    WHERE ((`ipi`.`type` = 2)  AND (`ipi`.`payment_type` = 1)  AND (`ipi`.`date` IS NOT NULL)) \n"
                + "                     AND ipi.organization_site_id =   \n"+ organizationSiteId + "\n");
                if (dateStrFrom != null) {
                    query.append(" AND IF ( '" ).append(dateStrFrom).append(  "' IS NOT NULL , `ipi`.`date` >= '" ).append(dateStrFrom).append(  "' ,true)");
                }
                if (dateStrTo != null) {
                    query.append(" AND IF ( '" ).append(dateStrTo).append(  "' IS NOT NULL , `ipi`.`date` <= '" ).append(dateStrTo).append(  "' ,true)");
                }
                query.append(  "                    group by ipi.id\n"
                + "                UNION ALL SELECT `ipi`.`serial` AS `serial`,\n"
                + "        `ipi`.`date` AS `date`,\n"
                + "        'مردود مبيعات' AS `ScreenName`,\n"
                + "        '' AS `gl_bank_name`,\n"
                + "        `ios`.`id` AS `organization_site_id`,\n"
                + "        `ios`.`code` AS `organization_code`,\n"
                + "        `ios`.`name` AS `organization_name`,\n"
                + "        `ios`.`type` AS `organization_type`,\n"
                + "        NULL AS `inst_contract_serial`, \n"
                + "        `ipi`.`remarks` AS `remarks`,\n"
                + "        ipi.supplier_invoice_number AS `supplier_invoice_number`,\n"
                + "        SUM(IFNULL(`ipid`.`total_before_fracture`, 0))  AS `total_before_fracture`,\n"
                + "        SUM(IFNULL(`ipid`.`fracture_number`, 0)) AS `fracture_number`,\n"
                + "        SUM(IFNULL(`ipid`.`value_fracture`, 0))  AS `value_fracture`,\n"
                + "        `ipi`.`look` AS `look`,\n"
                + "        `ipi`.`is_done` AS `is_done`,\n"
                + "         0 AS `exitt`, IFNULL(NET(TRUNCATE(SUM(IFNULL(`net`, 0)), 2), ipi.`discount_type`, ipi.`discount`, `tax_flag`,  15,0,0,0),0) AS `adding`  , ipi.id AS idNumber \n"
                + "                    FROM `inv_purchase_invoice` `ipi` LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON `ipi`.`id` = `ipid`.`inv_purchase_invoice_id`\n"
                + "                    LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `ipi`.`organization_site_id`))\n"
                + "                    WHERE ((`ipi`.`type` = 3) AND (`ipi`.`payment_type` = 1) AND (`ipi`.`date` IS NOT NULL)) \n"
                + "                     AND ipi.organization_site_id =   \n"+ organizationSiteId + "\n");
                if (dateStrFrom != null) {
                    query.append(" AND IF ( '" ).append(dateStrFrom).append(  "' IS NOT NULL , `ipi`.`date` >= '" ).append(dateStrFrom).append(  "' ,true)");
                }
                if (dateStrTo != null) {
                    query.append(" AND IF ( '" ).append(dateStrTo).append(  "' IS NOT NULL , `ipi`.`date` <= '" ).append(dateStrTo).append(  "' ,true)");
                }
                query.append(  "                    group by ipi.id\n"
                + "                UNION ALL SELECT \n"
                + "               `gbt`.`serial` AS `serial`,\n"
                + "        `gbt`.`date` AS `date`,\n"
                + "        'قبض نقدي' AS `ScreenName`,\n"
                + "        `gb`.`name` AS `gl_bank_name`,\n"
                + "        `ios`.`id` AS `id`,\n"
                + "        `ios`.`code` AS `code`,\n"
                + "        `ios`.`name` AS `name`,\n"
                + "        `ios`.`type` AS `type`,\n"
                + "        `ic`.`serial` AS `inst_contract_serial`, \n"
                + "         `gbt`.`remark` AS `remarks`,\n"
                + "        NULL AS `supplier_invoice_number`,\n"
                + "        NULL AS `total_before_fracture`,\n"
                + "        NULL AS `fracture_number`,\n"
                + "        NULL AS `value_fracture`,\n"
                + "        `gbt`.`look` AS `look`,\n"
                + "        `gbt`.`is_done` AS `is_done`,\n"
                + "        0 AS `exitt`, IFNULL(`gbtd`.`value_local`, 0) AS `adding` , gbt.id AS idNumber \n"
                + "                    FROM  (((((`gl_bank_transaction` `gbt`\n"
                + "        LEFT JOIN `gl_bank_transaction_detail` `gbtd` ON ((`gbt`.`id` = `gbtd`.`gl_bank_transaction_id`)))\n"
                + "        LEFT JOIN `gl_bank` `gb` ON ((`gb`.`id` = `gbt`.`gl_bank_id`)))\n"
                + "        LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `gbtd`.`organization_site_id`)))\n"
                + "        LEFT JOIN `inst_contract_detail` `icd` ON ((`icd`.`id` = `gbt`.`installment_id`)))\n"
                + "        LEFT JOIN `inst_contract` `ic` ON ((`ic`.`id` = `icd`.`inst_contract_id`)))\n"
                + "                    WHERE ((`gbt`.`payment_type` = 0) \n"
                + "                     AND gbtd.organization_site_id =   \n"+ organizationSiteId + "\n");
                if (dateStrFrom != null) {
                    query.append(" AND IF ( '" ).append(dateStrFrom).append(  "' IS NOT NULL , `gbt`.`date` >= '" ).append(dateStrFrom).append(  "' ,true)");
                }
                if (dateStrTo != null) {
                    query.append(" AND IF ( '" ).append(dateStrTo).append(  "' IS NOT NULL , `gbt`.`date` <= '" ).append(dateStrTo).append(  "' ,true)");
                }
                query.append(  "                AND (`gbt`.`transaction_type` = 1)) \n"
                + "                    GROUP BY  `gbt`.`id`\n"
                + "                UNION ALL SELECT  `gbt`.`serial` AS `serial`,\n"
                + "        `gbt`.`date` AS `date`,\n"
                + "        'قبض شيك' AS `ScreenName`,\n"
                + "        `gb`.`name` AS `gl_bank_name`,\n"
                + "        `ios`.`id` AS `id`,\n"
                + "        `ios`.`code` AS `code`,\n"
                + "        `ios`.`name` AS `name`,\n"
                + "        `ios`.`type` AS `type`,\n"
                + "        `ic`.`serial` AS `inst_contract_serial`, \n"
                + "         `gbt`.`remark` AS `remarks`,\n"
                + "        NULL AS `supplier_invoice_number`,\n"
                + "        NULL AS `total_before_fracture`,\n"
                + "        NULL AS `fracture_number`,\n"
                + "        NULL AS `value_fracture`,\n"
                + "        `gbt`.`look` AS `look`,\n"
                + "        `gbt`.`is_done` AS `is_done`,\n"
                + "        0 AS `exitt`, IFNULL(`gbtd`.`value_local`, 0) AS `adding` , gbt.id AS idNumber \n"
                + "                	FROM (((((`gl_bank_transaction` `gbt`\n"
                + "        LEFT JOIN `gl_bank_transaction_detail` `gbtd` ON ((`gbt`.`id` = `gbtd`.`gl_bank_transaction_id`)))\n"
                + "        LEFT JOIN `gl_bank` `gb` ON ((`gb`.`id` = `gbt`.`gl_bank_id`)))\n"
                + "        LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `gbtd`.`organization_site_id`)))\n"
                + "        LEFT JOIN `inst_contract_detail` `icd` ON ((`icd`.`id` = `gbt`.`installment_id`)))\n"
                + "        LEFT JOIN `inst_contract` `ic` ON ((`ic`.`id` = `icd`.`inst_contract_id`)))\n"
                + "                    WHERE (`gbt`.`payment_type` = 1) \n"
                + "                     AND gbtd.organization_site_id =     \n"+ organizationSiteId + "\n");
                if (dateStrFrom != null) {
                    query.append(" AND IF ( '" ).append(dateStrFrom).append(  "' IS NOT NULL , `gbt`.`date` >= '" ).append(dateStrFrom).append(  "' ,true)");
                }
                if (dateStrTo != null) {
                    query.append(" AND IF ( '" ).append(dateStrTo).append(  "' IS NOT NULL , `gbt`.`date` <= '" ).append(dateStrTo).append(  "' ,true)");
                }
                query.append(  "                AND (`gbt`.`transaction_type` = 1)\n"
                + "                    GROUP BY  `gbt`.`id`\n"
                + "                UNION ALL SELECT `gbt`.`serial` AS `serial`,\n"
                + "        `gbt`.`date` AS `date`,\n"
                + "        'قبض شيك خطي' AS `ScreenName`,\n"
                + "        `gb`.`name` AS `gl_bank_name`,\n"
                + "        `ios`.`id` AS `id`,\n"
                + "        `ios`.`code` AS `code`,\n"
                + "        `ios`.`name` AS `name`,\n"
                + "        `ios`.`type` AS `type`,\n"
                + "        `ic`.`serial` AS `inst_contract_serial`, \n"
                + "         `gbt`.`remark` AS `remarks`,\n"
                + "        NULL AS `supplier_invoice_number`,\n"
                + "        NULL AS `total_before_fracture`,\n"
                + "        NULL AS `fracture_number`,\n"
                + "        NULL AS `value_fracture`,\n"
                + "        `gbt`.`look` AS `look`,\n"
                + "        `gbt`.`is_done` AS `is_done`, 0 AS `exitt`, IFNULL(`gbtd`.`value_local`, 0) AS `adding` , gbt.id AS idNumber \n"
                + "                    FROM (((((`gl_bank_transaction` `gbt`\n"
                + "        LEFT JOIN `gl_bank_transaction_detail` `gbtd` ON ((`gbt`.`id` = `gbtd`.`gl_bank_transaction_id`)))\n"
                + "        LEFT JOIN `gl_bank` `gb` ON ((`gb`.`id` = `gbt`.`gl_bank_id`)))\n"
                + "        LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `gbtd`.`organization_site_id`)))\n"
                + "        LEFT JOIN `inst_contract_detail` `icd` ON ((`icd`.`id` = `gbt`.`installment_id`)))\n"
                + "        LEFT JOIN `inst_contract` `ic` ON ((`ic`.`id` = `icd`.`inst_contract_id`)))\n"
                + "                    WHERE (`gbt`.`payment_type` = 2)  \n"
                + "                     AND gbtd.organization_site_id =     \n"+ organizationSiteId + "\n");
                if (dateStrFrom != null) {
                    query.append(" AND IF ( '" ).append(dateStrFrom).append(  "' IS NOT NULL , `gbt`.`date` >= '" ).append(dateStrFrom).append(  "' ,true)");
                }
                if (dateStrTo != null) {
                    query.append(" AND IF ( '" ).append(dateStrTo).append(  "' IS NOT NULL , `gbt`.`date` <= '" ).append(dateStrTo).append(  "' ,true)");
                }
                query.append(  "                AND (`gbt`.`transaction_type` = 1)\n"
                + "                    GROUP BY  `gbt`.`id`\n"
                + "                UNION ALL SELECT   `gbt`.`serial` AS `serial`,\n"
                + "        `gbt`.`date` AS `date`,\n"
                + "        'صرف نقدي' AS `ScreenName`,\n"
                + "        `gb`.`name` AS `gl_bank_name`,\n"
                + "        `ios`.`id` AS `id`,\n"
                + "        `ios`.`code` AS `code`,\n"
                + "        `ios`.`name` AS `name`,\n"
                + "        `ios`.`type` AS `type`,\n"
                + "        `ic`.`serial` AS `inst_contract_serial`, \n"
                + "         `gbt`.`remark` AS `remarks`,\n"
                + "        NULL AS `supplier_invoice_number`,\n"
                + "        NULL AS `total_before_fracture`,\n"
                + "        NULL AS `fracture_number`,\n"
                + "        NULL AS `value_fracture`,\n"
                + "        `gbt`.`look` AS `look`,\n"
                + "        `gbt`.`is_done` AS `is_done`,\n"
                + "        SUM(IFNULL(`gbtd`.`value_local`, 0)) AS `exitt`, 0 AS `adding` , gbt.id AS idNumber \n"
                + "                    FROM (((((`gl_bank_transaction` `gbt`\n"
                + "        LEFT JOIN `gl_bank_transaction_detail` `gbtd` ON ((`gbt`.`id` = `gbtd`.`gl_bank_transaction_id`)))\n"
                + "        LEFT JOIN `gl_bank` `gb` ON ((`gb`.`id` = `gbt`.`gl_bank_id`)))\n"
                + "        LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `gbtd`.`organization_site_id`)))\n"
                + "        LEFT JOIN `inst_contract_detail` `icd` ON ((`icd`.`id` = `gbt`.`installment_id`)))\n"
                + "        LEFT JOIN `inst_contract` `ic` ON ((`ic`.`id` = `icd`.`inst_contract_id`)))\n"
                + "                    WHERE (`gbt`.`payment_type` = 0)  \n"
                + "                     AND gbtd.organization_site_id =     \n"+ organizationSiteId + "\n");
                if (dateStrFrom != null) {
                    query.append(" AND IF ( '" ).append(dateStrFrom).append(  "' IS NOT NULL , `gbt`.`date` >= '" ).append(dateStrFrom).append(  "' ,true)");
                }
                if (dateStrTo != null) {
                    query.append(" AND IF ( '" ).append(dateStrTo).append(  "' IS NOT NULL , `gbt`.`date` <= '" ).append(dateStrTo).append(  "' ,true)");
                }
                query.append( "                AND (`gbt`.`transaction_type` = 0)\n"
                + "                    GROUP BY  `gbt`.`id`\n"
                + "                UNION ALL SELECT     `gbt`.`serial` AS `serial`,\n"
                + "        `gbt`.`date` AS `date`,\n"
                + "        'صرف شيك' AS `ScreenName`,\n"
                + "        `gb`.`name` AS `gl_bank_name`,\n"
                + "        `ios`.`id` AS `id`,\n"
                + "        `ios`.`code` AS `code`,\n"
                + "        `ios`.`name` AS `name`,\n"
                + "        `ios`.`type` AS `type`,\n"
                + "        `ic`.`serial` AS `inst_contract_serial`, \n"
                + "         `gbt`.`remark` AS `remarks`,\n"
                + "        NULL AS `supplier_invoice_number`,\n"
                + "        NULL AS `total_before_fracture`,\n"
                + "        NULL AS `fracture_number`,\n"
                + "        NULL AS `value_fracture`,\n"
                + "        `gbt`.`look` AS `look`,\n"
                + "        `gbt`.`is_done` AS `is_done`,\n"
                + "        SUM(IFNULL(`gbtd`.`value_local`, 0)) AS `exitt`, 0 AS `adding` , gbt.id AS idNumber \n"
                + "                    FROM (((((`gl_bank_transaction` `gbt`\n"
                + "        LEFT JOIN `gl_bank_transaction_detail` `gbtd` ON ((`gbt`.`id` = `gbtd`.`gl_bank_transaction_id`)))\n"
                + "        LEFT JOIN `gl_bank` `gb` ON ((`gb`.`id` = `gbt`.`gl_bank_id`)))\n"
                + "        LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `gbtd`.`organization_site_id`)))\n"
                + "        LEFT JOIN `inst_contract_detail` `icd` ON ((`icd`.`id` = `gbt`.`installment_id`)))\n"
                + "        LEFT JOIN `inst_contract` `ic` ON ((`ic`.`id` = `icd`.`inst_contract_id`)))\n"
                + "                    WHERE (`gbt`.`payment_type` = 1) \n"
                + "                     AND gbtd.organization_site_id =     \n"+ organizationSiteId + "\n");
                if (dateStrFrom != null) {
                    query.append(" AND IF ( '" ).append(dateStrFrom).append(  "' IS NOT NULL , `gbt`.`date` >= '" ).append(dateStrFrom).append(  "' ,true)");
                }
                if (dateStrTo != null) {
                    query.append(" AND IF ( '" ).append(dateStrTo).append(  "' IS NOT NULL , `gbt`.`date` <= '" ).append(dateStrTo).append(  "' ,true)");
                }
                query.append(  "                AND (`gbt`.`transaction_type` = 0)\n"
                + "                    GROUP BY  `gbt`.`id` \n"
                + "                UNION ALL SELECT  `gbt`.`serial` AS `serial`,\n"
                + "        `gbt`.`date` AS `date`,\n"
                + "        'صرف شيك خطي' AS `ScreenName`,\n"
                + "        `gb`.`name` AS `gl_bank_name`,\n"
                + "        `ios`.`id` AS `id`,\n"
                + "        `ios`.`code` AS `code`,\n"
                + "        `ios`.`name` AS `name`,\n"
                + "        `ios`.`type` AS `type`,\n"
                + "        `ic`.`serial` AS `inst_contract_serial`, \n"
                + "         `gbt`.`remark` AS `remarks`,\n"
                + "        NULL AS `supplier_invoice_number`,\n"
                + "        NULL AS `total_before_fracture`,\n"
                + "        NULL AS `fracture_number`,\n"
                + "        NULL AS `value_fracture`,\n"
                + "        `gbt`.`look` AS `look`,\n"
                + "        `gbt`.`is_done` AS `is_done`,\n"
                + "        SUM(IFNULL(`gbtd`.`value_local`, 0)) AS `exitt`,  0 AS `adding` , gbt.id AS idNumber  \n"
                + "                    FROM (((((`gl_bank_transaction` `gbt`\n"
                + "        LEFT JOIN `gl_bank_transaction_detail` `gbtd` ON ((`gbt`.`id` = `gbtd`.`gl_bank_transaction_id`)))\n"
                + "        LEFT JOIN `gl_bank` `gb` ON ((`gb`.`id` = `gbt`.`gl_bank_id`)))\n"
                + "        LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `gbtd`.`organization_site_id`)))\n"
                + "        LEFT JOIN `inst_contract_detail` `icd` ON ((`icd`.`id` = `gbt`.`installment_id`)))\n"
                + "        LEFT JOIN `inst_contract` `ic` ON ((`ic`.`id` = `icd`.`inst_contract_id`)))\n"
                + "                    WHERE  (`gbt`.`payment_type` = 2) \n"
                + "                     AND gbtd.organization_site_id =     \n"+ organizationSiteId + "\n");
                if (dateStrFrom != null) {
                    query.append(" AND IF ( '" ).append(dateStrFrom).append(  "' IS NOT NULL , `gbt`.`date` >= '" ).append(dateStrFrom).append(  "' ,true)");
                }
                if (dateStrTo != null) {
                    query.append(" AND IF ( '" ).append(dateStrTo).append(  "' IS NOT NULL , `gbt`.`date` <= '" ).append(dateStrTo).append(  "' ,true)");
                }
                query.append(  "                AND (`gbt`.`transaction_type` = 0)\n"
                + "                    GROUP BY `gbt`.`id`\n"
                + "                UNION ALL  SELECT `in`.`serial` AS `serial`,\n"
                + "        `in`.`date` AS `date`,\n"
                + "        'اشعار اضافة للعميل' AS `ScreenName`,\n"
                + "        '' AS `gl_bank_name`,\n"
                + "        `ios`.`id` AS `id`,\n"
                + "        `ios`.`code` AS `code`,\n"
                + "        `ios`.`name` AS `name`,\n"
                + "        `ios`.`type` AS `type`,\n"
                + "        NULL AS `inst_contract_serial`,\n"
                + "         `in`.`remark` AS `remarks`,\n"
                + "        NULL AS `supplier_invoice_number`,\n"
                + "        NULL AS `total_before_fracture`,\n"
                + "        NULL AS `fracture_number`,\n"
                + "        NULL AS `value_fracture`,\n"
                + "        NULL AS `look`,\n"
                + "        NULL AS `is_done`,\n"
                + "         IFNULL(`in`.`value`, 0) AS `exitt`,  0 AS `adding` , `in`.id AS idNumber \n"
                + "                    FROM (`inv_notice` `in`\n"
                + "        LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `in`.`organization_site_id`)))   WHERE (`in`.`type` = 0)  \n"
                + "                and `in`.organization_site_id =      \n"+ organizationSiteId + "\n");
                if (dateStrFrom != null) {
                    query.append(" AND IF ( '" ).append(dateStrFrom).append(  "' IS NOT NULL ,  `date` >= '" ).append(dateStrFrom).append(  "' ,true)");
                }
                if (dateStrTo != null) {
                    query.append(" AND IF ( '" ).append(dateStrTo).append(  "' IS NOT NULL ,  `date` <= '" ).append(dateStrTo).append(  "' ,true)");
                }
                query.append(  "                UNION ALL SELECT  `in`.`serial` AS `serial`,\n"
                + "        `in`.`date` AS `date`,\n"
                + "         'اشعار خصم عملاء' AS `ScreenName`,\n"
                + "        '' AS `gl_bank_name`,\n"
                + "        `ios`.`id` AS `id`,\n"
                + "        `ios`.`code` AS `code`,\n"
                + "        `ios`.`name` AS `name`,\n"
                + "        `ios`.`type` AS `type`,\n"
                + "        NULL AS `inst_contract_serial`,\n"
                + "         `in`.`remark` AS `remarks`,\n"
                + "        NULL AS `supplier_invoice_number`,\n"
                + "        NULL AS `total_before_fracture`,\n"
                + "        NULL AS `fracture_number`,\n"
                + "        NULL AS `value_fracture`,\n"
                + "        NULL AS `look`,\n"
                + "        NULL AS `is_done`,\n"
                + "        0 AS `exitt`, IFNULL(`in`.`value`, 0) AS `adding`, `in`.id AS idNumber\n"
                + "                    FROM (`inv_notice` `in`\n"
                + "        LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `in`.`organization_site_id`))) WHERE  (`in`.`type` = 1 or `in`.`type` = 4 )  \n"
                + "                and `in`.organization_site_id =      \n"+ organizationSiteId + "\n");
                if (dateStrFrom != null) {
                    query.append(" AND IF ( '" ).append(dateStrFrom).append(  "' IS NOT NULL ,  `date` >= '" ).append(dateStrFrom).append(  "' ,true)");
                }
                if (dateStrTo != null) {
                    query.append(" AND IF ( '" ).append(dateStrTo).append(  "' IS NOT NULL ,  `date` <= '" ).append(dateStrTo).append(  "' ,true)");
                }
                query.append(  "                UNION ALL SELECT  `in`.`serial` AS `serial`,\n"
                + "        `in`.`date` AS `date`,\n"
                + "       'اشعار اضافة موردين' AS `ScreenName`,\n"
                + "        '' AS `gl_bank_name`,\n"
                + "        `ios`.`id` AS `id`,\n"
                + "        `ios`.`code` AS `code`,\n"
                + "        `ios`.`name` AS `name`,\n"
                + "        `ios`.`type` AS `type`,\n"
                + "        NULL AS `inst_contract_serial`,\n"
                + "         `in`.`remark` AS `remarks`,\n"
                + "        NULL AS `supplier_invoice_number`,\n"
                + "        NULL AS `total_before_fracture`,\n"
                + "        NULL AS `fracture_number`,\n"
                + "        NULL AS `value_fracture`,\n"
                + "        NULL AS `look`,\n"
                + "        NULL AS `is_done`,\n"
                + "        0 AS `exitt`,IFNULL(`in`.`value`, 0) AS `adding`, `in`.id AS idNumber\n"
                + "                    FROM (`inv_notice` `in`\n"
                + "        LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `in`.`organization_site_id`))) WHERE  (`in`.`type` = 2) \n"
                + "                and `in`.organization_site_id =   \n"+ organizationSiteId + "\n");
                if (dateStrFrom != null) {
                    query.append(" AND IF ( '" ).append(dateStrFrom).append(  "' IS NOT NULL ,  `date` >= '" ).append(dateStrFrom).append(  "' ,true)");
                }
                if (dateStrTo != null) {
                    query.append(" AND IF ( '" ).append(dateStrTo).append(  "' IS NOT NULL ,  `date` <= '" ).append(dateStrTo).append(  "' ,true)");
                }
                query.append(  "                UNION ALL SELECT  `in`.`serial` AS `serial`,\n"
                + "        `in`.`date` AS `date`,\n"
                + "        'اشعار خصم موردين' AS `ScreenName`,\n"
                + "        '' AS `gl_bank_name`,\n"
                + "        `ios`.`id` AS `id`,\n"
                + "        `ios`.`code` AS `code`,\n"
                + "        `ios`.`name` AS `name`,\n"
                + "        `ios`.`type` AS `type`,\n"
                + "        NULL AS `inst_contract_serial`,\n"
                + "         `in`.`remark` AS `remarks`,\n"
                + "        NULL AS `supplier_invoice_number`,\n"
                + "        NULL AS `total_before_fracture`,\n"
                + "        NULL AS `fracture_number`,\n"
                + "        NULL AS `value_fracture`,\n"
                + "        NULL AS `look`,\n"
                + "        NULL AS `is_done`,\n"
                + "        IFNULL(`in`.`value`, 0) AS `exitt`,  0 AS `adding`, `in`.id AS idNumber\n"
                + "                    FROM  (`inv_notice` `in`\n"
                + "        LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `in`.`organization_site_id`)))   WHERE   (`in`.`type` = 3) \n"
                + "                and `in`.organization_site_id =      \n"+ organizationSiteId + "\n");
                if (dateStrFrom != null) {
                    query.append(" AND IF ( '" ).append(dateStrFrom).append(  "' IS NOT NULL , `date` >= '" ).append(dateStrFrom).append(  "' ,true)");
                }
                if (dateStrTo != null) {
                    query.append(" AND IF ( '" ).append(dateStrTo).append(  "' IS NOT NULL ,  `date` <= '" ).append(dateStrTo).append(  "' ,true)");
                }
                query.append( ")  as s order by date");

        List<Object[]> res = dao.executeNativeQuery(query.toString());
        return findAllOrganizationSiteStatementSearchBeanNew(res);
    }
    
    public  List<OrganizationSiteStatementView> findAllOrganizationSiteStatementSearchBeanNew(List<Object[]> res) {

        List<OrganizationSiteStatementView> organizationSiteStatementViewList = new ArrayList<>();

        Iterator it = res.iterator();
        while (it.hasNext()) {
            Object[] line = (Object[]) it.next();
            OrganizationSiteStatementView view = new OrganizationSiteStatementView();
            view.setSerial( line[0] != null ? ((Long) line[0]).intValue() : null);
            view.setDate(line[1] != null ?(Date) line[1] : null);
            view.setScreenName(line[2] != null ?(String) line[2] : null);
            view.setGlBankName(line[3] != null ?(String) line[3]: null);
            view.setOrganizationSiteId(line[4] != null ?(Integer) line[4]: null);
            view.setOrganizationCode(line[5] != null ?(String) line[5]: null);
            view.setOrganizationName(line[6] != null ?(String) line[6]: null);
            view.setOrganizationType(line[7] != null ?(Integer) line[7]: null);
            view.setInstContractSerial(line[8] != null ?(Integer) line[8]: null);
            view.setRemarks(line[9] != null ?(String) line[9]: null);
            view.setSupplierInvoiceNumber(line[10] != null ?(String) line[10]: null);
            view.setTotalBeforeFracture(line[11] != null ?(BigDecimal) line[11]: null);
            view.setFractureNumber(line[12] != null ?(BigDecimal) line[12]: null);
            view.setValueFracture(line[13] != null ?(BigDecimal) line[13]: null);
            view.setLook(line[14] != null ?(Integer) line[14]: null);
            view.setIsDone(line[15] != null ?(Integer) line[15]: null);
            view.setExitt(line[16] != null ?(BigDecimal) line[16]: null);
            view.setAdding(line[17] != null ?(BigDecimal) line[17]: null);
            view.setIdNumber(line[18] != null ?(Integer) line[18]: null);
                    
            organizationSiteStatementViewList.add(view);
        }
        return organizationSiteStatementViewList;
    }

}
