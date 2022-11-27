/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.LessonVoiceDTO;
import com.isag.entity.IgmaUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface LessonVoiceService {

    public List<String> findAllLessons(Integer lessonId);

    public void deleteLesson(Integer lessonId);

    public List<LessonVoiceDTO> save(List<LessonVoiceDTO> lessonVoiceDTOList, IgmaUser isagUser, Integer lessonId);

    public LessonVoiceDTO saveList(List<LessonVoiceDTO> LessonVoiceDTOist, Integer lessonId, Integer branchId, Integer companyId);

    public List<LessonVoiceDTO> findlessinList(Integer lessonId);

    public void deleteLessonVoice(Integer id);

    public void updateimage(LessonVoiceDTO lessonVoiceDTO, IgmaUser isagUser);
}
