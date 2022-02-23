/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.InvReturnPurchaseService;
import com.isag.businessservice.IsagUserInventoryService;
import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.reports.InvReturnPurchaseReportService;
import com.isag.businessservice.reports.searchBean.InvReturnPurchaseSearchBean;
import com.isag.converter.InvInventoryConverter;
import com.isag.converter.InvOrganizationSiteConverter;
import com.isag.converter.InvReturnPurchaseConverter;
import com.isag.entity.InvInventory;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.InvReturnPurchase;
import com.isag.isag.BaseReportBean;
import com.isag.isag.UserData;
import com.isag.report.entity.ReturnInvoiceReportBean;
import com.isag.views.ReturnInvoiceView;
import java.io.IOException;
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
@Named("InvReturnPurchaseReportMB")
@ViewScoped
public class InvReturnPurchaseReportMB extends BaseReportBean {

    // Lists and Converters
    private List<InvInventory> invInventoryList;
    private InvInventoryConverter invInventoryConverter;

    List<InvReturnPurchase> invReturnPurchaseList;
    private InvReturnPurchaseConverter invReturnPurchaseConverter;

    private List<InvOrganizationSite> suplierList;
    private InvOrganizationSiteConverter suplierConverter;

    private List<ReturnInvoiceView> returnInvoiceViewList;
    private List<ReturnInvoiceReportBean> returnInvoiceReportBeanList;

    // Objects
    private Integer branchId;
    private String uri;
    private String screenMode;

    private Boolean isSales;

    // Bean Objs and Lists
    private InvReturnPurchaseSearchBean invReturnPurchaseSearchBean;

    // EJBs
    @EJB
    private IsagUserInventoryService isagUserInventoryService;
    @EJB
    private InvReturnPurchaseReportService invReturnPurchaseReportService;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private InvReturnPurchaseService invReturnPurchaseService;

    @Override
    @PostConstruct
    public void load() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        setScreenMode((String) context.getSessionMap().get("ScreenMode"));
        branchId = getUserData().getUserBranch().getId();
        uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
        initObjs();

        fillLists();
    }

    private void initObjs() {
        invInventoryList = new ArrayList<>();
        suplierList = new ArrayList<>();
        invReturnPurchaseList = new ArrayList<>();
        returnInvoiceViewList = new ArrayList<>();
        invReturnPurchaseSearchBean = new InvReturnPurchaseSearchBean();

        if (uri.contains("invreturnpurchasereport")) {
            invReturnPurchaseSearchBean.setType(0);
            isSales = false;
        } else {
            invReturnPurchaseSearchBean.setType(1);
            isSales = true;
        }

        invReturnPurchaseSearchBean.setBranchId(branchId);
        invReturnPurchaseSearchBean.setShowReport(false);
    }

    private void fillLists() {
        invReturnPurchaseList = invReturnPurchaseService.getALLInvReturnPurchaseByBranchIdPer(branchId, isSales);

        invReturnPurchaseConverter = new InvReturnPurchaseConverter(invReturnPurchaseList);

        suplierList = organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(getUserData().getUserBranch().getId(),true,1);
        suplierConverter = new InvOrganizationSiteConverter(suplierList);

        invInventoryList = isagUserInventoryService.getAllInventroisByUserAndBranchPer(getUserData().getUser().getId(), branchId);
        invInventoryConverter = new InvInventoryConverter(invInventoryList);
    }

    @Override
    public void reset() {
        initObjs();
        fillLists();
        invReturnPurchaseSearchBean = new InvReturnPurchaseSearchBean();
        returnInvoiceViewList = new ArrayList<>();
    }

    public void showItemReport() {
    }

    @Override
    public void search() {
        if (isSales) {
            invReturnPurchaseSearchBean.setType(3);
        } else {
            invReturnPurchaseSearchBean.setType(2);
        }

        invReturnPurchaseSearchBean.setBranchId(branchId);

        returnInvoiceViewList = new ArrayList<>();
        returnInvoiceReportBeanList = new ArrayList<>();

        returnInvoiceViewList = invReturnPurchaseReportService.getAllReturnInvoiceViewByInvReturnPurchaseSearchBean(invReturnPurchaseSearchBean);
        if (returnInvoiceViewList != null && !returnInvoiceViewList.isEmpty()) {
            for (ReturnInvoiceView invoiceView : returnInvoiceViewList) {
                ReturnInvoiceReportBean reportBean = new ReturnInvoiceReportBean();
                reportBean.setSerial(invoiceView.getSerial());
                reportBean.setInvoiceSerial(invoiceView.getInvoiceSerial());
                reportBean.setSupplierCode(invoiceView.getSupplierCode());
                reportBean.setSupplierName(invoiceView.getSupplierName());
                reportBean.setDate(invoiceView.getDate());
                reportBean.setNet(invoiceView.getNet());
                returnInvoiceReportBeanList.add(reportBean);
            }
        }
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        search();
        if (isSales) {
            fillReport(prepareReport(), getUserData().getReportPath() + "InvReturnReport.jasper", returnInvoiceReportBeanList, "pdf");
        } else {
            fillReport(prepareReport(), getUserData().getReportPath() + "InvReturnReport.jasper", returnInvoiceReportBeanList, "pdf");
        }
    }


    @Override
    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();

        hashMap.put("PrintedBy", getUserData().getUser().getName());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        hashMap.put("CompanyName", getUserData().getCompany().getName());

        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        hashMap.put("dateText", userDDs.get("DATE"));

        if (isSales) {
            hashMap.put("header", userDDs.get("SALES_REP"));
        } else {
            hashMap.put("header", userDDs.get("PUR_RET"));
        }

        hashMap.put("DateFromLabel", userDDs.get("FROM_DATE"));
        hashMap.put("DateToLabel", userDDs.get("TO_DATE"));

        hashMap.put("DateFromValue", invReturnPurchaseSearchBean.getDateFrom());
        hashMap.put("DateToValue", invReturnPurchaseSearchBean.getDateTo());

        hashMap.put("ReceiptNum", userDDs.get("RETU_NO"));
        hashMap.put("ReceiptDate", userDDs.get("DATEE"));

        hashMap.put("invoiceNumText", userDDs.get("PURCH_INVO_NO"));

        hashMap.put("supplierCodeText", userDDs.get("SUPPLIER_NUM"));
        hashMap.put("supplierNameText", userDDs.get("SUPPLIER_NAME"));
        hashMap.put("netText", userDDs.get("RETURN"));
        hashMap.put("totalText", userDDs.get("TOTAL"));

        hashMap.put("inventoryText", userDDs.get("DEPOSITORY"));
        hashMap.put("totalNetText", userDDs.get("TOTAL_NET"));
        hashMap.put("totalReportText", userDDs.get("TOTAL_REPORT"));
        return hashMap;
    }

    public List<InvInventory> completInventory(String query) {
        List<InvInventory> invList = invInventoryList;
        if (query == null || query.trim().equals("")) {

            invInventoryConverter = new InvInventoryConverter(invList);
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

        invInventoryConverter = new InvInventoryConverter(filteredInvs);
        return filteredInvs;
    }

    public List<InvReturnPurchase> completePurchaseReturn(String query) {
        List<InvReturnPurchase> invList = invReturnPurchaseList;
        if (query == null || query.trim().equals("")) {

            invReturnPurchaseConverter = new InvReturnPurchaseConverter(invList);
            return invList;
        }
        List<InvReturnPurchase> filteredInvs = new ArrayList<>();

        Integer code;
        InvReturnPurchase invFilter;
        for (int i = 0; i < invReturnPurchaseList.size(); i++) {
            invFilter = invList.get(i);

            code = invFilter.getSerial();
            if (code != null && String.valueOf(code).toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invFilter)) {
                    filteredInvs.add(invFilter);
                }
            }
        }

        invReturnPurchaseConverter = new InvReturnPurchaseConverter(filteredInvs);
        return filteredInvs;
    }

    public List<InvOrganizationSite> completSupliers(String query) {
        List<InvOrganizationSite> invOrganizationSites = suplierList;
        if (query == null || query.trim().equals("")) {

            suplierConverter = new InvOrganizationSiteConverter(invOrganizationSites);
            return invOrganizationSites;
        }
        List<InvOrganizationSite> filteredSupliers = new ArrayList<>();

        String nameAr;
        String code;
        InvOrganizationSite suplierFilter;
        for (int i = 0; i < suplierList.size(); i++) {
            suplierFilter = invOrganizationSites.get(i);
            nameAr = suplierFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase()) && !filteredSupliers.contains(suplierFilter)) {
                filteredSupliers.add(suplierFilter);
            }

            code = suplierFilter.getCode();
            if (code != null && code.toLowerCase().contains(query.toLowerCase()) && !filteredSupliers.contains(suplierFilter)) {
                filteredSupliers.add(suplierFilter);
            }
        }

        suplierConverter = new InvOrganizationSiteConverter(filteredSupliers);
        return filteredSupliers;
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

    public List<InvInventory> getInvInventoryList() {
        return invInventoryList;
    }

    public void setInvInventoryList(List<InvInventory> invInventoryList) {
        this.invInventoryList = invInventoryList;
    }

    public InvInventoryConverter getInvInventoryConverter() {
        return invInventoryConverter;
    }

    public void setInvInventoryConverter(InvInventoryConverter invInventoryConverter) {
        this.invInventoryConverter = invInventoryConverter;
    }

    public Boolean getIsSales() {
        return isSales;
    }

    public void setIsSales(Boolean isSales) {
        this.isSales = isSales;
    }

    public InvOrganizationSiteConverter getSuplierConverter() {
        return suplierConverter;
    }

    public void setSuplierConverter(InvOrganizationSiteConverter suplierConverter) {
        this.suplierConverter = suplierConverter;
    }

    public InvReturnPurchaseSearchBean getInvReturnPurchaseSearchBean() {
        return invReturnPurchaseSearchBean;
    }

    public void setInvReturnPurchaseSearchBean(InvReturnPurchaseSearchBean invReturnPurchaseSearchBean) {
        this.invReturnPurchaseSearchBean = invReturnPurchaseSearchBean;
    }

    public InvReturnPurchaseConverter getInvReturnPurchaseConverter() {
        return invReturnPurchaseConverter;
    }

    public void setInvReturnPurchaseConverter(InvReturnPurchaseConverter invReturnPurchaseConverter) {
        this.invReturnPurchaseConverter = invReturnPurchaseConverter;
    }

    public List<ReturnInvoiceView> getReturnInvoiceViewList() {
        return returnInvoiceViewList;
    }

    public void setReturnInvoiceViewList(List<ReturnInvoiceView> returnInvoiceViewList) {
        this.returnInvoiceViewList = returnInvoiceViewList;
    }

    /**
     * @return the ReturnInvoiceReportBeanList
     */
    public List<ReturnInvoiceReportBean> getReturnInvoiceReportBeanList() {
        return returnInvoiceReportBeanList;
    }

    /**
     * @param ReturnInvoiceReportBeanList the ReturnInvoiceReportBeanList to set
     */
    public void setReturnInvoiceReportBeanList(List<ReturnInvoiceReportBean> ReturnInvoiceReportBeanList) {
        this.returnInvoiceReportBeanList = ReturnInvoiceReportBeanList;
    }
}
