/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;

import com.toby.dto.TobyUserProproductionDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author M.atallah
 */
public class TobyUserProproductionDTOConverter implements Converter {

    private List<TobyUserProproductionDTO> tobyUserProproduction;

    public TobyUserProproductionDTOConverter(List<TobyUserProproductionDTO> users) {
        this.tobyUserProproduction = users;
    }


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (TobyUserProproductionDTO item : tobyUserProproduction) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                e.printStackTrace();
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
                if (((TobyUserProproductionDTO) value).getId()!= null && ((TobyUserProproductionDTO) value).getId().longValue() != 0) {
                    TobyUserProproductionDTO codegenerator = (TobyUserProproductionDTO) value;
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


