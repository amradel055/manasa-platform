/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;


import com.isag.dto.GmFreezeDto;
import com.isag.dto.GmMovementDTO;
import com.isag.dto.GmSubscriptionsDTO;
import com.isag.entity.IsagUser;
import com.isag.entity.GmFreeze;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Lenovo
 */
@Remote
public interface GmMovementService {
    
    public List<GmMovementDTO> getALLGmMovementDtoByCompanyId(Integer companyId, IsagUser isagUser);
    public List<GmMovementDTO> getALLGmMovementDtoByBranchId(Integer branchId, IsagUser isagUser);
    public GmMovementDTO findGmMovementDtoById(GmMovementDTO gmMovementDTO, IsagUser isagUser) ;
    public GmMovementDTO save(GmMovementDTO gmMovementDTO, GmSubscriptionsDTO gmSubscriptionsDTO  , IsagUser isagUser);
    public Integer calcTakenDaysByCustomerIdAndSubscribtionId( Integer subscribtionId);
    
}
