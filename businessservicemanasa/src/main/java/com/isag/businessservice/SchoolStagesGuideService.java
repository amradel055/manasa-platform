/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.SchoolStagesGuideDTO;
import com.isag.entity.SchoolStagesGuide;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface SchoolStagesGuideService {

    public List<SchoolStagesGuideDTO> findschoolStagesGuide(Integer branchId);

    public List<SchoolStagesGuideDTO> findschoolStagesGuideByTeacherId(Integer teacherId);
    
    public List<SchoolStagesGuideDTO> findStage(Integer materialId) ;
    
    public void deleteStage(Integer stageId);
    
    public List<SchoolStagesGuideDTO> findschoolStagesGuideByCompanyId(Integer companyId);
    
    public SchoolStagesGuide findschoolStagesGuideById(Integer stageId);
    
    public SchoolStagesGuide addSchoolStagesGuide(SchoolStagesGuide schoolStagesGuide) ;
    
    public List<SchoolStagesGuideDTO> findAll();

}
