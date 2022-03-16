/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.define.ScreenNameClassEnum;
import com.isag.dto.InvDailogueData;
import com.isag.dto.InvInvoiceDTO;
import com.isag.dto.InventorySetupDTO;
import com.isag.entity.InvAddingorder;
import com.isag.entity.InvPurchaseInvoice;
import com.isag.entity.InvPurchaseOrder;
import com.isag.entity.InvQotation;
import com.isag.entity.InvStrip;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class InvDialogueServiceImpl implements InvDialogueService {

    @EJB
    private GenericDAO dao;
    @EJB
    private InvPurchaseOrderService invPurchaseOrderService;
    @EJB
    private InvQuotationService invQuotationService;
    @EJB
    private InvStripService invStripService;
    @EJB
    private InvAddingOrderService invAddingOrderService;
    @EJB
    private InvPurchaseInvoiceService_1 invPurchaseInvoiceService;
    @EJB
    private InvGlobalService invGlobalService;

    @Override
    public List<InvDailogueData> loadInvDailogueData(int first, int pageSize, String sortField, Boolean asc, Map<String, Object> filters, InventorySetupDTO inventorySetupDTO, ScreenNameClassEnum screenNameClassEnum, Integer inventoryId) {
        List<InvDailogueData> dailogueDataList = new ArrayList<>();
        if (screenNameClassEnum.equals(ScreenNameClassEnum.INVPURCHASEINVOICEFORM)) {
            if (inventorySetupDTO.getSellBuy()) {
//                dailogueDataList = getInvAddingOrderByBranchIdAndNotLoaded(inventorySetupDTO.getBranchId());
                dailogueDataList = getInvAddingOrderByBranchIdAndNotLoaded(first, pageSize, sortField, asc, inventorySetupDTO.getBranchId(), filters);
                return dailogueDataList;
            } else {
                //  dailogueDataList = getPurchaseOrderByBranchIdAndNotLoaded(inventorySetupDTO.getBranchId(), screenNameClassEnum);
                dailogueDataList = getPurchaseOrderByBranchIdAndNotLoaded(first, pageSize, sortField, asc, inventorySetupDTO.getBranchId(), filters, screenNameClassEnum);
                return dailogueDataList;
            }
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.INVSALESINVOICEFORM)) {
//            dailogueDataList = getInvQutationByBranchIdAndNotLoaded(inventorySetupDTO.getBranchId());
//            dailogueDataList = getInvQutationByBranchIdAndNotLoaded(first, pageSize, sortField, asc, inventorySetupDTO.getBranchId(), filters);
            dailogueDataList = getInvPurchaseInvoiceByBranchIdAndNotLoaded(first, pageSize, sortField, asc, inventorySetupDTO.getBranchId(), filters, 0, screenNameClassEnum);

            return dailogueDataList;
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.INVRETURNPURCHASEFORM)) {
//            dailogueDataList = getInvPurchaseInvoiceByBranchIdAndNotLoaded(inventorySetupDTO.getBranchId(), 2, screenNameClassEnum);
            dailogueDataList = getInvPurchaseInvoiceByBranchIdAndNotLoaded(first, pageSize, sortField, asc, inventorySetupDTO.getBranchId(), filters, 2, screenNameClassEnum);
            return dailogueDataList;
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.INVRETURNSALESFORM)) {
            dailogueDataList = getInvPurchaseInvoiceByBranchIdAndNotLoaded(first, pageSize, sortField, asc, inventorySetupDTO.getBranchId(), filters, 3, screenNameClassEnum);
            return dailogueDataList;
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.INVADDINGORDERFORM)) {
            //  dailogueDataList = getPurchaseOrderByBranchIdAndNotLoaded(inventorySetupDTO.getBranchId(), screenNameClassEnum);
            dailogueDataList = getPurchaseOrderByBranchIdAndNotLoaded(first, pageSize, sortField, asc, inventorySetupDTO.getBranchId(), filters, screenNameClassEnum);
            return dailogueDataList;
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.INVPERMISSIONORDERFORM)) {
            //  dailogueDataList = getInvPurchaseInvoiceByBranchIdAndNotLoaded(inventorySetupDTO.getBranchId(), 1, screenNameClassEnum);
            dailogueDataList = getInvPurchaseInvoiceByBranchIdAndNotLoaded(first, pageSize, sortField, asc, inventorySetupDTO.getBranchId(), filters, 1, screenNameClassEnum);
            return dailogueDataList;
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.INVUPDATE)) {
            dailogueDataList = getInvStripByBranchIdAndNotLoaded(first, pageSize, sortField, asc, inventorySetupDTO.getBranchId(), filters, screenNameClassEnum, inventoryId);
            return dailogueDataList;
        }

        return dailogueDataList;
    }

//    private List<InvDailogueData> getPurchaseOrderByBranchIdAndNotLoaded(Integer branchId, ScreenNameClassEnum screenNameClassEnum) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("branchId", branchId);
//        List<InvPurchaseOrder> invPurchaseOrders = null;
//        if (ScreenNameClassEnum.INVPURCHASEINVOICEFORM.equals(screenNameClassEnum) || ScreenNameClassEnum.INVADDINGORDERFORM.equals(screenNameClassEnum)) {
//            invPurchaseOrders = dao.findListByQuery("SELECT e FROM InvPurchaseOrder e WHERE e.branchId.id = :branchId and e.type = 0 and e.id not in (select p.invPurchaseOrderId.id from InvPurchaseInvoice p where p.branchId.id = :branchId AND p.invPurchaseOrderId.id is not null ) and e.id not in (select i.purchaseOrderId.id from InvAddingorder i where i.branchId.id = :branchId AND i.purchaseOrderId.id is not null ) ", params);
//        } else {
//            invPurchaseOrders = dao.findListByQuery("SELECT e FROM InvPurchaseOrder e WHERE e.branchId.id = :branchId and e.type = 1 and e.id not in (select p.purchaseOrderId.id from InvAddingorder p where p.branchId.id = :branchId and  p.purchaseOrderId.id is not null )", params);
//        }
//        List<InvDailogueData> InvDailogueDataList = mapInvPurchaseOrderToDTOList(invPurchaseOrders);
//        return InvDailogueDataList;
//    }
//    private List<InvDailogueData> getInvAddingOrderByBranchIdAndNotLoaded(Integer branchId) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("branchId", branchId);
//        List<InvAddingorder> invAddingOrdersList = dao.findListByQuery("SELECT e FROM InvAddingorder e WHERE e.branchId.id = :branchId and  e.type = 1 and e.id not in (select p.invAddingOrderId.id from InvPurchaseInvoice p where p.branchId.id = :branchId and p.invAddingOrderId.id is not null ) ", params);
//        List<InvDailogueData> InvDailogueDataList = mapInvAddingOrderToDTOList(invAddingOrdersList);
//        return InvDailogueDataList;
//    }
    private List<InvDailogueData> getInvAddingOrderByBranchIdAndNotLoaded(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters) {
        filters.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT e FROM InvAddingorder e WHERE e.branchId.id = :branchId and  e.type = 0 and e.id not in (select p.invAddingOrderId.id from InvPurchaseInvoice p where p.branchId.id = :branchId and p.invAddingOrderId.id is not null ) ");
        appendFilterInvAddingOrder(filters, builder);
        builder.append(" order by e.serial DESC");
        List<InvAddingorder> invAddingOrdersList = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        List<InvDailogueData> InvDailogueDataList = mapInvAddingOrderToDTOList(invAddingOrdersList);
        return InvDailogueDataList;
    }

//    private List<InvDailogueData> getInvQutationByBranchIdAndNotLoaded(Integer branchId) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("branchId", branchId);
//        List<InvQotation> invQotationList = dao.findListByQuery("SELECT e FROM InvQotation e WHERE e.branchId.id = :branchId and  e.type = 1 and e.id not in (select p.invQotationId.id from InvPurchaseInvoice p where p.branchId.id = :branchId and p.invQotationId.id is not null and p.type = 1 ) ", params);
//        List<InvDailogueData> InvDailogueDataList = mapInvQotationToDTOList(invQotationList);
//        return InvDailogueDataList;
//    }
    private List<InvDailogueData> getInvQutationByBranchIdAndNotLoaded(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters) {

        filters.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT e FROM InvQotation e WHERE e.branchId.id = :branchId and  e.type = 1 and e.id not in (select p.invQotationId.id from InvPurchaseInvoice p where p.branchId.id = :branchId and p.invQotationId.id is not null and p.type = 1 ) ");
        appendFilterInvQutation(filters, builder);
        builder.append(" order by e.serial DESC");
        List<InvQotation> invQotationList = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        List<InvDailogueData> InvDailogueDataList = mapInvQotationToDTOList(invQotationList);
        return InvDailogueDataList;
    }

//    private List<InvDailogueData> getInvPurchaseInvoiceByBranchIdAndNotLoaded(Integer branchId, Integer type, ScreenNameClassEnum screenNameClassEnum) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("branchId", branchId);
//        params.put("type", type);
//        if (type == 2) {
//            params.put("Invtype", 0);
//        } else if (type == 3) {
//            params.put("Invtype", 1);
//        } else {
//            params.put("Invtype", type);
//        }
//        List<InvPurchaseInvoice> invPurchaseInvoiceList = null;
//        if (ScreenNameClassEnum.INVPERMISSIONORDERFORM.equals(screenNameClassEnum)
//                || ScreenNameClassEnum.INVRETURNPURCHASEFORM.equals(screenNameClassEnum)
//                || ScreenNameClassEnum.INVRETURNSALESFORM.equals(screenNameClassEnum)) {
//            invPurchaseInvoiceList = dao.findListByQuery("SELECT e FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId and  e.type = :Invtype and e.id not in (select p.parent.id from InvPurchaseInvoice p where p.branchId.id = :branchId AND p.type = :type and p.parent.id is not null) ", params);
//        } else {
//            invPurchaseInvoiceList = dao.findListByQuery("SELECT e FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId and  e.type = :Invtype and e.id not in (select i.invPurchaseInvoice.id from InvAddingorder i where i.branchId.id = :branchId AND i.type = :type and i.invPurchaseInvoice.id is not null) ", params);
//        }
//        List<InvDailogueData> InvDailogueDataList = mapInvPurchaseInvoiceToDTOList(invPurchaseInvoiceList);
//        return InvDailogueDataList;
//    }
    private List<InvDailogueData> getInvPurchaseInvoiceByBranchIdAndNotLoaded(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters, Integer type, ScreenNameClassEnum screenNameClassEnum) {
        StringBuilder builder = new StringBuilder();
        filters.put("branchId", branchId);
        filters.put("type", type);
        if (type == 2) {
            filters.put("Invtype", 0);
        } else if (type == 3) {
            filters.put("Invtype", 1);
        } else {
            filters.put("Invtype", type);
        }
        if (ScreenNameClassEnum.INVPERMISSIONORDERFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVRETURNPURCHASEFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVRETURNSALESFORM.equals(screenNameClassEnum)) {
            builder.append("SELECT e FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId and  e.type = :Invtype and e.id not in (select p.parent.id from InvPurchaseInvoice p where p.branchId.id = :branchId AND p.type = :type and p.parent.id is not null) ");
        } else if (ScreenNameClassEnum.INVSALESINVOICEFORM.equals(screenNameClassEnum)) {
            filters.put("type", 1);
            builder.append("SELECT e FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId and  e.type = :Invtype and e.id not in (select p.parent.id from InvPurchaseInvoice p where p.branchId.id = :branchId AND p.type = :type and p.parent.id is not null) ");
        } else {
            builder.append("SELECT e FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId and  e.type = :Invtype and e.id not in (select i.invPurchaseInvoice.id from InvAddingorder i where i.branchId.id = :branchId AND i.type = :type and i.invPurchaseInvoice.id is not null) ");
        }
        appendFilterInvPurchaseInvoice(filters, builder);
        builder.append(" order by e.serial DESC");
        List<InvPurchaseInvoice> invPurchaseInvoiceList = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        List<InvDailogueData> InvDailogueDataList = mapInvPurchaseInvoiceToDTOList(invPurchaseInvoiceList, screenNameClassEnum);
        return InvDailogueDataList;
    }

    @Override
    public Long getTotalRegistors(Integer branchId, Integer inventoryId, Map<String, Object> filters, Boolean sellBuy, ScreenNameClassEnum screenNameClassEnum) {
        if (screenNameClassEnum.equals(ScreenNameClassEnum.INVPURCHASEINVOICEFORM)) {
            if (sellBuy) {
                return getTotalRegistorsInvAddingOrder(branchId, filters);
            } else {
                return getTotalRegistorsPurchaseOrder(branchId, filters, screenNameClassEnum);
            }
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.INVSALESINVOICEFORM)) {
            return getTotalRegistorsInvQutation(branchId, filters);
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.INVRETURNPURCHASEFORM)) {
            return getTotalRegistorsInvPurchaseInvoice(branchId, 2, filters, screenNameClassEnum);
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.INVRETURNSALESFORM)) {
            return getTotalRegistorsInvPurchaseInvoice(branchId, 3, filters, screenNameClassEnum);
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.INVADDINGORDERFORM)) {
            return getTotalRegistorsPurchaseOrder(branchId, filters, screenNameClassEnum);
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.INVPERMISSIONORDERFORM)) {
            return getTotalRegistorsInvPurchaseInvoice(branchId, 1, filters, screenNameClassEnum);
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.INVUPDATE)) {
            return getTotalRegistorsInvStrip(branchId, inventoryId, filters);
        }

        return null;
    }

    private List<InvDailogueData> getInvStripByBranchIdAndNotLoaded(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters, ScreenNameClassEnum screenNameClassEnum, Integer inventoryId) {
        filters.put("branchId", branchId);
        filters.put("inventoryId", inventoryId);
        StringBuilder builder = new StringBuilder();
        if (ScreenNameClassEnum.INVUPDATE.equals(screenNameClassEnum)) {
            builder.append("SELECT e FROM InvStrip e WHERE e.branchId.id = :branchId AND e.inventoryId.id = :inventoryId ");
        }
        appendFilterInvStrip(filters, builder);
        builder.append(" order by e.serial DESC");
        List<InvStrip> invStripList = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        List<InvDailogueData> InvDailogueDataList = mapInvStripToDTOList(invStripList);
        return InvDailogueDataList;
    }

    private List<InvDailogueData> getPurchaseOrderByBranchIdAndNotLoaded(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters, ScreenNameClassEnum screenNameClassEnum) {
        filters.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        if (ScreenNameClassEnum.INVPURCHASEINVOICEFORM.equals(screenNameClassEnum) || ScreenNameClassEnum.INVADDINGORDERFORM.equals(screenNameClassEnum)) {
            builder.append("SELECT e FROM InvPurchaseOrder e WHERE e.branchId.id = :branchId and e.type = 0 and e.id not in (select p.invPurchaseOrderId.id from InvPurchaseInvoice p where p.branchId.id = :branchId AND p.invPurchaseOrderId.id is not null ) and e.id not in (select i.purchaseOrderId.id from InvAddingorder i where i.branchId.id = :branchId AND i.purchaseOrderId.id is not null ) ");
        } else {
            builder.append("SELECT e FROM InvPurchaseOrder e WHERE e.branchId.id = :branchId and e.type = 1 and e.id not in (select p.purchaseOrderId.id from InvAddingorder p where p.branchId.id = :branchId and  p.purchaseOrderId.id is not null )");
        }
        appendFilterPurchaseOrder(filters, builder);
        builder.append(" order by e.serial DESC");
        List<InvPurchaseOrder> invAddingOrdersList = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        List<InvDailogueData> InvDailogueDataList = mapInvPurchaseOrderToDTOList(invAddingOrdersList);
        return InvDailogueDataList;
    }

    private Long getTotalRegistorsPurchaseOrder(Integer branchId, Map<String, Object> filters, ScreenNameClassEnum screenNameClassEnum) {
        StringBuilder builder = new StringBuilder();
        filters.put("branchId", branchId);
        if (ScreenNameClassEnum.INVPURCHASEINVOICEFORM.equals(screenNameClassEnum) || ScreenNameClassEnum.INVADDINGORDERFORM.equals(screenNameClassEnum)) {
            builder.append("SELECT count(e) FROM InvPurchaseOrder e WHERE e.branchId.id = :branchId and e.type = 0 and e.id not in (select p.invPurchaseOrderId.id from InvPurchaseInvoice p where p.branchId.id = :branchId AND p.invPurchaseOrderId.id is not null ) and e.id not in (select i.purchaseOrderId.id from InvAddingorder i where i.branchId.id = :branchId AND i.purchaseOrderId.id is not null ) ");
        } else {
            builder.append("SELECT count(e) FROM InvPurchaseOrder e WHERE e.branchId.id = :branchId and e.type = 1 and e.id not in (select p.purchaseOrderId.id from InvAddingorder p where p.branchId.id = :branchId and  p.purchaseOrderId.id is not null )");
        }
        appendFilterPurchaseOrder(filters, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);
        return details == null ? 0 : details;
    }

    private void appendFilterPurchaseOrder(Map<String, Object> filters, StringBuilder builder) {
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and ( e.serial like CONCAT('%',:globalFilter ,'%') OR e.supplierId.name  like CONCAT('%',:globalFilter ,'%') OR e.remarks like CONCAT('%',:globalFilter ,'%') OR  FUNCTION('DATE_FORMAT',e.date, '%d-%m-%Y')  like CONCAT('%',:globalFilter ,'%') )";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }

    private Long getTotalRegistorsInvAddingOrder(Integer branchId, Map<String, Object> filters) {
        StringBuilder builder = new StringBuilder();
        filters.put("branchId", branchId);
        builder.append("SELECT count(e) FROM InvAddingorder e WHERE e.branchId.id = :branchId and  e.type = 0 and e.id not in (select p.invAddingOrderId.id from InvPurchaseInvoice p where p.branchId.id = :branchId and p.invAddingOrderId.id is not null )");
        appendFilterInvAddingOrder(filters, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);
        return details == null ? 0 : details;
    }

    private void appendFilterInvAddingOrder(Map<String, Object> filters, StringBuilder builder) {
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and (e.serial like CONCAT('%',:globalFilter ,'%')  OR e.organizationSiteId.name  like CONCAT('%',:globalFilter ,'%') OR e.remark like CONCAT('%',:globalFilter ,'%') OR  FUNCTION('DATE_FORMAT',e.date, '%d-%m-%Y')  like CONCAT('%',:globalFilter ,'%')) ";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }

    private Long getTotalRegistorsInvPurchaseInvoice(Integer branchId, Integer type, Map<String, Object> filters, ScreenNameClassEnum screenNameClassEnum) {
        StringBuilder builder = new StringBuilder();
        filters.put("branchId", branchId);
        filters.put("type", type);
        if (type == 2) {
            filters.put("Invtype", 0);
        } else if (type == 3) {
            filters.put("Invtype", 1);
        } else {
            filters.put("Invtype", type);
        }

        if (ScreenNameClassEnum.INVPERMISSIONORDERFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVRETURNPURCHASEFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVRETURNSALESFORM.equals(screenNameClassEnum)) {
            builder.append("SELECT count(e) FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId and  e.type = :Invtype and e.id not in (select p.parent.id from InvPurchaseInvoice p where p.branchId.id = :branchId AND p.type = :type and p.parent.id is not null) ");
        } else {
            builder.append("SELECT count(e) FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId and  e.type = :Invtype and e.id not in (select i.invPurchaseInvoice.id from InvAddingorder i where i.branchId.id = :branchId AND i.type = :type and i.invPurchaseInvoice.id is not null) ");
        }
        appendFilterInvPurchaseInvoice(filters, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);
        return details == null ? 0 : details;
    }

    private void appendFilterInvPurchaseInvoice(Map<String, Object> filters, StringBuilder builder) {
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and (e.serial like CONCAT('%',:globalFilter ,'%')  OR e.organizationSiteId.name  like CONCAT('%',:globalFilter ,'%') OR e.remarks like CONCAT('%',:globalFilter ,'%') OR FUNCTION('DATE_FORMAT',e.date, '%d-%m-%Y')  like CONCAT('%',:globalFilter ,'%') ) ";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }

    private Long getTotalRegistorsInvQutation(Integer branchId, Map<String, Object> filters) {
        StringBuilder builder = new StringBuilder();
        filters.put("branchId", branchId);
        builder.append("SELECT count(e) FROM InvQotation e WHERE e.branchId.id = :branchId and  e.type = 1 and e.id not in (select p.invQotationId.id from InvPurchaseInvoice p where p.branchId.id = :branchId and p.invQotationId.id is not null and p.type = 1 ) ");
        appendFilterInvQutation(filters, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);
        return details == null ? 0 : details;
    }

    private void appendFilterInvQutation(Map<String, Object> filters, StringBuilder builder) {
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and (e.serial like CONCAT('%',:globalFilter ,'%')  OR e.supplierId.name  like CONCAT('%',:globalFilter ,'%') OR e.remark like CONCAT('%',:globalFilter ,'%') OR FUNCTION('DATE_FORMAT',e.date, '%d-%m-%Y')  like CONCAT('%',:globalFilter ,'%') ) ";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }

    private Long getTotalRegistorsInvStrip(Integer branchId, Integer inventoryId, Map<String, Object> filters) {
        StringBuilder builder = new StringBuilder();
        filters.put("branchId", branchId);
        filters.put("inventoryId", inventoryId);
        builder.append("SELECT count(e) FROM InvStrip e WHERE e.branchId.id = :branchId AND e.inventoryId.id = :inventoryId ");
        appendFilterInvStrip(filters, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);
        return details == null ? 0 : details;
    }

    private void appendFilterInvStrip(Map<String, Object> filters, StringBuilder builder) {
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and (e.serial like CONCAT('%',:globalFilter ,'%')  OR e.stripDocument  like CONCAT('%',:globalFilter ,'%') OR e.remarks like CONCAT('%',:globalFilter ,'%') OR FUNCTION('DATE_FORMAT',e.stripDate, '%d-%m-%Y')  like CONCAT('%',:globalFilter ,'%') ) ";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }

    private List<InvDailogueData> mapInvStripToDTOList(List<InvStrip> entityList) {
        List<InvDailogueData> entityDTOList = new ArrayList<>();
        for (InvStrip entity : entityList) {
            entityDTOList.add(mapInvStripToDTO(entity));
        }
        return entityDTOList;
    }

    private List<InvDailogueData> mapInvPurchaseOrderToDTOList(List<InvPurchaseOrder> entityList) {
        List<InvDailogueData> entityDTOList = new ArrayList<>();
        for (InvPurchaseOrder entity : entityList) {
            entityDTOList.add(mapInvPurchaseOrderToDTO(entity));
        }
        return entityDTOList;
    }

    private InvDailogueData mapInvStripToDTO(InvStrip entity) {

        InvDailogueData dto = new InvDailogueData();

        dto.setScreenName(ScreenNameClassEnum.INVSTRIP);
        dto.setDate(entity.getStripDate());
        dto.setRemarks(entity.getRemarks());
        dto.setSupplierInvoiceNumber(entity.getStripDocument());

        dto.setId(entity.getId());
        dto.setSerial(entity.getSerial());
        dto.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
        dto.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dto.setCreatedDate(entity.getCreationDate());
        dto.setIndex(entity.getId());
        dto.setCompanyId(entity.getCompanyId().getId() != null ? entity.getCompanyId().getId() : null);
        dto.setCreatedByName(entity.getCreatedBy() != null ? entity.getCreatedBy().getName() : null);

        return dto;
    }

    private InvDailogueData mapInvPurchaseOrderToDTO(InvPurchaseOrder entity) {

        InvDailogueData dto = new InvDailogueData();

        dto.setScreenName(ScreenNameClassEnum.INVPURCHASEORDERFORM);
        dto.setDate(entity.getDate());
        dto.setRemarks(entity.getRemarks());
        if (entity.getSupplierId() != null) {
            dto.setOrganizationSiteName(entity.getSupplierId().getName());
        }

        dto.setId(entity.getId());
        dto.setSerial(entity.getSerial());
        dto.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
        dto.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dto.setCreatedDate(entity.getCreationDate());
        dto.setIndex(entity.getId());
        dto.setCompanyId(entity.getCompanyId().getId() != null ? entity.getCompanyId().getId() : null);
        dto.setCreatedByName(entity.getCreatedBy() != null ? entity.getCreatedBy().getName() : null);

        return dto;
    }

    private List<InvDailogueData> mapInvPurchaseInvoiceToDTOList(List<InvPurchaseInvoice> entityList, ScreenNameClassEnum screenNameClassEnum) {
        List<InvDailogueData> entityDTOList = new ArrayList<>();
        for (InvPurchaseInvoice entity : entityList) {
            entityDTOList.add(mapInvPurchaseInvoiceToDTO(entity,screenNameClassEnum));
        }
        return entityDTOList;
    }

    private List<InvDailogueData> mapInvAddingOrderToDTOList(List<InvAddingorder> entityList) {
        List<InvDailogueData> entityDTOList = new ArrayList<>();
        for (InvAddingorder entity : entityList) {
            entityDTOList.add(mapInvAddingOrderToDTO(entity));
        }
        return entityDTOList;
    }

    private List<InvDailogueData> mapInvQotationToDTOList(List<InvQotation> entityList) {
        List<InvDailogueData> entityDTOList = new ArrayList<>();
        for (InvQotation entity : entityList) {
            entityDTOList.add(mapInvQotationToDTO(entity));
        }
        return entityDTOList;
    }

    private InvDailogueData mapInvQotationToDTO(InvQotation entity) {

        InvDailogueData dto = new InvDailogueData();
        if (entity.getType() == 0) {
            dto.setScreenName(null);
        } else if (entity.getType() == 1) {
            dto.setScreenName(ScreenNameClassEnum.INVQUOTATIONFORM);
        }
        dto.setRemarks(entity.getRemark());
        if (entity.getSupplierId() != null) {
            dto.setOrganizationSiteName(entity.getSupplierId().getName());
        }

        dto.setId(entity.getId());
        dto.setSerial(entity.getSerial());
        dto.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
        dto.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dto.setCreatedDate(entity.getCreationDate());
        dto.setIndex(entity.getId());
        dto.setCompanyId(entity.getCompanyId().getId() != null ? entity.getCompanyId().getId() : null);
        dto.setCreatedByName(entity.getCreatedBy() != null ? entity.getCreatedBy().getName() : null);

        return dto;
    }

    private InvDailogueData mapInvAddingOrderToDTO(InvAddingorder entity) {

        InvDailogueData dto = new InvDailogueData();
        if (entity.getType() == 0) {
            dto.setScreenName(ScreenNameClassEnum.INVADDINGORDERFORM);
        } else if (entity.getType() == 1) {
            dto.setScreenName(ScreenNameClassEnum.INVPERMISSIONORDERFORM);
        }
        dto.setDate(entity.getDate());
        dto.setRemarks(entity.getRemark());
        if (entity.getOrganizationSiteId() != null) {
            dto.setOrganizationSiteName(entity.getOrganizationSiteId().getName());
        }
        dto.setSupplierInvoiceDate(entity.getSupplierDate());
        dto.setSupplierInvoiceNumber(entity.getSupplierInvoice());

        dto.setId(entity.getId());
        dto.setSerial(entity.getSerial());
        dto.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
        dto.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dto.setCreatedDate(entity.getCreationDate());
        dto.setIndex(entity.getId());
        dto.setCompanyId(entity.getCompanyId().getId() != null ? entity.getCompanyId().getId() : null);
        dto.setCreatedByName(entity.getCreatedBy() != null ? entity.getCreatedBy().getName() : null);

        return dto;
    }

    private InvDailogueData mapInvPurchaseInvoiceToDTO(InvPurchaseInvoice entity, ScreenNameClassEnum screenNameClassEnum) {

        InvDailogueData dto = new InvDailogueData();
        if (ScreenNameClassEnum.INVSALESINVOICEFORM.equals(screenNameClassEnum)) {
            dto.setScreenName(screenNameClassEnum);
        } else {
            if (entity.getType() == 0) {
                dto.setScreenName(ScreenNameClassEnum.INVADDINGORDERFORM);
            } else if (entity.getType() == 1) {
                dto.setScreenName(ScreenNameClassEnum.INVPERMISSIONORDERFORM);
            }
        }
        dto.setDate(entity.getDate());
        dto.setRemarks(entity.getRemarks());
        if (entity.getOrganizationSiteId() != null) {
            dto.setOrganizationSiteName(entity.getOrganizationSiteId().getName());
        }
        dto.setSupplierInvoiceDate(entity.getSupplierInvoiceDate());
        dto.setSupplierInvoiceNumber(entity.getSupplierInvoiceNumber());

        dto.setId(entity.getId());
        dto.setSerial(entity.getSerial());
        dto.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
        dto.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dto.setCreatedDate(entity.getCreationDate());
        dto.setIndex(entity.getId());
        dto.setCompanyId(entity.getCompanyId().getId() != null ? entity.getCompanyId().getId() : null);
        dto.setCreatedByName(entity.getCreatedBy() != null ? entity.getCreatedBy().getName() : null);
        return dto;
    }

    @Override
    public InvInvoiceDTO loadDlg(InvInvoiceDTO dTO) {
        ScreenNameClassEnum screenNameClassEnum = dTO.getScreenName();
        if (screenNameClassEnum.equals(ScreenNameClassEnum.INVPURCHASEINVOICEFORM)) {
            if (dTO.getInventorySetupDTO().getSellBuy()) {
                dTO = loadInvAddingOrder(dTO);
            } else {
                dTO = loadInvPurchaseOrder(dTO);
            }
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.INVRETURNPURCHASEFORM)) {
            dTO = loadInvPurchaseInvoice(dTO, 0);
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.INVRETURNSALESFORM)) {
            dTO = loadInvPurchaseInvoice(dTO, 1);
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.INVADDINGORDERFORM)) {
            dTO = loadInvPurchaseOrder(dTO);
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.INVPERMISSIONORDERFORM)) {
            dTO = loadInvPurchaseInvoice(dTO, 1);
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.INVSALESINVOICEFORM)) {
            dTO = loadInvQotation(dTO, 1);
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.INVUPDATE)) {
            dTO = loadInvStrip(dTO);
        }
        return dTO;
    }

    public InvInvoiceDTO loadInvQotation(InvInvoiceDTO dTO, Integer type) {
        dTO = invQuotationService.convertQotationToPurchaseInvoice(dTO, type);
        dTO = invGlobalService.updateSummition(dTO);
        return dTO;
    }

    public InvInvoiceDTO loadInvStrip(InvInvoiceDTO dTO) {
        dTO = invStripService.convertInvStripListToInvInvoice(dTO);
        dTO = invGlobalService.updateSummition(dTO);
        return dTO;
    }

    public InvInvoiceDTO loadInvPurchaseOrder(InvInvoiceDTO dTO) {
        dTO = invPurchaseOrderService.convertPurchaseOrderToPurchaseInvoice(dTO);
        dTO = invGlobalService.updateSummition(dTO);
        return dTO;
    }

    public InvInvoiceDTO loadInvAddingOrder(InvInvoiceDTO dTO) {
        dTO = invAddingOrderService.convertAddingOrderToPurchaseInvoice(dTO);
        dTO = invGlobalService.updateSummition(dTO);
        return dTO;
    }

    public InvInvoiceDTO loadInvPurchaseInvoice(InvInvoiceDTO dTO, Integer type) {
        dTO = invPurchaseInvoiceService.convertInvPurchaseInvoiceToPurchaseInvoice(dTO, type);
        dTO = invGlobalService.updateSummition(dTO);
        return dTO;
    }

}
