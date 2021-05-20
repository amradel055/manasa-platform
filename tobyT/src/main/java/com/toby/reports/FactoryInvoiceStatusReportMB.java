package com.toby.reports;

import com.toby.businessservice.InvPurchaseInvoiceService_1;
import com.toby.businessservice.TobyUserInventoryService;
import com.toby.converter.InvInventoryDTOConverter;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.ProGroupDTO;
import com.toby.toby.Basic;
import com.toby.views.FactoryInvoiceStatus;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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

@Named(value = "factoryInvoiceStatusReportMB")
@ViewScoped
public class FactoryInvoiceStatusReportMB extends Basic {

    private FactoryInvoiceStatus factoryInvoiceStatus;
    private List<FactoryInvoiceStatus> list;
    private Date toDate;
    private Date fromDate;
    private InvInventoryDTO galleryList;

    private InvInventoryDTOConverter gallaryDTOConverter;
    private List<InvInventoryDTO> gallaryListSelected;
    private List<InvInventoryDTO> gallarySelected;

    private ProGroupDTO status;

    @EJB
    private InvPurchaseInvoiceService_1 invPurchaseInvoiceService;
    @EJB
    private TobyUserInventoryService tobyUserInventoryService;

    @PostConstruct
    public void init() {
        getGallarySelected().addAll(getGallaryListSelected());
    }

    public void search() {
        if (getGallarySelected() != null && !getGallarySelected().isEmpty()) {
            for (InvInventoryDTO invInventoryDTO : getGallarySelected()) {
                setGalleryList(invInventoryDTO);
                getList().addAll(invPurchaseInvoiceService.findInvoiceStatus(getFromDate(), getToDate(), getStatus() == null ? null : getStatus().getId(), getGalleryList() == null ? null : getGalleryList().getId(), getUserData().getUserBranch().getId()));
            }
        }
    }

    public void convertDateFrom() {
        setFromDate(findDateFromHijriFrom());
    }

    public void convertDateTo() {
        setToDate(findDateFromHijriTo());
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
        String reportName = "FactoryInvoiceStatusReport.jasper";
        fillReport(prepareReport(), getUserData().getReportPath() + reportName, getList(), "pdf");
    }

    public HashMap prepareReport() {
        HashMap hashMap = new HashMap();

        hashMap.put("PrintedBy", getUserData().getUser().getName());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());

        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }

        hashMap.put("galleryListText", "معرض");
        hashMap.put("galleryListValue", getGalleryList() == null ? null : getGalleryList().getName());
        hashMap.put("statusText", "المرحلة");
        hashMap.put("statusValue", getStatus() == null ? null : getStatus().getName());
        hashMap.put("fromDateText", "من تاريخ");
        hashMap.put("fromDateValue", getFromDate());
        hashMap.put("toDateText", "الى تاريخ");
        hashMap.put("toDateValue", getToDate());
        hashMap.put("invoiceNumberText", "رقم الفاتورة");
        hashMap.put("galleryText", "المعرض");
        hashMap.put("invoiceDateText", "تاريخ الفاتورة");
        hashMap.put("clientNameText", "اسم العميل");
        hashMap.put("numberText", "العدد");
        hashMap.put("stageText", "المرحلة");
        hashMap.put("stageDateText", "تاريخ المرحلة");
        hashMap.put("employeeText", "الموظف");

        return hashMap;
    }

    /**
     * @return the factoryInvoiceStatus
     */
    public FactoryInvoiceStatus getFactoryInvoiceStatus() {

        if (factoryInvoiceStatus == null) {

            factoryInvoiceStatus = new FactoryInvoiceStatus();
        }

        return factoryInvoiceStatus;
    }

    /**
     * @param factoryInvoiceStatus the factoryInvoiceStatus to set
     */
    public void setFactoryInvoiceStatus(FactoryInvoiceStatus factoryInvoiceStatus) {
        this.factoryInvoiceStatus = factoryInvoiceStatus;
    }

    /**
     * @return the toDate
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    /**
     * @return the fromDate
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return the list
     */
    public List<FactoryInvoiceStatus> getList() {
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(List<FactoryInvoiceStatus> list) {
        this.list = list;
    }

    /**
     * @return the gallery
     */
    /**
     * @return the status
     */
    public ProGroupDTO getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(ProGroupDTO status) {
        this.status = status;
    }

    /**
     * @return the galleryList
     */
    public InvInventoryDTO getGalleryList() {
        if (galleryList == null) {
            galleryList = new InvInventoryDTO();
        }
        return galleryList;
    }

    /**
     * @param galleryList the galleryList to set
     */
    public void setGalleryList(InvInventoryDTO galleryList) {
        this.galleryList = galleryList;
    }

    /**
     * @return the gallaryDTOConverter
     */
    public InvInventoryDTOConverter getGallaryDTOConverter() {
        return gallaryDTOConverter;
    }

    /**
     * @param gallaryDTOConverter the gallaryDTOConverter to set
     */
    public void setGallaryDTOConverter(InvInventoryDTOConverter gallaryDTOConverter) {
        this.gallaryDTOConverter = gallaryDTOConverter;
    }

    /**
     * @return the gallaryListSelected
     */
    public List<InvInventoryDTO> getGallaryListSelected() {

        if (gallaryListSelected == null || gallaryListSelected.isEmpty()) {
            gallaryListSelected = tobyUserInventoryService.getAllGallaryByUserAndBranch(getUserData().getUser());
            setGallaryDTOConverter(new InvInventoryDTOConverter(gallaryListSelected));
        }
        return gallaryListSelected;
    }

    /**
     * @param gallaryListSelected the gallaryListSelected to set
     */
    public void setGallaryListSelected(List<InvInventoryDTO> gallaryListSelected) {
        this.gallaryListSelected = gallaryListSelected;
    }

    /**
     * @return the gallarySelected
     */
    public List<InvInventoryDTO> getGallarySelected() {
        if (gallarySelected == null) {
            gallarySelected = new ArrayList<>();
        }
        return gallarySelected;
    }

    /**
     * @param gallarySelected the gallarySelected to set
     */
    public void setGallarySelected(List<InvInventoryDTO> gallarySelected) {
        this.gallarySelected = gallarySelected;
    }

    /**
     * @return the status
     */
}
