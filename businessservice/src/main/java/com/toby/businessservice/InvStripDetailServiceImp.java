package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.InvStripDTO;
import com.toby.dto.InvStripDetailDTO;
import com.toby.dto.QuantityItemsStoreDTO;
import com.toby.dto.SymbolDTO;
import com.toby.entity.InvItem;
import com.toby.entity.InvStrip;
import com.toby.entity.InvStripDetail;
import com.toby.entity.TobyCompany;
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
public class InvStripDetailServiceImp implements InvStripDetailService {

    @EJB
    private GenericDAO dao;
    @EJB
    GenericService genericService;
    @EJB
    InvPurchaseInvoiceDetailsService invPurchaseInvoiceDetailsService;

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

    public InvDetailDTO mapToDTOInvDetail(InvStripDetail entity, Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap) {

        InvDetailDTO dto = new InvDetailDTO();

        dto.setId(entity.getId());
        dto.setSerial(entity.getSerial());

        ItemsBarcodesDetailsView barcodesDetailsView = itemsBarcodeMap.get(entity.getItemBarcode());
        dto.setItemsBarcodesDetail(barcodesDetailsView);
        dto.setItemsBarcodesDetailTrans(dto.getItemsBarcodesDetail());
        dto.setItemName(entity.getInvItemId().getName());
        dto.setItemCode(entity.getInvItemId().getCode());

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
            dto.setItemId(invItemDTO);

        }

        dto.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dto.setCreatedDate(entity.getCreationDate());

        dto.setCompanyId(entity.getCompanyId() != null ? entity.getCompanyId().getId() : null);

        return dto;
    }

    public InvStripDetailDTO mapToDTO(InvStripDetail invStripDetail) {

        InvStripDetailDTO invStripDetailDTO = new InvStripDetailDTO();

        invStripDetailDTO.setId(invStripDetail.getId());
        invStripDetailDTO.setActualAmount(invStripDetail.getActualAmount());
        invStripDetailDTO.setBookBalance(invStripDetail.getBookBalance());
        invStripDetailDTO.setDifference(invStripDetail.getDifference());
        invStripDetailDTO.setIndex(invStripDetail.getId());
        invStripDetailDTO.setSerial(invStripDetail.getSerial());
        if (invStripDetail.getInvStripId() != null) {
            InvStripDTO invStripDTO = new InvStripDTO();
            invStripDTO.setId(invStripDetail.getInvStripId().getId());
            invStripDetailDTO.setInvStripId(invStripDTO);
        }

        if (invStripDetail.getInvItemId() != null) {
            InvItemDTO invItemDTO = new InvItemDTO();
            invItemDTO.setId(invStripDetail.getInvItemId().getId());
            invItemDTO.setName(invStripDetail.getInvItemId().getName());
            invItemDTO.setCode(invStripDetail.getInvItemId().getCode());
            invStripDetailDTO.setInvItemId(invItemDTO);

        }

        invStripDetailDTO.setCreatedBy(invStripDetail.getCreatedBy() != null ? invStripDetail.getCreatedBy().getId() : null);
        invStripDetailDTO.setCreatedDate(invStripDetail.getCreationDate());

        invStripDetailDTO.setCompanyId(invStripDetail.getCompanyId() != null ? invStripDetail.getCompanyId().getId() : null);

        return invStripDetailDTO;
    }

    public InvStripDetail mapToEntity(InvStripDetailDTO invStripDetailDTO, TobyUser tobyUser) {

        InvStripDetail invStripDetail = new InvStripDetail();

        invStripDetail.setId(invStripDetailDTO.getId());
        invStripDetail.setActualAmount(invStripDetailDTO.getActualAmount());
        invStripDetail.setBookBalance(invStripDetailDTO.getBookBalance());
        invStripDetail.setDifference(invStripDetailDTO.getDifference());
        invStripDetail.setSerial(invStripDetailDTO.getSerial());
        if (invStripDetailDTO.getInvStripId() != null) {
            InvStrip invStrip = new InvStrip();
            invStrip.setId(invStripDetailDTO.getInvStripId().getId());
            invStripDetail.setInvStripId(invStrip);
        }

        if (invStripDetailDTO.getInvItemId() != null) {
            InvItem invItem = new InvItem();
            invItem.setId(invStripDetailDTO.getInvItemId().getId());
            invItem.setName(invStripDetailDTO.getInvItemId().getName());
            invItem.setCode(invStripDetailDTO.getInvItemId().getCode());
            invStripDetail.setInvItemId(invItem);
        }

        if (invStripDetailDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(invStripDetailDTO.getCreatedBy());
            invStripDetail.setCreatedBy(user);
            invStripDetail.setCreationDate(invStripDetailDTO.getCreatedDate());
            invStripDetail.setModifiedBy(tobyUser);
            invStripDetail.setModificationDate(new Date());
            if (invStripDetailDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(invStripDetailDTO.getCompanyId());
                invStripDetail.setCompanyId(company);
            }

        } else {
            invStripDetail.setCreatedBy(tobyUser);
            invStripDetail.setCreationDate(new Date());
            invStripDetail.setCompanyId(tobyUser.getCompanyId());

        }
        return invStripDetail;
    }

    public List<InvDetailDTO> mapToDTOListInvDetail(List<InvStripDetail> invStripDetailList, TobyUser tobyUser) {
        List<InvDetailDTO> invStripDetailDTOList = new ArrayList<>();
        Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap = new HashMap<>();//        }
        List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList = findItemBarcodeDetails(invStripDetailList, tobyUser.getBranchId().getId());

        for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailsViewList) {
            itemsBarcodeMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
        }

        for (InvStripDetail invStripDetail : invStripDetailList) {
            invStripDetailDTOList.add(mapToDTOInvDetail(invStripDetail, itemsBarcodeMap));
        }
        return invStripDetailDTOList;
    }

    public List<InvStripDetailDTO> mapToDTOList(List<InvStripDetail> invStripDetailList, TobyUser tobyUser) {
        List<InvStripDetailDTO> invStripDetailDTOList = new ArrayList<>();
        for (InvStripDetail invStripDetail : invStripDetailList) {
            invStripDetailDTOList.add(mapToDTO(invStripDetail));
        }
        return invStripDetailDTOList;
    }

    @Override
    public List<InvStripDetailDTO> mapToDTOList1(List<QuantityItemsStoreDTO> quantityItemsStoreList, InvStripDTO invStrip, TobyUser tobyUser) {
        List<InvStripDetailDTO> invStripDetailDTOList = new ArrayList<>();
        for (QuantityItemsStoreDTO quantityItemsStoreDTO : quantityItemsStoreList) {

            InvStripDetailDTO detailDTO = mapToDTO1(quantityItemsStoreDTO, invStrip);
            invStripDetailDTOList.add(detailDTO);
        }
        return invStripDetailDTOList;
    }

    public InvStripDetailDTO mapToDTO1(QuantityItemsStoreDTO quantityItemsStoreDTO, InvStripDTO invStrip) {
        InvStripDetailDTO invStripDetailDTO = new InvStripDetailDTO();
        invStripDetailDTO.setActualAmount(BigDecimal.ZERO);
        invStripDetailDTO.setBookBalance(quantityItemsStoreDTO.getQty());
        invStripDetailDTO.setDifference(invStripDetailDTO.getActualAmount().subtract(invStripDetailDTO.getBookBalance()));

        InvItemDTO invItemDTO = new InvItemDTO();
        invItemDTO.setCode(quantityItemsStoreDTO.getItemCode());
        invItemDTO.setName(quantityItemsStoreDTO.getItemName());
        invItemDTO.setId(quantityItemsStoreDTO.getId());
        invStripDetailDTO.setInvItemId(invItemDTO);
        updateSummition(invStripDetailDTO, invStrip);
        return invStripDetailDTO;
    }

    @Override
    public InvStripDTO updateSummition(InvStripDetailDTO detailEntity, InvStripDTO invStrip) {
        invStrip.getTotalQuantity().add(detailEntity.getActualAmount() != null ? detailEntity.getActualAmount() : BigDecimal.ZERO);
        invStrip.getTotalBalance().add(detailEntity.getBookBalance() != null ? detailEntity.getBookBalance() : BigDecimal.ZERO);
        return invStrip;
    }

    @Override
    public List<InvStripDetailDTO> getAllInvStripDetailsDTO(Integer id, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap();
        params.put("invStrip", id);
        List<InvStripDetail> details = dao.findListByQuery("SELECT d FROM InvStripDetail d WHERE d.invStripId.id =:invStrip", params);
        return mapToDTOList(details, tobyUser);

    }

    @Override
    public List<InvDetailDTO> getAllInvStripDetailsDTOASINVDEtail(Integer id, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap();
        params.put("invStrip", id);
        List<InvStripDetail> details = dao.findListByQuery("SELECT d FROM InvStripDetail d WHERE d.invStripId.id =:invStrip", params);
        return mapToDTOListInvDetail(details, tobyUser);

    }

    @Override
    public void deleteInvStripDetailDTO(InvDetailDTO invDetailDTO) {
        if (invDetailDTO != null && invDetailDTO.getId() != null) {
            InvStripDetail detail = new InvStripDetail();
            detail.setId(invDetailDTO.getId());
            dao.deleteEntity(detail);
        }

    }

    @Override
    public InvDetailDTO addInvStripDetailsDTONew(InvDetailDTO invDetailDTO, Integer InvStripDetailId, TobyUser tobyUser) {
        StringBuilder error = null;
        if (invDetailDTO != null && InvStripDetailId != null) {
            if (invDetailDTO.getItemsBarcodesDetail() == null && invDetailDTO.getItemsBarcodesDetailTrans() != null) {
                invDetailDTO.setItemsBarcodesDetail(invDetailDTO.getItemsBarcodesDetailTrans());
            } else if (invDetailDTO.getItemsBarcodesDetail() != null && invDetailDTO.getItemsBarcodesDetailTrans() == null) {
                invDetailDTO.setItemsBarcodesDetailTrans(invDetailDTO.getItemsBarcodesDetail());
            } else if (invDetailDTO.getItemsBarcodesDetail() == null && invDetailDTO.getItemsBarcodesDetailTrans() == null) {
                if (error == null) {
                    error = new StringBuilder();
                }
                error.append("\n يجب ادخال الصنف");
            }

            if (error != null && !error.toString().isEmpty()) {
                invDetailDTO.setMsg(error.toString());
                return invDetailDTO;
            }
            InvStripDetail detail;

            detail = mapToEntity(invDetailDTO, InvStripDetailId, tobyUser);
            detail = dao.updateEntity(detail);
            invDetailDTO.setId(detail.getId());
            invDetailDTO.setSerial(detail.getSerial());
        }

        return invDetailDTO;
    }

    private InvStripDetail mapToEntity(InvDetailDTO dTO, Integer InvStripDetailId, TobyUser tobyUser) {
        InvStripDetail entity = new InvStripDetail();

        entity.setId(dTO.getId());
        entity.setActualAmount(dTO.getActualQuantity());
        entity.setBookBalance(dTO.getBookBalance());
        entity.setDifference(dTO.getActualQuantity().subtract(dTO.getBookBalance()));
        entity.setWasalate(dTO.getWasalate());

        InvStrip invStrip = new InvStrip();
        invStrip.setId(InvStripDetailId);
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
                params.put("invStripId", InvStripDetailId);
                Integer max = genericService.getMaxGenericSerialForDetail(InvStripDetail.class, "invStripId", InvStripDetailId);
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

    public List<ItemsBarcodesDetailsView> findItemBarcodeDetails(List<InvStripDetail> list, Integer branchId) {
        List<String> barcodeList = new ArrayList<>();
        for (InvStripDetail entity : list) {
            barcodeList.add(entity.getItemBarcode());
        }
        List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList = findItemsBarcodesDetailsViewBranchIdByBarcode(branchId, barcodeList);
        return itemsBarcodesDetailsViewList;
    }

    public List<ItemsBarcodesDetailsView> findItemsBarcodesDetailsViewBranchIdByBarcode(Integer branchId, List<String> barcodeList) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<ItemsBarcodesDetailsView> list = new ArrayList<>();
        StringBuilder query1 = new StringBuilder();
        query1.append(" select i from InvItem i where i.branchId.id = :branchId  ");
        if (barcodeList != null && !barcodeList.isEmpty()) {
            params.put("barcode", barcodeList);
            query1.append(" AND i.code in   :barcode ");
        }
        List<InvItem> queryList = dao.findListByQuery(query1.toString(), params);

        for (InvItem invItem : queryList) {
            ItemsBarcodesDetailsView itemsBarcodesDetailsView = new ItemsBarcodesDetailsView();
            itemsBarcodesDetailsView.setBarcode(invItem.getCode());
            itemsBarcodesDetailsView.setItemId(invItem.getId());
            itemsBarcodesDetailsView.setCostAverage(invItem.getCostAverage());
            itemsBarcodesDetailsView.setName(invItem.getName());
            itemsBarcodesDetailsView.setNameEn(invItem.getNameen());
            itemsBarcodesDetailsView.setSellPrice(invItem.getSellPrice());
            itemsBarcodesDetailsView.setUnitId(invItem.getUnitId() == null ? null : invItem.getUnitId().getId());
            itemsBarcodesDetailsView.setUnitName(invItem.getUnitId().getName());
            itemsBarcodesDetailsView.setProGroupId(invItem.getProGroupId() == null ? null : invItem.getProGroupId().getId());
            itemsBarcodesDetailsView.setTypeShow(invItem.getItemNatural());
            list.add(itemsBarcodesDetailsView);
        }

        return list;
    }

}
