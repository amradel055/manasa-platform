/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.report.entity;

/**
 *
 * @author hq002
 */
public class GeneralBudgetBean {
        private String number;
        private String nameAr;
        private String nameEn;
        private String accGroup;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    
    /**
     * @return the nameAr
     */
    public String getNameAr() {
        return nameAr;
    }

    /**
     * @param nameAr the nameAr to set
     */
    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    /**
     * @return the nameEn
     */
    public String getNameEn() {
        return nameEn;
    }

    /**
     * @param nameEn the nameEn to set
     */
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    /**
     * @return the accGroup
     */
    public String getAccGroup() {
        return accGroup;
    }

    /**
     * @param accGroup the accGroup to set
     */
    public void setAccGroup(String accGroup) {
        this.accGroup = accGroup;
    }
        
        
}
