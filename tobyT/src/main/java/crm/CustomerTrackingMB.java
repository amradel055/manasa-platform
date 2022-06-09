/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crm;

import com.toby.businessservice.CRMEventServiceDTO;
import com.toby.toby.*;
import com.toby.businessservice.EmployeeService;
import com.toby.businessservice.InvPurchaseInvoiceService_1;
import com.toby.businessservice.InvoiceProductionStagesReportService;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.SymbolService;
import com.toby.bussinessservice.global.AutoCompleteService;
import com.toby.converter.InvOrganizationSiteDTOConverter;
import com.toby.converter.SymbolDTOConverter;
import com.toby.dto.CrmEventDTO;
import com.toby.dto.EmployeeDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.PrintProductionDTO;
import com.toby.dto.SymbolDTO;
import com.toby.views.InvoiceProductionStagesReport;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author hq004
 */
@Named(value = "CustomerTrackingMB")
@ViewScoped
public class CustomerTrackingMB extends Basic implements Serializable {

    private InvOrganizationSiteDTO invOrganizationSiteDTO;

    private Boolean errorMsgbool = Boolean.TRUE;

    private String errorMsg;

    private InvOrganizationSiteDTOConverter invOrganizationSiteDTOConverter;

    private List<InvOrganizationSiteDTO> invOrganizationSiteDTOList;

    private List<InvInvoiceDTO> invInvoiceList;

    private InvInvoiceDTO invInvoiceDTOSelected;

    private PrintProductionDTO printProductionDTO;

    private List<InvoiceProductionStagesReport> invoiceProductionStagesReports;

    private List<CrmEventDTO> listEveentMainScrren;

    private Integer dateType;

    private Boolean employeeIsSupervisor = Boolean.FALSE;

    private CrmEventDTO crmEventDTO;

    private SymbolDTOConverter activityTypeDTOconverter;

    private List<SymbolDTO> activityTypeDTOList;

    private SymbolDTOConverter eventStatusDTOconverter;

    private List<SymbolDTO> eventStatusDTOList;

    private Date lastInvoiceDate;
    private BigDecimal lastInvoiceValue;

    /**
     * Creates a new instance of LoginBean
     */
    @EJB
    private OrganizationSiteService organizationSiteService;

    @EJB
    EmployeeService employeeService;

    @EJB
    AutoCompleteService autoCompleteService;

    @EJB
    private InvPurchaseInvoiceService_1 invPurchaseInvoiceService;

    @EJB
    private InvoiceProductionStagesReportService invoiceProductionStagesReportService;

    @EJB
    CRMEventServiceDTO cRMEventServiceDTO;

    @EJB
    SymbolService symbolService;

    public void search() {
        if (getInvOrganizationSiteDTO() != null && getInvOrganizationSiteDTO().getId() != null) {
            setListEveentMainScrren(new ArrayList<>());
            Integer type = 4;
            setInvInvoiceList(invPurchaseInvoiceService.getALLInvPurchaseInvoiceDTOByTypeAndBranchIdFroGallary2(type, null, getInvOrganizationSiteDTO().getId()));
            if (getListEveentMainScrren() == null || getListEveentMainScrren().isEmpty() ) {
                setListEveentMainScrren(cRMEventServiceDTO.findALLEventByCustomerId(getInvOrganizationSiteDTO().getId(), getUserData().getUserBranch().getId()));
            }
            if (getInvInvoiceList() != null && !getInvInvoiceList().isEmpty()) {
                setLastInvoiceDate(getInvInvoiceList().get(0).getDate());
                setLastInvoiceValue(getInvInvoiceList().get(0).getValue());
            }
        }
    }

    public void FillEventByCustomerId() {

    }

    public void searchProductionStagesReport(SelectEvent event) {
        try {

            invInvoiceDTOSelected = (InvInvoiceDTO) event.getObject();
            getPrintProductionDTO().setFromInvInvoice(invInvoiceDTOSelected.getSerial());
            if ((printProductionDTO.getFromInvInvoice() != null)) {
//            InvInvoiceDTO invoiceDTO = invPurchaseInvoiceService.getInvPurchaseInvoiceDTOBySerial(printProductionDTO.getFromInvInvoice(), getUserData().getUserBranch().getId());
                getPrintProductionDTO().setFromInvoice(invInvoiceDTOSelected);
                invoiceProductionStagesReports = invoiceProductionStagesReportService.searchReport(printProductionDTO, getUserData().getUserBranch().getId());
            } else {
                printProductionDTO.setMsg("ادخل رقم فاتوره ");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", printProductionDTO.getMsg()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTODOValue() {
        getCrmEventDTO().setType(1);
    }

    public void updateEventValue() {
        getCrmEventDTO().setType(0);
    }

    public void reset() {
        invOrganizationSiteDTO = new InvOrganizationSiteDTO();
    }

    public void resetEvent() {
        setCrmEventDTO(new CrmEventDTO());
    }

    public String saveEvent() {
        if (getInvOrganizationSiteDTO() != null) {
            getCrmEventDTO().setCustomerId(getInvOrganizationSiteDTO());
        }
        if (!getEmployeeIsSupervisor()) {
            getCrmEventDTO().setAssignedTo(getUserData().getEmployeeDTODefault());
        }
        String error = validateSave(getCrmEventDTO());
        if (error != null && !error.isEmpty()) {
            errorMessage(error);
            errorMsgbool = Boolean.TRUE;
        } else {
            errorMsgbool = Boolean.FALSE;
            getCrmEventDTO().setMsg(null);
            EmployeeDTO employeeDTO = new EmployeeDTO();
            employeeDTO.setId(1);
            getCrmEventDTO().setAssignedTo(employeeDTO);
            setCrmEventDTO(cRMEventServiceDTO.save(getCrmEventDTO(), getUserData().getUser()));
            msgSave(getCrmEventDTO().getMsg());

            //update Crm Notification
//            if (getCrmEventDTO().getNotificationDate() != null && getCrmEventDTO().getNotificationDate().before(endDate())) {
//                getUserData().setCrmNotification(cRMEventServiceDTO.findALLCrmNotification(getUserData().getUser().getBranchId().getId(), getUserData().getEmployeeDTODefault().getId()));
//            }
        }
        return "";
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

    public String validateSave(CrmEventDTO crmEventDTO) {
        String error = cRMEventServiceDTO.validateSave(crmEventDTO);
        return error;
    }

    public List<SymbolDTO> completeEventStatus(String query) {
        return AutoComplete.completeEventStatus(query, getEventStatusDTOList(), getEventStatusDTOconverter());
    }

    public List<SymbolDTO> completeActivityType(String query) {
        return AutoComplete.completeActivityType(query, getActivityTypeDTOList(), getActivityTypeDTOconverter());
    }

    public List<InvOrganizationSiteDTO> completeCustomerDTO(String query) {

        invOrganizationSiteDTOList = autoCompleteService.autoCompleteOrganizationSiteDTO(getUserData().getUserBranch().getId(), Boolean.TRUE, null, query, null);
        invOrganizationSiteDTOConverter = new InvOrganizationSiteDTOConverter(invOrganizationSiteDTOList);
        return invOrganizationSiteDTOList;

    }

    /**
     * @return the invOrganizationSiteDTO
     */
    public InvOrganizationSiteDTO getInvOrganizationSiteDTO() {
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
        if (invOrganizationSiteDTOList == null) {
            invOrganizationSiteDTOList = new ArrayList<>();
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
     * @return the invInvoiceList
     */
    public List<InvInvoiceDTO> getInvInvoiceList() {
        return invInvoiceList;
    }

    /**
     * @param invInvoiceList the invInvoiceList to set
     */
    public void setInvInvoiceList(List<InvInvoiceDTO> invInvoiceList) {
        this.invInvoiceList = invInvoiceList;
    }

    /**
     * @return the printProductionDTO
     */
    public PrintProductionDTO getPrintProductionDTO() {
        if (printProductionDTO == null) {
            printProductionDTO = new PrintProductionDTO();
        }
        return printProductionDTO;
    }

    /**
     * @param printProductionDTO the printProductionDTO to set
     */
    public void setPrintProductionDTO(PrintProductionDTO printProductionDTO) {
        this.printProductionDTO = printProductionDTO;
    }

    /**
     * @return the invoiceProductionStagesReports
     */
    public List<InvoiceProductionStagesReport> getInvoiceProductionStagesReports() {
        return invoiceProductionStagesReports;
    }

    /**
     * @param invoiceProductionStagesReports the invoiceProductionStagesReports
     * to set
     */
    public void setInvoiceProductionStagesReports(List<InvoiceProductionStagesReport> invoiceProductionStagesReports) {
        this.invoiceProductionStagesReports = invoiceProductionStagesReports;
    }

    /**
     * @return the invInvoiceDTOSelected
     */
    public InvInvoiceDTO getInvInvoiceDTOSelected() {
        return invInvoiceDTOSelected;
    }

    /**
     * @param invInvoiceDTOSelected the invInvoiceDTOSelected to set
     */
    public void setInvInvoiceDTOSelected(InvInvoiceDTO invInvoiceDTOSelected) {
        this.invInvoiceDTOSelected = invInvoiceDTOSelected;
    }

    /**
     * @return the listEveentMainScrren
     */
    public List<CrmEventDTO> getListEveentMainScrren() {
//        if (listEveentMainScrren == null || listEveentMainScrren.isEmpty()) {
//            listEveentMainScrren = new ArrayList<>();
//        }
//        if (getUserData() != null) {
//            listEveentMainScrren = cRMEventServiceDTO.findCrmEventListWithDate(getUserData().getUserlogin().getBranchId().getId(), null, getUserData().getUser().getId(), getDateType(), null);
//        }
        return listEveentMainScrren;
    }

    /**
     * @param listEveentMainScrren the listEveentMainScrren to set
     */
    public void setListEveentMainScrren(List<CrmEventDTO> listEveentMainScrren) {
        this.listEveentMainScrren = listEveentMainScrren;
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
     * @return the lastInvoiceDate
     */
    public Date getLastInvoiceDate() {

        return lastInvoiceDate;
    }

    /**
     * @param lastInvoiceDate the lastInvoiceDate to set
     */
    public void setLastInvoiceDate(Date lastInvoiceDate) {
        this.lastInvoiceDate = lastInvoiceDate;
    }

    /**
     * @return the lastInvoiceValue
     */
    public BigDecimal getLastInvoiceValue() {

        return lastInvoiceValue;
    }

    /**
     * @param lastInvoiceValue the lastInvoiceValue to set
     */
    public void setLastInvoiceValue(BigDecimal lastInvoiceValue) {
        this.lastInvoiceValue = lastInvoiceValue;
    }

}
