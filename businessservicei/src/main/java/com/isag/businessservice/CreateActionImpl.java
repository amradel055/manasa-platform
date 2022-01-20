/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.ActionsDTO;
import com.isag.entity.CreateAction;
import com.isag.entity.IsagUser;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Igma
 */
@Stateless

public class CreateActionImpl implements CreateActionService{
    
    @EJB
    private GenericDAO dao;
     private ActionsDTO mapToDto(CreateAction actions, IsagUser isagUser) {
        ActionsDTO actionsdto = new ActionsDTO();
        actionsdto.setId(actions.getId());
        actionsdto.setType(actions.getType());
        actionsdto.setUserId(isagUser.getId());

        actionsdto.setScreenName(actions.getScreenName());
        return actionsdto;
    }

    private CreateAction mapToEntity(ActionsDTO actionsdto, IsagUser isagUser) {
        CreateAction actions = new CreateAction();
        actions.setId(actionsdto.getId());
        actions.setType(actionsdto.getType());
        actions.setUserId(isagUser);
        actions.setScreenName(actionsdto.getScreenName());
        return actions;
    }
 
@Override
    public void SaveAction(ActionsDTO actionsDTO, IsagUser isagUser) {
        CreateAction actions;
        actions = mapToEntity(actionsDTO, isagUser);
        actions = dao.updateEntity(actions);
    }
    
}
