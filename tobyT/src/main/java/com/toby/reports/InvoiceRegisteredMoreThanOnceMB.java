package com.toby.reports;

import com.toby.businessservice.ReportsService;
import com.toby.toby.Basic;
import com.toby.views.InvoicesRegisteredMoreThanOnce;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
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

@Named(value = "invoiceRegisteredMoreThanOnceMB")
@ViewScoped
public class InvoiceRegisteredMoreThanOnceMB extends Basic {

    private List<InvoicesRegisteredMoreThanOnce> list;
    private InvoicesRegisteredMoreThanOnce registeredMoreThanOnce;
    private InvoicesRegisteredMoreThanOnce registeredMoreThanOnceSelected;

    @EJB
    private ReportsService reportsService;

    @PostConstruct
    public void init() {
        getRegisteredMoreThanOnce().setDate(new Date());
    }

    public boolean validateSearch() {
        if (getRegisteredMoreThanOnce().getDate() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار التاريخ "));
        } else if (getRegisteredMoreThanOnce().getSerial() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار رقم الفاتورة   "));
        } else {
            return true;
        }
        return false;
    }

    public void search() {
        if (validateSearch() == true) {

            setList(reportsService.getInvoicesRegisteredMoreThanOnce(registeredMoreThanOnce, getRegisteredMoreThanOnce().getSerial(), getRegisteredMoreThanOnce().getDate(), getUserData().getUserBranch().getId()));
        }
    }

    public void delete() {
        reportsService.deleteIncrease(getRegisteredMoreThanOnceSelected().getStageId(), getRegisteredMoreThanOnceSelected().getFactoryId(), getRegisteredMoreThanOnceSelected().getWorkNumber(), getRegisteredMoreThanOnceSelected().getOriginNumber());
        list.remove(getRegisteredMoreThanOnceSelected());
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
        String reportName = "InvoiceRegisteredMoreThanOnceReport.jasper";
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

        hashMap.put("serialText", " رقم الفاتورة");
        hashMap.put("serialValue", getRegisteredMoreThanOnce().getSerial());
        hashMap.put("stageNameText", "اسم المرحلة");
        hashMap.put("itemNameText", "اسم الصنف");
        hashMap.put("workNumberText", " عدد مرات الشغل");
        hashMap.put("originNumberText", "العدد بالفاتورة");
        hashMap.put("titleText", " الفواتير المسجلة اكثر من مرة");

        return hashMap;
    }

    /**
     * @return the list
     */
    public List<InvoicesRegisteredMoreThanOnce> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(List<InvoicesRegisteredMoreThanOnce> list) {
        this.list = list;
    }

    /**
     * @return the registeredMoreThanOnce
     */
    public InvoicesRegisteredMoreThanOnce getRegisteredMoreThanOnce() {
        if (registeredMoreThanOnce == null) {
            registeredMoreThanOnce = new InvoicesRegisteredMoreThanOnce();
        }
        return registeredMoreThanOnce;
    }

    /**
     * @param registeredMoreThanOnce the registeredMoreThanOnce to set
     */
    public void setRegisteredMoreThanOnce(InvoicesRegisteredMoreThanOnce registeredMoreThanOnce) {
        this.registeredMoreThanOnce = registeredMoreThanOnce;
    }

    /**
     * @return the registeredMoreThanOnceSelected
     */
    public InvoicesRegisteredMoreThanOnce getRegisteredMoreThanOnceSelected() {
        if (registeredMoreThanOnceSelected == null) {
            registeredMoreThanOnceSelected = new InvoicesRegisteredMoreThanOnce();
        }
        return registeredMoreThanOnceSelected;
    }

    /**
     * @param registeredMoreThanOnceSelected the registeredMoreThanOnceSelected
     * to set
     */
    public void setRegisteredMoreThanOnceSelected(InvoicesRegisteredMoreThanOnce registeredMoreThanOnceSelected) {
        this.registeredMoreThanOnceSelected = registeredMoreThanOnceSelected;
    }

}
