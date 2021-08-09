/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.dto.HrSalaryStructureAccuralDeductionEffectTypeDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author AhmedEssam
 */
public class HrSalaryStructureAccuralDeductionEffectTypeConverter implements Converter{
      private List<HrSalaryStructureAccuralDeductionEffectTypeDTO> accuralDeductionEffectTypeDTOList;

    public HrSalaryStructureAccuralDeductionEffectTypeConverter(List<HrSalaryStructureAccuralDeductionEffectTypeDTO> accuralDeductionEffectTypeDTOList) {
        this.accuralDeductionEffectTypeDTOList = accuralDeductionEffectTypeDTOList;
    }


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (HrSalaryStructureAccuralDeductionEffectTypeDTO accuralDeductionEffectTypeDTO : accuralDeductionEffectTypeDTOList) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && accuralDeductionEffectTypeDTO.getId().longValue() == Long.parseLong(value)) {
                return accuralDeductionEffectTypeDTO;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((HrSalaryStructureAccuralDeductionEffectTypeDTO) value).getId()!= null && ((HrSalaryStructureAccuralDeductionEffectTypeDTO) value).getId().longValue() != 0) {
                    HrSalaryStructureAccuralDeductionEffectTypeDTO codegenerator = (HrSalaryStructureAccuralDeductionEffectTypeDTO) value;
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
