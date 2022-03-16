/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.reports;

import com.toby.businessservice.TobyUserInventoryService;
import com.toby.businessservice.report.InvDelegatorSalesByMainItemsGroupService;
import com.toby.businessservice.InvDelegatorService;
import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.reports.searchBean.InvDelegatorSalesByMainItemsGroupSearchBean;
import com.toby.converter.InvDelegatorConvertor;
import com.toby.converter.InvInventoryConverter;
import com.toby.converter.InvOrganizationSiteConverter;
import com.toby.entity.InvInventory;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.InventoryDelegator;
import com.toby.entity.SalesDelegateView;
import com.toby.report.entity.InvDelegatorSalesByMainItemsGroupBean;
import com.toby.report.entity.InvDelegatorSalesDynamicReportBean;
import com.toby.toby.BaseReportBean;
import com.toby.toby.UserData;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author ahmed
 */
@Named("invDelegatorSalesByMainItemsGroupMB")
@ViewScoped
public class InvDelegatorSalesByMainItemsGroupMB extends BaseReportBean {

    private UserData userData;
    private List<SalesDelegateView> salesDelegateViewList;
    private InvDelegatorSalesByMainItemsGroupSearchBean delegatorSalesByMainItemsGroupSearchBean;
    private List<InvDelegatorSalesByMainItemsGroupBean> invDelegatorSalesByMainItemsGroupBeanList;
    private InvOrganizationSiteConverter invOrgSiteConverter;
    private List<InvOrganizationSite> invOrgSiteList;
    private InvDelegatorConvertor invDelegatorConverter;
    private List<InventoryDelegator> inventoryDelegatorList;
    private InvInventoryConverter invInventoryConverter;
    private List<InvInventory> invInventoryList;
    private List<ColumnModel> columns;
    private final static List<String> VALID_COLUMN_KEYS = Arrays.asList("organization_name", "cash", "posting", "credit_document", "total");
    private String columnTemplate = "organizationName";
    private String[] selectedInvoiceTypes;
    private List<String> invoiceTypes;
    private List<InvDelegatorSalesDynamicReportBean> invDelegatorSalesDynamicReportBeanList;
    private HashMap hashMap;
    private Integer parameterNumber;
    private Map<String, String> translateOptionsMap = new HashMap<>();

    @EJB
    private InvDelegatorSalesByMainItemsGroupService invDelegatorSalesByMainItemsGroupService;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private InvDelegatorService invDelegatorService;
    @EJB
    private TobyUserInventoryService tobyUserInventoryService;

    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        reset();
    }

    @Override
    public void reset() {
        Map<String, String> userDDs = getUserData().getUserDDs();

        salesDelegateViewList = new ArrayList<>();
        delegatorSalesByMainItemsGroupSearchBean = new InvDelegatorSalesByMainItemsGroupSearchBean();
        invDelegatorSalesByMainItemsGroupBeanList = new ArrayList<>();
        invOrgSiteList = new ArrayList<>();
        inventoryDelegatorList = new ArrayList<>();
        invInventoryList = new ArrayList<>();
        invDelegatorSalesDynamicReportBeanList = new ArrayList<>();
        fillLists();
        createDynamicColumns();
        invoiceTypes = new ArrayList<>();
        invoiceTypes.add(userDDs.get("CASH"));
        invoiceTypes.add(userDDs.get("POSTING"));
        invoiceTypes.add(userDDs.get("CREDIT_DOCUMENT"));
        translateOptionsMap.put(userDDs.get("CASH"), "cash");
        translateOptionsMap.put(userDDs.get("POSTING"), "posting");
        translateOptionsMap.put(userDDs.get("CREDIT_DOCUMENT"), "credit_document");
    }

    public void fillLists() {
        invOrgSiteList = organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(getUserData().getUserBranch().getId(),true,0);
        invOrgSiteConverter = new InvOrganizationSiteConverter(invOrgSiteList);
        inventoryDelegatorList = invDelegatorService.getSalesByBranch(getUserData().getUserBranch().getId());
        invDelegatorConverter = new InvDelegatorConvertor(inventoryDelegatorList);
        invInventoryList = tobyUserInventoryService.getAllInventroisByUserAndBranchPer(getUserData().getUser().getId(), getUserData().getUserBranch().getId());
        invInventoryConverter = new InvInventoryConverter(invInventoryList);
    }

    @Override
    public void search() {
        salesDelegateViewList = new ArrayList<>();
        invDelegatorSalesDynamicReportBeanList = new ArrayList<>();
        invDelegatorSalesByMainItemsGroupBeanList = new ArrayList<>();
        delegatorSalesByMainItemsGroupSearchBean.setBranchId(getUserData().getUserBranch().getId());
        delegatorSalesByMainItemsGroupSearchBean.setTypeView(true);
        salesDelegateViewList = invDelegatorSalesByMainItemsGroupService.findAllSalesDelegate(delegatorSalesByMainItemsGroupSearchBean);
        createDynamicColumns();
        if (salesDelegateViewList != null && !salesDelegateViewList.isEmpty()) {
            for (SalesDelegateView sdv : salesDelegateViewList) {
                InvDelegatorSalesByMainItemsGroupBean byMainItemsGroupBean = new InvDelegatorSalesByMainItemsGroupBean();
                byMainItemsGroupBean.setOrganization_name(sdv.getDelegatorName());
                byMainItemsGroupBean.setCash(sdv.getCash());
                byMainItemsGroupBean.setPosting(sdv.getPostpone());
                byMainItemsGroupBean.setCredit_document(sdv.getDocumentarycredit());
                byMainItemsGroupBean.setTotal(calculateTotalDueToInvoceType(sdv));
                invDelegatorSalesByMainItemsGroupBeanList.add(byMainItemsGroupBean);
            }
        }
        UIComponent table = FacesContext.getCurrentInstance().getViewRoot().findComponent(":form:table");
        table.setValueExpression("sortBy", null);

    }

    private void createDynamicColumns() {
        Map<String, String> userDDs = getUserData().getUserDDs();

        hashMap = new HashMap();
        hashMap.put("organizationName", userDDs.get("DELEGATOR_NAME"));
        hashMap.put("total", userDDs.get("TOTAL"));
        parameterNumber = 1;

        columnTemplate = new String();
        columnTemplate = "organization_name";
        if (selectedInvoiceTypes == null || (selectedInvoiceTypes != null && selectedInvoiceTypes.length == 0)) {
            columnTemplate = columnTemplate.concat(" cash").concat(" posting").concat(" credit_document");
            hashMap.put("parameter_1", userDDs.get("CASH"));
            hashMap.put("parameter_2", userDDs.get("POSTING"));
            hashMap.put("parameter_3", userDDs.get("CREDIT_DOCUMENT"));
        } else {
            for (String str : selectedInvoiceTypes) {
                if (translateOptionsMap.containsKey(str)) {
                    columnTemplate = columnTemplate.concat(" " + translateOptionsMap.get(str));
                    hashMap.put("parameter_" + parameterNumber.toString(), str);
                } else {
                    columnTemplate = columnTemplate.concat(" " + str);
                    hashMap.put("parameter_" + parameterNumber.toString(), userDDs.get(str.toUpperCase()));
                }

                parameterNumber++;
            }
        }
        columnTemplate = columnTemplate.concat(" total");
        String[] columnKeys = columnTemplate.split(" ");
        columns = new ArrayList<ColumnModel>();

        for (String columnKey : columnKeys) {
            String key = columnKey.trim();

            // if (VALID_COLUMN_KEYS.contains(key)) {
            columns.add(new ColumnModel(userDDs.get(columnKey.toUpperCase()), columnKey));
            //    }
        }
    }

    public void updateColumns() {
        //reset table state
        UIComponent table = FacesContext.getCurrentInstance().getViewRoot().findComponent(":form:table");
        table.setValueExpression("sortBy", null);

        //update columns
        createDynamicColumns();
    }

    public BigDecimal calculateTotalDueToInvoceType(SalesDelegateView sdv) {

        InvDelegatorSalesDynamicReportBean dynamicReportBean = new InvDelegatorSalesDynamicReportBean();
        Integer fieldNum = 1;
        dynamicReportBean.setOrganizationName(sdv.getDelegatorName());

        BigDecimal bd = BigDecimal.ZERO;

        if (columnTemplate != null) {
            if (columnTemplate.contains("cash")) {
                bd = bd.add(sdv.getCash());
                fillReportBean(sdv.getCash(), dynamicReportBean);
            }
            if (columnTemplate.contains("posting")) {
                bd = bd.add(sdv.getPostpone());
                fillReportBean(sdv.getPostpone(), dynamicReportBean);
            }
            if (columnTemplate.contains("creditDocument")) {
                bd = bd.add(sdv.getDocumentarycredit());
                fillReportBean(sdv.getDocumentarycredit(), dynamicReportBean);
            }
        } else {
            bd = sdv.getDocumentarycredit().add(sdv.getPostpone()).add(sdv.getCash());
        }
        dynamicReportBean.setTotal(bd);
        invDelegatorSalesDynamicReportBeanList.add(dynamicReportBean);
        return bd;
    }

    public void fillReportBean(BigDecimal bigDecimal, InvDelegatorSalesDynamicReportBean delegatorSalesDynamicReportBean) {

        if (delegatorSalesDynamicReportBean.getField_1() == null) {
            delegatorSalesDynamicReportBean.setField_1(bigDecimal);
        } else if (delegatorSalesDynamicReportBean.getField_2() == null) {
            delegatorSalesDynamicReportBean.setField_2(bigDecimal);
        } else if (delegatorSalesDynamicReportBean.getField_3() == null) {
            delegatorSalesDynamicReportBean.setField_3(bigDecimal);
        }

    }

    @Override
    public HashMap prepareReport() {
        parameterNumber = 5;
        //  HashMap hashMap = new HashMap();
        hashMap.put("reportNameText", " تقرير مندوب مبيعات حسب مجموعات الاصناف الرئيسية");
        hashMap.put("PrintedBy", getUserData().getUser().getName());
        hashMap.put("branchName", getUserData().getCompany().getName());
      //  hashMap.put("companyName", getUserData().getCompany().getName());
        if (delegatorSalesByMainItemsGroupSearchBean.getInvOrganizationSiteFrom() != null) {
            hashMap.put("parameter_" + parameterNumber.toString(), userDDs.get("FROM_CUSTOM"));
            parameterNumber++;
            hashMap.put("parameter_" + parameterNumber.toString(), delegatorSalesByMainItemsGroupSearchBean.getInvOrganizationSiteFrom().getName());
            parameterNumber++;
        }
        if (delegatorSalesByMainItemsGroupSearchBean.getInvOrganizationSiteTo() != null) {
            hashMap.put("parameter_" + parameterNumber.toString(), userDDs.get("TO_CUSTOM"));
            parameterNumber++;
            hashMap.put("parameter_" + parameterNumber.toString(), delegatorSalesByMainItemsGroupSearchBean.getInvOrganizationSiteTo().getName());
            parameterNumber++;
        }
        if (delegatorSalesByMainItemsGroupSearchBean.getDelegateCodeFrom() != null) {
            hashMap.put("parameter_" + parameterNumber.toString(), userDDs.get("FROM_SALEPER"));
            parameterNumber++;
            hashMap.put("parameter_" + parameterNumber.toString(), delegatorSalesByMainItemsGroupSearchBean.getDelegateCodeFrom().getName());
            parameterNumber++;
        }
        if (delegatorSalesByMainItemsGroupSearchBean.getDelegateCodeTo() != null) {
            hashMap.put("parameter_" + parameterNumber.toString(), userDDs.get("TO_SALEPER"));
            parameterNumber++;
            hashMap.put("parameter_" + parameterNumber.toString(), delegatorSalesByMainItemsGroupSearchBean.getDelegateCodeTo().getName());
            parameterNumber++;
        }
        if (delegatorSalesByMainItemsGroupSearchBean.getInventoryIdFrom() != null) {
            hashMap.put("parameter_" + parameterNumber.toString(), userDDs.get("FROM_INVENTORY"));
            parameterNumber++;
            hashMap.put("parameter_" + parameterNumber.toString(), delegatorSalesByMainItemsGroupSearchBean.getInventoryIdFrom().getName());
            parameterNumber++;
        }
        if (delegatorSalesByMainItemsGroupSearchBean.getInventoryIdTo() != null) {
            hashMap.put("parameter_" + parameterNumber.toString(), userDDs.get("TO_REPOS"));
            parameterNumber++;
            hashMap.put("parameter_" + parameterNumber.toString(), delegatorSalesByMainItemsGroupSearchBean.getInventoryIdTo().getName());
            parameterNumber++;
        }
        if (delegatorSalesByMainItemsGroupSearchBean.getDateFrom() != null) {
            hashMap.put("parameter_" + parameterNumber.toString(), userDDs.get("YEAR_FROM"));
            parameterNumber++;
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(delegatorSalesByMainItemsGroupSearchBean.getDateFrom());
            hashMap.put("parameter_" + parameterNumber.toString(), formatDateFrom);
            parameterNumber++;
        }
        if (delegatorSalesByMainItemsGroupSearchBean.getDateTo() != null) {
            hashMap.put("parameter_" + parameterNumber.toString(), userDDs.get("YEAR_TO"));
            parameterNumber++;
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(delegatorSalesByMainItemsGroupSearchBean.getDateTo());
            hashMap.put("parameter_" + parameterNumber.toString(), formatDateTo);
            parameterNumber++;
        }
        if (delegatorSalesByMainItemsGroupSearchBean.getInvoiceNumberFrom() != null) {
            hashMap.put("parameter_" + parameterNumber.toString(), userDDs.get("FROM_INVOI_NO"));
            parameterNumber++;
            hashMap.put("parameter_" + parameterNumber.toString(), delegatorSalesByMainItemsGroupSearchBean.getInvoiceNumberFrom().toString());
            parameterNumber++;
        }
        if (delegatorSalesByMainItemsGroupSearchBean.getInvoiceNumberTo() != null) {
            hashMap.put("parameter_" + parameterNumber.toString(), userDDs.get("TO_INVOI_NO"));
            parameterNumber++;
            hashMap.put("parameter_" + parameterNumber.toString(), delegatorSalesByMainItemsGroupSearchBean.getInvoiceNumberTo().toString());
            parameterNumber++;
        }
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        search();
        if (invDelegatorSalesDynamicReportBeanList != null && !invDelegatorSalesDynamicReportBeanList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "invDelegatorSalesDynamicReport.jasper", invDelegatorSalesDynamicReportBeanList, "pdf");
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
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<InvOrganizationSite> completOrgSite(String query) {
        List<InvOrganizationSite> invOrganizationSites = invOrgSiteList;
        if (query == null || query.trim().equals("")) {

            setInvOrgSiteConverter(new InvOrganizationSiteConverter(invOrganizationSites));
            return invOrganizationSites;
        }
        List<InvOrganizationSite> filteredInvs = new ArrayList<>();

        String nameAr;
        String code;
        InvOrganizationSite invOrgSiteFilter;

        for (int i = 0; i < invOrgSiteList.size(); i++) {
            invOrgSiteFilter = invOrganizationSites.get(i);
            nameAr = invOrgSiteFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invOrgSiteFilter)) {
                    filteredInvs.add(invOrgSiteFilter);
                }
            }

            code = invOrgSiteFilter.getCode();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invOrgSiteFilter)) {
                    filteredInvs.add(invOrgSiteFilter);
                }
            }
        }

        setInvOrgSiteConverter(new InvOrganizationSiteConverter(filteredInvs));
        return filteredInvs;
    }

    public List<InventoryDelegator> completeInvDelegator(String query) {
        List<InventoryDelegator> invDelegatorList = inventoryDelegatorList;
        if (query == null || query.trim().equals("")) {
            invDelegatorConverter = new InvDelegatorConvertor(invDelegatorList);
            return invDelegatorList;
        }
        List<InventoryDelegator> filteredInvDelegators = new ArrayList<>();

        String nameAr;
        Integer code;
        InventoryDelegator invDelegatorFilter;
        for (int i = 0; i < inventoryDelegatorList.size(); i++) {
            invDelegatorFilter = invDelegatorList.get(i);
            nameAr = invDelegatorFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvDelegators.contains(invDelegatorFilter)) {
                    filteredInvDelegators.add(invDelegatorFilter);
                }
            }

            code = invDelegatorFilter.getId();
            if (code != null && String.valueOf(code).toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvDelegators.contains(invDelegatorFilter)) {
                    filteredInvDelegators.add(invDelegatorFilter);
                }
            }
        }

        invDelegatorConverter = new InvDelegatorConvertor(filteredInvDelegators);
        return filteredInvDelegators;
    }

    public List<InvInventory> completeInventory(String query) {
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
     * @return the salesDelegateViewList
     */
    public List<SalesDelegateView> getSalesDelegateViewList() {
        return salesDelegateViewList;
    }

    /**
     * @param salesDelegateViewList the salesDelegateViewList to set
     */
    public void setSalesDelegateViewList(List<SalesDelegateView> salesDelegateViewList) {
        this.salesDelegateViewList = salesDelegateViewList;
    }

    /**
     * @return the delegatorSalesByMainItemsGroupSearchBean
     */
    public InvDelegatorSalesByMainItemsGroupSearchBean getDelegatorSalesByMainItemsGroupSearchBean() {
        return delegatorSalesByMainItemsGroupSearchBean;
    }

    /**
     * @param delegatorSalesByMainItemsGroupSearchBean the
     * delegatorSalesByMainItemsGroupSearchBean to set
     */
    public void setDelegatorSalesByMainItemsGroupSearchBean(InvDelegatorSalesByMainItemsGroupSearchBean delegatorSalesByMainItemsGroupSearchBean) {
        this.delegatorSalesByMainItemsGroupSearchBean = delegatorSalesByMainItemsGroupSearchBean;
    }

    /**
     * @return the invDelegatorSalesByMainItemsGroupBeanList
     */
    public List<InvDelegatorSalesByMainItemsGroupBean> getInvDelegatorSalesByMainItemsGroupBeanList() {
        return invDelegatorSalesByMainItemsGroupBeanList;
    }

    /**
     * @param invDelegatorSalesByMainItemsGroupBeanList the
     * invDelegatorSalesByMainItemsGroupBeanList to set
     */
    public void setInvDelegatorSalesByMainItemsGroupBeanList(List<InvDelegatorSalesByMainItemsGroupBean> invDelegatorSalesByMainItemsGroupBeanList) {
        this.invDelegatorSalesByMainItemsGroupBeanList = invDelegatorSalesByMainItemsGroupBeanList;
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
     * @return the columns
     */
    public List<ColumnModel> getColumns() {
        return columns;
    }

    /**
     * @param columns the columns to set
     */
    public void setColumns(List<ColumnModel> columns) {
        this.columns = columns;
    }

    /**
     * @return the VALID_COLUMN_KEYS
     */
    public static List<String> getVALID_COLUMN_KEYS() {
        return VALID_COLUMN_KEYS;
    }

    /**
     * @return the columnTemplate
     */
    public String getColumnTemplate() {
        return columnTemplate;
    }

    /**
     * @param columnTemplate the columnTemplate to set
     */
    public void setColumnTemplate(String columnTemplate) {
        this.columnTemplate = columnTemplate;
    }

    /**
     * @return the selectedInvoiceTypes
     */
    public String[] getSelectedInvoiceTypes() {
        return selectedInvoiceTypes;
    }

    /**
     * @param selectedInvoiceTypes the selectedInvoiceTypes to set
     */
    public void setSelectedInvoiceTypes(String[] selectedInvoiceTypes) {
        this.selectedInvoiceTypes = selectedInvoiceTypes;
    }

    /**
     * @return the invoiceTypes
     */
    public List<String> getInvoiceTypes() {
        return invoiceTypes;
    }

    /**
     * @param invoiceTypes the invoiceTypes to set
     */
    public void setInvoiceTypes(List<String> invoiceTypes) {
        this.invoiceTypes = invoiceTypes;
    }

    /**
     * @return the invDelegatorSalesDynamicReportBeanList
     */
    public List<InvDelegatorSalesDynamicReportBean> getInvDelegatorSalesDynamicReportBeanList() {
        return invDelegatorSalesDynamicReportBeanList;
    }

    /**
     * @param invDelegatorSalesDynamicReportBeanList the
     * invDelegatorSalesDynamicReportBeanList to set
     */
    public void setInvDelegatorSalesDynamicReportBeanList(List<InvDelegatorSalesDynamicReportBean> invDelegatorSalesDynamicReportBeanList) {
        this.invDelegatorSalesDynamicReportBeanList = invDelegatorSalesDynamicReportBeanList;
    }

    /**
     * @return the hashMap
     */
    public HashMap getHashMap() {
        return hashMap;
    }

    /**
     * @param hashMap the hashMap to set
     */
    public void setHashMap(HashMap hashMap) {
        this.hashMap = hashMap;
    }

    /**
     * @return the parameterNumber
     */
    public Integer getParameterNumber() {
        return parameterNumber;
    }

    /**
     * @param parameterNumber the parameterNumber to set
     */
    public void setParameterNumber(Integer parameterNumber) {
        this.parameterNumber = parameterNumber;
    }

    private static class userDDs {

        private static Object get(String from_custom) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public userDDs() {
        }
    }

}
