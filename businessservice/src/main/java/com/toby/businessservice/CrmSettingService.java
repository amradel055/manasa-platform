/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.TobyUser;
import com.toby.dto.CrmSettingDTO;
import javax.ejb.Remote;

/**
 *
 * @author ahmedEssam
 */

@Remote
public interface CrmSettingService {
    
    public CrmSettingDTO addCrmSettingDTO(CrmSettingDTO crmSettingDTO, TobyUser tobyUser);

    public CrmSettingDTO getCrmSettingDTOByUserId(Integer branchId , Integer userId);
}
