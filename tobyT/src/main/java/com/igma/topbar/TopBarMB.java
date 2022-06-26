/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.topbar;

import com.toby.businessservice.FireBaseNotificationServices;
import com.toby.businessservice.NotificationsService;
import com.toby.businessservice.UserNotificationsService;
import com.toby.dto.UserNotificationsDTO;
import com.toby.toby.Basic;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "topBarMB")
@ViewScoped
public class TopBarMB extends Basic implements Serializable {

    @EJB
    FireBaseNotificationServices firebaseServices;

    @EJB
    NotificationsService notificationsService;
    
    @EJB
    UserNotificationsService userNotificationsService;

    private Integer onReadNotifications;
    private Integer lastEventId;


    public void updateNotificationsData() {
        UserNotificationsDTO notificationsDTO = userNotificationsService.getUserNotificationsbyeventId(getLastEventId());
        if(notificationsDTO == null){
        setOnReadNotifications(notificationsService.getUserUnreadNotificationsNumber(getUserData().getUser().getId(), getUserData().getGallaryIdList()).size());
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.getSessionMap().put("notificationNumber", getOnReadNotifications()); 
        }
    }

    /**
     * @return the onReadNotifications
     */
    public Integer getOnReadNotifications() {
        if(onReadNotifications == null){
         Integer notificationNumber = notificationsService.getUserUnreadNotificationsNumber(getUserData().getUser().getId(), getUserData().getGallaryIdList()).size();
         getContext().getSessionMap().put("notificationNumber", notificationNumber);
        }
        setOnReadNotifications((Integer) getContext().getSessionMap().get("notificationNumber"));

        return onReadNotifications;
    }

    /**
     * @param onReadNotifications the onReadNotifications to set
     */
    public void setOnReadNotifications(Integer onReadNotifications) {
        this.onReadNotifications = onReadNotifications;
    }

    /**
     * @return the lastEventId
     */
    public Integer getLastEventId() {
        return lastEventId;
    }

    /**
     * @param lastEventId the lastEventId to set
     */
    public void setLastEventId(Integer lastEventId) {
        this.lastEventId = lastEventId;
    }

}
