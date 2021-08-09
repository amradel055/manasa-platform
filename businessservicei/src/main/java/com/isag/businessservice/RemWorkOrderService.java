/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.RemWorkOrderDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface RemWorkOrderService {

    public RemWorkOrderDTO findWorkOrder(Integer workOrderId);

    public void deleteWorkOrder(RemWorkOrderDTO workOrderesDTO);

    public RemWorkOrderDTO save(RemWorkOrderDTO workOrderesDTO, IsagUser isagUser);

    public List<RemWorkOrderDTO> findWorkOrders(Integer branchId);

}
