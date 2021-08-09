package com.isag.businessservice;

import com.isag.dto.CurrencyDTO;
import com.isag.entity.Currency;
import com.isag.entity.CurrencyOperation;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface CurrencyService {

    public List<Currency> getAllCurrency();

    public void addCurrency(Currency currency);

    public Currency updateCurrency(Currency currency);

    public List<CurrencyOperation> getCurrencyOperationForCurrency(Integer id);

    public List<Currency> getAllCurrencyByCompanyId(Integer companyId);

    public void deleteCurrency(Currency currency);

    public Currency findCurrencyById(Integer currencyId);

    public List<Currency> getAllCurrencyByCompanyIdAndCode(Integer selectedBranchId, Currency currency);

    public List<Currency> getAllCurrenciesHavingRatesByCompanyId(Integer companyId);

    public List<Currency> getAllCurrenciesWithLocalCurrencyHavingRatesByCompanyId(Integer companyId);

    public Currency getCurrencyByCompanyAndCode(Integer companyId, Integer serial);
    
    public List<CurrencyDTO> getAllCurrenciesWithLocalCurrencyHavingRatesByCompanyIdDTO(Integer companyId);
    
}
