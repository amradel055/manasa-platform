/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.CommentsDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface CommentsService {

    public List<CommentsDTO> findCommentsByLessonId(Integer lessonId);

    public CommentsDTO save(CommentsDTO commentsDTO, IsagUser isagUser);

    public void deleteComments(CommentsDTO commentsDTO);

    public void deleteComments(Integer commentId);

    public void deleteCommentsByLesson(Integer lessonId);
}
