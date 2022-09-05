/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.API;

import java.util.List;

public class CustomerBalanceAPI {

    private Integer balance;
    private List<CustomerInvoiceApi> invoicesList;

    /**
     * @return the balance
     */
    public Integer getBalance() {
        return balance;
    }

    /**
     * @param balance the balance to set
     */
    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    /**
     * @return the invoicesList
     */
    public List<CustomerInvoiceApi> getInvoicesList() {
        return invoicesList;
    }

    /**
     * @param invoicesList the invoicesList to set
     */
    public void setInvoicesList(List<CustomerInvoiceApi> invoicesList) {
        this.invoicesList = invoicesList;
    }

}
