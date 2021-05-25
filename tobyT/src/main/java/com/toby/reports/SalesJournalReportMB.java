/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.reports;

import com.toby.businessservice.InvDelegatorService;
import com.toby.businessservice.InvInventoryService;
import com.toby.businessservice.InvPurchaseInvoiceService;

import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.report.SalesJournalService;


import com.toby.businessservice.reports.searchBean.SalesJournalSearchBean;

import com.toby.converter.InvDelegatorConvertor;
import com.toby.converter.InvInventoryConverter;
import com.toby.converter.InvOrganizationSiteConverter;
import com.toby.converter.InvPurchaseInvoiceConverter;
import com.toby.entity.InvInventory;

import com.toby.entity.InvOrganizationSite;
import com.toby.entity.InvPurchaseInvoice;

import com.toby.entity.InventoryDelegator;

import com.toby.toby.BaseReportBean;
import com.toby.toby.UserData;

import com.toby.report.entity.SalesJournalReportBean;
import com.toby.views.NetView;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
@Named("salesJournalReportMB")
@ViewScoped
public class SalesJournalReportMB extends BaseReportBean {

    // Objects
    private Integer branchId;
    private Date selectedDate;
    private Boolean type;
    private BigDecimal totalnet;
    private BigDecimal totalTotalSumAvarage;
    private BigDecimal totalDiscount;
    private BigDecimal allNet;
    private BigDecimal allDiscount;
    private Integer discountTypeList;
    private BigDecimal discountValue1;
    private Boolean taxFlag1;
    private BigDecimal taxValueList = new BigDecimal(0.14);
    private BigDecimal net;
    private BigDecimal discountType1;
    private BigDecimal hundred = new BigDecimal(100);

    private String uri;
    private Boolean isSales;
    private String screenMode;
    private List<SalesJournalReportBean> salesJournalReportBeanList;
    private SalesJournalReportBean salesJournalReportBean;
    private SalesJournalSearchBean salesJournalSearchBean;

    private List<NetView> netViewList;
    private List<InvPurchaseInvoice> invInvoicesPurchaseList;
    private InvPurchaseInvoiceConverter invInvoicePurchaseConverter;
    private List<InvPurchaseInvoice> invInvoicesSalesList;
    private InvPurchaseInvoiceConverter invInvoiceSalesConverter;
    private List<InvOrganizationSite> invCustomerList;
    private InvOrganizationSiteConverter invCustomerConverter;
    private List<InventoryDelegator> invDelegatorList;
    private InvDelegatorConvertor invDelegatorConverter;
    private List<InvInventory> invInventoryList;
    private InvInventoryConverter invInventoryConverter;
    //---------------------
    private Map<Date, List<NetView>> netViewDetailMap = new HashMap<>();
    private List<NetView> dateRootList;
    private Map<Date, SalesJournalReportBean> salesJournalReportBeanMap = new HashMap<>();
    private TreeMap<Integer, SalesJournalReportBean> salesJournalReportBeanTreeMap = new TreeMap<>();

    private Integer salesMapReference;

    private Map<Date, NetView> invPurchaseInvoiceBeanMap = new HashMap<>();

    //----------------------  
    @EJB
    private SalesJournalService salesJournalService;
    @EJB
    private InvInventoryService inventoryService;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private InvDelegatorService delegatorService;
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
//        setDateRootList(salesJournalService.getAllDates(getSalesJournalSearchBean()));

        if (uri != null && uri.contains("SalesJournal")) {
//            setInvCustomerList(organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(getUserData().getUserBranch().getId(), true, 0));

            invDelegatorList = delegatorService.getSalesByBranch(getBranchId()); // مبيعات
        } else {
            setInvCustomerList(organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(getUserData().getUserBranch().getId(), true, 1));
            invDelegatorList = delegatorService.getPurchaseByBranch(getBranchId()); //مشتريات
        }
        invInventoryList = inventoryService.getALLInventoriesByBranchPer(branchId);
    }
    
     public void convertDateFrom() {
        getSalesJournalSearchBean().setDateFrom(findDateFromHijriFrom());
    }

    public void convertDateTo() {
        getSalesJournalSearchBean().setDateTo(findDateFromHijriTo());
    }

    private void initObjs() {

        setInvCustomerList(new ArrayList<>());
        setInvDelegatorList(new ArrayList<>());
        invInventoryList = new ArrayList<>();
        setDateRootList(new ArrayList<>());
        setSalesJournalReportBeanList(new ArrayList<>());
        setSalesJournalSearchBean(new SalesJournalSearchBean());

        netViewDetailMap = new HashMap<>();
        setSalesJournalReportBeanMap(new HashMap<>());

        setSalesJournalReportBeanTreeMap(new TreeMap<>());
        setInvPurchaseInvoiceBeanMap(new HashMap<>());

        if (uri != null && uri.contains("SalesJournal")) {
            getSalesJournalSearchBean().setType(1);
            isSales = true;
        } else {
            getSalesJournalSearchBean().setType(0);
            isSales = false;
        }

        getSalesJournalSearchBean().setBranchId(getBranchId());
        getSalesJournalSearchBean().setShowReport(false);
    }

    private void fillLists() {
        if (uri != null && uri.contains("SalesJournal")) {
//            setInvCustomerList(organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(getUserData().getUserBranch().getId(), true, 0));
            invDelegatorList = delegatorService.getSalesByBranch(getBranchId()); // مبيعات
            invInvoicesSalesList = invPurchaseInvoiceService.getALLInvPurchaseInvoicePostFlagedByBranchIdPer(branchId, 4);
            setInvInvoiceSalesConverter(new InvPurchaseInvoiceConverter(invInvoicesSalesList));

        } else {
            setInvCustomerList(organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(getUserData().getUserBranch().getId(), true, 1));
            invDelegatorList = delegatorService.getPurchaseByBranch(getBranchId()); //مشتريات
            invInvoicesPurchaseList = invPurchaseInvoiceService.getALLInvPurchaseInvoicePostFlagedByBranchIdPer(branchId, 0);
            invInvoicePurchaseConverter = new InvPurchaseInvoiceConverter(invInvoicesPurchaseList);
        }
        invInventoryList = inventoryService.getALLInventoriesByBranchPer(branchId);
//        netViewList = salesJournalService.getAllPurchaseInvoiceSearchBean(salesJournalSearchBean);

        setInvCustomerConverter(new InvOrganizationSiteConverter(getInvCustomerList()));// مبيعات

        setInvDelegatorConverter(new InvDelegatorConvertor(getInvDelegatorList()));
        invInventoryConverter = new InvInventoryConverter(invInventoryList);
        //  setNetViewList(new ArrayList<>());
    }

    @Override
    public void reset() {
        initObjs();
        fillLists();
        setSalesJournalSearchBean(new SalesJournalSearchBean());

        netViewList = new ArrayList<>();

    }

    public void showItemReport() {

    }

    @Override
    public void search() {

        getSalesJournalSearchBean().setBranchId(getUserData().getUserBranch().getId());
        setNetViewList(new ArrayList<>());
        if (uri != null && uri.contains("SalesJournalForUser")) {
            getSalesJournalSearchBean().setType(4);
            getSalesJournalSearchBean().setTobyUserId(getUserData().getUser().getId());
            getSalesJournalSearchBean().setFrompaymentType(1);
            getSalesJournalSearchBean().setTopaymentType(1);
        }
        
        netViewList = salesJournalService.getAllPurchaseInvoiceSearchBean(salesJournalSearchBean);
        netViewDetailMap = new HashMap<>();
        allDiscount = BigDecimal.ZERO;
        allNet = BigDecimal.ZERO;
        setSalesJournalReportBeanMap(new HashMap<>());
        setSalesJournalReportBeanTreeMap(new TreeMap<>());
        setSalesMapReference((Integer) 0);
        //salesJournalReportBeanList = new ArrayList<>();
        setInvPurchaseInvoiceBeanMap(new HashMap<>());

        setSalesJournalReportBeanList(new ArrayList<>());
        if (uri != null && uri.contains("SalesJournal")) {
            getSalesJournalSearchBean().setType(1);
        } else if (uri != null && uri.contains("SalesJournalForUser")) {
            getSalesJournalSearchBean().setType(1);
            getSalesJournalSearchBean().setTobyUserId(getUserData().getUser().getId());
        } else {
            getSalesJournalSearchBean().setType(0);
        }
        fillEachGroupWithItsCorrespondingList();
        fillItemIdMapByGroupId();

    }

    private void fillItemIdMapByGroupId() {
        setNetViewList(new ArrayList<>());
        TreeMap<Date, List<NetView>> treeMap = new TreeMap<Date, List<NetView>>();
        treeMap.putAll(netViewDetailMap);

        for (Map.Entry<Date, List<NetView>> entry : treeMap.entrySet()) {
            setInvPurchaseInvoiceBeanMap(new HashMap<>());
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                SalesJournalReportBean reportBean = new SalesJournalReportBean();
                reportBean.setDate(entry.getKey());
                totalDiscount = BigDecimal.ZERO;

                totalnet = BigDecimal.ZERO;
                totalTotalSumAvarage = BigDecimal.ZERO;
                getSalesJournalReportBeanList().add(reportBean);
                for (NetView orgView : entry.getValue()) {

                    SalesJournalReportBean sjrb = new SalesJournalReportBean();

                    sjrb.setDelegatorName(orgView.getInvDelegatorName());
                    sjrb.setSupplierName(orgView.getOrganizationSiteName());
                    sjrb.setDiscount(orgView.getHeadDiscount());
                    sjrb.setInvInventoryName(orgView.getInventoryName());
                    sjrb.setNet(orgView.getNet());
                    sjrb.setSumTotalCost(orgView.getSumTotalCost());
                    sjrb.setSerialNum(orgView.getSerial());
                    sjrb.setSupplierCode(orgView.getOrganizationSiteCode());

                    //------------------------
                    if (getSalesJournalSearchBean().getIsReturn()) {
                        if (orgView.getType() == 2 || orgView.getType() == 3) {
                            totalDiscount = totalDiscount.subtract(orgView.getHeadDiscount());
                            totalTotalSumAvarage = totalTotalSumAvarage.subtract(orgView.getSumTotalCost());
                            totalnet = totalnet.subtract(orgView.getNet());
                        } else {
                            totalDiscount = totalDiscount.add(orgView.getHeadDiscount());
                            totalTotalSumAvarage = totalTotalSumAvarage.add(orgView.getSumTotalCost());
                            totalnet = totalnet.add(orgView.getNet());
                        }
                    } else {
                        totalDiscount = totalDiscount.add(orgView.getHeadDiscount());
                        totalTotalSumAvarage = totalTotalSumAvarage.add(orgView.getSumTotalCost());
                        totalnet = totalnet.add(orgView.getNet());
                    }
                    //-------------------------
                    if (!salesJournalSearchBean.getShowReport()) {
                        getSalesJournalReportBeanList().add(sjrb);
                    }
                }
                reportBean.setDiscount(totalDiscount);
                reportBean.setNet(totalnet);
                reportBean.setSumTotalCost(totalTotalSumAvarage);
                allDiscount = allDiscount.add(totalDiscount);
                allNet = allNet.add(totalnet);

            }
        }

    }

    private void fillEachGroupWithItsCorrespondingList() {
        netViewDetailMap = new HashMap<>();
        for (NetView item : getNetViewList()) {
            if (!netViewDetailMap.containsKey(item.getDate())) {
                List<NetView> itemSalesViews = new ArrayList<>();
                itemSalesViews.add(item);
                netViewDetailMap.put((item.getDate()), itemSalesViews);
            } else {
                List<NetView> itemSalesViews = netViewDetailMap.get(item.getDate());
                itemSalesViews.add(item);
                netViewDetailMap.put(item.getDate(), itemSalesViews);
            }
        }
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
//        search();
        if (salesJournalReportBeanList != null && !salesJournalReportBeanList.isEmpty()) {
            if (salesJournalSearchBean.getShowReport().equals(false)) {
                fillReport(prepareReport(), getUserData().getReportPath() + "journalByDate.jasper", getSalesJournalReportBeanList(), "pdf");
            } else {
                fillReport(prepareReport(), getUserData().getReportPath() + "totalJournal.jasper", getSalesJournalReportBeanList(), "pdf");
            }
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

        //  hashMap.put("CompanyLogo", getUserData().getImageReportPath());
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        if (isSales) {
            hashMap.put("header2", userDDs.get("SALES_JOURNAL"));
        } else {
            hashMap.put("header2", userDDs.get("PROCUR_JOURNAL"));
        }
        hashMap.put("dateText", userDDs.get("DATE"));
//        if (salesJournalSearchBean.getShowReport().equals(false)) {
        hashMap.put("serial", userDDs.get("INVOICE_NO"));
        hashMap.put("inventoryName", userDDs.get("DEPOSITORY"));
        if (isSales) {
            hashMap.put("supplierName", userDDs.get("CUSTOMER"));
        } else {
            hashMap.put("supplierName", userDDs.get("SUPPLIERR"));
        }
        hashMap.put("delegatorN", userDDs.get("DELEGATOR"));
        hashMap.put("invTypeFromText", userDDs.get("FROM_INVOI_TYP"));
        hashMap.put("invTypeToText", userDDs.get("TO_INVOI_TYP"));
        hashMap.put("dateFromText", userDDs.get("FROM_DATE"));
        hashMap.put("dateTotText", userDDs.get("TO_DATE"));
        hashMap.put("inventoryFromText", userDDs.get("FROM_DEPOSITORY"));
        hashMap.put("inventoryToText", userDDs.get("TO_DEPOSITORY"));
        hashMap.put("invoiceNumberFromText", userDDs.get("FROM_INVOI_NO"));
        hashMap.put("invoiceNumberToText", userDDs.get("TO_INVOI_NO"));
        hashMap.put("supplierCode", "الكود");
        if (salesJournalSearchBean.getFrompaymentType() != null && salesJournalSearchBean.getFrompaymentType() == 0) {
            hashMap.put("invTypeFromValue", userDDs.get("CASH"));
        } else if (salesJournalSearchBean.getFrompaymentType() != null && salesJournalSearchBean.getFrompaymentType() == 1) {
            hashMap.put("invTypeFromValue", userDDs.get("POSTPONE"));
        } else if (salesJournalSearchBean.getFrompaymentType() != null && salesJournalSearchBean.getFrompaymentType() == 2) {
            hashMap.put("invTypeFromValue", userDDs.get("DECOM_CREDIT"));
        }
        if (salesJournalSearchBean.getTopaymentType() != null && salesJournalSearchBean.getTopaymentType() == 0) {
            hashMap.put("invTypeToValue", userDDs.get("CASH"));
        } else if (salesJournalSearchBean.getTopaymentType() != null && salesJournalSearchBean.getTopaymentType() == 1) {
            hashMap.put("invTypeToValue", userDDs.get("POSTPONE"));
        } else if (salesJournalSearchBean.getTopaymentType() != null && salesJournalSearchBean.getTopaymentType() == 2) {
            hashMap.put("invTypeToValue", userDDs.get("DECOM_CREDIT"));
        }
        hashMap.put("dateFromValue", salesJournalSearchBean.getDateFrom());
        hashMap.put("dateTotValue", salesJournalSearchBean.getDateTo());
        hashMap.put("inventoryFromValue", salesJournalSearchBean.getFromInventoryName() == null ? "" : salesJournalSearchBean.getFromInventoryName().getName());
        hashMap.put("inventoryToValue", salesJournalSearchBean.getToInventoryName() == null ? "" : salesJournalSearchBean.getToInventoryName().getName());
        hashMap.put("invoiceNumberFromValue", salesJournalSearchBean.getFromserial());
        hashMap.put("invoiceNumberToValue", salesJournalSearchBean.getToserial());
        hashMap.put("supplierFromText", userDDs.get("FROM_SUPPLIER"));
        hashMap.put("supplierToText", userDDs.get("TO_SUPPLIER"));
        hashMap.put("supplierFromValue", salesJournalSearchBean.getFromCustomer() == null ? "" : salesJournalSearchBean.getFromCustomer().getName());
        hashMap.put("supplierToValue", salesJournalSearchBean.getToCustomer() == null ? "" : salesJournalSearchBean.getToCustomer().getName());
        hashMap.put("delegatorFromText", userDDs.get("FROM_DELE"));
        hashMap.put("delegatorToText", userDDs.get("TO_DELE"));
        hashMap.put("delegatorFromValue", salesJournalSearchBean.getFromDelegator() == null ? "" : salesJournalSearchBean.getFromDelegator().getName());
        hashMap.put("delegatorToValue", salesJournalSearchBean.getToDelegator() == null ? "" : salesJournalSearchBean.getToDelegator().getName());
//        }
        hashMap.put("discount", userDDs.get("DISCOUNT"));
        hashMap.put("netText", userDDs.get("NET"));
        hashMap.put("total", userDDs.get("TOTAL"));
        hashMap.put("totaldiscount", allDiscount);
        hashMap.put("totalnet", allNet);

        return hashMap;
    }

    public List<InvOrganizationSite> completCustomer(String query) {
        List<InvOrganizationSite> invCustomerList = getInvCustomerList();
        if (query == null || query.trim().equals("")) {

            setInvCustomerConverter(new InvOrganizationSiteConverter(invCustomerList));
            return invCustomerList;
        }
        List<InvOrganizationSite> filteredInvs = new ArrayList<>();

        String nameAr;
        String code;
        InvOrganizationSite InvCustomerFilter;

        for (int i = 0; i < getInvCustomerList().size(); i++) {
            InvCustomerFilter = invCustomerList.get(i);
            nameAr = InvCustomerFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(InvCustomerFilter)) {
                    filteredInvs.add(InvCustomerFilter);
                }
            }

            code = InvCustomerFilter.getCode().toString();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(InvCustomerFilter)) {
                    filteredInvs.add(InvCustomerFilter);
                }
            }
        }

        setInvCustomerConverter(new InvOrganizationSiteConverter(filteredInvs));
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

            setInvInvoiceSalesConverter(new InvPurchaseInvoiceConverter(invList));
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

        setInvInvoiceSalesConverter(new InvPurchaseInvoiceConverter(filteredInvs));
        return filteredInvs;
    }

    public List<InventoryDelegator> completDelegator(String query) {
        List<InventoryDelegator> invDelList = getInvDelegatorList();
        if (query == null || query.trim().equals("")) {

            setInvDelegatorConverter(new InvDelegatorConvertor(invDelList));
            return invDelList;
        }
        List<InventoryDelegator> filteredInvs = new ArrayList<>();

        String nameAr;
        String code;
        InventoryDelegator InvDelFilter;

        for (int i = 0; i < getInvDelegatorList().size(); i++) {
            InvDelFilter = getInvDelegatorList().get(i);
            nameAr = InvDelFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(InvDelFilter)) {
                    filteredInvs.add(InvDelFilter);
                }
            }

            code = InvDelFilter.getCode().toString();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(InvDelFilter)) {
                    filteredInvs.add(InvDelFilter);
                }
            }
        }

        setInvDelegatorConverter(new InvDelegatorConvertor(filteredInvs));
        return filteredInvs;
    }

    public List<InvInventory> completeInventory(String query) {
        List<InvInventory> invList = invInventoryList;
        if (query == null || query.trim().equals("")) {

            setInvInventoryConverter(new InvInventoryConverter(invList));
            return invList;
        }
        List<InvInventory> filteredInvs = new ArrayList<>();

        String nameAr;
        String code;
        InvInventory invFilter;
        for (int i = 0; i < invInventoryList.size(); i++) {
            invFilter = invList.get(i);
            nameAr = invFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invFilter)) {
                    filteredInvs.add(invFilter);
                }
            }

            code = invFilter.getCode();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invFilter)) {
                    filteredInvs.add(invFilter);
                }
            }
        }

        setInvInventoryConverter(new InvInventoryConverter(filteredInvs));
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
     * @return the selectedDate
     */
    public Date getSelectedDate() {
        return selectedDate;
    }

    /**
     * @param selectedDate the selectedDate to set
     */
    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    /**
     * @return the discountTypeList
     */
    public Integer getDiscountTypeList() {
        return discountTypeList;
    }

    /**
     * @param discountTypeList the discountTypeList to set
     */
    public void setDiscountTypeList(Integer discountTypeList) {
        this.discountTypeList = discountTypeList;
    }

    /**
     * @return the discountValue1
     */
    public BigDecimal getDiscountValue1() {
        return discountValue1;
    }

    /**
     * @param discountValue1 the discountValue1 to set
     */
    public void setDiscountValue1(BigDecimal discountValue1) {
        this.discountValue1 = discountValue1;
    }

    /**
     * @return the taxFlag1
     */
    public Boolean getTaxFlag1() {
        return taxFlag1;
    }

    /**
     * @param taxFlag1 the taxFlag1 to set
     */
    public void setTaxFlag1(Boolean taxFlag1) {
        this.taxFlag1 = taxFlag1;
    }

    /**
     * @return the taxValueList
     */
    public BigDecimal getTaxValueList() {
        return taxValueList;
    }

    /**
     * @param taxValueList the taxValueList to set
     */
    public void setTaxValueList(BigDecimal taxValueList) {
        this.taxValueList = taxValueList;
    }

    /**
     * @return the net
     */
    public BigDecimal getNet() {
        return net;
    }

    /**
     * @param net the net to set
     */
    public void setNet(BigDecimal net) {
        this.net = net;
    }

    /**
     * @return the discountType1
     */
    public BigDecimal getDiscountType1() {
        return discountType1;
    }

    /**
     * @param discountType1 the discountType1 to set
     */
    public void setDiscountType1(BigDecimal discountType1) {
        this.discountType1 = discountType1;
    }

    /**
     * @return the hundred
     */
    public BigDecimal getHundred() {
        return hundred;
    }

    /**
     * @param hundred the hundred to set
     */
    public void setHundred(BigDecimal hundred) {
        this.hundred = hundred;
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
     * @return the screenMode
     */
    public String getScreenMode() {
        return screenMode;
    }

    /**
     * @param screenMode the screenMode to set
     */
    public void setScreenMode(String screenMode) {
        this.screenMode = screenMode;
    }

    /**
     * @return the salesJournalReportBeanList
     */
    public List<SalesJournalReportBean> getSalesJournalReportBeanList() {
        return salesJournalReportBeanList;
    }

    /**
     * @param salesJournalReportBeanList the salesJournalReportBeanList to set
     */
    public void setSalesJournalReportBeanList(List<SalesJournalReportBean> salesJournalReportBeanList) {
        this.salesJournalReportBeanList = salesJournalReportBeanList;
    }

    /**
     * @return the salesJournalReportBean
     */
    public SalesJournalReportBean getSalesJournalReportBean() {
        return salesJournalReportBean;
    }

    /**
     * @param salesJournalReportBean the salesJournalReportBean to set
     */
    public void setSalesJournalReportBean(SalesJournalReportBean salesJournalReportBean) {
        this.salesJournalReportBean = salesJournalReportBean;
    }

    /**
     * @return the salesJournalSearchBean
     */
    public SalesJournalSearchBean getSalesJournalSearchBean() {
        return salesJournalSearchBean;
    }

    /**
     * @param salesJournalSearchBean the salesJournalSearchBean to set
     */
    public void setSalesJournalSearchBean(SalesJournalSearchBean salesJournalSearchBean) {
        this.salesJournalSearchBean = salesJournalSearchBean;
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
     * @return the invCustomerList
     */
    public List<InvOrganizationSite> getInvCustomerList() {
        return invCustomerList;
    }

    /**
     * @param invCustomerList the invCustomerList to set
     */
    public void setInvCustomerList(List<InvOrganizationSite> invCustomerList) {
        this.invCustomerList = invCustomerList;
    }

    /**
     * @return the invCustomerConverter
     */
    public InvOrganizationSiteConverter getInvCustomerConverter() {
        return invCustomerConverter;
    }

    /**
     * @param invCustomerConverter the invCustomerConverter to set
     */
    public void setInvCustomerConverter(InvOrganizationSiteConverter invCustomerConverter) {
        this.invCustomerConverter = invCustomerConverter;
    }

    /**
     * @return the netViewDetailMap
     */
    public Map<Date, List<NetView>> getNetViewDetailMap() {
        return netViewDetailMap;
    }

    /**
     * @param netViewDetailMap the netViewDetailMap to set
     */
    public void setNetViewDetailMap(Map<Date, List<NetView>> netViewDetailMap) {
        this.netViewDetailMap = netViewDetailMap;
    }

    /**
     * @return the dateRootList
     */
    public List<NetView> getDateRootList() {
        return dateRootList;
    }

    /**
     * @param dateRootList the dateRootList to set
     */
    public void setDateRootList(List<NetView> dateRootList) {
        this.dateRootList = dateRootList;
    }

    /**
     * @return the salesJournalReportBeanMap
     */
    public Map<Date, SalesJournalReportBean> getSalesJournalReportBeanMap() {
        return salesJournalReportBeanMap;
    }

    /**
     * @param salesJournalReportBeanMap the salesJournalReportBeanMap to set
     */
    public void setSalesJournalReportBeanMap(Map<Date, SalesJournalReportBean> salesJournalReportBeanMap) {
        this.salesJournalReportBeanMap = salesJournalReportBeanMap;
    }

    /**
     * @return the salesJournalReportBeanTreeMap
     */
    public TreeMap<Integer, SalesJournalReportBean> getSalesJournalReportBeanTreeMap() {
        return salesJournalReportBeanTreeMap;
    }

    /**
     * @param salesJournalReportBeanTreeMap the salesJournalReportBeanTreeMap to
     * set
     */
    public void setSalesJournalReportBeanTreeMap(TreeMap<Integer, SalesJournalReportBean> salesJournalReportBeanTreeMap) {
        this.salesJournalReportBeanTreeMap = salesJournalReportBeanTreeMap;
    }

    /**
     * @return the salesMapReference
     */
    public Integer getSalesMapReference() {
        return salesMapReference;
    }

    /**
     * @param salesMapReference the salesMapReference to set
     */
    public void setSalesMapReference(Integer salesMapReference) {
        this.salesMapReference = salesMapReference;
    }

    /**
     * @return the invPurchaseInvoiceBeanMap
     */
    public Map<Date, NetView> getInvPurchaseInvoiceBeanMap() {
        return invPurchaseInvoiceBeanMap;
    }

    /**
     * @param invPurchaseInvoiceBeanMap the invPurchaseInvoiceBeanMap to set
     */
    public void setInvPurchaseInvoiceBeanMap(Map<Date, NetView> invPurchaseInvoiceBeanMap) {
        this.invPurchaseInvoiceBeanMap = invPurchaseInvoiceBeanMap;
    }

    /**
     * @return the invDelegatorList
     */
    public List<InventoryDelegator> getInvDelegatorList() {
        return invDelegatorList;
    }

    /**
     * @param invDelegatorList the invDelegatorList to set
     */
    public void setInvDelegatorList(List<InventoryDelegator> invDelegatorList) {
        this.invDelegatorList = invDelegatorList;
    }

    /**
     * @return the invDelegatorConverter
     */
    public InvDelegatorConvertor getInvDelegatorConverter() {
        return invDelegatorConverter;
    }

    /**
     * @param invDelegatorConverter the invDelegatorConverter to set
     */
    public void setInvDelegatorConverter(InvDelegatorConvertor invDelegatorConverter) {
        this.invDelegatorConverter = invDelegatorConverter;
    }

    /**
     * @return the delegatorService
     */
    public InvDelegatorService getDelegatorService() {
        return delegatorService;
    }

    /**
     * @param delegatorService the delegatorService to set
     */
    public void setDelegatorService(InvDelegatorService delegatorService) {
        this.delegatorService = delegatorService;
    }

    /**
     * @return the totalnet
     */
    public BigDecimal getTotalnet() {
        return totalnet;
    }

    /**
     * @param totalnet the totalnet to set
     */
    public void setTotalnet(BigDecimal totalnet) {
        this.totalnet = totalnet;
    }

    /**
     * @return the totalDiscount
     */
    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    /**
     * @param totalDiscount the totalDiscount to set
     */
    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
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
     * @return the allNet
     */
    public BigDecimal getAllNet() {
        return allNet;
    }

    /**
     * @param allNet the allNet to set
     */
    public void setAllNet(BigDecimal allNet) {
        this.allNet = allNet;
    }

    /**
     * @return the allDiscount
     */
    public BigDecimal getAllDiscount() {
        return allDiscount;
    }

    /**
     * @param allDiscount the allDiscount to set
     */
    public void setAllDiscount(BigDecimal allDiscount) {
        this.allDiscount = allDiscount;
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
     * @return the inventoryService
     */
    public InvInventoryService getInventoryService() {
        return inventoryService;
    }

    /**
     * @param inventoryService the inventoryService to set
     */
    public void setInventoryService(InvInventoryService inventoryService) {
        this.inventoryService = inventoryService;
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

    /**
     * @return the invPurchaseInvoiceService
     */
    public InvPurchaseInvoiceService getInvPurchaseInvoiceService() {
        return invPurchaseInvoiceService;
    }

    /**
     * @param invPurchaseInvoiceService the invPurchaseInvoiceService to set
     */
    public void setInvPurchaseInvoiceService(InvPurchaseInvoiceService invPurchaseInvoiceService) {
        this.invPurchaseInvoiceService = invPurchaseInvoiceService;
    }

    /**
     * @return the totalTotalSumAvarage
     */
    public BigDecimal getTotalTotalSumAvarage() {
        return totalTotalSumAvarage;
    }

    /**
     * @param totalTotalSumAvarage the totalTotalSumAvarage to set
     */
    public void setTotalTotalSumAvarage(BigDecimal totalTotalSumAvarage) {
        this.totalTotalSumAvarage = totalTotalSumAvarage;
    }

}
