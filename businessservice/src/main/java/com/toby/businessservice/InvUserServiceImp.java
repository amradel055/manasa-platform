package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.entity.InvUser;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author khaled
 */
@Stateless
public class InvUserServiceImp implements InvUserService {
    @EJB
    private GenericDAO dao;

    @Override
    public InvUser addInvTransfer(InvUser invUser) {
        if (invUser.getId() != null) {
            dao.updateEntity(invUser);
        } else {
            dao.saveEntity(invUser);
        }
        return invUser;
    }

    @Override
    public void deleteInvUser(InvUser invUser) {
        dao.deleteEntity(invUser);
    }

    @Override
    public List<InvUser> getALLInvUser() {
        return dao.findAll(InvUser.class);
    }

    @Override
    public List<InvUser> getALLInvUserByCompanyId(Integer Id) {
        return dao.findEntityListByCompanyId(InvUser.class, Id);
    }

    @Override
    public List<InvUser> getALLInvUserByBranchId(Integer Id) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", Id);
        List<InvUser> invUserList = dao.findListByQuery("SELECT e FROM InvUser e WHERE e.branchId.id = :branchId", params);
        return invUserList;
    }

    @Override
    public InvUser findInvUserByInvUserId(Integer invUserId) {
        return dao.findEntityById(InvUser.class, invUserId);
    }
}
