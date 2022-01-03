
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.JournalCloseDTO;
import com.toby.dto.JournalCloseDetailsDTO;
import com.toby.entity.JournalClose;
import com.toby.entity.JournalCloseDetails;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq004
 */
@Stateless
public class JournalCloseDetailsServiceImpl implements JournalCloseDetailsService {

    @EJB
    private GenericDAO dao;
    
    
   
    @Override
    public JournalCloseDetailsDTO save(JournalCloseDetailsDTO jounalCloseDeatailsDto) {
        try {
        
            JournalCloseDetails entity;
            if (jounalCloseDeatailsDto != null && (jounalCloseDeatailsDto.getMsg() == null || jounalCloseDeatailsDto.getMsg().isEmpty())) {
                entity = mapToEntity(jounalCloseDeatailsDto);
                if (jounalCloseDeatailsDto.getId() != null) {
                    //بعمل حفظ فى حالة التعديل
                    entity = dao.updateEntity(entity);
                } else {
                   
                    entity = dao.updateEntity(entity);
                }
          
                }
            
        } catch (Exception e) {
            jounalCloseDeatailsDto.setMsg(e.toString());
            dao.rollbackQuery();
        }

        return jounalCloseDeatailsDto;
    }
    
    private JournalCloseDetailsDTO mapToDTO (JournalCloseDetails entity){
     JournalCloseDetailsDTO  closeDetailsDTO = new JournalCloseDetailsDTO();
     
     if(entity.getJounalId()!= null){
          closeDetailsDTO.setJounalId(entity.getJounalId());
     }
      closeDetailsDTO.setImageName(entity.getImageName());
      
      
      return closeDetailsDTO ;
    }
    
    
    private JournalCloseDetails mapToEntity (JournalCloseDetailsDTO dTO){
       JournalCloseDetails journalCloseDetials = new JournalCloseDetails();
        if(dTO.getJounalId()!= null){
          journalCloseDetials.setJounalId(dTO.getJounalId());
        }
        journalCloseDetials.setImageName(dTO.getImageName());
      
        return journalCloseDetials;
    }

   
}
