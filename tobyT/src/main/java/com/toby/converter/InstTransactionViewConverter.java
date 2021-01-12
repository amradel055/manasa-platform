/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;

import com.toby.views.InstTransactionView;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author ahmed
 */
public class InstTransactionViewConverter implements Converter {

    private List<InstTransactionView> instTransactionViews;

    public InstTransactionViewConverter(List<InstTransactionView> instTransactionViews) {
        this.instTransactionViews = instTransactionViews;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (instTransactionViews != null && !instTransactionViews.isEmpty()) {
            for (InstTransactionView transactionView : instTransactionViews) {
                try {
                    Long.parseLong(value);
                } catch (Exception e) {
                    return null;
                }
                if (!value.equals("") && !value.equals(" ") && transactionView.getRowNum().longValue() == Long.parseLong(value)) {
                    return transactionView;
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((InstTransactionView) value).getRowNum() != null && ((InstTransactionView) value).getRowNum().longValue() != 0) {
                    InstTransactionView codegenerator = (InstTransactionView) value;
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
