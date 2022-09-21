/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.google.firebase.database.DatabaseReference;
import com.toby.entity.Branch;
import com.toby.entity.TobyUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface FireBaseNotificationServices {


    public DatabaseReference readNotifications();

    public void saveNotifications(Integer branchId, Integer eventId);

}
