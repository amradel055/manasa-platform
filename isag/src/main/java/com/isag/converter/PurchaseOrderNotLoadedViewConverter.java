/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.views.PurchaseOrderNotLoadedFromInvAddingOrder;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author m_els
 */
public class PurchaseOrderNotLoadedViewConverter implements Converter {

    private List<PurchaseOrderNotLoadedFromInvAddingOrder> PurchaseOrderNLoadViewList;

    public PurchaseOrderNotLoadedViewConverter(List<PurchaseOrderNotLoadedFromInvAddingOrder> PurchaseOrderNotLoadedViews) {
        this.PurchaseOrderNLoadViewList = PurchaseOrderNotLoadedViews;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (PurchaseOrderNotLoadedFromInvAddingOrder detailsView : PurchaseOrderNLoadViewList) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && detailsView.getRowNum().longValue() == Long.parseLong(value)) {
                return detailsView;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((PurchaseOrderNotLoadedFromInvAddingOrder) value).getRowNum() != null && ((PurchaseOrderNotLoadedFromInvAddingOrder) value).getRowNum().longValue() != 0) {
                    PurchaseOrderNotLoadedFromInvAddingOrder codegenerator = (PurchaseOrderNotLoadedFromInvAddingOrder) value;
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
