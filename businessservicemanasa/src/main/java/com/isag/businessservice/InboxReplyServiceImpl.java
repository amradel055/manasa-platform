/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.InboxReplyDTO;
import com.isag.entity.IgmaUser;
import com.isag.entity.InboxReply;
import java.util.ArrayList;
import java.util.Date;
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
    public InboxReply save(InboxReply inboxReply) {
        inboxReply = dao.updateEntity(inboxReply);
        return inboxReply;
    }
    
    @Override
    public InboxReplyDTO save(InboxReplyDTO dto  , IgmaUser igmaUser) {
//        inboxReply = dao.updateEntity(inboxReply);
//        return inboxReply;
         if (dto != null) {
            InboxReply entity = mapToEntity(dto, igmaUser);
            entity = dao.updateEntity(entity);
            dto.setId(entity.getId());
            return dto;
        }
        return null;
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
    public List<InboxReplyDTO> find(Integer messageId) {
        Map<String, Object> params = new HashMap<>();
        
        StringBuilder builder = new StringBuilder();
        
        builder.append("SELECT d FROM InboxReply d WHERE 1=1    ");
        if(messageId != null){
            params.put("messageId", messageId);
            builder.append(" AND d.message = :messageId ");
        }
        builder.append(" order by d.sentDate ") ;
        
        List<InboxReply> list = dao.findListByQuery(builder.toString(), params);
        if(list != null && !list.isEmpty()){
                    List<InboxReplyDTO> listDTO = new ArrayList<>();

            for(InboxReply entity : list){
                listDTO.add(mapToDTO(entity));
            }
            return listDTO;
        }

        return new ArrayList<>();
    }
    
    

    private InboxReplyDTO mapToDTO(InboxReply entity) {
        if (entity != null) {
            InboxReplyDTO dto = new InboxReplyDTO();
            dto.setId(entity.getId());
            dto.setBody(entity.getBody());
            dto.setSentDate(entity.getSentDate());
            dto.setMessage(entity.getMessage());
            dto.setImage(entity.getImage());
            return dto;
        }
        return null;
    }

    private InboxReply mapToEntity(InboxReplyDTO dto, IgmaUser igmaUser) {
        if (dto != null) {
            InboxReply entity = new InboxReply();
            entity.setId(dto.getId());
            entity.setBody(dto.getBody());
            entity.setSentDate(dto.getSentDate());
            entity.setMessage(dto.getMessage());
            entity.setImage(dto.getImage());

            if (dto.getId() == null) {
                entity.setCreatedBy(igmaUser);
                entity.setCreationDate(new Date());
            } else {
                entity.setId(dto.getId());
                entity.setModificationDate(new Date());
                entity.setModifiedBy(igmaUser);
            }

            entity.setCompanyId(igmaUser.getCompanyId());

            return entity;
        }
        return null;
    }


}
