package com.toby.reports;

import com.toby.businessservice.ReportsService;
import com.toby.toby.Basic;
import com.toby.views.StatementOfBondsByBranche;
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

@Named(value = "statementOfBondsByBranchReport")
@ViewScoped
public class StatementOfBondsByBranchReport extends Basic {

    private List<StatementOfBondsByBranche> statementOfBondsByBrancheList;
    private StatementOfBondsByBranche statementOfBondsByBranche;

    @EJB
    private ReportsService reportsService;

    public void search() {
        setStatementOfBondsByBrancheList(reportsService.getStatementOfBondsByBranch(getStatementOfBondsByBranche(), getUserData().getUserBranch().getId()));
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
        String reportName = "StatementOfBondsByBranchReport.jasper";
        fillReport(prepareReport(), getUserData().getReportPath() + reportName, getStatementOfBondsByBrancheList(), "pdf");
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
        hashMap.put("dateFromValue", getStatementOfBondsByBranche().getDateFrom());
        hashMap.put("dateToText", " الى تاريخ");
        hashMap.put("dateToValue", getStatementOfBondsByBranche().getDateTo());
        hashMap.put("galleryNameText", "الفرع");
        hashMap.put("galleryCodeText", " كودالفرع");
        hashMap.put("typeText", "النوع");
        hashMap.put("paymentTypeText", " نوع السداد");
        hashMap.put("totalAmountText", " المبلغ");

        return hashMap;
    }

    /**
     * @return the statementOfBondsByBrancheList
     */
    public List<StatementOfBondsByBranche> getStatementOfBondsByBrancheList() {
        return statementOfBondsByBrancheList;
    }

    /**
     * @param statementOfBondsByBrancheList the statementOfBondsByBrancheList to
     * set
     */
    public void setStatementOfBondsByBrancheList(List<StatementOfBondsByBranche> statementOfBondsByBrancheList) {
        this.statementOfBondsByBrancheList = statementOfBondsByBrancheList;
    }

    /**
     * @return the statementOfBondsByBranche
     */
    public StatementOfBondsByBranche getStatementOfBondsByBranche() {

        if (statementOfBondsByBranche == null) {
            statementOfBondsByBranche = new StatementOfBondsByBranche();
        }

        return statementOfBondsByBranche;
    }

    /**
     * @param statementOfBondsByBranche the statementOfBondsByBranche to set
     */
    public void setStatementOfBondsByBranche(StatementOfBondsByBranche statementOfBondsByBranche) {
        this.statementOfBondsByBranche = statementOfBondsByBranche;
    }

}
