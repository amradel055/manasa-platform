/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.ActionsDTO;
import com.isag.dto.searchDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Igma
 */
@Remote

public interface CreateActionService {

    public void SaveAction(ActionsDTO actionsDTO, IsagUser isagUser);
  public List<ActionsDTO> getAllValidSubsByDate(searchDTO search, IsagUser isagUser, ActionsDTO actionsDTO);

}
