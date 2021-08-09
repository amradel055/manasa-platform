/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.InvGroupService;
import com.isag.businessservice.InvItemService;
import com.isag.businessservice.IsagUserInventoryService;
import com.isag.businessservice.reports.InvItemMainDataByGroupReportService;
import com.isag.businessservice.reports.InventoryStripReportService;
import com.isag.businessservice.reports.searchBean.ItemCardReportSerachBean;
import com.isag.businessservice.reports.searchBean.ItemMainDataByGroupSearchBean;
import com.isag.converter.InvGroupConverter;
import com.isag.converter.InvInventoryConverter;
import com.isag.converter.ItemConverter;
import com.isag.entity.InvGroup;
import com.isag.entity.InvInventory;
import com.isag.entity.InvItem;
import com.isag.isag.BaseReportBean;
import com.isag.isag.UserData;
import com.isag.report.entity.InventoryItemsQuantity;
import com.isag.report.entity.itemMainDataByGroupBean;
import com.isag.views.ItemMainDataByGroupView;
import com.isag.views.ItemsDataView;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
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
@Named("invItemOrderLimitReportMB")
@ViewScoped
public class InvItemOrderLimitReportMB extends BaseReportBean {

    // Lists and Converters
    private List<InvGroup> invGroupList;
    private List<InvGroup> invGroupSubList;
    private InvGroupConverter invGroupConverter;
    private List<InvInventory> invInventoryList;
    private InvInventoryConverter invInventoryConverter;
    private List<InvItem> invItems;
    private ItemConverter itemConverter;
    // Objects
    private Integer branchId;
    private String uri;
    private String screenMode;
    private ItemCardReportSerachBean itemCardReportSerachBean;
    private Map<Integer, Integer> inventoryToFieldNameMap = new HashMap<>();
    private Map<Integer, String> itemQuantityInventoryMap = new HashMap<>();
    private TreeMap<Integer, InventoryItemsQuantity> inventoryAndItemWithFinalQuantity = new TreeMap<>();
    private List<InventoryItemsQuantity> inventoryItemsQuantityList;
    private List<InventoryItemsQuantity> inventoryItemsQuantityListFinal;

    private Integer inventoryFieldNumber;
    private Boolean hiddenOrderLimit = Boolean.TRUE;

    private List<InvGroup> invGroupRootList;
    List<InvItem> invItemsReport;
    private List<ItemsDataView> invItemsQuantityList;
    private String columnTemplate;
    private List<ColumnModel> columns;
    private HashMap hashMap;
    @EJB
    private InvGroupService InvGroupService;
    @EJB
    private InvItemService invItemService;
    @EJB
    private InventoryStripReportService inventoryStripReportService;

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
        itemCardReportSerachBean = new ItemCardReportSerachBean();
        itemCardReportSerachBean.setGroupSelected(new ArrayList<>());
        for (InvGroup sublist : invGroupSubList) {
            itemCardReportSerachBean.getGroupSelected().add(sublist.getId().toString());
        }

    }

    private void fillLists() {

        setInvItems(invItemService.getInvItemByBranchIdForInvItemMainDataByGroupReportMB(branchId));
        setItemConverter(new ItemConverter(invItems));

        invGroupSubList = getInvGroupService().getSubInvGroupByBranchIdPer(branchId);
        setInvGroupList(getInvGroupService().getallInvGroupByBranchId(getBranchId()));
        setInvGroupList(invGroupSubList);
        setInvGroupConverter(new InvGroupConverter(getInvGroupList()));

    }

    @Override
    public void reset() {
        initObjs();

        itemCardReportSerachBean.setGroupSelected(new ArrayList<>());
        for (InvGroup sublist : invGroupSubList) {
            itemCardReportSerachBean.getGroupSelected().add(sublist.getId().toString());
        }
        setInvGroupList(invGroupSubList);

    }

    private void initObjs() {

        setInvInventoryList(new ArrayList<>());
        setInvGroupList(new ArrayList<>());
        itemCardReportSerachBean = new ItemCardReportSerachBean();
        itemCardReportSerachBean.setBranchId(getBranchId());
        itemQuantityInventoryMap = new HashMap<>();
        inventoryToFieldNameMap = new HashMap<>();
        inventoryAndItemWithFinalQuantity = new TreeMap<>();
        invItemsQuantityList = new ArrayList<>();
        inventoryItemsQuantityList = new ArrayList<>();

    }

    @Override
    public void search() {
        IntializeSearchLab();
        itemCardReportSerachBean.setBranchId(getUserData().getUserBranch().getId());
        inventoryFieldNumber = 1;
        invItemsQuantityList = inventoryStripReportService.getAllItemsWithInventories(itemCardReportSerachBean);
        fillInventoryAndItemWithFinalQuantity(invItemsQuantityList);
        inventoryItemsQuantityList = new ArrayList<>(inventoryAndItemWithFinalQuantity.values());
        createDynamicColumns();
        modifyData();
        UIComponent table = FacesContext.getCurrentInstance().getViewRoot().findComponent(":form:ItemMainDataByGroupViewTable");
        table.setValueExpression("sortBy", null);
    }

    public void modifyData() {
        setInventoryItemsQuantityListFinal(null);
        for (InventoryItemsQuantity itemsQuantity : inventoryItemsQuantityList) {
            BigDecimal totalAmount = itemsQuantity.getInventory_1().add(itemsQuantity.getInventory_2()).add(itemsQuantity.getInventory_3()).add(itemsQuantity.getInventory_4()).add(itemsQuantity.getInventory_5());
            itemsQuantity.setOrderLimit(itemsQuantity.getOrderLimit().add(itemCardReportSerachBean.getSaveAmount()));
            if (hiddenOrderLimit) {
                if (totalAmount.compareTo(itemsQuantity.getOrderLimit()) != 0) {
                    if (!(itemsQuantity.getInventory_1().compareTo(BigDecimal.ZERO) == 0
                            && itemsQuantity.getInventory_2().compareTo(BigDecimal.ZERO) == 0
                            && itemsQuantity.getInventory_3().compareTo(BigDecimal.ZERO) == 0
                            && itemsQuantity.getInventory_4().compareTo(BigDecimal.ZERO) == 0
                            && itemsQuantity.getInventory_5().compareTo(BigDecimal.ZERO) == 0)) {
                        if("6934442225889".equals(itemsQuantity.getItemCode())){
                            System.out.println("com.isag.report.InvItemOrderLimitReportMB.modifyData()");
                        }
                        getInventoryItemsQuantityListFinal().add(itemsQuantity);
                    }
                }
            } else {
                getInventoryItemsQuantityListFinal().add(itemsQuantity);
            }
        }
        setInventoryItemsQuantityList(getInventoryItemsQuantityListFinal());
    }

    public void IntializeSearchLab() {
        inventoryItemsQuantityList = new ArrayList<>();
        inventoryAndItemWithFinalQuantity = new TreeMap<>();
    }

    @Override
    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();

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

        if (itemCardReportSerachBean.getGroupSelected() != null && !itemCardReportSerachBean.getGroupSelected().isEmpty()) {
            String groupSelected = "";
            int index = 0;
            for (InvGroup invGroup : invGroupSubList) {
                if (itemCardReportSerachBean.getGroupSelected().size() == index) {
                    break;
                }
                if (invGroup.getId() == Integer.valueOf(itemCardReportSerachBean.getGroupSelected().get(index))) {
                    groupSelected += invGroup.getName() + " - ";
                    index++;
                }
            }
            hashMap.put("groupLabel", " مجموعة");
            if (!groupSelected.isEmpty()) {
                hashMap.put("groupValue", groupSelected.substring(0, groupSelected.lastIndexOf("-")));
            }
        }
        if (itemCardReportSerachBean.getFromItem() != null) {
            hashMap.put("fromItemText", "من صنف");
            hashMap.put("fromItem", itemCardReportSerachBean.getFromItem().getName());
        }
        if (itemCardReportSerachBean.getToItem() != null) {
            hashMap.put("toItemText", "الي صنف");
            hashMap.put("toItem", itemCardReportSerachBean.getToItem().getName());
        }
        return hashMap;
    }

    public BigDecimal fillQuantityFieldForInventory(ItemsDataView idv, InventoryItemsQuantity inventoryItemsQuantity) {

        if (inventoryToFieldNameMap != null && inventoryToFieldNameMap.containsKey(idv.getInventoryId())) {
            setQuantityField(idv, inventoryItemsQuantity);
        } else {
            inventoryToFieldNameMap.put(idv.getInventoryId(), inventoryFieldNumber);
            itemQuantityInventoryMap.put(inventoryFieldNumber, "inventory_".concat(inventoryFieldNumber.toString()).concat(",").concat(idv.getInventoryName()));
            inventoryFieldNumber++;
            setQuantityField(idv, inventoryItemsQuantity);
        }
        return BigDecimal.ZERO;
    }

    public void setQuantityField(ItemsDataView idv, InventoryItemsQuantity inventoryItemsQuantity) {
        try {
            String fieldName = "inventory_".concat(inventoryToFieldNameMap.get(idv.getInventoryId()).toString());
            Field field = InventoryItemsQuantity.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(inventoryItemsQuantity, idv.getQty());
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(InventoryItemsQuantity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        if (inventoryItemsQuantityList != null && !inventoryItemsQuantityList.isEmpty()) {
            fillReport(prepareReport(), getUserData().getReportPath() + "invItemOrderLimitReport.jasper", inventoryItemsQuantityList, "pdf");
        }

    }

    public void fillInventoryAndItemWithFinalQuantity(List<ItemsDataView> invItemsQuantityList) {
        if (invItemsQuantityList != null && !invItemsQuantityList.isEmpty()) {
            for (ItemsDataView idv : invItemsQuantityList) {
                if (inventoryAndItemWithFinalQuantity.containsKey(idv.getItemId())) {
                    InventoryItemsQuantity inventoryItemsQuantity = inventoryAndItemWithFinalQuantity.get(idv.getItemId());
                    inventoryItemsQuantity.setItemCode(idv.getItemCode());
                    inventoryItemsQuantity.setItemName(idv.getItemName());
                    inventoryItemsQuantity.setOrderLimit(idv.getMinimumAmount());
                    //inventoryItemsQuantity.setQuantity(idv.getQty() != null ? idv.getQty().setScale(2, BigDecimal.ROUND_UP) : BigDecimal.ZERO);
                    fillQuantityFieldForInventory(idv, inventoryItemsQuantity);
                    inventoryAndItemWithFinalQuantity.put(idv.getItemId(), inventoryItemsQuantity);
                } else {
                    InventoryItemsQuantity inventoryItemsQuantity = new InventoryItemsQuantity();
                    inventoryItemsQuantity.setItemCode(idv.getItemCode());
                    inventoryItemsQuantity.setItemName(idv.getItemName());
                    inventoryItemsQuantity.setOrderLimit(idv.getMinimumAmount());
                    // inventoryItemsQuantity.setQuantity(idv.getQty() != null ? idv.getQty().setScale(2, BigDecimal.ROUND_UP) : BigDecimal.ZERO);
                    fillQuantityFieldForInventory(idv, inventoryItemsQuantity);
                    inventoryAndItemWithFinalQuantity.put(idv.getItemId(), inventoryItemsQuantity);

                }
            }
        }

    }

    private void createDynamicColumns() {

        columnTemplate = new String();
        columnTemplate = "itemCode itemName orderLimit";
        hashMap = new HashMap();

        columns = new ArrayList<>();
        Map<String, String> reverseInventoryToFieldNameMap = new HashMap<>();

        reverseInventoryToFieldNameMap.put("itemCode", "رقم الصنف");
        reverseInventoryToFieldNameMap.put("itemName", "اسم الصنف");
        reverseInventoryToFieldNameMap.put("orderLimit", "حد الطلب");

        if (itemQuantityInventoryMap != null) {
            for (Map.Entry<Integer, String> entry : itemQuantityInventoryMap.entrySet()) {
                String[] columnName = entry.getValue().split(",");
                columnTemplate = columnTemplate.concat(" " + columnName[0]);
                reverseInventoryToFieldNameMap.put(columnName[0], columnName[1]);
                hashMap.put(columnName[0], columnName[1]);
            }
        }

        String[] columnKeys = columnTemplate.split(" ");

        for (String columnKey : columnKeys) {
            if (!columnKey.contains("inventory")) {
                columns.add(new ColumnModel(reverseInventoryToFieldNameMap.get(columnKey), columnKey));
            } else {
                columns.add(new ColumnModel(reverseInventoryToFieldNameMap.get(columnKey), columnKey));
            }
        }
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
     * @return the invItemsQuantityList
     */
    public List<ItemsDataView> getInvItemsQuantityList() {
        return invItemsQuantityList;
    }

    /**
     * @param invItemsQuantityList the invItemsQuantityList to set
     */
    public void setInvItemsQuantityList(List<ItemsDataView> invItemsQuantityList) {
        this.invItemsQuantityList = invItemsQuantityList;
    }

    /**
     * @return the inventoryItemsQuantityList
     */
    public List<InventoryItemsQuantity> getInventoryItemsQuantityList() {
        return inventoryItemsQuantityList;
    }

    /**
     * @param inventoryItemsQuantityList the inventoryItemsQuantityList to set
     */
    public void setInventoryItemsQuantityList(List<InventoryItemsQuantity> inventoryItemsQuantityList) {
        this.inventoryItemsQuantityList = inventoryItemsQuantityList;
    }

    /**
     * @return the itemCardReportSerachBean
     */
    public ItemCardReportSerachBean getItemCardReportSerachBean() {
        return itemCardReportSerachBean;
    }

    /**
     * @param itemCardReportSerachBean the itemCardReportSerachBean to set
     */
    public void setItemCardReportSerachBean(ItemCardReportSerachBean itemCardReportSerachBean) {
        this.itemCardReportSerachBean = itemCardReportSerachBean;
    }

    /**
     * @return the inventoryItemsQuantityListFinal
     */
    public List<InventoryItemsQuantity> getInventoryItemsQuantityListFinal() {
        if (inventoryItemsQuantityListFinal == null) {
            inventoryItemsQuantityListFinal = new ArrayList<>();
        }
        return inventoryItemsQuantityListFinal;
    }

    /**
     * @param inventoryItemsQuantityListFinal the
     * inventoryItemsQuantityListFinal to set
     */
    public void setInventoryItemsQuantityListFinal(List<InventoryItemsQuantity> inventoryItemsQuantityListFinal) {
        this.inventoryItemsQuantityListFinal = inventoryItemsQuantityListFinal;
    }

    /**
     * @return the hiddenOrderLimit
     */
    public Boolean getHiddenOrderLimit() {
        return hiddenOrderLimit;
    }

    /**
     * @param hiddenOrderLimit the hiddenOrderLimit to set
     */
    public void setHiddenOrderLimit(Boolean hiddenOrderLimit) {
        this.hiddenOrderLimit = hiddenOrderLimit;
    }
}
