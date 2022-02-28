/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.LessonLocationDTO;
import com.isag.entity.IgmaUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface LessonLocationService {

    public List<LessonLocationDTO> findAllLessonLocation();

    public void deleteLessonLocation(Integer lessonId);
    
    public List<LessonLocationDTO> save(List<LessonLocationDTO> lessonImgDTOList, IgmaUser isagUser , Integer lessonId);
}
