/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.isag;

import com.isag.businessservice.InvItemInstProjectGuideViewService;
import com.isag.converter.InvItemInstProjectGuideViewConverter;
import com.isag.views.InvItemInstProjectGuideView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 *
 * @author AhmedEssam
 */
public abstract class instPurchaseInvoiceDataForm extends BaseFormBean implements BaseManagedBean {

    private ExternalContext context;
    private UserData userData;
    private String ScreenMode;
    private Boolean columnRender = true;
    private List<InvItemInstProjectGuideView> itemInstProjectGuideViewList;
    private InvItemInstProjectGuideViewConverter invItemInstProjectGuideViewConverter;
   
    @EJB
    private InvItemInstProjectGuideViewService  invItemInstProjectGuideViewService;
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

    public void exit(String url) {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            fc.getExternalContext().redirect(url);
        } catch (IOException ex) {
            Logger.getLogger(null).log(Level.SEVERE, null, ex);
        }
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
    
     public List<InvItemInstProjectGuideView> completeInvItemInstProjectGuideViewDTO(String query) {
        List<InvItemInstProjectGuideView> invItemInstProjectGuideView = getItemInstProjectGuideViewList();
        if (query == null || query.trim().equals("")) {
            setInvItemInstProjectGuideViewConverter(new InvItemInstProjectGuideViewConverter(invItemInstProjectGuideView));
            return invItemInstProjectGuideView;
        }
        List<InvItemInstProjectGuideView> filteredInvItemInstProjectGuideView = new ArrayList<>();

        String nameAr;
        String code;
        InvItemInstProjectGuideView itemInstProjectGuideView;
        for (int i = 0; i < getItemInstProjectGuideViewList().size(); i++) {
            itemInstProjectGuideView = getItemInstProjectGuideViewList().get(i);
            nameAr = itemInstProjectGuideView.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvItemInstProjectGuideView.contains(itemInstProjectGuideView)) {
                    filteredInvItemInstProjectGuideView.add(itemInstProjectGuideView);
                }
            }

            code = itemInstProjectGuideView.getCode();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredInvItemInstProjectGuideView.contains(itemInstProjectGuideView)) {
                    filteredInvItemInstProjectGuideView.add(itemInstProjectGuideView);
                }
            }
        }

        setInvItemInstProjectGuideViewConverter(new InvItemInstProjectGuideViewConverter(filteredInvItemInstProjectGuideView));
        return filteredInvItemInstProjectGuideView;
    }

    /**
     * @return the context
     */
    public ExternalContext getContext() {
        return context;
    }

    /**
     * @param context the context to set
     */
    public void setContext(ExternalContext context) {
        this.context = context;
    }

    /**
     * @return the userData
     */
    public UserData getUserData() {
        return userData;
    }

    /**
     * @param userData the userData to set
     */
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    /**
     * @return the ScreenMode
     */
    public String getScreenMode() {
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
     * @return the itemInstProjectGuideViewDTOList
     */
    public List<InvItemInstProjectGuideView> getItemInstProjectGuideViewList() {
        if(itemInstProjectGuideViewList == null || itemInstProjectGuideViewList.isEmpty()){
            setItemInstProjectGuideViewList(invItemInstProjectGuideViewService.findInvItemInstProjectGuideView(getUserData().getUserBranch().getId()));
            setInvItemInstProjectGuideViewConverter(new InvItemInstProjectGuideViewConverter(itemInstProjectGuideViewList));
        }
        return itemInstProjectGuideViewList;
    }

    /**
     * @param itemInstProjectGuideViewDTOList the itemInstProjectGuideViewDTOList to set
     */
    public void setItemInstProjectGuideViewList(List<InvItemInstProjectGuideView> itemInstProjectGuideViewList) {
        this.itemInstProjectGuideViewList = itemInstProjectGuideViewList;
    }

    /**
     * @return the invItemInstProjectGuideViewDTOConverter
     */
    public InvItemInstProjectGuideViewConverter getInvItemInstProjectGuideViewDTOConverter() {
        return invItemInstProjectGuideViewConverter;
    }

    /**
     * @param invItemInstProjectGuideViewDTOConverter the invItemInstProjectGuideViewDTOConverter to set
     */
    public void setInvItemInstProjectGuideViewConverter(InvItemInstProjectGuideViewConverter invItemInstProjectGuideViewConverter) {
        this.invItemInstProjectGuideViewConverter = invItemInstProjectGuideViewConverter;
    }
}
