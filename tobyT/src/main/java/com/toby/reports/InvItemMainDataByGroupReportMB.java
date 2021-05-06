/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.report;

import com.toby.businessservice.TobyUserInventoryService;
import com.toby.businessservice.InvGroupService;
import com.toby.businessservice.InvItemService;
import com.toby.businessservice.report.InvItemMainDataByGroupReportService;
import com.toby.businessservice.reports.searchBean.ItemMainDataByGroupSearchBean;
import com.toby.converter.InvGroupConverter;
import com.toby.converter.InvInventoryConverter;
import com.toby.converter.ItemConverter;
import com.toby.entity.InvGroup;
import com.toby.entity.InvInventory;
import com.toby.entity.InvItem;
import com.toby.report.entity.itemMainDataByGroupBean;
import com.toby.toby.BaseReportBean;
import com.toby.toby.UserData;
import com.toby.views.ItemMainDataByGroupView;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
@Named("invItemMainDataByGroupReportMB")
@ViewScoped
public class InvItemMainDataByGroupReportMB extends BaseReportBean {

    // Lists and Converters
    private List<InvGroup> invGroupList;
    private List<InvGroup> invGroupSubList;
    private List<InvGroup> invGroupSearchList;
    private BigDecimal repositoryBalance = BigDecimal.ZERO;
    private InvGroupConverter invGroupConverter;
    private List<InvInventory> invInventoryList;
    private InvInventoryConverter invInventoryConverter;
    private List<InvInventory> invInventoryIteratedList;
    private List<InvItem> invItems;
    private ItemConverter itemConverter;
    private Integer level;
    private List<ItemMainDataByGroupView> itemMainDataByGroupViewList;
    // Objects
    private Integer branchId;
    private String uri;
    private String screenMode;
    private Integer stripMapReference;
    private ItemMainDataByGroupSearchBean itemMainDataByGroupSearchBean;
    private ItemMainDataByGroupSearchBean itemMainDataByGroupSearchViewBean;
    private itemMainDataByGroupBean dataByGroupBean;
    private itemMainDataByGroupBean itemMainDataByGroupViewBean;
    private List<itemMainDataByGroupBean> itemMainDataByGroupBeanList;
    private Map<Integer, List<ItemMainDataByGroupView>> itemGroupmap = new HashMap<>();
    private Map<Integer, itemMainDataByGroupBean> itemMainMap = new HashMap<>();
    private TreeMap<Integer, itemMainDataByGroupBean> itemGroupTreeMap = new TreeMap<>();

    private List<InvGroup> invGroupRootList;
    List<InvItem> invItemsReport;
    // EJBs
    @EJB
    private InvItemMainDataByGroupReportService InvItemMainDataByGroupReportService;
    @EJB
    private InvGroupService InvGroupService;
    @EJB
    private TobyUserInventoryService tobyUserInventoryService;
    @EJB
    private InvItemService invItemService;

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

        invGroupRootList = new ArrayList<>();
        invGroupRootList = InvGroupService.getInvGroupsRootsByBranchId(branchId);
        itemMainDataByGroupSearchBean.setGroupSelected(new ArrayList<>());
        for (InvGroup sublist : invGroupSubList) {
            itemMainDataByGroupSearchBean.getGroupSelected().add(sublist.getId().toString());
        }

    }

    private void initObjs() {

        setInvInventoryList(new ArrayList<>());
        setInvGroupList(new ArrayList<>());
        setItemMainDataByGroupSearchBean(new ItemMainDataByGroupSearchBean());
        getItemMainDataByGroupSearchBean().setBranchId(getBranchId());
        getItemMainDataByGroupSearchBean().setShowReport(false);
        itemGroupmap = new HashMap<>();
        setItemGroupTreeMap(new TreeMap<>());
        setItemMainMap(new HashMap<>());
        setitemMainDataByGroupBeanList(new ArrayList<>());
    }

    private void fillLists() {
        if (uri.contains("basicdataofitemsbygroupsreport")) {
            setInvInventoryList(tobyUserInventoryService.getAllInventroisByUserAndBranchPer(getUserData().getUser().getId(), branchId));
            setInvInventoryConverter(new InvInventoryConverter(getInvInventoryList()));
        } else {
            setInvItems(invItemService.getInvItemByBranchIdForInvItemMainDataByGroupReportMB(branchId));
            setItemConverter(new ItemConverter(invItems));
        }
        invGroupSubList = getInvGroupService().getSubInvGroupByBranchIdPer(branchId);
        setInvGroupList(getInvGroupService().getallInvGroupByBranchId(getBranchId()));
        setInvGroupList(invGroupSubList);
        setInvGroupConverter(new InvGroupConverter(getInvGroupList()));

    }

    public void IntializeSearchLab() {
        itemMainDataByGroupViewList = new ArrayList<>();
        itemGroupmap = new HashMap<>();
        itemGroupTreeMap = new TreeMap<>();
    }

    @Override
    public void reset() {
        initObjs();

        setItemMainDataByGroupSearchViewBean(new ItemMainDataByGroupSearchBean());
        setItemMainDataByGroupViewList(new ArrayList<>());
        itemMainDataByGroupSearchBean.setGroupSelected(new ArrayList<>());
        for (InvGroup sublist : invGroupSubList) {
            itemMainDataByGroupSearchBean.getGroupSelected().add(sublist.getId().toString());
        }
        setInvGroupList(invGroupSubList);

    }

    public void showItemReport() {
    }

    @Override
    public void search() {
        stripMapReference = 0;
        setitemMainDataByGroupBeanList(new ArrayList<>());
        setItemMainDataByGroupSearchViewBean(new ItemMainDataByGroupSearchBean());
        getItemMainDataByGroupSearchBean().setBranchId(getUserData().getUserBranch().getId());
        getItemMainDataByGroupSearchBean().setDateFrom(getUserData().getGlYear().getDateFrom());
        getItemMainDataByGroupSearchBean().setDateTo(getUserData().getGlYear().getDateTo());
        setItemMainDataByGroupViewList(new ArrayList<>());

        if (uri.contains("basicdataofitemsbygroupsreport")) {
            fillRepoList();
            for (InvInventory inv : invInventoryIteratedList) {
                dataByGroupBean = new itemMainDataByGroupBean();
                dataByGroupBean.setItemName(inv.getName());
                IntializeSearchLab();
                repositoryBalance = BigDecimal.ZERO;
                itemMainDataByGroupViewBean = new itemMainDataByGroupBean();
                itemMainDataByGroupViewBean.setInventoryName(inv.getName());
                itemMainDataByGroupViewBean.setItemName("المستودع");
                getItemMainDataByGroupSearchViewBean().setFrominventory(inv);
                getItemMainDataByGroupSearchViewBean().setToinventory(inv);
                getItemMainDataByGroupSearchViewBean().setBranchId(getUserData().getUserBranch().getId());
                getItemMainDataByGroupSearchViewBean().setFromGroup(itemMainDataByGroupSearchBean.getFromGroup());
                getItemMainDataByGroupSearchViewBean().setToGroup(itemMainDataByGroupSearchBean.getToGroup());
                //  getItemMainDataByGroupSearchViewBean().setDateFrom(getUserData().getGlYear().getDateFrom());
                // getItemMainDataByGroupSearchViewBean().setDateTo(getUserData().getGlYear().getDateTo());
                itemMainDataByGroupViewList = InvItemMainDataByGroupReportService.getAllItemMainDataByGroupViewByItemMainDataByGroupSearchBean(getItemMainDataByGroupSearchViewBean());
                if (itemMainDataByGroupViewList != null && !itemMainDataByGroupViewList.isEmpty()) {
                    fillGroupMapWithCorrespondingItem();
                    prepareRootGroupValues();
                }
                mergeGroupsWithItems();
            }
        } else {
            // dataByGroupBean = new itemMainDataByGroupBean();
            IntializeSearchLab();
            itemMainDataByGroupViewBean = new itemMainDataByGroupBean();
            getItemMainDataByGroupSearchViewBean().setBranchId(getUserData().getUserBranch().getId());
            getItemMainDataByGroupSearchViewBean().setFromGroup(itemMainDataByGroupSearchBean.getFromGroup());
            getItemMainDataByGroupSearchViewBean().setToGroup(itemMainDataByGroupSearchBean.getToGroup());
//            invItemService.getInvItemByBranchId(branchId);
            invItemsReport = invItemService.getInvItemReportForInvItemMainDataByGroupReportMB(itemMainDataByGroupSearchBean);
            invGroupSearchList = getInvGroupService().getInvGroupByGroupId(itemMainDataByGroupSearchBean);
            if (invItems != null && !invItems.isEmpty()) {
                fillGroupMapWithCorrespondingItemFromInvItem();
                prepareRootGroupValuesForItems();
            }
            mergeGroupsWithItemsForItems();

            if (itemMainDataByGroupSearchBean.getGroupSelected() != null && !itemMainDataByGroupSearchBean.getGroupSelected().isEmpty()
                    && itemMainDataByGroupSearchBean.getGroupSelected().size() > 0) {
                List<itemMainDataByGroupBean> finalBean = new ArrayList<>();
                for (itemMainDataByGroupBean v : getitemMainDataByGroupBeanList()) {
                    if (v.getLevel() != null) {
                        if (itemMainDataByGroupSearchBean.getGroupSelected() != null
                                && itemMainDataByGroupSearchBean.getGroupSelected().contains(v.getGroupId().toString())) {
                            finalBean.add(v);
                        }
                    } else {
                        finalBean.add(v);
                    }

                }
                setitemMainDataByGroupBeanList(finalBean);
            }
        }

    }

    public void fillRepoList() {
        invInventoryIteratedList = new ArrayList<>();
        if (invInventoryList != null && !invInventoryList.isEmpty()) {
            if (getItemMainDataByGroupSearchBean().getFrominventory() != null || getItemMainDataByGroupSearchBean().getFrominventory() != null) {
                for (InvInventory inv : invInventoryList) {
                    if (getItemMainDataByGroupSearchBean().getFrominventory().getCode() != null && inv.getCode().compareTo(getItemMainDataByGroupSearchBean().getFrominventory().getCode()) == 0) {
                        invInventoryIteratedList.add(inv);
                    }
                    if (getItemMainDataByGroupSearchBean().getToinventory().getCode() != null && inv.getCode().compareTo(getItemMainDataByGroupSearchBean().getToinventory().getCode()) == 0) {
                        invInventoryIteratedList.add(inv);
                    }
                    if (getItemMainDataByGroupSearchBean().getFrominventory().getCode() != null && inv.getCode().compareTo(getItemMainDataByGroupSearchBean().getFrominventory().getCode()) == 1
                            && inv.getCode().compareTo(getItemMainDataByGroupSearchBean().getToinventory().getCode()) == -1 && getItemMainDataByGroupSearchBean().getToinventory().getCode() != null) {
                        invInventoryIteratedList.add(inv);
                    }
                }
            } else {
                invInventoryIteratedList = new ArrayList<>(invInventoryList);
            }
        }
    }

    public void prepareRootGroupValues() {
        if (invGroupRootList != null && !invGroupRootList.isEmpty()) {
            for (InvGroup ig : invGroupRootList) {
                itemMainDataByGroupBean imdbgb = new itemMainDataByGroupBean();
                imdbgb.setGroupId(ig.getId());
                imdbgb.setGroupName(ig.getName());
                itemMainMap.put(ig.getId(), imdbgb);
                findListFromMapAndPutValue(ig, imdbgb);
                getItemGroupTreeMap().put(stripMapReference++, itemMainMap.get(ig.getId()));
                getChildTreeNodesForGroup(ig);
            }
        }
    }

    public void prepareRootGroupValuesForItems() {
        if (invGroupRootList != null && !invGroupRootList.isEmpty()) {
            for (InvGroup ig : invGroupRootList) {
                level = 1;
                itemMainDataByGroupBean imdbgb = new itemMainDataByGroupBean();
                imdbgb.setGroupId(ig.getId());
                imdbgb.setItemName(ig.getName());
                imdbgb.setGroupId(ig.getId());
                imdbgb.setLevel(level);
                itemMainMap.put(ig.getId(), imdbgb);
                getItemGroupTreeMap().put(stripMapReference++, itemMainMap.get(ig.getId()));
                getChildTreeNodesForGroupForItems(ig);
            }
        }
    }

    private void getChildTreeNodesForGroupForItems(InvGroup grp) {
        if (grp.getInvGroupCollection() != null && !grp.getInvGroupCollection().isEmpty()) {
            level++;
            for (InvGroup childGrp : grp.getInvGroupCollection()) {
                itemMainDataByGroupBean imdbgb = new itemMainDataByGroupBean();
                imdbgb.setGroupId(childGrp.getId());
                imdbgb.setItemName(childGrp.getName());
                imdbgb.setLevel(level);
                itemMainMap.put(childGrp.getId(), imdbgb);
                getItemGroupTreeMap().put(stripMapReference++, itemMainMap.get(childGrp.getId()));
                getChildTreeNodesForGroupForItems(childGrp);
            }
        }
    }

    private void getChildTreeNodesForGroup(InvGroup grp) {
        if (grp.getInvGroupCollection() != null && !grp.getInvGroupCollection().isEmpty()) {
            for (InvGroup childGrp : grp.getInvGroupCollection()) {
                itemMainDataByGroupBean imdbgb = new itemMainDataByGroupBean();
                imdbgb.setGroupId(childGrp.getId());
                imdbgb.setGroupName(childGrp.getName());
                findListFromMapAndPutValue(childGrp, imdbgb);
                itemMainMap.put(childGrp.getId(), imdbgb);
                getItemGroupTreeMap().put(stripMapReference++, itemMainMap.get(childGrp.getId()));
                getChildTreeNodesForGroup(childGrp);
            }
        }
    }

    private void findListFromMapAndPutValue(InvGroup childGrp, itemMainDataByGroupBean srvb) {
        if (itemGroupmap.containsKey(childGrp.getId())) {
            if (itemGroupmap.get(childGrp.getId()) != null && !itemGroupmap.get(childGrp.getId()).isEmpty()) {
                BigDecimal decimal = BigDecimal.ZERO;
                for (ItemMainDataByGroupView groupView : itemGroupmap.get(childGrp.getId())) {
                    decimal = decimal.add(groupView.getQty());
                }
                srvb.setQty(decimal);
                if (childGrp.getParent() != null) {
                    putValueOfParent(childGrp.getParent(), decimal);
                } else {
                    BigDecimal qtyAll = itemMainMap.get(childGrp.getId()).getQty();
                    repositoryBalance = repositoryBalance.add(qtyAll != null ? qtyAll : BigDecimal.ZERO);
                }
            }
        }
    }

    public void putValueOfParent(InvGroup ParentGrp, BigDecimal bd) {
        if (itemMainMap.containsKey(ParentGrp.getId())) {
            itemMainDataByGroupBean bean = itemMainMap.get(ParentGrp.getId());
            bean.setQty(bd != null ? bd : BigDecimal.ZERO);
            bean.setQty(bean.getQty().add(bd));
            if (ParentGrp.getParent() != null) {
                putValueOfParent(ParentGrp.getParent(), bd);
            }
        }
    }

    public void mergeGroupsWithItems() {
        dataByGroupBean.setQty(repositoryBalance.setScale(2, BigDecimal.ROUND_UP));
        getitemMainDataByGroupBeanList().add(dataByGroupBean);
        for (Map.Entry<Integer, itemMainDataByGroupBean> entry : getItemGroupTreeMap().entrySet()) {
            itemMainDataByGroupBean viewBean = entry.getValue();
            getitemMainDataByGroupBeanList().add(viewBean);
            if (itemGroupmap.containsKey(viewBean.getGroupId())) {
                if (itemGroupmap.get(viewBean.getGroupId()) != null && !itemGroupmap.get(viewBean.getGroupId()).isEmpty()) {
                    for (ItemMainDataByGroupView idv : itemGroupmap.get(viewBean.getGroupId())) {
                        itemMainDataByGroupBean reportViewBean = new itemMainDataByGroupBean();
                        reportViewBean.setCostAverage(idv.getCostAverage());
                        reportViewBean.setGroupId(idv.getGroupId());

                        reportViewBean.setGroupName(idv.getGroupName());
                        reportViewBean.setItemCode(idv.getItemCode());
                        reportViewBean.setItemId(idv.getItemId());
                        reportViewBean.setQty(idv.getQty());

                        getitemMainDataByGroupBeanList().add(reportViewBean);
                    }
                }
            }
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    public void mergeGroupsWithItemsForItems() {

        for (Map.Entry<Integer, itemMainDataByGroupBean> entry : getItemGroupTreeMap().entrySet()) {
            itemMainDataByGroupBean viewBean = entry.getValue();
            getitemMainDataByGroupBeanList().add(viewBean);
            if (itemGroupmap.containsKey(viewBean.getGroupId())) {
                if (itemGroupmap.get(viewBean.getGroupId()) != null && !itemGroupmap.get(viewBean.getGroupId()).isEmpty()) {
                    for (ItemMainDataByGroupView idv : itemGroupmap.get(viewBean.getGroupId())) {
                        itemMainDataByGroupBean reportViewBean = new itemMainDataByGroupBean();
                        reportViewBean.setCostAverage(idv.getCostAverage());
                        reportViewBean.setGroupId(idv.getGroupId());
                        reportViewBean.setItemCode(idv.getItemCode());
                        reportViewBean.setItemId(idv.getItemId());
                        reportViewBean.setItemUnitName(idv.getItemUnitName());
                        reportViewBean.setQty(idv.getQty());
                        reportViewBean.setItemName(idv.getItemName());
                        reportViewBean.setCost(idv.getSellPrice() != null ? new BigDecimal(idv.getSellPrice()) : BigDecimal.ZERO);
                        getitemMainDataByGroupBeanList().add(reportViewBean);
                    }
                }
            }
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    private void fillGroupMapWithCorrespondingItem() {
        if (itemMainDataByGroupViewList != null && !itemMainDataByGroupViewList.isEmpty()) {
            for (ItemMainDataByGroupView groupView : itemMainDataByGroupViewList) {
                if (itemGroupmap.containsKey(groupView.getGroupId())) {
                    List list = itemGroupmap.get(groupView.getGroupId());
                    list.add(groupView);
                    itemGroupmap.put(groupView.getGroupId(), list);
                } else {
                    List list = new ArrayList();
                    list.add(groupView);
                    itemGroupmap.put(groupView.getGroupId(), list);
                }
            }
        }
    }

    private void fillGroupMapWithCorrespondingItemFromInvItem() {
        for (InvItem groupView : invItemsReport) {
            ItemMainDataByGroupView groupView1 = new ItemMainDataByGroupView();
            if (itemGroupmap.containsKey(groupView.getGroupId().getId())) {
                List list = itemGroupmap.get(groupView.getGroupId().getId());
                converInvItemToItemMainDataByGroupView(groupView1, groupView);
                list.add(groupView1);
                itemGroupmap.put(groupView.getGroupId().getId(), list);
            } else {
                List list = new ArrayList();
                converInvItemToItemMainDataByGroupView(groupView1, groupView);
                list.add(groupView1);
                itemGroupmap.put(groupView.getGroupId().getId(), list);
            }
        }
    }

    public void converInvItemToItemMainDataByGroupView(ItemMainDataByGroupView groupView1, InvItem groupView) {
        groupView1.setBranchId(groupView.getBranchId().getId());
        groupView1.setGroupCode((groupView.getGroupId() != null && groupView.getGroupId().getCode() != null) ? groupView.getGroupId().getCode().toString() : "");
        groupView1.setGroupId(groupView.getGroupId() != null ? groupView.getGroupId().getId() : null);
        groupView1.setItemCode(groupView.getCode());
        groupView1.setItemId(groupView.getId());
        groupView1.setItemName(groupView.getName());
        groupView1.setItemUnitName(groupView.getUnitId() != null ? groupView.getUnitId().getName() : "");
        groupView1.setCostAverage(groupView.getCostAverage() != null ? groupView.getCostAverage() : BigDecimal.ZERO);
        groupView1.setSellPrice(groupView.getSellPrice() != null ? groupView.getSellPrice().toString() : "");
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (getitemMainDataByGroupBeanList() != null && !getitemMainDataByGroupBeanList().isEmpty()) {
            if (uri.contains("basicdataofitemsbygroupsreport")) {
                fillReport(prepareReport(), getUserData().getReportPath() + "InvItemMainDataByGroupReport.jasper", getitemMainDataByGroupBeanList(), "pdf");
            } else {
                fillReport(prepareReport(), getUserData().getReportPath() + "basicdataofitemsbygroupsreport.jasper", getitemMainDataByGroupBeanList(), "pdf");
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
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        hashMap.put("dateText", userDDs.get("DATE"));
        hashMap.put("header2", userDDs.get("ITEMS_REPORT"));
        hashMap.put("levelText", userDDs.get("LEVEL"));
        hashMap.put("groupText", userDDs.get("GROUP"));
        hashMap.put("groupName", userDDs.get("GROUP_NAME"));

        hashMap.put("itemNum", userDDs.get("ITEM_NUMBER"));
        hashMap.put("itemName", userDDs.get("ITEM_NAME"));

        hashMap.put("unit", userDDs.get("UNIT"));

        hashMap.put("netCost", userDDs.get("COST"));
        hashMap.put("quantity", userDDs.get("QUANTITY_WARE"));
        hashMap.put("costText", userDDs.get("SELLING_PRICE"));

        if (itemMainDataByGroupSearchBean.getGroupSelected() != null && !itemMainDataByGroupSearchBean.getGroupSelected().isEmpty()) {
            String groupSelected = "";
            int index = 0;
            for (InvGroup invGroup : invGroupSubList) {
                if (itemMainDataByGroupSearchBean.getGroupSelected().size() == index) {
                    break;
                }
                if (invGroup.getId() == Integer.valueOf(itemMainDataByGroupSearchBean.getGroupSelected().get(index))) {
                    groupSelected += invGroup.getName() + " - ";
                    index++;
                }
            }
            hashMap.put("groupLabel", " مجموعة");
            if (!groupSelected.isEmpty()) {
                hashMap.put("groupValue", groupSelected.substring(0, groupSelected.lastIndexOf("-")));
            }
            if (itemMainDataByGroupSearchBean.getFromItem() != null) {
                hashMap.put("fromItemText", "من صنف");
                hashMap.put("fromItem", itemMainDataByGroupSearchBean.getFromItem().getName());
            }
            if (itemMainDataByGroupSearchBean.getToItem() != null) {
                hashMap.put("toItemText", "الي صنف");
                hashMap.put("toItem", itemMainDataByGroupSearchBean.getToItem().getName());
            }
        }
            return hashMap;
    }
    public List<InvGroup> completGroup(String query) {
        List<InvGroup> invGroList = getInvGroupList();
        if (query == null || query.trim().equals("")) {

            setInvGroupConverter(new InvGroupConverter(invGroList));
            return invGroList;
        }
        List<InvGroup> filteredInvs = new ArrayList<>();

        String nameAr;
        String code;
        InvGroup invGroFilter;

        for (int i = 0; i < getInvGroupList().size(); i++) {
            invGroFilter = invGroList.get(i);
            nameAr = invGroFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invGroFilter)) {
                    filteredInvs.add(invGroFilter);
                }
            }

            code = invGroFilter.getCode().toString();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invGroFilter)) {
                    filteredInvs.add(invGroFilter);
                }
            }
        }

        setInvGroupConverter(new InvGroupConverter(filteredInvs));
        return filteredInvs;
    }

    public List<InvInventory> completInventory(String query) {
        List<InvInventory> invinventoryList = getInvInventoryList();
        if (query == null || query.trim().equals("")) {

            setInvInventoryConverter(new InvInventoryConverter(invinventoryList));
            return invinventoryList;
        }
        List<InvInventory> filteredInvs = new ArrayList<>();

        String nameAr;
        String code;
        InvInventory invinventoryFilter;

        for (int i = 0; i < getInvInventoryList().size(); i++) {
            invinventoryFilter = invinventoryList.get(i);
            nameAr = invinventoryFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invinventoryFilter)) {
                    filteredInvs.add(invinventoryFilter);
                }
            }

            code = invinventoryFilter.getCode().toString();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(invinventoryFilter)) {
                    filteredInvs.add(invinventoryFilter);
                }
            }
        }

        setInvInventoryConverter(new InvInventoryConverter(filteredInvs));
        return filteredInvs;
    }

    public List<InvItem> completItem(String query) {
        List<InvItem> itemList = getInvItems();
        if (query == null || query.trim().equals("")) {

            setItemConverter(new ItemConverter(itemList));
            return itemList;
        }
        List<InvItem> filteredInvs = new ArrayList<>();

        String nameAr;
        String code;
        InvItem itemFilter;

        for (int i = 0; i < getInvItems().size(); i++) {
            itemFilter = filteredInvs.get(i);
            nameAr = itemFilter.getName();
            if (nameAr != null && nameAr.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(itemFilter)) {
                    filteredInvs.add(itemFilter);
                }
            }

            code = itemFilter.getCode().toString();
            if (code != null && code.toLowerCase().contains(query.toLowerCase())) {
                if (!filteredInvs.contains(itemFilter)) {
                    filteredInvs.add(itemFilter);
                }
            }
        }

        setItemConverter(new ItemConverter(filteredInvs));
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

    public String getScreenMode() {
        return screenMode;
    }

    public void setScreenMode(String screenMode) {
        this.screenMode = screenMode;
    }

    /*  public Boolean getIsSales() {
     return isSales;
     }

     public void setIsSales(Boolean isSales) {
     this.isSales = isSales;
     } */
    public ItemMainDataByGroupSearchBean getItemMainDataByGroupSearchBean() {
        return itemMainDataByGroupSearchBean;
    }

    public void setItemMainDataByGroupSearchBean(ItemMainDataByGroupSearchBean ItemMainDataByGroupSearchBean) {
        this.itemMainDataByGroupSearchBean = ItemMainDataByGroupSearchBean;
    }

    /**
     * @return the InvGroupList
     */
    public List<InvGroup> getInvGroupList() {
        return invGroupList;
    }

    /**
     * @param InvGroupList the InvGroupList to set
     */
    public void setInvGroupList(List<InvGroup> InvGroupList) {
        this.invGroupList = InvGroupList;
    }

    /**
     * @return the invGroupConverter
     */
    public InvGroupConverter getInvGroupConverter() {
        return invGroupConverter;
    }

    /**
     * @param invGroupConverter the invGroupConverter to set
     */
    public void setInvGroupConverter(InvGroupConverter invGroupConverter) {
        this.invGroupConverter = invGroupConverter;
    }

    /**
     * @return the ItemMainDataByGroupViewList
     */
    public List<ItemMainDataByGroupView> getItemMainDataByGroupViewList() {

        return itemMainDataByGroupViewList;
    }

    /**
     * @param ItemMainDataByGroupViewList the ItemMainDataByGroupViewList to set
     */
    public void setItemMainDataByGroupViewList(List<ItemMainDataByGroupView> ItemMainDataByGroupViewList) {
        this.itemMainDataByGroupViewList = ItemMainDataByGroupViewList;
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
     * @return the InvItemMainDataByGroupReportService
     */
    public InvItemMainDataByGroupReportService getInvItemMainDataByGroupReportService() {
        return InvItemMainDataByGroupReportService;
    }

    /**
     * @param InvItemMainDataByGroupReportService the
     * InvItemMainDataByGroupReportService to set
     */
    public void setInvItemMainDataByGroupReportService(InvItemMainDataByGroupReportService InvItemMainDataByGroupReportService) {
        this.InvItemMainDataByGroupReportService = InvItemMainDataByGroupReportService;
    }

    /**
     * @return the InvGroupService
     */
    public InvGroupService getInvGroupService() {
        return InvGroupService;
    }

    /**
     * @param InvGroupService the InvGroupService to set
     */
    public void setInvGroupService(InvGroupService InvGroupService) {
        this.InvGroupService = InvGroupService;
    }

    /**
     * @return the itemGroupmap
     */
    public Map<Integer, List<ItemMainDataByGroupView>> getItemGroupmap() {
        return itemGroupmap;
    }

    /**
     * @param itemGroupmap the itemGroupmap to set
     */
    public void setItemGroupmap(Map<Integer, List<ItemMainDataByGroupView>> itemGroupmap) {
        this.itemGroupmap = itemGroupmap;
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
     * @return the itemMainMap
     */
    public Map<Integer, itemMainDataByGroupBean> getItemMainMap() {
        return itemMainMap;
    }

    /**
     * @param itemMainMap the itemMainMap to set
     */
    public void setItemMainMap(Map<Integer, itemMainDataByGroupBean> itemMainMap) {
        this.itemMainMap = itemMainMap;
    }

    /**
     * @return the itemGroupTreeMap
     */
    public TreeMap<Integer, itemMainDataByGroupBean> getItemGroupTreeMap() {
        return itemGroupTreeMap;
    }

    /**
     * @param itemGroupTreeMap the itemGroupTreeMap to set
     */
    public void setItemGroupTreeMap(TreeMap<Integer, itemMainDataByGroupBean> itemGroupTreeMap) {
        this.itemGroupTreeMap = itemGroupTreeMap;
    }

    /**
     * @return the itemMainDataByGroupBeanList
     */
    public List<itemMainDataByGroupBean> getitemMainDataByGroupBeanList() {
        return itemMainDataByGroupBeanList;
    }

    /**
     * @param itemMainDataByGroupBeanList the itemMainDataByGroupBeanList to set
     */
    public void setitemMainDataByGroupBeanList(List<itemMainDataByGroupBean> itemMainDataByGroupBeanList) {
        this.itemMainDataByGroupBeanList = itemMainDataByGroupBeanList;
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
     * @return the itemMainDataByGroupSearchViewBean
     */
    public ItemMainDataByGroupSearchBean getItemMainDataByGroupSearchViewBean() {
        return itemMainDataByGroupSearchViewBean;
    }

    /**
     * @param itemMainDataByGroupSearchViewBean the
     * itemMainDataByGroupSearchViewBean to set
     */
    public void setItemMainDataByGroupSearchViewBean(ItemMainDataByGroupSearchBean itemMainDataByGroupSearchViewBean) {
        this.itemMainDataByGroupSearchViewBean = itemMainDataByGroupSearchViewBean;
    }

    /**
     * @return the invItems
     */
    public List<InvItem> getInvItems() {
        return invItems;
    }

    /**
     * @param invItems the invItems to set
     */
    public void setInvItems(List<InvItem> invItems) {
        this.invItems = invItems;
    }

    /**
     * @return the itemConverter
     */
    public ItemConverter getItemConverter() {
        return itemConverter;
    }

    /**
     * @param itemConverter the itemConverter to set
     */
    public void setItemConverter(ItemConverter itemConverter) {
        this.itemConverter = itemConverter;
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

}
