/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.JournalCloseDTO;
import com.toby.dto.JournalCloseDetailsDTO;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface JournalCloseService {
 
     public JournalCloseDTO save(JournalCloseDTO jounalCloseDto , List<JournalCloseDetailsDTO> deatailsList);
    

}
