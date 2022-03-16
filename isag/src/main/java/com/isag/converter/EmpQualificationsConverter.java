/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.dto.HrEmpQualificationsDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
public class EmpQualificationsConverter implements Converter {

    private List<HrEmpQualificationsDTO> hrEmpQualificationsDTOlIST;

    public EmpQualificationsConverter(List<HrEmpQualificationsDTO> hrEmpQualificationsDTOlIST) {
        this.hrEmpQualificationsDTOlIST = hrEmpQualificationsDTOlIST;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (HrEmpQualificationsDTO item : hrEmpQualificationsDTOlIST) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && item.getId().longValue() == Long.parseLong(value)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((HrEmpQualificationsDTO) value).getId() != null && ((HrEmpQualificationsDTO) value).getId().longValue() != 0) {
                    HrEmpQualificationsDTO codegenerator = (HrEmpQualificationsDTO) value;
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
