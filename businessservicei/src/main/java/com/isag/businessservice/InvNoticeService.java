/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.InvNoticeDTO;
import com.isag.entity.InvNotice;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface InvNoticeService {

    public void deleteInvNotice(InvNotice invNotice);

    public void updateInvNotice(InvNotice invNotice);

    public InvNotice addInvNotice(InvNotice invNotice);

    public List<InvNotice> getALLInvNotice();

    public List<InvNotice> getALLInvNoticeByCompanyId(Integer Id);

    public List<InvNotice> getALLInvNoticeByBranchIdAndType(Integer Id , Integer type);
    
    public List<InvNoticeDTO> getALLInvNoticeByBranchIdAndTypeDTO(Integer Id, Integer type);

    public InvNotice findInvNoticeByInvNoticeId(Integer invNoticeId);

    Integer findMaxSerialNoByBranch();
    
    public InvNotice updateInvNoticeForShifting(InvNotice invNotice);
    
    public List<InvNoticeDTO> getInvNoticeListDTO(Integer branchId, Integer type,Date dateFrom,
            Date dateTo,Integer noticeFrom,Integer noticeTo);
    
    public Map<Integer, Vector<Object>> getAllinstallmentIdAndPayedValue(Integer branchId, Integer installmentParentId);

}
