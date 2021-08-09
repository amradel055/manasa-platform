/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.entiy;

import com.isag.entity.GlAccount;

/**
 *
 * @author WIN7
 */
public class GlAccountEntity extends BaseEntity {

    private GlAccount glAccount;
    private Integer generalBudgetCreditId;
    private Integer generalBudgetDebitId;
    private Integer generalBudgetId;

    public GlAccount getGlAccount() {
        return glAccount;
    }

    public void setGlAccount(GlAccount glAccount) {
        this.glAccount = glAccount;
    }

    public Integer getGeneralBudgetCreditId() {
        return generalBudgetCreditId;
    }

    public void setGeneralBudgetCreditId(Integer generalBudgetCreditId) {
        this.generalBudgetCreditId = generalBudgetCreditId;
    }

    public Integer getGeneralBudgetDebitId() {
        return generalBudgetDebitId;
    }

    public void setGeneralBudgetDebitId(Integer generalBudgetDebitId) {
        this.generalBudgetDebitId = generalBudgetDebitId;
    }

    /**
     * @return the generalBudgetId
     */
    public Integer getGeneralBudgetId() {
        return generalBudgetId;
    }

    /**
     * @param generalBudgetId the generalBudgetId to set
     */
    public void setGeneralBudgetId(Integer generalBudgetId) {
        this.generalBudgetId = generalBudgetId;
    }
}
