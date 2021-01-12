package com.toby.converter;

import com.toby.dto.ProProductionTransactionDTO;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class ProProductionTransactionDTOConverter implements Converter {
    private List<ProProductionTransactionDTO> item;
    public ProProductionTransactionDTOConverter(List<ProProductionTransactionDTO> item) {
        this.item = item;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (ProProductionTransactionDTO item : item) {
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
                if (((ProProductionTransactionDTO) value).getId() != null && ((ProProductionTransactionDTO) value).getId().longValue() != 0) {
                    ProProductionTransactionDTO codegenerator = (ProProductionTransactionDTO) value;
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
