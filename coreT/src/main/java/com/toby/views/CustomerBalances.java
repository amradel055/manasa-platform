/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.views;

import com.toby.dto.InvInventoryDTO;
import java.math.BigDecimal;

/**
 *
 * @author m lapan
 */
public class CustomerBalances {

    private String clientCode;
    private String clientName;
    private BigDecimal Debit;
    private BigDecimal Creditor;
    private BigDecimal Balance;
    private InvInventoryDTO gallerySelected;

    /**
     * @return the clientCode
     */
    public String getClientCode() {
        return clientCode;
    }

    /**
     * @param clientCode the clientCode to set
     */
    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    /**
     * @return the clientName
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @param clientName the clientName to set
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * @return the Debit
     */
    public BigDecimal getDebit() {
        if (Debit == null) {
            return BigDecimal.ZERO;
        }
        return Debit;
    }

    /**
     * @param Debit the Debit to set
     */
    public void setDebit(BigDecimal Debit) {
        this.Debit = Debit;
    }

    /**
     * @return the Creditor
     */
    public BigDecimal getCreditor() {
        if (Creditor == null) {
            return BigDecimal.ZERO;
        }
        return Creditor;
    }

    /**
     * @param Creditor the Creditor to set
     */
    public void setCreditor(BigDecimal Creditor) {
        this.Creditor = Creditor;
    }

    /**
     * @return the Balance
     */
    public BigDecimal getBalance() {
        if (Balance == null) {
//            Balance = getDebit().subtract(getCreditor());
            Balance = getCreditor().subtract(getDebit());
        }
        return Balance;
    }

    /**
     * @param Balance the Balance to set
     */
    public void setBalance(BigDecimal Balance) {
        this.Balance = Balance;
    }

    /**
     * @return the gallerySelected
     */
    public InvInventoryDTO getGallerySelected() {
        return gallerySelected;
    }

    /**
     * @param gallerySelected the gallerySelected to set
     */
    public void setGallerySelected(InvInventoryDTO gallerySelected) {
        this.gallerySelected = gallerySelected;
    }

}
