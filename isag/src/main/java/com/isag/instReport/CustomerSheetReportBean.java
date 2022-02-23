/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.instReport;

import com.isag.businessservice.InstContractService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.converter.InvOrganizationSiteDTOConverter;
import com.isag.dto.InstContractDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.isag.AutoComplete;
import com.isag.isag.BaseReportBean;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author ahmed
 */
@Named("customerSheetReportBean")
@ViewScoped
public class CustomerSheetReportBean extends BaseReportBean {
    
    private List<InvOrganizationSiteDTO> invOrgSiteFromList;
    private InvOrganizationSiteDTOConverter invOrgSiteFromConverter;
    private List<InvOrganizationSiteDTO> invOrgSiteToList;
    private InvOrganizationSiteDTOConverter invOrgSiteToConverter;
    
    private List<InstContractDTO> contractDTOList;
    
    private InvOrganizationSiteDTO customer;
    
    private Integer contractId;
    
    private InstContractDTO contractDTO;
    
    private AutoComplete autoComplete;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private InstContractService instContractService;
    
    public void searchContract() {
        setContractDTOList(instContractService.getALLInstContractByBranchIdAndCustomerIdDTO(getUserData().getUserBranch().getId(),getCustomer().getId()));
    }
    
    @PostConstruct
    public void init() {
        reset();
        load();
    }
    Integer Start = 0;
    
    @Override
    public void load() {
        
    }
    
    @Override
    public void reset() {
    }
    
    @Override
    public void search() {
        setContractDTO(instContractService.findInstContractDTOById(contractId));
        }
    
    public void hishis() {
        System.out.println("com.isag.report.InstMaturityMB.hishis()");
    }
    
    @Override
    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        
        HashMap hashMap = new HashMap();
        hashMap.put("customerFromText", userDDs.get("FROM_CUSTOM"));
        hashMap.put("customerToText", userDDs.get("TO_CUSTOM"));
        hashMap.put("yearFromText", userDDs.get("YEAR_FROM"));
        hashMap.put("yearToText", userDDs.get("UNTIL_DATE"));
        hashMap.put("serialText", userDDs.get("SERIAL"));
        hashMap.put("dateText", userDDs.get("DATE"));
        hashMap.put("totalValueText", userDDs.get("TOTAL_CONTRACT"));
        hashMap.put("installmentText", userDDs.get("INSTALLMENT"));
        hashMap.put("customerNameText", userDDs.get("CUSTOM_NAME"));
        hashMap.put("projectNameText", userDDs.get("PROJ_NAME"));
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
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
//        if (instMaturityBeanList != null && !instMaturityBeanList.isEmpty()) {
//            fillReport(prepareReport(), getUserData().getReportPath() + "instMaturityReport.jasper", instMaturityBeanList, "pdf");
//        } else {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا توجد نتائج للطباعة"));
//        }
    }
    
    @Override
    public void exportExcel(ActionEvent actionEvent) throws JRException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void exportHtml(ActionEvent actionEvent) throws JRException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public List<InvOrganizationSiteDTO> completOrgSiteFrom(String query) {
        return getAutoComplete().completOrgSiteDTO(query, getInvOrgSiteFromList(), invOrgSiteFromConverter);
    }
    
    public List<InvOrganizationSiteDTO> completOrgSiteTo(String query) {
        return getAutoComplete().completOrgSiteDTO(query, getInvOrgSiteToList(), invOrgSiteToConverter);
    }
    
    public void onMoreText(javax.faces.event.AjaxBehaviorEvent event) {
        // org.primefaces.component.autocomplete.AutoComplete ac = (org.primefaces.component.autocomplete.AutoComplete) event.getSource();
        //ac.setMaxResults(ac.getMaxResults() + 10);
        Start += 10;
        executeJS("PF('organization').search('" + getInvOrgSiteFromList() + "')");
    }
    
    public void executeJS(String source) {
        org.primefaces.context.RequestContext.getCurrentInstance().execute(source);
    }

    /**
     * @return the invOrgSiteFromList
     */
    public List<InvOrganizationSiteDTO> getInvOrgSiteFromList() {
        invOrgSiteFromList = organizationSiteService.getCustomerByBranchIdForReportDTO(getUserData().getUserBranch() != null ? getUserData().getUserBranch().getId() : null);
        setInvOrgSiteFromConverter(new InvOrganizationSiteDTOConverter(invOrgSiteFromList));
        return invOrgSiteFromList;
    }
    
    private List<InvOrganizationSiteDTO> invOrganizationSiteDTOList(Integer start) {
        List<InvOrganizationSiteDTO> invOrgSiteList = new ArrayList<>();
        //   invOrgSiteList = organizationSiteService.getCustomerByBranchIdForReportDTO(getUserData().getUserBranch() != null ? getUserData().getUserBranch().getId() : null, start, 11);
        return invOrgSiteList;
    }

    /**
     * @param invOrgSiteFromList the invOrgSiteFromList to set
     */
    public void setInvOrgSiteFromList(List<InvOrganizationSiteDTO> invOrgSiteFromList) {
        this.invOrgSiteFromList = invOrgSiteFromList;
    }

    /**
     * @return the invOrgSiteFromConverter
     */
    public InvOrganizationSiteDTOConverter getInvOrgSiteFromConverter() {
        return invOrgSiteFromConverter;
    }

    /**
     * @param invOrgSiteFromConverter the invOrgSiteFromConverter to set
     */
    public void setInvOrgSiteFromConverter(InvOrganizationSiteDTOConverter invOrgSiteFromConverter) {
        this.invOrgSiteFromConverter = invOrgSiteFromConverter;
    }

    /**
     * @return the invOrgSiteToList
     */
    public List<InvOrganizationSiteDTO> getInvOrgSiteToList() {
        invOrgSiteToList = organizationSiteService.getCustomerByBranchIdForReportDTO(getUserData().getUserBranch() != null ? getUserData().getUserBranch().getId() : null);
        setInvOrgSiteToConverter(new InvOrganizationSiteDTOConverter(invOrgSiteToList));
        return invOrgSiteToList;
    }

    /**
     * @param invOrgSiteToList the invOrgSiteToList to set
     */
    public void setInvOrgSiteToList(List<InvOrganizationSiteDTO> invOrgSiteToList) {
        this.invOrgSiteToList = invOrgSiteToList;
    }

    /**
     * @return the invOrgSiteToConverter
     */
    public InvOrganizationSiteDTOConverter getInvOrgSiteToConverter() {
        return invOrgSiteToConverter;
    }

    /**
     * @param invOrgSiteToConverter the invOrgSiteToConverter to set
     */
    public void setInvOrgSiteToConverter(InvOrganizationSiteDTOConverter invOrgSiteToConverter) {
        this.invOrgSiteToConverter = invOrgSiteToConverter;
    }

    /**
     * @return the autoComplete
     */
    public AutoComplete getAutoComplete() {
        if (autoComplete == null) {
            autoComplete = new AutoComplete();
        }
        return autoComplete;
    }

    /**
     * @param autoComplete the autoComplete to set
     */
    public void setAutoComplete(AutoComplete autoComplete) {
        this.autoComplete = autoComplete;
    }

    /**
     * @return the customer
     */
    public InvOrganizationSiteDTO getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(InvOrganizationSiteDTO customer) {
        this.customer = customer;
    }

    /**
     * @return the contractDTOList
     */
    public List<InstContractDTO> getContractDTOList() {
        return contractDTOList;
    }

    /**
     * @param contractDTOList the contractDTOList to set
     */
    public void setContractDTOList(List<InstContractDTO> contractDTOList) {
        this.contractDTOList = contractDTOList;
    }

    /**
     * @return the contractId
     */
    public Integer getContractId() {
        return contractId;
    }

    /**
     * @param contractId the contractId to set
     */
    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    /**
     * @return the contractDTO
     */
    public InstContractDTO getContractDTO() {
        return contractDTO;
    }

    /**
     * @param contractDTO the contractDTO to set
     */
    public void setContractDTO(InstContractDTO contractDTO) {
        this.contractDTO = contractDTO;
    }
}
