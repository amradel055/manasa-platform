/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;

import com.toby.entity.InvPurchaseOrder;
import com.toby.entity.InvPurchaseOrderDetail;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author m_els
 */
public class InvPurchaseOrderDetailConverter implements Converter {

    private List<InvPurchaseOrderDetail> invPurchaseOrdersDetail;

    public InvPurchaseOrderDetailConverter(List<InvPurchaseOrderDetail> InvPurchaseOrderDetails) {
        this.invPurchaseOrdersDetail = InvPurchaseOrderDetails;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (InvPurchaseOrderDetail purchaseOrderDetail : invPurchaseOrdersDetail) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && purchaseOrderDetail.getId().longValue() == Long.parseLong(value)) {
                return purchaseOrderDetail;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((InvPurchaseOrderDetail) value).getId() != null && ((InvPurchaseOrderDetail) value).getId().longValue() != 0) {
                    InvPurchaseOrderDetail codegenerator = (InvPurchaseOrderDetail) value;
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
