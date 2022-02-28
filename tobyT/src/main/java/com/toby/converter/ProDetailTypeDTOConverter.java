/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;

import com.toby.dto.ProDetailTypeDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author M.atallah
 */
public class ProDetailTypeDTOConverter implements Converter {

    private List<ProDetailTypeDTO> list;

    public ProDetailTypeDTOConverter(List<ProDetailTypeDTO> list) {
        this.list = list;
    }


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (ProDetailTypeDTO item : list) {
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
                if (((ProDetailTypeDTO) value).getId()!= null && ((ProDetailTypeDTO) value).getId().longValue() != 0) {
                    ProDetailTypeDTO codegenerator = (ProDetailTypeDTO) value;
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


