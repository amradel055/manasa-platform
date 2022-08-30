/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.toby;
import com.toby.businessservice.CRMEventServiceDTO;
import com.toby.businessservice.CrmSettingService;
import com.toby.businessservice.EmployeeService;
import com.toby.businessservice.InstProjectGuideService;
import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.SymbolService;
import com.toby.businessservice.TobyNotificationSettingService;
import com.toby.businessservice.reports.searchBean.CrmLeadsSearchBean;
import com.toby.bussinessservice.global.AutoCompleteService;
import com.toby.converter.EmployeeDTOConverter;
import com.toby.converter.InstProjectGuideDTOConverter;
import com.toby.converter.InvOrganizationSiteDTOConverter;
import com.toby.converter.SymbolDTOConverter;
import com.toby.dto.CrmCustomerInterestedProductsDTO;
import com.toby.dto.CrmEventDTO;
import com.toby.dto.CrmSettingDTO;
import com.toby.dto.EmployeeDTO;
import com.toby.dto.InstProjectGuideDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.SymbolDTO;
import com.toby.dto.TobyNotificationSettingDTO;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author m_els
 */
public abstract class CrmBasicDataForm extends BaseFormBean {

    private List<EmployeeDTO> employeeDTOList;
    private List<EmployeeDTO> employeeDTOTransferList;
    private List<EmployeeDTO> employeeDTOFollowerList;
    private List<EmployeeDTO> allEmployeeDTOList;
    private List<SymbolDTO> leadStatusDTOList;
    private List<SymbolDTO> eventStatusDTOList;
    private List<SymbolDTO> activityTypeDTOList;
    private List<SymbolDTO> priorityDTOList;
    private List<SymbolDTO> LearnAboutCompanyfromDTOList;
    private List<SymbolDTO> countryIdDTOList;
    private List<SymbolDTO> projectNameDTOList;
    private List<SymbolDTO> workTypeDTOList;
    private List<SymbolDTO> crmTyprList;
    private List<InstProjectGuideDTO> instProjectGuideDTOList;
    private List<InvOrganizationSiteDTO> invOrganizationSiteDTOList;
    private List<InvOrganizationSiteDTO> invOrganizationSiteDTOListByLeaders;

    private EmployeeDTOConverter employeeDTOConverter;
    private EmployeeDTOConverter employeeDTOTransferConverter;
    private SymbolDTOConverter leadStatusDTOconverter;
    private SymbolDTOConverter eventStatusDTOconverter;
    private SymbolDTOConverter activityTypeDTOconverter;
    private SymbolDTOConverter priorityDTOconverter;
    private SymbolDTOConverter LearnAboutCompanyfromconverter;
    private SymbolDTOConverter countryIdconverter;
    private SymbolDTOConverter ProjectNameConverter;
    private SymbolDTOConverter JobConverter;
    private SymbolDTOConverter crmTypeConverter;
    private InstProjectGuideDTOConverter instProjectGuideDTOConverter;
    private InvOrganizationSiteDTOConverter invOrganizationSiteDTOConverter;
    private TobyNotificationSettingDTO tobyNotificationSettingDTO;
    private CrmLeadsSearchBean crmLeadsSearchBean;

    private CrmEventDTO crmEventDTO;

    private CrmEventDTO crmEventDTOSearch;

    private InvOrganizationSiteDTO invOrganizationSiteDTO;

    private CrmCustomerInterestedProductsDTO crmCustomerInterestedProductsDTOSelected;

    private EmployeeDTO employeeDTOSelected;

    private Boolean employeeIsSupervisor = Boolean.FALSE;

    private Boolean errorMsg = Boolean.TRUE;
    private Date dateNow;
    private Integer leadSelected;
    private LazyDataModel<CrmEventDTO> dataModel;
    private LazyDataModel<InvOrganizationSiteDTO> dataModelInvOrganization;

    private CrmSettingDTO crmSettingDTO;
    private String notificationId;

    /* 
       check the leads 0 is myLeads OR 1 is All Leads belong to supervisor
       to get leads data in crm main page and popup in homa page
     */
    private Integer dateType;
    /*
    check the leads 0 is myLeads OR
    1 is All leads to get lead number in crm main page
     */
    private Integer leadsType;

    // EJBs
    @EJB
    EmployeeService employeeService;
    @EJB
    private SymbolService symbolService;
    @EJB
    private CRMEventServiceDTO cRMEventServiceDTO;
    @EJB
    InstProjectGuideService instProjectGuideService;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private AutoCompleteService autoCompleteService;
    @EJB
    private TobyNotificationSettingService tobyNotificationSettingService;
    @EJB
    private CrmSettingService crmSettingService;

    @Override
    public abstract String save();

    @Override
    public abstract void init();

    @Override
    public abstract void load();

    @Override
    public abstract String getScreenName();

    public String validateSave(CrmEventDTO crmEventDTO) {
        String error = cRMEventServiceDTO.validateSave(crmEventDTO);
        return error;
    }

    public String validateSaveLead(InvOrganizationSiteDTO invOrganizationSiteDTO) {
        String error = organizationSiteService.validateSaveLead(invOrganizationSiteDTO, getUserData().getUser());
        return error;
    }

    public String saveEvent() { 
        if (!getEmployeeIsSupervisor()) {
            getCrmEventDTO().setAssignedTo(getUserData().getEmployeeDTODefault());
        }
        String error = validateSave(getCrmEventDTO());
        if (error != null && !error.isEmpty()) {
            errorMessage(error);
            errorMsg = Boolean.TRUE;
        } else {
            errorMsg = Boolean.FALSE;
            getCrmEventDTO().setMsg(null);
            setCrmEventDTO(cRMEventServiceDTO.save(getCrmEventDTO(), getUserData().getUser()));
            msgSave(getCrmEventDTO().getMsg());

            //update Crm Notification
            if (getCrmEventDTO().getNotificationDate() != null && getCrmEventDTO().getNotificationDate().before(endDate())) {
                getUserData().setCrmNotification(cRMEventServiceDTO.findALLCrmNotification(getUserData().getUser().getBranchId().getId(), getUserData().getEmployeeDTODefault().getId()));
            }
        }
        return "";
    }

    public String saveLead() {
        if (!getEmployeeIsSupervisor()) {
            getInvOrganizationSiteDTO().setAssignedTo(getUserData().getEmployeeDTODefault());
        }
        String error = validateSaveLead(getInvOrganizationSiteDTO());

        if (error != null && !error.isEmpty()) {
            errorMessage(error);
            errorMsg = Boolean.TRUE;
        } else {
            errorMsg = Boolean.FALSE;
            getInvOrganizationSiteDTO().setMsg(null);
            if (getInvOrganizationSiteDTO().getInstProjectGuideDTO() != null) {
                CrmCustomerInterestedProductsDTO crmCustomerInterestedProductsDTO = new CrmCustomerInterestedProductsDTO();
                crmCustomerInterestedProductsDTO.setAskDate(new Date());
                crmCustomerInterestedProductsDTO.setProjectId(getInvOrganizationSiteDTO().getInstProjectGuideDTO());
                getInvOrganizationSiteDTO().getCrmCustomerInterestedProductsDTOList().add(crmCustomerInterestedProductsDTO);
            }
            setInvOrganizationSiteDTO(organizationSiteService.saveLead(getInvOrganizationSiteDTO(), getUserData().getUser()));
            msgSave(getInvOrganizationSiteDTO().getMsg());
//             exit("../crmLeads/leadsList.xhtml");
        }
        return "";
    }

    public String saveLeadANDEvent() {
        saveLead();
        if (!errorMsg) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('dlg1').show();");
            context.execute("PF('dlg12').hide();");
            getCrmEventDTO().setCustomerId(getInvOrganizationSiteDTO());
            updateAssignedTo();
        }
        return "";
    }

    public String saveCrmNotificationSetting() {
        getTobyNotificationSettingDTO().setTheme("info");
        setTobyNotificationSettingDTO(tobyNotificationSettingService.addTobyNotificationSettingDTO(getTobyNotificationSettingDTO(), getUserData().getUser()));
        return "";
    }

    public String saveCrmSetting() {
        setCrmSettingDTO(crmSettingService.addCrmSettingDTO(getCrmSettingDTO(), getUserData().getUser()));
        return "";
    }

    public void updateAssignedTo() {
        if (getCrmEventDTO().getCustomerId() != null) {
            EmployeeDTO assignedTo = organizationSiteService.findOrganizationSiteDTOById(getCrmEventDTO().getCustomerId().getId()).getAssignedTo();
            getCrmEventDTO().setAssignedTo(assignedTo);
            getCrmEventDTO().setActiveAssignedTo(Boolean.TRUE);
        } else {
            getCrmEventDTO().setActiveAssignedTo(Boolean.FALSE);
        }
    }

    public void resetCrmNotificationSetting() {
        setTobyNotificationSettingDTO(new TobyNotificationSettingDTO());
    }

    public void resetCrmEventDTO() {
        setCrmEventDTO(new CrmEventDTO());
    }

    public void updateTODOValue() {
        getCrmEventDTO().setType(1);
    }

    public void updateEventValue() {
        getCrmEventDTO().setType(0);
    }

    public void fillEventsDTO(Integer customerId) {
        if (customerId != null && customerId > 0) {
            getInvOrganizationSiteDTO().setCrmEventDTOList(cRMEventServiceDTO.findALLCrmEventByCustomerId(customerId));
        }
    }

    public String goToLeadsEvent(Integer leadId) {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.getSessionMap().put("leadSelected", leadId);
            context.getSessionMap().put("ScreenMode", "EditLead");
            exit("../crmLeads/leadsevent.xhtml");
            return "";
        } catch (Exception e) {
            saveError(e, "OrganizationSiteListMB", "goToEdit");
            return null;
        }
    }

    public String goToEventEdit(Integer crmEventSelectedId) {
        try {
            if (crmEventSelectedId != null && crmEventSelectedId > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.getSessionMap().put("crmEventDTOSelected", crmEventSelectedId);
                context.getSessionMap().put("ScreenMode", "EditcrmTaskDTO");
                exit("../crmTodoEvent/TodoEvent.xhtml");
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "crmEventListDTOMB", "goToEdit");
            return null;
        }

    }

    //// Auto Complte ////
    public List<SymbolDTO> completeLeadStatus(String query) {
        return AutoComplete.completeLeadStatus(query, getLeadStatusDTOList(), getLeadStatusDTOconverter());
    }

    public List<SymbolDTO> completeEventStatus(String query) {
        return AutoComplete.completeEventStatus(query, getEventStatusDTOList(), getEventStatusDTOconverter());
    }

    public List<SymbolDTO> completeActivityType(String query) {
        return AutoComplete.completeActivityType(query, getActivityTypeDTOList(), getActivityTypeDTOconverter());
    }

    public List<SymbolDTO> completepriority(String query) {
        return AutoComplete.completepriority(query, getPriorityDTOList(), getActivityTypeDTOconverter());
    }

    public List<EmployeeDTO> completeEmployeeDTO(String query) {
        return AutoComplete.completeEmployeeDTO(query, getEmployeeDTOList(), getEmployeeDTOConverter());
    }

    public List<InvOrganizationSiteDTO> completeInvOrganizationSiteDTO(String query) {
        return AutoComplete.completOrgSiteDTO(query, getInvOrganizationSiteDTOList(), getInvOrganizationSiteDTOConverter());
    }

    public List<InvOrganizationSiteDTO> completeInvOrganizationSiteDTOByLeader(String query) {
        return AutoComplete.completOrgSiteDTO(query, getInvOrganizationSiteDTOListByLeaders(query), getInvOrganizationSiteDTOConverter());
    }

    public List<EmployeeDTO> completeEmployeeFollowDTO(String query) {
        return AutoComplete.completeEmployeeDTO(query, getEmployeeDTOFollowerList(), getEmployeeDTOConverter());
    }

    public List<SymbolDTO> completeLearnAboutCompanyfrom(String query) {
        return AutoComplete.completeSymbol(query, getLearnAboutCompanyfromDTOList(), getLearnAboutCompanyfromconverter());
    }

    public List<SymbolDTO> countryCompanyfrom(String query) {
        return AutoComplete.completeSymbol(query, getCountryIdDTOList(), getCountryIdconverter());
    }

    public List<SymbolDTO> completeProjectName(String query) {
        return AutoComplete.completeSymbol(query, getProjectNameDTOList(), getProjectNameConverter());
    }

    public List<SymbolDTO> completeWorkType(String query) {
        return AutoComplete.completeSymbol(query, getWorkTypeDTOList(), getJobConverter());
    }

    public void vaildationContacts() {
        String error = organizationSiteService.vaildationContacts(getInvOrganizationSiteDTO(), getUserData().getUser());
        if (error != null && !error.isEmpty()) {
            errorMessage(error);
            errorMsg = Boolean.TRUE;
        }
    }

    public void numberOfLeads() {
        if (getUri() != null && getUri().contains("main") && getUserData() != null) {
            // 1 is my leads
            if (getLeadsType() == 1) {
                getInvOrganizationSiteDTO().setNumberOfLeadsAssigned(organizationSiteService.getNumberOfCustomerByBranchIdAndAssignedTo(getUserData().getUserlogin().getBranchId().getId(), getUserData().getEmployeeDTODefault() != null ? getUserData().getEmployeeDTODefault().getId() : null, getUserData().getUserlogin().getId()));
                getInvOrganizationSiteDTO().setNumberOfNewLeadsAssigned(organizationSiteService.getNumberOfNewCustomerByBranchIdAndAssignedTo(getUserData().getUserlogin().getBranchId().getId(), getUserData().getEmployeeDTODefault() != null ? getUserData().getEmployeeDTODefault().getId() : null, getUserData().getUserlogin().getId()));
            } else {
                // 0 is al leads
                getInvOrganizationSiteDTO().setNumberOfLeadsAssigned(organizationSiteService.getNumberOfCustomerByBranchId(getUserData().getUserlogin().getBranchId().getId(), getUserData().getEmployeeDTODefault() != null ? getUserData().getEmployeeDTODefault().getId() : null, getUserData().getUserlogin().getId(), null, null));
                getInvOrganizationSiteDTO().setNumberOfNewLeadsAssigned(organizationSiteService.getNumberOfNewCustomerByBranchId(getUserData().getUserlogin().getBranchId().getId(), getUserData().getEmployeeDTODefault() != null ? getUserData().getEmployeeDTODefault().getId() : null, getUserData().getUserlogin().getId(), null, null));
            }
        }
    }

    public String exit() {
        try {
            if (getUri().contains("leads.xhtml")) {
                exit("../crmLeads/leadsList.xhtml");
            } else if (getUri().contains("crmSetting")) {
                exit("../crm/main.xhtml");
            }
        } catch (Exception e) {
            saveError(e, "InvUpdateFormMB", "exit()");
        }
        return "";
    }

    public String goToLeadsEventFromList() {
        try {
            if (getLeadSelected() != null && getLeadSelected() > 0) {
                fillEventsDTO(getLeadSelected());
                return "";
            } else {
                return "";
            }
        } catch (Exception e) {
            saveError(e, "OrganizationSiteListMB", "goToEdit");
            return null;
        }
    }

    private Date endDate() {
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(new Date(new Date().getTime() + (1000 * 60 * 60 * 24)));
        calEnd.set(Calendar.HOUR_OF_DAY, 0);
        calEnd.set(Calendar.MINUTE, 0);
        calEnd.set(Calendar.SECOND, 0);
        calEnd.set(Calendar.MILLISECOND, 0);
        return calEnd.getTime();
    }

    public void closeNotification() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        notificationId = params.get("notificationId");
        if (notificationId != null || !notificationId.isEmpty()) {
            cRMEventServiceDTO.closeNotification(Integer.valueOf(notificationId));
            getUserData().setCrmNotification(cRMEventServiceDTO.findALLCrmNotification(getUserData().getUser().getBranchId().getId(), getUserData().getEmployeeDTODefault() != null ? getUserData().getEmployeeDTODefault().getId() : null));
        }
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

    /**
     * @return the employeeDTOList
     */
    public List<EmployeeDTO> getEmployeeDTOTransferList() {
        if (employeeDTOTransferList == null || employeeDTOTransferList.isEmpty()) {
            if (getUserData().getEmployeeDTODefault() != null) {
                employeeDTOTransferList = employeeService.getAllEmployeesListByCompanyIdAndBranchId(getUserlogin().getCompanyId().getId(), getUserData().getUser().getBranchId().getId());
                setEmployeeDTOTransferConverter(new EmployeeDTOConverter(employeeDTOTransferList));
            } else {
                employeeDTOTransferList = new ArrayList<>();
            }
        }
        return employeeDTOTransferList;
    }

    /**
     * @param employeeDTOList the employeeDTOTransferList to set
     */
    public void setEmployeeDTOTransferList(List<EmployeeDTO> employeeDTOTransferList) {
        this.employeeDTOTransferList = employeeDTOTransferList;
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
     * @return the activityTypeDTOconverter
     */
    public SymbolDTOConverter getActivityTypeDTOconverter() {
        return activityTypeDTOconverter;
    }

    /**
     * @param activityTypeDTOconverter the activityTypeDTOconverter to set
     */
    public void setActivityTypeDTOconverter(SymbolDTOConverter activityTypeDTOconverter) {
        this.activityTypeDTOconverter = activityTypeDTOconverter;
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
     * @return the activityTypeDTOList
     */
    public List<SymbolDTO> getActivityTypeDTOList() {
        if (activityTypeDTOList == null || activityTypeDTOList.isEmpty()) {
            activityTypeDTOList = symbolService.getSymbolListByCompanyId(getUserData().getCompany().getId(), 58);
            setActivityTypeDTOconverter(new SymbolDTOConverter(activityTypeDTOList));
        }
        return activityTypeDTOList;
    }

    /**
     * @param activityTypeDTOList the activityTypeDTOList to set
     */
    public void setActivityTypeDTOList(List<SymbolDTO> activityTypeDTOList) {
        this.activityTypeDTOList = activityTypeDTOList;
    }

    /**
     * @return the priorityDTOList
     */
    public List<SymbolDTO> getPriorityDTOList() {
        if (priorityDTOList == null || priorityDTOList.isEmpty()) {
            priorityDTOList = symbolService.getSymbolListByCompanyId(getUserData().getCompany().getId(), 59);
            setPriorityDTOconverter(new SymbolDTOConverter(priorityDTOList));
        }
        return priorityDTOList;
    }

    /**
     * @param priorityDTOList the priorityDTOList to set
     */
    public void setPriorityDTOList(List<SymbolDTO> priorityDTOList) {
        this.priorityDTOList = priorityDTOList;
    }

    /**
     * @return the priorityDTOconverter
     */
    public SymbolDTOConverter getPriorityDTOconverter() {
        return priorityDTOconverter;
    }

    /**
     * @param priorityDTOconverter the priorityDTOconverter to set
     */
    public void setPriorityDTOconverter(SymbolDTOConverter priorityDTOconverter) {
        this.priorityDTOconverter = priorityDTOconverter;
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
     * @return the countryIdDTOList
     */
    public List<SymbolDTO> getCountryIdDTOList() {
        if (countryIdDTOList == null || countryIdDTOList.isEmpty()) {
            countryIdDTOList = symbolService.getSymbolListByCompanyId(getUserData().getCompany().getId(), 31);
            setCountryIdconverter(new SymbolDTOConverter(countryIdDTOList));
        }
        return countryIdDTOList;
    }

    /**
     * @param countryIdDTOList the countryIdDTOList to set
     */
    public void setCountryIdDTOList(List<SymbolDTO> countryIdDTOList) {
        this.countryIdDTOList = countryIdDTOList;
    }

    /**
     * @return the countryIdconverter
     */
    public SymbolDTOConverter getCountryIdconverter() {
        return countryIdconverter;
    }

    /**
     * @param countryIdconverter the countryIdconverter to set
     */
    public void setCountryIdconverter(SymbolDTOConverter countryIdconverter) {
        this.countryIdconverter = countryIdconverter;
    }

    /**
     * @return the employeeDTOSelected
     */
    public EmployeeDTO getEmployeeDTOSelected() {
        return employeeDTOSelected;
    }

    /**
     * @param employeeDTOSelected the employeeDTOSelected to set
     */
    public void setEmployeeDTOSelected(EmployeeDTO employeeDTOSelected) {
        this.employeeDTOSelected = employeeDTOSelected;
    }

    /**
     * @return the crmEventDTO
     */
    public CrmEventDTO getCrmEventDTO() {
        if (crmEventDTO == null) {
            crmEventDTO = new CrmEventDTO();
        }
        if (crmEventDTO != null && getUserData() != null && getUserData().getEmployeeDTODefault() != null && getUri() != null && !getUri().contains("TodoEvent")) {
            crmEventDTO.setNumberOfAllEvent(cRMEventServiceDTO.findCrmEventListtCount(getUserData().getEmployeeDTODefault() != null ? getUserData().getEmployeeDTODefault().getId() : 0, getDateType(), null, getUserData().getUserBranch().getId()));
            crmEventDTO.setNumberOfNewEvent(cRMEventServiceDTO.findCrmEventListtCount(getUserData().getEmployeeDTODefault() != null ? getUserData().getEmployeeDTODefault().getId() : 0, getDateType(), 0, getUserData().getUserBranch().getId()));
            crmEventDTO.setNumberOfLateEvent(cRMEventServiceDTO.findCrmEventListtCount(getUserData().getEmployeeDTODefault() != null ? getUserData().getEmployeeDTODefault().getId() : 0, getDateType(), 2, getUserData().getUserBranch().getId()));
            crmEventDTO.setNumberOfNowEvent(cRMEventServiceDTO.findCrmEventListtCount(getUserData().getEmployeeDTODefault() != null ? getUserData().getEmployeeDTODefault().getId() : 0, getDateType(), 1, getUserData().getUserBranch().getId()));
//            crmEventDTO.setNumberOfEvent(cRMEventServiceDTO.numberOfEvent(getUserData().getUserBranch().getId(), getUserData().getEmployeeDTODefault() != null ? getUserData().getEmployeeDTODefault().getId() : null, 0));
//            crmEventDTO.setNumberOfToDo(cRMEventServiceDTO.numberOfEvent(getUserData().getUserBranch().getId(), getUserData().getEmployeeDTODefault() != null ? getUserData().getEmployeeDTODefault().getId() : null, 1));
//            crmEventDTO.setNumberOfEventAssigned(cRMEventServiceDTO.numberOfEventAndAssigned(getUserData().getUserBranch().getId(), getUserData().getEmployeeDTODefault() != null ? getUserData().getEmployeeDTODefault().getId() : null, 0));
//            crmEventDTO.setNumberOfToDoAssigned(cRMEventServiceDTO.numberOfEventAndAssigned(getUserData().getUserBranch().getId(), getUserData().getEmployeeDTODefault() != null ? getUserData().getEmployeeDTODefault().getId() : null, 1));
        }
        return crmEventDTO;
    }

    /**
     * @param crmEventDTO the crmEventDTO to set
     */
    public void setCrmEventDTO(CrmEventDTO crmEventDTO) {
        this.crmEventDTO = crmEventDTO;
    }

    /**
     * @return the instProjectGuideDTOList
     */
    public List<InstProjectGuideDTO> getInstProjectGuideDTOList() {
        if ((instProjectGuideDTOList == null || instProjectGuideDTOList.isEmpty())) {
            instProjectGuideDTOList = instProjectGuideService.getAllInstProjectGuidesDTO(getUserData().getUserBranch().getId());
            setInstProjectGuideDTOConverter(new InstProjectGuideDTOConverter(instProjectGuideDTOList));
        }
        return instProjectGuideDTOList;
    }

    /**
     * @param instProjectGuideDTOList the instProjectGuideDTOList to set
     */
    public void setInstProjectGuideDTOList(List<InstProjectGuideDTO> instProjectGuideDTOList) {
        this.instProjectGuideDTOList = instProjectGuideDTOList;
    }

    /**
     * @return the instProjectGuideDTOConverter
     */
    public InstProjectGuideDTOConverter getInstProjectGuideDTOConverter() {
        return instProjectGuideDTOConverter;
    }

    /**
     * @param instProjectGuideDTOConverter the instProjectGuideDTOConverter to
     * set
     */
    public void setInstProjectGuideDTOConverter(InstProjectGuideDTOConverter instProjectGuideDTOConverter) {
        this.instProjectGuideDTOConverter = instProjectGuideDTOConverter;
    }

    public void resetInvOrganizationSiteDTO() {
        setInvOrganizationSiteDTO(null);
        getInvOrganizationSiteDTO();
    }

    /**
     * @return the invOrganizationSiteDTO
     */
    public InvOrganizationSiteDTO getInvOrganizationSiteDTO() {
        if (invOrganizationSiteDTO == null) {
            invOrganizationSiteDTO = new InvOrganizationSiteDTO();
            invOrganizationSiteDTO.setType(5);
        }
        if (invOrganizationSiteDTO != null && getUri() != null && !getUri().contains("home")) {
            if (!getUri().contains("main") && !getUri().contains("crmLeads")) {
                invOrganizationSiteDTO.setNumberOfLeads(organizationSiteService.getNumberOfCustomerByBranchId(getUserData().getUserlogin().getBranchId().getId(), null, null, null, null));
                invOrganizationSiteDTO.setNumberOfNewLeads(organizationSiteService.getNumberOfNewCustomerByBranchId(getUserData().getUserlogin().getBranchId().getId(), null, null, null, null));
            }
//            invOrganizationSiteDTO.setNumberOfLeadsAssigned(organizationSiteService.getNumberOfCustomerByBranchIdAndAssignedTo(getUserData().getUserlogin().getBranchId().getId(), getUserData().getEmployeeDTODefault() != null ? getUserData().getEmployeeDTODefault().getId() : null, getUserData().getUserlogin().getId()));
//            invOrganizationSiteDTO.setNumberOfNewLeadsAssigned(organizationSiteService.getNumberOfNewCustomerByBranchIdAndAssignedTo(getUserData().getUserlogin().getBranchId().getId(), getUserData().getEmployeeDTODefault() != null ? getUserData().getEmployeeDTODefault().getId() : null, getUserData().getUserlogin().getId()));
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
     * @return the crmCustomerInterestedProductsDTOSelected
     */
    public CrmCustomerInterestedProductsDTO getCrmCustomerInterestedProductsDTOSelected() {
        return crmCustomerInterestedProductsDTOSelected;
    }

    /**
     * @param crmCustomerInterestedProductsDTOSelected the
     * crmCustomerInterestedProductsDTOSelected to set
     */
    public void setCrmCustomerInterestedProductsDTOSelected(CrmCustomerInterestedProductsDTO crmCustomerInterestedProductsDTOSelected) {
        this.crmCustomerInterestedProductsDTOSelected = crmCustomerInterestedProductsDTOSelected;
    }

    /**
     * @return the employeeDTOFollowerList
     */
    public List<EmployeeDTO> getEmployeeDTOFollowerList() {
        if ((employeeDTOFollowerList == null || employeeDTOFollowerList.isEmpty())) {
            employeeDTOFollowerList = employeeService.getAllEmployeesListByCompanyId(getUserData().getCompany().getId());
            setEmployeeDTOConverter(new EmployeeDTOConverter(employeeDTOFollowerList));
        }
        return employeeDTOFollowerList;
    }

    /**
     * @param employeeDTOFollowerList the employeeDTOFollowerList to set
     */
    public void setEmployeeDTOFollowerList(List<EmployeeDTO> employeeDTOFollowerList) {
        this.employeeDTOFollowerList = employeeDTOFollowerList;
    }

    /**
     * @return the employeeIsSupervisor
     */
    public Boolean getEmployeeIsSupervisor() {
        if (getUserData().getEmployeeDTODefault() != null) {
            employeeIsSupervisor = employeeService.getAllEmployeesIsSuperVisor(getUserData().getEmployeeDTODefault().getId());
        }
        getInvOrganizationSiteDTO().setEmployeeIsSupervisor(employeeIsSupervisor);
        return employeeIsSupervisor;
    }

    /**
     * @param employeeIsSupervisor the employeeIsSupervisor to set
     */
    public void setEmployeeIsSupervisor(Boolean employeeIsSupervisor) {
        this.employeeIsSupervisor = employeeIsSupervisor;
    }

    /**
     * @return the projectNameDTOList
     */
    public List<SymbolDTO> getProjectNameDTOList() {
        if (projectNameDTOList == null || projectNameDTOList.isEmpty()) {
            projectNameDTOList = symbolService.getSymbolListByCompanyId(getUserData().getCompany().getId(), 60);
            setProjectNameConverter(new SymbolDTOConverter(projectNameDTOList));
        }
        return projectNameDTOList;
    }

    /**
     * @param projectNameDTOList the projectNameDTOList to set
     */
    public void setProjectNameDTOList(List<SymbolDTO> projectNameDTOList) {
        this.projectNameDTOList = projectNameDTOList;
    }

    /**
     * @return the ProjectNameConverter
     */
    public SymbolDTOConverter getProjectNameConverter() {
        return ProjectNameConverter;
    }

    /**
     * @param ProjectNameConverter the ProjectNameConverter to set
     */
    public void setProjectNameConverter(SymbolDTOConverter ProjectNameConverter) {
        this.ProjectNameConverter = ProjectNameConverter;
    }

    /**
     * @return the invOrganizationSiteDTOConverter
     */
    public InvOrganizationSiteDTOConverter getInvOrganizationSiteDTOConverter() {
        return invOrganizationSiteDTOConverter;
    }

    /**
     * @param invOrganizationSiteDTOConverter the
     * invOrganizationSiteDTOConverter to set
     */
    public void setInvOrganizationSiteDTOConverter(InvOrganizationSiteDTOConverter invOrganizationSiteDTOConverter) {
        this.invOrganizationSiteDTOConverter = invOrganizationSiteDTOConverter;
    }

    /**
     * @return the invOrganizationSiteDTOList
     */
    public List<InvOrganizationSiteDTO> getInvOrganizationSiteDTOList() {
        if (invOrganizationSiteDTOList == null || invOrganizationSiteDTOList.isEmpty()) {
            invOrganizationSiteDTOList = autoCompleteService.autoCompleteOrganizationSiteDTO(getUserData().getUserlogin().getBranchId().getId(), Boolean.FALSE, 5, "", getUserData().getEmployeeDTODefault() != null ? getUserData().getEmployeeDTODefault().getId() : null);
            setInvOrganizationSiteDTOConverter(new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList));
        }
        return invOrganizationSiteDTOList;
    }

    /**
     * @param invOrganizationSiteDTOList the invOrganizationSiteDTOList to set
     */
    public void setInvOrganizationSiteDTOList(List<InvOrganizationSiteDTO> invOrganizationSiteDTOList) {
        this.invOrganizationSiteDTOList = invOrganizationSiteDTOList;
    }

    /**
     * @return the eventStatusDTOconverter
     */
    public SymbolDTOConverter getEventStatusDTOconverter() {
        return eventStatusDTOconverter;
    }

    /**
     * @param eventStatusDTOconverter the eventStatusDTOconverter to set
     */
    public void setEventStatusDTOconverter(SymbolDTOConverter eventStatusDTOconverter) {
        this.eventStatusDTOconverter = eventStatusDTOconverter;
    }

    /**
     * @return the eventStatusDTOList
     */
    public List<SymbolDTO> getEventStatusDTOList() {
        if (eventStatusDTOList == null || eventStatusDTOList.isEmpty()) {
            eventStatusDTOList = symbolService.getSymbolListByCompanyId(getUserData().getCompany().getId(), 61);
            setEventStatusDTOconverter(new SymbolDTOConverter(eventStatusDTOList));
        }
        return eventStatusDTOList;
    }

    /**
     * @param eventStatusDTOList the eventStatusDTOList to set
     */
    public void setEventStatusDTOList(List<SymbolDTO> eventStatusDTOList) {
        this.eventStatusDTOList = eventStatusDTOList;
    }

    /**
     * @return the dateNow
     */
    public Date getDateNow() {
        dateNow = new Date();
        return dateNow;
    }

    /**
     * @param dateNow the dateNow to set
     */
    public void setDateNow(Date dateNow) {
        this.dateNow = dateNow;
    }

    /**
     * @return the invOrganizationSiteDTOListByLeaders
     */
    public List<InvOrganizationSiteDTO> getInvOrganizationSiteDTOListByLeaders(String query) {
        if (query.equals(null)) {
            query = "";
        }
//        if (invOrganizationSiteDTOListByLeaders == null || invOrganizationSiteDTOListByLeaders.isEmpty()) {
        invOrganizationSiteDTOListByLeaders = autoCompleteService.autoCompleteOrganizationSiteWitAssignedList(getUserData().getUserlogin().getBranchId().getId(), Boolean.FALSE, 5, query, getUserData().getEmployeeDTODefault() != null ? getUserData().getEmployeeDTODefault().getId() : null);
        setInvOrganizationSiteDTOConverter(new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOListByLeaders));
//        }
        return invOrganizationSiteDTOListByLeaders;
    }

    /**
     * @param invOrganizationSiteDTOListByLeaders the
     * invOrganizationSiteDTOListByLeaders to set
     */
    public void setInvOrganizationSiteDTOListByLeaders(List<InvOrganizationSiteDTO> invOrganizationSiteDTOListByLeaders) {
        this.invOrganizationSiteDTOListByLeaders = invOrganizationSiteDTOListByLeaders;
    }

    /**
     * @return the crmEventDTOSearch
     */
    public CrmEventDTO getCrmEventDTOSearch() {
        if (crmEventDTOSearch == null) {
            crmEventDTOSearch = new CrmEventDTO();
        }
        return crmEventDTOSearch;
    }

    /**
     * @param crmEventDTOSearch the crmEventDTOSearch to set
     */
    public void setCrmEventDTOSearch(CrmEventDTO crmEventDTOSearch) {
        this.crmEventDTOSearch = crmEventDTOSearch;
    }

    /**
     * @return the dateType
     */
    public Integer getDateType() {
        if (dateType == null) {
            dateType = 0;
        }
        return dateType;
    }

    /**
     * @param dateType the dateType to set
     */
    public void setDateType(Integer dateType) {
        this.dateType = dateType;
    }

    /**
     * @return the leadsType
     */
    public Integer getLeadsType() {
        if (leadsType == null) {
            leadsType = 1;
        }
        return leadsType;
    }

    /**
     * @param leadsType the leadsType to set
     */
    public void setLeadsType(Integer leadsType) {
        this.leadsType = leadsType;
    }

    /**
     * @return the leadSelected
     */
    public Integer getLeadSelected() {
        return leadSelected;
    }

    /**
     * @param leadSelected the leadSelected to set
     */
    public void setLeadSelected(Integer leadSelected) {
        this.leadSelected = leadSelected;
    }

    /**
     * @return the tobyNotificationSettingDTO
     */
    public TobyNotificationSettingDTO getTobyNotificationSettingDTO() {
        if (tobyNotificationSettingDTO == null) {
            setTobyNotificationSettingDTO(tobyNotificationSettingService.getTobyNotificationSettingDTOByUserId(getUserData().getUser().getBranchId().getId(), getUserData().getUser().getId()));
        }
        return tobyNotificationSettingDTO;
    }

    /**
     * @param tobyNotificationSettingDTO the tobyNotificationSettingDTO to set
     */
    public void setTobyNotificationSettingDTO(TobyNotificationSettingDTO tobyNotificationSettingDTO) {
        this.tobyNotificationSettingDTO = tobyNotificationSettingDTO;
    }

    /**
     * @return the dataModel
     */
    public LazyDataModel<CrmEventDTO> getDataModel() {
        return dataModel;
    }

    /**
     * @param dataModel the dataModel to set
     */
    public void setDataModel(LazyDataModel<CrmEventDTO> dataModel) {
        this.dataModel = dataModel;
    }

    /**
     * @return the dataModelInvOrganization
     */
    public LazyDataModel<InvOrganizationSiteDTO> getDataModelInvOrganization() {
        return dataModelInvOrganization;
    }

    /**
     * @param dataModelInvOrganization the dataModelInvOrganization to set
     */
    public void setDataModelInvOrganization(LazyDataModel<InvOrganizationSiteDTO> dataModelInvOrganization) {
        this.dataModelInvOrganization = dataModelInvOrganization;
    }

    /**
     * @return the crmLeadsSearchBean
     */
    public CrmLeadsSearchBean getCrmLeadsSearchBean() {
        return crmLeadsSearchBean;
    }

    /**
     * @param crmLeadsSearchBean the crmLeadsSearchBean to set
     */
    public void setCrmLeadsSearchBean(CrmLeadsSearchBean crmLeadsSearchBean) {
        this.crmLeadsSearchBean = crmLeadsSearchBean;
    }

    /**
     * @return the crmSettingDTO
     */
    public CrmSettingDTO getCrmSettingDTO() {
        if (crmSettingDTO == null) {
            setCrmSettingDTO(crmSettingService.getCrmSettingDTOByUserId(getUserData().getUser().getBranchId().getId(), getUserData().getUser().getId()));
        }
        return crmSettingDTO;
    }

    /**
     * @param crmSettingDTO the crmSettingDTO to set
     */
    public void setCrmSettingDTO(CrmSettingDTO crmSettingDTO) {
        this.crmSettingDTO = crmSettingDTO;
    }

    /**
     * @return the notificationId
     */
    public String getNotificationId() {
        return notificationId;
    }

    /**
     * @param notificationId the notificationId to set
     */
    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    /**
     * @return the allEmployeeDTOList
     */
    public List<EmployeeDTO> getAllEmployeeDTOList() {
        if (allEmployeeDTOList == null || allEmployeeDTOList.isEmpty()) {
            allEmployeeDTOList = employeeService.getAllEmployeesListByCompanyId(getUserData().getUserlogin().getCompanyId().getId(), getUserData().getUserlogin().getBranchId().getId());
            setEmployeeDTOConverter(new EmployeeDTOConverter(allEmployeeDTOList));

        }

        return allEmployeeDTOList;
    }

    /**
     * @param allEmployeeDTO the allEmployeeDTOList to set
     */
    public void setAllEmployeeDTOList(List<EmployeeDTO> allEmployeeDTOList) {
        this.allEmployeeDTOList = allEmployeeDTOList;
    }

    /**
     * @return the employeeDTOTransferConverter
     */
    public EmployeeDTOConverter getEmployeeDTOTransferConverter() {
        return employeeDTOTransferConverter;
    }

    /**
     * @param employeeDTOTransferConverter the employeeDTOTransferConverter to
     * set
     */
    public void setEmployeeDTOTransferConverter(EmployeeDTOConverter employeeDTOTransferConverter) {
        this.employeeDTOTransferConverter = employeeDTOTransferConverter;
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
     * @return the crmTyprList
     */
    public List<SymbolDTO> getCrmTyprList() {
         if (crmTyprList == null || crmTyprList.isEmpty()) {
            crmTyprList = symbolService.getCrmEventTypeFromByCompanyIdDTO(getUserData().getCompany().getId());
            setCrmTypeConverter(new SymbolDTOConverter(crmTyprList));
        }
        return crmTyprList;
    }

    /**
     * @param crmTyprList the crmTyprList to set
     */
    public void setCrmTyprList(List<SymbolDTO> crmTyprList) {
        this.crmTyprList = crmTyprList;
    }

    /**
     * @return the crmTypeConverter
     */
    public SymbolDTOConverter getCrmTypeConverter() {
        return crmTypeConverter;
    }

    /**
     * @param crmTypeConverter the crmTypeConverter to set
     */
    public void setCrmTypeConverter(SymbolDTOConverter crmTypeConverter) {
        this.crmTypeConverter = crmTypeConverter;
    }

}
