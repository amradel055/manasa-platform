/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.IsagUserService;
import com.isag.businessservice.reports.InvPurchaseInvoiceService2;
import com.isag.businessservice.reports.searchBean.InvPurchaseInvoiceSearchBean;
import com.isag.converter.InvInvoiceDTOConverter;
import com.isag.converter.IsagUserDTOConverter;
import com.isag.dto.InvInvoiceDTO;
import com.isag.dto.IsagUserDTO;
import com.isag.isag.UserData;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author khale
 */
@Named("invPurchaseInvoiceMB")
@ViewScoped
public class InvPurchaseInvoiceMB implements Serializable {

    @EJB
    private InvPurchaseInvoiceService2 invPurchaseInvoiceService;
    @EJB
    private IsagUserService isagUserService;
    
    private InvPurchaseInvoiceSearchBean invPurchaseInvoiceSearchBean;
    private IsagUserDTOConverter isagUserDTOConverterFrom;
    private IsagUserDTOConverter isagUserDTOConverterTo;

   
    private List<InvInvoiceDTO> purchaseReturn;
    private List<IsagUserDTO> cashierFrom;
    private List<IsagUserDTO> cashierTo;
   

    private UserData userData = new UserData();

    @PostConstruct
    public void load() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));

    }

    public void reset() {
        setPurchaseReturn(null);

    }

    public void search(){
        setPurchaseReturn(invPurchaseInvoiceService.getPurchaseInvoice(getUserData().getUserBranch().getId() , getInvPurchaseInvoiceSearchBean()));

    }

    /**
     * @return the purchaseReturn
     */
    public List<InvInvoiceDTO> getPurchaseReturn() {
        return purchaseReturn;
    }

    /**
     * @param purchaseReturn the purchaseReturn to set
     */
    public void setPurchaseReturn(List<InvInvoiceDTO> purchaseReturn) {
        this.purchaseReturn = purchaseReturn;
    }

    /**
     * @return the userData
     */
    public UserData getUserData() {
        return userData;
    }

    /**
     * @param userData the userData to set
     */
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    /**
     * @return the invPurchaseInvoiceSearchBean
     */
    public InvPurchaseInvoiceSearchBean getInvPurchaseInvoiceSearchBean() {
         if(invPurchaseInvoiceSearchBean == null){
            invPurchaseInvoiceSearchBean = new InvPurchaseInvoiceSearchBean();
        }
        
        return invPurchaseInvoiceSearchBean;
    }

    /**
     * @param invPurchaseInvoiceSearchBean the invPurchaseInvoiceSearchBean to set
     */
    public void setInvPurchaseInvoiceSearchBean(InvPurchaseInvoiceSearchBean invPurchaseInvoiceSearchBean) {
        this.invPurchaseInvoiceSearchBean = invPurchaseInvoiceSearchBean;
    }

    /**
     * @return the cashierFrom
     */
    public List<IsagUserDTO> getCashierFrom() {
          if(cashierFrom == null || cashierFrom.isEmpty()){
            cashierFrom = isagUserService.getUsersByCompanyId(getUserData().getCompany().getId());
            setIsagUserDTOConverterFrom(new IsagUserDTOConverter(cashierFrom));
        }
        return cashierFrom;
    }

    /**
     * @param cashierFrom the cashierFrom to set
     */
    public void setCashierFrom(List<IsagUserDTO> cashierFrom) {
        this.cashierFrom = cashierFrom;
    }

    /**
     * @return the cashierTo
     */
    public List<IsagUserDTO> getCashierTo() {
        if(cashierTo == null || cashierTo.isEmpty()){
            cashierTo = isagUserService.getUsersByCompanyId(getUserData().getCompany().getId());
            setIsagUserDTOConverterTo(new IsagUserDTOConverter(cashierTo));
        }

        return cashierTo;
    }

    /**
     * @param cashierTo the cashierTo to set
     */
    public void setCashierTo(List<IsagUserDTO> cashierTo) {
        this.cashierTo = cashierTo;
    }

    /**
     * @return the isagUserDTOConverterFrom
     */
    public IsagUserDTOConverter getIsagUserDTOConverterFrom() {
        return isagUserDTOConverterFrom;
    }

    /**
     * @param isagUserDTOConverterFrom the isagUserDTOConverterFrom to set
     */
    public void setIsagUserDTOConverterFrom(IsagUserDTOConverter isagUserDTOConverterFrom) {
        this.isagUserDTOConverterFrom = isagUserDTOConverterFrom;
    }

    /**
     * @return the isagUserDTOConverterTo
     */
    public IsagUserDTOConverter getIsagUserDTOConverterTo() {
        return isagUserDTOConverterTo;
    }

    /**
     * @param isagUserDTOConverterTo the isagUserDTOConverterTo to set
     */
    public void setIsagUserDTOConverterTo(IsagUserDTOConverter isagUserDTOConverterTo) {
        this.isagUserDTOConverterTo = isagUserDTOConverterTo;
    }

    /**
     * @return the invInvoiceDTOConverterFrom
     */
    
  
   
}
