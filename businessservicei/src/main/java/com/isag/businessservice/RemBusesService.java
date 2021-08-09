/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.RemBusesDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface RemBusesService {

    public RemBusesDTO findBus(Integer busId);

    public void deleteBuses(RemBusesDTO busesDTO);

    public RemBusesDTO save(RemBusesDTO busesDTO, IsagUser isagUser);

    public List<RemBusesDTO> findBuses(Integer branchId);

}
