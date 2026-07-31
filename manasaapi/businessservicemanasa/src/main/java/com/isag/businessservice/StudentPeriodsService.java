/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.APIStudentDataDTO;
import com.isag.dto.StudentPeriodsDTO;
import com.isag.dto.TeachersDTO;
import com.isag.entity.IgmaUser;
import com.isag.entity.StudentPeriods;
import com.isag.views.TeachersByStudentId;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface StudentPeriodsService {

    public List<StudentPeriodsDTO> findStudentPeriods(StudentPeriodsDTO studentPeriodsDTO);

    public List<TeachersDTO> findteachersByStudentIdLogin(Integer studentId);

    public List<Integer> findPeriodsByStudentId(Integer studentId, Integer materialId);

    public List<TeachersByStudentId> findteachersByStudentId(Integer studentId);

    public void deleteStudentPeriodsBy(Integer id, Integer teacherId, Integer periodId, Integer studentId);

    public StudentPeriodsDTO save(StudentPeriodsDTO studentPeriodsDTO, IgmaUser isagUser);

    public void deleteStudentPeriods(StudentPeriodsDTO studentPeriodsDTO);

    public void deleteStudentPeriods1(Integer studentPeriodsDTO) ;

    public void saveTransaction(StudentPeriods studentPeriods);

    public List<StudentPeriodsDTO> checkByStudentAndMaterialActive(Integer studentId, Integer materialId, Integer periodId);
    
    public List<APIStudentDataDTO> studentActivationOrder(Integer studentId);
}
