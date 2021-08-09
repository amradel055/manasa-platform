/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.CurrencyDTO;
import com.isag.entity.Currency;
import com.isag.entity.CurrencyOperation;
import com.isag.entity.IsagUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq004
 */
@Stateless
public class CurrencyServiceImpl implements CurrencyService {

    @EJB
    private GenericDAO dao;

    @Override
    public Currency updateCurrency(Currency currency) {
        return dao.updateEntity(currency);
    }

    @Override
    public List<Currency> getAllCurrency() {
        return dao.findAll(Currency.class);
    }

    @Override
    public void addCurrency(Currency currency) {
        dao.updateEntity(currency);
    }

    @Override
    public List<CurrencyOperation> getCurrencyOperationForCurrency(Integer id) {
        Map<String, Object> params = new HashMap<>();
        params.put("currencyId", id);
        List<CurrencyOperation> currencyOperations = dao.findListByQuery("SELECT e FROM " + CurrencyOperation.class.getSimpleName() + " e WHERE e.currencyId.id=:currencyId", params);
        return currencyOperations;
    }

    public List<Currency> findAllCurrencyPerant() {
        List<Currency> currencys = dao.executeNativeQuery("SELECT * FROM Currency WHERE deleteby is null");
        return currencys;
    }

    @Override
    public List<Currency> getAllCurrencyByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT e FROM Currency e WHERE e.companyId.id =:companyId ORDER BY e.serial", params);
    }

    @Override
    public void deleteCurrency(Currency currency) {
        dao.deleteEntity(currency);
    }

    @Override
    public Currency findCurrencyById(Integer currencyId) {
        return dao.findEntityById(Currency.class, currencyId);
    }

    @Override
    public List<Currency> getAllCurrencyByCompanyIdAndCode(Integer companyId, Currency currency) {
        Map<String, Object> params = new HashMap();
        params.put("id", companyId);
        params.put("name", currency.getName());
        params.put("serial", currency.getSerial());
        if (currency.getId() == null) {
            return dao.findListByQuery("SELECT c FROM Currency c WHERE c.companyId.id = :id AND (c.serial = :serial OR c.name = :name)", params);
        } else {
            params.put("currencyId", currency.getId());
            return dao.findListByQuery("SELECT c FROM Currency c WHERE c.id != :currencyId AND c.companyId.id = :id AND (c.serial = :serial OR c.name = :name)", params);
        }
    }

    @Override
    public List<Currency> getAllCurrenciesHavingRatesByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT c FROM Currency c WHERE c.id in (SELECT co.currencyId.id FROM CurrencyOperation co WHERE co.companyId.id =:companyId)", params);
    }

    @Override
    public List<Currency> getAllCurrenciesWithLocalCurrencyHavingRatesByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        StringBuilder query = new StringBuilder();
        query.append("SELECT cu FROM Currency cu where cu.companyId.id = :companyId  ");
        List<Currency> currencys = dao.findListByQuery(query.toString(), params);
//        query.append(" UNION ALL ");

        query = new StringBuilder();
        query.append(" SELECT c FROM Currency c WHERE c.id in (SELECT co.currencyId.id FROM CurrencyOperation co WHERE co.companyId.id =:companyId)");

        List<Currency> currencys1 = dao.findListByQuery(query.toString(), params);
        currencys1.add(0, currencys.get(0));
        return currencys1;
    }

    @Override
    public Currency getCurrencyByCompanyAndCode(Integer companyId, Integer serial) {
        Map<String, Object> params = new HashMap();
        List<Currency> currencyList = null;
        params.put("id", companyId);
        params.put("serial", serial);

        currencyList = dao.findListByQuery("SELECT c FROM Currency c WHERE c.companyId.id = :id AND c.serial = :serial", params);

        if (currencyList != null && currencyList.size() > 0) {
            return currencyList.get(0);
        }
        return null;

    }

    @Override
    public List<CurrencyDTO> getAllCurrenciesWithLocalCurrencyHavingRatesByCompanyIdDTO(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        StringBuilder query = new StringBuilder();
        query.append("SELECT cu FROM Currency cu where cu.companyId.id = :companyId  ");
        List<Currency> currencys = dao.findListByQuery(query.toString(), params);
//        query.append(" UNION ALL ");

        query = new StringBuilder();
        query.append(" SELECT c FROM Currency c WHERE c.id in (SELECT co.currencyId.id FROM CurrencyOperation co WHERE co.companyId.id =:companyId)");

        List<Currency> currencys1 = dao.findListByQuery(query.toString(), params);
        currencys1.add(0, currencys.get(0));
        return returnListDTO(currencys1);
    }

    private List<CurrencyDTO> returnListDTO(List<Currency> list) {
        List<CurrencyDTO> dTOList = new ArrayList<>();
        for (Currency entity : list) {
            dTOList.add(mapTODTO(entity, false));
        }
        return dTOList;
    }

    private CurrencyDTO mapTODTO(Currency entity, Boolean needDetail) {
        CurrencyDTO dTO = initMapToDTO(entity);
        if (needDetail) {

        }
        return dTO;
    }

    private CurrencyDTO initMapToDTO(Currency entity) {

        CurrencyDTO dTO = new CurrencyDTO();
        dTO.setId(entity.getId());

        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
        dTO.setSerial(entity.getSerial());

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());

        return dTO;
    }

    private Currency mapFromDTO(CurrencyDTO dTO, IsagUser isagUser) {
        Currency entity = new Currency();

        entity.setCode(dTO.getCode());
        entity.setName(dTO.getName());
        entity.setSerial(dTO.getSerial());

        if (dTO.getId()
                == null) {
            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());
        } else {
            IsagUser isagUser1 = new IsagUser();
            isagUser1.setId(dTO.getCreatedBy());
            entity.setCreatedBy(isagUser1);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);
        }

        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }

}
