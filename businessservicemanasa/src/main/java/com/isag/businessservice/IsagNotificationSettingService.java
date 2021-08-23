/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.IsagNotificationSettingDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author ahmedEssam
 */
@Remote
public interface IsagNotificationSettingService {

    public IsagNotificationSettingDTO addIsagNotificationSettingDTO(IsagNotificationSettingDTO isagNotificationSettingDTO, IsagUser isagUser);

    public List<IsagNotificationSettingDTO> getAllisagNotificationSettingDTO(Integer branchid);
    
    public IsagNotificationSettingDTO getIsagNotificationSettingDTOByUserId(Integer branchId , Integer userId);
     
}
