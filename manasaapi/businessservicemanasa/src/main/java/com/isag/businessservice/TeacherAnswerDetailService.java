/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.TeacherAnswerDetailDTO;
import com.isag.entity.IgmaUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface TeacherAnswerDetailService {

    public void save(TeacherAnswerDetailDTO teacherAnswerDetailDTO, IgmaUser isagUser);

    public void delete(Integer id, IgmaUser igmaUser);

    public List<TeacherAnswerDetailDTO> findTeacherAnswerDetailDTO(Integer branchId);

    public List<TeacherAnswerDetailDTO> findAnswerDetail(Integer answerId);

}
