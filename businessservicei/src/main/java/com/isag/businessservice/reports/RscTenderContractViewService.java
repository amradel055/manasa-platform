/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.reports.searchBean.RscTenderContractReportSearchBean;
import com.isag.dto.RscTenderContractViewDTO;

import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface RscTenderContractViewService {

 public List<RscTenderContractViewDTO> getAllRscTenderContractViewByDateSearchBean(RscTenderContractReportSearchBean rscTenderContractReportSearchBean,Integer branchId) ;
  

}
