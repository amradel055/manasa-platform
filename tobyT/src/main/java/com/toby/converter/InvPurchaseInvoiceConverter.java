/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;

import com.toby.entity.InvPurchaseInvoice;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author m_els
 */
public class InvPurchaseInvoiceConverter implements Converter {

    private List<InvPurchaseInvoice> invPurchaseInvoices;

    public InvPurchaseInvoiceConverter(List<InvPurchaseInvoice> invPurchaseInvoices) {
        this.invPurchaseInvoices = invPurchaseInvoices;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (InvPurchaseInvoice purchaseInvoice : invPurchaseInvoices) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && purchaseInvoice.getId().longValue() == Long.parseLong(value)) {
                return purchaseInvoice;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((InvPurchaseInvoice) value).getId() != null && ((InvPurchaseInvoice) value).getId().longValue() != 0) {
                    InvPurchaseInvoice codegenerator = (InvPurchaseInvoice) value;
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
