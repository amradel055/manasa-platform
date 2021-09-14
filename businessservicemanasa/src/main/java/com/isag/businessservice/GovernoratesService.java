/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.GovernoratesDTO;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface GovernoratesService {

 public GovernoratesDTO findGovernorate(Integer governorateId);
 
 public List<GovernoratesDTO> findGovernorate();
    

}
