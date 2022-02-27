/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;

import com.toby.dto.GlAdminUnitDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author m_els
 */
public class GlAdminUnitDTOConverter implements Converter {

    private List<GlAdminUnitDTO> glAdminUnitDTOList;

    public GlAdminUnitDTOConverter(List<GlAdminUnitDTO> glAdminUnitDTOList) {
        this.glAdminUnitDTOList = glAdminUnitDTOList;
    }


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (GlAdminUnitDTO adminUnitDTO : glAdminUnitDTOList) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && adminUnitDTO.getId().longValue() == Long.parseLong(value)) {
                return adminUnitDTO;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((GlAdminUnitDTO) value).getId()!= null && ((GlAdminUnitDTO) value).getId().longValue() != 0) {
                    GlAdminUnitDTO codegenerator = (GlAdminUnitDTO) value;
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