/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.reports;


import com.toby.businessservice.report.SupplierDataServiceReport;


import com.toby.report.entity.SupplierDataReportViewBean;
import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.reports.searchBean.SupplierDataReportSearchBean;
import com.toby.converter.InvOrganizationSiteConverter;
import com.toby.entity.InvOrganizationSite;
import com.toby.toby.BaseReportBean;
import com.toby.toby.UserData;

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
@Named("supplierDataReportMB")
@ViewScoped
public class SupplierDataReportMB extends BaseReportBean {

    // Objects
    private Integer branchId;
    Integer type;
    BigDecimal balance = BigDecimal.ZERO;
    private String uri;
    private Boolean isSupplier;
    private String screenMode;
    private List<SupplierDataReportViewBean> supplierDataReportViewBeanList;
    private SupplierDataReportSearchBean supplierDataReportSearchBean;

    private List<InvOrganizationSite> invOrgSiteList;
    private InvOrganizationSiteConverter invOrgSiteConverter;

    @EJB
    private SupplierDataServiceReport supplierDataServiceReport;
    @EJB
    private OrganizationSiteService organizationSiteService;

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

        setInvOrgSiteList(new ArrayList<>());
        setSupplierDataReportSearchBean(new SupplierDataReportSearchBean());
        getSupplierDataReportSearchBean().setBranchId(getBranchId());
        getSupplierDataReportSearchBean().setShowReport(false);
        if (uri.contains("supplierdata")) {
            supplierDataReportSearchBean.setType(0);
            isSupplier = true;
        } else {
            supplierDataReportSearchBean.setType(1);
            isSupplier = false;
        }

        setSupplierDataReportViewBeanList(new ArrayList<>());

    }

    private void fillLists() {
//        if (isSupplier) {
//            setInvOrgSiteList(getOrganizationSiteService().getorganizationSiteByBranchIdForGlBankModule(getUserData().getUserBranch().getId(),true,1)); // مورد
//
//        } else {
//            setInvOrgSiteList(getOrganizationSiteService().getorganizationSiteByBranchIdForGlBankModule(getUserData().getUserBranch().getId(),true,0)); //عميل
//
//        }
//
//        setInvOrgSiteConverter(new InvOrganizationSiteConverter(getInvOrgSiteList()));

    }

    @Override
    public void reset() {
        initObjs();
        fillLists();
       // supplierDataReportSearchBean = new SupplierDataReportSearchBean();

       // invOrgSiteList = new ArrayList<>();
    }

    public void showItemReport() {
        invOrgSiteList = new ArrayList<>();
    }

    @Override
    public void search() {
        if (isSupplier) {
            supplierDataReportSearchBean.setType(0);
        } else {
            supplierDataReportSearchBean.setType(1);
        }
        balance = BigDecimal.ZERO;
        setSupplierDataReportViewBeanList(new ArrayList<>());

        getSupplierDataReportSearchBean().setBranchId(getUserData().getUserBranch().getId());

        List<InvOrganizationSite> InvOrganizationSiteList = new ArrayList<>();

        InvOrganizationSiteList = supplierDataServiceReport.getAllSupplierDataReportSearchBean(getSupplierDataReportSearchBean());
        if (InvOrganizationSiteList != null && !InvOrganizationSiteList.isEmpty()) {
            for (InvOrganizationSite list : InvOrganizationSiteList) {
                SupplierDataReportViewBean bean = new SupplierDataReportViewBean();
                bean.setCode(list.getCode());
                bean.setName(list.getName());
                bean.setPhone(list.getPhone());
                bean.setAccountId(list.getAccountId());
                bean.setParent(list.getParent() != null ? (list.getParent().getName() != null ? list.getParent().getName() : null) : null);
                bean.setBalanceLimit(list.getBalanceLimit());
                bean.setDelegator(list.getDelegatorId()!=null ? (list.getDelegatorId().getName() !=null ? list.getDelegatorId().getName() : "" ):null);
                bean.setAccountName(list.getAccountId() !=null ?(list.getAccountId().getName() != null ? list.getAccountId().getName() : null ):null);
                getSupplierDataReportViewBeanList().add(bean);
            }
        }

    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        search();
        if (supplierDataReportSearchBean.getShowReport() != null && supplierDataReportSearchBean.getShowReport()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "SupplierDataReport.jasper", getSupplierDataReportViewBeanList(), "pdf");
        } else {
            fillReport(prepareReport(), getUserData().getReportPath() + "CustomerDataReport.jasper", getSupplierDataReportViewBeanList(), "pdf");
        }

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
        if (isSupplier) {
            hashMap.put("header2", userDDs.get("BASIC_DATA_SUPPLIER"));
            hashMap.put("codeText", userDDs.get("SUPPL_CODE"));
           
        } else {
            hashMap.put("header2", userDDs.get("BASIC_DATA_CUSTOMERS"));
            hashMap.put("codeText", userDDs.get("CUSTOM_CODE"));
            
        }
 if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
 hashMap.put("Fromorganization","من مورد:");
        hashMap.put("FromorganizationValue",supplierDataReportSearchBean.getFromorganizationName()!=null ? supplierDataReportSearchBean.getFromorganizationName().getName():"" );
        hashMap.put("Toorganization", "إلي مورد:");
        hashMap.put("ToorganizationValue",supplierDataReportSearchBean.getToorganizationName()!=null ? supplierDataReportSearchBean.getToorganizationName().getName() :"");
        hashMap.put("orgSiteText", userDDs.get("NAME"));
        hashMap.put("phoneText", userDDs.get("PHONE"));
        hashMap.put("balanceLimitText", userDDs.get("CREDIT_LIMIT"));
        hashMap.put("parentText", userDDs.get("ACCOUNT_NUMBER"));
        hashMap.put("delText", userDDs.get("DELEGATOR"));
        return hashMap;
    }

    public List<InvOrganizationSite> completOrgSite(String query) {
        List<InvOrganizationSite> invOrgSiteList = getInvOrgSiteList();
        if (query == null || query.trim().equals("")) {

            setInvOrgSiteConverter(new InvOrganizationSiteConverter(invOrgSiteList));
            return invOrgSiteList;
        }
        List<InvOrganizationSite> filteredInvs = new ArrayList<>();

        String nameAr;
        String code;
        InvOrganizationSite invOrgSiteFilter;

        for (int i = 0; i < getInvOrgSiteList().size(); i++) {
            invOrgSiteFilter = invOrgSiteList.get(i);
            nameAr = invOrgSiteFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invOrgSiteFilter)) {
                    filteredInvs.add(invOrgSiteFilter);
                }
            }

            code = invOrgSiteFilter.getCode().toString();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invOrgSiteFilter)) {
                    filteredInvs.add(invOrgSiteFilter);
                }
            }
        }

        setInvOrgSiteConverter(new InvOrganizationSiteConverter(filteredInvs));
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
     * @return the invOrgSiteList
     */
    public List<InvOrganizationSite> getInvOrgSiteList() {
        return invOrgSiteList;
    }

    /**
     * @param invOrgSiteList the invOrgSiteList to set
     */
    public void setInvOrgSiteList(List<InvOrganizationSite> invOrgSiteList) {
        this.invOrgSiteList = invOrgSiteList;
    }

    /**
     * @return the invOrgSiteConverter
     */
    public InvOrganizationSiteConverter getInvOrgSiteConverter() {
        return invOrgSiteConverter;
    }

    /**
     * @param invOrgSiteConverter the invOrgSiteConverter to set
     */
    public void setInvOrgSiteConverter(InvOrganizationSiteConverter invOrgSiteConverter) {
        this.invOrgSiteConverter = invOrgSiteConverter;
    }

    /**
     * @return the organizationSiteService
     */
    public OrganizationSiteService getOrganizationSiteService() {
        return organizationSiteService;
    }

    /**
     * @param organizationSiteService the organizationSiteService to set
     */
    public void setOrganizationSiteService(OrganizationSiteService organizationSiteService) {
        this.organizationSiteService = organizationSiteService;
    }

    /**
     * @return the supplierDataReportViewBeanList
     */
    public List<SupplierDataReportViewBean> getSupplierDataReportViewBeanList() {
        return supplierDataReportViewBeanList;
    }

    /**
     * @param supplierDataReportViewBeanList the supplierDataReportViewBeanList
     * to set
     */
    public void setSupplierDataReportViewBeanList(List<SupplierDataReportViewBean> supplierDataReportViewBeanList) {
        this.supplierDataReportViewBeanList = supplierDataReportViewBeanList;
    }

    /**
     * @return the supplierDataReportSearchBean
     */
    public SupplierDataReportSearchBean getSupplierDataReportSearchBean() {
        return supplierDataReportSearchBean;
    }

    /**
     * @param supplierDataReportSearchBean the supplierDataReportSearchBean to
     * set
     */
    public void setSupplierDataReportSearchBean(SupplierDataReportSearchBean supplierDataReportSearchBean) {
        this.supplierDataReportSearchBean = supplierDataReportSearchBean;
    }

    /**
     * @return the supplierDataServiceReport
     */
    public SupplierDataServiceReport getSupplierDataServiceReport() {
        return supplierDataServiceReport;
    }

    /**
     * @param supplierDataServiceReport the supplierDataServiceReport to set
     */
    public void setSupplierDataServiceReport(SupplierDataServiceReport supplierDataServiceReport) {
        this.supplierDataServiceReport = supplierDataServiceReport;
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
     * @return the isSupplier
     */
    public Boolean getIsSupplier() {
        return isSupplier;
    }

    /**
     * @param isSupplier the isSupplier to set
     */
    public void setIsSupplier(Boolean isSupplier) {
        this.isSupplier = isSupplier;
    }

}
