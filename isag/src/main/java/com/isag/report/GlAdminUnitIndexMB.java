/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.GlAdminUnitService;
import com.isag.businessservice.reports.searchBean.GlAdminUnitSearchBean;
import com.isag.entity.GlAdminUnit;
import com.isag.isag.BaseGlAccountReportBean;
import com.isag.isag.UserData;
import com.isag.report.entity.GlAdminUnitIndexBean;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author hq002
 */
public class GlAdminUnitIndexMB extends BaseGlAccountReportBean {

    private List<GlAdminUnit> glAdminUnit;
    private List<GlAdminUnitIndexBean> adminUnitIndexBeanList;
    private UserData userData;
    private JasperPrint jasperPrint;

    private GlAdminUnitSearchBean glAdminUnitSearchBean;

    @EJB
    GlAdminUnitService gladminUnitService;

    public GlAdminUnitIndexMB() {
    }

    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        userData = (UserData) context.getSessionMap().get("userLogInData");

        reset();
        load();
    }

    @Override
    public void reset() {

        //complexRevisionBalanceSearchBean = new ComplexRevisionBalanceSearchBean();
        glAdminUnitSearchBean = new GlAdminUnitSearchBean();
    }

    @Override
    public void search() {
        adminUnitIndexBeanList = new ArrayList<>(0);
        glAdminUnitSearchBean.setBranchId(getUserData().getUserBranch().getId());
        glAdminUnit = gladminUnitService.getAllAdminUnitIndex(glAdminUnitSearchBean);
        for (GlAdminUnit adminUnit : glAdminUnit) {
            GlAdminUnitIndexBean adminUnitIndexBean = new GlAdminUnitIndexBean();
            if (adminUnit.getActive()) {
                adminUnitIndexBean.setActive(userData.getUserDDs().get("ACTIVE"));
            } else {
                adminUnitIndexBean.setActive(userData.getUserDDs().get("NOT_ACTIVE"));
            }
            adminUnitIndexBean.setName(adminUnit.getName().toString());
            adminUnitIndexBean.setCode(adminUnit.getCode());
            adminUnitIndexBean.setShort_code(adminUnit.getShortCode());
            adminUnitIndexBean.setLevel(adminUnit.getLevel());

            adminUnitIndexBeanList.add(adminUnitIndexBean);
        }
    }

    public List<GlAdminUnit> getGlAdminUnit() {
        return glAdminUnit;
    }

    public void setGlAdminUnit(List<GlAdminUnit> glAdminUnit) {
        this.glAdminUnit = glAdminUnit;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public JasperPrint getJasperPrint() {
        return jasperPrint;
    }

    public void setJasperPrint(JasperPrint jasperPrint) {
        this.jasperPrint = jasperPrint;
    }

    public GlAdminUnitSearchBean getGlAdminUnitSearchBean() {
        return glAdminUnitSearchBean;
    }

    public void setGlAdminUnitSearchBean(GlAdminUnitSearchBean glAdminUnitSearchBean) {
        this.glAdminUnitSearchBean = glAdminUnitSearchBean;
    }

    public GlAdminUnitService getGladminUnitService() {
        return gladminUnitService;
    }

    public void setGladminUnitService(GlAdminUnitService gladminUnitService) {
        this.gladminUnitService = gladminUnitService;
    }

    /**
     * @return the adminUnitIndexBeanList
     */
    public List<GlAdminUnitIndexBean> getAdminUnitIndexBeanList() {
        return adminUnitIndexBeanList;
    }

    /**
     * @param adminUnitIndexBeanList the adminUnitIndexBeanList to set
     */
    public void setAdminUnitIndexBeanList(List<GlAdminUnitIndexBean> adminUnitIndexBeanList) {
        this.adminUnitIndexBeanList = adminUnitIndexBeanList;
    }

    @Override
    public HashMap prepareReport() {
        HashMap hashMap = new HashMap();
        
        hashMap.put("adminUnitFrom", glAdminUnitSearchBean.getAdminUnitFrom());
        hashMap.put("adminUnitTo", glAdminUnitSearchBean.getAdminUnitTo());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());

        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        search();
        if (adminUnitIndexBeanList != null && !adminUnitIndexBeanList.isEmpty()) {
            fillReport(prepareReport(), "D:\\Reports\\glAdminUnitIndex.jasper", adminUnitIndexBeanList, "pdf");
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
}
