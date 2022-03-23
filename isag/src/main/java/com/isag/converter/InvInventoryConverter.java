/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.entity.InvInventory;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author m_els
 */
public class InvInventoryConverter implements Converter {

    private List<InvInventory> invInventoryList;

    public InvInventoryConverter(List<InvInventory> currencies) {
        this.invInventoryList = currencies;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (InvInventory inventory : invInventoryList) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && inventory.getId().longValue() == Long.parseLong(value)) {
                return inventory;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((InvInventory) value).getId() != null && ((InvInventory) value).getId().longValue() != 0) {
                    InvInventory codegenerator = (InvInventory) value;
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
