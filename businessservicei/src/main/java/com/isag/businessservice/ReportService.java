/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.resualtDTO;
import com.isag.dto.searchDTO;
import com.isag.entity.GmSubscriptions;
import com.isag.entity.IsagUser;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public interface ReportService {

    public List<resualtDTO> getReporySearch(searchDTO search);

    public List<resualtDTO> getAllValidSubsByDate(searchDTO search);

    public BigDecimal getAllValidSubsFinalNet(searchDTO search);

    public List<resualtDTO> getAllFreezeByDate(searchDTO search);

    public List<resualtDTO> getAllDeletedSubsByDate(searchDTO search);

    public resualtDTO findElresultById(resualtDTO reseualtdto);

   public void deleteGmSubscribtionDeleted(Integer id);

}
