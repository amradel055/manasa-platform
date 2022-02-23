/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.entity.IsagPrivilege;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author ahmed
 */
public class IsagPrivilegeConverter implements Converter {
    
    
       private List<IsagPrivilege> isagPrivileges;

    public IsagPrivilegeConverter(List<IsagPrivilege> isagPrivileges) {
        this.isagPrivileges = isagPrivileges;
    }


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (IsagPrivilege isagPrivilege : isagPrivileges) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && isagPrivilege.getId().longValue() == Long.parseLong(value)) {
                return isagPrivilege;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((IsagPrivilege) value).getId()!= null && ((IsagPrivilege) value).getId().longValue() != 0) {
                    IsagPrivilege codegenerator = (IsagPrivilege) value;
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
