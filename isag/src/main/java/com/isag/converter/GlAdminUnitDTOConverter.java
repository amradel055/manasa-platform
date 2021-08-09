/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.dto.GlAdminUnitDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author m_els
 */
public class GlAdminUnitDTOConverter implements Converter {

    private List<GlAdminUnitDTO> adminUnits;

    public GlAdminUnitDTOConverter(List<GlAdminUnitDTO> adminUnits) {
        this.adminUnits = adminUnits;
    }


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (GlAdminUnitDTO gladminunit : adminUnits) {
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
                if (((GlAdminUnitDTO) value).getId()!= null && ((GlAdminUnitDTO) value).getId().longValue() != 0) {
                    GlAdminUnitDTO codegenerator = (GlAdminUnitDTO) value;
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