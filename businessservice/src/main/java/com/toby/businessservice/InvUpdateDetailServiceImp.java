package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.SymbolDTO;
import com.toby.entity.InvBarcode;
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
    public List<InvDetailDTO> getAllInvUpdatesDetailFromInvTransferDetail(String invStripSerial, Integer branchId, Integer inventoryId) {
        List<InvDetailDTO> invdetailListAfterSummition = new ArrayList<>();
        StringBuilder itemIdString = new StringBuilder();
        StringBuilder itemIdS = new StringBuilder();
        itemIdString.append(0);
        itemIdS.append(0);
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("inventoryId", inventoryId);
        StringBuilder query = new StringBuilder();
        query.append("SELECT d FROM InvStripDetail d WHERE d.invStripId.branchId.id = :branchId AND d.invStripId.inventoryId.id = :inventoryId AND d.invStripId.serial in ( ").append(invStripSerial).append(")");
//        List<InvStripDetail> stripDetailList = dao.findListByQuery(query.toString());
        List<InvStripDetail> stripDetailList = dao.findListByQuery(query.toString(), params);
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
                itemIdS.append(",").append(detailDTO.getItemId().getId());
            }
        }
        List<InvDetailDTO> result = new ArrayList(map.values());
        Map<Integer, BigDecimal> itemMap = quantityItemsStoreAddExitService.getAllQuantatyItemByinventory(inventoryId, itemIdS.toString() , true, null);
        for (InvDetailDTO dTO : result) {
            dTO.setAvailableQuantityRow(itemMap.get(dTO.getItemId().getId()));
            dTO.setOldQuantity(dTO.getAvailableQuantityRow());
            dTO.setOldQuantityBak(dTO.getAvailableQuantityRow());
            dTO.setCostAvarage(dTO.getItemId().getLastCost());
            dTO.setTotalQuantityCost(dTO.getCostAvarage().multiply(dTO.getActualQuantity()).stripTrailingZeros());
            dTO.setId(null);
            dTO.setSerial(null);
            itemIdString.append(",").append(dTO.getItemId().getId());
            invdetailListAfterSummition.add(dTO);
        }

        addRemainItems(inventoryId, itemIdString, invdetailListBeforeSummition, invdetailListAfterSummition);

        return invdetailListAfterSummition;
    }

    @Override
    public List<InvDetailDTO> getAllInvUpdatesDetailFromInvTransferDetail(Integer invStripSerial, Integer branchId, Integer inventoryId) {
        List<InvDetailDTO> invdetailListAfterSummition = new ArrayList<>();
        StringBuilder itemIdString = new StringBuilder();
        itemIdString.append(0);

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
            dTO.setTotalQuantityCost(dTO.getCostAvarage().multiply(dTO.getActualQuantity()).stripTrailingZeros());
            dTO.setId(null);
            dTO.setSerial(null);
            itemIdString.append(",").append(dTO.getItemId().getId());
            invdetailListAfterSummition.add(dTO);
        }

        addRemainItems(inventoryId, itemIdString, invdetailListBeforeSummition, invdetailListAfterSummition);

        return invdetailListAfterSummition;
    }

    public void addRemainItems(Integer inventoryId, StringBuilder itemIdString, List<InvDetailDTO> invdetailListBeforeSummition, List<InvDetailDTO> invdetailListAfterSummition) {
        //////////////////////

        StringBuilder builder = new StringBuilder();
        builder.append("select  IDV.cost_average , IDV.itemId ,IDV.code  ,IDV.itemName,IDV.supplierName \n"
                + "from ( \n"
                + "	SELECT  IFNULL(`ii`.`cost_average`, 0) AS `cost_average`,\n"
                + "        `ipid`.`item_id` AS `itemId`,ii.code,ii.name as itemName ,ios.name as supplierName,\n"
                + "        'فاتورة مشتريات' AS `ScreenName`\n"
                + "    FROM\n"
                + "       ( ((`inv_purchase_invoice` `ipi`\n"
                + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON ((`ipi`.`id` = `ipid`.`inv_purchase_invoice_id`)))\n"
                + "        LEFT JOIN `inv_item` `ii` ON ((`ii`.`id` = `ipid`.`item_id`)))\n"
                + "         LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `ii`.`site_id`)))\n"
                + "    WHERE\n"
                + "        ((`ipi`.`type` = 0) AND (`ipi`.`date` IS NOT NULL))  AND `ipi`.`inv_inventory_id` = ").append(inventoryId);
        builder.append("    UNION ALL SELECT  IFNULL(`ii`.`cost_average`, 0) AS `cost_average`,\n"
                + "        `ipid`.`item_id` AS `item_Id`,ii.code,ii.name as itemName ,ios.name as supplierName,\n"
                + "        'فاتورة مبيعات' AS `ScreenName`\n"
                + "    FROM\n"
                + "        (((`inv_purchase_invoice` `ipi`\n"
                + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON ((`ipi`.`id` = `ipid`.`inv_purchase_invoice_id`)))\n"
                + "        LEFT JOIN `inv_item` `ii` ON ((`ii`.`id` = `ipid`.`item_id`)))        \n"
                + "         LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `ii`.`site_id`)))\n"
                + "    WHERE\n"
                + "        ((`ipi`.`type` = 4)\n"
                + "            AND (`ipi`.`date` IS NOT NULL))  AND `ipi`.`inv_inventory_id` =  ").append(inventoryId);
        builder.append("    UNION ALL SELECT  IFNULL(`ii`.`cost_average`, 0) AS `cost_average`,\n"
                + "        `ipid`.`item_id` AS `item_Id`,ii.code,ii.name as itemName ,ios.name as supplierName,\n"
                + "        'مردودات المشتريات' AS `ScreenName`\n"
                + "    FROM\n"
                + "        (((`inv_purchase_invoice` `ipi`\n"
                + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON ((`ipi`.`id` = `ipid`.`inv_purchase_invoice_id`)))\n"
                + "        LEFT JOIN `inv_item` `ii` ON ((`ii`.`id` = `ipid`.`item_id`)))\n"
                + "         LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `ii`.`site_id`)))\n"
                + "    WHERE\n"
                + "        ((`ipi`.`type` = 2)\n"
                + "            AND (`ipi`.`date` IS NOT NULL))  AND `ipi`.`inv_inventory_id` =  ").append(inventoryId);
        builder.append("    UNION ALL SELECT  IFNULL(`ii`.`cost_average`, 0) AS `cost_average`,\n"
                + "        `ipid`.`item_id` AS `item_Id`,ii.code,ii.name as itemName ,ios.name as supplierName,\n"
                + "        'مردودات المبيعات' AS `ScreenName`\n"
                + "    FROM\n"
                + "        (((`inv_purchase_invoice` `ipi`\n"
                + "        LEFT JOIN `inv_purchase_invoice_detail` `ipid` ON ((`ipi`.`id` = `ipid`.`inv_purchase_invoice_id`)))\n"
                + "        LEFT JOIN `inv_item` `ii` ON ((`ii`.`id` = `ipid`.`item_id`)))\n"
                + "         LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `ii`.`site_id`)))\n"
                + "    WHERE\n"
                + "        ((`ipi`.`type` = 3) AND (`ipi`.`date` IS NOT NULL))  AND `ipi`.`inv_inventory_id` =  ").append(inventoryId);
        builder.append("    UNION ALL SELECT  IFNULL(`ii`.`cost_average`, 0) AS `cost_average`,\n"
                + "        `itd`.`inv_item_id` AS `item_Id`,ii.code,ii.name as itemName ,ios.name as supplierName,\n"
                + "        'تحويل بين المخازن من ' AS `ScreenName`\n"
                + "    FROM\n"
                + "        (((`inv_transfer` `it`\n"
                + "        LEFT JOIN `inv_transfer_detail` `itd` ON ((`it`.`id` = `itd`.`inv_transfer_id`)))\n"
                + "        LEFT JOIN `inv_item` `ii` ON ((`ii`.`id` = `itd`.`inv_item_id`)))\n"
                + "         LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `ii`.`site_id`)))\n"
                + "    WHERE\n"
                + "        ((`it`.`transfer_date` IS NOT NULL)  AND (`it`.`transfer_type` = 0))  AND `it`.`inv_from` =  ").append(inventoryId);
        builder.append("    UNION ALL SELECT  IFNULL(`ii`.`cost_average`, 0) AS `cost_average`,\n"
                + "        `itd`.`inv_item_id` AS `item_Id`,ii.code,ii.name as itemName ,ios.name as supplierName,\n"
                + "        'تحويل بين المخازن الى' AS `ScreenName`\n"
                + "    FROM\n"
                + "        (((`inv_transfer` `it`\n"
                + "        LEFT JOIN `inv_transfer_detail` `itd` ON ((`it`.`id` = `itd`.`inv_transfer_id`)))\n"
                + "        LEFT JOIN `inv_item` `ii` ON ((`ii`.`id` = `itd`.`inv_item_id`)))\n"
                + "         LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `ii`.`site_id`)))\n"
                + "    WHERE\n"
                + "        ((`it`.`transfer_date` IS NOT NULL)\n"
                + "            AND (`it`.`transfer_type` = 0))  AND `it`.`inv_to` =  ").append(inventoryId);
        builder.append("    UNION ALL SELECT \n"
                + "        IFNULL(`ii`.`cost_average`, 0) AS `cost_average`,\n"
                + "        `isd`.`inv_item_id` AS `item_Id`,ii.code,ii.name as itemName ,ios.name as supplierName,\n"
                + "        'ضبط  المخزون' AS `ScreenName`\n"
                + "    FROM\n"
                + "        (((`inv_update` `is`\n"
                + "        LEFT JOIN `inv_update_detail` `isd` ON ((`is`.`id` = `isd`.`inv_update_id`)))\n"
                + "        LEFT JOIN `inv_item` `ii` ON ((`ii`.`id` = `isd`.`inv_item_id`)))\n"
                + "         LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `ii`.`site_id`)))\n"
                + "    WHERE\n"
                + "        (`is`.`date` IS NOT NULL)  AND  `is`.`inventory_id` =  ").append(inventoryId);
        builder.append("    UNION ALL SELECT \n"
                + "        IFNULL(`ii`.`cost_average`, 0) AS `cost_average`,\n"
                + "        `iobid`.`item_id` AS `item_Id`,ii.code,ii.name as itemName ,ios.name as supplierName,\n"
                + "        'رصيد افتتاحي للاصناف' AS `ScreenName`\n"
                + "    FROM\n"
                + "        (((`inv_openning_balance_item` `iobi`\n"
                + "        LEFT JOIN `inv_openning_balance_item_detail` `iobid` ON ((`iobi`.`id` = `iobid`.`inv_openning_balance_item_id`)))\n"
                + "        LEFT JOIN `inv_item` `ii` ON ((`ii`.`id` = `iobid`.`item_id`)))\n"
                + "         LEFT JOIN `inv_organization_site` `ios` ON ((`ios`.`id` = `ii`.`site_id`)))\n"
                + "    WHERE\n"
                + "        (`iobi`.`date` IS NOT NULL) AND `iobi`.`inv_inventory_id` =  ").append(inventoryId);
        builder.append("     ) IDV    where itemId not in (").append(itemIdString.toString()).append(")");
        builder.append("        group by IDV.itemId");

        List<InvDetailDTO> result2 = new ArrayList<>();

        List<Object[]> res2 = dao.executeNativeQuery(builder.toString());
        List<String> barcodeList = new ArrayList<>();
        StringBuilder invItemId = null;
        Map<Integer, InvDetailDTO> map2 = new HashMap<>();
        for (Object[] object : res2) {
            InvDetailDTO dto = new InvDetailDTO();
//            barcodeList = new ArrayList<>();
            barcodeList.add((String) object[2]);
//            ItemsBarcodesDetailsView barcodesDetailsView = itemsBarcodesDetailsViewService.findItemsBarcodesDetailsViewBranchIdByBarcode(182, barcodeList).get(0);

            dto.setItemCode((String) object[2]);
            dto.setItemName((String) object[3]);
            dto.setSupplierName((String) object[4]);

            dto.setActualQuantity(BigDecimal.ZERO);
            dto.setWasalate(BigDecimal.ZERO);

            InvItemDTO invItemDTO = new InvItemDTO();
            invItemDTO.setId((Integer) object[1]);
            invItemDTO.setCode((String) object[2]);
            invItemDTO.setName((String) object[3]);
            invItemDTO.setLastCost((BigDecimal) object[0]);
            dto.setItemId(invItemDTO);
            dto.setIndex((Integer) object[1] + 10000000);

            if (map2.containsKey(dto.getItemId().getId())) {
                InvDetailDTO detail = map2.get(dto.getItemId().getId());
                dto.setActualQuantity(detail.getActualQuantity().add(dto.getActualQuantity()));
                dto.setWasalate(detail.getWasalate().add(dto.getWasalate()));
                map2.put(dto.getItemId().getId(), dto);
            } else {
                map2.put(dto.getItemId().getId(), dto);
            }

            if (invItemId == null) {
                invItemId = new StringBuilder();
                invItemId.append((Integer) object[1]);
            } else {
                invItemId.append(",").append((Integer) object[1]);
            }

            result2.add(dto);
        }

        List<InvDetailDTO> result22 = new ArrayList(map2.values());
        Map<Integer, BigDecimal> map = quantityItemsStoreAddExitService.getAllQuantatyItemByinventory(inventoryId, invItemId.toString(), true, null);
        Map<String, ItemsBarcodesDetailsView> barcodesDetailsViewMap = itemsBarcodesDetailsViewService.findItemsBarcodesDetailsViewBranchIdByBarcodeMap(182, barcodeList);

        for (InvDetailDTO dTO : result22) {
            dTO.setItemsBarcodesDetail(barcodesDetailsViewMap.get(dTO.getItemCode()));
            dTO.setItemsBarcodesDetailTrans(dTO.getItemsBarcodesDetail());
            dTO.setAvailableQuantityRow(map.get(dTO.getItemId().getId()));
            dTO.setOldQuantity(dTO.getAvailableQuantityRow());
            dTO.setOldQuantityBak(dTO.getAvailableQuantityRow());
            dTO.setCostAvarage(dTO.getItemId().getLastCost());
            dTO.setTotalQuantityCost(dTO.getCostAvarage().multiply(dTO.getActualQuantity()).stripTrailingZeros());
            dTO.setId(null);
            invdetailListAfterSummition.add(dTO);
        }
    }

    private List<InvDetailDTO> returnListDTO(List<InvStripDetail> list, Integer branchId, Integer inventoryId) {
        List<InvDetailDTO> dTOList = new ArrayList<>();
        Map<String, ItemsBarcodesDetailsView> itemsBarcodeMap = new HashMap<>();
        List<String> barcodeList = new ArrayList<>();
        for (InvDetailDTO entity : dTOList) {
            barcodeList.add(entity.getItemBarcode());
        }

        List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList = findItemsBarcodesDetailsViewBranchIdByBarcode(branchId, barcodeList);

        for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailsViewList) {
            itemsBarcodeMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
        }
        for (InvStripDetail entity : list) {
            dTOList.add(mapTODTO(entity, itemsBarcodeMap, inventoryId));
        }
        return dTOList;
    }

    public List<ItemsBarcodesDetailsView> findItemsBarcodesDetailsViewBranchIdByBarcode(Integer branchId, List<String> barcodeList) {

        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<ItemsBarcodesDetailsView> list = new ArrayList<>();
        StringBuilder query1 = new StringBuilder();
        StringBuilder query2 = new StringBuilder();
        query1.append(" select i from InvItem i where i.branchId.id = :branchId  ");
        query2.append(" select i from InvBarcode i where i.branchId.id = :branchId  ");
        if (barcodeList != null && !barcodeList.isEmpty()) {
            params.put("barcode", barcodeList);
            query1.append(" AND i.code in   :barcode ");
            query2.append(" AND i.code in   :barcode ");
        }
        List<InvItem> queryList = dao.findListByQuery(query1.toString(), params);
        List<InvBarcode> queryList2 = dao.findListByQuery(query2.toString(), params);

        for (InvItem invItem : queryList) {
            ItemsBarcodesDetailsView itemsBarcodesDetailsView = new ItemsBarcodesDetailsView();
            itemsBarcodesDetailsView.setBarcode(invItem.getCode());
            itemsBarcodesDetailsView.setItemId(invItem.getId());
            itemsBarcodesDetailsView.setCostAverage(invItem.getCostAverage());
            itemsBarcodesDetailsView.setName(invItem.getName());
            itemsBarcodesDetailsView.setNameEn(invItem.getNameen());
            itemsBarcodesDetailsView.setSellPrice(invItem.getSellPrice());
            itemsBarcodesDetailsView.setUnitId(invItem.getUnitId() == null ? null : invItem.getUnitId().getId());
            itemsBarcodesDetailsView.setWeight(invItem.getWeight());
            itemsBarcodesDetailsView.setHeight(invItem.getHeight());
            itemsBarcodesDetailsView.setAddon1(invItem.getAddon1() == null ? null : invItem.getAddon1().getName());
            itemsBarcodesDetailsView.setAddon2(invItem.getAddon2() == null ? null : invItem.getAddon2().getName());
            itemsBarcodesDetailsView.setPaintColor(invItem.getPaintColor() == null ? null : invItem.getPaintColor().getId());
            itemsBarcodesDetailsView.setUnitName(invItem.getUnitId().getName());
            itemsBarcodesDetailsView.setPaintColorName(invItem.getPaintColor() == null ? null : invItem.getPaintColor().getName());
            itemsBarcodesDetailsView.setScrewing(BigDecimal.ONE);
            itemsBarcodesDetailsView.setIsInventoryItem(invItem.getIsInventoryItem());
            itemsBarcodesDetailsView.setMaxPriceMen(invItem.getMaxpricemen());
            itemsBarcodesDetailsView.setMinPriceMen(invItem.getMinpricemen());
            itemsBarcodesDetailsView.setMaxPriceYoung(invItem.getMaxpriceyoung());
            itemsBarcodesDetailsView.setMinPriceYoung(invItem.getMinpriceyoung());
            itemsBarcodesDetailsView.setNumberMetersFreeMen(invItem.getNumbermetersfreemen());
            itemsBarcodesDetailsView.setNumberMetersFreeYoung(invItem.getNumbermetersfreeyoung());
            itemsBarcodesDetailsView.setNumberMetersMen(invItem.getNumbermetersmen());
            itemsBarcodesDetailsView.setNumberMetersYoung(invItem.getNumbermetersyoung());
            itemsBarcodesDetailsView.setProGroupId(invItem.getProGroupId() == null ? null : invItem.getProGroupId().getId());
            itemsBarcodesDetailsView.setTypeShow(invItem.getItemNatural());
            list.add(itemsBarcodesDetailsView);
        }

        for (InvBarcode invBarcode : queryList2) {
            ItemsBarcodesDetailsView itemsBarcodesDetailsView = new ItemsBarcodesDetailsView();
            itemsBarcodesDetailsView.setBarcode(invBarcode.getCode());
            itemsBarcodesDetailsView.setItemId(invBarcode.getItemId().getId());
            itemsBarcodesDetailsView.setCostAverage(invBarcode.getItemId().getCostAverage());
            itemsBarcodesDetailsView.setName(invBarcode.getItemId().getName());
            itemsBarcodesDetailsView.setNameEn(invBarcode.getItemId().getNameen());
            itemsBarcodesDetailsView.setSellPrice(invBarcode.getPrice1());
            itemsBarcodesDetailsView.setUnitId(invBarcode.getUnitId() == null ? null : invBarcode.getUnitId().getId());
            itemsBarcodesDetailsView.setWeight(invBarcode.getWeight());
            itemsBarcodesDetailsView.setHeight(invBarcode.getHeight());
            itemsBarcodesDetailsView.setAddon1(invBarcode.getItemId().getAddon1() == null ? null : invBarcode.getItemId().getAddon1().getName());
            itemsBarcodesDetailsView.setAddon2(invBarcode.getItemId().getAddon2() == null ? null : invBarcode.getItemId().getAddon2().getName());
            itemsBarcodesDetailsView.setPaintColor(invBarcode.getPaintColor() == null ? null : invBarcode.getPaintColor().getId());
            itemsBarcodesDetailsView.setUnitName(invBarcode.getUnitId() == null ? null : invBarcode.getUnitId().getName());
            itemsBarcodesDetailsView.setPaintColorName(invBarcode.getPaintColor() == null ? null : invBarcode.getPaintColor().getName());
            itemsBarcodesDetailsView.setScrewing(invBarcode.getScrewing());
            itemsBarcodesDetailsView.setIsInventoryItem(invBarcode.getItemId() == null ? 1 : invBarcode.getItemId().getIsInventoryItem());
            list.add(itemsBarcodesDetailsView);
        }
        return list;
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
