/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.isag;

import com.isag.businessservice.CompanyService;
import com.isag.core.UserDataDTO;
import com.isag.entity.IgmaCompany;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.primefaces.context.RequestContext;

/**
 *
 * @author hq004
 */
public abstract class BaseFormBean extends Basic implements BaseManagedBean {

    private Integer index = 999999;

    
    
    private UserDataDTO userDataDTO;
    private String ScreenMode;
    private Boolean columnRender = true;
    private String className = "BaseFormBean";
    private Map<String, String> userDDs;
    
    
    

    public abstract String save();

    public abstract void init();

    public boolean errorMessage(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), msg));
        return false;
    }

    public boolean savedMeesage(String msg) {

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), msg));
        return false;
    }

    public boolean deletedMeesage() {

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, getUserData().getUserDDs().get("INFO"), "تم الحذف بنجاح"));
        return false;
    }

    public void errorMsg(String error) {
        if (error != null && !error.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), error));
        }
    }

    public void msgSave(String error) {
        if (error != null && !error.isEmpty()) {
            errorMsg(error);
        } else {
            savedMeesage(getUserData().getUserDDs().get("SAVED"));
        }
    }

    public void exit(String url) {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            fc.getExternalContext().redirect(url);
        } catch (IOException ex) {
            Logger.getLogger(null).log(Level.SEVERE, null, ex);
        }
    }

    public void OpenDlg(String dlgvar) {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('" + dlgvar + "').show();");
    }

    public void CloseDlg(String dlgvar) {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('" + dlgvar + "').hide();");
    }
    public void changeFocus(String focusId) {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PrimeFaces.focus('"+focusId+"')");
    }

    public void preProcessXLS() {
        setColumnRender((Boolean) false);
    }

    public void postProcessXLS(Object document) {
        setColumnRender((Boolean) true);
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

    public boolean isFileExist(String filePath) {
        try {
            if (filePath != null) {
                File file = new File(filePath);
                return file.exists();
            } else {
                return false;
            }
        } catch (Exception e) {
            saveError(e, "BaseFormBean", "isFileExist");
        }
        return false;
    }

    /**
     * @return the ScreenMode
     */
    public String getScreenMode() {
        setScreenMode((String) getContext().getSessionMap().get("ScreenMode"));
        return ScreenMode;
    }

    /**
     * @param ScreenMode the ScreenMode to set
     */
    public void setScreenMode(String ScreenMode) {
        this.ScreenMode = ScreenMode;
    }

    /**
     * @return the columnRender
     */
    public Boolean getColumnRender() {
        return columnRender;
    }

    /**
     * @param columnRender the columnRender to set
     */
    public void setColumnRender(Boolean columnRender) {
        this.columnRender = columnRender;
    }

    /**
     * @return the index
     */
    public Integer getIndex() {
        index = index + 1;
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * @return the userDataDTO
     */
    public UserDataDTO getUserDataDTO() {
        if (userDataDTO == null) {
            setUserDataDTO((UserDataDTO) getContext().getSessionMap().get("userDataDTO"));
        }
        return userDataDTO;
    }

    /**
     * @param userDataDTO the userDataDTO to set
     */
    public void setUserDataDTO(UserDataDTO userDataDTO) {
        this.userDataDTO = userDataDTO;
    }

    /**
     * @return the userDDs
     */
    public Map<String, String> getUserDDs() {
        if (userDDs == null) {
            userDDs = getUserData().getUserDDs();
        }
        return userDDs;
    }

    /**
     * @param userDDs the userDDs to set
     */
    public void setUserDDs(Map<String, String> userDDs) {
        this.userDDs = userDDs;
    }
    
}
