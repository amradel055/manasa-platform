/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.CurrencyOperationDTO;
import java.util.Date;
import javax.ejb.Remote;

/**
 *
 * @author AhmedEssam
 */
@Remote
public interface CurrencyOperationServiceDTO {
    
    public CurrencyOperationDTO getRatesByDates(Integer currencyId, Date operationDate, Integer companyId);
    
}
