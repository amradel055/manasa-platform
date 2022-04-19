package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.SymbolDTO;
import com.toby.entity.InvItem;
import com.toby.entity.InvStripDetail;
import com.toby.entity.InvUpdate;
import com.toby.entity.InvUpdateDetail;
import com.toby.entity.TobyUser;
import com.toby.views.ItemsBarcodesDetailsView;
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
    @EJB
    private QuantityItemsStoreByDateService quantityItemsStoreAddExitService;

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
    public List<InvDetailDTO> getAllInvDetailDTOByInvUpdateDetails(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("invUpdate", id);
        List<InvUpdateDetail> list = dao.findListByQuery("SELECT d FROM InvUpdateDetail d WHERE d.invUpdateId.id =:invUpdate", params);
       return returnListDTOInvUpdateDetail(list, list.get(0).getInvUpdateId().getBranchId().getId(), list.get(0).getInvUpdateId().getInventoryId().getId());
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
        Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap = new HashMap<>();
        List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList = invPurchaseInvoiceDetailsService.findItemBarcodeDetails(list, invInvoiceDTO.getBranchId());

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
                entity.getId(), entity.getSerial(), (entity.getInvUpdateId().getBranchId() != null ? entity.getInvUpdateId().getBranchId().getId() : null), BigDecimal.ZERO,
                BigDecimal.ZERO, entity.getOldAmount(), 0, (entity.getInvUpdateId() != null ? entity.getInvUpdateId().getId() : null),
                BigDecimal.ZERO, BigDecimal.ZERO, entity.getId(), entity.getCreationDate(), entity.getCreatedBy().getId(),
                null, null, null, null, null,
                entity.getItemBarcode(), entity.getScrewing(), entity.getInvItemId().getId(), BigDecimal.ZERO,
                null, null, null,
                entity.getInvItemId().getIsInventoryItem(),
                BigDecimal.ZERO, BigDecimal.ZERO, entity.getInvUpdateId().getInventoryId(), entity.getInvItemId(),
                invInvoiceDTO.getCompanyActivity(), invInvoiceDTO.getScreenName(), null, BigDecimal.ZERO);

        dTO = invGlobalService.calculateDetailTotalsRow(dTO, invInvoiceDTO, true, false);
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
    public void deleteInvUpdateDetailDTO(InvDetailDTO invDetailDTO) {
        if (invDetailDTO != null && invDetailDTO.getId() != null) {
            InvUpdateDetail detail = new InvUpdateDetail();
            detail.setId(invDetailDTO.getId());
            dao.deleteEntity(detail);
        }

    }

    @Override
    public String addInvUpdateDetailsDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList, InvInvoiceDTO invInvoiceDTO, ScreenNameClassEnum screenNameClassEnum, TobyUser tobyUser) {
        String error = null;
        if (invPurchaseInvoiceDetailList != null && !invPurchaseInvoiceDetailList.isEmpty()) {
            error = invGlobalService.validateSave(invPurchaseInvoiceDetailList, invInvoiceDTO.getInvInventoryId().getId(), screenNameClassEnum,
                    invInvoiceDTO.getInventorySetupDTO().getNegativeSell(), (invInvoiceDTO.getLoadedSerial() != null),
                    invInvoiceDTO.getInventorySetupDTO().getSellBuy(), invInvoiceDTO.getCompanyActivity());
            if (error != null && !error.isEmpty()) {
                return error;
            }
            InvUpdateDetail detail;
            for (InvDetailDTO detailDTO : invPurchaseInvoiceDetailList) {

                detail = mapToEntity(detailDTO, invInvoiceDTO.getId(), tobyUser);
                dao.updateEntity(detail);
            }
        }

        return error;
    }

    private InvUpdateDetail mapToEntity(InvDetailDTO dTO, Integer invUpdateId, TobyUser tobyUser) {
        InvUpdateDetail entity = new InvUpdateDetail();

        entity.setSerial(dTO.getSerial());
        entity.setItemBarcode(dTO.getItemBarcode());
        entity.setOldAmount(dTO.getAvailableQuantityRow());

        entity.setNewAmount(dTO.getActualQuantity());
        entity.setDifference(dTO.getActualQuantity().subtract(dTO.getAvailableQuantityRow()));
        entity.setWasalate(dTO.getWasalate());
        entity.setCostAvarage(dTO.getCostAvarage());

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
            entity.setCreatedBy(tobyUser);
            entity.setCreationDate(new Date());
        } else {
            TobyUser user = new TobyUser();
            user.setId(dTO.getCreatedBy());
            entity.setCreatedBy(user);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(tobyUser);
        }

        entity.setCompanyId(tobyUser.getCompanyId());
        return entity;
    }

    @Override
    public List<InvDetailDTO> addInvUpdateDetailsDTONewList(List<InvDetailDTO> detailDTOList, Integer invUpdateId, TobyUser tobyUser) {
        StringBuilder error = null;
        List<InvDetailDTO> list = new ArrayList<>();
        for (InvDetailDTO detailDTO : detailDTOList) {
            error = invGlobalService.validateSelectItem(detailDTO, error);

            if (error != null && !error.toString().isEmpty()) {
                detailDTO.setMsg(error.toString());
                return null;
            }
            InvUpdateDetail detail;

            detail = mapToEntity(detailDTO, invUpdateId, tobyUser);
            detail = dao.updateEntity(detail);
            detailDTO.setId(detail.getId());
            detailDTO.setSerial(detail.getSerial());
            list.add(detailDTO);
        }

        return list;
    }

    @Override
    public InvDetailDTO addInvUpdateDetailsDTONew(InvDetailDTO detailDTO, Integer invUpdateId, TobyUser tobyUser) {
        StringBuilder error = null;
        if (detailDTO != null) {
            error = invGlobalService.validateSelectItem(detailDTO, error);

            if (error != null && !error.toString().isEmpty()) {
                detailDTO.setMsg(error.toString());
                return detailDTO;
            }
            InvUpdateDetail detail;

            detail = mapToEntity(detailDTO, invUpdateId, tobyUser);
            detail = dao.updateEntity(detail);
            detailDTO.setId(detail.getId());
            detailDTO.setSerial(detail.getSerial());
        }

        return detailDTO;
    }

    @Override
    public List<InvDetailDTO> getAllInvUpdatesDetailFromInvTransferDetail(Integer invStripSerial, Integer branchId, Integer inventoryId) {
        List<InvDetailDTO> invdetailListAfterSummition = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        params.put("invStripSerial", invStripSerial);
        List<InvStripDetail> stripDetailList = dao.findListByQuery("SELECT d FROM InvStripDetail d WHERE d.invStripId.serial =:invStripSerial", params);
        List<InvDetailDTO> invdetailListBeforeSummition = returnListDTO(stripDetailList, branchId, inventoryId);

        Map<Integer, InvDetailDTO> map = new HashMap<>();
        for (InvDetailDTO detailDTO : invdetailListBeforeSummition) {
            if (map.containsKey(detailDTO.getItemId().getId())) {
                InvDetailDTO detail = map.get(detailDTO.getItemId().getId());
                detailDTO.setActualQuantity(detail.getActualQuantity().add(detailDTO.getActualQuantity()));
                detailDTO.setWasalate(detail.getWasalate().add(detailDTO.getWasalate()));
                map.put(detailDTO.getItemId().getId(), detailDTO);
            } else {
                map.put(detailDTO.getItemId().getId(), detailDTO);
            }
        }
        List<InvDetailDTO> result = new ArrayList(map.values());

        for (InvDetailDTO dTO : result) {
            dTO.setAvailableQuantityRow(quantityItemsStoreAddExitService.getQuantatyItemByinventory(inventoryId, dTO.getItemId().getId(), true, null));
            dTO.setOldQuantity(dTO.getAvailableQuantityRow());
            dTO.setOldQuantityBak(dTO.getAvailableQuantityRow());
            dTO.setCostAvarage(dTO.getItemId().getLastCost());
            dTO.setTotalQuantityCost(dTO.getCostAvarage().multiply(dTO.getActualQuantity()));
            dTO.setId(null);
            invdetailListAfterSummition.add(dTO);
        }

        return invdetailListAfterSummition;
    }

    private List<InvDetailDTO> returnListDTO(List<InvStripDetail> list, Integer branchId, Integer inventoryId) {
        List<InvDetailDTO> dTOList = new ArrayList<>();
        Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap = new HashMap<>();
        List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList = invPurchaseInvoiceDetailsService.findItemBarcodeDetails(list, branchId);

        for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailsViewList) {
            itemsBarcodeMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
        }
        for (InvStripDetail entity : list) {
            dTOList.add(mapTODTO(entity, itemsBarcodeMap, inventoryId));
        }
        return dTOList;
    }

    private InvDetailDTO mapTODTO(InvStripDetail entity, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap, Integer inventoryId) {
        InvDetailDTO dTO = initMapToDTO(entity, itemsBarcodeMap, inventoryId);
        return dTO;
    }

    private InvDetailDTO initMapToDTO(InvStripDetail entity, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap, Integer inventoryId) {

        InvDetailDTO dto = new InvDetailDTO();

        dto.setId(entity.getId());
        dto.setSerial(entity.getSerial());

        ItemsBarcodesDetailsView barcodesDetailsView = itemsBarcodeMap.get(entity.getItemBarcode());
        dto.setItemsBarcodesDetail(barcodesDetailsView);
        dto.setItemsBarcodesDetailTrans(dto.getItemsBarcodesDetail());
        dto.setItemName(entity.getInvItemId().getName());
        dto.setItemCode(entity.getInvItemId().getCode());
        dto.setSupplierName(entity.getInvItemId().getSiteId() != null ? entity.getInvItemId().getSiteId().getName() : "");

        if (entity.getUnitId() != null) {
            SymbolDTO unitDTO = new SymbolDTO();
            unitDTO.setId(entity.getUnitId().getId());
            unitDTO.setName(entity.getUnitId().getName());
            unitDTO.setSerial(entity.getUnitId().getSerial());
            dto.setUnitName(entity.getUnitId().getName());
            dto.setUnitId(unitDTO);
        }

        dto.setIndex(entity.getId());
        dto.setCreatedDate(dto.getCreatedDate());
        dto.setCreatedBy(entity.getCreatedBy().getId());

        dto.setActualQuantity(entity.getActualAmount());
        dto.setWasalate(entity.getWasalate());

        if (entity.getInvItemId() != null) {
            InvItemDTO invItemDTO = new InvItemDTO();
            invItemDTO.setId(entity.getInvItemId().getId());
            invItemDTO.setName(entity.getInvItemId().getName());
            invItemDTO.setCode(entity.getInvItemId().getCode());
            invItemDTO.setLastCost(entity.getInvItemId().getLastCost());
            dto.setItemId(invItemDTO);

        }

        dto.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dto.setCreatedDate(entity.getCreationDate());

        dto.setCompanyId(entity.getCompanyId() != null ? entity.getCompanyId().getId() : null);

        return dto;
    }

    private List<InvDetailDTO> returnListDTOInvUpdateDetail(List<InvUpdateDetail> list, Integer branchId, Integer inventoryId) {
        List<InvDetailDTO> dTOList = new ArrayList<>();
        Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap = new HashMap<>();
        List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList = invPurchaseInvoiceDetailsService.findItemBarcodeDetails(list, branchId);

        for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailsViewList) {
            itemsBarcodeMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
        }
        for (InvUpdateDetail entity : list) {
            dTOList.add(mapTODTO(entity, itemsBarcodeMap, inventoryId));
        }
        return dTOList;
    }

    private InvDetailDTO mapTODTO(InvUpdateDetail entity, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap, Integer inventoryId) {
        InvDetailDTO dTO = initMapToDTO(entity, itemsBarcodeMap, inventoryId);
        return dTO;
    }

    private InvDetailDTO initMapToDTO(InvUpdateDetail entity, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap, Integer inventoryId) {

        InvDetailDTO dto = new InvDetailDTO();

        dto.setId(entity.getId());
        dto.setSerial(entity.getSerial());

        ItemsBarcodesDetailsView barcodesDetailsView = itemsBarcodeMap.get(entity.getItemBarcode());
        dto.setItemsBarcodesDetail(barcodesDetailsView);
        dto.setItemsBarcodesDetailTrans(dto.getItemsBarcodesDetail());
        dto.setItemName(entity.getInvItemId().getName());
        dto.setItemCode(entity.getInvItemId().getCode());
        dto.setSupplierName(entity.getInvItemId().getSiteId() != null ? entity.getInvItemId().getSiteId().getName() : "");

        if (entity.getUnitId() != null) {
            SymbolDTO unitDTO = new SymbolDTO();
            unitDTO.setId(entity.getUnitId().getId());
            unitDTO.setName(entity.getUnitId().getName());
            unitDTO.setSerial(entity.getUnitId().getSerial());
            dto.setUnitName(entity.getUnitId().getName());
            dto.setUnitId(unitDTO);
        }

        dto.setIndex(entity.getId());
        dto.setCreatedDate(dto.getCreatedDate());
        dto.setCreatedBy(entity.getCreatedBy().getId());

        dto.setOldQuantity(entity.getOldAmount());
        dto.setOldQuantityBak(entity.getOldAmount());
        dto.setWasalate(entity.getWasalate());
        dto.setActualQuantity(entity.getNewAmount());
        dto.setDifferanceQuantity(entity.getDifference());
        dto.setCostAvarage(entity.getCostAvarage());
        dto.setTotalQuantityCost(entity.getCostAvarage().multiply(entity.getNewAmount()));

        if (entity.getInvItemId() != null) {
            InvItemDTO invItemDTO = new InvItemDTO();
            invItemDTO.setId(entity.getInvItemId().getId());
            invItemDTO.setName(entity.getInvItemId().getName());
            invItemDTO.setCode(entity.getInvItemId().getCode());
            dto.setItemId(invItemDTO);

        }

        dto.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dto.setCreatedDate(entity.getCreationDate());

        dto.setCompanyId(entity.getCompanyId() != null ? entity.getCompanyId().getId() : null);

        return dto;
    }

}
