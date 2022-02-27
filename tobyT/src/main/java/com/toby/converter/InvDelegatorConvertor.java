/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;

import com.toby.entity.InventoryDelegator;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author WIN7
 */
public class InvDelegatorConvertor implements Converter {

    private List<InventoryDelegator> inventoryDelegatorList;

    public InvDelegatorConvertor(List<InventoryDelegator> inventoryDelegators) {
        this.inventoryDelegatorList = inventoryDelegators;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (InventoryDelegator inventoryDelegator : inventoryDelegatorList) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && inventoryDelegator.getId().longValue() == Long.parseLong(value)) {
                return inventoryDelegator;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((InventoryDelegator) value).getId() != null && ((InventoryDelegator) value).getId().longValue() != 0) {
                    InventoryDelegator codegenerator = (InventoryDelegator) value;
                    return codegenerator.getId().longValue() + "";
                } else {
                    return "";
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
