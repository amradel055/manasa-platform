/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.ProjectGuideSearchBean;
import com.toby.dto.InstProjectGuideDTO;
import com.toby.entity.InstProjectGuide;
import com.toby.entity.TobyUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author ahmed
 */
@Remote
public interface InstProjectGuideService {

    public List<InstProjectGuide> getAllInstProjectGuideRoots();

    public List<InstProjectGuide> getCompanyInstProjectGuideRoots(Integer companyId);

    public List<InstProjectGuide> getAllInstProjectGuides(Integer branchId);

    public List<InstProjectGuide> getCompanyInstProjectGuides(Integer companyId);

    public void deleteInstProjectGuide(InstProjectGuide selectedCS);

    public InstProjectGuide addInstProjectGuide(InstProjectGuide newInstProjectGuide, TobyUser tobyUser);

    public void saveInstProjectGuide(InstProjectGuide newInstProjectGuide);

    public InstProjectGuide findInstProjectGuide(Integer instProjectId);

    public InstProjectGuide findInstProjectGuideByCode(String parentCode, Integer branchId);

    public List<InstProjectGuide> getBranchInstProjectGuidesActive(Integer id);

    public List<InstProjectGuide> getBranchInstProjectGuidesActiveNotAvailable(Integer id);

    public List<InstProjectGuide> getAllSubInstProjectGuideByBranchIdActive(Integer id);

    public List<InstProjectGuide> getActiveSubInstProjectGuidesByBranch(Integer branchId);

    public List<InstProjectGuide> getBranchWithoutParentInstProjectGuides(Integer branchId);

    public List<InstProjectGuide> getBranchInstProjectGuideRoots(Integer id);

    public List<InstProjectGuide> getAllprojectGuideIndex(ProjectGuideSearchBean projectGuideSearchBean);

    public List<InstProjectGuide> getAllInstProjectGuideByBranchIdAndAccNumOrShotCode(Integer selectedBranchId, String instProjectGuideCode);

    public List<InstProjectGuide> getAllInstProjectGuideByBranchIdAndAccNumOrShotCode(Integer selectedBranchId, String instProjectGuideCode, Integer instProjectGuideId);

    public List<InstProjectGuide> getAllSubInstProjectGuideByBranchIdActiveAndInstProjectGuideId(Integer instProjectId);

    public List<InstProjectGuide> getAllSubInstProjectGuideByBranchIdActiveAndLevel(Integer branchId, Integer level);

    public List<InstProjectGuideDTO> getBranchInstProjectGuidesActiveNotAvailableDTO(Integer id);

    public List<InstProjectGuideDTO> findInstProjectGuidesByBranch(Integer id, Integer level);

    public List<InstProjectGuideDTO> getAllInstProjectGuidesDTO(Integer branchId);

    public List<InstProjectGuideDTO> getAllSubInstProjectGuideByBranchIdActiveAndLevelDTO(Integer branchId, Integer level, Integer level2);

    public List<InstProjectGuide> getAllprojectGuideSellAndNotSell(ProjectGuideSearchBean projectGuideSearchBean);

    public void updateReserved(Boolean reserved, Integer id, Integer branchId);
    
    
}
