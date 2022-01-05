/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.toby;

import com.toby.businessservice.EmployeeService;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.SymbolService;
import com.toby.converter.EmployeeDTOConverter;
import com.toby.converter.SymbolDTOConverter;
import com.toby.dto.CrmCustomerInterestedProductsDTO;
import com.toby.dto.EmployeeDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.SymbolDTO;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author hq004
 */
@Named(value = "globalMB")
@ViewScoped
public class globalMB extends Basic implements Serializable {

    private InvOrganizationSiteDTO invOrganizationSiteDTO;
    private Boolean errorMsgbool = Boolean.TRUE;
    private SymbolDTOConverter LearnAboutCompanyfromconverter;
    private SymbolDTOConverter JobConverter;
    private List<SymbolDTO> workTypeDTOList;
    private List<SymbolDTO> LearnAboutCompanyfromDTOList;
    private SymbolDTOConverter leadStatusDTOconverter;
    private List<SymbolDTO> leadStatusDTOList;
    private EmployeeDTOConverter employeeDTOConverter;
    private List<EmployeeDTO> employeeDTOList;
    private String errorMsg;

    /**
     * Creates a new instance of LoginBean
     */
    @EJB
    private OrganizationSiteService organizationSiteService;

    @EJB
    private SymbolService symbolService;

    @EJB
    EmployeeService employeeService;

    @PostConstruct
    public void init() {

        System.out.println("");
    }

    public void showAddDialog() {
        OpenDlg("dlg12add");
    }
    
    public void reset(){  
        invOrganizationSiteDTO = new InvOrganizationSiteDTO();
    }

    public String validateSaveLead(InvOrganizationSiteDTO invOrganizationSiteDTO) {
        StringBuilder error = new StringBuilder();
        return organizationSiteService.validateSmallSaveLead(error , invOrganizationSiteDTO, getUserData().getUser());
    }

    public String saveLead() {

        String error = validateSaveLead(getInvOrganizationSiteDTO());

        if (error != null && !error.isEmpty()) {
            errorMessage(error);
            setErrorMsgbool(Boolean.TRUE);
        } else {
            setErrorMsgbool(Boolean.FALSE);
            getInvOrganizationSiteDTO().setMsg(null);
            if (getInvOrganizationSiteDTO().getInstProjectGuideDTO() != null) {
                CrmCustomerInterestedProductsDTO crmCustomerInterestedProductsDTO = new CrmCustomerInterestedProductsDTO();
                crmCustomerInterestedProductsDTO.setAskDate(new Date());
                crmCustomerInterestedProductsDTO.setProjectId(getInvOrganizationSiteDTO().getInstProjectGuideDTO());
                getInvOrganizationSiteDTO().getCrmCustomerInterestedProductsDTOList().add(crmCustomerInterestedProductsDTO);
            }
            setInvOrganizationSiteDTO(organizationSiteService.saveSmallLead(getInvOrganizationSiteDTO(), getUserData().getUser()));
            msgSave(getInvOrganizationSiteDTO().getMsg());
//             exit("../crmLeads/leadsList.xhtml");
        }
        return "";
    }

    public List<EmployeeDTO> completeEmployeeDTO(String query) {
        return AutoComplete.completeEmployeeDTO(query, getEmployeeDTOList(), getEmployeeDTOConverter());
    }

    public List<SymbolDTO> completeLeadStatus(String query) {
        return AutoComplete.completeLeadStatus(query, getLeadStatusDTOList(), getLeadStatusDTOconverter());
    }

    public List<SymbolDTO> completeLearnAboutCompanyfrom(String query) {
        return AutoComplete.completeSymbol(query, getLearnAboutCompanyfromDTOList(), getLearnAboutCompanyfromconverter());
    }

    public List<SymbolDTO> completeWorkType(String query) {
        return AutoComplete.completeSymbol(query, getWorkTypeDTOList(), getJobConverter());
    }

    /**
     * @return the invOrganizationSiteDTO
     */
    public InvOrganizationSiteDTO getInvOrganizationSiteDTO() {
        if (invOrganizationSiteDTO == null) {
            invOrganizationSiteDTO = new InvOrganizationSiteDTO();
        }
        return invOrganizationSiteDTO;
    }

    /**
     * @param invOrganizationSiteDTO the invOrganizationSiteDTO to set
     */
    public void setInvOrganizationSiteDTO(InvOrganizationSiteDTO invOrganizationSiteDTO) {
        this.invOrganizationSiteDTO = invOrganizationSiteDTO;
    }

    /**
     * @return the errorMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * @param errorMsg the errorMsg to set
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     * @return the errorMsgbool
     */
    public Boolean getErrorMsgbool() {
        return errorMsgbool;
    }

    /**
     * @param errorMsgbool the errorMsgbool to set
     */
    public void setErrorMsgbool(Boolean errorMsgbool) {
        this.errorMsgbool = errorMsgbool;
    }

    /**
     * @return the LearnAboutCompanyfromconverter
     */
    public SymbolDTOConverter getLearnAboutCompanyfromconverter() {
        return LearnAboutCompanyfromconverter;
    }

    /**
     * @param LearnAboutCompanyfromconverter the LearnAboutCompanyfromconverter
     * to set
     */
    public void setLearnAboutCompanyfromconverter(SymbolDTOConverter LearnAboutCompanyfromconverter) {
        this.LearnAboutCompanyfromconverter = LearnAboutCompanyfromconverter;
    }

    /**
     * @return the JobConverter
     */
    public SymbolDTOConverter getJobConverter() {
        return JobConverter;
    }

    /**
     * @param JobConverter the JobConverter to set
     */
    public void setJobConverter(SymbolDTOConverter JobConverter) {
        this.JobConverter = JobConverter;
    }

    /**
     * @return the workTypeDTOList
     */
    public List<SymbolDTO> getWorkTypeDTOList() {
        if (workTypeDTOList == null || workTypeDTOList.isEmpty()) {
            workTypeDTOList = symbolService.getSymbolListByCompanyId(getUserData().getCompany().getId(), 52);
            setJobConverter(new SymbolDTOConverter(workTypeDTOList));
        }
        return workTypeDTOList;
    }

    /**
     * @param workTypeDTOList the workTypeDTOList to set
     */
    public void setWorkTypeDTOList(List<SymbolDTO> workTypeDTOList) {
        this.workTypeDTOList = workTypeDTOList;
    }

    /**
     * @return the LearnAboutCompanyfromDTOList
     */
    public List<SymbolDTO> getLearnAboutCompanyfromDTOList() {
        if (LearnAboutCompanyfromDTOList == null || LearnAboutCompanyfromDTOList.isEmpty()) {
            LearnAboutCompanyfromDTOList = symbolService.getSymbolListByCompanyId(getUserData().getCompany().getId(), 55);
            setLearnAboutCompanyfromconverter(new SymbolDTOConverter(LearnAboutCompanyfromDTOList));
        }
        return LearnAboutCompanyfromDTOList;
    }

    /**
     * @param LearnAboutCompanyfromDTOList the LearnAboutCompanyfromDTOList to
     * set
     */
    public void setLearnAboutCompanyfromDTOList(List<SymbolDTO> LearnAboutCompanyfromDTOList) {
        this.LearnAboutCompanyfromDTOList = LearnAboutCompanyfromDTOList;
    }

    /**
     * @return the leadStatusDTOconverter
     */
    public SymbolDTOConverter getLeadStatusDTOconverter() {
        return leadStatusDTOconverter;
    }

    /**
     * @param leadStatusDTOconverter the leadStatusDTOconverter to set
     */
    public void setLeadStatusDTOconverter(SymbolDTOConverter leadStatusDTOconverter) {
        this.leadStatusDTOconverter = leadStatusDTOconverter;
    }

    /**
     * @return the leadStatusDTOList
     */
    public List<SymbolDTO> getLeadStatusDTOList() {
        if (leadStatusDTOList == null || leadStatusDTOList.isEmpty()) {
            leadStatusDTOList = symbolService.getSymbolListByCompanyId(getUserData().getCompany().getId(), 57);
            setLeadStatusDTOconverter(new SymbolDTOConverter(leadStatusDTOList));
        }
        return leadStatusDTOList;
    }

    /**
     * @param leadStatusDTOList the leadStatusDTOList to set
     */
    public void setLeadStatusDTOList(List<SymbolDTO> leadStatusDTOList) {
        this.leadStatusDTOList = leadStatusDTOList;
    }

    /**
     * @return the employeeDTOConverter
     */
    public EmployeeDTOConverter getEmployeeDTOConverter() {
        return employeeDTOConverter;
    }

    /**
     * @param employeeDTOConverter the employeeDTOConverter to set
     */
    public void setEmployeeDTOConverter(EmployeeDTOConverter employeeDTOConverter) {
        this.employeeDTOConverter = employeeDTOConverter;
    }

    /**
     * @return the employeeDTOList
     */
    public List<EmployeeDTO> getEmployeeDTOList() {
        if (employeeDTOList == null || employeeDTOList.isEmpty()) {
            if (getUserData() != null && getUserData().getEmployeeDTODefault() != null) {
                boolean isActive = false;
                if (getUri().contains("leads.xhtml")) {
                    isActive = true;
                }
                employeeDTOList = employeeService.getAllEmployeesListByParentId(getUserlogin().getCompanyId().getId(), getUserData().getUser().getBranchId().getId(), getUserData().getEmployeeDTODefault().getId(), isActive);
                employeeDTOList.add(getUserData().getEmployeeDTODefault());
                setEmployeeDTOConverter(new EmployeeDTOConverter(employeeDTOList));
            } else {
                employeeDTOList = new ArrayList<>();
            }
        }
        return employeeDTOList;
    }

    /**
     * @param employeeDTOList the employeeDTOList to set
     */
    public void setEmployeeDTOList(List<EmployeeDTO> employeeDTOList) {
        this.employeeDTOList = employeeDTOList;
    }

}
