/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.entity.InboxReply;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface InboxReplyService {

    public InboxReply save(InboxReply inboxReply) ;
    
     public InboxReply findById(Integer id) ;

    public void delete(Integer id);

    public List<InboxReply> find(Integer messageId);

}
