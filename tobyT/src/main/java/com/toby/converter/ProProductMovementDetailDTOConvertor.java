package com.toby.converter;

import com.toby.dto.ProProductMovementDetailDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;


public class ProProductMovementDetailDTOConvertor   implements Converter{
      private List<ProProductMovementDetailDTO> proProductMovementDetailDTOs ;
    public ProProductMovementDetailDTOConvertor(List<ProProductMovementDetailDTO> item) {
        this.proProductMovementDetailDTOs = item;
    }


    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
         for (ProProductMovementDetailDTO item : proProductMovementDetailDTOs) {
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
                if (((ProProductMovementDetailDTO) value).getId() != null && ((ProProductMovementDetailDTO) value).getId().longValue() != 0) {
                    ProProductMovementDetailDTO codegenerator = (ProProductMovementDetailDTO) value;
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
