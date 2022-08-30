/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.EventResponseDTO;
import com.toby.entity.TobyUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author H
 */
@Remote
public interface EventResponseService {

    public void save(EventResponseDTO eventResponseDTO, TobyUser tobyUser);

    public void delete(Integer eventResponseId);

    public List<EventResponseDTO> returnEventResponseDTOByEventId(Integer eventId);

    public List<EventResponseDTO> returnEventResponseDTO();

}
