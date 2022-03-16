/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;

import com.toby.dto.GlYearDTO; 
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author AhmedEssam
 */
public class GlYearDTOConverter implements Converter {

    private List<GlYearDTO> glyear;

    public GlYearDTOConverter(List<GlYearDTO> glyear) {
        this.glyear = glyear;
    }


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (GlYearDTO year : glyear) {
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
                if (((GlYearDTO) value).getId()!= null && ((GlYearDTO) value).getId().longValue() != 0) {
                    GlYearDTO codegenerator = (GlYearDTO) value;
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


