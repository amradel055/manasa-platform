/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.businessservice.reports.searchBean.InvItemSalesSearchBean;
import com.toby.businessservice.reports.searchBean.InvStripReportViewBean;
import com.toby.businessservice.reports.searchBean.ItemCardReportSerachBean;
import com.toby.core.GenericDAO;
import com.toby.entity.GlYear;
import com.toby.views.InvStripReportView;
import com.toby.views.ItemsDataView;
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
public class InventoryStripReportServiceImpl implements InventoryStripReportService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<ItemsDataView> getAllInvStripListByInvStripSearchBean(InvItemSalesSearchBean invStripSearchBean) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", invStripSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(""
                + "SELECT "
                + " sum(IDV.qtyout),"
                + " IDV.costAverage "
                + " FROM ItemsDataView IDV WHERE IDV.branchId = :branchId AND IDV.date is not null AND IDV.itemId is not null "
                + " AND IDV.inventoryId is not null AND IDV.groupId is not null"
                + " AND IDV.itemName is not null  AND IDV.itemCode is not null AND IDV.itemUnitName is not null "
                + " AND IDV.qtyin is not null  AND IDV.qtyout is not null AND IDV.costAverage is not null  ");

        stringBuilder.append(appendDate(invStripSearchBean, params));
        stringBuilder.append(appendInventory(invStripSearchBean, params));
        stringBuilder.append(appendGroup(invStripSearchBean, params));
        stringBuilder.append(appendSuplier(invStripSearchBean, params));

        stringBuilder.append(" group by IDV.costAverage");

        List<Object[]> arrayList = new ArrayList<>();

        arrayList = dao.findListByQuery(stringBuilder.toString(), params);

        stringBuilder = new StringBuilder();
        stringBuilder.append(""
                + "SELECT "
                + " sum(IDV.qtyin),"
                + " sum(IDV.qtyin) - sum(IDV.qtyout) , "
                + " sum((IDV.qtyin - IDV.qtyout) * IDV.costAverage), "
                + " IDV.itemId,"
                + " IDV.itemName,"
                + " IDV.itemCode,"
                + " IDV.itemUnitName, "
                + " IDV.groupId, "
                + " IDV.groupName, "
                + " IDV.inventoryId, "
                + " IDV.inventoryCode, "
                + " IDV.inventoryName"
                + " FROM ItemsDataView IDV WHERE IDV.branchId = :branchId AND IDV.date is not null AND IDV.itemId is not null "
                + " AND IDV.inventoryId is not null AND IDV.groupId is not null"
                + " AND IDV.itemName is not null  AND IDV.itemCode is not null AND IDV.itemUnitName is not null "
                + " AND IDV.qtyin is not null  AND IDV.qtyout is not null AND IDV.costAverage is not null  ");

        stringBuilder.append(appendDate(invStripSearchBean, params));
        stringBuilder.append(appendInventory(invStripSearchBean, params));
        stringBuilder.append(appendGroup(invStripSearchBean, params));
        stringBuilder.append(appendSuplier(invStripSearchBean, params));

        stringBuilder.append(" group by IDV.itemId, "
                + " IDV.itemName, "
                + " IDV.itemCode, "
                + " IDV.itemUnitName, "
                + " IDV.groupId, "
                + " IDV.groupName, "
                + " IDV.inventoryId,"
                + " IDV.inventoryCode,"
                + " IDV.inventoryName");

        List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);

        List<ItemsDataView> details = prepareList(res, arrayList);

        return details;
    }

    private List<ItemsDataView> prepareList(List<Object[]> res, List<Object[]> arrayList) {
        List<ItemsDataView> list = new ArrayList<>();
        ItemsDataView itemsDataView;
        for (Object[] object : res) {
            itemsDataView = new ItemsDataView();
            itemsDataView.setQtyin((BigDecimal) object[0]);
            itemsDataView.setBalance((BigDecimal) object[1]);
            itemsDataView.setBalanceValue((BigDecimal) object[2]);
            itemsDataView.setItemId((Integer) object[3]);
            itemsDataView.setItemName((String) object[4]);
            itemsDataView.setItemCode((String) object[5]);
            itemsDataView.setItemUnitName((String) object[6]);
            itemsDataView.setGroupId((Integer) object[7]);
            itemsDataView.setGroupName((String) object[8]);
            itemsDataView.setInventoryId((Integer) object[9]);
            itemsDataView.setInventoryCode((String) object[10]);
            itemsDataView.setInventoryName((String) object[11]);

            list.add(itemsDataView);
        }

        for (int i = 0; i < arrayList.size(); i++) {
            Object[] objs = arrayList.get(i);
            list.get(i).setQtyout((BigDecimal) objs[0]);
            list.get(i).setCostAverage((BigDecimal) objs[1]);
        }
        return list;
    }

    private String appendDate(InvItemSalesSearchBean invStripSearchBean, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();

        if (invStripSearchBean.getDateTo() != null) {
            params.put("dateTo", invStripSearchBean.getDateTo());
            appendQuery.append(" AND IDV.date <= :dateTo");
        }

        return appendQuery.toString();
    }

    private String appendInventory(InvItemSalesSearchBean invStripSearchBean, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();

        if (invStripSearchBean.getInventoryFrom() != null) {
            params.put("inventoryCodeFrom", invStripSearchBean.getInventoryFrom().getId());
            appendQuery.append(" AND IDV.inventoryId >= :inventoryCodeFrom");
        }

        if (invStripSearchBean.getInventoryTo() != null) {
            params.put("inventoryCodeTo", invStripSearchBean.getInventoryTo().getId());
            appendQuery.append(" AND IDV.inventoryId <= :inventoryCodeTo");
        }
        return appendQuery.toString();
    }

    private String appendGroup(InvItemSalesSearchBean invStripSearchBean, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();

//        if (invStripSearchBean.getGroupFrom() != null) {
//            params.put("groupCodeFrom", invStripSearchBean.getGroupFrom().getId());
//            appendQuery.append(" AND IDV.groupId >= :groupCodeFrom");
//        }
//
//        if (invStripSearchBean.getGroupTo() != null) {
//            params.put("groupCodeTo", invStripSearchBean.getGroupTo().getId());
//            appendQuery.append(" AND IDV.groupId <= :groupCodeTo");
//        }
        if (invStripSearchBean.getStringBuilder() != null && invStripSearchBean.getStringBuilder().length() > 0) {
            appendQuery.append(" AND IDV.groupId in( ").append(invStripSearchBean.getStringBuilder().toString()).append(" ) ");
        }
        return appendQuery.toString();
    }

    private String appendSuplier(InvItemSalesSearchBean invStripSearchBean, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();

        if (invStripSearchBean.getSuplierFrom() != null) {
            params.put("suplierCodeFrom", invStripSearchBean.getSuplierFrom().getId());
            appendQuery.append(" AND IDV.organizationSiteId >= :suplierCodeFrom");
        }

        if (invStripSearchBean.getSuplierTo() != null) {
            params.put("suplierCodeTo", invStripSearchBean.getSuplierTo().getId());
            appendQuery.append(" AND IDV.organizationSiteId <= :suplierCodeTo");
        }

        return appendQuery.toString();
    }
    @Override
    public List<ItemsDataView> getAllInvSupplierStripListByInvStripSearchBean(InvItemSalesSearchBean invStripSearchBean) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", invStripSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(""
                + "SELECT "
                + " sum(IDV.qtyout),"
                + " IDV.costAverage "
                + " FROM ItemsDataView IDV WHERE IDV.branchId = :branchId AND IDV.date is not null AND IDV.itemId is not null "
                + " AND IDV.inventoryId is not null AND IDV.groupId is not null"
                + " AND IDV.itemName is not null  AND IDV.itemCode is not null AND IDV.itemUnitName is not null "
                + " AND IDV.qtyin is not null  AND IDV.qtyout is not null AND IDV.costAverage is not null  ");

        stringBuilder.append(appendSupplierDate(invStripSearchBean, params));
        stringBuilder.append(appendSupplierInventory(invStripSearchBean, params));
        stringBuilder.append(appendSGroup(invStripSearchBean, params));
        stringBuilder.append(appendSSupplier(invStripSearchBean, params));

        stringBuilder.append(" group by IDV.costAverage");

        List<Object[]> arrayList = new ArrayList<>();

        arrayList = dao.findListByQuery(stringBuilder.toString(), params);

        stringBuilder = new StringBuilder();
        stringBuilder.append(""
                + "SELECT "
                + " sum(IDV.qtyin),"
                + " sum(IDV.qtyin) - sum(IDV.qtyout) , "
                + " sum((IDV.qtyin - IDV.qtyout) * IDV.costAverage), "
                + " IDV.itemId,"
                + " IDV.itemName,"
                + " IDV.itemCode,"
                + " IDV.itemUnitName, "
                + " IDV.groupId, "
                + " IDV.groupName, "
                + " IDV.inventoryId, "
                + " IDV.inventoryCode, "
                + " IDV.inventoryName"
                + " FROM ItemsDataView IDV WHERE IDV.branchId = :branchId AND IDV.date is not null AND IDV.itemId is not null "
                + " AND IDV.inventoryId is not null AND IDV.groupId is not null"
                + " AND IDV.itemName is not null  AND IDV.itemCode is not null AND IDV.itemUnitName is not null "
                + " AND IDV.qtyin is not null  AND IDV.qtyout is not null AND IDV.costAverage is not null  ");

        stringBuilder.append(appendSupplierDate(invStripSearchBean, params));
        stringBuilder.append(appendSupplierInventory(invStripSearchBean, params));
        stringBuilder.append(appendSGroup(invStripSearchBean, params));
        stringBuilder.append(appendSSupplier(invStripSearchBean, params));

        stringBuilder.append(" group by IDV.itemId, "
                + " IDV.itemName, "
                + " IDV.itemCode, "
                + " IDV.itemUnitName, "
                + " IDV.groupId, "
                + " IDV.groupName, "
                + " IDV.inventoryId,"
                + " IDV.inventoryCode,"
                + " IDV.inventoryName");

        List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);

        List<ItemsDataView> details = prepareSupplierList(res, arrayList);

        return details;
    }

    private List<ItemsDataView> prepareSupplierList(List<Object[]> res, List<Object[]> arrayList) {
        List<ItemsDataView> list = new ArrayList<>();
        ItemsDataView itemsDataView;
        for (Object[] object : res) {
            itemsDataView = new ItemsDataView();
            itemsDataView.setQtyin((BigDecimal) object[0]);
            itemsDataView.setBalance((BigDecimal) object[1]);
            itemsDataView.setBalanceValue((BigDecimal) object[2]);
            itemsDataView.setItemId((Integer) object[3]);
            itemsDataView.setItemName((String) object[4]);
            itemsDataView.setItemCode((String) object[5]);
            itemsDataView.setItemUnitName((String) object[6]);
            itemsDataView.setGroupId((Integer) object[7]);
            itemsDataView.setGroupName((String) object[8]);
            itemsDataView.setInventoryId((Integer) object[9]);
            itemsDataView.setInventoryCode((String) object[10]);
            itemsDataView.setInventoryName((String) object[11]);

            list.add(itemsDataView);
        }

        for (int i = 0; i < arrayList.size(); i++) {
            Object[] objs = arrayList.get(i);
            list.get(i).setQtyout((BigDecimal) objs[0]);
            list.get(i).setCostAverage((BigDecimal) objs[1]);
        }
        return list;
    }

    private String appendSupplierDate(InvItemSalesSearchBean invStripSearchBean, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();

        if (invStripSearchBean.getDateTo() != null) {
            params.put("dateTo", invStripSearchBean.getDateTo());
            appendQuery.append(" AND IDV.date <= :dateTo");
        }

        return appendQuery.toString();
    }

    private String appendSupplierInventory(InvItemSalesSearchBean invStripSearchBean, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();

        if (invStripSearchBean.getInventoryFrom() != null) {
            params.put("inventoryCodeFrom", invStripSearchBean.getInventoryFrom().getCode());
            appendQuery.append(" AND IDV.inventoryCode >= :inventoryCodeFrom");
        }

        if (invStripSearchBean.getInventoryTo() != null) {
            params.put("inventoryCodeTo", invStripSearchBean.getInventoryTo().getCode());
            appendQuery.append(" AND IDV.inventoryCode <= :inventoryCodeTo");
        }
        return appendQuery.toString();
    }

    private String appendSGroup(InvItemSalesSearchBean invStripSearchBean, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();

//        if (invStripSearchBean.getGroupFrom() != null) {
//            params.put("groupCodeFrom", invStripSearchBean.getGroupFrom().getId());
//            appendQuery.append(" AND IDV.groupId >= :groupCodeFrom");
//        }
//
//        if (invStripSearchBean.getGroupTo() != null) {
//            params.put("groupCodeTo", invStripSearchBean.getGroupTo().getId());
//            appendQuery.append(" AND IDV.groupId <= :groupCodeTo");
//        }
        if (invStripSearchBean.getStringBuilder() != null && invStripSearchBean.getStringBuilder().length() > 0) {
            appendQuery.append(" AND IDV.groupId in( ").append(invStripSearchBean.getStringBuilder().toString()).append(" ) ");
        }
        return appendQuery.toString();
    }

    private String appendSSupplier(InvItemSalesSearchBean invStripSearchBean, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();

        if (invStripSearchBean.getSalesPersonFrom() != null) {
            params.put("suplierCodeFrom", invStripSearchBean.getSuplierFrom().getCode());
            appendQuery.append(" AND IDV.itemSupplierId >= :suplierCodeFrom");
        }

        if (invStripSearchBean.getSalesPersonTo() != null) {
            params.put("suplierCodeTo", invStripSearchBean.getSuplierTo().getCode());
            appendQuery.append(" AND IDV.itemSupplierId <= :suplierCodeTo");
        }

        return appendQuery.toString();
    }

    @Override
    public List<InvStripReportViewBean> getAllInvStripByInvStripSearchBean(InvItemSalesSearchBean invStripSearchBean) {
        Map<String, Object> params = new HashMap<>();
        Map<Integer, InvStripReportView> map = new HashMap<>();

        List<InvStripReportView> details = new ArrayList<>();

        params.put("branchId", invStripSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT IDV From InvStripReportView IDV Where IDV.branchId = :branchId AND IDV.date is not null ");

        stringBuilder.append(appendDate(invStripSearchBean, params));
        stringBuilder.append(appendInventory(invStripSearchBean, params));
        stringBuilder.append(appendGroup(invStripSearchBean, params));
        stringBuilder.append(appendSuplier(invStripSearchBean, params));

        details = dao.findListByQuery(stringBuilder.toString(), params);

        for (InvStripReportView detail : details) {
            if (map.containsKey(detail.getItemId())) {
                InvStripReportView x = map.get(detail.getItemId());
                x.setBalance(x.getBalance().add(detail.getBalance()));
                x.setBalancevalue(x.getBalancevalue().add(detail.getBalancevalue()));
                x.setQtyin(x.getQtyin().add(detail.getQtyin()));
                x.setQtyout(x.getQtyout().add(detail.getQtyout()));
                map.put(detail.getItemId(), x);
            } else {
                map.put(detail.getItemId(), detail);
            }

        }

        List<InvStripReportViewBean> arrayList = new ArrayList<>();

        details = new ArrayList<>();
        InvStripReportView value;
        InvStripReportViewBean bean = new InvStripReportViewBean();
        for (Map.Entry<Integer, InvStripReportView> entrySet : map.entrySet()) {
            value = new InvStripReportView();
            value = entrySet.getValue();

            bean = new InvStripReportViewBean();
            bean.setBalance(value.getBalance());
            bean.setBalanceValue(value.getBalancevalue());
            bean.setCostAverage(value.getCostAverage());
            bean.setGroupName(value.getGroupName());
            bean.setInventoryName(value.getInventoryCode());
            bean.setItemCode(value.getItemCode());
            bean.setItemName(value.getItemName());
            bean.setQtyIn(value.getQtyin());
            bean.setQtyOut(value.getQtyout());
            bean.setUnitName(value.getItemUnitName());

            arrayList.add(bean);
//            details.add(entrySet.getValue());
        }

        return arrayList;
    }

    @Override
    public List<ItemsDataView> getAllItemsByItemCardReportSerachBean(ItemCardReportSerachBean itemCardReportSerachBean, GlYear glYear) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", itemCardReportSerachBean.getBranchId());

        StringBuilder stringBuilder = new StringBuilder();
        Map<Integer, BigDecimal> itemOpeningBalanceMap = new HashMap<>();
        Map<Integer, String> itemOpeningBalanceAfterAllDateMap = new HashMap<>();
        Map<Integer, BigDecimal> qtyInMap = new HashMap<>();
        Map<Integer, BigDecimal> qtyOutMap = new HashMap<>();
        if (itemCardReportSerachBean.getDateFrom() != null && itemCardReportSerachBean.getDateFrom().after(glYear.getDateFrom())) {
            stringBuilder.append("SELECT IDV.itemId, sum(COALESCE(IDV.qtyin * IDV.screwing,0))-sum(COALESCE(IDV.qtyout * IDV.screwing,0)),IDV.itemName"
                    + ",sum(COALESCE(IDV.qtyin * IDV.screwing,0)), sum(COALESCE(IDV.qtyout * IDV.screwing,0)) "
                    + " FROM ItemsDataView IDV WHERE IDV.branchId = :branchId "
                    + " AND IDV.date is not null AND IDV.itemId is not null "
                    + " AND IDV.inventoryId is not null AND IDV.groupId is not null"
                    + " AND IDV.itemName is not null AND IDV.itemCode is not null "
                    + " AND IDV.qtyin is not null  AND IDV.qtyout is not null ");

            stringBuilder.append(appendGlYear(itemCardReportSerachBean, glYear, params));
            stringBuilder.append(appendTillDate(itemCardReportSerachBean, glYear, params));
            stringBuilder.append(appendInventory(itemCardReportSerachBean, params));
            stringBuilder.append(appendInvItem(itemCardReportSerachBean, params));

            stringBuilder.append(" group by IDV.itemId ");
//        stringBuilder.append(" order by IDV.itemId ASC, IDV.date ASC");
            List<Object[]> list = dao.findListByQuery(stringBuilder.toString(), params);

            for (Object[] os : list) {
                itemOpeningBalanceMap.put((Integer) os[0], (BigDecimal) os[1]);
                itemOpeningBalanceAfterAllDateMap.put((Integer) os[0], (String) os[2]);
            }
        }
        //to add total qtyin and qtyout
        params = new HashMap<>();
        params.put("branchId", itemCardReportSerachBean.getBranchId());

        stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT IDV.itemId, sum(COALESCE(IDV.qtyin * IDV.screwing,0))-sum(COALESCE(IDV.qtyout * IDV.screwing,0)),IDV.itemName"
                + ",sum(COALESCE(IDV.qtyin * IDV.screwing,0)), sum(COALESCE(IDV.qtyout * IDV.screwing,0)) "
                + " FROM ItemsDataView IDV WHERE IDV.branchId = :branchId "
                + " AND IDV.date is not null AND IDV.itemId is not null "
                + " AND IDV.inventoryId is not null AND IDV.groupId is not null"
                + " AND IDV.itemName is not null AND IDV.itemCode is not null "
                + " AND IDV.qtyin is not null  AND IDV.qtyout is not null ");

        stringBuilder.append(appendGlYear(itemCardReportSerachBean, glYear, params));
        stringBuilder.append(appendDate(itemCardReportSerachBean, params));
        stringBuilder.append(appendInventory(itemCardReportSerachBean, params));
        stringBuilder.append(appendInvItem(itemCardReportSerachBean, params));

        stringBuilder.append(" group by IDV.itemId ");
//        stringBuilder.append(" order by IDV.itemId ASC, IDV.date ASC");
        List<Object[]> list2 = dao.findListByQuery(stringBuilder.toString(), params);
        for (Object[] os : list2) {
            qtyInMap.put((Integer) os[0], (BigDecimal) os[3]);
            qtyOutMap.put((Integer) os[0], (BigDecimal) os[4]);
        }

        stringBuilder = new StringBuilder();
        params = new HashMap<>();
        params.put("branchId", itemCardReportSerachBean.getBranchId());
        stringBuilder.append("SELECT IDV FROM ItemsDataView IDV WHERE IDV.branchId = :branchId "
                + " AND IDV.date is not null AND IDV.itemId is not null "
                + " AND IDV.inventoryId is not null AND IDV.groupId is not null"
                + " AND IDV.itemName is not null AND IDV.itemCode is not null "
                + " AND IDV.qtyin is not null  AND IDV.qtyout is not null ");

        stringBuilder.append(appendDate(itemCardReportSerachBean, params));
        stringBuilder.append(appendInventory(itemCardReportSerachBean, params));
        stringBuilder.append(appendInvItem(itemCardReportSerachBean, params));

        stringBuilder.append(" order by IDV.itemId ASC, IDV.date ASC");

        List<ItemsDataView> itemsDataViewList = dao.findListByQuery(stringBuilder.toString(), params);

        for (ItemsDataView dataView : itemsDataViewList) {
            dataView.setOpeningBalance(itemOpeningBalanceMap.get(dataView.getItemId()));
            //i will set in organizationopenbalance credit and debit fields and use them as total value for qtyin and qty out
            dataView.setTotalQtyIn(qtyInMap.get(dataView.getItemId()));
            dataView.setTotalQtyOut(qtyOutMap.get(dataView.getItemId()));
            if (itemOpeningBalanceAfterAllDateMap.containsKey(dataView.getItemId())) {
                itemOpeningBalanceAfterAllDateMap.remove(dataView.getItemId());
            }
        }
//if search after all dates of item and need to get the open balance for them
        for (Map.Entry<Integer, String> entry : itemOpeningBalanceAfterAllDateMap.entrySet()) {
            ItemsDataView idv = new ItemsDataView();
            idv.setItemName(entry.getValue());
            idv.setItemId(entry.getKey());
            idv.setOpeningBalance(itemOpeningBalanceMap.get(entry.getKey()));
            idv.setTotalQtyIn(qtyInMap.get(entry.getKey()));
            idv.setTotalQtyOut(qtyOutMap.get(entry.getKey()));
            itemsDataViewList.add(idv);
        }
        return itemsDataViewList;
    }

    private String appendGlYear(ItemCardReportSerachBean itemCardReportSerachBean, GlYear glYear, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();
        if (itemCardReportSerachBean.getDateFrom() != null) {
            params.put("dateFrom", glYear.getDateFrom());
            appendQuery.append(" AND IDV.date >= :dateFrom");
        }

        if (itemCardReportSerachBean.getDateTo() != null) {
            params.put("dateTo", itemCardReportSerachBean.getDateFrom());
            appendQuery.append(" AND IDV.date <= :dateTo");
        }

        return appendQuery.toString();
    }

    private String appendDate(ItemCardReportSerachBean itemCardReportSerachBean, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();
        if (itemCardReportSerachBean.getDateFrom() != null) {
            params.put("dateFrom", itemCardReportSerachBean.getDateFrom());
            appendQuery.append(" AND IDV.date >= :dateFrom");
        }

        if (itemCardReportSerachBean.getDateTo() != null) {
            params.put("dateTo", itemCardReportSerachBean.getDateTo());
            appendQuery.append(" AND IDV.date <= :dateTo");
        }

        return appendQuery.toString();
    }

    private String appendTillDate(ItemCardReportSerachBean itemCardReportSerachBean, GlYear glYear, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();
        if (itemCardReportSerachBean.getDateFrom() != null) {
            params.put("dateTillFrom", itemCardReportSerachBean.getDateFrom());
        }
        appendQuery.append(" AND IDV.date < :dateTillFrom");

        return appendQuery.toString();
    }

    private String appendInventory(ItemCardReportSerachBean itemCardReportSerachBean, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();
        if (itemCardReportSerachBean.getInventoryFrom() != null && itemCardReportSerachBean.getInventoryFrom().getCode() != null) {
            params.put("inventoryFrom", itemCardReportSerachBean.getInventoryFrom().getCode());
            appendQuery.append(" AND IDV.inventoryCode >= :inventoryFrom");
        }

        if (itemCardReportSerachBean.getInventoryTo() != null && itemCardReportSerachBean.getInventoryTo().getCode() != null) {
            params.put("inventoryTo", itemCardReportSerachBean.getInventoryTo().getCode());
            appendQuery.append(" AND IDV.inventoryCode <= :inventoryTo");
        }

        return appendQuery.toString();
    }

    private String appendInvItem(ItemCardReportSerachBean itemCardReportSerachBean, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();
        if (itemCardReportSerachBean.getInvItemForm() != null && itemCardReportSerachBean.getInvItemForm().getCode() != null) {
            params.put("itemCodeFrom", itemCardReportSerachBean.getInvItemForm().getCode());
            appendQuery.append(" AND IDV.itemCode >= :itemCodeFrom");
        }

        if (itemCardReportSerachBean.getInvItemForm() != null && itemCardReportSerachBean.getInvItemTo().getCode() != null) {
            params.put("itemCodeTo", itemCardReportSerachBean.getInvItemTo().getCode());
            appendQuery.append(" AND IDV.itemCode <= :itemCodeTo");
        }

        return appendQuery.toString();
    }
}
