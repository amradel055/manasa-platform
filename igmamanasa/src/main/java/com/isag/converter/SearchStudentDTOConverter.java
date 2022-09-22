/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.api.dto.SearchStudentDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
public class SearchStudentDTOConverter implements Converter {

    private List<SearchStudentDTO> searchStudentDTOList;

    public SearchStudentDTOConverter(List<SearchStudentDTO> searchStudentDTOList) {
        this.searchStudentDTOList = searchStudentDTOList;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (SearchStudentDTO item : searchStudentDTOList) {
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
                if (((SearchStudentDTO) value).getId() != null && ((SearchStudentDTO) value).getId().longValue() != 0) {
                    SearchStudentDTO codegenerator = (SearchStudentDTO) value;
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
