package com.toby.dto;


import com.toby.entity.InventorySetup;
import com.toby.views.ItemsBarcodesDetailsView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class InvReservationDTO extends BaseEntityDTO {

    /**
     * @return the invSetup
     */
    public InventorySetup getInvSetup() {
        return invSetup;
    }

    /**
     * @param invSetup the invSetup to set
     */
    public void setInvSetup(InventorySetup invSetup) {
        this.invSetup = invSetup;
    }

    private Date reservationDate;
    private Date endDate;
    private Integer delegator;
    private Integer inventory;
    private Integer site;
    private Integer mainSite;
    private String address;
    private String remark;
    
    private Integer serial;
    
    
    private BigDecimal finalTotal;

    private BigDecimal totalPrice = BigDecimal.ZERO;
    private BigDecimal totalQuatity = BigDecimal.ZERO;
    private BigDecimal totalTotal = BigDecimal.ZERO;
    private BigDecimal totalAdding = BigDecimal.ZERO;
    private BigDecimal totalDiscount = BigDecimal.ZERO;
    private BigDecimal totalNet = BigDecimal.ZERO;
    
    private Map<Integer, BigDecimal> itemsMap;
    private Map<Integer, InvReservationDetailDTO> invReservationDetailMap;
    private Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap;
    
    
     private List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList;
     private List<InvReservationDetailDTO> invReservationDetailsEntityList ;
     
     
     private InventorySetup invSetup;
     
     

    public InvReservationDTO() {
        setIndex(getIndex() + 1);
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the invReservationDetailsEntityList
     */
    public List<InvReservationDetailDTO> getInvReservationDetailsEntityList() {
        if(invReservationDetailsEntityList == null){
           invReservationDetailsEntityList =  new ArrayList<>();
        }
        return invReservationDetailsEntityList;
    }

    /**
     * @param invReservationDetailsEntityList the invReservationDetailsEntityList to set
     */
    public void setInvReservationDetailsEntityList(List<InvReservationDetailDTO> invReservationDetailsEntityList) {
        this.invReservationDetailsEntityList = invReservationDetailsEntityList;
    }

    /**
     * @return the delegator
     */
    public Integer getDelegator() {
        return delegator;
    }

    /**
     * @param delegator the delegator to set
     */
    public void setDelegator(Integer delegator) {
        this.delegator = delegator;
    }

    /**
     * @return the inventory
     */
    public Integer getInventory() {
        return inventory;
    }

    /**
     * @param inventory the inventory to set
     */
    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    /**
     * @return the site
     */
    public Integer getSite() {
        return site;
    }

    /**
     * @param site the site to set
     */
    public void setSite(Integer site) {
        this.site = site;
    }

    /**
     * @return the mainSite
     */
    public Integer getMainSite() {
        return mainSite;
    }

    /**
     * @param mainSite the mainSite to set
     */
    public void setMainSite(Integer mainSite) {
        this.mainSite = mainSite;
    }

    /**
     * @return the serial
     */
    public Integer getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    /**
     * @return the finalTotal
     */
    public BigDecimal getFinalTotal() {
        return finalTotal;
    }

    /**
     * @param finalTotal the finalTotal to set
     */
    public void setFinalTotal(BigDecimal finalTotal) {
        this.finalTotal = finalTotal;
    }

    /**
     * @return the totalPrice
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    /**
     * @param totalPrice the totalPrice to set
     */
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * @return the totalQuatity
     */
    public BigDecimal getTotalQuatity() {
        return totalQuatity;
    }

    /**
     * @param totalQuatity the totalQuatity to set
     */
    public void setTotalQuatity(BigDecimal totalQuatity) {
        this.totalQuatity = totalQuatity;
    }

    /**
     * @return the totalTotal
     */
    public BigDecimal getTotalTotal() {
        return totalTotal;
    }

    /**
     * @param totalTotal the totalTotal to set
     */
    public void setTotalTotal(BigDecimal totalTotal) {
        this.totalTotal = totalTotal;
    }

    /**
     * @return the totalAdding
     */
    public BigDecimal getTotalAdding() {
        return totalAdding;
    }

    /**
     * @param totalAdding the totalAdding to set
     */
    public void setTotalAdding(BigDecimal totalAdding) {
        this.totalAdding = totalAdding;
    }

    /**
     * @return the totalDiscount
     */
    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    /**
     * @param totalDiscount the totalDiscount to set
     */
    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    /**
     * @return the totalNet
     */
    public BigDecimal getTotalNet() {
        return totalNet;
    }

    /**
     * @param totalNet the totalNet to set
     */
    public void setTotalNet(BigDecimal totalNet) {
        this.totalNet = totalNet;
    }

    /**
     * @return the itemsMap
     */
    public Map<Integer, BigDecimal> getItemsMap() {
        return itemsMap;
    }

    /**
     * @param itemsMap the itemsMap to set
     */
    public void setItemsMap(Map<Integer, BigDecimal> itemsMap) {
        this.itemsMap = itemsMap;
    }

    /**
     * @return the invReservationDetailMap
     */
    public Map<Integer, InvReservationDetailDTO> getInvReservationDetailMap() {
        return invReservationDetailMap;
    }

    /**
     * @param invReservationDetailMap the invReservationDetailMap to set
     */
    public void setInvReservationDetailMap(Map<Integer, InvReservationDetailDTO> invReservationDetailMap) {
        this.invReservationDetailMap = invReservationDetailMap;
    }

    /**
     * @return the itemsBarcodesDetailsViewList
     */
    public List<ItemsBarcodesDetailsView> getItemsBarcodesDetailsViewList() {
        return itemsBarcodesDetailsViewList;
    }

    /**
     * @param itemsBarcodesDetailsViewList the itemsBarcodesDetailsViewList to set
     */
    public void setItemsBarcodesDetailsViewList(List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList) {
        this.itemsBarcodesDetailsViewList = itemsBarcodesDetailsViewList;
    }

    /**
     * @return the itemsBarcodeMap
     */
    public Map<String, ItemsBarcodesDetailsView> getItemsBarcodeMap() {
        return itemsBarcodeMap;
    }

    /**
     * @param itemsBarcodeMap the itemsBarcodeMap to set
     */
    public void setItemsBarcodeMap(Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap) {
        this.itemsBarcodeMap = itemsBarcodeMap;
    }
}
