package com.igma.report;


import com.isag.businessservice.ReportsService;
import com.isag.converter.InvInventoryDTOConverter;
import com.isag.dto.InvInventoryDTO;
import com.isag.isag.Basic;
import com.isag.views.SalesReportByValueAdedDetails;
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

@Named(value = "salesReportByValueAdedDetailsReportMB")
@ViewScoped
public class SalesReportByValueAdedDetailsReportMB extends Basic {

    private List<SalesReportByValueAdedDetails> salesReportByValueAdedDetailsList;
    private SalesReportByValueAdedDetails salesReportByValueAdedDetails;
    private List<InvInventoryDTO> invInventoryDTOList;
    private InvInventoryDTOConverter invInventoryDTOConverter;
    private List<InvInventoryDTO> inventoryDTOSelected;
    @EJB
    private ReportsService reportsService;

    @PostConstruct
    public void init() {
    }

    public void search() {
        if (getInvInventoryDTOList()!= null && !getInvInventoryDTOList().isEmpty()) {
            for ( InvInventoryDTO inventoryDTO : getInvInventoryDTOList()) {
                salesReportByValueAdedDetails.setInvInventoryDTO(inventoryDTO);
                List<SalesReportByValueAdedDetails> list = reportsService.getSalesReportByValueAdedDetails(salesReportByValueAdedDetails, getUserData().getUserBranch().getId());
                getSalesReportByValueAdedDetailsList().addAll(list);
            }
        }
//        salesReportByValueAdedDetails.setGallerySelected(null);
//        List<SalesReportByValueAdedDetails> list = reportsService.getSalesReportByValueAdedDetails(salesReportByValueAdedDetails, getUserData().getUserBranch().getId());
//        getSalesReportByValueAdedDetailsList().addAll(list);
    }

    public void convertDateFrom() {
        getSalesReportByValueAdedDetails().setDateFrom(findDateFromHijriFrom());
    }

    public void convertDateTo() {
        getSalesReportByValueAdedDetails().setDateTo(findDateFromHijriTo());
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
        String reportName = "SalesReportByValueAdedDetailsReport.jasper";
        fillReport(prepareReport(), getUserData().getReportPath() + reportName, getSalesReportByValueAdedDetailsList(), "pdf");
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

        hashMap.put("gallerySelectedText", " معرض");
        hashMap.put("gallerySelectedValue", salesReportByValueAdedDetails.getInvInventoryDTO()== null ? null : salesReportByValueAdedDetails.getInvInventoryDTO().getName());
        hashMap.put("dateFromText", " من تاريخ");
        hashMap.put("dateFromValue", salesReportByValueAdedDetails.getDateFrom());
        hashMap.put("dateToText", " الى تاريخ");
        hashMap.put("dateToValue", salesReportByValueAdedDetails.getDateTo());
        hashMap.put("invoiceNumberText", " رقم الفاتورة");
        hashMap.put("invoiceTypeText", " نوع الفاتورة");
        hashMap.put("invoiceDateText", "  التاريخ");
        hashMap.put("galleryNameText", " اسم المعرض");
        hashMap.put("employeeNameText", " اسم الموظف");
        hashMap.put("discountText", "الخصم");
        hashMap.put("totalBeforeTaxText", "قبل الضريبة");
        hashMap.put("totalAfterTaxText", "بعد الضريبة");
        hashMap.put("taxText", " الضريبة");
        hashMap.put("reportDate", new Date());

        return hashMap;
    }

    /**
     * @return the salesReportByValueAdedDetailsList
     */
    public List<SalesReportByValueAdedDetails> getSalesReportByValueAdedDetailsList() {
        if (salesReportByValueAdedDetailsList == null) {
            salesReportByValueAdedDetailsList = new ArrayList<>();
        }
        return salesReportByValueAdedDetailsList;
    }

    /**
     * @param salesReportByValueAdedDetailsList the
     * salesReportByValueAdedDetailsList to set
     */
    public void setSalesReportByValueAdedDetailsList(List<SalesReportByValueAdedDetails> salesReportByValueAdedDetailsList) {
        this.salesReportByValueAdedDetailsList = salesReportByValueAdedDetailsList;
    }

    /**
     * @return the salesReportByValueAdedDetails
     */
    public SalesReportByValueAdedDetails getSalesReportByValueAdedDetails() {

        if (salesReportByValueAdedDetails == null) {
            salesReportByValueAdedDetails = new SalesReportByValueAdedDetails();
        }
        return salesReportByValueAdedDetails;
    }

    /**
     * @param salesReportByValueAdedDetails the salesReportByValueAdedDetails to
     * set
     */
    public void setSalesReportByValueAdedDetails(SalesReportByValueAdedDetails salesReportByValueAdedDetails) {
        this.salesReportByValueAdedDetails = salesReportByValueAdedDetails;
    }

    /**
     * @return the invInventoryDTOList
     */
    public List<InvInventoryDTO> getInvInventoryDTOList() {
        if(invInventoryDTOList == null){
            invInventoryDTOList = getUserData().getInventoryDTOList();
        }
        return invInventoryDTOList;
    }

    /**
     * @param invInventoryDTOList the invInventoryDTOList to set
     */
    public void setInvInventoryDTOList(List<InvInventoryDTO> invInventoryDTOList) {
        this.invInventoryDTOList = invInventoryDTOList;
    }

    /**
     * @return the invInventoryDTOConverter
     */
    public InvInventoryDTOConverter getInvInventoryDTOConverter() {
        return invInventoryDTOConverter;
    }

    /**
     * @param invInventoryDTOConverter the invInventoryDTOConverter to set
     */
    public void setInvInventoryDTOConverter(InvInventoryDTOConverter invInventoryDTOConverter) {
        this.invInventoryDTOConverter = invInventoryDTOConverter;
    }

    /**
     * @return the inventoryDTOSelected
     */
    public List<InvInventoryDTO> getInventoryDTOSelected() {
        return inventoryDTOSelected;
    }

    /**
     * @param inventoryDTOSelected the inventoryDTOSelected to set
     */
    public void setInventoryDTOSelected(List<InvInventoryDTO> inventoryDTOSelected) {
        this.inventoryDTOSelected = inventoryDTOSelected;
    }
}
