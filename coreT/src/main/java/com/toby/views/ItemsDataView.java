/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hhhh
 */
@Entity
@Table(name = "items_data_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ItemsDataView.findAll", query = "SELECT i FROM ItemsDataView i"),
    @NamedQuery(name = "ItemsDataView.findByRowNum", query = "SELECT i FROM ItemsDataView i WHERE i.rowNum = :rowNum"),
    @NamedQuery(name = "ItemsDataView.findById", query = "SELECT i FROM ItemsDataView i WHERE i.id = :id"),
    @NamedQuery(name = "ItemsDataView.findBySerial", query = "SELECT i FROM ItemsDataView i WHERE i.serial = :serial"),
    @NamedQuery(name = "ItemsDataView.findByQty", query = "SELECT i FROM ItemsDataView i WHERE i.qty = :qty"),
    @NamedQuery(name = "ItemsDataView.findByDate", query = "SELECT i FROM ItemsDataView i WHERE i.date = :date"),
    @NamedQuery(name = "ItemsDataView.findByGroupId", query = "SELECT i FROM ItemsDataView i WHERE i.groupId = :groupId"),
    @NamedQuery(name = "ItemsDataView.findByGroupName", query = "SELECT i FROM ItemsDataView i WHERE i.groupName = :groupName"),
    @NamedQuery(name = "ItemsDataView.findByGroupCode", query = "SELECT i FROM ItemsDataView i WHERE i.groupCode = :groupCode"),
    @NamedQuery(name = "ItemsDataView.findByItemName", query = "SELECT i FROM ItemsDataView i WHERE i.itemName = :itemName"),
    @NamedQuery(name = "ItemsDataView.findByItemCode", query = "SELECT i FROM ItemsDataView i WHERE i.itemCode = :itemCode"),
    @NamedQuery(name = "ItemsDataView.findBySellPrice", query = "SELECT i FROM ItemsDataView i WHERE i.sellPrice = :sellPrice"),
    @NamedQuery(name = "ItemsDataView.findByItemBarcode", query = "SELECT i FROM ItemsDataView i WHERE i.itemBarcode = :itemBarcode"),
    @NamedQuery(name = "ItemsDataView.findByItemUnitName", query = "SELECT i FROM ItemsDataView i WHERE i.itemUnitName = :itemUnitName"),
    @NamedQuery(name = "ItemsDataView.findByDetailUnitName", query = "SELECT i FROM ItemsDataView i WHERE i.detailUnitName = :detailUnitName"),
    @NamedQuery(name = "ItemsDataView.findByQtyin", query = "SELECT i FROM ItemsDataView i WHERE i.qtyin = :qtyin"),
    @NamedQuery(name = "ItemsDataView.findByQtyout", query = "SELECT i FROM ItemsDataView i WHERE i.qtyout = :qtyout"),
    @NamedQuery(name = "ItemsDataView.findByCostAverage", query = "SELECT i FROM ItemsDataView i WHERE i.costAverage = :costAverage"),
    @NamedQuery(name = "ItemsDataView.findByScrewing", query = "SELECT i FROM ItemsDataView i WHERE i.screwing = :screwing"),
    @NamedQuery(name = "ItemsDataView.findByItemId", query = "SELECT i FROM ItemsDataView i WHERE i.itemId = :itemId"),
    @NamedQuery(name = "ItemsDataView.findByBranchId", query = "SELECT i FROM ItemsDataView i WHERE i.branchId = :branchId"),
    @NamedQuery(name = "ItemsDataView.findByPostFlag", query = "SELECT i FROM ItemsDataView i WHERE i.postFlag = :postFlag"),
    @NamedQuery(name = "ItemsDataView.findByInventoryId", query = "SELECT i FROM ItemsDataView i WHERE i.inventoryId = :inventoryId"),
    @NamedQuery(name = "ItemsDataView.findByInventoryCode", query = "SELECT i FROM ItemsDataView i WHERE i.inventoryCode = :inventoryCode"),
    @NamedQuery(name = "ItemsDataView.findByInventoryName", query = "SELECT i FROM ItemsDataView i WHERE i.inventoryName = :inventoryName"),
    @NamedQuery(name = "ItemsDataView.findByOrganizationSiteId", query = "SELECT i FROM ItemsDataView i WHERE i.organizationSiteId = :organizationSiteId"),
    @NamedQuery(name = "ItemsDataView.findByOrganizationCode", query = "SELECT i FROM ItemsDataView i WHERE i.organizationCode = :organizationCode"),
    @NamedQuery(name = "ItemsDataView.findByOrganizationName", query = "SELECT i FROM ItemsDataView i WHERE i.organizationName = :organizationName"),
    @NamedQuery(name = "ItemsDataView.findByOrganizationType", query = "SELECT i FROM ItemsDataView i WHERE i.organizationType = :organizationType"),
    @NamedQuery(name = "ItemsDataView.findByOrganizationOpenBalanceDebit", query = "SELECT i FROM ItemsDataView i WHERE i.organizationOpenBalanceDebit = :organizationOpenBalanceDebit"),
    @NamedQuery(name = "ItemsDataView.findByOrganizationOpenBalanceCredit", query = "SELECT i FROM ItemsDataView i WHERE i.organizationOpenBalanceCredit = :organizationOpenBalanceCredit"),
    @NamedQuery(name = "ItemsDataView.findByOrganizationCurrencyId", query = "SELECT i FROM ItemsDataView i WHERE i.organizationCurrencyId = :organizationCurrencyId"),
    @NamedQuery(name = "ItemsDataView.findByType", query = "SELECT i FROM ItemsDataView i WHERE i.type = :type"),
    @NamedQuery(name = "ItemsDataView.findByScreenName", query = "SELECT i FROM ItemsDataView i WHERE i.screenName = :screenName"),
    @NamedQuery(name = "ItemsDataView.findByOperationType", query = "SELECT i FROM ItemsDataView i WHERE i.operationType = :operationType")})
public class ItemsDataView implements Serializable {

    /**
     * @return the itemSupplierId
     */
    public Integer getItemSupplierId() {
        return itemSupplierId;
    }

    /**
     * @param itemSupplierId the itemSupplierId to set
     */
    public void setItemSupplierId(Integer itemSupplierId) {
        this.itemSupplierId = itemSupplierId;
    }

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "rowNum")
    private Integer rowNum;
    @Column(name = "id")
    private int id;
    
    @Basic(optional = false)
   
    @Column(name = "serial")
    private Integer serial;
     @Column(name = "item_supplier_id")
    private Integer itemSupplierId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "qty")
    private BigDecimal qty;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "group_id")
    private Integer groupId;
    @Column(name = "group_name")
    private String groupName;
    @Column(name = "group_code")
    private String groupCode;
    @Column(name = "item_name")
    private String itemName;
    @Column(name = "item_code")
    private String itemCode;
    @Column(name = "sell_price")
    private BigDecimal sellPrice;
    @Column(name = "item_barcode")
    private String itemBarcode;
    @Column(name = "item_unit_name")
    private String itemUnitName;
    @Column(name = "detail_unit_name")
    private String detailUnitName;
    @Basic(optional = false)
    @Column(name = "qtyin")
     private BigDecimal qtyin;
    @Basic(optional = false)
    @Column(name = "qtyout")
    private BigDecimal qtyout;
    @Basic(optional = false)
    @Column(name = "cost_average")
    private BigDecimal costAverage;
    @Basic(optional = false)
    @Column(name = "screwing")
    private BigDecimal screwing;
    @Column(name = "item_id")
    private Integer itemId;
    @Column(name = "branch_id")
    private Integer branchId;
    @Column(name = "post_flag")
    private Integer postFlag;
    @Column(name = "inventory_id")
    private Integer inventoryId;
    @Column(name = "inventory_code")
    private String inventoryCode;
    @Column(name = "inventory_name")
    private String inventoryName;
    @Column(name = "organization_site_id")
    private Integer organizationSiteId;
    @Column(name = "organization_code")
    private String organizationCode;
    @Column(name = "organization_name")
    private String organizationName;
    @Column(name = "organization_type")
    private Integer organizationType;
    @Column(name = "organization_open_balance_debit")
    private BigDecimal organizationOpenBalanceDebit;
    @Column(name = "organization_open_balance_credit")
    private BigDecimal organizationOpenBalanceCredit;
    @Column(name = "organization_currency_id")
    private Integer organizationCurrencyId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "weight_detail")
    private int weightDetail;
    @Basic(optional = false)
    @Column(name = "total_actual_weight")
    private int totalActualWeight;
    @Basic(optional = false)
    @NotNull
    @Column(name = "price_kilo")
    private int priceKilo;
    @Basic(optional = false)
    @Column(name = "type")
    private int type;
    @Basic(optional = false)
    @Column(name = "ScreenName")
    private String screenName;
    @Basic(optional = false)
    @Column(name = "operationType")
    private int operationType;
    @Basic(optional = false)
    @Column(name = "ScreenCode")
    private int screenCode;
    @Transient
    private BigDecimal balance;
    @Transient
    private BigDecimal balanceValue;

    @Transient
    private BigDecimal openingBalance;
    @Transient
    private BigDecimal totalQtyIn;
    @Transient
    private BigDecimal totalQtyOut;
    @Transient
    private String gallaryName;

    public ItemsDataView() {
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public BigDecimal getSellPrice() {
        if(sellPrice == null){
            sellPrice = BigDecimal.ZERO;
        }
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public String getItemUnitName() {
        return itemUnitName;
    }

    public void setItemUnitName(String itemUnitName) {
        this.itemUnitName = itemUnitName;
    }

    public String getDetailUnitName() {
        return detailUnitName;
    }

    public void setDetailUnitName(String detailUnitName) {
        this.detailUnitName = detailUnitName;
    }

  

    public BigDecimal getQtyout() {
        return qtyout;
    }

    public void setQtyout(BigDecimal qtyout) {
        this.qtyout = qtyout;
    }

    public BigDecimal getCostAverage() {
        return costAverage;
    }

    public void setCostAverage(BigDecimal costAverage) {
        this.costAverage = costAverage;
    }

    public BigDecimal getScrewing() {
        return screwing;
    }

    public void setScrewing(BigDecimal screwing) {
        this.screwing = screwing;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getPostFlag() {
        return postFlag;
    }

    public void setPostFlag(Integer postFlag) {
        this.postFlag = postFlag;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getInventoryCode() {
        return inventoryCode;
    }

    public void setInventoryCode(String inventoryCode) {
        this.inventoryCode = inventoryCode;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    public Integer getOrganizationSiteId() {
        return organizationSiteId;
    }

    public void setOrganizationSiteId(Integer organizationSiteId) {
        this.organizationSiteId = organizationSiteId;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Integer getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(Integer organizationType) {
        this.organizationType = organizationType;
    }

    public BigDecimal getOrganizationOpenBalanceDebit() {
        return organizationOpenBalanceDebit;
    }

    public void setOrganizationOpenBalanceDebit(BigDecimal organizationOpenBalanceDebit) {
        this.organizationOpenBalanceDebit = organizationOpenBalanceDebit;
    }

    public BigDecimal getOrganizationOpenBalanceCredit() {
        return organizationOpenBalanceCredit;
    }

    public void setOrganizationOpenBalanceCredit(BigDecimal organizationOpenBalanceCredit) {
        this.organizationOpenBalanceCredit = organizationOpenBalanceCredit;
    }

    public Integer getOrganizationCurrencyId() {
        return organizationCurrencyId;
    }

    public void setOrganizationCurrencyId(Integer organizationCurrencyId) {
        this.organizationCurrencyId = organizationCurrencyId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public int getOperationType() {
        return operationType;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }

    /**
     * @return the balance
     */
    public BigDecimal getBalance() {
        if(balance == null){
            balance = BigDecimal.ZERO;
        }
        return balance;
    }

    /**
     * @param balance the balance to set
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * @return the balanceValue
     */
    public BigDecimal getBalanceValue() {
        return balanceValue;
    }

    /**
     * @param balanceValue the balanceValue to set
     */
    public void setBalanceValue(BigDecimal balanceValue) {
        this.balanceValue = balanceValue;
    }

    /**
     * @return the openingBalance
     */
    public BigDecimal getOpeningBalance() {
        return openingBalance;
    }

    /**
     * @param openingBalance the openingBalance to set
     */
    public void setOpeningBalance(BigDecimal openingBalance) {
        this.openingBalance = openingBalance;
    }

    /**
     * @return the qtyin
     */
    public BigDecimal getQtyin() {
        return qtyin;
    }

    /**
     * @param qtyin the qtyin to set
     */
    public void setQtyin(BigDecimal qtyin) {
        this.qtyin = qtyin;
    }
    
    public int getScreenCode() {
        return screenCode;
    }

    public void setScreenCode(int screenCode) {
        this.screenCode = screenCode;
    }
    /**
     * @return the weightDetail
     */
    public int getWeightDetail() {
        return weightDetail;
    }

    /**
     * @param weightDetail the weightDetail to set
     */
    public void setWeightDetail(int weightDetail) {
        this.weightDetail = weightDetail;
    }

    /**
     * @return the totalActualWeight
     */
    public int getTotalActualWeight() {
        return totalActualWeight;
    }

    /**
     * @param totalActualWeight the totalActualWeight to set
     */
    public void setTotalActualWeight(int totalActualWeight) {
        this.totalActualWeight = totalActualWeight;
    }

    /**
     * @return the priceKilo
     */
    public int getPriceKilo() {
        return priceKilo;
    }

    /**
     * @param priceKilo the priceKilo to set
     */
    public void setPriceKilo(int priceKilo) {
        this.priceKilo = priceKilo;
    }

    /**
     * @return the totalQtyIn
     */
    public BigDecimal getTotalQtyIn() {
        return totalQtyIn;
    }

    /**
     * @param totalQtyIn the totalQtyIn to set
     */
    public void setTotalQtyIn(BigDecimal totalQtyIn) {
        this.totalQtyIn = totalQtyIn;
    }

    /**
     * @return the totalQtyOut
     */
    public BigDecimal getTotalQtyOut() {
        return totalQtyOut;
    }

    /**
     * @param totalQtyOut the totalQtyOut to set
     */
    public void setTotalQtyOut(BigDecimal totalQtyOut) {
        this.totalQtyOut = totalQtyOut;
    }

    /**
     * @return the gallaryName
     */
    public String getGallaryName() {
        return gallaryName;
    }

    /**
     * @param gallaryName the gallaryName to set
     */
    public void setGallaryName(String gallaryName) {
        this.gallaryName = gallaryName;
    }
   
}
