/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.InvNotice;
import java.util.List;
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

    public InvNotice findInvNoticeByInvNoticeId(Integer invNoticeId);

    Integer findMaxSerialNoByBranch();
}
