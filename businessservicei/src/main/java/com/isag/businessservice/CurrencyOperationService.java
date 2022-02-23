/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.CurrencyOperationDTO;
import com.isag.entity.CurrencyOperation;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface CurrencyOperationService {

    public void updateCurrencyOpration(CurrencyOperation currencyOperation);

    public List<CurrencyOperation> getAllCurrencyOperation();

    public CurrencyOperation getOperationsByCurrency(Integer currencyId);

    public CurrencyOperation addCurrencyOperation(CurrencyOperation currencyOperation);

    public void deleteCurrencyOperation(CurrencyOperation currencyOperation);

    public CurrencyOperation getRatesByDates(Integer currencyId ,Date operationDate , Integer companyId);
    
    public CurrencyOperationDTO getRatesByDatesDTO(Integer currencyId, Date operationDate, Integer companyId);

}
