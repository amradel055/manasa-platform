/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;

import com.toby.dto.TobyUserDTO;
import com.toby.dto.TobyUserInventoryDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author Ahmed Saleh
 */
public class TobyUserInventoryDTOConverter implements Converter{
  private List<TobyUserInventoryDTO> tobyUserInventoryDTOList;

    public TobyUserInventoryDTOConverter(List<TobyUserInventoryDTO> tobyUserInventoryDTOList) {
        this.tobyUserInventoryDTOList = tobyUserInventoryDTOList;
    }


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (TobyUserInventoryDTO item : tobyUserInventoryDTOList) {
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
                if (((TobyUserInventoryDTO) value).getId()!= null && ((TobyUserInventoryDTO) value).getId().longValue() != 0) {
                    TobyUserInventoryDTO codegenerator = (TobyUserInventoryDTO) value;
                    return codegenerator.getId().longValue() +"" ;
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
