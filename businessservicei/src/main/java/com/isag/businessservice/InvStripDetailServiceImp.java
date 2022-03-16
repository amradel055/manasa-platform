package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.define.ScreenNameClassEnum;
import com.isag.dto.InvDetailDTO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.dto.SymbolDTO;
import com.isag.entity.InvItem;
import com.isag.entity.InvStrip;
import com.isag.entity.InvStripDetail;
import com.isag.entity.IsagUser;
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
public class InvStripDetailServiceImp implements InvStripDetailService{
    @EJB
    private GenericDAO dao;
    @EJB
    ItemsBarcodesDetailsViewService itemsBarcodesDetailsViewService;
    @EJB
    InvGlobalService invGlobalService;
    @EJB
    GenericService genericService;


    @Override
    public void addInvStripDetails(InvStripDetail details) {
        dao.saveEntity(details);
    }

    @Override
    public void deleteInvStripDetails(InvStripDetail details) {
        dao.deleteEntity(details);
    }

    @Override
    public InvStripDetail updateInvStripDetails(InvStripDetail details) {
        return dao.updateEntity(details);
    }

    @Override
    public List<InvStripDetail> getAllInvStripDetails() {
        return dao.findAll(InvStripDetail.class);
    }

    @Override
    public List<InvStripDetail> getAllInvStripDetails(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("invStrip", id);
        return dao.findListByQuery("SELECT d FROM InvStripDetail d WHERE d.invStripId.id =:invStrip", params);
    }
    
    @Override
    public List<InvDetailDTO> getAllInvTransferDetailsByInvPurchaseInvoiceIdDTO(InvInvoiceDTO invInvoiceDTO, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("invStrip", invInvoiceDTO.getId());
        List<InvStripDetail> details = dao.findListByQuery("SELECT e FROM InvStripDetail e WHERE e.invStripId.id = :invStrip", params);
        return returnListDTO(details, branchId, invInvoiceDTO);
    }
    
    private List<InvDetailDTO> returnListDTO(List<InvStripDetail> list, Integer branchId, InvInvoiceDTO invPurchaseInvoiceDTO) {
        List<InvDetailDTO> dTOList = new ArrayList<>();
        List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList = itemsBarcodesDetailsViewService.findItemsBarcodesDetailsViewBranchId(branchId);
        Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap = new HashMap<>();
        for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailsViewList) {
            itemsBarcodeMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
        }
        for (InvStripDetail entity : list) {
            dTOList.add(mapTODTO(entity, itemsBarcodeMap, invPurchaseInvoiceDTO));
        }
        return dTOList;
    }

    private InvDetailDTO mapTODTO(InvStripDetail entity, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap, InvInvoiceDTO invPurchaseInvoiceDTO) {
        InvDetailDTO dTO = initMapToDTO(entity, itemsBarcodeMap, invPurchaseInvoiceDTO);
        return dTO;
    }

    private InvDetailDTO initMapToDTO(InvStripDetail entity, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap, InvInvoiceDTO invInvoiceDTO) {

        InvDetailDTO dTO = new InvDetailDTO();
        dTO = invGlobalService.fillItemDetail(dTO, itemsBarcodeMap,
                    entity.getId(), entity.getSerial(), null, BigDecimal.ZERO,
                    BigDecimal.ZERO, entity.getBookBalance(), 0, (entity.getInvStripId() != null ? entity.getInvStripId().getId() : null),
                    BigDecimal.ZERO, BigDecimal.ZERO, entity.getId(), entity.getCreationDate(), entity.getCreatedBy().getId(),
                    entity.getItemBarcode(), BigDecimal.ZERO, entity.getInvItemId().getId(), BigDecimal.ZERO, 
                    null, null, null,entity.getInvItemId().getIsInventoryItem(),entity.getActualAmount(),BigDecimal.ZERO
                    ,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        dTO = invGlobalService.calculateDetailTotalsRow(dTO, invInvoiceDTO,true);        
        dTO.setRealQuantity(dTO.getQuantity().multiply(dTO.getScrewing()));
        dTO.setCostAvarage(entity.getInvItemId().getCostAverage());
        return dTO;
    }
    
    @Override
    public void deleteInvTransferDetailListDTO(List<InvDetailDTO> invDetailDTOList) {
        if (invDetailDTOList != null && !invDetailDTOList.isEmpty()) {
            for (InvDetailDTO detailDTO : invDetailDTOList) {
                if (detailDTO.getId() != null) {
                    InvStripDetail detail = new InvStripDetail();
                    detail.setId(detailDTO.getId());
                    dao.deleteEntity(detail);
                }
            }
        }

    }
    
     @Override
    public String addInvStripDetailsDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList, InvInvoiceDTO invInvoiceDTO,ScreenNameClassEnum screenNameClassEnum,  IsagUser isagUser) {
        String error = null;
        if (invPurchaseInvoiceDetailList != null && !invPurchaseInvoiceDetailList.isEmpty()) {
            error = invGlobalService.validateSave(invPurchaseInvoiceDetailList, invInvoiceDTO.getInvInventoryId().getId(), screenNameClassEnum, 
                    invInvoiceDTO.getInventorySetupDTO().getNegativeSell(),(invInvoiceDTO.getLoadedSerial() != null),
                    invInvoiceDTO.getInventorySetupDTO().getSellBuy(),invInvoiceDTO.getSupplierInvoiceNumber());
            if (error != null && !error.isEmpty()) {
                return error;
            }
            InvStripDetail detail;
            for (InvDetailDTO detailDTO : invPurchaseInvoiceDetailList) {

                detail = mapToEntity(detailDTO, invInvoiceDTO.getId(), isagUser);
                dao.updateEntity(detail);
            }
        }

        return error;
    }
    
    private InvStripDetail mapToEntity(InvDetailDTO dTO, Integer invStripId, IsagUser isagUser) {
        InvStripDetail entity = new InvStripDetail();

        entity.setSerial(dTO.getSerial());
        entity.setActualAmount(dTO.getActualQuantity());
        entity.setItemBarcode(dTO.getItemBarcode());
        entity.setBookBalance(dTO.getAvailableQuantityRow());
        
        InvStrip invStrip = new InvStrip();
        invStrip.setId(invStripId);
        entity.setInvStripId(invStrip);


        if (dTO.getItemsBarcodesDetail() != null || dTO.getItemsBarcodesDetailTrans() != null) {
            InvItem invItem = new InvItem();
            invItem.setId(dTO.getItemsBarcodesDetail() != null ? dTO.getItemsBarcodesDetail().getItemId() : dTO.getItemsBarcodesDetailTrans().getItemId());
            entity.setInvItemId(invItem);
            entity.setItemBarcode(dTO.getItemsBarcodesDetail() != null ? dTO.getItemsBarcodesDetail().getBarcode() : dTO.getItemsBarcodesDetailTrans().getBarcode());
        }

        
        if (entity.getUnitId() != null) {
            SymbolDTO unitDTO = new SymbolDTO();
            unitDTO.setId(entity.getUnitId().getId());
            unitDTO.setName(entity.getUnitId().getName());
            unitDTO.setSerial(entity.getUnitId().getSerial());
            dTO.setUnitId(unitDTO);
        }
        
        
        
        
        if (dTO.getId() == null) {
            if (entity.getSerial() == null) {
                Map<String, Object> params = new HashMap<>();
                params.put("invStripId", invStripId);
                Integer max = genericService.getMaxGenericSerialForDetail(InvStripDetail.class, "invStripId", invStripId);
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
        return entity;
    }


}
