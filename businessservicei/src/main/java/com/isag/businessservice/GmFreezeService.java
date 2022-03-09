/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;


import com.isag.dto.GmFreezeDto;
import com.isag.entity.IsagUser;
import com.isag.entity.GmFreeze;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Lenovo
 */
@Remote
public interface GmFreezeService {
    
    public List<GmFreezeDto> getALLgm_freezeDtoByCompanyId(Integer companyId , IsagUser isagUser);
    public List<GmFreezeDto> getALLgm_freezeDtoByBranchId(Integer branchId , IsagUser isagUser);
    public GmFreezeDto findgm_freezeDtoById(GmFreezeDto gm_freezeDto ,IsagUser isagUser);
    public GmFreezeDto save(GmFreezeDto freezeDto, IsagUser isagUser);
    public Integer calcTakenDaysByCustomerIdAndSubscribtionId( Integer subscribtionId);
    public boolean calcFreezeStatusBySubscribtionId(Integer subscribtionId);
    public List<GmFreezeDto> getALLgm_freezeDtoByCustomerId(Integer branchId, Integer customerId, IsagUser isagUser);

}
