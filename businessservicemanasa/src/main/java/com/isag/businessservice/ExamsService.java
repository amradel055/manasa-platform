/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.ExamsDTO;
import com.isag.entity.Exams;
import com.isag.entity.IgmaUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface ExamsService {

    public List<ExamsDTO> findAutoExamsListForStudent(ExamsDTO examsDTO);
    
    public List<ExamsDTO> findSolvedExamsListForStudent(ExamsDTO examsDTO);
            
    public ExamsDTO findExams(Integer id);

    public ExamsDTO checkExamForStudent(Integer studentId, Integer examId);

    public List<ExamsDTO> findExamsListForTeacher(Integer schoolStagesId, Integer teacherId, Integer lessonId);

    //public List<ExamsDTO> findExamsListForStudent(Integer schoolStagesId, Integer branchId, Integer lessonId, Integer companyId, Integer studentId, Integer periodId);
    public ExamsDTO save(ExamsDTO examsDTO, IgmaUser isagUser);

    public List<ExamsDTO> findExamsListForStudent(ExamsDTO examsDTO);

    public List<ExamsDTO> deleteExamsListForStudent(Integer studentId);

    public void deleteExams(ExamsDTO examsDTO);

    public void deleteExams(Integer examId);
    
    public List<ExamsDTO> returnListDTO(List<Exams> list, Boolean detail);
}
