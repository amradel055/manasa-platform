/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.gmsubscriptions;

import com.isag.businessservice.InvPurchaseInvoiceService_1;
import com.isag.isag.BaseFormBean;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author WIN7
 */
@Named(value = "gmFormDTOMB")
@ViewScoped
public class GmFormDTOMB extends BaseFormBean {
    
    private Boolean checkDiscountValue = false ;
    private Boolean checkDiscountRate = false ;
    // EJBs
    @EJB
    private InvPurchaseInvoiceService_1 invPurchaseInvoiceService;
    private BigDecimal remain;

    @Override
    @PostConstruct
    public void init() {
        try {
           
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "init");
        }
    }

    @Override
    public void load() {
        try {

            
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceFormMB", "load");
        }
    }
    
   

    @Override
    public String save() {
      
        return "";
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        try {
            

                fillReport(new HashMap(), getUserData().getReportPath() + "salesReceipGM.jasper", new ArrayList<>(), "pdf");
        } catch (Exception e) {
            saveError(e, "SettlementDeedFormMB", "exportPDF");
        }
    }


}
