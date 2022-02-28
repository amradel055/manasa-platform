/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.reports;

import com.toby.businessservice.report.InvItemSalesReportService;
import com.toby.toby.BaseInventoryReportBean;
import com.toby.businessservice.InvGroupService;
import com.toby.businessservice.reports.searchBean.InvItemSalesSearchBean;
import com.toby.entity.InvGroup;
import com.toby.report.entity.ItemSalesViewBean;
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
@Named("InvItemSalesReportMB")
@ViewScoped
public class InvItemSalesReportMB extends BaseInventoryReportBean {

    // Objects
    private Integer branchId;
    private String uri;
    private String screenMode;

    private Boolean isSales;
    private BigDecimal total = BigDecimal.ZERO;
    // Bean Objs and Lists
    private InvItemSalesSearchBean invItemSalesSearchBean;
    private List<ItemSalesView> itemSalesViewList;
    private Map<Integer, List<ItemSalesView>> itemSalesViewMap = new HashMap<>();

    private List<InvGroup> invGroupRootList;
    private Map<Integer, ItemSalesViewBean> itemSalesReportBeanMap = new HashMap<>();
    private TreeMap<Integer, ItemSalesViewBean> salesReportTreeMap = new TreeMap<>();
    private Integer salesMapReference;
    private List<ItemSalesViewBean> itemSalesViewBeanList;
    private List<ItemSalesViewBean> itemSalesViewBeanShortList;
    private List<ItemSalesViewBean> itemSalesViewBeanTempList;
    private Map<Integer, ItemSalesView> ItemSalesViewGroupedByItemIdMap = new HashMap<>();
    private List<Object> pieChartsRows;
    //   private Boolean chart = false;

    @EJB
    private InvItemSalesReportService invItemSalesReportService;
    @EJB
    private InvGroupService invGroupService;

    @Override
    @PostConstruct
    public void load() {

        branchId = getUserData().getUserBranch().getId();
        uri = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
        super.load();
        initObjs();

        fillLists();
        invGroupRootList = invGroupService.getInvGroupsRootsByBranchId(branchId);

    }

    private void initObjs() {

        itemSalesViewList = new ArrayList<>();
        itemSalesViewBeanList = new ArrayList<>();
        itemSalesViewBeanShortList = new ArrayList<>();
        itemSalesViewBeanTempList = new ArrayList<>();
        invItemSalesSearchBean = new InvItemSalesSearchBean();
        invGroupRootList = new ArrayList<>();
        itemSalesViewMap = new HashMap<>();
        itemSalesReportBeanMap = new HashMap<>();
        salesReportTreeMap = new TreeMap<>();
        ItemSalesViewGroupedByItemIdMap = new HashMap<>();

        if (uri.contains("itemSalesReport")) {
            invItemSalesSearchBean.setType(Boolean.TRUE);
            isSales = true;
        } else {
            invItemSalesSearchBean.setType(Boolean.FALSE);
            isSales = false;
        }

        invItemSalesSearchBean.setBranchId(branchId);
        invItemSalesSearchBean.setShowReport(false);
    }

    private void fillLists() {
        getInventoryListByBranch();
        if (isSales) {
            getDelegateSalesListByBranch();
        } else {
            getDelegatePurchaseListByBranch();
        }

    }

    @Override
    public void reset() {
        initObjs();
        // fillLists();
    }

    public void showItemReport() {
        itemSalesViewList = new ArrayList<>();
        search();
    }

    public void shortReportListing() {
        if (invItemSalesSearchBean.getShortReport() != null) {
            if (invItemSalesSearchBean.getShortReport()) {
                itemSalesViewBeanList = new ArrayList<>(itemSalesViewBeanShortList);
            } else {
                itemSalesViewBeanList = new ArrayList<>(itemSalesViewBeanTempList);
            }
        }
    }

    @Override
    public void search() {

        itemSalesViewMap = new HashMap<>();
        itemSalesViewList = new ArrayList<>();
        itemSalesReportBeanMap = new HashMap<>();
        salesReportTreeMap = new TreeMap<>();
        salesMapReference = 0;
        itemSalesViewBeanList = new ArrayList<>();
        itemSalesViewBeanShortList = new ArrayList<>();
        itemSalesViewBeanTempList = new ArrayList<>();
        total = BigDecimal.ZERO;
        ItemSalesViewGroupedByItemIdMap = new HashMap<>();

        if (isSales) {
            invItemSalesSearchBean.setType(Boolean.TRUE);
        } else {
            invItemSalesSearchBean.setType(Boolean.FALSE);
        }

        invItemSalesSearchBean.setBranchId(branchId);

        /* if (invItemSalesSearchBean.getShowReport() != null && invItemSalesSearchBean.getShowReport()) {
         itemSalesViewList = invItemSalesReportService.getAllInvPurchaseInvoiceDetailsByInvItemSalesSearchBean(invItemSalesSearchBean);
         fillItemIdMapByGroupId();
         } else {
         itemSalesViewList = invItemSalesReportService.getAllInvPurchaseInvoiceDetailsByInvItemSalesSearchBean(invItemSalesSearchBean);
         }*/
        itemSalesViewList = invItemSalesReportService.getAllInvPurchaseInvoiceDetailsByInvItemSalesSearchBean(invItemSalesSearchBean);

        if (itemSalesViewList != null && !itemSalesViewList.isEmpty()) {
            fillEachGroupWithItsCorrespondingList();
            if (invItemSalesSearchBean.getShowReport() != null && invItemSalesSearchBean.getShowReport()) {
                fillItemIdMapByGroupId();
                fillEachGroupWithItsCorrespondingList();
            }
            prepareRootGroupValues();
        }
        mergeGroupsWithItems();

        if (invItemSalesSearchBean.getShortReport() != null) {
            shortReportListing();
        }
    }

    private void fillItemIdMapByGroupId() {
        itemSalesViewList = new ArrayList<>();
        for (Map.Entry<Integer, List<ItemSalesView>> entry : itemSalesViewMap.entrySet()) {
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

    private void fillEachGroupWithItsCorrespondingList() {
        itemSalesViewMap = new HashMap<>();
        for (ItemSalesView item : itemSalesViewList) {
            if (!itemSalesViewMap.containsKey(item.getGroupId())) {
                List<ItemSalesView> itemSalesViews = new ArrayList<>();
                itemSalesViews.add(item);
                itemSalesViewMap.put(item.getGroupId(), itemSalesViews);
            } else {
                List<ItemSalesView> itemSalesViews = itemSalesViewMap.get(item.getGroupId());
                itemSalesViews.add(item);
                itemSalesViewMap.put(item.getGroupId(), itemSalesViews);
            }
        }
    }

    public void prepareRootGroupValues() {
        if (invGroupRootList != null && !invGroupRootList.isEmpty()) {
            for (InvGroup grp : invGroupRootList) {
                ItemSalesViewBean isvb = new ItemSalesViewBean();
                // isvb.setItemName("المجموعة");
                //isvb.setDetailUnitName(grp.getName());
                //isvb.setItemUnitName(grp.getName());
                isvb.setGroupName(grp.getName());
                isvb.setGroupId(grp.getId());
                itemSalesReportBeanMap.put(grp.getId(), isvb);
                salesReportTreeMap.put(salesMapReference++, itemSalesReportBeanMap.get(grp.getId()));

                getChildTreeNodesForGroup(grp);
            }
        }
    }

    private void getChildTreeNodesForGroup(InvGroup grp) {
        if (grp.getInvGroupCollection() != null && !grp.getInvGroupCollection().isEmpty()) {
            for (InvGroup childGrp : grp.getInvGroupCollection()) {
                ItemSalesViewBean isvb = new ItemSalesViewBean();
                //  isvb.setItemName("المجموعة");
                // isvb.setDetailUnitName(childGrp.getName());
                //isvb.setItemUnitName(childGrp.getName());
                isvb.setGroupId(childGrp.getId());
                isvb.setGroupName(grp.getName());
                itemSalesReportBeanMap.put(childGrp.getId(), isvb);
                salesReportTreeMap.put(salesMapReference++, itemSalesReportBeanMap.get(childGrp.getId()));
                getChildTreeNodesForGroup(childGrp);
            }
        }
    }

    public void mergeGroupsWithItems() {

        for (Map.Entry<Integer, ItemSalesViewBean> entry : salesReportTreeMap.entrySet()) {
            ItemSalesViewBean viewBean = entry.getValue();

            if (itemSalesViewMap.containsKey(viewBean.getGroupId())) {

                if (itemSalesViewMap.get(viewBean.getGroupId()) != null && !itemSalesViewMap.get(viewBean.getGroupId()).isEmpty()) {
                    ItemSalesViewBean viewBean1 = new ItemSalesViewBean();
                    viewBean1.setGroupName(itemSalesViewMap.get(viewBean.getGroupId()).get(0).getGroupName());

                    BigDecimal totalNet = BigDecimal.ZERO;
                    itemSalesViewBeanList.add(viewBean1);
                    itemSalesViewBeanShortList.add(viewBean1);
                    int index = itemSalesViewBeanList.size() - 1;
                    //indexMap.put(index, viewBean1);
                    for (ItemSalesView idv : itemSalesViewMap.get(viewBean.getGroupId())) {
                        ItemSalesViewBean salesViewBean = new ItemSalesViewBean();
                        salesViewBean.setItemCode(idv.getItemCode());
                        salesViewBean.setItemName(idv.getItemName());
                        salesViewBean.setItemUnitName(idv.getItemUnitName());
                        salesViewBean.setItemQuantity(idv.getItemQuantity());
                        salesViewBean.setNet(idv.getNet());
                        salesViewBean.setDiscount(idv.getDiscount());
                        salesViewBean.setCost(idv.getCost());
                        salesViewBean.setDetailUnitName(idv.getDetailUnitName());
                        salesViewBean.setDate(idv.getDate());
                        salesViewBean.setSerial(idv.getSerial());
                        salesViewBean.setItemBarcode(idv.getItemBarcode());
                        salesViewBean.setDetailItemQuantity(idv.getDetailItemQuantity());
                        itemSalesViewBeanList.add(salesViewBean);

                        totalNet = totalNet.add(idv.getNet());
                    }
                    viewBean1.setNet(totalNet);
                    viewBean1.setIndex(index);
                    total = total.add(totalNet);
                    itemSalesViewBeanList.set(index, viewBean1);

                }
            }
        }
        if (itemSalesViewBeanList != null && !itemSalesViewBeanList.isEmpty()) {
            itemSalesViewBeanTempList = new ArrayList<>(itemSalesViewBeanList);
        }

        //start to prepare the chart array 
        pieChartsRows = new ArrayList<>();

        List<Object> colmns = new ArrayList<>();
        colmns.add("'" + "x" + "'");
        colmns.add("'" + "y" + "'");
        pieChartsRows.add(colmns);
        for (ItemSalesViewBean itemSalesViewBean : itemSalesViewBeanShortList) {
            colmns = new ArrayList<>();
            colmns.add("'" + itemSalesViewBean.getGroupName() + "'");
            colmns.add(itemSalesViewBean.getNet());
            pieChartsRows.add(colmns);

            BigDecimal percentage = itemSalesViewBean.getNet().multiply(BigDecimal.valueOf(100)).divide(total, 4, 4);
            percentage = percentage.setScale(1, BigDecimal.ROUND_UP);

            ItemSalesViewBean salesViewBean = itemSalesViewBeanList.get(itemSalesViewBean.getIndex());
            salesViewBean.setChartPercentage(percentage);
            itemSalesViewBeanList.set(salesViewBean.getIndex(), salesViewBean);
            System.out.println("the percentage is : " + percentage);
        }

        /*  for (Object row : pieChartsRows) {
         System.out.println(row);
         }*/
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        search();
        if (invItemSalesSearchBean.getShowReport() != null && invItemSalesSearchBean.getShowReport()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "ItemSalesTotalReport.jasper", itemSalesViewBeanList, "pdf");
        } else {
            fillReport(prepareReport(), getUserData().getReportPath() + "ItemSalesReport.jasper", itemSalesViewBeanList, "pdf");
        }
    }

    public void exportChartPDF(ActionEvent actionEvent) throws JRException, IOException {
        search();
        if (itemSalesViewBeanList != null && !itemSalesViewBeanList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "ItemSalesReportWithChart.jasper", itemSalesViewBeanList, "pdf");
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

        if (isSales) {
            hashMap.put("header", userDDs.get("ITEM_SALE_GROUP"));
        } else {
            hashMap.put("header", userDDs.get("PURCHASES_ITE_GRO"));
        }

        hashMap.put("DateFromLabel", userDDs.get("FROM_PERIOD"));
        hashMap.put("DateToLabel", userDDs.get("TO_PERIOD"));

        hashMap.put("DateFromValue", invItemSalesSearchBean.getDateFrom());
        hashMap.put("DateToValue", invItemSalesSearchBean.getDateTo());

        hashMap.put("ReceiptNum", userDDs.get("INVOICE_NO"));
        hashMap.put("ReceiptDate", userDDs.get("INVOICE_DATE"));

        hashMap.put("ItemNum", userDDs.get("ITEM_NUMBER"));
        hashMap.put("Description", userDDs.get("ITEM_NAME"));

        hashMap.put("Unit", userDDs.get("UNIT"));
        hashMap.put("Quantitiy", userDDs.get("QUANTITY"));
        hashMap.put("UnitPrice", userDDs.get("UNIT_PRICE"));
        hashMap.put("Discount", userDDs.get("DISCOUNT"));
        hashMap.put("Value", userDDs.get("VALUE"));
        hashMap.put("groupName", userDDs.get("GROUP_NAME"));
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

    public InvItemSalesSearchBean getInvItemSalesSearchBean() {
        return invItemSalesSearchBean;
    }

    public void setInvItemSalesSearchBean(InvItemSalesSearchBean invItemSalesSearchBean) {
        this.invItemSalesSearchBean = invItemSalesSearchBean;
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
     * @return the invGroupRootList
     */
    public List<InvGroup> getInvGroupRootList() {
        return invGroupRootList;
    }

    /**
     * @param invGroupRootList the invGroupRootList to set
     */
    public void setInvGroupRootList(List<InvGroup> invGroupRootList) {
        this.invGroupRootList = invGroupRootList;
    }

    /**
     * @return the itemSalesViewMap
     */
    public Map<Integer, List<ItemSalesView>> getItemSalesViewMap() {
        return itemSalesViewMap;
    }

    /**
     * @param itemSalesViewMap the itemSalesViewMap to set
     */
    public void setItemSalesViewMap(Map<Integer, List<ItemSalesView>> itemSalesViewMap) {
        this.itemSalesViewMap = itemSalesViewMap;
    }

    /**
     * @return the itemSalesReportBeanMap
     */
    public Map<Integer, ItemSalesViewBean> getItemSalesReportBeanMap() {
        return itemSalesReportBeanMap;
    }

    /**
     * @param itemSalesReportBeanMap the itemSalesReportBeanMap to set
     */
    public void setItemSalesReportBeanMap(Map<Integer, ItemSalesViewBean> itemSalesReportBeanMap) {
        this.itemSalesReportBeanMap = itemSalesReportBeanMap;
    }

    /**
     * @return the salesReportTreeMap
     */
    public TreeMap<Integer, ItemSalesViewBean> getSalesReportTreeMap() {
        return salesReportTreeMap;
    }

    /**
     * @param salesReportTreeMap the salesReportTreeMap to set
     */
    public void setSalesReportTreeMap(TreeMap<Integer, ItemSalesViewBean> salesReportTreeMap) {
        this.salesReportTreeMap = salesReportTreeMap;
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
     * @return the itemSalesViewBeanList
     */
    public List<ItemSalesViewBean> getItemSalesViewBeanList() {
        return itemSalesViewBeanList;
    }

    /**
     * @param itemSalesViewBeanList the itemSalesViewBeanList to set
     */
    public void setItemSalesViewBeanList(List<ItemSalesViewBean> itemSalesViewBeanList) {
        this.itemSalesViewBeanList = itemSalesViewBeanList;
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
     * @return the itemSalesViewBeanShortList
     */
    public List<ItemSalesViewBean> getItemSalesViewBeanShortList() {
        return itemSalesViewBeanShortList;
    }

    /**
     * @param itemSalesViewBeanShortList the itemSalesViewBeanShortList to set
     */
    public void setItemSalesViewBeanShortList(List<ItemSalesViewBean> itemSalesViewBeanShortList) {
        this.itemSalesViewBeanShortList = itemSalesViewBeanShortList;
    }

    /**
     * @return the itemSalesViewBeanTempList
     */
    public List<ItemSalesViewBean> getItemSalesViewBeanTempList() {
        return itemSalesViewBeanTempList;
    }

    /**
     * @param itemSalesViewBeanTempList the itemSalesViewBeanTempList to set
     */
    public void setItemSalesViewBeanTempList(List<ItemSalesViewBean> itemSalesViewBeanTempList) {
        this.itemSalesViewBeanTempList = itemSalesViewBeanTempList;
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
