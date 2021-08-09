/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.define.ScreenNameClassEnum;
import com.isag.dto.InvDetailDTO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.dto.SymbolDTO;
import com.isag.entity.InvItem;
import com.isag.entity.InvOpenningBalanceItem;
import com.isag.entity.InvOpenningBalanceItemDetail;
import com.isag.entity.IsagUser;
import com.isag.views.ItemsBarcodesDetailsView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq002
 */
@Stateless
public class InvOpeningBalanceItemDetailServiceImpl implements InvOpeningBalanceItemDetailService {

    @EJB
    private GenericDAO dao;
    @EJB
    ItemsBarcodesDetailsViewService itemsBarcodesDetailsViewService;
    @EJB
    InvGlobalService invGlobalService;
    @EJB
    GenericService genericService;

    @Override
    public void addInvOpenningBalanceItemDetail(InvOpenningBalanceItemDetail details) {
        dao.saveEntity(details);
    }

    @Override
    public void deleteInvOpenningBalanceItemDetail(InvOpenningBalanceItemDetail details) {
        dao.deleteEntity(details);
    }

    @Override
    public InvOpenningBalanceItemDetail updateInvOpenningBalanceItemDetail(InvOpenningBalanceItemDetail details) {
        return dao.updateEntity(details);
    }

    @Override
    public List<InvOpenningBalanceItemDetail> getAllInvOpenningBalanceItemDetails() {
        return dao.findAll(InvOpenningBalanceItemDetail.class);
    }

    @Override
    public List<InvOpenningBalanceItemDetail> getAllInvOpenningBalanceItemDetails(Integer id) {
        Map<String, Object> params = new HashMap();
        params.put("balanceItemId", id);
        return dao.findListByQuery("SELECT d FROM InvOpenningBalanceItemDetail d WHERE d.balanceItemId.id =:balanceItemId", params);
    }
    
    @Override
    public Boolean getInvOpenningBalanceItemDetailsByItemId(Integer itemId) {
        Map<String, Object> params = new HashMap();
        params.put("itemId", itemId);
        List<InvOpenningBalanceItemDetail> list = dao.findListByQuery("SELECT d FROM InvOpenningBalanceItemDetail d WHERE d.itemId.id =:itemId", params);
        return (list != null && !list.isEmpty()) ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public InvOpenningBalanceItemDetail findInvOpenningBalanceItemDetailsById(Integer id) {
        return dao.findEntityById(InvOpenningBalanceItemDetail.class, id);
    }
    
    @Override
    public List<InvDetailDTO> getAllInvOpenningBalanceItemDetailByInvPurchaseInvoiceIdDTO(InvInvoiceDTO invInvoiceDTO, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("balanceItemId", invInvoiceDTO.getId());
        List<InvOpenningBalanceItemDetail> details = dao.findListByQuery("SELECT e FROM InvOpenningBalanceItemDetail e WHERE e.balanceItemId.id = :balanceItemId", params);
        return returnListDTO(details, branchId, invInvoiceDTO);
    }
    
    private List<InvDetailDTO> returnListDTO(List<InvOpenningBalanceItemDetail> list, Integer branchId, InvInvoiceDTO invPurchaseInvoiceDTO) {
        List<InvDetailDTO> dTOList = new ArrayList<>();
        List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList = itemsBarcodesDetailsViewService.findItemsBarcodesDetailsViewBranchId(branchId);
        Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap = new HashMap<>();
        for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailsViewList) {
            itemsBarcodeMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
        }
        for (InvOpenningBalanceItemDetail entity : list) {
            dTOList.add(mapTODTO(entity, itemsBarcodeMap, invPurchaseInvoiceDTO));
        }
        return dTOList;
    }

    private InvDetailDTO mapTODTO(InvOpenningBalanceItemDetail entity, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap, InvInvoiceDTO invPurchaseInvoiceDTO) {
        InvDetailDTO dTO = initMapToDTO(entity, itemsBarcodeMap, invPurchaseInvoiceDTO);
        return dTO;
    }

    private InvDetailDTO initMapToDTO(InvOpenningBalanceItemDetail entity, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap, InvInvoiceDTO invInvoiceDTO) {

        InvDetailDTO dTO = new InvDetailDTO();
        dTO = invGlobalService.fillItemDetail(dTO, itemsBarcodeMap,
                    entity.getId(), entity.getSerial(), (entity.getBranchId() != null ? entity.getBranchId().getId() : null), entity.getCost(),
                    BigDecimal.ZERO, entity.getQuantity(), 0, (entity.getBalanceItemId() != null ? entity.getBalanceItemId().getId() : null),
                    BigDecimal.ZERO, entity.getNet(), entity.getId(), entity.getCreationDate(), entity.getCreatedBy().getId(),
                    entity.getItemBarcode(), entity.getScrewing(), entity.getItemId().getId(), BigDecimal.ZERO, 
                    (entity.getUnitId() != null ? entity.getUnitId().getId() : null ), (entity.getUnitId() != null ? entity.getUnitId().getSerial() : null),(entity.getUnitId() != null ? entity.getUnitId().getName() : null),
                    (entity.getItemId() != null ? entity.getItemId().getIsInventoryItem() : null ),BigDecimal.ZERO,BigDecimal.ZERO
                    ,entity.getAdd1(),entity.getAdd2(),entity.getAdd3(),entity.getDisc1(),entity.getDisc2(),entity.getDisc3(),BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO,entity.getCartonsNumber(),entity.getCartonsCapacity(), BigDecimal.ZERO);
        dTO = invGlobalService.calculateDetailTotalsRow(dTO, invInvoiceDTO,true);        
        dTO.setRealQuantity(dTO.getQuantity().multiply(dTO.getScrewing()));
        return dTO;
    }
    
    @Override
    public void deleteInvOpenningBalanceItemDetailListDTO(List<InvDetailDTO> invDetailDTOList) {
        if (invDetailDTOList != null && !invDetailDTOList.isEmpty()) {
            for (InvDetailDTO detailDTO : invDetailDTOList) {
                if (detailDTO.getId() != null) {
                    InvOpenningBalanceItemDetail detail = new InvOpenningBalanceItemDetail();
                    detail.setId(detailDTO.getId());
                    dao.deleteEntity(detail);
                }
            }
        }

    }
    
    @Override
    public String addInvOpenningBalanceItemDetailDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList, InvInvoiceDTO invInvoiceDTO,ScreenNameClassEnum screenNameClassEnum,  IsagUser isagUser) {
        String error = null;
        if (invPurchaseInvoiceDetailList != null && !invPurchaseInvoiceDetailList.isEmpty()) {
            error = invGlobalService.validateSave(invPurchaseInvoiceDetailList, invInvoiceDTO.getInvInventoryId().getId(), screenNameClassEnum, 
                    invInvoiceDTO.getInventorySetupDTO().getNegativeSell(),(invInvoiceDTO.getLoadedSerial() != null),
                    invInvoiceDTO.getInventorySetupDTO().getSellBuy(),invInvoiceDTO.getSupplierInvoiceNumber());
            if (error != null && !error.isEmpty()) {
                return error;
            }
            InvOpenningBalanceItemDetail detail;
            for (InvDetailDTO detailDTO : invPurchaseInvoiceDetailList) {

                detail = mapToEntity(detailDTO, invInvoiceDTO.getId(), isagUser);
                dao.updateEntity(detail);
            }
        }

        return error;
    }
    
    @Override
    public List<Object[]> getInvOpeningBalanceItemDetailsByBranchId(Integer branchId,List<Integer> itemIdList) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("itemIdList", itemIdList);
        List<Object[]> balanceItemDetailList = dao.findListByQuery("SELECT i.itemId.id , i.cost  FROM InvOpenningBalanceItemDetail i WHERE i.branchId.id = :branchId and i.itemId.id in :itemIdList", params);
        return balanceItemDetailList;
    }
    
     private InvOpenningBalanceItemDetail mapToEntity(InvDetailDTO dTO, Integer balanceItemId, IsagUser isagUser) {
        InvOpenningBalanceItemDetail entity = new InvOpenningBalanceItemDetail();

        entity.setSerial(dTO.getSerial());
        entity.setCost(dTO.getCost());
        entity.setQuantity(dTO.getQuantity());
        entity.setNet(dTO.getNet());
        entity.setScrewing(dTO.getScrewing());
        entity.setCartonsCapacity(dTO.getCartonsCapacity());
        entity.setCartonsNumber(dTO.getCartonsNumber());
        entity.setAdd1(dTO.getAdd1());
        entity.setAdd2(dTO.getAdd2());
        entity.setAdd3(dTO.getAdd3());
        entity.setDisc1(dTO.getDisc1());
        entity.setDisc2(dTO.getDisc2());
        entity.setDisc3(dTO.getDisc3());
        
        InvOpenningBalanceItem invOpenningBalanceItem = new InvOpenningBalanceItem();
        invOpenningBalanceItem.setId(balanceItemId);
        entity.setBalanceItemId(invOpenningBalanceItem);


        if (dTO.getItemsBarcodesDetail() != null || dTO.getItemsBarcodesDetailTrans() != null) {
            InvItem invItem = new InvItem();
            invItem.setId(dTO.getItemsBarcodesDetail() != null ? dTO.getItemsBarcodesDetail().getItemId() : dTO.getItemsBarcodesDetailTrans().getItemId());
            entity.setItemId(invItem);
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
                params.put("balanceItemId", balanceItemId);
                Integer max = genericService.getMaxGenericSerialForDetail(InvOpenningBalanceItemDetail.class, "balanceItemId", balanceItemId);
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
