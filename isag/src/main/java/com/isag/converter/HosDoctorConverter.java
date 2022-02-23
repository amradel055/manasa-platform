/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.entity.HosDoctor;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author ahmed
 */
public class HosDoctorConverter implements Converter {

    private List<HosDoctor> hosDoctors;

    public HosDoctorConverter(List<HosDoctor> HosDoctors) {
        this.hosDoctors = HosDoctors;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (hosDoctors != null && !hosDoctors.isEmpty()) {
            for (HosDoctor hosDoctor : hosDoctors) {
                try {
                    Long.parseLong(value);
                } catch (Exception e) {
                    return null;
                }
                if (!value.equals("") && !value.equals(" ") && hosDoctor.getId().longValue() == Long.parseLong(value)) {
                    return hosDoctor;
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((HosDoctor) value).getId() != null && ((HosDoctor) value).getId().longValue() != 0) {
                    HosDoctor codegenerator = (HosDoctor) value;
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
