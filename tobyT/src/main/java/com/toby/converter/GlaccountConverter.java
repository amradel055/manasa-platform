/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;

import com.toby.entity.GlAccount;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author m_els
 */
public class GlaccountConverter implements Converter {

    private List<GlAccount> glaccounts;

    public GlaccountConverter(List<GlAccount> glaccounts) {
        this.glaccounts = glaccounts;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (GlAccount glaccount : glaccounts) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && glaccount.getId().longValue() == Long.parseLong(value)) {
                return glaccount;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((GlAccount) value).getId() != null && ((GlAccount) value).getId().longValue() != 0) {
                    GlAccount codegenerator = (GlAccount) value;
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