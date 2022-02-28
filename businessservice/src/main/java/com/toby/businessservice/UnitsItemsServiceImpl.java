/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.entity.UnitsItems;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author H
 */
@Stateless

   
public class UnitsItemsServiceImpl implements UnitsItemsService{
 @EJB
    GenericDAO dao;
    @Override
    public List<UnitsItems> getUnitsItemsList() {
      List<UnitsItems> itemses=  dao.findListByQuery("SELECT e FROM UnitsItems e");
      return itemses;
    }

    @Override
    public List<UnitsItems> getUnitsByItemId(Integer itemId) {
        Map<String, Object> params = new HashMap<>();
        params.put("itemId", itemId);
        List<UnitsItems> items = dao.findListByQuery("SELECT e FROM UnitsItems e where e.itemId=:itemId",params);
        return items;
    }

    @Override
    public UnitsItems getScrewingAndPrice(Integer itemId, Integer unitId) {
        Map<String, Object> params = new HashMap<>();
        params.put("itemId", itemId);         //////  AND ((e.type = 0 OR e.type = 2) AND e.active = 1) ORDER BY e.code
        params.put("unitId", unitId);         //////  AND ((e.type = 0 OR e.type = 2) AND e.active = 1) ORDER BY e.code
        UnitsItems unitsItems = dao.findEntityByQuery("SELECT e FROM UnitsItems e WHERE e.itemId=:itemId and e.unitId=:unitId", params);
        return unitsItems;
    }


}
