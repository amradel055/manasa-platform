/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.entity.InstProjectGuide;
import com.isag.entity.InvOrganizationSite;
import com.isag.views.ProjectStatusView;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface ProjectStatusService {



    public List<ProjectStatusView> getProjectStatusByBranchId(Integer branchId,Integer parent,Integer level,Integer status);
    
    public List<InstProjectGuide> getProjectGuideByLevel(Integer branchId);
     
    public List<InstProjectGuide> getProjectGuideByLevelAndParent(Integer branchId,Integer parent);

    
}
