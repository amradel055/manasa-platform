/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.reports;

import com.toby.businessservice.TobyUserInventoryService;
import com.toby.businessservice.report.InventoryStripReportService;
import com.toby.businessservice.InvGroupService;
import com.toby.businessservice.ItemsDataViewService;
import com.toby.businessservice.OrganizationSiteService;
import com.toby.businessservice.reports.searchBean.InvItemSalesSearchBean;
import com.toby.converter.InvGroupConverter;
import com.toby.converter.InvInventoryConverter;
import com.toby.converter.InvOrganizationSiteConverter;
import com.toby.entity.InvGroup;
import com.toby.entity.InvInventory;
import com.toby.entity.InvOrganizationSite;
import com.toby.report.entity.StripReportView;
import com.toby.report.entity.StripReportViewBean;
import com.toby.report.entity.TotalsOfReport;
import com.toby.toby.BaseReportBean;
import com.toby.toby.UserData;
import com.toby.views.ItemsDataView;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author hhhh
 */
@Named("inventoryStripReportMB")
@ViewScoped
public class InventoryStripReportMB extends BaseReportBean {

    // Objects and Variables
    private InvItemSalesSearchBean invStripSearchBean;
    private InvItemSalesSearchBean invStripSearchServiceBean;
    private List<StripReportView> invStripReportViewList;
    private List<ItemsDataView> itemsDataViewList;

    private Integer branchId;
    private String uri;
    private String screenMode;

    // Lists and Converters
    private List<InvInventory> invInventoryList;
    private InvInventoryConverter invInventoryConverter;
    private List<InvInventory> invInventoryIteratedList;
    private List<InvGroup> invGroupList;
    private List<InvGroup> invSubGroupsList;
    private List<InvGroup> invGroupRootList;
    private InvGroupConverter groupConverter;
    private List<InvOrganizationSite> suplierList;
    private InvOrganizationSiteConverter suplierConverter;

    private Map<Integer, List<ItemsDataView>> stripReportItemsMap = new HashMap<>();
    private Map<Integer, StripReportViewBean> stripReportItemsBeanMap = new HashMap<>();
    private TreeMap<Integer, StripReportViewBean> stripReportMap = new TreeMap<>();

    private StripReportViewBean stripReportViewBean;
    private List<StripReportViewBean> stripReportViewBeanList;
    private Integer stripMapReference;
    private Integer level;
    private BigDecimal repositoryBalance = BigDecimal.ZERO;
    private StringBuilder stringBuilder;
    private TotalsOfReport totalsOfReport;
    private TotalsOfReport finalTotalsOfReport;

    private String balanceValue;
    private String debitValue;
    private String creditValue;
    private String firstValue;
    private String secondValue;
    private boolean negativeValues = false;
    private Integer evaluationStatus;
    private BigDecimal weightPrice = BigDecimal.ZERO;
    // EJBs
    @EJB
    private TobyUserInventoryService tobyUserInventoryService;
    @EJB
    private InvGroupService invGroupService;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    InventoryStripReportService inventoryStripReportService;
    @EJB
    private ItemsDataViewService itemsDataViewService;

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
        invGroupRootList = invGroupService.getInvGroupsRootsByBranchId(branchId);
        //invSubGroupsList = invGroupService.getInvGroupFromToByBranchId(branchId, group1, group2);
    }

    private void initObjs() {
        reset();
        invInventoryList = new ArrayList<>();
        invGroupList = new ArrayList<>();
        invGroupRootList = new ArrayList<>();
        invSubGroupsList = new ArrayList<>();
        suplierList = new ArrayList<>();
        invInventoryIteratedList = new ArrayList<>();
        stripReportItemsMap = new HashMap<>();
        stripReportItemsBeanMap = new HashMap<>();
        stripReportMap = new TreeMap<>();
        stripReportViewBeanList = new ArrayList<>();
        evaluationStatus = 0;
    }

    private void fillLists() {
        invInventoryList = tobyUserInventoryService.getAllInventroisByUserAndBranchPer(getUserData().getUser().getId(), branchId);
        invInventoryConverter = new InvInventoryConverter(invInventoryList);

        invGroupList = invGroupService.getallInvGroupByBranchId(branchId);
        groupConverter = new InvGroupConverter(invGroupList);

        suplierList = organizationSiteService.getSupplierByBranchIdForSupplierAccountReportMB(branchId, 1); // its type = 1 
        suplierConverter = new InvOrganizationSiteConverter(suplierList);
    }

    @Override
    public void reset() {
        invStripSearchBean = new InvItemSalesSearchBean();
        invStripSearchServiceBean = new InvItemSalesSearchBean();
        invStripSearchBean.setBranchId(branchId);
        invStripSearchBean.setDateTo(new Date());

        invStripReportViewList = new ArrayList<>();
        itemsDataViewList = new ArrayList<>();
    }

    /**
     * @Override public void search() {
     * invStripSearchBean.setBranchId(branchId); itemsDataViewList =
     * inventoryStripReportService.getAllInvStripListByInvStripSearchBean(invStripSearchBean);
     *
     * StripReportView invStripReportBean; invStripReportViewList = new
     * ArrayList<>(); for (ItemsDataView dataView : itemsDataViewList) {
     * invStripReportBean = new StripReportView();
     * invStripReportBean.setBalance(dataView.getBalance());
     * invStripReportBean.setBalanceValue(dataView.getBalanceValue());
     * invStripReportBean.setCostAverage(dataView.getCostAverage());
     * invStripReportBean.setGroupId(dataView.getGroupId());
     * invStripReportBean.setGroupName(dataView.getGroupName());
     * invStripReportBean.setInventoryName(dataView.getInventoryName());
     * invStripReportBean.setItemCode(dataView.getItemCode());
     * invStripReportBean.setItemName(dataView.getItemName());
     * invStripReportBean.setQtyIn(dataView.getQtyin());
     * invStripReportBean.setQtyOut(dataView.getQtyout());
     * invStripReportBean.setUnitName(dataView.getItemUnitName());
     * invStripReportViewList.add(invStripReportBean); }
     *
     * }
     */
    public void IntializeSearchLab() {
        itemsDataViewList = new ArrayList<>();
        stripReportItemsMap = new HashMap<>();
        stripReportItemsBeanMap = new HashMap<>();
        stripReportMap = new TreeMap<>();
    }

    @Override
    public void search() {

        if (invStripSearchBean != null && invStripSearchBean.getDateTo() != null) {
            findTheChildsOfGroupsWhenSearchFromTo();
            stripReportViewBeanList = new ArrayList<>();
            stripMapReference = 0;

            fillRepoList();
            invStripSearchBean.setBranchId(branchId);
            finalTotalsOfReport = new TotalsOfReport();
            for (InvInventory inv : invInventoryIteratedList) {
                IntializeSearchLab();
                repositoryBalance = BigDecimal.ZERO;
                totalsOfReport = new TotalsOfReport();
                stripReportViewBean = new StripReportViewBean();
//            stripReportViewBean.setNameHoldPlace(inv.getName());
//            stripReportViewBean.setItemName("المستودع");
                stripReportViewBean.setGroupName(inv.getName());
                invStripSearchServiceBean.setInventoryFrom(inv);
                invStripSearchServiceBean.setInventoryTo(inv);
                invStripSearchServiceBean.setDateFrom(invStripSearchBean.getDateFrom());
                invStripSearchServiceBean.setDateTo(invStripSearchBean.getDateTo());
                invStripSearchServiceBean.setBranchId(branchId);
                invStripSearchServiceBean.setPaymentTypeFrom(invStripSearchBean.getPaymentTypeFrom());
                invStripSearchServiceBean.setPaymentTypeTo(invStripSearchBean.getPaymentTypeTo());
                invStripSearchServiceBean.setSalesInvoiceFrom(invStripSearchBean.getSalesInvoiceFrom());
                invStripSearchServiceBean.setSalesInvoiceTo(invStripSearchBean.getSalesInvoiceTo());
                invStripSearchServiceBean.setSalesPersonFrom(invStripSearchBean.getSalesPersonFrom());
                invStripSearchServiceBean.setSalesPersonTo(invStripSearchBean.getSalesPersonTo());
                invStripSearchServiceBean.setSuplierFrom(invStripSearchBean.getSuplierFrom());
                invStripSearchServiceBean.setSuplierTo(invStripSearchBean.getSuplierTo());
                invStripSearchServiceBean.setType(invStripSearchBean.getType());
//            invStripSearchServiceBean.setGroupFrom(invStripSearchBean.getGroupFrom());
//            invStripSearchServiceBean.setGroupTo(invStripSearchBean.getGroupTo());

                if (invStripSearchBean.getGroupFrom() != null) {

                }

                itemsDataViewList = itemsDataViewService.getAllInvStripListByInvStripSearchBean(invStripSearchServiceBean);
                if (itemsDataViewList != null && !itemsDataViewList.isEmpty()) {
                    fillEachGroupWithItsCorrespondingList();
                    prepareRootGroupValues();
                }
                mergeGroupsWithItems();
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, getUserData().getUserDDs().get("ERROR"), "يجب اختيار حتى تاريخ"));
        }
    }

    private void findTheChildsOfGroupsWhenSearchFromTo() {
        invSubGroupsList = new ArrayList<>();
        stringBuilder = new StringBuilder();
        invStripSearchServiceBean.setStringBuilder(new StringBuilder());
        if (invStripSearchBean.getGroupFrom() != null || invStripSearchBean.getGroupTo() != null) {
            invSubGroupsList = invGroupService.getInvGroupFromToByBranchId(branchId, invStripSearchBean.getGroupFrom(), invStripSearchBean.getGroupTo());
            if (invSubGroupsList != null && !invSubGroupsList.isEmpty()) {
                invSubGroupsList = invGroupService.getAllSubInvGroupByGroupsAndBranchId(invSubGroupsList, branchId);
                if (invSubGroupsList != null && !invSubGroupsList.isEmpty()) {
                    getAllTheChildsOfSearchedGroup();
                }
            }
        }
    }

    private void getAllTheChildsOfSearchedGroup() {

        if (invSubGroupsList != null && !invSubGroupsList.isEmpty()) {
            for (InvGroup ig : invSubGroupsList) {
                if (stringBuilder != null && stringBuilder.length() == 0) {
                    stringBuilder.append(ig.getId().toString());
                } else {
                    stringBuilder.append(",").append(ig.getId().toString());
                }
            }
            invStripSearchServiceBean.setStringBuilder(stringBuilder);
        }

    }

    private void fillEachGroupWithItsCorrespondingList() {
        for (ItemsDataView item : itemsDataViewList) {
            if (!stripReportItemsMap.containsKey(item.getGroupId())) {
                List<ItemsDataView> itemsDataViewForMap = new ArrayList<>();
                itemsDataViewForMap.add(item);
                stripReportItemsMap.put(item.getGroupId(), itemsDataViewForMap);
            } else {
                List<ItemsDataView> itemsDataViewForMap = stripReportItemsMap.get(item.getGroupId());
                itemsDataViewForMap.add(item);
                stripReportItemsMap.put(item.getGroupId(), itemsDataViewForMap);
            }
        }
    }

    public void prepareRootGroupValues() {
        if (invGroupRootList != null && !invGroupRootList.isEmpty()) {

            for (InvGroup grp : invGroupRootList) {
                level = 1;
                StripReportViewBean srvb = new StripReportViewBean();
                //srvb.setNameHoldPlace(grp.getName());
                //srvb.setItemName("المجموعة");
                srvb.setGroupId(grp.getId());
                srvb.setGroupName(grp.getName());
                srvb.setLevel(level);
                stripReportItemsBeanMap.put(grp.getId(), srvb);
                findListFromMapAndPutValue(grp, srvb);
                stripReportMap.put(stripMapReference++, stripReportItemsBeanMap.get(grp.getId()));

                getChildTreeNodesForGroup(grp);
            }
        }
    }

    private void getChildTreeNodesForGroup(InvGroup grp) {
        if (grp.getInvGroupCollection() != null && !grp.getInvGroupCollection().isEmpty()) {
            level++;
            for (InvGroup childGrp : grp.getInvGroupCollection()) {
                StripReportViewBean srvb = new StripReportViewBean();
//                srvb.setNameHoldPlace(childGrp.getName());
//                srvb.setItemName("المجموعة");
                srvb.setGroupId(childGrp.getId());
                srvb.setGroupName(childGrp.getName());
                srvb.setLevel(level);
                findListFromMapAndPutValue(childGrp, srvb);
                stripReportItemsBeanMap.put(childGrp.getId(), srvb);
                stripReportMap.put(stripMapReference++, stripReportItemsBeanMap.get(childGrp.getId()));
                getChildTreeNodesForGroup(childGrp);
            }
        }
    }

    private void findListFromMapAndPutValue(InvGroup childGrp, StripReportViewBean srvb) {
        if (stripReportItemsMap.containsKey(childGrp.getId())) {
            if (stripReportItemsMap.get(childGrp.getId()) != null && !stripReportItemsMap.get(childGrp.getId()).isEmpty()) {
//                BigDecimal decimal = BigDecimal.ZERO;
                for (ItemsDataView idv : stripReportItemsMap.get(childGrp.getId())) {
//                    decimal = decimal.add(idv.getBalance() != null ? idv.getBalance().setScale(2, BigDecimal.ROUND_UP) : BigDecimal.ZERO);
                    srvb.setBalance((srvb.getBalance() != null ? srvb.getBalance() : BigDecimal.ZERO).add(idv.getBalance() != null ? idv.getBalance().setScale(2, BigDecimal.ROUND_UP) : BigDecimal.ZERO));
                    srvb.setQtyIn((srvb.getQtyIn() != null ? srvb.getQtyIn() : BigDecimal.ZERO).add(idv.getQtyin() != null ? idv.getQtyin().setScale(2, BigDecimal.ROUND_UP) : BigDecimal.ZERO));
                    srvb.setQtyOut((srvb.getQtyOut() != null ? srvb.getQtyOut() : BigDecimal.ZERO).add(idv.getQtyout() != null ? idv.getQtyout().setScale(2, BigDecimal.ROUND_UP) : BigDecimal.ZERO));
                    srvb.setCostAverage((srvb.getCostAverage() != null ? srvb.getCostAverage() : BigDecimal.ZERO).add(idv.getCostAverage() != null ? idv.getCostAverage().setScale(2, BigDecimal.ROUND_UP) : BigDecimal.ZERO));
//                    srvb.setBalanceValue((srvb.getBalanceValue() != null ? srvb.getBalanceValue() : BigDecimal.ZERO).add(idv.getBalanceValue() != null ? idv.getBalanceValue().setScale(2, BigDecimal.ROUND_UP) : BigDecimal.ZERO));
                    srvb.setBalanceValue((srvb.getBalanceValue() != null ? srvb.getBalanceValue() : BigDecimal.ZERO).add(calculateBalanceValueBasedWeightExistenceAndEvaluation(idv)));

                }
//                srvb.setBalance(decimal.setScale(2, BigDecimal.ROUND_UP));
                //repositoryBalance = repositoryBalance.add(srvb.getBalance() != null ? srvb.getBalance() : BigDecimal.ZERO);
                totalsOfReport.setBalanceValue(totalsOfReport.getBalanceValue().add(srvb.getBalance() != null ? srvb.getBalance() : BigDecimal.ZERO));
                totalsOfReport.setDebitValue(totalsOfReport.getDebitValue().add(srvb.getQtyIn() != null ? srvb.getQtyIn() : BigDecimal.ZERO));
                totalsOfReport.setCreditValue(totalsOfReport.getCreditValue().add(srvb.getQtyOut() != null ? srvb.getQtyOut() : BigDecimal.ZERO));
                totalsOfReport.setFirstValue(totalsOfReport.getFirstValue().add(srvb.getCostAverage() != null ? srvb.getCostAverage() : BigDecimal.ZERO));
                totalsOfReport.setSecondValue(totalsOfReport.getSecondValue().add(srvb.getBalanceValue() != null ? srvb.getBalanceValue() : BigDecimal.ZERO));
                if (childGrp.getParent() != null) {
                    putValueOfParent(childGrp.getParent(), srvb);
                }
//                else {
//                    BigDecimal balanceAll = stripReportItemsBeanMap.get(childGrp.getId()).getBalance();
//                    repositoryBalance = repositoryBalance.add(balanceAll != null ? balanceAll : BigDecimal.ZERO);
//                    StripReportViewBean reportViewBean = stripReportItemsBeanMap.get(childGrp.getId());
//                    if (reportViewBean.getLevel() == 1) {
//                        repositoryBalance = repositoryBalance.add(reportViewBean.getBalance() != null ? reportViewBean.getBalance() : BigDecimal.ZERO);
//                    }
//                }
            }
        }
    }

    private BigDecimal calculateBalanceValueBasedWeightExistenceAndEvaluation(ItemsDataView idv) {

        BigDecimal avgBalance = idv.getBalance() != null ? idv.getBalance() : BigDecimal.ZERO;
        BigDecimal finalBalanceValue = idv.getBalanceValue() != null ? idv.getBalanceValue().setScale(2, BigDecimal.ROUND_UP) : BigDecimal.ZERO;
        BigDecimal quntity = idv.getBalance() != null ? idv.getBalance().setScale(2, BigDecimal.ROUND_UP) : BigDecimal.ZERO;
        BigDecimal sellPrice = idv.getSellPrice() != null ? idv.getSellPrice() : BigDecimal.ZERO;

        if (evaluationStatus == 0) {
            return finalBalanceValue;
        } else {
            return quntity.multiply(sellPrice);
        }

    }

    public void putValueOfParent(InvGroup ParentGrp, StripReportViewBean reportViewBean) {
        if (stripReportItemsBeanMap.containsKey(ParentGrp.getId())) {
            StripReportViewBean srvb = stripReportItemsBeanMap.get(ParentGrp.getId());
            srvb.setBalance(srvb.getBalance() != null ? srvb.getBalance() : BigDecimal.ZERO);
            srvb.setBalance(srvb.getBalance().add(reportViewBean.getBalance()).setScale(2, BigDecimal.ROUND_UP));

            srvb.setQtyIn(srvb.getQtyIn() != null ? srvb.getQtyIn() : BigDecimal.ZERO);
            srvb.setQtyIn(srvb.getQtyIn().add(reportViewBean.getQtyIn()).setScale(2, BigDecimal.ROUND_UP));

            srvb.setQtyOut(srvb.getQtyOut() != null ? srvb.getQtyOut() : BigDecimal.ZERO);
            srvb.setQtyOut(srvb.getQtyOut().add(reportViewBean.getQtyOut()).setScale(2, BigDecimal.ROUND_UP));

            srvb.setCostAverage(srvb.getCostAverage() != null ? srvb.getCostAverage() : BigDecimal.ZERO);
            srvb.setCostAverage(srvb.getCostAverage().add(reportViewBean.getCostAverage()).setScale(2, BigDecimal.ROUND_UP));

            srvb.setBalanceValue(srvb.getBalanceValue() != null ? srvb.getBalanceValue() : BigDecimal.ZERO);
            srvb.setBalanceValue(srvb.getBalanceValue().add(reportViewBean.getBalanceValue()).setScale(2, BigDecimal.ROUND_UP));

            stripReportItemsBeanMap.put(ParentGrp.getId(), srvb);

            if (ParentGrp.getParent() != null) {
                putValueOfParent(ParentGrp.getParent(), srvb);
            }
//            else {
//                StripReportViewBean reportViewBean = stripReportItemsBeanMap.get(ParentGrp.getId());
//                if (reportViewBean.getLevel() == level) {
//                    repositoryBalance = repositoryBalance.add(reportViewBean.getBalance() != null ? reportViewBean.getBalance() : BigDecimal.ZERO);
//                }
//            }
        }
    }

    public void fillRepoList() {
        invInventoryIteratedList = new ArrayList<>();
        if (invInventoryList != null && !invInventoryList.isEmpty()) {
            if (invStripSearchBean.getInventoryFrom() != null || invStripSearchBean.getInventoryFrom() != null) {
                for (InvInventory inv : invInventoryList) {
                    if (invStripSearchBean.getInventoryFrom().getId() != null && inv.getId().compareTo(invStripSearchBean.getInventoryFrom().getId()) == 0) {
                        invInventoryIteratedList.add(inv);
                    } else if (invStripSearchBean.getInventoryTo().getId() != null && inv.getId().compareTo(invStripSearchBean.getInventoryTo().getId()) == 0) {
                        invInventoryIteratedList.add(inv);
                    } else if (invStripSearchBean.getInventoryFrom().getId() != null && inv.getId().compareTo(invStripSearchBean.getInventoryFrom().getId()) == 1
                            && invStripSearchBean.getInventoryTo().getId() != null && inv.getId().compareTo(invStripSearchBean.getInventoryTo().getId()) == -1) {
                        invInventoryIteratedList.add(inv);
                    }
                }
            } else {
                invInventoryIteratedList = new ArrayList<>(invInventoryList);
            }
        }
    }

    public void mergeGroupsWithItems() {
        if (itemsDataViewList != null && !itemsDataViewList.isEmpty()) {
            //stripReportViewBean.setBalance(repositoryBalance.setScale(2, BigDecimal.ROUND_UP));
            stripReportViewBean.setBalance(totalsOfReport.getBalanceValue().setScale(2, BigDecimal.ROUND_UP));
            stripReportViewBean.setQtyIn(totalsOfReport.getDebitValue().setScale(2, BigDecimal.ROUND_UP));
            stripReportViewBean.setQtyOut(totalsOfReport.getCreditValue().setScale(2, BigDecimal.ROUND_UP));
            stripReportViewBean.setCostAverage(totalsOfReport.getFirstValue().setScale(2, BigDecimal.ROUND_UP));
            stripReportViewBean.setBalanceValue(totalsOfReport.getSecondValue().setScale(2, BigDecimal.ROUND_UP));

            finalTotalsOfReport.setBalanceTotalValue(finalTotalsOfReport.getBalanceTotalValue().add(stripReportViewBean.getBalance()));
            finalTotalsOfReport.setDebitTotalValue(finalTotalsOfReport.getDebitTotalValue().add(stripReportViewBean.getQtyIn()));
            finalTotalsOfReport.setCreditTotalValue(finalTotalsOfReport.getCreditTotalValue().add(stripReportViewBean.getQtyOut()));
            finalTotalsOfReport.setFirstTotalValue(finalTotalsOfReport.getFirstTotalValue().add(stripReportViewBean.getCostAverage()));
            finalTotalsOfReport.setSecondTotalValue(finalTotalsOfReport.getSecondTotalValue().add(stripReportViewBean.getBalanceValue()));

            stripReportViewBeanList.add(stripReportViewBean);
        }
        for (Map.Entry<Integer, StripReportViewBean> entry : stripReportMap.entrySet()) {
            StripReportViewBean viewBean = entry.getValue();
            if (viewBean.getBalance() != null) {
                stripReportViewBeanList.add(viewBean);
            }
            if (stripReportItemsMap.containsKey(viewBean.getGroupId())) {
                if (stripReportItemsMap.get(viewBean.getGroupId()) != null && !stripReportItemsMap.get(viewBean.getGroupId()).isEmpty()) {
                    for (ItemsDataView idv : stripReportItemsMap.get(viewBean.getGroupId())) {
                        StripReportViewBean reportViewBean = new StripReportViewBean();
                        reportViewBean.setBalance(idv.getBalance() != null ? idv.getBalance() : BigDecimal.ZERO);
                        reportViewBean.setItemName(idv.getItemName());
                        reportViewBean.setUnitName(idv.getItemUnitName());
                        reportViewBean.setItemCode(idv.getItemCode());
                        reportViewBean.setSellPrice(idv.getSellPrice());
//                        reportViewBean.setBalanceValue(idv.getBalanceValue() != null ? idv.getBalanceValue().setScale(2, BigDecimal.ROUND_UP) : BigDecimal.ZERO);
                        reportViewBean.setBalanceValue(calculateBalanceValueBasedWeightExistenceAndEvaluation(idv));
                        reportViewBean.setCostAverage(idv.getCostAverage() != null ? idv.getCostAverage().setScale(2, BigDecimal.ROUND_UP) : BigDecimal.ZERO);
                        reportViewBean.setQtyIn(idv.getQtyin() != null ? idv.getQtyin().setScale(2, BigDecimal.ROUND_UP) : BigDecimal.ZERO);
                        reportViewBean.setQtyOut(idv.getQtyout() != null ? idv.getQtyout().setScale(2, BigDecimal.ROUND_UP) : BigDecimal.ZERO);
                        stripReportViewBeanList.add(reportViewBean);
                    }
                }
            }
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
        DecimalFormat df = new DecimalFormat("#,##0.00");
        balanceValue = df.format(finalTotalsOfReport.getBalanceTotalValue());
        debitValue = df.format(finalTotalsOfReport.getDebitTotalValue());
        creditValue = df.format(finalTotalsOfReport.getCreditTotalValue());
        firstValue = df.format(finalTotalsOfReport.getFirstTotalValue());
        secondValue = df.format(finalTotalsOfReport.getSecondTotalValue());
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        search();
        fillReport(prepareReport(), getUserData().getReportPath() + "InvStripReport.jasper", stripReportViewBeanList, "pdf");
    }

    @Override
    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();

        hashMap.put("PrintedBy", getUserData().getUser().getName());
        hashMap.put("branchName", getUserData().getUserBranch().getNameAr());
        //   hashMap.put("CompanyName", getUserData().getCompany().getName());

        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }

        hashMap.put("dateText", userDDs.get("DATE"));
        hashMap.put("totalText", userDDs.get("TOTAL"));

        hashMap.put("invText", userDDs.get("REPOSITORY"));

        hashMap.put("header", userDDs.get("INVENT_REPOS"));

        hashMap.put("DateToText", userDDs.get("INV_REPO_TILL_DATE"));
        hashMap.put("DateToValue", invStripSearchBean.getDateTo());

        hashMap.put("supplierToText", userDDs.get("FROM_SUPP_NO"));
        hashMap.put("supplierFromValue", invStripSearchBean.getSuplierFrom() != null ? invStripSearchBean.getSuplierFrom().getName() : null);

        hashMap.put("supplierFromText", userDDs.get("TO_SUPP_NO"));
        hashMap.put("supplierToValue", invStripSearchBean.getSuplierTo() != null ? invStripSearchBean.getSuplierTo().getName() : null);

        hashMap.put("inventoryFromText", userDDs.get("FROM_INVENTORY"));
        hashMap.put("inventoryFromValue", invStripSearchBean.getInventoryFrom() != null ? invStripSearchBean.getInventoryFrom().getName() : null);

        hashMap.put("inventoryToText", userDDs.get("TO_REPOS"));
        hashMap.put("inventoryToValue", invStripSearchBean.getInventoryTo() != null ? invStripSearchBean.getInventoryTo().getName() : null);

        hashMap.put("groupFromText", userDDs.get("FROM_GROUP"));
        hashMap.put("groupFromValue", invStripSearchBean.getGroupFrom() != null ? invStripSearchBean.getGroupFrom().getName() : null);

        hashMap.put("groupToText", userDDs.get("TO_GROUP"));
        hashMap.put("groupToValue", invStripSearchBean.getGroupTo() != null ? invStripSearchBean.getGroupTo().getName() : null);

        hashMap.put("ReceiptNumText", userDDs.get("INVOICE_NO"));
        hashMap.put("ReceiptDateText", userDDs.get("INVOICE_DATE"));

        hashMap.put("ItemNumText", userDDs.get("ITEM_NUMBER"));
        hashMap.put("ItemNameText", userDDs.get(" ITEM_NAM "));
        hashMap.put("groupNameText", userDDs.get("GROUP_NAME"));
        hashMap.put("groupNameText", userDDs.get("GROUP_NAME"));
        hashMap.put("UnitText", userDDs.get("UNIT"));

        hashMap.put("totalInvText", userDDs.get("TOT_REPOS_TRANS"));
        hashMap.put("importedText", userDDs.get("IMPORT"));
        hashMap.put("exportedText", userDDs.get("EXPORT"));

        hashMap.put("QuantitiyText", userDDs.get("QUANTITY"));
        hashMap.put("UnitPriceText", userDDs.get("COST"));
        hashMap.put("resetValueText", userDDs.get("BALA_VALUE"));

        hashMap.put("balanceValueText", balanceValue);
        hashMap.put("debitValueText", debitValue);
        hashMap.put("creditValueText", creditValue);
        hashMap.put("firstValueText", firstValue);
        hashMap.put("secondValueText", secondValue);

        return hashMap;
    }

    @Override
    public void exportExcel(ActionEvent actionEvent) throws JRException, IOException {
    }

    @Override
    public void exportHtml(ActionEvent actionEvent) throws JRException, IOException {
    }

    @Override
    public String getScreenName() {
        return screenMode;
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
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase()) && !filteredInvs.contains(invFilter)) {
                filteredInvs.add(invFilter);
            }

            code = invFilter.getCode();
            if (code != null && code.toLowerCase().contains(query.toLowerCase()) && !filteredInvs.contains(invFilter)) {
                filteredInvs.add(invFilter);
            }
        }

        invInventoryConverter = new InvInventoryConverter(filteredInvs);
        return filteredInvs;
    }

    public List<InvGroup> completInvGroups(String query) {
        List<InvGroup> groups = invGroupList;
        if (query == null || query.trim().equals("")) {

            groupConverter = new InvGroupConverter(groups);
            return groups;
        }
        List<InvGroup> filteredGroups = new ArrayList<>();

        String nameAr;
        Integer code;
        InvGroup invFilter;
        for (int i = 0; i < invGroupList.size(); i++) {
            invFilter = groups.get(i);
            nameAr = invFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase()) && !filteredGroups.contains(invFilter)) {
                filteredGroups.add(invFilter);
            }

            code = invFilter.getId();
            if (code != null && String.valueOf(code).toLowerCase().contains(query.toLowerCase()) && !filteredGroups.contains(invFilter)) {
                filteredGroups.add(invFilter);
            }
        }

        groupConverter = new InvGroupConverter(filteredGroups);
        return filteredGroups;
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

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getScreenMode() {
        return screenMode;
    }

    public void setScreenMode(String screenMode) {
        this.screenMode = screenMode;
    }

    public InvItemSalesSearchBean getInvStripSearchBean() {
        return invStripSearchBean;
    }

    public void setInvStripSearchBean(InvItemSalesSearchBean invStripSearchBean) {
        this.invStripSearchBean = invStripSearchBean;
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

    public List<InvGroup> getInvGroupList() {
        return invGroupList;
    }

    public void setInvGroupList(List<InvGroup> invGroupList) {
        this.invGroupList = invGroupList;
    }

    public InvGroupConverter getGroupConverter() {
        return groupConverter;
    }

    public void setGroupConverter(InvGroupConverter groupConverter) {
        this.groupConverter = groupConverter;
    }

    public List<InvOrganizationSite> getSuplierList() {
        return suplierList;
    }

    public void setSuplierList(List<InvOrganizationSite> suplierList) {
        this.suplierList = suplierList;
    }

    public InvOrganizationSiteConverter getSuplierConverter() {
        return suplierConverter;
    }

    public void setSuplierConverter(InvOrganizationSiteConverter suplierConverter) {
        this.suplierConverter = suplierConverter;
    }

    public List<StripReportView> getInvStripReportViewList() {
        return invStripReportViewList;
    }

    public void setInvStripReportViewList(List<StripReportView> invStripReportViewList) {
        this.invStripReportViewList = invStripReportViewList;
    }

    public List<ItemsDataView> getItemsDataViewList() {
        return itemsDataViewList;
    }

    public void setItemsDataViewList(List<ItemsDataView> itemsDataViewList) {
        this.itemsDataViewList = itemsDataViewList;
    }

    /**
     * @return the invInventoryIteratedList
     */
    public List<InvInventory> getInvInventoryIteratedList() {
        return invInventoryIteratedList;
    }

    /**
     * @param invInventoryIteratedList the invInventoryIteratedList to set
     */
    public void setInvInventoryIteratedList(List<InvInventory> invInventoryIteratedList) {
        this.invInventoryIteratedList = invInventoryIteratedList;
    }

    /**
     * @return the stripReportItemsMap
     */
    public Map<Integer, List<ItemsDataView>> getStripReportItemsMap() {
        return stripReportItemsMap;
    }

    /**
     * @param stripReportItemsMap the stripReportItemsMap to set
     */
    public void setStripReportItemsMap(Map<Integer, List<ItemsDataView>> stripReportItemsMap) {
        this.stripReportItemsMap = stripReportItemsMap;
    }

    /**
     * @return the stripReportItemsBeanMap
     */
    public Map<Integer, StripReportViewBean> getStripReportItemsBeanMap() {
        return stripReportItemsBeanMap;
    }

    /**
     * @param stripReportItemsBeanMap the stripReportItemsBeanMap to set
     */
    public void setStripReportItemsBeanMap(Map<Integer, StripReportViewBean> stripReportItemsBeanMap) {
        this.stripReportItemsBeanMap = stripReportItemsBeanMap;
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
     * @return the stripReportMap
     */
    public TreeMap<Integer, StripReportViewBean> getStripReportMap() {
        return stripReportMap;
    }

    /**
     * @param stripReportMap the stripReportMap to set
     */
    public void setStripReportMap(TreeMap<Integer, StripReportViewBean> stripReportMap) {
        this.stripReportMap = stripReportMap;
    }

    /**
     * @return the stripMapReference
     */
    public Integer getStripMapReference() {
        return stripMapReference;
    }

    /**
     * @param stripMapReference the stripMapReference to set
     */
    public void setStripMapReference(Integer stripMapReference) {
        this.stripMapReference = stripMapReference;
    }

    /**
     * @return the stripReportViewBeanList
     */
    public List<StripReportViewBean> getStripReportViewBeanList() {
        return stripReportViewBeanList;
    }

    /**
     * @param stripReportViewBeanList the stripReportViewBeanList to set
     */
    public void setStripReportViewBeanList(List<StripReportViewBean> stripReportViewBeanList) {
        this.stripReportViewBeanList = stripReportViewBeanList;
    }

    /**
     * @return the invStripSearchServiceBean
     */
    public InvItemSalesSearchBean getInvStripSearchServiceBean() {
        return invStripSearchServiceBean;
    }

    /**
     * @param invStripSearchServiceBean the invStripSearchServiceBean to set
     */
    public void setInvStripSearchServiceBean(InvItemSalesSearchBean invStripSearchServiceBean) {
        this.invStripSearchServiceBean = invStripSearchServiceBean;
    }

    /**
     * @return the repositoryBalance
     */
    public BigDecimal getRepositoryBalance() {
        return repositoryBalance;
    }

    /**
     * @param repositoryBalance the repositoryBalance to set
     */
    public void setRepositoryBalance(BigDecimal repositoryBalance) {
        this.repositoryBalance = repositoryBalance;
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
     * @return the invSubGroupsList
     */
    public List<InvGroup> getInvSubGroupsList() {
        return invSubGroupsList;
    }

    /**
     * @param invSubGroupsList the invSubGroupsList to set
     */
    public void setInvSubGroupsList(List<InvGroup> invSubGroupsList) {
        this.invSubGroupsList = invSubGroupsList;
    }

    /**
     * @return the stringBuilder
     */
    public StringBuilder getStringBuilder() {
        return stringBuilder;
    }

    /**
     * @param stringBuilder the stringBuilder to set
     */
    public void setStringBuilder(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    /**
     * @return the totalsOfReport
     */
    public TotalsOfReport getTotalsOfReport() {
        return totalsOfReport;
    }

    /**
     * @param totalsOfReport the totalsOfReport to set
     */
    public void setTotalsOfReport(TotalsOfReport totalsOfReport) {
        this.totalsOfReport = totalsOfReport;
    }

    /**
     * @return the finalTotalsOfReport
     */
    public TotalsOfReport getFinalTotalsOfReport() {
        return finalTotalsOfReport;
    }

    /**
     * @param finalTotalsOfReport the finalTotalsOfReport to set
     */
    public void setFinalTotalsOfReport(TotalsOfReport finalTotalsOfReport) {
        this.finalTotalsOfReport = finalTotalsOfReport;
    }

    /**
     * @return the balanceValue
     */
    public String getBalanceValue() {
        return balanceValue;
    }

    /**
     * @param balanceValue the balanceValue to set
     */
    public void setBalanceValue(String balanceValue) {
        this.balanceValue = balanceValue;
    }

    /**
     * @return the debitValue
     */
    public String getDebitValue() {
        return debitValue;
    }

    /**
     * @param debitValue the debitValue to set
     */
    public void setDebitValue(String debitValue) {
        this.debitValue = debitValue;
    }

    /**
     * @return the creditValue
     */
    public String getCreditValue() {
        return creditValue;
    }

    /**
     * @param creditValue the creditValue to set
     */
    public void setCreditValue(String creditValue) {
        this.creditValue = creditValue;
    }

    /**
     * @return the firstValue
     */
    public String getFirstValue() {
        return firstValue;
    }

    /**
     * @param firstValue the firstValue to set
     */
    public void setFirstValue(String firstValue) {
        this.firstValue = firstValue;
    }

    /**
     * @return the secondValue
     */
    public String getSecondValue() {
        return secondValue;
    }

    /**
     * @param secondValue the secondValue to set
     */
    public void setSecondValue(String secondValue) {
        this.secondValue = secondValue;
    }

    /**
     * @return the negativeValues
     */
    public boolean isNegativeValues() {
        return negativeValues;
    }

    /**
     * @param negativeValues the negativeValues to set
     */
    public void setNegativeValues(boolean negativeValues) {
        this.negativeValues = negativeValues;
    }

    /**
     * @return the evaluationStatus
     */
    public Integer getEvaluationStatus() {
        return evaluationStatus;
    }

    /**
     * @param evaluationStatus the evaluationStatus to set
     */
    public void setEvaluationStatus(Integer evaluationStatus) {
        this.evaluationStatus = evaluationStatus;
    }

    /**
     * @return the weightPrice
     */
    public BigDecimal getWeightPrice() {
        return weightPrice;
    }

    /**
     * @param weightPrice the weightPrice to set
     */
    public void setWeightPrice(BigDecimal weightPrice) {
        this.weightPrice = weightPrice;
    }
}
