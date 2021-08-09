/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.entity.InstContract;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author ahmed
 */
public class InstContractConverter implements Converter {

    private List<InstContract> instContracts;

    public InstContractConverter(List<InstContract> instContracts) {
        this.instContracts = instContracts;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (instContracts != null && !instContracts.isEmpty()) {
            for (InstContract instContract : instContracts) {
                try {
                    Long.parseLong(value);
                } catch (Exception e) {
                    return null;
                }
                if (!value.equals("") && !value.equals(" ") && instContract.getId().longValue() == Long.parseLong(value)) {
                    return instContract;
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((InstContract) value).getId() != null && ((InstContract) value).getId().longValue() != 0) {
                    InstContract codegenerator = (InstContract) value;
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
