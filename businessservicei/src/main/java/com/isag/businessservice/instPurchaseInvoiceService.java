/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.InstPurchaseInvoiceDTO;
import com.isag.dto.InstPurchaseInvoiceDetailDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author AhmedEssam
 */
@Remote
public interface instPurchaseInvoiceService {

    public InstPurchaseInvoiceDTO addInstPurchaseInvoice(InstPurchaseInvoiceDTO instPurchaseInvoiceDTO,List<InstPurchaseInvoiceDetailDTO> instPurchaseInvoiceDetailDTOList ,List<InstPurchaseInvoiceDetailDTO> instPurchaseInvoiceDetailDTOListDelete, IsagUser isagUser);

    public void deleteByInstPurchaseInvoiceId(Integer InstPurchaseInvoiceId);

    public InstPurchaseInvoiceDTO findbyInstPurchaseInvoiceId(Integer InstPurchaseInvoiceId);
    
    public List<InstPurchaseInvoiceDTO> getAllInstPurchaseInvoiceByBranchId(Integer branchId);
    
    public void deleteInstPurchaseInvoiceDTO(InstPurchaseInvoiceDTO instPurchaseInvoiceDTO, IsagUser isagUser);

    public Long getTotalRegistors(Integer branchId, Map<String, Object> filters);
    
    public List<InstPurchaseInvoiceDTO> getAllInstPurchaseInvoiceByBranchId(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters);
}
