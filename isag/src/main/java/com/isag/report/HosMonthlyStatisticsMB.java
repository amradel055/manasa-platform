/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.HosDoctorService;
import com.isag.businessservice.reports.HosClinicViewService;
import com.isag.businessservice.reports.searchBean.HosDoctorSearchBean;
import com.isag.converter.HosDoctorConverter;
import com.isag.entity.HosDoctor;
import com.isag.isag.BaseReportBean;
import com.isag.isag.UserData;
import com.isag.report.entity.PurchasesByCategoriesOfItemsBean;
import com.isag.views.HosClinicView;
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
@Named("hosMonthlyStatisticsMB")
@ViewScoped
public class HosMonthlyStatisticsMB extends BaseReportBean {

    private List<HosDoctor> hosClinicsList;
    private HosDoctorConverter hosClinicConverter;
    private Map<Integer, PurchasesByCategoriesOfItemsBean> hosClinicBeanMap = new HashMap<>();
    private Map<Date, String> hosClinicByDateTreeMap = new TreeMap<>();
    private Map<String, Integer> hosClinicByMonthsNumbersOrderedTreeMap = new TreeMap<>();
    private Map<String, Integer> hosClinicOrderedAscendingTreeMap = new TreeMap<>();
    private HosDoctorSearchBean hosDoctorSearchBean;
    private List<HosClinicView> hosClinicViewList;
    private Map<String, BigDecimal> totalValuesMap;

    private String uri;

    private HashMap hashMap;
    private Integer parameterNumber;
    private String columnTemplate;
    private List<ColumnModel> columns;
    private List<PurchasesByCategoriesOfItemsBean> purchaseByCategoriesOfItemsBeanList;
    @EJB
    private HosDoctorService hosDoctorService;
    @EJB
    private HosClinicViewService hosClinicViewService;

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
        hosClinicsList = hosDoctorService.getAllSubHosDoctorByBranchIdActiveAndLevel(getUserData().getUserBranch().getId(), 1);
        hosClinicConverter = new HosDoctorConverter(hosClinicsList);
    }

    @Override
    public void reset() {
        hosClinicViewList = new ArrayList<>();
        hosClinicBeanMap = new HashMap<>();
        hosClinicOrderedAscendingTreeMap = new TreeMap<>();
        hosClinicByMonthsNumbersOrderedTreeMap = new TreeMap<>();
        hosClinicByDateTreeMap = new TreeMap<>();
        totalValuesMap = new HashMap<>();
        hosDoctorSearchBean = new HosDoctorSearchBean();
        purchaseByCategoriesOfItemsBeanList = new ArrayList<>();

    }

    public void searchIntialize() {
        hosClinicViewList = new ArrayList<>();
        hosClinicBeanMap = new HashMap<>();
        hosClinicOrderedAscendingTreeMap = new TreeMap<>();
        hosClinicByMonthsNumbersOrderedTreeMap = new TreeMap<>();
        hosClinicByDateTreeMap = new TreeMap<>();
        totalValuesMap = new HashMap<>();
        purchaseByCategoriesOfItemsBeanList = new ArrayList<>();

    }

    @Override
    public void search() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        searchIntialize();
        hosDoctorSearchBean.setBranchId(getUserData().getUserBranch().getId());
        hosClinicViewList = hosClinicViewService.getHosClinicViewBySearchBean(hosDoctorSearchBean);

        prepareMaps();
        PurchasesByCategoriesOfItemsBean lastItemsBean = new PurchasesByCategoriesOfItemsBean();

        if (hosClinicViewList != null && !hosClinicViewList.isEmpty()) {
            for (HosClinicView view : hosClinicViewList) {
                if (hosClinicBeanMap.containsKey(view.getHosClinicId())) {
                    PurchasesByCategoriesOfItemsBean itemsBean = hosClinicBeanMap.get(view.getHosClinicId());
                    itemsBean.setGroup_name(view.getClinicName());
                    String dateFormat = new SimpleDateFormat("yyyy-MM").format(view.getDate());
                    if (hosClinicByDateTreeMap.containsKey(view.getDate())) {
                        try {
                            //thsi block of code get the field and make it accessible thin get the existing value from that specefied field and added to it the the previous values
                            String fieldName = "month_".concat(hosClinicOrderedAscendingTreeMap.get(dateFormat).toString());
                            Field field = PurchasesByCategoriesOfItemsBean.class.getDeclaredField(fieldName);
                            field.setAccessible(true);
                            BigDecimal monthValue = (BigDecimal) field.get(itemsBean);
                            monthValue = (monthValue != null ? monthValue : BigDecimal.ZERO).add(view.getValue() != null ? view.getValue() : BigDecimal.ZERO);
                            field.set(itemsBean, monthValue);

                            //calculate the total to sum the values horizontally  
                            itemsBean.setTotal((itemsBean.getTotal() != null ? itemsBean.getTotal() : BigDecimal.ZERO).add(view.getValue() != null ? view.getValue() : BigDecimal.ZERO));
                            hosClinicBeanMap.put(view.getHosClinicId(), itemsBean);

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
                    String fieldName = "month_".concat(hosClinicOrderedAscendingTreeMap.get(entry.getKey()).toString());
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
        purchaseByCategoriesOfItemsBeanList = new ArrayList<>(hosClinicBeanMap.values());
        purchaseByCategoriesOfItemsBeanList.add(lastItemsBean);
        UIComponent table = FacesContext.getCurrentInstance().getViewRoot().findComponent(":form:table");
        table.setValueExpression("sortBy", null);
    }

    public void prepareMaps() {
        // create map has group id and new objects of months class will be filled later for each corresponding month
        for (HosClinicView clinicView : hosClinicViewList) {
            if (hosClinicBeanMap != null && !hosClinicBeanMap.containsKey(clinicView.getHosClinicId())) {
                hosClinicBeanMap.put(clinicView.getHosClinicId(), new PurchasesByCategoriesOfItemsBean());
            }
            // another map contains date as a key and month+year as a value will help us to determine number of months+date (ex:02-2019)
            String dateFormat = new SimpleDateFormat("yyyy-MM").format(clinicView.getDate());
            if (hosClinicByDateTreeMap != null && !hosClinicByDateTreeMap.containsKey(clinicView.getDate())) {
                hosClinicByDateTreeMap.put(clinicView.getDate(), dateFormat);
            }
        }

        for (Date entry : hosClinicByDateTreeMap.keySet()) {
            String dateMonthFormat = new SimpleDateFormat("MM").format(entry);
            String dateMonthYearFormat = new SimpleDateFormat("yyyy-MM").format(entry);
            if (hosClinicByMonthsNumbersOrderedTreeMap != null && !hosClinicByMonthsNumbersOrderedTreeMap.containsValue(Integer.parseInt(dateMonthFormat))) {
                hosClinicByMonthsNumbersOrderedTreeMap.put(dateMonthYearFormat, Integer.parseInt(dateMonthFormat));
                totalValuesMap.put(dateMonthYearFormat, BigDecimal.ZERO);
                //second else if to check if this months repeated but with the next year if yes 12 summed with that month 
                // ex : if month 2 exist with year 2018 and found again but with year 2019 summed 12 on 2 to be 14 and that number will be the number appended to the field
            } else if (hosClinicByMonthsNumbersOrderedTreeMap.containsValue(Integer.parseInt(dateMonthFormat)) && !hosClinicByMonthsNumbersOrderedTreeMap.containsKey(dateMonthYearFormat)) {
                Integer x = Integer.parseInt(dateMonthFormat);
                hosClinicByMonthsNumbersOrderedTreeMap.put(dateMonthYearFormat, x + 12);
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
        for (Map.Entry<String, Integer> entry : hosClinicByMonthsNumbersOrderedTreeMap.entrySet()) {
            hosClinicOrderedAscendingTreeMap.put(entry.getKey(), initialNumber);
            initialNumber++;
        }
    }

    private void createDynamicColumns() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        parameterNumber = 1;
        hashMap = new HashMap();
        hashMap.put("organizationName", userDDs.get("CLINICS"));
        hashMap.put("total", userDDs.get("TOTAL"));

        columnTemplate = new String();
        columnTemplate = "group_name";
        if (hosClinicOrderedAscendingTreeMap != null) {
            for (Map.Entry<String, Integer> entry : hosClinicOrderedAscendingTreeMap.entrySet()) {
                String columnName = entry.getKey();
                columnTemplate = columnTemplate.concat(" " + columnName);
                hashMap.put("parameter_" + parameterNumber.toString(), columnName);
                parameterNumber++;
            }
        }
        columnTemplate = columnTemplate.concat(" total");
        String[] columnKeys = columnTemplate.split(" ");
        columns = new ArrayList<>();

        for (String columnKey : columnKeys) {
            if (columnKey.contains("group_name") || columnKey.contains("total")) {
                columns.add(new ColumnModel(userDDs.get(columnKey.toUpperCase()), columnKey));
            } else {
                columns.add(new ColumnModel(columnKey.toUpperCase(), "month_" + hosClinicOrderedAscendingTreeMap.get(columnKey)));
            }
        }
    }

    @Override
    public HashMap prepareReport() {

        hashMap.put("reportNameText", userDDs.get("MONTHLY_STAT"));

        hashMap.put("PrintedBy", getUserData().getUser().getName());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        //    hashMap.put("companyName", getUserData().getCompany().getName());
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        hashMap.put("itemFromText", userDDs.get("FROM_CLINIC"));
        hashMap.put("itemToText", userDDs.get("TO_CLINIC"));
        hashMap.put("dateFromText", userDDs.get("YEAR_FROM"));
        hashMap.put("dateTotText", userDDs.get("YEAR_TO"));

//        hashMap.put("inventoryFromText", "من مستودع");
//        hashMap.put("inventoryToText", "الى مستودع");
//        hashMap.put("invoiceNumberFromText", "من رقم فاتورة");
//        hashMap.put("invoiceNumberToText", "الى رقم فاتورة");

        hashMap.put("itemFromValue", hosDoctorSearchBean.getHosDoctorIdFrom() != null ? hosDoctorSearchBean.getHosDoctorIdFrom().getName() : null);
        hashMap.put("itemToValue", hosDoctorSearchBean.getHosDoctorIdTo() != null ? hosDoctorSearchBean.getHosDoctorIdTo().getName() : null);
        hashMap.put("dateFromValue", hosDoctorSearchBean.getDateFrom());
        hashMap.put("dateTotValue", hosDoctorSearchBean.getDateTo());
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

    public List<HosDoctor> completeClinicHosDoctor(String query) {
        List<HosDoctor> doctorsList = hosClinicsList;
        if (query == null || query.trim().equals("")) {

            hosClinicConverter = new HosDoctorConverter(doctorsList);
            return doctorsList;
        }
        List<HosDoctor> filteredHosDoctors = new ArrayList<>();

        String nameAr;
        String code;
        HosDoctor hosDoctorFilter;
        for (int i = 0; i < hosClinicsList.size(); i++) {
            hosDoctorFilter = doctorsList.get(i);
            nameAr = hosDoctorFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredHosDoctors.contains(hosDoctorFilter)) {
                    filteredHosDoctors.add(hosDoctorFilter);
                }
            }

            code = hosDoctorFilter.getCode();
            if (code != null && String.valueOf(code).contains(query.toLowerCase())) {
                if (!filteredHosDoctors.contains(hosDoctorFilter)) {
                    filteredHosDoctors.add(hosDoctorFilter);
                }
            }
        }

        hosClinicConverter = new HosDoctorConverter(filteredHosDoctors);
        return filteredHosDoctors;
    }

    /**
     * @return the hosClinicsList
     */
    public List<HosDoctor> getHosClinicsList() {
        return hosClinicsList;
    }

    /**
     * @param hosClinicsList the hosClinicsList to set
     */
    public void setHosClinicsList(List<HosDoctor> hosClinicsList) {
        this.hosClinicsList = hosClinicsList;
    }

    /**
     * @return the hosClinicConverter
     */
    public HosDoctorConverter getHosClinicConverter() {
        return hosClinicConverter;
    }

    /**
     * @param hosClinicConverter the hosClinicConverter to set
     */
    public void setHosClinicConverter(HosDoctorConverter hosClinicConverter) {
        this.hosClinicConverter = hosClinicConverter;
    }

    /**
     * @return the hosClinicBeanMap
     */
    public Map<Integer, PurchasesByCategoriesOfItemsBean> getHosClinicBeanMap() {
        return hosClinicBeanMap;
    }

    /**
     * @param hosClinicBeanMap the hosClinicBeanMap to set
     */
    public void setHosClinicBeanMap(Map<Integer, PurchasesByCategoriesOfItemsBean> hosClinicBeanMap) {
        this.hosClinicBeanMap = hosClinicBeanMap;
    }

    /**
     * @return the hosClinicByDateTreeMap
     */
    public Map<Date, String> getHosClinicByDateTreeMap() {
        return hosClinicByDateTreeMap;
    }

    /**
     * @param hosClinicByDateTreeMap the hosClinicByDateTreeMap to set
     */
    public void setHosClinicByDateTreeMap(Map<Date, String> hosClinicByDateTreeMap) {
        this.hosClinicByDateTreeMap = hosClinicByDateTreeMap;
    }

    /**
     * @return the hosClinicByMonthsNumbersOrderedTreeMap
     */
    public Map<String, Integer> getHosClinicByMonthsNumbersOrderedTreeMap() {
        return hosClinicByMonthsNumbersOrderedTreeMap;
    }

    /**
     * @param hosClinicByMonthsNumbersOrderedTreeMap the
     * hosClinicByMonthsNumbersOrderedTreeMap to set
     */
    public void setHosClinicByMonthsNumbersOrderedTreeMap(Map<String, Integer> hosClinicByMonthsNumbersOrderedTreeMap) {
        this.hosClinicByMonthsNumbersOrderedTreeMap = hosClinicByMonthsNumbersOrderedTreeMap;
    }

    /**
     * @return the hosClinicOrderedAscendingTreeMap
     */
    public Map<String, Integer> getHosClinicOrderedAscendingTreeMap() {
        return hosClinicOrderedAscendingTreeMap;
    }

    /**
     * @param hosClinicOrderedAscendingTreeMap the
     * hosClinicOrderedAscendingTreeMap to set
     */
    public void setHosClinicOrderedAscendingTreeMap(Map<String, Integer> hosClinicOrderedAscendingTreeMap) {
        this.hosClinicOrderedAscendingTreeMap = hosClinicOrderedAscendingTreeMap;
    }

    /**
     * @return the hosDoctorSearchBean
     */
    public HosDoctorSearchBean getHosDoctorSearchBean() {
        return hosDoctorSearchBean;
    }

    /**
     * @param hosDoctorSearchBean the hosDoctorSearchBean to set
     */
    public void setHosDoctorSearchBean(HosDoctorSearchBean hosDoctorSearchBean) {
        this.hosDoctorSearchBean = hosDoctorSearchBean;
    }

    /**
     * @return the hosClinicViewList
     */
    public List<HosClinicView> getHosClinicViewList() {
        return hosClinicViewList;
    }

    /**
     * @param hosClinicViewList the hosClinicViewList to set
     */
    public void setHosClinicViewList(List<HosClinicView> hosClinicViewList) {
        this.hosClinicViewList = hosClinicViewList;
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

    private static class userDDs {

        private static Object get(String year_from) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public userDDs() {
        }
    }

}
