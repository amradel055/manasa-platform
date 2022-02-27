/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.entity.AccountsSystemSettings;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq002
 */
@Stateless
public class AccountsSystemSettingsServiceImpl implements AccountsSystemSettingsService {

    @EJB
    private GenericDAO dao;

    @Override
    public void addInventorySetup(AccountsSystemSettings accountsSystemSettings) {
     dao.updateEntity(accountsSystemSettings);
    }

    @Override
    public AccountsSystemSettings getInventoryByCompanyId(Integer companyId) {
     AccountsSystemSettings accountsSystemSettings=dao.findEntityByCompany(AccountsSystemSettings.class, companyId);
        return accountsSystemSettings;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
