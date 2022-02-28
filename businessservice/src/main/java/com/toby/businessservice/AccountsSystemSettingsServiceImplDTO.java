/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.TobyUser;
import com.toby.core.GenericDAO;
import com.toby.dto.AccountsSystemSettingsDTO;
import com.toby.entity.AccountsSystemSettings;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author AhmedEssam
 */
@Stateless
public class AccountsSystemSettingsServiceImplDTO implements AccountsSystemSettingsServiceDTO {

    @EJB
    private GenericDAO dao;

    @Override
    public AccountsSystemSettingsDTO getInventoryByCompanyId(Integer companyId) {
        AccountsSystemSettings accountsSystemSettings = dao.findEntityByCompany(AccountsSystemSettings.class, companyId);
        return mapTODTO(accountsSystemSettings, false);
    }

    private List<AccountsSystemSettingsDTO> returnListDTO(List<AccountsSystemSettings> list) {
        List<AccountsSystemSettingsDTO> dTOList = new ArrayList<>();
        for (AccountsSystemSettings entity : list) {
            dTOList.add(mapTODTO(entity, false));
        }
        return dTOList;
    }

    private AccountsSystemSettingsDTO mapTODTO(AccountsSystemSettings entity, Boolean needDetail) {
        AccountsSystemSettingsDTO dTO = initMapToDTO(entity);
        if (needDetail) {

        }
        return dTO;
    }

    private AccountsSystemSettingsDTO initMapToDTO(AccountsSystemSettings entity) {

        AccountsSystemSettingsDTO dTO = new AccountsSystemSettingsDTO();
        dTO.setId(entity.getId());

        dTO.setJournalsSerial(entity.getJournalsSerial());
        dTO.setJournalsDailyBalanced(entity.getJournalsDailyBalanced());
        dTO.setJournalsEdit(entity.getJournalsEdit());
        dTO.setAccountStatementAppearance(entity.getAccountStatementAppearance());
        dTO.setAllowSubsequentCacheAdministration(entity.getAllowSubsequentCacheAdministration());
        dTO.setWorkWithAccounts(entity.getWorkWithAccounts());
        dTO.setNoteSreceivablesType(entity.getNoteSreceivablesType());

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        return dTO;
    }

    private AccountsSystemSettings mapFromDTO(AccountsSystemSettingsDTO dTO, TobyUser tobyUser) {
        AccountsSystemSettings entity = new AccountsSystemSettings();

        entity.setJournalsSerial(dTO.getJournalsSerial());
        entity.setJournalsDailyBalanced(dTO.getJournalsDailyBalanced());
        entity.setJournalsEdit(dTO.getJournalsEdit());
        entity.setAccountStatementAppearance(dTO.getAccountStatementAppearance());
        entity.setAllowSubsequentCacheAdministration(dTO.getAllowSubsequentCacheAdministration());
        entity.setWorkWithAccounts(dTO.getWorkWithAccounts());
        entity.setNoteSreceivablesType(dTO.getNoteSreceivablesType());

        if (dTO.getId() == null) {
            entity.setCreatedBy(tobyUser);
            entity.setCreationDate(new Date());
        } else {
            TobyUser tobyUser1 = new TobyUser();
            tobyUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(tobyUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(tobyUser);
        }

        entity.setCompanyId(tobyUser.getCompanyId());
        return entity;
    }
}
