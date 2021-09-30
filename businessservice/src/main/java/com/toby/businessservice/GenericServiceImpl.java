/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hhhh
 */
@Stateless
public class GenericServiceImpl implements GenericService {

    @EJB
    private GenericDAO dao;

    @Override
    public synchronized <T> Integer getMaxSerialForInvoice(Class<T> entityClass, Integer branchId, Integer galaryId, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);

        StringBuilder query = new StringBuilder();
        query.append("SELECT max(e.serial) FROM ").append(entityClass.getSimpleName()).append(" e WHERE e.branchId.id =:branchId");

        if (type != null) {
            params.put("type", type);
            query.append(" AND e.type =:type");
        }

        if (galaryId != null) {
            params.put("galaryId", galaryId);
            query.append(" AND e.galaryId.id =:galaryId");
        }

        Integer serial = dao.findEntityByQuery(query.toString(), params);

        if (serial == null) {
            return 1;
        } else {
            return serial + 1;
        }
    }
   
    @Override
    public synchronized <T> Integer getMaxSerialForOpenningBalance(Class<T> entityClass, Integer branchId, Integer invInventoryId, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);

        StringBuilder query = new StringBuilder();
        query.append("SELECT max(e.serial) FROM ").append(entityClass.getSimpleName()).append(" e WHERE e.branchId.id =:branchId");

        if (type != null) {
            params.put("type", type);
            query.append(" AND e.type =:type");
        }

        if (invInventoryId != null) {
            params.put("invInventoryId", invInventoryId);
            query.append(" AND e.invInventoryId.id =:invInventoryId");
        }

        Integer serial = dao.findEntityByQuery(query.toString(), params);

        if (serial == null) {
            return 1;
        } else {
            return serial + 1;
        }
    }

    @Override
    public synchronized <T> Integer getMaxGenericSerialByType(Class<T> entityClass, Integer branchId, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);

        StringBuilder query = new StringBuilder();
        query.append("SELECT max(e.serial) FROM ").append(entityClass.getSimpleName()).append(" e WHERE e.branchId.id =:branchId");

        if (type != null) {
            params.put("type", type);
            query.append(" AND e.type =:type");
        }

        Integer serial = dao.findEntityByQuery(query.toString(), params);

        if (serial == null) {
            return 1;
        } else {
            return serial + 1;
        }
    }    
    
    @Override
    public synchronized <T> Integer getMaxGenericSerialByInvTransferType(Class<T> entityClass, Integer branchId, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);

        StringBuilder query = new StringBuilder();
        query.append("SELECT max(e.serialNo) FROM ").append(entityClass.getSimpleName()).append(" e WHERE e.branchId.id =:branchId");

        if (type != null) {
            params.put("type", type);
            query.append(" AND e.transferType =:type");
        }

        Integer serial = dao.findEntityByQuery(query.toString(), params);

        if (serial == null) {
            return 1;
        } else {
            return serial + 1;
        }
    }

    @Override
    public synchronized <T> Integer getMaxGenericSerialForDetail(Class<T> entityClass, String headName, Integer headId) {
        Map<String, Object> params = new HashMap<>();
        params.put("headId", headId);

        StringBuilder query = new StringBuilder();
        query.append("SELECT max(e.serial) FROM ").append(entityClass.getSimpleName()).append(" e WHERE e.").append(headName).append(".id =:headId");

        Integer serial = dao.findEntityByQuery(query.toString(), params);

        if (serial == null) {
            return 1;
        } else {
            return serial + 1;
        }
    }
}
