/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.entity.InvOrganizationSite;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author M.atallah
 */
public class InvOrganizationSiteConverter implements Converter {

    private List<InvOrganizationSite>  invOrganizationSite;

    public InvOrganizationSiteConverter(List<InvOrganizationSite> invOrganizationSite) {
        this.invOrganizationSite = invOrganizationSite;
    }


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (InvOrganizationSite item : invOrganizationSite) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && item.getId().longValue() == Long.parseLong(value)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((InvOrganizationSite) value).getId()!= null && ((InvOrganizationSite) value).getId().longValue() != 0) {
                    InvOrganizationSite codegenerator = (InvOrganizationSite) value;
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


