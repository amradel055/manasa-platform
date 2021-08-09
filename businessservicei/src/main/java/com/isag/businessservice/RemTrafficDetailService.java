/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.RemTrafficDTO;
import com.isag.dto.RemTrafficDetailDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface RemTrafficDetailService {

    public RemTrafficDetailDTO findTrafficDetail(Integer trafficId);
    
    public List<RemTrafficDetailDTO> findAllTrafficDetail(Integer trafficId);

    public void deleteTrafficDetail(RemTrafficDetailDTO trafficesDTO);
    
    public void deleteAllTrafficDetail(List<RemTrafficDetailDTO> trafficDetailesDTOList);

    public RemTrafficDetailDTO save(RemTrafficDetailDTO trafficesDTO, IsagUser isagUser,Integer remTrafficId);
    
    public List<RemTrafficDetailDTO> saveAll(List<RemTrafficDetailDTO> trafficDetailesDTOList, IsagUser isagUser,Integer remTrafficId) ;
    
    public RemTrafficDTO addDetail(RemTrafficDTO remTrafficDTO, Integer index);

}
