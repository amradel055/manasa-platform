/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.toby;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
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

    public <T> void fillReport(HashMap hashMap, String reportPath, List<T> listBean, String exportType) throws JRException, IOException {
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listBean);
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, hashMap, beanCollectionDataSource);
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        if ("pdf".equals(exportType)) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            httpServletResponse.flushBuffer();
            servletOutputStream.flush();
            servletOutputStream.close();
            return;
        } else if ("excel".equals(exportType)) {

        } else if ("html".equals(exportType)) {

        }

    }
    
//     public boolean isFileExist(String filePath) {
//        File file = new File(getUserData().getImageReportPath());
//        return file.exists();
//    }


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
