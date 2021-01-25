package com.toby.businessservice;

import com.toby.dto.ProItemProductionStagesDTO;
import com.toby.entity.InvItem;
import com.toby.entity.TobyUser;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

@Remote
public interface ProItemProductionStagesService {
    
    public List<ProItemProductionStagesDTO> getAllInvItem(TobyUser tobyUser);
    
    public ProItemProductionStagesDTO save(ProItemProductionStagesDTO proItemProductionStagesDTO, TobyUser tobyUser);
    
     public List<ProItemProductionStagesDTO> getAll(TobyUser tobyUser, Integer proGroupId);
     
     public ProItemProductionStagesDTO findByDTO(TobyUser tobyUser,ProItemProductionStagesDTO dTO);
       
     public void deleteByStage(ProItemProductionStagesDTO itemStages);
     
     public void saveAll(Map<String, InvItem> invItemMap, TobyUser tobyUser);

}
