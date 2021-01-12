package com.toby.businessservice;

import com.toby.dto.InvStripDTO;
import com.toby.dto.InvStripDetailDTO;
import com.toby.dto.QuantityItemsStoreDTO;
import com.toby.entity.InvStripDetail;
import com.toby.entity.TobyUser;

import javax.ejb.Remote;
import java.util.List;

/**
 * @author khaled
 */
@Remote
public interface InvStripDetailService {
    void addInvStripDetails(InvStripDetail details);

    void deleteInvStripDetails(InvStripDetail details);

    InvStripDetail updateInvStripDetails(InvStripDetail details);

    List<InvStripDetail> getAllInvStripDetails();

    List<InvStripDetail> getAllInvStripDetails(Integer id);
   
    
    List<InvStripDetailDTO> getAllInvStripDetailsDTO(Integer id ,TobyUser tobyUser);
    
       public List<InvStripDetailDTO> mapToDTOList1(List<QuantityItemsStoreDTO> quantityItemsStoreList,  InvStripDTO invStrip, TobyUser tobyUser);
      
      public InvStripDTO updateSummition(InvStripDetailDTO detailEntity,InvStripDTO invStrip);
}
