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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author elsakr6
 */
@Entity
@Table(name = "summition_and_quantity_invoice_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SummitionAndQuantityInvoiceView.findAll", query = "SELECT s FROM SummitionAndQuantityInvoiceView s"),
    @NamedQuery(name = "SummitionAndQuantityInvoiceView.findById", query = "SELECT s FROM SummitionAndQuantityInvoiceView s WHERE s.id = :id"),
    @NamedQuery(name = "SummitionAndQuantityInvoiceView.findByBranchId", query = "SELECT s FROM SummitionAndQuantityInvoiceView s WHERE s.branchId = :branchId"),
    @NamedQuery(name = "SummitionAndQuantityInvoiceView.findByHeadId", query = "SELECT s FROM SummitionAndQuantityInvoiceView s WHERE s.headId = :headId"),
    @NamedQuery(name = "SummitionAndQuantityInvoiceView.findByDetailId", query = "SELECT s FROM SummitionAndQuantityInvoiceView s WHERE s.detailId = :detailId"),
    @NamedQuery(name = "SummitionAndQuantityInvoiceView.findByType", query = "SELECT s FROM SummitionAndQuantityInvoiceView s WHERE s.type = :type"),
    @NamedQuery(name = "SummitionAndQuantityInvoiceView.findByDate", query = "SELECT s FROM SummitionAndQuantityInvoiceView s WHERE s.date = :date"),
    @NamedQuery(name = "SummitionAndQuantityInvoiceView.findByInventoryId", query = "SELECT s FROM SummitionAndQuantityInvoiceView s WHERE s.inventoryId = :inventoryId"),
    @NamedQuery(name = "SummitionAndQuantityInvoiceView.findByInventoryName", query = "SELECT s FROM SummitionAndQuantityInvoiceView s WHERE s.inventoryName = :inventoryName"),
    @NamedQuery(name = "SummitionAndQuantityInvoiceView.findByGlBankId", query = "SELECT s FROM SummitionAndQuantityInvoiceView s WHERE s.glBankId = :glBankId"),
    @NamedQuery(name = "SummitionAndQuantityInvoiceView.findByGlBankName", query = "SELECT s FROM SummitionAndQuantityInvoiceView s WHERE s.glBankName = :glBankName"),
    @NamedQuery(name = "SummitionAndQuantityInvoiceView.findByInvDelegator", query = "SELECT s FROM SummitionAndQuantityInvoiceView s WHERE s.invDelegator = :invDelegator"),
    @NamedQuery(name = "SummitionAndQuantityInvoiceView.findByInvDelegatorName", query = "SELECT s FROM SummitionAndQuantityInvoiceView s WHERE s.invDelegatorName = :invDelegatorName"),
    @NamedQuery(name = "SummitionAndQuantityInvoiceView.findByOrganizationSiteId", query = "SELECT s FROM SummitionAndQuantityInvoiceView s WHERE s.organizationSiteId = :organizationSiteId"),
    @NamedQuery(name = "SummitionAndQuantityInvoiceView.findByOrganizationSiteName", query = "SELECT s FROM SummitionAndQuantityInvoiceView s WHERE s.organizationSiteName = :organizationSiteName"),
    @NamedQuery(name = "SummitionAndQuantityInvoiceView.findByOrganizationSiteType", query = "SELECT s FROM SummitionAndQuantityInvoiceView s WHERE s.organizationSiteType = :organizationSiteType"),
    @NamedQuery(name = "SummitionAndQuantityInvoiceView.findByPaymentType", query = "SELECT s FROM SummitionAndQuantityInvoiceView s WHERE s.paymentType = :paymentType"),
    @NamedQuery(name = "SummitionAndQuantityInvoiceView.findByHeadDiscount", query = "SELECT s FROM SummitionAndQuantityInvoiceView s WHERE s.headDiscount = :headDiscount"),
    @NamedQuery(name = "SummitionAndQuantityInvoiceView.findByDiscountType", query = "SELECT s FROM SummitionAndQuantityInvoiceView s WHERE s.discountType = :discountType"),
    @NamedQuery(name = "SummitionAndQuantityInvoiceView.findByQty", query = "SELECT s FROM SummitionAndQuantityInvoiceView s WHERE s.qty = :qty"),
    @NamedQuery(name = "SummitionAndQuantityInvoiceView.findByCost", query = "SELECT s FROM SummitionAndQuantityInvoiceView s WHERE s.cost = :cost"),
    @NamedQuery(name = "SummitionAndQuantityInvoiceView.findByDiscount", query = "SELECT s FROM SummitionAndQuantityInvoiceView s WHERE s.discount = :discount"),
    @NamedQuery(name = "SummitionAndQuantityInvoiceView.findBySumAfterDiscount", query = "SELECT s FROM SummitionAndQuantityInvoiceView s WHERE s.sumAfterDiscount = :sumAfterDiscount"),
    @NamedQuery(name = "SummitionAndQuantityInvoiceView.findByRemarks", query = "SELECT s FROM SummitionAndQuantityInvoiceView s WHERE s.remarks = :remarks")})
public class SummitionAndQuantityInvoiceView implements Serializable {

    /**
     * @return the purchaseId
     */
    public Integer getPurchaseId() {
        return purchaseId;
    }

    /**
     * @param purchaseId the purchaseId to set
     */
    public void setPurchaseId(Integer purchaseId) {
        this.purchaseId = purchaseId;
    }

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "branch_id")
    private Integer branchId;
    @Basic(optional = false)
    @Column(name = "head_id")
    private int headId;
    @Column(name = "serial")
    private Integer serial;
    @Column(name = "purchase_id")
    private Integer purchaseId;
    @Column(name = "detail_id")
    private Integer detailId;
    @Column(name = "type")
    private Boolean type;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "inventory_id")
    private Integer inventoryId;
    @Column(name = "inventory_name")
    private String inventoryName;
    @Column(name = "gl_bank_id")
    private Integer glBankId;
    @Column(name = "gl_bank_name")
    private String glBankName;
    @Column(name = "inv_delegator")
    private Integer invDelegator;
    @Column(name = "inv_delegator_name")
    private String invDelegatorName;
    @Column(name = "organization_site_id")
    private Integer organizationSiteId;
    @Column(name = "organization_site_name")
    private String organizationSiteName;
    @Column(name = "organization_site_type")
    private Integer organizationSiteType;
    @Basic(optional = false)
    @Column(name = "payment_type")
    private int paymentType;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "head_discount")
    private BigDecimal headDiscount;
    @Column(name = "discount_type")
    private Integer discountType;
    @Basic(optional = false)
    @Column(name = "qty")
    private BigDecimal qty;
    @Basic(optional = false)
    @Column(name = "cost")
    private BigDecimal cost;
    @Basic(optional = false)
    @Column(name = "discount")
    private BigDecimal discount;
    @Column(name = "sum_after_discount")
    private BigDecimal sumAfterDiscount;
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "gl_year")
    private Integer glYear;
    @Basic(optional = false)
    @Column(name = "post_flag")
    private int postFlag;
    @Column(name = "general_journal_id")
    private Integer generalJournalId;
    @Column(name = "rate")
    private BigDecimal rate;
    @Basic(optional = false)
    @Column(name = "company_id")
    private int companyId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "weight_detail")
    private BigDecimal weightDetail;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_actual_weight")
    private BigDecimal totalActualWeight;
    @Basic(optional = false)
    @NotNull
    @Column(name = "price_kilo")
    private BigDecimal priceKilo;

    public SummitionAndQuantityInvoiceView() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public int getHeadId() {
        return headId;
    }

    public void setHeadId(int headId) {
        this.headId = headId;
    }

    public Integer getDetailId() {
        return detailId;
    }

    public void setDetailId(Integer detailId) {
        this.detailId = detailId;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    public Integer getGlBankId() {
        return glBankId;
    }

    public void setGlBankId(Integer glBankId) {
        this.glBankId = glBankId;
    }

    public String getGlBankName() {
        return glBankName;
    }

    public void setGlBankName(String glBankName) {
        this.glBankName = glBankName;
    }

    public Integer getInvDelegator() {
        return invDelegator;
    }

    public void setInvDelegator(Integer invDelegator) {
        this.invDelegator = invDelegator;
    }

    public String getInvDelegatorName() {
        return invDelegatorName;
    }

    public void setInvDelegatorName(String invDelegatorName) {
        this.invDelegatorName = invDelegatorName;
    }

    public Integer getOrganizationSiteId() {
        return organizationSiteId;
    }

    public void setOrganizationSiteId(Integer organizationSiteId) {
        this.organizationSiteId = organizationSiteId;
    }

    public String getOrganizationSiteName() {
        return organizationSiteName;
    }

    public void setOrganizationSiteName(String organizationSiteName) {
        this.organizationSiteName = organizationSiteName;
    }

    public Integer getOrganizationSiteType() {
        return organizationSiteType;
    }

    public void setOrganizationSiteType(Integer organizationSiteType) {
        this.organizationSiteType = organizationSiteType;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getHeadDiscount() {
        return headDiscount;
    }

    public void setHeadDiscount(BigDecimal headDiscount) {
        this.headDiscount = headDiscount;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
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

    public BigDecimal getSumAfterDiscount() {
        return sumAfterDiscount;
    }

    public void setSumAfterDiscount(BigDecimal sumAfterDiscount) {
        this.sumAfterDiscount = sumAfterDiscount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }
    public Integer getGlYear() {
        return glYear;
    }

    public void setGlYear(Integer glYear) {
        this.glYear = glYear;
    }

    public int getPostFlag() {
        return postFlag;
    }

    public void setPostFlag(int postFlag) {
        this.postFlag = postFlag;
    }

    public Integer getGeneralJournalId() {
        return generalJournalId;
    }

    public void setGeneralJournalId(Integer generalJournalId) {
        this.generalJournalId = generalJournalId;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    
    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
    
    
    /**
     * @return the weightDetail
     */
    public BigDecimal getWeightDetail() {
        return weightDetail;
    }

    /**
     * @param weightDetail the weightDetail to set
     */
    public void setWeightDetail(BigDecimal weightDetail) {
        this.weightDetail = weightDetail;
    }

    /**
     * @return the totalActualWeight
     */
    public BigDecimal getTotalActualWeight() {
        return totalActualWeight;
    }

    /**
     * @param totalActualWeight the totalActualWeight to set
     */
    public void setTotalActualWeight(BigDecimal totalActualWeight) {
        this.totalActualWeight = totalActualWeight;
    }

    /**
     * @return the priceKilo
     */
    public BigDecimal getPriceKilo() {
        return priceKilo;
    }

    /**
     * @param priceKilo the priceKilo to set
     */
    public void setPriceKilo(BigDecimal priceKilo) {
        this.priceKilo = priceKilo;
    }


}
