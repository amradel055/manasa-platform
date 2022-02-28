/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.dto.IgmaScreenDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author M.atallah
 */
public class IsagScreenDTOConverter implements Converter {

    private List<IgmaScreenDTO> isagScreen;

    public IsagScreenDTOConverter(List<IgmaScreenDTO> isagScreen) {
        this.isagScreen = isagScreen;
    }


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (IgmaScreenDTO item : isagScreen) {
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
                if (((IgmaScreenDTO) value).getId()!= null && ((IgmaScreenDTO) value).getId().longValue() != 0) {
                    IgmaScreenDTO codegenerator = (IgmaScreenDTO) value;
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


