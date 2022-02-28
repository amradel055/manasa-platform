/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.reports;

import com.toby.businessservice.report.PurchaseOfItemsBySuppliersReportService;
import com.toby.businessservice.InvItemService;
import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.reports.searchBean.PurchaseOfItemsBySuppliersSearchBean;
import com.toby.entity.InvOrganizationSite;
import com.toby.report.entity.PurchaseOfItemsBySuppliersViewBean;
import com.toby.toby.BaseInventoryReportBean;
import com.toby.views.ItemSalesView;
import com.toby.views.ItemSalesViewGroup;
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
@Named("purchaseOfItemsBySuppliersReportMB")
@ViewScoped
public class PurchaseOfItemsBySuppliersReportMB extends BaseInventoryReportBean {

    Boolean type;

    // Objects
    private Integer branchId;
    private String uri;
    private String screenMode;
    private Integer invoiceType;

    BigDecimal balance = BigDecimal.ZERO;
    private Boolean isSales;

    // Bean Objs and Lists
    private List<PurchaseOfItemsBySuppliersViewBean> purchaseOfItemsBySuppliersViewBeanList;
    private List<PurchaseOfItemsBySuppliersViewBean> purchaseOfItemsBySuppliersViewBeanShortList;
    private List<PurchaseOfItemsBySuppliersViewBean> purchaseOfItemsBySuppliersViewBeanTempList;
    private PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean;
    private List<ItemSalesView> itemSalesViewList;
    // EJBs
    @EJB
    private InvItemService invItemService;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private PurchaseOfItemsBySuppliersReportService purchaseOfItemsBySuppliersReportService;

    //-----------------------
    private Integer stripMapReference = 0;
    private BigDecimal repositoryBalance = BigDecimal.ZERO;
    private PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchViewBean;
    private PurchaseOfItemsBySuppliersViewBean purchaseOfItemsBySuppliersViewBean;
    private List<InvOrganizationSite> organizationSiteRootList;
    private Map<Integer, List<ItemSalesView>> ItemSalesViewmap = new HashMap<>();
    private Map<Integer, ItemSalesView> ItemSalesViewGroupedByItemIdMap = new HashMap<>();
    private Map<Integer, PurchaseOfItemsBySuppliersViewBean> PurchaseOfItemsBySuppliersBeanMap = new HashMap<>();
    private TreeMap<Integer, PurchaseOfItemsBySuppliersViewBean> PurchaseOfItemsBySuppliersViewTreeMap = new TreeMap<>();
    private BigDecimal total = BigDecimal.ZERO;
    private List<Object> pieChartsRows;
    //private Boolean chart = false;

    //-----------------------
    @Override
    @PostConstruct
    public void load() {

        setBranchId(getUserData().getUserBranch().getId());
        uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
        super.load();
        initObjs();

        fillLists();
        getPurchaseOfItemsBySuppliersSearchBean().setFromInventoryName(getInvInventoryList().get(0));
        getPurchaseOfItemsBySuppliersSearchBean().setToInventoryName(getInvInventoryList().get(0));
        getPurchaseOfItemsBySuppliersSearchBean().setDateTo(new Date());
        getPurchaseOfItemsBySuppliersSearchBean().setDateFrom(new Date());
        setOrganizationSiteRootList(new ArrayList<>());
//        if (isSales) {
//            setOrganizationSiteRootList(organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(getUserData().getUserBranch().getId(),true,0));
//        } else {
//            setOrganizationSiteRootList(organizationSiteService.getorganizationSiteByBranchIdForGlBankModule(getUserData().getUserBranch().getId(),true,1));
//        }
        purchaseOfItemsBySuppliersViewBeanList = new ArrayList<>();
    }
    
     public void convertDateFrom() {
        getPurchaseOfItemsBySuppliersSearchBean().setDateFrom(findDateFromHijriFrom());
    }

    public void convertDateTo() {
        getPurchaseOfItemsBySuppliersSearchBean().setDateTo(findDateFromHijriTo());
    }

    private void initObjs() {
        itemSalesViewList = new ArrayList<>();
        purchaseOfItemsBySuppliersSearchBean = new PurchaseOfItemsBySuppliersSearchBean();

        purchaseOfItemsBySuppliersSearchBean.setBranchId(getBranchId());
        purchaseOfItemsBySuppliersSearchBean.setShowReport(false);
        if (uri.contains("PurchaseOfItemsBySuppliers")) {
            purchaseOfItemsBySuppliersSearchBean.setType(0);
            isSales = false;
        } else {
            purchaseOfItemsBySuppliersSearchBean.setType(4);
            isSales = true;
        }

        setItemSalesViewmap(new HashMap<>());
        setPurchaseOfItemsBySuppliersViewTreeMap(new TreeMap<>());
        setPurchaseOfItemsBySuppliersBeanMap(new HashMap<>());
        ItemSalesViewmap = new HashMap<>();
        ItemSalesViewGroupedByItemIdMap = new HashMap<>();

        setPurchaseOfItemsBySuppliersViewBeanList(new ArrayList<>());
        purchaseOfItemsBySuppliersViewBeanShortList = new ArrayList<>();
        purchaseOfItemsBySuppliersViewBeanTempList = new ArrayList<>();
        
        getPurchaseOfItemsBySuppliersSearchBean().setGalleryFrom(getGallaryLists());

    }

    private void fillLists() {
        getInventoryListByBranch();
//        getInvItemByBranch();

//        if (isSales) {
//            getInvOrganizationSiteAsCustomer();
//            getInvoiceSalesListByBranch();
//            // setInvOrganizationList(getOrganizationSiteService().getCustomerByBranchId(getBranchId())); // عميل
//        } else {
//            getInvOrganizationSiteAsSupplier();
//            getInvoicePurchaseListByBranch();
//            // setInvOrganizationList(getOrganizationSiteService().getSupplierByBranchId(getBranchId())); //مورد
//        }
    }

    public void IntializeSearchLab() {
        itemSalesViewList = new ArrayList<>();
        ItemSalesViewmap = new HashMap<>();
        ItemSalesViewGroupedByItemIdMap = new HashMap<>();

        PurchaseOfItemsBySuppliersViewTreeMap = new TreeMap<>();
    }

    @Override
    public void reset() {
        initObjs();
//        fillLists();
        //   purchaseOfItemsBySuppliersSearchBean = new PurchaseOfItemsBySuppliersSearchBean();
        //   setPurchaseOfItemsBySuppliersSearchViewBean(new PurchaseOfItemsBySuppliersSearchBean());
        //   itemSalesViewList = new ArrayList<>();
        setTotal(BigDecimal.ZERO);
    }

    public void showItemReport() {
        itemSalesViewList = new ArrayList<>();
        search();
    }

    public void shortReportListing() {
        if (purchaseOfItemsBySuppliersSearchBean.getShortReport() != null) {
            if (purchaseOfItemsBySuppliersSearchBean.getShortReport()) {
                purchaseOfItemsBySuppliersViewBeanList = new ArrayList<>(purchaseOfItemsBySuppliersViewBeanShortList);
            } else {
                purchaseOfItemsBySuppliersViewBeanList = new ArrayList<>(purchaseOfItemsBySuppliersViewBeanTempList);
            }
        }
    }

    @Override
    public void search() {
        total = BigDecimal.ZERO;
        if (isSales) {
            purchaseOfItemsBySuppliersSearchBean.setType(4);
        } else {
            purchaseOfItemsBySuppliersSearchBean.setType(0);
        }
        IntializeSearchLab();
        setPurchaseOfItemsBySuppliersViewBeanList(new ArrayList<>());
        purchaseOfItemsBySuppliersViewBeanShortList = new ArrayList<>();
        purchaseOfItemsBySuppliersViewBeanTempList = new ArrayList<>();

        purchaseOfItemsBySuppliersSearchBean.setFrompaymentType(purchaseOfItemsBySuppliersSearchBean.getFrompaymentType() != null ? purchaseOfItemsBySuppliersSearchBean.getFrompaymentType() - 1 : null);
        purchaseOfItemsBySuppliersSearchBean.setTopaymentType(purchaseOfItemsBySuppliersSearchBean.getTopaymentType() != null ? purchaseOfItemsBySuppliersSearchBean.getTopaymentType() - 1 : null);

        balance = BigDecimal.ZERO;

        setPurchaseOfItemsBySuppliersSearchViewBean(new PurchaseOfItemsBySuppliersSearchBean());

        getPurchaseOfItemsBySuppliersSearchBean().setBranchId(getUserData().getUserBranch().getId());

        //getPurchaseOfItemsBySuppliersSearchBean().setType(Boolean.FALSE);

        /*if (purchaseOfItemsBySuppliersSearchBean.getShowReport() != null && purchaseOfItemsBySuppliersSearchBean.getShowReport()) {
         itemSalesViewList = purchaseOfItemsBySuppliersReportService.getAllInvoiceDetailBySuppliersSearchBean(purchaseOfItemsBySuppliersSearchBean);

         } else {
         itemSalesViewList = purchaseOfItemsBySuppliersReportService.getAllPurchaseOfItemsBySuppliersSearchBean(purchaseOfItemsBySuppliersSearchBean);
         }*/
        setItemSalesViewList(purchaseOfItemsBySuppliersReportService.getAllPurchaseOfItemsBySuppliersSearchBeanNew(getPurchaseOfItemsBySuppliersSearchBean(),getInvoiceType()));
        if (getItemSalesViewList() != null && !itemSalesViewList.isEmpty()) {
            fillGroupMapWithCorrespondingItem();
            if (purchaseOfItemsBySuppliersSearchBean.getShowReport() != null && purchaseOfItemsBySuppliersSearchBean.getShowReport()) {
                fillItemIdMapByGroupId();
                fillGroupMapWithCorrespondingItem();
            }
            
            InvOrganizationSite ios = new InvOrganizationSite();
            Map<Integer, Integer> customermap = new HashMap<>();
            for (ItemSalesView itemSalesView : getItemSalesViewList()) {
                ios = new InvOrganizationSite();
                if (!customermap.containsKey(itemSalesView.getOrganizationSiteId())) {
                    ios.setId(itemSalesView.getOrganizationSiteId());
                    ios.setName(itemSalesView.getOrganizationSiteName());
                    ios.setCode(itemSalesView.getOrganizationSiteCode());
                    getOrganizationSiteRootList().add(ios);
                    customermap.put(itemSalesView.getOrganizationSiteId(), itemSalesView.getOrganizationSiteId());
                }
            }         
            prepareRootGroupValues();
        }
        mergeGroupsWithItems();
        if (purchaseOfItemsBySuppliersSearchBean.getShortReport() != null) {
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
                if (ItemSalesViewmap.containsKey(orgView.getOrganizationSiteId())) {
                    List list = ItemSalesViewmap.get(orgView.getOrganizationSiteId());
                    list.add(orgView);
                    ItemSalesViewmap.put(orgView.getOrganizationSiteId(), list);
                } else {
                    List list = new ArrayList();
                    list.add(orgView);
                    ItemSalesViewmap.put(orgView.getOrganizationSiteId(), list);
                }
            }
        }
    }

    public void prepareRootGroupValues() {
        stripMapReference = 0;
        if (organizationSiteRootList != null && !organizationSiteRootList.isEmpty()) {
            for (InvOrganizationSite ig : organizationSiteRootList) {

                PurchaseOfItemsBySuppliersViewBean imdbgb = new PurchaseOfItemsBySuppliersViewBean();
                imdbgb.setOrganizationId(ig.getId());
                // imdbgb.setItemName(ig.getName());
                imdbgb.setGroupSupplierName(ig.getName());
                imdbgb.setGroupName(ig.getName());
                PurchaseOfItemsBySuppliersBeanMap.put(ig.getId(), imdbgb);
                findListFromMapAndPutValue(ig, imdbgb);
                getPurchaseOfItemsBySuppliersViewTreeMap().put(stripMapReference++, PurchaseOfItemsBySuppliersBeanMap.get(ig.getId()));
                getChildTreeNodesForGroup(ig);
            }
        }
    }

    private void getChildTreeNodesForGroup(InvOrganizationSite grp) {
        //stripMapReference = 0;
        if (grp.getInvOrganizationSiteCollection() != null && !grp.getInvOrganizationSiteCollection().isEmpty()) {
            for (InvOrganizationSite childGrp : grp.getInvOrganizationSiteCollection()) {
                PurchaseOfItemsBySuppliersViewBean imdbgb = new PurchaseOfItemsBySuppliersViewBean();
                imdbgb.setOrganizationId(childGrp.getId());
                //imdbgb.setItemName(childGrp.getName());
                imdbgb.setGroupSupplierName(childGrp.getName());
                imdbgb.setGroupName(childGrp.getName());
                findListFromMapAndPutValue(childGrp, imdbgb);
                PurchaseOfItemsBySuppliersBeanMap.put(childGrp.getId(), imdbgb);
                getPurchaseOfItemsBySuppliersViewTreeMap().put(stripMapReference++, PurchaseOfItemsBySuppliersBeanMap.get(childGrp.getId()));
                getChildTreeNodesForGroup(childGrp);
            }
        }
    }

    private void findListFromMapAndPutValue(InvOrganizationSite childGrp, PurchaseOfItemsBySuppliersViewBean srvb) {
        if (ItemSalesViewmap.containsKey(childGrp.getId())) {
            if (ItemSalesViewmap.get(childGrp.getId()) != null && !ItemSalesViewmap.get(childGrp.getId()).isEmpty()) {
                BigDecimal decimal = BigDecimal.ZERO;
                for (ItemSalesView orgSiteView : ItemSalesViewmap.get(childGrp.getId())) {
                    decimal = decimal.add(orgSiteView.getCost());
                }
                srvb.setCost(decimal);
                if (childGrp.getParent() != null) {
                    putValueOfParent(childGrp.getParent(), decimal);
                } else {
                    BigDecimal qtyAll = PurchaseOfItemsBySuppliersBeanMap.get(childGrp.getId()).getItemQuantity();
                    repositoryBalance = repositoryBalance.add(qtyAll != null ? qtyAll : BigDecimal.ZERO);
                }
            }
        }
    }

    public void putValueOfParent(InvOrganizationSite ParentGrp, BigDecimal bd) {
        if (PurchaseOfItemsBySuppliersBeanMap.containsKey(ParentGrp.getId())) {
            PurchaseOfItemsBySuppliersViewBean bean = PurchaseOfItemsBySuppliersBeanMap.get(ParentGrp.getId());
            bean.setItemQuantity(bd != null ? bd : BigDecimal.ZERO);
            bean.setItemQuantity(bean.getItemQuantity().add(bd));
            if (ParentGrp.getParent() != null) {
                putValueOfParent(ParentGrp.getParent(), bd);
            }
        }
    }

    public void mergeGroupsWithItems() {

        for (Map.Entry<Integer, PurchaseOfItemsBySuppliersViewBean> entry : getPurchaseOfItemsBySuppliersViewTreeMap().entrySet()) {
            PurchaseOfItemsBySuppliersViewBean viewBean = entry.getValue();
            if (ItemSalesViewmap.containsKey(viewBean.getOrganizationId())) {
                if (ItemSalesViewmap.get(viewBean.getOrganizationId()) != null && !ItemSalesViewmap.get(viewBean.getOrganizationId()).isEmpty()) {
                    //  getPurchaseOfItemsBySuppliersViewBeanList().add(viewBean);
                    PurchaseOfItemsBySuppliersViewBean viewBean1 = new PurchaseOfItemsBySuppliersViewBean();
                    viewBean1.setGroupName(viewBean.getGroupName());
                    BigDecimal totalNet = BigDecimal.ZERO;
                    purchaseOfItemsBySuppliersViewBeanList.add(viewBean1);
                    purchaseOfItemsBySuppliersViewBeanShortList.add(viewBean1);
                    int index = purchaseOfItemsBySuppliersViewBeanList.size() - 1;
                    for (ItemSalesView idv : ItemSalesViewmap.get(viewBean.getOrganizationId())) {
                        PurchaseOfItemsBySuppliersViewBean reportViewBean = new PurchaseOfItemsBySuppliersViewBean();
                        reportViewBean.setCost(idv.getCost());
                        reportViewBean.setDate(idv.getDate());
                        reportViewBean.setDiscount(idv.getDiscount());
                        reportViewBean.setItemCode(idv.getItemCode());
                        reportViewBean.setItemName(idv.getItemName());
                        reportViewBean.setItemQuantity(idv.getItemQuantity());
                        reportViewBean.setItemUnitName(idv.getItemUnitName());
                        reportViewBean.setSerial(idv.getSerial());
                        reportViewBean.setSerialTax(idv.getSerialTax());
                        reportViewBean.setNet(idv.getNet());

                        reportViewBean.setDetailUnitName(idv.getDetailUnitName());
                        reportViewBean.setItemBarcode(idv.getItemBarcode());
                        reportViewBean.setDetailItemQuantity(idv.getDetailItemQuantity());
                        getPurchaseOfItemsBySuppliersViewBeanList().add(reportViewBean);

                        totalNet = totalNet.add(idv.getNet());
                    }
                    viewBean1.setNet(totalNet);
                    viewBean1.setIndex(index);
                    total = total.add(totalNet);
                    purchaseOfItemsBySuppliersViewBeanList.set(index, viewBean1);
                }
            }
            //   System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        if (purchaseOfItemsBySuppliersViewBeanList != null && !purchaseOfItemsBySuppliersViewBeanList.isEmpty()) {
            purchaseOfItemsBySuppliersViewBeanTempList = new ArrayList<>(purchaseOfItemsBySuppliersViewBeanList);
        }
        pieChartsRows = new ArrayList<>();

        List<Object> colmns = new ArrayList<>();
        colmns.add("'" + "x" + "'");
        colmns.add("'" + "y" + "'");
        pieChartsRows.add(colmns);
        for (PurchaseOfItemsBySuppliersViewBean itemsBySuppliersViewBean : purchaseOfItemsBySuppliersViewBeanShortList) {
            colmns = new ArrayList<>();
            colmns.add("'" + itemsBySuppliersViewBean.getGroupName() + "'");
            colmns.add(itemsBySuppliersViewBean.getNet());
            pieChartsRows.add(colmns);

            BigDecimal percentage = itemsBySuppliersViewBean.getNet().multiply(BigDecimal.valueOf(100)).divide(total, 4, 4);
            percentage = percentage.setScale(1, BigDecimal.ROUND_UP);

            PurchaseOfItemsBySuppliersViewBean suppliersViewBean = purchaseOfItemsBySuppliersViewBeanList.get(itemsBySuppliersViewBean.getIndex());
            suppliersViewBean.setChartPercentage(percentage);
            purchaseOfItemsBySuppliersViewBeanList.set(suppliersViewBean.getIndex(), suppliersViewBean);
            System.out.println("the percentage is : " + percentage);
        }
        /* for (Object row : pieChartsRows) {
         System.out.println(row);
         }*/
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
//        search();
        if (purchaseOfItemsBySuppliersSearchBean.getShowReport() != null && purchaseOfItemsBySuppliersSearchBean.getShowReport()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "ItemSalesTotalReport.jasper", getPurchaseOfItemsBySuppliersViewBeanList(), "pdf");
        } else {
            fillReport(prepareReport(), getUserData().getReportPath() + "ItemSalesReporttoby.jasper", getPurchaseOfItemsBySuppliersViewBeanList(), "pdf");
        }
    }

    public void exportChartPDF(ActionEvent actionEvent) throws JRException, IOException {
        search();
        if (getPurchaseOfItemsBySuppliersViewBeanList() != null && !getPurchaseOfItemsBySuppliersViewBeanList().isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "ItemSalesReportWithColumnChart.jasper", getPurchaseOfItemsBySuppliersViewBeanList(), "pdf");
        }
    }

    

    @Override
    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();

        hashMap.put("UserName", getUserData().getUser().getName());
        hashMap.put("Branch", getUserData().getUserBranch().getNameAr());
        hashMap.put("CompanyName", getUserData().getCompany().getName());

        hashMap.put("dateText", userDDs.get("DATE"));

        hashMap.put("dateFromText", userDDs.get("YEAR_FROM"));
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        hashMap.put("dateToText", userDDs.get("YEAR_TO"));
        if (isSales) {
            hashMap.put("header", userDDs.get("ITEM_SALE_CUST"));
            hashMap.put("groupName", userDDs.get("CUSTOM_NAME"));
        } else {
            hashMap.put("header", userDDs.get("PURCHASES_ITM_SUPP"));
            hashMap.put("groupName", userDDs.get("SUPPLIER_NAME"));
        }

        hashMap.put("DateFromLabel", userDDs.get("FROM_PERIOD"));
        hashMap.put("DateToLabel", userDDs.get("TO_PERIOD"));

        hashMap.put("DateFromValue", purchaseOfItemsBySuppliersSearchBean.getDateFrom());
        hashMap.put("DateToValue", purchaseOfItemsBySuppliersSearchBean.getDateTo());

        hashMap.put("ReceiptNum", userDDs.get("INVOICE_NO"));
        hashMap.put("workOrderNum", "رقم الشغل");
        hashMap.put("ReceiptDate", userDDs.get("INVOICE_DATE"));

        hashMap.put("ItemNum", userDDs.get("ITEM_NUMBER"));
        hashMap.put("description", userDDs.get("ITEM_NAME"));

        hashMap.put("unit", userDDs.get("UNIT"));
        hashMap.put("quantitiy", userDDs.get("QUANTITY"));
        hashMap.put("discount", userDDs.get("DISCOUNT"));
        hashMap.put("costText", userDDs.get("COST"));

        hashMap.put("net", userDDs.get("NET"));

        hashMap.put("Description", userDDs.get("DESCRIPTION"));
        hashMap.put("Unit", userDDs.get("UNIT"));
        hashMap.put("Quantitiy", userDDs.get("QUANTITY"));
        hashMap.put("UnitPrice", userDDs.get("PURCHASE_COST"));
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
     * @return the purchaseOfItemsBySuppliersReportService
     */
    public PurchaseOfItemsBySuppliersReportService getPurchaseOfItemsBySuppliersReportService() {
        return purchaseOfItemsBySuppliersReportService;
    }

    /**
     * @param purchaseOfItemsBySuppliersReportService the
     * purchaseOfItemsBySuppliersReportService to set
     */
    public void setPurchaseOfItemsBySuppliersReportService(PurchaseOfItemsBySuppliersReportService purchaseOfItemsBySuppliersReportService) {
        this.purchaseOfItemsBySuppliersReportService = purchaseOfItemsBySuppliersReportService;
    }

    /**
     * @return the purchaseOfItemsBySuppliersSearchBean
     */
    public PurchaseOfItemsBySuppliersSearchBean getPurchaseOfItemsBySuppliersSearchBean() {
        if(purchaseOfItemsBySuppliersSearchBean == null){
            purchaseOfItemsBySuppliersSearchBean = new PurchaseOfItemsBySuppliersSearchBean();
        }
        return purchaseOfItemsBySuppliersSearchBean;
    }

    /**
     * @param purchaseOfItemsBySuppliersSearchBean the
     * purchaseOfItemsBySuppliersSearchBean to set
     */
    public void setPurchaseOfItemsBySuppliersSearchBean(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchBean) {
        this.purchaseOfItemsBySuppliersSearchBean = purchaseOfItemsBySuppliersSearchBean;
    }

    /**
     * @return the purchaseOfItemsBySuppliersViewBeanList
     */
    public List<PurchaseOfItemsBySuppliersViewBean> getPurchaseOfItemsBySuppliersViewBeanList() {
        return purchaseOfItemsBySuppliersViewBeanList;
    }

    /**
     * @param purchaseOfItemsBySuppliersViewBeanList the
     * purchaseOfItemsBySuppliersViewBeanList to set
     */
    public void setPurchaseOfItemsBySuppliersViewBeanList(List<PurchaseOfItemsBySuppliersViewBean> purchaseOfItemsBySuppliersViewBeanList) {
        this.purchaseOfItemsBySuppliersViewBeanList = purchaseOfItemsBySuppliersViewBeanList;
    }

    /**
     * @return the purchaseOfItemsBySuppliersSearchViewBean
     */
    public PurchaseOfItemsBySuppliersSearchBean getPurchaseOfItemsBySuppliersSearchViewBean() {
        return purchaseOfItemsBySuppliersSearchViewBean;
    }

    /**
     * @param purchaseOfItemsBySuppliersSearchViewBean the
     * purchaseOfItemsBySuppliersSearchViewBean to set
     */
    public void setPurchaseOfItemsBySuppliersSearchViewBean(PurchaseOfItemsBySuppliersSearchBean purchaseOfItemsBySuppliersSearchViewBean) {
        this.purchaseOfItemsBySuppliersSearchViewBean = purchaseOfItemsBySuppliersSearchViewBean;
    }

    /**
     * @return the purchaseOfItemsBySuppliersViewBean
     */
    public PurchaseOfItemsBySuppliersViewBean getPurchaseOfItemsBySuppliersViewBean() {
        return purchaseOfItemsBySuppliersViewBean;
    }

    /**
     * @param purchaseOfItemsBySuppliersViewBean the
     * purchaseOfItemsBySuppliersViewBean to set
     */
    public void setPurchaseOfItemsBySuppliersViewBean(PurchaseOfItemsBySuppliersViewBean purchaseOfItemsBySuppliersViewBean) {
        this.purchaseOfItemsBySuppliersViewBean = purchaseOfItemsBySuppliersViewBean;
    }

    /**
     * @return the organizationSiteRootList
     */
    public List<InvOrganizationSite> getOrganizationSiteRootList() {
        if(organizationSiteRootList == null){
            organizationSiteRootList = new ArrayList<>();
        }
        return organizationSiteRootList;
    }

    /**
     * @param organizationSiteRootList the organizationSiteRootList to set
     */
    public void setOrganizationSiteRootList(List<InvOrganizationSite> organizationSiteRootList) {
        this.organizationSiteRootList = organizationSiteRootList;
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
     * @return the PurchaseOfItemsBySuppliersBeanMap
     */
    public Map<Integer, PurchaseOfItemsBySuppliersViewBean> getPurchaseOfItemsBySuppliersBeanMap() {
        return PurchaseOfItemsBySuppliersBeanMap;
    }

    /**
     * @param PurchaseOfItemsBySuppliersBeanMap the
     * PurchaseOfItemsBySuppliersBeanMap to set
     */
    public void setPurchaseOfItemsBySuppliersBeanMap(Map<Integer, PurchaseOfItemsBySuppliersViewBean> PurchaseOfItemsBySuppliersBeanMap) {
        this.PurchaseOfItemsBySuppliersBeanMap = PurchaseOfItemsBySuppliersBeanMap;
    }

    /**
     * @return the PurchaseOfItemsBySuppliersViewTreeMap
     */
    public TreeMap<Integer, PurchaseOfItemsBySuppliersViewBean> getPurchaseOfItemsBySuppliersViewTreeMap() {
        return PurchaseOfItemsBySuppliersViewTreeMap;
    }

    /**
     * @param PurchaseOfItemsBySuppliersViewTreeMap the
     * PurchaseOfItemsBySuppliersViewTreeMap to set
     */
    public void setPurchaseOfItemsBySuppliersViewTreeMap(TreeMap<Integer, PurchaseOfItemsBySuppliersViewBean> PurchaseOfItemsBySuppliersViewTreeMap) {
        this.PurchaseOfItemsBySuppliersViewTreeMap = PurchaseOfItemsBySuppliersViewTreeMap;
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
     * @return the purchaseOfItemsBySuppliersViewBeanShortList
     */
    public List<PurchaseOfItemsBySuppliersViewBean> getPurchaseOfItemsBySuppliersViewBeanShortList() {
        return purchaseOfItemsBySuppliersViewBeanShortList;
    }

    /**
     * @param purchaseOfItemsBySuppliersViewBeanShortList the
     * purchaseOfItemsBySuppliersViewBeanShortList to set
     */
    public void setPurchaseOfItemsBySuppliersViewBeanShortList(List<PurchaseOfItemsBySuppliersViewBean> purchaseOfItemsBySuppliersViewBeanShortList) {
        this.purchaseOfItemsBySuppliersViewBeanShortList = purchaseOfItemsBySuppliersViewBeanShortList;
    }

    /**
     * @return the purchaseOfItemsBySuppliersViewBeanTempList
     */
    public List<PurchaseOfItemsBySuppliersViewBean> getPurchaseOfItemsBySuppliersViewBeanTempList() {
        return purchaseOfItemsBySuppliersViewBeanTempList;
    }

    /**
     * @param purchaseOfItemsBySuppliersViewBeanTempList the
     * purchaseOfItemsBySuppliersViewBeanTempList to set
     */
    public void setPurchaseOfItemsBySuppliersViewBeanTempList(List<PurchaseOfItemsBySuppliersViewBean> purchaseOfItemsBySuppliersViewBeanTempList) {
        this.purchaseOfItemsBySuppliersViewBeanTempList = purchaseOfItemsBySuppliersViewBeanTempList;
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

    /**
     * @return the invoiceType
     */
    public Integer getInvoiceType() {
        return invoiceType;
    }

    /**
     * @param invoiceType the invoiceType to set
     */
    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }

    /**
     *
     * public Boolean getChart() { return chart; }
     *
     *
     * public void setChart(Boolean chart) { this.chart = chart; }
     */
}
