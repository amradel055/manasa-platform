/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.TobyNotificationSettingDTO;
import com.toby.entity.TobyUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author ahmedEssam
 */
@Remote
public interface TobyNotificationSettingService {

    public TobyNotificationSettingDTO addTobyNotificationSettingDTO(TobyNotificationSettingDTO tobyNotificationSettingDTO, TobyUser tobyUser);

    public List<TobyNotificationSettingDTO> getAlltobyNotificationSettingDTO(Integer branchid);
    
    public TobyNotificationSettingDTO getTobyNotificationSettingDTOByUserId(Integer branchId , Integer userId);
     
}
