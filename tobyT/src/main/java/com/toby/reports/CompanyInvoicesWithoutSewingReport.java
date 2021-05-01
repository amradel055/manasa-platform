package com.toby.reports;

import com.toby.businessservice.ReportsService;
import com.toby.toby.Basic;
import com.toby.views.CompanyInvoicesWithoutSewing;
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

@Named(value = "companyInvoicesWithoutSewingReport")
@ViewScoped
public class CompanyInvoicesWithoutSewingReport extends Basic {

    private List<CompanyInvoicesWithoutSewing> invoicesWithoutSewingList;
    private CompanyInvoicesWithoutSewing invoicesWithoutSewing;

    @EJB
    private ReportsService reportsService;

    public void search() {
        setInvoicesWithoutSewingList(reportsService.getCompanyInvoicesWithoutSewing(invoicesWithoutSewing, getUserData().getUserBranch().getId()));
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
        String reportName = "CompanyInvoicesWithoutSewingReport.jasper";
        fillReport(prepareReport(), getUserData().getReportPath() + reportName, getInvoicesWithoutSewingList(), "pdf");
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
        hashMap.put("dateFromValue", getInvoicesWithoutSewing().getDateFrom());
        hashMap.put("dateToText", " الى تاريخ");
        hashMap.put("dateToValue", getInvoicesWithoutSewing().getDateTo());
        hashMap.put("idText", " رقم الفاتورة");
        hashMap.put("galleryNameText", "اسم المعرض");
        hashMap.put("nameText", " الاسم");
        hashMap.put("codeText", " الكود");
        hashMap.put("totalText", " الاجمالى");

        return hashMap;
    }

    /**
     * @return the invoicesWithoutSewingList
     */
    public List<CompanyInvoicesWithoutSewing> getInvoicesWithoutSewingList() {
        return invoicesWithoutSewingList;
    }

    /**
     * @param invoicesWithoutSewingList the invoicesWithoutSewingList to set
     */
    public void setInvoicesWithoutSewingList(List<CompanyInvoicesWithoutSewing> invoicesWithoutSewingList) {
        this.invoicesWithoutSewingList = invoicesWithoutSewingList;
    }

    /**
     * @return the invoicesWithoutSewing
     */
    public CompanyInvoicesWithoutSewing getInvoicesWithoutSewing() {
        if (invoicesWithoutSewing == null) {
            invoicesWithoutSewing = new CompanyInvoicesWithoutSewing();
        }

        return invoicesWithoutSewing;
    }

    /**
     * @param invoicesWithoutSewing the invoicesWithoutSewing to set
     */
    public void setInvoicesWithoutSewing(CompanyInvoicesWithoutSewing invoicesWithoutSewing) {
        this.invoicesWithoutSewing = invoicesWithoutSewing;
    }

}
