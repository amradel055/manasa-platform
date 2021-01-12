package com.toby.invoiceProductionReport;

import com.toby.businessservice.InvPurchaseInvoiceService;
import com.toby.businessservice.InvoiceDetailReportService;
import com.toby.converter.InvPurchaseInvoiceDTOConverter;
import com.toby.dto.InvPurchaseInvoiceDTO;
import com.toby.dto.PrintProductionDTO;
import com.toby.toby.AutoComplete;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import com.toby.views.InvoiceDetailsReport;
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
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import net.sf.jasperreports.engine.JRException;


@Named(value = "invoiceDetailReportMB")
@ViewScoped
public class InvoiceDetailReportMB extends BaseListBean{
    
    
    private List<InvPurchaseInvoiceDTO> invPurchaseInvoiceDTOList;
    private List<InvoiceDetailsReport>  invoiceDetailsReports;
    private PrintProductionDTO printProductionDTO;
    
    private InvPurchaseInvoiceDTOConverter invPurchaseInvoiceDTOConverter;

  
    @EJB
    private InvPurchaseInvoiceService invPurchaseInvoiceService;
    @EJB
    private InvoiceDetailReportService invoiceDetailReportService;

    
    public InvoiceDetailReportMB() {
    }

    @Override
    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        getInvPurchaseInvoiceDTOList();
    }
    
//     public  List<InvPurchaseInvoiceDTO> completeInvpurchaseInvoice(String query){
//     
//     return AutoComplete.completeInvpurchaseInvoice(query, invPurchaseInvoiceDTOList, invPurchaseInvoiceDTOConverter);
//     }
    
    public void search() {
        if((printProductionDTO.getFromInvoice()!=null&&printProductionDTO.getToInvoice()!=null)
                ||(printProductionDTO.getFromDate()!=null&&printProductionDTO.getToDate()!=null)){
          invoiceDetailsReports = invoiceDetailReportService.searchReport(printProductionDTO, getUserData().getUser());
        
        }else{
               printProductionDTO.setMsg("ادخل من فاتوره الي فاتوره او من تاريخ الي تاريخ " );
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", printProductionDTO.getMsg()));
      
    }}
    
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (invoiceDetailsReports != null && !invoiceDetailsReports.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "invoiceDetailReport.jasper", invoiceDetailsReports, "pdf");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا توجد نتائج للطباعة"));
        }
    }
    
    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();
        
        hashMap.put("fromInvoice", printProductionDTO.getFromInvoice().getId());
        hashMap.put("toInvoice", printProductionDTO.getToInvoice().getId());
        hashMap.put("toDate", printProductionDTO.getToDate());
        hashMap.put("fromDate", printProductionDTO.getFromDate());
        hashMap.put("fromInvoiceText", "من فاتورة");
        hashMap.put("toInvoiceText", "إلى فاتورة");
        hashMap.put("toDateText", "الي تاريخ");
        hashMap.put("fromDateText", "من تاريخ");
        
        hashMap.put("invoiceText", "الفاتورة");
        hashMap.put("branchText", "الفرع");
        hashMap.put("dateText", "التاريخ");
        hashMap.put("clientText","العميل");
        hashMap.put("clientNameText", "اسم العميل");
        hashMap.put("quantityText", "الكمية");
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
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void filter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToAdd() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String goToEdit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public PrintProductionDTO getPrintProductionDTO() {
        if (printProductionDTO == null) {
            printProductionDTO = new PrintProductionDTO();
        }
        return printProductionDTO;
    }

    public void setPrintProductionDTO(PrintProductionDTO printProductionDTO) {
        this.printProductionDTO = printProductionDTO;
    }

    public List<InvoiceDetailsReport> getInvoiceDetailsReports() {
        return invoiceDetailsReports;
    }

   
    public void setInvoiceDetailsReports(List<InvoiceDetailsReport> invoiceDetailsReports) {
        this.invoiceDetailsReports = invoiceDetailsReports;
    }

    public InvPurchaseInvoiceDTOConverter getInvPurchaseInvoiceDTOConverter() {
        return invPurchaseInvoiceDTOConverter;
    }

    
    public void setInvPurchaseInvoiceDTOConverter(InvPurchaseInvoiceDTOConverter invPurchaseInvoiceDTOConverter) {
        this.invPurchaseInvoiceDTOConverter = invPurchaseInvoiceDTOConverter;
    }
  
    public List<InvPurchaseInvoiceDTO> getInvPurchaseInvoiceDTOList() {
        if (invPurchaseInvoiceDTOList == null || invPurchaseInvoiceDTOList.isEmpty()) {
//            invPurchaseInvoiceDTOList = invPurchaseInvoiceService.findInvPurchaseInvoiceDTOList(getUserData().getUser());
            setInvPurchaseInvoiceDTOConverter(new InvPurchaseInvoiceDTOConverter(invPurchaseInvoiceDTOList));
        }
        return invPurchaseInvoiceDTOList;
    }

    
    public void setInvPurchaseInvoiceDTOList(List<InvPurchaseInvoiceDTO> invPurchaseInvoiceDTOList) {
        this.invPurchaseInvoiceDTOList = invPurchaseInvoiceDTOList;
    }
    
}
