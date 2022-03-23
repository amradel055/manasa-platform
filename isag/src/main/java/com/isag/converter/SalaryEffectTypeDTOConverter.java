/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.dto.HrSalaryEffectDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
public class SalaryEffectTypeDTOConverter implements Converter {

    private List<HrSalaryEffectDTO> hrSalaryEffectTypeDTOList;

    public SalaryEffectTypeDTOConverter(List<HrSalaryEffectDTO> hrSalaryEffectTypeDTOList) {
        this.hrSalaryEffectTypeDTOList = hrSalaryEffectTypeDTOList;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (HrSalaryEffectDTO item : hrSalaryEffectTypeDTOList) {
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
                if (((HrSalaryEffectDTO) value).getId() != null && ((HrSalaryEffectDTO) value).getId().longValue() != 0) {
                    HrSalaryEffectDTO codegenerator = (HrSalaryEffectDTO) value;
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
