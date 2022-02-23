/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.IgmaMessageDTO;
import com.isag.entity.IgmaMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq004
 */
@Stateless
public class IgmaMessageServiceImpl implements IgmaMessageService {

    @EJB
    private GenericDAO dao;

    @Override
    public IgmaMessageDTO getIgmaMessageDTO(Integer branchId) {
        Map<String, Object> params = new HashMap<>();

        params.put("branchId", branchId);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT i FROM IgmaMessage i WHERE i.branchId.id = :branchId ");
        List<IgmaMessageDTO> details = dao.findListByQuery(stringBuilder.toString(), params);
        if (details != null && !details.isEmpty()) {
            details.get(0);
        }
        return null;
    }

    @Override
    public void updateIgmaMessage(Integer IgmaMessageId, Integer remainNumber) {
        IgmaMessage igmaMessage = dao.findEntityById(IgmaMessage.class, IgmaMessageId);
        if (igmaMessage != null) {
            igmaMessage.setNumber(remainNumber);
            dao.updateEntity(igmaMessage);
        }
    }
}
