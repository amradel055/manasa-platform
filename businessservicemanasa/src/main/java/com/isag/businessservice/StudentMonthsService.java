/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.StudentMonthsDTO;
import com.isag.entity.IgmaUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface StudentMonthsService {

    public List<Integer> findStudentMonthsByMonths(Integer studentId);

    public List<StudentMonthsDTO> findStudentMonths(Integer branchId, Integer id);

    public StudentMonthsDTO save(StudentMonthsDTO studentMonthsDTO, IgmaUser isagUser);

    public void deleteStudentMontsByStudent(Integer studentDTO);

    public void deleteStudentMonths(StudentMonthsDTO studentMonthsDTO);

    public void deleteStudentMonths(Integer studentMonthsDTO);
}
