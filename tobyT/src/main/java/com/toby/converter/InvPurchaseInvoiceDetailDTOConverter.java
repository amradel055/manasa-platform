/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;

import com.toby.dto.InvPurchaseInvoiceDetailDTO;
import com.toby.entity.InvPurchaseInvoiceDetail;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author m_els
 */
public class InvPurchaseInvoiceDetailDTOConverter implements Converter {

    private List<InvPurchaseInvoiceDetailDTO> invPurchaseInvoiceDetailDTOList;

    public InvPurchaseInvoiceDetailDTOConverter(List<InvPurchaseInvoiceDetailDTO> invPurchaseInvoiceDetailDTOList) {
        this.invPurchaseInvoiceDetailDTOList = invPurchaseInvoiceDetailDTOList;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (InvPurchaseInvoiceDetailDTO invPurchaseInvoiceDetailDTO : invPurchaseInvoiceDetailDTOList) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && invPurchaseInvoiceDetailDTO.getId().longValue() == Long.parseLong(value)) {
                return invPurchaseInvoiceDetailDTO;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((InvPurchaseInvoiceDetailDTO) value).getId() != null && ((InvPurchaseInvoiceDetailDTO) value).getId().longValue() != 0) {
                    InvPurchaseInvoiceDetailDTO codegenerator = (InvPurchaseInvoiceDetailDTO) value;
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
