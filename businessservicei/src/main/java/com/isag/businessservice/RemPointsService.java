/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.RemPointsDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface RemPointsService {

    public RemPointsDTO findPoint(Integer pointId);

    public void deletePoint(RemPointsDTO pointsDTO);

    public RemPointsDTO save(RemPointsDTO pointsDTO, IsagUser isagUser);

    public List<RemPointsDTO> findPoints(Integer branchId);

}
