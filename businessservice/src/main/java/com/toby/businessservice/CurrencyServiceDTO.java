/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.CurrencyDTO;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author AhmedEssam
 */
@Remote
public interface CurrencyServiceDTO {

    public List<CurrencyDTO> getAllCurrenciesHavingRatesByCompanyId(Integer companyId);
    
    public List<CurrencyDTO> getAllCurrenciesByCompanyId(Integer companyId);
    
    public CurrencyDTO getDefaultCurrenciesByCompanyId(Integer companyId);
    
}
