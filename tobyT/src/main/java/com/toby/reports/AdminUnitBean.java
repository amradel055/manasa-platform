/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.reports;

import com.toby.businessservice.AdminUnitsService;
import com.toby.businessservice.reports.searchBean.AdminUnitSearchBean;
import com.toby.converter.GlAdminUnitConverter;
import com.toby.entity.GlAdminUnit;
import com.toby.entity.TobyCompany;
import com.toby.toby.BaseGlAccountReportBean;
import com.toby.toby.UserData;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author hq002
 */
@Named(value = "adminUnitMBean")
@ViewScoped
public class AdminUnitBean extends BaseGlAccountReportBean {

    private UserData userData;
    private List<GlAdminUnit> allAdminUnits;
    private List<AdminUnitReport> allAdminUnitsReport;
    private AdminUnitSearchBean adminUnitSearchBean;
    private GlAdminUnitConverter glAdminUnitConverter;
    private String unitNameChar;
    @EJB
    AdminUnitsService adminUnitsService;

    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        userData = (UserData) context.getSessionMap().get("userLogInData");
        allAdminUnitsReport = new ArrayList<>();
        load();
        reset();
        glAdminUnitConverter = new GlAdminUnitConverter(getGlAdminUnitList());

    }

    @Override
    public void search() {
        allAdminUnitsReport = new ArrayList<>();

        TobyCompany  companyId = getUserData().getCompany();
        adminUnitSearchBean.setCompanyId(companyId.getId());

        allAdminUnits = adminUnitsService.getAdminUnitsBBranchId(getUserData().getUserBranch().getId(), getAdminUnitSearchBean());
        AdminUnitReport adminUnitReport;
        for (GlAdminUnit glAdminUnit : allAdminUnits) {
            adminUnitReport = new AdminUnitReport();
            adminUnitReport.setLevel(glAdminUnit.getLevel() != null ? glAdminUnit.getLevel() : null);
            adminUnitReport.setName(glAdminUnit.getName() != null ? glAdminUnit.getName() : null);
//             Code=glAdminUnit.getParent().getCode();
            if (glAdminUnit.getParent() != null) {
                adminUnitReport.setParent(glAdminUnit.getParent().getCode());
                adminUnitReport.setParentName(glAdminUnit.getParent().getName());
            } else {
                adminUnitReport.setParent(null);
                adminUnitReport.setParentName(null);
            }

            adminUnitReport.setCode(glAdminUnit.getCode() != null ? glAdminUnit.getCode() : null);

            if (glAdminUnit.getActive() == true) {
                adminUnitReport.setStatus("نشط");
            } else {
                adminUnitReport.setStatus("غير نشط");
            }

            allAdminUnitsReport.add(adminUnitReport);
        }
        unitNameChar = adminUnitSearchBean.getUnitName();
    }

    @Override
    public void reset() {
        adminUnitSearchBean = new AdminUnitSearchBean();
    }

    public void viewPDF(ActionEvent actionEvent) throws JRException, IOException {
        String webpath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/AdminUnit.jasper");
        File file = new File(webpath);
        JRBeanCollectionDataSource bCDSource = new JRBeanCollectionDataSource(allAdminUnitsReport);
        byte[] bytes = JasperRunManager.runReportToPdf(file.getPath(), new HashMap(), bCDSource);
        HttpServletResponse hsr = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        hsr.setHeader("Content-Disposition", "inline");
        hsr.setContentType("application/pdf");
        hsr.setContentLength(bytes.length);
        ServletOutputStream outputStream = hsr.getOutputStream();
        outputStream.write(bytes, 0, bytes.length);
        outputStream.flush();
        outputStream.close();
        FacesContext.getCurrentInstance().responseComplete();
    }

    public List<GlAdminUnit> getAllAdminUnits() {
        return allAdminUnits;
    }

    public void setAllAdminUnits(List<GlAdminUnit> allAdminUnits) {
        this.allAdminUnits = allAdminUnits;
    }

    public List<AdminUnitReport> getAllAdminUnitsReport() {
        return allAdminUnitsReport;
    }

    public void setAllAdminUnitsReport(List<AdminUnitReport> allAdminUnitsReport) {
        this.allAdminUnitsReport = allAdminUnitsReport;
    }

    public AdminUnitSearchBean getAdminUnitSearchBean() {
        return adminUnitSearchBean;
    }

    public void setAdminUnitSearchBean(AdminUnitSearchBean adminUnitSearchBean) {
        this.adminUnitSearchBean = adminUnitSearchBean;
    }

    public AdminUnitsService getAdminUnitsService() {
        return adminUnitsService;
    }

    public void setAdminUnitsService(AdminUnitsService adminUnitsService) {
        this.adminUnitsService = adminUnitsService;
    }

    public List<GlAdminUnit> completeAdminUnit(String query) {
        List<GlAdminUnit> glAdminUnits = getGlAdminUnitList();
        if (query == null || query.trim().equals("")) {
            glAdminUnitConverter = new GlAdminUnitConverter(glAdminUnits);
            return glAdminUnits;
        }
        List<GlAdminUnit> filteredGlAdminUnitList = new ArrayList<>();

        String nameAr;
        Integer code;
        GlAdminUnit glAdminUnit;
        for (int i = 0; i < getGlAdminUnitList().size(); i++) {
            glAdminUnit = glAdminUnits.get(i);
            nameAr = glAdminUnit.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredGlAdminUnitList.contains(glAdminUnit)) {
                    filteredGlAdminUnitList.add(glAdminUnit);
                }
            }

            code = glAdminUnit.getCode();
            if (code != null && String.valueOf(code).contains(query)) {
                if (!filteredGlAdminUnitList.contains(glAdminUnit)) {
                    filteredGlAdminUnitList.add(glAdminUnit);
                }
            }
        }
        glAdminUnitConverter = new GlAdminUnitConverter(filteredGlAdminUnitList);
        return filteredGlAdminUnitList;
    }

    @Override
    public HashMap prepareReport() {

        Map<String, String> userDDs = userData.getUserDDs();

        HashMap hashMap = new HashMap();
        
        hashMap.put("unitCodeFrom", getAdminUnitSearchBean().getUnitCodeFrom() != null ? getAdminUnitSearchBean().getUnitCodeFrom().getCode() : null);
        hashMap.put("unitCodeNameFrom", getAdminUnitSearchBean().getUnitCodeFrom() != null ? getAdminUnitSearchBean().getUnitCodeFrom().getName() : null);
        hashMap.put("unitCodeTo", getAdminUnitSearchBean().getUnitCodeTo() != null ? getAdminUnitSearchBean().getUnitCodeTo().getCode() : null);
        hashMap.put("unitCodeNameTo", getAdminUnitSearchBean().getUnitCodeTo() != null ? getAdminUnitSearchBean().getUnitCodeTo().getName() : null);
        if (unitNameChar != null && unitNameChar.length() == 0) {
            unitNameChar = null;
        }
        hashMap.put("unitCodeName", unitNameChar);
        hashMap.put("codeFromText", userDDs.get("CODE_FROM"));
        hashMap.put("codeToText", userDDs.get("CODE_TO"));
        hashMap.put("unitNameText", userDDs.get("UNIT_NAME"));
        hashMap.put("unitCodeText", userDDs.get("UNIT_CODE"));
        hashMap.put("unitParentText", userDDs.get("UNIT_PARENT"));
        hashMap.put("levelText", userDDs.get("LEVEL"));
        hashMap.put("statusText", userDDs.get("STATUS"));
        hashMap.put("adminUnitReport", userDDs.get("ADMIN_UNIT_REPORT"));
        hashMap.put("PrintedBy", userData.getUser().getName());
        //   hashMap.put("companyImage", getUserData().getImageReportPath());
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());

        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (allAdminUnitsReport != null && allAdminUnitsReport.size() != 0) {
            fillReport(prepareReport(), userData.getReportPath() + "glAdminUnitIndex.jasper", allAdminUnitsReport, "pdf");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "لا يوجد نتائج للطباعة"));
        }
    }

    @Override
    public void exportExcel(ActionEvent actionEvent) throws JRException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportHtml(ActionEvent actionEvent) throws JRException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void checkDate(Boolean dateType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetDateFrom() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetDateTo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the glAdminUnitConverter
     */
    public GlAdminUnitConverter getGlAdminUnitConverter() {
        return glAdminUnitConverter;
    }

    /**
     * @param glAdminUnitConverter the glAdminUnitConverter to set
     */
    public void setGlAdminUnitConverter(GlAdminUnitConverter glAdminUnitConverter) {
        this.glAdminUnitConverter = glAdminUnitConverter;
    }

    /**
     * @return the unitNameChar
     */
    public String getUnitNameChar() {
        return unitNameChar;
    }

    /**
     * @param unitNameChar the unitNameChar to set
     */
    public void setUnitNameChar(String unitNameChar) {
        this.unitNameChar = unitNameChar;
    }

}
