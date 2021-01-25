/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.bussinessservice.global;

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
import java.util.ArrayList;
import java.util.HashMap;
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
    public List<ItemsBarcodesDetailsView> autoCompleteItemsBarcodesDetailsViewBranchId(Integer branchId, String qeury) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        params.put("qeury", qeury);
        appendItemsBarcodesDetailsViewBranchId(sql);
        return dao.findListByQuery(sql.toString(), params, 0, 100);
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
    public List<InvOrganizationSiteDTO> autoCompleteOrganizationSiteDTO(Integer branchId, boolean active, Integer type, String qeury) {
        List<InvOrganizationSiteDTO> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        params.put("qeury", qeury);
        appendOrganizationTypeAndActive(params, type, sql, active);
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params, 0, 10);
        list = returnListDTO(detailses);
        return list;
    }
    
    @Override
    public List<InvInvoiceDTO> autoCompleteInvPurchaseForFactoryDTO(Integer branchId,String qeury) {
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
    public List<InvOrganizationSite> autoCompleteOrganizationSite(Integer branchId, boolean active, Integer type, String qeury) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        params.put("branchId", branchId);
        params.put("qeury", qeury);
        appendOrganizationTypeAndActive(params, type, sql, active);
        List<InvOrganizationSite> detailses = dao.findListByQuery(sql.toString(), params, 0, 10);
        List<InvOrganizationSite> DTOlist = new ArrayList();
        for (InvOrganizationSite e : detailses) {
            DTOlist.add(mapToInvOrganizationSite(e));
        }
        return DTOlist;
    }

    private void appendOrganizationTypeAndActive(Map<String, Object> params, Integer type, StringBuilder sql, boolean active) {
        params.put("type", type);
        sql.append("SELECT e FROM InvOrganizationSite e WHERE e.branchId.id = :branchId AND ((:qeury is null OR e.name  like CONCAT('%',:qeury ,'%')) OR (:qeury is null OR e.code  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.phone  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.mobile  like CONCAT('%',:qeury ,'%') ) OR (:qeury is null OR e.email  like CONCAT('%',:qeury ,'%') ) )");
        if (type == 0 || type == 1) {
            sql.append(" AND (e.type = :type OR e.type = 2) ");
        } else {
            sql.append(" AND e.type = :type  ");
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
        InvOrganizationSiteDTO dTO = new InvOrganizationSiteDTO();
        InvOrganizationSite entity = (InvOrganizationSite) Entity;
        dTO.setId(entity.getId());
        dTO.setIndex(entity.getId());
        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
        if (entity.getCurrencyId() != null) {
            CurrencyDTO currencyDTO = new CurrencyDTO();
            currencyDTO.setId(entity.getCurrencyId().getId());
            currencyDTO.setCode(entity.getCurrencyId().getCode());
            currencyDTO.setName(entity.getCurrencyId().getName());
            dTO.setCurrencyId(currencyDTO);
        }
        if (entity.getAccountId() != null) {
            GlAccountDTO accountDTO = new GlAccountDTO();
            accountDTO.setAccNumber(entity.getAccountId().getAccNumber());
            accountDTO.setId(entity.getAccountId().getId());
            dTO.setAccountId(accountDTO);
        }
        dTO.setOpenBalanceCredit(entity.getOpenBalanceCredit());
        dTO.setOpenBalanceDebit(entity.getOpenBalanceDebit());
        dTO.setOpenBalanceDate(entity.getOpenBalanceDate());
        return (T) dTO;
    }
    private InvOrganizationSite mapToInvOrganizationSite(InvOrganizationSite Entity) {
        InvOrganizationSite dTO = new InvOrganizationSite();
        InvOrganizationSite entity = (InvOrganizationSite) Entity;
        dTO.setId(entity.getId());
        dTO.setCode(entity.getCode());
        dTO.setName(entity.getName());
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
