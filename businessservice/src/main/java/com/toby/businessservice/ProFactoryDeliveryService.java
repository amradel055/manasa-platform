/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.PrintProductionDTO;
import com.toby.entity.TobyUser;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface ProFactoryDeliveryService {

    public Long getTotalRegistors(Integer type, Integer branchId, Map<String, Object> filters);

    public Boolean checkProFactoryDelivery(Integer id);

    public List<InvInvoiceDTO> getALLProFactoryDeliveryDTOByBranchId(Integer type, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters);

    public InvInvoiceDTO findProFactoryDeliveryById(InvInvoiceDTO invPurchaseInvoiceDto);

    public InvInvoiceDTO convertProFactoryDeliveryToInvInvoice(InvInvoiceDTO invPurchaseInvoiceDTO, Integer type);

    public InvInvoiceDTO save(InvInvoiceDTO invInvoiceDTO, TobyUser tobyUser);

    public void deleteInvAddingOrder(InvInvoiceDTO invoiceDTO);

    public List<InvInvoiceDTO> getALLProFactoryDeliveryDTOByStatus(PrintProductionDTO printProductionDTO, Integer branchId);

    public List<InvInvoiceDTO> getALLProFactoryDeliveryDTOByStatusRemain(PrintProductionDTO printProductionDTO, Integer branchId);
}
