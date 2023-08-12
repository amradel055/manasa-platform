/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.StudentStatusDTO;
import com.isag.entity.IgmaUser;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface StudentStatusService {

    public StudentStatusDTO save(StudentStatusDTO studentStatusDTO, IgmaUser isagUser);

    public void deleteStudentReset(Integer studentStatusId);

    public Integer findStudentStatus(Integer studentId, Integer branchId);
}
