/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.RemWorkOrderDTO;
import com.isag.dto.RemWorkOrderDetailDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface RemWorkOrderDetailService {

    public RemWorkOrderDetailDTO findWorkOrderDetail(Integer workOrderId);
    
    public List<RemWorkOrderDetailDTO> findAllWorkOrderDetail(Integer workOrderId);

    public void deleteWorkOrderDetail(RemWorkOrderDetailDTO workOrderesDTO);
    
    public void deleteAllWorkOrderDetail(List<RemWorkOrderDetailDTO> workOrderDetailesDTOList);

    public RemWorkOrderDetailDTO save(RemWorkOrderDetailDTO workOrderesDTO, IsagUser isagUser,Integer remWorkOrderId);
    
    public List<RemWorkOrderDetailDTO> saveAll(List<RemWorkOrderDetailDTO> workOrderDetailesDTOList, IsagUser isagUser,Integer remWorkOrderId) ;
    
    public RemWorkOrderDTO addDetail(RemWorkOrderDTO remWorkOrderDTO, Integer index);

}
