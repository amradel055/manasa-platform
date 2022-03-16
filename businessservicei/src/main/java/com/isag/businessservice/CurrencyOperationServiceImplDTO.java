/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.CurrencyDTO;
import com.isag.dto.CurrencyOperationDTO;
import com.isag.entity.Currency;
import com.isag.entity.CurrencyOperation;
import com.isag.entity.IsagUser;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
public class CurrencyOperationServiceImplDTO implements CurrencyOperationServiceDTO {

    @EJB
    private GenericDAO dao;

    @Override
    public CurrencyOperationDTO getRatesByDates(Integer currencyId, Date operationDate, Integer companyId) {
        CurrencyOperation currencyOperation = null;
        Map<String, Object> params = new HashMap<>();
        String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(operationDate);
        StringBuilder queryBuilder = new StringBuilder();
        params.put("currencyId", currencyId);
        params.put("companyId", companyId);
        String query = "SELECT co FROM CurrencyOperation co WHERE co.currencyId.id =:currencyId AND co.companyId.id =:companyId AND co.operationDate <= '" + queryBuilder.append(formatDateFrom).append("' ORDER BY co.operationDate DESC ");
        List<CurrencyOperation> currencyOperationList = dao.findListByQuery(query, params);
        if (currencyOperationList != null && !currencyOperationList.isEmpty()) {
            currencyOperation = currencyOperationList.get(0);
            return initMapToDTO(currencyOperation);
        } else {
            params.remove("currencyId");
            query = "SELECT co FROM CurrencyOperation co WHERE  co.companyId.id =:companyId  AND co.currencyId.serial = 1 ORDER BY co.operationDate DESC ";
            currencyOperation = dao.findEntityByQuery(query, params);
            return initMapToDTO(currencyOperation);
        }

    }

    private List<CurrencyOperationDTO> returnListDTO(List<CurrencyOperation> list) {
        List<CurrencyOperationDTO> dTOList = new ArrayList<>();
        for (CurrencyOperation entity : list) {
            dTOList.add(mapTODTO(entity, false));
        }
        return dTOList;
    }

    private CurrencyOperationDTO mapTODTO(CurrencyOperation entity, Boolean needDetail) {
        CurrencyOperationDTO dTO = initMapToDTO(entity);
        if (needDetail) {

        }
        return dTO;
    }

    private CurrencyOperationDTO initMapToDTO(CurrencyOperation entity) {

        CurrencyOperationDTO dTO = new CurrencyOperationDTO();
        dTO.setId(entity.getId());

        if (entity.getCurrencyId() != null && entity.getCurrencyId().getId() != null) {
            CurrencyDTO currencyDTO = new CurrencyDTO();
            currencyDTO.setId(entity.getId());
            dTO.setCurrencyId(currencyDTO);
        }

        dTO.setOperationDate(entity.getOperationDate());
        dTO.setNotes(entity.getNotes());
        dTO.setRate(entity.getRate());

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());

        return dTO;
    }

    private CurrencyOperation mapFromDTO(CurrencyOperationDTO dTO, IsagUser isagUser) {
        CurrencyOperation entity = new CurrencyOperation();

        if (dTO.getCurrencyId() != null && dTO.getCurrencyId().getId() != null) {
            Currency currency = new Currency();
            currency.setId(dTO.getId());
            entity.setCurrencyId(currency);
        }

        entity.setOperationDate(dTO.getOperationDate());
        entity.setNotes(entity.getNotes());
        entity.setRate(entity.getRate());

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

//        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }
}
