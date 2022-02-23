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
import java.text.DateFormat;
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
 * @author hq004
 */
@Stateless
public class CurrencyOperationServiceImpl implements CurrencyOperationService {
    
    @EJB
    private GenericDAO dao;
    
    @Override
    public void updateCurrencyOpration(CurrencyOperation currencyOperation) {
        dao.updateEntity(currencyOperation);
    }
    
    @Override
    public List<CurrencyOperation> getAllCurrencyOperation() {
        return dao.findAll(CurrencyOperation.class);
    }
    
    @Override
    public CurrencyOperation addCurrencyOperation(CurrencyOperation currencyOperation) {
        return dao.updateEntity(currencyOperation);
    }
    
    @Override
    public void deleteCurrencyOperation(CurrencyOperation currencyOperation) {
        dao.deleteEntity(currencyOperation);
    }
    
    public List<CurrencyOperation> findAllCurrencyChildByParentId(Integer parentId) {
        List<CurrencyOperation> currencyOperations = dao.executeNativeQuery("SELECT * FROM currency_operation WHERE  currency_id = " + parentId);
        return currencyOperations;
    }
    
    public CurrencyOperation findCurrencyDetail(Integer currencyID, Date date) {
        if (currencyID != null && currencyID != 0) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date1 = dateFormat.format(date);
            List<CurrencyOperation> list;
            List<CurrencyOperation> currencyOperations = dao.executeNativeQuery("SELECT * FROM currency_operation WHERE currency_id = " + currencyID
                    + " AND operation_date >= " + date1 + " ORDER BY operation_date DESC");
            if (!currencyOperations.isEmpty()) {
                return currencyOperations.get(0);
            } else {
                return null;
            }
        }
        return null;
    }
    
    @Override
    public CurrencyOperation getOperationsByCurrency(Integer currencyId) {
        CurrencyOperation co = dao.findEntityByQuery("SELECT cp FROM CurrencyOperation cp WHERE cp.id=(SELECT max(c.id) FROM CurrencyOperation c WHERE c.currencyId.id =" + currencyId + ")");
        return co;
    }
    
    @Override
    public CurrencyOperation getRatesByDates(Integer currencyId, Date operationDate, Integer companyId) {
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
        }
        return currencyOperation;
    }
    
    @Override
    public CurrencyOperationDTO getRatesByDatesDTO(Integer currencyId, Date operationDate, Integer companyId) {
        CurrencyOperation currencyOperation = null;
        Map<String, Object> params = new HashMap<>();
        CurrencyOperationDTO currencyOperationDTO = null;
        String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(operationDate);
        StringBuilder queryBuilder = new StringBuilder();
        params.put("currencyId", currencyId);
        params.put("companyId", companyId);
        String query = "SELECT co FROM CurrencyOperation co WHERE co.currencyId.id =:currencyId AND co.companyId.id =:companyId AND co.operationDate <= '" + queryBuilder.append(formatDateFrom).append("' ORDER BY co.operationDate DESC ");
        List<CurrencyOperation> currencyOperationList = dao.findListByQuery(query, params);
        if (currencyOperationList != null && !currencyOperationList.isEmpty()) {
            currencyOperation = currencyOperationList.get(0);
            currencyOperationDTO = mapTODTO(currencyOperation, false);
        }
        return currencyOperationDTO;
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
        
        dTO.setRate(entity.getRate());
        dTO.setNotes(entity.getNotes());
        dTO.setOperationDate(entity.getOperationDate());
        
        if(entity.getCurrencyId() != null){
            CurrencyDTO currencyDTO = new CurrencyDTO();
            currencyDTO.setId(entity.getCurrencyId().getId());
            dTO.setCurrencyId(currencyDTO);
        }
        
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        
        return dTO;
    }
    
    private CurrencyOperation mapToEntity(CurrencyOperationDTO dTO, IsagUser isagUser) {
        CurrencyOperation entity = new CurrencyOperation();
        
        entity.setRate(dTO.getRate());
        entity.setNotes(dTO.getNotes());
        entity.setOperationDate(dTO.getOperationDate());
        
        if(dTO.getCurrencyId() != null){
            Currency currency = new Currency();
            currency.setId(dTO.getCurrencyId().getId());
            entity.setCurrencyId(currency);
        }
        
        if (dTO.getId() == null) {
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
