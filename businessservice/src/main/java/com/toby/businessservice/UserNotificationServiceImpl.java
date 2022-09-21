/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.UserNotificationsDTO;
import com.toby.entity.UserNotifications;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author H
 */
@Stateless
public class UserNotificationServiceImpl implements UserNotificationsService {

    @EJB
    private GenericDAO dao;

    public UserNotifications mapToEntity(UserNotificationsDTO notificationsDTO) {

        UserNotifications userNotifications = new UserNotifications();
        userNotifications.setId(notificationsDTO.getId());
        userNotifications.setIsOpened(notificationsDTO.getIsOpened());
        userNotifications.setIsReaded(notificationsDTO.getIsReaded());
        userNotifications.setUserId(notificationsDTO.getUserId());
        userNotifications.setEventId(notificationsDTO.getEventId());
        return userNotifications;
    }

    public UserNotificationsDTO mapToDTO(UserNotifications notifications) {

        UserNotificationsDTO notificationsDTO = new UserNotificationsDTO();
        notificationsDTO.setId(notifications.getId());
        notificationsDTO.setIsOpened(notifications.getIsOpened());
        notificationsDTO.setIsReaded(notifications.getIsReaded());
        notificationsDTO.setEventId(notifications.getEventId());
        return notificationsDTO;
    }
     public List<UserNotificationsDTO> mapToDtoList(List<UserNotifications> entityList){
         List<UserNotificationsDTO> dtoList = new ArrayList<>();
         for(UserNotifications entity : entityList){
            UserNotificationsDTO dto = mapToDTO(entity);
             dtoList.add(dto);
         }
         return dtoList;
     }
    
    
        @Override
        public List<UserNotificationsDTO> getUserNotificationsbyUserId(Integer userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        List<UserNotifications> notifications = dao.findEntityByQuery("SELECT e FROM UserNotifications e WHERE e.userId = :userId", params);
        return mapToDtoList(notifications);
    }
        
        @Override
        public List<UserNotificationsDTO> getUnreadedUserNotificationsbyUserId(Integer userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        List<UserNotifications> notifications = dao.findListByQuery("SELECT e FROM UserNotifications e WHERE e.userId = :userId and (e.isReaded is null or e.isReaded = 0)", params);
        return mapToDtoList(notifications);
    }
        
        @Override
        public UserNotificationsDTO getUserNotificationsbyeventId(Integer eventId) {
        Map<String, Object> params = new HashMap<>();
        params.put("eventId", eventId);
        UserNotifications notifications = dao.findEntityByQuery("SELECT e FROM UserNotifications e WHERE e.eventId = :eventId", params);
        return mapToDTO(notifications);
    }
        
        
    public UserNotificationsDTO save(UserNotificationsDTO dto) {
        UserNotifications entity = mapToEntity(dto);
        entity = dao.updateEntity(entity);
        return mapToDTO(entity);
    }
    
    @Override
    public void saveList(List<UserNotificationsDTO> dtoList) {
        for(UserNotificationsDTO dTO : dtoList){
            save(dTO);
        }
    }


}
