/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;

import com.toby.views.QuantityItemsStore;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author m_els
 */
public class QuantityItemsStoreConverter implements Converter {

    private List<QuantityItemsStore> quantityItemsStoreList;

    public QuantityItemsStoreConverter(List<QuantityItemsStore> currencies) {
        this.quantityItemsStoreList = currencies;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (QuantityItemsStore itemsStore : quantityItemsStoreList) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && itemsStore.getRowNum().longValue() == Long.parseLong(value)) {
                return itemsStore;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((QuantityItemsStore) value).getRowNum() != null && ((QuantityItemsStore) value).getRowNum().longValue() != 0) {
                    QuantityItemsStore codegenerator = (QuantityItemsStore) value;
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
