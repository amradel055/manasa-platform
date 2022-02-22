/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.IgmaNotificationSettingDTO;
import com.isag.entity.IgmaUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author ahmedEssam
 */
@Remote
public interface IgmaNotificationSettingService {

    public IgmaNotificationSettingDTO addIsagNotificationSettingDTO(IgmaNotificationSettingDTO isagNotificationSettingDTO, IgmaUser isagUser);

    public List<IgmaNotificationSettingDTO> getAllisagNotificationSettingDTO(Integer branchid);
    
    public IgmaNotificationSettingDTO getIsagNotificationSettingDTOByUserId(Integer branchId , Integer userId);
     
}
