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

    public List<MaterialsDTO> findMaterials(Integer branchId);

    public MaterialsDTO save(MaterialsDTO materialsDTO, IgmaUser isagUser);

    public void deleteMaterials(Integer materialsDTO);
}
