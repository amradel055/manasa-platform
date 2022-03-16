/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;

import com.toby.entity.UnitsItems;
import com.toby.views.QuantityItemsStore;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author H
 */
public class UnitsItemsConverter implements Converter {

    
    
    
    private List<UnitsItems>  unitsItemses;

    public UnitsItemsConverter(List<UnitsItems> unitsItemses) {
        this.unitsItemses = unitsItemses;
    }


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (UnitsItems item : unitsItemses) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && item.getRowNum().longValue() == Long.parseLong(value)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((UnitsItems) value).getRowNum()!= null && ((UnitsItems) value).getRowNum().longValue() != 0) {
                    UnitsItems codegenerator = (UnitsItems) value;
                    return codegenerator.getRowNum().longValue() + "";
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


