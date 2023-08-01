/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.entity.InboxMessages;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface InboxMessageService {

    public InboxMessages save(InboxMessages inboxMessages) ;
    
     public InboxMessages findById(Integer id) ;

    public void delete(Integer id);

    public List<InboxMessages> find(Integer studentId , Integer teacherId);

}
