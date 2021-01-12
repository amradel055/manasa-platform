/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.proProductmovmentReport;

import com.toby.businessservice.ProProductionTransactionOfInvoiceService;
import com.toby.dto.InvPurchaseInvoiceDTO1;
import com.toby.dto.PrintProductionDTO;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import com.toby.views.ProProductionTransactionOfInvoice;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author Toby
 */
@Named(value = "productionOfInvoiceReportMB")
@ViewScoped
public class ProductionOfInvoiceReportMB extends BaseFormBean{
   
    
      private PrintProductionDTO productionDTO;
    private List<ProProductionTransactionOfInvoice> proProductionTransactionOfInvoiceList;
    @EJB
    private  ProProductionTransactionOfInvoiceService proProductionTransactionOfInvoiceService;
    
     
     
     
     
     
     public void search(){
      if(productionDTO.getFromInvoice()!=null){
             
            proProductionTransactionOfInvoiceList= proProductionTransactionOfInvoiceService.getProProductionTransactionOfInvoiceListOfPourchais(productionDTO.getFromInvoice().getSerial(), getUserData().getUser());
     }else{
        productionDTO.setMsg("ادخل الفاتوره " );
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", productionDTO.getMsg()));
        
      
      } 
     }
     
     
       public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (proProductionTransactionOfInvoiceList != null && !proProductionTransactionOfInvoiceList.isEmpty()) {
                fillReport(prepareReport(), getUserData().getReportPath() + "productionOfInvoiceReport.jasper", proProductionTransactionOfInvoiceList, "pdf");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا توجد نتائج للطباعة"));
        }
    }
    

    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();
        
        hashMap.put("formInvoice",productionDTO.getFromInvoice().getId());
      
        hashMap.put("fromInvoiceText", "الفاتوره");
        hashMap.put("dateText", "التاريخ");
        hashMap.put("productionStagesIdText", "المرحله");
        hashMap.put("userNameText", "الموظف ");
        hashMap.put("itemIdText", "الصنف");
        hashMap.put("itemNameText", "اسم الصنف");
        hashMap.put("numberExcuteText", "الكميه المنفذه");
          hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        hashMap.put("PrintedBy", getUserData().getUser().getName());
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        return hashMap;
    }
     
     
    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @PostConstruct
    @Override
    public void init() {
  ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData")); 
    
    
    
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

  
    public PrintProductionDTO getProductionDTO() {
        if(productionDTO ==null){
            productionDTO =new PrintProductionDTO();
//            productionDTO.setFromInvoice(new InvPurchaseInvoiceDTO1());
        
        }
        return productionDTO;
    }

    /**
     * @param productionDTO the productionDTO to set
     */
    public void setProductionDTO(PrintProductionDTO productionDTO) {
        this.productionDTO = productionDTO;
    }

    /**
     * @return the proProductionTransactionOfInvoiceList
     */
    public List<ProProductionTransactionOfInvoice> getProProductionTransactionOfInvoiceList() {
        
        
     
        
        return proProductionTransactionOfInvoiceList;
    }

    /**
     * @param proProductionTransactionOfInvoiceList the proProductionTransactionOfInvoiceList to set
     */
    public void setProProductionTransactionOfInvoiceList(List<ProProductionTransactionOfInvoice> proProductionTransactionOfInvoiceList) {
        this.proProductionTransactionOfInvoiceList = proProductionTransactionOfInvoiceList;
    }

}
