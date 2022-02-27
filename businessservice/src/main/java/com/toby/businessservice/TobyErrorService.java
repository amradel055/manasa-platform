/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.TobyErrorDTO;
import com.toby.entity.TobyUser;
import javax.ejb.Remote;

/**
 *
 * @author hhhh
 */
@Remote
public interface TobyErrorService {

    public  void save(TobyErrorDTO tobyErrorDTO, TobyUser tobyUser);
}
