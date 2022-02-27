/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;

import com.toby.businessservice.CompanyService;
import com.toby.entity.TobyCompany;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author hq003
 */
public class CompanyConverter implements Converter {

    @EJB
    private CompanyService companyService;
    private List<TobyCompany> companies;

    public CompanyConverter(List<TobyCompany> all) {
        this.companies = all;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Integer companyId;
        for (TobyCompany company : companies) {
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
            if (value != null && (value instanceof TobyCompany)) {
                TobyCompany company = (TobyCompany) value;
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
