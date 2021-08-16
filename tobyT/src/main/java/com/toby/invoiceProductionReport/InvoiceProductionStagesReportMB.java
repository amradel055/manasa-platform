package com.toby.invoiceProductionReport;

import com.toby.businessservice.InvPurchaseInvoiceService;
import com.toby.businessservice.InvPurchaseInvoiceService_1;
import com.toby.businessservice.InvoiceMovementReportService;
import com.toby.businessservice.InvoiceProductionStagesReportService;
import com.toby.businessservice.ProProductionStagesService;
import com.toby.converter.InvPurchaseInvoiceDTOConverter;
import com.toby.converter.ProProductionStagesDTOConverter;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.InvPurchaseInvoiceDTO;
import com.toby.dto.PrintProductionDTO;
import com.toby.dto.ProProductionStagesDTO;
import com.toby.toby.AutoComplete;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import com.toby.views.InvoiceMovementReport;
import com.toby.views.InvoiceProductionStagesReport;
import java.io.IOException;
import java.util.ArrayList;
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
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

@Named(value = "invoiceProductionStagesReportMB")
@ViewScoped
public class InvoiceProductionStagesReportMB extends BaseListBean {
    
    private List<ProProductionStagesDTO> proProductionStagesDTOList;
    private List<InvPurchaseInvoiceDTO> invPurchaseInvoiceDTOList;
    private List<InvoiceProductionStagesReport> invoiceProductionStagesReports;
    private PrintProductionDTO printProductionDTO;
    
    private ProProductionStagesDTOConverter proProductionStagesDTOConverter;
    private InvPurchaseInvoiceDTOConverter invPurchaseInvoiceDTOConverter;
    
    private InvoiceMovementReport invoiceMovementReport;
    
    @EJB
    private ProProductionStagesService proProductionStagesService;
    @EJB
    private InvPurchaseInvoiceService_1 invPurchaseInvoiceService;
    @EJB
    private InvoiceProductionStagesReportService invoiceProductionStagesReportService;
    @EJB
    private InvoiceMovementReportService invoiceMovementReportService;
    
    public InvoiceProductionStagesReportMB() {
    }
    
    @Override
    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        proProductionStagesDTOList = new ArrayList<>();
        getInvPurchaseInvoiceDTOList();
        
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("proproductionreportFormAdd")) {
            getPrintProductionDTO().setFromInvoice((InvInvoiceDTO) getContext().getSessionMap().get("proSalesFormSelected"));
            getPrintProductionDTO().setFromInvInvoice(getPrintProductionDTO().getFromInvoice().getSerial());
            findInvoiceDate();
            search();
        }
        
    }
//     public  List<InvPurchaseInvoiceDTO> completeInvpurchaseInvoice(String query){
//     
//     return AutoComplete.completeInvpurchaseInvoice(query, invPurchaseInvoiceDTOList, invPurchaseInvoiceDTOConverter);
//     }

    public void search() {
        if ((printProductionDTO.getFromInvInvoice() != null)) {
            InvInvoiceDTO invoiceDTO = invPurchaseInvoiceService.getInvPurchaseInvoiceDTOBySerial(printProductionDTO.getFromInvInvoice(), getUserData().getUserBranch().getId());
            getPrintProductionDTO().setFromInvoice(invoiceDTO);
            invoiceProductionStagesReports = invoiceProductionStagesReportService.searchReport(printProductionDTO, getUserData().getUserBranch().getId());
        } else {
            printProductionDTO.setMsg("ادخل رقم فاتوره ");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", printProductionDTO.getMsg()));
        }
    }
    
    public void convertDateFrom() {
        getPrintProductionDTO().setFromDate(findDateFromHijriFrom());
    }
    
    public void convertDateTo() {
        getPrintProductionDTO().setToDate(findDateFromHijriTo());
    }
    
    public void postProcessXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow header = sheet.getRow(0);
        
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            HSSFCell cell = header.getCell(i);
            
            cell.setCellStyle(cellStyle);
        }
    }
    
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (invoiceProductionStagesReports != null && !invoiceProductionStagesReports.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "invoiceProductionStage.jasper", invoiceProductionStagesReports, "pdf");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا توجد نتائج للطباعة"));
        }
    }
    
    public void findInvoiceDate() {
        PrintProductionDTO printProductionDTOSearch = new PrintProductionDTO();
        printProductionDTOSearch.setFromInvInvoice(printProductionDTO.getFromInvInvoice());
        printProductionDTOSearch.setToInvInvoice(printProductionDTO.getFromInvInvoice());
        List<InvoiceMovementReport> invoiceMovementReportList = invoiceMovementReportService.searchReport(printProductionDTOSearch, getUserData().getUserBranch().getId(),null);
        if (invoiceMovementReportList != null && !invoiceMovementReportList.isEmpty()) {
            setInvoiceMovementReport(invoiceMovementReportList.get(0));
        }
    }
    
    public HashMap prepareReport() {
        HashMap hashMap = new HashMap();
        
        hashMap.put("branchName", getUserData().getUserBranch() == null ? null : getUserData().getUserBranch().getNameAr());
        hashMap.put("PrintedBy", getUserData().getUser() == null ? null : getUserData().getUser().getName());
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        
        hashMap.put("fromInvInvoiceText", "  رقم الفاتورة");
        hashMap.put("fromInvInvoiceValue", printProductionDTO.getFromInvInvoice() == null ? null :  printProductionDTO.getFromInvInvoice());
        hashMap.put("fromDateText", "من تاريخ");
        hashMap.put("fromDateValue", printProductionDTO.getFromDate());
        hashMap.put("toDateText", "الى تاريخ");
        hashMap.put("toDateValue", printProductionDTO.getToDate());
        hashMap.put("clientNameText", "اسم العميل");
        hashMap.put("clientNameValue", invoiceMovementReport.getClientName() == null ? null : invoiceMovementReport.getClientName());
        hashMap.put("factoryDateText", " تاريخ الاستلام بالمشغل ");
        hashMap.put("factoryDateValue", invoiceMovementReport.getFactoryDate());
        hashMap.put("deliveryDateText", " تاريخ التسليم للطيار");
        hashMap.put("deliveryDateValue", invoiceMovementReport.getDeliveryDate());
        hashMap.put("galaryDateText", " تاريخ الاستلام بالمعرض");
        hashMap.put("galaryDateValue", invoiceMovementReport.getGalaryDate());
        hashMap.put("clientDateText", " تاريخ التسليم للعميل");
        hashMap.put("clientDateValue", invoiceMovementReport.getClientDate());
        hashMap.put("numberText", " عدد الثياب");
        hashMap.put("dateText", "  التاريخ");
        hashMap.put("quantityText", "الكمية");
        hashMap.put("stageText", "المرحلة");
        hashMap.put("empNameText", "الموظف");
        
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
    
    public List<ProProductionStagesDTO> getProProductionStagesDTOList() {
        if (proProductionStagesDTOList == null | proProductionStagesDTOList.isEmpty()) {
            proProductionStagesDTOList = proProductionStagesService.getAllProproductionStages(getUserData().getUser(), getUserData().getUserBranch().getId());
        }
        return proProductionStagesDTOList;
    }
    
    public void setProProductionStagesDTOList(List<ProProductionStagesDTO> proProductionStagesDTOList) {
        this.proProductionStagesDTOList = proProductionStagesDTOList;
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
    
    public PrintProductionDTO getPrintProductionDTO() {
        if (printProductionDTO == null) {
            printProductionDTO = new PrintProductionDTO();
        }
        return printProductionDTO;
        
    }
    
    public void setPrintProductionDTO(PrintProductionDTO printProductionDTO) {
        this.printProductionDTO = printProductionDTO;
    }
    
    public ProProductionStagesDTOConverter getProProductionStagesDTOConverter() {
        return proProductionStagesDTOConverter;
    }
    
    public void setProProductionStagesDTOConverter(ProProductionStagesDTOConverter proProductionStagesDTOConverter) {
        this.proProductionStagesDTOConverter = proProductionStagesDTOConverter;
    }
    
    public InvPurchaseInvoiceDTOConverter getInvPurchaseInvoiceDTOConverter() {
        return invPurchaseInvoiceDTOConverter;
    }
    
    public void setInvPurchaseInvoiceDTOConverter(InvPurchaseInvoiceDTOConverter invPurchaseInvoiceDTOConverter) {
        this.invPurchaseInvoiceDTOConverter = invPurchaseInvoiceDTOConverter;
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
     * @return the invoiceMovementReport
     */
    public InvoiceMovementReport getInvoiceMovementReport() {
        return invoiceMovementReport;
    }

    /**
     * @param invoiceMovementReport the invoiceMovementReport to set
     */
    public void setInvoiceMovementReport(InvoiceMovementReport invoiceMovementReport) {
        this.invoiceMovementReport = invoiceMovementReport;
    }
    
}
