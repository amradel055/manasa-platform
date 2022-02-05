/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.ItemCardReportSerachBean;
import com.isag.businessservice.reports.searchBean.ItemMainDataByGroupSearchBean;
import com.isag.core.GenericDAO;
import com.isag.define.ScreenNameClassEnum;
import com.isag.dto.InstPurchaseInvoiceDTO;
import com.isag.dto.InstPurchaseInvoiceDetailDTO;
import com.isag.dto.InvItemDTO;
import com.isag.dto.InventorySetupDTO;
import com.isag.entity.Branch;
import com.isag.entity.InvBarcode;
import com.isag.entity.InvGroup;
import com.isag.entity.InvItem;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.IsagUser;
import com.isag.entity.Symbol;
import com.isag.views.ItemsBarcodesDetailsView;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author WIN7
 */
@Stateless
public class InvItemServiceImp implements InvItemService {

    StringBuilder appendQuery;

    @EJB
    private GenericDAO dao;
    @EJB
    private InvBarcodeSevice invBarcodeSevice;
    @EJB
    private InvItemService invItemService;
    @EJB
    private instPurchaseInvoiceService invoiceService;
    @EJB
    private InvOpeningBalanceItemDetailService invOpeningBalanceItemDetailService;
    @EJB
    private InventorySetupService inventorySetupService;
    @EJB
    private QuantityItemsStoreByDateService quantityItemsStoreAddExitService;

    @Override
    public InvItem addInvItem(InvItem invItem, List<InvBarcode> invBarcodeList, List<InvBarcode> invBarcodeDeletedList) {

        if (invItem.getId() != null) {
            dao.updateEntity(invItem);
        } else {
            invItem = dao.updateEntity(invItem);

//            if (invItem.getId() != null) {
//                InstPurchaseInvoiceDTO dto = new InstPurchaseInvoiceDTO();
//                dto.setDate(new Date());
//                List<InstPurchaseInvoiceDetailDTO> instPurchaseInvoiceDetailDTOList = new ArrayList();
//                InstPurchaseInvoiceDetailDTO instPurchaseInvoiceDetailDTO = new InstPurchaseInvoiceDetailDTO();
//                InvItemInstProjectGuideView instProjectGuideView = new InvItemInstProjectGuideView();
//                instProjectGuideView.setId(invItem.getId());
//                instProjectGuideView.setTableType(0);
//                instProjectGuideView.setName(invItem.getName());
//                instPurchaseInvoiceDetailDTO.setItemId(instProjectGuideView);
//                instPurchaseInvoiceDetailDTO.setQuantity(BigDecimal.ONE);
//                instPurchaseInvoiceDetailDTO.setUnitPrice(invItem.getSellPrice());
//                instPurchaseInvoiceDetailDTOList.add(instPurchaseInvoiceDetailDTO);
//                addInstPurchaseInvoiceDTO(dto, instPurchaseInvoiceDetailDTOList, null, invItem.getCreatedBy());
//            }
        }
        if (invBarcodeDeletedList != null) {
            for (InvBarcode invBarcode : invBarcodeDeletedList) {
                invBarcodeSevice.deleteInvBarcode(invBarcode);
            }
        }

        if (invBarcodeList != null) {
            for (InvBarcode barcode : invBarcodeList) {
                if (barcode != null && barcode.getPrice1() != null
                        && barcode.getUnitId() != null) {
                    InvItem itemBarcode = new InvItem();
                    itemBarcode.setCode(barcode.getCode());
                    itemBarcode.setBranchId(invItem.getBranchId());
                    itemBarcode.setId(invItem.getId());
                    if (barcode.getCode() != null && !barcode.getCode().isEmpty()) {
                        if (validateCodeFromView(itemBarcode)) {
                            invItem.setMsg("الكود  مكرر");
                            dao.rollbackQuery();
                            return invItem;
                        }
                    }
                    barcode.setItemId(invItem);
                    if (barcode.getId() != null) {
                        invBarcodeSevice.updateInvBarcode(barcode);
                    } else {
                        invBarcodeSevice.addInvBarcode(barcode);
                    }
                } else {
                    invItem.setMsg("ادخل بيانات الباركود الناقصه");
                    dao.rollbackQuery();
                    return invItem;
                }
            }
        }
        return invItem;
    }

    private InstPurchaseInvoiceDTO addInstPurchaseInvoiceDTO(InstPurchaseInvoiceDTO instPurchaseInvoiceDTO, List<InstPurchaseInvoiceDetailDTO> instPurchaseInvoiceDetailDTOList, List<InstPurchaseInvoiceDetailDTO> instPurchaseInvoiceDetailDTOListDelete, IsagUser isagUser) {
        return invoiceService.addInstPurchaseInvoice(instPurchaseInvoiceDTO, instPurchaseInvoiceDetailDTOList, instPurchaseInvoiceDetailDTOListDelete, isagUser);
    }

    /**
     *
     * @param invItem
     * @return
     */
    @Override
    public InvItem updateInvItem(InvItem invItem) {
        return dao.updateEntity(invItem);
    }

    @Override
    public void updateInvItem(Integer invItemId) {
        InvItem item = dao.findEntityById(InvItem.class, invItemId);
        if (item.getCode() == null || item.getCode().isEmpty()) {
            item.setCode(item.getId().toString());
            dao.updateEntity(item);
        }
    }

    @Override
    public void deleteInvItem(InvItem invItem) {
        dao.deleteEntity(invItem);
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
    public List<InvItem> getInvItemByBranchIdAndGroupIdUpdate(Integer branchId, String groupId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvItem> details = dao.findListByQuery("SELECT e FROM InvItem e WHERE e.branchId.id = :branchId AND e.groupId.id in( " +groupId+ "  ) ORDER BY e.code", params);
        List<InvItem> detailsReturn = retriveItemData(details);
        return detailsReturn;
    }
    
    
    @Override
    public List<InvItemDTO> getInvItemByBranchIdAndGroupIdDTO(Integer branchId, Integer groupId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("groupId", groupId);
        List<InvItem> details = dao.findListByQuery("SELECT e FROM InvItem e WHERE e.branchId.id = :branchId AND e.groupId.id = :groupId  ORDER BY e.code", params);
        List<InvItemDTO> detailsReturn = retriveItemDataDTO(details);
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
            invItemService.updateInvItem(invItem);
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
    public List<InvItemDTO> getInvItemListByBranchId(Integer branchId, ScreenNameClassEnum screenNameClassEnum) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvItem> detailses = dao.findListByQuery("SELECT i FROM InvItem i WHERE i.branchId.id = :branchId   ORDER BY i.code", params);
        List<InvItemDTO> detailsesReturn = retriveItemDTOData(detailses, screenNameClassEnum);
        return detailsesReturn;
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

    public List<Integer> getInvItemListByBranchIdNotInInvPurchaseInvoice(Integer branchId, ItemCardReportSerachBean itemCardReportSerachBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);

        StringBuilder query = new StringBuilder();
        query.append("SELECT i.id FROM InvItem i WHERE i.branchId.id = :branchId ");
        if (itemCardReportSerachBean.getInvItemForm() != null) {
            params.put("codeFrom", itemCardReportSerachBean.getInvItemForm().getCode());
            query.append(" AND i.code >= :codeFrom");
        }
        if (itemCardReportSerachBean.getInvItemTo() != null) {
            params.put("codeTo", itemCardReportSerachBean.getInvItemTo().getCode());
            query.append(" AND i.code <= :codeTo");
        }

        query.append(" ORDER BY i.code");

        List<Integer> detailses = dao.findListByQuery(query.toString(), params);
        return detailses;
    }

    private List<InvItemDTO> retriveItemDTOData(List<InvItem> detailses, ScreenNameClassEnum screenNameClassEnum) {
        List<InvItemDTO> detailsesReturn = new ArrayList<>();
        for (InvItem detail : detailses) {
            InvItemDTO dest = new InvItemDTO();
            if (ScreenNameClassEnum.ITEMCARDREPORT.equals(screenNameClassEnum)) {
                dest.setId(detail.getId());
                dest.setName(detail.getName());
                dest.setCode(detail.getCode());
                InvGroup group = null;
                if (detail.getGroupId() != null) {
                    group = new InvGroup();
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
                InvGroup group = null;
                if (detail.getGroupId() != null) {
                    group = new InvGroup();
                    group.setId(detail.getGroupId().getId());
                    group.setName(detail.getGroupId().getName());
                    group.setCode(detail.getGroupId().getCode());
                    group.setLevel(detail.getGroupId().getLevel());
                }
                dest.setGroupId(group);
                dest.setCostAverage(detail.getCostAverage());
                dest.setSellPrice(detail.getSellPrice());
                Symbol unit = null;
                if (detail.getUnitId() != null) {
                    unit = new Symbol();
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

    @Override
    public List<InvItem> getInvItemReport(ItemMainDataByGroupSearchBean ItemMainDataByGroupSearchBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", ItemMainDataByGroupSearchBean.getBranchId());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT e FROM InvItem e WHERE e.branchId.id = :branchId ");

        stringBuilder.append(appendGroup(ItemMainDataByGroupSearchBean, params));
        stringBuilder.append(appendItem(ItemMainDataByGroupSearchBean, params));
        stringBuilder.append("  ORDER BY i.code");
        List<InvItem> details = dao.findListByQuery(stringBuilder.toString(), params);
        return details;
    }

    @Override
    public List<InvItem> getInvItemReportForInvItemMainDataByGroupReportMB(ItemMainDataByGroupSearchBean ItemMainDataByGroupSearchBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", ItemMainDataByGroupSearchBean.getBranchId());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT e FROM InvItem e WHERE e.branchId.id = :branchId ");

        stringBuilder.append(appendGroup(ItemMainDataByGroupSearchBean, params));
        stringBuilder.append(appendItem2(ItemMainDataByGroupSearchBean, params));

        List<InvItem> details = dao.findListByQuery(stringBuilder.toString(), params);
        List<InvItem> detailsReturn = retriveItemData(details);
        return detailsReturn;
    }

    private List<InvItem> retriveItemData(List<InvItem> details) {
        List<InvItem> detailsReturn = new ArrayList<>();
        for (InvItem detail : details) {
            InvItem invItem = new InvItem();
            invItem.setId(detail.getId());
            invItem.setCode(detail.getCode());
            invItem.setName(detail.getName());
            invItem.setCostAverage(detail.getCostAverage());
            invItem.setCartonsCapacity(detail.getCartonsCapacity());
            invItem.setPrintShow(detail.getPrintShow());
            Symbol sorting = null;
            if (detail.getSorting() != null) {//invItem.getUnitId() != null
                sorting = new Symbol();
                sorting.setId(detail.getUnitId().getId());
                sorting.setSerial(detail.getUnitId().getSerial());
                sorting.setName(detail.getUnitId().getName());
            }

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
                if (detail.getGroupId().getParent() != null) {
                    InvGroup invGroup2 = new InvGroup();
                    invGroup2.setId(detail.getGroupId().getParent().getId());
                    invGroup2.setName(detail.getGroupId().getParent().getName());
                    invGroup2.setCode(detail.getGroupId().getParent().getCode());

                    invGroup.setParent(invGroup2);
                }
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

    private List<InvItemDTO> retriveItemDataDTO(List<InvItem> details) {
        List<InvItemDTO> detailsReturn = new ArrayList<>();
        for (InvItem detail : details) {
            InvItemDTO invItem = mapToDTO(detail);
            detailsReturn.add(invItem);
        }
        return detailsReturn;
    }

    @Override
    public InvItemDTO mapToDTO(InvItem detail) {
        InvItemDTO invItem = new InvItemDTO();
        invItem.setId(detail.getId());
        invItem.setCode(detail.getCode());
        invItem.setName(detail.getName());
        invItem.setCostAverage(detail.getCostAverage());
        invItem.setPrintShow(detail.getPrintShow());
        Symbol sorting = null;
        if (detail.getSorting() != null) {//invItem.getUnitId() != null
            sorting = new Symbol();
            sorting.setId(detail.getUnitId().getId());
            sorting.setSerial(detail.getUnitId().getSerial());
            sorting.setName(detail.getUnitId().getName());
        }
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
            if (detail.getGroupId().getParent() != null) {
                InvGroup invGroup2 = new InvGroup();
                invGroup2.setId(detail.getGroupId().getParent().getId());
                invGroup2.setName(detail.getGroupId().getParent().getName());
                invGroup2.setCode(detail.getGroupId().getParent().getCode());
                
                invGroup.setParent(invGroup2);
            }
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
        InvOrganizationSite site = null;
        if (detail.getSiteId() != null) {
            site = new InvOrganizationSite();
            site.setId(detail.getSiteId().getId());
            site.setName(detail.getSiteId().getName());
            site.setCode(detail.getSiteId().getCode());
        }
        invItem.setSiteId(site);
        invItem.setDateCreateCat(detail.getDateCreateCat());
        return invItem;
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
     private String appendItem2(ItemMainDataByGroupSearchBean ItemMainDataByGroupSearchBean, Map<String, Object> params) {
        appendQuery = new StringBuilder();

        if (ItemMainDataByGroupSearchBean.getItemSelected() != null && !ItemMainDataByGroupSearchBean.getItemSelected().isEmpty() ) {
            appendQuery.append(" AND e.id in (").append(ItemMainDataByGroupSearchBean.getItemSelected()).append(")"); 
        }

      

        return appendQuery.toString();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public synchronized <T> Integer getMaxGenericSerialForItem(Integer branchId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT max(CONVERT(code,UNSIGNED INTEGER)) FROM isag.inv_item i where  ");
        query.append("  branch_id = ").append(branchId);
        List<BigInteger> list = dao.executeNativeQuery(query.toString());
        Integer code = list != null && !list.isEmpty() && list.get(0) != null ? list.get(0).intValue() : null;
        if (code == null) {
            return 1;
        } else {
            return code + 1;
        }
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
    public List<InvItem> getInvItemViewData(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters, Integer inventoryId) {

        StringBuilder builder = new StringBuilder();
        List<InvItem> detailsNew = new ArrayList<>();
        builder.append("SELECT s FROM InvItem s where s.branchId.id = :branchId  ");
        appendFilter(filters, branchId, builder);
        builder.append(" ORDER BY s.id DESC");
        InventorySetupDTO inventorySetupDTO = inventorySetupService.getInventoryByBranchIdDTO(branchId);

        List<InvItem> details = dao.findListByQuery(builder.toString(), filters, first, pageSize);

        StringBuilder builder2 = new StringBuilder();
        builder2.append("SELECT i FROM InvBarcode i where i.branchId.id = :branchId  ");
        appendFilter2(filters, branchId, builder2);

        List<InvBarcode> details2 = dao.findListByQuery(builder2.toString(), filters, first, pageSize);

        for (InvItem invItemMapper : details) {

            InvItem invItem = new InvItem();

            invItem.setId(invItemMapper.getId() != null ? invItemMapper.getId() : null);
            invItem.setBranchId(invItemMapper.getBranchId());
            invItem.setBrandId(invItemMapper.getBrandId());
            invItem.setCode(invItemMapper.getCode());
            invItem.setCompanyId(invItemMapper.getCompanyId());
            invItem.setGroupId(invItemMapper.getGroupId());
            invItem.setName(invItemMapper.getName());
            invItem.setNameen(invItemMapper.getNameen());
            invItem.setWeightPackage(invItemMapper.getWeightPackage());
            invItem.setSiteId(invItemMapper.getSiteId());
            invItem.setUnitId(invItemMapper.getUnitId());
            invItem.setSellPrice(invItemMapper.getSellPrice());
            invItem.setCostAverage(invItemMapper.getCostAverage());

            if (inventoryId != null) {
                invItem.setStoreQuantity((quantityItemsStoreAddExitService.getQuantatyItemByinventory(inventoryId, invItemMapper.getId(), inventorySetupDTO.getSellBuy(), null)).setScale(3, BigDecimal.ROUND_UP));
            }

            detailsNew.add(invItem);
        }

        for (InvBarcode invItemMapper : details2) {

            InvItem invItem = new InvItem();

            invItem.setId(invItemMapper.getItemId().getId());
            invItem.setBranchId(invItemMapper.getBranchId());
            invItem.setBrandId(invItemMapper.getItemId().getBrandId());
            invItem.setCode(invItemMapper.getCode());
            invItem.setCompanyId(invItemMapper.getCompanyId());
            invItem.setGroupId(invItemMapper.getItemId().getGroupId());
            invItem.setName(invItemMapper.getItemId().getName());
            invItem.setNameen(invItemMapper.getItemId().getNameen());
            invItem.setWeightPackage(invItemMapper.getItemId().getWeightPackage());
            invItem.setSiteId(invItemMapper.getItemId().getSiteId());
            invItem.setUnitId(invItemMapper.getUnitId());
            invItem.setSellPrice(invItemMapper.getItemId().getSellPrice());
            invItem.setCostAverage(invItemMapper.getItemId().getCostAverage());

            if (inventoryId != null) {
                invItem.setStoreQuantity((quantityItemsStoreAddExitService.getQuantatyItemByinventory(inventoryId, invItemMapper.getId(), inventorySetupDTO.getSellBuy(), null)).setScale(3, BigDecimal.ROUND_UP));
            }

            detailsNew.add(invItem);
        }

        return detailsNew;
    }

    public void appendFilter(Map<String, Object> filters, Integer branchId, StringBuilder builder) {
        filters.put("branchId", branchId);
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and (s.code like CONCAT('%',:globalFilter ,'%') OR s.code like CONCAT('%',:globalFilter ,'%')  OR s.name  like CONCAT('%',:globalFilter ,'%')  OR s.nickname like CONCAT('%',:globalFilter ,'%') OR s.unitId.name like CONCAT('%',:globalFilter ,'%') OR s.groupId.name like CONCAT('%',:globalFilter ,'%') ) ";
            builder.append(filter);
        }
    }

    public void appendFilter2(Map<String, Object> filters, Integer branchId, StringBuilder builder) {
        filters.put("branchId", branchId);
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and (i.code like CONCAT('%',:globalFilter ,'%')) ";
            builder.append(filter);
        }
    }

    @Override
    public Long getTotalRegistors(Integer branchId, Map<String, Object> filters) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT count(s) FROM InvItem s WHERE s.branchId.id = :branchId ");
        appendFilter(filters, branchId, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);

        StringBuilder builder2 = new StringBuilder();
        builder2.append("SELECT count(i) FROM InvBarcode i WHERE i.branchId.id = :branchId ");
        appendFilter2(filters, branchId, builder2);
        Long details2 = dao.findEntityByQuery(builder2.toString(), filters);

        Long total = Long.sum(details, details2);

        return total;
    }
}
