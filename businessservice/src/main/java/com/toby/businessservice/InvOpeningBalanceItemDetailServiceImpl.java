/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.define.CompanyActivityClassEnum;
import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.InvOpenningBalanceItemDTO;
import com.toby.dto.InvOpenningBalanceItemDetailDTO;
import com.toby.dto.SymbolDTO;
import com.toby.entity.Branch;
import com.toby.entity.InvItem;
import com.toby.entity.InvOpenningBalanceItem;
import com.toby.entity.InvOpenningBalanceItemDetail;
import com.toby.entity.Symbol;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import com.toby.views.ItemsBarcodesDetailsView;
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
    InvGlobalService invGlobalService;    
    @EJB
    GenericService genericService;    
    @EJB
    ItemsBarcodesDetailsViewService itemsBarcodesDetailsViewService;

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
    public InvOpenningBalanceItemDetail findInvOpenningBalanceItemDetailsById(Integer id) {
        return dao.findEntityById(InvOpenningBalanceItemDetail.class, id);
    }

    @Override
    public List<InvOpenningBalanceItemDetailDTO> deleteInvInventoryTransactionDetailList(Integer inventoryTransactionId, TobyUser tobyUser) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public InvOpenningBalanceItemDetailDTO mapToDTO(InvOpenningBalanceItemDetail invOpenningBalanceItemDetail, TobyUser tobyUser) {
        InvOpenningBalanceItemDetailDTO invOpenningBalanceItemDetailDTO = new InvOpenningBalanceItemDetailDTO();
        invOpenningBalanceItemDetailDTO.setId(invOpenningBalanceItemDetail.getId());
        invOpenningBalanceItemDetailDTO.setIndex(invOpenningBalanceItemDetail.getId());
        invOpenningBalanceItemDetailDTO.setNet(invOpenningBalanceItemDetail.getNet());
        invOpenningBalanceItemDetailDTO.setQuantity(invOpenningBalanceItemDetail.getQuantity());
        invOpenningBalanceItemDetailDTO.setScrewing(invOpenningBalanceItemDetail.getScrewing());
        invOpenningBalanceItemDetailDTO.setSerial(invOpenningBalanceItemDetail.getSerial());
        invOpenningBalanceItemDetailDTO.setCost(invOpenningBalanceItemDetail.getCost());
        if (invOpenningBalanceItemDetail.getBalanceItemId() != null) {
            InvOpenningBalanceItemDTO invOpenningBalanceItemDTO = new InvOpenningBalanceItemDTO();
            invOpenningBalanceItemDTO.setId(invOpenningBalanceItemDetail.getBalanceItemId().getId());
            invOpenningBalanceItemDetailDTO.setBalanceItemId(invOpenningBalanceItemDTO);
        }

        if (invOpenningBalanceItemDetail.getUnitId() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(invOpenningBalanceItemDetail.getUnitId().getId());
            symbolDTO.setName(invOpenningBalanceItemDetail.getUnitId().getName());
            invOpenningBalanceItemDetailDTO.setUnitId(symbolDTO);
        }

        if (invOpenningBalanceItemDetail.getItemId() != null) {
            InvItemDTO invItemDTO = new InvItemDTO();
            invItemDTO.setId(invOpenningBalanceItemDetail.getItemId().getId());
            invItemDTO.setName(invOpenningBalanceItemDetail.getItemId().getName());
            if (invOpenningBalanceItemDetail.getItemId().getUnitId() != null) {
                SymbolDTO symbolDTO = new SymbolDTO();
                symbolDTO.setId(invOpenningBalanceItemDetail.getItemId().getUnitId().getId());
                symbolDTO.setName(invOpenningBalanceItemDetail.getItemId().getUnitId().getName());
                invItemDTO.setUnitId(symbolDTO);
            }
            invOpenningBalanceItemDetailDTO.setItemId(invItemDTO);

        }
        if (invOpenningBalanceItemDetail.getItemId().getUnitId() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(invOpenningBalanceItemDetail.getItemId().getUnitId().getId());
            symbolDTO.setName(invOpenningBalanceItemDetail.getItemId().getUnitId().getName());
            invOpenningBalanceItemDetailDTO.setUnitId(symbolDTO);
        }

        invOpenningBalanceItemDetailDTO.setCreatedBy(invOpenningBalanceItemDetail.getCreatedBy() != null ? invOpenningBalanceItemDetail.getCreatedBy().getId() : null);
        invOpenningBalanceItemDetailDTO.setCreatedDate(invOpenningBalanceItemDetail.getCreationDate());
        invOpenningBalanceItemDetailDTO.setBranchId(invOpenningBalanceItemDetail.getBranchId().getId() != null ? invOpenningBalanceItemDetail.getBranchId().getId() : null);
        invOpenningBalanceItemDetailDTO.setCompanyId(invOpenningBalanceItemDetail.getCompanyId() != null ? invOpenningBalanceItemDetail.getCompanyId().getId() : null);

        return invOpenningBalanceItemDetailDTO;
    }

    public InvOpenningBalanceItemDetail mapToEntity(InvOpenningBalanceItemDetailDTO invOpenningBalanceItemDetailDTO, TobyUser tobyUser) {
        InvOpenningBalanceItemDetail invOpenningBalanceItemDetail = new InvOpenningBalanceItemDetail();
        invOpenningBalanceItemDetail.setId(invOpenningBalanceItemDetailDTO.getId());
        invOpenningBalanceItemDetail.setNet(invOpenningBalanceItemDetailDTO.getNet());
        invOpenningBalanceItemDetail.setQuantity(invOpenningBalanceItemDetailDTO.getQuantity());
        invOpenningBalanceItemDetail.setScrewing(invOpenningBalanceItemDetailDTO.getScrewing());
        invOpenningBalanceItemDetail.setSerial(invOpenningBalanceItemDetailDTO.getSerial());
        invOpenningBalanceItemDetail.setCost(invOpenningBalanceItemDetailDTO.getCost());
        if (invOpenningBalanceItemDetailDTO.getBalanceItemId() != null) {
            InvOpenningBalanceItem invOpenningBalanceItem = new InvOpenningBalanceItem();
            invOpenningBalanceItem.setId(invOpenningBalanceItemDetailDTO.getBalanceItemId().getId());
            invOpenningBalanceItemDetail.setBalanceItemId(invOpenningBalanceItem);
        }
        if (invOpenningBalanceItemDetailDTO.getUnitId() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invOpenningBalanceItemDetailDTO.getUnitId().getId());
            symbol.setName(invOpenningBalanceItemDetailDTO.getUnitId().getName());
            invOpenningBalanceItemDetail.setUnitId(symbol);
        }

        if (invOpenningBalanceItemDetailDTO.getItemId() != null) {
            InvItem invItem = new InvItem();
            invItem.setId(invOpenningBalanceItemDetailDTO.getItemId().getId());
            invItem.setName(invOpenningBalanceItemDetailDTO.getItemId().getName());
            invOpenningBalanceItemDetail.setItemId(invItem);

        }
        if (invOpenningBalanceItemDetailDTO.getItemId().getUnitId() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invOpenningBalanceItemDetailDTO.getItemId().getUnitId().getId());
            symbol.setName(invOpenningBalanceItemDetailDTO.getItemId().getUnitId().getName());
            invOpenningBalanceItemDetail.setUnitId(symbol);
        }

        if (invOpenningBalanceItemDetailDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(invOpenningBalanceItemDetailDTO.getCreatedBy());
            invOpenningBalanceItemDetail.setCreatedBy(user);
            invOpenningBalanceItemDetail.setCreationDate(invOpenningBalanceItemDetailDTO.getCreatedDate());
            invOpenningBalanceItemDetail.setModifiedBy(tobyUser);
            invOpenningBalanceItemDetail.setModificationDate(new Date());
            if (invOpenningBalanceItemDetailDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(invOpenningBalanceItemDetailDTO.getCompanyId());
                invOpenningBalanceItemDetail.setCompanyId(company);
            }

            if (invOpenningBalanceItemDetailDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(invOpenningBalanceItemDetailDTO.getBranchId());
                invOpenningBalanceItemDetail.setBranchId(branch);
            }
        } else {
            invOpenningBalanceItemDetail.setCreatedBy(tobyUser);
            invOpenningBalanceItemDetail.setCreationDate(new Date());
            invOpenningBalanceItemDetail.setCompanyId(tobyUser.getCompanyId());
            invOpenningBalanceItemDetail.setBranchId(tobyUser.getBranchId());
        }

        return invOpenningBalanceItemDetail;
    }

    @Override
    public List<InvOpenningBalanceItemDetailDTO> saveInvOpenningBalanceItemDetailDTO(List<InvOpenningBalanceItemDetailDTO> invOpenningBalanceItemDetailDTOList, Integer invOpenningBalanceItemDetailDTOId, TobyUser tobyUser) {
        List<InvOpenningBalanceItemDetailDTO> detailDTOs = new ArrayList<>();

        for (InvOpenningBalanceItemDetailDTO invOpenningBalanceItemDetailDTO : invOpenningBalanceItemDetailDTOList) {
            InvOpenningBalanceItemDTO invOpenningBalanceItemDTO = new InvOpenningBalanceItemDTO();
            invOpenningBalanceItemDTO.setId(invOpenningBalanceItemDetailDTOId);
            invOpenningBalanceItemDetailDTO.setBalanceItemId(invOpenningBalanceItemDTO);
            InvOpenningBalanceItemDetail invOpenningBalanceItemDetail = mapToEntity(invOpenningBalanceItemDetailDTO, tobyUser);
            invOpenningBalanceItemDetail = dao.updateEntity(invOpenningBalanceItemDetail);
            detailDTOs.add(mapToDTO(invOpenningBalanceItemDetail, tobyUser));
        }

        return detailDTOs;
    }
    
    @Override
    public List<Object[]> getInvOpeningBalanceItemDetailsByBranchId(Integer branchId,List<Integer> itemIdList) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("itemIdList", itemIdList);
        List<Object[]> balanceItemDetailList = dao.findListByQuery("SELECT i.itemId.id , i.cost  FROM InvOpenningBalanceItemDetail i WHERE i.branchId.id = :branchId and i.itemId.id in :itemIdList", params);
        return balanceItemDetailList;
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
    public List<InvDetailDTO> getAllInvOpenningBalanceItemDetailByInvPurchaseInvoiceIdDTO(InvInvoiceDTO invInvoiceDTO, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("balanceItemId", invInvoiceDTO.getId());
        List<InvOpenningBalanceItemDetail> details = dao.findListByQuery("SELECT e FROM InvOpenningBalanceItemDetail e WHERE e.balanceItemId.id = :balanceItemId", params);
        return returnListDTO(details, branchId, invInvoiceDTO);
    }

    @Override
    public String addInvOpenningBalanceItemDetailDTO(List<InvDetailDTO> invPurchaseInvoiceDetailList, InvInvoiceDTO invInvoiceDTO,ScreenNameClassEnum screenNameClassEnum,  TobyUser tobyUser) {
        String error = null;
        if (invPurchaseInvoiceDetailList != null && !invPurchaseInvoiceDetailList.isEmpty()) {
            error = invGlobalService.validateSave(invPurchaseInvoiceDetailList, invInvoiceDTO.getInvInventoryId().getId(), screenNameClassEnum, 
                    invInvoiceDTO.getInventorySetupDTO().getNegativeSell(),(invInvoiceDTO.getLoadedSerial() != null),invInvoiceDTO.getInventorySetupDTO().getSellBuy(),CompanyActivityClassEnum.RetailSaleForClothFactory);
            if (error != null && !error.isEmpty()) {
                return error;
            }
            InvOpenningBalanceItemDetail detail;
            for (InvDetailDTO detailDTO : invPurchaseInvoiceDetailList) {

                detail = mapToEntity(detailDTO, invInvoiceDTO.getId(), tobyUser);
                dao.updateEntity(detail);
            }
        }

        return error;
    }
    
    private InvOpenningBalanceItemDetail mapToEntity(InvDetailDTO dTO, Integer balanceItemId, TobyUser tobyUser) {
        InvOpenningBalanceItemDetail entity = new InvOpenningBalanceItemDetail();

        entity.setSerial(dTO.getSerial());
        entity.setCost(dTO.getCost());
        entity.setQuantity(dTO.getQuantity());
        entity.setNet(dTO.getNet());
        entity.setScrewing(dTO.getScrewing());
        
        InvOpenningBalanceItem invOpenningBalanceItem = new InvOpenningBalanceItem();
        invOpenningBalanceItem.setId(balanceItemId);
        entity.setBalanceItemId(invOpenningBalanceItem);


        if (dTO.getItemsBarcodesDetail() != null || dTO.getItemsBarcodesDetailTrans() != null) {
            InvItem invItem = new InvItem();
            invItem.setId(dTO.getItemsBarcodesDetail() != null ? dTO.getItemsBarcodesDetail().getItemId() : dTO.getItemsBarcodesDetailTrans().getItemId());
            entity.setItemId(invItem);
            entity.setItemBarcode(dTO.getItemsBarcodesDetail() != null ? dTO.getItemsBarcodesDetail().getBarcode() : dTO.getItemsBarcodesDetailTrans().getBarcode());
        }

        
        if (dTO.getUnitId() != null) {
            Symbol unitDTO = new Symbol();
            unitDTO.setId(dTO.getUnitId().getId());
            entity.setUnitId(unitDTO);
        }
        
        
        
        
        if (dTO.getId() == null) {
            if (entity.getSerial() == null) {
                Map<String, Object> params = new HashMap<>();
                params.put("balanceItemId", balanceItemId);
                Integer max = genericService.getMaxGenericSerialForDetail(InvOpenningBalanceItemDetail.class, "balanceItemId", balanceItemId);
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
        entity.setBranchId(tobyUser.getBranchId());
        return entity;
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
                        null, entity.getQuantity(), null, (entity.getBalanceItemId() != null ? entity.getBalanceItemId().getId() : null),
                        null, entity.getNet(), entity.getId(), entity.getCreationDate(), entity.getCreatedBy().getId(),
                        null, null,
                        null,
                        null, null,
                        entity.getItemBarcode(), entity.getScrewing(), entity.getItemId().getId(), null,
                        entity.getUnitId().getId(), entity.getUnitId().getSerial(), entity.getUnitId().getName(),
                        entity.getItemId().getIsInventoryItem(),
                        null, null, null, entity.getItemId(),
                        invInvoiceDTO.getCompanyActivity(), invInvoiceDTO.getScreenName(), null,BigDecimal.ZERO,null);        
        dTO.setRealQuantity(dTO.getQuantity().multiply(dTO.getScrewing()));
        return dTO;
    }
    

}
