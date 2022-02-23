/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.entity.RscWorkUnitsGuide;
import com.isag.entity.RscWorkUnitsGuide;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author ahmed
 */
public class RscWorkUnitsGuideConverter implements Converter {

    private List<RscWorkUnitsGuide> workUnitsGuide;

    public RscWorkUnitsGuideConverter(List<RscWorkUnitsGuide> workUnitsGuide) {
        this.workUnitsGuide = workUnitsGuide;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (workUnitsGuide != null && !workUnitsGuide.isEmpty()) {
            for (RscWorkUnitsGuide unitsGuide : workUnitsGuide) {
                try {
                    Long.parseLong(value);
                } catch (Exception e) {
                    return null;
                }
                if (!value.equals("") && !value.equals(" ") && unitsGuide.getId().longValue() == Long.parseLong(value)) {
                    return unitsGuide;
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((RscWorkUnitsGuide) value).getId() != null && ((RscWorkUnitsGuide) value).getId().longValue() != 0) {
                    RscWorkUnitsGuide codegenerator = (RscWorkUnitsGuide) value;
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
