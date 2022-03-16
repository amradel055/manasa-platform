package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.entity.InvUpdateDetail;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author khaled
 */
@Stateless
public class InvUpdateDetailServiceImp implements InvUpdateDetailService{
    @EJB
    private GenericDAO dao;


    @Override
    public void addInvUpdateDetails(InvUpdateDetail details) {
        dao.saveEntity(details);
    }

    @Override
    public void deleteInvUpdateDetails(InvUpdateDetail details) {
        dao.deleteEntity(details);
    }

    @Override
    public InvUpdateDetail updateInvUpdateDetails(InvUpdateDetail details) {
        return dao.updateEntity(details);
    }

    @Override
    public List<InvUpdateDetail> getAllInvUpdateDetails() {
        return dao.findAll(InvUpdateDetail.class);
    }

    @Override
    public List<InvUpdateDetail> getAllInvUpdateDetails(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("invUpdate", id);
        return dao.findListByQuery("SELECT d FROM InvUpdateDetail d WHERE d.invUpdateId.id =:invUpdate", params);
    }
}
