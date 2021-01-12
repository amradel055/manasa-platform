/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.QuantityItemsStoreDTO;
import com.toby.entity.Branch;
import com.toby.entity.QuantityItemsStoreAddExit;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import com.toby.views.QuantityItemsStore;
import java.util.ArrayList;
import java.util.Date;
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
    private GenericDAO dao ;

    @Override
    public List<QuantityItemsStore> findInventoryItemsList(Integer inventoryId, Integer branchId, Boolean sellBuy) {
        Map<String, Object> params = new HashMap();
        List<QuantityItemsStore> quantityItemsStores = null;
        List<QuantityItemsStoreAddExit> itemsStoreAddExits;
        params.put("inventoryId", inventoryId);
        params.put("branchId", branchId);
        StringBuilder sql = new StringBuilder();
        if (sellBuy) {
            quantityItemsStores=new ArrayList<>();
            sql.append("SELECT inv FROM QuantityItemsStore inv WHERE inv.inventoryId = :inventoryId AND inv.branchId = :branchId");
            quantityItemsStores = dao.findListByQuery(sql.toString(), params);
        } else {
            quantityItemsStores=new ArrayList<>();
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

    
      public QuantityItemsStore mapToEntity(QuantityItemsStoreDTO quantityItemsStoreDTO, TobyUser tobyUser) {
        QuantityItemsStore quantityItemsStore = new QuantityItemsStore();
 
        quantityItemsStore.setCostAverage(quantityItemsStoreDTO.getCostAverage());
        quantityItemsStore.setInventoryCode(quantityItemsStoreDTO.getInventoryCode());
        quantityItemsStore.setInventoryId(quantityItemsStoreDTO.getInventoryId());
        quantityItemsStore.setInventoryName(quantityItemsStoreDTO.getInventoryName());
        quantityItemsStore.setItemCode(quantityItemsStoreDTO.getItemCode());
        quantityItemsStore.setItemId(quantityItemsStoreDTO.getItemId());
        quantityItemsStore.setItemName(quantityItemsStoreDTO.getItemName());
        quantityItemsStore.setQty(quantityItemsStoreDTO.getQty());
       
        return quantityItemsStore;
      }
      
      
      public QuantityItemsStoreDTO mapToDTO(QuantityItemsStore quantityItemsStore, TobyUser tobyUser) {
        QuantityItemsStoreDTO quantityItemsStoreDTO = new QuantityItemsStoreDTO();
       
        quantityItemsStoreDTO.setCostAverage(quantityItemsStore.getCostAverage());
        quantityItemsStoreDTO.setInventoryCode(quantityItemsStore.getInventoryCode());
        quantityItemsStoreDTO.setInventoryId(quantityItemsStore.getInventoryId());
        quantityItemsStoreDTO.setInventoryName(quantityItemsStore.getInventoryName());
        quantityItemsStoreDTO.setItemCode(quantityItemsStore.getItemCode());
        quantityItemsStoreDTO.setItemId(quantityItemsStore.getItemId());
        quantityItemsStoreDTO.setItemName(quantityItemsStore.getItemName());
        quantityItemsStoreDTO.setQty(quantityItemsStore.getQty());
      
      
      return quantityItemsStoreDTO;

}
       public List<QuantityItemsStoreDTO> mapToDTOList(List<QuantityItemsStore> quantityItemsStoreList, TobyUser tobyUser) {
        List<QuantityItemsStoreDTO> quantityItemsStoreDTOList = new ArrayList<>();
        for (QuantityItemsStore quantityItemsStore : quantityItemsStoreList) {
            quantityItemsStoreDTOList.add(mapToDTO(quantityItemsStore,tobyUser));
        }
        return quantityItemsStoreDTOList;
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
    public List<QuantityItemsStore> findItemsForBranchList(Integer itemId, Integer branchId, Boolean sellBuy) {
        Map<String, Object> params = new HashMap();
        StringBuilder sql = new StringBuilder();
        params.put("itemId", itemId);
        params.put("branchId", branchId);
        List<QuantityItemsStore> quantityItemsStores = null;
        List<QuantityItemsStoreAddExit> itemsStoreAddExits;
        if (sellBuy) {
            quantityItemsStores=new ArrayList<>();
            sql.append("SELECT inv FROM QuantityItemsStore inv WHERE inv.itemId = :itemId AND inv.branchId = :branchId AND inv.inventoryId != null");
            quantityItemsStores = dao.findListByQuery(sql.toString(), params);
        } else {
            quantityItemsStores=new ArrayList<>();
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

    @Override
    public List<QuantityItemsStoreDTO> findInventoryItemsListDTO(Integer inventoryId, TobyUser tobyUser, Boolean sellBuy) {
          Map<String, Object> params = new HashMap();
        List<QuantityItemsStore> quantityItemsStores = new ArrayList<>();
        List<QuantityItemsStoreAddExit> itemsStoreAddExits;
        params.put("inventoryId", inventoryId);
        params.put("branchId", tobyUser.getBranchId().getId());
        StringBuilder sql = new StringBuilder();
        if (sellBuy) {
            sql.append("SELECT inv FROM QuantityItemsStore inv WHERE inv.inventoryId = :inventoryId AND inv.branchId = :branchId");
            quantityItemsStores = dao.findListByQuery(sql.toString(), params);
        } else {
           
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
        return mapToDTOList(quantityItemsStores, tobyUser);
    }
}
