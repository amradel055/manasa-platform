/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.IgmaApiTokenDTO;
import com.isag.dto.StudentDTO;
import com.isag.entity.Employee;
import com.isag.entity.IgmaUser;
import javax.ejb.Remote;

/**
 *
 * @author AhmedEssam
 */
@Remote
public interface IgmaApiTokenService {

    public IgmaApiTokenDTO addIsagApiToken(IgmaApiTokenDTO isagApiTokenDTO, Employee employee);

    public IgmaApiTokenDTO getIsagApiTokenDTOByTokenAndTokenID(IgmaApiTokenDTO isagApiTokenDTO);

    public IgmaApiTokenDTO addIsagApiToken(IgmaApiTokenDTO isagApiTokenDTO, StudentDTO studentDTO);
    
    public IgmaApiTokenDTO addIsagApiToken(IgmaApiTokenDTO isagApiTokenDTO, IgmaUser isagUser);
    
    public void deleteByStudentId(Integer studentId);

}
