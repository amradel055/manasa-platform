/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.LessonImgDTO;
import com.isag.entity.IgmaUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface LessonImgService {

    public List<String> findAllLessons(Integer lessonId);

    public void deleteLesson(Integer lessonId);
    
    public List<LessonImgDTO>  save(List<LessonImgDTO> lessonImgDTOList, IgmaUser isagUser, Integer lessonId);
    
    public LessonImgDTO saveList(List<String> LessonImgDTOist, Integer lessonId, Integer branchId, Integer companyId);
}
