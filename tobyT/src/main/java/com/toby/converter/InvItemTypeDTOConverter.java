/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;

import com.toby.dto.InvItemTypeDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author H
 */
public class InvItemTypeDTOConverter implements Converter{
    
    
    private List<InvItemTypeDTO> invItemTypeDTOList;

    public InvItemTypeDTOConverter(List<InvItemTypeDTO> invItemTypeDTOList) {
        this.invItemTypeDTOList = invItemTypeDTOList;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (InvItemTypeDTO invItemTypeDTO : invItemTypeDTOList) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && invItemTypeDTO.getId().longValue() == Long.parseLong(value)) {
                return invItemTypeDTO;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((InvItemTypeDTO) value).getId() != null && ((InvItemTypeDTO) value).getId().longValue() != 0) {
                    InvItemTypeDTO codegenerator = (InvItemTypeDTO) value;
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

