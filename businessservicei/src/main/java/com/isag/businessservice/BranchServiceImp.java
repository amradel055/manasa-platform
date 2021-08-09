/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.APIBranchDTO;
import com.isag.entity.Branch;
import com.isag.entity.GlAnalyticsAccounts;
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
 * @author hq004
 */
@Stateless
public class BranchServiceImp implements BranchService {

    @EJB
    private GenericDAO dao;
    @EJB
    private GlAnalyticsAccountsServcie analyticsAccountsServcie;

    @Override
    public List<Branch> getAllBranch() {
        //return dao.findAll(Branch.class);
        return dao.findListByQuery("SELECT b FROM Branch b order by b.serial ");
    }

    @Override
    public List<APIBranchDTO> getAllBranchDTO() {
        //return dao.findAll(Branch.class);
        List<Branch> BranchList = dao.findListByQuery("SELECT b FROM Branch b order by b.serial ");
        List<APIBranchDTO> branchDTOList = new ArrayList<>();
        for (Branch branche : BranchList) {
            APIBranchDTO branchDto = new APIBranchDTO();
            branchDto.setId(branche.getId());
            branchDto.setNameAr(branche.getNameAr());
            branchDto.setNameEn(branche.getNameEn());
            branchDto.setCompanyId(branche.getCompanyId().getId());
            branchDTOList.add(branchDto);
        }
        return branchDTOList;
    }

    @Override
    public void deleteBranch(Branch branch) {
        analyticsAccountsServcie.delete(branch.getId());
        dao.deleteEntity(branch);
    }

    @Override
    public Branch updateBranch(Branch branch) {
        if (!validateBranch(branch.getCompanyId().getId(), branch, branch.getSerial())) {
            return dao.updateEntity(branch);
        }
        return null;
    }

    @Override
    public Branch addBranch(Branch branch, IsagUser userData) {
        Branch branch1 = null;
        if (!validateBranch(branch.getCompanyId().getId(), branch, branch.getSerial())) {
            branch1 = dao.updateEntity(branch);
            List<GlAnalyticsAccounts> glAnaList = new ArrayList<>();
            glAnaList = analyticsAccountsServcie.getALLGlAnalyticsAccountsByCompany();
            for (GlAnalyticsAccounts gaa : glAnaList) {
                GlAnalyticsAccounts analyticsAccounts = new GlAnalyticsAccounts();
                analyticsAccounts.setCode(gaa.getCode());
                analyticsAccounts.setCompanyId(branch.getCompanyId());
                analyticsAccounts.setBranchId(branch1);
                analyticsAccounts.setCreatedBy(userData);
                analyticsAccounts.setCreationDate(new Date());
                analyticsAccounts.setType(gaa.getType());
                analyticsAccountsServcie.addGlAnalyticsAccounts(analyticsAccounts);
            }
        }
        return branch1;
    }

    private synchronized Boolean validateBranch(Integer companyId, Branch branchId, Integer serial) {
        List<Branch> branches = getBranchCompanyBySerial(companyId, branchId, serial);
        return branches.size() > 0;
    }

    @Override
    public Branch findBranch(Integer id) {
        return dao.findEntityById(Branch.class, id);
    }

    @Override
    public List<Branch> getBranchCompanyBySerial(Integer companyId, Branch branchId, Integer serial) {
        Map<String, Object> params = new HashMap();
        params.put("companyId", companyId);
        params.put("serial", serial);

        if (branchId.getId() == null) {
            return dao.findListByQuery("SELECT b FROM Branch b WHERE b.companyId.id =:companyId AND b.serial = :serial order by b.serial ", params);
        } else {
            params.put("branchId", branchId.getId());
            return dao.findListByQuery("SELECT b FROM Branch b WHERE b.companyId.id =:companyId AND b.id != :branchId AND b.serial = :serial order by b.serial ", params);
        }
    }

    @Override
    public List<Branch> getBranchCompanyBySerial(Integer companyId, Integer serial) {
        Map<String, Object> params = new HashMap();
        params.put("companyId", companyId);
        params.put("serial", serial);
        return dao.findListByQuery("SELECT b FROM Branch b WHERE b.companyId.id =:companyId AND b.serial = :serial order by b.serial ", params);
    }

    @Override
    public List<Branch> getAllBranchByCompanyId(Integer companyId) {
        Map<String, Object> params = new HashMap();
        params.put("companyId", companyId);
        return dao.findListByQuery("SELECT b FROM Branch b WHERE b.companyId.id =:companyId order by b.serial ", params);
        //return dao.findEntityListByCompanyId(Branch.class, companyId);
    }

    @Override
    public Integer getMaxId() {
        Integer maxId = dao.findEntityByQuery("SELECT  MAX(b.id) FROM Branch b");
        return maxId;
    }

    @Override
    public List<Branch> getBranchesForUser(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("id", id);
        return dao.findListByQuery("SELECT userRole.branchId FROM IsagUserRole userRole WHERE userRole.userId.id = :id", params);
    }

    @Override
    public List<Branch> getImageByBranchId(String imageName, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("imageName", imageName);
        params.put("branchId", branchId);
        List<Branch> imageBranch = dao.findListByQuery("SELECT b FROM Branch b WHERE b.id != :branchId AND b.image = :imageName", params);

        return imageBranch;
    }
}
