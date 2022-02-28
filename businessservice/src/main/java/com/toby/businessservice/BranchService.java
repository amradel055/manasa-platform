/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.Branch;
import com.toby.entity.TobyUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface BranchService {

    public List<Branch> getAllBranch();

    public void deleteBranch(Branch branch);

    public Branch updateBranch(Branch branch);

    public Branch addBranch(Branch branch , TobyUser userData);

    public Branch findBranch(Integer id);

    public List<Branch> getAllBranchByCompanyId(Integer companyId);

    public Integer getMaxId();

    public List<Branch> getBranchesForUser(Integer id);

    public List<Branch> getImageByBranchId(String imageName, Integer branchId);

    public List<Branch> getBranchCompanyBySerial(Integer companyId, Branch branchId, Integer serial);
}
