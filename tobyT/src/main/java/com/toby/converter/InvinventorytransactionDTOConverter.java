/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;

import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvInventoryTransactionDTO;
import com.toby.entity.InvInventory;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author m_els
 */
public class InvinventorytransactionDTOConverter implements Converter {

    private List<InvInventoryTransactionDTO>  invInventoryTransactionDTO;

    public InvinventorytransactionDTOConverter(List<InvInventoryTransactionDTO> invInventoryTransactionDTO) {
        this.invInventoryTransactionDTO = invInventoryTransactionDTO;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (InvInventoryTransactionDTO inventory : invInventoryTransactionDTO) {
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
                if (((InvInventoryTransactionDTO) value).getId() != null && ((InvInventoryTransactionDTO) value).getId().longValue() != 0) {
                    InvInventoryTransactionDTO codegenerator = (InvInventoryTransactionDTO) value;
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
