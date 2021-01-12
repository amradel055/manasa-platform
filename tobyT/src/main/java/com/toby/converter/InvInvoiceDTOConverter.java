/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;

import com.toby.dto.InvInvoiceDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author m_els
 */
public class InvInvoiceDTOConverter implements Converter {

    private List<InvInvoiceDTO> invInvoiceDTOList;

    public InvInvoiceDTOConverter(List<InvInvoiceDTO> invInvoiceDTOList) {
        this.invInvoiceDTOList = invInvoiceDTOList;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (InvInvoiceDTO purchaseInvoice : invInvoiceDTOList) {
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
                if (((InvInvoiceDTO) value).getId() != null && ((InvInvoiceDTO) value).getId().longValue() != 0) {
                    InvInvoiceDTO codegenerator = (InvInvoiceDTO) value;
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
