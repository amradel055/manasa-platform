/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.GeneralJournalDetailsDTO;
import com.isag.entity.IsagUser;
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
    
    public List<GeneralJournalDetailsDTO> addGenDetalils(List<GeneralJournalDetailsDTO> details, List<GeneralJournalDetailsDTO> generalListDeleted,IsagUser isagUser);
}
