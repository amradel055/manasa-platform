/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.general;

import java.io.UnsupportedEncodingException;
import javax.ejb.Remote;

/**
 *
 * @author ahmed
 */
@Remote
public interface GeneralService {



    public Boolean verifyToken(String token);

    public String DecodeJWT(String token) throws UnsupportedEncodingException;
    
}
