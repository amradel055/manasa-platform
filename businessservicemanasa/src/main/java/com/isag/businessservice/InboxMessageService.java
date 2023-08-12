/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.InboxMessagesDTO;
import com.isag.entity.IgmaUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface InboxMessageService {

    public InboxMessagesDTO save(InboxMessagesDTO dto , IgmaUser igmaUser);

    public InboxMessagesDTO findById(Integer id);
    
    public void readDone(Integer id);

    public void delete(Integer id);

    public List<InboxMessagesDTO> find(Integer studentId, Integer teacherId, Integer schoolId, 
            Integer materialId , Integer readDone);

}
