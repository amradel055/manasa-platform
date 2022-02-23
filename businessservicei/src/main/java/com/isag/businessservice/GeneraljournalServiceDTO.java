/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.GeneralJournalDTO;
import com.isag.dto.GlYearDTO;
import com.isag.entity.GeneralJournal;
import com.isag.entity.IsagUser;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author AhmedEssam
 */
@Remote
public interface GeneraljournalServiceDTO {

    public Long getTotalGeneralJournal(GlYearDTO year, Integer branchId, Map<String, Object> filters);
    
    public Long getTotalGeneralJournalForRevision(GlYearDTO year, Integer branchId,Date dateFrom , Date dateTo , Integer revisionAccount, Map<String, Object> filters);

    public List<GeneralJournalDTO> getGeneralJournalDTOByBranchId(GlYearDTO year, Integer branchId, int first, int pageSize, Map<String, Object> filters);

    public List<GeneralJournalDTO> getGeneralJournalDTOByBranchIdForRevision(GlYearDTO year, Integer branchId,Date dateFrom , Date dateTo , Integer revisionAccount, int first,
            Integer pageSize, Map<String, Object> filters);
            
    public GeneralJournalDTO addGeneralJournalDTO(GeneralJournalDTO generalJournalDTO, IsagUser isagUser);

    public List<GeneralJournalDTO> getALLGeneralJournalDTOByBranchIdAndYear(Integer selectedBranch, GlYearDTO year);
 
    public void addGeneralJournalsDTOForReviewing(List<GeneralJournalDTO> generalJournalDTOList,IsagUser isagUser);

    public void updateGeneralJournalsDTOForReviewing(Date dateFrom, Date dateTo, Integer branchId, Integer postFlag, String postFlagReview);

    public void deleteGeneralJournalDTO(GeneralJournalDTO generalJournalDTO, IsagUser isagUser);

    public GeneralJournal convertFromDTO(GeneralJournalDTO dTO, IsagUser isagUser);
    
    public void addGeneralJournalDTOForCorrectence(List<GeneralJournalDTO> generalJournals,IsagUser isagUser); 
    
    public Integer findMaxGenralDocuments(Map<String, Object> params1);
    
    
}
