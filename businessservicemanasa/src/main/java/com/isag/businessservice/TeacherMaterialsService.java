/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.TeacherMaterialsDTO;
import com.isag.entity.IgmaUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface TeacherMaterialsService {

    public List<TeacherMaterialsDTO> findTeacherMaterials(Integer branchId);

    public TeacherMaterialsDTO save(TeacherMaterialsDTO teacherMaterialsDTO, IgmaUser isagUser);

    public void deleteTeacherMaterials(Integer teacherMaterialsDTO);

}
