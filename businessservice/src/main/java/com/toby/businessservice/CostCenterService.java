/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.CostCenterSearchBean;
import com.toby.dto.CostCenterDTO;
import com.toby.entity.CostCenter;
import com.toby.entity.TobyUser;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface CostCenterService {

    public List<CostCenter> getAllCostCenterRoots();

    public List<CostCenter> getCompanyCostCenterRoots(Integer companyId);

    public List<CostCenter> getAllCostCenters(Integer branchId);
    
    public CostCenterDTO findCostCenterDTO(Integer costCenterId);

    public List<CostCenter> getCompanyCostCenters(Integer companyId);

    public void deleteCostCenter(CostCenter selectedCS);

    public CostCenter addCostCenter(CostCenter newCostCenter);

    public void saveCostCenter(CostCenter newCostCenter);

    public CostCenter findCostCenter(Integer costCenterId);

    public CostCenter findCostCenterByCode(Integer parentCode, Integer branchId);

    public List<CostCenter> getBranchCostCentersActive(Integer id);

    public List<CostCenter> getAllSubCostCenterByBranchIdActive(Integer id);

    public List<CostCenter> getActiveSubCostCentersByBranch(Integer branchId);
    
    public List<CostCenterDTO> getActiveSubCostCenterDTOListByBranch(TobyUser tobyUser);

    public List<CostCenter> getBranchWithoutParentCostCenters(Integer branchId);

    public List<CostCenter> getBranchCostCenterRoots(Integer id);

    public List<CostCenter> getAllCostCentersIndex(CostCenterSearchBean costCenterSearchBean);

    public List<CostCenter> getAllCostCenterByBranchIdAndAccNumOrShotCode(Integer selectedBranchId, Integer CostCenterCode);

    public List<CostCenter> getAllCostCenterByBranchIdAndAccNumOrShotCode(Integer selectedBranchId, Integer CostCenterCode, Integer CostCenterId);

    public List<CostCenter> getAllSubCostCenterByBranchIdActiveAndCostCenterId(Integer costCenterId);

    public List<CostCenter> getAllSubCostCenterByBranchIdActiveAndLevel(Integer branchId, Integer level);
    
    public List<CostCenterDTO> getAllCostCenterDTOList(Integer branchId);
    
    public List<CostCenterDTO> getActiveSubCostCentersByBranchDTO(Integer branchId);
    
    public Map<Integer,Integer> getBranchCostCentersByMap(Integer selectedBranchId);
}
