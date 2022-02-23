/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.OrganizationSiteService;
import com.isag.businessservice.reports.InstViewService;
import com.isag.businessservice.reports.searchBean.CollectionAndPaymentReportSearchBean;
import com.isag.converter.InvOrganizationSiteConverter;
import com.isag.converter.InvOrganizationSiteDTOConverter;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.PurchaseItemsGroupView;
import com.isag.isag.AutoComplete;
import com.isag.isag.BaseReportBean;
import com.isag.isag.UserData;
import com.isag.report.entity.InstMaturityBean;
import com.isag.report.entity.PurchasesByCategoriesOfItemsBean;
import com.isag.views.InstView;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author ahmed
 */
@Named("instMonthlyPaymentsMB")
@ViewScoped
public class InstMonthlyPaymentsMB extends BaseReportBean {

    private List<InvOrganizationSiteDTO> invOrgSiteList;
    private InvOrganizationSiteDTOConverter invOrgSiteConverter;
    private String uri;
    private CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean;
    private Map<Integer, PurchasesByCategoriesOfItemsBean> instMaturityBeanMap = new HashMap<>();
    private Map<Date, String> instMaturityByDateTreeMap = new TreeMap<>();
    private Map<String, Integer> instMaturityByMonthsNumbersOrderedTreeMap = new TreeMap<>();
    private Map<String, Integer> instMaturityOrderedAscendingTreeMap = new TreeMap<>();

    private List<InstView> instViewList;
    private Map<String, BigDecimal> totalValuesMap;

    private HashMap hashMap;
    private Integer parameterNumber;
    private String columnTemplate;
    private List<ColumnModel> columns;
    private List<PurchasesByCategoriesOfItemsBean> purchaseByCategoriesOfItemsBeanList;
    private AutoComplete autoComplete;

    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private InstViewService instViewService;

    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();

        reset();
        load();
        createDynamicColumns();

    }

    @Override
    public void load() {
         
    }

    @Override
    public void reset() {
        instViewList = new ArrayList<>();
        instMaturityBeanMap = new HashMap<>();
        instMaturityOrderedAscendingTreeMap = new TreeMap<>();
        instMaturityByMonthsNumbersOrderedTreeMap = new TreeMap<>();
        instMaturityByDateTreeMap = new TreeMap<>();
        totalValuesMap = new HashMap<>();
        collectionAndPaymentReportSearchBean = new CollectionAndPaymentReportSearchBean();
        purchaseByCategoriesOfItemsBeanList = new ArrayList<>();

    }

    public void searchIntialize() {
        instViewList = new ArrayList<>();
        instMaturityBeanMap = new HashMap<>();
        instMaturityOrderedAscendingTreeMap = new TreeMap<>();
        instMaturityByMonthsNumbersOrderedTreeMap = new TreeMap<>();
        instMaturityByDateTreeMap = new TreeMap<>();
        totalValuesMap = new HashMap<>();
        purchaseByCategoriesOfItemsBeanList = new ArrayList<>();

    }

    @Override
    public void search() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        searchIntialize();
        collectionAndPaymentReportSearchBean.setBranchId(getUserData().getUserBranch().getId());
        instViewList = instViewService.getInstViewBySearchBean(collectionAndPaymentReportSearchBean);
        prepareMaps();
        PurchasesByCategoriesOfItemsBean lastItemsBean = new PurchasesByCategoriesOfItemsBean();

        if (instViewList != null && !instViewList.isEmpty()) {
            for (InstView view : instViewList) {
                if (instMaturityBeanMap.containsKey(view.getCustomerId())) {
                    PurchasesByCategoriesOfItemsBean itemsBean = instMaturityBeanMap.get(view.getCustomerId());
                    itemsBean.setGroup_name(view.getCustomerName());
                    String dateFormat = new SimpleDateFormat("yyyy-MM").format(view.getDueDate());
                    if (instMaturityByDateTreeMap.containsKey(view.getDueDate())) {
                        try {
                            //thsi block of code get the field and make it accessible thin get the existing value from that specefied field and added to it the the previous values
                            String fieldName = "month_".concat(instMaturityOrderedAscendingTreeMap.get(dateFormat).toString());
                            Field field = PurchasesByCategoriesOfItemsBean.class.getDeclaredField(fieldName);
                            field.setAccessible(true);
                            BigDecimal monthValue = (BigDecimal) field.get(itemsBean);
                            monthValue = (monthValue != null ? monthValue : BigDecimal.ZERO).add(view.getValue() != null ? view.getValue() : BigDecimal.ZERO);
                            field.set(itemsBean, monthValue);

                            //calculate the total to sum the values horizontally  
                            itemsBean.setTotal((itemsBean.getTotal() != null ? itemsBean.getTotal() : BigDecimal.ZERO).add(view.getValue() != null ? view.getValue() : BigDecimal.ZERO));
                            instMaturityBeanMap.put(view.getCustomerId(), itemsBean);

                            //calculate the total vertically 
                            BigDecimal total = totalValuesMap.get(dateFormat);
                            total = (total != null ? total : BigDecimal.ZERO).add(view.getValue() != null ? view.getValue() : BigDecimal.ZERO);
                            totalValuesMap.put(dateFormat, total);

                        } catch (NoSuchFieldException | IllegalAccessException ex) {
                            Logger.getLogger(PurchasesByCategoriesOfItemsMB.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
            for (Map.Entry<String, BigDecimal> entry : totalValuesMap.entrySet()) {
                //this block to get the fields of the last row and assign total values by the help of totalValuesMap filled during calculate the totals in the previous block
                try {
                    String fieldName = "month_".concat(instMaturityOrderedAscendingTreeMap.get(entry.getKey()).toString());
                    Field field = PurchasesByCategoriesOfItemsBean.class.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    BigDecimal totalValue = (BigDecimal) field.get(lastItemsBean);
                    totalValue = (totalValue != null ? totalValue : BigDecimal.ZERO).add(entry.getValue());
                    field.set(lastItemsBean, totalValue);
                } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(PurchasesByCategoriesOfItemsMB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            lastItemsBean.setGroup_name(userDDs.get("TOTAL"));
        }
        purchaseByCategoriesOfItemsBeanList = new ArrayList<>(instMaturityBeanMap.values());
        purchaseByCategoriesOfItemsBeanList.add(lastItemsBean);
        UIComponent table = FacesContext.getCurrentInstance().getViewRoot().findComponent(":form:table");
        table.setValueExpression("sortBy", null);
    }

    public void prepareMaps() {
        // create map has group id and new objects of months class will be filled later for each corresponding month
        for (InstView instView : instViewList) {
            if (instMaturityBeanMap != null && !instMaturityBeanMap.containsKey(instView.getCustomerId())) {
                instMaturityBeanMap.put(instView.getCustomerId(), new PurchasesByCategoriesOfItemsBean());
            }
            // another map contains date as a key and month+year as a value will help us to determine number of months+date (ex:02-2019)
            String dateFormat = new SimpleDateFormat("yyyy-MM").format(instView.getDueDate());
            if (instMaturityByDateTreeMap != null && !instMaturityByDateTreeMap.containsKey(instView.getDueDate())) {
                instMaturityByDateTreeMap.put(instView.getDueDate(), dateFormat);
            }
        }

        for (Date entry : instMaturityByDateTreeMap.keySet()) {
            String dateMonthFormat = new SimpleDateFormat("MM").format(entry);
            String dateMonthYearFormat = new SimpleDateFormat("yyyy-MM").format(entry);
            if (instMaturityByMonthsNumbersOrderedTreeMap != null && !instMaturityByMonthsNumbersOrderedTreeMap.containsValue(Integer.parseInt(dateMonthFormat))) {
                instMaturityByMonthsNumbersOrderedTreeMap.put(dateMonthYearFormat, Integer.parseInt(dateMonthFormat));
                totalValuesMap.put(dateMonthYearFormat, BigDecimal.ZERO);
                //second else if to check if this months repeated but with the next year if yes 12 summed with that month 
                // ex : if month 2 exist with year 2018 and found again but with year 2019 summed 12 on 2 to be 14 and that number will be the number appended to the field
            } else if (instMaturityByMonthsNumbersOrderedTreeMap.containsValue(Integer.parseInt(dateMonthFormat)) && !instMaturityByMonthsNumbersOrderedTreeMap.containsKey(dateMonthYearFormat)) {
                Integer x = Integer.parseInt(dateMonthFormat);
                instMaturityByMonthsNumbersOrderedTreeMap.put(dateMonthYearFormat, x + 12);
                totalValuesMap.put(dateMonthYearFormat, BigDecimal.ZERO);
            }
        }
        arrangeListForPrinting();
        createDynamicColumns();
    }

    public void arrangeListForPrinting() {
        //arrange fields regardless the month actual month
        // ex : the search show month april then may the fields start from month_1 by help of this map purchaseItemsOrderedAscendinTreeMap
        Integer initialNumber = 1;
        for (Map.Entry<String, Integer> entry : instMaturityByMonthsNumbersOrderedTreeMap.entrySet()) {
            instMaturityOrderedAscendingTreeMap.put(entry.getKey(), initialNumber);
            initialNumber++;
        }
    }

    private void createDynamicColumns() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        parameterNumber = 1;
        hashMap = new HashMap();
        hashMap.put("organizationName", userDDs.get("CUSTOMERS"));
        hashMap.put("total", userDDs.get("TOTAL"));

        columnTemplate = new String();
        columnTemplate = "group_name";
        if (instMaturityOrderedAscendingTreeMap != null) {
            for (Map.Entry<String, Integer> entry : instMaturityOrderedAscendingTreeMap.entrySet()) {
                String columnName = entry.getKey();
                columnTemplate = columnTemplate.concat(" " + columnName);
                hashMap.put("parameter_" + parameterNumber.toString(), columnName);
                parameterNumber++;
            }
        }
        columnTemplate = columnTemplate.concat(" total");
        String[] columnKeys = columnTemplate.split(" ");
        columns = new ArrayList<ColumnModel>();

        for (String columnKey : columnKeys) {
            if (columnKey.contains("group_name") || columnKey.contains("total")) {
                columns.add(new ColumnModel(userDDs.get(columnKey.toUpperCase()), columnKey));
            } else {
                columns.add(new ColumnModel(columnKey.toUpperCase(), "month_" + instMaturityOrderedAscendingTreeMap.get(columnKey)));
            }
        }
    }

    @Override
    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        hashMap.put("reportNameText", userDDs.get("MONTH_PAY_REP"));

        hashMap.put("PrintedBy", getUserData().getUser().getName());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        //    hashMap.put("companyName", getUserData().getCompany().getName());
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        hashMap.put("itemFromText", userDDs.get("FROM_CUSTOM"));
        hashMap.put("itemToText", userDDs.get("TO_CUSTOM"));
        hashMap.put("dateFromText", userDDs.get("YEAR_FROM"));
        hashMap.put("dateTotText", userDDs.get("YEAR_TO"));
//        hashMap.put("inventoryFromText", "من مستودع");
//        hashMap.put("inventoryToText", "الى مستودع");
//        hashMap.put("invoiceNumberFromText", "من رقم فاتورة");
//        hashMap.put("invoiceNumberToText", "الى رقم فاتورة");

        hashMap.put("itemFromValue", collectionAndPaymentReportSearchBean.getOrgNameFrom() != null ? collectionAndPaymentReportSearchBean.getOrgNameFrom().getName() : null);
        hashMap.put("itemToValue", collectionAndPaymentReportSearchBean.getOrgNameTo() != null ? collectionAndPaymentReportSearchBean.getOrgNameTo().getName() : null);
        hashMap.put("dateFromValue", collectionAndPaymentReportSearchBean.getFromDate());
        hashMap.put("dateTotValue", collectionAndPaymentReportSearchBean.getToDate());
//        hashMap.put("inventoryFromValue", purchaseItemsGroupViewSearchBean.getInventoryIdFrom() != null ? purchaseItemsGroupViewSearchBean.getInventoryIdFrom().getName() : null);
//        hashMap.put("inventoryToValue", purchaseItemsGroupViewSearchBean.getInventoryIdTo() != null ? purchaseItemsGroupViewSearchBean.getInventoryIdTo().getName() : null);
//        hashMap.put("invoiceNumberFromValue", purchaseItemsGroupViewSearchBean.getSalesInvoiceFrom() != null ? purchaseItemsGroupViewSearchBean.getSalesInvoiceFrom().getSerial() : null);
//        hashMap.put("invoiceNumberToValue", purchaseItemsGroupViewSearchBean.getSalesInvoiceTo() != null ? purchaseItemsGroupViewSearchBean.getSalesInvoiceTo().getSerial() : null);

        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (purchaseByCategoriesOfItemsBeanList != null && !purchaseByCategoriesOfItemsBeanList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "purchaseByCategoriesOfItemsReportLS.jasper", purchaseByCategoriesOfItemsBeanList, "pdf");
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

    public List<InvOrganizationSiteDTO> completOrgSite(String query) {
        return getAutoComplete().completOrgSiteDTO(query, getInvOrgSiteList(), invOrgSiteConverter);

    }

    /**
     * @return the invOrgSiteList
     */
    public List<InvOrganizationSiteDTO> getInvOrgSiteList() {
        invOrgSiteList = organizationSiteService.getCustomerByBranchIdForReportDTO(getUserData().getUserBranch() != null ? getUserData().getUserBranch().getId() : null);
        setInvOrgSiteConverter(new InvOrganizationSiteDTOConverter(invOrgSiteList));
        return invOrgSiteList;
    }

    /**
     * @param invOrgSiteList the invOrgSiteList to set
     */
    public void setInvOrgSiteList(List<InvOrganizationSiteDTO> invOrgSiteList) {
        this.invOrgSiteList = invOrgSiteList;
    }

    /**
     * @return the invOrgSiteConverter
     */
    public InvOrganizationSiteDTOConverter getInvOrgSiteConverter() {
        return invOrgSiteConverter;
    }

    /**
     * @param invOrgSiteConverter the invOrgSiteConverter to set
     */
    public void setInvOrgSiteConverter(InvOrganizationSiteDTOConverter invOrgSiteConverter) {
        this.invOrgSiteConverter = invOrgSiteConverter;
    }

    /**
     * @return the collectionAndPaymentReportSearchBean
     */
    public CollectionAndPaymentReportSearchBean getCollectionAndPaymentReportSearchBean() {
        return collectionAndPaymentReportSearchBean;
    }

    /**
     * @param collectionAndPaymentReportSearchBean the
     * collectionAndPaymentReportSearchBean to set
     */
    public void setCollectionAndPaymentReportSearchBean(CollectionAndPaymentReportSearchBean collectionAndPaymentReportSearchBean) {
        this.collectionAndPaymentReportSearchBean = collectionAndPaymentReportSearchBean;
    }

    /**
     * @return the instMaturityBeanMap
     */
    public Map<Integer, PurchasesByCategoriesOfItemsBean> getInstMaturityBeanMap() {
        return instMaturityBeanMap;
    }

    /**
     * @param instMaturityBeanMap the instMaturityBeanMap to set
     */
    public void setInstMaturityBeanMap(Map<Integer, PurchasesByCategoriesOfItemsBean> instMaturityBeanMap) {
        this.instMaturityBeanMap = instMaturityBeanMap;
    }

    /**
     * @return the instViewList
     */
    public List<InstView> getInstViewList() {
        return instViewList;
    }

    /**
     * @param instViewList the instViewList to set
     */
    public void setInstViewList(List<InstView> instViewList) {
        this.instViewList = instViewList;
    }

    /**
     * @return the instMaturityByDateTreeMap
     */
    public Map<Date, String> getInstMaturityByDateTreeMap() {
        return instMaturityByDateTreeMap;
    }

    /**
     * @param instMaturityByDateTreeMap the instMaturityByDateTreeMap to set
     */
    public void setInstMaturityByDateTreeMap(Map<Date, String> instMaturityByDateTreeMap) {
        this.instMaturityByDateTreeMap = instMaturityByDateTreeMap;
    }

    /**
     * @return the instMaturityByMonthsNumbersOrderedTreeMap
     */
    public Map<String, Integer> getInstMaturityByMonthsNumbersOrderedTreeMap() {
        return instMaturityByMonthsNumbersOrderedTreeMap;
    }

    /**
     * @param instMaturityByMonthsNumbersOrderedTreeMap the
     * instMaturityByMonthsNumbersOrderedTreeMap to set
     */
    public void setInstMaturityByMonthsNumbersOrderedTreeMap(Map<String, Integer> instMaturityByMonthsNumbersOrderedTreeMap) {
        this.instMaturityByMonthsNumbersOrderedTreeMap = instMaturityByMonthsNumbersOrderedTreeMap;
    }

    /**
     * @return the totalValuesMap
     */
    public Map<String, BigDecimal> getTotalValuesMap() {
        return totalValuesMap;
    }

    /**
     * @param totalValuesMap the totalValuesMap to set
     */
    public void setTotalValuesMap(Map<String, BigDecimal> totalValuesMap) {
        this.totalValuesMap = totalValuesMap;
    }

    /**
     * @return the instMaturityOrderedAscendingTreeMap
     */
    public Map<String, Integer> getInstMaturityOrderedAscendingTreeMap() {
        return instMaturityOrderedAscendingTreeMap;
    }

    /**
     * @param instMaturityOrderedAscendingTreeMap the
     * instMaturityOrderedAscendingTreeMap to set
     */
    public void setInstMaturityOrderedAscendingTreeMap(Map<String, Integer> instMaturityOrderedAscendingTreeMap) {
        this.instMaturityOrderedAscendingTreeMap = instMaturityOrderedAscendingTreeMap;
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
     * @return the purchaseByCategoriesOfItemsBeanList
     */
    public List<PurchasesByCategoriesOfItemsBean> getPurchaseByCategoriesOfItemsBeanList() {
        return purchaseByCategoriesOfItemsBeanList;
    }

    /**
     * @param purchaseByCategoriesOfItemsBeanList the
     * purchaseByCategoriesOfItemsBeanList to set
     */
    public void setPurchaseByCategoriesOfItemsBeanList(List<PurchasesByCategoriesOfItemsBean> purchaseByCategoriesOfItemsBeanList) {
        this.purchaseByCategoriesOfItemsBeanList = purchaseByCategoriesOfItemsBeanList;
    }

    /**
     * @return the autoComplete
     */
    public AutoComplete getAutoComplete() {
        if (autoComplete == null) {
            autoComplete = new AutoComplete();
        }
        return autoComplete;
    }

    /**
     * @param autoComplete the autoComplete to set
     */
    public void setAutoComplete(AutoComplete autoComplete) {
        this.autoComplete = autoComplete;
    }
}
