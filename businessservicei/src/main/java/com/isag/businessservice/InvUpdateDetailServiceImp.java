package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.define.ScreenNameClassEnum;
import com.isag.dto.InvDetailDTO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.dto.SymbolDTO;
import com.isag.entity.InvItem;
import com.isag.entity.InvUpdate;
import com.isag.entity.InvUpdateDetail;
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
public class InvUpdateDetailServiceImp implements InvUpdateDetailService {

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
    public void addInvUpdateDetails(InvUpdateDetail details) {
        dao.saveEntity(details);
    }

    @Override
    public void deleteInvUpdateDetails(InvUpdateDetail details) {
        dao.deleteEntity(details);
    }

    @Override
    public InvUpdateDetail updateInvUpdateDetails(InvUpdateDetail details) {
        return dao.updateEntity(details);
    }

    @Override
    public List<InvUpdateDetail> getAllInvUpdateDetails() {
        return dao.findAll(InvUpdateDetail.class);
    }

    @Override
    public List<InvUpdateDetail> getAllInvUpdateDetails(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("invUpdate", id);
        return dao.findListByQuery("SELECT d FROM InvUpdateDetail d WHERE d.invUpdateId.id =:invUpdate", params);
    }

    @Override
    public List<InvDetailDTO> getAllInvTransferDetailsByInvPurchaseInvoiceIdDTO(InvInvoiceDTO invInvoiceDTO, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("invUpdate", invInvoiceDTO.getId());
        List<InvUpdateDetail> details = dao.findListByQuery("SELECT e FROM InvUpdateDetail e WHERE e.invUpdateId.id = :invUpdate", params);
        return returnListDTO(details, branchId, invInvoiceDTO);
    }

    private List<InvDetailDTO> returnListDTO(List<InvUpdateDetail> list, Integer branchId, InvInvoiceDTO invInvoiceDTO) {
        List<InvDetailDTO> dTOList = new ArrayList<>();
//        List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList = itemsBarcodesDetailsViewService.findItemsBarcodesDetailsViewBranchIdAll(branchId);
//        Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap = new HashMap<>();
        Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap = new HashMap<>();
//        for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailsViewList) {
//            itemsBarcodeMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
//        }
        List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList = invPurchaseInvoiceDetailsService.findItemBarcodeDetails(list, invInvoiceDTO);

        for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailsViewList) {
            itemsBarcodeMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
        }
        for (InvUpdateDetail entity : list) {
            dTOList.add(mapTODTO(entity, itemsBarcodeMap, invInvoiceDTO));
        }
        return dTOList;
    }

    private InvDetailDTO mapTODTO(InvUpdateDetail entity, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap, InvInvoiceDTO invPurchaseInvoiceDTO) {
        InvDetailDTO dTO = initMapToDTO(entity, itemsBarcodeMap, invPurchaseInvoiceDTO);
        return dTO;
    }

    private InvDetailDTO initMapToDTO(InvUpdateDetail entity, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap, InvInvoiceDTO invInvoiceDTO) {

        InvDetailDTO dTO = new InvDetailDTO();
        dTO = invGlobalService.fillItemDetail(dTO, itemsBarcodeMap,
                entity.getId(), entity.getSerial(), null, BigDecimal.ZERO,
                BigDecimal.ZERO, entity.getOldAmount(), 0, (entity.getInvUpdateId() != null ? entity.getInvUpdateId().getId() : null),
                BigDecimal.ZERO, BigDecimal.ZERO, entity.getId(), entity.getCreationDate(), entity.getCreatedBy().getId(),
                entity.getItemBarcode(), BigDecimal.ZERO, entity.getInvItemId().getId(), BigDecimal.ZERO,
                null, null, null, entity.getInvItemId().getIsInventoryItem(), entity.getNewAmount(), entity.getOldAmount(),
                 BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                entity.getCartonNumber(), entity.getCartonsCapacity(), BigDecimal.ZERO);
        dTO = invGlobalService.calculateDetailTotalsRow(dTO, invInvoiceDTO, true);
        dTO.setRealQuantity(dTO.getQuantity().multiply(dTO.getScrewing()));
        return dTO;
    }

    @Override
    public void deleteInvUpdateDetailListDTO(List<InvDetailDTO> invDetailDTOList) {
        if (invDetailDTOList != null && !invDetailDTOList.isEmpty()) {
            for (InvDetailDTO detailDTO : invDetailDTOList) {
                if (detailDTO.getId() != null) {
                    InvUpdateDetail detail = new InvUpdateDetail();
                    detail.setId(detailDTO.getId());
                    dao.deleteEntity(detail);
                }
            }
        }

    }

    @Override
    public String addInvUpdateDetailsDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList, InvInvoiceDTO invInvoiceDTO, ScreenNameClassEnum screenNameClassEnum, IsagUser isagUser) {
        String error = null;
        if (invPurchaseInvoiceDetailList != null && !invPurchaseInvoiceDetailList.isEmpty()) {
            error = invGlobalService.validateSave(invPurchaseInvoiceDetailList, invInvoiceDTO.getInvInventoryId().getId(), screenNameClassEnum,
                    invInvoiceDTO.getInventorySetupDTO().getNegativeSell(), (invInvoiceDTO.getLoadedSerial() != null),
                    invInvoiceDTO.getInventorySetupDTO().getSellBuy(), invInvoiceDTO.getSupplierInvoiceNumber());
            if (error != null && !error.isEmpty()) {
                return error;
            }
            InvUpdateDetail detail;
            for (InvDetailDTO detailDTO : invPurchaseInvoiceDetailList) {

                detail = mapToEntity(detailDTO, invInvoiceDTO.getId(), isagUser);
                dao.updateEntity(detail);
            }
        }

        return error;
    }

    private InvUpdateDetail mapToEntity(InvDetailDTO dTO, Integer invUpdateId, IsagUser isagUser) {
        InvUpdateDetail entity = new InvUpdateDetail();

        entity.setSerial(dTO.getSerial());
        entity.setNewAmount(dTO.getActualQuantity());
        entity.setItemBarcode(dTO.getItemBarcode());
        entity.setOldAmount(dTO.getAvailableQuantityRow());
        entity.setDifference(dTO.getActualQuantity().subtract(dTO.getAvailableQuantityRow()));

        entity.setCartonNumber(dTO.getCartonsNumber());
        entity.setCartonsCapacity(dTO.getCartonsCapacity());

        InvUpdate invUpdate = new InvUpdate();
        invUpdate.setId(invUpdateId);
        entity.setInvUpdateId(invUpdate);

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
                params.put("invUpdateId", invUpdateId);
                Integer max = genericService.getMaxGenericSerialForDetail(InvUpdateDetail.class, "invUpdateId", invUpdateId);
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
