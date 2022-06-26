/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.dto;

/**
 *
 * @author amr
 */
public class UserNotificationsDTO  {

 
    private Integer id;
    private Integer isOpened;
    private Integer userId;
    private Integer eventId;
    private Integer isReaded;


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserNotificationsDTO)) {
            return false;
        }
        UserNotificationsDTO other = (UserNotificationsDTO) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }


    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

  

    /**
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return the eventId
     */
    public Integer getEventId() {
        return eventId;
    }

    /**
     * @param eventId the eventId to set
     */
    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    /**
     * @return the isOpened
     */
    public Integer getIsOpened() {
        return isOpened;
    }

    /**
     * @param isOpened the isOpened to set
     */
    public void setIsOpened(Integer isOpened) {
        this.isOpened = isOpened;
    }

    /**
     * @return the isReaded
     */
    public Integer getIsReaded() {
        return isReaded;
    }

    /**
     * @param isReaded the isReaded to set
     */
    public void setIsReaded(Integer isReaded) {
        this.isReaded = isReaded;
    }

}
