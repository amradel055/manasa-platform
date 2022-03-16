/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.TobyUser;
import com.toby.dto.GeneralJournalDetailsDTO;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author AhmedEssam
 */
@Remote
public interface GeneraljournalDetailsServiceDTO {

    public List<GeneralJournalDetailsDTO> getAllJournalDetailsForJorunal(Integer id);
    
    public Integer getMaxSerialByGeneral(Integer id);
    
    public List<GeneralJournalDetailsDTO> addGenDetalils(List<GeneralJournalDetailsDTO> details, List<GeneralJournalDetailsDTO> generalListDeleted,TobyUser tobyUser);
}
