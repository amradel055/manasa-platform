/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.entity.InvTransfer;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author M.atallah
 */
public class InvTransferConverter implements Converter {

    private List<InvTransfer> invTransfer;

    public InvTransferConverter(List<InvTransfer> invTransfer) {
        this.invTransfer = invTransfer;
    }


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (InvTransfer year : invTransfer) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && year.getId().longValue() == Long.parseLong(value)) {
                return year;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((InvTransfer) value).getId()!= null && ((InvTransfer) value).getId().longValue() != 0) {
                    InvTransfer codegenerator = (InvTransfer) value;
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


