/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.entity.IsagRole;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author M.atallah
 */
public class IsagRoleConverter implements Converter {

    private List<IsagRole> role;

    public IsagRoleConverter(List<IsagRole> role) {
        this.role = role;
    }


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (IsagRole item : role) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                e.printStackTrace();
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
                if (((IsagRole) value).getId()!= null && ((IsagRole) value).getId().longValue() != 0) {
                    IsagRole codegenerator = (IsagRole) value;
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


