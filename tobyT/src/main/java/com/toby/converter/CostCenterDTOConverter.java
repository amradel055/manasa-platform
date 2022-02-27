/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;

import com.toby.dto.CostCenterDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author m_els
 */
public class CostCenterDTOConverter implements Converter {

    private List<CostCenterDTO> costCenterDTOList;

    public CostCenterDTOConverter(List<CostCenterDTO> costCenterDTOList) {
        this.costCenterDTOList = costCenterDTOList;
    }


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (CostCenterDTO costCenterDTO : costCenterDTOList) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && costCenterDTO.getId().longValue() == Long.parseLong(value)) {
                return costCenterDTO;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((CostCenterDTO) value).getId()!= null && ((CostCenterDTO) value).getId().longValue() != 0) {
                    CostCenterDTO codegenerator = (CostCenterDTO) value;
                    return codegenerator.getId().longValue() +"" ;
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