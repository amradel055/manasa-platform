/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.views.ItemsBarcodesDetailsView;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author m_els
 */
public class ItemsBarcodesDetailsViewConverter implements Converter {

    private List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList;

    public ItemsBarcodesDetailsViewConverter(List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViews) {
        this.itemsBarcodesDetailsViewList = itemsBarcodesDetailsViews;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (ItemsBarcodesDetailsView detailsView : itemsBarcodesDetailsViewList) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && detailsView.getId().longValue() == Long.parseLong(value)) {
                return detailsView;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((ItemsBarcodesDetailsView) value).getId() != null && ((ItemsBarcodesDetailsView) value).getId().longValue() != 0) {
                    ItemsBarcodesDetailsView codegenerator = (ItemsBarcodesDetailsView) value;
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
