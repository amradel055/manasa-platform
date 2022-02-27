package com.toby.entiy;

import com.toby.entity.InvItem;
import com.toby.views.QuantityItemsStore;
import java.math.BigDecimal;

public class InvStripDetailEntity extends BaseEntity {

    private InvItem  itemId;
    private String itemName;
    private String itemCode;
    private BigDecimal difference;
    private BigDecimal actualAmount = BigDecimal.ZERO;
    private BigDecimal bookBalance = BigDecimal.ZERO;

    private QuantityItemsStore itemsInStore;
    private QuantityItemsStore itemsInStoreTrans;

    public InvStripDetailEntity() {
        setCounter(getCounter() + 1);
    }

    public InvItem  getItemId() {
        return itemId;
    }

    public void setItemId(InvItem  itemId) {
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

    public BigDecimal getDifference() {
        return difference;
    }

    public void setDifference(BigDecimal difference) {
        this.difference = difference;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public BigDecimal getBookBalance() {
        return bookBalance;
    }

    public void setBookBalance(BigDecimal bookBalance) {
        this.bookBalance = bookBalance;
    }

    public QuantityItemsStore getItemsInStore() {
        return itemsInStore;
    }

    public void setItemsInStore(QuantityItemsStore itemsInStore) {
        this.itemsInStore = itemsInStore;
    }

    public QuantityItemsStore getItemsInStoreTrans() {
        return itemsInStoreTrans;
    }

    public void setItemsInStoreTrans(QuantityItemsStore itemsInStoreTrans) {
        this.itemsInStoreTrans = itemsInStoreTrans;
    }
}
