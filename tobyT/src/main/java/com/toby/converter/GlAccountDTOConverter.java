/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;

import com.toby.dto.GlAccountDTO;
import com.toby.entity.GlAccount;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author Toby
 */
public class GlAccountDTOConverter implements Converter{
     private List<GlAccountDTO> glAccountDTOList;

    public GlAccountDTOConverter(List<GlAccountDTO> glAccountDTOList) {
        this.glAccountDTOList = glAccountDTOList;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (GlAccountDTO glAccountDTO : glAccountDTOList) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && glAccountDTO.getId().longValue() == Long.parseLong(value)) {
                return glAccountDTO;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((GlAccountDTO) value).getId() != null && ((GlAccountDTO) value).getId().longValue() != 0) {
                    GlAccountDTO codegenerator = (GlAccountDTO) value;
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
