/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.converter;

import com.toby.dto.InvPurchaseInvoiceDTO1;
import com.toby.views.ProductionTransactionStagesInvoice;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;


/**
 *
 * @author H
 */

    
    
 
public class ProductionTransactionStagesInvoiceConverter implements Converter {

    private List<ProductionTransactionStagesInvoice> productionTransactionStagesInvoiceList;

    public ProductionTransactionStagesInvoiceConverter(List<ProductionTransactionStagesInvoice> productionTransactionStagesInvoiceList) {
        this.productionTransactionStagesInvoiceList = productionTransactionStagesInvoiceList;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        for (ProductionTransactionStagesInvoice productionTransactionStagesInvoice : productionTransactionStagesInvoiceList) {
            try {
                Long.parseLong(value);
            } catch (Exception e) {
                return null;
            }
            if (!value.equals("") && !value.equals(" ") && productionTransactionStagesInvoice.getRowNum().longValue() == Long.parseLong(value)) {
                return productionTransactionStagesInvoice;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {

            if (value != null && !value.equals("") && !value.equals(" ")) {
                if (((ProductionTransactionStagesInvoice) value).getRowNum()!= null && ((ProductionTransactionStagesInvoice) value).getRowNum().longValue() != 0) {
                    ProductionTransactionStagesInvoice codegenerator = (ProductionTransactionStagesInvoice) value;
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

    
