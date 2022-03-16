/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.bean;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author IT Shop
 */
public class BankBalanceReportBeanListDetail implements Serializable{

     private List<BankBalanceReportBean> bankBalanceReportBeanList;
     
    /**
     * @return the bankBalanceReportBeanList
     */
    public List<BankBalanceReportBean> getBankBalanceReportBeanList() {
        return bankBalanceReportBeanList;
    }

    /**
     * @param bankBalanceReportBeanList the bankBalanceReportBeanList to set
     */
    public void setBankBalanceReportBeanList(List<BankBalanceReportBean> bankBalanceReportBeanList) {
        this.bankBalanceReportBeanList = bankBalanceReportBeanList;
    }
    
   
}
