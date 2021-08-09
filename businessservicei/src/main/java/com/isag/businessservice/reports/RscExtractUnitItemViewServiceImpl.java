/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.entityBean.RscExtractUnitItemAmountViewBean;
import com.isag.businessservice.reports.searchBean.RscExtractUnitItemAmountSearchBean;
import com.isag.core.GenericDAO;
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
 * @author ahmed
 */
@Stateless
public class RscExtractUnitItemViewServiceImpl implements RscExtractUnitItemAmountViewService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<RscExtractUnitItemAmountViewBean> findRscExtractUnitItemViewService(RscExtractUnitItemAmountSearchBean rscExtractUnitItemAmountSearchBean, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        List<RscExtractUnitItemAmountViewBean> list = new ArrayList<>();

        params.put("branchId", branchId);
        StringBuilder query = new StringBuilder();
//        query.append(" select  ipg.code , ipg.name , ios.code, ios.name ,rwug.code ,rwug.name  ,ios2.code,ios2.name , sum(rerd.net_cost),rwug.id ");
        query.append(" select  rwug.code ,rwug.name  , sum(rerd.net_cost),rwug.id ");
        query.append(" from rsc_extract_register_detail rerd left join rsc_extract_register rer on rer.id = rerd.rsc_extract_register ");
        query.append(" left join inst_project_guide ipg on ipg.id = rer.inst_projectGuide ");
        query.append(" left join rsc_work_units_guide rwug on rwug.id = rerd.work_unit_id ");
        query.append(" left join inv_organization_site ios on ios.id = rer.site_id ");
        query.append(" left join inv_organization_site ios2 on ios2.id = rer.customer_id ");

//        query.append(" select  e.rscExtractRegister.InstProjectGuide.code , e.rscExtractRegister.InstProjectGuide.name , ");
//        query.append(" e.rscExtractRegister.siteId.code , e.rscExtractRegister.siteId.name , ");
//        query.append(" e.workUnitId.code , e.workUnitId.name ,e.rscExtractRegister.customerId.code ,e.rscExtractRegister.customerId.name , sum(e.netCost) ");
//        query.append(" from rsc_extract_register_detail e ");
        query.append("where rer.branch_Id =  ").append(branchId);

        appendDateForQueryBuilder(query, params, rscExtractUnitItemAmountSearchBean);
        appendCustomerForQuery(query, params, rscExtractUnitItemAmountSearchBean);
        appendSupplierForQuery(query, params, rscExtractUnitItemAmountSearchBean);
        appendProjectForQuery(query, params, rscExtractUnitItemAmountSearchBean);
        appendItemForQuery(query, params, rscExtractUnitItemAmountSearchBean);

//        query.append(" group by ipg.code , ipg.name , ios.code, ios.name ,rwug.code ,rwug.name  ,ios2.code,ios2.name ");
        query.append(" group by rwug.code ,rwug.name,rwug.id  ");

//        query.append(" group by e.rscExtractRegister.InstProjectGuide.code , e.rscExtractRegister.InstProjectGuide.name ,  ");
//        query.append("e.rscExtractRegister.siteId.code , e.rscExtractRegister.siteId.name , e.workUnitId.code , e.workUnitId.name ,");
//        query.append("e.rscExtractRegister.customerId.code ,e.rscExtractRegister.customerId.name  ");
//        List<Object[]> res = dao.findListByQuery(query.toString(), params);
        List<Object[]> res = dao.executeNativeQuery(query.toString());
        // List<InstView> details = dao.findListByQuery(stringBuilder.toString(), params);
        list = getTheTotals(res, false);
        for (RscExtractUnitItemAmountViewBean rscExtractUnitItemAmountViewBean : list) {
            rscExtractUnitItemAmountViewBean.setExtractUnitItemAmountViewBeanList(findRscExtractUnitItemDetailViewService(rscExtractUnitItemAmountSearchBean, branchId, rscExtractUnitItemAmountViewBean.getItemId()));
        }
        return list;
    }

    @Override
    public List<RscExtractUnitItemAmountViewBean> findRscExtractUnitItemDetailViewService(RscExtractUnitItemAmountSearchBean rscExtractUnitItemAmountSearchBean, Integer branchId, Integer itemId) {
        Map<String, Object> params = new HashMap<>();
        List<RscExtractUnitItemAmountViewBean> list = new ArrayList<>();
        if (itemId != null) {
            params.put("branchId", branchId);
            StringBuilder query = new StringBuilder();
            query.append(" select  rer.date , ipg.name , ios.name ,rwug.code ,rwug.name  , rerd.net_cost, rerd.excute_quantity, rerd.price_sell ");
            query.append(" from rsc_extract_register_detail rerd left join rsc_extract_register rer on rer.id = rerd.rsc_extract_register ");
            query.append(" left join inst_project_guide ipg on ipg.id = rer.inst_projectGuide ");
            query.append(" left join rsc_work_units_guide rwug on rwug.id = rerd.work_unit_id ");
            query.append(" left join inv_organization_site ios on ios.id = rer.site_id ");
            query.append(" left join inv_organization_site ios2 on ios2.id = rer.customer_id ");

            query.append("where rer.branch_Id =  ").append(branchId);

            appendDateForQueryBuilder(query, params, rscExtractUnitItemAmountSearchBean);
            appendCustomerForQuery(query, params, rscExtractUnitItemAmountSearchBean);
            appendSupplierForQuery(query, params, rscExtractUnitItemAmountSearchBean);
            appendProjectForQuery(query, params, rscExtractUnitItemAmountSearchBean);
            query.append(" AND rwug.id = ").append(itemId).append(" ");
            List<Object[]> res = dao.executeNativeQuery(query.toString());
            list = getTheTotals(res, true);
        }
        return list;
    }

    public void appendDateForQueryBuilder(StringBuilder query, Map<String, Object> params, RscExtractUnitItemAmountSearchBean rscExtractUnitItemAmountSearchBean) {
        if (rscExtractUnitItemAmountSearchBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(rscExtractUnitItemAmountSearchBean.getDateFrom());
//            query.append(" and e.rscExtractRegister >= '").append(formatDateFrom).append("'");
            query.append(" and rer.date >= '").append(formatDateFrom).append("'");
        }

        if (rscExtractUnitItemAmountSearchBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(rscExtractUnitItemAmountSearchBean.getDateTo());
            query.append(" and rer.date <= '").append(formatDateTo).append("'");
        }
    }

    private void appendCustomerForQuery(StringBuilder stringBuilder, Map<String, Object> params, RscExtractUnitItemAmountSearchBean rscExtractUnitItemAmountSearchBean) {

        if (rscExtractUnitItemAmountSearchBean.getCustomerFrom() != null) {
            params.put("customerFrom", rscExtractUnitItemAmountSearchBean.getCustomerFrom().getCode());
//            stringBuilder.append(" AND e.rscExtractRegister.customerId.code >= :customerFrom");
            stringBuilder.append(" AND ios2.code >= '").append(rscExtractUnitItemAmountSearchBean.getCustomerFrom().getCode()).append("'");
        }

        if (rscExtractUnitItemAmountSearchBean.getCustomerTo() != null) {
            params.put("customerTo", rscExtractUnitItemAmountSearchBean.getCustomerTo().getCode());
//            stringBuilder.append(" AND e.rscExtractRegister.customerId.code <= :customerTo");
            stringBuilder.append(" AND ios2.code <= '").append(rscExtractUnitItemAmountSearchBean.getCustomerTo().getCode()).append("'");
        }

    }

    private void appendSupplierForQuery(StringBuilder stringBuilder, Map<String, Object> params, RscExtractUnitItemAmountSearchBean rscExtractUnitItemAmountSearchBean) {

        if (rscExtractUnitItemAmountSearchBean.getSupplierFrom() != null) {
            params.put("supplierFrom", rscExtractUnitItemAmountSearchBean.getSupplierFrom().getCode());
//            stringBuilder.append(" AND e.rscExtractRegister.siteId.code >= :customerFrom");
            stringBuilder.append(" AND ios.code >= '").append(rscExtractUnitItemAmountSearchBean.getSupplierFrom().getCode()).append("'");
        }

        if (rscExtractUnitItemAmountSearchBean.getSupplierTo() != null) {
            params.put("supplierTo", rscExtractUnitItemAmountSearchBean.getSupplierTo().getCode());
//            stringBuilder.append(" AND e.rscExtractRegister.siteId.code <= :customerTo");
            stringBuilder.append(" AND ios.code <= '").append(rscExtractUnitItemAmountSearchBean.getSupplierTo().getCode()).append("'");
        }

    }

    private void appendProjectForQuery(StringBuilder stringBuilder, Map<String, Object> params, RscExtractUnitItemAmountSearchBean rscExtractUnitItemAmountSearchBean) {

        if (rscExtractUnitItemAmountSearchBean.getInstProjectGuideFrom() != null) {
            params.put("customerFrom", rscExtractUnitItemAmountSearchBean.getInstProjectGuideFrom().getCode());
            stringBuilder.append(" AND ipg.code >= '").append(rscExtractUnitItemAmountSearchBean.getInstProjectGuideFrom().getCode()).append("'");
//            stringBuilder.append(" AND e.rscExtractRegister.InstProjectGuide.code >= :customerFrom");
        }

        if (rscExtractUnitItemAmountSearchBean.getInstProjectGuideTo() != null) {
            params.put("customerTo", rscExtractUnitItemAmountSearchBean.getInstProjectGuideTo().getCode());
            stringBuilder.append(" AND ipg.code <= '").append(rscExtractUnitItemAmountSearchBean.getInstProjectGuideTo().getCode()).append("'");
//            stringBuilder.append(" AND e.rscExtractRegister.InstProjectGuide.code <= :customerTo");
        }

    }

    private void appendItemForQuery(StringBuilder stringBuilder, Map<String, Object> params, RscExtractUnitItemAmountSearchBean rscExtractUnitItemAmountSearchBean) {

        if (rscExtractUnitItemAmountSearchBean.getRscWorkUnitsGuideFrom() != null) {
            params.put("workUnitrom", rscExtractUnitItemAmountSearchBean.getRscWorkUnitsGuideFrom().getCode());
//            stringBuilder.append(" AND e.workUnitId.code >= :workUnitrom");
            stringBuilder.append(" AND rwug.code >= '").append(rscExtractUnitItemAmountSearchBean.getRscWorkUnitsGuideFrom().getCode()).append("'");
        }

        if (rscExtractUnitItemAmountSearchBean.getRscWorkUnitsGuideTo() != null) {
            params.put("workUnitTo", rscExtractUnitItemAmountSearchBean.getRscWorkUnitsGuideTo().getCode());
//            stringBuilder.append(" AND e.workUnitId.code <= :workUnitTo");
            stringBuilder.append(" AND rwug.code <= '").append(rscExtractUnitItemAmountSearchBean.getRscWorkUnitsGuideTo().getCode()).append("'");
        }

    }

    public List<RscExtractUnitItemAmountViewBean> getTheTotals(List<Object[]> res, Boolean isDetail) {
        List<RscExtractUnitItemAmountViewBean> itemViewTotalList = new ArrayList<>();
        if (isDetail) {
            Iterator it = res.iterator();
            while (it.hasNext()) {
                Object[] line = (Object[]) it.next();
                RscExtractUnitItemAmountViewBean amountViewBean = new RscExtractUnitItemAmountViewBean();
                amountViewBean.setDate((Date) line[0]);
                amountViewBean.setProjectName((String) line[1]);
                amountViewBean.setSupplierName((String) line[2]);
                amountViewBean.setItemCode((String) line[3]);
                amountViewBean.setItemName((String) line[4]);
                amountViewBean.setNetCost((BigDecimal) line[5]);
                amountViewBean.setAmount((BigDecimal) line[6]);
                amountViewBean.setPrice((BigDecimal) line[7]);
                amountViewBean.setRowExpansion(Boolean.FALSE);
                amountViewBean.setMaster(Boolean.FALSE);
                itemViewTotalList.add(amountViewBean);
            }
        } else {

            Iterator it = res.iterator();
            while (it.hasNext()) {
                Object[] line = (Object[]) it.next();
                RscExtractUnitItemAmountViewBean amountViewBean = new RscExtractUnitItemAmountViewBean();
//                amountViewBean.setProjectCode((String) line[0]);
//                amountViewBean.setProjectName((String) line[1]);
//                amountViewBean.setSupplierCode((String) line[2]);
//                amountViewBean.setSupplierName((String) line[3]);
//                amountViewBean.setItemCode((String) line[4]);
//                amountViewBean.setItemName((String) line[5]);
//                amountViewBean.setCustomerCode((String) line[6]);
//                amountViewBean.setCustomerName((String) line[7]);
//                amountViewBean.setNetCost((BigDecimal) line[8]);
//                amountViewBean.setItemId((Integer) line[9]);

                amountViewBean.setItemCode((String) line[0]);
                amountViewBean.setItemName((String) line[1]);
                amountViewBean.setNetCost((BigDecimal) line[2]);
                amountViewBean.setItemId((Integer) line[3]);
                amountViewBean.setRowExpansion(Boolean.TRUE);
                amountViewBean.setMaster(Boolean.TRUE);
                itemViewTotalList.add(amountViewBean);
            }
        }
        return itemViewTotalList;
    }
}
