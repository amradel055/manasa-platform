/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.resualtDTO;
import com.isag.dto.searchDTO;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public interface ReportService {
    
 public  List<resualtDTO> getReporySearch(searchDTO search);
 
  
    
}
