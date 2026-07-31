/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.entity.IgmaUser;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
public class IgmaUserConverter implements Converter {

    private List<IgmaUser> igmaUsers;

    public IgmaUserConverter(List<IgmaUser> all) {
        this.igmaUsers = all;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Integer igmaUserId;
        for (IgmaUser igmaUser : igmaUsers) {
            try {
                if (value != null && !value.isEmpty()) {
                    igmaUserId = Integer.valueOf(value);
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
                if (((IgmaUser) value).getId() != null && ((IgmaUser) value).getId().longValue() != 0) {
                    IgmaUser codegenerator = (IgmaUser) value;
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
