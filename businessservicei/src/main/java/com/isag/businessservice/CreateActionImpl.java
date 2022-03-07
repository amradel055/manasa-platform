/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.ActionsDTO;
import com.isag.dto.searchDTO;
import com.isag.entity.CreateAction;
import com.isag.entity.IsagUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Igma
 */
@Stateless

public class CreateActionImpl implements CreateActionService {

    @EJB
    private GenericDAO dao;

    private ActionsDTO mapToDto(CreateAction actions, IsagUser isagUser) {
        ActionsDTO actionsdto = new ActionsDTO();
        actionsdto.setId(actions.getId());
        actionsdto.setType(actions.getType());
        actionsdto.setUserId(actions.getUserId().getName());
        actionsdto.setDate(actions.getDateAction());
        actionsdto.setSerialId(actions.getSerialId());

        actionsdto.setScreenName(actions.getScreenName());
        return actionsdto;
    }

    private CreateAction mapToEntity(ActionsDTO actionsdto, IsagUser isagUser) {
        CreateAction actions = new CreateAction();
        actions.setId(actionsdto.getId());
        actions.setType(actionsdto.getType());
        actions.setUserId(isagUser);
        actions.setScreenName(actionsdto.getScreenName());
        actions.setDateAction(new Date());
        actions.setSerialId(actionsdto.getSerialId());

        return actions;
    }

    @Override
    public void SaveAction(ActionsDTO actionsDTO, IsagUser isagUser) {
        CreateAction actions;
        actions = mapToEntity(actionsDTO, isagUser);
        actions = dao.updateEntity(actions);
    }

    @Override
    public List<ActionsDTO> getAllValidSubsByDate(searchDTO search, IsagUser isagUser, ActionsDTO actionsDTO) {

        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT e FROM CreateAction e   ");
        if (search.getDateFrom() != null) {
            params.put("dateFrom", search.getDateFrom());
            query.append("WHERE e.dateAction >= :dateFrom");
        }
        if (search.getDateTo() != null) {
            params.put("dateTo", search.getDateTo());
            query.append(" AND e.dateAction <= :dateTo");
        }
        if (actionsDTO.getSerialId() != null) {
            params.put("serialId", actionsDTO.getSerialId());
            query.append(" AND e.serialId <= :serialId");
        }

        List<CreateAction> createActions = dao.findListByQuery(query.toString(), params);
        List<ActionsDTO> dtos = mapToDtoList(createActions, isagUser);
        return dtos;
    }

    private List<ActionsDTO> mapToDtoList(List<CreateAction> actionsDtoList, IsagUser isagUser) {
        List<ActionsDTO> dtos = new ArrayList<>();
        for (int i = 0; i < actionsDtoList.size(); i++) {
            dtos.add(mapToDto(actionsDtoList.get(i), isagUser));
        }
        return dtos;
    }

}
