/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.entity.InvPurchaseInvoiceDetail;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author m_els
 */
public class InvPurchaseInvoiceDetailConverter implements Converter {

    private List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetails;

    public InvPurchaseInvoiceDetailConverter(List<InvPurchaseInvoiceDetail> purchaseInvoiceDetails) {
        this.invPurchaseInvoiceDetails = purchaseInvoiceDetails;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (InvPurchaseInvoiceDetail purchaseInvoice : invPurchaseInvoiceDetails) {
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
                if (((InvPurchaseInvoiceDetail) value).getId() != null && ((InvPurchaseInvoiceDetail) value).getId().longValue() != 0) {
                    InvPurchaseInvoiceDetail codegenerator = (InvPurchaseInvoiceDetail) value;
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
