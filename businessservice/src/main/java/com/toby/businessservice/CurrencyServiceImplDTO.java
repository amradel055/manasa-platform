/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.TobyUser;
import com.toby.core.GenericDAO;
import com.toby.dto.CurrencyDTO;
import com.toby.entity.Currency;
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

    private Currency mapFromDTO(CurrencyDTO dTO, TobyUser tobyUser) {
        Currency entity = new Currency();

        entity.setCode(dTO.getCode());
        entity.setName(dTO.getName());
        entity.setSerial(dTO.getSerial());

        if (dTO.getId()
                == null) {
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
}
