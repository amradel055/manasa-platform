package com.toby.converter;

import com.toby.entity.ProProductionStages;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;


public class ProProductionStagesConverter implements Converter {

    private List<ProProductionStages> item;

    public ProProductionStagesConverter(List<ProProductionStages> item) {
        this.item = item;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
         for (ProProductionStages item : item) {
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
                if (((ProProductionStages) value).getId() != null && ((ProProductionStages) value).getId().longValue() != 0) {
                    ProProductionStages codegenerator = (ProProductionStages) value;
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
