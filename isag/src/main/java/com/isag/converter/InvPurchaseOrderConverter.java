/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.entity.InvPurchaseOrder;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author m_els
 */
public class InvPurchaseOrderConverter implements Converter {

    private List<InvPurchaseOrder> invPurchaseOrders;

    public InvPurchaseOrderConverter(List<InvPurchaseOrder> invPurchaseInvoices) {
        this.invPurchaseOrders = invPurchaseInvoices;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (InvPurchaseOrder purchaseOrder : invPurchaseOrders) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && purchaseOrder.getId().longValue() == Long.parseLong(value)) {
                return purchaseOrder;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((InvPurchaseOrder) value).getId() != null && ((InvPurchaseOrder) value).getId().longValue() != 0) {
                    InvPurchaseOrder codegenerator = (InvPurchaseOrder) value;
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
