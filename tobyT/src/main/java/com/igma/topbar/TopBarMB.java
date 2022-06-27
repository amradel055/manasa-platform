/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.topbar;

import com.toby.businessservice.FireBaseNotificationServices;
import com.toby.businessservice.NotificationsService;
import com.toby.toby.BaseFormBean;
import com.toby.toby.UserData;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "topBarMB")
@ViewScoped
public class TopBarMB extends BaseFormBean implements Serializable {

    @EJB
    FireBaseNotificationServices firebaseServices;

    @EJB
    NotificationsService notificationsService;

    private Integer onReadNotifications;
    private Integer lastEventId;

    @Override
    public String save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void updateNotificationsData() {
        setOnReadNotifications(notificationsService.updateNotifications(getUserData().getUser().getId(), getLastEventId()));
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.getSessionMap().put("notificationNumber", getOnReadNotifications());
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getScreenName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the onReadNotifications
     */
    public Integer getOnReadNotifications() {

        setOnReadNotifications((Integer) getContext().getSessionMap().get("notificationNumber"));

        return onReadNotifications;
    }

    /**
     * @param onReadNotifications the onReadNotifications to set
     */
    public void setOnReadNotifications(Integer onReadNotifications) {
        this.onReadNotifications = onReadNotifications;
    }

    @Override
    public void init() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
