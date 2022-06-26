/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;


import com.toby.dto.UserNotificationsDTO;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author H
 */
@Remote
public interface UserNotificationsService {
    
 public List<UserNotificationsDTO> getUserNotificationsbyUserId(Integer userId);
 
 public List<UserNotificationsDTO> getUnreadedUserNotificationsbyUserId(Integer userId);
 
 public void saveList(List<UserNotificationsDTO> dtoList);
 
 public UserNotificationsDTO getUserNotificationsbyeventId(Integer eventId);
 
}
