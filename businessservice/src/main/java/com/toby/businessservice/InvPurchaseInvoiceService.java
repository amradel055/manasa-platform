/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.bussinessservice.global.InvPurchaseReturnSave;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvPurchaseInvoiceDTO1;
import com.toby.entity.InvPurchaseInvoice;
import com.toby.entity.InvPurchaseInvoiceDetail;
import com.toby.entity.InventorySetup;
import com.toby.entity.TobyUser;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author H
 */
@Remote
public interface InvPurchaseInvoiceService {

    public List<InvPurchaseInvoice> findInvPurchaseInvoiceforCustomer(Integer customerId);

    public InvPurchaseInvoiceDTO1 addInvPurchaseInvoice(InvPurchaseInvoiceDTO1 invPurchaseInvoiceDTO, TobyUser tobyUser, List<InvInventoryDTO> inventoryDTOList);

    public List<InvPurchaseInvoiceDTO1> findInvPurchaseInvoicefordelgatorDTO(TobyUser tobyUser);

    public List<InvPurchaseInvoiceDTO1> getlistPURCHASEORNO(TobyUser tobyUser);

    public List<InvPurchaseInvoiceDTO1> findInvPurchaseInvoiceDTOList(TobyUser tobyUser);

    public List<InvPurchaseInvoice> findInvPurchaseInvoiceList(TobyUser tobyUser);

    public List<InvPurchaseInvoiceDTO1> findInvPurchaseInvoiceDTOListByReceved(TobyUser tobyUser);

    public List<InvPurchaseInvoiceDTO1> getPurchaseIdFromView(TobyUser tobyUser);

    public List<InvPurchaseInvoice> getALLInvPurchaseInvoicePostFlagedByBranchIdPer(Integer branchId, Integer type);

    public InvPurchaseInvoiceDTO1 findInvPurchaseInvoiceDTO(Integer invPurchaseInvoiceDTOId, TobyUser tobyUser);

    public InvPurchaseInvoiceDTO1 deleteInvPurchaseInvoiceDTO(InvPurchaseInvoiceDTO1 invPurchaseInvoiceDTO, TobyUser tobyUser);

    public InvPurchaseInvoice updateInvPurchaseInvoice(InvPurchaseInvoice invPurchaseInvoice);

    public InvPurchaseInvoice findInvPurchaseInvoiceById(Integer invPurchaseInvoiceId);

    public InvPurchaseReturnSave addInvPurchaseInvoice(InvPurchaseInvoice invPurchaseInvoice,
            List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailList,
            List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailListDeleted,
            List<Integer> updatedPurchaseOrAddingOrderList, Integer purchaseOrAddingOrder, StringBuilder headIdList, Integer isPurchaseOrSales);

    public Integer getMaxSerialTaxForInvoice(Integer branchId, Integer inventoryId, Boolean type, Boolean taxFalgFinal);

    public String updateFinalQuantityAndStatusDetailAndSaveDetail(List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailsUpdatedList,
            Integer isPurchaseOrSales, InvPurchaseInvoice invPurchaseInvoice);

    public Integer getMaxSerialForInvoiceDetail(InvPurchaseInvoice invPurchaseInvoiceId);

    public BigDecimal findQuantityItemByDate(InvPurchaseInvoice invPurchaseInvoice, InvPurchaseInvoiceDetail invPurchaseInvoiceDetail);

    public void saveDetail(InvPurchaseInvoiceDetail invPurchaseInvoiceDetail, InvPurchaseInvoice invPurchaseInvoice,
            Integer isPurchaseOrSales, BigDecimal quantityItemsStoreByDate, InventorySetup invSetup);

    public void deleteInvPurchaseInvoice(InvPurchaseInvoice invPurchaseInvoice, Integer isPurchaseOrSales);

    public List<InvPurchaseInvoice> getALLSalesInvoiceByBranchIdWithStatusPer(Integer branchId);

    

}
