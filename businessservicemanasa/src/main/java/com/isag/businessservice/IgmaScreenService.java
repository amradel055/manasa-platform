/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.IgmaScreenDTO;
import com.isag.entity.IgmaScreen;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface IgmaScreenService {
    
    public List<IgmaScreenDTO> getAllScreens(Integer moduleType);

  
}
