/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.CrmSettingDTO;
import com.isag.entity.IsagUser;
import javax.ejb.Remote;

/**
 *
 * @author ahmedEssam
 */

@Remote
public interface CrmSettingService {
    
    public CrmSettingDTO addCrmSettingDTO(CrmSettingDTO crmSettingDTO, IsagUser isagUser);

    public CrmSettingDTO getCrmSettingDTOByUserId(Integer branchId , Integer userId);
}
