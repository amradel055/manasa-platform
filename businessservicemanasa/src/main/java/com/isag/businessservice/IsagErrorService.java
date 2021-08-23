/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.IsagErrorDTO;
import com.isag.entity.IsagUser;
import javax.ejb.Remote;

/**
 *
 * @author hhhh
 */
@Remote
public interface IsagErrorService {

    public  void save(IsagErrorDTO isagErrorDTO, IsagUser isagUser);
}
