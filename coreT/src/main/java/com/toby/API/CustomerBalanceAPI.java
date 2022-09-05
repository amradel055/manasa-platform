/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.API;

import java.math.BigDecimal;
import java.util.List;

public class CustomerBalanceAPI {

    private BigDecimal balance;
//    private List<CustomerInvoiceApi> invoicesList;
    private List<InvInvoiceDTOApi> invoicesList;

    /**
     * @return the invoicesList
     */
    public List<InvInvoiceDTOApi> getInvoicesList() {
        return invoicesList;
    }

    /**
     * @param invoicesList the invoicesList to set
     */
    public void setInvoicesList(List<InvInvoiceDTOApi> invoicesList) {
        this.invoicesList = invoicesList;
    }

//    /**
//     * @return the invoicesList
//     */
//    public List<CustomerInvoiceApi> getInvoicesList() {
//        return invoicesList;
//    }
//
//    /**
//     * @param invoicesList the invoicesList to set
//     */
//    public void setInvoicesList(List<CustomerInvoiceApi> invoicesList) {
//        this.invoicesList = invoicesList;
//    }

    /**
     * @return the balance
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * @param balance the balance to set
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}
