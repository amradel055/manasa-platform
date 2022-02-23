/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.entity.CostCenter;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author m_els
 */
public class CostCenterConverter implements Converter {

    private List<CostCenter> costCenters;

    public CostCenterConverter(List<CostCenter> costCenters) {
        this.costCenters = costCenters;
    }


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (CostCenter costCenter : costCenters) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && costCenter.getId().longValue() == Long.parseLong(value)) {
                return costCenter;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((CostCenter) value).getId()!= null && ((CostCenter) value).getId().longValue() != 0) {
                    CostCenter codegenerator = (CostCenter) value;
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