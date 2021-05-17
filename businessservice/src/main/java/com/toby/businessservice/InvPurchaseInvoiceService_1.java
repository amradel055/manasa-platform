/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.entity.TobyUser;
import com.toby.bussinessservice.global.InvPurchaseReturnSave;
import com.toby.core.UserDataDTO;
import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.PrintProductionDTO;
import com.toby.entity.InvPurchaseInvoice;
import com.toby.entity.InvPurchaseInvoiceDetail;
import com.toby.entity.InventorySetup;
import com.toby.views.ClientsNoMovment;
import com.toby.views.FactoryInvoiceStatus;
import com.toby.views.SummitionItemsView;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author WIN7
 */
@Remote
public interface InvPurchaseInvoiceService_1 {

    public InvPurchaseReturnSave addInvPurchaseInvoice(InvPurchaseInvoice invPurchaseInvoice,
            List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailList,
            List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailListDeleted,
            List<Integer> updatedPurchaseOrAddingOrderList, Integer purchaseOrAddingOrder, StringBuilder headIdList, Integer isPurchaseOrSales);

    public void addInvPurchaseInvoiceTest(InvPurchaseInvoice invPurchaseInvoice);

    public void deleteInvPurchaseInvoice(InvPurchaseInvoice invPurchaseInvoice, Integer isPurchaseOrSales);

    public List<InvPurchaseInvoice> getALLInvPurchaseInvoiceByCompanyId(Integer companyId);

    public List<InvPurchaseInvoice> getALLInvPurchaseInvoiceByBranchId(Integer branchId);

    public List<InvPurchaseInvoice> getALLInvPurchaseInvoicePostFlagedByBranchId(Integer branchId, Boolean type);

    public List<InvPurchaseInvoice> getALLSalesInvoiceByBranchId(Integer branchId);

    public List<InvPurchaseInvoice> getALLSalesInvoiceByBranchIdWithStatus(Integer branchId);

    public List<InvPurchaseInvoice> getALLSalesInvoiceByBranchIdWithStatusPer(Integer branchId);

    public List<InvPurchaseInvoice> getALLInvPurchaseInvoicePostFlagedByBranchIdPer(Integer branchId, Boolean type);

    public InvInvoiceDTO findInvPurchaseInvoiceById(InvInvoiceDTO invPurchaseInvoiceDto);

    public InvPurchaseInvoice updateInvPurchaseInvoice(InvPurchaseInvoice invPurchaseInvoice);

    public InvPurchaseInvoice updateInvPurchaseInvoiceForStopped(Integer invPurchaseInvoiceId, Integer stopped);

    public String updateFinalQuantityAndStatusDetailAndSaveDetail(List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailsUpdatedList, Integer isPurchaseOrSales, InvPurchaseInvoice invPurchaseInvoice);

    public Integer getMaxSerialForInvoiceDetail(InvPurchaseInvoice invPurchaseInvoiceId);

    public Integer getMaxSerialTaxForInvoice(Integer branchId, Integer inventoryId, Integer type, Boolean taxFalgFinal);

    public void findmofakroun();

    public void reCalcCostAvarage(Integer branchId);

    public BigDecimal findQuantityItemByDate(InvPurchaseInvoice invPurchaseInvoice, InvPurchaseInvoiceDetail invPurchaseInvoiceDetail);

    public void saveDetail(InvPurchaseInvoiceDetail invPurchaseInvoiceDetail, InvPurchaseInvoice invPurchaseInvoice,
            Integer isPurchaseOrSales, BigDecimal quantityItemsStoreByDate, InventorySetup invSetup);

    public InvInvoiceDTO updateRate(InvInvoiceDTO invPurchaseInvoiceDTO);

    public String validateloginDistributedScreens(UserDataDTO userDataDTO, ScreenNameClassEnum screenNameClassEnum,Integer gallaryId);

    public InvInvoiceDTO putCurrency(InvInvoiceDTO invPurchaseInvoiceDTO);

    public InvInvoiceDTO save(InvInvoiceDTO invPurchaseInvoiceDTO, TobyUser tobyUser);

    public InvInvoiceDTO convertInvPurchaseInvoiceToPurchaseInvoice(InvInvoiceDTO invPurchaseInvoiceDTO, Integer type);

    public Long getTotalRegistors(Integer type, Integer inventoryId, Integer branchId, Map<String, Object> filters);

    public Long getTotalRegistorsForGallary(Integer type, Integer galaryId, Integer branchId, Map<String, Object> filters);

    public List<InvInvoiceDTO> getALLInvPurchaseInvoiceDTOByTypeAndBranchId(Integer type, Integer inventoryId, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters);

    public List<InvInvoiceDTO> getALLInvPurchaseInvoiceDTOByTypeAndBranchIdFroGallary2(Integer type, Integer galaryId, Integer customerId);

    public InvInvoiceDTO getInvPurchaseInvoiceDTOBySerial(Integer serial, Integer branchId);

    public List<InvInvoiceDTO> getALLInvPurchaseInvoiceDTOByTypeAndBranchIdFroGallary(Integer type, Integer galaryId, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters);

    public void deleteInvPurchaseInvoiceDTO(InvInvoiceDTO invPurchaseInvoice, Integer isPurchaseOrSales);

    public InvInvoiceDTO returnInvInvoiceSummarySumation(InvPurchaseInvoice entity, BigDecimal sumDetailNet);

    public BigDecimal remainForInvPurchaseInvoice(Integer invPurchaseInvoiceId);

    public String findInvoiceStatus(Integer invPurchaseInvoiceId);

    public List<FactoryInvoiceStatus> findInvoiceStatus(Date dateFrom, Date dateTo, Integer status, Integer gallaryId, Integer branchId);

    public List<SummitionItemsView> findSummitionQuantity(Integer gallaryId, Integer branchId, Date dateFrom, Date dateTo, Integer groupId);

    public List<SummitionItemsView> getTheListBasedOnStagesList(Integer gallaryId, Date dateFrom, Date dateTo, Integer groupId, Integer branchId, TobyUser tobyUser);

    public List<ClientsNoMovment> findClientsNoMovment(Integer branchId, Integer gallaryId, Date date, Integer type);

    public List<InvInvoiceDTO> getALLInvPurchaseInvoiceDTOByStatus(PrintProductionDTO printProductionDTO, Integer branchId);

}
