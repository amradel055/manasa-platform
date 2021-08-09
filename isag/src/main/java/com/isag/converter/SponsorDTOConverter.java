/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;


import com.isag.dto.HrSponsorDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author M.atallah
 */
public class SponsorDTOConverter implements Converter {

    private List<HrSponsorDTO> hrSponsorDTOList ;

    public SponsorDTOConverter(List<HrSponsorDTO> hrSponsorDTOList) {
        this.hrSponsorDTOList = hrSponsorDTOList;
    }


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (HrSponsorDTO item : hrSponsorDTOList) {
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
                if (((HrSponsorDTO) value).getId()!= null && ((HrSponsorDTO) value).getId().longValue() != 0) {
                    HrSponsorDTO codegenerator = (HrSponsorDTO) value;
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


