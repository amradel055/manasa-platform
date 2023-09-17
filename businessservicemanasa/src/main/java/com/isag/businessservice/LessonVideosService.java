/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.LessonVideosDTO;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface LessonVideosService {

    public List<String> findlessonList(Integer lessonId , Integer type) ;
     
    public void deleteLesson(Integer lessonId);
    
    public void deleteVideos(String voice);

    public LessonVideosDTO saveList(List<String> LessonVideosDTOist, Integer lessonId, Integer branchId, Integer companyId , Integer type);
    
    public List<LessonVideosDTO> findlessinList(Integer lessonId);

    public void deleteLessonVideos(Integer id);

}
