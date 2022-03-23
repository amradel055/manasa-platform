/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.isag;

import com.isag.businessservice.ReportManagementService;
import com.isag.entity.ReportManagement;
import java.io.IOException;
import java.util.HashMap;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import net.sf.jasperreports.engine.JRException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 *
 * @author hq002
 */
public abstract class BaseReportBean extends Basic implements BaseManagedBean {

    @EJB
    private ReportManagementService reportManagementService;

    private UserData userData;

    private String exportType;

    private Boolean showMessageGeneral = false;

    private boolean stickyHeader = false;

    @Override
    public abstract void load();

    public abstract void reset();

    public abstract void search();

    public abstract HashMap prepareReport();

    public abstract void exportPDF(ActionEvent actionEvent) throws JRException, IOException;

    public abstract void exportExcel(ActionEvent actionEvent) throws JRException, IOException;

    public abstract void exportHtml(ActionEvent actionEvent) throws JRException, IOException;

    public boolean errorMessage(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), msg));
        return false;
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

//     public boolean isFileExist(String filePath) {
//        File file = new File(getUserData().getImageReportPath());
//        return file.exists();
//    }
    public void reportManagement(HashMap hashMap) {
        ReportManagement reportManagement = reportManagementService.reportManagementByBranch(getUserData().getUserBranch().getId());
        if (reportManagement != null) {
            hashMap.put("LeftUp1", reportManagement.getLeftUp1());
            hashMap.put("LeftUp2", reportManagement.getLeftUp2());
            hashMap.put("LeftUp3", reportManagement.getLeftUp3());
            hashMap.put("LeftUp4", reportManagement.getLeftUp4());
            hashMap.put("RightUp1", reportManagement.getRightUp1());
            hashMap.put("RightUp2", reportManagement.getRightUp2());
            hashMap.put("RightUp3", reportManagement.getRightUp3());
            hashMap.put("RightUp4", reportManagement.getRightUp4());
        }
        hashMap.put("PrintedBy", getUserData().getUser().getName());
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
    }

    @Override
    public abstract String getScreenName();

    /**
     * @return the userData
     */
    public UserData getUserData() {
        if (userData == null) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            userData = (UserData) context.getSessionMap().get("userLogInData");
        }
        return userData;
    }

    /**
     * @param userData the userData to set
     */
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    /**
     * @return the exportType
     */
    public String getExportType() {
        return exportType;
    }

    /**
     * @param exportType the exportType to set
     */
    public void setExportType(String exportType) {
        this.exportType = exportType;
    }

    /**
     * @return the showMessageGeneral
     */
    public Boolean getShowMessageGeneral() {
        return showMessageGeneral;
    }

    /**
     * @param showMessageGeneral the showMessageGeneral to set
     */
    public void setShowMessageGeneral(Boolean showMessageGeneral) {
        this.showMessageGeneral = showMessageGeneral;
    }

    /**
     * @return the stickyHeader
     */
    public boolean isStickyHeader() {
        return stickyHeader;
    }

    /**
     * @param stickyHeader the stickyHeader to set
     */
    public void setStickyHeader(boolean stickyHeader) {
        this.stickyHeader = stickyHeader;
    }

}
