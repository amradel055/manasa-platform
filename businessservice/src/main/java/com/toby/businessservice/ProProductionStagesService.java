/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.ProProductionStagesDTO;
import com.toby.entity.ProProductionStages;

import com.toby.entity.TobyUser;
import com.toby.entity.TobyUserProproduction;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author H
 */
@Remote
public interface ProProductionStagesService {

    public ProProductionStagesDTO saveProProductionStage(ProProductionStagesDTO proProductionStagesDTO, TobyUser tobyUser);

    public void deleteProProductionStages(ProProductionStagesDTO proProductionStagesDTO, TobyUser tobyUser);

    public void deleteProProductionStage(Integer proProductionStageId);

    public List<ProProductionStagesDTO> getAllProProductionStagesByBranchId(Integer branchId, TobyUser tobyUser);

    public ProProductionStagesDTO findProProductionStages(Integer id);

    public List<ProProductionStagesDTO> getAllProproductionByBranchId(Integer branchId, TobyUser tobyUser);
    
    public List<ProProductionStages> getAllProproductionStagesByBranchId(Integer branchId, TobyUser tobyUser);

    public List<TobyUserProproduction> getAllTobyUserProproductionListByUserAndBranch(Integer userId, Integer branchId);
    
    public List<ProProductionStages> getAllProproductionListByUserAndBranch( TobyUser tobyUser, Integer branchId);
    
    public List<ProProductionStagesDTO> getAllProproductionStagesListByUserAndBranch( TobyUser tobyUser, Integer branchId);
    
    public List<ProProductionStagesDTO> getAllProproductionStages( TobyUser tobyUser, Integer branchId);
    
    public ProProductionStages checkForLastStage(TobyUser tobyUser );
    
    public ProProductionStages getCheckStage(TobyUser tobyUser);
    
    public List<ProProductionStages> getAllProProductionStagesByBranchId3(Integer branchId);

}
