/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.RscWorkUnitsGuideDTO;
import com.isag.entity.IsagUser;
import com.isag.entity.RscWorkUnitsGuide;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface RscWorkUnitsGuideService {

    public List<RscWorkUnitsGuideDTO> getBranchRscWorkUnitsGuidesActiveByBranch(Integer branchId);

    public List<RscWorkUnitsGuideDTO> getRscWorkUnitsGuidesForBranchForMainUnits(Integer selectedBranchId, Integer parentId);

    public List<RscWorkUnitsGuideDTO> getCompanyWorkUnitsForMainUnits(Integer selectedBranchId, Integer parentId);

    public RscWorkUnitsGuideDTO findRscWorkUnitsGuide(Integer workUnitsGuide);

    public List<RscWorkUnitsGuideDTO> getAllRscWorkUnitsGuideByBranchIdAndcode(Integer selectedBranchId, String code);

    public List<RscWorkUnitsGuideDTO> getAllRscWorkUnitsGuideByBranchIdAndcode(Integer selectedBranchId, String code, Integer workunitId);

    public void addRscWorkUnitsGuide(RscWorkUnitsGuideDTO unit, IsagUser isagUser);

    public void deleteRscWorkUnitsGuide(RscWorkUnitsGuideDTO unit, IsagUser isagUser);

    public List<RscWorkUnitsGuideDTO> getBranchUnit(Integer id);

    public List<RscWorkUnitsGuideDTO> getBranchUnitAndType(Integer id);

    public List<RscWorkUnitsGuideDTO> getAllUnitsRoots();

    public RscWorkUnitsGuideDTO findUnitbyBranchIdAndCode(Integer branchId, String code);

    public List<RscWorkUnitsGuideDTO> getRscWorkUnitsGuidesChildForParent(Integer selectedBranchId, Integer parentId);
    
    public List<RscWorkUnitsGuide> getBranchRscWorkUnitsGuidesByBranchANDLevel(Integer branchId,Integer level);
    
    public RscWorkUnitsGuide findRscWorkUnitsGuidesbyBranchIdAndCode(Integer branchId, String code);
    
    public void addRscWorkUnitsGuideForTransfer(RscWorkUnitsGuide rscWorkUnitsGuide);
}
