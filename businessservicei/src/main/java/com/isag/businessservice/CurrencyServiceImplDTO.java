/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.CurrencyDTO;
import com.isag.entity.Currency;
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
 * @author AhmedEssam
 */
@Stateless
public class CurrencyServiceImplDTO implements CurrencyServiceDTO {

    @EJB
    private GenericDAO dao;

    @Override
    public List<CurrencyDTO> getAllCurrenciesHavingRatesByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        List<Currency> detail = new ArrayList<>();
        detail = dao.findListByQuery("SELECT c FROM Currency c WHERE c.id in (SELECT co.currencyId.id FROM CurrencyOperation co WHERE co.companyId.id =:companyId)", params);
        return returnListDTO(detail);
    }
    
    @Override
    public List<CurrencyDTO> getAllCurrenciesByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        List<Currency> detail = new ArrayList<>();
        detail = dao.findListByQuery("SELECT c FROM Currency c WHERE c.companyId.id = :companyId ", params);
        return returnListDTO(detail);
    }
    
    @Override
    public CurrencyDTO getDefaultCurrenciesByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", companyId);
        List<Currency> detail = new ArrayList<>();
        detail = dao.findListByQuery("SELECT c FROM Currency c WHERE c.companyId.id = :companyId ", params);
        if(detail != null && !detail.isEmpty()){
            return mapTODTO(detail.get(0), Boolean.FALSE);
        }
        return null;
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
        dTO.setSubName(entity.getSubName());
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
        entity.setSubName(dTO.getSubName());
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
