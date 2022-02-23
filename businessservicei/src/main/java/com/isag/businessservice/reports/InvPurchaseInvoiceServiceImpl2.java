/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice.reports;

import com.isag.businessservice.InvPurchaseInvoiceDetailsService;
import com.isag.businessservice.reports.searchBean.InvPurchaseInvoiceSearchBean;
import com.isag.bussinessservice.global.InvPurchaseReturnSave;
import com.isag.core.GenericDAO;
import com.isag.dto.InvInventoryDTO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.InvPurchaseInvoice;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author WIN7
 */
@Stateless
public class InvPurchaseInvoiceServiceImpl2 implements InvPurchaseInvoiceService2 {

    @EJB
    private GenericDAO dao;
    @EJB
    InvPurchaseInvoiceDetailsService invPurchaseInvoiceDetailsService;
   

    InvPurchaseReturnSave invPurchaseReturnSave;

  
    @Override
    public List<InvPurchaseInvoice> getALLInvPurchaseInvoiceByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        return dao.findListByQuery("SELECT e FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId", params);
    }

    @Override
    public List<InvPurchaseInvoice> getALLInvPurchaseInvoicePostFlagedByBranchId(Integer branchId, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        List<InvPurchaseInvoice> invPurchaseInvoiceList = dao.findListByQuery("SELECT e FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId AND e.postFlag != 1 AND e.type =:type", params);
        return invPurchaseInvoiceList;
    }

 
    @Override
    public List<InvPurchaseInvoice> getALLSalesInvoiceByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        return dao.findListByQuery("SELECT e FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId AND e.type = true", params);
    }
    
    @Override
    public List<InvInvoiceDTO> getPurchaseInvoice(Integer branchId ,InvPurchaseInvoiceSearchBean invPurchaseInvoiceSearchBean){
        
        Map<String, Object> params = new HashMap<>();

        params.put("branchId", branchId);

        StringBuilder query = new StringBuilder();
        query.append("SELECT e FROM InvPurchaseInvoice e where e.branchId.id = :branchId ");
        
        appendDateFrom(invPurchaseInvoiceSearchBean, params, query);
        appendDateTo(invPurchaseInvoiceSearchBean, params, query);
        appendCashierFrom(invPurchaseInvoiceSearchBean, params, query);
        appendCashierTo(invPurchaseInvoiceSearchBean, params, query);
        
        List<InvPurchaseInvoice> details = dao.findListByQuery(query.toString(), params);
        
        return returnListDTOForList(details);
    }

    private void appendCashierTo(InvPurchaseInvoiceSearchBean invPurchaseInvoiceSearchBean, Map<String, Object> params, StringBuilder query) {
        if (invPurchaseInvoiceSearchBean.getCashierTo()!= null) {
            params.put("CashierTo", invPurchaseInvoiceSearchBean.getCashierTo().getUserCode());
            query.append(" and e.createdBy.userCode <= :CashierTo ");
        }
    }

    private void appendCashierFrom(InvPurchaseInvoiceSearchBean invPurchaseInvoiceSearchBean, Map<String, Object> params, StringBuilder query) {
        if (invPurchaseInvoiceSearchBean.getCashierFrom() != null) {
            params.put("CashierFrom", invPurchaseInvoiceSearchBean.getCashierFrom().getUserCode());
            query.append(" and e.createdBy.userCode >= :CashierFrom ");
        }
    }

    private void appendDateTo(InvPurchaseInvoiceSearchBean invPurchaseInvoiceSearchBean, Map<String, Object> params, StringBuilder query) {
        if (invPurchaseInvoiceSearchBean.getDateTo() != null) {
            params.put("DateTo", invPurchaseInvoiceSearchBean.getDateTo());
            query.append(" and e.date <= :DateTo ");
        }
    }

    private void appendDateFrom(InvPurchaseInvoiceSearchBean invPurchaseInvoiceSearchBean, Map<String, Object> params, StringBuilder query) {
        if(invPurchaseInvoiceSearchBean.getDateFrom() != null){
            params.put("DateFrom", invPurchaseInvoiceSearchBean.getDateFrom());
            query.append(" and e.date >= :DateFrom ");
        }
    }

   
  

    private List<InvInvoiceDTO> returnListDTOForList(List<InvPurchaseInvoice> invPurchaseInvoiceList) {
        List<InvInvoiceDTO> invInvoiceDTOList = new ArrayList<>();
        for (InvPurchaseInvoice invPurchaseInvoice : invPurchaseInvoiceList) {
            InvInvoiceDTO invInvoiceDTO = new InvInvoiceDTO();
            invInvoiceDTO = initDataForList(invInvoiceDTO, invPurchaseInvoice);
            invInvoiceDTOList.add(invInvoiceDTO);
        }
        return invInvoiceDTOList;
    }

    private InvInvoiceDTO initDataForList(InvInvoiceDTO invInvoiceDTO, InvPurchaseInvoice invPurchaseInvoice) {
        commonData(invInvoiceDTO, invPurchaseInvoice);
        return invInvoiceDTO;
    }

    private void commonData(InvInvoiceDTO invPurchaseInvoiceDTO, InvPurchaseInvoice entity) {
        invPurchaseInvoiceDTO.setId(entity.getId());
        invPurchaseInvoiceDTO.setSerial(entity.getSerial());
        invPurchaseInvoiceDTO.setDate(entity.getDate());
        invPurchaseInvoiceDTO.setIndex(entity.getSerial());
        invPurchaseInvoiceDTO.setPostFlag(entity.getPostFlag());
        invPurchaseInvoiceDTO.setRemarks(entity.getRemarks());
        if (entity.getOrganizationSiteId() != null) {
            invPurchaseInvoiceDTO.setOrganizationSiteName(entity.getOrganizationSiteId().getName());
        }
        if (entity.getInvInventoryId() != null) {
            InvInventoryDTO invInventoryDTO = new InvInventoryDTO();
            invInventoryDTO.setName(entity.getInvInventoryId().getName());
            invPurchaseInvoiceDTO.setInvInventoryId(invInventoryDTO);
        }
        if (entity.getInvDelegatorId()!= null) { 
            invPurchaseInvoiceDTO.setInvDelegatorName(entity.getInvDelegatorId().getName());
        }
        
        invPurchaseInvoiceDTO.setPaymentType(entity.getPaymentType());
       
        if (entity.getCreatedBy()!= null) {
        invPurchaseInvoiceDTO.setCreatedByName(entity.getCreatedBy().getName());
        }
    }
}
