/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.bussinessservice.global.InvPurchaseReturnSave;
import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.InvPurchaseInvoice;
import com.isag.entity.InvPurchaseInvoiceDetail;
import com.isag.entity.InventorySetup;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author WIN7
 */
@Remote
public interface InvPurchaseInvoiceService {

    public InvPurchaseReturnSave addInvPurchaseInvoice(InvPurchaseInvoice invPurchaseInvoice,
            List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailList,
            List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailListDeleted,
            List<Integer> updatedPurchaseOrAddingOrderList, Integer purchaseOrAddingOrder, StringBuilder headIdList, Integer isPurchaseOrSales);

    public void addInvPurchaseInvoiceTest(InvPurchaseInvoice invPurchaseInvoice);

    public void deleteInvPurchaseInvoice(InvPurchaseInvoice invPurchaseInvoice, Integer isPurchaseOrSales);

    public void deleteInvPurchaseInvoiceDTO(InvInvoiceDTO invPurchaseInvoice, Integer isPurchaseOrSales);

    public List<InvPurchaseInvoice> getALLInvPurchaseInvoiceByCompanyId(Integer companyId);

    public List<InvPurchaseInvoice> getALLInvPurchaseInvoiceByBranchId(Integer branchId);

    public List<InvPurchaseInvoice> getALLInvPurchaseInvoicePostFlagedByBranchId(Integer branchId, Integer type);

    public List<InvPurchaseInvoice> getALLSalesInvoiceByBranchId(Integer branchId);

    public List<InvPurchaseInvoice> getALLSalesInvoiceByBranchIdWithStatus(Integer branchId);

    public List<InvPurchaseInvoice> getALLSalesInvoiceByBranchIdWithStatusPer(Integer branchId);

    public List<InvPurchaseInvoice> getALLInvPurchaseInvoicePostFlagedByBranchIdPer(Integer branchId, Integer type);

    public InvPurchaseInvoice findInvPurchaseInvoiceById(Integer invPurchaseInvoiceId);

    public InvPurchaseInvoice updateInvPurchaseInvoice(InvPurchaseInvoice invPurchaseInvoice);

    public String updateFinalQuantityAndStatusDetailAndSaveDetail(List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailsUpdatedList, Integer isPurchaseOrSales, InvPurchaseInvoice invPurchaseInvoice);

    public Integer getMaxSerialForInvoiceDetail(InvPurchaseInvoice invPurchaseInvoiceId);

    public Integer getMaxSerialTaxForInvoice(Integer branchId, Integer inventoryId, Integer type, Boolean taxFalgFinal);

    public void findmofakroun();

    public void reCalcCostAvarage(Integer branchId);

    public BigDecimal findQuantityItemByDate(InvPurchaseInvoice invPurchaseInvoice, InvPurchaseInvoiceDetail invPurchaseInvoiceDetail);

    public BigDecimal findQuantityItemByDateWithOutInventory(InvPurchaseInvoice invPurchaseInvoice, Boolean sellBuy, InvPurchaseInvoiceDetail invPurchaseInvoiceDetail);

    public void saveDetail(InvPurchaseInvoiceDetail invPurchaseInvoiceDetail, InvPurchaseInvoice invPurchaseInvoice,
            Integer isPurchaseOrSales, BigDecimal quantityItemsStoreByDate, InventorySetup invSetup);

    public Long getTotalRegistors(Integer type, Integer inventoryId, Integer branchId, Map<String, Object> filters);

    public List<InvInvoiceDTO> getALLInvPurchaseInvoiceDTOByTypeAndBranchId(Integer type, Integer inventoryId, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters);

    public InvPurchaseInvoice getALLInvPurchaseInvoiceByBranchIdAndTypeAndSerial(Integer branchId, Integer type, Integer serial);

    public Integer FindSupplierInvoiceNumberByDate(Integer branchId, Integer type);

    public Integer FindSupplierInvoiceNumberByMonth(Integer branchId, Integer type);
}
