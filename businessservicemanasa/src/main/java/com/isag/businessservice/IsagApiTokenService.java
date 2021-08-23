/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.IsagApiTokenDTO;
import com.isag.dto.StudentDTO;
import com.isag.entity.Employee;
import com.isag.entity.IsagUser;
import javax.ejb.Remote;

/**
 *
 * @author AhmedEssam
 */
@Remote
public interface IsagApiTokenService {

    public IsagApiTokenDTO addIsagApiToken(IsagApiTokenDTO isagApiTokenDTO, Employee employee);

    public IsagApiTokenDTO getIsagApiTokenDTOByTokenAndTokenID(IsagApiTokenDTO isagApiTokenDTO);

    public IsagApiTokenDTO addIsagApiToken(IsagApiTokenDTO isagApiTokenDTO, StudentDTO studentDTO);
    
    public IsagApiTokenDTO addIsagApiToken(IsagApiTokenDTO isagApiTokenDTO, IsagUser isagUser);
    
    public void deleteByStudentId(Integer studentId);

}
