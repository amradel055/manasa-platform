/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.CostCenterDTO;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author AhmedEssam
 */
@Remote
public interface CostCenterServiceDTO {
    
     public List<CostCenterDTO> getAllSubCostCenterByBranchIdActive(Integer id);
     
      public CostCenterDTO findCostCenter(Integer costCenterId);
}
