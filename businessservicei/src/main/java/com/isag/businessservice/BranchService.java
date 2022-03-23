/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.APIBranchDTO;
import com.isag.entity.Branch;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface BranchService {

    public List<Branch> getAllBranch();
    
    public List<APIBranchDTO> getAllBranchDTO();

    public void deleteBranch(Branch branch);

    public Branch updateBranch(Branch branch);

    public Branch addBranch(Branch branch , IsagUser userData);

    public Branch findBranch(Integer id);

    public List<Branch> getAllBranchByCompanyId(Integer companyId);

    public Integer getMaxId();

    public List<Branch> getBranchesForUser(Integer id);

    public List<Branch> getImageByBranchId(String imageName, Integer branchId);

    public List<Branch> getBranchCompanyBySerial(Integer companyId, Branch branchId, Integer serial);
    
    public List<Branch> getBranchCompanyBySerial(Integer companyId, Integer serial);
}
