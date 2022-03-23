/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.InvDelegatorService;
import com.isag.businessservice.reports.SalesDataReportService;
import com.isag.businessservice.reports.searchBean.SalesDataReportSearchBean;
import com.isag.converter.InvDelegatorConvertor;
import com.isag.entity.InventoryDelegator;

import com.isag.isag.BaseReportBean;
import com.isag.isag.UserData;

import com.isag.report.entity.SalesDataReportViewBean;

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
@Named("salesDataReportMB")
@ViewScoped
public class SalesDataReportMB extends BaseReportBean {

    // Objects
    private Integer branchId;
    Integer type;
    BigDecimal balance = BigDecimal.ZERO;
    private String uri;
    private Boolean isSales;
    private String screenMode;
    private List<SalesDataReportViewBean> salesDataReportViewBeanList;
    private SalesDataReportSearchBean salesDataReportSearchBean;

    private List<InventoryDelegator> inventoryDelegatorList;
    private InvDelegatorConvertor invDelegatorConvertor;

    @EJB
    private SalesDataReportService salesDataReportService;
    @EJB
    private InvDelegatorService invDelegatorService;

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

        setInventoryDelegatorList(new ArrayList<>());
        setSalesDataReportSearchBean(new SalesDataReportSearchBean());
        getSalesDataReportSearchBean().setBranchId(getBranchId());
        getSalesDataReportSearchBean().setShowReport(false);
        if (uri.contains("salesDataReport")) {
            salesDataReportSearchBean.setType(0);
            isSales = true;
        } else {
            salesDataReportSearchBean.setType(1);
            isSales = false;
        }

        setSalesDataReportViewBeanList(new ArrayList<>());

    }

    private void fillLists() {
        if (isSales) {
            setInventoryDelegatorList(getInvDelegatorService().getSalesByBranch(getBranchId())); // مبيعات

        } else {
            setInventoryDelegatorList(getInvDelegatorService().getPurchaseByBranch(getBranchId())); //مشتريات

        }

        setInvDelegatorConvertor(new InvDelegatorConvertor(getInventoryDelegatorList()));

    }

    @Override
    public void reset() {
        initObjs();
        fillLists();
       // salesDataReportSearchBean = new SalesDataReportSearchBean();

       // inventoryDelegatorList = new ArrayList<>();
    }

    public void showItemReport() {

    }

    @Override
    public void search() {
        if (isSales) {
            salesDataReportSearchBean.setType(0);
        } else {
            salesDataReportSearchBean.setType(1);
        }
        balance = BigDecimal.ZERO;
        setSalesDataReportViewBeanList(new ArrayList<>());

        getSalesDataReportSearchBean().setBranchId(getUserData().getUserBranch().getId());

        List<InventoryDelegator> inventoryDelegatorListView = salesDataReportService.getAllInventoryDelegatorSearchBean(getSalesDataReportSearchBean());
        if (inventoryDelegatorListView != null && !inventoryDelegatorListView.isEmpty()) {
            for (InventoryDelegator list : inventoryDelegatorListView) {
                SalesDataReportViewBean bean = new SalesDataReportViewBean();
                bean.setCode(list.getCode());
                bean.setName(list.getName());
                bean.setMobile(list.getMobile());
                bean.setCommission(list.getCommission());
                getSalesDataReportViewBeanList().add(bean);
            }
        }

    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        search();
        if (salesDataReportSearchBean.getShowReport() != null && salesDataReportSearchBean.getShowReport()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "SalesDataReport.jasper", getSalesDataReportViewBeanList(), "pdf");
        } else {
            fillReport(prepareReport(), getUserData().getReportPath() + "PurchaseDataReport.jasper", getSalesDataReportViewBeanList(), "pdf");
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
 if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        hashMap.put("dateText", userDDs.get("DATE"));
        if (isSales) {
            hashMap.put("header2", userDDs.get("BACIS_DA_SALE_DELE"));

        } else {
            hashMap.put("header2", userDDs.get("BACIS_DA_PROC_DELE"));

        }
        hashMap.put("fromRepresentative","من مندوب :");
        hashMap.put("fromRepresentativeValue",salesDataReportSearchBean.getFromName() !=null ? salesDataReportSearchBean.getFromName().getName() : "");
        hashMap.put("toRepresentative", "إلي مندوب:");
        hashMap.put("toRepresentativeValue",salesDataReportSearchBean.getToName()!=null ? salesDataReportSearchBean.getToName().getName() : "");

        hashMap.put("codeText", userDDs.get("CS_COD"));
        hashMap.put("orgSiteText", userDDs.get("NAME"));
        hashMap.put("phoneText", userDDs.get("PHONE"));
        hashMap.put("commissionText", userDDs.get("COMMISSION"));

        return hashMap;
    }

    public List<InventoryDelegator> completDelegator(String query) {
        List<InventoryDelegator> invDelegatorList = getInventoryDelegatorList();
        if (query == null || query.trim().equals("")) {

            setInvDelegatorConvertor(new InvDelegatorConvertor(invDelegatorList));
            return invDelegatorList;
        }
        List<InventoryDelegator> filteredInvs = new ArrayList<>();

        String nameAr;
        String code;
        InventoryDelegator invDelegatorListFilter;

        for (int i = 0; i < getInventoryDelegatorList().size(); i++) {
            invDelegatorListFilter = invDelegatorList.get(i);
            nameAr = invDelegatorListFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invDelegatorListFilter)) {
                    filteredInvs.add(invDelegatorListFilter);
                }
            }

            code = invDelegatorListFilter.getCode().toString();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invDelegatorListFilter)) {
                    filteredInvs.add(invDelegatorListFilter);
                }
            }
        }

        setInvDelegatorConvertor(new InvDelegatorConvertor(filteredInvs));
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
     * @return the isSales
     */
    public Boolean getIsSales() {
        return isSales;
    }

    /**
     * @param isSales the isSales to set
     */
    public void setIsSales(Boolean isSales) {
        this.isSales = isSales;
    }

    /**
     * @return the salesDataReportViewBeanList
     */
    public List<SalesDataReportViewBean> getSalesDataReportViewBeanList() {
        return salesDataReportViewBeanList;
    }

    /**
     * @param salesDataReportViewBeanList the salesDataReportViewBeanList to set
     */
    public void setSalesDataReportViewBeanList(List<SalesDataReportViewBean> salesDataReportViewBeanList) {
        this.salesDataReportViewBeanList = salesDataReportViewBeanList;
    }

    /**
     * @return the salesDataReportSearchBean
     */
    public SalesDataReportSearchBean getSalesDataReportSearchBean() {
        return salesDataReportSearchBean;
    }

    /**
     * @param salesDataReportSearchBean the salesDataReportSearchBean to set
     */
    public void setSalesDataReportSearchBean(SalesDataReportSearchBean salesDataReportSearchBean) {
        this.salesDataReportSearchBean = salesDataReportSearchBean;
    }

    /**
     * @return the inventoryDelegatorList
     */
    public List<InventoryDelegator> getInventoryDelegatorList() {
        return inventoryDelegatorList;
    }

    /**
     * @param inventoryDelegatorList the inventoryDelegatorList to set
     */
    public void setInventoryDelegatorList(List<InventoryDelegator> inventoryDelegatorList) {
        this.inventoryDelegatorList = inventoryDelegatorList;
    }

    /**
     * @return the invDelegatorConvertor
     */
    public InvDelegatorConvertor getInvDelegatorConvertor() {
        return invDelegatorConvertor;
    }

    /**
     * @param invDelegatorConvertor the invDelegatorConvertor to set
     */
    public void setInvDelegatorConvertor(InvDelegatorConvertor invDelegatorConvertor) {
        this.invDelegatorConvertor = invDelegatorConvertor;
    }

    /**
     * @return the salesDataReportService
     */
    public SalesDataReportService getSalesDataReportService() {
        return salesDataReportService;
    }

    /**
     * @param salesDataReportService the salesDataReportService to set
     */
    public void setSalesDataReportService(SalesDataReportService salesDataReportService) {
        this.salesDataReportService = salesDataReportService;
    }

    /**
     * @return the invDelegatorService
     */
    public InvDelegatorService getInvDelegatorService() {
        return invDelegatorService;
    }

    /**
     * @param invDelegatorService the invDelegatorService to set
     */
    public void setInvDelegatorService(InvDelegatorService invDelegatorService) {
        this.invDelegatorService = invDelegatorService;
    }

}
