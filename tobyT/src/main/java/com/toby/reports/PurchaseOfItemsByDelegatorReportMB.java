/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.reports;

import com.toby.businessservice.report.PurchaseOfItemsByDelegatorReportService;
import com.toby.businessservice.InvDelegatorService;
import com.toby.businessservice.InvItemService;
import com.toby.businessservice.reports.searchBean.PurchaseOfItemsByDelegatorSearchBean;
import com.toby.entity.InventoryDelegator;
import com.toby.report.entity.PurchaseOfItemsByDelegatorViewBean;
import com.toby.toby.BaseInventoryReportBean;
import com.toby.views.ItemSalesView;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.collections.ListUtils;

/**
 *
 * @author hhhh
 */
@Named("purchaseOfItemsByDelegatorReportMB")
@ViewScoped
public class PurchaseOfItemsByDelegatorReportMB extends BaseInventoryReportBean {

    Boolean type;
    private Integer level;

    // Objects
    private Integer branchId;
    private String uri;
    private String screenMode;

    BigDecimal balance = BigDecimal.ZERO;
    private Boolean isSales;

    // Bean Objs and Lists
    private List<PurchaseOfItemsByDelegatorViewBean> purchaseOfItemsByDelegatorViewBeanList;
    private List<PurchaseOfItemsByDelegatorViewBean> purchaseOfItemsByDelegatorViewBeanShortList;
    private List<PurchaseOfItemsByDelegatorViewBean> purchaseOfItemsByDelegatorViewBeanTempList;
    private PurchaseOfItemsByDelegatorSearchBean purchaseOfItemsByDelegatorSearchBean;
    private List<ItemSalesView> itemSalesViewList;

    // EJBs
    @EJB
    private InvItemService invItemService;
    @EJB
    private InvDelegatorService delegatorService;
    @EJB
    private PurchaseOfItemsByDelegatorReportService purchaseOfItemsByDelegatorReportService;
    //-----------------------
    private BigDecimal total = BigDecimal.ZERO;
    private Integer stripMapReference = 0;
    private BigDecimal repositoryBalance = BigDecimal.ZERO;
    private PurchaseOfItemsByDelegatorSearchBean purchaseOfItemsByDelegatorSearchViewBean;
    private PurchaseOfItemsByDelegatorViewBean purchaseOfItemsByDelegatorViewBean;
    private List<InventoryDelegator> delegatorRootList;
    private Map<Integer, List<ItemSalesView>> ItemSalesViewmap = new HashMap<>();
    private Map<Integer, PurchaseOfItemsByDelegatorViewBean> PurchaseOfItemsByDelegatorBeanMap = new HashMap<>();
    private TreeMap<Integer, PurchaseOfItemsByDelegatorViewBean> PurchaseOfItemsByDelegatorViewTreeMap = new TreeMap<>();
    private Map<Integer, ItemSalesView> ItemSalesViewGroupedByItemIdMap = new HashMap<>();
    private List<Object> pieChartsRows;
   // private Boolean chart = false;

    //-----------------------
    @Override
    @PostConstruct
    public void load() {

        setBranchId(getUserData().getUserBranch().getId());
        uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
        initObjs();
        super.load();
        fillLists();
        delegatorRootList = new ArrayList<>();
        if (isSales) {
            delegatorRootList = delegatorService.getSalesByBranch(getBranchId());
        } else {
            delegatorRootList = delegatorService.getPurchaseByBranch(getBranchId());
        }
        purchaseOfItemsByDelegatorViewBeanList = new ArrayList<>();
    }

    private void initObjs() {
        itemSalesViewList = new ArrayList<>();
        purchaseOfItemsByDelegatorSearchBean = new PurchaseOfItemsByDelegatorSearchBean();
        ItemSalesViewGroupedByItemIdMap = new HashMap<>();

        purchaseOfItemsByDelegatorSearchBean.setBranchId(getBranchId());
        purchaseOfItemsByDelegatorSearchBean.setShowReport(false);
        if (uri.contains("PurchaseOfItemsByDelegator")) {
            purchaseOfItemsByDelegatorSearchBean.setType(Boolean.FALSE);
            isSales = false;
        } else {
            purchaseOfItemsByDelegatorSearchBean.setType(true);
            isSales = true;
        }

        setItemSalesViewmap(new HashMap<>());
        PurchaseOfItemsByDelegatorViewTreeMap = new TreeMap<>();
        PurchaseOfItemsByDelegatorBeanMap = new HashMap<>();
        ItemSalesViewmap = new HashMap<>();

        purchaseOfItemsByDelegatorViewBeanList = new ArrayList<>();
        purchaseOfItemsByDelegatorViewBeanShortList = new ArrayList<>();
        purchaseOfItemsByDelegatorViewBeanTempList = new ArrayList<>();

    }

    private void fillLists() {
        getInventoryListByBranch();
        getInvItemByBranch();
        if (isSales) {
            //  invDelegatorList = delegatorService.getSalesByBranch(getBranchId()); // مبيعات
            // invSalesInvoiceList = invPurchaseInvoiceService.getALLInvPurchaseInvoicePostFlagedByBranchId(branchId, true); // فواتير المبيعات
            getDelegateSalesListByBranch();
            getInvoiceSalesListByBranch();
        } else {
            // invDelegatorList = delegatorService.getPurchaseByBranch(getBranchId()); //مشتريات
            // invSalesInvoiceList = invPurchaseInvoiceService.getALLInvPurchaseInvoicePostFlagedByBranchId(branchId, false); // فواتير المشتريات
            getDelegatePurchaseListByBranch();
            getInvoicePurchaseListByBranch();
        }

    }

    public void IntializeSearchLab() {
        itemSalesViewList = new ArrayList<>();
        ItemSalesViewmap = new HashMap<>();

        PurchaseOfItemsByDelegatorViewTreeMap = new TreeMap<>();
    }

    @Override
    public void reset() {
        initObjs();
        //   fillLists();
        //     purchaseOfItemsByDelegatorSearchBean = new PurchaseOfItemsByDelegatorSearchBean();
        //    purchaseOfItemsByDelegatorSearchViewBean = new PurchaseOfItemsByDelegatorSearchBean();
        //   itemSalesViewList = new ArrayList<>();

    }

    public void showItemReport() {
        itemSalesViewList = new ArrayList<>();
        search();
    }

    public void shortReportListing() {
        if (purchaseOfItemsByDelegatorSearchBean.getShortReport() != null) {
            if (purchaseOfItemsByDelegatorSearchBean.getShortReport()) {
                purchaseOfItemsByDelegatorViewBeanList = new ArrayList<>(purchaseOfItemsByDelegatorViewBeanShortList);
            } else {
                purchaseOfItemsByDelegatorViewBeanList = new ArrayList<>(purchaseOfItemsByDelegatorViewBeanTempList);
            }
        }
    }

    @Override
    public void search() {
        total = BigDecimal.ZERO;
        ItemSalesViewmap = new HashMap<>();
        PurchaseOfItemsByDelegatorBeanMap = new HashMap<>();
        purchaseOfItemsByDelegatorViewBeanList = new ArrayList<>();
        purchaseOfItemsByDelegatorViewBeanShortList = new ArrayList<>();
        purchaseOfItemsByDelegatorViewBeanTempList = new ArrayList<>();
        ItemSalesViewGroupedByItemIdMap = new HashMap<>();
        purchaseOfItemsByDelegatorSearchBean.setFrompaymentType(purchaseOfItemsByDelegatorSearchBean.getFrompaymentType() != null ? purchaseOfItemsByDelegatorSearchBean.getFrompaymentType() - 1 : null);
        purchaseOfItemsByDelegatorSearchBean.setTopaymentType(purchaseOfItemsByDelegatorSearchBean.getTopaymentType() != null ? purchaseOfItemsByDelegatorSearchBean.getTopaymentType() - 1 : null);
        if (isSales) {
            purchaseOfItemsByDelegatorSearchBean.setType(true);
        } else {
            purchaseOfItemsByDelegatorSearchBean.setType(false);
        }
        balance = BigDecimal.ZERO;

        purchaseOfItemsByDelegatorSearchViewBean = new PurchaseOfItemsByDelegatorSearchBean();

        purchaseOfItemsByDelegatorSearchBean.setBranchId(getUserData().getUserBranch().getId());

        itemSalesViewList = new ArrayList<>();

        itemSalesViewList = purchaseOfItemsByDelegatorReportService.getAllPurchaseOfItemsByDelegatorSearchBean(purchaseOfItemsByDelegatorSearchBean);
        if (itemSalesViewList != null && !itemSalesViewList.isEmpty()) {

            fillGroupMapWithCorrespondingItem();
            if (purchaseOfItemsByDelegatorSearchBean.getShowReport() != null && purchaseOfItemsByDelegatorSearchBean.getShowReport()) {
                fillItemIdMapByGroupId();
                fillGroupMapWithCorrespondingItem();
            }
            prepareRootGroupValues();
        }

        mergeGroupsWithItems();

        if (purchaseOfItemsByDelegatorSearchBean.getShortReport() != null) {
            shortReportListing();
        }
    }

    private void fillItemIdMapByGroupId() {

        itemSalesViewList = new ArrayList<>();
        for (Map.Entry<Integer, List<ItemSalesView>> entry : ItemSalesViewmap.entrySet()) {
            ItemSalesViewGroupedByItemIdMap = new HashMap<>();
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                for (ItemSalesView orgView : entry.getValue()) {
                    if (ItemSalesViewGroupedByItemIdMap.containsKey(orgView.getItemId())) {
                        ItemSalesView isv = ItemSalesViewGroupedByItemIdMap.get(orgView.getItemId());
                        isv.setNet((isv.getNet() != null ? isv.getNet() : BigDecimal.ZERO).add(orgView.getNet() != null ? orgView.getNet() : BigDecimal.ZERO));
                        isv.setItemQuantity((isv.getItemQuantity() != null ? isv.getItemQuantity() : BigDecimal.ZERO).add(orgView.getItemQuantity() != null ? orgView.getItemQuantity() : BigDecimal.ZERO));

                        ItemSalesViewGroupedByItemIdMap.put(orgView.getItemId(), isv);
                    } else {
                        ItemSalesViewGroupedByItemIdMap.put(orgView.getItemId(), orgView);
                    }
                }
                List<ItemSalesView> isvs = new ArrayList<>(ItemSalesViewGroupedByItemIdMap.values());
                itemSalesViewList = ListUtils.union(itemSalesViewList, isvs);
            }
        }
    }

    private void fillGroupMapWithCorrespondingItem() {
        ItemSalesViewmap = new HashMap<>();

        if (itemSalesViewList != null && !itemSalesViewList.isEmpty()) {
            for (ItemSalesView orgView : itemSalesViewList) {
                if (ItemSalesViewmap.containsKey(orgView.getDelegatorId())) {
                    List list = ItemSalesViewmap.get(orgView.getDelegatorId());
                    list.add(orgView);
                    ItemSalesViewmap.put(orgView.getDelegatorId(), list);
                } else {
                    List list = new ArrayList();
                    list.add(orgView);
                    ItemSalesViewmap.put(orgView.getDelegatorId(), list);
                }
            }
        }
    }

    public void prepareRootGroupValues() {
        stripMapReference = 0;
        if (delegatorRootList != null && !delegatorRootList.isEmpty()) {
            for (InventoryDelegator ig : delegatorRootList) {

                PurchaseOfItemsByDelegatorViewBean imdbgb = new PurchaseOfItemsByDelegatorViewBean();
                imdbgb.setDelegatorId(ig.getId());
                // imdbgb.setItemName(ig.getName());
                imdbgb.setGroupName(ig.getName());

                PurchaseOfItemsByDelegatorBeanMap.put(ig.getId(), imdbgb);
                findListFromMapAndPutValue(ig, imdbgb);
                PurchaseOfItemsByDelegatorViewTreeMap.put(stripMapReference++, PurchaseOfItemsByDelegatorBeanMap.get(ig.getId()));
                //    getChildTreeNodesForGroup(ig);
            }
        }
    }

    /*  private void getChildTreeNodesForGroup(InventoryDelegator grp) {
     //stripMapReference = 0;
        
            
     PurchaseOfItemsByDelegatorViewBean imdbgb = new PurchaseOfItemsByDelegatorViewBean();
     imdbgb.setDelegatorId(grp.getId());
     imdbgb.setItemName(grp.getName());
     findListFromMapAndPutValue(grp, imdbgb);
     PurchaseOfItemsByDelegatorBeanMap.put(grp.getId(), imdbgb);
     getPurchaseOfItemsByDelegatorViewTreeMap().put(stripMapReference++, PurchaseOfItemsByDelegatorBeanMap.get(grp.getId()));
                
            
       
     }*/
    private void findListFromMapAndPutValue(InventoryDelegator childGrp, PurchaseOfItemsByDelegatorViewBean srvb) {
        if (ItemSalesViewmap.containsKey(childGrp.getId())) {
            if (ItemSalesViewmap.get(childGrp.getId()) != null && !ItemSalesViewmap.get(childGrp.getId()).isEmpty()) {
                BigDecimal decimal = BigDecimal.ZERO;
                for (ItemSalesView orgSiteView : ItemSalesViewmap.get(childGrp.getId())) {
                    decimal = decimal.add(orgSiteView.getCost());
                }
                srvb.setCost(decimal);

                BigDecimal qtyAll = PurchaseOfItemsByDelegatorBeanMap.get(childGrp.getId()).getItemQuantity();
                repositoryBalance = repositoryBalance.add(qtyAll != null ? qtyAll : BigDecimal.ZERO);

            }
        }
    }

    public void mergeGroupsWithItems() {

        for (Map.Entry<Integer, PurchaseOfItemsByDelegatorViewBean> entry : getPurchaseOfItemsByDelegatorViewTreeMap().entrySet()) {
            PurchaseOfItemsByDelegatorViewBean viewBean = entry.getValue();
            // getPurchaseOfItemsByDelegatorViewBeanList().add(viewBean);
            if (ItemSalesViewmap.containsKey(viewBean.getDelegatorId())) {
                if (ItemSalesViewmap.get(viewBean.getDelegatorId()) != null && !ItemSalesViewmap.get(viewBean.getDelegatorId()).isEmpty()) {
                    PurchaseOfItemsByDelegatorViewBean viewBean1 = new PurchaseOfItemsByDelegatorViewBean();
                    viewBean1.setGroupName(viewBean.getGroupName());
                    BigDecimal totalNet = BigDecimal.ZERO;
                    getPurchaseOfItemsByDelegatorViewBeanList().add(viewBean1);
                    purchaseOfItemsByDelegatorViewBeanShortList.add(viewBean1);
                    int index = getPurchaseOfItemsByDelegatorViewBeanList().size() - 1;
                    for (ItemSalesView idv : ItemSalesViewmap.get(viewBean.getDelegatorId())) {
                        PurchaseOfItemsByDelegatorViewBean reportViewBean = new PurchaseOfItemsByDelegatorViewBean();
                        reportViewBean.setCost(idv.getCost());
                        reportViewBean.setDate(idv.getDate());
                        reportViewBean.setDiscount(idv.getDiscount());
                        reportViewBean.setItemCode(idv.getItemCode());
                        reportViewBean.setItemName(idv.getItemName());
                        reportViewBean.setItemQuantity(idv.getItemQuantity());
                        reportViewBean.setItemUnitName(idv.getItemUnitName());
                        reportViewBean.setSerial(idv.getSerial());
                        reportViewBean.setNet(idv.getNet());
                        reportViewBean.setDetailUnitName(idv.getDetailUnitName());
                        reportViewBean.setItemBarcode(idv.getItemBarcode());
                        reportViewBean.setDetailItemQuantity(idv.getDetailItemQuantity());
                        getPurchaseOfItemsByDelegatorViewBeanList().add(reportViewBean);

                        totalNet = totalNet.add(idv.getNet());
                    }
                    viewBean1.setNet(totalNet);
                    viewBean1.setIndex(index);
                    total = total.add(totalNet);
                    getPurchaseOfItemsByDelegatorViewBeanList().set(index, viewBean1);
                }
            }
            // System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        if (purchaseOfItemsByDelegatorViewBeanList != null && !purchaseOfItemsByDelegatorViewBeanList.isEmpty()) {
            purchaseOfItemsByDelegatorViewBeanTempList = new ArrayList<>(purchaseOfItemsByDelegatorViewBeanList);
        }

        pieChartsRows = new ArrayList<>();

        List<Object> colmns = new ArrayList<>();
        colmns.add("'" + "x" + "'");
        colmns.add("'" + "y" + "'");
        pieChartsRows.add(colmns);
        for (PurchaseOfItemsByDelegatorViewBean delegatorViewBean : purchaseOfItemsByDelegatorViewBeanShortList) {
            colmns = new ArrayList<>();
            colmns.add("'" + delegatorViewBean.getGroupName() + "'");
            colmns.add(delegatorViewBean.getNet());
            pieChartsRows.add(colmns);

            BigDecimal percentage = delegatorViewBean.getNet().multiply(BigDecimal.valueOf(100)).divide(total, 4, 4);
            percentage = percentage.setScale(1, BigDecimal.ROUND_UP);

            PurchaseOfItemsByDelegatorViewBean itemsByDelegatorViewBean = purchaseOfItemsByDelegatorViewBeanList.get(delegatorViewBean.getIndex());
            itemsByDelegatorViewBean.setChartPercentage(percentage);
            purchaseOfItemsByDelegatorViewBeanList.set(itemsByDelegatorViewBean.getIndex(), itemsByDelegatorViewBean);
            System.out.println("the percentage is : " + percentage);
        }
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
//        search();
        /*     if (purchaseOfItemsByDelegatorSearchBean.getShowReport() != null && purchaseOfItemsByDelegatorSearchBean.getShowReport()) {
         fillReport(prepareReport(), getUserData().getReportPath() + "PurchaseOfItemsByDelegator.jasper", getPurchaseOfItemsByDelegatorViewBeanList(), "pdf");
         } else {
         fillReport(prepareReport(), getUserData().getReportPath() + "SalesOfItemsByDelegator.jasper", getPurchaseOfItemsByDelegatorViewBeanList(), "pdf");
         }*/
        if (purchaseOfItemsByDelegatorSearchBean.getShowReport() != null && purchaseOfItemsByDelegatorSearchBean.getShowReport()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "ItemSalesTotalReport.jasper", getPurchaseOfItemsByDelegatorViewBeanList(), "pdf");
        } else {
            fillReport(prepareReport(), getUserData().getReportPath() + "ItemSalesReport.jasper", getPurchaseOfItemsByDelegatorViewBeanList(), "pdf");
        }
    }

    
     public void exportChartPDF(ActionEvent actionEvent) throws JRException, IOException {
        search();
        if (getPurchaseOfItemsByDelegatorViewBeanList() != null && !getPurchaseOfItemsByDelegatorViewBeanList().isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "ItemSalesReportWithColumnChart.jasper", getPurchaseOfItemsByDelegatorViewBeanList(), "pdf");
        }
    }

    @Override
    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();

        hashMap.put("UserName", getUserData().getUser().getName());
        hashMap.put("Branch", getUserData().getUserBranch().getNameAr());
        hashMap.put("CompanyName", getUserData().getCompany().getName());

        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        hashMap.put("dateText", userDDs.get("DATE"));

        hashMap.put("dateFromText", userDDs.get("YEAR_FROM"));

        hashMap.put("dateToText", userDDs.get("YEAR_TO"));
        if (isSales) {
            hashMap.put("header", userDDs.get("SALE_BY_DELE"));

        } else {
            hashMap.put("header", userDDs.get("PURCHA_BY_DELE"));

        }

        hashMap.put("DateFromLabel", userDDs.get("FROM_PERIOD"));
        hashMap.put("DateToLabel", userDDs.get("TO_PERIOD"));

        hashMap.put("DateFromValue", purchaseOfItemsByDelegatorSearchBean.getDateFrom());
        hashMap.put("DateToValue", purchaseOfItemsByDelegatorSearchBean.getDateTo());

        hashMap.put("ReceiptNum", userDDs.get("INVOICE_NO"));
        hashMap.put("ReceiptDate", userDDs.get("INVOICE_DATE"));

        hashMap.put("ItemNum", userDDs.get("ITEM_NUMBER"));
        hashMap.put("description", userDDs.get("DESCRIPTION"));

        hashMap.put("costText", userDDs.get("COST"));

        hashMap.put("net", userDDs.get("NET"));
        hashMap.put("groupName", userDDs.get("DELEGATOR_NAME"));

        hashMap.put("net", userDDs.get("NET"));

        hashMap.put("ReceiptNum", userDDs.get("INVOICE_NO"));
        hashMap.put("ReceiptDate", userDDs.get("INVOICE_DATE"));

        hashMap.put("ItemNum", userDDs.get("ITEM_NUMBER"));
        hashMap.put("Description", userDDs.get("ITEM_NAME"));

        hashMap.put("Unit", userDDs.get("UNIT"));
        hashMap.put("Quantitiy", userDDs.get("QUANTITY"));
        hashMap.put("UnitPrice", userDDs.get("UNIT_PRICE"));
        hashMap.put("Discount", userDDs.get("DISCOUNT"));
        hashMap.put("Value", userDDs.get("VALUE"));
        hashMap.put("total", userDDs.get("TOTAL"));
        hashMap.put("totalnet", total);
        return hashMap;
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

    public List<ItemSalesView> getItemSalesViewList() {
        return itemSalesViewList;
    }

    public void setItemSalesViewList(List<ItemSalesView> itemSalesViewList) {
        this.itemSalesViewList = itemSalesViewList;
    }

    public Boolean getIsSales() {
        return isSales;
    }

    public void setIsSales(Boolean isSales) {
        this.isSales = isSales;
    }

    /**
     * @return the invItemService
     */
    public InvItemService getInvItemService() {
        return invItemService;
    }

    /**
     * @param invItemService the invItemService to set
     */
    public void setInvItemService(InvItemService invItemService) {
        this.invItemService = invItemService;
    }

    /**
     * @return the ItemSalesViewmap
     */
    public Map<Integer, List<ItemSalesView>> getItemSalesViewmap() {
        return ItemSalesViewmap;
    }

    /**
     * @param ItemSalesViewmap the ItemSalesViewmap to set
     */
    public void setItemSalesViewmap(Map<Integer, List<ItemSalesView>> ItemSalesViewmap) {
        this.ItemSalesViewmap = ItemSalesViewmap;
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
     * @return the purchaseOfItemsByDelegatorReportService
     */
    public PurchaseOfItemsByDelegatorReportService getPurchaseOfItemsByDelegatorReportService() {
        return purchaseOfItemsByDelegatorReportService;
    }

    /**
     * @param purchaseOfItemsByDelegatorReportService the
     * purchaseOfItemsByDelegatorReportService to set
     */
    public void setPurchaseOfItemsByDelegatorReportService(PurchaseOfItemsByDelegatorReportService purchaseOfItemsByDelegatorReportService) {
        this.purchaseOfItemsByDelegatorReportService = purchaseOfItemsByDelegatorReportService;
    }

    /**
     * @return the purchaseOfItemsByDelegatorSearchViewBean
     */
    public PurchaseOfItemsByDelegatorSearchBean getPurchaseOfItemsByDelegatorSearchViewBean() {
        return purchaseOfItemsByDelegatorSearchViewBean;
    }

    /**
     * @param purchaseOfItemsByDelegatorSearchViewBean the
     * purchaseOfItemsByDelegatorSearchViewBean to set
     */
    public void setPurchaseOfItemsByDelegatorSearchViewBean(PurchaseOfItemsByDelegatorSearchBean purchaseOfItemsByDelegatorSearchViewBean) {
        this.purchaseOfItemsByDelegatorSearchViewBean = purchaseOfItemsByDelegatorSearchViewBean;
    }

    /**
     * @return the purchaseOfItemsByDelegatorViewBean
     */
    public PurchaseOfItemsByDelegatorViewBean getPurchaseOfItemsByDelegatorViewBean() {
        return purchaseOfItemsByDelegatorViewBean;
    }

    /**
     * @param purchaseOfItemsByDelegatorViewBean the
     * purchaseOfItemsByDelegatorViewBean to set
     */
    public void setPurchaseOfItemsByDelegatorViewBean(PurchaseOfItemsByDelegatorViewBean purchaseOfItemsByDelegatorViewBean) {
        this.purchaseOfItemsByDelegatorViewBean = purchaseOfItemsByDelegatorViewBean;
    }

    /**
     * @return the delegatorRootList
     */
    public List<InventoryDelegator> getDelegatorRootList() {
        return delegatorRootList;
    }

    /**
     * @param delegatorRootList the delegatorRootList to set
     */
    public void setDelegatorRootList(List<InventoryDelegator> delegatorRootList) {
        this.delegatorRootList = delegatorRootList;
    }

    /**
     * @return the PurchaseOfItemsByDelegatorBeanMap
     */
    public Map<Integer, PurchaseOfItemsByDelegatorViewBean> getPurchaseOfItemsByDelegatorBeanMap() {
        return PurchaseOfItemsByDelegatorBeanMap;
    }

    /**
     * @param PurchaseOfItemsByDelegatorBeanMap the
     * PurchaseOfItemsByDelegatorBeanMap to set
     */
    public void setPurchaseOfItemsByDelegatorBeanMap(Map<Integer, PurchaseOfItemsByDelegatorViewBean> PurchaseOfItemsByDelegatorBeanMap) {
        this.PurchaseOfItemsByDelegatorBeanMap = PurchaseOfItemsByDelegatorBeanMap;
    }

    /**
     * @return the PurchaseOfItemsByDelegatorViewTreeMap
     */
    public TreeMap<Integer, PurchaseOfItemsByDelegatorViewBean> getPurchaseOfItemsByDelegatorViewTreeMap() {
        return PurchaseOfItemsByDelegatorViewTreeMap;
    }

    /**
     * @param PurchaseOfItemsByDelegatorViewTreeMap the
     * PurchaseOfItemsByDelegatorViewTreeMap to set
     */
    public void setPurchaseOfItemsByDelegatorViewTreeMap(TreeMap<Integer, PurchaseOfItemsByDelegatorViewBean> PurchaseOfItemsByDelegatorViewTreeMap) {
        this.PurchaseOfItemsByDelegatorViewTreeMap = PurchaseOfItemsByDelegatorViewTreeMap;
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
     * @return the purchaseOfItemsByDelegatorViewBeanList
     */
    public List<PurchaseOfItemsByDelegatorViewBean> getPurchaseOfItemsByDelegatorViewBeanList() {
        return purchaseOfItemsByDelegatorViewBeanList;
    }

    /**
     * @param purchaseOfItemsByDelegatorViewBeanList the
     * purchaseOfItemsByDelegatorViewBeanList to set
     */
    public void setPurchaseOfItemsByDelegatorViewBeanList(List<PurchaseOfItemsByDelegatorViewBean> purchaseOfItemsByDelegatorViewBeanList) {
        this.purchaseOfItemsByDelegatorViewBeanList = purchaseOfItemsByDelegatorViewBeanList;
    }

    /**
     * @return the purchaseOfItemsByDelegatorSearchBean
     */
    public PurchaseOfItemsByDelegatorSearchBean getPurchaseOfItemsByDelegatorSearchBean() {
        return purchaseOfItemsByDelegatorSearchBean;
    }

    /**
     * @param purchaseOfItemsByDelegatorSearchBean the
     * purchaseOfItemsByDelegatorSearchBean to set
     */
    public void setPurchaseOfItemsByDelegatorSearchBean(PurchaseOfItemsByDelegatorSearchBean purchaseOfItemsByDelegatorSearchBean) {
        this.purchaseOfItemsByDelegatorSearchBean = purchaseOfItemsByDelegatorSearchBean;
    }

    /**
     * @return the level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * @return the ItemSalesViewGroupedByItemIdMap
     */
    public Map<Integer, ItemSalesView> getItemSalesViewGroupedByItemIdMap() {
        return ItemSalesViewGroupedByItemIdMap;
    }

    /**
     * @param ItemSalesViewGroupedByItemIdMap the
     * ItemSalesViewGroupedByItemIdMap to set
     */
    public void setItemSalesViewGroupedByItemIdMap(Map<Integer, ItemSalesView> ItemSalesViewGroupedByItemIdMap) {
        this.ItemSalesViewGroupedByItemIdMap = ItemSalesViewGroupedByItemIdMap;
    }

    /**
     * @return the purchaseOfItemsByDelegatorViewBeanShortList
     */
    public List<PurchaseOfItemsByDelegatorViewBean> getPurchaseOfItemsByDelegatorViewBeanShortList() {
        return purchaseOfItemsByDelegatorViewBeanShortList;
    }

    /**
     * @param purchaseOfItemsByDelegatorViewBeanShortList the
     * purchaseOfItemsByDelegatorViewBeanShortList to set
     */
    public void setPurchaseOfItemsByDelegatorViewBeanShortList(List<PurchaseOfItemsByDelegatorViewBean> purchaseOfItemsByDelegatorViewBeanShortList) {
        this.purchaseOfItemsByDelegatorViewBeanShortList = purchaseOfItemsByDelegatorViewBeanShortList;
    }

    /**
     * @return the purchaseOfItemsByDelegatorViewBeanTempList
     */
    public List<PurchaseOfItemsByDelegatorViewBean> getPurchaseOfItemsByDelegatorViewBeanTempList() {
        return purchaseOfItemsByDelegatorViewBeanTempList;
    }

    /**
     * @param purchaseOfItemsByDelegatorViewBeanTempList the
     * purchaseOfItemsByDelegatorViewBeanTempList to set
     */
    public void setPurchaseOfItemsByDelegatorViewBeanTempList(List<PurchaseOfItemsByDelegatorViewBean> purchaseOfItemsByDelegatorViewBeanTempList) {
        this.purchaseOfItemsByDelegatorViewBeanTempList = purchaseOfItemsByDelegatorViewBeanTempList;
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

    /*
     public Boolean getChart() {
     return chart;
     }

 
     public void setChart(Boolean chart) {
     this.chart = chart;
     }*/
}
