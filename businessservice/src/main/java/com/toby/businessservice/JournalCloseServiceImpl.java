/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.JournalCloseDTO;
import com.toby.dto.JournalCloseDetailsDTO;
import com.toby.entity.InvInventory;
import com.toby.entity.JournalClose;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq004
 */
@Stateless
public class JournalCloseServiceImpl implements JournalCloseService {

    @EJB
    private GenericDAO dao;
    
    @EJB
    private JournalCloseDetailsService journalCloseDetailsService ;
    
    private JournalCloseDTO mapToDTO (JournalClose entity){
     JournalCloseDTO  journalCloseDTO = new JournalCloseDTO();
     
     if(entity.getGalleryId() != null){
         InvInventoryDTO invInventoryDTO = new InvInventoryDTO();
         invInventoryDTO.setId(entity.getGalleryId().getId());
         journalCloseDTO.setGalleryId(invInventoryDTO);
     }
      journalCloseDTO.setDate(entity.getDate());
      
      
      return journalCloseDTO ;
    }
    
    
    private JournalClose mapToEntity (JournalCloseDTO dTO){
       JournalClose journalClose = new JournalClose();
        if(dTO.getGalleryId() != null){
            InvInventory invInventory = new InvInventory();
            invInventory.setId(dTO.getGalleryId().getId());
          journalClose.setGalleryId(invInventory);
        }
        journalClose.setDate(dTO.getDate());
      
        return journalClose;
    }

    @Override
    public JournalCloseDTO save(JournalCloseDTO jounalCloseDto , List<JournalCloseDetailsDTO> deatailsList) {
        try {
            

            JournalClose entity;
            if (jounalCloseDto != null && (jounalCloseDto.getMsg() == null || jounalCloseDto.getMsg().isEmpty())) {
                entity = mapToEntity(jounalCloseDto);
                if (jounalCloseDto.getId() != null) {
                    //بعمل حفظ فى حالة التعديل
                    entity = dao.updateEntity(entity);
                } else {
                   
                    entity = dao.updateEntity(entity);
                }
                jounalCloseDto = mapToDTO(entity);
                for(JournalCloseDetailsDTO journalCloseDetailsDTO : deatailsList){
                 journalCloseDetailsDTO.setJounalId(entity);
                 journalCloseDetailsService.save(journalCloseDetailsDTO);
                }
            }
        } catch (Exception e) {
            jounalCloseDto.setMsg(e.toString());
            dao.rollbackQuery();
        }

        return jounalCloseDto;
    }
}
