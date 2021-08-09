/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.dto.RemTrafficDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author M.atallah
 */
public class RemTrafficDTOConverter implements Converter {

    private List<RemTrafficDTO> remTrafficDTO;

    public RemTrafficDTOConverter(List<RemTrafficDTO> remTrafficDTO) {
        this.remTrafficDTO = remTrafficDTO;
    }


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (RemTrafficDTO item : remTrafficDTO) {
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
                if (((RemTrafficDTO) value).getId()!= null && ((RemTrafficDTO) value).getId().longValue() != 0) {
                    RemTrafficDTO codegenerator = (RemTrafficDTO) value;
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


