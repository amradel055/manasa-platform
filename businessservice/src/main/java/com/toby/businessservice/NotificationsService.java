/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;


import javax.ejb.Remote;

/**
 *
 * @author H
 */
@Remote
public interface NotificationsService {

 public Integer updateNotifications(Integer userId , Integer eventId);
 public Integer getUserUnreadNotificationsNumber(Integer userId);
}
