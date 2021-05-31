/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.InvItemSalesSearchBean;
import com.toby.businessservice.reports.searchBean.ItemCardReportSerachBean;
import com.toby.core.GenericDAO;
import com.toby.dto.InvGroupDTO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.ProGroupDTO;
import com.toby.dto.SymbolDTO;
import com.toby.entity.Branch;
import com.toby.entity.GlYear;
import com.toby.entity.InvGroup;
import com.toby.entity.InvItem;
import com.toby.entity.ProGroup;
import com.toby.entity.Symbol;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import com.toby.views.ItemsDataView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @EJB
    private InvBarcodeSevice invBarcodeSevice;

    @Override
    public List<ItemsDataView> findItemsDataViewByBranchIdAndItemIdAndInventoryId(Integer branchId, Integer itemId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("itemId", itemId);
        return dao.findListByQuery("SELECT e FROM ItemsDataView e WHERE e.branchId = :branchId and e.itemId = :itemId and e.inventoryId != null", params);
    }

//    @Override
//    public List<ItemsDataView> getAllItemCardByInvStripSearchBean(ItemCardReportSerachBean itemCardReportSerachBean, GlYear glYear) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("branchId", itemCardReportSerachBean.getBranchId());
//
//        StringBuilder stringBuilder = new StringBuilder();
//        Map<Integer, BigDecimal> itemOpeningBalanceMap = new HashMap<>();
//        Map<Integer, String> itemOpeningBalanceAfterAllDateMap = new HashMap<>();
//        Map<Integer, BigDecimal> qtyInMap = new HashMap<>();
//        Map<Integer, BigDecimal> qtyOutMap = new HashMap<>();
//        if (itemCardReportSerachBean.getDateFrom() != null && itemCardReportSerachBean.getDateFrom().after(glYear.getDateFrom())) {
//            stringBuilder.append("SELECT IDV.itemId, sum(COALESCE(IDV.qtyin * IDV.screwing,0))-sum(COALESCE(IDV.qtyout * IDV.screwing,0)),IDV.itemName"
//                    + ",sum(COALESCE(IDV.qtyin * IDV.screwing,0)), sum(COALESCE(IDV.qtyout * IDV.screwing,0)) "
//                    + " FROM ItemsDataView IDV WHERE IDV.branchId = :branchId "
//                    + " AND IDV.date is not null AND IDV.itemId is not null "
//                    + " AND IDV.inventoryId is not null AND IDV.groupId is not null"
//                    + " AND IDV.itemName is not null AND IDV.itemCode is not null "
//                    + " AND IDV.qtyin is not null  AND IDV.qtyout is not null ");
//
//            stringBuilder.append(appendGlYear(itemCardReportSerachBean, glYear, params));
//            stringBuilder.append(appendTillDate(itemCardReportSerachBean, glYear, params));
//            stringBuilder.append(appendInventory(itemCardReportSerachBean, params));
//            stringBuilder.append(appendInvItem(itemCardReportSerachBean, params));
//
//            stringBuilder.append(" group by IDV.itemId ,IDV.itemName");
////            stringBuilder.append(" order by IDV.itemId ,IDV.date ASC");
//            List<Object[]> list = dao.findListByQuery(stringBuilder.toString(), params);
//
//            for (Object[] os : list) {
//                itemOpeningBalanceMap.put((Integer) os[0], (BigDecimal) os[1]);
//                itemOpeningBalanceAfterAllDateMap.put((Integer) os[0], (String) os[2]);
//            }
//        }
//        //to add total qtyin and qtyout
//        params = new HashMap<>();
//        params.put("branchId", itemCardReportSerachBean.getBranchId());
//
//        stringBuilder = new StringBuilder();
//        stringBuilder.append("SELECT IDV.itemId, sum(COALESCE(IDV.qtyin * IDV.screwing,0))-sum(COALESCE(IDV.qtyout * IDV.screwing,0)),IDV.itemName"
//                + ",sum(COALESCE(IDV.qtyin * IDV.screwing,0)), sum(COALESCE(IDV.qtyout * IDV.screwing,0)) "
//                + " FROM ItemsDataView IDV WHERE IDV.branchId = :branchId "
//                + " AND IDV.date is not null AND IDV.itemId is not null "
//                + " AND IDV.inventoryId is not null AND IDV.groupId is not null"
//                + " AND IDV.itemName is not null AND IDV.itemCode is not null "
//                + " AND IDV.qtyin is not null  AND IDV.qtyout is not null ");
//
//        stringBuilder.append(appendGlYear(itemCardReportSerachBean, glYear, params));
//        stringBuilder.append(appendDate(itemCardReportSerachBean, params));
//        stringBuilder.append(appendInventory(itemCardReportSerachBean, params));
//        stringBuilder.append(appendInvItem(itemCardReportSerachBean, params));
//
//        stringBuilder.append(" group by IDV.itemId ,IDV.itemName");
//        stringBuilder.append(" order by IDV.itemId ASC, IDV.date ASC");
//        List<Object[]> list2 = dao.findListByQuery(stringBuilder.toString(), params);
//        for (Object[] os : list2) {
//            qtyInMap.put((Integer) os[0], (BigDecimal) os[3]);
//            qtyOutMap.put((Integer) os[0], (BigDecimal) os[4]);
//        }
//
//        stringBuilder = new StringBuilder();
//        params = new HashMap<>();
//        params.put("branchId", itemCardReportSerachBean.getBranchId());
//        stringBuilder.append("SELECT IDV FROM ItemsDataView IDV WHERE IDV.branchId = :branchId "
//                + " AND IDV.date is not null AND IDV.itemId is not null "
//                + " AND IDV.inventoryId is not null AND IDV.groupId is not null"
//                + " AND IDV.itemName is not null AND IDV.itemCode is not null "
//                + " AND IDV.qtyin is not null  AND IDV.qtyout is not null ");
//
//        stringBuilder.append(appendDate(itemCardReportSerachBean, params));
//        stringBuilder.append(appendInventory(itemCardReportSerachBean, params));
//        stringBuilder.append(appendInvItem(itemCardReportSerachBean, params));
//
//        stringBuilder.append(" order by IDV.itemId , IDV.date ASC");
//
//        List<ItemsDataView> itemsDataViewList = dao.findListByQuery(stringBuilder.toString(), params);
//
//        for (ItemsDataView dataView : itemsDataViewList) {
//            dataView.setOpeningBalance(itemOpeningBalanceMap.get(dataView.getItemId()));
//            //i will set in organizationopenbalance credit and debit fields and use them as total value for qtyin and qty out
//            dataView.setTotalQtyIn(qtyInMap.get(dataView.getItemId()));
//            dataView.setTotalQtyOut(qtyOutMap.get(dataView.getItemId()));
//            if (itemOpeningBalanceAfterAllDateMap.containsKey(dataView.getItemId())) {
//                itemOpeningBalanceAfterAllDateMap.remove(dataView.getItemId());
//            }
//        }
////if search after all dates of item and need to get the open balance for them
//        for (Map.Entry<Integer, String> entry : itemOpeningBalanceAfterAllDateMap.entrySet()) {
//            ItemsDataView idv = new ItemsDataView();
//            idv.setItemName(entry.getValue());
//            idv.setItemId(entry.getKey());
//            idv.setOpeningBalance(itemOpeningBalanceMap.get(entry.getKey()));
//            idv.setTotalQtyIn(qtyInMap.get(entry.getKey()));
//            idv.setTotalQtyOut(qtyOutMap.get(entry.getKey()));
//            itemsDataViewList.add(idv);
//        }
//        
////        TreeMap<Date,ItemsDataView> treeMap = new TreeMap<>();
////        for(ItemsDataView dataView : itemsDataViewList){
////            treeMap.put(dataView.getDate(), dataView);
////        }
////        itemsDataViewList = new ArrayList<>();
////        itemsDataViewList = new ArrayList<ItemsDataView>(treeMap.values());
//        return itemsDataViewList;
//    }
//    private String appendGlYear(ItemCardReportSerachBean itemCardReportSerachBean, GlYear glYear, Map<String, Object> params) {
//        StringBuilder appendQuery = new StringBuilder();
//        if (itemCardReportSerachBean.getDateFrom() != null) {
//            params.put("dateFrom", glYear.getDateFrom());
//            appendQuery.append(" AND IDV.date >= :dateFrom");
//        }
//
//        if (itemCardReportSerachBean.getDateTo() != null) {
//            params.put("dateTo", itemCardReportSerachBean.getDateFrom());
//            appendQuery.append(" AND IDV.date <= :dateTo");
//        }
//
//        return appendQuery.toString();
//    }
//    private String appendTillDate(ItemCardReportSerachBean itemCardReportSerachBean, GlYear glYear, Map<String, Object> params) {
//        StringBuilder appendQuery = new StringBuilder();
//        if (itemCardReportSerachBean.getDateFrom() != null) {
//            params.put("dateTillFrom", itemCardReportSerachBean.getDateFrom());
//        }
//        appendQuery.append(" AND IDV.date < :dateTillFrom");
//
//        return appendQuery.toString();
//    }
//    private String appendInventory(ItemCardReportSerachBean itemCardReportSerachBean, Map<String, Object> params) {
//        StringBuilder appendQuery = new StringBuilder();
//        if (itemCardReportSerachBean.getInventoryFrom() != null && itemCardReportSerachBean.getInventoryFrom().getCode() != null) {
//            params.put("inventoryFrom", itemCardReportSerachBean.getInventoryFrom().getCode());
//            appendQuery.append(" AND IDV.inventoryCode >= :inventoryFrom");
//        }
//
//        if (itemCardReportSerachBean.getInventoryTo() != null && itemCardReportSerachBean.getInventoryTo().getCode() != null) {
//            params.put("inventoryTo", itemCardReportSerachBean.getInventoryTo().getCode());
//            appendQuery.append(" AND IDV.inventoryCode <= :inventoryTo");
//        }
//
//        return appendQuery.toString();
//    }
//    private String appendInvItem(ItemCardReportSerachBean itemCardReportSerachBean, Map<String, Object> params) {
//        StringBuilder appendQuery = new StringBuilder();
//        if (itemCardReportSerachBean.getInvItemForm() != null && itemCardReportSerachBean.getInvItemForm().getCode() != null) {
//            params.put("itemCodeFrom", itemCardReportSerachBean.getInvItemForm().getCode());
//            appendQuery.append(" AND IDV.itemCode >= :itemCodeFrom");
//        }
//
//        if (itemCardReportSerachBean.getInvItemForm() != null && itemCardReportSerachBean.getInvItemTo().getCode() != null) {
//            params.put("itemCodeTo", itemCardReportSerachBean.getInvItemTo().getCode());
//            appendQuery.append(" AND IDV.itemCode <= :itemCodeTo");
//        }
//
//        return appendQuery.toString();
//    }
//    private String appendDate(ItemCardReportSerachBean itemCardReportSerachBean, Map<String, Object> params) {
//        StringBuilder appendQuery = new StringBuilder();
//        if (itemCardReportSerachBean.getDateFrom() != null) {
//            params.put("dateFrom", itemCardReportSerachBean.getDateFrom());
//            appendQuery.append(" AND IDV.date >= :dateFrom");
//        }
//
//        if (itemCardReportSerachBean.getDateTo() != null) {
//            params.put("dateTo", itemCardReportSerachBean.getDateTo());
//            appendQuery.append(" AND IDV.date <= :dateTo");
//        }
//
//        return appendQuery.toString();
//    }
//
//    @Override
//    public List<ItemsDataView> getAllInvStripListByInvStripSearchBean(InvItemSalesSearchBean invStripSearchBean) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("branchId", invStripSearchBean.getBranchId());
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(""
//                + "SELECT sum(IDV.qtyout),IDV.costAverage,"
//                + " sum(IDV.qtyin),"
//                + " sum(IDV.qtyin) - sum(IDV.qtyout) , "
//                + " sum((IDV.qtyin - IDV.qtyout) * IDV.costAverage), "
//                + " IDV.itemId,"
//                + " IDV.itemName,"
//                + " IDV.itemCode,"
//                + " IDV.itemUnitName, "
//                + " IDV.groupId, "
//                + " IDV.groupName, "
//                + " IDV.inventoryId, "
//                + " IDV.inventoryCode, "
//                + " IDV.inventoryName"
//                + " FROM ItemsDataView IDV WHERE IDV.branchId = :branchId AND IDV.date is not null AND IDV.itemId is not null "
//                + " AND IDV.inventoryId is not null AND IDV.groupId is not null"
//                + " AND IDV.itemName is not null  AND IDV.itemCode is not null AND IDV.itemUnitName is not null "
//                + " AND IDV.qtyin is not null  AND IDV.qtyout is not null AND IDV.costAverage is not null  ");
//
//        stringBuilder.append(appendDateinv(invStripSearchBean, params));
//        stringBuilder.append(appendInventoryinv(invStripSearchBean, params));
//        stringBuilder.append(appendGroupinv(invStripSearchBean, params));
//        stringBuilder.append(appendSuplierinv(invStripSearchBean, params));
//
//        stringBuilder.append(" group by IDV.itemId, "
//                + " IDV.itemName, "
//                + " IDV.itemCode, "
//                + " IDV.itemUnitName, "
//                + " IDV.groupId, "
//                + " IDV.groupName, "
//                + " IDV.inventoryId,"
//                + " IDV.inventoryCode,"
//                + " IDV.inventoryName,IDV.costAverage");
//
//        List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);
//
//        List<ItemsDataView> details = prepareList(res);
//
//        return details;
//    }
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
            itemsDataView.setSellPrice((BigDecimal) object[11]);
            itemsDataView.setGroupId((Integer) object[12]);
            itemsDataView.setGroupName((String) object[13]);
            itemsDataView.setInventoryId((Integer) object[14]);
            itemsDataView.setInventoryCode((String) object[15]);
            itemsDataView.setInventoryName((String) object[16]);

            list.add(itemsDataView);
        }
        return list;
    }

    public InvItemDTO mapToDTO(InvItem invItem, TobyUser tobyUser) {
        InvItemDTO invItemDTO = new InvItemDTO();
        invItemDTO.setCreatedBy(invItem.getCreatedBy() != null ? invItem.getCreatedBy().getId() : null);
        invItemDTO.setCreatedDate(invItem.getCreationDate());
        invItemDTO.setBranchId(invItem.getBranchId() != null ? invItem.getBranchId().getId() : null);
        invItemDTO.setCompanyId(invItem.getCompanyId() != null ? invItem.getCompanyId().getId() : null);
        invItemDTO.setId(invItem.getId());
        invItemDTO.setCode(invItem.getCode());
        invItemDTO.setContractPrice(invItem.getContractPrice());
        invItemDTO.setCostAverage(invItem.getCostAverage());
        invItemDTO.setDateCreateCat(invItem.getCreationDate());
        invItemDTO.setImage(invItem.getImage() != null ? invItem.getImage() : null);
//        invItemDTO.setShowImage(invItem.getImage()!= null ? fileUploadController.getDestination().concat(invItem.getImage()) : fileUploadController.getDestination() + "1.png");
//        invItemDTO.setUploadedImage(invItem.getImage());

        invItemDTO.setIspurchase(invItem.getIspurchase());
        invItemDTO.setIssell(invItem.getIssell());
        invItemDTO.setLastCost(invItem.getLastCost() != null ? invItem.getLastCost() : BigDecimal.ZERO);
        invItemDTO.setMarkEdit(invItem.getMarkEdit());
        invItemDTO.setMaxmumAmount(invItem.getMaxmumAmount());
        invItemDTO.setMinimumAmount(invItem.getMinimumAmount());
        invItemDTO.setName(invItem.getName());
        invItemDTO.setNameen(invItem.getNameen());
        invItemDTO.setNickname(invItem.getNickname());
        invItemDTO.setOpeningCost(invItem.getOpeningCost());
        invItemDTO.setWeightPackage(invItem.getWeightPackage());
        invItemDTO.setRemarks(invItem.getRemarks());
        invItemDTO.setSellPrice(invItem.getSellPrice());
        invItemDTO.setStatusCat(invItem.getStatusCat());
        invItemDTO.setStorageLocation(invItem.getStorageLocation());
        invItemDTO.setStoresQuality(invItem.getStoresQuality());
        invItemDTO.setTypeCat(invItem.getTypeCat());
        invItemDTO.setUndirectCost(invItem.getUndirectCost());
        invItemDTO.setSiteId(invItem.getSiteId());
        invItemDTO.setLength(invItem.getLength());
        invItemDTO.setHeight(invItem.getHeight());
        invItemDTO.setWeight(invItem.getWeight());
        invItemDTO.setWidth(invItem.getWidth());
        invItemDTO.setDiscountrate(invItem.getDiscountrate());
        invItemDTO.setDiscountvalue(invItem.getDiscountvalue());
        invItemDTO.setMaxpriceyoung(invItem.getMaxpriceyoung());
        invItemDTO.setMaxpricemen(invItem.getMaxpricemen());
        invItemDTO.setMinpriceyoung(invItem.getMinpriceyoung());
        invItemDTO.setMinpricemen(invItem.getMinpricemen());
        invItemDTO.setNumbermetersyoung(invItem.getNumbermetersyoung());
        invItemDTO.setNumbermetersmen(invItem.getNumbermetersmen());
        invItemDTO.setNumbermetersfreeyoung(invItem.getNumbermetersfreeyoung());
        invItemDTO.setNumbermetersfreemen(invItem.getNumbermetersfreemen());
        invItemDTO.setBounsepriceyoung(invItem.getBounsepriceyoung());
        invItemDTO.setBounsepricemen(invItem.getBounsepricemen());
        invItemDTO.setCommissionrate(invItem.getCommissionrate());
        invItemDTO.setCommissionvalue(invItem.getCommissionvalue());
        invItemDTO.setPrice_edit(invItem.getPrice_edit());
        invItemDTO.setIsinventoryitem(invItem.getIsInventoryItem());        
        invItemDTO.setTypeshow(invItem.getTypeshow());
        invItemDTO.setQuantity(invItem.getQuantity());
        invItemDTO.setItemNatural(invItem.getItemNatural());
        if (invItem.getOriginCountry() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(invItem.getOriginCountry().getId());
            symbolDTO.setName(invItem.getOriginCountry().getName());
            invItemDTO.setOriginCountry(symbolDTO);
        }
        if (invItem.getPaintColor() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(invItem.getPaintColor().getId());
            symbolDTO.setName(invItem.getPaintColor().getName());
            invItemDTO.setPaintColor(symbolDTO);
        }
        if (invItem.getStone() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(invItem.getStone().getId());
            symbolDTO.setName(invItem.getStone().getName());
            invItemDTO.setStone(symbolDTO);
        }
        if (invItem.getUnitId() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(invItem.getUnitId().getId());
            symbolDTO.setName(invItem.getUnitId().getName());
            symbolDTO.setCreatedByCode(invItem.getUnitId().getCreatedBy().getUserCode());
            invItemDTO.setUnitId(symbolDTO);
        }
        if (invItem.getAddon1() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(invItem.getAddon1().getId());
            symbolDTO.setName(invItem.getAddon1().getName());
            invItemDTO.setAddon1(symbolDTO);
        }
        if (invItem.getAddon2() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(invItem.getAddon2().getId());
            symbolDTO.setName(invItem.getAddon2().getName());
            invItemDTO.setAddon2(symbolDTO);
        }
        
        if (invItem.getGroupId() != null) {
            InvGroupDTO invGroupDTO = new InvGroupDTO();
            invGroupDTO.setId(invItem.getGroupId().getId());
            invGroupDTO.setName(invItem.getGroupId().getName());
            invGroupDTO.setCode(invItem.getGroupId().getCode());
            invItemDTO.setGroupId(invGroupDTO);
        }
        
        if (invItem.getProGroupId() != null) {
            ProGroupDTO proGroupDTO = new ProGroupDTO();
            proGroupDTO.setId(invItem.getProGroupId().getId());
            proGroupDTO.setName(invItem.getProGroupId().getName());
            proGroupDTO.setCode(invItem.getProGroupId().getCode());
            invItemDTO.setProGroupId(proGroupDTO);
        }
        
        invItemDTO.setInvBarcodeEntity(invBarcodeSevice.getInvBarcodeByInvItemId(invItem.getBranchId().getId(), invItem.getId(), tobyUser));

        return invItemDTO;
    }

    public InvItem mapToEntity(InvItemDTO invItemDTO, TobyUser tobyUser) {

        InvItem invItem = new InvItem();
        invItem.setId(invItemDTO.getId());
        invItem.setCode(invItemDTO.getCode());
        invItem.setContractPrice(invItemDTO.getContractPrice());
        invItem.setCostAverage(invItemDTO.getCostAverage());
        invItem.setDateCreateCat(invItemDTO.getDateCreateCat());
        invItem.setImage(invItemDTO.getImage() != null ? invItemDTO.getImage() : null);
//        invItemDTO.setShowImage(invItem.getImage()!= null ? fileUploadController.getDestination().concat(invItem.getImage()) : fileUploadController.getDestination() + "1.png");
//        invItemDTO.setUploadedImage(invItem.getImage());

        invItem.setIspurchase(invItemDTO.getIspurchase());
        invItem.setIssell(invItemDTO.getIssell());
        invItem.setLastCost(invItemDTO.getLastCost() != null ? invItemDTO.getLastCost() : BigDecimal.ZERO);
        invItem.setMarkEdit(invItemDTO.getMarkEdit());
        invItem.setMaxmumAmount(invItemDTO.getMaxmumAmount());
        invItem.setMinimumAmount(invItemDTO.getMinimumAmount());
        invItem.setName(invItemDTO.getName());
        invItem.setNameen(invItemDTO.getNameen());
        invItem.setNickname(invItemDTO.getNickname());
        invItem.setOpeningCost(invItemDTO.getOpeningCost());
        invItem.setWeightPackage(invItemDTO.getWeightPackage());
        invItem.setRemarks(invItemDTO.getRemarks());
        invItem.setSellPrice(invItemDTO.getSellPrice());
        invItem.setStatusCat(invItemDTO.getStatusCat());
        invItem.setStorageLocation(invItemDTO.getStorageLocation());
        invItem.setStoresQuality(invItemDTO.getStoresQuality());
        invItem.setTypeCat(invItemDTO.getTypeCat());
        invItem.setUndirectCost(invItemDTO.getUndirectCost());
        invItem.setSiteId(invItemDTO.getSiteId());
        invItem.setLength(invItemDTO.getLength());
        invItem.setHeight(invItemDTO.getHeight());
        invItem.setWeight(invItemDTO.getWeight());
        invItem.setWidth(invItemDTO.getWidth());
        invItem.setDiscountrate(invItemDTO.getDiscountrate());
        invItem.setDiscountvalue(invItemDTO.getDiscountvalue());
        invItem.setMaxpriceyoung(invItemDTO.getMaxpriceyoung());
        invItem.setMaxpricemen(invItemDTO.getMaxpricemen());
        invItem.setMinpriceyoung(invItemDTO.getMinpriceyoung());
        invItem.setMinpricemen(invItemDTO.getMinpricemen());
        invItem.setNumbermetersyoung(invItemDTO.getNumbermetersyoung());
        invItem.setNumbermetersmen(invItemDTO.getNumbermetersmen());
        invItem.setNumbermetersfreeyoung(invItemDTO.getNumbermetersyoung());
        invItem.setNumbermetersfreemen(invItemDTO.getNumbermetersfreemen());
        invItem.setBounsepriceyoung(invItemDTO.getBounsepriceyoung());
        invItem.setBounsepricemen(invItemDTO.getBounsepricemen());
        invItem.setCommissionrate(invItemDTO.getCommissionrate());
        invItem.setCommissionvalue(invItemDTO.getCommissionvalue());
        invItem.setPrice_edit(invItemDTO.getPrice_edit());
        invItem.setIsInventoryItem(invItemDTO.getIsinventoryitem());
        invItem.setTypeshow(invItemDTO.getTypeshow());
        invItem.setItemNatural(invItemDTO.getItemNatural());
        invItem.setQuantity(invItemDTO.getQuantity());
        /// Get Serial ///
//        Map<String, Object> params = new HashMap<>();
//        params.put("branchId", tobyUser.getBranchId().getId());
//        Integer serialmax = 0;
//        synchronized (serialmax) {
//            try {
//                serialmax = dao.findEntityByQuery("SELECT MAX(g.serial) FROM InvPurchaseInvoice g  WHERE g.branchId.id =:branchId ", params);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        if (serialmax == null) {
//            serialmax = 0;
//        }
//        invItem.setSerial(serialmax + 1);
        if (invItemDTO.getOriginCountry() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invItemDTO.getGroupId().getId());
            symbol.setName(invItemDTO.getGroupId().getName());
            invItem.setOriginCountry(symbol);
        }
        if (invItemDTO.getPaintColor() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invItemDTO.getPaintColor().getId());
            symbol.setName(invItemDTO.getPaintColor().getName());
            invItem.setPaintColor(symbol);
        }
        if (invItemDTO.getStone() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invItemDTO.getStone().getId());
            symbol.setName(invItemDTO.getStone().getName());
            invItem.setStone(symbol);
        }
        if (invItemDTO.getUnitId() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invItemDTO.getUnitId().getId());
            symbol.setName(invItemDTO.getUnitId().getName());
            invItem.setUnitId(symbol);
        }
        if (invItemDTO.getAddon1() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invItemDTO.getAddon1().getId());
            symbol.setName(invItemDTO.getAddon1().getName());
            invItem.setAddon1(symbol);
        }
        if (invItemDTO.getAddon2() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invItemDTO.getAddon2().getId());
            symbol.setName(invItemDTO.getAddon2().getName());
            invItem.setAddon2(symbol);
        }
        if (invItemDTO.getGroupId() != null) {
            InvGroup invGroup = new InvGroup();
            invGroup.setId(invItemDTO.getGroupId().getId());
            invGroup.setName(invItemDTO.getGroupId().getName());
            invGroup.setCode(invItemDTO.getGroupId().getCode());
            invItem.setGroupId(invGroup);
        }
        
        if (invItemDTO.getProGroupId() != null) {
            ProGroup proGroup = new ProGroup();
            proGroup.setId(invItemDTO.getProGroupId().getId());
            proGroup.setName(invItemDTO.getProGroupId().getName());
            proGroup.setCode(invItemDTO.getProGroupId().getCode());
            invItem.setProGroupId(proGroup);
        }

        if (invItemDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(invItemDTO.getCreatedBy());
            invItem.setCreatedBy(user);
            invItem.setCreationDate(invItemDTO.getCreatedDate());
            invItem.setModifiedBy(tobyUser);
            invItem.setModificationDate(new Date());
            if (invItemDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(invItemDTO.getCompanyId());
                invItem.setCompanyId(company);
            }

            if (invItemDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(invItemDTO.getBranchId());
                invItem.setBranchId(branch);
            }
        } else {
            invItem.setCreatedBy(tobyUser);
            invItem.setCreationDate(new Date());
            invItem.setCompanyId(tobyUser.getCompanyId());
            invItem.setBranchId(tobyUser.getBranchId());
        }
        return invItem;
    }

    public List<InvItemDTO> mapToDTOList(List<InvItem> invItemList, TobyUser tobyUser) {
        List<InvItemDTO> invItemDTOList = new ArrayList<>();
        for (InvItem invItem : invItemList) {
            invItemDTOList.add(mapToDTO(invItem, tobyUser));
        }
        return invItemDTOList;
    }

    @Override
    public InvItemDTO addItemsData(InvItemDTO invItemDTO, TobyUser tobyUser) {
        InvItemDTO invItemDTO1 = new InvItemDTO();
        InvItem invItem = mapToEntity(invItemDTO, tobyUser);
        invItem = dao.updateEntity(invItem);

        if (invItemDTO.getInvBarcodeDeletedList() != null) {
            invBarcodeSevice.deleteListInvBarcode(invItemDTO.getInvBarcodeDeletedList());
        }
        
        if (invItemDTO.getInvBarcodeEntity() != null) {
            invBarcodeSevice.addListInvBarcode(invItemDTO.getInvBarcodeEntity(),tobyUser,invItem.getId());
        }

        invItemDTO1 = mapToDTO(invItem, tobyUser);
        return invItemDTO1;
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
                + " IDV.inventoryName"
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
                + " IDV.groupId, "
                + " IDV.groupName, "
                + " IDV.inventoryId,"
                + " IDV.inventoryCode,"
                + " IDV.inventoryName,IDV.costAverage");

        List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);

        List<ItemsDataView> details = prepareList(res);

        return details;
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

    @Override
    public InvItemDTO findInvItem(Integer invItemById, TobyUser tobyUser) {
        InvItem invItem = dao.findEntityById(InvItem.class, invItemById);
        return mapToDTO(invItem, tobyUser);
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
    

}
