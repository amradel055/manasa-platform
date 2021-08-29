/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.GlBankDTO;
import com.toby.entity.GlBank;
import com.toby.entity.TobyUser;
import com.toby.entity.TobyUserBank;
import com.toby.entity.TobyUserRole;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author WIN7
 */
@Remote
public interface TobyUserBankService {

    public List<GlBank> getAllGlBankForUserByTypeAndBranchId(Integer userId, Integer branchId, Integer type);

    public List<GlBank> getAllGlBankForUserByTypeAndBranchIdPer(Integer userId, Integer branchId, Integer type, Integer gallaryId);

    public List<GlBank> getAllGlBankByTypeAndBranchId(Integer branchId, Integer type);

    public List<GlBank> getAllBankListByUserAndBranch(Integer userId, Integer branchId);

    public List<TobyUserBank> getAllTobyUserBankListByUserAndBranch(Integer userId, Integer branchId);

    public List<TobyUserBank> findAll();

    public void deleteGlBank(TobyUserRole userRole, GlBank glBank);

    public void deleteTobyUserBank(List<TobyUserBank> tobyUserBankList);

    public TobyUserBank updateTobyUserBank(TobyUserBank tobyUserBank);

    public List<GlBankDTO> getAllGlBankDTOByUserAndBranch(TobyUser tobyUser);

    public List<GlBankDTO> getAllGlBankForUserByTypeAndBranchIdPerDTO(Integer userId, Integer branchId, Integer type, Integer gallaryId);
    
    public List<GlBankDTO> getAllGlBankForUserByTypeAndBranchIdAndGallaryListPerDTO(Integer userId, Integer branchId, Integer type, String gallaryId) ;
    
    public List<Integer> getAllGlBankIdForUserByTypeAndBranchId(Integer userId, Integer branchId, Integer type);

}
