/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.dto.RscQotationPriceTenderDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author ahmed
 */
public class RscQotationTenderDTOConverter implements Converter {

    private List<RscQotationPriceTenderDTO> tenderDto;

    public RscQotationTenderDTOConverter(List<RscQotationPriceTenderDTO> tender) {
        this.tenderDto = tender;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (tenderDto != null && !tenderDto.isEmpty()) {
            for (RscQotationPriceTenderDTO tenderDTO : tenderDto) {
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
                if (((RscQotationPriceTenderDTO) value).getId() != null && ((RscQotationPriceTenderDTO) value).getId().longValue() != 0) {
                    RscQotationPriceTenderDTO codegenerator = (RscQotationPriceTenderDTO) value;
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
