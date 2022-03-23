/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.entity.AccountsSystemSettings;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface AccountsSystemSettingsService {

    public void addInventorySetup(AccountsSystemSettings accountsSystemSettings);

    public AccountsSystemSettings getInventoryByCompanyId(Integer companyId);
}
