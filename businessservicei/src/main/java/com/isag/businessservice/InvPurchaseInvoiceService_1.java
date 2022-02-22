/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.api.dto.DistributionApiData;
import com.isag.api.dto.DistributionApiSearch;
import com.isag.businessservice.reports.searchBean.SubAccountSummarySearchBean;
import com.isag.bussinessservice.global.InvPurchaseReturnSave;
import com.isag.core.UserDataDTO;
import com.isag.define.ScreenNameClassEnum;
import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.InvPurchaseInvoice;
import com.isag.entity.InvPurchaseInvoiceDetail;
import com.isag.entity.InventorySetup;
import com.isag.entity.IsagUser;
import com.isag.views.SupplierAccountViewBean;
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

    public InvInvoiceDTO getInvPurchaseInvoiceDTOBySupplierInvoiceNumber(InvInvoiceDTO invInvoiceDTO, Integer branchId);

    public List<InvPurchaseInvoice> getALLInvPurchaseInvoicePostFlagedByBranchIdPer(Integer branchId, Boolean type);

    public InvInvoiceDTO findInvPurchaseInvoiceById(InvInvoiceDTO invPurchaseInvoiceDto, IsagUser isagUser);

    public InvInvoiceDTO findInvPurchaseInvoiceByTableId(InvInvoiceDTO invPurchaseInvoiceDto, Integer branchId);

    public InvInvoiceDTO findInvPurchaseInvoiceBySerial(InvInvoiceDTO invInvoiceDTO, Integer branchId);

    public InvPurchaseInvoice updateInvPurchaseInvoice(InvPurchaseInvoice invPurchaseInvoice);

    public String updateFinalQuantityAndStatusDetailAndSaveDetail(List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailsUpdatedList, Integer isPurchaseOrSales, InvPurchaseInvoice invPurchaseInvoice);

    public Integer getMaxSerialForInvoiceDetail(InvPurchaseInvoice invPurchaseInvoiceId);

    public Integer getMaxSerialTaxForInvoice(Integer branchId, Integer inventoryId, Integer type, Boolean taxFalgFinal);

    public void findmofakroun();

    public void reCalcCostAvarage(Integer branchId);

    public BigDecimal findQuantityItemByDate(InvPurchaseInvoice invPurchaseInvoice, InvPurchaseInvoiceDetail invPurchaseInvoiceDetail);

    public void saveDetail(InvPurchaseInvoiceDetail invPurchaseInvoiceDetail, InvPurchaseInvoice invPurchaseInvoice,
            Integer isPurchaseOrSales, BigDecimal quantityItemsStoreByDate, InventorySetup invSetup);

    public InvInvoiceDTO updateRate(InvInvoiceDTO invPurchaseInvoiceDTO);

    public String validateloginDistributedScreens(UserDataDTO userDataDTO, ScreenNameClassEnum screenNameClassEnum);

    public InvInvoiceDTO putCurrency(InvInvoiceDTO invPurchaseInvoiceDTO);

    public InvInvoiceDTO save(InvInvoiceDTO invPurchaseInvoiceDTO, IsagUser isagUser);

    public InvInvoiceDTO convertInvPurchaseInvoiceToPurchaseInvoice(InvInvoiceDTO invPurchaseInvoiceDTO, Integer type);

    public Long getTotalRegistors(Integer type, Integer inventoryId, Integer branchId, Map<String, Object> filters);

    public List<InvInvoiceDTO> getALLInvPurchaseInvoiceDTOByTypeAndBranchId(Integer type, Integer inventoryId, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters);

    public void deleteInvPurchaseInvoiceDTO(InvInvoiceDTO invPurchaseInvoice, Integer isPurchaseOrSales);

    public void deleteInvPurchaseInvoiceDTO(Integer invoiceId);

    public InvInvoiceDTO returnInvInvoiceSummarySumation(InvPurchaseInvoice entity, BigDecimal sumDetailNet);

    public BigDecimal netPay(SubAccountSummarySearchBean cashAccountSettlementSearchBean, Integer bankId);

    public BigDecimal returnInvInvoiceSummarySummationDuration(Integer BranchId, Integer type, Date dateFrom, Date dateTo);

    public void updatelookAndIsDone(SupplierAccountViewBean supplierAccountViewBean);

    public void updatelookAndIsDone(List<SupplierAccountViewBean> supplierAccountViewBeanList);

    public DistributionApiData distributionApiData(DistributionApiSearch distributionApiSearch);

    public Integer findInvPurchaseInvoiceBySerialNew(Integer serial, Integer branchId, Integer type)  ;
    
    public void updateDetailInvoiceValues(Integer invPurchaseInvoiceId) ;

    }
