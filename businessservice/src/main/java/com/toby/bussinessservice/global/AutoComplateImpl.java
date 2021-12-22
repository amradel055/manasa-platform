/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.bussinessservice.global;

import com.toby.businessservice.EmployeeService;
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
import com.toby.dto.InventoryDelegatorDTO;
import com.toby.dto.OfferCompanyDTO;
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
    @EJB
    private EmployeeService employeeService;

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
    public List<ItemsBarcodesDetailsView> autoCompleteItemsBarcodesDetailsViewBranchId(Integer branchId, Integer type, String qeury) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        params.put("qeury", qeury);
        sql.append(" SELECT \n"
                + "        `ii`.`code` AS `barcode`,\n"
                + "        `ii`.`id` AS `item_id`,\n"
                + "        `ii`.`cost_average` AS `cost_average`,\n"
                + "        `ii`.`name` AS `name`,\n"
                + "        `ii`.`last_cost` AS `sell_price`,\n"
                + "        `ii`.`unit_id` AS `unit_id`,\n"
                + "        `ii`.`min_price_men` AS `min_price_men`,\n"
                + "        `ii`.`max_price_men` AS `max_price_men`,\n"
                + "        `ii`.`min_price_young` AS `min_price_young`,\n"
                + "        `ii`.`max_price_young` AS `max_price_young`,\n"
                + "        `ii`.`discount_rate` AS `discount_rate`,\n"
                + "        `s`.`name` AS `unit_name`,\n"
                + "        `ii`.`is_inventory_item` AS `is_inventory_item`,\n"
                + "        `ii`.`item_natural` AS `type_show`,\n"
                + "        `ii`.`pro_group_id` AS `pro_group_id`,\n"
                + "        `ii`.`number_meters_young` AS `number_meters_young`,\n"
                + "        `ii`.`number_meters_men` AS `number_meters_men`,\n"
                + "        `ii`.`number_meters_free_young` AS `number_meters_free_young`,\n"
                + "        `ii`.`number_meters_free_men` AS `number_meters_free_men`,\n"
                + "        `ii`.`site_id` AS `supplier_id`,\n"
                + "        `ii`.`type` AS `type`,\n"
                + "        `ii`.`price_type` AS `price_type`\n"
                + "    FROM\n"
                + "        (`inv_item` `ii`\n"
                + "        LEFT JOIN `symbol` `s` ON ((`ii`.`unit_id` = `s`.`id`)))\n"
                + "         where ii.branch_id = 182 AND ");
        if (type != null && type == 0) {
            sql.append("      (ii.name  like CONCAT('%','").append(qeury).append("' ,'%')) ");
        } else if (type != null && type == 0) {
            sql.append(" OR (ii.code  like CONCAT('%','").append(qeury).append("' ,'%') )");
        } else {
            sql.append("      (ii.name  like CONCAT('%','").append(qeury).append("' ,'%')) ");
            sql.append(" OR (ii.code  like CONCAT('%','").append(qeury).append("' ,'%') )");
        }

        sql.append(" order by ii.code limit 30 ");
//        appendItemsBarcodesDetailsViewBranchId(sql);
//          List<ItemsBarcodesDetailsView>  list = dao.executeNativeQuery(sql.toString());
//          return list;
        List<Object[]> res = dao.executeNativeQuery(sql.toString());
        return convertToItemsBarcodesDetailsView(res);
    }

    public List<ItemsBarcodesDetailsView> convertToItemsBarcodesDetailsView(List<Object[]> res) {

        List<ItemsBarcodesDetailsView> list = new ArrayList<>();

        Integer index = 1;
        Iterator it = res.iterator();
        while (it.hasNext()) {
            index++;
            Object[] line = (Object[]) it.next();
            ItemsBarcodesDetailsView dto = new ItemsBarcodesDetailsView();
            dto.setId(index);
            dto.setBarcode((String) line[0]);
            dto.setItemId((Integer) line[1]);
            dto.setCostAverage((BigDecimal) line[2]);
            dto.setName((String) line[3]);
            dto.setSellPrice((BigDecimal) line[4]);
            dto.setUnitId((Integer) line[5]);
            dto.setMinPriceMen((BigDecimal) line[6]);
            dto.setMaxPriceMen((BigDecimal) line[7]);
            dto.setMinPriceYoung((BigDecimal) line[8]);
            dto.setMaxPriceYoung((BigDecimal) line[9]);
            dto.setDiscountrate((BigDecimal) line[10]);
            dto.setUnitName((String) line[11]);
//            dto.setScrewing(new BigDecimal((Long) line[12]));
            dto.setIsInventoryItem((Integer) line[12]);
            dto.setTypeShow((Integer) line[13]);
            dto.setProGroupId((Integer) line[14]);
            dto.setNumberMetersYoung((BigDecimal) line[15]);
            dto.setNumberMetersMen((BigDecimal) line[16]);
            dto.setNumberMetersFreeYoung((BigDecimal) line[17]);
            dto.setNumberMetersFreeMen((BigDecimal) line[18]);
            dto.setSupplierId((Integer) line[19]);
            dto.setType((Integer) line[20]);
            dto.setPrice_type((Integer) line[21]);

            list.add(dto);
        }
        return list;
    }

    @Override
    public List<ItemsBarcodesDetailsView> findAllItemsBarcodesDetailsViewBranchId(Integer branchId, String qeury) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        params.put("qeury", qeury);
        appendItemsBarcodesDetailsViewBranchId(sql);
        return dao.findListByQuery(sql.toString(), params);
    }

    private void appendItemsBarcodesDetailsViewBranchId(StringBuilder sql) {

        sql.append("SELECT e FROM ItemsBarcodesDetailsView e WHERE e.branchId = :branchId AND ((:qeury is null OR e.name  like CONCAT('%',:qeury ,'%')) OR (:qeury is null OR e.barcode  like CONCAT('%',:qeury ,'%') ))");

        sql.append(" ORDER BY e.barcode");
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
        List<InvItem> detailses = dao.findListByQuery(sql.toString(), params, 0, 10);
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
        list = returnListDTO2(detailses);
        return list;
    }

    private List<InvOrganizationSiteDTO> returnListDTO2(List<InvOrganizationSite> list) {
        List<InvOrganizationSiteDTO> DTOlist = new ArrayList();
        for (InvOrganizationSite e : list) {
            DTOlist.add(mapToDTOSmall(e));
        }
        return DTOlist;
    }

    public InvOrganizationSiteDTO mapToDTOSmall(InvOrganizationSite invOrganizationSite) {
        InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
        invOrganizationSiteDTO.setId(invOrganizationSite.getId());
        invOrganizationSiteDTO.setName(invOrganizationSite.getName());
        invOrganizationSiteDTO.setCode(invOrganizationSite.getCode());
        invOrganizationSiteDTO.setMobile(invOrganizationSite.getMobile());
        invOrganizationSiteDTO.setLength(invOrganizationSite.getLength());
        invOrganizationSiteDTO.setShoulder(invOrganizationSite.getShoulder());
        invOrganizationSiteDTO.setStep(invOrganizationSite.getStep());
        invOrganizationSiteDTO.setDiscount(invOrganizationSite.getDiscount());
        if (invOrganizationSite.getCurrencyId() != null) {
            CurrencyDTO currencyDTO = new CurrencyDTO();
            currencyDTO.setId(invOrganizationSite.getCurrencyId().getId());
            currencyDTO.setCode(invOrganizationSite.getCurrencyId().getCode());
            currencyDTO.setName(invOrganizationSite.getCurrencyId().getName());
            invOrganizationSiteDTO.setCurrencyId(currencyDTO);
        }
        if (invOrganizationSite.getParent() != null) {
            InvOrganizationSiteDTO parent = new InvOrganizationSiteDTO();
            parent.setId(invOrganizationSite.getParent().getId());
            parent.setName(invOrganizationSite.getParent().getName());
            parent.setCode(invOrganizationSite.getParent().getCode());
            parent.setMobile(invOrganizationSite.getParent().getMobile());
            parent.setDiscount(invOrganizationSite.getParent().getDiscount());
            invOrganizationSiteDTO.setParent(parent);
        }
        if (invOrganizationSite.getOfferCompanyId() != null) {
            OfferCompanyDTO offerCompanyDTO = new OfferCompanyDTO();
            offerCompanyDTO.setId(invOrganizationSite.getOfferCompanyId().getId());
            offerCompanyDTO.setName(invOrganizationSite.getOfferCompanyId().getName());
            offerCompanyDTO.setCode(invOrganizationSite.getOfferCompanyId().getCode());
            offerCompanyDTO.setDiscount(invOrganizationSite.getOfferCompanyId().getDiscount());
            invOrganizationSiteDTO.setOfferCompanyDTO(offerCompanyDTO);
        }

        if (invOrganizationSite.getGallaryId() != null) {
            InvInventoryDTO gallary = new InvInventoryDTO();
            gallary.setId(invOrganizationSite.getGallaryId().getId());
            gallary.setName(invOrganizationSite.getGallaryId().getName());
            gallary.setCode(invOrganizationSite.getGallaryId().getCode());
            invOrganizationSiteDTO.setGallaryIdDTO(gallary);
        }

        if (invOrganizationSite.getAccountId() != null) {
            GlAccountDTO accountDTO = new GlAccountDTO();
            accountDTO.setAccNumber(invOrganizationSite.getAccountId().getAccNumber());
            accountDTO.setName(invOrganizationSite.getAccountId().getName());
            accountDTO.setId(invOrganizationSite.getAccountId().getId());
            invOrganizationSiteDTO.setAccountId(accountDTO);
        }
        invOrganizationSiteDTO.setOpenBalanceCredit(invOrganizationSite.getOpenBalanceCredit());
        invOrganizationSiteDTO.setOpenBalanceDebit(invOrganizationSite.getOpenBalanceDebit());
        invOrganizationSiteDTO.setOpenBalanceDate(invOrganizationSite.getOpenBalanceDate());

        return invOrganizationSiteDTO;
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
        sql.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ((:qeury is null OR e.name  like CONCAT('%',:qeury ,'%')) OR (:qeury is null OR e.code  like CONCAT('%',:qeury ,'%') )  OR (:qeury is null OR e.mobile  like CONCAT('%',:qeury ,'%') ) )");
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

    @Override
    public List<InvOrganizationSiteDTO> autoCompleteOrganizationSiteWitAssignedList(Integer branchId, boolean active, Integer type, String qeury, Integer assignedTo) {
        List<InvOrganizationSiteDTO> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        Boolean employeesIsSuperVisor = employeeService.getAllEmployeesIsSuperVisor(assignedTo);
        appendOrganizationTypeAndActive(params, type, sql, active, qeury, assignedTo, employeesIsSuperVisor);
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params, 0, 10);
        list = returnListDTO(detailses);
        return list;
    }

    private void appendOrganizationTypeAndActive(Map<String, Object> params, Integer type, StringBuilder sql, boolean active, String qeury, Integer assignedTo, Boolean assignedIsLeader) {
        params.put("type", type);
        sql.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId");
        if (qeury != null && !qeury.isEmpty()) {
            sql.append(" AND ((:qeury is null OR e.name  like CONCAT('%',:qeury ,'%')) OR (:qeury is null OR e.code  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.phone  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.mobile  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.email  like CONCAT('%',:qeury ,'%') ) )");
            params.put("qeury", qeury);
        }
        if (type == 0 || type == 1) {
            sql.append(" AND (e.type = :type OR e.type = 2) ");
        } else if (type == 5) {
            sql.append(" AND e.type = :type ");
            if (assignedIsLeader && assignedTo != null) {
//                List<Integer> assignedList = employeeService.getAllEmployeesListByParentAndchield((Integer) params.get("branchId"), assignedTo);
//                assignedList.add(assignedTo);
                StringBuilder empIds = new StringBuilder();
                empIds = employeeService.returnEmployeeList(assignedTo, empIds);
                sql.append(" AND e.assignedTo.id IN ( ").append(empIds).append(" )");
            } else if (assignedTo != null) {
                params.put("assignedTo", assignedTo);
                sql.append(" AND e.assignedTo.id = :assignedTo");
            }

        } else if (type == 4) {
            sql.append(" AND (e.type = :type  OR e.type = 1) ");
        } else {
            sql.append(" AND e.type = :type  ");
        }
        if (active) {
            sql.append(" AND e.active = 1 ");
        }
        sql.append(" ORDER BY e.code");
    }

}
