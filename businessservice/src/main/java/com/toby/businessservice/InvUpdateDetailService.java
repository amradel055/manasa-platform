package com.toby.businessservice;

import com.toby.entity.InvUpdateDetail;

import javax.ejb.Remote;
import java.util.List;

/**
 * @author khaled
 */
@Remote
public interface InvUpdateDetailService {
    void addInvUpdateDetails(InvUpdateDetail details);

    void deleteInvUpdateDetails(InvUpdateDetail details);

    InvUpdateDetail updateInvUpdateDetails(InvUpdateDetail details);

    List<InvUpdateDetail> getAllInvUpdateDetails();

    List<InvUpdateDetail> getAllInvUpdateDetails(Integer id);
}
