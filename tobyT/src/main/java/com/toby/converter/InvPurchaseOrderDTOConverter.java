/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;

import com.toby.dto.InvPurchaseInvoiceDTO1;
import com.toby.dto.InvPurchaseOrderDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author m_els
 */
public class InvPurchaseOrderDTOConverter implements Converter {

    private List<InvPurchaseOrderDTO> invPurchaseOrderDTOList;

    public InvPurchaseOrderDTOConverter(List<InvPurchaseOrderDTO> invPurchaseOrderDTOList) {
        this.invPurchaseOrderDTOList = invPurchaseOrderDTOList;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (InvPurchaseOrderDTO invPurchaseOrderDTO : invPurchaseOrderDTOList) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && invPurchaseOrderDTO.getId().longValue() == Long.parseLong(value)) {
                return invPurchaseOrderDTO;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((InvPurchaseOrderDTO) value).getId() != null && ((InvPurchaseOrderDTO) value).getId().longValue() != 0) {
                    InvPurchaseOrderDTO codegenerator = (InvPurchaseOrderDTO) value;
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
