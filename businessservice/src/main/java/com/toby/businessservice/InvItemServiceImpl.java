/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.ItemMainDataByGroupSearchBean;
import com.toby.core.GenericDAO;
import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.CurrencyDTO;
import com.toby.dto.InvGroupDTO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.SymbolDTO;
import com.toby.entity.Branch;
import com.toby.entity.InvBarcode;
import com.toby.entity.InvGroup;
import com.toby.entity.InvItem;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.ProGroup;
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
 * @author H
 */
@Stateless
public class InvItemServiceImpl implements InvItemService {

    @EJB
    private GenericDAO dao;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private CurrencyServiceDTO currencyServiceDTO;
    @EJB
    private InvOpeningBalanceItemDetailService invOpeningBalanceItemDetailService;

    StringBuilder appendQuery;

    //mapToEntity
    public InvItem mapToEntity(InvItemDTO invItemDTO, TobyUser tobyUser) {

        InvItem invItem = new InvItem();
        invItem.setCode(invItemDTO.getCode());
        invItem.setId(invItemDTO.getId());
        invItem.setCode(invItemDTO.getCode());
        invItem.setName(invItemDTO.getName());
        invItem.setCode(invItemDTO.getCode());
        invItem.setTypeCat(invItemDTO.getTypeCat());
        invItem.setCostAverage(invItemDTO.getCostAverage());
        invItem.setContractPrice(invItemDTO.getContractPrice());
        invItem.setLastCost(invItemDTO.getLastCost());
        invItem.setMaxmumAmount(invItemDTO.getMaxmumAmount());
        invItem.setMinimumAmount(invItemDTO.getMinimumAmount());
        invItem.setWeight(invItemDTO.getWeight());
        invItem.setSellPrice(invItemDTO.getSellPrice());
        ////////////////////////////////

        invItem.setIspurchase(invItemDTO.getIspurchase());
        invItem.setIssell(invItemDTO.getIssell());
        invItem.setLastCost(invItemDTO.getLastCost() != null ? invItemDTO.getLastCost() : BigDecimal.ZERO);
        invItem.setMarkEdit(invItemDTO.getMarkEdit());
        invItem.setMaxmumAmount(invItemDTO.getMaxmumAmount());
        invItem.setMinimumAmount(invItemDTO.getMinimumAmount());
        invItem.setName(invItemDTO.getName());
        invItem.setNameen(invItemDTO.getNameen());
        invItem.setNickname(invItemDTO.getNickname());
        invItem.setOpeningCost(invItemDTO.getOpeningCost());
        invItem.setWeightPackage(invItemDTO.getWeightPackage());
        invItem.setRemarks(invItemDTO.getRemarks());
        invItem.setSellPrice(invItemDTO.getSellPrice());
        invItem.setStatusCat(invItemDTO.getStatusCat());
        invItem.setStorageLocation(invItemDTO.getStorageLocation());
        invItem.setStoresQuality(invItemDTO.getStoresQuality());
        invItem.setTypeCat(invItemDTO.getTypeCat());
        invItem.setUndirectCost(invItemDTO.getUndirectCost());
        invItem.setSiteId(invItemDTO.getSiteId());
        invItem.setLength(invItemDTO.getLength());
        invItem.setHeight(invItemDTO.getHeight());
        invItem.setWeight(invItemDTO.getWeight());
        invItem.setWidth(invItemDTO.getWidth());
        invItem.setDiscountrate(invItemDTO.getDiscountrate());
        invItem.setDiscountvalue(invItemDTO.getDiscountvalue());
        invItem.setMaxpriceyoung(invItemDTO.getMaxpriceyoung());
        invItem.setMaxpricemen(invItemDTO.getMaxpricemen());
        invItem.setMinpriceyoung(invItemDTO.getMinpriceyoung());
        invItem.setMinpricemen(invItemDTO.getMinpricemen());
        invItem.setNumbermetersfreeyoung(invItemDTO.getNumbermetersyoung());
        invItem.setNumbermetersfreemen(invItemDTO.getNumbermetersfreemen());
        invItem.setBounsepriceyoung(invItemDTO.getBounsepriceyoung());
        invItem.setBounsepricemen(invItemDTO.getBounsepricemen());
        invItem.setCommissionrate(invItemDTO.getCommissionrate());
        invItem.setCommissionvalue(invItemDTO.getCommissionvalue());
        invItem.setPrice_edit(invItemDTO.getPrice_edit());
        invItem.setIsInventoryItem(invItemDTO.getIsinventoryitem());
        invItem.setTypeshow(invItemDTO.getTypeshow());
//        invItem.setItemNatural(invItemDTO.getItem_natural());
        if (invItemDTO.getOriginCountry() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invItemDTO.getGroupId().getId());
            symbol.setName(invItemDTO.getGroupId().getName());
            invItem.setOriginCountry(symbol);
        }
        if (invItemDTO.getPaintColor() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invItem.getGroupId().getId());
            symbol.setName(invItem.getGroupId().getName());
            invItem.setPaintColor(symbol);
        }
        if (invItemDTO.getStone() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invItem.getGroupId().getId());
            symbol.setName(invItem.getGroupId().getName());
            invItem.setStone(symbol);
        }
        if (invItemDTO.getAddon1() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invItem.getGroupId().getId());
            symbol.setName(invItem.getGroupId().getName());
            invItem.setAddon1(symbol);
        }
        if (invItemDTO.getAddon2() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invItem.getGroupId().getId());
            symbol.setName(invItem.getGroupId().getName());
            invItem.setAddon2(symbol);
        }
        if (invItemDTO.getGroupId() != null) {
            InvGroup invGroup = new InvGroup();
            invGroup.setId(invItem.getGroupId().getId());
            invGroup.setName(invItem.getGroupId().getName());
            invItem.setGroupId(invGroup);
        }

        ///////////////////////////////////////
        if (invItemDTO.getUnitId() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invItemDTO.getUnitId().getId());
            symbol.setName(invItemDTO.getUnitId().getName());
            invItem.setUnitId(symbol);
        }
        if (invItemDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(invItemDTO.getCreatedBy());
            invItem.setCreatedBy(user);
            invItem.setCreationDate(invItemDTO.getCreatedDate());
            invItem.setModifiedBy(tobyUser);
            invItem.setModificationDate(new Date());
            if (invItemDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(invItemDTO.getCompanyId());
                invItem.setCompanyId(company);
            }

            if (invItem.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(invItemDTO.getBranchId());
                invItemDTO.setBranchId(branch.getId());
            }
        } else {
            invItem.setCreatedBy(tobyUser);
            invItem.setCreationDate(new Date());
            invItem.setCompanyId(tobyUser.getCompanyId());
            invItem.setBranchId(tobyUser.getBranchId());
        }

        return invItem;
    }

    public InvItemDTO mapToDTO(InvItem invItem, TobyUser tobyUser) {

        InvItemDTO invItemDTO = new InvItemDTO();
        invItemDTO.setCreatedBy(invItem.getCreatedBy() != null ? invItem.getCreatedBy().getId() : null);
        invItemDTO.setCreatedDate(invItem.getCreationDate());
        invItemDTO.setBranchId(invItem.getBranchId() != null ? invItem.getBranchId().getId() : null);
        invItemDTO.setId(invItem.getId());
        invItemDTO.setCompanyId(invItem.getCompanyId() != null ? invItem.getCompanyId().getId() : null);
        invItemDTO.setTypeCat(invItem.getTypeCat());
        invItemDTO.setName(invItem.getName());
        invItemDTO.setCode(invItem.getCode());
        invItemDTO.setSellPrice(invItem.getSellPrice());
        invItemDTO.setMsg(invItem.getMsg());
        invItemDTO.setIspurchase(invItem.getIspurchase());
        invItemDTO.setIssell(invItem.getIssell());
        invItemDTO.setLastCost(invItem.getLastCost() != null ? invItem.getLastCost() : BigDecimal.ZERO);
        invItemDTO.setMarkEdit(invItem.getMarkEdit());
        invItemDTO.setMaxmumAmount(invItem.getMaxmumAmount());
        invItemDTO.setMinimumAmount(invItem.getMinimumAmount());
        invItemDTO.setName(invItem.getName());
        invItemDTO.setNameen(invItem.getNameen());
        invItemDTO.setNickname(invItem.getNickname());
        invItemDTO.setOpeningCost(invItem.getOpeningCost());
        invItemDTO.setWeightPackage(invItem.getWeightPackage());
        invItemDTO.setRemarks(invItem.getRemarks());
        invItemDTO.setSellPrice(invItem.getSellPrice());
        invItemDTO.setStatusCat(invItem.getStatusCat());
        invItemDTO.setStorageLocation(invItem.getStorageLocation());
        invItemDTO.setStoresQuality(invItem.getStoresQuality());
        invItemDTO.setTypeCat(invItem.getTypeCat());
        invItemDTO.setUndirectCost(invItem.getUndirectCost());
        invItemDTO.setSiteId(invItem.getSiteId());
        invItemDTO.setLength(invItem.getLength());
        invItemDTO.setHeight(invItem.getHeight());
        invItemDTO.setWeight(invItem.getWeight());
        invItemDTO.setWidth(invItem.getWidth());
        invItemDTO.setDiscountrate(invItem.getDiscountrate());
        invItemDTO.setDiscountvalue(invItem.getDiscountvalue());
        invItemDTO.setMaxpriceyoung(invItem.getMaxpriceyoung());
        invItemDTO.setMaxpricemen(invItem.getMaxpricemen());
        invItemDTO.setMinpriceyoung(invItem.getMinpriceyoung());
        invItemDTO.setMinpricemen(invItem.getMinpricemen());
        invItemDTO.setNumbermetersfreeyoung(invItem.getNumbermetersyoung());
        invItemDTO.setNumbermetersfreemen(invItem.getNumbermetersfreemen());
        invItemDTO.setBounsepriceyoung(invItem.getBounsepriceyoung());
        invItemDTO.setBounsepricemen(invItem.getBounsepricemen());
        invItemDTO.setCommissionrate(invItem.getCommissionrate());
        invItemDTO.setCommissionvalue(invItem.getCommissionvalue());
        invItemDTO.setPrice_edit(invItem.getPrice_edit());
        invItemDTO.setIsinventoryitem(invItem.getIsInventoryItem());
        invItemDTO.setTypeshow(invItem.getTypeshow());
        invItemDTO.setCostAverage(invItem.getCostAverage());

//        invItemDTO.setItem_natural(invItem.getItemNatural());
        if (invItem.getOriginCountry() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(invItem.getGroupId().getId());
            symbolDTO.setName(invItem.getGroupId().getName());
            invItemDTO.setOriginCountry(symbolDTO);
        }
        if (invItem.getPaintColor() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(invItem.getGroupId().getId());
            symbolDTO.setName(invItem.getGroupId().getName());
            invItemDTO.setPaintColor(symbolDTO);
        }
        if (invItem.getStone() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(invItem.getGroupId().getId());
            symbolDTO.setName(invItem.getGroupId().getName());
            invItemDTO.setStone(symbolDTO);
        }
        if (invItem.getAddon1() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(invItem.getGroupId().getId());
            symbolDTO.setName(invItem.getGroupId().getName());
            invItemDTO.setAddon1(symbolDTO);
        }
        if (invItem.getAddon2() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(invItem.getGroupId().getId());
            symbolDTO.setName(invItem.getGroupId().getName());
            invItemDTO.setAddon2(symbolDTO);
        }
        if (invItem.getGroupId() != null) {
            InvGroupDTO invGroupDTO = new InvGroupDTO();
            invGroupDTO.setId(invItem.getGroupId().getId());
            invGroupDTO.setName(invItem.getGroupId().getName());
            invItemDTO.setGroupId(invGroupDTO);

        }

        ////////////////////////////////////////
        if (invItem.getUnitId() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(invItem.getUnitId().getId());
            symbolDTO.setName(invItem.getUnitId().getName());
            invItemDTO.setUnitId(symbolDTO);
        }

        return invItemDTO;
    }

    public List<InvItemDTO> mapToDTOList(List<InvItem> invItemList, TobyUser tobyUser) {
        List<InvItemDTO> invItemDTOList = new ArrayList<>();
        for (InvItem invItem : invItemList) {
            invItemDTOList.add(mapToDTO(invItem, tobyUser));
        }
        return invItemDTOList;
    }

    @Override
    public List<InvItemDTO> findInvItemDTOList(TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        List<InvItem> invItemList = dao.findListByQuery("SELECT e FROM InvItem e WHERE e.branchId.id = :branchId ", params);
        return mapToDTOList(invItemList, tobyUser);
    }

    @Override
    public Map<String, String> findInvItemDTOMap(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        Map<String, String> list = new HashMap<>();
        params.put("branchId", branchId);
        List<InvItem> invItemList = dao.findListByQuery("SELECT e FROM InvItem e WHERE e.branchId.id = :branchId ", params);
        for (InvItem invItem : invItemList) {
            list.put(invItem.getCode(), invItem.getCode());
        }
        return list;
    }

    public void findInvItemMap(Integer branchId, List<InvItemDTO> invItemDTOList) {
        Map<String, Object> params = new HashMap<>();
        Map<String, InvItem> mapItems = new HashMap<>();
        params.put("branchId", branchId);
        List<InvItem> invItemList = dao.findListByQuery("SELECT e FROM InvItem e WHERE e.branchId.id = :branchId ", params);
        for (InvItem invItem : invItemList) {
            mapItems.put(invItem.getCode(), invItem);
        }

        for (InvItemDTO invItemDTO : invItemDTOList) {
            if (mapItems.containsKey(invItemDTO.getCode())) {
                InvItem invItem = mapItems.get(invItemDTO.getCode());
                ProGroup proGroupId = new ProGroup();
                proGroupId.setId(Integer.parseInt(invItemDTO.getName()));
                invItem.setProGroupId(proGroupId);
                addInvItem(invItem, null, null);
            }
        }
    }

    @Override
    public Map<String, String> findInvItemDTOMapForNewItem(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        Map<String, String> list = new HashMap<>();
        params.put("branchId", branchId);
        List<InvItem> invItemList = dao.findListByQuery("SELECT e FROM InvItem e WHERE e.branchId.id = :branchId AND e.isNew = 1", params);
        for (InvItem invItem : invItemList) {
            list.put(invItem.getCode(), invItem.getCode());
        }
        return list;
    }

    public InvItem addInvItem(InvItem invItem, List<InvBarcode> invBarcodeList, List<InvBarcode> invBarcodeDeletedList) {

        if (invItem.getId() != null) {
            dao.updateEntity(invItem);
        } else {
            dao.saveEntity(invItem);
        }
        return invItem;
    }

    /**
     *
     * @param invItem
     * @return
     */
    @Override
    public synchronized InvItem updateInvItem(InvItem invItem) {
        return dao.updateEntity(invItem);
    }

    @Override
    public void deleteInvItem(InvItem invItem) {
        dao.deleteEntity(invItem);
    }

    @Override
    public void deleteInvItemDTO(InvItemDTO invItem) {
        InvItem item = new InvItem();
        item.setId(invItem.getId());
        dao.deleteEntity(item);
    }

    @Override
    public InvItem findInvItem(Integer invItemById) {
        InvItem invItem = new InvItem();
        invItem = dao.findEntityById(InvItem.class, invItemById);
        return invItem;
    }

    @Override
    public List<InvItem> getInvItemByCompanyId(Integer companyId) {
        return dao.findEntityListByCompanyId(InvItem.class, companyId);
    }

    @Override
    public List<InvItem> getInvItemByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvItem> details = dao.findListByQuery("SELECT e FROM InvItem e WHERE e.branchId.id = :branchId  ORDER BY e.code", params);
        return details;
    }

    @Override
    public List<InvItemDTO> getInvItemDTOByBranchId(Integer branchId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvItem> details = dao.findListByQuery("SELECT e FROM InvItem e WHERE e.branchId.id = :branchId  ORDER BY e.code", params);
        return mapToDTOList(details, tobyUser);
    }

    @Override
    public List<InvItem> getInvItemByBranchIdForInvItemMainDataByGroupReportMB(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvItem> details = dao.findListByQuery("SELECT e FROM InvItem e WHERE e.branchId.id = :branchId  ORDER BY e.code", params);
        List<InvItem> detailsReturn = new ArrayList<>();
        for (InvItem detail : details) {
            InvItem invItem = new InvItem();
            invItem.setId(detail.getId());
            invItem.setCode(detail.getCode());
            invItem.setName(detail.getName());
            detailsReturn.add(invItem);
        }
        return detailsReturn;
    }

    @Override
    public List<InvItem> getInvItemByBranchIdAndGroupId(Integer branchId, Integer groupId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("groupId", groupId);
        List<InvItem> details = dao.findListByQuery("SELECT e FROM InvItem e WHERE e.branchId.id = :branchId AND e.groupId.id = :groupId  ORDER BY e.code", params);
        List<InvItem> detailsReturn = retriveItemData(details);
        return detailsReturn;
    }

    @Override
    public List<InvItem> getImageByCompanyId(String imageName, Integer companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("imageName", imageName);
        params.put("companyId", companyId);
        List<InvItem> invItems = dao.findListByQuery("SELECT i FROM InvItem i WHERE i.companyId.id != :companyId AND i.image = :imageName  ORDER BY e.code", params);

        return invItems;
    }

    @Override
    public List<InvItem> getImageByBranchId(String imageName, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("imageName", imageName);
        params.put("branchId", branchId);
        List<InvItem> invItems = dao.findListByQuery("SELECT i FROM InvItem i WHERE i.branchId.id != :branchId AND i.image = :imageName  ORDER BY i.code", params);

        return invItems;
    }

    @Override
    public List<InvItem> findImageOfBranchId(String imageName, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("imageName", imageName);
        params.put("branchId", branchId);
        List<InvItem> invItems = dao.findListByQuery("SELECT i FROM InvItem i WHERE i.branchId.id = :branchId AND i.image = :imageName  ORDER BY i.code", params);

        return invItems;
    }

    @Override
    public Boolean validateCode(InvItem invItem) {
        Boolean result = true;
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();

        params.put("code", invItem.getCode());
        params.put("branchId", invItem.getBranchId().getId());

        query.append("SELECT i FROM InvItem i WHERE i.branchId.id = :branchId AND i.code = :code ");

        if (invItem.getId() != null) {
            params.put("id", invItem.getId());
            query.append(" AND i.id != :id");
        }

        query.append("  ORDER BY i.code");

        String q = query.toString();

        List<InvItem> invItems = dao.findListByQuery(q, params);

        if (invItems == null || invItems.isEmpty()) {
            return false;
        }

        return result;

    }

    @Override
    public Boolean validateNickName(InvItem invItem) {
        Boolean result = true;
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();

        params.put("nickName", invItem.getNickname());
        params.put("branchId", invItem.getBranchId().getId());

        query.append("SELECT i FROM InvItem i WHERE i.branchId.id = :branchId AND i.nickname = :nickName ");

        if (invItem.getId() != null) {
            params.put("id", invItem.getId());
            query.append(" AND i.id != :id");
        }

        query.append("  ORDER BY i.code");

        String q = query.toString();

        List<InvItem> invItems = dao.findListByQuery(q, params);

        if (invItems == null || invItems.isEmpty()) {
            return false;
        }

        return result;

    }

    @Override
    public Boolean validateCodeFromView(InvItem invItem) {
        Boolean result = true;
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();

        params.put("barcode", invItem.getCode());
        params.put("branchId", invItem.getBranchId().getId());

        query.append("SELECT i FROM ItemsBarcodesDetailsView i WHERE i.branchId = :branchId AND i.barcode = :barcode");

        if (invItem.getId() != null) {
            params.put("id", invItem.getId());
            query.append(" AND i.itemId != :id");
        }

        query.append("  ORDER BY i.barcode");
        String q = query.toString();

        List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViews = dao.findListByQuery(q, params);

        if (itemsBarcodesDetailsViews == null || itemsBarcodesDetailsViews.isEmpty()) {
            return false;
        }

        return result;

    }

    @Override
    public synchronized void updateAllInvItems(List<InvItem> invItemList) {
        for (InvItem invItem : invItemList) {
            updateInvItem(invItem);
        }
    }

    @Override
    public List<InvItem> findInventoryItem(Integer inventoryId) {
        Map<String, Object> params = new HashMap<>();
        params.put("inventoryId", inventoryId);
        List<InvItem> invItems = dao.findListByQuery("SELECT i FROM InvItem i WHERE i.id in ("
                + " select q.itemId from QuantityItemsStore q "
                + "where (q.inventoryId = :inventoryId and q.itemId is not null))   ORDER BY i.code", params);

        return invItems;
    }

    @Override
    public List<InvItemDTO> findInventoryDTOItem(Integer inventoryId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("inventoryId", inventoryId);
        List<InvItem> invItems = dao.findListByQuery("SELECT i FROM InvItem i WHERE i.id in ("
                + " select q.itemId from QuantityItemsStore q "
                + "where (q.inventoryId = :inventoryId and q.itemId is not null))   ORDER BY i.code", params);

        return mapToDTOList(invItems, tobyUser);
    }

    @Override
    public List<InvItemDTO> getInvItemListByBranchId(Integer branchId, ScreenNameClassEnum screenNameClassEnum) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvItem> detailses = dao.findListByQuery("SELECT i FROM InvItem i WHERE i.branchId.id = :branchId   ORDER BY i.code", params);
        List<InvItemDTO> detailsesReturn = retriveItemDTOData(detailses, screenNameClassEnum);
        return detailsesReturn;
    }

    private List<InvItemDTO> retriveItemDTOData(List<InvItem> detailses, ScreenNameClassEnum screenNameClassEnum) {
        List<InvItemDTO> detailsesReturn = new ArrayList<>();
        for (InvItem detail : detailses) {
            InvItemDTO dest = new InvItemDTO();
            if (ScreenNameClassEnum.ITEMCARDREPORT.equals(screenNameClassEnum)) {
                dest.setId(detail.getId());
                dest.setName(detail.getName());
                dest.setCode(detail.getCode());
                InvGroupDTO group = null;
                if (detail.getGroupId() != null) {
                    group = new InvGroupDTO();
                    group.setId(detail.getGroupId().getId());
                    group.setName(detail.getGroupId().getName());
                    group.setCode(detail.getGroupId().getCode());
                    group.setLevel(detail.getGroupId().getLevel());
                }
                dest.setGroupId(group);
            } else if (ScreenNameClassEnum.BASEINVENTORYREPORTBEAN.equals(screenNameClassEnum)
                    || ScreenNameClassEnum.INVOPENINGBALANCEITEMFORM.equals(screenNameClassEnum)) {
                dest.setId(detail.getId());
                dest.setName(detail.getName());
                dest.setCode(detail.getCode());
                InvGroupDTO group = null;
                if (detail.getGroupId() != null) {
                    group = new InvGroupDTO();
                    group.setId(detail.getGroupId().getId());
                    group.setName(detail.getGroupId().getName());
                    group.setCode(detail.getGroupId().getCode());
                    group.setLevel(detail.getGroupId().getLevel());
                }
                dest.setGroupId(group);
                dest.setCostAverage(detail.getCostAverage());
                dest.setSellPrice(detail.getSellPrice());
                SymbolDTO unit = null;
                if (detail.getUnitId() != null) {
                    unit = new SymbolDTO();
                    unit.setId(detail.getUnitId().getId());
                    unit.setName(detail.getUnitId().getName());
                    unit.setSerial(detail.getUnitId().getSerial());
                }
                dest.setUnitId(unit);
            }
            detailsesReturn.add(dest);

//        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
//            if (ScreenNameClassEnum.ITEMCARDREPORT.equals(screenNameClassEnum)) {
//                mapperFactory.classMap(InvItem.class, InvItemDTO.class)
//                        .exclude("msg")
//                        .exclude("companyId")
//                        .exclude("createdBy")
//                        .exclude("modifiedBy")
//                        .exclude("modificationDate")
//                        .exclude("creationDate")
//                        .exclude("typeCat")
//                        .exclude("sellPrice")
//                        .exclude("undirectCost")
//                        .exclude("remarks")
//                        .exclude("statusCat")
//                        .exclude("contractPrice")
//                        .exclude("storageLocation")
//                        .exclude("nickname")
//                        .exclude("minimumAmount")
//                        .exclude("maxmumAmount")
//                        .exclude("lastCost")
//                        .exclude("costAverage")
//                        .exclude("dateCreateCat")
//                        .exclude("openingCost")
//                        .exclude("storesQuality")
//                        .exclude("image")
//                        .exclude("weightPackage")
//                        .exclude("issell")
//                        .exclude("ispurchase")
//                        .exclude("branchId")
//                        .exclude("brandId")
//                        .exclude("enamelColor")
//                        .exclude("groupId")
//                        .exclude("unitId")
//                        .exclude("originCountry")
//                        .exclude("paintColor")
//                        .exclude("author")
//                        .exclude("addon1")
//                        .exclude("addon2")
//                        .exclude("siteId")
//                        .exclude("stone")
//                        .exclude("height")
//                        .exclude("weight")
//                        .exclude("length")
//                        .exclude("width")
//                        .byDefault().register();
//            } else if (ScreenNameClassEnum.BASEINVENTORYREPORTBEAN.equals(screenNameClassEnum)
//                    || ScreenNameClassEnum.INVOPENINGBALANCEITEMFORM.equals(screenNameClassEnum)) {
//                mapperFactory.classMap(InvItem.class, InvItemDTO.class)
//                        .exclude("msg")
//                        .exclude("companyId")
//                        .exclude("createdBy")
//                        .exclude("modifiedBy")
//                        .exclude("modificationDate")
//                        .exclude("creationDate")
//                        .exclude("typeCat")
//                        .exclude("undirectCost")
//                        .exclude("remarks")
//                        .exclude("statusCat")
//                        .exclude("contractPrice")
//                        .exclude("storageLocation")
//                        .exclude("nickname")
//                        .exclude("minimumAmount")
//                        .exclude("maxmumAmount")
//                        .exclude("lastCost")
//                        .exclude("dateCreateCat")
//                        .exclude("openingCost")
//                        .exclude("storesQuality")
//                        .exclude("image")
//                        .exclude("weightPackage")
//                        .exclude("issell")
//                        .exclude("ispurchase")
//                        .exclude("brandId")
//                        .exclude("enamelColor")
//                        .exclude("originCountry")
//                        .exclude("paintColor")
//                        .exclude("author")
//                        .exclude("addon1")
//                        .exclude("addon2")
//                        .exclude("siteId")
//                        .exclude("stone")
//                        .exclude("height")
//                        .exclude("weight")
//                        .exclude("length")
//                        .exclude("width")
//                        .byDefault().register();
//            }
//
//            MapperFacade mapper = mapperFactory.getMapperFacade();
//            InvItemDTO dest = mapper.map(detail, InvItemDTO.class);
//            detailsesReturn.add(dest);
        }
        return detailsesReturn;
    }

    @Override
    public List<InvItem> getInvItemListByBranchIdForOpeningBalanceItemForm(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvItem> detailses = dao.findListByQuery("SELECT i FROM InvItem i WHERE i.branchId.id = :branchId  ORDER BY i.code", params);
        List<InvItem> detailsesReturn = new ArrayList<>();
        for (InvItem detail : detailses) {
            InvItem dest = new InvItem();
            dest.setId(detail.getId());
            dest.setName(detail.getName());
            dest.setCode(detail.getCode());
            dest.setUnitId(detail.getUnitId());
            detailsesReturn.add(dest);
        }
        return detailsesReturn;
    }

    @Override
    public Boolean checkIfGroupHasItems(InvGroup invGroup) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();
        List<InvItem> invItems = new ArrayList<>();
        if (invGroup.getId() != null) {
            params.put("invGroup", invGroup.getId());
            query.append("SELECT i FROM InvItem i WHERE i.groupId.id = :invGroup  ORDER BY i.code");
            invItems = dao.findListByQuery(query.toString(), params);
        }

        if (invItems != null && !invItems.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private List<InvItem> retriveItemData(List<InvItem> details) {
        List<InvItem> detailsReturn = new ArrayList<>();
        for (InvItem detail : details) {
            InvItem invItem = new InvItem();
            invItem.setId(detail.getId());
            invItem.setCode(detail.getCode());
            invItem.setName(detail.getName());
            invItem.setCostAverage(detail.getCostAverage());
            invItem.setMaxpricemen(detail.getMaxpricemen());
            invItem.setMinpricemen(detail.getMinpricemen());
            invItem.setMaxpriceyoung(detail.getMaxpriceyoung());
            invItem.setMinpriceyoung(detail.getMinpriceyoung());
            Symbol unitId = null;
            if (detail.getUnitId() != null) {//invItem.getUnitId() != null
                unitId = new Symbol();
                unitId.setId(detail.getUnitId().getId());
                unitId.setSerial(detail.getUnitId().getSerial());
                unitId.setName(detail.getUnitId().getName());
            }
            invItem.setUnitId(unitId);
            invItem.setSellPrice(detail.getSellPrice());

            if (detail.getGroupId() != null) {
                InvGroup invGroup = new InvGroup();
                invGroup.setId(detail.getGroupId().getId());
                invGroup.setName(detail.getGroupId().getName());
                invGroup.setCode(detail.getGroupId().getCode());
                invItem.setGroupId(invGroup);
            }

            if (detail.getSiteId() != null) {
                InvOrganizationSite site = new InvOrganizationSite();
                site.setId(detail.getSiteId().getId());
                site.setName(detail.getSiteId().getName());
                site.setCode(detail.getSiteId().getCode());
                invItem.setSiteId(site);
            }

            Branch branch = new Branch();
            if (detail.getBranchId() != null) {
                branch.setId(detail.getBranchId().getId());
                branch.setNameAr(detail.getBranchId().getNameAr());
            }
            invItem.setBranchId(branch);
            InvOrganizationSite site = null;
            if (detail.getSiteId() != null) {
                site = new InvOrganizationSite();
                site.setId(detail.getSiteId().getId());
                site.setName(detail.getSiteId().getName());
                site.setCode(detail.getSiteId().getCode());
            }
            invItem.setSiteId(site);
            invItem.setDateCreateCat(detail.getDateCreateCat());
            detailsReturn.add(invItem);
        }
        return detailsReturn;
    }

    @Override
    public List<InvItem> findItemByCode(String code, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();

        params.put("code", code);
        params.put("branchId", branchId);

        query.append("SELECT i FROM InvItem i WHERE i.branchId.id = :branchId AND i.code = :code  ORDER BY i.code");

        String q = query.toString();

        List<InvItem> invItems = dao.findListByQuery(q, params);

        if (invItems == null || invItems.isEmpty()) {
            return null;
        }

        return invItems;

    }

    @Override
    public BigDecimal findMaxItemCode(Integer settingType, Integer searchId, String searchCode) {

        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder();

        params.put("id", searchId);

        if (settingType == 0) {
            query.append("SELECT max(i.code) FROM InvItem i WHERE i.siteId.id = :id");
        } else if (settingType == 1) {
            query.append("SELECT max(i.code) FROM InvItem i WHERE i.groupId.id = :id");
        }

        String q = query.toString();

        List<String> invItems = dao.findListByQuery(q, params);

        if (invItems == null || invItems.isEmpty()) {
            String ccode = searchId + "001";
            return new BigDecimal(ccode.trim());
        }

        String code = invItems.get(invItems.size() - 1);
        if (code != null && !code.isEmpty()) {
            BigDecimal x = new BigDecimal(code.trim()).add(BigDecimal.ONE);
            return x;
        }
        String ccode = searchCode + "001";
        return new BigDecimal(ccode.trim());
    }

    @Override
    public List<InvItem> getInvItemView(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters) {

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT s FROM InvItem s where s.branchId.id = :branchId  ");
        appendFilter(filters, branchId, builder);
        builder.append(" ORDER BY s.id DESC");

        List<InvItem> details = dao.findListByQuery(builder.toString(), filters, first, pageSize);

//        List<InvItem> details = dao.creteQueryInvItemView(first, pageSize, sortField, asc, branchId, filters);
        return details;
    }

    @Override
    public List<InvItemDTO> getInvItemDTOView(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters, TobyUser tobyUser) {

        StringBuilder builder = new StringBuilder();
        builder.append("SELECT s FROM InvItem s where s.branchId.id = :branchId  ");
        appendFilter(filters, branchId, builder);
        builder.append(" ORDER BY s.id DESC");

        List<InvItem> details = dao.findListByQuery(builder.toString(), filters, first, pageSize);

        return mapToDTOList(details, tobyUser);
    }

    public void appendFilter(Map<String, Object> filters, Integer branchId, StringBuilder builder) {
        filters.put("branchId", branchId);
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and (s.code like CONCAT('%',:globalFilter ,'%')  OR s.name  like CONCAT('%',:globalFilter ,'%')  OR s.nickname like CONCAT('%',:globalFilter ,'%') OR s.unitId.name like CONCAT('%',:globalFilter ,'%') OR s.groupId.name like CONCAT('%',:globalFilter ,'%') ) ";
            builder.append(filter);
        }
    }

    @Override
    public Long getTotalRegistors(Integer branchId, Map<String, Object> filters) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT count(s) FROM InvItem s WHERE s.branchId.id = :branchId ");
        appendFilter(filters, branchId, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);

        return details;
    }

    @Override
    public List<InvItem> getInvItemReportForInvItemMainDataByGroupReportMB(ItemMainDataByGroupSearchBean ItemMainDataByGroupSearchBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", ItemMainDataByGroupSearchBean.getBranchId());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT e FROM InvItem e WHERE e.branchId.id = :branchId ");

        stringBuilder.append(appendGroup(ItemMainDataByGroupSearchBean, params));
        stringBuilder.append(appendItem(ItemMainDataByGroupSearchBean, params));

        List<InvItem> details = dao.findListByQuery(stringBuilder.toString(), params);
        List<InvItem> detailsReturn = retriveItemData(details);
        return detailsReturn;
    }

    private String appendGroup(ItemMainDataByGroupSearchBean ItemMainDataByGroupSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (ItemMainDataByGroupSearchBean.getGroupSelected() != null && !ItemMainDataByGroupSearchBean.getGroupSelected().isEmpty()
                && ItemMainDataByGroupSearchBean.getGroupSelected().size() > 0) {
            StringBuilder groups = new StringBuilder();
            for (String groupId : ItemMainDataByGroupSearchBean.getGroupSelected()) {
                if (groups.toString().isEmpty()) {
                    groups.append(groupId);
                } else {
                    groups.append(",").append(groupId);
                }
            }
            if (ItemMainDataByGroupSearchBean.getSupplier() == 1) {
                appendQuery.append(" AND e.siteId.id in (").append(groups).append(")");
            } else {
                appendQuery.append(" AND e.groupId.id in (").append(groups).append(")");
            }
        }

        return appendQuery.toString();
    }

    private String appendItem(ItemMainDataByGroupSearchBean ItemMainDataByGroupSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (ItemMainDataByGroupSearchBean.getFromItem() != null) {
            params.put("fromItem", ItemMainDataByGroupSearchBean.getFromItem().getCode()); //change to get code not id
            appendQuery.append(" AND e.code >= :fromItem"); //e.id
        }

        if (ItemMainDataByGroupSearchBean.getToItem() != null) {
            params.put("toItem", ItemMainDataByGroupSearchBean.getToItem().getCode());//change to get code not id
            appendQuery.append(" AND e.code <= :toItem"); //e.id
        }

        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<InvItemDTO> findInvItemDTOCompletedList(TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        List<InvItem> invItemList = dao.findListByQuery("SELECT e FROM InvItem e WHERE e.branchId.id =:branchId AND e.typeCat = 2", params);
        return mapToDTOList(invItemList, tobyUser);
    }

    @Override
    public List<InvItemDTO> getInvItemListByBranchIdForOpeningBalanceItemFormDTO(TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        List<InvItem> detailses = dao.findListByQuery("SELECT i FROM InvItem i WHERE i.branchId.id = :branchId  ORDER BY i.code", params);

        return mapToDTOList(detailses, tobyUser);

    }

    @Override
    public void updateInvItem2(Map<String, InvItem> invItemMap, TobyUser tobyUser) {
        Map<String, Integer> supplier = new HashMap<>();
        Integer i = 1;
        Integer x = 1;
        List<CurrencyDTO> currencyList = currencyServiceDTO.getAllCurrenciesByCompanyId(tobyUser.getCompanyId().getId());

        for (Map.Entry<String, InvItem> entry : invItemMap.entrySet()) {
            System.out.println("   item  " + entry.getKey());
            x++;
            InvItem excelItem = entry.getValue();
            List<InvItem> itemList = findItemByCode(entry.getKey(), 182);
            if (itemList != null && !itemList.isEmpty()) {
                InvItem item = itemList.get(0);

                if (excelItem.getImage() != null && !excelItem.getImage().trim().isEmpty()) {
                    if (excelItem.getImage().trim().equals("صنف غير مخزني")) {
                        item.setIsInventoryItem(0);
                    } else if (excelItem.getImage().trim().equals("صنف مخزني")) {
                        item.setIsInventoryItem(1);
                    }
                }

                if (excelItem.getNameen() != null && !excelItem.getNameen().trim().isEmpty()) {
                    Integer checkSupplier = supplier.get(excelItem.getNameen().trim());
                    if (checkSupplier == null) {
                        InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
                        invOrganizationSiteDTO.setName(excelItem.getNameen());
                        invOrganizationSiteDTO.setCode(i.toString());
                        invOrganizationSiteDTO.setCurrencyId(currencyList.get(0));
                        invOrganizationSiteDTO.setType(1);
                        InvOrganizationSiteDTO invOrganizationSiteDTO2 = organizationSiteService.saveInvOrganizationSiteDTO(invOrganizationSiteDTO, tobyUser);
                        InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
                        invOrganizationSite.setId(invOrganizationSiteDTO2.getId());
                        item.setSiteId(invOrganizationSite);
                        supplier.put(excelItem.getNameen(), invOrganizationSiteDTO2.getId());
                        ++i;
                    } else if (checkSupplier != null) {
                        InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
                        invOrganizationSite.setId(checkSupplier);
                        item.setSiteId(invOrganizationSite);
                    }
                }
                if (entry.getKey().equals("001829")) {
                    System.out.println("Stop");
                }
                dao.saveEntity(item);
            }
        }
    }
    
     @Override
    public void getInvItemListByBranchId(Integer branchId) {
        updateInvItemCostAverageToZero(branchId);
        List<Integer> invItemIdList = getInvItemListByBranchIdNotInInvPurchaseInvoice(branchId);
        List<Object[]> objectList = invOpeningBalanceItemDetailService.getInvOpeningBalanceItemDetailsByBranchId(branchId, invItemIdList);
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        for (Object[] objects : objectList) {
            String builder = "UPDATE  InvItem i set i.costAverage = " + (BigDecimal) objects[1] + " WHERE i.id = " + (Integer) objects[0] + " and i.branchId.id = :branchId";
            dao.executeUpdateQuery(builder, params);
        }
    }
    
    @Override
    public void updateInvItemCostAverageToZero(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        String builder_Zero = "UPDATE  InvItem i set i.costAverage = null WHERE i.id > 0 and i.branchId.id = :branchId";
        dao.executeUpdateQuery(builder_Zero, params);
    }
    
    @Override
    public List<Integer> getInvItemListByBranchIdNotInInvPurchaseInvoice(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        //  List<Integer> detailses = dao.findListByQuery("SELECT i.id FROM InvItem i WHERE i.branchId.id = :branchId  and i.id not in (SELECT e.itemId.id FROM InvPurchaseInvoiceDetail e WHERE e.branchId.id = :branchId and e.invPurchaseInvoiceId.type = 0) ORDER BY i.code", params);
        List<Integer> detailses = dao.findListByQuery("SELECT i.id FROM InvItem i WHERE i.branchId.id = :branchId  ORDER BY i.code", params);
        return detailses;
    }

}
