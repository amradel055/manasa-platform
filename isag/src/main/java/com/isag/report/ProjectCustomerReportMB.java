/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.CRMCustomerInterestedProductsServiceDTO;
import com.isag.businessservice.reports.searchBean.CrmEventsReportSearchBean;
import com.isag.dto.CrmCustomerInterestedProductsDTO;
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
@Named(value = "projectCustomerReportMB")
@ViewScoped
public class ProjectCustomerReportMB implements Serializable{
    
    private CrmEventsReportSearchBean crmEventsReportSearchBean;
    private List<CrmCustomerInterestedProductsDTO> crmCustomerInterestedProductsDTOList;
    
    @EJB
    CRMCustomerInterestedProductsServiceDTO cRMCustomerInterestedProductsServiceDTO;
    
    private UserData userData = new UserData();
    
    @PostConstruct
    public void load(){
    ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
    setUserData((UserData) context.getSessionMap().get("userLogInData"));
    
    }
    public void reset(){
        setCrmEventsReportSearchBean(null);
    }
    
    public void search(){
        setCrmCustomerInterestedProductsDTOList(cRMCustomerInterestedProductsServiceDTO.findCrmCustomerInterestedProducts(crmEventsReportSearchBean, getUserData().getUserBranch().getId()));
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
     * @return the crmEventsReportSearchBean
     */
    public CrmEventsReportSearchBean getCrmEventsReportSearchBean() {
         if(crmEventsReportSearchBean == null){
            crmEventsReportSearchBean = new CrmEventsReportSearchBean();
        }
        return crmEventsReportSearchBean;
    }

    /**
     * @param crmEventsReportSearchBean the crmEventsReportSearchBean to set
     */
    public void setCrmEventsReportSearchBean(CrmEventsReportSearchBean crmEventsReportSearchBean) {
        this.crmEventsReportSearchBean = crmEventsReportSearchBean;
    }

    /**
     * @return the crmCustomerInterestedProductsDTOList
     */
    public List<CrmCustomerInterestedProductsDTO> getCrmCustomerInterestedProductsDTOList() {
        return crmCustomerInterestedProductsDTOList;
    }

    /**
     * @param crmCustomerInterestedProductsDTOList the crmCustomerInterestedProductsDTOList to set
     */
    public void setCrmCustomerInterestedProductsDTOList(List<CrmCustomerInterestedProductsDTO> crmCustomerInterestedProductsDTOList) {
        this.crmCustomerInterestedProductsDTOList = crmCustomerInterestedProductsDTOList;
    }
    
}
