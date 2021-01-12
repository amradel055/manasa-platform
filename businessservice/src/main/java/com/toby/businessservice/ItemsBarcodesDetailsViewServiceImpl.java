/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.entity.InvBarcode;
import com.toby.entity.InvItem;
import com.toby.views.ItemsBarcodesDetailsView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author anady
 */
@Stateless
public class ItemsBarcodesDetailsViewServiceImpl implements ItemsBarcodesDetailsViewService {
    
    @EJB
    private GenericDAO dao;
    
    @Override
    public List<ItemsBarcodesDetailsView> findItemsBarcodesDetailsViewBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        return dao.findListByQuery("SELECT e FROM ItemsBarcodesDetailsView e WHERE e.branchId = :branchId", params);
    }
    
    @Override
    public List<ItemsBarcodesDetailsView> findItemsBarcodesDetailsViewBranchIdByBarcode(Integer branchId , StringBuilder barcode,List<String> barcodeList) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<ItemsBarcodesDetailsView> list = new ArrayList<>();
        for(String x : barcodeList){
            params.put("barcode", x);
            String query = "SELECT e FROM ItemsBarcodesDetailsView e WHERE e.branchId = :branchId AND e.barcode = :barcode " ;
            List<ItemsBarcodesDetailsView> queryList =dao.findListByQuery(query, params);
            for(ItemsBarcodesDetailsView view : queryList){
                list.add(view);
            }
        }      
        return list;
    }
    
    @Override
    public Map<String, ItemsBarcodesDetailsView> findItemsBarcodesDetailsViewBranchIdMap(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        Map<String, ItemsBarcodesDetailsView> itemsBarcodesDetailsViewMap = new HashMap<>();
        params.put("branchId", branchId);
        List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList = dao.findListByQuery("SELECT e FROM ItemsBarcodesDetailsView e WHERE e.branchId = :branchId", params);
        for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailsViewList) {
            itemsBarcodesDetailsViewMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
        }
        return itemsBarcodesDetailsViewMap;
    }
    
    @Override
    public List<ItemsBarcodesDetailsView> findItemsBarcodesDetailsViewBranchIdAndStore(Integer branchId, String barcodes) {
        List<ItemsBarcodesDetailsView> barcodesDetailsViewsList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        String query;
        if (barcodes != null && !barcodes.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            query = "SELECT e FROM ItemsBarcodesDetailsView e WHERE e.branchId = :branchId and e.barcode";
            builder.append(query).append(" in (").append(barcodes).append(")");
            barcodesDetailsViewsList = dao.findListByQuery(builder.toString(), params);
            
        } else {
            return null;
        }
        return barcodesDetailsViewsList;
    }
    
    @Override
    public List<ItemsBarcodesDetailsView> findItemsBarcodesDetailsViewBranchIdByBarcode(Integer branchId, List<String> barcodeList) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<ItemsBarcodesDetailsView> list = new ArrayList<>(); 
         StringBuilder query1 = new StringBuilder();
         StringBuilder query2 = new StringBuilder();
            query1.append(" select i from InvItem i where i.branchId.id = :branchId  ");
            query2.append(" select i from InvBarcode i where i.branchId.id = :branchId  ");
         if(barcodeList != null && !barcodeList.isEmpty()){
              params.put("barcode", barcodeList);
           query1.append(" AND i.code in   :barcode ");
           query2.append(" AND i.code in   :barcode ");
         }
        List<InvItem> queryList = dao.findListByQuery(query1.toString(), params);
        List<InvBarcode> queryList2 = dao.findListByQuery(query2.toString(), params);

        for (InvItem invItem : queryList) {
            ItemsBarcodesDetailsView itemsBarcodesDetailsView = new ItemsBarcodesDetailsView();
            itemsBarcodesDetailsView.setBarcode(invItem.getCode());
            itemsBarcodesDetailsView.setItemId(invItem.getId());
            itemsBarcodesDetailsView.setCostAverage(invItem.getCostAverage());
            itemsBarcodesDetailsView.setName(invItem.getName());
            itemsBarcodesDetailsView.setNameEn(invItem.getNameen());
            itemsBarcodesDetailsView.setSellPrice(invItem.getSellPrice());
            itemsBarcodesDetailsView.setUnitId(invItem.getUnitId() == null ? null : invItem.getUnitId().getId());
            itemsBarcodesDetailsView.setWeight(invItem.getWeight());
            itemsBarcodesDetailsView.setHeight(invItem.getHeight());
            itemsBarcodesDetailsView.setAddon1(invItem.getAddon1() == null ? null : invItem.getAddon1().getName());
            itemsBarcodesDetailsView.setAddon2(invItem.getAddon2() == null ? null : invItem.getAddon2().getName());
            itemsBarcodesDetailsView.setPaintColor(invItem.getPaintColor() == null ? null : invItem.getPaintColor().getId());
            itemsBarcodesDetailsView.setUnitName(invItem.getUnitId().getName());
            itemsBarcodesDetailsView.setPaintColorName(invItem.getPaintColor() == null ? null : invItem.getPaintColor().getName());
            itemsBarcodesDetailsView.setScrewing(BigDecimal.ONE);
            itemsBarcodesDetailsView.setIsInventoryItem(invItem.getIsInventoryItem());
            list.add(itemsBarcodesDetailsView);
        }

        for (InvBarcode invBarcode : queryList2) {
            ItemsBarcodesDetailsView itemsBarcodesDetailsView = new ItemsBarcodesDetailsView();
            itemsBarcodesDetailsView.setBarcode(invBarcode.getCode());
            itemsBarcodesDetailsView.setItemId(invBarcode.getItemId().getId());
            itemsBarcodesDetailsView.setCostAverage(invBarcode.getItemId().getCostAverage());
            itemsBarcodesDetailsView.setName(invBarcode.getItemId().getName());
            itemsBarcodesDetailsView.setNameEn(invBarcode.getItemId().getNameen());
            itemsBarcodesDetailsView.setSellPrice(invBarcode.getPrice1());
            itemsBarcodesDetailsView.setUnitId(invBarcode.getUnitId() == null ? null : invBarcode.getUnitId().getId());
            itemsBarcodesDetailsView.setWeight(invBarcode.getWeight());
            itemsBarcodesDetailsView.setHeight(invBarcode.getHeight());
            itemsBarcodesDetailsView.setAddon1(invBarcode.getItemId().getAddon1() == null ? null : invBarcode.getItemId().getAddon1().getName());
            itemsBarcodesDetailsView.setAddon2(invBarcode.getItemId().getAddon2() == null ? null : invBarcode.getItemId().getAddon2().getName());
            itemsBarcodesDetailsView.setPaintColor(invBarcode.getPaintColor() == null ? null : invBarcode.getPaintColor().getId());
            itemsBarcodesDetailsView.setUnitName(invBarcode.getUnitId() == null ? null : invBarcode.getUnitId().getName());
            itemsBarcodesDetailsView.setPaintColorName(invBarcode.getPaintColor() == null ? null : invBarcode.getPaintColor().getName());
            itemsBarcodesDetailsView.setScrewing(invBarcode.getScrewing());
            itemsBarcodesDetailsView.setIsInventoryItem(invBarcode.getItemId() == null ? 1 : invBarcode.getItemId().getIsInventoryItem());
            list.add(itemsBarcodesDetailsView);
        }
        return list;
    }
}
