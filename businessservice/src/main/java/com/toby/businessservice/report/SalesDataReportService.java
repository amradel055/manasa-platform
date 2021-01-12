/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice.report;

import com.toby.businessservice.reports.searchBean.SalesDataReportSearchBean;
import com.toby.entity.InventoryDelegator;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hhhh
 */
@Remote
public interface SalesDataReportService {

   public List<InventoryDelegator> getAllInventoryDelegatorSearchBean(SalesDataReportSearchBean salesDataReportSearchBean);
  


}
