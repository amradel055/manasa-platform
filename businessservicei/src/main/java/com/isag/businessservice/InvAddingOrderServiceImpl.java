/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.bussinessservice.global.InvPurchaseReturnSave;
import com.isag.core.GenericDAO;
import com.isag.define.ScreenNameClassEnum;
import com.isag.dto.CurrencyDTO;
import com.isag.dto.InvDetailDTO;
import com.isag.dto.InvInventoryDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.dto.InventoryDelegatorDTO;
import com.isag.entity.Branch;
import com.isag.entity.InvAddingorder;
import com.isag.entity.InvAddingorderDetail;
import com.isag.entity.InvInventory;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.InvPurchaseInvoice;
import com.isag.entity.InvPurchaseInvoiceDetail;
import com.isag.entity.InvPurchaseOrder;
import com.isag.entity.InventoryDelegator;
import com.isag.entity.InventorySetup;
import com.isag.entity.IsagCompany;
import com.isag.entity.IsagUser;
import com.isag.views.QuantityItemsStore;
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
 * @author WIN7
 */
@Stateless
public class InvAddingOrderServiceImpl implements InvAddingOrderService {

    @EJB
    private GenericDAO dao;
    @EJB
    private GenericService genericService;
    @EJB
    private InvPurchaseInvoiceService invPurchaseInvoiceService;
    @EJB
    private InvPurchaseInvoiceDetailsService invPurchaseInvoiceDetailsService;
    @EJB
    private InvPurchaseOrderDetailsService invPurchaseOrderDetailsService;
    @EJB
    private InvQuotationDetailsService invQuotationDetailsService;
    @EJB
    private InvReservationDetailService invReservationDetailService;
    @EJB
    private InvPurchaseOrderService invPurchaseOrderService;
    @EJB
    private InvAddingOrderDetailsService invAddingOrderDetailsService;
    @EJB
    private InvItemService invItemService;
    @EJB
    private QuantityItemsStoreService itemsStoreService;
    @EJB
    private InventorySetupService inventorySetupService;
    @EJB
    public InvGlobalService invGlobalService;

    InvPurchaseReturnSave invPurchaseReturnSave;

    @Override
    public InvPurchaseReturnSave addInvAddingOrder(InvAddingorder invAddingOrder,
            List<InvAddingorderDetail> invAddingOrderDetailList,
            List<InvAddingorderDetail> invAddingOrderDetailListDeleted,
            List<Integer> updatedPurchaseOrAddingOrderList,
            StringBuilder headIdList, Boolean isPurchaseOrSales) {
        invPurchaseReturnSave = new InvPurchaseReturnSave();
        Integer serial;
        if (invAddingOrder.getId() != null) {
            dao.updateEntity(invAddingOrder);
        } else {

            serial = genericService.getMaxSerialForInvoice(InvAddingorder.class, invAddingOrder.getBranchId().getId(), invAddingOrder.getInvInventoryId().getId(), invAddingOrder.getType());
            invAddingOrder.setSerial(serial);

            dao.saveEntity(invAddingOrder);
        }

        //--------------------
        // لو انا محمل الفاتورة من مرحلة سابقة بعمل هنا تعديل للكميات وحالة كل فاتورة من المحمل منها
        String msg = updateFinalQuantityAndStatusDetailAndSaveDetail(invAddingOrderDetailList, isPurchaseOrSales, invAddingOrder);

        if (msg == null) {
            // لو الدنيا اللى فاتت تمام نروح نمسح من قاعدة البيانات اللى اتمسح من الفاتورة
            deleteDetails(invAddingOrderDetailListDeleted, isPurchaseOrSales);
            // هنا بروح اعدل حالة الفاتورة علشان وانا جاي استدعي الفواتير اكون عارف حالتها
            if (updatedPurchaseOrAddingOrderList != null && !updatedPurchaseOrAddingOrderList.isEmpty()) {
                updateHeadStatus(updatedPurchaseOrAddingOrderList);
            } else {
                if (headIdList != null && !"".equals(headIdList.toString())) {
                    updateHeadStatus(findHeadSelectedId(headIdList));
                }
            }
            invPurchaseReturnSave.setInvAddingorder(invAddingOrder);
            invPurchaseReturnSave.setInvAddingorderDetailList(invAddingOrderDetailsService.getAllInvAddingOrderDetailsByInvAddingOrderId(invAddingOrder.getId()));
        } else {
            invAddingOrder.setMsg(msg);
            invPurchaseReturnSave.setMsgOrder(msg);
            dao.rollbackQuery();
        }

        return invPurchaseReturnSave;

    }

    //-----------------------
    public List<Integer> findHeadSelectedId(StringBuilder headIdList) {
        String query;

        query = "select DISTINCT h.id FROM InvPurchaseInvoice h Left join InvPurchaseInvoiceDetail d WHERE h.id = d.invPurchaseInvoiceId.id and d.id in (" + headIdList.toString() + ") ";
        return dao.findListByQuery(query);

    }

//---------------------------
    public synchronized String updateFinalQuantityAndStatusDetailAndSaveDetail(List<InvAddingorderDetail> invAddingorderDetailsUpdatedList,
            Boolean isPurchaseOrSales, InvAddingorder invAddingorder) {
        InventorySetup invSetup = inventorySetupService.getInventoryByBranchId(invAddingorder.getBranchId().getId());

        Integer selectedId;
        QuantityItemsStore quantityItemsStore;
        // بحدد اذا انا كنت فى فاتورة المشتريات
        if (!isPurchaseOrSales) {
            // بعمل لوب على تفاصيل الفاتورة
            for (InvAddingorderDetail invAddingorderDetail : invAddingorderDetailsUpdatedList) {
                // هنا بقي بعدل الكميات وحالة الفواتير من الشاشات المحمل منها الفاتورةر و بحفظ التفاصيل
                mergeDetailQuantities(invAddingorderDetail, isPurchaseOrSales, invAddingorder);
            }
        } else {
            // اذا كنت فى فاتورة مبيعات فالامر بيزيد تعقيد
            // لانه بيشوف هل مسموحله يبيع بكميات سالبة ولا لاء
            for (InvAddingorderDetail invAddingorderDetail : invAddingorderDetailsUpdatedList) {
                quantityItemsStore = itemsStoreService.findInventoryItem(invAddingorder.getInvInventoryId().getId(),
                        invAddingorder.getBranchId().getId(), invAddingorderDetail.getItemId().getId(), invSetup.getSellBuy());
                // لو مسموحلة يبيع بكميات سالبة بيخش يشوف شغلة وينفض للباقى
                if (invSetup.getNegativeSell()) {
                    mergeDetailQuantities(invAddingorderDetail, isPurchaseOrSales, invAddingorder);
                } else {
                    quantityItemsStore = findQuantityItemNow(invAddingorder, invAddingorderDetail, invSetup);

                    if (quantityItemsStore != null && quantityItemsStore.getQty() != null
                            && quantityItemsStore.getQty().compareTo(invAddingorderDetail.getQuantity()) != -1 && !invSetup.getNegativeSell()) {
                        // بيدخل هنا فى حالة لو مش مسموحلة يبيع بكميات سالبة بس بشرط يكون الكمية اكبر من الصفر 
                        if (invAddingorderDetail.getId() != null) {
                            InvAddingorderDetail oldvalue = invAddingOrderDetailsService.findInvAddingOrderDetailsById(invAddingorderDetail.getId());
                            quantityItemsStore.setQty(quantityItemsStore.getQty().add(oldvalue.getQuantity()));
                        }
                        mergeDetailQuantities(invAddingorderDetail, isPurchaseOrSales, invAddingorder);
                    } else {
//                    dao.rollbackQuery();
                        return "Quantity Not Avialable";
                    }
                }

            }
        }

        return null;
    }

    private QuantityItemsStore findQuantityItemNow(InvAddingorder invAddingorder, InvAddingorderDetail addingorderDetail, InventorySetup invSetup) {
        QuantityItemsStore quantityItemsStore;
        quantityItemsStore = itemsStoreService.findInventoryItem(invAddingorder.getInvInventoryId().getId(),
                invAddingorder.getBranchId().getId(), addingorderDetail.getItemId().getId(), invSetup.getSellBuy());
        return quantityItemsStore;
    }

    //---------------------
    public void mergeDetailQuantities(InvAddingorderDetail invAddingorderDetail, Boolean isPurchaseOrSales, InvAddingorder invAddingorder) {
        // انا بجيب هنا رقم التفصيلى اللى فى الفاتورة اللى محمل منها وبردوا بحدد منه اذا كان التفصيلى دا متحمل من فاتورة ولا لاء   

        if (isPurchaseOrSales) {
            InvPurchaseInvoiceDetail selectedId = invAddingorderDetail.getSelectedId();
            if (selectedId != null) {
                Integer transferFrom = invAddingorderDetail.getTransferFrom();
                // 0 -> محمل من فاتورة امر شراء
                if (0 == transferFrom) {
                    updateSalesInvoiceStatusAndQuantity(invAddingorderDetail);
                }
            }
            // فى الاخر بحفظ التفصيلى
            saveDetail(invAddingorderDetail, invAddingorder, isPurchaseOrSales);
        } else {
            InvPurchaseInvoiceDetail selectedPurchaseId = invAddingorderDetail.getSelectedPurchaseId();
            if (selectedPurchaseId != null) {
                Integer transferFrom = invAddingorderDetail.getTransferFrom();
                // 0 -> محمل من فاتورة امر شراء
                if (0 == transferFrom) {
                    updateSalesInvoiceStatusAndQuantity(invAddingorderDetail);
                }
            }
            // فى الاخر بحفظ التفصيلى
            saveDetail(invAddingorderDetail, invAddingorder, isPurchaseOrSales);
        }

    }
    // هنا بقى لو كان التفصيلى دا متحمل من فاتورة تانية بخش اعمل تعديل فى الكميات والحالة بتاعته فى فاتورتة الاساسية

    //----------------------
    public void saveDetail(InvAddingorderDetail invAddingorderDetail, InvAddingorder invAddingorder, Boolean isPurchaseOrSales) {
        invAddingorderDetail.setAddingorderId(invAddingorder);
        if (invAddingorderDetail.getId() != null) {
            invAddingOrderDetailsService.updateInvAddingOrderDetails(invAddingorderDetail);
        } else {
            invAddingorderDetail.setSerial(invAddingOrderDetailsService.getMaxSerialForInvoiceDetail(invAddingorderDetail.getAddingorderId().getId()));
            invAddingOrderDetailsService.addInvAddingOrderDetails(invAddingorderDetail);
        }
    }

    //----------------------      
    public void updateSalesInvoiceStatusAndQuantity(InvAddingorderDetail invAddingorderDetail) {
        InvPurchaseInvoiceDetail purchaseInvoiceDetail;

        BigDecimal purchaseInvoiceFinalQuantity;
        BigDecimal finalQuantity;
        Integer res;
        BigDecimal quantityInvoiceOld = null;
        // بجيب السطر اللى هشتغل عليه من جدول امر الشراء
//        purchaseInvoiceDetail = invPurchaseInvoiceDetailsService.findInvPurchaseInvoiceDetailsById(invAddingorderDetail.getSelectedId().getId());
        purchaseInvoiceDetail = invAddingorderDetail.getSelectedId();
        // بجيب الكمية المتبقية من الصنف دا علشان اشتغل عليها
        purchaseInvoiceFinalQuantity = purchaseInvoiceDetail.getFinalQuantity() != null
                ? purchaseInvoiceDetail.getFinalQuantity() : purchaseInvoiceDetail.getQuantity();
        // هنا بقى بجيب الكمية القديمة اللى متسجله فى قاعدة البايات يعنى قبل اما اعدل فيها 
        if (invAddingorderDetail.getId() != null) {
            InvAddingorderDetail invAddingorderDetailOld = invAddingOrderDetailsService.findInvAddingOrderDetailsById(invAddingorderDetail.getId());
            quantityInvoiceOld = invAddingorderDetailOld.getQuantity();
        }
        // المعادلة دي بتجيب الكمية النهائية المتبقية علشان احطها فى الجدول الاساسي
        finalQuantity = calculateFinalQuantity(invAddingorderDetail, purchaseInvoiceFinalQuantity, quantityInvoiceOld);
        // هندخل على مرحلة تعديل حالة التفصيلى
        // هنشوف هل لسه فيه كمية متبقية فى التفصيلى ولا لسه 
        res = finalQuantity.compareTo(BigDecimal.ZERO);
        //لو لسه فى كمية متبقية فى التفصيلى هنحطها وهنخلى الحالة بواحد
        if (res == 1) {
            purchaseInvoiceDetail.setFinalQuantity(finalQuantity);
            purchaseInvoiceDetail.setStatus(1);
        } else {
            // لو الكمية صفر او اقل هنخلى الحالة باثنين
            purchaseInvoiceDetail.setFinalQuantity(BigDecimal.ZERO);
            purchaseInvoiceDetail.setStatus(2);
        }
        // هنحفظ التفصيلى
        invPurchaseInvoiceDetailsService.updateInvPurchaseInvoiceDetails(purchaseInvoiceDetail);
    }
//---------------------

    public BigDecimal calculateFinalQuantity(InvAddingorderDetail invAddingorderDetail,
            BigDecimal existQuantitiyFromDataBaseSource, BigDecimal quantityInvoiceOld) {
        BigDecimal finalQuantity;
        // بتأكد الاول هل السطر دا متعدل ولا لسه جديد
        if (invAddingorderDetail.getId() == null) {
            // لو كان لسه جديد فكل اللى هعمله انى هطرح الكمية اللى داخلة فى التفصيلى من اللى فى قاعدة البيانات اللى فى جدول امر الشرا
            finalQuantity = existQuantitiyFromDataBaseSource.subtract(invAddingorderDetail.getQuantity());
        } else {
            // اما لو كان تعديل فهطرح الكمية اللى مكتوبه فى التفصيلى من الكمية القديمة اللى فى جدول المشتريات 
            // وبعدين الناتج هطرحه من الكمية اللى موجدة فى جدول امر الشرا
            finalQuantity = existQuantitiyFromDataBaseSource.subtract(invAddingorderDetail.getQuantity().subtract(quantityInvoiceOld));
        }
        return finalQuantity;
    }

    //--------------------------
    public void deleteDetails(List<InvAddingorderDetail> invAddingorderDetailListDeleted, Boolean isPurchaseOrSales) {
        if (invAddingorderDetailListDeleted != null && !invAddingorderDetailListDeleted.isEmpty()) {
            for (InvAddingorderDetail deleted : invAddingorderDetailListDeleted) {
                if (deleted.getId() != null) {
                    restorQuantity(deleted, isPurchaseOrSales);
                    invAddingOrderDetailsService.deleteInvAddingOrderDetails(deleted);
                }
            }
        }
    }
//----------------------

    public synchronized void restorQuantity(InvAddingorderDetail invAddingorderDetail, Boolean isPurchaseOrSales) {
        if (isPurchaseOrSales) {
            if (invAddingorderDetail.getSelectedId() != null) {
                if (invAddingorderDetail.getTransferFrom() == 0) {
//                    InvPurchaseInvoiceDetail dbPurchaseInvoice = invPurchaseInvoiceDetailsService.findInvPurchaseInvoiceDetailsById(invAddingorderDetail.getSelectedId());
                    InvPurchaseInvoiceDetail dbPurchaseInvoice = invAddingorderDetail.getSelectedId();
                    InvAddingorderDetail invAddingorderDetailOld = invAddingOrderDetailsService.findInvAddingOrderDetailsById(invAddingorderDetail.getId());
                    dbPurchaseInvoice.setFinalQuantity(dbPurchaseInvoice.getFinalQuantity().add(invAddingorderDetailOld.getQuantity()));
                    dbPurchaseInvoice.setStatus(restorDetailStatus(dbPurchaseInvoice.getQuantity(), dbPurchaseInvoice.getFinalQuantity()));
                    invPurchaseInvoiceDetailsService.updateInvPurchaseInvoiceDetails(dbPurchaseInvoice);
                    List<Integer> updatedPurchaseOrAddingOrderList = new ArrayList<>();
                    updatedPurchaseOrAddingOrderList.add(dbPurchaseInvoice.getInvPurchaseInvoiceId().getId());
                    updateHeadStatus(updatedPurchaseOrAddingOrderList);
                }
            }
        }
        /* else{
         if (invAddingorderDetail.getSelectedPurchaseId() != null) {
         if (invAddingorderDetail.getTransferFrom() == 0) {
         //                    InvPurchaseInvoiceDetail dbPurchaseInvoice = invPurchaseInvoiceDetailsService.findInvPurchaseInvoiceDetailsById(invAddingorderDetail.getSelectedId());
         InvPurchaseInvoiceDetail dbPurchaseInvoice = invAddingorderDetail.getSelectedId();
         InvAddingorderDetail invAddingorderDetailOld = invAddingOrderDetailsService.findInvAddingOrderDetailsById(invAddingorderDetail.getId());
         dbPurchaseInvoice.setFinalQuantity(dbPurchaseInvoice.getFinalQuantity().add(invAddingorderDetailOld.getQuantity()));
         dbPurchaseInvoice.setStatus(restorDetailStatus(dbPurchaseInvoice.getQuantity(), dbPurchaseInvoice.getFinalQuantity()));
         invPurchaseInvoiceDetailsService.updateInvPurchaseInvoiceDetails(dbPurchaseInvoice);
         List<Integer> updatedPurchaseOrAddingOrderList = new ArrayList<>();
         updatedPurchaseOrAddingOrderList.add(dbPurchaseInvoice.getInvPurchaseInvoiceId().getId());
         updateHeadStatus(updatedPurchaseOrAddingOrderList);
         }
         } 
         }*/
    }
//----------------

    public Integer restorDetailStatus(BigDecimal quantity, BigDecimal finalQuantity) {
        if (quantity.compareTo(finalQuantity) == 0) {
            return 0;
        } else if (finalQuantity.compareTo(BigDecimal.ZERO) == 1 && quantity.compareTo(finalQuantity) == 1) {
            return 1;
        } else {
            return 2;
        }

    }

    //----------------------
    public void updateHeadStatus(List<Integer> updatedPurchaseOrAddingOrderList) {
        InvPurchaseInvoice purchaseInvoice;

        Map<String, Object> params = new HashMap<>();
        List<Integer> purchaseInvoiceDetailList;

        if (updatedPurchaseOrAddingOrderList != null) {
            for (Integer headId : updatedPurchaseOrAddingOrderList) {
                String query;

                params.put("headId", headId);
                query = "select p.status FROM InvPurchaseInvoiceDetail p WHERE p.invPurchaseInvoiceId.id = :headId ";
                purchaseInvoiceDetailList = dao.findListByQuery(query, params);
                purchaseInvoice = invPurchaseInvoiceService.findInvPurchaseInvoiceById(headId);
                if (purchaseInvoiceDetailList != null && purchaseInvoiceDetailList.size() > 0) {
                    for (int i = 0; i < purchaseInvoiceDetailList.size(); i++) {
                        if (i == 0) {
                            purchaseInvoice.setStatus(purchaseInvoiceDetailList.get(0));
                        } else {
                            Integer x = purchaseInvoiceDetailList.get(0) == null ? 0 : purchaseInvoiceDetailList.get(0);
                            Integer y = purchaseInvoiceDetailList.get(i) == null ? 0 : purchaseInvoiceDetailList.get(i);
                            if (x.compareTo(y) != 0) {

                                purchaseInvoice.setStatus(1);
                                break;
                            }
                        }
                    }
                    invPurchaseInvoiceService.updateInvPurchaseInvoice(purchaseInvoice);
                }

            }
        }
    }
    //----------------------

    @Override
    public void deleteInvAddingOrder(InvAddingorder invAddingOrder) {
        if (invAddingOrder.getId() != null && invAddingOrder.getId() > 0) {
            dao.executeDeleteQuery("delete from InvAddingorderDetail e WHERE e.id > 0 AND e.addingorderId.id = " + invAddingOrder.getId());
        }
        dao.deleteEntity(invAddingOrder);
    }

    @Override
    public List<InvAddingorder> getALLInvAddingOrderByCompanyId(Integer companyId) {
        return dao.findEntityListByCompanyId(InvAddingorder.class, companyId);
    }

    @Override
    public List<InvAddingorder> getALLInvAddingOrderByBranchId(Integer branchId, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        List<InvAddingorder> invAddingOrdersList = dao.findListByQuery("SELECT e FROM InvAddingorder e WHERE e.branchId.id = :branchId and e.type = :type", params);
        return invAddingOrdersList;
    }

    @Override
    public InvAddingorder findInvAddingOrderByInvAddingOrderId(Integer invAddingOrderId) {
        return dao.findEntityById(InvAddingorder.class, invAddingOrderId);
    }

    @Override
    public InvAddingorder updateInvAddingOrder(InvAddingorder invAddingOrder) {
        return dao.updateEntity(invAddingOrder);
    }

    @Override
    public List<InvAddingorder> getALLInvSalesOrderByBranchIdByStatus(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvAddingorder> invAddingOrdersList = dao.findListByQuery("SELECT e FROM InvAddingorder e WHERE e.branchId.id = :branchId and e.status != 2 and e.type = 1 ", params);
        return invAddingOrdersList;
    }

    @Override
    public List<InvAddingorder> getALLInvAddingOrderByBranchIdByStatus(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvAddingorder> invAddingOrdersList = dao.findListByQuery("SELECT e FROM InvAddingorder e WHERE e.branchId.id = :branchId and e.status != 2 and e.type = 0 ", params);
        return invAddingOrdersList;
    }

    /**
     * @return the invPurchaseInvoiceDetailsService
     */
    public InvPurchaseInvoiceDetailsService getInvPurchaseInvoiceDetailsService() {
        return invPurchaseInvoiceDetailsService;
    }

    /**
     * @param invPurchaseInvoiceDetailsService the
     * invPurchaseInvoiceDetailsService to set
     */
    public void setInvPurchaseInvoiceDetailsService(InvPurchaseInvoiceDetailsService invPurchaseInvoiceDetailsService) {
        this.invPurchaseInvoiceDetailsService = invPurchaseInvoiceDetailsService;
    }

    /**
     * @return the invPurchaseOrderDetailsService
     */
    public InvPurchaseOrderDetailsService getInvPurchaseOrderDetailsService() {
        return invPurchaseOrderDetailsService;
    }

    /**
     * @param invPurchaseOrderDetailsService the invPurchaseOrderDetailsService
     * to set
     */
    public void setInvPurchaseOrderDetailsService(InvPurchaseOrderDetailsService invPurchaseOrderDetailsService) {
        this.invPurchaseOrderDetailsService = invPurchaseOrderDetailsService;
    }

    /**
     * @return the invQuotationDetailsService
     */
    public InvQuotationDetailsService getInvQuotationDetailsService() {
        return invQuotationDetailsService;
    }

    /**
     * @param invQuotationDetailsService the invQuotationDetailsService to set
     */
    public void setInvQuotationDetailsService(InvQuotationDetailsService invQuotationDetailsService) {
        this.invQuotationDetailsService = invQuotationDetailsService;
    }

    /**
     * @return the invReservationDetailService
     */
    public InvReservationDetailService getInvReservationDetailService() {
        return invReservationDetailService;
    }

    /**
     * @param invReservationDetailService the invReservationDetailService to set
     */
    public void setInvReservationDetailService(InvReservationDetailService invReservationDetailService) {
        this.invReservationDetailService = invReservationDetailService;
    }

    /**
     * @return the invPurchaseOrderService
     */
    public InvPurchaseOrderService getInvPurchaseOrderService() {
        return invPurchaseOrderService;
    }

    /**
     * @param invPurchaseOrderService the invPurchaseOrderService to set
     */
    public void setInvPurchaseOrderService(InvPurchaseOrderService invPurchaseOrderService) {
        this.invPurchaseOrderService = invPurchaseOrderService;
    }

    /**
     * @return the invItemService
     */
    public InvItemService getInvItemService() {
        return invItemService;
    }

    /**
     * @param invItemService the invItemService to set
     */
    public void setInvItemService(InvItemService invItemService) {
        this.invItemService = invItemService;
    }

    /**
     * @return the itemsStoreService
     */
    public QuantityItemsStoreService getItemsStoreService() {
        return itemsStoreService;
    }

    /**
     * @param itemsStoreService the itemsStoreService to set
     */
    public void setItemsStoreService(QuantityItemsStoreService itemsStoreService) {
        this.itemsStoreService = itemsStoreService;
    }

    /**
     * @return the inventorySetupService
     */
    public InventorySetupService getInventorySetupService() {
        return inventorySetupService;
    }

    /**
     * @param inventorySetupService the inventorySetupService to set
     */
    public void setInventorySetupService(InventorySetupService inventorySetupService) {
        this.inventorySetupService = inventorySetupService;
    }

    @Override
    public InvInvoiceDTO convertAddingOrderToPurchaseInvoice(InvInvoiceDTO invPurchaseInvoiceDTO) {
        InvAddingorder invAddingorder = dao.findEntityById(InvAddingorder.class, invPurchaseInvoiceDTO.getInvDailogeDataSelected().getId());
        if (invAddingorder != null) {
            invPurchaseInvoiceDTO = mapToDTO(invAddingorder, invPurchaseInvoiceDTO);
            modifyReturnData(invPurchaseInvoiceDTO,invAddingorder);
        } else {
            invPurchaseInvoiceDTO.setMsg("حدث خطأ - تم حذفه ");
        }
        return invPurchaseInvoiceDTO;
    }

    private void modifyReturnData(InvInvoiceDTO invPurchaseInvoiceDTO,InvAddingorder invAddingorder) {
        invPurchaseInvoiceDTO.setId(null);
        invPurchaseInvoiceDTO.setSerial(null);
        invPurchaseInvoiceDTO.setLoadedSerial(invAddingorder.getSerial());
        invPurchaseInvoiceDTO.setInvAddingOrderId(invAddingorder.getId());
        List<InvDetailDTO> detailDTOs = new ArrayList<>();
        for (InvDetailDTO invDetailDTO : invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList()) {
            invDetailDTO.setId(null);
            invDetailDTO.setSerial(null);
            detailDTOs.add(invDetailDTO);
        }
        invPurchaseInvoiceDTO.setInvPurchaseInvoiceDetailList(detailDTOs);
    }

    private InvInvoiceDTO mapToDTO(InvAddingorder entity) {

        InvInvoiceDTO invPurchaseInvoiceDTO = new InvInvoiceDTO();

        invPurchaseInvoiceDTO = initData(invPurchaseInvoiceDTO, entity);

        return invPurchaseInvoiceDTO;
    }

    private InvInvoiceDTO mapToDTO(InvAddingorder entity, InvInvoiceDTO invPurchaseInvoiceDTO) {

        invPurchaseInvoiceDTO = initData(invPurchaseInvoiceDTO, entity);

        return invPurchaseInvoiceDTO;
    }

    private InvInvoiceDTO initData(InvInvoiceDTO invPurchaseInvoiceDTO, InvAddingorder entity) {
        invPurchaseInvoiceDTO.setId(entity.getId());
        invPurchaseInvoiceDTO.setSerial(entity.getSerial());
        invPurchaseInvoiceDTO.setCompanyActivity(entity.getBranchId().getCompanyActivity());
        invPurchaseInvoiceDTO.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
        invPurchaseInvoiceDTO.setDate(entity.getDate());
        invPurchaseInvoiceDTO.setRemarks(entity.getRemark());
        invPurchaseInvoiceDTO.setType(entity.getType());
        invPurchaseInvoiceDTO.setSupplierInvoiceDate(entity.getSupplierDate());
        invPurchaseInvoiceDTO.setSupplierInvoiceNumber(entity.getSupplierInvoice());
//        invPurchaseInvoiceDTO.setInvAddingOrderId(entity.getId());
        if (entity.getPurchaseOrderId() != null) {
            invPurchaseInvoiceDTO.setLoadedSerial(entity.getPurchaseOrderId() != null ? entity.getPurchaseOrderId().getSerial() : null);
            invPurchaseInvoiceDTO.setInvPurchaseOrderId(entity.getPurchaseOrderId() != null ? entity.getPurchaseOrderId().getId() : null);
            invPurchaseInvoiceDTO.setParent(entity.getPurchaseOrderId().getId());
        }
        
        if (entity.getInvPurchaseInvoice() != null) {
            invPurchaseInvoiceDTO.setLoadedSerial(entity.getInvPurchaseInvoice() != null ? entity.getInvPurchaseInvoice().getSerial() : null);
            invPurchaseInvoiceDTO.setInvPurchaseInvoiceId(entity.getInvPurchaseInvoice() != null ? entity.getInvPurchaseInvoice().getId() : null);
        }
        
        invPurchaseInvoiceDTO.setScreenName(invPurchaseInvoiceDTO.getScreenName());
        if (entity.getInvInventoryId() != null) {
            InvInventoryDTO invInventoryDTO = new InvInventoryDTO();
            invInventoryDTO.setId(entity.getInvInventoryId().getId());
            invInventoryDTO.setName(entity.getInvInventoryId().getName());
            invInventoryDTO.setCode(entity.getInvInventoryId().getCode());
            invPurchaseInvoiceDTO.setInvInventoryId(invInventoryDTO);
        }
        if (entity.getOrganizationSiteId() != null) {
            InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
            invOrganizationSiteDTO.setId(entity.getOrganizationSiteId().getId());
            invOrganizationSiteDTO.setName(entity.getOrganizationSiteId().getName());
            invOrganizationSiteDTO.setCode(entity.getOrganizationSiteId().getCode());

            if (entity.getOrganizationSiteId().getCurrencyId() != null) {
                CurrencyDTO currencyDTO = new CurrencyDTO();
                currencyDTO.setId(entity.getOrganizationSiteId().getCurrencyId().getId());
                currencyDTO.setName(entity.getOrganizationSiteId().getCurrencyId().getName());
                currencyDTO.setCode(entity.getOrganizationSiteId().getCurrencyId().getCode());
                invOrganizationSiteDTO.setCurrencyId(currencyDTO);
            }
            invPurchaseInvoiceDTO.setOrganizationSiteId(invOrganizationSiteDTO);
        }
        if (entity.getInvDelegatorId() != null) {
            InventoryDelegatorDTO inventoryDelegatorDTO = new InventoryDelegatorDTO();
            inventoryDelegatorDTO.setId(entity.getInvDelegatorId().getId());
            inventoryDelegatorDTO.setName(entity.getInvDelegatorId().getName());
            inventoryDelegatorDTO.setCode(entity.getInvDelegatorId().getCode());
            invPurchaseInvoiceDTO.setInvDelegatorId(inventoryDelegatorDTO);
        }
        invPurchaseInvoiceDTO.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        invPurchaseInvoiceDTO.setCreatedDate(entity.getCreationDate());
        invPurchaseInvoiceDTO.setIndex(entity.getSerial());
        invPurchaseInvoiceDTO.setCompanyId(entity.getCompanyId().getId() != null ? entity.getCompanyId().getId() : null);
        invPurchaseInvoiceDTO.setCreatedByName(entity.getCreatedBy() != null ? entity.getCreatedBy().getName() : null);
        List<InvDetailDTO> invPurchaseInvoiceDetailsListDTO = invAddingOrderDetailsService.getAllInvAddingOrderDetailsByInvPurchaseInvoiceIdDTO(invPurchaseInvoiceDTO);
        invPurchaseInvoiceDTO.setInvPurchaseInvoiceDetailList(invPurchaseInvoiceDetailsListDTO);
        invPurchaseInvoiceDTO.setInvPurchaseInvoiceDetailsDeletedList(new ArrayList<InvDetailDTO>());
        invPurchaseInvoiceDTO = invGlobalService.updateSummition(invPurchaseInvoiceDTO);
        return invPurchaseInvoiceDTO;
    }

    @Override
    public InvInvoiceDTO findInvAddingOrderById(InvInvoiceDTO invAddingInvoice, IsagUser isagUser) {
        InvAddingorder invAddingorder = dao.findEntityById(InvAddingorder.class, invAddingInvoice.getId());
        InvInvoiceDTO invPurchaseInvoiceDTO = mapToDTO(invAddingorder, invAddingInvoice);
        invPurchaseInvoiceDTO = invGlobalService.updateSummition(invPurchaseInvoiceDTO);
        return invPurchaseInvoiceDTO;
    }

    public InvInvoiceDTO validateSave(InvInvoiceDTO invPurchaseInvoiceDTO) {
        StringBuilder error = new StringBuilder();

        if (invPurchaseInvoiceDTO.getOrganizationSiteId() == null) {
            error.append("\n يجب أختيار عميل / مورد");
        }
        
        checkIfLoaded(invPurchaseInvoiceDTO,  error) ;
        
        if (!error.toString().isEmpty()) {
            invPurchaseInvoiceDTO.setMsg(error.toString());
        }
        return invPurchaseInvoiceDTO;
    }
    
    
    public void checkIfLoaded(InvInvoiceDTO invPurchaseInvoiceDTO, StringBuilder error) {
        if (invPurchaseInvoiceDTO.getId() != null) {
            List<Integer> id = dao.executeNativeQuery("select id from (select inv_adding_order_id as id from inv_purchase_invoice "
                    + " where branch_id = " + invPurchaseInvoiceDTO.getBranchId() + " and inv_adding_order_id is not null\n"+" ) as s\n"
                    + " where id = "+invPurchaseInvoiceDTO.getId());
            if (!id.isEmpty()) {
                    error.append("\n لا يجوز تعديل هذا السند لأنه تم تحميلة");
            }
        }
    }

    @Override
    public InvInvoiceDTO save(InvInvoiceDTO invInvoiceDTO, IsagUser isagUser) {
        try {
            invInvoiceDTO = validateSave(invInvoiceDTO);

            InvAddingorder invAddingOrder;
            if (invInvoiceDTO != null && (invInvoiceDTO.getMsg() == null || invInvoiceDTO.getMsg().isEmpty())) {
                invAddingOrder = mapToEntity(invInvoiceDTO, isagUser);
                if (invAddingOrder.getId() != null) {
                    //بعمل حفظ فى حالة التعديل
                    invAddingOrder = dao.updateEntity(invAddingOrder);
                } else {
                    // فى حالة ان الفاتورة جديدة بروح اجيب السيريال الاول 
                    Integer serial = genericService.getMaxGenericSerialByType(InvAddingorder.class, invAddingOrder.getBranchId().getId(), invAddingOrder.getType());
                    invAddingOrder.setSerial(serial);

                    invAddingOrder = dao.updateEntity(invAddingOrder);
                    invInvoiceDTO.setId(invAddingOrder.getId());
                }

                invAddingOrderDetailsService.deleteInvAddingOrderDetailListDTO(invInvoiceDTO.getInvPurchaseInvoiceDetailsDeletedList());
                String error;
                if (invAddingOrder.getInvInventoryId() != null) {
                    error = invAddingOrderDetailsService.addInvAddingOrderDetailsDTO(invInvoiceDTO.getInvPurchaseInvoiceDetailList(), invInvoiceDTO, isagUser);
                } else {
                    error = invAddingOrderDetailsService.addInvAddingOrderDetailsDTO(invInvoiceDTO.getInvPurchaseInvoiceDetailList(), invInvoiceDTO, isagUser);
                }
                if (error != null && !error.isEmpty()) {
                    invInvoiceDTO.setMsg(error);
                    dao.rollbackQuery();
                } else {
                    invInvoiceDTO = mapToDTO(invAddingOrder, invInvoiceDTO);
                    invInvoiceDTO = invGlobalService.updateSummition(invInvoiceDTO);
                }

            }
        } catch (Exception e) {
            invInvoiceDTO.setMsg(e.toString());
            dao.rollbackQuery();
        }

        return invInvoiceDTO;
    }

    //mapToEntity
    private InvAddingorder mapToEntity(InvInvoiceDTO invPurchaseInvoiceDTO, IsagUser isagUser) {

        InvAddingorder entity = new InvAddingorder();

        if (invPurchaseInvoiceDTO.getOrganizationSiteId() != null) {
            InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
            invOrganizationSite.setId(invPurchaseInvoiceDTO.getOrganizationSiteId().getId());
            invOrganizationSite.setName(invPurchaseInvoiceDTO.getOrganizationSiteId().getName());
            entity.setOrganizationSiteId(invOrganizationSite);
        }

        if (invPurchaseInvoiceDTO.getInvDelegatorId() != null) {
            InventoryDelegator inventoryDelegator = new InventoryDelegator();
            inventoryDelegator.setId(invPurchaseInvoiceDTO.getInvDelegatorId().getId());
            entity.setInvDelegatorId(inventoryDelegator);
        }
        if (invPurchaseInvoiceDTO.getInvInventoryId() != null) {
            InvInventory invInventory = new InvInventory();
            invInventory.setId(invPurchaseInvoiceDTO.getInvInventoryId().getId());
            entity.setInvInventoryId(invInventory);
        }

        if (invPurchaseInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVADDINGORDERFORM)) {
            entity.setType(0);
        } else if (invPurchaseInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVPERMISSIONORDERFORM)) {
            entity.setType(1);
        }

        entity.setId(invPurchaseInvoiceDTO.getId());
        entity.setSerial(invPurchaseInvoiceDTO.getSerial());
        entity.setDate(invPurchaseInvoiceDTO.getDate());
        entity.setRemark(invPurchaseInvoiceDTO.getRemarks());
        entity.setSupplierDate(invPurchaseInvoiceDTO.getSupplierInvoiceDate());
        entity.setSupplierInvoice(invPurchaseInvoiceDTO.getSupplierInvoiceNumber());

        if (invPurchaseInvoiceDTO.getInvPurchaseOrderId() != null) {
            InvPurchaseOrder order = new InvPurchaseOrder();
            order.setId(invPurchaseInvoiceDTO.getInvPurchaseOrderId());
            entity.setPurchaseOrderId(order);
        }
        
        if (invPurchaseInvoiceDTO.getInvPurchaseInvoiceId() != null) {
            InvPurchaseInvoice invPurchaseInvoice = new InvPurchaseInvoice();
            invPurchaseInvoice.setId(invPurchaseInvoiceDTO.getInvPurchaseInvoiceId());
            entity.setInvPurchaseInvoice(invPurchaseInvoice);
        }

        if (invPurchaseInvoiceDTO.getCreatedBy() != null) {
            IsagUser user = new IsagUser();
            user.setId(invPurchaseInvoiceDTO.getCreatedBy());
            entity.setCreatedBy(user);
            entity.setCreationDate(invPurchaseInvoiceDTO.getCreatedDate());
            entity.setModifiedBy(isagUser);
            entity.setModificationDate(new Date());
            if (invPurchaseInvoiceDTO.getCompanyId() != null) {
                IsagCompany company = new IsagCompany();
                company.setId(invPurchaseInvoiceDTO.getCompanyId());
                entity.setCompanyId(company);
            }

            if (invPurchaseInvoiceDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(invPurchaseInvoiceDTO.getBranchId());
                entity.setBranchId(branch);
            }
        } else {
            entity.setPostFlag(0);
            entity.setCreatedBy(isagUser);
            entity.setCreationDate(new Date());
            entity.setCompanyId(isagUser.getCompanyId());
            entity.setBranchId(isagUser.getBranchId());
        }

        return entity;
    }

    private void commonData(InvInvoiceDTO invPurchaseInvoiceDTO, InvAddingorder entity) {
        invPurchaseInvoiceDTO.setId(entity.getId());
        invPurchaseInvoiceDTO.setSerial(entity.getSerial());
        invPurchaseInvoiceDTO.setDate(entity.getDate());
        invPurchaseInvoiceDTO.setIndex(entity.getSerial());
        invPurchaseInvoiceDTO.setSupplierInvoiceDate(entity.getSupplierDate());
        invPurchaseInvoiceDTO.setSupplierInvoiceNumber(entity.getSupplierInvoice());
        invPurchaseInvoiceDTO.setPostFlag(entity.getPostFlag());
        invPurchaseInvoiceDTO.setRemarks(entity.getRemark());
        if (entity.getOrganizationSiteId() != null) {
            InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
            invOrganizationSiteDTO.setName(entity.getOrganizationSiteId().getName());
            invPurchaseInvoiceDTO.setOrganizationSiteId(invOrganizationSiteDTO);
        }
          if (entity.getPurchaseOrderId() != null) {
            invPurchaseInvoiceDTO.setLoadedSerial(entity.getPurchaseOrderId().getSerial());
          }
    }

    private InvInvoiceDTO initDataForList(InvInvoiceDTO invInvoiceDTO, InvAddingorder invAddingorder) {
        commonData(invInvoiceDTO, invAddingorder);
        return invInvoiceDTO;
    }

    @Override
    public Long getTotalRegistors(Integer type, Integer branchId, Map<String, Object> filters) {
        StringBuilder builder = new StringBuilder();
        filters.put("type", type);
        filters.put("branchId", branchId);
        builder.append("SELECT count(s) FROM InvAddingorder s WHERE   s.branchId.id = :branchId AND s.type =:type ");
        appendFilter(filters, builder);
          builder.append("   group by s.id ");
        Long details = dao.findEntityByQuery(builder.toString(), filters);

       
        return details == null ? 0 : details;
    }

    private void appendFilter(Map<String, Object> filters, StringBuilder builder) {
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and ( FUNCTION('DATE_FORMAT',s.date, '%d-%m-%Y')  like CONCAT('%',:globalFilter ,'%') OR  s.serial like CONCAT('%',:globalFilter ,'%')  OR s.organizationSiteId.name  like CONCAT('%',:globalFilter ,'%') OR s.supplierInvoice like CONCAT('%',:globalFilter ,'%') OR  s.purchaseOrderId.serial like CONCAT('%',:globalFilter ,'%')) or s.purchaseOrderId IS NULL ";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }

    @Override
    public List<InvInvoiceDTO> getALLInvAddingorderDTOByBranchId(Integer type, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters) {
        filters.put("type", type);
        filters.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT s FROM InvAddingorder s WHERE   s.branchId.id = :branchId AND s.type =:type  ");
        appendFilter(filters, builder);
          builder.append("  group by s.id ");
        builder.append(" order by s.serial DESC");
        

        List<InvAddingorder> details = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        return returnListDTOForList(details);
    }

    private List<InvInvoiceDTO> returnListDTOForList(List<InvAddingorder> invAddingorderList) {
        List<InvInvoiceDTO> invInvoiceDTOList = new ArrayList<>();
        for (InvAddingorder invAddingorder : invAddingorderList) {
            InvInvoiceDTO invInvoiceDTO = new InvInvoiceDTO();
            invInvoiceDTO = initDataForList(invInvoiceDTO, invAddingorder);
            invInvoiceDTOList.add(invInvoiceDTO);
        }
        return invInvoiceDTOList;
    }

}
