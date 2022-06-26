/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author WIN7
 */
@Entity
@Table(name = "user_notifications")
@XmlRootElement
public class UserNotifications implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "event_id")
    private Integer eventId;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "is_opened")
    private Integer isOpened;
    @Column(name = "is_readed")
    private Integer isReaded;
    public UserNotifications() {
    }

    public UserNotifications(Integer id) {
        this.id = id;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserNotifications)) {
            return false;
        }
        UserNotifications other = (UserNotifications) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
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
