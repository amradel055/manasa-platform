package com.toby.reports;

import com.toby.businessservice.ReportsService;
import com.toby.dto.InvInventoryDTO;
import com.toby.toby.Basic;
import com.toby.views.CustomersHaveNoMovementFromTheDate;
import java.io.IOException;
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

@Named(value = "customersHaveNoMovmentFromTheDateMB")
@ViewScoped
public class CustomersHaveNoMovmentFromTheDateMB extends Basic {

    private CustomersHaveNoMovementFromTheDate noMovementFromTheDate;
    private List<CustomersHaveNoMovementFromTheDate> list;
    private List<InvInventoryDTO> gallarySelected;

    @EJB
    ReportsService reportsService;

    @PostConstruct
    public void init() {
        getGallarySelected().addAll(getGallaryLists());
    }

    public void search() {
        if (getGallarySelected() != null && !getGallarySelected().isEmpty()) {
            setList(new ArrayList<>());
            for (InvInventoryDTO invInventoryDTO : getGallarySelected()) {
                getNoMovementFromTheDate().setGallerySelected(invInventoryDTO);
                getList().addAll(reportsService.getCustomersHaveNoMovementFromTheDate(getNoMovementFromTheDate(), getUserData().getUserBranch().getId()));
            }
        }
    }

    public void convertDateFrom() {
        getNoMovementFromTheDate().setDate(findDateFromHijriFrom());
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
        String reportName = "CustomersHaveNoMovmentFromTheDate.jasper";
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

        hashMap.put("titleText", " عمال ليس لديهم حركة من تاريخ");
        hashMap.put("dateText", " التاريخ");
        hashMap.put("dateValue", getNoMovementFromTheDate().getDate());
        hashMap.put("gallarySelectedText", " المعرض");
        hashMap.put("gallarySelectedValue", getNoMovementFromTheDate() == null ? null : getNoMovementFromTheDate().getGallerySelected().getName());
        hashMap.put("nameText", "اسم الموظف");
        hashMap.put("mobileText", "التليفون");

        return hashMap;
    }

    /**
     * @return the noMovementFromTheDate
     */
    public CustomersHaveNoMovementFromTheDate getNoMovementFromTheDate() {
        if (noMovementFromTheDate == null) {
            noMovementFromTheDate = new CustomersHaveNoMovementFromTheDate();
        }
        return noMovementFromTheDate;
    }

    /**
     * @param noMovementFromTheDate the noMovementFromTheDate to set
     */
    public void setNoMovementFromTheDate(CustomersHaveNoMovementFromTheDate noMovementFromTheDate) {
        this.noMovementFromTheDate = noMovementFromTheDate;
    }

    /**
     * @return the list
     */
    public List<CustomersHaveNoMovementFromTheDate> getList() {
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(List<CustomersHaveNoMovementFromTheDate> list) {
        this.list = list;
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

}
