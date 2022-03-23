/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.InvInventoryService;
import com.isag.businessservice.InvPurchaseInvoiceService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.reports.PurchasesDuringPeriodServiceReport;

import com.isag.businessservice.reports.searchBean.PurchasesDuringPeriodSearchBean;
import com.isag.converter.InvInventoryConverter;

import com.isag.converter.InvOrganizationSiteConverter;
import com.isag.converter.InvPurchaseInvoiceConverter;
import com.isag.entity.InvInventory;

import com.isag.entity.InvOrganizationSite;
import com.isag.entity.InvPurchaseInvoice;

import com.isag.isag.BaseReportBean;
import com.isag.isag.UserData;
import com.isag.report.entity.PurchasesDuringPeriodBean;
import com.isag.views.NetView;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
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
@Named("purchasesDuringPeriodReportMB")
@ViewScoped
public class PurchasesDuringPeriodReportMB extends BaseReportBean {

    // Objects
    private Integer branchId;
    private Boolean type;
    private Boolean isSales;
    BigDecimal balance = BigDecimal.ZERO;
    private String uri;
    private String screenMode;
    private List<PurchasesDuringPeriodBean> purchasesDuringPeriodBeanList;
    private PurchasesDuringPeriodSearchBean purchasesDuringPeriodSearchBean;
    private List<NetView> netViewList;
    private List<InvPurchaseInvoice> invInvoicesPurchaseList;
    private InvPurchaseInvoiceConverter invInvoicePurchaseConverter;
    private List<InvPurchaseInvoice> invInvoicesSalesList;
    private InvPurchaseInvoiceConverter invInvoiceSalesConverter;
    private List<InvOrganizationSite> invOrgSiteList;
    private InvOrganizationSiteConverter invOrgSiteConverter;
    private List<InvInventory> invInventoryList;
    private InvInventoryConverter invInventoryConverter;
    private BigDecimal total;
    private String totalString;
    @EJB
    private PurchasesDuringPeriodServiceReport purchasesDuringPeriodServiceReport;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private InvInventoryService invInventoryService;
    @EJB
    private InvPurchaseInvoiceService invPurchaseInvoiceService;

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
        setPurchasesDuringPeriodSearchBean(new PurchasesDuringPeriodSearchBean());
        getPurchasesDuringPeriodSearchBean().setBranchId(getBranchId());
        getPurchasesDuringPeriodSearchBean().setShowReport(false);
        if (uri.contains("PurchasesDuringPeriod")) {
            purchasesDuringPeriodSearchBean.setType(0);
            isSales = false;
        } else {
            purchasesDuringPeriodSearchBean.setType(1);
            isSales = true;
        }

        setPurchasesDuringPeriodBeanList(new ArrayList<>());

    }

    private void fillLists() {
        if (isSales) {
            setInvOrgSiteList(getOrganizationSiteService().getorganizationSiteByBranchIdForGlBankModule(getUserData().getUserBranch().getId(),true,0)); // عميل
            invInvoicesSalesList = invPurchaseInvoiceService.getALLInvPurchaseInvoicePostFlagedByBranchIdPer(branchId, 1);
           setInvInvoiceSalesConverter(new InvPurchaseInvoiceConverter(invInvoicesSalesList));
        } else {
            setInvOrgSiteList(getOrganizationSiteService().getorganizationSiteByBranchIdForGlBankModule(getUserData().getUserBranch().getId(),true,1)); //مورد
            invInvoicesPurchaseList = invPurchaseInvoiceService.getALLInvPurchaseInvoicePostFlagedByBranchIdPer(branchId, 0);
            invInvoicePurchaseConverter = new InvPurchaseInvoiceConverter(invInvoicesPurchaseList);
        }
        
       
        setInvOrgSiteConverter(new InvOrganizationSiteConverter(getInvOrgSiteList()));
        setInvInventoryList(getInvInventoryService().getALLInventoriesByBranch(getBranchId()));
        setInvInventoryConverter(new InvInventoryConverter(getInvInventoryList()));
    }

    @Override
    public void reset() {
        initObjs();
        fillLists();
        purchasesDuringPeriodSearchBean = new PurchasesDuringPeriodSearchBean();

        netViewList = new ArrayList<>();
    }

    public void showItemReport() {
    }

    @Override
    public void search() {
        if (isSales) {
            purchasesDuringPeriodSearchBean.setType(1);
        } else {
            purchasesDuringPeriodSearchBean.setType(0);
        }
        balance = BigDecimal.ZERO;
        totalString = new String();
        setPurchasesDuringPeriodBeanList(new ArrayList<>());

        getPurchasesDuringPeriodSearchBean().setBranchId(getUserData().getUserBranch().getId());

        netViewList = new ArrayList<>();
        // purchasesDuringPeriodSearchBean.setType(false);
        purchasesDuringPeriodSearchBean.setTopaymentType(purchasesDuringPeriodSearchBean.getTopaymentType() != null ? purchasesDuringPeriodSearchBean.getTopaymentType() - 1 : null);
        purchasesDuringPeriodSearchBean.setFrompaymentType(purchasesDuringPeriodSearchBean.getFrompaymentType() != null ? purchasesDuringPeriodSearchBean.getFrompaymentType() - 1 : null);
        netViewList = purchasesDuringPeriodServiceReport.getAllPurchasesDuringPeriodSearchBean(getPurchasesDuringPeriodSearchBean());
        total = BigDecimal.ZERO;
        if (netViewList != null && !netViewList.isEmpty()) {
            for (NetView view : netViewList) {
                PurchasesDuringPeriodBean bean = new PurchasesDuringPeriodBean();
                bean.setSerial(view.getSerial());
                bean.setDate(view.getDate());

                bean.setPaymentTypeText(view.getPaymentTypeText());
                bean.setOrganizationName(view.getOrganizationSiteName());
                bean.setNet(view.getNet());
                getPurchasesDuringPeriodBeanList().add(bean);
                total = total.add(view.getNet());
            }
            DecimalFormat df = new DecimalFormat("#,##0.00");
            totalString = df.format(total);
        }

    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
//        search();
        if (purchasesDuringPeriodSearchBean.getShowReport() != null && purchasesDuringPeriodSearchBean.getShowReport()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "PurchasesDuringPeriod.jasper", getPurchasesDuringPeriodBeanList(), "pdf");
        } else {
            fillReport(prepareReport(), getUserData().getReportPath() + "PurchasesDuringPeriod.jasper", getPurchasesDuringPeriodBeanList(), "pdf");
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
        // hashMap.put("CompanyName", getUserData().getCompany().getName());

        //      hashMap.put("CompanyLogo", getUserData().getImageReportPath());
        hashMap.put("dateText", userDDs.get("DATE"));

        hashMap.put("dateFromText", userDDs.get("YEAR_FROM"));

        hashMap.put("dateToText", userDDs.get("YEAR_TO"));
        hashMap.put("dateFromValue", purchasesDuringPeriodSearchBean.getDateFrom());
        hashMap.put("dateToValue", purchasesDuringPeriodSearchBean.getDateTo());
        if (isSales) {
            hashMap.put("header2", userDDs.get("SALE_DUR_PER"));
            hashMap.put("orgSiteText", userDDs.get("CUSTOMER"));
        } else {
            hashMap.put("header2", userDDs.get("PURCHASES_DUR_PER"));
            hashMap.put("orgSiteText", userDDs.get("SUPPLIER"));
        }
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }

        hashMap.put("serialText", userDDs.get("INVOICE_NO"));

        hashMap.put("dateText", userDDs.get("INVOICE_DATE"));

        hashMap.put("paymentTypeText", userDDs.get("TYPE"));

        hashMap.put("netText", userDDs.get("TOTAL"));
        hashMap.put("sumText", "الإجمـــــالى");
        hashMap.put("sumValue", totalString);
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

    public List<InvInventory> completInventory(String query) {
        List<InvInventory> invInventoryList = getInvInventoryList();
        if (query == null || query.trim().equals("")) {

            setInvInventoryConverter(new InvInventoryConverter(invInventoryList));
            return invInventoryList;
        }
        List<InvInventory> filteredInvs = new ArrayList<>();

        String nameAr;
        String code;
        InvInventory invInventoryFilter;

        for (int i = 0; i < getInvOrgSiteList().size(); i++) {
            invInventoryFilter = invInventoryList.get(i);
            nameAr = invInventoryFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invInventoryFilter)) {
                    filteredInvs.add(invInventoryFilter);
                }
            }

            code = invInventoryFilter.getCode().toString();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invInventoryFilter)) {
                    filteredInvs.add(invInventoryFilter);
                }
            }
        }

        setInvInventoryConverter(new InvInventoryConverter(filteredInvs));
        return filteredInvs;
    }

    public List<InvPurchaseInvoice> completePurchaseInvoice(String query) {
        List<InvPurchaseInvoice> invList = invInvoicesPurchaseList;
        if (query == null || query.trim().equals("")) {

            setInvInvoicePurchaseConverter(new InvPurchaseInvoiceConverter(invList));
            return invList;
        }
        List<InvPurchaseInvoice> filteredInvs = new ArrayList<>();

        Integer code;
        InvPurchaseInvoice invFilter;
        for (int i = 0; i < invInvoicesPurchaseList.size(); i++) {
            invFilter = invList.get(i);

            code = invFilter.getSerial();
            if (code != null && String.valueOf(code).toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invFilter)) {
                    filteredInvs.add(invFilter);
                }
            }
        }

        setInvInvoicePurchaseConverter(new InvPurchaseInvoiceConverter(filteredInvs));
        return filteredInvs;
    }
     public List<InvPurchaseInvoice> completeSalesInvoice(String query) {
        List<InvPurchaseInvoice> invList = invInvoicesSalesList;
        if (query == null || query.trim().equals("")) {

            invInvoiceSalesConverter = new InvPurchaseInvoiceConverter(invList);
            return invList;
        }
        List<InvPurchaseInvoice> filteredInvs = new ArrayList<>();

        Integer code;
        InvPurchaseInvoice invFilter;
        for (int i = 0; i < invInvoicesSalesList.size(); i++) {
            invFilter = invList.get(i);

            code = invFilter.getSerial();
            if (code != null && String.valueOf(code).toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invFilter)) {
                    filteredInvs.add(invFilter);
                }
            }
        }

        invInvoiceSalesConverter = new InvPurchaseInvoiceConverter(filteredInvs);
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
     * @return the invInventoryList
     */
    public List<InvInventory> getInvInventoryList() {
        return invInventoryList;
    }

    /**
     * @param invInventoryList the invInventoryList to set
     */
    public void setInvInventoryList(List<InvInventory> invInventoryList) {
        this.invInventoryList = invInventoryList;
    }

    /**
     * @return the invInventoryConverter
     */
    public InvInventoryConverter getInvInventoryConverter() {
        return invInventoryConverter;
    }

    /**
     * @param invInventoryConverter the invInventoryConverter to set
     */
    public void setInvInventoryConverter(InvInventoryConverter invInventoryConverter) {
        this.invInventoryConverter = invInventoryConverter;
    }

    /**
     * @return the purchasesDuringPeriodBeanList
     */
    public List<PurchasesDuringPeriodBean> getPurchasesDuringPeriodBeanList() {
        return purchasesDuringPeriodBeanList;
    }

    /**
     * @param purchasesDuringPeriodBeanList the purchasesDuringPeriodBeanList to
     * set
     */
    public void setPurchasesDuringPeriodBeanList(List<PurchasesDuringPeriodBean> purchasesDuringPeriodBeanList) {
        this.purchasesDuringPeriodBeanList = purchasesDuringPeriodBeanList;
    }

    /**
     * @return the purchasesDuringPeriodSearchBean
     */
    public PurchasesDuringPeriodSearchBean getPurchasesDuringPeriodSearchBean() {
        return purchasesDuringPeriodSearchBean;
    }

    /**
     * @param purchasesDuringPeriodSearchBean the
     * purchasesDuringPeriodSearchBean to set
     */
    public void setPurchasesDuringPeriodSearchBean(PurchasesDuringPeriodSearchBean purchasesDuringPeriodSearchBean) {
        this.purchasesDuringPeriodSearchBean = purchasesDuringPeriodSearchBean;
    }

    /**
     * @return the netViewList
     */
    public List<NetView> getNetViewList() {
        return netViewList;
    }

    /**
     * @param netViewList the netViewList to set
     */
    public void setNetViewList(List<NetView> netViewList) {
        this.netViewList = netViewList;
    }

    /**
     * @return the purchasesDuringPeriodServiceReport
     */
    public PurchasesDuringPeriodServiceReport getPurchasesDuringPeriodServiceReport() {
        return purchasesDuringPeriodServiceReport;
    }

    /**
     * @param purchasesDuringPeriodServiceReport the
     * purchasesDuringPeriodServiceReport to set
     */
    public void setPurchasesDuringPeriodServiceReport(PurchasesDuringPeriodServiceReport purchasesDuringPeriodServiceReport) {
        this.purchasesDuringPeriodServiceReport = purchasesDuringPeriodServiceReport;
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
     * @return the invInventoryService
     */
    public InvInventoryService getInvInventoryService() {
        return invInventoryService;
    }

    /**
     * @param invInventoryService the invInventoryService to set
     */
    public void setInvInventoryService(InvInventoryService invInventoryService) {
        this.invInventoryService = invInventoryService;
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
     * @return the type
     */
    public Boolean getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Boolean type) {
        this.type = type;
    }

    /**
     * @return the total
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * @return the totalString
     */
    public String getTotalString() {
        return totalString;
    }

    /**
     * @param totalString the totalString to set
     */
    public void setTotalString(String totalString) {
        this.totalString = totalString;
    }

    /**
     * @return the invInvoicePurchaseConverter
     */
    public InvPurchaseInvoiceConverter getInvInvoicePurchaseConverter() {
        return invInvoicePurchaseConverter;
    }

    /**
     * @param invInvoicePurchaseConverter the invInvoicePurchaseConverter to set
     */
    public void setInvInvoicePurchaseConverter(InvPurchaseInvoiceConverter invInvoicePurchaseConverter) {
        this.invInvoicePurchaseConverter = invInvoicePurchaseConverter;
    }

    /**
     * @return the invInvoiceSalesConverter
     */
    public InvPurchaseInvoiceConverter getInvInvoiceSalesConverter() {
        return invInvoiceSalesConverter;
    }

    /**
     * @param invInvoiceSalesConverter the invInvoiceSalesConverter to set
     */
    public void setInvInvoiceSalesConverter(InvPurchaseInvoiceConverter invInvoiceSalesConverter) {
        this.invInvoiceSalesConverter = invInvoiceSalesConverter;
    }

}
