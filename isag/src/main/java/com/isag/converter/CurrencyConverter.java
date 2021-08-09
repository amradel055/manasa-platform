/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.entity.Currency;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author m_els
 */
public class CurrencyConverter implements Converter {

    private List<Currency> currencies;

    public CurrencyConverter(List<Currency> currencies) {
        this.currencies = currencies;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (Currency currency : currencies) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && currency.getId().longValue() == Long.parseLong(value)) {
                return currency;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((Currency) value).getId() != null && ((Currency) value).getId().longValue() != 0) {
                    Currency codegenerator = (Currency) value;
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