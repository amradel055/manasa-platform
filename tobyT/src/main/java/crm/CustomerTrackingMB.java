/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crm;

import com.toby.toby.*;
import com.toby.businessservice.EmployeeService;
import com.toby.businessservice.InvPurchaseInvoiceService_1;
import com.toby.businessservice.InvoiceProductionStagesReportService;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import com.toby.businessservice.OrganizationSiteService;
import com.toby.bussinessservice.global.AutoCompleteService;
import com.toby.converter.InvOrganizationSiteDTOConverter;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.PrintProductionDTO;
import com.toby.views.InvoiceProductionStagesReport;
import java.util.ArrayList;
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
 
    private InvInvoiceDTO  invInvoiceDTOSelected ;
    
    private PrintProductionDTO printProductionDTO;
       
    private List<InvoiceProductionStagesReport> invoiceProductionStagesReports;
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
      
    public void search() {
        if ( getInvOrganizationSiteDTO() != null && getInvOrganizationSiteDTO().getId() != null ){
        Integer type = 4 ;
        
        setInvInvoiceList(invPurchaseInvoiceService.getALLInvPurchaseInvoiceDTOByTypeAndBranchIdFroGallary2(type, null , getInvOrganizationSiteDTO().getId()));
        }
    }
    
    
        public void searchProductionStagesReport(SelectEvent event) {
            try{
           
            invInvoiceDTOSelected = (InvInvoiceDTO) event.getObject() ;
            getPrintProductionDTO().setFromInvInvoice(invInvoiceDTOSelected.getSerial());
        if ((printProductionDTO.getFromInvInvoice() != null)) {
//            InvInvoiceDTO invoiceDTO = invPurchaseInvoiceService.getInvPurchaseInvoiceDTOBySerial(printProductionDTO.getFromInvInvoice(), getUserData().getUserBranch().getId());
            getPrintProductionDTO().setFromInvoice(invInvoiceDTOSelected);
            invoiceProductionStagesReports = invoiceProductionStagesReportService.searchReport(printProductionDTO, getUserData().getUserBranch().getId());
        } else {
            printProductionDTO.setMsg("ادخل رقم فاتوره ");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", printProductionDTO.getMsg()));
        } } 
            catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void reset() {
        invOrganizationSiteDTO = new InvOrganizationSiteDTO();
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
        if(printProductionDTO == null){
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
     * @param invoiceProductionStagesReports the invoiceProductionStagesReports to set
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


}
