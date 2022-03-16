/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import java.math.BigDecimal;
import java.util.Date;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface analyticalRatiosService {

    public BigDecimal getCurrentRatio(Date startGlYear, Date endDate, StringBuilder stringBuilder);
    
     public BigDecimal getSpecificRatio(Date startGlYear, Date endDate, Integer accountId);
}
