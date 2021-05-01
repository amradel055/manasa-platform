package com.toby.reports;

import com.toby.businessservice.ReportsService;
import com.toby.toby.Basic;
import com.toby.views.ItemBalancesStatement;
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

@Named(value = "itemBalancesStatementReport")
@ViewScoped
public class ItemBalancesStatementReport extends Basic {

    private List<ItemBalancesStatement> itemBalancesStatementList;
    private ItemBalancesStatement itemBalancesStatement;

    @EJB
    private ReportsService reportsService;

    public void search() {
        setItemBalancesStatementList(reportsService.getItemBalancesStatement(itemBalancesStatement, getUserData().getUserBranch().getId()));
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
        String reportName = "ItemBalancesStatementReport.jasper";
        fillReport(prepareReport(), getUserData().getReportPath() + reportName, getItemBalancesStatementList(), "pdf");
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
        hashMap.put("dateFromValue", getItemBalancesStatement().getDateFrom());
        hashMap.put("dateToText", " الى تاريخ");
        hashMap.put("dateToValue", getItemBalancesStatement().getDateTo());
        hashMap.put("codeText", " رقم الصنف");
        hashMap.put("nameText", "اسم الصنف ");
        hashMap.put("dateText", " تاريخ اخر فاتورة");
        hashMap.put("sallaryText", " سعر البيع");
        hashMap.put("costAverageText", " متوسط التكلفة");

        return hashMap;
    }

    public List<ItemBalancesStatement> getItemBalancesStatementList() {
        return itemBalancesStatementList;
    }

    /**
     * @param itemBalancesStatementList the itemBalancesStatementList to set
     */
    public void setItemBalancesStatementList(List<ItemBalancesStatement> itemBalancesStatementList) {
        this.itemBalancesStatementList = itemBalancesStatementList;
    }

    /**
     * @return the itemBalancesStatement
     */
    public ItemBalancesStatement getItemBalancesStatement() {

        if (itemBalancesStatement == null) {
            itemBalancesStatement = new ItemBalancesStatement();
        }
        return itemBalancesStatement;
    }

    /**
     * @param itemBalancesStatement the itemBalancesStatement to set
     */
    public void setItemBalancesStatement(ItemBalancesStatement itemBalancesStatement) {
        this.itemBalancesStatement = itemBalancesStatement;
    }

}
