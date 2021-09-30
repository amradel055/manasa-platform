/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.InstProjectGuideDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author ahmed
 */
@Remote
public interface InstContractProjectService {

   public void save(List<InstProjectGuideDTO> list, Integer instContractId,IsagUser isagUser) ;
    
    
}
