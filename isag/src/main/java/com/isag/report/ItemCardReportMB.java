/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.report;

import com.isag.businessservice.InvItemService;
import com.isag.businessservice.IsagUserInventoryService;
import com.isag.businessservice.ItemsDataViewService;
import com.isag.businessservice.reports.InventoryStripReportService;
import com.isag.businessservice.reports.searchBean.ItemCardReportSerachBean;
import com.isag.converter.InvInventoryConverter;
import com.isag.converter.ItemDTOConverter;
import com.isag.define.ScreenNameClassEnum;
import com.isag.dto.InvItemDTO;
import com.isag.entity.InvInventory;
import com.isag.isag.BaseReportBean;
import com.isag.isag.UserData;
import com.isag.report.entity.ItemCardReportEntity;
import com.isag.views.ItemsDataView;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author hhhh
 */
@Named(value = "ItemCardReportMB")
@ViewScoped
public class ItemCardReportMB extends BaseReportBean {
    // Lists and Converters

    private List<InvInventory> invInventoryList;
    private InvInventoryConverter invInventoryConverter;
    private List<InvItemDTO> invItemList;
    private ItemDTOConverter itemConverter;

    // Bean Objs and Lists
    private List<ItemsDataView> itemsDataViewList;
    private List<ItemCardReportEntity> itemCardReportEntityList;

    // Objects
    private Integer branchId;
    private String screenMode;

    private ItemCardReportSerachBean itemCardReportSerachBean;
    private BigDecimal qtyInTotal;
    private BigDecimal qtyoutTotal;
    private BigDecimal cartonCapacity;

    // EJBs
    @EJB
    private IsagUserInventoryService isagUserInventoryService;
    @EJB
    private InvItemService invItemService;
    @EJB
    private InventoryStripReportService inventoryStripReportService;
    @EJB
    private ItemsDataViewService itemsDataViewService;

    @Override
    @PostConstruct
    public void load() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        setUserData((UserData) context.getSessionMap().get("userLogInData"));
        setScreenMode((String) context.getSessionMap().get("ScreenMode"));
        branchId = getUserData().getUserBranch().getId();
        initObjs();

        fillLists();
    }

    private void initObjs() {
        itemCardReportSerachBean = new ItemCardReportSerachBean();

        itemCardReportSerachBean.setBranchId(branchId);
        invInventoryList = new ArrayList<>();
        invItemList = new ArrayList<>();
        itemCardReportEntityList = new ArrayList<>();
    }

    private void fillLists() {
        invInventoryList = isagUserInventoryService.getAllInventroisByUserAndBranchPer(getUserData().getUser().getId(), branchId);
        invInventoryConverter = new InvInventoryConverter(invInventoryList);

        invItemList = invItemService.getInvItemListByBranchId(branchId, ScreenNameClassEnum.ITEMCARDREPORT);
        itemConverter = new ItemDTOConverter(invItemList);
    }

    @Override
    public void reset() {
        itemCardReportSerachBean = new ItemCardReportSerachBean();
        itemCardReportSerachBean.setBranchId(branchId);
        itemCardReportEntityList = new ArrayList<>();
        itemsDataViewList = new ArrayList<>();
    }

    @Override
    public void search() {
        itemsDataViewList = itemsDataViewService.getAllItemCardByInvStripSearchBean(itemCardReportSerachBean, getUserData().getGlYear());
        ItemCardReportEntity itemCardReport;
        BigDecimal balance = BigDecimal.ZERO;
        itemCardReportEntityList = new ArrayList<>();
        qtyInTotal = BigDecimal.ZERO;
        qtyoutTotal = BigDecimal.ZERO;
        Map<Integer, BigDecimal> balanceMap = new HashMap<>();
        for (ItemsDataView itemsDataView : itemsDataViewList) {
            itemCardReport = new ItemCardReportEntity();
            itemCardReport.setId(itemsDataView.getItemId());
            itemCardReport.setDate(itemsDataView.getDate());
            itemCardReport.setQtyin(itemsDataView.getQtyin() != null ? itemsDataView.getQtyin().multiply(itemsDataView.getScrewing()) : BigDecimal.ZERO);
            itemCardReport.setQtyout(itemsDataView.getQtyout() != null ? itemsDataView.getQtyout().multiply(itemsDataView.getScrewing()) : BigDecimal.ZERO);
            itemCardReport.setTotalQtyin(itemsDataView.getTotalQtyIn());
            itemCardReport.setTotalQtyout(itemsDataView.getTotalQtyOut());

            if (itemsDataView.getCartonsCapacity() != null && itemsDataView.getCartonsCapacity().compareTo(BigDecimal.ZERO) == 1) {
                cartonCapacity = itemsDataView.getCartonsCapacity();
                itemCardReport.setQtyinCarton((itemCardReport.getQtyin().divide(itemsDataView.getCartonsCapacity(), 3, RoundingMode.HALF_DOWN)));
                itemCardReport.setQtyoutCarton((itemCardReport.getQtyout().divide(itemsDataView.getCartonsCapacity(), 3, RoundingMode.HALF_DOWN)));
                itemCardReport.setTotalQtyinCarton((itemCardReport.getTotalQtyin().divide(itemsDataView.getCartonsCapacity(), 3, RoundingMode.HALF_DOWN)));
                itemCardReport.setTotalQtyoutCarton((itemCardReport.getTotalQtyout().divide(itemsDataView.getCartonsCapacity(), 3, RoundingMode.HALF_DOWN)));
            }

            balance = BigDecimal.ZERO;
            if (balanceMap.containsKey(itemsDataView.getItemId())) {
                balance = balanceMap.get(itemsDataView.getItemId());
                balance = balance.add(itemCardReport.getQtyin().subtract(itemCardReport.getQtyout()));
                balanceMap.put(itemsDataView.getItemId(), balance);
            } else {
                balance = balance.add(itemCardReport.getQtyin().subtract(itemCardReport.getQtyout()));
                if (itemsDataView.getOpeningBalance() != null) {
                    balance = balance.add(itemsDataView.getOpeningBalance());
                }
                balanceMap.put(itemsDataView.getItemId(), balance);
            }

            // balance = balance.add(itemCardReport.getQtyin().subtract(itemCardReport.getQtyout()));
            /* int tempItemId = 0;
             if (tempItemId != itemsDataView.getItemId()) {
             balance = balance.add(itemsDataView.getOpeningBalance() != null ? itemsDataView.getOpeningBalance() : BigDecimal.ZERO);
             tempItemId = itemsDataView.getItemId();
             }*/
            itemCardReport.setBalance(balance);
            if (getCartonCapacity().compareTo(BigDecimal.ZERO) == 1) {
                itemCardReport.setBalance(balance.divide(getCartonCapacity(), 3, RoundingMode.HALF_DOWN));
            }

            itemCardReport.setSerial(itemsDataView.getSerial());
            itemCardReport.setTransType(itemsDataView.getScreenName());

            itemCardReport.setItemName(itemsDataView.getItemName());
            itemCardReport.setItemCode(itemsDataView.getItemCode());
            itemCardReport.setItemUnitName(itemsDataView.getItemUnitName());
            itemCardReport.setGroupName(itemsDataView.getGroupName());

            itemCardReport.setItemId(itemsDataView.getItemId());
            itemCardReport.setInventoryCode(itemsDataView.getInventoryCode());
            itemCardReport.setInventoryName(itemsDataView.getInventoryName());
            itemCardReport.setOpeningBalance(itemsDataView.getOpeningBalance());
            setQtyInTotal(getQtyInTotal().add(itemsDataView.getQtyin() != null ? itemsDataView.getQtyin() : BigDecimal.ZERO));
            setQtyoutTotal(getQtyoutTotal().add(itemsDataView.getQtyout() != null ? itemsDataView.getQtyout() : BigDecimal.ZERO));
            getItemCardReportEntityList().add(itemCardReport);
        }
    }

    @Override
    public HashMap prepareReport() {
        Map<String, String> userDDs = getUserData().getUserDDs();
        HashMap hashMap = new HashMap();

        hashMap.put("UserName", getUserData().getUser().getName());
        hashMap.put("Branch", getUserData().getUserBranch().getNameAr());
        //  hashMap.put("CompanyName", getUserData().getCompany().getName());

        //  hashMap.put("CompanyLogo", getUserData().getImageReportPath());
        if (isFileExist(getUserData().getImageReportPath())) {
            hashMap.put("companyImage", getUserData().getImageReportPath());
        } else {
            hashMap.put("companyImage", null);
        }
        hashMap.put("dateText", userDDs.get("DATE"));

        hashMap.put("header", userDDs.get("GENE_PROD_STOR"));
        hashMap.put("header2", userDDs.get("ITEM_CARD"));
        hashMap.put("inventoryText", userDDs.get("DEPOSITORY"));

        hashMap.put("DateFromLabel", userDDs.get("FROM_PERIOD"));
        hashMap.put("DateToLabel", userDDs.get("TO_PERIOD"));

        hashMap.put("DateFromValue", itemCardReportSerachBean.getDateFrom());
        hashMap.put("DateToValue", itemCardReportSerachBean.getDateTo());

        hashMap.put("ReceiptNum", userDDs.get("TRANSACTION_NO"));
        hashMap.put("ReceiptDate", userDDs.get("TRANSACTION_DATE"));

        hashMap.put("transType", userDDs.get("TRANSACTION_TYPE"));
        hashMap.put("qtyIn", userDDs.get("IMPORT"));

        hashMap.put("qtyOut", userDDs.get("EXPORT"));
        hashMap.put("balance", userDDs.get("BALANCE"));

        hashMap.put("itemNameText", userDDs.get("ITEM_NAME"));
        hashMap.put("totalText", userDDs.get("TOTAL"));
        hashMap.put("beforeDateBalanceText", userDDs.get("PRE-BALANCE"));
        return hashMap;
    }

    @Override
    public void exportPDF(ActionEvent actionEvent) throws JRException, IOException {
        search();
        fillReport(prepareReport(), getUserData().getReportPath() + "ItemCardReport.jasper", itemCardReportEntityList, "pdf");
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

    public List<InvItemDTO> completInvItem(String query) {
        List<InvItemDTO> invList = invItemList;
        if (query == null || query.trim().equals("")) {

            itemConverter = new ItemDTOConverter(invList);
            return invList;
        }
        List<InvItemDTO> filteredInvs = new ArrayList<>();

        String nameAr;
        String code;
        InvItemDTO invFilter;
        for (int i = 0; i < invItemList.size(); i++) {
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

        itemConverter = new ItemDTOConverter(filteredInvs);
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

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public String getScreenMode() {
        return screenMode;
    }

    public void setScreenMode(String screenMode) {
        this.screenMode = screenMode;
    }

    public ItemCardReportSerachBean getItemCardReportSerachBean() {
        return itemCardReportSerachBean;
    }

    public void setItemCardReportSerachBean(ItemCardReportSerachBean itemCardReportSerachBean) {
        this.itemCardReportSerachBean = itemCardReportSerachBean;
    }

    public List<InvItemDTO> getInvItemList() {
        return invItemList;
    }

    public void setInvItemList(List<InvItemDTO> invItemList) {
        this.invItemList = invItemList;
    }

    public ItemDTOConverter getItemConverter() {
        return itemConverter;
    }

    public void setItemConverter(ItemDTOConverter itemConverter) {
        this.itemConverter = itemConverter;
    }

    public List<ItemsDataView> getItemsDataViewList() {
        return itemsDataViewList;
    }

    public void setItemsDataViewList(List<ItemsDataView> itemsDataViewList) {
        this.itemsDataViewList = itemsDataViewList;
    }

    public List<ItemCardReportEntity> getItemCardReportEntityList() {
        return itemCardReportEntityList;
    }

    public void setItemCardReportEntityList(List<ItemCardReportEntity> itemCardReportEntityList) {
        this.itemCardReportEntityList = itemCardReportEntityList;
    }

    /**
     * @return the qtyInTotal
     */
    public BigDecimal getQtyInTotal() {
        if(qtyInTotal == null){
            qtyInTotal = BigDecimal.ZERO;
        }
        return qtyInTotal;
    }

    /**
     * @param qtyInTotal the qtyInTotal to set
     */
    public void setQtyInTotal(BigDecimal qtyInTotal) {
        this.qtyInTotal = qtyInTotal;
    }

    /**
     * @return the qtyoutTotal
     */
    public BigDecimal getQtyoutTotal() {
        if(qtyoutTotal == null){
            qtyoutTotal = BigDecimal.ZERO;
        }
        return qtyoutTotal;
    }

    /**
     * @param qtyoutTotal the qtyoutTotal to set
     */
    public void setQtyoutTotal(BigDecimal qtyoutTotal) {
        this.qtyoutTotal = qtyoutTotal;
    }

    /**
     * @return the cartonCapacity
     */
    public BigDecimal getCartonCapacity() {
        if(cartonCapacity == null){
            cartonCapacity = BigDecimal.ZERO;
        }
        return cartonCapacity;
    }

    /**
     * @param cartonCapacity the cartonCapacity to set
     */
    public void setCartonCapacity(BigDecimal cartonCapacity) {
        this.cartonCapacity = cartonCapacity;
    }
}
