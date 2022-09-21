/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.GeneralJournalDTO;
import com.toby.dto.InvNoticeDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.entity.GeneralJournal;
import com.toby.entity.InvNotice;
import com.toby.entity.TobyUser;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface InvNoticeService {

    public void deleteInvNotice(InvNotice invNotice);
    
    public void deleteInvNoticeNew(InvNotice invNotice);

    public void updateInvNotice(InvNotice invNotice);
    
     public InvNotice addInvNotice(List<InvNotice> invNoticeList, TobyUser tobyUser, Integer yearSelection);

    public InvNotice addInvNotice(InvNotice invNotice, TobyUser tobyUser, Integer yearSelection);

    public List<InvNotice> getALLInvNotice();

    public List<InvNotice> getALLInvNoticeByCompanyId(Integer Id);

    public List<InvNotice> getALLInvNoticeByBranchIdAndType(Integer Id, Integer type);

    public List<InvNoticeDTO> getALLInvNoticeByBranchIdAndTypeDTO(Integer Id, Integer type);

    public InvNotice findInvNoticeByInvNoticeId(Integer invNoticeId);

    Integer findMaxSerialNoByBranch();

    public InvNotice updateInvNoticeForShifting(InvNotice invNotice);

    public List<InvNoticeDTO> getInvNoticeListDTO(Integer branchId, Integer type, Date dateFrom,
            Date dateTo, Integer noticeFrom, Integer noticeTo);

    public GeneralJournal tarhil(InvNotice dto, TobyUser tobyUser, Integer yearSelection) throws CloneNotSupportedException;

    public void getALLInvNoticeForToby(TobyUser tobyUser) throws CloneNotSupportedException;
    
    public List<GeneralJournalDTO> getALLInvNoticeForToby2(TobyUser tobyUser) throws CloneNotSupportedException;

    public List<InvNoticeDTO> getALLInvNoticeByBranchIdAndTypeAndSerialAndOrgan_idDTO(Integer Id, Integer type, InvOrganizationSiteDTO organizationSiteId, Integer serial);

}
