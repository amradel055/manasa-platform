/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.InvDailogueData;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.InventorySetupDTO;
import com.toby.entity.InvAddingorder;
import com.toby.entity.InvPurchaseInvoice;
import com.toby.entity.InvPurchaseOrder;
import com.toby.entity.InvQotation;
import com.toby.entity.ProFactoryDelivery;
import java.util.ArrayList;
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
    private InvAddingOrderService invAddingOrderService;
    @EJB
    private InvPurchaseInvoiceService_1 invPurchaseInvoiceService;
    @EJB
    private InvGlobalService invGlobalService;
    @EJB
    private ProFactoryDeliveryService proFactoryDeliveryService;

    @Override
    public List<InvDailogueData> loadInvDailogueData(int first, int pageSize, String sortField, Boolean asc, Map<String, Object> filters, InventorySetupDTO inventorySetupDTO, Integer branchId, ScreenNameClassEnum screenNameClassEnum) {
        List<InvDailogueData> dailogueDataList = new ArrayList<>();
        if (screenNameClassEnum.equals(ScreenNameClassEnum.INVPURCHASEINVOICEFORM)) {
            if (inventorySetupDTO.getSellBuy()) {
//                dailogueDataList = getInvAddingOrderByBranchIdAndNotLoaded(branchId);
                dailogueDataList = getInvAddingOrderByBranchIdAndNotLoaded(first, pageSize, sortField, asc, branchId, filters);
                return dailogueDataList;
            } else {
                //  dailogueDataList = getPurchaseOrderByBranchIdAndNotLoaded(branchId, screenNameClassEnum);
                dailogueDataList = getPurchaseOrderByBranchIdAndNotLoaded(first, pageSize, sortField, asc, branchId, filters, screenNameClassEnum);
                return dailogueDataList;
            }
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.INVSALESINVOICEFORM)) {
//            dailogueDataList = getInvQutationByBranchIdAndNotLoaded(branchId);
//            dailogueDataList = getInvQutationByBranchIdAndNotLoaded(first, pageSize, sortField, asc, branchId, filters);
            dailogueDataList = getInvPurchaseInvoiceByBranchIdAndNotLoaded(first, pageSize, sortField, asc, branchId, filters, 1, screenNameClassEnum);
            return dailogueDataList;
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.PROWORKORDER)) {
//            dailogueDataList = getInvQutationByBranchIdAndNotLoaded(branchId);
            dailogueDataList = getInvQutationByBranchIdAndNotLoaded(first, pageSize, sortField, asc, branchId, filters);

            return dailogueDataList;
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.INVRETURNPURCHASEFORM)) {
//            dailogueDataList = getInvPurchaseInvoiceByBranchIdAndNotLoaded(branchId, 2, screenNameClassEnum);
            dailogueDataList = getInvPurchaseInvoiceByBranchIdAndNotLoaded(first, pageSize, sortField, asc, branchId, filters, 2, screenNameClassEnum);
            return dailogueDataList;
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.INVRETURNSALESFORM)) {
            dailogueDataList = getInvPurchaseInvoiceByBranchIdAndNotLoaded(first, pageSize, sortField, asc, branchId, filters, 3, screenNameClassEnum);
            return dailogueDataList;
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.INVADDINGORDERFORM)) {
            //  dailogueDataList = getPurchaseOrderByBranchIdAndNotLoaded(branchId, screenNameClassEnum);
            dailogueDataList = getPurchaseOrderByBranchIdAndNotLoaded(first, pageSize, sortField, asc, branchId, filters, screenNameClassEnum);
            return dailogueDataList;
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.INVPERMISSIONORDERFORM)) {
            //  dailogueDataList = getInvPurchaseInvoiceByBranchIdAndNotLoaded(branchId, 1, screenNameClassEnum);
            dailogueDataList = getInvPurchaseInvoiceByBranchIdAndNotLoaded(first, pageSize, sortField, asc, branchId, filters, 1, screenNameClassEnum);
            return dailogueDataList;
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.PROFACTORYDELIVERY)) {
            //  dailogueDataList = getInvPurchaseInvoiceByBranchIdAndNotLoaded(branchId, 1, screenNameClassEnum);
            dailogueDataList = getInvPurchaseInvoiceByBranchIdAndNotLoaded(first, pageSize, sortField, asc, branchId, filters, 4, screenNameClassEnum);
            return dailogueDataList;
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.PROSENDTOGALLARY)) {
            dailogueDataList = getProFactoryDeliveryByBranchIdAndNotLoaded(first, pageSize, sortField, asc, branchId, filters, 1, screenNameClassEnum);
            return dailogueDataList;
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.PRORECIEVEGALLARY)) {
            dailogueDataList = getProFactoryDeliveryByBranchIdAndNotLoaded(first, pageSize, sortField, asc, branchId, filters, 2, screenNameClassEnum);
            return dailogueDataList;
        }

        return dailogueDataList;
    }

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

    private List<InvDailogueData> getProFactoryDeliveryByBranchIdAndNotLoaded(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters, Integer type, ScreenNameClassEnum screenNameClassEnum) {
        StringBuilder builder = new StringBuilder();

        filters.put("branchId", branchId);
        filters.put("type", type);
        if (type == 1) {
            filters.put("Invtype", 0);
        } else if (type == 2) {
            filters.put("Invtype", 1);
        }
        if (ScreenNameClassEnum.PROSENDTOGALLARY.equals(screenNameClassEnum) || ScreenNameClassEnum.PRORECIEVEGALLARY.equals(screenNameClassEnum)) {
            builder.append("SELECT e FROM ProFactoryDelivery e WHERE e.branchId.id = :branchId and  e.type = :Invtype and e.id not in (select p.parent.id from ProFactoryDelivery p where p.branchId.id = :branchId AND p.type = :type and p.parent.id is not null)  ");
            if (screenNameClassEnum.PROSENDTOGALLARY.equals(screenNameClassEnum)) {
                builder.append(" AND e.id in (select pro.proFactoryDelivery.id from ProProductionTransaction pro where pro.branchId.id = :branchId ) ");
            } else if (screenNameClassEnum.PRORECIEVEGALLARY.equals(screenNameClassEnum)) {
                builder.append(" AND e.id in (select pro.id from ProFactoryDelivery pro where pro.branchId.id = :branchId AND pro.type = :Invtype ) ");
            }
            appendFilterProFactoryDelivery(filters, builder);
            builder.append(" order by e.serial DESC");
            List<ProFactoryDelivery> factoryDeliveryList = dao.findListByQuery(builder.toString(), filters, first, pageSize);
            List<InvDailogueData> InvDailogueDataList = mapProFactoryDeliveryToDTOList(factoryDeliveryList);
            return InvDailogueDataList;
        } else if (ScreenNameClassEnum.PROFACTORYDELIVERY.equals(screenNameClassEnum)) {
            builder.append("SELECT e FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId and  e.type = :type    and e.id not in (select i.invPurchaseInvoiceId.id from ProFactoryDelivery i where i.branchId.id = :branchId  and i.invPurchaseInvoiceId.id is not null) ");
            appendFilterInvPurchaseInvoice(filters, builder);
            builder.append(" order by e.serial DESC");
            List<InvPurchaseInvoice> invPurchaseInvoiceList = dao.findListByQuery(builder.toString(), filters, first, pageSize);
            List<InvDailogueData> InvDailogueDataList = mapInvPurchaseInvoiceToDTOList(invPurchaseInvoiceList);
            return InvDailogueDataList;
        }
        return null;
    }

    private Long getTotalRegistorsInvProFactoryDelivery(Integer branchId, Integer type, Map<String, Object> filters, ScreenNameClassEnum screenNameClassEnum) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT count(e) FROM ProFactoryDelivery e WHERE e.branchId.id = :branchId and  e.type = :Invtype and e.id not in (select p.parent.id from ProFactoryDelivery p where p.branchId.id = :branchId AND p.type = :type and p.parent.id is not null) ");
        if (screenNameClassEnum.PROSENDTOGALLARY.equals(screenNameClassEnum)) {
            builder.append(" AND e.id in (select pro.proFactoryDelivery.id from ProProductionTransaction pro where pro.branchId.id = :branchId ) ");
        } else if (screenNameClassEnum.PRORECIEVEGALLARY.equals(screenNameClassEnum)) {
            builder.append(" AND e.id in (select pro.id from ProFactoryDelivery pro where pro.branchId.id = :branchId AND pro.type = :Invtype ) ");
        }

        filters.put("branchId", branchId);
        filters.put("type", type);
        if (type == 1) {
            filters.put("Invtype", 0);
        } else if (type == 2) {
            filters.put("Invtype", 1);
        }

        appendFilterProFactoryDelivery(filters, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);
        return details == null ? 0 : details;
    }

    private void appendFilterProFactoryDelivery(Map<String, Object> filters, StringBuilder builder) {
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and (e.serial like CONCAT('%',:globalFilter ,'%')  OR e.invPurchaseInvoiceId.serial  like CONCAT('%',:globalFilter ,'%') OR e.remark like CONCAT('%',:globalFilter ,'%') OR FUNCTION('DATE_FORMAT',e.date, '%d-%m-%Y')  like CONCAT('%',:globalFilter ,'%') OR FUNCTION('DATE_FORMAT',e.time, '%d-%m-%Y')  like CONCAT('%',:globalFilter ,'%') ) ";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }

    private List<InvDailogueData> getInvPurchaseInvoiceByBranchIdAndNotLoaded(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters, Integer type, ScreenNameClassEnum screenNameClassEnum) {
        StringBuilder builder = new StringBuilder();
        filters.put("branchId", branchId);
        filters.put("type", type);
        if (type == 2) {
            filters.put("Invtype", 0);
        } else if (type == 3) {
            filters.put("Invtype", 1);
        } else if (type == 1) {
            filters.put("Invtype", 4);
        } else {
            filters.put("Invtype", type);
        }
        List<InvPurchaseInvoice> invPurchaseInvoiceList = new ArrayList<>();
        List<ProFactoryDelivery> proFactoryDeliveryList = new ArrayList<>();
        if (ScreenNameClassEnum.INVPERMISSIONORDERFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVRETURNPURCHASEFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVRETURNSALESFORM.equals(screenNameClassEnum)) {
            builder.append("SELECT e FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId and  e.type = :Invtype and e.id not in (select p.parent.id from InvPurchaseInvoice p where p.branchId.id = :branchId AND p.type = :type and p.parent.id is not null) ");
            appendFilterInvPurchaseInvoice(filters, builder);
            builder.append(" order by e.serial DESC");
            invPurchaseInvoiceList = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        } else if (ScreenNameClassEnum.PROFACTORYDELIVERY.equals(screenNameClassEnum)) {
            builder.append("SELECT e FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId and  e.type = :type AND ( e.stopped is null OR e.stopped = 0)   and e.id not in (select i.invPurchaseInvoiceId.id from ProFactoryDelivery i where i.branchId.id = :branchId  and i.invPurchaseInvoiceId.id is not null) ");
            appendFilterInvPurchaseInvoice(filters, builder);
            builder.append(" order by e.serial DESC");
            filters.remove("Invtype");
            invPurchaseInvoiceList = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        } else if (ScreenNameClassEnum.INVSALESINVOICEFORM.equals(screenNameClassEnum)) {
            builder.append("select i from ProFactoryDelivery i where i.branchId.id = :branchId AND i.type = 2 AND i.id not in (select e.proFactoryDeliveryId.id from InvPurchaseInvoice e where e.type = 1  ");
//            builder.append("SELECT e FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId and  e.type = :Invtype and e.id not in (select i.invPurchaseInvoice.id from ProFactoryDelivery i where  i.branchId.id = :branchId AND i.type = 2 and i.invPurchaseInvoiceId.id is not null and i.id not in (select ip.ProFactoryDelivery.id from InvPurchaseInvoice ip where ip.type = 1 )) ");
            appendFilterInvPurchaseInvoice(filters, builder);
            builder.append(")");
            builder.append(" order by i.serial DESC");
            filters.remove("Invtype");
            filters.remove("type");
            proFactoryDeliveryList = dao.findListByQuery(builder.toString(), filters, first, pageSize);
            for (ProFactoryDelivery factoryDelivery : proFactoryDeliveryList) {
                if (factoryDelivery.getInvPurchaseInvoiceId() != null) {
                    factoryDelivery.getInvPurchaseInvoiceId().setProFactoryDeliveryId(factoryDelivery);
                    invPurchaseInvoiceList.add(factoryDelivery.getInvPurchaseInvoiceId());
                }
            }
        } else {
            builder.append("SELECT e FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId and  e.type = :Invtype and e.id not in (select i.invPurchaseInvoice.id from InvAddingorder i where i.branchId.id = :branchId AND i.type = :type and i.invPurchaseInvoice.id is not null) ");
            appendFilterInvPurchaseInvoice(filters, builder);
            builder.append(" order by e.serial DESC");
            invPurchaseInvoiceList = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        }

        List<InvDailogueData> InvDailogueDataList = mapInvPurchaseInvoiceToDTOList(invPurchaseInvoiceList);
        return InvDailogueDataList;
    }

    @Override
    public Long getTotalRegistors(Integer branchId, Map<String, Object> filters, Boolean sellBuy, ScreenNameClassEnum screenNameClassEnum) {
        if (screenNameClassEnum.equals(ScreenNameClassEnum.INVPURCHASEINVOICEFORM)) {
            if (sellBuy) {
                return getTotalRegistorsInvAddingOrder(branchId, filters);
            } else {
                return getTotalRegistorsPurchaseOrder(branchId, filters, screenNameClassEnum);
            }
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.INVSALESINVOICEFORM)) {
//            return getTotalRegistorsInvQutation(branchId, filters);
            return getTotalRegistorsInvPurchaseInvoice(branchId, 1, filters, screenNameClassEnum);
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.PROWORKORDER)) {
            return getTotalRegistorsInvQutation(branchId, filters);
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.INVRETURNPURCHASEFORM)) {
            return getTotalRegistorsInvPurchaseInvoice(branchId, 2, filters, screenNameClassEnum);
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.INVRETURNSALESFORM)) {
            return getTotalRegistorsInvPurchaseInvoice(branchId, 3, filters, screenNameClassEnum);
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.INVADDINGORDERFORM)) {
            return getTotalRegistorsPurchaseOrder(branchId, filters, screenNameClassEnum);
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.INVPERMISSIONORDERFORM)) {
            return getTotalRegistorsInvPurchaseInvoice(branchId, 1, filters, screenNameClassEnum);
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.PROFACTORYDELIVERY)) {
            return getTotalRegistorsInvPurchaseInvoice(branchId, 4, filters, screenNameClassEnum);
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.PROSENDTOGALLARY)) {
            return getTotalRegistorsInvProFactoryDelivery(branchId, 1, filters, screenNameClassEnum);
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.PRORECIEVEGALLARY)) {
            return getTotalRegistorsInvProFactoryDelivery(branchId, 2, filters, screenNameClassEnum);
        }

        return null;
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
        } else if (type == 1) {
            filters.put("Invtype", 4);
        } else {
            filters.put("Invtype", type);
        }

        if (ScreenNameClassEnum.INVPERMISSIONORDERFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVRETURNPURCHASEFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVRETURNSALESFORM.equals(screenNameClassEnum)) {
            builder.append("SELECT count(e) FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId and  e.type = :Invtype and e.id not in (select p.parent.id from InvPurchaseInvoice p where p.branchId.id = :branchId AND p.type = :type and p.parent.id is not null) ");
        } else if (ScreenNameClassEnum.PROFACTORYDELIVERY.equals(screenNameClassEnum)) {
            builder.append("SELECT count(e) FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId and  e.type = :type and e.id not in (select i.invPurchaseInvoiceId.id from ProFactoryDelivery i where i.branchId.id = :branchId  and i.invPurchaseInvoiceId.id is not null) ");
            filters.remove("Invtype");
        } else if (ScreenNameClassEnum.INVSALESINVOICEFORM.equals(screenNameClassEnum)) {
            builder.append("SELECT count(e) FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId and  e.type = :Invtype and e.id in (select i.invPurchaseInvoiceId.id from ProFactoryDelivery i where  i.branchId.id = :branchId AND i.type = 2 and i.invPurchaseInvoiceId.id is not null and i.id not in (select ip.proFactoryDeliveryId.id from InvPurchaseInvoice ip where ip.type = :type )) ");
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

    private List<InvDailogueData> mapInvPurchaseOrderToDTOList(List<InvPurchaseOrder> entityList) {
        List<InvDailogueData> entityDTOList = new ArrayList<>();
        for (InvPurchaseOrder entity : entityList) {
            entityDTOList.add(mapInvPurchaseOrderToDTO(entity));
        }
        return entityDTOList;
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

    private List<InvDailogueData> mapInvPurchaseInvoiceToDTOList(List<InvPurchaseInvoice> entityList) {
        List<InvDailogueData> entityDTOList = new ArrayList<>();
        for (InvPurchaseInvoice entity : entityList) {
            entityDTOList.add(mapInvPurchaseInvoiceToDTO(entity));
        }
        return entityDTOList;
    }

    private List<InvDailogueData> mapProFactoryDeliveryToDTOList(List<ProFactoryDelivery> entityList) {
        List<InvDailogueData> entityDTOList = new ArrayList<>();
        for (ProFactoryDelivery entity : entityList) {
            entityDTOList.add(mapProFactoryDeliveryToDTO(entity));
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

    private InvDailogueData mapInvPurchaseInvoiceToDTO(InvPurchaseInvoice entity) {

        InvDailogueData dto = new InvDailogueData();

        if (entity.getType() == 0) {
            dto.setScreenName(ScreenNameClassEnum.INVADDINGORDERFORM);
        } else if (entity.getType() == 1) {
//            dto.setScreenName(ScreenNameClassEnum.INVPERMISSIONORDERFORM);
            dto.setScreenName(ScreenNameClassEnum.PRORECIEVEGALLARY);
        }
        dto.setDate(entity.getDate());
        dto.setRemarks(entity.getRemarks());
        if (entity.getOrganizationSiteId() != null) {
            dto.setOrganizationSiteName(entity.getOrganizationSiteId().getName());
        }

        if (entity.getProFactoryDeliveryId() != null && entity.getProFactoryDeliveryId().getId() != null) {
            dto.setProFactoryDeliveryId(entity.getProFactoryDeliveryId().getId());
        }
        dto.setSupplierInvoiceDate(entity.getSupplierInvoiceDate());
        dto.setSupplierInvoiceNumber(entity.getSupplierInvoiceNumber());

        dto.setId(entity.getId());
        dto.setSerial(entity.getSerial());
        dto.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
        dto.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dto.setCreatedDate(entity.getCreationDate());
        dto.setIndex(entity.getId());
        dto.setInventoryName(entity.getGalaryId().getName());
        dto.setCompanyId(entity.getCompanyId().getId() != null ? entity.getCompanyId().getId() : null);
        dto.setCreatedByName(entity.getCreatedBy() != null ? entity.getCreatedBy().getName() : null);
        return dto;
    }

    private InvDailogueData mapProFactoryDeliveryToDTO(ProFactoryDelivery entity) {

        InvDailogueData dto = new InvDailogueData();

        if (entity.getType() == 0) {
            dto.setScreenName(ScreenNameClassEnum.INVADDINGORDERFORM);
        } else if (entity.getType() == 1) {
            dto.setScreenName(ScreenNameClassEnum.INVPERMISSIONORDERFORM);
        }
        dto.setDate(entity.getDate());
        dto.setRemarks(entity.getRemark());

        dto.setId(entity.getId());
        dto.setSerial(entity.getInvPurchaseInvoiceId().getSerial());
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
            dTO = loadInvPurchaseInvoice(dTO, 1);
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.PROWORKORDER)) {
            dTO = loadInvQotation(dTO, 1);
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.PROFACTORYDELIVERY)) {
            dTO = loadProFactoryDelivery(dTO, 4);
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.PROSENDTOGALLARY)) {
            dTO = loadProSendToGallary(dTO, 0);
        } else if (screenNameClassEnum.equals(ScreenNameClassEnum.PRORECIEVEGALLARY)) {
            dTO = loadProSendToGallary(dTO, 1);
        }else if (screenNameClassEnum.equals(ScreenNameClassEnum.PROSALES)) {
            dTO = loadProSendToGallary(dTO, 2);
        }
        return dTO;
    }

    public InvInvoiceDTO loadInvQotation(InvInvoiceDTO dTO, Integer type) {
        dTO = invQuotationService.convertQotationToPurchaseInvoice(dTO, type);
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

    public InvInvoiceDTO loadProFactoryDelivery(InvInvoiceDTO dTO, Integer type) {
        dTO = invPurchaseInvoiceService.convertInvPurchaseInvoiceToPurchaseInvoice(dTO, type);
        return dTO;
    }

    public InvInvoiceDTO loadProSendToGallary(InvInvoiceDTO dTO, Integer type) {
        dTO = proFactoryDeliveryService.convertProFactoryDeliveryToInvInvoice(dTO, type);
        return dTO;
    }

}
