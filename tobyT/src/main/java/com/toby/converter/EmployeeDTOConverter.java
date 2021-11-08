/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;

import com.toby.dto.EmployeeDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
public class EmployeeDTOConverter implements Converter {

    private List<EmployeeDTO> employeeDTOList;

    public EmployeeDTOConverter(List<EmployeeDTO> employeeDTOList) {
        this.employeeDTOList = employeeDTOList;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (EmployeeDTO item : employeeDTOList) {
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
                if (((EmployeeDTO) value).getId() != null && ((EmployeeDTO) value).getId().longValue() != 0) {
                    EmployeeDTO codegenerator = (EmployeeDTO) value;
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
