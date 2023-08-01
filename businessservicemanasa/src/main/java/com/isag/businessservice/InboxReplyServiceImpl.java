/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.entity.IgmaUser;
import com.isag.entity.InboxReply;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq003
 */
@Stateless
public class InboxReplyServiceImpl implements InboxReplyService {

    @EJB
    private GenericDAO dao;
    

    @Override
    public InboxReply save(InboxReply inboxReply, IgmaUser isagUser) {
        inboxReply = dao.updateEntity(inboxReply);
        return inboxReply;
    }

    @Override
    public void delete(Integer id) {
        InboxReply studentPeriodsOrder = dao.findEntityById(InboxReply.class, id);
        dao.deleteEntity(studentPeriodsOrder);
    }

    @Override
    public InboxReply findById(Integer id) {
        InboxReply questionCommon = dao.findEntityById(InboxReply.class, id);
        return questionCommon;
    }
    
    @Override
    public List<InboxReply> find(Integer messageId) {
        Map<String, Object> params = new HashMap<>();
        
        StringBuilder builder = new StringBuilder();
        
        builder.append("SELECT d FROM InboxReply d WHERE 1=1    ");
        if(messageId != null){
            params.put("messageId", messageId);
            builder.append(" AND d.message = :teacherId ");
        }
        builder.append(" order by d.sentDate ") ;
        
        List<InboxReply> list = dao.findListByQuery(builder.toString(), params);
        
        return list;
    }

}
