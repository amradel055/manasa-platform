package com.toby.converter;

import com.toby.dto.ProProductMovementDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class ProProductMovementDTOConvertor  implements Converter{
    private List<ProProductMovementDTO> proProductMovementDTOs;
    public ProProductMovementDTOConvertor(List<ProProductMovementDTO> item) {
        this.proProductMovementDTOs = item;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
         for (ProProductMovementDTO item : proProductMovementDTOs) {
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
                if (((ProProductMovementDTO) value).getId() != null && ((ProProductMovementDTO) value).getId().longValue() != 0) {
                    ProProductMovementDTO codegenerator = (ProProductMovementDTO) value;
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
