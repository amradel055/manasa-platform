/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.entity.GeneralBudgetGuide;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author m_els
 */
public class GeneralBudgetGuideConvertor implements Converter {

    private List<GeneralBudgetGuide> generalBudgetGuides;

    public GeneralBudgetGuideConvertor(List<GeneralBudgetGuide> generalBudgetGuides) {
        this.generalBudgetGuides = generalBudgetGuides;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (GeneralBudgetGuide generalBudgetGuide : generalBudgetGuides) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && generalBudgetGuide.getId().longValue() == Long.parseLong(value)) {
                return generalBudgetGuide;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((GeneralBudgetGuide) value).getId() != null && ((GeneralBudgetGuide) value).getId().longValue() != 0) {
                    GeneralBudgetGuide codegenerator = (GeneralBudgetGuide) value;
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
