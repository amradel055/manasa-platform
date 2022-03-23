/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.entity.InstProjectGuide;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author ahmed
 */
public class InstProjectGuideConverter implements Converter {

    private List<InstProjectGuide> instProjectGuides;

    public InstProjectGuideConverter(List<InstProjectGuide> instProjectGuides) {
        this.instProjectGuides = instProjectGuides;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (instProjectGuides != null && !instProjectGuides.isEmpty()) {
            for (InstProjectGuide projectGuide : instProjectGuides) {
                try {
                    Long.parseLong(value);
                } catch (Exception e) {
                    return null;
                }
                if (!value.equals("") && !value.equals(" ") && projectGuide.getId().longValue() == Long.parseLong(value)) {
                    return projectGuide;
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((InstProjectGuide) value).getId() != null && ((InstProjectGuide) value).getId().longValue() != 0) {
                    InstProjectGuide codegenerator = (InstProjectGuide) value;
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
