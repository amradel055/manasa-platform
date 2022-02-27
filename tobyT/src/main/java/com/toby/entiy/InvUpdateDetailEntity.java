package com.toby.entiy;

import com.toby.entity.InvItem;
import com.toby.entity.InvUpdate;
import com.toby.entity.Symbol;
import java.math.BigDecimal;

public class InvUpdateDetailEntity extends BaseEntity {

    private InvItem invItemId;
    private BigDecimal newAmount;
   
    private BigDecimal oldAmount;
    private BigDecimal difference;
    private BigDecimal value;
    private String itemBarcode;
    private BigDecimal screwing;
    private InvUpdate invUpdateId;
    private Symbol unitId;
  

    public InvUpdateDetailEntity() {
        setCounter(getCounter() + 1);
    }

    /**
     * @return the invItemId
     */
    public InvItem getInvItemId() {
        return invItemId;
    }

    /**
     * @param invItemId the invItemId to set
     */
    public void setInvItemId(InvItem invItemId) {
        this.invItemId = invItemId;
    }

    /**
     * @return the newAmount
     */
    public BigDecimal getNewAmount() {
        return newAmount;
    }

    /**
     * @param newAmount the newAmount to set
     */
    public void setNewAmount(BigDecimal newAmount) {
        this.newAmount = newAmount;
    }

    /**
     * @return the oldAmount
     */
    public BigDecimal getOldAmount() {
        return oldAmount;
    }

    /**
     * @param oldAmount the oldAmount to set
     */
    public void setOldAmount(BigDecimal oldAmount) {
        this.oldAmount = oldAmount;
    }

    /**
     * @return the difference
     */
    public BigDecimal getDifference() {
        return difference;
    }

    /**
     * @param difference the difference to set
     */
    public void setDifference(BigDecimal difference) {
        this.difference = difference;
    }

    /**
     * @return the value
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * @return the itemBarcode
     */
    public String getItemBarcode() {
        return itemBarcode;
    }

    /**
     * @param itemBarcode the itemBarcode to set
     */
    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
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
     * @return the invUpdateId
     */
    public InvUpdate getInvUpdateId() {
        return invUpdateId;
    }

    /**
     * @param invUpdateId the invUpdateId to set
     */
    public void setInvUpdateId(InvUpdate invUpdateId) {
        this.invUpdateId = invUpdateId;
    }

    /**
     * @return the unitId
     */
    public Symbol getUnitId() {
        return unitId;
    }

    /**
     * @param unitId the unitId to set
     */
    public void setUnitId(Symbol unitId) {
        this.unitId = unitId;
    }

    
}
