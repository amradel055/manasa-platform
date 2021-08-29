/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.StudentPeriodsDTO;
import com.isag.entity.IgmaUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface StudentPeriodsService {

    public List<StudentPeriodsDTO> findStudentPeriods(StudentPeriodsDTO studentPeriodsDTO);

    public List<Integer> findPeriodsByStudentId(Integer studentId);

    public List<Integer> findteachersByStudentId(Integer studentId);

    public void deleteStudentPeriodsBy(Integer teacherId, Integer periodId, Integer studentId);

    public StudentPeriodsDTO save(StudentPeriodsDTO studentPeriodsDTO, IgmaUser isagUser);

    public void deleteStudentPeriods(StudentPeriodsDTO studentPeriodsDTO);

    public void deleteStudentPeriods(Integer studentPeriodsDTO);
}
