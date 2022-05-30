/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.businessservice.reports.searchBean.InvItemSalesSearchBean;
import com.toby.businessservice.reports.searchBean.ItemCardReportSerachBean;
import com.toby.core.GenericDAO;
import com.toby.dto.InvGroupDTO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.ProGroupDTO;
import com.toby.dto.SymbolDTO;
import com.toby.entity.Branch;
import com.toby.entity.GlYear;
import com.toby.entity.InvGroup;
import com.toby.entity.InvItem;
import com.toby.entity.ProGroup;
import com.toby.entity.Symbol;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import com.toby.views.ItemsDataView;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author elsakr6
 */
@Stateless
public class ItemsDataViewServiceImpl implements ItemsDataViewService {

    @EJB
    private GenericDAO dao;
    @EJB
    private InvBarcodeSevice invBarcodeSevice;

    @Override
    public List<ItemsDataView> findItemsDataViewByBranchIdAndItemIdAndInventoryId(Integer branchId, Integer itemId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("itemId", itemId);
        return dao.findListByQuery("SELECT e FROM ItemsDataView e WHERE e.branchId = :branchId and e.itemId = :itemId and e.inventoryId != null", params);
    }

    private List<ItemsDataView> prepareList(List<Object[]> res) {
        List<ItemsDataView> list = new ArrayList<>();
        ItemsDataView itemsDataView;
        for (Object[] object : res) {
            itemsDataView = new ItemsDataView();
            itemsDataView.setQtyout((BigDecimal) object[0]);
            itemsDataView.setCostAverage((BigDecimal) object[1]);
            itemsDataView.setQtyin((BigDecimal) object[2]);
            itemsDataView.setBalance((BigDecimal) object[3]);
            itemsDataView.setBalanceValue((BigDecimal) object[4]);
            itemsDataView.setItemId((Integer) object[5]);
            itemsDataView.setItemName((String) object[6]);
            itemsDataView.setItemCode((String) object[7]);
            itemsDataView.setItemUnitName((String) object[8]);
            itemsDataView.setItemBarcode((String) object[9]);
            itemsDataView.setSellPrice((BigDecimal) object[11]);
            itemsDataView.setGroupId((Integer) object[12]);
            itemsDataView.setGroupName((String) object[13]);
            itemsDataView.setInventoryId((Integer) object[14]);
            itemsDataView.setInventoryCode((String) object[15]);
            itemsDataView.setInventoryName((String) object[16]);

            list.add(itemsDataView);
        }
        return list;
    }

    public InvItemDTO mapToDTO(InvItem invItem, TobyUser tobyUser) {
        InvItemDTO invItemDTO = new InvItemDTO();
        invItemDTO.setCreatedBy(invItem.getCreatedBy() != null ? invItem.getCreatedBy().getId() : null);
        invItemDTO.setCreatedDate(invItem.getCreationDate());
        invItemDTO.setBranchId(invItem.getBranchId() != null ? invItem.getBranchId().getId() : null);
        invItemDTO.setCompanyId(invItem.getCompanyId() != null ? invItem.getCompanyId().getId() : null);
        invItemDTO.setId(invItem.getId());
        invItemDTO.setCode(invItem.getCode());
        invItemDTO.setContractPrice(invItem.getContractPrice());
        invItemDTO.setCostAverage(invItem.getCostAverage());
        invItemDTO.setDateCreateCat(invItem.getCreationDate());
        invItemDTO.setImage(invItem.getImage() != null ? invItem.getImage() : null);
//        invItemDTO.setShowImage(invItem.getImage()!= null ? fileUploadController.getDestination().concat(invItem.getImage()) : fileUploadController.getDestination() + "1.png");
//        invItemDTO.setUploadedImage(invItem.getImage());

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
        invItemDTO.setNumbermetersyoung(invItem.getNumbermetersyoung());
        invItemDTO.setNumbermetersmen(invItem.getNumbermetersmen());
        invItemDTO.setNumbermetersfreeyoung(invItem.getNumbermetersfreeyoung());
        invItemDTO.setNumbermetersfreemen(invItem.getNumbermetersfreemen());
        invItemDTO.setBounsepriceyoung(invItem.getBounsepriceyoung());
        invItemDTO.setBounsepricemen(invItem.getBounsepricemen());
        invItemDTO.setCommissionrate(invItem.getCommissionrate());
        invItemDTO.setCommissionvalue(invItem.getCommissionvalue());
        invItemDTO.setPrice_edit(invItem.getPrice_edit());
        invItemDTO.setIsinventoryitem(invItem.getIsInventoryItem());
        invItemDTO.setIsOfferItem(invItem.getIsOfferItem());
        invItemDTO.setTypeshow(invItem.getTypeshow());
        invItemDTO.setQuantity(invItem.getQuantity());
        invItemDTO.setItemNatural(invItem.getItemNatural());
        invItemDTO.setTypeItem(invItem.getType());
        if (invItem.getOriginCountry() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(invItem.getOriginCountry().getId());
            symbolDTO.setName(invItem.getOriginCountry().getName());
            invItemDTO.setOriginCountry(symbolDTO);
        }
        if (invItem.getPaintColor() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(invItem.getPaintColor().getId());
            symbolDTO.setName(invItem.getPaintColor().getName());
            invItemDTO.setPaintColor(symbolDTO);
        }
        if (invItem.getStone() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(invItem.getStone().getId());
            symbolDTO.setName(invItem.getStone().getName());
            invItemDTO.setStone(symbolDTO);
        }
        if (invItem.getUnitId() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(invItem.getUnitId().getId());
            symbolDTO.setName(invItem.getUnitId().getName());
            symbolDTO.setCreatedByCode(invItem.getUnitId().getCreatedBy().getUserCode());
            invItemDTO.setUnitId(symbolDTO);
        }
        if (invItem.getAddon1() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(invItem.getAddon1().getId());
            symbolDTO.setName(invItem.getAddon1().getName());
            invItemDTO.setAddon1(symbolDTO);
        }
        if (invItem.getAddon2() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(invItem.getAddon2().getId());
            symbolDTO.setName(invItem.getAddon2().getName());
            invItemDTO.setAddon2(symbolDTO);
        }

        if (invItem.getGroupId() != null) {
            InvGroupDTO invGroupDTO = new InvGroupDTO();
            invGroupDTO.setId(invItem.getGroupId().getId());
            invGroupDTO.setName(invItem.getGroupId().getName());
            invGroupDTO.setCode(invItem.getGroupId().getCode());
            invItemDTO.setGroupId(invGroupDTO);
        }

        if (invItem.getProGroupId() != null) {
            ProGroupDTO proGroupDTO = new ProGroupDTO();
            proGroupDTO.setId(invItem.getProGroupId().getId());
            proGroupDTO.setName(invItem.getProGroupId().getName());
            proGroupDTO.setCode(invItem.getProGroupId().getCode());
            invItemDTO.setProGroupId(proGroupDTO);
        }

        invItemDTO.setInvBarcodeEntity(invBarcodeSevice.getInvBarcodeByInvItemId(invItem.getBranchId().getId(), invItem.getId(), tobyUser));

        return invItemDTO;
    }

    public InvItem mapToEntity(InvItemDTO invItemDTO, TobyUser tobyUser) {

        InvItem invItem = new InvItem();
        invItem.setId(invItemDTO.getId());
        if (invItemDTO.getId() == null && invItemDTO.getSiteId() != null && invItemDTO.getSiteId().getId() != null && invItemDTO.getSiteId().getCode() != null) {
            invItem.setCode((findMaxItemCode(0, invItemDTO.getSiteId().getId(), invItemDTO.getSiteId().getCode())).toString());
        } else {
            invItem.setCode(invItemDTO.getCode());

        }
        invItem.setContractPrice(invItemDTO.getContractPrice());
        invItem.setCostAverage(invItemDTO.getCostAverage());
        invItem.setDateCreateCat(invItemDTO.getDateCreateCat());
        invItem.setImage(invItemDTO.getImage() != null ? invItemDTO.getImage() : null);
//        invItemDTO.setShowImage(invItem.getImage()!= null ? fileUploadController.getDestination().concat(invItem.getImage()) : fileUploadController.getDestination() + "1.png");
//        invItemDTO.setUploadedImage(invItem.getImage());

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
        invItem.setNumbermetersyoung(invItemDTO.getNumbermetersyoung());
        invItem.setNumbermetersmen(invItemDTO.getNumbermetersmen());
        invItem.setNumbermetersfreeyoung(invItemDTO.getNumbermetersfreeyoung());
        invItem.setNumbermetersfreemen(invItemDTO.getNumbermetersfreemen());
        invItem.setBounsepriceyoung(invItemDTO.getBounsepriceyoung());
        invItem.setBounsepricemen(invItemDTO.getBounsepricemen());
        invItem.setCommissionrate(invItemDTO.getCommissionrate());
        invItem.setCommissionvalue(invItemDTO.getCommissionvalue());
        invItem.setPrice_edit(invItemDTO.getPrice_edit());
        invItem.setIsInventoryItem(invItemDTO.getIsinventoryitem());
        invItem.setTypeshow(invItemDTO.getTypeshow());
        invItem.setItemNatural(invItemDTO.getItemNatural());
        invItem.setType(invItemDTO.getTypeItem());
        invItem.setQuantity(invItemDTO.getQuantity());
        /// Get Serial ///
//        Map<String, Object> params = new HashMap<>();
//        params.put("branchId", tobyUser.getBranchId().getId());
//        Integer serialmax = 0;
//        synchronized (serialmax) {
//            try {
//                serialmax = dao.findEntityByQuery("SELECT MAX(g.serial) FROM InvPurchaseInvoice g  WHERE g.branchId.id =:branchId ", params);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        if (serialmax == null) {
//            serialmax = 0;
//        }
//        invItem.setSerial(serialmax + 1);
        if (invItemDTO.getOriginCountry() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invItemDTO.getGroupId().getId());
            symbol.setName(invItemDTO.getGroupId().getName());
            invItem.setOriginCountry(symbol);
        }
        if (invItemDTO.getPaintColor() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invItemDTO.getPaintColor().getId());
            symbol.setName(invItemDTO.getPaintColor().getName());
            invItem.setPaintColor(symbol);
        }
        if (invItemDTO.getStone() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invItemDTO.getStone().getId());
            symbol.setName(invItemDTO.getStone().getName());
            invItem.setStone(symbol);
        }
        if (invItemDTO.getUnitId() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invItemDTO.getUnitId().getId());
            symbol.setName(invItemDTO.getUnitId().getName());
            invItem.setUnitId(symbol);
        }
        if (invItemDTO.getAddon1() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invItemDTO.getAddon1().getId());
            symbol.setName(invItemDTO.getAddon1().getName());
            invItem.setAddon1(symbol);
        }
        if (invItemDTO.getAddon2() != null) {
            Symbol symbol = new Symbol();
            symbol.setId(invItemDTO.getAddon2().getId());
            symbol.setName(invItemDTO.getAddon2().getName());
            invItem.setAddon2(symbol);
        }

        if (invItemDTO.getGroupId() != null) {
            InvGroup invGroup = new InvGroup();
            invGroup.setId(invItemDTO.getGroupId().getId());
            invItem.setGroupId(invGroup);
        } else {
            InvGroup invGroup = new InvGroup();
            invGroup.setId(56);
            invItem.setGroupId(invGroup);
        }

        if (invItemDTO.getProGroupId() != null) {
            ProGroup proGroup = new ProGroup();
            proGroup.setId(invItemDTO.getProGroupId().getId());
            proGroup.setName(invItemDTO.getProGroupId().getName());
            proGroup.setCode(invItemDTO.getProGroupId().getCode());
            invItem.setProGroupId(proGroup);
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

            if (invItemDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(invItemDTO.getBranchId());
                invItem.setBranchId(branch);
            }
        } else {
            invItem.setCreatedBy(tobyUser);
            invItem.setCreationDate(new Date());
            invItem.setCompanyId(tobyUser.getCompanyId());
            invItem.setBranchId(tobyUser.getBranchId());
        }

        invItem.setIsOfferItem(invItemDTO.getIsOfferItem());
        return invItem;
    }

    public List<InvItemDTO> mapToDTOList(List<InvItem> invItemList, TobyUser tobyUser) {
        List<InvItemDTO> invItemDTOList = new ArrayList<>();
        for (InvItem invItem : invItemList) {
            invItemDTOList.add(mapToDTO(invItem, tobyUser));
        }
        return invItemDTOList;
    }

    @Override
    public InvItemDTO addItemsData(InvItemDTO invItemDTO, TobyUser tobyUser) {
        InvItemDTO invItemDTO1 = new InvItemDTO();
        InvItem invItem = mapToEntity(invItemDTO, tobyUser);
        invItem = dao.updateEntity(invItem);

        if (invItemDTO.getInvBarcodeDeletedList() != null) {
            invBarcodeSevice.deleteListInvBarcode(invItemDTO.getInvBarcodeDeletedList());
        }

        if (invItemDTO.getInvBarcodeEntity() != null) {
            invBarcodeSevice.addListInvBarcode(invItemDTO.getInvBarcodeEntity(), tobyUser, invItem.getId());
        }

        invItemDTO1 = mapToDTO(invItem, tobyUser);
        return invItemDTO1;
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
    public List<ItemsDataView> getAllInvStripListByInvStripSearchBean(InvItemSalesSearchBean invStripSearchBean) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", invStripSearchBean.getBranchId());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(""
                + "SELECT sum(IDV.qtyout),IDV.costAverage,"
                + " sum(IDV.qtyin),"
                + " sum(IDV.qtyin) - sum(IDV.qtyout) , "
                + " sum((IDV.qtyin - IDV.qtyout) * IDV.costAverage), "
                + " IDV.itemId,"
                + " IDV.itemName,"
                + " IDV.itemCode,"
                + " IDV.itemUnitName, "
                + " IDV.itemBarcode, "
                + " null, "
                + " IDV.sellPrice, "
                + " IDV.groupId, "
                + " IDV.groupName, "
                + " IDV.inventoryId, "
                + " IDV.inventoryCode, "
                + " IDV.inventoryName"
                + " FROM ItemsDataView IDV WHERE IDV.branchId = :branchId AND IDV.date is not null AND IDV.itemId is not null "
                + " AND IDV.inventoryId is not null AND IDV.groupId is not null"
                + " AND IDV.itemName is not null  AND IDV.itemCode is not null AND IDV.itemUnitName is not null "
                + " AND IDV.qtyin is not null  AND IDV.qtyout is not null AND IDV.costAverage is not null  ");

        stringBuilder.append(appendDateinv(invStripSearchBean, params));
        stringBuilder.append(appendInventoryinv(invStripSearchBean, params));
        stringBuilder.append(appendGroupinv(invStripSearchBean, params));
        stringBuilder.append(appendSuplierinv(invStripSearchBean, params));

        stringBuilder.append(" group by IDV.itemId, "
                + " IDV.itemName, "
                + " IDV.itemCode, "
                + " IDV.itemUnitName, "
                + " IDV.groupId, "
                + " IDV.groupName, "
                + " IDV.inventoryId,"
                + " IDV.inventoryCode,"
                + " IDV.inventoryName,IDV.costAverage");

        List<Object[]> res = dao.findListByQuery(stringBuilder.toString(), params);

        List<ItemsDataView> details = prepareList(res);

        return details;
    }

    private String appendDateinv(InvItemSalesSearchBean invStripSearchBean, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();

        if (invStripSearchBean.getDateTo() != null) {
            params.put("dateTo", invStripSearchBean.getDateTo());
            appendQuery.append(" AND IDV.date <= :dateTo");
        }

        return appendQuery.toString();
    }

    private String appendInventoryinv(InvItemSalesSearchBean invStripSearchBean, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();

        if (invStripSearchBean.getInventoryFrom() != null) {
            params.put("inventoryCodeFrom", invStripSearchBean.getInventoryFrom().getId());
            appendQuery.append(" AND IDV.inventoryId >= :inventoryCodeFrom");
        }

        if (invStripSearchBean.getInventoryTo() != null) {
            params.put("inventoryCodeTo", invStripSearchBean.getInventoryTo().getId());
            appendQuery.append(" AND IDV.inventoryId <= :inventoryCodeTo");
        }
        return appendQuery.toString();
    }

    private String appendGroupinv(InvItemSalesSearchBean invStripSearchBean, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();

//        if (invStripSearchBean.getGroupFrom() != null) {
//            params.put("groupCodeFrom", invStripSearchBean.getGroupFrom().getId());
//            appendQuery.append(" AND IDV.groupId >= :groupCodeFrom");
//        }
//
//        if (invStripSearchBean.getGroupTo() != null) {
//            params.put("groupCodeTo", invStripSearchBean.getGroupTo().getId());
//            appendQuery.append(" AND IDV.groupId <= :groupCodeTo");
//        }
        if (invStripSearchBean.getStringBuilder() != null && invStripSearchBean.getStringBuilder().length() > 0) {
            appendQuery.append(" AND IDV.groupId in( ").append(invStripSearchBean.getStringBuilder().toString()).append(" ) ");
        }
        return appendQuery.toString();
    }

    private String appendSuplierinv(InvItemSalesSearchBean invStripSearchBean, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();

        if (invStripSearchBean.getSuplierFrom() != null) {
            params.put("suplierCodeFrom", invStripSearchBean.getSuplierFrom().getId());
            appendQuery.append(" AND IDV.organizationSiteId >= :suplierCodeFrom");
        }

        if (invStripSearchBean.getSuplierTo() != null) {
            params.put("suplierCodeTo", invStripSearchBean.getSuplierTo().getId());
            appendQuery.append(" AND IDV.organizationSiteId <= :suplierCodeTo");
        }

        return appendQuery.toString();
    }

    @Override
    public List<ItemsDataView> getAllItemCardByInvStripSearchBeanNew(ItemCardReportSerachBean itemCardReportSerachBean, GlYear glYear) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", itemCardReportSerachBean.getBranchId());

        StringBuilder stringBuilder = new StringBuilder();
        Map<Integer, BigDecimal> itemOpeningBalanceMap = new HashMap<>();
        Map<Integer, String> itemOpeningBalanceAfterAllDateMap = new HashMap<>();
        Map<Integer, BigDecimal> qtyInMap = new HashMap<>();
        Map<Integer, BigDecimal> qtyOutMap = new HashMap<>();
        if (itemCardReportSerachBean.getDateFrom() != null && itemCardReportSerachBean.getDateFrom().after(glYear.getDateFrom())) {
            stringBuilder.append("SELECT IDV.itemId, sum(COALESCE(IDV.qtyin * IDV.screwing,0))-sum(COALESCE(IDV.qtyout * IDV.screwing,0)),IDV.itemName"
                    + ",sum(COALESCE(IDV.qtyin * IDV.screwing,0)), sum(COALESCE(IDV.qtyout * IDV.screwing,0)) "
                    + " FROM ItemsDataView IDV WHERE IDV.branchId = :branchId "
                    + " AND IDV.date is not null AND IDV.itemId is not null "
                    + " AND IDV.inventoryId is not null AND IDV.groupId is not null"
                    + " AND IDV.itemName is not null AND IDV.itemCode is not null "
                    + " AND IDV.qtyin is not null  AND IDV.qtyout is not null ");

            stringBuilder.append(appendGlYear(itemCardReportSerachBean, glYear, params));
            stringBuilder.append(appendTillDate(itemCardReportSerachBean, glYear, params));
            stringBuilder.append(appendInventory(itemCardReportSerachBean, params));
            stringBuilder.append(appendInvItem(itemCardReportSerachBean, params));

            stringBuilder.append(" group by IDV.itemId ,IDV.itemName");
//            stringBuilder.append(" order by IDV.itemId ,IDV.date ASC");
            List<Object[]> list = dao.findListByQuery(stringBuilder.toString(), params);

            for (Object[] os : list) {
                itemOpeningBalanceMap.put((Integer) os[0], (BigDecimal) os[1]);
                itemOpeningBalanceAfterAllDateMap.put((Integer) os[0], (String) os[2]);
            }
        }
        //to add total qtyin and qtyout
        stringBuilder = new StringBuilder();
        stringBuilder.append(" SELECT IDV.item_id "
                + ",sum(COALESCE(IDV.qtyin * IDV.screwing,0)), sum(COALESCE(IDV.qtyout * IDV.screwing,0)) from ( ");
        stringBuilder.append("SELECT         \n"
                + "        0 AS `qtyin`,\n"
                + "        IFNULL(`iaod`.`quantity`, 0) AS `qtyout`,\n"
                + "        IFNULL(`iaod`.`screwing`, 1) AS `screwing`,\n"
                + "        `iaod`.`item_id` AS `item_id`\n"
                + "    FROM\n"
                + "        (`inv_inventory_transaction` `iao`\n"
                + "        LEFT JOIN `inv_inventory_transaction_detail` `iaod` ON ((`iao`.`id` = `iaod`.`inventory_transaction_id`)))\n"
                + "    WHERE\n"
                + "        ((`iao`.`type` = 1)\n"
                + "            AND (`iao`.`date` IS NOT NULL)) ");
        if (itemCardReportSerachBean.getInvItemForm() != null && itemCardReportSerachBean.getInvItemForm().getId() != null) {
            stringBuilder.append(" AND `iaod`.`item_id` = ").append(itemCardReportSerachBean.getInvItemForm().getId());
        }
        if (itemCardReportSerachBean.getInventoryFrom() != null && itemCardReportSerachBean.getInventoryFrom().getId() != null) {
            stringBuilder.append(" AND `iao`.`inv_inventory_id` = ").append(itemCardReportSerachBean.getInventoryFrom().getId());
        }
        if (itemCardReportSerachBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateFrom());
            stringBuilder.append(" AND `iao`.`date` >= '").append(formatDateFrom).append("00:00:00 '");
        }
        if (itemCardReportSerachBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateTo());
            stringBuilder.append(" AND `iao`.`date` <= '").append(formatDateTo).append("23:59:59 '");
        }

        stringBuilder.append(" AND `iao`.`branch_id` = ").append(itemCardReportSerachBean.getBranchId()).append("  \n"
                + "    UNION ALL SELECT \n"
                + "        IFNULL(`iaod`.`quantity`, 0) AS `qtyin`,\n"
                + "        0 AS `qtyout`,\n"
                + "        IFNULL(`iaod`.`screwing`, 1) AS `screwing`,\n"
                + "        `iaod`.`item_id` AS `item_Id`\n"
                + "    FROM\n"
                + "        (`inv_inventory_transaction` `iao`\n"
                + "        LEFT JOIN `inv_inventory_transaction_detail` `iaod` ON ((`iao`.`id` = `iaod`.`inventory_transaction_id`)))\n"
                + "    WHERE\n"
                + "        ((`iao`.`type` = 0)\n"
                + "            AND (`iao`.`date` IS NOT NULL))");
        if (itemCardReportSerachBean.getInvItemForm() != null && itemCardReportSerachBean.getInvItemForm().getId() != null) {
            stringBuilder.append(" AND `iaod`.`item_id` = ").append(itemCardReportSerachBean.getInvItemForm().getId());
        }
        if (itemCardReportSerachBean.getInventoryFrom() != null && itemCardReportSerachBean.getInventoryFrom().getId() != null) {
            stringBuilder.append(" AND `iao`.`inv_inventory_id` = ").append(itemCardReportSerachBean.getInventoryFrom().getId());
        }
        if (itemCardReportSerachBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateFrom());
            stringBuilder.append(" AND `iao`.`date` >= '").append(formatDateFrom).append("00:00:00 '");
        }
        if (itemCardReportSerachBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateTo());
            stringBuilder.append(" AND `iao`.`date` <= '").append(formatDateTo).append("23:59:59 '");
        }
        stringBuilder.append(" AND `iao`.`branch_id` = ").append(itemCardReportSerachBean.getBranchId()).append("  \n"
                + "    UNION ALL SELECT \n"
                + "        IFNULL(`ipid`.`quantity`, 0) AS `qtyin`,\n"
                + "        0 AS `qtyout`,\n"
                + "        IFNULL(`ipid`.`screwing`, 1) AS `screwing`,\n"
                + "        `ipid`.`item_id` AS `item_Id`\n"
                + "    FROM\n"
                + "        (`inv_purchase_invoice` `ipi`\n"
                + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON ((`ipi`.`id` = `ipid`.`inv_purchase_invoice_id`)))\n"
                + "    WHERE\n"
                + "        ((`ipi`.`type` = 0)\n"
                + "            AND (`ipi`.`date` IS NOT NULL)) ");
        if (itemCardReportSerachBean.getInvItemForm() != null && itemCardReportSerachBean.getInvItemForm().getId() != null) {
            stringBuilder.append(" AND `ipid`.`item_id` = ").append(itemCardReportSerachBean.getInvItemForm().getId());
        }

        if (itemCardReportSerachBean.getInventoryFrom() != null && itemCardReportSerachBean.getInventoryFrom().getId() != null) {
            stringBuilder.append(" AND `ipid`.`inv_inventory_id` = ").append(itemCardReportSerachBean.getInventoryFrom().getId());
        }

        if (itemCardReportSerachBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateFrom());
            stringBuilder.append(" AND `ipi`.`date` >= '").append(formatDateFrom).append("00:00:00 '");
        }
        if (itemCardReportSerachBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateTo());
            stringBuilder.append(" AND `ipi`.`date` <= '").append(formatDateTo).append("23:59:59 '");
        }

        stringBuilder.append(" AND `ipi`.`branch_id` = ").append(itemCardReportSerachBean.getBranchId()).append("  \n"
                + "    UNION ALL SELECT \n"
                + "        0 AS `qtyin`,\n"
                + "        IFNULL(`ipid`.`quantity`, 0) AS `qtyout`,\n"
                + "        IFNULL(`ipid`.`screwing`, 1) AS `screwing`,\n"
                + "        `ipid`.`item_id` AS `item_Id`\n"
                + "    FROM\n"
                + "        (`inv_purchase_invoice` `ipi`\n"
                + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON ((`ipi`.`id` = `ipid`.`inv_purchase_invoice_id`)))\n"
                + "    WHERE\n"
                + "        ((`ipi`.`type` = 4)\n"
                + "            AND (`ipi`.`date` IS NOT NULL)) ");
        if (itemCardReportSerachBean.getInvItemForm() != null && itemCardReportSerachBean.getInvItemForm().getId() != null) {
            stringBuilder.append(" AND `ipid`.`item_id` = ").append(itemCardReportSerachBean.getInvItemForm().getId());
        }

        if (itemCardReportSerachBean.getInventoryFrom() != null && itemCardReportSerachBean.getInventoryFrom().getId() != null) {
            stringBuilder.append(" AND `ipid`.`inv_inventory_id` = ").append(itemCardReportSerachBean.getInventoryFrom().getId());
        }

        if (itemCardReportSerachBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateFrom());
            stringBuilder.append(" AND `ipi`.`date` >= '").append(formatDateFrom).append("00:00:00 '");
        }
        if (itemCardReportSerachBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateTo());
            stringBuilder.append(" AND `ipi`.`date` <= '").append(formatDateTo).append("23:59:59 '");
        }
        stringBuilder.append(" AND `ipi`.`branch_id` = ").append(itemCardReportSerachBean.getBranchId()).append("  \n"
                + "    UNION ALL SELECT \n"
                + "        0 AS `qtyin`,\n"
                + "        IFNULL(`ird`.`amount`, 0) AS `qtyout`,\n"
                + "        IFNULL(`ird`.`screwing`, 1) AS `screwing`,\n"
                + "        `ird`.`item_id` AS `item_Id` \n"
                + "    FROM\n"
                + "        (`inv_reservation` `ir`\n"
                + "        LEFT JOIN `inv_reservation_detail` `ird` ON ((`ir`.`id` = `ird`.`reservation_id`)))\n"
                + "    WHERE\n"
                + "        ((`ir`.`reservation_date` IS NOT NULL)) ");
        if (itemCardReportSerachBean.getInvItemForm() != null && itemCardReportSerachBean.getInvItemForm().getId() != null) {
            stringBuilder.append(" AND `ird`.`item_id` = ").append(itemCardReportSerachBean.getInvItemForm().getId());
        }

        if (itemCardReportSerachBean.getInventoryFrom() != null && itemCardReportSerachBean.getInventoryFrom().getId() != null) {
            stringBuilder.append(" AND `ir`.`inv_id` = ").append(itemCardReportSerachBean.getInventoryFrom().getId());
        }
        if (itemCardReportSerachBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateFrom());
            stringBuilder.append(" AND `ir`.`reservation_date` >= '").append(formatDateFrom).append("00:00:00 '");
        }
        if (itemCardReportSerachBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateTo());
            stringBuilder.append(" AND `ir`.`reservation_date` <= '").append(formatDateTo).append("23:59:59 '");
        }
        stringBuilder.append(" AND `ir`.`branch_id` = ").append(itemCardReportSerachBean.getBranchId()).append("  \n"
                + "    UNION ALL SELECT \n"
                + "        0 AS `qtyin`,\n"
                + "        IFNULL(`ipid`.`quantity`, 0) AS `qtyout`,\n"
                + "        IFNULL(`ipid`.`screwing`, 1) AS `screwing`,\n"
                + "        `ipid`.`item_id` AS `item_Id` \n"
                + "    FROM\n"
                + "        (`inv_purchase_invoice` `ipi`\n"
                + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON ((`ipi`.`id` = `ipid`.`inv_purchase_invoice_id`)))\n"
                + "    WHERE\n"
                + "        ((`ipi`.`type` = 2)\n"
                + "            AND (`ipi`.`date` IS NOT NULL)) ");
        if (itemCardReportSerachBean.getInvItemForm() != null && itemCardReportSerachBean.getInvItemForm().getId() != null) {
            stringBuilder.append(" AND `ipid`.`item_id` = ").append(itemCardReportSerachBean.getInvItemForm().getId());
        }

        if (itemCardReportSerachBean.getInventoryFrom() != null && itemCardReportSerachBean.getInventoryFrom().getId() != null) {
            stringBuilder.append(" AND `ipid`.`inv_inventory_id` = ").append(itemCardReportSerachBean.getInventoryFrom().getId());
        }
        if (itemCardReportSerachBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateFrom());
            stringBuilder.append(" AND `ipi`.`date` >= '").append(formatDateFrom).append("00:00:00 '");
        }
        if (itemCardReportSerachBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateTo());
            stringBuilder.append(" AND `ipi`.`date` <= '").append(formatDateTo).append("23:59:59 '");
        }

        stringBuilder.append(" AND `ipi`.`branch_id` = ").append(itemCardReportSerachBean.getBranchId()).append("  \n"
                + "    UNION ALL SELECT \n"
                + "        IFNULL(`ipid`.`quantity`, 0) AS `qtyin`,\n"
                + "        0 AS `qtyout`,\n"
                + "        IFNULL(`ipid`.`screwing`, 1) AS `screwing`,\n"
                + "        `ipid`.`item_id` AS `item_Id`\n"
                + "    FROM\n"
                + "        (`inv_purchase_invoice` `ipi`\n"
                + "         LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON ((`ipi`.`id` = `ipid`.`inv_purchase_invoice_id`)))\n"
                + "    WHERE\n"
                + "        ((`ipi`.`type` = 3)\n"
                + "            AND (`ipi`.`date` IS NOT NULL)) ");
        if (itemCardReportSerachBean.getInvItemForm() != null && itemCardReportSerachBean.getInvItemForm().getId() != null) {
            stringBuilder.append(" AND `ipid`.`item_id` = ").append(itemCardReportSerachBean.getInvItemForm().getId());
        }

        if (itemCardReportSerachBean.getInventoryFrom() != null && itemCardReportSerachBean.getInventoryFrom().getId() != null) {
            stringBuilder.append(" AND `ipid`.`inv_inventory_id` = ").append(itemCardReportSerachBean.getInventoryFrom().getId());
        }
        if (itemCardReportSerachBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateFrom());
            stringBuilder.append(" AND `ipi`.`date` >= '").append(formatDateFrom).append("00:00:00 '");
        }
        if (itemCardReportSerachBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateTo());
            stringBuilder.append(" AND `ipi`.`date` <= '").append(formatDateTo).append("23:59:59 '");
        }
        stringBuilder.append(" AND `ipi`.`branch_id` = ").append(itemCardReportSerachBean.getBranchId()).append("  \n"
                + "    UNION ALL SELECT \n"
                + "        0 AS `qtyin`,\n"
                + "        IFNULL(`itd`.`amount`, 0) AS `qtyout`,\n"
                + "        IFNULL(`itd`.`screwing`, 1) AS `screwing`,\n"
                + "        `itd`.`inv_item_id` AS `item_Id`\n"
                + "    FROM\n"
                + "        (`inv_transfer` `it`\n"
                + "        LEFT JOIN `inv_transfer_detail` `itd` ON ((`it`.`id` = `itd`.`inv_transfer_id`)))\n"
                + "    WHERE\n"
                + "        ((`it`.`transfer_date` IS NOT NULL)\n"
                + "            AND (`it`.`transfer_type` = 0)) ");
        if (itemCardReportSerachBean.getInvItemForm() != null && itemCardReportSerachBean.getInvItemForm().getId() != null) {
            stringBuilder.append(" AND `itd`.`inv_item_id` = ").append(itemCardReportSerachBean.getInvItemForm().getId());
        }

        if (itemCardReportSerachBean.getInventoryFrom() != null && itemCardReportSerachBean.getInventoryFrom().getId() != null) {
            stringBuilder.append(" AND `it`.`inv_from` = ").append(itemCardReportSerachBean.getInventoryFrom().getId());
        }
        if (itemCardReportSerachBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateFrom());
            stringBuilder.append(" AND `it`.`transfer_date` >= '").append(formatDateFrom).append("00:00:00 '");
        }
        if (itemCardReportSerachBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateTo());
            stringBuilder.append(" AND `it`.`transfer_date` <= '").append(formatDateTo).append("23:59:59 '");
        }
        stringBuilder.append(" AND `it`.`branch_id` = ").append(itemCardReportSerachBean.getBranchId()).append("  \n"
                + "    UNION ALL SELECT \n"
                + "        IFNULL(`itd`.`amount`, 0) AS `qtyin`,\n"
                + "        0 AS `qtyout`,\n"
                + "        IFNULL(`itd`.`screwing`, 1) AS `screwing`,\n"
                + "        `itd`.`inv_item_id` AS `item_Id`\n"
                + "    FROM\n"
                + "        (`inv_transfer` `it`\n"
                + "        LEFT JOIN `inv_transfer_detail` `itd` ON ((`it`.`id` = `itd`.`inv_transfer_id`)))\n"
                + "    WHERE\n"
                + "        ((`it`.`transfer_date` IS NOT NULL)\n"
                + "            AND (`it`.`transfer_type` = 0)) ");
        if (itemCardReportSerachBean.getInvItemForm() != null && itemCardReportSerachBean.getInvItemForm().getId() != null) {
            stringBuilder.append(" AND `itd`.`inv_item_id` = ").append(itemCardReportSerachBean.getInvItemForm().getId());
        }

        if (itemCardReportSerachBean.getInventoryFrom() != null && itemCardReportSerachBean.getInventoryFrom().getId() != null) {
            stringBuilder.append(" AND `it`.`inv_to` = ").append(itemCardReportSerachBean.getInventoryFrom().getId());
        }
        if (itemCardReportSerachBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateFrom());
            stringBuilder.append(" AND `it`.`transfer_date` >= '").append(formatDateFrom).append("00:00:00 '");
        }
        if (itemCardReportSerachBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateTo());
            stringBuilder.append(" AND `it`.`transfer_date` <= '").append(formatDateTo).append("23:59:59 '");
        }

        stringBuilder.append(" AND `it`.`branch_id` = ").append(itemCardReportSerachBean.getBranchId()).append("  \n"
                + "    UNION ALL SELECT \n"
                + "        `isd`.`difference` AS `qtyin`,\n"
                + "        0 AS `qtyout`,\n"
                + "        IFNULL(`isd`.`screwing`, 1) AS `screwing`,\n"
                + "        `isd`.`inv_item_id` AS `item_Id`\n"
                + "    FROM\n"
                + "        (`inv_update` `is`\n"
                + "        LEFT JOIN `inv_update_detail` `isd` ON ((`is`.`id` = `isd`.`inv_update_id`)))\n"
                + "    WHERE\n"
                + "        (`is`.`date` IS NOT NULL) ");
        if (itemCardReportSerachBean.getInvItemForm() != null && itemCardReportSerachBean.getInvItemForm().getId() != null) {
            stringBuilder.append(" AND `isd`.`inv_item_id` = ").append(itemCardReportSerachBean.getInvItemForm().getId());
        }

        if (itemCardReportSerachBean.getInventoryFrom() != null && itemCardReportSerachBean.getInventoryFrom().getId() != null) {
            stringBuilder.append(" AND `is`.`inventory_id` = ").append(itemCardReportSerachBean.getInventoryFrom().getId());
        }
        if (itemCardReportSerachBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateFrom());
            stringBuilder.append(" AND `is`.`date` >= '").append(formatDateFrom).append("00:00:00 '");
        }
        if (itemCardReportSerachBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateTo());
            stringBuilder.append(" AND `is`.`date` <= '").append(formatDateTo).append("23:59:59 '");
        }
        stringBuilder.append(" AND `is`.`branch_id` = ").append(itemCardReportSerachBean.getBranchId()).append("  \n"
                + "    UNION ALL SELECT \n"
                + "        `iobid`.`quantity` AS `qtyin`,\n"
                + "        0 AS `qtyout`,\n"
                + "        IFNULL(`iobid`.`screwing`, 1) AS `screwing`,\n"
                + "        `iobid`.`item_id` AS `item_Id`\n"
                + "    FROM\n"
                + "        (`inv_openning_balance_item` `iobi`\n"
                + "        LEFT JOIN `inv_openning_balance_item_detail` `iobid` ON ((`iobi`.`id` = `iobid`.`inv_openning_balance_item_id`)))\n"
                + "    WHERE\n"
                + "        (`iobi`.`date` IS NOT NULL) ");
        if (itemCardReportSerachBean.getInvItemForm() != null && itemCardReportSerachBean.getInvItemForm().getId() != null) {
            stringBuilder.append(" AND `iobid`.`item_id` = ").append(itemCardReportSerachBean.getInvItemForm().getId());
        }

        if (itemCardReportSerachBean.getInventoryFrom() != null && itemCardReportSerachBean.getInventoryFrom().getId() != null) {
            stringBuilder.append(" AND `iobi`.`inv_inventory_id` = ").append(itemCardReportSerachBean.getInventoryFrom().getId());
        }
        if (itemCardReportSerachBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateFrom());
            stringBuilder.append(" AND `iobi`.`date` >= '").append(formatDateFrom).append(" 00:00:00'");
        }
        if (itemCardReportSerachBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateTo());
            stringBuilder.append(" AND `iobi`.`date` <= '").append(formatDateTo).append(" 23:59:59'");
        }
        stringBuilder.append(" AND `iobi`.`branch_id` = ").append(itemCardReportSerachBean.getBranchId()).append("  \n"
                + "                   ");

        stringBuilder.append(" ) IDV group by IDV.item_id order by IDV.item_id ASC");

        List<Object[]> res2 = dao.executeNativeQuery(stringBuilder.toString());

        Iterator it2 = res2.iterator();
        while (it2.hasNext()) {
            Object[] line = (Object[]) it2.next();
            qtyInMap.put((Integer) line[0], (BigDecimal) line[1]);
            qtyOutMap.put((Integer) line[0], (BigDecimal) line[2]);
        }

//        stringBuilder.append(" group by IDV.itemId ,IDV.itemName");
//        stringBuilder.append(" order by IDV.itemId ASC, IDV.date ASC");
//        List<Object[]> list2 = dao.findListByQuery(stringBuilder.toString(), params);
//       
        stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT IDV.* from ( ");
        stringBuilder.append("SELECT         \n"
                + "        `iao`.`id` AS `id`,\n"
                + "        `iao`.`serial` AS `serial`,\n"
                + "        NULL AS `discount_flag_head`,\n"
                + "        NULL AS `discount_value_head`,\n"
                + "        NULL AS `tax_flag_head`,\n"
                + "        NULL AS `tax_value_head`,\n"
                + "        NULL AS `net_detail_exit`,\n"
                + "        NULL AS `net_detail_adding`,\n"
                + "        NULL AS `payment_type`,\n"
                + "        `iaod`.`quantity` AS `qty`,\n"
                + "        `iao`.`date` AS `date`,\n"
                + "        `ig`.`id` AS `group_id`,\n"
                + "        `ig`.`name` AS `group_name`,\n"
                + "        `ig`.`code` AS `group_code`,\n"
                + "        `ii`.`name` AS `item_name`,\n"
                + "        `ii`.`code` AS `item_code`,\n"
                + "        `ii`.`sell_price` AS `sell_price`,\n"
                + "        `iaod`.`item_barcode` AS `item_barcode`,\n"
                + "        `s`.`name` AS `item_unit_name`,\n"
                + "        `ss`.`name` AS `detail_unit_name`,\n"
                + "        0 AS `qtyin`,\n"
                + "        IFNULL(`iaod`.`quantity`, 0) AS `qtyout`,\n"
                + "        IFNULL(`ii`.`cost_average`, 0) AS `cost_average`,\n"
                + "        IFNULL(`iaod`.`screwing`, 1) AS `screwing`,\n"
                + "        `iaod`.`item_id` AS `item_id`,\n"
                + "        `iao`.`branch_id` AS `branch_id`,\n"
                + "        `iao`.`post_flag` AS `post_flag`,\n"
                + "        `iao`.`inv_inventory_id` AS `inventory_id`,\n"
                + "        `ini`.`code` AS `inventory_code`,\n"
                + "        `ini`.`name` AS `inventory_name`,\n"
                + "        `iao`.`organization_site_id` AS `organization_site_id`,\n"
                + "        `ios`.`code` AS `organization_code`,\n"
                + "        `ios`.`name` AS `organization_name`,\n"
                + "        `ios`.`type` AS `organization_type`,\n"
                + "        `ios`.`open_balance_debit` AS `organization_open_balance_debit`,\n"
                + "        `ios`.`open_balance_credit` AS `organization_open_balance_credit`,\n"
                + "        `ios`.`currency_id` AS `organization_currency_id`,\n"
                + "        `ios`.`gallary_id` AS `organization_gallary_id`,\n"
                + "        0 AS `weight_detail`,\n"
                + "        0 AS `actual_weight`,\n"
                + "        0 AS `total_actual_weight`,\n"
                + "        0 AS `price_kilo`,\n"
                + "        0 AS `type`,\n"
                + "        'صرف بضاعة' AS `ScreenName`,\n"
                + "        1 AS `operationType`,\n"
                + "        1 AS `ScreenCode`,\n"
                + "        FIND_SUPPLIER_OF_ITEM(`iaod`.`item_id`) AS `item_supplier_id`,\n"
                + "        0 AS `disc_halala`\n"
                + "    FROM\n"
                + "        (((((((`inv_inventory_transaction` `iao`\n"
                + "        LEFT JOIN `inv_inventory_transaction_detail` `iaod` ON ((`iao`.`id` = `iaod`.`inventory_transaction_id`)))\n"
                + "        LEFT JOIN `inv_item` `ii` ON ((`ii`.`id` = `iaod`.`item_id`)))\n"
                + "        LEFT JOIN `inv_group` `ig` ON ((`ig`.`id` = `ii`.`group_id`)))\n"
                + "        LEFT JOIN `symbol` `s` ON ((`s`.`id` = `ii`.`unit_id`)))\n"
                + "        LEFT JOIN `symbol` `ss` ON ((`ss`.`id` = `iaod`.`unit_id`)))\n"
                + "        LEFT JOIN `inv_inventory` `ini` ON ((`ini`.`id` = `iao`.`inv_inventory_id`)))\n"
                + "        LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `iao`.`organization_site_id`)))\n"
                + "    WHERE\n"
                + "        ((`iao`.`type` = 1)\n"
                + "            AND (`iao`.`date` IS NOT NULL)) ");
        if (itemCardReportSerachBean.getInvItemForm() != null && itemCardReportSerachBean.getInvItemForm().getId() != null) {
            stringBuilder.append(" AND `iaod`.`item_id` = ").append(itemCardReportSerachBean.getInvItemForm().getId());
        }

        if (itemCardReportSerachBean.getInventoryFrom() != null && itemCardReportSerachBean.getInventoryFrom().getId() != null) {
            stringBuilder.append(" AND `iao`.`inv_inventory_id` = ").append(itemCardReportSerachBean.getInventoryFrom().getId());
        }
        if (itemCardReportSerachBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateFrom());
            stringBuilder.append(" AND `iao`.`date` >= '").append(formatDateFrom).append("00:00:00 '");
        }
        if (itemCardReportSerachBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateTo());
            stringBuilder.append(" AND `iao`.`date` <= '").append(formatDateTo).append("23:59:59 '");
        }

        stringBuilder.append(" AND `iao`.`branch_id` = ").append(itemCardReportSerachBean.getBranchId()).append("  \n"
                + "    UNION ALL SELECT \n"
                + "        \n"
                + "        `iao`.`id` AS `id`,\n"
                + "        `iao`.`serial` AS `serial`,\n"
                + "        NULL AS `discount_flag_head`,\n"
                + "        NULL AS `discount_value_head`,\n"
                + "        NULL AS `tax_flag_head`,\n"
                + "        NULL AS `tax_value_head`,\n"
                + "        NULL AS `net_detail_exit`,\n"
                + "        NULL AS `net_detail_adding`,\n"
                + "        NULL AS `payment_type`,\n"
                + "        `iaod`.`quantity` AS `qty`,\n"
                + "        `iao`.`date` AS `date`,\n"
                + "        `ig`.`id` AS `group_id`,\n"
                + "        `ig`.`name` AS `group_name`,\n"
                + "        `ig`.`code` AS `group_code`,\n"
                + "        `ii`.`name` AS `item_name`,\n"
                + "        `ii`.`code` AS `item_code`,\n"
                + "        `ii`.`sell_price` AS `sell_price`,\n"
                + "        `iaod`.`item_barcode` AS `item_barcode`,\n"
                + "        `s`.`name` AS `item_unit_name`,\n"
                + "        `ss`.`name` AS `detail_unit_name`,\n"
                + "        IFNULL(`iaod`.`quantity`, 0) AS `qtyin`,\n"
                + "        0 AS `qtyout`,\n"
                + "        IFNULL(`ii`.`cost_average`, 0) AS `cost_average`,\n"
                + "        IFNULL(`iaod`.`screwing`, 1) AS `screwing`,\n"
                + "        `iaod`.`item_id` AS `item_Id`,\n"
                + "        `iao`.`branch_id` AS `branch_id`,\n"
                + "        `iao`.`post_flag` AS `post_flag`,\n"
                + "        `iao`.`inv_inventory_id` AS `inventory_id`,\n"
                + "        `ini`.`code` AS `inventory_code`,\n"
                + "        `ini`.`name` AS `inventory_name`,\n"
                + "        `iao`.`organization_site_id` AS `organization_site_id`,\n"
                + "        `ios`.`code` AS `organization_code`,\n"
                + "        `ios`.`name` AS `organization_name`,\n"
                + "        `ios`.`type` AS `organization_type`,\n"
                + "        `ios`.`open_balance_debit` AS `organization_open_balance_debit`,\n"
                + "        `ios`.`open_balance_credit` AS `organization_open_balance_credit`,\n"
                + "        `ios`.`currency_id` AS `organization_currency_id`,\n"
                + "        `ios`.`gallary_id` AS `organization_gallary_id`,\n"
                + "        0 AS `weight_detail`,\n"
                + "        0 AS `actual_weight`,\n"
                + "        0 AS `total_actual_weight`,\n"
                + "        0 AS `price_kilo`,\n"
                + "        1 AS `type`,\n"
                + "        'امر اضافة' AS `ScreenName`,\n"
                + "        0 AS `operationType`,\n"
                + "        2 AS `ScreenCode`,\n"
                + "        FIND_SUPPLIER_OF_ITEM(`iaod`.`item_id`) AS `item_supplier_id`,\n"
                + "        0 AS `disc_halala`\n"
                + "    FROM\n"
                + "        (((((((`inv_inventory_transaction` `iao`\n"
                + "        LEFT JOIN `inv_inventory_transaction_detail` `iaod` ON ((`iao`.`id` = `iaod`.`inventory_transaction_id`)))\n"
                + "        LEFT JOIN `inv_item` `ii` ON ((`ii`.`id` = `iaod`.`item_id`)))\n"
                + "        LEFT JOIN `inv_group` `ig` ON ((`ig`.`id` = `ii`.`group_id`)))\n"
                + "        LEFT JOIN `symbol` `s` ON ((`s`.`id` = `ii`.`unit_id`)))\n"
                + "        LEFT JOIN `symbol` `ss` ON ((`ss`.`id` = `iaod`.`unit_id`)))\n"
                + "        LEFT JOIN `inv_inventory` `ini` ON ((`ini`.`id` = `iao`.`inv_inventory_id`)))\n"
                + "        LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `iao`.`organization_site_id`)))\n"
                + "    WHERE\n"
                + "        ((`iao`.`type` = 0)\n"
                + "            AND (`iao`.`date` IS NOT NULL))");
        if (itemCardReportSerachBean.getInvItemForm() != null && itemCardReportSerachBean.getInvItemForm().getId() != null) {
            stringBuilder.append(" AND `iaod`.`item_id` = ").append(itemCardReportSerachBean.getInvItemForm().getId());
        }

        if (itemCardReportSerachBean.getInventoryFrom() != null && itemCardReportSerachBean.getInventoryFrom().getId() != null) {
            stringBuilder.append(" AND `iao`.`inv_inventory_id` = ").append(itemCardReportSerachBean.getInventoryFrom().getId());
        }
        if (itemCardReportSerachBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateFrom());
            stringBuilder.append(" AND `iao`.`date` >= '").append(formatDateFrom).append("00:00:00 '");
        }
        if (itemCardReportSerachBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateTo());
            stringBuilder.append(" AND `iao`.`date` <= '").append(formatDateTo).append("23:59:59 '");
        }
        stringBuilder.append(" AND `iao`.`branch_id` = ").append(itemCardReportSerachBean.getBranchId()).append("  \n"
                + "    UNION ALL SELECT \n"
                + "        \n"
                + "        `ipi`.`id` AS `id`,\n"
                + "        `ipi`.`serial` AS `serial`,\n"
                + "        `ipi`.`discount_type` AS `discount_flag_head`,\n"
                + "        `ipi`.`discount` AS `discount_value_head`,\n"
                + "        `ipi`.`tax_flag` AS `tax_flag_head`,\n"
                + "        15 AS `tax_value_head`,\n"
                + "        NULL AS `net_detail_exit`,\n"
                + "        `ipid`.`net` AS `net_detail_adding`,\n"
                + "        `ipi`.`payment_type` AS `payment_type`,\n"
                + "        `ipid`.`quantity` AS `qty`,\n"
                + "        `ipi`.`date` AS `date`,\n"
                + "        `ig`.`id` AS `group_id`,\n"
                + "        `ig`.`name` AS `group_name`,\n"
                + "        `ig`.`code` AS `group_code`,\n"
                + "        `ii`.`name` AS `item_name`,\n"
                + "        `ii`.`code` AS `item_code`,\n"
                + "        `ii`.`sell_price` AS `sell_price`,\n"
                + "        `ipid`.`item_barcode` AS `item_barcode`,\n"
                + "        `s`.`name` AS `item_unit_name`,\n"
                + "        `ss`.`name` AS `detail_unit_name`,\n"
                + "        IFNULL(`ipid`.`quantity`, 0) AS `qtyin`,\n"
                + "        0 AS `qtyout`,\n"
                + "        IFNULL(`ii`.`cost_average`, 0) AS `cost_average`,\n"
                + "        IFNULL(`ipid`.`screwing`, 1) AS `screwing`,\n"
                + "        `ipid`.`item_id` AS `item_Id`,\n"
                + "        `ipi`.`branch_id` AS `branch_id`,\n"
                + "        `ipi`.`post_flag` AS `post_flag`,\n"
                + "        `ipi`.`inv_inventory_id` AS `inventory_id`,\n"
                + "        `ini`.`code` AS `inventory_code`,\n"
                + "        `ini`.`name` AS `inventory_name`,\n"
                + "        `ipi`.`organization_site_id` AS `organization_site_id`,\n"
                + "        `ios`.`code` AS `organization_code`,\n"
                + "        `ios`.`name` AS `organization_name`,\n"
                + "        `ios`.`type` AS `organization_type`,\n"
                + "        `ios`.`open_balance_debit` AS `organization_open_balance_debit`,\n"
                + "        `ios`.`open_balance_credit` AS `organization_open_balance_credit`,\n"
                + "        `ios`.`currency_id` AS `organization_currency_id`,\n"
                + "        `ios`.`gallary_id` AS `organization_gallary_id`,\n"
                + "        (IFNULL(`ipid`.`weight`, 0) * IFNULL(`ipid`.`quantity`, 0)) AS `weight_detail`,\n"
                + "        IFNULL(`ipi`.`actual_weight`, 0) AS `actual_weight`,\n"
                + "        IFNULL(`ipi`.`total_actual_weight`, 0) AS `total_actual_weight`,\n"
                + "        IFNULL(`ipi`.`price_kilo`, 0) AS `price_kilo`,\n"
                + "        2 AS `type`,\n"
                + "        'فاتورة مشتريات' AS `ScreenName`,\n"
                + "        0 AS `operationType`,\n"
                + "        3 AS `ScreenCode`,\n"
                + "        FIND_SUPPLIER_OF_ITEM(`ipid`.`item_id`) AS `item_supplier_id`,\n"
                + "        IFNULL(`ipi`.`disc_halala`, 0) AS `disc_halala`\n"
                + "    FROM\n"
                + "        (((((((`inv_purchase_invoice` `ipi`\n"
                + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON ((`ipi`.`id` = `ipid`.`inv_purchase_invoice_id`)))\n"
                + "        LEFT JOIN `inv_item` `ii` ON ((`ii`.`id` = `ipid`.`item_id`)))\n"
                + "        LEFT JOIN `inv_group` `ig` ON ((`ig`.`id` = `ii`.`group_id`)))\n"
                + "        LEFT JOIN `symbol` `s` ON ((`s`.`id` = `ii`.`unit_id`)))\n"
                + "        LEFT JOIN `symbol` `ss` ON ((`ss`.`id` = `ipid`.`unit_id`)))\n"
                + "        LEFT JOIN `inv_inventory` `ini` ON ((`ini`.`id` = `ipid`.`inv_inventory_id`)))\n"
                + "        LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `ipi`.`organization_site_id`)))\n"
                + "    WHERE\n"
                + "        ((`ipi`.`type` = 0)\n"
                + "            AND (`ipi`.`date` IS NOT NULL)) ");
        if (itemCardReportSerachBean.getInvItemForm() != null && itemCardReportSerachBean.getInvItemForm().getId() != null) {
            stringBuilder.append(" AND `ipid`.`item_id` = ").append(itemCardReportSerachBean.getInvItemForm().getId());
        }

        if (itemCardReportSerachBean.getInventoryFrom() != null && itemCardReportSerachBean.getInventoryFrom().getId() != null) {
            stringBuilder.append(" AND `ipid`.`inv_inventory_id` = ").append(itemCardReportSerachBean.getInventoryFrom().getId());
        }
        if (itemCardReportSerachBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateFrom());
            stringBuilder.append(" AND `ipi`.`date` >= '").append(formatDateFrom).append("00:00:00 '");
        }
        if (itemCardReportSerachBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateTo());
            stringBuilder.append(" AND `ipi`.`date` <= '").append(formatDateTo).append("23:59:59 '");
        }

        stringBuilder.append(" AND `ipi`.`branch_id` = ").append(itemCardReportSerachBean.getBranchId()).append("  \n"
                + "    UNION ALL SELECT \n"
                + "        \n"
                + "        `ipi`.`id` AS `id`,\n"
                + "        `ipi`.`serial` AS `serial`,\n"
                + "        `ipi`.`discount_type` AS `discount_flag_head`,\n"
                + "        `ipi`.`discount` AS `discount_value_head`,\n"
                + "        `ipi`.`tax_flag` AS `tax_flag_head`,\n"
                + "        15 AS `tax_value_head`,\n"
                + "        `ipid`.`net` AS `net_detail_exit`,\n"
                + "        NULL AS `net_detail_adding`,\n"
                + "        `ipi`.`payment_type` AS `payment_type`,\n"
                + "        `ipid`.`quantity` AS `qty`,\n"
                + "        `ipi`.`date` AS `date`,\n"
                + "        `ig`.`id` AS `group_id`,\n"
                + "        `ig`.`name` AS `group_name`,\n"
                + "        `ig`.`code` AS `group_code`,\n"
                + "        `ii`.`name` AS `item_name`,\n"
                + "        `ii`.`code` AS `item_code`,\n"
                + "        `ii`.`sell_price` AS `sell_price`,\n"
                + "        `ipid`.`item_barcode` AS `item_barcode`,\n"
                + "        `s`.`name` AS `item_unit_name`,\n"
                + "        `ss`.`name` AS `detail_unit_name`,\n"
                + "        0 AS `qtyin`,\n"
                + "        IFNULL(`ipid`.`quantity`, 0) AS `qtyout`,\n"
                + "        IFNULL(`ii`.`cost_average`, 0) AS `cost_average`,\n"
                + "        IFNULL(`ipid`.`screwing`, 1) AS `screwing`,\n"
                + "        `ipid`.`item_id` AS `item_Id`,\n"
                + "        `ipi`.`branch_id` AS `branch_id`,\n"
                + "        `ipi`.`post_flag` AS `post_flag`,\n"
                + "        `ipi`.`inv_inventory_id` AS `inventory_id`,\n"
                + "        `ini`.`code` AS `inventory_code`,\n"
                + "        `ini`.`name` AS `inventory_name`,\n"
                + "        `ipi`.`organization_site_id` AS `organization_site_id`,\n"
                + "        `ios`.`code` AS `organization_code`,\n"
                + "        `ios`.`name` AS `organization_name`,\n"
                + "        `ios`.`type` AS `organization_type`,\n"
                + "        `ios`.`open_balance_debit` AS `organization_open_balance_debit`,\n"
                + "        `ios`.`open_balance_credit` AS `organization_open_balance_credit`,\n"
                + "        `ios`.`currency_id` AS `organization_currency_id`,\n"
                + "        `ios`.`gallary_id` AS `organization_gallary_id`,\n"
                + "        (IFNULL(`ipid`.`weight`, 0) * IFNULL(`ipid`.`quantity`, 0)) AS `weight_detail`,\n"
                + "        IFNULL(`ipi`.`actual_weight`, 0) AS `actual_weight`,\n"
                + "        IFNULL(`ipi`.`total_actual_weight`, 0) AS `total_actual_weight`,\n"
                + "        IFNULL(`ipi`.`price_kilo`, 0) AS `price_kilo`,\n"
                + "        3 AS `type`,\n"
                + "        'فاتورة مبيعات' AS `ScreenName`,\n"
                + "        1 AS `operationType`,\n"
                + "        4 AS `ScreenCode`,\n"
                + "        FIND_SUPPLIER_OF_ITEM(`ipid`.`item_id`) AS `item_supplier_id`,\n"
                + "        IFNULL(`ipi`.`disc_halala`, 0) AS `disc_halala`\n"
                + "    FROM\n"
                + "        (((((((`inv_purchase_invoice` `ipi`\n"
                + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON ((`ipi`.`id` = `ipid`.`inv_purchase_invoice_id`)))\n"
                + "        LEFT JOIN `inv_item` `ii` ON ((`ii`.`id` = `ipid`.`item_id`)))\n"
                + "        LEFT JOIN `inv_group` `ig` ON ((`ig`.`id` = `ii`.`group_id`)))\n"
                + "        LEFT JOIN `symbol` `s` ON ((`s`.`id` = `ii`.`unit_id`)))\n"
                + "        LEFT JOIN `symbol` `ss` ON ((`ss`.`id` = `ipid`.`unit_id`)))\n"
                + "        LEFT JOIN `inv_inventory` `ini` ON ((`ini`.`id` = `ipid`.`inv_inventory_id`)))\n"
                + "        LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `ipi`.`organization_site_id`)))\n"
                + "    WHERE\n"
                + "        ((`ipi`.`type` = 4)\n"
                + "            AND (`ipi`.`date` IS NOT NULL)) ");
        if (itemCardReportSerachBean.getInvItemForm() != null && itemCardReportSerachBean.getInvItemForm().getId() != null) {
            stringBuilder.append(" AND `ipid`.`item_id` = ").append(itemCardReportSerachBean.getInvItemForm().getId());
        }

        if (itemCardReportSerachBean.getInventoryFrom() != null && itemCardReportSerachBean.getInventoryFrom().getId() != null) {
            stringBuilder.append(" AND `ipid`.`inv_inventory_id` = ").append(itemCardReportSerachBean.getInventoryFrom().getId());
        }
        if (itemCardReportSerachBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateFrom());
            stringBuilder.append(" AND `ipi`.`date` >= '").append(formatDateFrom).append("00:00:00 '");
        }
        if (itemCardReportSerachBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateTo());
            stringBuilder.append(" AND `ipi`.`date` <= '").append(formatDateTo).append("23:59:59 '");
        }
        stringBuilder.append(" AND `ipi`.`branch_id` = ").append(itemCardReportSerachBean.getBranchId()).append("  \n"
                + "    UNION ALL SELECT \n"
                + "        \n"
                + "        `ir`.`id` AS `id`,\n"
                + "        `ir`.`serial` AS `serial`,\n"
                + "        NULL AS `discount_flag_head`,\n"
                + "        NULL AS `discount_value_head`,\n"
                + "        NULL AS `tax_flag_head`,\n"
                + "        NULL AS `tax_value_head`,\n"
                + "        NULL AS `net_detail_exit`,\n"
                + "        NULL AS `net_detail_adding`,\n"
                + "        NULL AS `payment_type`,\n"
                + "        `ird`.`amount` AS `qty`,\n"
                + "        `ir`.`reservation_date` AS `date`,\n"
                + "        `ig`.`id` AS `group_id`,\n"
                + "        `ig`.`name` AS `group_name`,\n"
                + "        `ig`.`code` AS `group_code`,\n"
                + "        `ii`.`name` AS `item_name`,\n"
                + "        `ii`.`code` AS `item_code`,\n"
                + "        `ii`.`sell_price` AS `sell_price`,\n"
                + "        `ird`.`item_barcode` AS `item_barcode`,\n"
                + "        `s`.`name` AS `item_unit_name`,\n"
                + "        `ss`.`name` AS `detail_unit_name`,\n"
                + "        0 AS `qtyin`,\n"
                + "        IFNULL(`ird`.`amount`, 0) AS `qtyout`,\n"
                + "        IFNULL(`ii`.`cost_average`, 0) AS `cost_average`,\n"
                + "        IFNULL(`ird`.`screwing`, 1) AS `screwing`,\n"
                + "        `ird`.`item_id` AS `item_Id`,\n"
                + "        `ir`.`branch_id` AS `branch_id`,\n"
                + "        `ir`.`post_flag` AS `post_flag`,\n"
                + "        `ir`.`inv_id` AS `inventory_id`,\n"
                + "        `ini`.`code` AS `inventory_code`,\n"
                + "        `ini`.`name` AS `inventory_name`,\n"
                + "        NULL AS `organization_site_id`,\n"
                + "        NULL AS `organization_code`,\n"
                + "        NULL AS `organization_name`,\n"
                + "        NULL AS `organization_type`,\n"
                + "        NULL AS `organization_open_balance_debit`,\n"
                + "        NULL AS `organization_open_balance_credit`,\n"
                + "        NULL AS `organization_currency_id`,\n"
                + "        NULL AS `organization_gallary_id`,\n"
                + "        0 AS `weight_detail`,\n"
                + "        0 AS `actual_weight`,\n"
                + "        0 AS `total_actual_weight`,\n"
                + "        0 AS `price_kilo`,\n"
                + "        4 AS `type`,\n"
                + "        'حجز بضاعة' AS `ScreenName`,\n"
                + "        1 AS `operationType`,\n"
                + "        5 AS `ScreenCode`,\n"
                + "        FIND_SUPPLIER_OF_ITEM(`ird`.`item_id`) AS `item_supplier_id`,\n"
                + "        0 AS `disc_halala`\n"
                + "    FROM\n"
                + "        ((((((`inv_reservation` `ir`\n"
                + "        LEFT JOIN `inv_reservation_detail` `ird` ON ((`ir`.`id` = `ird`.`reservation_id`)))\n"
                + "        LEFT JOIN `inv_item` `ii` ON ((`ii`.`id` = `ird`.`item_id`)))\n"
                + "        LEFT JOIN `inv_group` `ig` ON ((`ig`.`id` = `ii`.`group_id`)))\n"
                + "        LEFT JOIN `symbol` `s` ON ((`s`.`id` = `ii`.`unit_id`)))\n"
                + "        LEFT JOIN `symbol` `ss` ON ((`ss`.`id` = `ird`.`unit_id`)))\n"
                + "        LEFT JOIN `inv_inventory` `ini` ON ((`ini`.`id` = `ir`.`inv_id`)))\n"
                + "    WHERE\n"
                + "        ((`ir`.`reservation_date` IS NOT NULL)) ");
        if (itemCardReportSerachBean.getInvItemForm() != null && itemCardReportSerachBean.getInvItemForm().getId() != null) {
            stringBuilder.append(" AND `ird`.`item_id` = ").append(itemCardReportSerachBean.getInvItemForm().getId());
        }

        if (itemCardReportSerachBean.getInventoryFrom() != null && itemCardReportSerachBean.getInventoryFrom().getId() != null) {
            stringBuilder.append(" AND `ir`.`inv_id` = ").append(itemCardReportSerachBean.getInventoryFrom().getId());
        }
        if (itemCardReportSerachBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateFrom());
            stringBuilder.append(" AND `ir`.`reservation_date` >= '").append(formatDateFrom).append("00:00:00 '");
        }
        if (itemCardReportSerachBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateTo());
            stringBuilder.append(" AND `ir`.`reservation_date` <= '").append(formatDateTo).append("23:59:59 '");
        }
        stringBuilder.append(" AND `ir`.`branch_id` = ").append(itemCardReportSerachBean.getBranchId()).append("  \n"
                + "    UNION ALL SELECT \n"
                + "        \n"
                + "        `ipi`.`id` AS `id`,\n"
                + "        `ipi`.`serial` AS `serial`,\n"
                + "        `ipi`.`discount_type` AS `discount_flag_head`,\n"
                + "        `ipi`.`discount` AS `discount_value_head`,\n"
                + "        `ipi`.`tax_flag` AS `tax_flag_head`,\n"
                + "        15 AS `tax_value_head`,\n"
                + "        `ipid`.`net` AS `net_detail_exit`,\n"
                + "        NULL AS `net_detail_adding`,\n"
                + "        `ipi`.`payment_type` AS `payment_type`,\n"
                + "        `ipid`.`quantity` AS `qty`,\n"
                + "        `ipi`.`date` AS `date`,\n"
                + "        `ig`.`id` AS `group_id`,\n"
                + "        `ig`.`name` AS `group_name`,\n"
                + "        `ig`.`code` AS `group_code`,\n"
                + "        `ii`.`name` AS `item_name`,\n"
                + "        `ii`.`code` AS `item_code`,\n"
                + "        `ii`.`sell_price` AS `sell_price`,\n"
                + "        `ipid`.`item_barcode` AS `item_barcode`,\n"
                + "        `s`.`name` AS `item_unit_name`,\n"
                + "        `ss`.`name` AS `detail_unit_name`,\n"
                + "        0 AS `qtyin`,\n"
                + "        IFNULL(`ipid`.`quantity`, 0) AS `qtyout`,\n"
                + "        IFNULL(`ii`.`cost_average`, 0) AS `cost_average`,\n"
                + "        IFNULL(`ipid`.`screwing`, 1) AS `screwing`,\n"
                + "        `ipid`.`item_id` AS `item_Id`,\n"
                + "        `ipi`.`branch_id` AS `branch_id`,\n"
                + "        `ipi`.`post_flag` AS `post_flag`,\n"
                + "        `ipi`.`inv_inventory_id` AS `inventory_id`,\n"
                + "        `ini`.`code` AS `inventory_code`,\n"
                + "        `ini`.`name` AS `inventory_name`,\n"
                + "        `ipi`.`organization_site_id` AS `organization_site_id`,\n"
                + "        `ios`.`code` AS `organization_code`,\n"
                + "        `ios`.`name` AS `organization_name`,\n"
                + "        `ios`.`type` AS `organization_type`,\n"
                + "        `ios`.`open_balance_debit` AS `organization_open_balance_debit`,\n"
                + "        `ios`.`open_balance_credit` AS `organization_open_balance_credit`,\n"
                + "        `ios`.`currency_id` AS `organization_currency_id`,\n"
                + "        `ios`.`gallary_id` AS `organization_gallary_id`,\n"
                + "        (IFNULL(`ipid`.`weight`, 0) * IFNULL(`ipid`.`quantity`, 0)) AS `weight_detail`,\n"
                + "        IFNULL(`ipi`.`actual_weight`, 0) AS `actual_weight`,\n"
                + "        IFNULL(`ipi`.`total_actual_weight`, 0) AS `total_actual_weight`,\n"
                + "        IFNULL(`ipi`.`price_kilo`, 0) AS `price_kilo`,\n"
                + "        5 AS `type`,\n"
                + "        'مردودات المشتريات' AS `ScreenName`,\n"
                + "        1 AS `operationType`,\n"
                + "        6 AS `ScreenCode`,\n"
                + "        FIND_SUPPLIER_OF_ITEM(`ipid`.`item_id`) AS `item_supplier_id`,\n"
                + "        IFNULL(`ipi`.`disc_halala`, 0) AS `disc_halala`\n"
                + "    FROM\n"
                + "        (((((((`inv_purchase_invoice` `ipi`\n"
                + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON ((`ipi`.`id` = `ipid`.`inv_purchase_invoice_id`)))\n"
                + "        LEFT JOIN `inv_item` `ii` ON ((`ii`.`id` = `ipid`.`item_id`)))\n"
                + "        LEFT JOIN `inv_group` `ig` ON ((`ig`.`id` = `ii`.`group_id`)))\n"
                + "        LEFT JOIN `symbol` `s` ON ((`s`.`id` = `ii`.`unit_id`)))\n"
                + "        LEFT JOIN `symbol` `ss` ON ((`ss`.`id` = `ipid`.`unit_id`)))\n"
                + "        LEFT JOIN `inv_inventory` `ini` ON ((`ini`.`id` = `ipid`.`inv_inventory_id`)))\n"
                + "        LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `ipi`.`organization_site_id`)))\n"
                + "    WHERE\n"
                + "        ((`ipi`.`type` = 2)\n"
                + "            AND (`ipi`.`date` IS NOT NULL)) ");
        if (itemCardReportSerachBean.getInvItemForm() != null && itemCardReportSerachBean.getInvItemForm().getId() != null) {
            stringBuilder.append(" AND `ipid`.`item_id` = ").append(itemCardReportSerachBean.getInvItemForm().getId());
        }

        if (itemCardReportSerachBean.getInventoryFrom() != null && itemCardReportSerachBean.getInventoryFrom().getId() != null) {
            stringBuilder.append(" AND `ipid`.`inv_inventory_id` = ").append(itemCardReportSerachBean.getInventoryFrom().getId());
        }
        if (itemCardReportSerachBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateFrom());
            stringBuilder.append(" AND `ipi`.`date` >= '").append(formatDateFrom).append("00:00:00 '");
        }
        if (itemCardReportSerachBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateTo());
            stringBuilder.append(" AND `ipi`.`date` <= '").append(formatDateTo).append("23:59:59 '");
        }

        stringBuilder.append(" AND `ipi`.`branch_id` = ").append(itemCardReportSerachBean.getBranchId()).append("  \n"
                + "    UNION ALL SELECT \n"
                + "        \n"
                + "        `ipi`.`id` AS `id`,\n"
                + "        `ipi`.`serial` AS `serial`,\n"
                + "        `ipi`.`discount_type` AS `discount_flag_head`,\n"
                + "        `ipi`.`discount` AS `discount_value_head`,\n"
                + "        `ipi`.`tax_flag` AS `tax_flag_head`,\n"
                + "        15 AS `tax_value_head`,\n"
                + "        NULL AS `net_detail_exit`,\n"
                + "        `ipid`.`net` AS `net_detail_adding`,\n"
                + "        `ipi`.`payment_type` AS `payment_type`,\n"
                + "        `ipid`.`quantity` AS `qty`,\n"
                + "        `ipi`.`date` AS `date`,\n"
                + "        `ig`.`id` AS `group_id`,\n"
                + "        `ig`.`name` AS `group_name`,\n"
                + "        `ig`.`code` AS `group_code`,\n"
                + "        `ii`.`name` AS `item_name`,\n"
                + "        `ii`.`code` AS `item_code`,\n"
                + "        `ii`.`sell_price` AS `sell_price`,\n"
                + "        `ipid`.`item_barcode` AS `item_barcode`,\n"
                + "        `s`.`name` AS `item_unit_name`,\n"
                + "        `ss`.`name` AS `detail_unit_name`,\n"
                + "        IFNULL(`ipid`.`quantity`, 0) AS `qtyin`,\n"
                + "        0 AS `qtyout`,\n"
                + "        IFNULL(`ii`.`cost_average`, 0) AS `cost_average`,\n"
                + "        IFNULL(`ipid`.`screwing`, 1) AS `screwing`,\n"
                + "        `ipid`.`item_id` AS `item_Id`,\n"
                + "        `ipi`.`branch_id` AS `branch_id`,\n"
                + "        `ipi`.`post_flag` AS `post_flag`,\n"
                + "        `ipi`.`inv_inventory_id` AS `inventory_id`,\n"
                + "        `ini`.`code` AS `inventory_code`,\n"
                + "        `ini`.`name` AS `inventory_name`,\n"
                + "        `ipi`.`organization_site_id` AS `organization_site_id`,\n"
                + "        `ios`.`code` AS `organization_code`,\n"
                + "        `ios`.`name` AS `organization_name`,\n"
                + "        `ios`.`type` AS `organization_type`,\n"
                + "        `ios`.`open_balance_debit` AS `organization_open_balance_debit`,\n"
                + "        `ios`.`open_balance_credit` AS `organization_open_balance_credit`,\n"
                + "        `ios`.`currency_id` AS `organization_currency_id`,\n"
                + "        `ios`.`gallary_id` AS `organization_gallary_id`,\n"
                + "        (IFNULL(`ipid`.`weight`, 0) * IFNULL(`ipid`.`quantity`, 0)) AS `weight_detail`,\n"
                + "        IFNULL(`ipi`.`actual_weight`, 0) AS `actual_weight`,\n"
                + "        IFNULL(`ipi`.`total_actual_weight`, 0) AS `total_actual_weight`,\n"
                + "        IFNULL(`ipi`.`price_kilo`, 0) AS `price_kilo`,\n"
                + "        6 AS `type`,\n"
                + "        'مردودات المبيعات' AS `ScreenName`,\n"
                + "        0 AS `operationType`,\n"
                + "        7 AS `ScreenCode`,\n"
                + "        FIND_SUPPLIER_OF_ITEM(`ipid`.`item_id`) AS `item_supplier_id`,\n"
                + "        IFNULL(`ipi`.`disc_halala`, 0) AS `disc_halala`\n"
                + "    FROM\n"
                + "        (((((((`inv_purchase_invoice` `ipi`\n"
                + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON ((`ipi`.`id` = `ipid`.`inv_purchase_invoice_id`)))\n"
                + "        LEFT JOIN `inv_item` `ii` ON ((`ii`.`id` = `ipid`.`item_id`)))\n"
                + "        LEFT JOIN `inv_group` `ig` ON ((`ig`.`id` = `ii`.`group_id`)))\n"
                + "        LEFT JOIN `symbol` `s` ON ((`s`.`id` = `ii`.`unit_id`)))\n"
                + "        LEFT JOIN `symbol` `ss` ON ((`ss`.`id` = `ipid`.`unit_id`)))\n"
                + "        LEFT JOIN `inv_inventory` `ini` ON ((`ini`.`id` = `ipid`.`inv_inventory_id`)))\n"
                + "        LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `ipi`.`organization_site_id`)))\n"
                + "    WHERE\n"
                + "        ((`ipi`.`type` = 3)\n"
                + "            AND (`ipi`.`date` IS NOT NULL)) ");
       if (itemCardReportSerachBean.getInvItemForm() != null && itemCardReportSerachBean.getInvItemForm().getId() != null) {
            stringBuilder.append(" AND `ipid`.`item_id` = ").append(itemCardReportSerachBean.getInvItemForm().getId());
        }

        if (itemCardReportSerachBean.getInventoryFrom() != null && itemCardReportSerachBean.getInventoryFrom().getId() != null) {
            stringBuilder.append(" AND `ipid`.`inv_inventory_id` = ").append(itemCardReportSerachBean.getInventoryFrom().getId());
        }
        if (itemCardReportSerachBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateFrom());
            stringBuilder.append(" AND `ipi`.`date` >= '").append(formatDateFrom).append("00:00:00 '");
        }
        if (itemCardReportSerachBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateTo());
            stringBuilder.append(" AND `ipi`.`date` <= '").append(formatDateTo).append("23:59:59 '");
        }
        stringBuilder.append(" AND `ipi`.`branch_id` = ").append(itemCardReportSerachBean.getBranchId()).append("  \n"
                + "    UNION ALL SELECT \n"
                + "        \n"
                + "        `it`.`id` AS `id`,\n"
                + "        `it`.`serial_no` AS `serial`,\n"
                + "        NULL AS `discount_flag_head`,\n"
                + "        NULL AS `discount_value_head`,\n"
                + "        NULL AS `tax_flag_head`,\n"
                + "        NULL AS `tax_value_head`,\n"
                + "        NULL AS `net_detail_exit`,\n"
                + "        NULL AS `net_detail_adding`,\n"
                + "        NULL AS `payment_type`,\n"
                + "        `itd`.`amount` AS `qty`,\n"
                + "        `it`.`transfer_date` AS `date`,\n"
                + "        `ig`.`id` AS `group_id`,\n"
                + "        `ig`.`name` AS `group_name`,\n"
                + "        `ig`.`code` AS `group_code`,\n"
                + "        `ii`.`name` AS `item_name`,\n"
                + "        `ii`.`code` AS `item_code`,\n"
                + "        `ii`.`sell_price` AS `sell_price`,\n"
                + "        `itd`.`item_barcode` AS `item_barcode`,\n"
                + "        `s`.`name` AS `item_unit_name`,\n"
                + "        `ss`.`name` AS `detail_unit_name`,\n"
                + "        0 AS `qtyin`,\n"
                + "        IFNULL(`itd`.`amount`, 0) AS `qtyout`,\n"
                + "        IFNULL(`ii`.`cost_average`, 0) AS `cost_average`,\n"
                + "        IFNULL(`itd`.`screwing`, 1) AS `screwing`,\n"
                + "        `itd`.`inv_item_id` AS `item_Id`,\n"
                + "        `it`.`branch_id` AS `branch_id`,\n"
                + "        `it`.`post_flag` AS `post_flag`,\n"
                + "        `it`.`inv_from` AS `inventory_id`,\n"
                + "        `ini`.`code` AS `inventory_code`,\n"
                + "        `ini`.`name` AS `inventory_name`,\n"
                + "        NULL AS `organization_site_id`,\n"
                + "        NULL AS `organization_code`,\n"
                + "        NULL AS `organization_name`,\n"
                + "        NULL AS `organization_type`,\n"
                + "        NULL AS `organization_open_balance_debit`,\n"
                + "        NULL AS `organization_open_balance_credit`,\n"
                + "        NULL AS `organization_currency_id`,\n"
                + "        NULL AS `organization_gallary_id`,\n"
                + "        0 AS `weight_detail`,\n"
                + "        0 AS `actual_weight`,\n"
                + "        0 AS `total_actual_weight`,\n"
                + "        0 AS `price_kilo`,\n"
                + "        7 AS `type`,\n"
                + "        'تحويل بين المخازن من ' AS `ScreenName`,\n"
                + "        0 AS `operationType`,\n"
                + "        8 AS `ScreenCode`,\n"
                + "        FIND_SUPPLIER_OF_ITEM(`itd`.`inv_item_id`) AS `item_supplier_id`,\n"
                + "        0 AS `disc_halala`\n"
                + "    FROM\n"
                + "        ((((((`inv_transfer` `it`\n"
                + "        LEFT JOIN `inv_transfer_detail` `itd` ON ((`it`.`id` = `itd`.`inv_transfer_id`)))\n"
                + "        LEFT JOIN `inv_item` `ii` ON ((`ii`.`id` = `itd`.`inv_item_id`)))\n"
                + "        LEFT JOIN `inv_group` `ig` ON ((`ig`.`id` = `ii`.`group_id`)))\n"
                + "        LEFT JOIN `symbol` `s` ON ((`s`.`id` = `ii`.`unit_id`)))\n"
                + "        LEFT JOIN `symbol` `ss` ON ((`ss`.`id` = `itd`.`unit_id`)))\n"
                + "        LEFT JOIN `inv_inventory` `ini` ON ((`ini`.`id` = `it`.`inv_from`)))\n"
                + "    WHERE\n"
                + "        ((`it`.`transfer_date` IS NOT NULL)\n"
                + "            AND (`it`.`transfer_type` = 0)) ");
        if (itemCardReportSerachBean.getInvItemForm() != null && itemCardReportSerachBean.getInvItemForm().getId() != null) {
            stringBuilder.append(" AND `itd`.`inv_item_id` = ").append(itemCardReportSerachBean.getInvItemForm().getId());
        }

        if (itemCardReportSerachBean.getInventoryFrom() != null && itemCardReportSerachBean.getInventoryFrom().getId() != null) {
            stringBuilder.append(" AND `it`.`inv_from` = ").append(itemCardReportSerachBean.getInventoryFrom().getId());
        }
        if (itemCardReportSerachBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateFrom());
            stringBuilder.append(" AND `it`.`transfer_date` >= '").append(formatDateFrom).append("00:00:00 '");
        }
        if (itemCardReportSerachBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateTo());
            stringBuilder.append(" AND `it`.`transfer_date` <= '").append(formatDateTo).append("23:59:59 '");
        }
        stringBuilder.append(" AND `it`.`branch_id` = ").append(itemCardReportSerachBean.getBranchId()).append("  \n"
                + "    UNION ALL SELECT \n"
                + "        \n"
                + "        `it`.`id` AS `id`,\n"
                + "        `it`.`serial_no` AS `serial`,\n"
                + "        NULL AS `discount_flag_head`,\n"
                + "        NULL AS `discount_value_head`,\n"
                + "        NULL AS `tax_flag_head`,\n"
                + "        NULL AS `tax_value_head`,\n"
                + "        NULL AS `net_detail_exit`,\n"
                + "        NULL AS `net_detail_adding`,\n"
                + "        NULL AS `payment_type`,\n"
                + "        `itd`.`amount` AS `qty`,\n"
                + "        `it`.`transfer_date` AS `date`,\n"
                + "        `ig`.`id` AS `group_id`,\n"
                + "        `ig`.`name` AS `group_name`,\n"
                + "        `ig`.`code` AS `group_code`,\n"
                + "        `ii`.`name` AS `item_name`,\n"
                + "        `ii`.`code` AS `item_code`,\n"
                + "        `ii`.`sell_price` AS `sell_price`,\n"
                + "        `itd`.`item_barcode` AS `item_barcode`,\n"
                + "        `s`.`name` AS `item_unit_name`,\n"
                + "        `ss`.`name` AS `detail_unit_name`,\n"
                + "        IFNULL(`itd`.`amount`, 0) AS `qtyin`,\n"
                + "        0 AS `qtyout`,\n"
                + "        IFNULL(`ii`.`cost_average`, 0) AS `cost_average`,\n"
                + "        IFNULL(`itd`.`screwing`, 1) AS `screwing`,\n"
                + "        `itd`.`inv_item_id` AS `item_Id`,\n"
                + "        `it`.`branch_id` AS `branch_id`,\n"
                + "        `it`.`post_flag` AS `post_flag`,\n"
                + "        `it`.`inv_to` AS `inventory_id`,\n"
                + "        `ini`.`code` AS `inventory_code`,\n"
                + "        `ini`.`name` AS `inventory_name`,\n"
                + "        NULL AS `organization_site_id`,\n"
                + "        NULL AS `organization_code`,\n"
                + "        NULL AS `organization_name`,\n"
                + "        NULL AS `organization_type`,\n"
                + "        NULL AS `organization_open_balance_debit`,\n"
                + "        NULL AS `organization_open_balance_credit`,\n"
                + "        NULL AS `organization_currency_id`,\n"
                + "        NULL AS `organization_gallary_id`,\n"
                + "        0 AS `weight_detail`,\n"
                + "        0 AS `actual_weight`,\n"
                + "        0 AS `total_actual_weight`,\n"
                + "        0 AS `price_kilo`,\n"
                + "        8 AS `type`,\n"
                + "        'تحويل بين المخازن الى' AS `ScreenName`,\n"
                + "        0 AS `operationType`,\n"
                + "        9 AS `ScreenCode`,\n"
                + "        FIND_SUPPLIER_OF_ITEM(`itd`.`inv_item_id`) AS `item_supplier_id`,\n"
                + "        0 AS `disc_halala`\n"
                + "    FROM\n"
                + "        ((((((`inv_transfer` `it`\n"
                + "        LEFT JOIN `inv_transfer_detail` `itd` ON ((`it`.`id` = `itd`.`inv_transfer_id`)))\n"
                + "        LEFT JOIN `inv_item` `ii` ON ((`ii`.`id` = `itd`.`inv_item_id`)))\n"
                + "        LEFT JOIN `inv_group` `ig` ON ((`ig`.`id` = `ii`.`group_id`)))\n"
                + "        LEFT JOIN `symbol` `s` ON ((`s`.`id` = `ii`.`unit_id`)))\n"
                + "        LEFT JOIN `symbol` `ss` ON ((`ss`.`id` = `itd`.`unit_id`)))\n"
                + "        LEFT JOIN `inv_inventory` `ini` ON ((`ini`.`id` = `it`.`inv_to`)))\n"
                + "    WHERE\n"
                + "        ((`it`.`transfer_date` IS NOT NULL)\n"
                + "            AND (`it`.`transfer_type` = 0)) ");
        if (itemCardReportSerachBean.getInvItemForm() != null && itemCardReportSerachBean.getInvItemForm().getId() != null) {
            stringBuilder.append(" AND `itd`.`inv_item_id` = ").append(itemCardReportSerachBean.getInvItemForm().getId());
        }

        if (itemCardReportSerachBean.getInventoryFrom() != null && itemCardReportSerachBean.getInventoryFrom().getId() != null) {
            stringBuilder.append(" AND `it`.`inv_to` = ").append(itemCardReportSerachBean.getInventoryFrom().getId());
        }
        if (itemCardReportSerachBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateFrom());
            stringBuilder.append(" AND `it`.`transfer_date` >= '").append(formatDateFrom).append("00:00:00 '");
        }
        if (itemCardReportSerachBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateTo());
            stringBuilder.append(" AND `it`.`transfer_date` <= '").append(formatDateTo).append("23:59:59 '");
        }

        stringBuilder.append(" AND `it`.`branch_id` = ").append(itemCardReportSerachBean.getBranchId()).append("  \n"
                + "    UNION ALL SELECT \n"
                + "        \n"
                + "        `is`.`id` AS `id`,\n"
                + "        `is`.`serial` AS `serial`,\n"
                + "        NULL AS `discount_flag_head`,\n"
                + "        NULL AS `discount_value_head`,\n"
                + "        NULL AS `tax_flag_head`,\n"
                + "        NULL AS `tax_value_head`,\n"
                + "        NULL AS `net_detail_exit`,\n"
                + "        NULL AS `net_detail_adding`,\n"
                + "        NULL AS `payment_type`,\n"
                + "        `isd`.`difference` AS `qty`,\n"
                + "        `is`.`date` AS `date`,\n"
                + "        `ig`.`id` AS `group_id`,\n"
                + "        `ig`.`name` AS `group_name`,\n"
                + "        `ig`.`code` AS `group_code`,\n"
                + "        `ii`.`name` AS `item_name`,\n"
                + "        `ii`.`code` AS `item_code`,\n"
                + "        `ii`.`sell_price` AS `sell_price`,\n"
                + "        `isd`.`item_barcode` AS `item_barcode`,\n"
                + "        `s`.`name` AS `item_unit_name`,\n"
                + "        `ss`.`name` AS `detail_unit_name`,\n"
                + "        `isd`.`difference` AS `qtyin`,\n"
                + "        0 AS `qtyout`,\n"
                + "        IFNULL(`ii`.`cost_average`, 0) AS `cost_average`,\n"
                + "        IFNULL(`isd`.`screwing`, 1) AS `screwing`,\n"
                + "        `isd`.`inv_item_id` AS `item_Id`,\n"
                + "        `is`.`branch_id` AS `branch_id`,\n"
                + "        `is`.`post_flag` AS `post_flag`,\n"
                + "        `is`.`inventory_id` AS `inventory_id`,\n"
                + "        `ini`.`code` AS `inventory_code`,\n"
                + "        `ini`.`name` AS `inventory_name`,\n"
                + "        NULL AS `organization_site_id`,\n"
                + "        NULL AS `organization_code`,\n"
                + "        NULL AS `organization_name`,\n"
                + "        NULL AS `organization_type`,\n"
                + "        NULL AS `organization_open_balance_debit`,\n"
                + "        NULL AS `organization_open_balance_credit`,\n"
                + "        NULL AS `organization_currency_id`,\n"
                + "        NULL AS `organization_gallary_id`,\n"
                + "        0 AS `weight_detail`,\n"
                + "        0 AS `actual_weight`,\n"
                + "        0 AS `total_actual_weight`,\n"
                + "        0 AS `price_kilo`,\n"
                + "        9 AS `type`,\n"
                + "        'ضبط  المخزون' AS `ScreenName`,\n"
                + "        0 AS `operationType`,\n"
                + "        10 AS `ScreenCode`,\n"
                + "        FIND_SUPPLIER_OF_ITEM(`isd`.`inv_item_id`) AS `item_supplier_id`,\n"
                + "        0 AS `disc_halala`\n"
                + "    FROM\n"
                + "        ((((((`inv_update` `is`\n"
                + "        LEFT JOIN `inv_update_detail` `isd` ON ((`is`.`id` = `isd`.`inv_update_id`)))\n"
                + "        LEFT JOIN `inv_item` `ii` ON ((`ii`.`id` = `isd`.`inv_item_id`)))\n"
                + "        LEFT JOIN `inv_group` `ig` ON ((`ig`.`id` = `ii`.`group_id`)))\n"
                + "        LEFT JOIN `symbol` `s` ON ((`s`.`id` = `ii`.`unit_id`)))\n"
                + "        LEFT JOIN `symbol` `ss` ON ((`ss`.`id` = `isd`.`unit_id`)))\n"
                + "        LEFT JOIN `inv_inventory` `ini` ON ((`ini`.`id` = `is`.`inventory_id`)))\n"
                + "    WHERE\n"
                + "        (`is`.`date` IS NOT NULL) ");
         if (itemCardReportSerachBean.getInvItemForm() != null && itemCardReportSerachBean.getInvItemForm().getId() != null) {
            stringBuilder.append(" AND `isd`.`inv_item_id` = ").append(itemCardReportSerachBean.getInvItemForm().getId());
        }

        if (itemCardReportSerachBean.getInventoryFrom() != null && itemCardReportSerachBean.getInventoryFrom().getId() != null) {
            stringBuilder.append(" AND `is`.`inventory_id` = ").append(itemCardReportSerachBean.getInventoryFrom().getId());
        }
        if (itemCardReportSerachBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateFrom());
            stringBuilder.append(" AND `is`.`date` >= '").append(formatDateFrom).append("00:00:00 '");
        }
        if (itemCardReportSerachBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateTo());
            stringBuilder.append(" AND `is`.`date` <= '").append(formatDateTo).append("23:59:59 '");
        }
        stringBuilder.append(" AND `is`.`branch_id` = ").append(itemCardReportSerachBean.getBranchId()).append("  \n"
                + "    UNION ALL SELECT \n"
                + "        \n"
                + "        `iobi`.`id` AS `id`,\n"
                + "        `iobi`.`serial` AS `serial`,\n"
                + "        NULL AS `discount_flag_head`,\n"
                + "        NULL AS `discount_value_head`,\n"
                + "        NULL AS `tax_flag_head`,\n"
                + "        NULL AS `tax_value_head`,\n"
                + "        NULL AS `net_detail_exit`,\n"
                + "        `iobid`.`net` AS `net_detail_adding`,\n"
                + "        NULL AS `payment_type`,\n"
                + "        `iobid`.`quantity` AS `qty`,\n"
                + "        `iobi`.`date` AS `date`,\n"
                + "        `ig`.`id` AS `group_id`,\n"
                + "        `ig`.`name` AS `group_name`,\n"
                + "        `ig`.`code` AS `group_code`,\n"
                + "        `ii`.`name` AS `item_name`,\n"
                + "        `ii`.`code` AS `item_code`,\n"
                + "        `ii`.`sell_price` AS `sell_price`,\n"
                + "        `ii`.`code` AS `item_barcode`,\n"
                + "        `s`.`name` AS `item_unit_name`,\n"
                + "        `ss`.`name` AS `detail_unit_name`,\n"
                + "        `iobid`.`quantity` AS `qtyin`,\n"
                + "        0 AS `qtyout`,\n"
                + "        IFNULL(`ii`.`cost_average`, 0) AS `cost_average`,\n"
                + "        IFNULL(`iobid`.`screwing`, 1) AS `screwing`,\n"
                + "        `iobid`.`item_id` AS `item_Id`,\n"
                + "        `iobi`.`branch_id` AS `branch_id`,\n"
                + "        `iobi`.`post_flag` AS `post_flag`,\n"
                + "        `iobi`.`inv_inventory_id` AS `inventory_id`,\n"
                + "        `ini`.`code` AS `inventory_code`,\n"
                + "        `ini`.`name` AS `inventory_name`,\n"
                + "        NULL AS `organization_site_id`,\n"
                + "        NULL AS `organization_code`,\n"
                + "        NULL AS `organization_name`,\n"
                + "        NULL AS `organization_type`,\n"
                + "        NULL AS `organization_open_balance_debit`,\n"
                + "        NULL AS `organization_open_balance_credit`,\n"
                + "        NULL AS `organization_currency_id`,\n"
                + "        NULL AS `organization_gallary_id`,\n"
                + "        0 AS `weight_detail`,\n"
                + "        0 AS `actual_weight`,\n"
                + "        0 AS `total_actual_weight`,\n"
                + "        0 AS `price_kilo`,\n"
                + "        10 AS `type`,\n"
                + "        'رصيد افتتاحي للاصناف' AS `ScreenName`,\n"
                + "        0 AS `operationType`,\n"
                + "        11 AS `ScreenCode`,\n"
                + "        FIND_SUPPLIER_OF_ITEM(`iobid`.`item_id`) AS `item_supplier_id`,\n"
                + "        0 AS `disc_halala`\n"
                + "    FROM\n"
                + "        ((((((`inv_openning_balance_item` `iobi`\n"
                + "        LEFT JOIN `inv_openning_balance_item_detail` `iobid` ON ((`iobi`.`id` = `iobid`.`inv_openning_balance_item_id`)))\n"
                + "        LEFT JOIN `inv_item` `ii` ON ((`ii`.`id` = `iobid`.`item_id`)))\n"
                + "        LEFT JOIN `inv_group` `ig` ON ((`ig`.`id` = `ii`.`group_id`)))\n"
                + "        LEFT JOIN `symbol` `s` ON ((`s`.`id` = `ii`.`unit_id`)))\n"
                + "        LEFT JOIN `symbol` `ss` ON ((`ss`.`id` = `iobid`.`unit_id`)))\n"
                + "        LEFT JOIN `inv_inventory` `ini` ON ((`ini`.`id` = `iobi`.`inv_inventory_id`)))\n"
                + "    WHERE\n"
                + "        (`iobi`.`date` IS NOT NULL) ");
         if (itemCardReportSerachBean.getInvItemForm() != null && itemCardReportSerachBean.getInvItemForm().getId() != null) {
            stringBuilder.append(" AND `iobid`.`item_id` = ").append(itemCardReportSerachBean.getInvItemForm().getId());
        }

        if (itemCardReportSerachBean.getInventoryFrom() != null && itemCardReportSerachBean.getInventoryFrom().getId() != null) {
            stringBuilder.append(" AND `iobi`.`inv_inventory_id` = ").append(itemCardReportSerachBean.getInventoryFrom().getId());
        }
        if (itemCardReportSerachBean.getDateFrom() != null) {
            String formatDateFrom = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateFrom());
            stringBuilder.append(" AND `iobi`.`date` >= '").append(formatDateFrom).append(" 00:00:00'");
        }
        if (itemCardReportSerachBean.getDateTo() != null) {
            String formatDateTo = new SimpleDateFormat("yyyy-MM-dd").format(itemCardReportSerachBean.getDateTo());
            stringBuilder.append(" AND `iobi`.`date` <= '").append(formatDateTo).append(" 23:59:59'");
        }
        stringBuilder.append(" AND `iobi`.`branch_id` = ").append(itemCardReportSerachBean.getBranchId()).append("  \n"
                + "                                  ");

        stringBuilder.append(" ) IDV order by IDV.item_id , IDV.date ASC");
        List<Object[]> res = dao.executeNativeQuery(stringBuilder.toString());

        List<ItemsDataView> newList = new ArrayList<>();
        Iterator it = res.iterator();
        while (it.hasNext()) {
            Object[] line = (Object[]) it.next();
            ItemsDataView itemsDataView = new ItemsDataView();
            itemsDataView.setId((Integer) line[0]);
            itemsDataView.setSerial((Integer) line[1]);
            itemsDataView.setQty((BigDecimal) line[9]);
            itemsDataView.setDate((Date) line[10]);
            itemsDataView.setGroupId((Integer) line[11]);
            itemsDataView.setGroupName((String) line[12]);
            itemsDataView.setGroupCode((String) line[13]);
            itemsDataView.setItemName((String) line[14]);
            itemsDataView.setItemCode((String) line[15]);
            itemsDataView.setSellPrice((BigDecimal) line[16]);
            itemsDataView.setItemBarcode((String) line[17]);
            itemsDataView.setItemUnitName((String) line[18]);
            itemsDataView.setDetailUnitName((String) line[19]);
            itemsDataView.setQtyin((BigDecimal) line[20]);
            itemsDataView.setQtyout((BigDecimal) line[21]);
            itemsDataView.setCostAverage((BigDecimal) line[22]);
            itemsDataView.setScrewing((BigDecimal) line[23]);
            itemsDataView.setItemId((Integer) line[24]);
            itemsDataView.setBranchId((Integer) line[25]);
            itemsDataView.setPostFlag((Integer) line[26]);
            itemsDataView.setInventoryId((Integer) line[27]);
            itemsDataView.setInventoryCode((String) line[28]);
            itemsDataView.setInventoryName((String) line[29]);
            itemsDataView.setOrganizationSiteId((Integer) line[30]);
            itemsDataView.setOrganizationCode((String) line[31]);
            itemsDataView.setOrganizationName((String) line[32]);
            itemsDataView.setOrganizationType((Integer) line[33]);
            itemsDataView.setOrganizationOpenBalanceDebit((BigDecimal) line[34]);
            itemsDataView.setOrganizationOpenBalanceCredit((BigDecimal) line[35]);
            itemsDataView.setOrganizationCurrencyId((Integer) line[36]);
            itemsDataView.setType(((Long) line[42]).intValue());
            itemsDataView.setScreenName((String) line[43]);
            itemsDataView.setOperationType(((Long) line[44]).intValue());
            itemsDataView.setScreenCode(((Long) line[45]).intValue());
            itemsDataView.setItemSupplierId((Integer) line[46]);

            newList.add(itemsDataView);
        }

        for (ItemsDataView dataView : newList) {
            dataView.setOpeningBalance(itemOpeningBalanceMap.get(dataView.getItemId()));
            //i will set in organizationopenbalance credit and debit fields and use them as total value for qtyin and qty out
            dataView.setTotalQtyIn(qtyInMap.get(dataView.getItemId()));
            dataView.setTotalQtyOut(qtyOutMap.get(dataView.getItemId()));
            if (itemOpeningBalanceAfterAllDateMap.containsKey(dataView.getItemId())) {
                itemOpeningBalanceAfterAllDateMap.remove(dataView.getItemId());
            }
        }
//if search after all dates of item and need to get the open balance for them
        for (Map.Entry<Integer, String> entry : itemOpeningBalanceAfterAllDateMap.entrySet()) {
            ItemsDataView idv = new ItemsDataView();
            idv.setItemName(entry.getValue());
            idv.setItemId(entry.getKey());
            idv.setOpeningBalance(itemOpeningBalanceMap.get(entry.getKey()));
            idv.setTotalQtyIn(qtyInMap.get(entry.getKey()));
            idv.setTotalQtyOut(qtyOutMap.get(entry.getKey()));
            newList.add(idv);
        }

//        TreeMap<Date,ItemsDataView> treeMap = new TreeMap<>();
//        for(ItemsDataView dataView : itemsDataViewList){
//            treeMap.put(dataView.getDate(), dataView);
//        }
//        itemsDataViewList = new ArrayList<>();
//        itemsDataViewList = new ArrayList<ItemsDataView>(treeMap.values());
        return newList;
    }

    @Override
    public InvItemDTO findInvItem(Integer invItemById, TobyUser tobyUser) {
        InvItem invItem = dao.findEntityById(InvItem.class, invItemById);
        return mapToDTO(invItem, tobyUser);
    }

    private String appendGlYear(ItemCardReportSerachBean itemCardReportSerachBean, GlYear glYear, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();
        if (itemCardReportSerachBean.getDateFrom() != null) {
            params.put("dateFrom", glYear.getDateFrom());
            appendQuery.append(" AND IDV.date >= :dateFrom");
        }

        if (itemCardReportSerachBean.getDateTo() != null) {
            params.put("dateTo", itemCardReportSerachBean.getDateFrom());
            appendQuery.append(" AND IDV.date <= :dateTo");
        }

        return appendQuery.toString();
    }

    private String appendTillDate(ItemCardReportSerachBean itemCardReportSerachBean, GlYear glYear, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();
        if (itemCardReportSerachBean.getDateFrom() != null) {
            params.put("dateTillFrom", itemCardReportSerachBean.getDateFrom());
        }
        appendQuery.append(" AND IDV.date < :dateTillFrom");

        return appendQuery.toString();
    }

    private String appendInventory(ItemCardReportSerachBean itemCardReportSerachBean, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();
        if (itemCardReportSerachBean.getInventoryFrom() != null && itemCardReportSerachBean.getInventoryFrom().getCode() != null) {
            params.put("inventoryFrom", itemCardReportSerachBean.getInventoryFrom().getCode());
            appendQuery.append(" AND IDV.inventoryCode >= :inventoryFrom");
        }

        if (itemCardReportSerachBean.getInventoryTo() != null && itemCardReportSerachBean.getInventoryTo().getCode() != null) {
            params.put("inventoryTo", itemCardReportSerachBean.getInventoryTo().getCode());
            appendQuery.append(" AND IDV.inventoryCode <= :inventoryTo");
        }

        return appendQuery.toString();
    }

    private String appendInvItem(ItemCardReportSerachBean itemCardReportSerachBean, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();
        if (itemCardReportSerachBean.getInvItemForm() != null && itemCardReportSerachBean.getInvItemForm().getCode() != null) {
            params.put("itemCodeFrom", itemCardReportSerachBean.getInvItemForm().getCode());
            appendQuery.append(" AND IDV.itemCode >= :itemCodeFrom");
        }

        if (itemCardReportSerachBean.getInvItemTo()!= null && itemCardReportSerachBean.getInvItemTo().getCode() != null) {
            params.put("itemCodeTo", itemCardReportSerachBean.getInvItemTo().getCode());
            appendQuery.append(" AND IDV.itemCode <= :itemCodeTo");
        }

        return appendQuery.toString();
    }

    private String appendDate(ItemCardReportSerachBean itemCardReportSerachBean, Map<String, Object> params) {
        if (itemCardReportSerachBean.getDateTo() != null) {
            itemCardReportSerachBean.getDateTo().setHours(23);
            itemCardReportSerachBean.getDateTo().setMinutes(59);
            itemCardReportSerachBean.getDateTo().setSeconds(59);
        }
        StringBuilder appendQuery = new StringBuilder();
        if (itemCardReportSerachBean.getDateFrom() != null) {
            params.put("dateFrom", itemCardReportSerachBean.getDateFrom());
            appendQuery.append(" AND IDV.date >= :dateFrom");
        }

        if (itemCardReportSerachBean.getDateTo() != null) {
            params.put("dateTo", itemCardReportSerachBean.getDateTo());
            appendQuery.append(" AND IDV.date <= :dateTo");
        }

        return appendQuery.toString();
    }

    private String appendInvoiceType(ItemCardReportSerachBean itemCardReportSerachBean, Map<String, Object> params) {
        StringBuilder appendQuery = new StringBuilder();
        if (itemCardReportSerachBean.getInvoiceType() != null) {
            params.put("invoiceType", itemCardReportSerachBean.getInvItemForm().getCode());
            appendQuery.append(" AND IDV.itemCode >= :itemCodeFrom");
        }

        return appendQuery.toString();
    }

    @Override
    public List<ItemsDataView> getAllItemCardByInvStripSearchBean(ItemCardReportSerachBean itemCardReportSerachBean, GlYear glYear) {
        dao.executeNativeQuery("select toby.sql_mode_function()");
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", itemCardReportSerachBean.getBranchId());

        StringBuilder stringBuilder = new StringBuilder();
        Map<Integer, BigDecimal> itemOpeningBalanceMap = new HashMap<>();
        Map<Integer, String> itemOpeningBalanceAfterAllDateMap = new HashMap<>();
        Map<Integer, BigDecimal> qtyInMap = new HashMap<>();
        Map<Integer, BigDecimal> qtyOutMap = new HashMap<>();
        if (itemCardReportSerachBean.getDateFrom() != null && itemCardReportSerachBean.getDateFrom().after(glYear.getDateFrom())) {
            stringBuilder.append("SELECT IDV.itemId, sum(COALESCE(IDV.qtyin * IDV.screwing,0))-sum(COALESCE(IDV.qtyout * IDV.screwing,0)),IDV.itemName"
                    + ",sum(COALESCE(IDV.qtyin * IDV.screwing,0)), sum(COALESCE(IDV.qtyout * IDV.screwing,0)) "
                    + " FROM ItemsDataView IDV WHERE IDV.branchId = :branchId "
                    + " AND IDV.date is not null AND IDV.itemId is not null "
                    + " AND IDV.inventoryId is not null AND IDV.groupId is not null"
                    + " AND IDV.itemName is not null AND IDV.itemCode is not null "
                    + " AND IDV.qtyin is not null  AND IDV.qtyout is not null ");

            stringBuilder.append(appendGlYear(itemCardReportSerachBean, glYear, params));
            stringBuilder.append(appendTillDate(itemCardReportSerachBean, glYear, params));
            stringBuilder.append(appendInventory(itemCardReportSerachBean, params));
            stringBuilder.append(appendInvItem(itemCardReportSerachBean, params));

            stringBuilder.append(" group by IDV.itemId ,IDV.itemName");
//            stringBuilder.append(" order by IDV.itemId ,IDV.date ASC");
            List<Object[]> list = dao.findListByQuery(stringBuilder.toString(), params);

            for (Object[] os : list) {
                itemOpeningBalanceMap.put((Integer) os[0], (BigDecimal) os[1]);
                itemOpeningBalanceAfterAllDateMap.put((Integer) os[0], (String) os[2]);
            }
        }
        //to add total qtyin and qtyout
        params = new HashMap<>();
        params.put("branchId", itemCardReportSerachBean.getBranchId());

        stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT IDV.itemId, sum(COALESCE(IDV.qtyin * IDV.screwing,0))-sum(COALESCE(IDV.qtyout * IDV.screwing,0)),IDV.itemName"
                + ",sum(COALESCE(IDV.qtyin * IDV.screwing,0)), sum(COALESCE(IDV.qtyout * IDV.screwing,0)) "
                + " FROM ItemsDataView IDV WHERE IDV.branchId = :branchId "
                + " AND IDV.date is not null AND IDV.itemId is not null "
                + " AND IDV.inventoryId is not null AND IDV.groupId is not null"
                + " AND IDV.itemName is not null AND IDV.itemCode is not null "
                + " AND IDV.qtyin is not null  AND IDV.qtyout is not null ");

        stringBuilder.append(appendGlYear(itemCardReportSerachBean, glYear, params));
        stringBuilder.append(appendDate(itemCardReportSerachBean, params));
        stringBuilder.append(appendInventory(itemCardReportSerachBean, params));
        stringBuilder.append(appendInvItem(itemCardReportSerachBean, params));

        stringBuilder.append(" group by IDV.itemId ,IDV.itemName");
        stringBuilder.append(" order by IDV.itemId ASC, IDV.date ASC");
        List<Object[]> list2 = dao.findListByQuery(stringBuilder.toString(), params);
        for (Object[] os : list2) {
            qtyInMap.put((Integer) os[0], (BigDecimal) os[3]);
            qtyOutMap.put((Integer) os[0], (BigDecimal) os[4]);
        }

        stringBuilder = new StringBuilder();
        params = new HashMap<>();
        params.put("branchId", itemCardReportSerachBean.getBranchId());
        stringBuilder.append("SELECT IDV FROM ItemsDataView IDV WHERE IDV.branchId = :branchId "
                + " AND IDV.date is not null AND IDV.itemId is not null "
                + " AND IDV.inventoryId is not null AND IDV.groupId is not null"
                + " AND IDV.itemName is not null AND IDV.itemCode is not null "
                + " AND IDV.qtyin is not null  AND IDV.qtyout is not null ");

        stringBuilder.append(appendDate(itemCardReportSerachBean, params));
        stringBuilder.append(appendInventory(itemCardReportSerachBean, params));
        stringBuilder.append(appendInvItem(itemCardReportSerachBean, params));

        stringBuilder.append(" order by IDV.itemId , IDV.date ASC");

        List<ItemsDataView> itemsDataViewList = dao.findListByQuery(stringBuilder.toString(), params);

        for (ItemsDataView dataView : itemsDataViewList) {
            dataView.setOpeningBalance(itemOpeningBalanceMap.get(dataView.getItemId()));
            //i will set in organizationopenbalance credit and debit fields and use them as total value for qtyin and qty out
            dataView.setTotalQtyIn(qtyInMap.get(dataView.getItemId()));
            dataView.setTotalQtyOut(qtyOutMap.get(dataView.getItemId()));
            if (itemOpeningBalanceAfterAllDateMap.containsKey(dataView.getItemId())) {
                itemOpeningBalanceAfterAllDateMap.remove(dataView.getItemId());
            }
        }
//if search after all dates of item and need to get the open balance for them
        for (Map.Entry<Integer, String> entry : itemOpeningBalanceAfterAllDateMap.entrySet()) {
            ItemsDataView idv = new ItemsDataView();
            idv.setItemName(entry.getValue());
            idv.setItemId(entry.getKey());
            idv.setOpeningBalance(itemOpeningBalanceMap.get(entry.getKey()));
            idv.setTotalQtyIn(qtyInMap.get(entry.getKey()));
            idv.setTotalQtyOut(qtyOutMap.get(entry.getKey()));
            itemsDataViewList.add(idv);
        }

//        TreeMap<Date,ItemsDataView> treeMap = new TreeMap<>();
//        for(ItemsDataView dataView : itemsDataViewList){
//            treeMap.put(dataView.getDate(), dataView);
//        }
//        itemsDataViewList = new ArrayList<>();
//        itemsDataViewList = new ArrayList<ItemsDataView>(treeMap.values());
        return itemsDataViewList;
    }

}
