/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.entity.HosPatientRegistration;

import com.isag.entity.HosPatientRegistration;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author WIN7
 */
public class HosPatientRegistrationConvertor implements Converter {

    private List<HosPatientRegistration> patientRegistrationList;

    public HosPatientRegistrationConvertor(List<HosPatientRegistration> patientRegistrations) {
        this.patientRegistrationList = patientRegistrations;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (HosPatientRegistration patientRegistration : patientRegistrationList) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && patientRegistration.getId().longValue() == Long.parseLong(value)) {
                return patientRegistration;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((HosPatientRegistration) value).getId() != null && ((HosPatientRegistration) value).getId().longValue() != 0) {
                    HosPatientRegistration codegenerator = (HosPatientRegistration) value;
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
