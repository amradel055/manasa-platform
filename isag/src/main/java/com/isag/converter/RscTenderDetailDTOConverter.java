/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.dto.RscTenderDTO;
import com.isag.dto.RscTenderDetailWorkUnitDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author ahmed
 */
public class RscTenderDetailDTOConverter implements Converter {

    private List<RscTenderDetailWorkUnitDTO> tenderDetailDto;

    public RscTenderDetailDTOConverter(List<RscTenderDetailWorkUnitDTO> tender) {
        this.tenderDetailDto = tender;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (tenderDetailDto != null && !tenderDetailDto.isEmpty()) {
            for (RscTenderDetailWorkUnitDTO tenderDTO : tenderDetailDto) {
                try {
                    Long.parseLong(value);
                } catch (Exception e) {
                    return null;
                }
                if (!value.equals("") && !value.equals(" ") && tenderDTO.getId().longValue() == Long.parseLong(value)) {
                    return tenderDTO;
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((RscTenderDetailWorkUnitDTO) value).getId() != null && ((RscTenderDetailWorkUnitDTO) value).getId().longValue() != 0) {
                    RscTenderDetailWorkUnitDTO codegenerator = (RscTenderDetailWorkUnitDTO) value;
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
