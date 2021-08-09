/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.api.dto.DistributionApiData;
import com.isag.api.dto.DistributionApiSearch;
import com.isag.businessservice.reports.SupplierAccountService;
import com.isag.businessservice.reports.searchBean.SubAccountSummarySearchBean;
import com.isag.bussinessservice.global.InvPurchaseReturnSave;
import com.isag.bussinessservice.global.Utility;
import com.isag.core.GenericDAO;
import com.isag.core.UserDataDTO;
import com.isag.define.CompanyActivityClassEnum;
import com.isag.define.ScreenNameClassEnum;
import com.isag.dto.CostCenterDTO;
import com.isag.dto.CurrencyDTO;
import com.isag.dto.CurrencyOperationDTO;
import com.isag.dto.GlAdminUnitDTO;
import com.isag.dto.GlBankDTO;
import com.isag.dto.InvInventoryDTO;
import com.isag.dto.InvOrganizationSiteDTO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.dto.InvDetailDTO;
import com.isag.dto.InventoryDelegatorDTO;
import com.isag.entity.Branch;
import com.isag.entity.CostCenter;
import com.isag.entity.Currency;
import com.isag.entity.GlAccount;
import com.isag.entity.GlAdminUnit;
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
import com.isag.entity.InventoryDelegator;
import com.isag.entity.InventorySetup;
import com.isag.entity.IsagCompany;
import com.isag.entity.IsagUser;
import com.isag.views.QuantityItemsStore;
import com.isag.views.SupplierAccountViewBean;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author WIN7
 */
@Stateless
public class InvPurchaseInvoiceServiceImpl_1 implements InvPurchaseInvoiceService_1 {

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
    @EJB
    private CurrencyOperationService currencyOperationService;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private CurrencyService currencyService;
    @EJB
    private InvGlobalService invGlobalService;
    @EJB
    SupplierAccountService supplierAccountService;

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
            Integer x = detail.getInvPurchaseInvoiceId().getType();
            if (x == 0) {
                x = 1;
            } else if (x == 1) {
                x = 0;
            }
            saveDetail(detail, detail.getInvPurchaseInvoiceId(), x, quantityItemsStoreByDate, invSetup);
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
            BigDecimal costAvarage = (oldValue.add(newValue)).divide(allAmount, 2, RoundingMode.HALF_UP);
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
    public List<InvPurchaseInvoice> getALLInvPurchaseInvoicePostFlagedByBranchId(Integer branchId, Boolean type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        List<InvPurchaseInvoice> invPurchaseInvoiceList = dao.findListByQuery("SELECT e FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId AND e.postFlag != 1 AND e.type =:type", params);
        return invPurchaseInvoiceList;
    }

    @Override
    public List<InvPurchaseInvoice> getALLInvPurchaseInvoicePostFlagedByBranchIdPer(Integer branchId, Boolean type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("type", type);
        List<InvPurchaseInvoice> invPurchaseInvoiceList = dao.findListByQuery("SELECT e FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId AND e.postFlag != 1 AND e.type =:type", params);
        List<InvPurchaseInvoice> list = retriveInvPurchaseInvoiceDate(invPurchaseInvoiceList);
        return list;
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
    public InvInvoiceDTO findInvPurchaseInvoiceById(InvInvoiceDTO invPurchaseInvoiceDto, IsagUser isagUser) {
        InvPurchaseInvoice invPurchaseInvoice = dao.findEntityById(InvPurchaseInvoice.class, invPurchaseInvoiceDto.getId());
        InvInvoiceDTO invPurchaseInvoiceDTO = mapToDTO(invPurchaseInvoice, invPurchaseInvoiceDto, false);
        invPurchaseInvoiceDTO = invGlobalService.updateSummition(invPurchaseInvoiceDTO);
        return invPurchaseInvoiceDTO;
    }

    @Override
    public InvInvoiceDTO findInvPurchaseInvoiceByTableId(InvInvoiceDTO invPurchaseInvoiceDto, Integer branchId) {
        InvPurchaseInvoice invPurchaseInvoice = null;
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("tableId", invPurchaseInvoiceDto.getTableNumber());
        List<InvPurchaseInvoice> invPurchaseInvoiceList = dao.findListByQuery("SELECT e FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId AND e.type = 1 AND e.tableNumber = :tableId AND (e.closeTable is null )", params);
        if (invPurchaseInvoiceList != null && !invPurchaseInvoiceList.isEmpty()) {
            invPurchaseInvoice = invPurchaseInvoiceList.get(0);
            InvInvoiceDTO invPurchaseInvoiceDTO = mapToDTO(invPurchaseInvoice, invPurchaseInvoiceDto, false);
            invPurchaseInvoiceDTO = invGlobalService.updateSummition(invPurchaseInvoiceDTO);
            return invPurchaseInvoiceDTO;
        }
        return null;
    }

    @Override
    public InvInvoiceDTO findInvPurchaseInvoiceBySerial(InvInvoiceDTO invInvoiceDTO, Integer branchId) {
        InvPurchaseInvoice invPurchaseInvoice = null;
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("serial", invInvoiceDTO.getLoadedSerial());
        params.put("invInventory", invInvoiceDTO.getInvInventoryId().getId());
        List<InvPurchaseInvoice> invPurchaseInvoiceList = dao.findListByQuery("SELECT e FROM InvPurchaseInvoice e WHERE e.invInventoryId.id = :invInventory AND e.branchId.id = :branchId AND e.type = 0 AND e.serial = :serial ", params);
        if (invPurchaseInvoiceList != null && !invPurchaseInvoiceList.isEmpty()) {
            invPurchaseInvoice = invPurchaseInvoiceList.get(0);
            InvInvoiceDTO invPurchaseInvoiceDTO = mapToDTO(invPurchaseInvoice, invInvoiceDTO, false);
            return invPurchaseInvoiceDTO;
        }
        return null;
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
    public InvInvoiceDTO getInvPurchaseInvoiceDTOBySupplierInvoiceNumber(InvInvoiceDTO invInvoiceDTO, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("supplierInvoiceNumber", invInvoiceDTO.getSupplierInvoiceNumber());
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT s FROM InvPurchaseInvoice s WHERE  s.branchId.id =:branchId AND s.supplierInvoiceNumber = :supplierInvoiceNumber AND s.type = 0 ");
        List<InvPurchaseInvoice> details = dao.findListByQuery(builder.toString(), params);

        if (details != null && !details.isEmpty()) {
            invInvoiceDTO.getInvDailogeDataSelected().setId(details.get(0).getId());
            invInvoiceDTO.setIsEdit(Boolean.TRUE);
            invInvoiceDTO = convertInvPurchaseInvoiceToPurchaseInvoice(invInvoiceDTO, branchId);
            invInvoiceDTO.setOrganizationSiteId(null);
            invInvoiceDTO.setIsEdit(Boolean.TRUE);
            invInvoiceDTO.setType(1);
            return invInvoiceDTO;
        } else {
            return null;
        }
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

    private InvInvoiceDTO mapToDTO(InvPurchaseInvoice entity, Boolean isLoaded) {

        InvInvoiceDTO invPurchaseInvoiceDTO = new InvInvoiceDTO();

        initData(invPurchaseInvoiceDTO, entity, isLoaded);

        return invPurchaseInvoiceDTO;
    }

    @Override
    public InvInvoiceDTO updateRate(InvInvoiceDTO invPurchaseInvoiceDTO) {
        try {
            CurrencyOperationDTO currencyOperation = currencyOperationService.getRatesByDatesDTO(invPurchaseInvoiceDTO.getCurrencyId().getId(), invPurchaseInvoiceDTO.getDate(), invPurchaseInvoiceDTO.getCompanyId());
            invPurchaseInvoiceDTO.setRate(currencyOperation == null ? BigDecimal.ONE : currencyOperation.getRate());
        } catch (Exception e) {
//            saveError(e, "InvPurchaseInvoiceFormMB", "updateRate");
        }
        return invPurchaseInvoiceDTO;
    }

    @Override
    public String validateloginDistributedScreens(UserDataDTO userDataDTO, ScreenNameClassEnum screenNameClassEnum) {
        StringBuilder error = new StringBuilder();
        List<InvInventoryDTO> invInventoryDTOList = userDataDTO.getInventoryDTOList();
        if (invInventoryDTOList == null || invInventoryDTOList.isEmpty()) {
            error.append("\n يجب اعطاء صلاحية للتعامل مع المخازن");
        }

        List<InvOrganizationSiteDTO> organizationSiteList = null;
        if (ScreenNameClassEnum.INVPURCHASEINVOICEFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVRETURNPURCHASEFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVADDINGORDERFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVPURCHASEORDERFORM.equals(screenNameClassEnum)) {
            organizationSiteList = organizationSiteService.getorganizationSiteByBranchIdDTO(userDataDTO.getBranchId(), true, 1);
        } else if (ScreenNameClassEnum.INVSALESINVOICEFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVPOSINVOICEFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVRETURNSALESFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVRESERVATIONFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVPERMISSIONORDERFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVQUOTATIONFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVSALESORDERFORM.equals(screenNameClassEnum)) {
            organizationSiteList = organizationSiteService.getorganizationSiteByBranchIdDTO(userDataDTO.getBranchId(), true, 0);
        }

        if (!ScreenNameClassEnum.INVTRANSFERINVENTORY.equals(screenNameClassEnum)
                && !ScreenNameClassEnum.INVSTRIP.equals(screenNameClassEnum)
                && !ScreenNameClassEnum.INVUPDATE.equals(screenNameClassEnum)
                && !ScreenNameClassEnum.INVPOSREST.equals(screenNameClassEnum)
                && !ScreenNameClassEnum.INVOPENINGBALANCEITEMFORM.equals(screenNameClassEnum)) {
            if (organizationSiteList == null || organizationSiteList.isEmpty()) {
                error.append("\n يجب انشاء موردين / عملاء ");
            }

            List<CurrencyDTO> currencyDTOList = currencyService.getAllCurrenciesWithLocalCurrencyHavingRatesByCompanyIdDTO(userDataDTO.getCompanyId()); // العملات
            if (currencyDTOList == null || currencyDTOList.isEmpty()) {
                error.append("\n يجب انشاء عملة للتعامل بها");
            }

            List<GlBankDTO> glBankList = userDataDTO.getGlBankDTOList();
            if (glBankList == null || glBankList.isEmpty()) {
                error.append("\n يجب اعطاء صلاحية للتعامل مع الخزائن");
            }
        }
        return error.toString();
    }

    public InvInvoiceDTO deleteInvPurchaseInvoiceDetail(InvInvoiceDTO invPurchaseInvoiceDTO) {
        try {
            if (invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailsSelected().getId() != null) {
                invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailsDeletedList().add(invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailsSelected());
            }
            invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList().remove(invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailsSelected());
            invPurchaseInvoiceDTO = invGlobalService.updateSummition(invPurchaseInvoiceDTO);
        } catch (Exception e) {
//            saveError(e, "InvPurchaseInvoiceFormMB", "deleteInvPurchaseInvoiceDetail");
        }
        return invPurchaseInvoiceDTO;
    }

    @Override
    public InvInvoiceDTO putCurrency(InvInvoiceDTO invPurchaseInvoiceDTO) {
        try {
            if (invPurchaseInvoiceDTO.getOrganizationSiteId() != null && invPurchaseInvoiceDTO.getOrganizationSiteId().getCurrencyId() != null) {
                invPurchaseInvoiceDTO.setCurrencyId(invPurchaseInvoiceDTO.getOrganizationSiteId().getCurrencyId());
                invPurchaseInvoiceDTO = updateRate(invPurchaseInvoiceDTO);

                invPurchaseInvoiceDTO = invGlobalService.updateSummition(invPurchaseInvoiceDTO);
            } else {
                invPurchaseInvoiceDTO.setMsg("هذا المورد ليس له عملة!");
            }
        } catch (Exception e) {
            invPurchaseInvoiceDTO.setMsg(e.toString());
        }
        return invPurchaseInvoiceDTO;
    }

    //mapToEntity
    private InvPurchaseInvoice mapToEntity(InvInvoiceDTO invInvoiceDTO, IsagUser isagUser) {

        InvPurchaseInvoice invPurchaseInvoice = new InvPurchaseInvoice();

        if (invInvoiceDTO.getInvInventoryId() != null) {
            InvInventory inventory = new InvInventory();
            inventory.setId(invInvoiceDTO.getInvInventoryId().getId());
            invPurchaseInvoice.setInvInventoryId(inventory);
        }

        if (invInvoiceDTO.getAdminUnitId() != null) {
            GlAdminUnit glAdminUnit = new GlAdminUnit();
            glAdminUnit.setId(invInvoiceDTO.getAdminUnitId().getId());
            invPurchaseInvoice.setAdminUnitId(glAdminUnit);
        }
        if (invInvoiceDTO.getCostCenterId() != null) {
            CostCenter costCenter = new CostCenter();
            costCenter.setId(invInvoiceDTO.getCostCenterId().getId());
            invPurchaseInvoice.setCostCenterId(costCenter);
        }
        if (invInvoiceDTO.getCurrencyId() != null) {
            Currency currency = new Currency();
            currency.setId(invInvoiceDTO.getCurrencyId().getId());
            invPurchaseInvoice.setCurrencyId(currency);
        }

        if (invInvoiceDTO.getOrganizationSiteId() != null) {
            InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
            invOrganizationSite.setId(invInvoiceDTO.getOrganizationSiteId().getId());
            invOrganizationSite.setName(invInvoiceDTO.getOrganizationSiteId().getName());
            invPurchaseInvoice.setOrganizationSiteId(invOrganizationSite);
        }

        if (invInvoiceDTO.getAccountId() != null) {
            GlAccount glAccount = new GlAccount();
            glAccount.setId(invInvoiceDTO.getAccountId());
            invPurchaseInvoice.setAccountId(glAccount);
        }

        if (invInvoiceDTO.getGlBankId() != null) {
            GlBank glBank = new GlBank();
            glBank.setId(invInvoiceDTO.getGlBankId().getId());
            invPurchaseInvoice.setGlBankId(glBank);
        }
        if (invInvoiceDTO.getInvDelegatorId() != null) {
            InventoryDelegator inventoryDelegator = new InventoryDelegator();
            inventoryDelegator.setId(invInvoiceDTO.getInvDelegatorId().getId());
            invPurchaseInvoice.setInvDelegatorId(inventoryDelegator);
        }

        if (invInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVPURCHASEINVOICEFORM)) {
            invPurchaseInvoice.setType(0);
            if (invInvoiceDTO.getInvAddingOrderId() != null) {
                InvAddingorder invAddingorder = new InvAddingorder();
                invAddingorder.setId(invInvoiceDTO.getInvAddingOrderId());
                invPurchaseInvoice.setInvAddingOrderId(invAddingorder);
            } else if (invInvoiceDTO.getInvPurchaseOrderId() != null) {
                InvPurchaseOrder invPurchaseOrder = new InvPurchaseOrder();
                invPurchaseOrder.setId(invInvoiceDTO.getInvPurchaseOrderId());
                invPurchaseInvoice.setInvPurchaseOrderId(invPurchaseOrder);
            }
        } else if (invInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVPOSINVOICEFORM) || invInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVPOSREST)) {
            invPurchaseInvoice.setType(1);
            invPurchaseInvoice.setPaymentType(3);
        } else if (invInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVSALESINVOICEFORM)) {
            invPurchaseInvoice.setType(1);
            if (invInvoiceDTO.getInvQotationId() != null) {
                InvQotation invQotation = new InvQotation();
                invQotation.setId(invInvoiceDTO.getInvQotationId());
                invPurchaseInvoice.setInvQotationId(invQotation);
            }
        } else if (invInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVRETURNPURCHASEFORM)) {
            invPurchaseInvoice.setType(2);
            if (invInvoiceDTO.getInvPurchaseInvoiceId() != null) {
                InvPurchaseInvoice purchaseInvoice = new InvPurchaseInvoice();
                purchaseInvoice.setId(invInvoiceDTO.getInvPurchaseInvoiceId());
                invPurchaseInvoice.setParent(purchaseInvoice);
            }
        } else if (invInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVRETURNSALESFORM)) {
            invPurchaseInvoice.setType(3);
            if (invInvoiceDTO.getInvPurchaseInvoiceId() != null) {
                InvPurchaseInvoice purchaseInvoice = new InvPurchaseInvoice();
                purchaseInvoice.setId(invInvoiceDTO.getInvPurchaseInvoiceId());
                invPurchaseInvoice.setParent(purchaseInvoice);
            }
        }

        invPurchaseInvoice.setTotalNolon(invInvoiceDTO.getTotalNolon());
        invPurchaseInvoice.setIsDone(invInvoiceDTO.getIsDone());
        invPurchaseInvoice.setLook(invInvoiceDTO.getLook());
        invPurchaseInvoice.setRevision(invInvoiceDTO.getRevision() ? 1 : 0);
        invPurchaseInvoice.setTypeActivity(invInvoiceDTO.getTypeActivity());
        invPurchaseInvoice.setId(invInvoiceDTO.getId());
        invPurchaseInvoice.setSerial(invInvoiceDTO.getSerial());
        invPurchaseInvoice.setSerialTax(invInvoiceDTO.getSerialTax());
        invPurchaseInvoice.setDate(invInvoiceDTO.getDate());
        invPurchaseInvoice.setDiscount(invInvoiceDTO.getDiscount());
        invPurchaseInvoice.setDiscountType(invInvoiceDTO.getDiscountType());
        invPurchaseInvoice.setDueDate(invInvoiceDTO.getDueDate());
        invPurchaseInvoice.setRemarks(invInvoiceDTO.getRemarks());
        invPurchaseInvoice.setPostFlag(0);
        invPurchaseInvoice.setPaymentType(invInvoiceDTO.getPaymentType() > 0 ? invInvoiceDTO.getPaymentType() : 0);
        invPurchaseInvoice.setTaxflag(invInvoiceDTO.getTaxflag());
        invPurchaseInvoice.setRate(invInvoiceDTO.getRate() != null ? invInvoiceDTO.getRate() : null);
        invPurchaseInvoice.setSupplierInvoiceNumber(invInvoiceDTO.getSupplierInvoiceNumber());
        invPurchaseInvoice.setSupplierInvoiceDate(invInvoiceDTO.getSupplierInvoiceDate());
        invPurchaseInvoice.setExtraCost(invInvoiceDTO.getExtraCost() != null ? invInvoiceDTO.getExtraCost() : BigDecimal.ZERO);
        invPurchaseInvoice.setTaxdiscflag(invInvoiceDTO.getTaxdiscflag());
        invPurchaseInvoice.setTaxdiscvalue(invInvoiceDTO.getTaxdiscvalue());
        invPurchaseInvoice.setTaxFlagFinal(invInvoiceDTO.getTaxFlagFinal());
        invPurchaseInvoice.setPayCash(invInvoiceDTO.getFinalNet().subtract(invInvoiceDTO.getPayNet()));
        invPurchaseInvoice.setPayNet(invInvoiceDTO.getPayNet());
        invPurchaseInvoice.setTypeRest(invInvoiceDTO.getTypeRest());
        invPurchaseInvoice.setTableNumber(invInvoiceDTO.getTableNumber());
        invPurchaseInvoice.setCloseTable(invInvoiceDTO.getCloseTable());

        if (invInvoiceDTO.getCreatedBy() != null) {
            IsagUser user = new IsagUser();
            user.setId(invInvoiceDTO.getCreatedBy());
            invPurchaseInvoice.setCreatedBy(user);
            invPurchaseInvoice.setCreationDate(invInvoiceDTO.getCreatedDate());
            invPurchaseInvoice.setModifiedBy(isagUser);
            invPurchaseInvoice.setModificationDate(new Date());
            if (invInvoiceDTO.getCompanyId() != null) {
                IsagCompany company = new IsagCompany();
                company.setId(invInvoiceDTO.getCompanyId());
                invPurchaseInvoice.setCompanyId(company);
            }

            if (invInvoiceDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(invInvoiceDTO.getBranchId());
                invPurchaseInvoice.setBranchId(branch);
            }
        } else {
            invPurchaseInvoice.setCreatedBy(isagUser);
            invPurchaseInvoice.setCreationDate(new Date());
            invPurchaseInvoice.setCompanyId(isagUser.getCompanyId());
            invPurchaseInvoice.setBranchId(isagUser.getBranchId());
        }
        return invPurchaseInvoice;
    }

    public InvInvoiceDTO validateSave(InvInvoiceDTO invInvoiceDTO) {
        StringBuilder error = new StringBuilder();
        if (invInvoiceDTO.getInvInventoryId() == null) {
            error.append("\n يجب أختيار مخزن");
        }

        if (invInvoiceDTO.getCurrencyId() == null) {
            error.append("\n يجب أختيار عمله");
        }

        if (invInvoiceDTO.getOrganizationSiteId() == null) {
            error.append("\n يجب أختيار عميل / مورد");
        }

        if (invInvoiceDTO.getId() == null && CompanyActivityClassEnum.CERAMIC.equals(invInvoiceDTO.getCompanyActivity())
                && invInvoiceDTO.getSupplierInvoiceNumber() != null && !invInvoiceDTO.getSupplierInvoiceNumber().isEmpty()) {
            Map<String, Object> params = new HashMap<>();
            params.put("supplierInvoiceNumber", invInvoiceDTO.getSupplierInvoiceNumber());
            params.put("type", invInvoiceDTO.getType());
            if (ScreenNameClassEnum.INVRETURNPURCHASEFORM.equals(invInvoiceDTO.getScreenName())) {
                params.put("type", 2);
            } else if (ScreenNameClassEnum.INVRETURNSALESFORM.equals(invInvoiceDTO.getScreenName())) {
                params.put("type", 3);
            }
            String supplierInvoiceNumber = dao.findEntityByQuery("SELECT e.supplierInvoiceNumber FROM InvPurchaseInvoice e WHERE e.type =:type "
                    + " AND  e.supplierInvoiceNumber =:supplierInvoiceNumber ", params);
            if (supplierInvoiceNumber != null) {
                error.append("\n رقم الاذن موجود من قبل");
            }
        }

        if (invInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVSALESINVOICEFORM)) {
            checkIfLoaded(invInvoiceDTO, error);
        }

        if (!error.toString().isEmpty()) {
            invInvoiceDTO.setMsg(error.toString());
        }

        return invInvoiceDTO;
    }

    public void checkIfLoaded(InvInvoiceDTO invPurchaseInvoiceDTO, StringBuilder error) {
        if (invPurchaseInvoiceDTO.getId() != null) {
            List<Integer> id = dao.executeNativeQuery("select id from (select parent as id from inv_purchase_invoice "
                    + " where branch_id = " + invPurchaseInvoiceDTO.getBranchId() + " and parent is not null\n" + " union all "
                    + " select inv_purchase_invoice_id as id from inv_addingorder "
                    + " where branch_id = " + invPurchaseInvoiceDTO.getBranchId() + " and inv_purchase_invoice_id is not null ) as s\n"
                    + " where id = " + invPurchaseInvoiceDTO.getId());
            if (!id.isEmpty()) {
                error.append("\n لا يجوز تعديل هذا السند لأنه تم تحميلة");
            }
        }
    }

    @Override
    public InvInvoiceDTO save(InvInvoiceDTO invInvoiceDTO, IsagUser isagUser) {
        try {
            invInvoiceDTO = validateSave(invInvoiceDTO);

            InvPurchaseInvoice invPurchaseInvoice;
            if (invInvoiceDTO != null && (invInvoiceDTO.getMsg() == null || invInvoiceDTO.getMsg().isEmpty())) {
                invPurchaseInvoice = mapToEntity(invInvoiceDTO, isagUser);
                if (invPurchaseInvoice.getId() != null) {
                    //بعمل حفظ فى حالة التعديل
                    invPurchaseInvoice = dao.updateEntity(invPurchaseInvoice);
                } else {
                    // فى حالة ان الفاتورة جديدة بروح اجيب السيريال الاول 
                    Integer serial = genericService.getMaxSerialForInvoice(InvPurchaseInvoice.class, invPurchaseInvoice.getBranchId().getId(), invPurchaseInvoice.getInvInventoryId().getId(), invPurchaseInvoice.getType());
                    if (serial == null) {
                        invInvoiceDTO.setMsg("من فضلك اعد حفظ الفاتورة");
                        dao.rollbackQuery();
                        return invInvoiceDTO;
                    }
                    invPurchaseInvoice.setSerial(serial);
                    // بجيب السيريال الخاص بالفواتير الضريبية
                    if (invPurchaseInvoice.getTaxflag()) {
                        Integer serialTax = getMaxSerialTaxForInvoice(invPurchaseInvoice.getBranchId().getId(), invPurchaseInvoice.getInvInventoryId().getId(), invPurchaseInvoice.getType(), invPurchaseInvoice.getTaxFlagFinal() == null ? Boolean.FALSE : invPurchaseInvoice.getTaxFlagFinal());
                        invPurchaseInvoice.setSerialTax(serialTax);
                    }

                    invPurchaseInvoice = dao.updateEntity(invPurchaseInvoice);
                    invInvoiceDTO.setId(invPurchaseInvoice.getId());
                }

                invPurchaseInvoiceDetailsService.deleteInvPurchaseInvoiceDetailListDTO(invInvoiceDTO.getInvPurchaseInvoiceDetailsDeletedList());

                String error = invPurchaseInvoiceDetailsService.addInvPurchaseInvoiceDetailsDTO(invInvoiceDTO.getInvPurchaseInvoiceDetailList(), invInvoiceDTO, isagUser);

                if (error != null && !error.isEmpty()) {
                    invInvoiceDTO.setMsg(error);
                    dao.rollbackQuery();
                } else {
                    invInvoiceDTO = mapToDTO(invPurchaseInvoice, invInvoiceDTO, false);
                    invInvoiceDTO = invGlobalService.updateSummition(invInvoiceDTO);
                }

            }
        } catch (Exception e) {
            invInvoiceDTO.setMsg(e.toString());
            dao.rollbackQuery();
        }

        return invInvoiceDTO;
    }

//    @Override
//    public InvInvoiceDTO convertInvPurchaseInvoiceToPurchaseInvoice(InvInvoiceDTO invPurchaseInvoiceDTO) {
//        InvPurchaseInvoice invPurchaseInvoice = dao.findEntityById(InvPurchaseInvoice.class, invPurchaseInvoiceDTO.getInvDailogeDataSelected().getId());
//        if (invPurchaseInvoice != null) {
//            invPurchaseInvoiceDTO = mapToDTO(invPurchaseInvoice, invPurchaseInvoiceDTO);
//            modifyReturnData(invPurchaseInvoiceDTO, invPurchaseInvoice);
//        } else {
//            invPurchaseInvoiceDTO.setMsg("حدث خطأ - تم حذفه ");
//        }
//        return invPurchaseInvoiceDTO;
//    }
    @Override
    public InvInvoiceDTO convertInvPurchaseInvoiceToPurchaseInvoice(InvInvoiceDTO invPurchaseInvoiceDTO, Integer type) {
        InvPurchaseInvoice invPurchaseInvoice = null;
        if (invPurchaseInvoiceDTO.getNeedLoadedSerial() != null && invPurchaseInvoiceDTO.getNeedLoadedSerial() != 0) {
            invPurchaseInvoice = findInvPurchaseInvoiceByInvPurchaseInvoiceId(invPurchaseInvoiceDTO.getNeedLoadedSerial(), type, invPurchaseInvoiceDTO.getBranchId());
        } else {
            invPurchaseInvoice = dao.findEntityById(InvPurchaseInvoice.class, invPurchaseInvoiceDTO.getInvDailogeDataSelected().getId());
        }
        if (invPurchaseInvoice != null) {
            invPurchaseInvoiceDTO = mapToDTO(invPurchaseInvoice, invPurchaseInvoiceDTO, true);
            modifyReturnData(invPurchaseInvoiceDTO, invPurchaseInvoice);
        } else {
            invPurchaseInvoiceDTO.setMsg("حدث خطأ - تم حذفه ");
        }
        return invPurchaseInvoiceDTO;
    }

    private InvPurchaseInvoice findInvPurchaseInvoiceByInvPurchaseInvoiceId(Integer serial, Integer type, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("serial", serial);
        params.put("branchId", branchId);
        params.put("type", type);
        List<InvPurchaseInvoice> details = dao.findListByQuery("SELECT e FROM InvPurchaseInvoice e WHERE e.serial = :serial AND e.branchId.id = :branchId AND e.type = :type", params);
        return (details == null || details.isEmpty()) ? null : details.get(0);
    }

    private void modifyReturnData(InvInvoiceDTO invPurchaseInvoiceDTO, InvPurchaseInvoice invPurchaseInvoice) {
        invPurchaseInvoiceDTO.setId(null);
        invPurchaseInvoiceDTO.setSerial(null);
        invPurchaseInvoiceDTO.setDate(new Date());

        invPurchaseInvoiceDTO.setParent(invPurchaseInvoice.getId());
        invPurchaseInvoiceDTO.setInvPurchaseInvoiceId(invPurchaseInvoice.getId());

        invPurchaseInvoiceDTO.setLoadedSerial(invPurchaseInvoice.getSerial());
        List<InvDetailDTO> detailDTOs = new ArrayList<>();
        for (InvDetailDTO invDetailDTO : invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailList()) {
            invDetailDTO.setId(null);
            invDetailDTO.setSerial(null);
            detailDTOs.add(invDetailDTO);
        }
        invPurchaseInvoiceDTO.setInvPurchaseInvoiceDetailList(detailDTOs);
    }

    private InvInvoiceDTO mapToDTO(InvPurchaseInvoice entity, InvInvoiceDTO invPurchaseInvoiceDTO, Boolean isLoaded) {

        invPurchaseInvoiceDTO = initData(invPurchaseInvoiceDTO, entity, isLoaded);

        return invPurchaseInvoiceDTO;
    }

    private InvInvoiceDTO initData(InvInvoiceDTO invInvoiceDTO, InvPurchaseInvoice entity, Boolean isLoaded) {
        invInvoiceDTO.setRevision((entity.getRevision() != null && 1 == entity.getRevision()));
        invInvoiceDTO.setTypeActivity(entity.getTypeActivity());
        invInvoiceDTO.setId(entity.getId());
        invInvoiceDTO.setSerial(entity.getSerial());
        invInvoiceDTO.setSerialTax(entity.getSerialTax());
        invInvoiceDTO.setCompanyActivity(entity.getBranchId().getCompanyActivity());
        invInvoiceDTO.setIsEdit(Boolean.TRUE);
        invInvoiceDTO.setIsDone(entity.getIsDone());
        invInvoiceDTO.setLook(entity.getLook());
        invInvoiceDTO.setTotalNolon(entity.getTotalNolon());
        if (entity.getAdminUnitId() != null) {
            GlAdminUnitDTO adminUnitDTO = new GlAdminUnitDTO();
            adminUnitDTO.setId(entity.getAdminUnitId().getId());
            adminUnitDTO.setName(entity.getAdminUnitId().getName());
            adminUnitDTO.setCode(entity.getAdminUnitId().getCode());
            invInvoiceDTO.setAdminUnitId(adminUnitDTO);
        }
        invInvoiceDTO.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
        if (entity.getCostCenterId() != null) {
            CostCenterDTO centerDTO = new CostCenterDTO();
            centerDTO.setId(entity.getCostCenterId().getId());
            centerDTO.setName(entity.getCostCenterId().getName());
            centerDTO.setCode(entity.getCostCenterId().getCode());
            invInvoiceDTO.setCostCenterId(centerDTO);
        }
        if (entity.getCurrencyId() != null) {
            CurrencyDTO currencyDTO = new CurrencyDTO();
            currencyDTO.setId(entity.getCurrencyId().getId());
            currencyDTO.setName(entity.getCurrencyId().getName());
            currencyDTO.setCode(entity.getCurrencyId().getCode());
            invInvoiceDTO.setCurrencyId(currencyDTO);
        }
        invInvoiceDTO.setDate(entity.getDate());
        if (entity.getAccountId() != null) {
            GlAccount glAccount = new GlAccount();
            glAccount.setId(entity.getAccountId().getId());
            invInvoiceDTO.setAccountId(glAccount.getId());
        }
        invInvoiceDTO.setTaxFlagFinal(entity.getTaxFlagFinal());
        invInvoiceDTO.setRemarks(entity.getRemarks());
        invInvoiceDTO.setDiscount(entity.getDiscount());
        invInvoiceDTO.setDiscountType(entity.getDiscountType());
        invInvoiceDTO.setDueDate(entity.getDueDate());
        invInvoiceDTO.setPaymentType(entity.getPaymentType());
        invInvoiceDTO.setPostFlag(entity.getPostFlag());
        invInvoiceDTO.setSupplierInvoiceDate(entity.getSupplierInvoiceDate());
        invInvoiceDTO.setRate(entity.getRate());
        invInvoiceDTO.setInvAddingOrderId(entity.getInvAddingOrderId() != null ? entity.getInvAddingOrderId().getId() : null);
        invInvoiceDTO.setInvPurchaseOrderId(entity.getInvPurchaseOrderId() != null ? entity.getInvPurchaseOrderId().getId() : null);
        invInvoiceDTO.setTableNumber(entity.getTableNumber());
        invInvoiceDTO.setTypeRest(entity.getTypeRest());
        invInvoiceDTO.setCloseTable(entity.getCloseTable());
        if (entity.getOrganizationSiteId() != null) {
            invInvoiceDTO.setOrganizationSiteId(organizationSiteService.mapToDTOSmall(entity.getOrganizationSiteId()));// here
            if (CompanyActivityClassEnum.CERAMIC.equals(invInvoiceDTO.getCompanyActivity())
                    && ScreenNameClassEnum.INVSALESINVOICEFORM.equals(invInvoiceDTO.getScreenName())) {
                invInvoiceDTO.setCustomerBalance(supplierAccountService.getAccountForOrganizationSite(entity.getOrganizationSiteId().getId()));
            }
        }
        invInvoiceDTO.setSupplierInvoiceNumber(entity.getSupplierInvoiceNumber());
        if (entity.getInvInventoryId() != null) {
            InvInventoryDTO invInventoryDTO = new InvInventoryDTO();
            invInventoryDTO.setId(entity.getInvInventoryId().getId());
            invInventoryDTO.setName(entity.getInvInventoryId().getName());
            invInventoryDTO.setCode(entity.getInvInventoryId().getCode());
            invInvoiceDTO.setInvInventoryId(invInventoryDTO);
        }

        if (invInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVPURCHASEINVOICEFORM)) {
            if (invInvoiceDTO.getInventorySetupDTO().getSellBuy()) {
                if (entity.getInvAddingOrderId() != null) {
                    invInvoiceDTO.setParent(entity.getInvAddingOrderId().getId());
                    invInvoiceDTO.setInvPurchaseInvoiceId(entity.getInvAddingOrderId().getId());
                    invInvoiceDTO.setLoadedSerial(entity.getInvAddingOrderId().getSerial());
                }
            } else {
                if (entity.getInvPurchaseOrderId() != null) {
                    invInvoiceDTO.setParent(entity.getInvPurchaseOrderId().getId());
                    invInvoiceDTO.setInvPurchaseInvoiceId(entity.getInvPurchaseOrderId().getId());
                    invInvoiceDTO.setLoadedSerial(entity.getInvPurchaseOrderId().getSerial());
                }
            }
        } else if (invInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVSALESINVOICEFORM) && entity.getInvQotationId() != null) {
            invInvoiceDTO.setInvQotationId(entity.getInvQotationId().getId());
            invInvoiceDTO.setLoadedSerial(entity.getInvQotationId().getSerial());
        } else if (invInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVRETURNPURCHASEFORM) && entity.getParent() != null) {
            invInvoiceDTO.setParent(entity.getParent().getId());
            invInvoiceDTO.setInvPurchaseInvoiceId(entity.getParent().getId());
            invInvoiceDTO.setLoadedSerial(entity.getParent().getSerial());
        } else if (invInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVRETURNSALESFORM) && entity.getParent() != null) {
            invInvoiceDTO.setParent(entity.getParent().getId());
            invInvoiceDTO.setInvPurchaseInvoiceId(entity.getParent().getId());
            invInvoiceDTO.setLoadedSerial(entity.getParent().getSerial());
        }

        invInvoiceDTO.setExtraCost(entity.getExtraCost());
        invInvoiceDTO.setTaxdiscflag(entity.getTaxdiscflag());
        invInvoiceDTO.setTaxdiscvalue(entity.getTaxdiscvalue());
        if (entity.getInvDelegatorId() != null) {
            InventoryDelegatorDTO inventoryDelegatorDTO = new InventoryDelegatorDTO();
            inventoryDelegatorDTO.setId(entity.getInvDelegatorId().getId());
            inventoryDelegatorDTO.setName(entity.getInvDelegatorId().getName());
            inventoryDelegatorDTO.setCode(entity.getInvDelegatorId().getCode());
            invInvoiceDTO.setInvDelegatorId(inventoryDelegatorDTO);
        }
        if (entity.getGlBankId() != null) {
            GlBankDTO glBankDTO = new GlBankDTO();
            glBankDTO.setId(entity.getGlBankId().getId());
            glBankDTO.setName(entity.getGlBankId().getName());
            glBankDTO.setCode(entity.getGlBankId().getCode());
            invInvoiceDTO.setGlBankId(glBankDTO);
        }
        invInvoiceDTO.setTaxflag(entity.getTaxflag());

        invInvoiceDTO.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        invInvoiceDTO.setCreatedDate(entity.getCreationDate());
        invInvoiceDTO.setIndex(entity.getSerial());
        invInvoiceDTO.setType(entity.getType());
        invInvoiceDTO.setCompanyId(entity.getCompanyId().getId() != null ? entity.getCompanyId().getId() : null);
        invInvoiceDTO.setCreatedByName(entity.getCreatedBy() != null ? entity.getCreatedBy().getName() : null);
        invInvoiceDTO.setPayNet(entity.getPayNet());
        invInvoiceDTO.setPayCash(entity.getPayCash());

        List<InvDetailDTO> invPurchaseInvoiceDetailsListDTO;
        if (isLoaded) {
            invPurchaseInvoiceDetailsListDTO = invPurchaseInvoiceDetailsService.getAllInvPurchaseInvoiceDetailsByInvPurchaseInvoiceIdDTO(invInvoiceDTO);
        } else {
            invPurchaseInvoiceDetailsListDTO = invPurchaseInvoiceDetailsService.getAllInvPurchaseInvoiceDetailList(invInvoiceDTO);
        }
        invInvoiceDTO.setInvPurchaseInvoiceDetailsDeletedList(new ArrayList<InvDetailDTO>());
        invInvoiceDTO.setInvPurchaseInvoiceDetailList(invPurchaseInvoiceDetailsListDTO);

        invInvoiceDTO = invGlobalService.updateSummition(invInvoiceDTO);
        invInvoiceDTO.setCheckData(CheckData(entity.getDate(), entity.getGeneralJournalId() != null ? true : false));
        return invInvoiceDTO;
    }

    private Boolean CheckData(Date date, Boolean checkGeneralJournal) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(new Date());
        calendar2.set(Calendar.HOUR_OF_DAY, 0);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);
        calendar2.set(Calendar.MILLISECOND, 0);
        Boolean checkDate = calendar.getTime().equals(calendar2.getTime());
        if (checkGeneralJournal) {
            return Boolean.TRUE;
        } else {
            return !checkDate;
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
            String filter = " and ( FUNCTION('DATE_FORMAT',s.date, '%d-%m-%Y')  like CONCAT('%',:globalFilter ,'%') OR  s.serial like CONCAT('%',:globalFilter ,'%') OR  s.supplierInvoiceNumber like CONCAT('%',:globalFilter ,'%')  OR s.invInventoryId.name  like CONCAT('%',:globalFilter ,'%') OR s.organizationSiteId.name like CONCAT('%',:globalFilter ,'%') OR  s.invDelegatorId.name like CONCAT('%',:globalFilter ,'%') OR s.paymentType  like CONCAT('%',:globalFilter ,'%') OR s.createdBy.name like CONCAT('%',:globalFilter ,'%') OR s.remarks like CONCAT('%',:globalFilter ,'%'))";
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
        invPurchaseInvoiceDTO.setSupplierInvoiceNumber(entity.getSupplierInvoiceNumber());
        invPurchaseInvoiceDTO.setTypeActivity(entity.getTypeActivity());
        invPurchaseInvoiceDTO.setRevision((entity.getRevision() != null && entity.getRevision() == 1));
        invPurchaseInvoiceDTO.setLook(entity.getLook());
        invPurchaseInvoiceDTO.setIsDone(entity.getIsDone());
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
    public void deleteInvPurchaseInvoiceDTO(Integer invoiceId) {
        if (invoiceId != null) {
            InvPurchaseInvoice invPurchaseInvoice = dao.findEntityById(InvPurchaseInvoice.class, invoiceId);
            if (invPurchaseInvoice.getType() != null) {
                if (invPurchaseInvoice.getType() == 0) {
                    invPurchaseInvoice.setType(5);
                } else if (invPurchaseInvoice.getType() == 1) {
                    invPurchaseInvoice.setType(6);
                } else if (invPurchaseInvoice.getType() == 2) {
                    invPurchaseInvoice.setType(7);
                } else if (invPurchaseInvoice.getType() == 3) {
                    invPurchaseInvoice.setType(8);
                }
                dao.updateEntity(invPurchaseInvoice);
            }
        }
    }

    @Override
    public InvInvoiceDTO returnInvInvoiceSummarySumation(InvPurchaseInvoice entity, BigDecimal sumDetailNet) {
        InvInvoiceDTO invInvoiceDTO = new InvInvoiceDTO();
        BigDecimal valueAfterDiscount = BigDecimal.ZERO;
        if (entity != null && entity.getDiscountType() == 0) {
            valueAfterDiscount = sumDetailNet.subtract(entity.getDiscount());
        } else if (entity != null && entity.getDiscountType() == 1) {
            valueAfterDiscount = sumDetailNet.subtract((entity.getDiscount().multiply(sumDetailNet)).divide(new BigDecimal(100)));
        }
        invInvoiceDTO.setValue(valueAfterDiscount);
        invInvoiceDTO.setValueLocal(invInvoiceDTO.getValue().multiply(entity.getRate()));

        if (entity != null && entity.getTaxflag()) {
            invInvoiceDTO.setTaxValue(invInvoiceDTO.getValue().multiply(new BigDecimal(0.14)).setScale(3, BigDecimal.ROUND_DOWN));

        } else if (entity != null && !entity.getTaxflag()) {
            invInvoiceDTO.setTaxValue(BigDecimal.ZERO);
        }
        invInvoiceDTO.setTaxValueLocal(invInvoiceDTO.getTaxValue().multiply(entity.getRate()));

        if (entity != null && entity.getTaxdiscflag() != null && entity.getTaxdiscflag()) {
            invInvoiceDTO.setTaxDiscValue(invInvoiceDTO.getValue().multiply(new BigDecimal(0.01)).setScale(3, BigDecimal.ROUND_DOWN));

        } else if (entity != null && (entity.getTaxdiscflag() == null || !entity.getTaxdiscflag())) {
            invInvoiceDTO.setTaxDiscValue(BigDecimal.ZERO);
        }
        invInvoiceDTO.setTaxDiscValueLocal(invInvoiceDTO.getTaxDiscValue().multiply(entity.getRate()));
        invInvoiceDTO.setRate(entity.getRate());
        invInvoiceDTO.setFinalNet(invInvoiceDTO.getValue().subtract(invInvoiceDTO.getDiscountValue()).add(invInvoiceDTO.getTaxValue()).subtract(invInvoiceDTO.getTaxdiscvalue()));
        return invInvoiceDTO;

    }

    @Override
    public BigDecimal netPay(SubAccountSummarySearchBean cashAccountSettlementSearchBean, Integer bankId) {
        if (bankId != null) {
            Map<String, Object> params = new HashMap();
            params.put("branchId", cashAccountSettlementSearchBean.getBranchId());
            StringBuilder query = new StringBuilder();
            BigDecimal sales = findNetPay(query, cashAccountSettlementSearchBean, params, 1, bankId);
            BigDecimal returnSales = findNetPay(query, cashAccountSettlementSearchBean, params, 3, bankId);
            BigDecimal finalPay = sales.subtract(returnSales);
            return finalPay;
        } else {
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal findNetPay(StringBuilder query, SubAccountSummarySearchBean cashAccountSettlementSearchBean, Map<String, Object> params, Integer type, Integer bankId) {
        params.put("type", type);
        query.append("select sum(i.payNet) from InvPurchaseInvoice i where i.branchId.id = :branchId AND i.type = :type ");
        if (bankId != null && bankId > 0) {
            query.append(" and i.glBankId.id = :glBankFrom");
            params.put("glBankFrom", bankId);
        }

        if (cashAccountSettlementSearchBean.getDateFrom() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateFrom());
            query.append(" and i.date >= '").append(d3).append("'");
        }

        if (cashAccountSettlementSearchBean.getDateTo() != null) {
            String d3 = new SimpleDateFormat("yyyy-MM-dd").format(cashAccountSettlementSearchBean.getDateTo());
            query.append(" and i.date<= '").append(d3).append("'");
        }
        BigDecimal list = dao.findEntityByQuery(query.toString(), params);
        if (list != null) {
            return list == null ? BigDecimal.ZERO : list;
        } else {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal returnInvInvoiceSummarySummationDuration(Integer branchId, Integer type, Date dateFrom, Date dateTo) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("type", type);
        params.put("dateFrom", dateFrom);
        params.put("dateTo", dateTo);
        return dao.findEntityByQuery("select sum(i.payCash) from InvPurchaseInvoice i where i.branchId.id = :branchId AND i.type = :type And i.date BETWEEN :dateFrom AND :dateTo", params);

    }

    @Override
    public void updatelookAndIsDone(SupplierAccountViewBean supplierAccountViewBean) {
        if (supplierAccountViewBean != null && supplierAccountViewBean.getIdNumber() != null) {
            InvPurchaseInvoice invPurchaseInvoice = dao.findEntityById(InvPurchaseInvoice.class, supplierAccountViewBean.getIdNumber());
            invPurchaseInvoice.setLook(supplierAccountViewBean.getLook() ? 1 : 0);
            invPurchaseInvoice.setIsDone(supplierAccountViewBean.getIsDone() ? 1 : 0);
            dao.updateEntity(invPurchaseInvoice);
        }
    }

    @Override
    public void updatelookAndIsDone(List<SupplierAccountViewBean> supplierAccountViewBeanList) {
        for (SupplierAccountViewBean supplierAccountViewBean : supplierAccountViewBeanList) {
            updatelookAndIsDone(supplierAccountViewBean);
        }
    }

    @Override
    public DistributionApiData distributionApiData(DistributionApiSearch distributionApiSearch) {
        DistributionApiData distributionApiData = new DistributionApiData();
        distributionApiData.setInvoicePurchaseNumbers(findInvoiceCount(distributionApiSearch, 0));
        distributionApiData.setInvoicePurchaseValues(findInvoiceNet(distributionApiSearch, 0));
        distributionApiData.setInvoiceSalesNumbers(findInvoiceCount(distributionApiSearch, 1));
        distributionApiData.setInvoiceSalesValues(findInvoiceNet(distributionApiSearch, 1));
        return distributionApiData;
    }

    private Integer findInvoiceCount(DistributionApiSearch distributionApiSearch, Integer type) {
        Map<String, Object> params = new HashMap();
        StringBuilder query = new StringBuilder();
        params.put("type", type);
        params.put("branchId", distributionApiSearch.getBranchId());
        query.append("select count(id) from InvPurchaseInvoice i where i.branchId.id = :branchId AND i.type = :type  ");

        if (distributionApiSearch.getDateFrom() != null) {
            params.put("dateFrom", Utility.returnFromDate(distributionApiSearch.getDateFrom()));
            query.append(" AND i.date >= :dateFrom");
            if (!distributionApiSearch.getDateType()) {
                params.put("dateTo", Utility.returnToDate(distributionApiSearch.getDateFrom()));
                query.append(" AND i.date <= :dateTo");
            }
        } else {
            params.put("dateFrom", Utility.returnFromDate(new Date()));
            query.append(" AND i.date >= :dateFrom");
            params.put("dateTo", Utility.returnToDate(new Date()));
            query.append(" AND i.date <= :dateTo");
        }

        if (distributionApiSearch.getDateTo() != null) {
            params.put("dateTo", Utility.returnToDate(distributionApiSearch.getDateTo()));
            query.append(" AND i.date <= :dateTo");
        }

        if (distributionApiSearch.getInventoryCodeFrom() != null) {
            params.put("InventoryCodeFrom", distributionApiSearch.getInventoryCodeFrom());
            query.append(" AND i.invInventoryId.code >= :InventoryCodeFrom");
            if (!distributionApiSearch.getInventoryType()) {
                params.put("InventoryCodeTo", distributionApiSearch.getInventoryCodeFrom());
                query.append(" AND i.invInventoryId.code <= :InventoryCodeTo");
            }
        }

        if (distributionApiSearch.getInventoryCodeTo() != null) {
            params.put("InventoryCodeTo", distributionApiSearch.getInventoryCodeTo());
            query.append(" AND i.invInventoryId.code <= :InventoryCodeTo");
        }
        List<Integer> details = dao.findListByQuery(query.toString(), params);
        if (details != null && !details.isEmpty()) {
            return details.get(0);
        }
        return 0;
    }

    private BigDecimal findInvoiceNet(DistributionApiSearch distributionApiSearch, Integer type) {
        Map<String, Object> params = new HashMap();
        StringBuilder query = new StringBuilder();
        params.put("type", type);
        params.put("branchId", distributionApiSearch.getBranchId());
        query.append("select sum(ipid.net) , ipid.invPurchaseInvoiceId.discountType , ipid.invPurchaseInvoiceId.discount  from InvPurchaseInvoiceDetail ipid  where ipid.invPurchaseInvoiceId.branchId.id = :branchId AND ipid.invPurchaseInvoiceId.type = :type  ");

        if (distributionApiSearch.getDateFrom() != null) {
            params.put("dateFrom", Utility.returnFromDate(distributionApiSearch.getDateFrom()));
            query.append(" AND ipid.invPurchaseInvoiceId.date >= :dateFrom");
            if (!distributionApiSearch.getDateType()) {
                params.put("dateTo", Utility.returnToDate(distributionApiSearch.getDateFrom()));
                query.append(" AND ipid.invPurchaseInvoiceId.date <= :dateTo");
            }
        } else {
            params.put("dateFrom", Utility.returnFromDate(new Date()));
            query.append(" AND ipid.invPurchaseInvoiceId.date >= :dateFrom");
            params.put("dateTo", Utility.returnToDate(new Date()));
            query.append(" AND ipid.invPurchaseInvoiceId.date <= :dateTo");
        }

        if (distributionApiSearch.getDateTo() != null) {
            params.put("dateTo", Utility.returnToDate(distributionApiSearch.getDateTo()));
            query.append(" AND ipid.invPurchaseInvoiceId.date <= :dateTo");
        }

        if (distributionApiSearch.getInventoryCodeFrom() != null) {
            params.put("InventoryCodeFrom", distributionApiSearch.getInventoryCodeFrom());
            query.append(" AND ipid.invPurchaseInvoiceId.invInventoryId.code >= :InventoryCodeFrom");
            if (!distributionApiSearch.getInventoryType()) {
                params.put("InventoryCodeTo", distributionApiSearch.getInventoryCodeFrom());
                query.append(" AND ipid.invPurchaseInvoiceId.invInventoryId.code <= :InventoryCodeTo");
            }
        }

        if (distributionApiSearch.getInventoryCodeTo() != null) {
            params.put("InventoryCodeTo", distributionApiSearch.getInventoryCodeTo());
            query.append(" AND ipid.invPurchaseInvoiceId.invInventoryId.code <= :InventoryCodeTo");
        }

        query.append(" group by ipid.invPurchaseInvoiceId.id ");
        List<Object[]> res = dao.findListByQuery(query.toString(), params);
        Iterator it = res.iterator();
        BigDecimal result = BigDecimal.ZERO;
        while (it.hasNext()) {
            Object[] line = (Object[]) it.next();
            BigDecimal summution = (BigDecimal) line[0];
            Integer discountType = (Integer) line[0];
            BigDecimal discount = (BigDecimal) line[0];
            if (discountType != null && discountType == 0) {
                result = result.add(summution.subtract(discount));
            } else if (discountType != null && discountType == 1) {
                result = result.add(summution.subtract(((discount.divide(new BigDecimal(100), 3, RoundingMode.HALF_UP)).multiply(summution))));
            }
        }
        return result;
    }

}
