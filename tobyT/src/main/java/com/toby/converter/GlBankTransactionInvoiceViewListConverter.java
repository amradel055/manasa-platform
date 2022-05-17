/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;

import com.toby.views.GlBankTransactionInvoiceView;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author WIN7
 */
public class GlBankTransactionInvoiceViewListConverter implements Converter {

    private List<GlBankTransactionInvoiceView> glBankTransactionInvoiceView;

    public GlBankTransactionInvoiceViewListConverter(List<GlBankTransactionInvoiceView> invGroups) {
        this.glBankTransactionInvoiceView = invGroups;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (GlBankTransactionInvoiceView item : glBankTransactionInvoiceView) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && item.getRowNum().longValue() == Long.parseLong(value)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((GlBankTransactionInvoiceView) value).getRowNum() != null && ((GlBankTransactionInvoiceView) value).getRowNum().longValue() != 0) {
                    GlBankTransactionInvoiceView codegenerator = (GlBankTransactionInvoiceView) value;
                    return codegenerator.getRowNum().longValue() + "";
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
