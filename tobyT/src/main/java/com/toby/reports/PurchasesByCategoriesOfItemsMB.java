/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.reports;

import com.toby.businessservice.report.PurchaseItemsGroupViewService;
import com.toby.businessservice.reports.searchBean.InvDelegatorSalesByMainItemsGroupSearchBean;
import com.toby.businessservice.reports.searchBean.PurchaseItemsGroupViewSearchBean;
import com.toby.entity.PurchaseItemsGroupView;
import com.toby.report.entity.PurchasesByCategoriesOfItemsBean;
import com.toby.toby.BaseInventoryReportBean;
import com.toby.toby.UserData;
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
import org.apache.commons.collections.ListUtils;

/**
 *
 * @author ahmed
 */
@Named("purchasesByCategoriesOfItemsMB")
@ViewScoped
public class PurchasesByCategoriesOfItemsMB extends BaseInventoryReportBean {

    private List<PurchaseItemsGroupView> purchaseItemsGroupViewList;
    private InvDelegatorSalesByMainItemsGroupSearchBean delegatorSalesByMainItemsGroupSearchBean;
    private PurchaseItemsGroupViewSearchBean purchaseItemsGroupViewSearchBean;
    private List<PurchasesByCategoriesOfItemsBean> purchaseByCategoriesOfItemsBeanList;
    private List<PurchasesByCategoriesOfItemsBean> purchaseByCategoriesOfItemsBeanForChartList;
    private List<PurchasesByCategoriesOfItemsBean> purchaseByCategoriesOfItemsOrderedAscendedBeanList;
    private Map<Integer, PurchasesByCategoriesOfItemsBean> purchaseItemBeanMap = new HashMap<>();
    private Map<Date, String> purchaseItemsByDateTreeMap = new TreeMap<>();
    private Map<String, Integer> purchaseItemsByMonthsNumbersOrderedTreeMap = new TreeMap<>();
    private Map<String, Integer> purchaseItemsOrderedAscendinTreeMap = new TreeMap<>();
    private String columnTemplate;
    private List<ColumnModel> columns;
    private Map<String, BigDecimal> totalValuesMap;
    private HashMap hashMap;
    private Integer parameterNumber;
    private String url;
    private List<Object> pieChartsRows;
//    private Boolean chart = false;

    @EJB
    private PurchaseItemsGroupViewService purchaseItemsGroupViewService;

    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        reset();
        super.load();
        load();
        createDynamicColumns();
    }

    @Override
    public void load() {
        url = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
        getInvItemByBranch();
        getInventoryListByBranch();
        if (url.contains("Sales")) {
            getInvoiceSalesListByBranch();
        } else {
            getInvoicePurchaseListByBranch();
        }
    }

    @Override
    public void reset() {
        purchaseItemsGroupViewList = new ArrayList<>();
        purchaseItemsGroupViewSearchBean = new PurchaseItemsGroupViewSearchBean();
        purchaseByCategoriesOfItemsBeanList = new ArrayList<>();
        purchaseItemBeanMap = new HashMap<>();
        purchaseItemsByDateTreeMap = new TreeMap<>();
        purchaseItemsOrderedAscendinTreeMap = new TreeMap<>();
        purchaseItemsByMonthsNumbersOrderedTreeMap = new TreeMap<>();
        purchaseByCategoriesOfItemsOrderedAscendedBeanList = new ArrayList<>();
        totalValuesMap = new HashMap<>();
        purchaseByCategoriesOfItemsBeanForChartList = new ArrayList<>();

    }

    public void searchIntialize() {
        purchaseItemsGroupViewList = new ArrayList<>();
        purchaseByCategoriesOfItemsBeanList = new ArrayList<>();
        purchaseByCategoriesOfItemsOrderedAscendedBeanList = new ArrayList<>();
        purchaseItemsByMonthsNumbersOrderedTreeMap = new TreeMap<>();
        purchaseItemsByDateTreeMap = new TreeMap<>();
        purchaseItemsOrderedAscendinTreeMap = new TreeMap<>();
        totalValuesMap = new HashMap<>();
        purchaseItemBeanMap = new HashMap<>();
        purchaseByCategoriesOfItemsBeanForChartList = new ArrayList<>();

    }

    @Override
    public void search() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        searchIntialize();
        purchaseItemsGroupViewSearchBean.setBranchId(getUserData().getUserBranch().getId());
        if (url.contains("Sales")) {
            purchaseItemsGroupViewSearchBean.setType(Boolean.TRUE);
        } else {
            purchaseItemsGroupViewSearchBean.setType(Boolean.FALSE);
        }
        purchaseItemsGroupViewList = purchaseItemsGroupViewService.findAllPurchaseItemsGroup(purchaseItemsGroupViewSearchBean);
        prepareMaps();
        PurchasesByCategoriesOfItemsBean lastItemsBean = new PurchasesByCategoriesOfItemsBean();

        if (purchaseItemsGroupViewList != null && !purchaseItemsGroupViewList.isEmpty()) {
            for (PurchaseItemsGroupView view : purchaseItemsGroupViewList) {
                if (purchaseItemBeanMap.containsKey(view.getGroupId())) {
                    PurchasesByCategoriesOfItemsBean itemsBean = purchaseItemBeanMap.get(view.getGroupId());
                    itemsBean.setGroup_name(view.getGroupName());
                    String dateFormat = new SimpleDateFormat("yyyy-MM").format(view.getDate());
                    if (purchaseItemsByDateTreeMap.containsKey(view.getDate())) {
                        try {
                            //thsi block of code get the field and make it accessible thin get the existing value from that specefied field and added to it the the previous values
                            String fieldName = "month_".concat(purchaseItemsOrderedAscendinTreeMap.get(dateFormat).toString());
                            Field field = PurchasesByCategoriesOfItemsBean.class.getDeclaredField(fieldName);
                            field.setAccessible(true);
                            BigDecimal monthValue = (BigDecimal) field.get(itemsBean);
                            monthValue = (monthValue != null ? monthValue : BigDecimal.ZERO).add(view.getNet() != null ? view.getNet() : BigDecimal.ZERO);
                            field.set(itemsBean, monthValue);

                            //calculate the total to sum the values horizontally  
                            itemsBean.setTotal((itemsBean.getTotal() != null ? itemsBean.getTotal() : BigDecimal.ZERO).add(view.getNet() != null ? view.getNet() : BigDecimal.ZERO));
                            purchaseItemBeanMap.put(view.getGroupId(), itemsBean);

                            //calculate the total vertically 
                            BigDecimal total = totalValuesMap.get(dateFormat);
                            total = (total != null ? total : BigDecimal.ZERO).add(view.getNet() != null ? view.getNet() : BigDecimal.ZERO);
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
                    String fieldName = "month_".concat(purchaseItemsOrderedAscendinTreeMap.get(entry.getKey()).toString());
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
        purchaseByCategoriesOfItemsBeanList = new ArrayList<>(purchaseItemBeanMap.values());
        purchaseByCategoriesOfItemsBeanList.add(lastItemsBean);
        UIComponent table = FacesContext.getCurrentInstance().getViewRoot().findComponent(":form:table");
        table.setValueExpression("sortBy", null);

        try {
            //start to fill array to draw the chart
            drawLineChart();
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            Logger.getLogger(PurchasesByCategoriesOfItemsMB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void drawLineChart() throws NoSuchFieldException, IllegalAccessException {
        Map<Integer, List<Object>> transposeMap = new TreeMap<>();
        PurchasesByCategoriesOfItemsBean additionalItemsBeanForChart;
        List<PurchasesByCategoriesOfItemsBean> additionalItemsBeanForChartList = new ArrayList<>();
        for (PurchasesByCategoriesOfItemsBean itemsBean : purchaseByCategoriesOfItemsBeanList) {

            if (!itemsBean.equals(purchaseByCategoriesOfItemsBeanList.get(purchaseByCategoriesOfItemsBeanList.size() - 1))) {
                if (transposeMap.containsKey(0)) {
                    List<Object> object = transposeMap.get(0);
                    object.add("'" + itemsBean.getGroup_name() + "'");
                } else {
                    List<Object> object = new ArrayList<>();
                    object.add("'" + "Months" + "'");
                    object.add("'" + itemsBean.getGroup_name() + "'");
                    transposeMap.put(0, object);
                }

                for (Map.Entry<String, Integer> entry : purchaseItemsOrderedAscendinTreeMap.entrySet()) {
                    additionalItemsBeanForChart = new PurchasesByCategoriesOfItemsBean();
                    additionalItemsBeanForChart.setGroupChartName(itemsBean.getGroup_name());
                    if (transposeMap.containsKey(entry.getValue())) {
                        List<Object> object = transposeMap.get(entry.getValue());
                        String fieldName = "month_".concat(purchaseItemsOrderedAscendinTreeMap.get(entry.getKey()).toString());
                        try {
                            Field field = PurchasesByCategoriesOfItemsBean.class.getDeclaredField(fieldName);
                            field.setAccessible(true);
                            BigDecimal monthValue = (BigDecimal) field.get(itemsBean);
                            object.add(monthValue != null ? monthValue : BigDecimal.ZERO);
                            transposeMap.put(entry.getValue(), object);
                            additionalItemsBeanForChart.setMonthName(entry.getKey());
                            additionalItemsBeanForChart.setMonthValue(monthValue);
                        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                            Logger.getLogger(PurchasesByCategoriesOfItemsMB.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        List<Object> object = new ArrayList<>();
                        object.add("'" + entry.getKey() + "'");

                        String fieldName = "month_".concat(purchaseItemsOrderedAscendinTreeMap.get(entry.getKey()).toString());
                        try {
                            Field field = PurchasesByCategoriesOfItemsBean.class.getDeclaredField(fieldName);
                            field.setAccessible(true);
                            BigDecimal monthValue = (BigDecimal) field.get(itemsBean);
                            object.add(monthValue != null ? monthValue : BigDecimal.ZERO);
                            transposeMap.put(entry.getValue(), object);
                            additionalItemsBeanForChart.setMonthName(entry.getKey());
                            additionalItemsBeanForChart.setMonthValue(monthValue);
                        } catch (SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                            Logger.getLogger(PurchasesByCategoriesOfItemsMB.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        transposeMap.put(entry.getValue(), object);
                    }
                    additionalItemsBeanForChartList.add(additionalItemsBeanForChart);
                }
            }
        }

        pieChartsRows = new ArrayList<>(transposeMap.values());
        for (Object row : pieChartsRows) {
            System.out.println(row);
        }
        purchaseByCategoriesOfItemsBeanForChartList = ListUtils.union(purchaseByCategoriesOfItemsBeanList, additionalItemsBeanForChartList);
    }

    public void prepareMaps() {
        // create map has group id and new objects of months class will be filled later for each corresponding month
        for (PurchaseItemsGroupView itemsGroupView : purchaseItemsGroupViewList) {
            if (purchaseItemBeanMap != null && !purchaseItemBeanMap.containsKey(itemsGroupView.getGroupId())) {
                purchaseItemBeanMap.put(itemsGroupView.getGroupId(), new PurchasesByCategoriesOfItemsBean());
            }
            // another map contains date as a key and month+year as a value will help us to determine number of months+date (ex:02-2019)
            String dateFormat = new SimpleDateFormat("yyyy-MM").format(itemsGroupView.getDate());
            if (purchaseItemsByDateTreeMap != null && !purchaseItemsByDateTreeMap.containsKey(itemsGroupView.getDate())) {
                purchaseItemsByDateTreeMap.put(itemsGroupView.getDate(), dateFormat);
            }
        }
        //purchaseItemsByMonthsNumbersOrderedTreeMap this map help us to determine the number of months that will be appended to the field name (ex month_(appended number of map)) )  
        // another map prepared with 0 value corresponding with months+year and will be filled during calculates the totals
        for (Date entry : purchaseItemsByDateTreeMap.keySet()) {
            String dateMonthFormat = new SimpleDateFormat("MM").format(entry);
            String dateMonthYearFormat = new SimpleDateFormat("yyyy-MM").format(entry);
            if (purchaseItemsByMonthsNumbersOrderedTreeMap != null && !purchaseItemsByMonthsNumbersOrderedTreeMap.containsValue(Integer.parseInt(dateMonthFormat))) {
                purchaseItemsByMonthsNumbersOrderedTreeMap.put(dateMonthYearFormat, Integer.parseInt(dateMonthFormat));
                totalValuesMap.put(dateMonthYearFormat, BigDecimal.ZERO);
                //second else if to check if this months repeated but with the next year if yes 12 summed with that month 
                // ex : if month 2 exist with year 2018 and found again but with year 2019 summed 12 on 2 to be 14 and that number will be the number appended to the field
            } else if (purchaseItemsByMonthsNumbersOrderedTreeMap.containsValue(Integer.parseInt(dateMonthFormat)) && !purchaseItemsByMonthsNumbersOrderedTreeMap.containsKey(dateMonthYearFormat)) {
                Integer x = Integer.parseInt(dateMonthFormat);
                purchaseItemsByMonthsNumbersOrderedTreeMap.put(dateMonthYearFormat, x + 12);
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
        for (Map.Entry<String, Integer> entry : purchaseItemsByMonthsNumbersOrderedTreeMap.entrySet()) {
            purchaseItemsOrderedAscendinTreeMap.put(entry.getKey(), initialNumber);
            initialNumber++;
        }
    }

    private void createDynamicColumns() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        parameterNumber = 1;
        hashMap = new HashMap();
        hashMap.put("organizationName", "اسم المجموعة");
        hashMap.put("total", "الاجمالي");

        columnTemplate = new String();
        columnTemplate = "group_name";
        if (purchaseItemsOrderedAscendinTreeMap != null) {
            for (Map.Entry<String, Integer> entry : purchaseItemsOrderedAscendinTreeMap.entrySet()) {
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
                columns.add(new ColumnModel(columnKey.toUpperCase(), "month_" + purchaseItemsOrderedAscendinTreeMap.get(columnKey)));
            }
        }
    }

    @Override
    public HashMap prepareReport() {
        if (purchaseItemsGroupViewSearchBean.getType()) {
            hashMap.put("reportNameText", userDDs.get("FROM_ITEM"));
        } else {
            hashMap.put("reportNameText", userDDs.get("MONTLY_PUR_REP"));
        }
        hashMap.put("PrintedBy", getUserData().getUser().getName());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        //    hashMap.put("companyName", getUserData().getCompany().getName());
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        hashMap.put("itemFromText", userDDs.get("FROM_ITEM"));
        hashMap.put("itemToText", userDDs.get("TO_ITEM"));
        hashMap.put("dateFromText", userDDs.get("YEAR_FROM"));
        hashMap.put("dateTotText", userDDs.get("YEAR_TO"));
        hashMap.put("inventoryFromText", userDDs.get("FROM_REPOS"));
        hashMap.put("inventoryToText", userDDs.get("TO_REPOS"));
        hashMap.put("invoiceNumberFromText", userDDs.get("FROM_INVOI_NO"));
        hashMap.put("invoiceNumberToText", userDDs.get("TO_INVOI_NO"));

        hashMap.put("itemFromValue", purchaseItemsGroupViewSearchBean.getInvItemForm() != null ? purchaseItemsGroupViewSearchBean.getInvItemForm().getName() : null);
        hashMap.put("itemToValue", purchaseItemsGroupViewSearchBean.getInvItemTo() != null ? purchaseItemsGroupViewSearchBean.getInvItemTo().getName() : null);
        hashMap.put("dateFromValue", purchaseItemsGroupViewSearchBean.getDateFrom());
        hashMap.put("dateTotValue", purchaseItemsGroupViewSearchBean.getDateTo());
        hashMap.put("inventoryFromValue", purchaseItemsGroupViewSearchBean.getInventoryIdFrom() != null ? purchaseItemsGroupViewSearchBean.getInventoryIdFrom().getName() : null);
        hashMap.put("inventoryToValue", purchaseItemsGroupViewSearchBean.getInventoryIdTo() != null ? purchaseItemsGroupViewSearchBean.getInventoryIdTo().getName() : null);
        hashMap.put("invoiceNumberFromValue", purchaseItemsGroupViewSearchBean.getSalesInvoiceFrom() != null ? purchaseItemsGroupViewSearchBean.getSalesInvoiceFrom().getSerial() : null);
        hashMap.put("invoiceNumberToValue", purchaseItemsGroupViewSearchBean.getSalesInvoiceTo() != null ? purchaseItemsGroupViewSearchBean.getSalesInvoiceTo().getSerial() : null);

        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (purchaseByCategoriesOfItemsBeanForChartList != null && !purchaseByCategoriesOfItemsBeanForChartList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "purchaseByCategoriesOfItemsReportLS.jasper", purchaseByCategoriesOfItemsBeanForChartList, "pdf");
        }
    }

     public void exportChartPDF(ActionEvent actionEvent) throws JRException, IOException {
       // search();
        if (purchaseByCategoriesOfItemsBeanForChartList != null && !purchaseByCategoriesOfItemsBeanForChartList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "purchaseByCategoriesOfItemsReportLSWithLineChart.jasper", purchaseByCategoriesOfItemsBeanForChartList, "pdf");
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
     * @return the purchaseItemsGroupViewSearchBean
     */
    public PurchaseItemsGroupViewSearchBean getPurchaseItemsGroupViewSearchBean() {
        return purchaseItemsGroupViewSearchBean;
    }

    /**
     * @param purchaseItemsGroupViewSearchBean the
     * purchaseItemsGroupViewSearchBean to set
     */
    public void setPurchaseItemsGroupViewSearchBean(PurchaseItemsGroupViewSearchBean purchaseItemsGroupViewSearchBean) {
        this.purchaseItemsGroupViewSearchBean = purchaseItemsGroupViewSearchBean;
    }

    /**
     * @return the purchaseItemsGroupViewList
     */
    public List<PurchaseItemsGroupView> getPurchaseItemsGroupViewList() {
        return purchaseItemsGroupViewList;
    }

    /**
     * @param purchaseItemsGroupViewList the purchaseItemsGroupViewList to set
     */
    public void setPurchaseItemsGroupViewList(List<PurchaseItemsGroupView> purchaseItemsGroupViewList) {
        this.purchaseItemsGroupViewList = purchaseItemsGroupViewList;
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
     * @return the purchaseItemBeanMap
     */
    public Map<Integer, PurchasesByCategoriesOfItemsBean> getPurchaseItemBeanMap() {
        return purchaseItemBeanMap;
    }

    /**
     * @param purchaseItemBeanMap the purchaseItemBeanMap to set
     */
    public void setPurchaseItemBeanMap(Map<Integer, PurchasesByCategoriesOfItemsBean> purchaseItemBeanMap) {
        this.purchaseItemBeanMap = purchaseItemBeanMap;
    }

    /**
     * @return the purchaseItemsByDateTreeMap
     */
    public Map<Date, String> getPurchaseItemsByDateTreeMap() {
        return purchaseItemsByDateTreeMap;
    }

    /**
     * @param purchaseItemsByDateTreeMap the purchaseItemsByDateTreeMap to set
     */
    public void setPurchaseItemsByDateTreeMap(Map<Date, String> purchaseItemsByDateTreeMap) {
        this.purchaseItemsByDateTreeMap = purchaseItemsByDateTreeMap;
    }

    /**
     * @return the purchaseItemsByMonthsNumbersOrderedTreeMap
     */
    public Map<String, Integer> getPurchaseItemsByMonthsNumbersOrderedTreeMap() {
        return purchaseItemsByMonthsNumbersOrderedTreeMap;
    }

    /**
     * @param purchaseItemsByMonthsNumbersOrderedTreeMap the
     * purchaseItemsByMonthsNumbersOrderedTreeMap to set
     */
    public void setPurchaseItemsByMonthsNumbersOrderedTreeMap(Map<String, Integer> purchaseItemsByMonthsNumbersOrderedTreeMap) {
        this.purchaseItemsByMonthsNumbersOrderedTreeMap = purchaseItemsByMonthsNumbersOrderedTreeMap;
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
     * @return the purchaseItemsOrderedAscendinTreeMap
     */
    public Map<String, Integer> getPurchaseItemsOrderedAscendinTreeMap() {
        return purchaseItemsOrderedAscendinTreeMap;
    }

    /**
     * @param purchaseItemsOrderedAscendinTreeMap the
     * purchaseItemsOrderedAscendinTreeMap to set
     */
    public void setPurchaseItemsOrderedAscendinTreeMap(Map<String, Integer> purchaseItemsOrderedAscendinTreeMap) {
        this.purchaseItemsOrderedAscendinTreeMap = purchaseItemsOrderedAscendinTreeMap;
    }

    /**
     * @return the purchaseByCategoriesOfItemsOrderedAscendedBeanList
     */
    public List<PurchasesByCategoriesOfItemsBean> getPurchaseByCategoriesOfItemsOrderedAscendedBeanList() {
        return purchaseByCategoriesOfItemsOrderedAscendedBeanList;
    }

    /**
     * @param purchaseByCategoriesOfItemsOrderedAscendedBeanList the
     * purchaseByCategoriesOfItemsOrderedAscendedBeanList to set
     */
    public void setPurchaseByCategoriesOfItemsOrderedAscendedBeanList(List<PurchasesByCategoriesOfItemsBean> purchaseByCategoriesOfItemsOrderedAscendedBeanList) {
        this.purchaseByCategoriesOfItemsOrderedAscendedBeanList = purchaseByCategoriesOfItemsOrderedAscendedBeanList;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the pieChartsRows
     */
    public List<Object> getPieChartsRows() {
        return pieChartsRows;
    }

    /**
     * @param pieChartsRows the pieChartsRows to set
     */
    public void setPieChartsRows(List<Object> pieChartsRows) {
        this.pieChartsRows = pieChartsRows;
    }

    /**
     * @return the purchaseByCategoriesOfItemsBeanForChartList
     */
    public List<PurchasesByCategoriesOfItemsBean> getPurchaseByCategoriesOfItemsBeanForChartList() {
        return purchaseByCategoriesOfItemsBeanForChartList;
    }

    /**
     * @param purchaseByCategoriesOfItemsBeanForChartList the
     * purchaseByCategoriesOfItemsBeanForChartList to set
     */
    public void setPurchaseByCategoriesOfItemsBeanForChartList(List<PurchasesByCategoriesOfItemsBean> purchaseByCategoriesOfItemsBeanForChartList) {
        this.purchaseByCategoriesOfItemsBeanForChartList = purchaseByCategoriesOfItemsBeanForChartList;
    }

    /*
    public Boolean getChart() {
        return chart;
    }

    public void setChart(Boolean chart) {
        this.chart = chart;
    }*/

    private static class userDDs {

        private static Object get(String year_from) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public userDDs() {
        }
    }
}
