/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.businessservice.CompanyService;
import com.isag.entity.IgmaCompany;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
public class CompanyConverter implements Converter {

    @EJB
    private CompanyService companyService;
    private List<IgmaCompany> companies;

    public CompanyConverter(List<IgmaCompany> all) {
        this.companies = all;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Integer companyId;
        for (IgmaCompany company : companies) {
            try {
                companyId = Integer.parseInt(value);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            if (company.getId().equals(companyId)) {
                return company;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {
            if (value != null && (value instanceof IgmaCompany)) {
                IgmaCompany company = (IgmaCompany) value;
                return company.getId().toString();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
