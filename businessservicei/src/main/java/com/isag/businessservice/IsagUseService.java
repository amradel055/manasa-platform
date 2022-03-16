/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.GlBankDTO;
import com.isag.entity.GlBank;
import com.isag.entity.IsagUserBank;
import com.isag.entity.IsagUserRole;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author WIN7
 */
@Remote
public interface IsagUseService {

    public List<GlBank> getAllGlBankForUserByTypeAndBranchId(Integer userId, Integer branchId, Integer type);
    
    public List<GlBank> getAllGlBankForUserByTypeAndBranchIdPer(Integer userId, Integer branchId, Integer type);

    public List<GlBank> getAllGlBankByTypeAndBranchId(Integer branchId, Integer type);

    public List<GlBank> getAllBankListByUserAndBranch(Integer userId, Integer branchId);

    public List<IsagUserBank> getAllIsagUserBankListByUserAndBranch(Integer userId, Integer branchId);
    
    public List<IsagUserBank> findAll();

    public void deleteGlBank(IsagUserRole userRole, GlBank glBank);

    public void deleteIsagUserBank(List<IsagUserBank> isagUserBankList);

    public IsagUserBank updateIsagUserBank(IsagUserBank isagUserBank);
    
    public List<GlBankDTO> getAllGlBankForUserByTypeAndBranchIdPerDTO(Integer userId, Integer branchId, Integer type);
}
