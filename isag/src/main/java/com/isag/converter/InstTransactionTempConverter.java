/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.converter;

import com.isag.views.InstTransactionTemp;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author AhmedEssam
 */
public class InstTransactionTempConverter  implements Converter{
    
      private List<InstTransactionTemp> instTransactionTemp;

    public InstTransactionTempConverter(List<InstTransactionTemp> instTransactionTemp) {
        this.instTransactionTemp = instTransactionTemp;
    }
 @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (instTransactionTemp != null && !instTransactionTemp.isEmpty()) {
            for (InstTransactionTemp transactionView : instTransactionTemp) {
                try {
                    Long.parseLong(value);
                } catch (Exception e) {
                    return null;
                }
                if (!value.equals("") && !value.equals(" ") && transactionView.getRowNum().longValue() == Long.parseLong(value)) {
                    return transactionView;
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((InstTransactionTemp) value).getRowNum()!= null && ((InstTransactionTemp) value).getRowNum().longValue() != 0) {
                    InstTransactionTemp codegenerator = (InstTransactionTemp) value;
                    return codegenerator.getRowNum().longValue() + "";
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
