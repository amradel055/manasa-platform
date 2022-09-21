/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.API.InvDetailsDTOAPI;
import com.toby.API.InvInvoiceDTOApi;
import com.toby.API.ResponseApi;
import com.toby.API.SummitionItemAPI;
import com.toby.entity.TobyUser;
import com.toby.bussinessservice.global.InvPurchaseReturnSave;
import com.toby.core.UserDataDTO;
import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.GeneralJournalDTO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.PrintProductionDTO;
import com.toby.dto.ProGroupDTO;
import com.toby.entity.InvPurchaseInvoice;
import com.toby.entity.InvPurchaseInvoiceDetail;
import com.toby.entity.InventorySetup;
import com.toby.views.ClientsNoMovment;
import com.toby.views.FactoryInvoiceStatus;
import com.toby.views.SummitionItemsView;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

    public Integer getInvoiceBySerial(Integer serial, Integer gallaryId);

    public Integer getInvoiceSerialById(Integer invoiceId);

    public InvInvoiceDTO findInvoiceBySerial(Integer serial);

    public InvPurchaseInvoice findInvPurchaseInvoiceGallaryDelivery(Integer serial, Integer type, Integer branchId);

    public void updateInvoiceForsendCloth(Integer id, Integer sendCloth);

    public BigDecimal getInvoiceByRaghiCode(String raghiCode, Integer invoiceId);

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

    public InvInvoiceDTO findInvPurchaseInvoiceById(Integer invoiceId);

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

    public String validateloginDistributedScreens(UserDataDTO userDataDTO, ScreenNameClassEnum screenNameClassEnum, Integer gallaryId);

    public InvInvoiceDTO putCurrency(InvInvoiceDTO invPurchaseInvoiceDTO);

    public InvInvoiceDTO save(InvInvoiceDTO invPurchaseInvoiceDTO, TobyUser tobyUser);

    public InvInvoiceDTO convertInvPurchaseInvoiceToPurchaseInvoice(InvInvoiceDTO invPurchaseInvoiceDTO, Integer type);

    public Long getTotalRegistors(Integer type, Integer inventoryId, Integer branchId, Map<String, Object> filters);

    public Long getTotalRegistorsForGallary(Integer type, Integer galaryId, Integer branchId, Map<String, Object> filters, Boolean isGallary);

    public List<InvInvoiceDTO> getALLInvPurchaseInvoiceDTOByTypeAndBranchId(Integer type, Integer inventoryId, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters);

    public List<InvInvoiceDTO> getALLInvPurchaseInvoiceDTOByTypeAndBranchIdFroGallary2(Integer type, Integer galaryId, Integer customerId);

    public List<InvInvoiceDTOApi> getALLInvPurchaseInvoiceDTOBYCustomerForWebSite(Integer type, String mobile);

    public InvInvoiceDTO getInvPurchaseInvoiceDTOBySerial(Integer serial, Integer branchId);

    public List<InvInvoiceDTO> getALLInvPurchaseInvoiceDTOByTypeAndBranchIdFroGallary(Integer type, Integer galaryId, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters, Boolean isGallary);

    public String deleteInvPurchaseInvoiceDTO(InvInvoiceDTO invPurchaseInvoice, Integer isPurchaseOrSales);

//    public InvInvoiceDTO returnInvInvoiceSummarySumation(InvPurchaseInvoice entity, BigDecimal sumDetailNet);
//    public BigDecimal remainForInvPurchaseInvoice(Integer invPurchaseInvoiceId);
    public BigDecimal returnRemain(InvInvoiceDTO invPurchaseInvoiceDTO);

    public BigDecimal findPayed(Integer invPurchaseInvoiceId);

    public BigDecimal returnRemain(Integer invPurchaseInvoiceId);

    public String findInvoiceStatus(Integer invPurchaseInvoiceId);

    public List<FactoryInvoiceStatus> findInvoiceStatus(Date dateFrom, Date dateTo, Integer status, Integer gallaryId, Integer branchId);

    public List<SummitionItemsView> findSummitionQuantity(Integer gallaryId, Integer branchId, Date dateFrom, Date dateTo, Integer groupId, List<InvInventoryDTO> invInventoryDTOs, List<ProGroupDTO> proGroupListSelected, Integer invoiceType);

    public List<SummitionItemsView> getTheListBasedOnStagesList(Integer gallaryId, Date dateFrom, Date dateTo, Integer branchId, TobyUser tobyUser, List<InvInventoryDTO> invInventoryDTOs, List<ProGroupDTO> proGroupIdList);

    public List<ClientsNoMovment> findClientsNoMovment(Integer branchId, Integer gallaryId, Date date, Integer type);

    public List<InvInvoiceDTO> getALLInvPurchaseInvoiceDTOByStatus(PrintProductionDTO printProductionDTO, Integer branchId);

    public String validateloginDistributedScreens(UserDataDTO userDataDTO, ScreenNameClassEnum screenNameClassEnum);

    public List<GeneralJournalDTO> tarhilOne(TobyUser tobyUser) throws CloneNotSupportedException;

    public List<InvInvoiceDTO> getALLInvPurchaseInvoiceDTOByStatusNew(PrintProductionDTO printProductionDTO, Integer branchId, String gallarySelected);

    public BigDecimal findNotice(Integer gallaryId, Integer branchId, Date dateFrom, Date dateTo, List<InvInventoryDTO> invInventoryDTOs);

    public BigDecimal findPersentageNotice(Integer gallaryId, Integer branchId, Date dateFrom, Date dateTo, List<InvInventoryDTO> invInventoryDTOs);

    public List<SummitionItemsView> findSummitionQuantityNew(Integer gallaryId, Integer branchId, Date dateFrom, Date dateTo,
            List<InvInventoryDTO> invInventoryDTOs, List<ProGroupDTO> proGroupListSelected, Integer invoiceType, Boolean detail);

    public Boolean chechIfinvoiceIsReturn(Integer serial);

    public void updateDetailInvoiceValues(Integer invPurchaseInvoiceId);

    public GeneralJournalDTO tarhilOneMoshtrayat(TobyUser tobyUser, Integer invPurchaseInvoiceId,
            Integer salesTaxAccount, Integer salesAccountNumber,
            Integer managerCostAccount, Integer costOfSoldGoods, Integer storeAccountNumber) throws CloneNotSupportedException;

    public Integer excuteQuery(String query);

    public List<Integer> findInvoiceIdList();

    public List<GeneralJournalDTO> testTarhil(TobyUser tobyUser) throws CloneNotSupportedException;

    public void updateTarhil(Integer invoiceId, Integer tarhilId);

    public InvInvoiceDTO completeTarhilInvoiceAfterSave(InvInvoiceDTO invInvoiceDTO, TobyUser tobyUser) throws CloneNotSupportedException;

    public InvInvoiceDTO saveNew(InvInvoiceDTO invInvoiceDTO, TobyUser tobyUser);

    public void tarhilExcel(TobyUser tobyUser) throws CloneNotSupportedException;

    public ResponseApi SaveApi(InvInvoiceDTOApi invInvoiceDTOApi);

    public InvInvoiceDTO getInvoiceBySerial(Integer branchId, Integer serial, Integer gallaryId);

    public void saveDeliveryPlace(Integer invoiceId, Integer deliverPlaceId);

    public void updateGallaryDelivery(Integer invoiceId);

    public InvInvoiceDTO convertInvPurchaseInvoiceToInvInvoice(InvInvoiceDTO invInvoiceDTO, Integer type);

    public List<InvInvoiceDTOApi> getALLInvPurchaseInvoiceDTOBYCustomerForWebSite(Integer type, Integer organizationId);

    public InvInvoiceDTO findInvPurchaseInvoiceByserial(Integer serial, Integer gallaryId);

    public InvInvoiceDTOApi findInvPurchaseInvoiceByserialAPI(Integer serial, Integer gallaryId, Integer branchId);

    public InvInvoiceDTOApi SaveApiDeskTop(InvInvoiceDTOApi invInvoiceDTOApi);

    public List<SummitionItemsView> findSummitionQuantityAPI(SummitionItemAPI summitionItemAPI);

    public List<InvInvoiceDTOApi> findCustomerInvoice(Integer customerId);

    public InvInvoiceDTOApi tarhilSalesAPI(InvInvoiceDTO dto);

    public InvInvoiceDTO mapToDTOApiDeskTop(InvInvoiceDTOApi invInvoiceDTOApi, Boolean isSave);
    
    public String printQRCode(Date invoiceDate ,String sellerName ,String sallerTRN ,BigDecimal finalNet ,BigDecimal VATTotalValue) throws UnsupportedEncodingException, IOException ;
    
    public void updateTarhil(Integer invoiceId);
    
    public List<InvDetailsDTOAPI> offer999Les16Les20Only(List<InvDetailsDTOAPI> detailList) ;

}
