/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;

import com.toby.entity.InvGroup;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author WIN7
 */
public class InvGroupConverter implements Converter {

    private List<InvGroup> invGroub;

    public InvGroupConverter(List<InvGroup> invGroups) {
        this.invGroub = invGroups;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (InvGroup item : invGroub) {
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
                if (((InvGroup) value).getId() != null && ((InvGroup) value).getId().longValue() != 0) {
                    InvGroup codegenerator = (InvGroup) value;
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
