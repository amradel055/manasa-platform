/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;


import com.isag.dto.RscWorkUnitsGuideDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author M.atallah
 */
public class RscWorkUnitDTOConverter implements Converter {

    private List<RscWorkUnitsGuideDTO> rscWorkUnitsGuideDTOList;

    public RscWorkUnitDTOConverter(List<RscWorkUnitsGuideDTO> guideDTOList) {
        this.rscWorkUnitsGuideDTOList = guideDTOList;
    }


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (RscWorkUnitsGuideDTO rscWorkUnitsGuideDTO : rscWorkUnitsGuideDTOList) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && rscWorkUnitsGuideDTO.getId().longValue() == Long.parseLong(value)) {
                return rscWorkUnitsGuideDTO;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((RscWorkUnitsGuideDTO) value).getId()!= null && ((RscWorkUnitsGuideDTO) value).getId().longValue() != 0) {
                    RscWorkUnitsGuideDTO codegenerator = (RscWorkUnitsGuideDTO) value;
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


