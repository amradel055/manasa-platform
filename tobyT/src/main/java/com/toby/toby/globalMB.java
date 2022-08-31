/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.toby;

import com.toby.businessservice.CRMEventServiceDTO;
import com.toby.businessservice.EmployeeService;
import com.toby.businessservice.InvPurchaseInvoiceService_1;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.SymbolService;
import com.toby.converter.EmployeeDTOConverter;
import com.toby.converter.SymbolDTOConverter;
import com.toby.dto.CrmCustomerInterestedProductsDTO;
import com.toby.dto.CrmEventDTO;
import com.toby.dto.EmployeeDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.SymbolDTO;
import java.util.ArrayList;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

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
    private LazyDataModel<InvInvoiceDTO> dataModel;

    private CrmEventDTO crmEventDTO;

    private String customerCode;

    private Integer gallaryId;

    private List<InvInvoiceDTO> customerInvoiceList;

    private List<EmployeeDTO> crmEmployeeList;
    private List<SymbolDTO> crmCRMTypeList;
    private List<SymbolDTO> crmPeriorityList;

    private EmployeeDTOConverter crmEmployeeConverter;
    private SymbolDTOConverter crmTypeConverter;
    private SymbolDTOConverter crmPeriorityConverter;

    /**
     * Creates a new instance of LoginBean
     */
    @EJB
    private OrganizationSiteService organizationSiteService;

    @EJB
    private SymbolService symbolService;

    @EJB
    EmployeeService employeeService;

    @EJB
    private InvPurchaseInvoiceService_1 invPurchaseInvoiceService;

    @EJB
    private CRMEventServiceDTO cRMEventServiceDTO;

    @PostConstruct
    public void init() {

        System.out.println("");
    }

    public void showAddDialog() {
        OpenDlg("dlg12add");
    }

    public void showAddEventDialog() {
        loadData();
        OpenDlg("dlgeventadd");
    }

    public void reset() {
        invOrganizationSiteDTO = new InvOrganizationSiteDTO();
    }

    public void findCustomer() {
        if (getGallaryId() != null && getCustomerCode() != null && !getCustomerCode().isEmpty()) {
            getCrmEventDTO().setCustomerId(organizationSiteService.findOrganizationSiteDTOByCodeAndGallaryIdDTO(getUserData().getUserBranch().getId(), getCustomerCode(), getGallaryId()));
            findCustomerInvoices();
        }
    }

    public void findCustomerInvoices() {
        if (getCrmEventDTO() != null && getCrmEventDTO().getCustomerId() != null && getCrmEventDTO().getCustomerId().getId() != null) {
            setCustomerInvoiceList(invPurchaseInvoiceService.getALLInvPurchaseInvoiceDTOByTypeAndBranchIdFroGallary2(4, null, getCrmEventDTO().getCustomerId().getId()));
        }
    }

    public void saveEvent() {
        setCrmEventDTO(cRMEventServiceDTO.save(getCrmEventDTO(), getUserData().getUser()));
        if (getCrmEventDTO().getMsg() != null && !getCrmEventDTO().getMsg().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), getCrmEventDTO().getMsg()));
        } else {
            loadData();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), "تم الحفظ بنجاح"));
        }
    }

    public String validateSaveLead(InvOrganizationSiteDTO invOrganizationSiteDTO) {
        StringBuilder error = new StringBuilder();
        return organizationSiteService.validateSmallSaveLead(error, invOrganizationSiteDTO, getUserData().getUser());
    }

    public void loadGallaryList() {
        try {
            if (getGallaryDTOSelect() != null && getGallaryDTOSelect().getId() != null && getGallaryDTOSelect().getId() > 0) {
                load();
            } else {
            }
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "loadInventoryList");
        }

    }

    public void loadData() {
        setCrmEventDTO(new CrmEventDTO());
        getCrmEventDTO().setAssignedTo((getCrmEmployeeList() == null || getCrmEmployeeList().isEmpty()) ? null : getCrmEmployeeList().get(0));
        getCrmEventDTO().setStatus((getCrmCRMTypeList() == null || getCrmCRMTypeList().isEmpty()) ? null : getCrmCRMTypeList().get(0));
        getCrmEventDTO().setSymbolPriority((getCrmPeriorityList() == null || getCrmPeriorityList().isEmpty()) ? null : getCrmPeriorityList().get(0));
    }

    public void load() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            setUserData((UserData) context.getSessionMap().get("userLogInData"));
            this.setDataModel(new LazyDataModel<InvInvoiceDTO>() {
                private static final long serialVersionUID = 1L;

                @Override
                public List<InvInvoiceDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                    Integer type = 0;
                    Boolean isGallary = true;
                    if (getUri().contains("invpurchaseinvoicelist")) {
                        type = 0;
                        isGallary = false;
                    } else if (getUri().contains("invsalesinvoicelist")) {
                        type = 1;
                    } else if (getUri().contains("invreturnpurchaselist")) {
                        type = 2;
                        isGallary = false;
                    } else if (getUri().contains("invreturnsaleslist")) {
                        type = 3;
                    } else if (getUri().contains("proworkorderlist")) {
                        type = 4;
                    } else if (getUri().contains("invpurchaseaddinglist")) {
                        type = 5;
                    }
                    Integer invInventorySelectionId = getInventoryDTOSelect() != null ? getInventoryDTOSelect().getId() : null;
                    Integer invGallarySelectionId = getGallaryDTOSelect() != null ? getGallaryDTOSelect().getId() : null;
                    if (isGallary) {
                        setRowCount(invPurchaseInvoiceService.getTotalRegistorsForGallary(type, invGallarySelectionId, getUserData().getUserBranch().getId(), filters, isGallary).intValue());
                        return invPurchaseInvoiceService.getALLInvPurchaseInvoiceDTOByTypeAndBranchIdFroGallary(type, invGallarySelectionId, first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), getUserData().getUserBranch().getId(), filters, isGallary);
                    } else {
                        setRowCount(invPurchaseInvoiceService.getTotalRegistorsForGallary(type, invInventorySelectionId, getUserData().getUserBranch().getId(), filters, isGallary).intValue());
                        return invPurchaseInvoiceService.getALLInvPurchaseInvoiceDTOByTypeAndBranchIdFroGallary(type, invInventorySelectionId, first, pageSize, sortField, sortOrder.ASCENDING.equals(sortOrder), getUserData().getUserBranch().getId(), filters, isGallary);
                    }
                }
            });
        } catch (Exception e) {
            saveError(e, "InvPurchaseInvoiceListMB", "load");
        }
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
            getInvOrganizationSiteDTO().setGallaryIdDTO(getGallaryDTOSelect());
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

    /**
     * @return the dataModel
     */
    public LazyDataModel<InvInvoiceDTO> getDataModel() {
        return dataModel;
    }

    /**
     * @param dataModel the dataModel to set
     */
    public void setDataModel(LazyDataModel<InvInvoiceDTO> dataModel) {
        this.dataModel = dataModel;
    }

    /**
     * @return the customerCode
     */
    public String getCustomerCode() {
        return customerCode;
    }

    /**
     * @param customerCode the customerCode to set
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    /**
     * @return the gallaryId
     */
    public Integer getGallaryId() {
        return gallaryId;
    }

    /**
     * @param gallaryId the gallaryId to set
     */
    public void setGallaryId(Integer gallaryId) {
        this.gallaryId = gallaryId;
    }

    /**
     * @return the crmEmployeeList
     */
    public List<EmployeeDTO> getCrmEmployeeList() {
        if (crmEmployeeList == null || crmEmployeeList.isEmpty()) {
            crmEmployeeList = employeeService.getAllCRMEmployee(getUserData().getUserBranch().getId());
            setCrmEmployeeConverter(new EmployeeDTOConverter(employeeDTOList));
        }
        return crmEmployeeList;
    }

    /**
     * @param crmEmployeeList the crmEmployeeList to set
     */
    public void setCrmEmployeeList(List<EmployeeDTO> crmEmployeeList) {
        this.crmEmployeeList = crmEmployeeList;
    }

    /**
     * @return the crmCRMTypeList
     */
    public List<SymbolDTO> getCrmCRMTypeList() {
        if (crmCRMTypeList == null || crmCRMTypeList.isEmpty()) {
            crmCRMTypeList = symbolService.getCrmEventTypeFromByCompanyIdDTO(getUserData().getCompany().getId());
            setCrmTypeConverter(new SymbolDTOConverter(crmCRMTypeList));
        }
        return crmCRMTypeList;
    }

    /**
     * @param crmCRMTypeList the crmCRMTypeList to set
     */
    public void setCrmCRMTypeList(List<SymbolDTO> crmCRMTypeList) {
        this.crmCRMTypeList = crmCRMTypeList;
    }

    /**
     * @return the crmPeriorityList
     */
    public List<SymbolDTO> getCrmPeriorityList() {
        if (crmPeriorityList == null || crmPeriorityList.isEmpty()) {
            crmPeriorityList = symbolService.getCrmPeriorityByCompanyIdDTO(getUserData().getCompany().getId());
            setCrmPeriorityConverter(new SymbolDTOConverter(crmPeriorityList));
        }
        return crmPeriorityList;
    }

    /**
     * @param crmPeriorityList the crmPeriorityList to set
     */
    public void setCrmPeriorityList(List<SymbolDTO> crmPeriorityList) {
        this.crmPeriorityList = crmPeriorityList;
    }

    /**
     * @return the crmEmployeeConverter
     */
    public EmployeeDTOConverter getCrmEmployeeConverter() {
        return crmEmployeeConverter;
    }

    /**
     * @param crmEmployeeConverter the crmEmployeeConverter to set
     */
    public void setCrmEmployeeConverter(EmployeeDTOConverter crmEmployeeConverter) {
        this.crmEmployeeConverter = crmEmployeeConverter;
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

    /**
     * @return the crmPeriorityConverter
     */
    public SymbolDTOConverter getCrmPeriorityConverter() {
        return crmPeriorityConverter;
    }

    /**
     * @param crmPeriorityConverter the crmPeriorityConverter to set
     */
    public void setCrmPeriorityConverter(SymbolDTOConverter crmPeriorityConverter) {
        this.crmPeriorityConverter = crmPeriorityConverter;
    }

    /**
     * @return the crmEventDTO
     */
    public CrmEventDTO getCrmEventDTO() {
        if (crmEventDTO == null) {
            crmEventDTO = new CrmEventDTO();
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
     * @return the customerInvoiceList
     */
    public List<InvInvoiceDTO> getCustomerInvoiceList() {
        return customerInvoiceList;
    }

    /**
     * @param customerInvoiceList the customerInvoiceList to set
     */
    public void setCustomerInvoiceList(List<InvInvoiceDTO> customerInvoiceList) {
        this.customerInvoiceList = customerInvoiceList;
    }

}
