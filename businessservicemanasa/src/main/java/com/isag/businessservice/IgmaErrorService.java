/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.IgmaErrorDTO;
import com.isag.entity.IgmaUser;
import javax.ejb.Remote;

/**
 *
 * @author hhhh
 */
@Remote
public interface IgmaErrorService {

    public  void save(IgmaErrorDTO isagErrorDTO, IgmaUser isagUser);
}
