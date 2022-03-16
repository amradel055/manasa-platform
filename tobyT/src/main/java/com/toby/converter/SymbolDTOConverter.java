/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;


import com.toby.dto.SymbolDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author M.atallah
 */
public class SymbolDTOConverter implements Converter {

    private List<SymbolDTO> symbolDTOList;

    public SymbolDTOConverter(List<SymbolDTO> symbolDTOList) {
        this.symbolDTOList = symbolDTOList;
    }


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (SymbolDTO item : symbolDTOList) {
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
                if (((SymbolDTO) value).getId()!= null && ((SymbolDTO) value).getId().longValue() != 0) {
                    SymbolDTO codegenerator = (SymbolDTO) value;
                    return codegenerator.getId().longValue() +"" ;
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


