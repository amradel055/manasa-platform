/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.ItemMainDataByGroupSearchBean;
import com.isag.core.GenericDAO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.InventorySetup;
import com.isag.views.QuantityItemsStore;
import com.isag.views.QuantityItemsStoreAddExit;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 *
 * @author anady
 */
@Stateless
public class QuantityItemsStoreServiceImpl implements QuantityItemsStoreService {

    @EJB
    private GenericDAO dao;
    @EJB
    InventorySetupService inventorySetupService;
    @EJB
    InvItemService itemService;
    @EJB
    QuantityItemsStoreByDateService itemsStoreByDateService;

    @Override
    public List<QuantityItemsStore> findInventoryItemsList(Integer inventoryId, Integer branchId, Boolean sellBuy) {
        Map<String, Object> params = new HashMap();
        List<QuantityItemsStore> quantityItemsStores = null;
        List<QuantityItemsStoreAddExit> itemsStoreAddExits;
        params.put("inventoryId", inventoryId);
        params.put("branchId", branchId);
        StringBuilder sql = new StringBuilder();
        if (sellBuy) {
            quantityItemsStores = new ArrayList<>();
            sql.append("SELECT inv FROM QuantityItemsStore inv WHERE inv.inventoryId = :inventoryId AND inv.branchId = :branchId");
            quantityItemsStores = dao.findListByQuery(sql.toString(), params);
        } else {
            quantityItemsStores = new ArrayList<>();
            sql.append("SELECT inv FROM QuantityItemsStoreAddExit inv WHERE inv.inventoryId = :inventoryId AND inv.branchId = :branchId");
            itemsStoreAddExits = dao.findListByQuery(sql.toString(), params);
            MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
            for (QuantityItemsStoreAddExit itemsStoreAddExit : itemsStoreAddExits) {
                mapperFactory.classMap(QuantityItemsStoreAddExit.class, QuantityItemsStore.class).byDefault().register();
                MapperFacade mapper = mapperFactory.getMapperFacade();
                QuantityItemsStore dest = mapper.map(itemsStoreAddExit, QuantityItemsStore.class);
                quantityItemsStores.add(dest);
            }
        }
        return quantityItemsStores;

    }

    @Override
    public synchronized QuantityItemsStore findInventoryItem(Integer inventoryId, Integer branchId, Integer itemId, Boolean sellBuy) {
        Map<String, Object> params = new HashMap();
        params.put("inventoryId", inventoryId);
        params.put("branchId", branchId);
        params.put("itemId", itemId);
        StringBuilder sql = new StringBuilder();
        List<QuantityItemsStore> quantityItemsStores = new ArrayList<>();
        List<QuantityItemsStoreAddExit> itemsStoreAddExits;
        if (sellBuy) {
            sql.append("SELECT inv FROM QuantityItemsStore inv WHERE inv.inventoryId = :inventoryId AND inv.branchId = :branchId AND inv.itemId = :itemId");
            quantityItemsStores = dao.findListByQuery(sql.toString(), params);
        } else {
            sql.append("SELECT inv FROM QuantityItemsStoreAddExit inv WHERE inv.inventoryId = :inventoryId AND inv.branchId = :branchId AND inv.itemId = :itemId");
            itemsStoreAddExits = dao.findListByQuery(sql.toString(), params);
            MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
            for (QuantityItemsStoreAddExit itemsStoreAddExit : itemsStoreAddExits) {
                mapperFactory.classMap(QuantityItemsStoreAddExit.class, QuantityItemsStore.class).byDefault().register();
                MapperFacade mapper = mapperFactory.getMapperFacade();
                QuantityItemsStore dest = mapper.map(itemsStoreAddExit, QuantityItemsStore.class);
                quantityItemsStores.add(dest);
            }
        }

        return (quantityItemsStores != null && quantityItemsStores.size() > 0) ? quantityItemsStores.get(0) : null;
    }
    
    @Override
    public List<QuantityItemsStore> findItemsForBranchList(ItemMainDataByGroupSearchBean  itemMainDataByGroupSearchBean, Integer branchId) {
        Map<String, Object> params = new HashMap();
        StringBuilder sql = new StringBuilder();
         InventorySetup setup = inventorySetupService.getInventoryByBranchId(branchId);
        params.put("branchId", branchId);
        List<QuantityItemsStore> quantityItemsStores = null;
        List<QuantityItemsStoreAddExit> itemsStoreAddExits;
        if (setup.getSellBuy()) {
            quantityItemsStores = new ArrayList<>();
            sql.append("SELECT inv FROM QuantityItemsStore inv WHERE inv.itemId = :itemId AND inv.branchId = :branchId AND inv.inventoryId != null");
            quantityItemsStores = dao.findListByQuery(sql.toString(), params);
        } else {
            quantityItemsStores = new ArrayList<>();
            sql.append("SELECT inv FROM QuantityItemsStoreAddExit inv WHERE inv.itemId = :itemId AND inv.branchId = :branchId AND inv.inventoryId != null");
            itemsStoreAddExits = dao.findListByQuery(sql.toString(), params);
            MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
            for (QuantityItemsStoreAddExit itemsStoreAddExit : itemsStoreAddExits) {
                mapperFactory.classMap(QuantityItemsStoreAddExit.class, QuantityItemsStore.class).byDefault().register();
                MapperFacade mapper = mapperFactory.getMapperFacade();
                QuantityItemsStore dest = mapper.map(itemsStoreAddExit, QuantityItemsStore.class);
                quantityItemsStores.add(dest);
            }
        }
        return quantityItemsStores;
    }
    
    private String appendItem(ItemMainDataByGroupSearchBean ItemMainDataByGroupSearchBean, Map<String, Object> params) {

        StringBuilder sql = new StringBuilder();
        
        if (ItemMainDataByGroupSearchBean.getFromItemDTO()!= null) {
            params.put("fromItem", ItemMainDataByGroupSearchBean.getFromItemDTO().getCode()); //change to get code not id
            sql.append(" AND inv.itemCode >= :fromItem"); //e.id
        }

        if (ItemMainDataByGroupSearchBean.getToItemDTO()!= null) {
            params.put("toItem", ItemMainDataByGroupSearchBean.getToItemDTO().getCode());//change to get code not id
            sql.append(" AND inv.itemCode <= :toItem"); //e.id
        }

        return sql.toString();
    }

    @Override
    public List<QuantityItemsStore> findItemsForBranchList(Integer itemId, Integer branchId) {
        Map<String, Object> params = new HashMap();
        StringBuilder sql = new StringBuilder();
        InventorySetup setup = inventorySetupService.getInventoryByBranchId(branchId);
        params.put("itemId", itemId);
        params.put("branchId", branchId);
        List<QuantityItemsStore> quantityItemsStores = null;
        List<QuantityItemsStoreAddExit> itemsStoreAddExits;
        if (setup.getSellBuy()) {
            quantityItemsStores = new ArrayList<>();
            sql.append("SELECT inv FROM QuantityItemsStore inv WHERE inv.itemId = :itemId AND inv.branchId = :branchId AND inv.inventoryId != null");
            quantityItemsStores = dao.findListByQuery(sql.toString(), params);
        } else {
            quantityItemsStores = new ArrayList<>();
            sql.append("SELECT inv FROM QuantityItemsStoreAddExit inv WHERE inv.itemId = :itemId AND inv.branchId = :branchId AND inv.inventoryId != null");
            itemsStoreAddExits = dao.findListByQuery(sql.toString(), params);
            MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
            for (QuantityItemsStoreAddExit itemsStoreAddExit : itemsStoreAddExits) {
                mapperFactory.classMap(QuantityItemsStoreAddExit.class, QuantityItemsStore.class).byDefault().register();
                MapperFacade mapper = mapperFactory.getMapperFacade();
                QuantityItemsStore dest = mapper.map(itemsStoreAddExit, QuantityItemsStore.class);
                quantityItemsStores.add(dest);
            }
        }
        return quantityItemsStores;

    }

    @Override
    public List<QuantityItemsStore> findAllQuantityItemsListStoreByBranchId(Integer branchId, Boolean sellBuy) {
        Map<String, Object> params = new HashMap();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        List<QuantityItemsStore> quantityItemsStores = null;
        List<QuantityItemsStoreAddExit> itemsStoreAddExits;
        if (sellBuy) {
            sql.append("SELECT inv FROM QuantityItemsStore inv WHERE inv.branchId = :branchId AND inv.inventoryId != null");
            quantityItemsStores = dao.findListByQuery(sql.toString(), params);
        } else {
            sql.append("SELECT inv FROM QuantityItemsStoreAddExit inv WHERE inv.branchId = :branchId AND inv.inventoryId != null");
            itemsStoreAddExits = dao.findListByQuery(sql.toString(), params);
            MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
            for (QuantityItemsStoreAddExit itemsStoreAddExit : itemsStoreAddExits) {
                mapperFactory.classMap(QuantityItemsStoreAddExit.class, QuantityItemsStore.class).byDefault().register();
                MapperFacade mapper = mapperFactory.getMapperFacade();
                QuantityItemsStore dest = mapper.map(itemsStoreAddExit, QuantityItemsStore.class);
                quantityItemsStores.add(dest);
            }
        }
        return quantityItemsStores;
    }

    public InvInvoiceDTO fillItemMap(InvInvoiceDTO invPurchaseInvoiceDTO) {
        try {
            if (invPurchaseInvoiceDTO.getInvInventoryId() != null) {
                invPurchaseInvoiceDTO.setItemsMap(new HashMap());
                invPurchaseInvoiceDTO.setItemsMap(returnItemsMap(invPurchaseInvoiceDTO.getInvInventoryId().getId(),invPurchaseInvoiceDTO.getBranchId(),invPurchaseInvoiceDTO.getInventorySetupDTO().getSellBuy()));
            }
        } catch (Exception e) {
            invPurchaseInvoiceDTO.setMsg(e.toString());
        }
        return invPurchaseInvoiceDTO;
    }

    private Map<Integer, BigDecimal> returnItemsMap(Integer invInventoryId, Integer branchId, Boolean sellBuy) {
        Map<Integer, BigDecimal> itemMap = new HashMap<>();
        List<QuantityItemsStore> quantityItemsStoresList = findInventoryItemsList(invInventoryId, branchId, sellBuy);

        for (QuantityItemsStore quantityItemsStore : quantityItemsStoresList) {
            itemMap.put(quantityItemsStore.getItemId(), quantityItemsStore.getQty());
        }
        return itemMap;
    }
}
