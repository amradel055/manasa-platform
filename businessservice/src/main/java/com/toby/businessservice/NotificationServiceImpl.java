/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.apiServices.AttributeService;
import com.toby.apiServices.VariationProductService;
import com.toby.businessservice.reports.searchBean.ItemMainDataByGroupSearchBean;
import com.toby.core.GenericDAO;
import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.AttributeDTO;
import com.toby.dto.CurrencyDTO;
import com.toby.dto.InvGroupDTO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.InvItemDTOApi;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.NotificationsDTO;
import com.toby.dto.ProGroupDTO;
import com.toby.dto.SymbolDTO;
import com.toby.dto.VariationDTO;
import com.toby.entity.Branch;
import com.toby.entity.InvBarcode;
import com.toby.entity.InvGroup;
import com.toby.entity.InvItem;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.Notifications;
import com.toby.entity.ProGroup;
import com.toby.entity.Symbol;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import com.toby.views.ItemsBarcodesDetailsView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
public class NotificationServiceImpl implements NotificationsService {

    @EJB
    private GenericDAO dao;

    public Notifications mapToEntity(NotificationsDTO notificationsDTO) {

        Notifications notifications = new Notifications();
        notifications.setId(notificationsDTO.getId());
        notifications.setNumber(notificationsDTO.getNumber());
        notifications.setUserId(notificationsDTO.getUserId());
        notifications.setEventId(notificationsDTO.getEventId());

        return notifications;
    }

    public NotificationsDTO mapToDTO(Notifications notifications) {

        NotificationsDTO notificationsDTO = new NotificationsDTO();
        notificationsDTO.setId(notifications.getId());
        notificationsDTO.setNumber(notifications.getNumber() != null ? notifications.getNumber() : 0);
        notificationsDTO.setUserId(notifications.getUserId());
        notificationsDTO.setEventId(notifications.getEventId());

        return notificationsDTO;
    }
    
    @Override
    public Integer updateNotifications(Integer userId , Integer eventId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        Notifications notifications = dao.findEntityByQuery("SELECT e FROM Notifications e WHERE e.userId = :userId ", params);
        NotificationsDTO dto = mapToDTO(notifications);
        if(dto.getEventId() != null && !dto.getEventId().equals(eventId))
        {
        Integer updateNumber = dto.getNumber()+1;
        dto.setNumber(updateNumber);
        dto.setEventId(eventId);
        notifications =  mapToEntity(dto);
        notifications =  dao.updateEntity(notifications);
        }
        return notifications.getNumber();
    }
        @Override
        public Integer getUserUnreadNotificationsNumber(Integer userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        Notifications notifications = dao.findEntityByQuery("SELECT e FROM Notifications e WHERE e.userId = :userId ", params);
        if(notifications == null){
          notifications = new Notifications();
          notifications.setUserId(userId);
          notifications.setNumber(0);
          notifications.setEventId(0);
          notifications =  dao.updateEntity(notifications);
        }
        NotificationsDTO dto = mapToDTO(notifications);
        return dto.getNumber();
    }


}
