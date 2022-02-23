/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.EmployeeService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.SymbolService;
import com.isag.businessservice.reports.searchBean.CrmLeadsSearchBean;
import com.isag.bussinessservice.global.AutoCompleteService;
import com.isag.converter.EmployeeDTOConverter;
import com.isag.converter.InvOrganizationSiteDTOConverter;
import com.isag.converter.SymbolDTOConverter;
import com.isag.dto.EmployeeDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.SymbolDTO;
import com.isag.isag.CrmBasicDataForm;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author khale
 */
@Named("crmLeadsDataMB")
@ViewScoped
public class CrmLeadsDataMB extends CrmBasicDataForm implements Serializable {

    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private EmployeeService employeeService;
    @EJB
    private SymbolService symbolService;
    @EJB
    private AutoCompleteService autoCompleteService;
    
    private CrmLeadsSearchBean crmLeadsSearchBean;

    private List<InvOrganizationSiteDTO> leadsFrom;
    private List<InvOrganizationSiteDTO> leadsTo;
    private List<InvOrganizationSiteDTO> leadsReturn;
    private List<SymbolDTO> sourceListFrom;
    private List<SymbolDTO> sourcesListTo;
    private SymbolDTOConverter symbolConverterFrom;
    private SymbolDTOConverter symbolConverterTo;

    private InvOrganizationSiteDTOConverter invOrganizationSiteDTOConverterFrom;
    private InvOrganizationSiteDTOConverter invOrganizationSiteDTOConverterTo;
    private List<EmployeeDTO> empFrom;
    private List<EmployeeDTO> empTo;
    private EmployeeDTOConverter employeeDTOConverterFrom;
    private EmployeeDTOConverter employeeDTOConverterTo;

    @PostConstruct
    public void load() {

    }
    
    public List<InvOrganizationSiteDTO> completeAllLeads(String query) {
        List<InvOrganizationSiteDTO> invOrganizationSiteDTOList;
        invOrganizationSiteDTOList = autoCompleteService.autoCompleteAllLeads(getUserData().getUserBranch().getId(), query);
        invOrganizationSiteDTOConverterFrom = new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList);
        return invOrganizationSiteDTOList;
    }

    public void reset() {
        setLeadsReturn(null);

    }

    public void search() {
        setLeadsReturn(organizationSiteService.getLeads(getUserData().getUserBranch().getId(), 5, getCrmLeadsSearchBean()));

    }

    public void print(ActionEvent actionEvent) throws  IOException, JRException {
        if (getLeadsReturn() != null && !getLeadsReturn().isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "leadsDataReport.jasper", getLeadsReturn(), "pdf");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد نتائج للطباعة"));
        }
    }

    public HashMap prepareReport() {

        Map<String, String> userDDs = getUserData().getUserDDs();

        HashMap hashMap = new HashMap();
        hashMap.put("PrintedBy", getUserData().getUser().getName());
        hashMap.put("Branch", getUserData().getUserBranch().getNameAr());
        hashMap.put("companyName", getUserData().getCompany().getName());

        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        hashMap.put("header2", " تقرير بيانات العملاء المحتملين");
        hashMap.put("code", "كود العميل");
        hashMap.put("name", "اسم العميل");
        hashMap.put("phone", "الهاتف");
        hashMap.put("FromCustomerText", "من عميل");
        hashMap.put("FromCustomerValue", crmLeadsSearchBean != null && crmLeadsSearchBean.getCodeFrom() != null ? crmLeadsSearchBean.getCodeFrom().getName() : null);
        hashMap.put("ToCustomerText", "الي عميل");
        hashMap.put("ToCustomerValue", crmLeadsSearchBean != null && crmLeadsSearchBean.getCodeTo() != null ? crmLeadsSearchBean.getCodeTo().getName() : null);
        hashMap.put("ToDateText", "الي تاريخ");
        hashMap.put("ToDateValue", crmLeadsSearchBean != null ? crmLeadsSearchBean.getDateTo() : null);
        hashMap.put("FromDateText", "من تاريخ");
        hashMap.put("FromDateValue", crmLeadsSearchBean != null ? crmLeadsSearchBean.getDateFrom() : null);
        return hashMap;
    }

    /**
     * @return the leadsFrom
     */
    public List<InvOrganizationSiteDTO> getLeadsFrom() {
        if (leadsFrom == null || leadsFrom.isEmpty()) {
            leadsFrom = organizationSiteService.findLeads(getUserData().getUserBranch().getId(), null,null);
            setInvOrganizationSiteDTOConverterFrom(new InvOrganizationSiteDTOConverter(leadsFrom));
        }
        return leadsFrom;
    }

    /**
     * @param leadsFrom the leadsFrom to set
     */
    public void setLeadsFrom(List<InvOrganizationSiteDTO> leadsFrom) {
        this.leadsFrom = leadsFrom;
    }

    /**
     * @return the leadsTo
     */
    public List<InvOrganizationSiteDTO> getLeadsTo() {
        if (leadsTo == null || leadsTo.isEmpty()) {
            leadsTo = organizationSiteService.findLeads(getUserData().getUserBranch().getId(), null,null);
            setInvOrganizationSiteDTOConverterTo(new InvOrganizationSiteDTOConverter(leadsTo));
        }
        return leadsTo;
    }

    /**
     * @param leadsTo the leadsTo to set
     */
    public void setLeadsTo(List<InvOrganizationSiteDTO> leadsTo) {
        this.leadsTo = leadsTo;
    }

    /**
     * @return the leadsReturn
     */
    public List<InvOrganizationSiteDTO> getLeadsReturn() {
        return leadsReturn;
    }

    /**
     * @param leadsReturn the leadsReturn to set
     */
    public void setLeadsReturn(List<InvOrganizationSiteDTO> leadsReturn) {
        this.leadsReturn = leadsReturn;
    }

    /**
     * @return the invOrganizationSiteDTOConverterFrom
     */
    public InvOrganizationSiteDTOConverter getInvOrganizationSiteDTOConverterFrom() {
        return invOrganizationSiteDTOConverterFrom;
    }

    /**
     * @param invOrganizationSiteDTOConverterFrom the
     * invOrganizationSiteDTOConverterFrom to set
     */
    public void setInvOrganizationSiteDTOConverterFrom(InvOrganizationSiteDTOConverter invOrganizationSiteDTOConverterFrom) {
        this.invOrganizationSiteDTOConverterFrom = invOrganizationSiteDTOConverterFrom;
    }

    /**
     * @return the invOrganizationSiteDTOConverterTo
     */
    public InvOrganizationSiteDTOConverter getInvOrganizationSiteDTOConverterTo() {
        return invOrganizationSiteDTOConverterTo;
    }

    /**
     * @param invOrganizationSiteDTOConverterTo the
     * invOrganizationSiteDTOConverterTo to set
     */
    public void setInvOrganizationSiteDTOConverterTo(InvOrganizationSiteDTOConverter invOrganizationSiteDTOConverterTo) {
        this.invOrganizationSiteDTOConverterTo = invOrganizationSiteDTOConverterTo;
    }

    /**
     * @return the crmLeadsSearchBean
     */
    public CrmLeadsSearchBean getCrmLeadsSearchBean() {
        if (crmLeadsSearchBean == null) {
            crmLeadsSearchBean = new CrmLeadsSearchBean();
        }
        return crmLeadsSearchBean;
    }

    /**
     * @param crmLeadsSearchBean the crmLeadsSearchBean to set
     */
    public void setCrmLeadsSearchBean(CrmLeadsSearchBean crmLeadsSearchBean) {
        this.crmLeadsSearchBean = crmLeadsSearchBean;
    }

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void init() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the empFrom
     */
    public List<EmployeeDTO> getEmpFrom() {
         if(empFrom == null || empFrom.isEmpty()){
            empFrom = employeeService.getAllEmployeesListByCompanyId(getUserData().getUserlogin().getCompanyId().getId(),getUserData().getUserlogin().getBranchId().getId());
            setEmployeeDTOConverterFrom(new EmployeeDTOConverter(empFrom));
        }
        return empFrom;
    }

    /**
     * @param empFrom the empFrom to set
     */
    public void setEmpFrom(List<EmployeeDTO> empFrom) {
       
        this.empFrom = empFrom;
    }

    /**
     * @return the empTo
     */
    public List<EmployeeDTO> getEmpTo() {
        if(empTo == null || empTo.isEmpty()){
            empTo = employeeService.getAllEmployeesListByCompanyId(getUserData().getUserlogin().getCompanyId().getId(),getUserData().getUserlogin().getBranchId().getId());
            setEmployeeDTOConverterTo(new EmployeeDTOConverter(empTo));
        }
        return empTo;
    }

    /**
     * @param empTo the empTo to set
     */
    public void setEmpTo(List<EmployeeDTO> empTo) {
         
        this.empTo = empTo;
    }

    /**
     * @return the employeeDTOConverterFrom
     */
    public EmployeeDTOConverter getEmployeeDTOConverterFrom() {
        return employeeDTOConverterFrom;
    }

    /**
     * @param employeeDTOConverterFrom the employeeDTOConverterFrom to set
     */
    public void setEmployeeDTOConverterFrom(EmployeeDTOConverter employeeDTOConverterFrom) {
        this.employeeDTOConverterFrom = employeeDTOConverterFrom;
    }

    /**
     * @return the employeeDTOConverterTo
     */
    public EmployeeDTOConverter getEmployeeDTOConverterTo() {
        return employeeDTOConverterTo;
    }

    /**
     * @param employeeDTOConverterTo the employeeDTOConverterTo to set
     */
    public void setEmployeeDTOConverterTo(EmployeeDTOConverter employeeDTOConverterTo) {
        this.employeeDTOConverterTo = employeeDTOConverterTo;
    }

    /**
     * @return the sourceListFrom
     */
    public List<SymbolDTO> getSourceListFrom() {
         if(sourceListFrom == null || sourceListFrom.isEmpty()){
            sourceListFrom = symbolService.getSymbolListByCompanyId(getUserData().getCompany().getId(), 55);
            setSymbolConverterFrom(new SymbolDTOConverter(sourceListFrom));
        }
        return sourceListFrom;
    }

    /**
     * @param sourceListFrom the sourceListFrom to set
     */
    public void setSourceListFrom(List<SymbolDTO> sourceListFrom) {
        this.sourceListFrom = sourceListFrom;
    }

    /**
     * @return the sourcesListTo
     */
    public List<SymbolDTO> getSourcesListTo() {
         if(sourcesListTo == null || sourcesListTo.isEmpty()){
            sourcesListTo = symbolService.getSymbolListByCompanyId(getUserData().getCompany().getId(), 55);
            setSymbolConverterTo(new SymbolDTOConverter(sourcesListTo));
        }
        return sourcesListTo;
    }

    /**
     * @param sourcesListTo the sourcesListTo to set
     */
    public void setSourcesListTo(List<SymbolDTO> sourcesListTo) {
        this.sourcesListTo = sourcesListTo;
    }

    /**
     * @return the symbolConverterFrom
     */
    public SymbolDTOConverter getSymbolConverterFrom() {
        return symbolConverterFrom;
    }

    /**
     * @param symbolConverterFrom the symbolConverterFrom to set
     */
    public void setSymbolConverterFrom(SymbolDTOConverter symbolConverterFrom) {
        this.symbolConverterFrom = symbolConverterFrom;
    }

    /**
     * @return the symbolConverterTo
     */
    public SymbolDTOConverter getSymbolConverterTo() {
        return symbolConverterTo;
    }

    /**
     * @param symbolConverterTo the symbolConverterTo to set
     */
    public void setSymbolConverterTo(SymbolDTOConverter symbolConverterTo) {
        this.symbolConverterTo = symbolConverterTo;
    }

    /**
     * @return the symbolConverterFrom
     */
   
   
}
