/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.dto.RemPointsDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author M.atallah
 */
public class RemPointsDTOConverter implements Converter {

    private List<RemPointsDTO> remPointsDTO;

    public RemPointsDTOConverter(List<RemPointsDTO> remPointsDTO) {
        this.remPointsDTO = remPointsDTO;
    }


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (RemPointsDTO item : remPointsDTO) {
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
                if (((RemPointsDTO) value).getId()!= null && ((RemPointsDTO) value).getId().longValue() != 0) {
                    RemPointsDTO codegenerator = (RemPointsDTO) value;
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


