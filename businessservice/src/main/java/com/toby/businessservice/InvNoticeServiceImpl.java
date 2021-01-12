/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.entity.InvNotice;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq002
 */
@Stateless
public class InvNoticeServiceImpl implements InvNoticeService {

    @EJB
    GenericDAO dao;
    @EJB
    GenericService genericService;

    @Override
    public void deleteInvNotice(InvNotice invNotice) {
        dao.deleteEntity(invNotice);
    }

    @Override
    public List<InvNotice> getALLInvNotice() {
        return dao.findAll(InvNotice.class);
    }

    @Override
    public List<InvNotice> getALLInvNoticeByCompanyId(Integer Id) {
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", Id);
        List<InvNotice> invNoticeList = dao.findListByQuery("SELECT i FROM InvNotice i WHERE i.companyId.id = :companyId", params);
        return invNoticeList;
    }

    @Override
    public List<InvNotice> getALLInvNoticeByBranchIdAndType(Integer Id, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", Id);
        StringBuilder query = new StringBuilder();
        query.append("SELECT i FROM InvNotice i WHERE i.branchId.id = :branchId ");

        if (type != null) {
            if (type == 2) {
                query.append(" AND i.type >= :type order by i.id desc");
            } else {
                query.append(" AND i.type <= :type order by i.id desc");
            }
            params.put("type", type);
        }

        List<InvNotice> invNoticeList = dao.findListByQuery(query.toString(), params);
        return invNoticeList;
    }

    @Override
    public InvNotice findInvNoticeByInvNoticeId(Integer invNoticeId) {
        return dao.findEntityById(InvNotice.class, invNoticeId);
    }

    @Override
    public Integer findMaxSerialNoByBranch() {
        Integer maxId = dao.findEntityByQuery("SELECT  MAX(invNotice.id) FROM InvNotice invNotice");
        return maxId;
    }

    @Override
    public void updateInvNotice(InvNotice invNotice) {
        dao.updateEntity(invNotice);
    }

    @Override
    public InvNotice addInvNotice(InvNotice invNotice) {
        if (invNotice.getId() == null) {
            invNotice.setSerial(getMaxGenericSerialByType(InvNotice.class, invNotice.getBranchId().getId(), invNotice.getType()));
            return dao.updateEntity(invNotice);
        } else {
           return  dao.updateEntity(invNotice);
        }
       
    }

    public synchronized <T> Integer getMaxGenericSerialByType(Class<T> entityClass, Integer branchId, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);

        StringBuilder query = new StringBuilder();
        query.append("SELECT max(e.serial) FROM ").append(entityClass.getSimpleName()).append(" e WHERE e.branchId.id =:branchId");

        if (type != null) {
            if (type == 2 || type == 3) {
                query.append(" AND e.type = 2 OR e.type = 3");
            } else {
                query.append(" AND e.type = 0 OR e.type = 1");
            }
        }

        Integer serial = dao.findEntityByQuery(query.toString(), params);

        if (serial == null) {
            return 1;
        } else {
            return serial + 1;
        }
    }
}
