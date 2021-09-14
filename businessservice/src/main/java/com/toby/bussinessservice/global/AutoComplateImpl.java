/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.bussinessservice.global;

import com.toby.businessservice.OrganizationSiteService;
import com.toby.core.GenericDAO;
import com.toby.dto.CostCenterDTO;
import com.toby.dto.CurrencyDTO;
import com.toby.dto.GlAccountDTO;
import com.toby.dto.GlAdminUnitDTO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.InvPurchaseInvoiceDTO;
import com.toby.dto.InventoryDelegatorDTO;
import com.toby.entity.CostCenter;
import com.toby.entity.Currency;
import com.toby.entity.GlAccount;
import com.toby.entity.GlAdminUnit;
import com.toby.entity.InvInventory;
import com.toby.entity.InvItem;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.InvPurchaseInvoice;
import com.toby.entity.InventoryDelegator;
import com.toby.views.ItemsBarcodesDetailsView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author AhmedEssam
 */
@Stateless
public class AutoComplateImpl implements AutoCompleteService {

    @EJB
    private GenericDAO dao;
    @EJB
    private OrganizationSiteService organizationSiteService;

    @Override
    public List<InvInvoiceDTO> autoCompleteProWorkOrderByBranch(Integer branchId, String qeury) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("qeury", qeury);
        List<InvPurchaseInvoice> list = dao.findListByQuery("SELECT s FROM InvPurchaseInvoice s WHERE  s.branchId.id = :branchId AND s.type = 5 AND  ( FUNCTION('DATE_FORMAT',s.date, '%d-%m-%Y')  like CONCAT('%',:globalFilter ,'%') OR  s.serial like CONCAT('%',:globalFilter ,'%')  OR s.invInventoryId.name  like CONCAT('%',:globalFilter ,'%') OR s.galaryId.name  like CONCAT('%',:globalFilter ,'%') OR s.organizationSiteId.name like CONCAT('%',:globalFilter ,'%') OR  s.invDelegatorId.name like CONCAT('%',:globalFilter ,'%') OR s.paymentType  like CONCAT('%',:globalFilter ,'%') OR s.createdBy.name like CONCAT('%',:globalFilter ,'%') OR s.remarks like CONCAT('%',:globalFilter ,'%'))  ORDER BY s.serial ASC ", params, 0, 10);
        List<InvInvoiceDTO> listDTO = new ArrayList<>();
        listDTO = returnListDTO(list);
        return listDTO;
    }

    @Override
    public List<ItemsBarcodesDetailsView> autoCompleteItemsBarcodesDetailsViewBranchId(Integer branchId, String qeury,Integer gallaryId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        params.put("qeury", qeury);
        appendItemsBarcodesDetailsViewBranchId(sql,qeury, gallaryId,null);
        List<ItemsBarcodesDetailsView> list = dao.findListByQuery(sql.toString(), params, 0, 30);
        if (list != null && !list.isEmpty()) {
            Map<Integer, ItemsBarcodesDetailsView> map = new HashMap<>();
            for (ItemsBarcodesDetailsView barcodesDetailsView : list) {
                if (!map.containsKey(barcodesDetailsView.getItemId())) {
                    map.put(barcodesDetailsView.getItemId(), barcodesDetailsView);
                }
            }
            list = new ArrayList<ItemsBarcodesDetailsView>(map.values());
        }
        return list;
    }

    @Override
    public List<ItemsBarcodesDetailsView> findAllItemsBarcodesDetailsViewBranchId(Integer branchId, String qeury,Integer gallaryId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        params.put("qeury", qeury);
        appendItemsBarcodesDetailsViewBranchId(sql, qeury, gallaryId,30);
        Map<Integer, ItemsBarcodesDetailsView> map = new HashMap<>();
        List<Object[]> res = dao.executeNativeQuery(sql.toString());
        List<ItemsBarcodesDetailsView> list = new ArrayList<>();
        Integer index = 1 ;
        Iterator it = res.iterator();
        while (it.hasNext()) {
            Object[] line = (Object[]) it.next();
            ItemsBarcodesDetailsView detailsIterated = new ItemsBarcodesDetailsView();
            Integer itemId = (Integer) line[1];
            if (!map.containsKey(itemId)) {
                detailsIterated.setId(index++);
                detailsIterated.setBarcode((String) line[0]);
                detailsIterated.setItemId((Integer) line[1]);
                detailsIterated.setCostAverage((BigDecimal) line[2]);
                detailsIterated.setName((String) line[3]);
                detailsIterated.setNameEn((String) line[4]);
                detailsIterated.setSellPrice((BigDecimal) line[5]);
                detailsIterated.setUnitId((Integer) line[6]);
                detailsIterated.setMinPriceMen((BigDecimal) line[7]);
                detailsIterated.setMaxPriceMen((BigDecimal) line[8]);
                detailsIterated.setMinPriceYoung((BigDecimal) line[9]);
                detailsIterated.setMaxPriceYoung((BigDecimal) line[10]);
                detailsIterated.setDiscountrate((BigDecimal) line[11]);
                detailsIterated.setUnitName((String) line[12]);
                detailsIterated.setScrewing((BigDecimal) line[13]);
                detailsIterated.setIsInventoryItem((Integer) line[14]);
                detailsIterated.setTypeShow((Integer) line[15]);
                detailsIterated.setProGroupId((Integer) line[16]);
                detailsIterated.setNumberMetersYoung((BigDecimal) line[17]);
                detailsIterated.setNumberMetersMen((BigDecimal) line[18]);
                detailsIterated.setNumberMetersFreeYoung((BigDecimal) line[19]);
                detailsIterated.setNumberMetersFreeMen((BigDecimal) line[20]);
                map.put(itemId, detailsIterated);
                list.add(detailsIterated);
            }
            
        }
        return list;
    }

    private void appendItemsBarcodesDetailsViewBranchId(StringBuilder sql, String qeury,Integer gallaryId,Integer limit) {

        if(gallaryId != null){
            sql.append(" SELECT \n"
                + "        `ii`.`code` AS `barcode`,\n"
                + "        `ii`.`id` AS `item_id`,\n"
                + "        `ii`.`cost_average` AS `cost_average`,\n"
                + "        `ii`.`name` AS `name`,\n"
                + "        `ii`.`nameen` AS `name_en`,\n"
                + "        `ii`.`last_cost` AS `sell_price`,\n"
                + "        `ii`.`unit_id` AS `unit_id`,\n"
                + "        `iip`.`min_price_men` AS `min_price_men`,\n"
                + "        `iip`.`max_price_men` AS `max_price_men`,\n"
                + "        `iip`.`min_price_young` AS `min_price_young`,\n"
                + "        `iip`.`max_price_young` AS `max_price_young`,\n"
                + "        `ii`.`discount_rate` AS `discount_rate`,\n"
                + "        `s`.`name` AS `unit_name`,\n"
                + "        1 AS `screwing`,\n"
                + "        `ii`.`is_inventory_item` AS `is_inventory_item`,\n"
                + "        `ii`.`item_natural` AS `type_show`,\n"
                + "        `ii`.`pro_group_id` AS `pro_group_id`,\n"
                + "        `ii`.`number_meters_young` AS `number_meters_young`,\n"
                + "        `ii`.`number_meters_men` AS `number_meters_men`,\n"
                + "        `ii`.`number_meters_free_young` AS `number_meters_free_young`,\n"
                + "        `ii`.`number_meters_free_men` AS `number_meters_free_men`\n"
                + "    FROM\n"
                + "        (((`inv_item_price` `iip`\n"
                + "        LEFT JOIN `inv_item` `ii` ON ((`ii`.`id` = `iip`.`item_id`)))\n"
                + "        LEFT JOIN `symbol` `sy` ON ((`ii`.`paint_color` = `sy`.`id`)))\n"
                + "        LEFT JOIN `symbol` `s` ON ((`ii`.`unit_id` = `s`.`id`))) where iip.gallary_id = "+gallaryId+" and ii.branch_id = 182 and ( ii.name  like CONCAT('%'," + qeury + " ,'%') OR `ii`.`code`  like CONCAT('%'," + qeury + ",'%')) \n"
                + "    UNION ALL ");
        }
        sql.append(" SELECT \n"
                + "        `ii`.`code` AS `barcode`,\n"
                + "        `ii`.`id` AS `item_id`,\n"
                + "        `ii`.`cost_average` AS `cost_average`,\n"
                + "        `ii`.`name` AS `name`,\n"
                + "        `ii`.`nameen` AS `name_en`,\n"
                + "        `ii`.`last_cost` AS `sell_price`,\n"
                + "        `ii`.`unit_id` AS `unit_id`,\n"
                + "        `ii`.`min_price_men` AS `min_price_men`,\n"
                + "        `ii`.`max_price_men` AS `max_price_men`,\n"
                + "        `ii`.`min_price_young` AS `min_price_young`,\n"
                + "        `ii`.`max_price_young` AS `max_price_young`,\n"
                + "        `ii`.`discount_rate` AS `discount_rate`,\n"
                + "        `s`.`name` AS `unit_name`,\n"
                + "        1 AS `screwing`,\n"
                + "        `ii`.`is_inventory_item` AS `is_inventory_item`,\n"
                + "        `ii`.`item_natural` AS `type_show`,\n"
                + "        `ii`.`pro_group_id` AS `pro_group_id`,\n"
                + "        `ii`.`number_meters_young` AS `number_meters_young`,\n"
                + "        `ii`.`number_meters_men` AS `number_meters_men`,\n"
                + "        `ii`.`number_meters_free_young` AS `number_meters_free_young`,\n"
                + "        `ii`.`number_meters_free_men` AS `number_meters_free_men`\n"
                + "    FROM\n"
                + "        ((`inv_item` `ii`\n"
                + "        LEFT JOIN `symbol` `s` ON ((`ii`.`unit_id` = `s`.`id`)))\n"
                + "        LEFT JOIN `symbol` `sy` ON ((`ii`.`paint_color` = `sy`.`id`))) where ii.branch_id = 182 and ( ii.name  like CONCAT('%'," + qeury + " ,'%') OR `ii`.`code`  like CONCAT('%'," + qeury + ",'%')) \n"
                + "    UNION ALL SELECT \n"
                + "        `ib`.`code` AS `barcode`,\n"
                + "        `ii`.`id` AS `item_id`,\n"
                + "        `ii`.`cost_average` AS `cost_average`,\n"
                + "        `ii`.`name` AS `name`,\n"
                + "        `ii`.`nameen` AS `name_en`,\n"
                + "        `ib`.`price1` AS `sell_price`,\n"
                + "        `ib`.`unit_id` AS `unit_id`,\n"
                + "        `ib`.`min_price_men` AS `min_price_men`,\n"
                + "        `ib`.`max_price_men` AS `max_price_men`,\n"
                + "        `ib`.`min_price_young` AS `min_price_young`,\n"
                + "        `ib`.`max_price_young` AS `max_price_young`,\n"
                + "        `ib`.`discount_rate` AS `discount_rate`,\n"
                + "        `s`.`name` AS `unit_name`,\n"
                + "        `ib`.`screwing` AS `screwing`,\n"
                + "        `ii`.`is_inventory_item` AS `is_inventory_item`,\n"
                + "        `ib`.`item_natural` AS `type_show`,\n"
                + "        `ii`.`pro_group_id` AS `pro_group_id`,\n"
                + "        `ii`.`number_meters_young` AS `number_meters_young`,\n"
                + "        `ii`.`number_meters_men` AS `number_meters_men`,\n"
                + "        `ii`.`number_meters_free_young` AS `number_meters_free_young`,\n"
                + "        `ii`.`number_meters_free_men` AS `number_meters_free_men`\n"
                + "    FROM\n"
                + "        (((`inv_barcode` `ib`\n"
                + "        LEFT JOIN `inv_item` `ii` ON ((`ii`.`id` = `ib`.`item_id`)))\n"
                + "        LEFT JOIN `symbol` `sy` ON ((`ib`.`paint_color` = `sy`.`id`)))\n"
                + "        LEFT JOIN `symbol` `s` ON ((`ib`.`unit_id` = `s`.`id`))) where ii.branch_id = 182 and ( ii.name  like CONCAT('%'," + qeury + " ,'%') OR `ii`.`code`  like CONCAT('%'," + qeury + ",'%')) ");

        if(limit != null){
            sql.append("limit 30");
        }
//        sql.append("SELECT e FROM ItemsBarcodesDetailsView e WHERE e.branchId = :branchId AND ((:qeury is null OR e.name  like CONCAT('%',:qeury ,'%')) OR (:qeury is null OR e.barcode  like CONCAT('%',:qeury ,'%') ))");
//
//        sql.append(" ORDER BY e.barcode");
    }

    @Override
    public Map<String, ItemsBarcodesDetailsView> findItemsBarcodesDetailsViewBranchIdMap(Integer branchId, String qeury) {
        Map<String, Object> params = new HashMap<>();
        Map<String, ItemsBarcodesDetailsView> itemsBarcodesDetailsViewMap = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        params.put("qeury", qeury);
        appendItemsBarcodesDetailsView(sql);
        List<ItemsBarcodesDetailsView> itemsBarcodesDetailsViewList = dao.findListByQuery(sql.toString(), params, 0, 10);
        for (ItemsBarcodesDetailsView itemsBarcodesDetailsView : itemsBarcodesDetailsViewList) {
            itemsBarcodesDetailsViewMap.put(itemsBarcodesDetailsView.getBarcode(), itemsBarcodesDetailsView);
        }
        return itemsBarcodesDetailsViewMap;
    }

    private void appendItemsBarcodesDetailsView(StringBuilder sql) {

        sql.append("SELECT e FROM ItemsBarcodesDetailsView e WHERE e.branchId = :branchId AND e.isInventoryItem = 1 AND ((:qeury is null OR e.name  like CONCAT('%',:qeury ,'%')) OR (:qeury is null OR e.barcode  like CONCAT('%',:qeury ,'%') ))");

        sql.append(" ORDER BY e.barcode");
    }

    @Override
    public List<InvItemDTO> autoCompleteInvItem(Integer branchId, String qeury) {
        List<InvItemDTO> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        params.put("qeury", qeury);
        appendInvItem(sql);
        List<InvItem> detailses = dao.findListByQuery(sql.toString(), params, 0, 30);
        list = returnListDTO(detailses);
        return list;
    }

    private void appendInvItem(StringBuilder sql) {

        sql.append("SELECT e FROM InvItem e WHERE e.branchId.id = :branchId AND e.isInventoryItem = 1 AND ((:qeury is null OR e.name  like CONCAT('%',:qeury ,'%')) OR (:qeury is null OR e.code  like CONCAT('%',:qeury ,'%') ))");

        sql.append(" ORDER BY e.code");
    }

    @Override
    public List<InvOrganizationSiteDTO> autoCompleteOrganizationSiteDTO(Integer branchId, boolean active, Integer type, String qeury, Integer gallaryId) {
        List<InvOrganizationSiteDTO> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        params.put("qeury", qeury);
        if (gallaryId != null) {
            params.put("gallaryId", gallaryId);
        }
        appendOrganizationTypeAndActive(params, type, sql, active, gallaryId);
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params, 0, 10);
        list = returnListDTO(detailses);
        return list;
    }

    @Override
    public List<InvInvoiceDTO> autoCompleteInvPurchaseForFactoryDTO(Integer branchId, String qeury) {
        List<InvInvoiceDTO> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        params.put("qeury", qeury);
        appendInvPurchaseForFactory(sql);
        List<InvPurchaseInvoice> detailses = dao.findListByQuery(sql.toString(), params, 0, 10);
        list = returnListDTO(detailses);
        return list;
    }

    private void appendInvPurchaseForFactory(StringBuilder sql) {

        sql.append("SELECT distinct(p.invPurchaseInvoiceId) FROM ProFactoryDelivery p WHERE p.branchId.id = :branchId AND ((:qeury is null OR p.invPurchaseInvoiceId.serial  like CONCAT('%',:qeury ,'%')))");

        sql.append(" ORDER BY p.invPurchaseInvoiceId.serial");
    }

    @Override
    public List<InvOrganizationSite> autoCompleteOrganizationSite(Integer branchId, boolean active, Integer type, String qeury, Integer gallaryId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        params.put("qeury", qeury);
        appendOrganizationTypeAndActive(params, type, sql, active, gallaryId);
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params, 0, 10);
        List<InvOrganizationSite> DTOlist = new ArrayList();
        for (InvOrganizationSite e : detailses) {
            DTOlist.add(mapToInvOrganizationSite(e));
        }
        return DTOlist;
    }

    private void appendOrganizationTypeAndActive(Map<String, Object> params, Integer type, StringBuilder sql, boolean active, Integer gallaryId) {
        params.put("type", type);
        sql.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ((:qeury is null OR e.name  like CONCAT('%',:qeury ,'%')) OR (:qeury is null OR e.code  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.phone  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.mobile  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.email  like CONCAT('%',:qeury ,'%') ) )");
        if (type == 0 || type == 1) {
            sql.append(" AND (e.type = :type OR e.type = 2) ");
        } else {
            sql.append(" AND e.type = :type  ");
        }
        if (gallaryId != null) {
            params.put("gallaryId", gallaryId);
            sql.append(" AND e.gallaryId.id = :gallaryId ");
        }
        if (active) {
            sql.append(" AND e.active = 1 ");
        }
        sql.append(" ORDER BY e.code");
    }

    @Override
    public List<InventoryDelegatorDTO> autoCompleteInventoryDelegatorByBranch(Integer branchId, Integer type, String qeury) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        params.put("qeury", qeury);
        String sql = "SELECT i FROM InventoryDelegator i where i.branchId.id = :branchId AND i.type = :type AND ((:qeury is null OR i.name  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR i.code  like CONCAT('%',:qeury ,'%'))) ORDER BY i.code";
        List<InventoryDelegator> inventoryDelegatorList = dao.findListByQuery(sql, params, 0, 10);
        List<InventoryDelegatorDTO> list = new ArrayList<>();
        list = returnListDTO(inventoryDelegatorList);
        return list;
    }

    @Override
    public List<CostCenterDTO> autoCompleteActiveSubCostCentersByBranch(Integer branchId, String qeury) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("qeury", qeury);
        List<CostCenter> costCenterList = dao.findListByQuery("SELECT cs FROM CostCenter cs WHERE cs.parent IS NOT NULL AND cs.branchId.id=:branchId AND cs.active = 1 AND ((:qeury is null OR cs.name  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR cs.code  like CONCAT('%',:qeury ,'%')))  ORDER BY cs.code ASC ", params, 0, 10);
        List<CostCenterDTO> list = new ArrayList<>();
        list = returnListDTO(costCenterList);
        return list;
    }

    private List<GlAdminUnit> getAllAdminUnitByBranchIdActive(Integer branchId, String qeury) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("qeury", qeury);
        return dao.findListByQuery("SELECT e FROM GlAdminUnit e WHERE  e.branchId.id= :branchId and e.active = 1 AND ((:qeury is null OR e.name  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.code  like CONCAT('%',:qeury ,'%')))  ORDER BY e.code ASC ", params, 0, 10);
    }

    @Override
    public List<InvInventoryDTO> getGallaryByBranchId(Integer branchId, String qeury) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("qeury", qeury);
        List<InvInventory> invInventoryList = dao.findListByQuery("SELECT i FROM InvInventory i WHERE i.invType = 2 AND i.branchId.id=:branchId AND ((:qeury is null OR i.name  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR i.code  like CONCAT('%',:qeury ,'%')))  ORDER BY i.code ASC ", params, 0, 10);
        List<InvInventoryDTO> list = new ArrayList<>();
        list = returnListDTO(invInventoryList);
        return list;
    }

    @Override
    public List<GlAdminUnitDTO> autoCompleteSubAdminUnitByBranchIdActiveDTO(Integer branchId, String qeury) {
        List<GlAdminUnit> allAdminUnits = getAllAdminUnitByBranchIdActive(branchId, qeury);
        List<GlAdminUnit> childrens = new ArrayList<>();
        Map<Integer, GlAdminUnit> map = new HashMap<>();

        for (GlAdminUnit gau : allAdminUnits) {
            if (gau.getParent() != null) {
                map.put(gau.getParent().getId(), gau);
            }
        }

        for (GlAdminUnit gau : allAdminUnits) {
            if (!map.containsKey(gau.getId())) {
                if (gau.getParent() == null) {
                    childrens.add(gau);
                    continue;
                }
                childrens.add(gau);
            }
        }
        List<GlAdminUnitDTO> list = new ArrayList<>();
        list = returnListDTO(childrens);
        return list;
    }

    private <T, E> List<T> returnListDTO(List<E> list) {
        List<T> DTOlist = new ArrayList();
        for (E e : list) {
            DTOlist.add((T) initMapToDTO(e));
        }
        return DTOlist;
    }

    private <T, E> T initMapToDTO(E Entity) {
        if (Entity.getClass().equals(InvOrganizationSite.class)) {
            return mapToInvOrganizationSiteDTO(Entity);
        } else if (Entity.getClass().equals(InventoryDelegator.class)) {
            return mapToInventoryDelegatorDTO(Entity);
        } else if (Entity.getClass().equals(CostCenter.class)) {
            return mapToCostCenterDTO(Entity);
        } else if (Entity.getClass().equals(GlAdminUnit.class)) {
            return mapToGlAdminUnitDTO(Entity);
        } else if (Entity.getClass().equals(InvItem.class)) {
            return mapToInvItemDTO(Entity);
        } else if (Entity.getClass().equals(InvPurchaseInvoice.class)) {
            return mapToInvInvoiceDTO(Entity);
        } else if (Entity.getClass().equals(InvInventory.class)) {
            return mapToInvInventoryDTO(Entity);
        }
        return null;
    }

    private <E, T> T mapToInvOrganizationSiteDTO(E Entity) {
        InvOrganizationSite entity = (InvOrganizationSite) Entity;
        InvOrganizationSiteDTO dTO = organizationSiteService.mapToDTOSmall(entity);
        return (T) dTO;
    }

    private InvOrganizationSite mapToInvOrganizationSite(InvOrganizationSite Entity) {
        InvOrganizationSite dTO = new InvOrganizationSite();
        InvOrganizationSite entity = (InvOrganizationSite) Entity;
        dTO.setId(entity.getId());
        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
        dTO.setMobile(entity.getMobile());
        if (entity.getCurrencyId() != null) {
            Currency currencyDTO = new Currency();
            currencyDTO.setId(entity.getCurrencyId().getId());
            currencyDTO.setCode(entity.getCurrencyId().getCode());
            currencyDTO.setName(entity.getCurrencyId().getName());
            dTO.setCurrencyId(currencyDTO);
        }
        if (entity.getAccountId() != null) {
            GlAccount accountDTO = new GlAccount();
            accountDTO.setAccNumber(entity.getAccountId().getAccNumber());
            accountDTO.setId(entity.getAccountId().getId());
            dTO.setAccountId(accountDTO);
        }
        dTO.setOpenBalanceCredit(entity.getOpenBalanceCredit());
        dTO.setOpenBalanceDebit(entity.getOpenBalanceDebit());
        dTO.setOpenBalanceDate(entity.getOpenBalanceDate());
        return dTO;
    }

    private <E, T> T mapToInventoryDelegatorDTO(E Entity) {
        InventoryDelegatorDTO dTO = new InventoryDelegatorDTO();
        InventoryDelegator entity = (InventoryDelegator) Entity;
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
        return (T) dTO;
    }

    private <E, T> T mapToCostCenterDTO(E Entity) {
        CostCenterDTO dTO = new CostCenterDTO();
        CostCenter entity = (CostCenter) Entity;
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
        return (T) dTO;
    }

    private <E, T> T mapToGlAdminUnitDTO(E Entity) {
        GlAdminUnitDTO dTO = new GlAdminUnitDTO();
        GlAdminUnit entity = (GlAdminUnit) Entity;
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
        return (T) dTO;
    }

    private <E, T> T mapToInvItemDTO(E Entity) {
        InvItemDTO dTO = new InvItemDTO();
        InvItem entity = (InvItem) Entity;
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
        return (T) dTO;
    }

    private <E, T> T mapToInvInvoiceDTO(E Entity) {
        InvInvoiceDTO dTO = new InvInvoiceDTO();
        InvPurchaseInvoice entity = (InvPurchaseInvoice) Entity;
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setSerial(entity.getSerial());
        return (T) dTO;
    }

    private <E, T> T mapToInvInventoryDTO(E Entity) {
        InvInventoryDTO dTO = new InvInventoryDTO();
        InvInventory entity = (InvInventory) Entity;
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
        return (T) dTO;
    }
}
