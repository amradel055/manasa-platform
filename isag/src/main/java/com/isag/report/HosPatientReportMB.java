/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.PatientRegistrationService;

import com.isag.businessservice.reports.HosPatientServiceReport;

import com.isag.businessservice.reports.SupplierDataServiceReport;
import com.isag.businessservice.reports.searchBean.HosPatientReportSearchBean;

import com.isag.businessservice.reports.searchBean.SupplierDataReportSearchBean;
import com.isag.converter.HosPatientRegistrationConvertor;

import com.isag.converter.InvOrganizationSiteConverter;

import com.isag.entity.HosPatientRegistration;

import com.isag.entity.InvOrganizationSite;

import com.isag.isag.BaseReportBean;
import com.isag.isag.UserData;
import com.isag.report.entity.PatientReportViewBean;

import com.isag.report.entity.SupplierDataReportViewBean;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author hhhh
 */
@Named("hosPatientReportMB")
@ViewScoped
public class HosPatientReportMB extends BaseReportBean {

    // Objects
    private Integer branchId;
    Integer type;
   
    private String uri;
    private Boolean isPatient;
    private String screenMode;
    private List<PatientReportViewBean> patientReportViewBean;
    private HosPatientReportSearchBean patientReportSearchBean;

    private List<HosPatientRegistration> patientRegistrationList;
    private HosPatientRegistrationConvertor patientRegistrationConvertor;

    @EJB
    private HosPatientServiceReport patientServiceReport;
     @EJB
    private PatientRegistrationService patientRegistrationService;

    @Override
    @PostConstruct
    public void load() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        setScreenMode((String) context.getSessionMap().get("ScreenMode"));
        setBranchId(getUserData().getUserBranch().getId());

        setUri(((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI());
        initObjs();

        fillLists();

    }

    private void initObjs() {

        patientRegistrationList=new ArrayList<>();
        patientReportSearchBean=new HosPatientReportSearchBean();
        patientReportSearchBean.setBranchId(getBranchId());
        patientReportSearchBean.setShowReport(false);
        if (uri.contains("hosPatientReport")) {
            patientReportSearchBean.setType(0);
            isPatient = true;
        } else {
            patientReportSearchBean.setType(1);
            isPatient = false;
        }

       patientReportViewBean=new ArrayList<>();

    }

    private void fillLists() {
        if (isPatient) {
           patientRegistrationList=patientRegistrationService.getPatientByBranchId(branchId); // مورد

        } else {
            patientRegistrationList=patientRegistrationService.getCompanyByBranchId(branchId); //عميل

        }

      patientRegistrationConvertor =new HosPatientRegistrationConvertor(getPatientRegistrationList());

    }

    @Override
    public void reset() {
        initObjs();
        fillLists();
         patientReportSearchBean=new HosPatientReportSearchBean();

       patientRegistrationList=new ArrayList<>();
    }

    public void showItemReport() {
       patientRegistrationList=new ArrayList<>();
    }

    @Override
    public void search() {
       if (isPatient) {
            patientReportSearchBean.setType(0);
        } else {
            patientReportSearchBean.setType(1);
        }
       
        patientReportViewBean=new ArrayList<>();

       patientReportSearchBean.setBranchId(getUserData().getUserBranch().getId());

        List<HosPatientRegistration> hosPatientRegistrationList = new ArrayList<>();

        hosPatientRegistrationList = patientServiceReport.getAllPatientDataBySearchBean(patientReportSearchBean);
        if (hosPatientRegistrationList != null && !hosPatientRegistrationList.isEmpty()) {
            for (HosPatientRegistration list : hosPatientRegistrationList) {
                PatientReportViewBean bean = new PatientReportViewBean();
                bean.setCode(list.getCode()!=null ?list.getCode():"");
                bean.setName(list.getName()!=null ?list.getName():"");
                bean.setAddress(list.getAddress()!=null ? list.getAddress() : "");
                bean.setParent(list.getParent()!=null? list.getParent().getName() :"");
                bean.setPhone(list.getPhone()!=null ?list.getPhone() :"");
               
                getPatientReportViewBean().add(bean);
            }
        }

    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        search();
    
            fillReport(prepareReport(), getUserData().getReportPath() + "PatientCompanyReport.jasper", getPatientReportViewBean(), "pdf");
        

    }

    @Override
    public <T> void fillReport(HashMap hashMap, String reportPath, List<T> listBean, String exportType) throws JRException, IOException {
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listBean);
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, hashMap, beanCollectionDataSource);
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        if ("pdf".equals(exportType)) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            servletOutputStream.flush();
            servletOutputStream.close();
            httpServletResponse.getOutputStream().flush();
            httpServletResponse.getOutputStream().close();
        } else if ("excel".equals(exportType)) {

        } else if ("html".equals(exportType)) {

        }
    }

    @Override
    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();

        hashMap.put("UserName", getUserData().getUser().getName());
        hashMap.put("Branch", getUserData().getUserBranch().getNameAr());
      //  hashMap.put("CompanyName", getUserData().getCompany().getName());

      //  hashMap.put("CompanyLogo", getUserData().getImageReportPath());
        hashMap.put("dateText", userDDs.get("DATE"));
        if (isPatient) {
            hashMap.put("header2", userDDs.get("PAT_MAIN_DATA"));
            hashMap.put("codeText", userDDs.get("PATIENT_CODE"));

        } else {
            hashMap.put("header2", userDDs.get("COMP_MAIN_DATA"));
            hashMap.put("codeText", userDDs.get("COMPANY_CODE"));

        }
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        hashMap.put("patientText", userDDs.get("NAME"));
        hashMap.put("phoneText", userDDs.get("PHONE"));
        hashMap.put("addressText", userDDs.get("ADDRESS"));
        hashMap.put("parentText", userDDs.get("INSURANCE_COMP"));
       
        return hashMap;
    }

    public List<HosPatientRegistration> completPatient(String query) {
        List<HosPatientRegistration> hosPatientRegistrationList= getPatientRegistrationList();
        if (query == null || query.trim().equals("")) {

            setPatientRegistrationConvertor(new HosPatientRegistrationConvertor(hosPatientRegistrationList));
            return hosPatientRegistrationList;
        }
        List<HosPatientRegistration> filteredInvs = new ArrayList<>();

        String nameAr;
        String code;
        HosPatientRegistration hosPatientFilter;

        for (int i = 0; i < getPatientRegistrationList().size(); i++) {
            hosPatientFilter = hosPatientRegistrationList.get(i);
            nameAr = hosPatientFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(hosPatientFilter)) {
                    filteredInvs.add(hosPatientFilter);
                }
            }

            code = hosPatientFilter.getCode().toString();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(hosPatientFilter)) {
                    filteredInvs.add(hosPatientFilter);
                }
            }
        }

        setPatientRegistrationConvertor(new HosPatientRegistrationConvertor(filteredInvs));
        return filteredInvs;
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

    public String getScreenMode() {
        return screenMode;
    }

    public void setScreenMode(String screenMode) {
        this.screenMode = screenMode;
    }

    /*  public Boolean getIsSales() {
     return isSales;
     }

     public void setIsSales(Boolean isSales) {
     this.isSales = isSales;
     } */
    /**
     * @return the branchId
     */
    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

  

    /**
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return the branchId
     */
    public Integer getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    

    /**
     * @return the patientReportViewBean
     */
    public List<PatientReportViewBean> getPatientReportViewBean() {
        return patientReportViewBean;
    }

    /**
     * @param patientReportViewBean the patientReportViewBean to set
     */
    public void setPatientReportViewBean(List<PatientReportViewBean> patientReportViewBean) {
        this.patientReportViewBean = patientReportViewBean;
    }

    /**
     * @return the patientReportSearchBean
     */
    public HosPatientReportSearchBean getPatientReportSearchBean() {
        return patientReportSearchBean;
    }

    /**
     * @param patientReportSearchBean the patientReportSearchBean to set
     */
    public void setPatientReportSearchBean(HosPatientReportSearchBean patientReportSearchBean) {
        this.patientReportSearchBean = patientReportSearchBean;
    }

    /**
     * @return the patientRegistrationList
     */
    public List<HosPatientRegistration> getPatientRegistrationList() {
        return patientRegistrationList;
    }

    /**
     * @param patientRegistrationList the patientRegistrationList to set
     */
    public void setPatientRegistrationList(List<HosPatientRegistration> patientRegistrationList) {
        this.patientRegistrationList = patientRegistrationList;
    }

    /**
     * @return the patientRegistrationConvertor
     */
    public HosPatientRegistrationConvertor getPatientRegistrationConvertor() {
        return patientRegistrationConvertor;
    }

    /**
     * @param patientRegistrationConvertor the patientRegistrationConvertor to set
     */
    public void setPatientRegistrationConvertor(HosPatientRegistrationConvertor patientRegistrationConvertor) {
        this.patientRegistrationConvertor = patientRegistrationConvertor;
    }

    /**
     * @return the patientServiceReport
     */
    public HosPatientServiceReport getPatientServiceReport() {
        return patientServiceReport;
    }

    /**
     * @param patientServiceReport the patientServiceReport to set
     */
    public void setPatientServiceReport(HosPatientServiceReport patientServiceReport) {
        this.patientServiceReport = patientServiceReport;
    }

    /**
     * @return the isPatient
     */
    public Boolean getIsPatient() {
        return isPatient;
    }

    /**
     * @param isPatient the isPatient to set
     */
    public void setIsPatient(Boolean isPatient) {
        this.isPatient = isPatient;
    }

    /**
     * @return the patientRegistrationService
     */
    public PatientRegistrationService getPatientRegistrationService() {
        return patientRegistrationService;
    }

    /**
     * @param patientRegistrationService the patientRegistrationService to set
     */
    public void setPatientRegistrationService(PatientRegistrationService patientRegistrationService) {
        this.patientRegistrationService = patientRegistrationService;
    }

}
