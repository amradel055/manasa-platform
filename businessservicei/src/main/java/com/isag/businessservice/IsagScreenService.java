/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.IsagScreenDTO;
import com.isag.entity.IsagScreen;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface IsagScreenService {
    
    public List<IsagScreenDTO> getAllScreens(Integer moduleType);

  
}
