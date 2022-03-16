package com.toby.entiy;

import com.toby.entity.InvItem;
import com.toby.entity.InvTransferDetail;
import com.toby.entity.Symbol;
import com.toby.views.ItemsBarcodesDetailsView;

import java.math.BigDecimal;

public class InvTransferDetailsEntity extends BaseEntity {


    private ItemsBarcodesDetailsView itemsBarcodesDetail;
    private ItemsBarcodesDetailsView itemsBarcodesDetailTrans;
    private InvItem itemId;
    private InvTransferDetail selectedId;
    private Integer transferFrom;
    private Integer item;
    private String itemName;
    private String itemCode;
    private Symbol unit;
    private String unitReport;
    private BigDecimal amount = BigDecimal.ZERO;
    private BigDecimal finalAmount = BigDecimal.ZERO;
    private BigDecimal balance = BigDecimal.ZERO;
    private BigDecimal screwing;

    public InvTransferDetailsEntity() {
        setCounter(getCounter() + 1);
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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

    public Symbol getUnit() {
        return unit;
    }

    public void setUnit(Symbol unit) {
        this.unit = unit;
    }

    public InvItem getItemId() {
        return itemId;
    }

    public void setItemId(InvItem itemId) {
        this.itemId = itemId;
    }

    public ItemsBarcodesDetailsView getItemsBarcodesDetail() {
        return itemsBarcodesDetail;
    }

    public void setItemsBarcodesDetail(ItemsBarcodesDetailsView itemsBarcodesDetail) {
        this.itemsBarcodesDetail = itemsBarcodesDetail;
    }

    public ItemsBarcodesDetailsView getItemsBarcodesDetailTrans() {
        return itemsBarcodesDetailTrans;
    }

    public void setItemsBarcodesDetailTrans(ItemsBarcodesDetailsView itemsBarcodesDetailTrans) {
        this.itemsBarcodesDetailTrans = itemsBarcodesDetailTrans;
    }
    
    /**
     * @return the screwing
     */
    public BigDecimal getScrewing() {
        return screwing;
    }

    /**
     * @param screwing the screwing to set
     */
    public void setScrewing(BigDecimal screwing) {
        this.screwing = screwing;
    }

    /**
     * @return the selectedId
     */
    public InvTransferDetail getSelectedId() {
        return selectedId;
    }

    /**
     * @param selectedId the selectedId to set
     */
    public void setSelectedId(InvTransferDetail selectedId) {
        this.selectedId = selectedId;
    }

    /**
     * @return the transferFrom
     */
    public Integer getTransferFrom() {
        return transferFrom;
    }

    /**
     * @param transferFrom the transferFrom to set
     */
    public void setTransferFrom(Integer transferFrom) {
        this.transferFrom = transferFrom;
    }

    /**
     * @return the unitReport
     */
    public String getUnitReport() {
        return unitReport;
    }

    /**
     * @param unitReport the unitReport to set
     */
    public void setUnitReport(String unitReport) {
        this.unitReport = unitReport;
    }

    /**
     * @return the finalAmount
     */
    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    /**
     * @param finalAmount the finalAmount to set
     */
    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }
}
