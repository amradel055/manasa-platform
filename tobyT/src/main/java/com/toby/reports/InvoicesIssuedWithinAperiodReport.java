package com.toby.reports;

import com.toby.businessservice.ReportsService;
import com.toby.toby.Basic;
import com.toby.views.InvoicesIssuedWithinAperiod;
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

@Named(value = "invoicesIssuedWithinAperiodReport")
@ViewScoped
public class InvoicesIssuedWithinAperiodReport extends Basic {

    private List<InvoicesIssuedWithinAperiod> issuedWithinAperiodList;
    private InvoicesIssuedWithinAperiod issuedWithinAperiod;

    @EJB
    private ReportsService reportsService;

    public void search() {
        setIssuedWithinAperiodList(reportsService.getInvoiceWithinAperiod(issuedWithinAperiod, getUserData().getUserBranch().getId()));
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
        String reportName = "InvoicesIssuedWithinAperiod.jasper";
        fillReport(prepareReport(), getUserData().getReportPath() + reportName, getIssuedWithinAperiodList(), "pdf");
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

        hashMap.put("dateFromText", " من تاريخ");
        hashMap.put("dateFromValue", getIssuedWithinAperiod().getDateFrom());
        hashMap.put("dateToText", " الى تاريخ");
        hashMap.put("dateToValue", getIssuedWithinAperiod().getDateTo());
        hashMap.put("invoiceNumberText", "رقم الفاتورة");
        hashMap.put("dateText", " التاريخ");
        hashMap.put("clientCodeText", " كود العميل");
        hashMap.put("clientNameText", "  اسم العميل");
        hashMap.put("invoiceValueText", " قيمة الفاتورة");
        hashMap.put("invoiceNetText", "صافى الفاتورة");
        hashMap.put("bouncedBackText", " مرتجع");
        hashMap.put("payedText", "المسدد");
        hashMap.put("remainingText", "  الباقى");
        hashMap.put("statusText", " الحالة");

        return hashMap;
    }

    /**
     * @return the issuedWithinAperiodList
     */
    public List<InvoicesIssuedWithinAperiod> getIssuedWithinAperiodList() {
        return issuedWithinAperiodList;
    }

    /**
     * @param issuedWithinAperiodList the issuedWithinAperiodList to set
     */
    public void setIssuedWithinAperiodList(List<InvoicesIssuedWithinAperiod> issuedWithinAperiodList) {
        this.issuedWithinAperiodList = issuedWithinAperiodList;
    }

    /**
     * @return the issuedWithinAperiod
     */
    public InvoicesIssuedWithinAperiod getIssuedWithinAperiod() {

        if (issuedWithinAperiod == null) {
            issuedWithinAperiod = new InvoicesIssuedWithinAperiod();
        }
        return issuedWithinAperiod;
    }

    /**
     * @param issuedWithinAperiod the issuedWithinAperiod to set
     */
    public void setIssuedWithinAperiod(InvoicesIssuedWithinAperiod issuedWithinAperiod) {
        this.issuedWithinAperiod = issuedWithinAperiod;
    }

}
