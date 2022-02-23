/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.dto.RscAccrualDeductionDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author M.atallah
 */
public class RscAccuralDeductionConverter implements Converter {

    private List<RscAccrualDeductionDTO> rscAccrualDeductionDTOList;

    public RscAccuralDeductionConverter(List<RscAccrualDeductionDTO> accrualDeductionList) {
        this.rscAccrualDeductionDTOList = accrualDeductionList;
    }


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (RscAccrualDeductionDTO rscAccrualDeductionDTO : rscAccrualDeductionDTOList) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && rscAccrualDeductionDTO.getId().longValue() == Long.parseLong(value)) {
                return rscAccrualDeductionDTO;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((RscAccrualDeductionDTO) value).getId()!= null && ((RscAccrualDeductionDTO) value).getId().longValue() != 0) {
                    RscAccrualDeductionDTO codegenerator = (RscAccrualDeductionDTO) value;
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


