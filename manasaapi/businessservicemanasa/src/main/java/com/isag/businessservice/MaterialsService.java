/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.MaterialsDTO;
import com.isag.entity.IgmaUser;
import java.util.List;
import javax.ejb.Remote;


/**
 *
 * @author hq003
 */
@Remote
public interface MaterialsService {

    public List<MaterialsDTO> findMaterials(Integer branchId, Integer teacherId);

    public MaterialsDTO save(MaterialsDTO materialsDTO, IgmaUser isagUser);
    
    public List<MaterialsDTO> findMaterialsByTeacher(Integer teacherId);

    public void deleteMaterials(Integer materialsDTO);

    public List<MaterialsDTO> findMaterialsByStage(Integer stageId);

    public List<MaterialsDTO> findMaterialsABI(Integer teacherId, Integer stageId);

    public MaterialsDTO findMaterialDTO(Integer materialsDTO);

    public void saveMaterial(MaterialsDTO materialsDTO, IgmaUser igmaUser);
    
    public List<MaterialsDTO> findMaterialsByCompanyAndTeachear(Integer companyId , Integer teacherId);
    
    
}
