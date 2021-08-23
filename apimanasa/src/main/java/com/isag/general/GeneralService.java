/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.general;

import com.isag.dto.APIWorkOrderDTO;
import com.isag.dto.APIloginDTO;
import com.isag.entity.Employee;
import com.isag.entity.IsagUser;
import com.isag.root.LoginRoot;
import java.io.UnsupportedEncodingException;
import javax.ejb.Remote;

/**
 *
 * @author ahmed
 */
@Remote
public interface GeneralService {


    public APIloginDTO authenticateManasa(LoginRoot loginRoot);

    public APIloginDTO authenticateStudent(LoginRoot loginRoot);

    public APIloginDTO issueTokenManasa(IsagUser isagUser);

    public Boolean verifyToken(String token);

    public String DecodeJWT(String token) throws UnsupportedEncodingException;
    
}
