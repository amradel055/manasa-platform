/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.dto.InvPurchaseInvoiceDetailDTO;
import com.toby.dto.ProProductionProductNumberDTO;
import com.toby.dto.ProProductionTransactionDetailDetailViewDTO;
import com.toby.dto.ProProductionTransactionDetailViewDTO;
import com.toby.entity.GlYear;
import com.toby.entity.InvPurchaseInvoice;
import com.toby.entity.TobyUser;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author H
 */
@Remote
public interface InvPurchaseInvoiceDetailService {

    public InvPurchaseInvoiceDetailDTO addInvPurchaseDetailsInvoice(InvPurchaseInvoiceDetailDTO invPurchaseInvoiceDTO, TobyUser tobyUser);

    public InvPurchaseInvoiceDetailDTO addInvPurchaseInvoice(InvPurchaseInvoiceDetailDTO invPurchaseInvoiceDetailDTO, TobyUser tobyUser);

    public List<InvPurchaseInvoiceDetailDTO> getInvPurchaseInvoiceDetailListByBranchId(Integer invPurchaseInvoiceId, TobyUser tobyUser);

    public InvPurchaseInvoiceDetailDTO getInvPurchaseInvoiceDetailsByBranchId(Integer invPurchaseInvoiceId, TobyUser tobyUser);

    public List<InvPurchaseInvoiceDetailDTO> addInvPurchaseInvoiceDetailList(List<InvPurchaseInvoiceDetailDTO> invPurchaseInvoiceDetailDTOList, Integer invPurchaseInvoiceDTOId, TobyUser tobyUser);

    public BigDecimal getLastCostItem(Integer itemId, GlYear glYear, Integer branchId);

    public List<InvPurchaseInvoiceDetailDTO> getInvPurchaseInvoiceDetailsByPurchaseId(Integer invPurchaseInvoiceId, TobyUser tobyUser);

    public InvPurchaseInvoiceDetailDTO deleteInvPurchaseInvoiceDTO(InvPurchaseInvoiceDetailDTO invPurchaseInvoiceDetailDTO, TobyUser tobyUser);

    public List<InvPurchaseInvoiceDetailDTO> deleteInvPurchaseInvoiceDetailDTO(InvPurchaseInvoice invPurchaseInvoice, TobyUser tobyUser);

    public List<ProProductionProductNumberDTO> getInvPurchaseInvoiceDetailClothNumber(Integer invPurchaseInvoiceId, Integer productionTransactionDTO);

    public List<ProProductionTransactionDetailViewDTO> getInvPurchaseInvoiceDetailItemCompleted(Integer invPurchaseInvoiceId, Integer clothNumber, Integer productionTransactionDTO);

    public List<ProProductionTransactionDetailDetailViewDTO> getInvPurchaseInvoiceDetailItems(Integer invPurchaseInvoiceId, Integer clothNumber, Integer stageId);
}
