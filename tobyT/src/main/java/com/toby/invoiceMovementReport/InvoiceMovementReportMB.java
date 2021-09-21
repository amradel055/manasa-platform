package com.toby.invoiceMovementReport;

import com.toby.businessservice.InvoiceMovementReportService;
import com.toby.businessservice.ProProductionStagesService;
import com.toby.businessservice.ProProductionTransactionService;
import com.toby.converter.ProProductionStagesDTOConverter;
import com.toby.converter.ProProductionTransactionDTOConverter;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.PrintProductionDTO;
import com.toby.dto.ProProductionStagesDTO;
import com.toby.dto.ProProductionTransactionDTO;
import com.toby.toby.BaseListBean;
import com.toby.toby.UserData;
import com.toby.views.InvoiceMovementReport;
import com.toby.views.ProductionReport;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.event.ActionEvent;
import net.sf.jasperreports.engine.JRException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

@Named(value = "invoiceMovementReportMB")
@ViewScoped
public class InvoiceMovementReportMB extends BaseListBean {

    private List<ProProductionStagesDTO> proProductionStagesDTOList;
    private List<ProProductionTransactionDTO> proProductionTransactionDTOList;
    private List<ProductionReport> productionReports;
    private List<InvoiceMovementReport> invoiceMovementReportList;
    private PrintProductionDTO printProductionDTO;
    private Integer invoiceType;

    private ProProductionStagesDTOConverter proProductionStagesDTOConverter;
    private ProProductionTransactionDTOConverter proProductionTransactionDTOConverter;

    private Boolean invoiceNotComplete;

    @EJB
    private ProProductionStagesService proProductionStagesService;
    @EJB
    private ProProductionTransactionService proProductionTransactionService;
    @EJB
    private InvoiceMovementReportService invoiceMovementReportService;

    @Override
    @PostConstruct
    public void init() {

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        proProductionStagesDTOList = new ArrayList<>();
        proProductionTransactionDTOList = new ArrayList<>();
        getPrintProductionDTO().getGallaryFromList().addAll(getGallaryLists());
        if (getScreenMode() != null && getScreenMode().equalsIgnoreCase("AddFromInvInvoice")) {
            InvInvoiceDTO invInvoiceDTO = (InvInvoiceDTO) context.getSessionMap().get("invInvoiceDTOSelected");
            getPrintProductionDTO().setFromInvInvoice(invInvoiceDTO.getSerial());
            getPrintProductionDTO().setToInvInvoice(invInvoiceDTO.getSerial());
            search();
        }
    }

    public void search() {

        if (getPrintProductionDTO() != null && getPrintProductionDTO().getGallaryFromList() != null && !getPrintProductionDTO().getGallaryFromList().isEmpty()) {
            invoiceMovementReportList = new ArrayList<>();
            for (InvInventoryDTO inventoryDTOFrom : getPrintProductionDTO().getGallaryFromList()) {
                getPrintProductionDTO().setGallaryFrom(inventoryDTOFrom);
                getInvoiceMovementReportList().addAll(invoiceMovementReportService.searchReport(printProductionDTO, getUserData().getUserBranch().getId(), getInvoiceType()));
            }
            if (getInvoiceNotComplete()) {
                List<InvoiceMovementReport> list = new ArrayList<>();
                for (InvoiceMovementReport report : getInvoiceMovementReportList()) {
                    if (report.getNumberTobExitFactory().compareTo(BigDecimal.ZERO) == 1 && 
                            report.getNumberTobExitFactory().compareTo(report.getNumberToFactory()) != 0) {
                        list.add(report);
                    }
                }
                setInvoiceMovementReportList(list);
            }
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
        if (invoiceMovementReportList != null && !invoiceMovementReportList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "InvoiceMovementReport.jasper", invoiceMovementReportList, "pdf");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا توجد نتائج للطباعة"));
        }
    }

    public HashMap prepareReport() {
        HashMap hashMap = new HashMap();

        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        hashMap.put("PrintedBy", getUserData().getUser().getName());
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }

        hashMap.put("fromInvoiceText", "من فاتورة");
        hashMap.put("fromInvoiceValue", printProductionDTO.getFromInvInvoice());
        hashMap.put("toInvoiceText", "إلى فاتورة");
        hashMap.put("toInvoiceValue", printProductionDTO.getToInvInvoice());
        hashMap.put("gallaryFromText", "من معرض");
        hashMap.put("gallaryFromValue", printProductionDTO.getGallaryFrom() == null ? null : printProductionDTO.getGallaryFrom().getName());
        hashMap.put("gallaryToText", "إلى معرض");
        hashMap.put("gallaryToValue", printProductionDTO.getGallaryTo() == null ? null : printProductionDTO.getGallaryTo().getName());
        hashMap.put("fromDateText", "من تاريخ");
        hashMap.put("fromDateValue", printProductionDTO.getFromDate());
        hashMap.put("toDateText", "الى تاريخ");
        hashMap.put("toDateValue", printProductionDTO.getToDate());
        hashMap.put("invoiceSerialText", " الفاتورة");
        hashMap.put("inventoryNameText", " الفرع");
        hashMap.put("clientNameText", " اسم العميل");
        hashMap.put("numberTobInvoiceText", "عدد الثياب بلفاتورة");
        hashMap.put("factoryDateText", "تاريخ التسليم المشغل");
        hashMap.put("numberToFactoryText", "عدد الثياب بالمشغل");
        hashMap.put("deliveryDateText", " تاريخ التسليم للطيار");
        hashMap.put("numberTobExitFactoryText", " عدد الثياب للطيار");
        hashMap.put("galaryDateText", " تاريخ الاستلام بالمعرض");
        hashMap.put("numberTobgallaryText", " عدد الثياب بالمعرض");
        hashMap.put("clientDateText", " تاريخ التسليم للعميل");

        return hashMap;
    }

    @Override
    public void delete() {
    }

    @Override
    public void filter() {
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
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<ProductionReport> getProductionReports() {
        return productionReports;
    }

    public void setProductionReports(List<ProductionReport> productionReports) {
        this.productionReports = productionReports;
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

    public List<ProProductionStagesDTO> getProProductionStagesDTOList() {
        if (proProductionStagesDTOList == null | proProductionStagesDTOList.isEmpty()) {
            proProductionStagesDTOList = proProductionStagesService.getAllProproductionStages(getUserData().getUser(), getUserData().getUserBranch().getId());
        }
        return proProductionStagesDTOList;
    }

    public void setProProductionStagesDTOList(List<ProProductionStagesDTO> proProductionStagesDTOList) {
        this.proProductionStagesDTOList = proProductionStagesDTOList;
    }

    public List<ProProductionTransactionDTO> getProProductionTransactionDTOList() {
        if (proProductionTransactionDTOList == null | proProductionTransactionDTOList.isEmpty()) {
            proProductionTransactionDTOList = proProductionTransactionService.getAll(getUserData().getUser());
            setProProductionTransactionDTOConverter(new ProProductionTransactionDTOConverter(proProductionTransactionDTOList));
        }
        return proProductionTransactionDTOList;
    }

    public void setProProductionTransactionDTOList(List<ProProductionTransactionDTO> proProductionTransactionDTOList) {
        this.proProductionTransactionDTOList = proProductionTransactionDTOList;
    }

    public ProProductionStagesDTOConverter getProProductionStagesDTOConverter() {
        return proProductionStagesDTOConverter;
    }

    public void setProProductionStagesDTOConverter(ProProductionStagesDTOConverter proProductionStagesDTOConverter) {
        this.proProductionStagesDTOConverter = proProductionStagesDTOConverter;
    }

    public ProProductionTransactionDTOConverter getProProductionTransactionDTOConverter() {
        return proProductionTransactionDTOConverter;
    }

    public void setProProductionTransactionDTOConverter(ProProductionTransactionDTOConverter proProductionTransactionDTOConverter) {
        this.proProductionTransactionDTOConverter = proProductionTransactionDTOConverter;
    }

    /**
     * @return the invoiceMovementReportList
     */
    public List<InvoiceMovementReport> getInvoiceMovementReportList() {
        if (invoiceMovementReportList == null || invoiceMovementReportList.isEmpty()) {
            invoiceMovementReportList = new ArrayList<>();
        }
        return invoiceMovementReportList;
    }

    /**
     * @param invoiceMovementReportList the invoiceMovementReportList to set
     */
    public void setInvoiceMovementReportList(List<InvoiceMovementReport> invoiceMovementReportList) {
        this.invoiceMovementReportList = invoiceMovementReportList;
    }

    /**
     * @return the invoiceType
     */
    public Integer getInvoiceType() {
        return invoiceType;
    }

    /**
     * @param invoiceType the invoiceType to set
     */
    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }

    /**
     * @return the invoiceNotComplete
     */
    public Boolean getInvoiceNotComplete() {
        if (invoiceNotComplete == null) {
            invoiceNotComplete = Boolean.FALSE;
        }
        return invoiceNotComplete;
    }

    /**
     * @param invoiceNotComplete the invoiceNotComplete to set
     */
    public void setInvoiceNotComplete(Boolean invoiceNotComplete) {
        this.invoiceNotComplete = invoiceNotComplete;
    }
}
