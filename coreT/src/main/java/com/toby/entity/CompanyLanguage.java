/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author hq003
 */
@Entity
@Table(name = "company_language")
public class CompanyLanguage extends BaseEntity {

    @JoinColumn(name = "symbol", referencedColumnName = "id")
    @ManyToOne()
    private Symbol symbol;

    /**
     * @return the symbol
     */
    public Symbol getSymbol() {
        return symbol;
    }

    /**
     * @param symbol the symbol to set
     */
    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

}
