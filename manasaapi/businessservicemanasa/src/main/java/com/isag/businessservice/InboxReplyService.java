/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.InboxReplyDTO;
import com.isag.entity.IgmaUser;
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
    
    public InboxReplyDTO save(InboxReplyDTO dto,IgmaUser igmaUser) ;
    
     public InboxReply findById(Integer id) ;

    public void delete(Integer id);

    public List<InboxReplyDTO> find(Integer messageId);

}
