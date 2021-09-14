/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.StudentResetDTO;
import com.isag.entity.IgmaUser;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface StudentResetService {

    public StudentResetDTO save(StudentResetDTO studentResetDTO, IgmaUser isagUser);

    public StudentResetDTO saveGeneratorCode(StudentResetDTO studentResetDTO, IgmaUser isagUser);

    public void deleteStudentReset(Integer studentResetDTO);
}
