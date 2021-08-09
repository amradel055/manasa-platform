/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.RemBusesFollowDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface RemBusesFollowService {

    public RemBusesFollowDTO findBusesFollow(Integer busesFollowId);

    public void deleteBusesFollow(RemBusesFollowDTO busesFollowDTO);

    public RemBusesFollowDTO save(RemBusesFollowDTO busesFollowDTO, IsagUser isagUser);

    public List<RemBusesFollowDTO> findBusesFollowList(Integer branchId);

}
