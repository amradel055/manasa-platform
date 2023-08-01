/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.entity.InboxMessages;
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
public class InboxMessageServiceImpl implements InboxMessageService {

    @EJB
    private GenericDAO dao;
    @EJB
    private InboxReplyService inboxReplyService;

    @Override
    public InboxMessages save(InboxMessages inboxMessages) {
        inboxMessages = dao.updateEntity(inboxMessages);
        return inboxMessages;
    }

    @Override
    public void delete(Integer id) {
        InboxMessages studentPeriodsOrder = dao.findEntityById(InboxMessages.class, id);
        dao.deleteEntity(studentPeriodsOrder);
    }

    @Override
    public InboxMessages findById(Integer id) {
        InboxMessages inboxMessages = dao.findEntityById(InboxMessages.class, id);
        List<InboxReply> replys = inboxReplyService.find(id);
        inboxMessages.setInboxReplys(replys);
        return inboxMessages;
    }
    
    @Override
    public List<InboxMessages> find(Integer studentId , Integer teacherId) {
        Map<String, Object> params = new HashMap<>();
        
        StringBuilder builder = new StringBuilder();
        
        builder.append("SELECT d FROM InboxMessages d WHERE 1=1    ");
        if(studentId != null){
            params.put("teacherId", teacherId);
            builder.append(" AND d.teacherId = :teacherId ");
        }
        if(teacherId != null){
            params.put("studentId", studentId);
            builder.append(" AND d.studentId = :studentId");
        }
        builder.append(" order by d.sentDate ") ;
        
        List<InboxMessages> list = dao.findListByQuery(builder.toString(), params);
        return list;
    }

}
