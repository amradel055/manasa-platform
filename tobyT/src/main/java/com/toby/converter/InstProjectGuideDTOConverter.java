/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;

import com.toby.dto.InstProjectGuideDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author AhmedEssam
 */
public class InstProjectGuideDTOConverter implements Converter{
      private List<InstProjectGuideDTO> instProjectGuideDTOList;

    public InstProjectGuideDTOConverter(List<InstProjectGuideDTO> instProjectGuideDTOList) {
        this.instProjectGuideDTOList = instProjectGuideDTOList;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (instProjectGuideDTOList != null && !instProjectGuideDTOList.isEmpty()) {
            for (InstProjectGuideDTO projectGuide : instProjectGuideDTOList) {
                try {
                    Long.parseLong(value);
                } catch (Exception e) {
                    return null;
                }
                if (!value.equals("") && !value.equals(" ") && projectGuide.getId().longValue() == Long.parseLong(value)) {
                    return projectGuide;
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((InstProjectGuideDTO) value).getId() != null && ((InstProjectGuideDTO) value).getId().longValue() != 0) {
                    InstProjectGuideDTO codegenerator = (InstProjectGuideDTO) value;
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
