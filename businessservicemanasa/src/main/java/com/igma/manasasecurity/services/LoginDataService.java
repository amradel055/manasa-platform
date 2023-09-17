/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.igma.manasasecurity.services;

import com.igma.manasasecurity.entities.Security;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface LoginDataService {

    public Security findData(String name, Integer version);


}
