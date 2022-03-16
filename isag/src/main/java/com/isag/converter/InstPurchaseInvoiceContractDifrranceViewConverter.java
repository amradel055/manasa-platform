/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.views.InstPurchaseInvoiceContractDifrranceView;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author AhmedEssam
 */
public class InstPurchaseInvoiceContractDifrranceViewConverter implements Converter{

    private List<InstPurchaseInvoiceContractDifrranceView> instPurchaseInvoiceContractDifrranceViewList;

    public InstPurchaseInvoiceContractDifrranceViewConverter(List<InstPurchaseInvoiceContractDifrranceView> instPurchaseInvoiceContractDifrranceViewList) {
        this.instPurchaseInvoiceContractDifrranceViewList = instPurchaseInvoiceContractDifrranceViewList;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (InstPurchaseInvoiceContractDifrranceView detailsView : instPurchaseInvoiceContractDifrranceViewList) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && detailsView.getId().longValue() == Long.parseLong(value)) {
                return detailsView;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((InstPurchaseInvoiceContractDifrranceView) value).getId() != null && ((InstPurchaseInvoiceContractDifrranceView) value).getId().longValue() != 0) {
                    InstPurchaseInvoiceContractDifrranceView codegenerator = (InstPurchaseInvoiceContractDifrranceView) value;
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
