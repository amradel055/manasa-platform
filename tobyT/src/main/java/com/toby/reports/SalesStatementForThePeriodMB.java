package com.toby.reports;

import com.toby.businessservice.ReportsService;
import com.toby.dto.InvInventoryDTO;
import com.toby.toby.Basic;
import com.toby.views.SalesStatementForThePeriod;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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

@Named(value = "salesStatementForThePeriodMB")
@ViewScoped
public class SalesStatementForThePeriodMB extends Basic {

    private List<SalesStatementForThePeriod> list;
    private SalesStatementForThePeriod statementForThePeriod;
    private Boolean notZeroValue ;
    private Boolean zeroValue ;

    @EJB
    private ReportsService reportsService;

    @PostConstruct
    public void init() {
        getStatementForThePeriod().getGallaryListSelected().addAll(getGallaryLists());
    }

    public void search() {
        if (getStatementForThePeriod() != null && getStatementForThePeriod().getGallaryListSelected() != null && !getStatementForThePeriod().getGallaryListSelected().isEmpty()) {
            list = new ArrayList<>();
            for (InvInventoryDTO invInventoryDTO : getStatementForThePeriod().getGallaryListSelected()) {
                getStatementForThePeriod().setGallerySelected(invInventoryDTO);
                getList().addAll(reportsService.getSalesStatementForThePeriod(getStatementForThePeriod()));
            }
            if(getNotZeroValue()){
                List<SalesStatementForThePeriod> listBak = new ArrayList<>();
                for( SalesStatementForThePeriod period : getList()){
                    if(period.getRemain().compareTo(BigDecimal.ZERO) == 1){
                        listBak.add(period);
                    }
                }
                setList(listBak);
            }
            if(getZeroValue()){
                List<SalesStatementForThePeriod> listBak = new ArrayList<>();
                for( SalesStatementForThePeriod period : getList()){
                    if(period.getRemain().compareTo(BigDecimal.ZERO) != 1){
                        listBak.add(period);
                    }
                }
                setList(listBak);
            }
        }
    }
    
    public void searchPay() {
        if (getStatementForThePeriod() != null && getStatementForThePeriod().getGallaryListSelected() != null && !getStatementForThePeriod().getGallaryListSelected().isEmpty()) {
            list = new ArrayList<>();
            for (InvInventoryDTO invInventoryDTO : getStatementForThePeriod().getGallaryListSelected()) {
                getStatementForThePeriod().setGallerySelected(invInventoryDTO);
                getList().addAll(reportsService.getSalesStatementForThePeriodForPay(getStatementForThePeriod()));
            }
            if(getNotZeroValue()){
                List<SalesStatementForThePeriod> listBak = new ArrayList<>();
                for( SalesStatementForThePeriod period : getList()){
                    if(period.getRemain().compareTo(BigDecimal.ZERO) == 1){
                        listBak.add(period);
                    }
                }
                setList(listBak);
            }
            if(getZeroValue()){
                List<SalesStatementForThePeriod> listBak = new ArrayList<>();
                for( SalesStatementForThePeriod period : getList()){
                    if(period.getRemain().compareTo(BigDecimal.ZERO) != 1){
                        listBak.add(period);
                    }
                }
                setList(listBak);
            }
        }
    }

    public void convertDateFrom() {
        getStatementForThePeriod().setDateFrom(findDateFromHijriFrom());
    }

    public void convertDateTo() {
        getStatementForThePeriod().setDateTo(findDateFromHijriTo());
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

        hashMap.put("gallaryListSelectedText", " معرض");
        hashMap.put("gallaryListSelectedValue", statementForThePeriod.getGallerySelected() == null ? null : statementForThePeriod.getGallerySelected().getName());
        hashMap.put("dateFromText", " من تاريخ");
        hashMap.put("dateFromValue", statementForThePeriod.getDateFrom());
        hashMap.put("dateToText", " الى تاريخ");
        hashMap.put("dateToValue", statementForThePeriod.getDateTo());
        hashMap.put("serialText", " ف رقم");
        hashMap.put("invoiceDateText", "التاريخ");
        hashMap.put("codeText", "رقم العميل");
        hashMap.put("nameText", "العميل");
        hashMap.put("totalNetText", "صافى الفاتورة");
        hashMap.put("netText", "قيمة الفاتورة");
        hashMap.put("paidText", "المسدد");
        hashMap.put("remainText", "الباقى");
        hashMap.put("invoiceStatusText", "الحالة");
        

        return hashMap;
    }

    /**
     * @return the list
     */
    public List<SalesStatementForThePeriod> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(List<SalesStatementForThePeriod> list) {
        this.list = list;
    }

    /**
     * @return the statementForThePeriod
     */
    public SalesStatementForThePeriod getStatementForThePeriod() {
        if (statementForThePeriod == null) {
            statementForThePeriod = new SalesStatementForThePeriod();
        }
        return statementForThePeriod;
    }

    /**
     * @param statementForThePeriod the statementForThePeriod to set
     */
    public void setStatementForThePeriod(SalesStatementForThePeriod statementForThePeriod) {
        this.statementForThePeriod = statementForThePeriod;
    }

    /**
     * @return the notZeroValue
     */
    public Boolean getNotZeroValue() {
        return notZeroValue;
    }

    /**
     * @param notZeroValue the notZeroValue to set
     */
    public void setNotZeroValue(Boolean notZeroValue) {
        this.notZeroValue = notZeroValue;
    }

    /**
     * @return the zeroValue
     */
    public Boolean getZeroValue() {
        if(zeroValue == null){
            zeroValue = Boolean.FALSE;
        }
        return zeroValue;
    }

    /**
     * @param zeroValue the zeroValue to set
     */
    public void setZeroValue(Boolean zeroValue) {
        this.zeroValue = zeroValue;
    }

}
