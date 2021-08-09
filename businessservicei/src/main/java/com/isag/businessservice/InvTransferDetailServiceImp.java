package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.define.ScreenNameClassEnum;
import com.isag.dto.InvDetailDTO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.InvAddingorderDetail;
import com.isag.entity.InvItem;
import com.isag.entity.InvTransfer;
import com.isag.entity.InvTransferDetail;
import com.isag.entity.IsagUser;
import com.isag.entity.Symbol;
import com.isag.views.ItemsBarcodesDetailsView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author khaled
 */
@Stateless
public class InvTransferDetailServiceImp implements InvTransferDetailService {

    @EJB
    private GenericDAO dao;
    @EJB
    ItemsBarcodesDetailsViewService itemsBarcodesDetailsViewService;
    @EJB
    InvGlobalService invGlobalService;
    @EJB
    GenericService genericService;
    @EJB
    InvPurchaseInvoiceDetailsService invPurchaseInvoiceDetailsService;

    @Override
    public void addInvTransferDetails(InvTransferDetail details) {
        dao.saveEntity(details);
    }

    @Override
    public void deleteInvTransferDetails(InvTransferDetail details) {
        dao.deleteEntity(details);
    }

    @Override
    public InvTransferDetail updateInvTransferDetails(InvTransferDetail details) {
        return dao.updateEntity(details);
    }

    @Override
    public List<InvTransferDetail> getAllInvTransferDetails() {
        return dao.findAll(InvTransferDetail.class);
    }

    @Override
    public List<InvTransferDetail> getAllInvTransferDetails(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("transferId", id);
        return dao.findListByQuery("SELECT d FROM InvTransferDetail d WHERE d.invTransferId.id =:transferId", params);
    }
     @Override
    public InvTransferDetail findInvTransferDetailsById(Integer invTransferDetailsId) {
        return dao.findEntityById(InvTransferDetail.class, invTransferDetailsId);
    }
     @Override
    public List<InvTransferDetail> getAllInvTransferDetailsByInvTransferId(Integer invTransferId) {
        Map<String, Object> params = new HashMap<>();
        params.put("invTransferId", invTransferId);
        List<InvTransferDetail> details = dao.findListByQuery("SELECT e FROM InvTransferDetail e WHERE e.invTransferId.id = :invTransferId", params);
        return details;
    }
     @Override
    public List<InvTransferDetail> getAllInvTransferDetailsByInvTransferIdWithStatus(Integer invTransferId) {
        Map<String, Object> params = new HashMap<>();
        params.put("invTransferId", invTransferId);
        List<InvTransferDetail> details = dao.findListByQuery("SELECT e FROM InvTransferDetail e WHERE e.invTransferId.id = :invTransferId and (e.status != 2  OR e.status is null) ", params);
        return details;
    }
    
    
    @Override
    public List<InvDetailDTO> getAllInvTransferDetailsByInvPurchaseInvoiceIdDTO(InvInvoiceDTO invPurchaseInvoiceDTO, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("invTransferId", invPurchaseInvoiceDTO.getId());
        List<InvTransferDetail> details = dao.findListByQuery("SELECT e FROM InvTransferDetail e WHERE e.invTransferId.id = :invTransferId", params);
        return returnListDTO(details, branchId, invPurchaseInvoiceDTO);
    }

    private List<InvDetailDTO> returnListDTO(List<InvTransferDetail> list, Integer branchId, InvInvoiceDTO invInvoiceDTO) {
//        List<InvDetailDTO> dTOList = new ArrayList<>();
//        List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList = itemsBarcodesDetailsViewService.findItemsBarcodesDetailsViewBranchId(branchId);
//        Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap = new HashMap<>();
//        for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailsViewList) {
//            itemsBarcodeMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
//        }


        
        List<InvDetailDTO> dTOList = new ArrayList<>();
        Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap = new HashMap<>();
        List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList = invPurchaseInvoiceDetailsService.findItemBarcodeDetails(list, invInvoiceDTO);

        for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailsViewList) {
            itemsBarcodeMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
        }
        
        for (InvTransferDetail entity : list) {
            dTOList.add(mapTODTO(entity, itemsBarcodeMap, invInvoiceDTO));
        }
        return dTOList;
    }

    private InvDetailDTO mapTODTO(InvTransferDetail entity, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap, InvInvoiceDTO invPurchaseInvoiceDTO) {
        InvDetailDTO dTO = initMapToDTO(entity, itemsBarcodeMap, invPurchaseInvoiceDTO);
        return dTO;
    }

    private InvDetailDTO initMapToDTO(InvTransferDetail entity, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap, InvInvoiceDTO invInvoiceDTO) {

        InvDetailDTO dTO = new InvDetailDTO();
        dTO = invGlobalService.fillItemDetail(dTO, itemsBarcodeMap,
                    entity.getId(), entity.getSerial(), (entity.getBranchId() != null ? entity.getBranchId().getId() : null), BigDecimal.ZERO,
                    BigDecimal.ZERO, entity.getAmount(), 0, (entity.getInvTransferId() != null ? entity.getInvTransferId().getId() : null),
                    BigDecimal.ZERO, BigDecimal.ZERO, entity.getId(), entity.getCreationDate(), entity.getCreatedBy().getId(),
                    entity.getItemBarcode(), entity.getScrewing(), entity.getInvItemId().getId(), BigDecimal.ZERO, 
                    entity.getUnitId().getId(), entity.getUnitId().getSerial(), entity.getUnitId().getName(),
                    entity.getInvItemId().getIsInventoryItem(),BigDecimal.ZERO,BigDecimal.ZERO
                    ,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,
                    BigDecimal.ZERO,BigDecimal.ZERO,entity.getCartonNumber(),entity.getCartonsCapacity(), BigDecimal.ZERO);
        dTO = invGlobalService.calculateDetailTotalsRow(dTO, invInvoiceDTO,true);        
        dTO.setRealQuantity(dTO.getQuantity().multiply(dTO.getScrewing()));
        return dTO;
    }

    @Override
    public void deleteInvTransferDetailListDTO(List<InvDetailDTO> invDetailDTOList) {
        if (invDetailDTOList != null && !invDetailDTOList.isEmpty()) {
            for (InvDetailDTO detailDTO : invDetailDTOList) {
                if (detailDTO.getId() != null) {
                    InvTransferDetail detail = new InvTransferDetail();
                    detail.setId(detailDTO.getId());
                    dao.deleteEntity(detail);
                }
            }
        }

    }
    
    
    @Override
    public String addInvTransferDetailsDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList, InvInvoiceDTO invInvoiceDTO, 
            ScreenNameClassEnum screenNameClassEnum, IsagUser isagUser) {
        String error = null;
        if (invPurchaseInvoiceDetailList != null && !invPurchaseInvoiceDetailList.isEmpty()) {
            error = invGlobalService.validateSave(invPurchaseInvoiceDetailList, invInvoiceDTO.getInvInventoryId().getId(), screenNameClassEnum, 
                    invInvoiceDTO.getInventorySetupDTO().getNegativeSell(),(invInvoiceDTO.getLoadedSerial() != null),
                    invInvoiceDTO.getInventorySetupDTO().getSellBuy(),invInvoiceDTO.getSupplierInvoiceNumber());
            if (error != null && !error.isEmpty()) {
                return error;
            }
            InvTransferDetail detail;
            for (InvDetailDTO detailDTO : invPurchaseInvoiceDetailList) {

                detail = mapToEntity(detailDTO, invInvoiceDTO.getId(), isagUser);
                dao.updateEntity(detail);
            }
        }

        return error;
    }

    private InvTransferDetail mapToEntity(InvDetailDTO dTO, Integer invTrasferId, IsagUser isagUser) {
        InvTransferDetail entity = new InvTransferDetail();

        entity.setSerial(dTO.getSerial());
        entity.setFinalQuantity(dTO.getFinalQuantity());
        entity.setItemBarcode(dTO.getItemBarcode());
        entity.setAmount(dTO.getQuantity());
        entity.setScrewing(dTO.getScrewing());
        entity.setStatus(dTO.getStatus());
        entity.setCartonNumber(dTO.getCartonsNumber());
        entity.setCartonsCapacity(dTO.getCartonsCapacity());
        
        InvTransfer invTransfer = new InvTransfer();
        invTransfer.setId(invTrasferId);
        entity.setInvTransferId(invTransfer);

        if(dTO.getItemsBarcodesDetail() == null && dTO.getItemsBarcodesDetailTrans() != null){
            dTO.setItemsBarcodesDetail(dTO.getItemsBarcodesDetailTrans());
        }
        
        if (dTO.getItemsBarcodesDetail() != null || dTO.getItemsBarcodesDetailTrans() != null) {
            InvItem invItem = new InvItem();
            invItem.setId(dTO.getItemsBarcodesDetail() != null ? dTO.getItemsBarcodesDetail().getItemId() : dTO.getItemsBarcodesDetailTrans().getItemId());
            entity.setInvItemId(invItem);
            entity.setItemBarcode(dTO.getItemsBarcodesDetail() != null ? dTO.getItemsBarcodesDetail().getBarcode() : dTO.getItemsBarcodesDetailTrans().getBarcode());
        }

        if ((dTO.getItemsBarcodesDetail() != null && dTO.getItemsBarcodesDetail().getUnitId() != null)  ||( dTO.getItemsBarcodesDetailTrans() != null && dTO.getItemsBarcodesDetailTrans().getUnitId() != null)) {
            Symbol symbol = new Symbol();
            symbol.setId(dTO.getItemsBarcodesDetail() != null ? dTO.getItemsBarcodesDetail().getUnitId() :  dTO.getItemsBarcodesDetailTrans().getUnitId());
            entity.setUnitId(symbol);
        }

        if (dTO.getId() == null) {
            if (entity.getSerial() == null) {
                Map<String, Object> params = new HashMap<>();
                params.put("qotationId", invTrasferId);
                Integer max = genericService.getMaxGenericSerialForDetail(InvTransferDetail.class, "invTransferId", invTrasferId);
                entity.setSerial(max);
            }
            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());
        } else {
            IsagUser user = new IsagUser();
            user.setId(dTO.getCreatedBy());
            entity.setCreatedBy(user);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);
        }

        entity.setCompanyId(isagUser.getCompanyId());
        entity.setBranchId(isagUser.getBranchId());
        return entity;
    }

}
