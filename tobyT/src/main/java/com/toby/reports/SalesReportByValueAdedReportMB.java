package com.toby.reports;

import com.toby.businessservice.ReportsService;
import com.toby.toby.Basic;
import com.toby.views.SalesReportByValueAded;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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

@Named(value = "salesReportByValueAdedReportMB")
@ViewScoped
public class SalesReportByValueAdedReportMB extends Basic {

    private List<SalesReportByValueAded> salesReportByValueAdedList;
    private SalesReportByValueAded salesReportByValueAded;

    @EJB
    private ReportsService reportsService;

    public void search() {
        setSalesReportByValueAdedList(reportsService.getSalesReportByValueAded(salesReportByValueAded, getUserData().getUserBranch().getId()));
    }

    public void convertDateFrom() {
        getSalesReportByValueAded().setDateFrom(findDateFromHijriFrom());
    }

    public void convertDateTo() {
        getSalesReportByValueAded().setDateTo(findDateFromHijriTo());
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
        String reportName = "SalesReportByValueAdedReport.jasper";
        fillReport(prepareReport(), getUserData().getReportPath() + reportName, getSalesReportByValueAdedList(), "pdf");
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
        hashMap.put("gallerySelectedValue", salesReportByValueAded.getGallerySelected() == null ? null : salesReportByValueAded.getGallerySelected().getName());
        hashMap.put("dateFromText", " من تاريخ");
        hashMap.put("dateFromValue", salesReportByValueAded.getDateFrom());
        hashMap.put("dateToText", " الى تاريخ");
        hashMap.put("dateToValue", salesReportByValueAded.getDateTo());
        hashMap.put("galleryNameText", " اسم المعرض");
        hashMap.put("totalBeforeTaxText", "قبل الضريبة");
        hashMap.put("totalAfterTaxText", "بعد الضريبة");
        hashMap.put("taxText", " بالضريبة");

        return hashMap;
    }

    public List<SalesReportByValueAded> getSalesReportByValueAdedList() {
        return salesReportByValueAdedList;
    }

    /**
     * @param salesReportByValueAdedList the salesReportByValueAdedList to set
     */
    public void setSalesReportByValueAdedList(List<SalesReportByValueAded> salesReportByValueAdedList) {
        this.salesReportByValueAdedList = salesReportByValueAdedList;
    }

    /**
     * @return the salesReportByValueAded
     */
    public SalesReportByValueAded getSalesReportByValueAded() {
        if (salesReportByValueAded == null) {
            salesReportByValueAded = new SalesReportByValueAded();
        }

        return salesReportByValueAded;
    }

    /**
     * @param salesReportByValueAded the salesReportByValueAded to set
     */
    public void setSalesReportByValueAded(SalesReportByValueAded salesReportByValueAded) {
        this.salesReportByValueAded = salesReportByValueAded;
    }

    /**
     * @return the salesReportByValueAdedDetailsList
     */
}
