/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.entity.InstContractDetail;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author ahmed
 */
public class InstContractDetailConverter implements Converter {

    private List<InstContractDetail> instContractDetails;

    public InstContractDetailConverter(List<InstContractDetail> instContractDetails) {
        this.instContractDetails = instContractDetails;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (instContractDetails != null && !instContractDetails.isEmpty()) {
            for (InstContractDetail instContractDetail : instContractDetails) {
                try {
                    Long.parseLong(value);
                } catch (Exception e) {
                    return null;
                }
                if (!value.equals("") && !value.equals(" ") && instContractDetail.getId().longValue() == Long.parseLong(value)) {
                    return instContractDetail;
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((InstContractDetail) value).getId() != null && ((InstContractDetail) value).getId().longValue() != 0) {
                    InstContractDetail codegenerator = (InstContractDetail) value;
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
