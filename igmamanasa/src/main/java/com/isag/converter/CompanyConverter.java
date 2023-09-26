/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.entity.IgmaCompany;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
public class CompanyConverter implements Converter {

    private List<IgmaCompany> companies;

    public CompanyConverter(List<IgmaCompany> all) {
        this.companies = all;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Integer companyId;

        for (IgmaCompany igmaUser : companies) {
            try {
                if (value != null && !value.isEmpty()) {
                    companyId = Integer.valueOf(value);
                }
            } catch (NumberFormatException e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && igmaUser.getId().longValue() == Long.parseLong(value)) {
                return igmaUser;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {
            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((IgmaCompany) value).getId() != null && ((IgmaCompany) value).getId().longValue() != 0) {
                    IgmaCompany codegenerator = (IgmaCompany) value;
                    return codegenerator.getId().longValue() + "";
                } else {
                    return "";
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
