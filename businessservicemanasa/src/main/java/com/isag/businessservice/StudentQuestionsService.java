/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.StudentQuestionsDTO;
import com.isag.entity.IgmaUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface StudentQuestionsService {

    public void save(StudentQuestionsDTO studentQuestionsDTO, IgmaUser isagUser);

    public void delete(Integer id, IgmaUser igmaUser);

    public List<StudentQuestionsDTO> findStudemtPeriodOrderDTO(Integer branchId);

    public List<StudentQuestionsDTO> findStudentQuestionsAndAnswer(Integer teacherId, Integer materialId) ;

}
