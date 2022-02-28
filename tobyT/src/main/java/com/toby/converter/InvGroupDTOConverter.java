/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;

import com.toby.dto.InvGroupDTO;
import com.toby.entity.InvGroup;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author WIN7
 */
public class InvGroupDTOConverter implements Converter {

    private List<InvGroupDTO> invGroub;

    public InvGroupDTOConverter(List<InvGroupDTO> invGroups) {
        this.invGroub = invGroups;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (InvGroupDTO item : invGroub) {
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
                if (((InvGroupDTO) value).getId() != null && ((InvGroupDTO) value).getId().longValue() != 0) {
                    InvGroupDTO codegenerator = (InvGroupDTO) value;
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
