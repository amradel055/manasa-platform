package com.toby.businessservice;

import com.toby.entity.InvUser;

import javax.ejb.Remote;
import java.util.List;

/**
 * @author khaled
 */
@Remote
public interface InvUserService {
    InvUser addInvTransfer(InvUser invUser);

    void deleteInvUser(InvUser invUser);

    List<InvUser> getALLInvUser();

    List<InvUser> getALLInvUserByCompanyId(Integer Id);

    List<InvUser> getALLInvUserByBranchId(Integer Id);

    InvUser findInvUserByInvUserId(Integer invUserId);
}
