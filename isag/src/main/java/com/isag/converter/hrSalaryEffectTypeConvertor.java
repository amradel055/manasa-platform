/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.dto.HrSalaryEffectTypeDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author AhmedEssam
 */
public class hrSalaryEffectTypeConvertor implements Converter{
       private List<HrSalaryEffectTypeDTO> hrSalaryEffectTypeDTOList;

    public hrSalaryEffectTypeConvertor(List<HrSalaryEffectTypeDTO> hrSalaryEffectTypeDTOList) {
        this.hrSalaryEffectTypeDTOList = hrSalaryEffectTypeDTOList;
    }


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (HrSalaryEffectTypeDTO hrSalaryEffectTypeDTO : hrSalaryEffectTypeDTOList) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && hrSalaryEffectTypeDTO.getId().longValue() == Long.parseLong(value)) {
                return hrSalaryEffectTypeDTO;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((HrSalaryEffectTypeDTO) value).getId()!= null && ((HrSalaryEffectTypeDTO) value).getId().longValue() != 0) {
                    HrSalaryEffectTypeDTO codegenerator = (HrSalaryEffectTypeDTO) value;
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
