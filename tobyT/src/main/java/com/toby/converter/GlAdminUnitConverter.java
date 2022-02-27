/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;

import com.toby.entity.GlAdminUnit;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author m_els
 */
public class GlAdminUnitConverter implements Converter {

    private List<GlAdminUnit> adminUnits;

    public GlAdminUnitConverter(List<GlAdminUnit> adminUnits) {
        this.adminUnits = adminUnits;
    }


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (GlAdminUnit gladminunit : adminUnits) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && gladminunit.getId().longValue() == Long.parseLong(value)) {
                return gladminunit;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((GlAdminUnit) value).getId()!= null && ((GlAdminUnit) value).getId().longValue() != 0) {
                    GlAdminUnit codegenerator = (GlAdminUnit) value;
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