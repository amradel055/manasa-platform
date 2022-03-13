/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.LessonDTO;
import com.isag.entity.IgmaUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface LessonService {

    public List<LessonDTO> findAllLessons();

    public List<LessonDTO> findLessonByPeriod(Integer monthId, Integer branchId, Integer companyId, Integer schoolStagesGuideId, Integer lessonType, Integer id);

    public List<LessonDTO> findFreeLesson(Integer branchId, Integer companyId);

    public List<LessonDTO> findAllLessonsByBranchId(Integer branchId);

    public List<LessonDTO> findAllLessonsByTeacher(Integer branchId, Integer companyId, Integer teacherId, 
            Integer materialId, Integer schoolStageId, Integer id, Integer periodId);

    public void deleteLesson(LessonDTO lessonDTO);

    public void deleteLesson(Integer lessonDTO);

    public LessonDTO save(LessonDTO lessonDTO, IgmaUser isagUser);
}
