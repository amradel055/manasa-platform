/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.entity.InvReturnPurchase;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author m_els
 */
public class InvReturnPurchaseConverter implements Converter {

    private List<InvReturnPurchase> invReturnPurchases;

    public InvReturnPurchaseConverter(List<InvReturnPurchase> invPurchaseInvoices) {
        this.invReturnPurchases = invPurchaseInvoices;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (InvReturnPurchase invReturnPurchase : invReturnPurchases) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && invReturnPurchase.getId().longValue() == Long.parseLong(value)) {
                return invReturnPurchase;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((InvReturnPurchase) value).getId() != null && ((InvReturnPurchase) value).getId().longValue() != 0) {
                    InvReturnPurchase codegenerator = (InvReturnPurchase) value;
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
