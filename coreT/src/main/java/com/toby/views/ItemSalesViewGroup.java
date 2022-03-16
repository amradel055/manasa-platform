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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author amr
 */
@Entity
@Table(name = "item_sales_view_group")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ItemSalesViewGroup.findAll", query = "SELECT i FROM ItemSalesViewGroup i"),
    @NamedQuery(name = "ItemSalesViewGroup.findById", query = "SELECT i FROM ItemSalesViewGroup i WHERE i.id = :id"),
    @NamedQuery(name = "ItemSalesViewGroup.findBySerial", query = "SELECT i FROM ItemSalesViewGroup i WHERE i.serial = :serial"),
    @NamedQuery(name = "ItemSalesViewGroup.findByDate", query = "SELECT i FROM ItemSalesViewGroup i WHERE i.date = :date"),
    @NamedQuery(name = "ItemSalesViewGroup.findByDelegatorId", query = "SELECT i FROM ItemSalesViewGroup i WHERE i.delegatorId = :delegatorId"),
    @NamedQuery(name = "ItemSalesViewGroup.findByInventoryId", query = "SELECT i FROM ItemSalesViewGroup i WHERE i.inventoryId = :inventoryId"),
    @NamedQuery(name = "ItemSalesViewGroup.findByPaymentType", query = "SELECT i FROM ItemSalesViewGroup i WHERE i.paymentType = :paymentType"),
    @NamedQuery(name = "ItemSalesViewGroup.findByGroupId", query = "SELECT i FROM ItemSalesViewGroup i WHERE i.groupId = :groupId"),
    @NamedQuery(name = "ItemSalesViewGroup.findByGroupName", query = "SELECT i FROM ItemSalesViewGroup i WHERE i.groupName = :groupName"),
    @NamedQuery(name = "ItemSalesViewGroup.findByItemId", query = "SELECT i FROM ItemSalesViewGroup i WHERE i.itemId = :itemId"),
    @NamedQuery(name = "ItemSalesViewGroup.findByItemName", query = "SELECT i FROM ItemSalesViewGroup i WHERE i.itemName = :itemName"),
    @NamedQuery(name = "ItemSalesViewGroup.findByItemCode", query = "SELECT i FROM ItemSalesViewGroup i WHERE i.itemCode = :itemCode"),
    @NamedQuery(name = "ItemSalesViewGroup.findByItemBarcode", query = "SELECT i FROM ItemSalesViewGroup i WHERE i.itemBarcode = :itemBarcode"),
    @NamedQuery(name = "ItemSalesViewGroup.findByItemUnitName", query = "SELECT i FROM ItemSalesViewGroup i WHERE i.itemUnitName = :itemUnitName"),
    @NamedQuery(name = "ItemSalesViewGroup.findByDetailUnitName", query = "SELECT i FROM ItemSalesViewGroup i WHERE i.detailUnitName = :detailUnitName"),
    @NamedQuery(name = "ItemSalesViewGroup.findByDetailItemQuantity", query = "SELECT i FROM ItemSalesViewGroup i WHERE i.detailItemQuantity = :detailItemQuantity"),
    @NamedQuery(name = "ItemSalesViewGroup.findByItemQuantity", query = "SELECT i FROM ItemSalesViewGroup i WHERE i.itemQuantity = :itemQuantity"),
    @NamedQuery(name = "ItemSalesViewGroup.findByCost", query = "SELECT i FROM ItemSalesViewGroup i WHERE i.cost = :cost"),
    @NamedQuery(name = "ItemSalesViewGroup.findByDiscount", query = "SELECT i FROM ItemSalesViewGroup i WHERE i.discount = :discount"),
    @NamedQuery(name = "ItemSalesViewGroup.findByNet", query = "SELECT i FROM ItemSalesViewGroup i WHERE i.net = :net")})
public class ItemSalesViewGroup implements Serializable {

   
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "type")
    private Integer type;
    @Column(name = "delegator_code")
    private String delegatorCode;
    @Column(name = "inventory_code")
    private String inventoryCode;
    @Column(name = "branch_id")
    private Integer branchId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "organization_site_id")
    private Integer organizationSiteId;
    @Column(name = "organization_site_code")
    private String organizationSiteCode;
    @Column(name = "organization_site_name")
    private String organizationSiteName;
    @Column(name = "supplier_id")
    private Integer supplierId;
    @Size(max = 45)
    @Column(name = "supplier_name")
    private String supplierName;
    @Size(max = 45)
    @Column(name = "supplier_code")
    private String supplierCode;
    @Column(name = "delegator_id")
    private Integer delegatorId;
    @Column(name = "inventory_id")
    private Integer inventoryId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "payment_type")
    private int paymentType;
    @Column(name = "group_id")
    private Integer groupId;
    @Size(max = 45)
    @Column(name = "group_name")
    private String groupName;
    @Column(name = "item_id")
    private Integer itemId;
    @Size(max = 45)
    @Column(name = "item_name")
    private String itemName;
    @Size(max = 45)
    @Column(name = "item_code")
    private String itemCode;
    @Size(max = 45)
    @Column(name = "item_barcode")
    private String itemBarcode;
    @Size(max = 255)
    @Column(name = "item_unit_name")
    private String itemUnitName;
    @Size(max = 255)
    @Column(name = "detail_unit_name")
    private String detailUnitName;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "detail_item_quantity")
    private BigDecimal detailItemQuantity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "item_quantity")
    private BigDecimal itemQuantity;
    @Column(name = "cost")
    private BigDecimal cost;
    @Column(name = "discount")
    private BigDecimal discount;
    @Column(name = "net")
    private BigDecimal net;
    @Column(name = "pro_group_id")
    private Integer proGroupId;
    @Column(name = "pro_group_name")
    private String proGroupName;
    @Column(name = "pro_group_code")
    private String proGroupCode;

    public ItemSalesViewGroup() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getDelegatorId() {
        return delegatorId;
    }

    public void setDelegatorId(Integer delegatorId) {
        this.delegatorId = delegatorId;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
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

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
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

    public BigDecimal getDetailItemQuantity() {
        return detailItemQuantity;
    }

    public void setDetailItemQuantity(BigDecimal detailItemQuantity) {
        this.detailItemQuantity = detailItemQuantity;
    }

    public BigDecimal getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(BigDecimal itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public String getDelegatorCode() {
        return delegatorCode;
    }

    public void setDelegatorCode(String delegatorCode) {
        this.delegatorCode = delegatorCode;
    }

    public String getInventoryCode() {
        return inventoryCode;
    }

    public void setInventoryCode(String inventoryCode) {
        this.inventoryCode = inventoryCode;
    }

    /**
     * @return the organizationSiteId
     */
    public Integer getOrganizationSiteId() {
        return organizationSiteId;
    }

    /**
     * @param organizationSiteId the organizationSiteId to set
     */
    public void setOrganizationSiteId(Integer organizationSiteId) {
        this.organizationSiteId = organizationSiteId;
    }

    /**
     * @return the organizationSiteCode
     */
    public String getOrganizationSiteCode() {
        return organizationSiteCode;
    }

    /**
     * @param organizationSiteCode the organizationSiteCode to set
     */
    public void setOrganizationSiteCode(String organizationSiteCode) {
        this.organizationSiteCode = organizationSiteCode;
    }

    /**
     * @return the organizationSiteName
     */
    public String getOrganizationSiteName() {
        return organizationSiteName;
    }

    /**
     * @param organizationSiteName the organizationSiteName to set
     */
    public void setOrganizationSiteName(String organizationSiteName) {
        this.organizationSiteName = organizationSiteName;
    }

    /**
     * @return the supplierId
     */
    public Integer getSupplierId() {
        return supplierId;
    }

    /**
     * @param supplierId the supplierId to set
     */
    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * @return the supplierName
     */
    public String getSupplierName() {
        return supplierName;
    }

    /**
     * @param supplierName the supplierName to set
     */
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    /**
     * @return the supplierCode
     */
    public String getSupplierCode() {
        return supplierCode;
    }

    /**
     * @param supplierCode the supplierCode to set
     */
    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    /**
     * @return the proGroupId
     */
    public Integer getProGroupId() {
        return proGroupId;
    }

    /**
     * @param proGroupId the proGroupId to set
     */
    public void setProGroupId(Integer proGroupId) {
        this.proGroupId = proGroupId;
    }

    /**
     * @return the proGroupName
     */
    public String getProGroupName() {
        return proGroupName;
    }

    /**
     * @param proGroupName the proGroupName to set
     */
    public void setProGroupName(String proGroupName) {
        this.proGroupName = proGroupName;
    }

    /**
     * @return the proGroupCode
     */
    public String getProGroupCode() {
        return proGroupCode;
    }

    /**
     * @param proGroupCode the proGroupCode to set
     */
    public void setProGroupCode(String proGroupCode) {
        this.proGroupCode = proGroupCode;
    }

}
