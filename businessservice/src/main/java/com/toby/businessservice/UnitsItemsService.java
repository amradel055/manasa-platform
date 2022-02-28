/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.UnitsItems;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author H
 */
@Remote
public interface UnitsItemsService {
    public List<UnitsItems> getUnitsItemsList();
    public List<UnitsItems> getUnitsByItemId(Integer itemId);
    public UnitsItems getScrewingAndPrice(Integer itemId , Integer unitId);
}
