package com.toby.converter;

import com.toby.dto.ProItemProductionStagesDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;


public class ProItemProductionStageDTOConverter implements Converter{
    
    private List<ProItemProductionStagesDTO> item;
    
    public ProItemProductionStageDTOConverter(List<ProItemProductionStagesDTO> item) {
        this.item = item;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (ProItemProductionStagesDTO item : item) {
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
                if (((ProItemProductionStagesDTO) value).getId() != null && ((ProItemProductionStagesDTO) value).getId().longValue() != 0) {
                    ProItemProductionStagesDTO codegenerator = (ProItemProductionStagesDTO) value;
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
