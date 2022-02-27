/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;

import com.toby.dto.InvPurchaseInvoiceDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author m_els
 */
public class InvPurchaseInvoiceDTOConverter implements Converter {

    private List<InvPurchaseInvoiceDTO> invPurchaseInvoiceDTOList;

    public InvPurchaseInvoiceDTOConverter(List<InvPurchaseInvoiceDTO> invPurchaseInvoiceDTOList) {
        this.invPurchaseInvoiceDTOList = invPurchaseInvoiceDTOList;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (InvPurchaseInvoiceDTO purchaseInvoice : invPurchaseInvoiceDTOList) {
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
                if (((InvPurchaseInvoiceDTO) value).getId() != null && ((InvPurchaseInvoiceDTO) value).getId().longValue() != 0) {
                    InvPurchaseInvoiceDTO codegenerator = (InvPurchaseInvoiceDTO) value;
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
