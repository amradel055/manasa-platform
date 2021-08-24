/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.TeachersDTO;
import com.isag.entity.IgmaUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface TeachersService {

    public List<TeachersDTO> findTeachers(Integer branchId, Integer id);

    public TeachersDTO save(TeachersDTO teachersDTO, IgmaUser isagUser);

    public void deleteTeachers(TeachersDTO teachersDTO);

    public void deleteTeachers(Integer teachersDTO);
}
