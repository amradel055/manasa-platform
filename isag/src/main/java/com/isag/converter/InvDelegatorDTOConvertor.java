/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.dto.InventoryDelegatorDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author WIN7
 */
public class InvDelegatorDTOConvertor implements Converter {

    private List<InventoryDelegatorDTO> inventoryDelegatorList;

    public InvDelegatorDTOConvertor(List<InventoryDelegatorDTO> inventoryDelegators) {
        this.inventoryDelegatorList = inventoryDelegators;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (InventoryDelegatorDTO inventoryDelegator : inventoryDelegatorList) {
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
                if (((InventoryDelegatorDTO) value).getId() != null && ((InventoryDelegatorDTO) value).getId().longValue() != 0) {
                    InventoryDelegatorDTO codegenerator = (InventoryDelegatorDTO) value;
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
