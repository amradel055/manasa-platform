/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.InvItemSalesSearchBean;
import com.isag.businessservice.reports.searchBean.ItemCardReportSerachBean;
import com.isag.core.GenericDAO;
import com.isag.entity.GlYear;
import com.isag.views.ItemsDataView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author elsakr6
 */
@Stateless
public class ItemsDataViewServiceImpl implements ItemsDataViewService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<ItemsDataView> findItemsDataViewByBranchIdAndItemIdAndInventoryId(Integer branchId, Integer itemId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("itemId", itemId);
        return dao.findListByQuery("SELECT e FROM ItemsDataView e WHERE e.branchId = :branchId and e.itemId = :itemId and e.inventoryId != null", params);
    }
    
    public List<ItemsDataView> findItemsByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
       
        return dao.findListByQuery("SELECT i FROM ItemsDataView i WHERE i.branchId = :branchId  and i.inventoryId != null", params);
    }

    @Override
    public List<ItemsDataView> getAllItemCardByInvStripSearchBean(ItemCardReportSerachBean itemCardReportSerachBean, GlYear glYear) {
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

            stringBuilder.append(" group by IDV.itemId ,IDV.itemName");
//            stringBuilder.append(" order by IDV.itemId ,IDV.date ASC");
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

        stringBuilder.append(" group by IDV.itemId ,IDV.itemName");
        stringBuilder.append(" order by IDV.itemId ASC, IDV.date ASC");
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

        stringBuilder.append(" order by IDV.itemId , IDV.date ASC");

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
        
//        TreeMap<Date,ItemsDataView> treeMap = new TreeMap<>();
//        for(ItemsDataView dataView : itemsDataViewList){
//            treeMap.put(dataView.getDate(), dataView);
//        }
//        itemsDataViewList = new ArrayList<>();
//        itemsDataViewList = new ArrayList<ItemsDataView>(treeMap.values());
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

    @Override
    public List<ItemsDataView> getAllInvStripListByInvStripSearchBean(InvItemSalesSearchBean invStripSearchBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", invStripSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(""
                + "SELECT sum(IDV.qtyout),IDV.costAverage,"
                + " sum(IDV.qtyin),"
                + " sum(IDV.qtyin) - sum(IDV.qtyout) , "
                + " sum((IDV.qtyin - IDV.qtyout) * IDV.costAverage), "
                + " IDV.itemId,"
                + " IDV.itemName,"
                + " IDV.itemCode,"
                + " IDV.itemUnitName, "
                + " IDV.itemBarcode, "
                + " IDV.weight, "
                + " IDV.sellPrice, "
                + " IDV.groupId, "
                + " IDV.groupName, "
                + " IDV.inventoryId, "
                + " IDV.inventoryCode, "
                + " IDV.inventoryName, "
                + " IDV.color, "
                + " IDV.colorBarcode, "
                + " IDV.length, "
                + " IDV.lengthBarcode,"
                + " IDV.supplierInvoiceNumber,"
                + " IDV.cartonNumber,"
                + " IDV.cartonsCapacity,"
                + " IDV.totalBeforeFracture,"
                + " IDV.fractureNumber,"
                + " IDV.valueFracture, "
                + " IDV.sortName "
                + " FROM ItemsDataView IDV WHERE IDV.branchId = :branchId AND IDV.date is not null AND IDV.itemId is not null "
                + " AND IDV.inventoryId is not null AND IDV.groupId is not null"
                + " AND IDV.itemName is not null  AND IDV.itemCode is not null AND IDV.itemUnitName is not null "
                + " AND IDV.qtyin is not null  AND IDV.qtyout is not null AND IDV.costAverage is not null  ");

        stringBuilder.append(appendDateinv(invStripSearchBean, params));
        stringBuilder.append(appendInventoryinv(invStripSearchBean, params));
        stringBuilder.append(appendGroupinv(invStripSearchBean, params));
        stringBuilder.append(appendSuplierinv(invStripSearchBean, params));

        stringBuilder.append(" group by IDV.itemId, "
                + " IDV.itemName, "
                + " IDV.itemCode, "
                + " IDV.itemUnitName, "
                + " IDV.itemBarcode, "
                + " IDV.groupId, "
                + " IDV.groupName, "
                + " IDV.inventoryId,"
                + " IDV.inventoryCode,"
                + " IDV.weight,"    
                + " IDV.color,"    
                + " IDV.colorBarcode,"    
                + " IDV.length,"    
                + " IDV.lengthBarcode,"  
                + " IDV.inventoryName,IDV.costAverage");

        List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);

        List<ItemsDataView> details = prepareList(res);

        return details;
    }

    private List<ItemsDataView> prepareList(List<Object[]> res) {
        List<ItemsDataView> list = new ArrayList<>();
        ItemsDataView itemsDataView;
        for (Object[] object : res) {
            itemsDataView = new ItemsDataView();
            itemsDataView.setQtyout((BigDecimal) object[0]);
            itemsDataView.setCostAverage((BigDecimal) object[1]);
            itemsDataView.setQtyin((BigDecimal) object[2]);
            itemsDataView.setBalance((BigDecimal) object[3]);
            itemsDataView.setBalanceValue((BigDecimal) object[4]);
            itemsDataView.setItemId((Integer) object[5]);
            itemsDataView.setItemName((String) object[6]);
            itemsDataView.setItemCode((String) object[7]);
            itemsDataView.setItemUnitName((String) object[8]);
            itemsDataView.setItemBarcode((String) object[9]);
            itemsDataView.setWeight((BigDecimal) object[10]);
            itemsDataView.setSellPrice((BigDecimal) object[11]);
            itemsDataView.setGroupId((Integer) object[12]);
            itemsDataView.setGroupName((String) object[13]);
            itemsDataView.setInventoryId((Integer) object[14]);
            itemsDataView.setInventoryCode((String) object[15]);
            itemsDataView.setInventoryName((String) object[16]);
            itemsDataView.setColor((String) object[17]);
            itemsDataView.setColorBarcode((String) object[18]);
            itemsDataView.setLength((BigDecimal) object[19]);
            itemsDataView.setLengthBarcode((BigDecimal) object[20]);
            itemsDataView.setSupplierInvoiceNumber((String) object[21]);
            itemsDataView.setCartonNumber((BigDecimal) object[22]);
            itemsDataView.setCartonsCapacity((BigDecimal) object[23]);
            itemsDataView.setTotalBeforeFracture((BigDecimal) object[24]);
            itemsDataView.setFractureNumber((BigDecimal) object[25]);
            itemsDataView.setValueFracture((BigDecimal) object[26]);
            itemsDataView.setSortName((String) object[27]);

            list.add(itemsDataView);
        }
        return list;
    }

    private String appendDateinv(InvItemSalesSearchBean invStripSearchBean, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();

        if (invStripSearchBean.getDateTo() != null) {
            params.put("dateTo", invStripSearchBean.getDateTo());
            appendQuery.append(" AND IDV.date <= :dateTo");
        }

        return appendQuery.toString();
    }

    private String appendInventoryinv(InvItemSalesSearchBean invStripSearchBean, Map<String, Object> params) {
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

    private String appendGroupinv(InvItemSalesSearchBean invStripSearchBean, Map<String, Object> params) {
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

    private String appendSuplierinv(InvItemSalesSearchBean invStripSearchBean, Map<String, Object> params) {
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
}
