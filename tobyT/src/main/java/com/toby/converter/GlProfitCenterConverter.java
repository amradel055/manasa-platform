/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;

import com.toby.entity.CostCenter;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author m_els
 */
public class GlProfitCenterConverter implements Converter {

    private List<CostCenter> profitCenters;

    public GlProfitCenterConverter(List<CostCenter> profitCenters) {
        this.profitCenters = profitCenters;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (CostCenter glprofitcenter : profitCenters) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && glprofitcenter.getId().longValue() == Long.parseLong(value)) {
                return glprofitcenter;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((CostCenter) value).getId() != null && ((CostCenter) value).getId().longValue() != 0) {
                    CostCenter codegenerator = (CostCenter) value;
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