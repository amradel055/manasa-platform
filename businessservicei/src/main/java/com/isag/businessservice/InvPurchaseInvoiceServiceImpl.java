/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.bussinessservice.global.InvPurchaseReturnSave;
import com.isag.core.GenericDAO;
import com.isag.dto.InvInventoryDTO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.CostCenter;
import com.isag.entity.GlBank;
import com.isag.entity.InvAddingorder;
import com.isag.entity.InvAddingorderDetail;
import com.isag.entity.InvInventory;
import com.isag.entity.InvItem;
import com.isag.entity.InvOrganizationSite;
import com.isag.entity.InvPurchaseInvoice;
import com.isag.entity.InvPurchaseInvoiceDetail;
import com.isag.entity.InvPurchaseOrder;
import com.isag.entity.InvPurchaseOrderDetail;
import com.isag.entity.InvQotation;
import com.isag.entity.InvQotationDetail;
import com.isag.entity.InvReservation;
import com.isag.entity.InvReservationDetail;
import com.isag.entity.InvTransfer;
import com.isag.entity.InvTransferDetail;
import com.isag.entity.InventorySetup;
import com.isag.views.QuantityItemsStore;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
public class InvPurchaseInvoiceServiceImpl implements InvPurchaseInvoiceService {

    @EJB
    private GenericDAO dao;
    @EJB
    InvPurchaseInvoiceDetailsService invPurchaseInvoiceDetailsService;
    @EJB
    private InvAddingOrderDetailsService invAddingOrderDetailsService;
    @EJB
    private InvPurchaseOrderDetailsService invPurchaseOrderDetailsService;
    @EJB
    private InvQuotationDetailsService invQuotationDetailsService;
    @EJB
    private InvReservationDetailService invReservationDetailService;
    @EJB
    private InvTransferDetailService invTransferDetailService;
    @EJB
    private InvPurchaseOrderService invPurchaseOrderService;
    @EJB
    private InvAddingOrderService invAddingOrderService;
    @EJB
    private InvQuotationService invQuotationService;
    @EJB
    private InvReservationService invReservationService;
    @EJB
    private InvTransferService invTransferService;
    @EJB
    private InvItemService invItemService;
    @EJB
    private QuantityItemsStoreService itemsStoreService;
    @EJB
    private QuantityItemsStoreByDateService quantityItemsStoreByDateService;
    @EJB
    private InventorySetupService inventorySetupService;
    @EJB
    private GenericService genericService;

    InvPurchaseReturnSave invPurchaseReturnSave;

    @Override
    public synchronized InvPurchaseReturnSave addInvPurchaseInvoice(InvPurchaseInvoice invPurchaseInvoice,
            List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailList,
            List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailListDeleted,
            List<Integer> updatedPurchaseOrAddingOrderList, Integer purchaseOrAddingOrder, StringBuilder headIdList, Integer isPurchaseOrSales) {

        invPurchaseReturnSave = new InvPurchaseReturnSave();
        if (invPurchaseInvoice.getId() != null) {
            //بعمل حفظ فى حالة التعديل
            dao.updateEntity(invPurchaseInvoice);
        } else {
            // فى حالة ان الفاتورة جديدة بروح اجيب السيريال الاول 
            Integer serial = genericService.getMaxSerialForInvoice(InvPurchaseInvoice.class, invPurchaseInvoice.getBranchId().getId(), invPurchaseInvoice.getInvInventoryId().getId(), invPurchaseInvoice.getType());
            // بجيب السيريال الخاص بالفواتير الضريبية
            if (invPurchaseInvoice.getTaxflag()) {
                Integer serialTax = getMaxSerialTaxForInvoice(invPurchaseInvoice.getBranchId().getId(), invPurchaseInvoice.getInvInventoryId().getId(), invPurchaseInvoice.getType(), invPurchaseInvoice.getTaxFlagFinal() == null ? Boolean.FALSE : invPurchaseInvoice.getTaxFlagFinal());
                invPurchaseInvoice.setSerialTax(serialTax);
            }
            invPurchaseInvoice.setSerial(serial);

            dao.saveEntity(invPurchaseInvoice);
        }
        // لو انا محمل الفاتورة من مرحلة سابقة بعمل هنا تعديل للكميات وحالة كل فاتورة من المحمل منها
        String msg = updateFinalQuantityAndStatusDetailAndSaveDetail(invPurchaseInvoiceDetailList, isPurchaseOrSales, invPurchaseInvoice);

        if (msg == null) {
            // لو الدنيا اللى فاتت تمام نروح نمسح من قاعدة البيانات اللى اتمسح من الفاتورة
            deleteDetails(invPurchaseInvoiceDetailListDeleted, isPurchaseOrSales);
            // هنا بروح اعدل حالة الفاتورة علشان وانا جاي استدعي الفواتير اكون عارف حالتها
            if (updatedPurchaseOrAddingOrderList != null && !updatedPurchaseOrAddingOrderList.isEmpty()) {
                updateHeadStatus(updatedPurchaseOrAddingOrderList, purchaseOrAddingOrder);
            }
            if (headIdList != null && !"".equals(headIdList.toString())) {
                updateHeadStatus(findHeadSelectedId(headIdList, purchaseOrAddingOrder), purchaseOrAddingOrder);
            }

        } else {
            invPurchaseInvoice.setMsg(msg);
            invPurchaseReturnSave.setMsg(msg);
            dao.rollbackQuery();
        }

        invPurchaseReturnSave.setInvPurchaseInvoice(invPurchaseInvoice);
        invPurchaseReturnSave.setInvPurchaseInvoiceDetailList(invPurchaseInvoiceDetailsService.getAllInvPurchaseInvoiceDetailsByInvPurchaseInvoiceId(invPurchaseInvoice.getId()));
        return invPurchaseReturnSave;
    }

    @Override
    public void addInvPurchaseInvoiceTest(InvPurchaseInvoice invPurchaseInvoice) {
        dao.saveEntity(invPurchaseInvoice);
    }

    @Override
    public void saveDetail(InvPurchaseInvoiceDetail invPurchaseInvoiceDetail, InvPurchaseInvoice invPurchaseInvoice,
            Integer isPurchaseOrSales, BigDecimal quantityItemsStoreByDate, InventorySetup invSetup) {
        quantityItemsStoreByDate = quantityItemsStoreByDate == null ? BigDecimal.ZERO : quantityItemsStoreByDate;
        invPurchaseInvoiceDetail.setInvPurchaseInvoiceId(invPurchaseInvoice);
        if (isPurchaseOrSales == 1) {
            if (!invSetup.getNegativeSell()) {
                invPurchaseInvoiceDetail.setCostAvarage(updateCostAvarage(invPurchaseInvoiceDetail, quantityItemsStoreByDate));
            }
        } else {
            invPurchaseInvoiceDetail.setCostAvarage(findCostAvarageForItemByDate(invPurchaseInvoice.getInvInventoryId().getId(), invPurchaseInvoice.getBranchId().getId(), invPurchaseInvoiceDetail.getItemId().getId(), invPurchaseInvoice.getDate()));
        }
        if (invPurchaseInvoiceDetail.getId() != null) {
            invPurchaseInvoiceDetailsService.updateInvPurchaseInvoiceDetails(invPurchaseInvoiceDetail);
        } else {
            invPurchaseInvoiceDetail.setSerial(getMaxSerialForInvoiceDetail(invPurchaseInvoiceDetail.getInvPurchaseInvoiceId()));
            invPurchaseInvoiceDetailsService.updateInvPurchaseInvoiceDetails(invPurchaseInvoiceDetail);
        }

//        invPurchaseReturnSave.getInvPurchaseInvoiceDetailList().add(invPurchaseInvoiceDetail);
    }

    public synchronized BigDecimal findCostAvarageForItemByDate(Integer inventoryId, Integer branchId, Integer itemId, Date date) {
        Map<String, Object> params = new HashMap();
        params.put("inventoryId", inventoryId);
        params.put("branchId", branchId);
        params.put("itemId", itemId);
        params.put("date", date);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ipid.costAvarage FROM InvPurchaseInvoiceDetail ipid LEFT JOIN InvPurchaseInvoice ipi WHERE ipid.invPurchaseInvoiceId.id = ipi.id  AND ipi.invInventoryId.id = :inventoryId AND ipi.branchId.id = :branchId and ipi.type = false AND ipid.itemId.id = :itemId AND ipi.date <= :date order by ipi.date DESC");

        List<BigDecimal> itemsStores = dao.findListByQuery(sql.toString(), params);

        return (itemsStores != null && itemsStores.size() > 0) ? itemsStores.get(0) : null;
    }

    @Override
    public void reCalcCostAvarage(Integer branchId) {
        InventorySetup invSetup = inventorySetupService.getInventoryByBranchId(branchId);
        List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailList = invPurchaseInvoiceDetailsService.getAllInvPurchaseInvoiceDetailByBranchId(branchId);
        for (InvPurchaseInvoiceDetail detail : invPurchaseInvoiceDetailList) {
            BigDecimal quantityItemsStoreByDate = findQuantityItemByDate(detail.getInvPurchaseInvoiceId(), detail);
            saveDetail(detail, detail.getInvPurchaseInvoiceId(), detail.getInvPurchaseInvoiceId().getType(), quantityItemsStoreByDate, invSetup);
        }

    }

    public BigDecimal updateCostAvarage(InvPurchaseInvoiceDetail invPurchaseInvoiceDetail, BigDecimal quantityItemsStoreByDate) {
        InvItem invItem = invItemService.findInvItem(invPurchaseInvoiceDetail.getItemId().getId());
        BigDecimal costAvarageDetail = invPurchaseInvoiceDetail.getNet().divide(((invPurchaseInvoiceDetail.getScrewing() == null ? BigDecimal.ONE : invPurchaseInvoiceDetail.getScrewing()).multiply(invPurchaseInvoiceDetail.getQuantity())), 2, RoundingMode.HALF_UP).setScale(3, BigDecimal.ROUND_UP);
        if (invItem.getCostAverage() == null) {
            invItem.setCostAverage(costAvarageDetail);
        } else {
            BigDecimal allQuantity = (invPurchaseInvoiceDetail.getScrewing() == null ? BigDecimal.ONE : invPurchaseInvoiceDetail.getScrewing()).multiply(invPurchaseInvoiceDetail.getQuantity());
            BigDecimal oldValue = invItem.getCostAverage().multiply(quantityItemsStoreByDate);
            BigDecimal newValue = costAvarageDetail.multiply(allQuantity);
            BigDecimal allAmount = quantityItemsStoreByDate.add(allQuantity);
            BigDecimal costAvarage = BigDecimal.ZERO;
            if (allAmount.compareTo(BigDecimal.ZERO) == 1) {
                costAvarage = (oldValue.add(newValue)).divide(allAmount, 2, RoundingMode.HALF_UP);
            }
            invItem.setCostAverage(costAvarage);
        }
        invItemService.updateInvItem(invItem);
        return invItem.getCostAverage();
    }

    public void deleteDetails(List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailListDeleted, Integer isPurchaseOrSales) {
        if (invPurchaseInvoiceDetailListDeleted != null && !invPurchaseInvoiceDetailListDeleted.isEmpty()) {
            for (InvPurchaseInvoiceDetail deleted : invPurchaseInvoiceDetailListDeleted) {
                if (deleted.getId() != null) {
                    restorQuantity(deleted, isPurchaseOrSales);
                    invPurchaseInvoiceDetailsService.deleteInvPurchaseInvoiceDetails(deleted);
                }
            }
        }
    }

    public List<Integer> findHeadSelectedId(StringBuilder headIdList, Integer isPurchaseOrder) {
        String query = null;
        if (isPurchaseOrder == 0) {
            query = "select DISTINCT h.id FROM InvPurchaseOrder h Left join InvPurchaseOrderDetail d WHERE h.id = d.invPurchaseOrderId.id and d.id in (" + headIdList.toString() + ") ";
        } else if (isPurchaseOrder == 1) {
            query = "select DISTINCT h.id FROM InvAddingorder h Left join InvAddingorderDetail d WHERE h.id = d.addingorderId.id and d.id in (" + headIdList.toString() + ") ";
        } else if (isPurchaseOrder == 2) {
            query = "select DISTINCT h.id FROM InvQotation h Left join InvQotationDetail d WHERE h.id = d.qotationId.id and d.id in (" + headIdList.toString() + ") ";
        } else if (isPurchaseOrder == 3) {
            query = "select DISTINCT h.id FROM InvReservation h Left join InvReservationDetail d WHERE h.id = d.reservationId.id and d.id in (" + headIdList.toString() + ") ";
        } else if (isPurchaseOrder == 4) {
            query = "select DISTINCT h.id FROM InvTransfer h Left join InvTransferDetail d WHERE h.id = d.invTransferId.id and d.id in (" + headIdList.toString() + ") ";
        }
        return dao.findListByQuery(query);

    }

    public synchronized void restorQuantity(InvPurchaseInvoiceDetail invPurchaseInvoiceDetail, Integer isPurchaseOrSales) {
        if (isPurchaseOrSales == 1) {
            if (invPurchaseInvoiceDetail.getSelectedId() != null) {
                if (invPurchaseInvoiceDetail.getTransferFrom() == 0) {
                    InvPurchaseOrderDetail dbPurchaseOrder = invPurchaseOrderDetailsService.findInvPurchaseOrderDetailById(invPurchaseInvoiceDetail.getSelectedId());
                    dbPurchaseOrder.setFinalQuantity(dbPurchaseOrder.getFinalQuantity().add(invPurchaseInvoiceDetail.getQuantity()));
                    dbPurchaseOrder.setStatus(restorDetailStatus(dbPurchaseOrder.getQuantity(), dbPurchaseOrder.getFinalQuantity()));
                    invPurchaseOrderDetailsService.updatePurchaseOrderDetails(dbPurchaseOrder);
                    List<Integer> updatedPurchaseOrAddingOrderList = new ArrayList<>();
                    updatedPurchaseOrAddingOrderList.add(dbPurchaseOrder.getInvPurchaseOrderId().getId());
                    updateHeadStatus(updatedPurchaseOrAddingOrderList, 1);
                } else {
                    InvAddingorderDetail addingorderDetail = invAddingOrderDetailsService.findInvAddingOrderDetailsById(invPurchaseInvoiceDetail.getSelectedId());
                    addingorderDetail.setFinalQuantity(addingorderDetail.getFinalQuantity().add(invPurchaseInvoiceDetail.getQuantity()));
                    addingorderDetail.setStatus(restorDetailStatus(addingorderDetail.getQuantity(), addingorderDetail.getFinalQuantity()));
                    invAddingOrderDetailsService.updateInvAddingOrderDetails(addingorderDetail);
                    List<Integer> updatedPurchaseOrAddingOrderList = new ArrayList<>();
                    updatedPurchaseOrAddingOrderList.add(addingorderDetail.getAddingorderId().getId());
                    updateHeadStatus(updatedPurchaseOrAddingOrderList, 2);
                }
            }
        }
    }

    public Integer restorDetailStatus(BigDecimal quantity, BigDecimal finalQuantity) {
        if (quantity.compareTo(finalQuantity) == 0) {
            return 0;
        } else if (finalQuantity.compareTo(BigDecimal.ZERO) == 1 && quantity.compareTo(finalQuantity) == 1) {
            return 1;
        } else {
            return 2;
        }

    }

    public void updateHeadStatus(List<Integer> updatedPurchaseOrAddingOrderList, Integer purchaseOrAddingOrder) {
        InvPurchaseOrder purchaseOrder;
        InvAddingorder invAddingorder;
        InvQotation invQotation;
        InvReservation invReservation;
        InvTransfer invTransfer;
        Map<String, Object> params = new HashMap<>();
        List<Integer> purchaseOrderDetailList;
        List<Integer> addingOrderDetailList;
        List<Integer> invQotationDetailList;
        List<Integer> invReservationDetailList;
        List<Integer> invTransferDetailList;
        if (updatedPurchaseOrAddingOrderList != null) {
            for (Integer headId : updatedPurchaseOrAddingOrderList) {
                String query;
                if (purchaseOrAddingOrder == 0) {
                    params.put("headId", headId);
                    query = "select p.status FROM InvPurchaseOrderDetail p WHERE p.invPurchaseOrderId.id = :headId ";
                    purchaseOrderDetailList = dao.findListByQuery(query, params);
                    purchaseOrder = invPurchaseOrderService.findPurchaseOrderByPurchaseOrderId(headId);
                    if (purchaseOrderDetailList != null && purchaseOrderDetailList.size() > 0) {
                        for (int i = 0; i < purchaseOrderDetailList.size(); i++) {
                            if (i == 0) {
                                purchaseOrder.setStatus(purchaseOrderDetailList.get(0));
                            } else {
                                Integer x = purchaseOrderDetailList.get(0) == null ? 0 : purchaseOrderDetailList.get(0);
                                Integer y = purchaseOrderDetailList.get(i) == null ? 0 : purchaseOrderDetailList.get(i);
                                if (x.compareTo(y) != 0) {
                                    purchaseOrder.setStatus(1);
                                    break;
                                }
                            }
                        }
                        invPurchaseOrderService.updateInvPurchaseOrder(purchaseOrder);
                    }
                } else if (purchaseOrAddingOrder == 1) {
                    params.put("headId", headId);
                    query = "select p.status FROM InvAddingorderDetail p WHERE p.addingorderId.id = :headId ";
                    addingOrderDetailList = dao.findListByQuery(query, params);
                    invAddingorder = invAddingOrderService.findInvAddingOrderByInvAddingOrderId(headId);
                    if (addingOrderDetailList != null && addingOrderDetailList.size() > 0) {
                        for (int i = 0; i < addingOrderDetailList.size(); i++) {
                            if (i == 0) {
                                invAddingorder.setStatus(addingOrderDetailList.get(0));
                            } else {
                                Integer x = addingOrderDetailList.get(0) == null ? 0 : addingOrderDetailList.get(0);
                                Integer y = addingOrderDetailList.get(i) == null ? 0 : addingOrderDetailList.get(i);
                                if (x.compareTo(y) != 0) {
                                    invAddingorder.setStatus(1);
                                    break;
                                }
                            }
                        }
                        invAddingOrderService.updateInvAddingOrder(invAddingorder);
                    }

                }
                if (purchaseOrAddingOrder == 2) {
                    params.put("headId", headId);
                    query = "select p.status FROM InvQotationDetail p WHERE p.qotationId.id = :headId ";
                    invQotationDetailList = dao.findListByQuery(query, params);
                    invQotation = invQuotationService.findInvQotationByInvQotationId(headId);
                    if (invQotationDetailList != null && invQotationDetailList.size() > 0) {
                        for (int i = 0; i < invQotationDetailList.size(); i++) {
                            if (i == 0) {
                                invQotation.setStatus(invQotationDetailList.get(0));
                            } else {
                                Integer x = invQotationDetailList.get(0) == null ? 0 : invQotationDetailList.get(0);
                                Integer y = invQotationDetailList.get(i) == null ? 0 : invQotationDetailList.get(i);
                                if (x.compareTo(y) != 0) {
                                    invQotation.setStatus(1);
                                    break;
                                }
                            }
                        }
                        invQuotationService.updateInvQotation(invQotation);
                    }
                }
                if (purchaseOrAddingOrder == 3) {
                    params.put("headId", headId);
                    query = "select p.status FROM InvReservationDetail p WHERE p.reservationId.id = :headId ";
                    invReservationDetailList = dao.findListByQuery(query, params);
                    invReservation = invReservationService.findInvReservationByInvReservationId(headId);
                    if (invReservationDetailList != null && invReservationDetailList.size() > 0) {
                        for (int i = 0; i < invReservationDetailList.size(); i++) {
                            if (i == 0) {
                                invReservation.setStatus(invReservationDetailList.get(0));
                            } else {
                                Integer x = invReservationDetailList.get(0) == null ? 0 : invReservationDetailList.get(0);
                                Integer y = invReservationDetailList.get(i) == null ? 0 : invReservationDetailList.get(i);
                                if (x.compareTo(y) != 0) {
                                    invReservation.setStatus(1);
                                    break;
                                }
                            }
                        }
                        invReservationService.updateInvReservation(invReservation);
                    }
                }
                if (purchaseOrAddingOrder == 4) {
                    params.put("headId", headId);
                    query = "select p.status FROM InvTransferDetail p WHERE p.invTransferId.id = :headId ";
                    invTransferDetailList = dao.findListByQuery(query, params);
                    invTransfer = invTransferService.findInvTransferByInvTransferId(headId);
                    if (invTransferDetailList != null && invTransferDetailList.size() > 0) {
                        for (int i = 0; i < invTransferDetailList.size(); i++) {
                            if (i == 0) {
                                invTransfer.setStatus(invTransferDetailList.get(0));
                            } else {
                                Integer x = invTransferDetailList.get(0) == null ? 0 : invTransferDetailList.get(0);
                                Integer y = invTransferDetailList.get(i) == null ? 0 : invTransferDetailList.get(i);
                                if (x.compareTo(y) != 0) {
                                    invTransfer.setStatus(1);
                                    break;
                                }
                            }
                        }
                        invTransferService.updateInvTransfer(invTransfer);
                    }
                }
            }
        }
    }

    @Override
    public void deleteInvPurchaseInvoice(InvPurchaseInvoice invPurchaseInvoice, Integer isPurchaseOrSales) {
        if (invPurchaseInvoice.getId() != null && invPurchaseInvoice.getId() > 0) {
            List<InvPurchaseInvoiceDetail> detailList = invPurchaseInvoiceDetailsService.getAllInvPurchaseInvoiceDetailsByInvPurchaseInvoiceId(invPurchaseInvoice.getId());
            deleteDetails(detailList, isPurchaseOrSales);
            dao.deleteEntity(invPurchaseInvoice);
        }
    }

    @Override
    public void deleteInvPurchaseInvoiceDTO(InvInvoiceDTO invPurchaseInvoice, Integer isPurchaseOrSales) {
        if (invPurchaseInvoice != null && invPurchaseInvoice.getId() > 0) {
            List<InvPurchaseInvoiceDetail> detailList = invPurchaseInvoiceDetailsService.getAllInvPurchaseInvoiceDetailsByInvPurchaseInvoiceId(invPurchaseInvoice.getId());
            deleteDetails(detailList, isPurchaseOrSales);
            InvPurchaseInvoice invPurchaseInvoice1 = new InvPurchaseInvoice();
            invPurchaseInvoice1.setId(invPurchaseInvoice != null ? invPurchaseInvoice.getId() : null);
            dao.deleteEntity(invPurchaseInvoice1);
        }
    }

    @Override
    public List<InvPurchaseInvoice> getALLInvPurchaseInvoiceByCompanyId(Integer companyId) {
        return dao.findEntityListByCompanyId(InvPurchaseInvoice.class, companyId);
    }

    @Override
    public List<InvPurchaseInvoice> getALLInvPurchaseInvoiceByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        return dao.findListByQuery("SELECT e FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId AND e.type = false", params);
    }

    @Override
    public List<InvPurchaseInvoice> getALLInvPurchaseInvoicePostFlagedByBranchId(Integer branchId, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        List<InvPurchaseInvoice> invPurchaseInvoiceList = dao.findListByQuery("SELECT e FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId AND e.postFlag != 1 AND e.type =:type", params);
        return invPurchaseInvoiceList;
    }

    @Override
    public List<InvPurchaseInvoice> getALLInvPurchaseInvoicePostFlagedByBranchIdPer(Integer branchId, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        List<InvPurchaseInvoice> invPurchaseInvoiceList = dao.findListByQuery("SELECT e FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId AND e.type =:type", params);
        List<InvPurchaseInvoice> list = retriveInvPurchaseInvoiceDate(invPurchaseInvoiceList);
        return list;
    }

    @Override
    public InvPurchaseInvoice getALLInvPurchaseInvoiceByBranchIdAndTypeAndSerial(Integer branchId, Integer type, Integer serial) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        params.put("serial", serial);
        InvPurchaseInvoice invPurchaseInvoice = dao.findEntityByQuery("SELECT e FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId AND e.type =:type AND e.serial =:serial", params);
        return invPurchaseInvoice;
    }

    private List<InvPurchaseInvoice> retriveInvPurchaseInvoiceDate(List<InvPurchaseInvoice> invPurchaseInvoiceList) {
        List<InvPurchaseInvoice> list = new ArrayList<>();
        for (InvPurchaseInvoice invPurchaseInvoice : invPurchaseInvoiceList) {
            InvPurchaseInvoice invoice = new InvPurchaseInvoice();
            invoice.setId(invPurchaseInvoice.getId());
            invoice.setSerial(invPurchaseInvoice.getSerial());
            invoice.setPaymentType(invPurchaseInvoice.getPaymentType());
            invoice.setRemarks(invPurchaseInvoice.getRemarks());
            invoice.setTaxflag(invPurchaseInvoice.getTaxflag());
            invoice.setTaxFlagFinal(invPurchaseInvoice.getTaxFlagFinal());
            InvInventory inventory = null;
            if (invPurchaseInvoice.getInvInventoryId() != null) {
                inventory = new InvInventory();
                inventory.setId(invPurchaseInvoice.getInvInventoryId().getId());
                inventory.setCode(invPurchaseInvoice.getInvInventoryId().getCode());
                inventory.setName(invPurchaseInvoice.getInvInventoryId().getName());
            }
            invoice.setInvInventoryId(inventory);
            GlBank glbank = null;
            if (invPurchaseInvoice.getGlBankId() != null) {
                glbank = new GlBank();
                glbank.setId(invPurchaseInvoice.getGlBankId().getId());
                glbank.setCode(invPurchaseInvoice.getGlBankId().getCode());
                glbank.setName(invPurchaseInvoice.getGlBankId().getName());
            }
            invoice.setGlBankId(glbank);
            InvOrganizationSite site = null;
            if (invPurchaseInvoice.getOrganizationSiteId() != null) {
                site = new InvOrganizationSite();
                site.setId(invPurchaseInvoice.getOrganizationSiteId().getId());
                site.setCode(invPurchaseInvoice.getOrganizationSiteId().getCode());
                site.setName(invPurchaseInvoice.getOrganizationSiteId().getName());
            }
            invoice.setOrganizationSiteId(site);
            CostCenter center = null;
            if (invPurchaseInvoice.getCostCenterId() != null) {
                center = new CostCenter();
                center.setId(invPurchaseInvoice.getCostCenterId().getId());
                center.setCode(invPurchaseInvoice.getCostCenterId().getCode());
                center.setName(invPurchaseInvoice.getCostCenterId().getName());
            }
            invoice.setCostCenterId(center);
            list.add(invoice);
        }
        return list;
    }

    @Override
    public InvPurchaseInvoice findInvPurchaseInvoiceById(Integer invPurchaseInvoiceId) {
        InvPurchaseInvoice invPurchaseInvoice = dao.findEntityById(InvPurchaseInvoice.class, invPurchaseInvoiceId);
        return invPurchaseInvoice;
    }

    @Override
    public List<InvPurchaseInvoice> getALLSalesInvoiceByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        return dao.findListByQuery("SELECT e FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId AND e.type = true", params);
    }

    @Override
    public List<InvPurchaseInvoice> getALLSalesInvoiceByBranchIdWithStatus(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        return dao.findListByQuery("SELECT e FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId AND e.type = true AND (e.status != 2  OR e.status is null)", params);
    }

    @Override
    public List<InvPurchaseInvoice> getALLSalesInvoiceByBranchIdWithStatusPer(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvPurchaseInvoice> invPurchaseInvoiceList = dao.findListByQuery("SELECT e FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId AND e.type = true AND (e.status != 2  OR e.status is null)", params);
        List<InvPurchaseInvoice> list = retriveInvPurchaseInvoiceDate(invPurchaseInvoiceList);
        return list;
    }

    @Override
    public InvPurchaseInvoice updateInvPurchaseInvoice(InvPurchaseInvoice invPurchaseInvoice) {

        InvPurchaseInvoice invoiceUpdate = new InvPurchaseInvoice();

        invoiceUpdate = dao.findEntityById(InvPurchaseInvoice.class, invPurchaseInvoice.getId());
        invoiceUpdate.setPostFlag(invPurchaseInvoice.getPostFlag());
        if (invPurchaseInvoice.getPostFlag().compareTo(1) == 0) {
            invoiceUpdate.setGeneralJournalId(invPurchaseInvoice.getGeneralJournalId());
        } else {
            invoiceUpdate.setGeneralJournalId(null);
        }

        return dao.updateEntity(invPurchaseInvoice);
    }

    @Override
    public synchronized String updateFinalQuantityAndStatusDetailAndSaveDetail(List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailsUpdatedList,
            Integer isPurchaseOrSales, InvPurchaseInvoice invPurchaseInvoice) {
        InventorySetup invSetup = inventorySetupService.getInventoryByBranchId(invPurchaseInvoice.getBranchId().getId());

        Integer selectedId;
        QuantityItemsStore quantityItemsStore;
        BigDecimal quantityItemsStoreByDate;
        // بحدد اذا انا كنت فى فاتورة المشتريات
        if (isPurchaseOrSales == 1) {
            // بعمل لوب على تفاصيل الفاتورة
            for (InvPurchaseInvoiceDetail invPurchaseInvoiceDetail : invPurchaseInvoiceDetailsUpdatedList) {
                quantityItemsStoreByDate = findQuantityItemByDate(invPurchaseInvoice, invPurchaseInvoiceDetail, invSetup);
                // هنا بقي بعدل الكميات وحالة الفواتير من الشاشات المحمل منها الفاتورةر و بحفظ التفاصيل
                mergeDetailQuantities(invPurchaseInvoiceDetail, isPurchaseOrSales, invPurchaseInvoice, quantityItemsStoreByDate, invSetup);
            }
        } else {
            // اذا كنت فى فاتورة مبيعات فالامر بيزيد تعقيد
            // لانه بيشوف هل مسموحله يبيع بكميات سالبة ولا لاء
            for (InvPurchaseInvoiceDetail invPurchaseInvoiceDetail : invPurchaseInvoiceDetailsUpdatedList) {
                // لو مسموحلة يبيع بكميات سالبة بيخش يشوف شغلة وينفض للباقى
                if (invSetup.getNegativeSell()) {
                    mergeDetailQuantities(invPurchaseInvoiceDetail, isPurchaseOrSales, invPurchaseInvoice, null, invSetup);
                } else {
                    quantityItemsStore = findQuantityItemNow(invPurchaseInvoice, invPurchaseInvoiceDetail, invSetup);

                    if (quantityItemsStore != null && quantityItemsStore.getQty() != null
                            && quantityItemsStore.getQty().compareTo(invPurchaseInvoiceDetail.getQuantity()) != -1 && !invSetup.getNegativeSell()) {
                        // بيدخل هنا فى حالة لو مش مسموحلة يبيع بكميات سالبة بس بشرط يكون الكمية اكبر من الصفر 
                        if (invPurchaseInvoiceDetail.getId() != null) {
                            InvPurchaseInvoiceDetail oldvalue = invPurchaseInvoiceDetailsService.findInvPurchaseInvoiceDetailsById(invPurchaseInvoiceDetail.getId());
                            quantityItemsStore.setQty(quantityItemsStore.getQty().add(oldvalue.getQuantity()));
                        }
                        mergeDetailQuantities(invPurchaseInvoiceDetail, isPurchaseOrSales, invPurchaseInvoice, null, invSetup);
                    } else {
//                    dao.rollbackQuery();
                        return "Quantity Not Avialable";
                    }

                }

            }
        }

        return null;
    }

    private QuantityItemsStore findQuantityItemNow(InvPurchaseInvoice invPurchaseInvoice, InvPurchaseInvoiceDetail invPurchaseInvoiceDetail, InventorySetup invSetup) {
        QuantityItemsStore quantityItemsStore;
        quantityItemsStore = itemsStoreService.findInventoryItem(invPurchaseInvoice.getInvInventoryId().getId(),
                invPurchaseInvoice.getBranchId().getId(), invPurchaseInvoiceDetail.getItemId().getId(), invSetup.getSellBuy());
        return quantityItemsStore;
    }

    private BigDecimal findQuantityItemByDate(InvPurchaseInvoice invPurchaseInvoice, InvPurchaseInvoiceDetail invPurchaseInvoiceDetail, InventorySetup invSetup) {
        BigDecimal quantityItemsStoreByDate;
        quantityItemsStoreByDate = quantityItemsStoreByDateService.findInventoryItem(invPurchaseInvoice.getInvInventoryId().getId(),
                invPurchaseInvoice.getBranchId().getId(), invPurchaseInvoiceDetail.getItemId().getId(), invSetup.getSellBuy(), invPurchaseInvoice.getDate());
        return quantityItemsStoreByDate;
    }

    @Override
    public BigDecimal findQuantityItemByDate(InvPurchaseInvoice invPurchaseInvoice, InvPurchaseInvoiceDetail invPurchaseInvoiceDetail) {
        BigDecimal quantityItemsStoreByDate;
        quantityItemsStoreByDate = quantityItemsStoreByDateService.findInventoryItemByDate(invPurchaseInvoice.getInvInventoryId().getId(),
                invPurchaseInvoice.getBranchId().getId(), invPurchaseInvoiceDetail.getItemId().getId(), invPurchaseInvoice.getDate());
        return quantityItemsStoreByDate;
    }

    @Override
    public BigDecimal findQuantityItemByDateWithOutInventory(InvPurchaseInvoice invPurchaseInvoice, Boolean sellBuy, InvPurchaseInvoiceDetail invPurchaseInvoiceDetail) {
        BigDecimal quantityItemsStoreByDate;
        quantityItemsStoreByDate = quantityItemsStoreByDateService.getQuantatyItemByinventoryRecalcCostAvarage(null,
                invPurchaseInvoiceDetail.getItemId().getId(), sellBuy, invPurchaseInvoice.getDate());
        return quantityItemsStoreByDate;
    }

    public void mergeDetailQuantities(InvPurchaseInvoiceDetail invPurchaseInvoiceDetail, Integer isPurchaseOrSales,
            InvPurchaseInvoice invPurchaseInvoice, BigDecimal quantityItemsStoreByDate, InventorySetup invSetup) {
        Integer selectedId;
        // انا بجيب هنا رقم التفصيلى اللى فى الفاتورة اللى محمل منها وبردوا بحدد منه اذا كان التفصيلى دا متحمل من فاتورة ولا لاء
        selectedId = invPurchaseInvoiceDetail.getSelectedId();
        // هنا بقى لو كان التفصيلى دا متحمل من فاتورة تانية بخش اعمل تعديل فى الكميات والحالة بتاعته فى فاتورتة الاساسية
        if (selectedId != null) {
            Integer transferFrom = invPurchaseInvoiceDetail.getTransferFrom();
            // 0 -> محمل من فاتورة امر شراء
            if (0 == transferFrom) {
                updatePurchaseOrderStatusAndQuantity(invPurchaseInvoiceDetail);
            } else if (1 == transferFrom) {
                updateAddingOrderStatusAndQuantity(invPurchaseInvoiceDetail);
            } else if (2 == transferFrom) {
                updateQutationStatusAndQuantity(invPurchaseInvoiceDetail);
            } else if (3 == transferFrom) {
                updateReservationStatusAndQuantity(invPurchaseInvoiceDetail);
            } else if (4 == transferFrom) {
                updateTransferStatusAndQuantity(invPurchaseInvoiceDetail);
            }
        }
        // فى الاخر بحفظ التفصيلى
        saveDetail(invPurchaseInvoiceDetail, invPurchaseInvoice, isPurchaseOrSales, quantityItemsStoreByDate, invSetup);
    }

    public void updatePurchaseOrderStatusAndQuantity(InvPurchaseInvoiceDetail invPurchaseInvoiceDetail) {
        InvPurchaseOrderDetail purchaseOrderDetail;

        BigDecimal purchaseOrderFinalQuantity;
        BigDecimal finalQuantity;
        Integer res;
        BigDecimal quantityInvoiceOld = null;
        // بجيب السطر اللى هشتغل عليه من جدول امر الشراء
        purchaseOrderDetail = invPurchaseOrderDetailsService.findInvPurchaseOrderDetailById(invPurchaseInvoiceDetail.getSelectedId());
        // بجيب الكمية المتبقية من الصنف دا علشان اشتغل عليها
        purchaseOrderFinalQuantity = purchaseOrderDetail.getFinalQuantity() != null
                ? purchaseOrderDetail.getFinalQuantity() : BigDecimal.ZERO;
        // هنا بقى بجيب الكمية القديمة اللى متسجله فى قاعدة البايات يعنى قبل اما اعدل فيها 
        if (invPurchaseInvoiceDetail.getId() != null) {
            InvPurchaseInvoiceDetail invPurchaseInvoiceDetailOld = invPurchaseInvoiceDetailsService.findInvPurchaseInvoiceDetailsById(invPurchaseInvoiceDetail.getId());
            quantityInvoiceOld = invPurchaseInvoiceDetailOld.getQuantity();
        }
        // المعادلة دي بتجيب الكمية النهائية المتبقية علشان احطها فى الجدول الاساسي
        finalQuantity = calculateFinalQuantity(invPurchaseInvoiceDetail, purchaseOrderFinalQuantity, quantityInvoiceOld);
        // هندخل على مرحلة تعديل حالة التفصيلى
        // هنشوف هل لسه فيه كمية متبقية فى التفصيلى ولا لسه 
        res = finalQuantity.compareTo(BigDecimal.ZERO);
        //لو لسه فى كمية متبقية فى التفصيلى هنحطها وهنخلى الحالة بواحد
        if (res == 1) {
            purchaseOrderDetail.setFinalQuantity(finalQuantity);
            purchaseOrderDetail.setStatus(1);
        } else {
            // لو الكمية صفر او اقل هنخلى الحالة باثنين
            purchaseOrderDetail.setFinalQuantity(BigDecimal.ZERO);
            purchaseOrderDetail.setStatus(2);
        }
        // هنحفظ التفصيلى
        invPurchaseOrderDetailsService.updatePurchaseOrderDetails(purchaseOrderDetail);
    }

    public void updateAddingOrderStatusAndQuantity(InvPurchaseInvoiceDetail invPurchaseInvoiceDetail) {
        InvAddingorderDetail addingOrderDetail;
        BigDecimal addingOrderFinalQuantity;
        BigDecimal finalQuantity;
        Integer res;
        BigDecimal quantityInvoiceOld = null;

        addingOrderDetail = invAddingOrderDetailsService.findInvAddingOrderDetailsById(invPurchaseInvoiceDetail.getSelectedId());
        addingOrderFinalQuantity = addingOrderDetail.getFinalQuantity() != null
                ? addingOrderDetail.getFinalQuantity() : BigDecimal.ZERO;

        if (invPurchaseInvoiceDetail.getId() != null) {
            InvPurchaseInvoiceDetail invPurchaseInvoiceDetailOld = invPurchaseInvoiceDetailsService.findInvPurchaseInvoiceDetailsById(invPurchaseInvoiceDetail.getId());
            quantityInvoiceOld = invPurchaseInvoiceDetailOld.getQuantity();
        }
        finalQuantity = calculateFinalQuantity(invPurchaseInvoiceDetail, addingOrderFinalQuantity, quantityInvoiceOld);

        res = finalQuantity.compareTo(BigDecimal.ZERO);
        if (res == 1) {
            addingOrderDetail.setFinalQuantity(finalQuantity);
            addingOrderDetail.setStatus(1);
        } else {
            addingOrderDetail.setFinalQuantity(BigDecimal.ZERO);
            addingOrderDetail.setStatus(2);
        }
        invAddingOrderDetailsService.updateInvAddingOrderDetails(addingOrderDetail);
    }

    public void updateReservationStatusAndQuantity(InvPurchaseInvoiceDetail invPurchaseInvoiceDetail) {
        InvReservationDetail reservationDetail;
        BigDecimal reservationFinalQuantity;
        BigDecimal finalQuantity;
        Integer res;
        BigDecimal quantityInvoiceOld = null;

        reservationDetail = invReservationDetailService.findInvReservationDetailById(invPurchaseInvoiceDetail.getSelectedId());
        reservationFinalQuantity = reservationDetail.getFinalQuantity() != null
                ? reservationDetail.getFinalQuantity() : BigDecimal.ZERO;

        if (invPurchaseInvoiceDetail.getId() != null) {
            InvPurchaseInvoiceDetail invPurchaseInvoiceDetailOld = invPurchaseInvoiceDetailsService.findInvPurchaseInvoiceDetailsById(invPurchaseInvoiceDetail.getId());
            quantityInvoiceOld = invPurchaseInvoiceDetailOld.getQuantity();
        }
        finalQuantity = calculateFinalQuantity(invPurchaseInvoiceDetail, reservationFinalQuantity, quantityInvoiceOld);

        res = finalQuantity.compareTo(BigDecimal.ZERO);
        if (res == 1) {
            reservationDetail.setFinalQuantity(finalQuantity);
            reservationDetail.setStatus(1);
        } else {
            reservationDetail.setFinalQuantity(BigDecimal.ZERO);
            reservationDetail.setStatus(2);
        }
        invReservationDetailService.updateInvReservationDetails(reservationDetail);
    }

    public void updateTransferStatusAndQuantity(InvPurchaseInvoiceDetail invPurchaseInvoiceDetail) {
        InvTransferDetail transferDetail;
        BigDecimal transferFinalQuantity;
        BigDecimal finalQuantity;
        Integer res;
        BigDecimal quantityInvoiceOld = null;

        transferDetail = invTransferDetailService.findInvTransferDetailsById(invPurchaseInvoiceDetail.getSelectedId());
        transferFinalQuantity = transferDetail.getFinalQuantity() != null
                ? transferDetail.getFinalQuantity() : BigDecimal.ZERO;

        if (invPurchaseInvoiceDetail.getId() != null) {
            InvPurchaseInvoiceDetail invPurchaseInvoiceDetailOld = invPurchaseInvoiceDetailsService.findInvPurchaseInvoiceDetailsById(invPurchaseInvoiceDetail.getId());
            quantityInvoiceOld = invPurchaseInvoiceDetailOld.getQuantity();
        }
        finalQuantity = calculateFinalQuantity(invPurchaseInvoiceDetail, transferFinalQuantity, quantityInvoiceOld);

        res = finalQuantity.compareTo(BigDecimal.ZERO);
        if (res == 1) {
            transferDetail.setFinalQuantity(finalQuantity);
            transferDetail.setStatus(1);
        } else {
            transferDetail.setFinalQuantity(BigDecimal.ZERO);
            transferDetail.setStatus(2);
        }
        invTransferDetailService.updateInvTransferDetails(transferDetail);
    }

    public void updateQutationStatusAndQuantity(InvPurchaseInvoiceDetail invPurchaseInvoiceDetail) {
        InvQotationDetail qotationDetail;
        BigDecimal qutaionFinalQuantity;
        BigDecimal finalQuantity;
        Integer res;
        BigDecimal quantityInvoiceOld = null;

        qotationDetail = invQuotationDetailsService.findInvQotationDetailById(invPurchaseInvoiceDetail.getSelectedId());
        qutaionFinalQuantity = qotationDetail.getFinalQuantity() != null
                ? qotationDetail.getFinalQuantity() : BigDecimal.ZERO;

        if (invPurchaseInvoiceDetail.getId() != null) {
            InvPurchaseInvoiceDetail invPurchaseInvoiceDetailOld = invPurchaseInvoiceDetailsService.findInvPurchaseInvoiceDetailsById(invPurchaseInvoiceDetail.getId());
            quantityInvoiceOld = invPurchaseInvoiceDetailOld.getQuantity();
        }
        finalQuantity = calculateFinalQuantity(invPurchaseInvoiceDetail, qutaionFinalQuantity, quantityInvoiceOld);

        res = finalQuantity.compareTo(BigDecimal.ZERO);
        if (res == 1) {
            qotationDetail.setFinalQuantity(finalQuantity);
            qotationDetail.setStatus(1);
        } else {
            qotationDetail.setFinalQuantity(BigDecimal.ZERO);
            qotationDetail.setStatus(2);
        }
        invQuotationDetailsService.updateInvQotationDetails(qotationDetail);
    }

    public BigDecimal calculateFinalQuantity(InvPurchaseInvoiceDetail invPurchaseInvoiceDetail,
            BigDecimal existQuantitiyFromDataBaseSource, BigDecimal quantityInvoiceOld) {
        BigDecimal finalQuantity;
        // بتأكد الاول هل السطر دا متعدل ولا لسه جديد
        if (invPurchaseInvoiceDetail.getId() == null) {
            // لو كان لسه جديد فكل اللى هعمله انى هطرح الكمية اللى داخلة فى التفصيلى من اللى فى قاعدة البيانات اللى فى جدول امر الشرا
            finalQuantity = existQuantitiyFromDataBaseSource.subtract(invPurchaseInvoiceDetail.getQuantity());
        } else {
            // اما لو كان تعديل فهطرح الكمية اللى مكتوبه فى التفصيلى من الكمية القديمة اللى فى جدول المشتريات 
            // وبعدين الناتج هطرحه من الكمية اللى موجدة فى جدول امر الشرا
            finalQuantity = existQuantitiyFromDataBaseSource.subtract(invPurchaseInvoiceDetail.getQuantity().subtract(quantityInvoiceOld));
        }
        return finalQuantity;
    }

    @Override
    public synchronized Integer getMaxSerialForInvoiceDetail(InvPurchaseInvoice invPurchaseInvoiceId) {
        Map<String, Object> params = new HashMap<>();
        params.put("invPurchaseInvoiceId", invPurchaseInvoiceId.getId());

        Integer serial = dao.findEntityByQuery("SELECT max(e.serial) FROM InvPurchaseInvoiceDetail e WHERE e.invPurchaseInvoiceId.id =:invPurchaseInvoiceId ", params);

        if (serial == null) {
            return 1;
        } else {
            serial = serial + 1;
            return serial;
        }
    }

    @Override
    public synchronized Integer getMaxSerialTaxForInvoice(Integer branchId, Integer inventoryId, Integer type, Boolean taxFalgFinal) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);

        StringBuilder query = new StringBuilder();
        query.append("SELECT max(e.serialTax) FROM InvPurchaseInvoice e WHERE e.branchId.id =:branchId");

        if (type != null) {
            params.put("type", type);
            query.append(" AND e.type =:type");
        }

        if (inventoryId != null) {
            params.put("inventoryId", inventoryId);
            query.append(" AND e.invInventoryId.id =:inventoryId");
        }

        if (taxFalgFinal) {
            query.append(" AND e.taxFlagFinal = 1 ");
        } else {
            query.append(" AND e.taxFlagFinal = 0 ");
        }

        Integer serial = dao.findEntityByQuery(query.toString(), params);

        if (serial == null) {
            return 1;
        } else {
            return serial + 1;
        }
    }

    @Override
    public void findmofakroun() {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> params2 = new HashMap<>();
        params2.put("branchId", 165);
        params2.put("type", Boolean.TRUE);
        List<InvPurchaseReturnSave> invPurchaseReturnSaves = new ArrayList<>();
        List<InvPurchaseInvoice> headdetails = dao.findListByQuery("SELECT e FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId and e.type = :type and e.invInventoryId.id not in (23)", params2);
        for (InvPurchaseInvoice x : headdetails) {
            InvPurchaseReturnSave invPurchaseReturnSave = new InvPurchaseReturnSave();
            invPurchaseReturnSave.setInvPurchaseInvoice(x);
            params.put("invPurchaseInvoiceId", x.getId());
            List<InvPurchaseInvoiceDetail> details = dao.findListByQuery("SELECT e FROM InvPurchaseInvoiceDetail e WHERE e.invPurchaseInvoiceId.id = :invPurchaseInvoiceId", params);
            invPurchaseReturnSave.setInvPurchaseInvoiceDetailList(details);
            invPurchaseReturnSaves.add(invPurchaseReturnSave);
        }
        for (InvPurchaseReturnSave x : invPurchaseReturnSaves) {
            InvPurchaseInvoice y = x.getInvPurchaseInvoice();
            InvTransfer invTransfer = new InvTransfer();
            invTransfer.setBranchId(y.getBranchId());
            invTransfer.setCompanyId(y.getCompanyId());
            invTransfer.setCreatedBy(y.getCreatedBy());
            invTransfer.setModifiedBy(y.getModifiedBy());
            invTransfer.setCreationDate(y.getCreationDate());
            invTransfer.setModifiedBy(y.getModifiedBy());
            invTransfer.setSerialNo(y.getSerial());
            invTransfer.setTransferDate(y.getDate());
            invTransfer.setInvTo(y.getInvInventoryId());
            InvInventory invFrom = new InvInventory();
            invFrom.setId(23);
            invTransfer.setInvFrom(invFrom);
            invTransfer.setRemarks(y.getRemarks());
            invTransfer.setTransferType(0);
            invTransfer.setStatus(0);
            invTransfer = invTransferService.addInvTransferMofakroun(invTransfer);
            for (InvPurchaseInvoiceDetail z : x.getInvPurchaseInvoiceDetailList()) {
                InvTransferDetail invTransferDetail = new InvTransferDetail();
                invTransferDetail.setCompanyId(z.getCompanyId());
                invTransferDetail.setCreatedBy(z.getCreatedBy());
                invTransferDetail.setModifiedBy(z.getModifiedBy());
                invTransferDetail.setCreationDate(z.getCreationDate());
                invTransferDetail.setModificationDate(z.getModificationDate());
                invTransferDetail.setInvItemId(z.getItemId());
                invTransferDetail.setAmount(z.getQuantity());
                invTransferDetail.setInvTransferId(invTransfer);
                invTransferDetail.setScrewing(z.getScrewing());
                invTransferDetail.setItemBarcode(z.getItemBarcode());
                invTransferDetail.setUnitId(z.getUnitId());
                invTransferDetail.setStatus(0);
                invTransferDetail.setFinalQuantity(z.getQuantity());
                invTransferDetailService.addInvTransferDetails(invTransferDetail);
            }
        }
    }

    @Override
    public Long getTotalRegistors(Integer type, Integer inventoryId, Integer branchId, Map<String, Object> filters) {
        StringBuilder builder = new StringBuilder();
        filters.put("type", type);
        filters.put("branchId", branchId);
        filters.put("inventoryId", inventoryId);
        builder.append("SELECT count(s) FROM InvPurchaseInvoice s WHERE   s.branchId.id = :branchId AND s.type =:type AND (:inventoryId is Null OR  s.invInventoryId.id = :inventoryId)");
        appendFilter(filters, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);
        return details == null ? 0 : details;
    }

    private void appendFilter(Map<String, Object> filters, StringBuilder builder) {
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and ( FUNCTION('DATE_FORMAT',s.date, '%d-%m-%Y')  like CONCAT('%',:globalFilter ,'%') OR  s.serial like CONCAT('%',:globalFilter ,'%')  OR s.invInventoryId.name  like CONCAT('%',:globalFilter ,'%') OR s.organizationSiteId.name like CONCAT('%',:globalFilter ,'%') OR  s.invDelegatorId.name like CONCAT('%',:globalFilter ,'%') OR s.paymentType  like CONCAT('%',:globalFilter ,'%') OR s.createdBy.name like CONCAT('%',:globalFilter ,'%') OR s.remarks like CONCAT('%',:globalFilter ,'%'))";
            builder.append(filter);
        } else {
            filters.remove("globalFilter");
        }
    }

    @Override
    public List<InvInvoiceDTO> getALLInvPurchaseInvoiceDTOByTypeAndBranchId(Integer type, Integer inventoryId, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters) {
        filters.put("type", type);
        filters.put("branchId", branchId);
        filters.put("inventoryId", inventoryId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT s FROM InvPurchaseInvoice s WHERE  s.branchId.id = :branchId AND s.type =:type AND  (:inventoryId is Null OR  s.invInventoryId.id = :inventoryId) ");
        appendFilter(filters, builder);
        builder.append(" order by s.serial DESC");
        List<InvPurchaseInvoice> details = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        return returnListDTOForList(details);
    }

    private List<InvInvoiceDTO> returnListDTOForList(List<InvPurchaseInvoice> invPurchaseInvoiceList) {
        List<InvInvoiceDTO> invInvoiceDTOList = new ArrayList<>();
        for (InvPurchaseInvoice invPurchaseInvoice : invPurchaseInvoiceList) {
            InvInvoiceDTO invInvoiceDTO = new InvInvoiceDTO();
            invInvoiceDTO = initDataForList(invInvoiceDTO, invPurchaseInvoice);
            invInvoiceDTOList.add(invInvoiceDTO);
        }
        return invInvoiceDTOList;
    }

    private InvInvoiceDTO initDataForList(InvInvoiceDTO invInvoiceDTO, InvPurchaseInvoice invPurchaseInvoice) {
        commonData(invInvoiceDTO, invPurchaseInvoice);
        return invInvoiceDTO;
    }

    private void commonData(InvInvoiceDTO invPurchaseInvoiceDTO, InvPurchaseInvoice entity) {
        invPurchaseInvoiceDTO.setId(entity.getId());
        invPurchaseInvoiceDTO.setSerial(entity.getSerial());
        invPurchaseInvoiceDTO.setDate(entity.getDate());
        invPurchaseInvoiceDTO.setIndex(entity.getSerial());
        invPurchaseInvoiceDTO.setPostFlag(entity.getPostFlag());
        invPurchaseInvoiceDTO.setRemarks(entity.getRemarks());
        if (entity.getOrganizationSiteId() != null) {
            invPurchaseInvoiceDTO.setOrganizationSiteName(entity.getOrganizationSiteId().getName());
        }
        if (entity.getInvInventoryId() != null) {
            InvInventoryDTO invInventoryDTO = new InvInventoryDTO();
            invInventoryDTO.setName(entity.getInvInventoryId().getName());
            invPurchaseInvoiceDTO.setInvInventoryId(invInventoryDTO);
        }
        if (entity.getInvDelegatorId() != null) {
            invPurchaseInvoiceDTO.setInvDelegatorName(entity.getInvDelegatorId().getName());
        }

        invPurchaseInvoiceDTO.setPaymentType(entity.getPaymentType());

        if (entity.getCreatedBy() != null) {
            invPurchaseInvoiceDTO.setCreatedByName(entity.getCreatedBy().getName());
        }
    }
}
