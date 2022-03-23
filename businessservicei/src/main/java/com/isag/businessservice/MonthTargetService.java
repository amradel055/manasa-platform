/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.InvInvoiceDTO;
import com.isag.dto.InvMonthTargetDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author ahmedEssam
 */
@Remote
public interface MonthTargetService {

    public InvInvoiceDTO addMonthTargetDTO(InvInvoiceDTO invInvoiceDTO, IsagUser isagUser);

    public void deleteMonthTargetDTO(InvInvoiceDTO invInvoiceDTO, IsagUser isagUser);

    public List<InvMonthTargetDTO> getMonthTargetDTOByBranchId(Integer branchId);

    public List<InvMonthTargetDTO> getMonthTargetDTOByUserId(Integer branchId, Integer userId);
    
    public InvMonthTargetDTO getMonthTargetDTOById(InvInvoiceDTO invInvoiceDTO);
    
    public Boolean monthTargetDTOaleardyExist(InvInvoiceDTO invInvoiceDTO);
    
    public InvMonthTargetDTO monthTargetDTOByduration(InvInvoiceDTO invInvoiceDTO);

}
