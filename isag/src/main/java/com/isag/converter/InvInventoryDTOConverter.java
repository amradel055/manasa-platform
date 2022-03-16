/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.dto.InvInventoryDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author m_els
 */
public class InvInventoryDTOConverter implements Converter {

    private List<InvInventoryDTO> invInventoryList;

    public InvInventoryDTOConverter(List<InvInventoryDTO> invInventoryList) {
        this.invInventoryList = invInventoryList;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (InvInventoryDTO inventory : invInventoryList) {
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
                if (((InvInventoryDTO) value).getId() != null && ((InvInventoryDTO) value).getId().longValue() != 0) {
                    InvInventoryDTO codegenerator = (InvInventoryDTO) value;
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
