/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.bussinessservice.global.AutoCompleteService;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import com.toby.bussinessservice.global.InvPurchaseReturnSave;
import com.toby.core.GenericDAO;
import com.toby.core.UserDataDTO;
import com.toby.define.CompanyActivityClassEnum;
import com.toby.define.ScreenNameClassEnum;
import com.toby.dto.CostCenterDTO;
import com.toby.dto.CurrencyDTO;
import com.toby.dto.CurrencyOperationDTO;
import com.toby.dto.GeneralJournalDTO;
import com.toby.dto.GlAdminUnitDTO;
import com.toby.dto.GlBankDTO;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.InventoryDelegatorDTO;
import com.toby.dto.PrintProductionDTO;
import com.toby.dto.ProGroupDTO;
import com.toby.entity.Branch;
import com.toby.entity.CostCenter;
import com.toby.entity.Currency;
import com.toby.entity.DiscountCodes;
import com.toby.entity.GeneralJournal;
import com.toby.entity.GeneralJournalDetails;
import com.toby.entity.GlAccount;
import com.toby.entity.GlAdminUnit;
import com.toby.entity.GlBank;
import com.toby.entity.InvAddingorder;
import com.toby.entity.InvAddingorderDetail;
import com.toby.entity.InvInventory;
import com.toby.entity.InvItem;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.InvPurchaseInvoice;
import com.toby.entity.InvPurchaseInvoiceDetail;
import com.toby.entity.InvPurchaseOrder;
import com.toby.entity.InvPurchaseOrderDetail;
import com.toby.entity.InvQotation;
import com.toby.entity.InvQotationDetail;
import com.toby.entity.InvReservation;
import com.toby.entity.InvReservationDetail;
import com.toby.entity.InvTransfer;
import com.toby.entity.InvTransferDetail;
import com.toby.entity.InventoryDelegator;
import com.toby.entity.InventorySetup;
import com.toby.entity.ProFactoryDelivery;
import com.toby.entity.Symbol;
import com.toby.views.ClientsNoMovment;
import com.toby.views.FactoryInvoiceStatus;
import com.toby.views.QuantityItemsStore;
import com.toby.views.SummitionItemsView;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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
    private SymbolService symbolService;
    @EJB
    private CurrencyService currencyService;
    @EJB
    private InvGlobalService invGlobalService;
    @EJB
    private GlBankTransactionService glBankTransactionService;
    @EJB
    AutoCompleteService autoCompleteService;
    @EJB
    InvoiceMovementReportService invoiceMovementReportService;
    @EJB
    private OrganizationSiteService organizationSiteService;
    @EJB
    private InvPurchaseInvoiceService_1 invPurchaseInvoiceService;
    @EJB
    ProFactoryDeliveryService proFactoryDeliveryService;
    @EJB
    private GeneraljournalService generaljournalService;
    @EJB
    private InvInventoryService inventoryService;
    @EJB
    private GeneraljournalDetailsService generaljournalDetailsService;

    InvPurchaseReturnSave invPurchaseReturnSave;

    @Override
    public Integer getInvoiceBySerial(Integer serial) {
        if (serial != null && serial > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(" select id from inv_purchase_invoice ").
                    append(" where serial = \n").
                    append(serial).
                    append(" and type = 4  and branch_id = 182 ");
            List<Integer> details = dao.executeNativeQuery(builder.toString());
            if (details != null && !details.isEmpty()) {
                return details.get(0);
            }
            return null;
        }
        return null;
    }

    @Override
    public InvInvoiceDTO findInvoiceBySerial(Integer serial) {

        if (serial != null && serial > 0) {
            Map<String, Object> params = new HashMap();
            params.put("serial", serial);
            params.put("type", 4);
            params.put("branchId", 182);
            StringBuilder builder = new StringBuilder();
            builder.append(" select i from InvPurchaseInvoice i ").
                    append(" where i.serial = :serial ").
                    append(" and i.type = :type  and i.branchId.id = :branchId ");
            List<InvPurchaseInvoice> details = dao.findListByQuery(builder.toString(), params);
            if (details != null && !details.isEmpty()) {
                return mapToDTO(details.get(0), Boolean.FALSE);
            }
            return null;
        }
        return null;
    }

    @Override
    public void updateInvoiceForsendCloth(Integer id, Integer sendCloth) {
        InvPurchaseInvoice entity = dao.findEntityById(InvPurchaseInvoice.class, id);
        entity.setSendCloth(sendCloth);
        dao.updateEntity(entity);
    }

    @Override
    public BigDecimal getInvoiceByRaghiCode(String raghiCode, Integer invoiceId) {
        if (raghiCode != null && !raghiCode.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            builder.append(" select id from inv_purchase_invoice ").append(" where raghi_code = '").append(raghiCode).append("' and type = 4  and branch_id = 182 ");
            if (invoiceId != null) {
                builder.append(" and id != ").append(invoiceId);
            }
            List<Integer> details = dao.executeNativeQuery(builder.toString());
            if (details != null && !details.isEmpty()) {
                return BigDecimal.ZERO;
            }
            DiscountCodes discountCodes = validateDiscountCode(raghiCode);
            return discountCodes == null ? BigDecimal.ZERO : discountCodes.getDiscountValue();
        }
        return BigDecimal.ZERO;
    }

    public DiscountCodes validateDiscountCode(String code) {
        Map<String, Object> params = new HashMap();
        params.put("code", code);
        List<DiscountCodes> discount = dao.findListByQuery("SELECT d FROM DiscountCodes d WHERE d.code = :code ", params);
        if (discount != null && !discount.isEmpty()) {
            return discount.get(0);
        }
        return null;
    }

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
//                    invTransfer = invTransferService.findInvTransferByInvTransferId(headId);
                    if (invTransferDetailList != null && invTransferDetailList.size() > 0) {
                        for (int i = 0; i < invTransferDetailList.size(); i++) {
                            if (i == 0) {
//                                invTransfer.setStatus(invTransferDetailList.get(0));
                            } else {
                                Integer x = invTransferDetailList.get(0) == null ? 0 : invTransferDetailList.get(0);
                                Integer y = invTransferDetailList.get(i) == null ? 0 : invTransferDetailList.get(i);
                                if (x.compareTo(y) != 0) {
//                                    invTransfer.setStatus(1);
                                    break;
                                }
                            }
                        }
//                        invTransferService.updateInvTransfer(invTransfer);
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
    public InvInvoiceDTO findInvPurchaseInvoiceById(InvInvoiceDTO invPurchaseInvoiceDto) {
        InvPurchaseInvoice invPurchaseInvoice = dao.findEntityById(InvPurchaseInvoice.class, invPurchaseInvoiceDto.getId());
        InvInvoiceDTO invPurchaseInvoiceDTO = mapToDTO(invPurchaseInvoice, invPurchaseInvoiceDto, false);
        invPurchaseInvoiceDTO = invGlobalService.updateSummition(invPurchaseInvoiceDTO);
        BigDecimal remain = remainForInvPurchaseInvoice(invPurchaseInvoiceDTO.getId());
        invPurchaseInvoiceDTO.setRemain(remain.setScale(0, BigDecimal.ROUND_UP));
        invPurchaseInvoiceDTO.setPayed(findPayed(invPurchaseInvoiceDTO.getId()).setScale(0, BigDecimal.ROUND_DOWN));
        return invPurchaseInvoiceDTO;
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
    public InvPurchaseInvoice updateInvPurchaseInvoiceForStopped(Integer invPurchaseInvoiceId, Integer stopped) {

        InvPurchaseInvoice invoiceUpdate = new InvPurchaseInvoice();

        invoiceUpdate = dao.findEntityById(InvPurchaseInvoice.class, invPurchaseInvoiceId);
        invoiceUpdate.setStopped(stopped);
        return dao.updateEntity(invoiceUpdate);
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
    public synchronized Integer getMaxSerialTaxForInvoice(Integer branchId, Integer galaryId, Integer type, Boolean taxFalgFinal) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);

        StringBuilder query = new StringBuilder();
        query.append("SELECT max(e.serialTax) FROM InvPurchaseInvoice e WHERE e.branchId.id =:branchId");

        if (type != null) {
            params.put("type", type);
            query.append(" AND e.type =:type");
        }

        if (galaryId != null) {
            params.put("galaryId", galaryId);
            query.append(" AND e.galaryId.id =:galaryId");
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
    public String validateloginDistributedScreens(UserDataDTO userDataDTO, ScreenNameClassEnum screenNameClassEnum, Integer gallaryId) {
        StringBuilder error = new StringBuilder();
        validateInventory(userDataDTO, error, screenNameClassEnum, gallaryId);
        return error.toString();
    }

    private void validateInventory(UserDataDTO userDataDTO, StringBuilder error, ScreenNameClassEnum screenNameClassEnum, Integer gallaryId) {

        if (!ScreenNameClassEnum.INVSALESINVOICEFORM.equals(screenNameClassEnum)) {
            List<InvInventoryDTO> invInventoryDTOList = userDataDTO.getInventoryDTOList();
            if (invInventoryDTOList == null || invInventoryDTOList.isEmpty()) {
                error.append("\n يجب اعطاء صلاحية للتعامل مع المخازن");
            }
            if (!ScreenNameClassEnum.INVTRANSFERINVENTORY.equals(screenNameClassEnum)
                    && !ScreenNameClassEnum.PROWORKORDER.equals(screenNameClassEnum)) {
                List<GlBankDTO> glBankList = userDataDTO.getGlBankDTOList();
                if (glBankList == null || glBankList.isEmpty()) {
                    error.append("\n يجب اعطاء صلاحية للتعامل مع الخزائن");
                }
            }
        }
        List<InvOrganizationSiteDTO> organizationSiteList = null;
        if (ScreenNameClassEnum.INVPURCHASEINVOICEFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVRETURNPURCHASEFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVADDINGORDERFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVPURCHASEADDING.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVPURCHASEORDERFORM.equals(screenNameClassEnum)) {
//            organizationSiteList = organizationSiteService.getorganizationSiteByBranchIdDTO(userDataDTO.getBranchId(), true, 1);
            organizationSiteList = autoCompleteService.autoCompleteOrganizationSiteDTO(userDataDTO.getBranchId(), true, 1, null, gallaryId);
        } else if (ScreenNameClassEnum.INVRETURNSALESFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVRESERVATIONFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVPERMISSIONORDERFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVQUOTATIONFORM.equals(screenNameClassEnum)
                || ScreenNameClassEnum.PROWORKORDER.equals(screenNameClassEnum)
                || ScreenNameClassEnum.INVSALESORDERFORM.equals(screenNameClassEnum)) {
//            organizationSiteList = organizationSiteService.getorganizationSiteByBranchIdDTO(userDataDTO.getBranchId(), true, 0);
            organizationSiteList = autoCompleteService.autoCompleteOrganizationSiteDTO(userDataDTO.getBranchId(), true, 0, null, gallaryId);
        }
        if (!ScreenNameClassEnum.INVTRANSFERINVENTORY.equals(screenNameClassEnum)
                && !ScreenNameClassEnum.INVSALESINVOICEFORM.equals(screenNameClassEnum)) {
            if (organizationSiteList == null || organizationSiteList.isEmpty()) {
                error.append("\n يجب انشاء موردين / عملاء ");
            }

            List<CurrencyDTO> currencyDTOList = currencyService.getAllCurrenciesWithLocalCurrencyHavingRatesByCompanyIdDTO(userDataDTO.getCompanyId()); // العملات
            if (currencyDTOList == null || currencyDTOList.isEmpty()) {
                error.append("\n يجب انشاء عملة للتعامل بها");
            }
        }
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
                invPurchaseInvoiceDTO.setMsg("هذا المورد أو العميل ليس له عمله");
            }
        } catch (Exception e) {
            invPurchaseInvoiceDTO.setMsg(e.toString());
        }
        return invPurchaseInvoiceDTO;
    }

    //mapToEntity
    private InvPurchaseInvoice mapToEntity(InvInvoiceDTO invInvoiceDTO, TobyUser tobyUser) {

        InvPurchaseInvoice invPurchaseInvoice = new InvPurchaseInvoice();

        if (invInvoiceDTO.getInvInventoryId() != null) {
            InvInventory inventory = new InvInventory();
            inventory.setId(invInvoiceDTO.getInvInventoryId().getId());
            invPurchaseInvoice.setInvInventoryId(inventory);
        }

        if (invInvoiceDTO.getGeneralJournalId() != null) {
            GeneralJournal generalJournal = new GeneralJournal();
            generalJournal.setId(invInvoiceDTO.getGeneralJournalId());
            invPurchaseInvoice.setGeneralJournalId(generalJournal);
        }

        if (invInvoiceDTO.getGallaryId() != null) {
            InvInventory inventory = new InvInventory();
            inventory.setId(invInvoiceDTO.getGallaryId().getId());
            invPurchaseInvoice.setGalaryId(inventory);
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
            invPurchaseInvoice.setGalaryId(null);
            if (invInvoiceDTO.getInvAddingOrderId() != null) {
                InvAddingorder invAddingorder = new InvAddingorder();
                invAddingorder.setId(invInvoiceDTO.getInvAddingOrderId());
                invPurchaseInvoice.setInvAddingOrderId(invAddingorder);
            } else if (invInvoiceDTO.getInvPurchaseOrderId() != null) {
                InvPurchaseOrder invPurchaseOrder = new InvPurchaseOrder();
                invPurchaseOrder.setId(invInvoiceDTO.getInvPurchaseOrderId());
                invPurchaseInvoice.setInvPurchaseOrderId(invPurchaseOrder);
            }
        } else if (invInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVSALESINVOICEFORM)) {
            invPurchaseInvoice.setType(1);

            if (invInvoiceDTO.getInvPurchaseInvoiceId() != null) {
                InvPurchaseInvoice purchaseInvoice = new InvPurchaseInvoice();
                purchaseInvoice.setId(invInvoiceDTO.getInvPurchaseInvoiceId());
                invPurchaseInvoice.setParent(purchaseInvoice);
            }

            if (invInvoiceDTO.getProFactoryDeliveryId() != null) {
                ProFactoryDelivery proFactoryDelivery = new ProFactoryDelivery();
                proFactoryDelivery.setId(invInvoiceDTO.getProFactoryDeliveryId());
                invPurchaseInvoice.setProFactoryDeliveryId(proFactoryDelivery);
            }
        } else if (invInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.PROWORKORDER)) {
            invPurchaseInvoice.setType(4);
            if (invInvoiceDTO.getInvQotationId() != null) {
                InvQotation invQotation = new InvQotation();
                invQotation.setId(invInvoiceDTO.getInvQotationId());
                invPurchaseInvoice.setInvQotationId(invQotation);
            }
        } else if (invInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVRETURNPURCHASEFORM)) {
            invPurchaseInvoice.setType(2);
            invPurchaseInvoice.setGalaryId(null);
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
        } else if (invInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVPURCHASEADDING)) {
            invPurchaseInvoice.setType(5);
            invPurchaseInvoice.setGalaryId(null);
        }

        invPurchaseInvoice.setId(invInvoiceDTO.getId());
        invPurchaseInvoice.setSerial(invInvoiceDTO.getSerial());
        invPurchaseInvoice.setSerialTax(invInvoiceDTO.getSerialTax());
        invPurchaseInvoice.setDate(invInvoiceDTO.getDate());
        invPurchaseInvoice.setDiscount(invInvoiceDTO.getDiscount());
        invPurchaseInvoice.setDiscountType(invInvoiceDTO.getDiscountType());
        invPurchaseInvoice.setDiscHalala(invInvoiceDTO.getDiscHalala());
        invPurchaseInvoice.setDueDate(invInvoiceDTO.getDueDate());
        invPurchaseInvoice.setRemarks(invInvoiceDTO.getRemarks());
        invPurchaseInvoice.setPostFlag(0);
        invPurchaseInvoice.setPaymentType(invInvoiceDTO.getPaymentType() > 0 ? invInvoiceDTO.getPaymentType() : 0);
        invPurchaseInvoice.setOfferCopoun(invInvoiceDTO.getOfferCopoun());
        if (invInvoiceDTO.getPaymentType() == 1) {
            invPurchaseInvoice.setGlBankId(null);
        }
        invPurchaseInvoice.setTaxflag(invInvoiceDTO.getTaxflag());
        invPurchaseInvoice.setRate(invInvoiceDTO.getRate() != null ? invInvoiceDTO.getRate() : null);
        invPurchaseInvoice.setSupplierInvoiceNumber(invInvoiceDTO.getSupplierInvoiceNumber());
        invPurchaseInvoice.setSupplierInvoiceDate(invInvoiceDTO.getSupplierInvoiceDate());
        invPurchaseInvoice.setExtraCost(invInvoiceDTO.getExtraCost() != null ? invInvoiceDTO.getExtraCost() : BigDecimal.ZERO);
        invPurchaseInvoice.setTaxdiscflag(invInvoiceDTO.getTaxdiscflag());
        invPurchaseInvoice.setTaxdiscvalue(invInvoiceDTO.getTaxdiscvalue());
        invPurchaseInvoice.setTaxFlagFinal(invInvoiceDTO.getTaxFlagFinal());
        invPurchaseInvoice.setPricetype(invInvoiceDTO.getPricetype());
        invPurchaseInvoice.setDueperiod(invInvoiceDTO.getDueperiod());
        invPurchaseInvoice.setInvoiceType(invInvoiceDTO.getInvoiceType());
        invPurchaseInvoice.setProof(invInvoiceDTO.getProof() ? 1 : 0);
        invPurchaseInvoice.setSendCloth(invInvoiceDTO.getSendCloth() ? 1 : 0);

        if (invInvoiceDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(invInvoiceDTO.getCreatedBy());
            invPurchaseInvoice.setCreatedBy(user);
            invPurchaseInvoice.setCreationDate(invInvoiceDTO.getCreatedDate());
            invPurchaseInvoice.setModifiedBy(tobyUser);
            invPurchaseInvoice.setModificationDate(new Date());
            if (invInvoiceDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(invInvoiceDTO.getCompanyId());
                invPurchaseInvoice.setCompanyId(company);
            }

            if (invInvoiceDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(invInvoiceDTO.getBranchId());
                invPurchaseInvoice.setBranchId(branch);
            }
        } else {
            invPurchaseInvoice.setCreatedBy(tobyUser);
            invPurchaseInvoice.setCreationDate(new Date());
            invPurchaseInvoice.setCompanyId(tobyUser.getCompanyId());
            invPurchaseInvoice.setBranchId(tobyUser.getBranchId());
        }
        return invPurchaseInvoice;
    }

    public InvInvoiceDTO validateSave(InvInvoiceDTO invInvoiceDTO) {
        StringBuilder error = new StringBuilder();
        if (invInvoiceDTO.getInvInventoryId() == null) {
            error.append("\n يجب أختيار مخزن");
        }
        if (invInvoiceDTO.getCompanyActivity().equals(CompanyActivityClassEnum.RetailSaleForClothFactory) && (ScreenNameClassEnum.INVSALESINVOICEFORM.equals(invInvoiceDTO.getScreenName()) || ScreenNameClassEnum.PROWORKORDER.equals(invInvoiceDTO.getScreenName()))) {
            if (invInvoiceDTO.getGallaryId() == null) {
                error.append("\n يجب أختيار معرض");
            }
        }

        if (invInvoiceDTO.getCurrencyId() == null) {
            error.append("\n يجب أختيار عمله");
        }

        if (invInvoiceDTO.getOrganizationSiteId() == null) {
            error.append("\n يجب أختيار عميل / مورد");
        }

        if (invInvoiceDTO.getScreenName().equals(ScreenNameClassEnum.INVSALESINVOICEFORM) || ScreenNameClassEnum.PROWORKORDER.equals(invInvoiceDTO.getScreenName())) {
            checkIfLoaded(invInvoiceDTO, error);
        }
//        if (invInvoiceDTO.getId() != null) {
//            if (proFactoryDeliveryService.checkProFactoryDelivery(invInvoiceDTO.getId())) {
//                error.append("\n  تم التسليم للمشغل");
//            }
//        }

        if (invInvoiceDTO.getRaghiCode() != null && !invInvoiceDTO.getRaghiCode().isEmpty()) {
//            if (invInvoiceDTO.getRaghiValue() == null) {
//                error.append("\n  يجب ادخال قيمة الكود او الغاء الكود");
//            } else {
            if (getInvoiceByRaghiCode(invInvoiceDTO.getRaghiCode(), invInvoiceDTO.getId()).compareTo(BigDecimal.ZERO) == 0) {
                error.append("\n  تم استخدام الكود من قبل");
            }
//            }
        }

        if (!error.toString().isEmpty()) {
            invInvoiceDTO.setMsg(error.toString());
        }
        return invInvoiceDTO;
    }

    public void checkIfLoaded(InvInvoiceDTO invPurchaseInvoiceDTO, StringBuilder error) {
        if (invPurchaseInvoiceDTO.getId() != null) {
            List<Integer> id = dao.executeNativeQuery("select id from (select parent as id from inv_purchase_invoice "
                    + " where branch_id = " + invPurchaseInvoiceDTO.getBranchId() + " and parent is not null\n"
                    + "  ) as s\n"
                    + " where id = " + invPurchaseInvoiceDTO.getId());
//            List<Integer> id = dao.executeNativeQuery("select id from (select parent as id from inv_purchase_invoice "
//                    + " where branch_id = " + invPurchaseInvoiceDTO.getBranchId() + " and parent is not null\n" + " union all "
//                    + " select inv_purchase_invoice_id as id from inv_addingorder "
//                    + " where branch_id = " + invPurchaseInvoiceDTO.getBranchId() + " and inv_purchase_invoice_id is not null ) as s\n"
//                    + " where id = " + invPurchaseInvoiceDTO.getId());
            if (!id.isEmpty()) {
                error.append("\n لا يجوز تعديل هذا السند لأنه تم تحميلة");
            }
        }
    }

    @Override
    public InvInvoiceDTO save(InvInvoiceDTO invInvoiceDTO, TobyUser tobyUser) {
        try {
            invInvoiceDTO = validateSave(invInvoiceDTO);

            InvPurchaseInvoice invPurchaseInvoice;
            if (invInvoiceDTO != null && (invInvoiceDTO.getMsg() == null || invInvoiceDTO.getMsg().isEmpty())) {
                invPurchaseInvoice = mapToEntity(invInvoiceDTO, tobyUser);
                if (invPurchaseInvoice.getId() != null) {
                    //بعمل حفظ فى حالة التعديل
                    invPurchaseInvoice = dao.updateEntity(invPurchaseInvoice);
                    if (invPurchaseInvoice.getGeneralJournalId() != null && invPurchaseInvoice.getGeneralJournalId().getId() != null) {
                        generaljournalService.deleteGeneralJournal(invPurchaseInvoice.getGeneralJournalId());
                    }
                } else {
                    // فى حالة ان الفاتورة جديدة بروح اجيب السيريال الاول 
                    Integer serial = genericService.getMaxSerialForInvoice(InvPurchaseInvoice.class, invPurchaseInvoice.getBranchId().getId(), null, invPurchaseInvoice.getType());
                    if (serial == null) {
                        invInvoiceDTO.setMsg("من فضلك اعد حفظ الفاتورة");
                        dao.rollbackQuery();
                        return invInvoiceDTO;
                    }
                    invPurchaseInvoice.setSerial(serial);
                    // بجيب السيريال الخاص بالفواتير الضريبية
                    if (invPurchaseInvoice.getTaxflag()) {
                        Integer serialTax = getMaxSerialTaxForInvoice(invPurchaseInvoice.getBranchId().getId(), invPurchaseInvoice.getGalaryId().getId(), invPurchaseInvoice.getType(), invPurchaseInvoice.getTaxFlagFinal() == null ? Boolean.FALSE : invPurchaseInvoice.getTaxFlagFinal());
                        invPurchaseInvoice.setSerialTax(serialTax);
                    }

                    invPurchaseInvoice = dao.updateEntity(invPurchaseInvoice);
                    invInvoiceDTO.setId(invPurchaseInvoice.getId());
                    if (invInvoiceDTO.getPayed() != null && invInvoiceDTO.getPayed().compareTo(BigDecimal.ZERO) == 1) {
                        glBankTransactionService.addCash(invInvoiceDTO, tobyUser);
                    }
                }

                invPurchaseInvoiceDetailsService.deleteInvPurchaseInvoiceDetailListDTO(invInvoiceDTO.getInvPurchaseInvoiceDetailsDeletedList());

                String error = invPurchaseInvoiceDetailsService.addInvPurchaseInvoiceDetailsDTO(invInvoiceDTO.getInvPurchaseInvoiceDetailList(), invInvoiceDTO, tobyUser);

                invInvoiceDTO.setInvPurchaseInvoiceId(invPurchaseInvoice.getId());
                if (invPurchaseInvoice.getId() == null) {
                    invAddingOrderService.save(invInvoiceDTO, tobyUser);
                }

                if (error != null && !error.isEmpty()) {
                    invInvoiceDTO.setMsg(error);
                    dao.rollbackQuery();
                } else {
                    invInvoiceDTO = mapToDTO(invPurchaseInvoice, invInvoiceDTO, false);
                    invInvoiceDTO = invGlobalService.updateSummition(invInvoiceDTO);
                }

                ///   الترحيل
//                GeneralJournal generalJournal = tarhil(invInvoiceDTO, tobyUser);
//                if (generalJournal != null) {
//                    invPurchaseInvoice.setPostFlag(1);
//                    invPurchaseInvoice.setGeneralJournalId(generalJournal);
//                    dao.updateEntity(invPurchaseInvoice);
//                }
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
            if (invPurchaseInvoiceDTO.getInvDailogeDataSelected() != null && invPurchaseInvoiceDTO.getInvDailogeDataSelected().getProFactoryDeliveryId() != null) {
                invPurchaseInvoiceDTO.setProFactoryDeliveryId(invPurchaseInvoiceDTO.getInvDailogeDataSelected().getProFactoryDeliveryId());
            }
            invPurchaseInvoiceDTO = mapToDTO(invPurchaseInvoice, invPurchaseInvoiceDTO, true);
            modifyReturnData(invPurchaseInvoiceDTO, invPurchaseInvoice);
            invPurchaseInvoiceDTO.setOrganizationSiteName((invPurchaseInvoice.getOrganizationSiteId() != null) ? invPurchaseInvoice.getOrganizationSiteId().getName() : null);
            invPurchaseInvoiceDTO.setGallaryName(invPurchaseInvoice.getGalaryId() != null ? invPurchaseInvoice.getGalaryId().getName() : null);
            invPurchaseInvoiceDTO.setNumberOfToob(invPurchaseInvoiceDetailsService.getNumberOftoob(invPurchaseInvoice.getId()));
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

    private InvInvoiceDTO initData(InvInvoiceDTO dto, InvPurchaseInvoice entity, Boolean isLoaded) {
        dto.setId(entity.getId());
        dto.setSerial(entity.getSerial());
        dto.setSerialTax(entity.getSerialTax());
        dto.setCompanyActivity(entity.getBranchId().getCompanyActivity());
        dto.setSendCloth(entity.getSendCloth() == 1);
        if (entity.getAdminUnitId() != null) {
            GlAdminUnitDTO adminUnitDTO = new GlAdminUnitDTO();
            adminUnitDTO.setId(entity.getAdminUnitId().getId());
            adminUnitDTO.setName(entity.getAdminUnitId().getName());
            adminUnitDTO.setCode(entity.getAdminUnitId().getCode());
            dto.setAdminUnitId(adminUnitDTO);
        }
        dto.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
        if (entity.getCostCenterId() != null) {
            CostCenterDTO centerDTO = new CostCenterDTO();
            centerDTO.setId(entity.getCostCenterId().getId());
            centerDTO.setName(entity.getCostCenterId().getName());
            centerDTO.setCode(entity.getCostCenterId().getCode());
            dto.setCostCenterId(centerDTO);
        }
        if (entity.getCurrencyId() != null) {
            CurrencyDTO currencyDTO = new CurrencyDTO();
            currencyDTO.setId(entity.getCurrencyId().getId());
            currencyDTO.setName(entity.getCurrencyId().getName());
            currencyDTO.setCode(entity.getCurrencyId().getCode());
            dto.setCurrencyId(currencyDTO);
        }
        dto.setDate(entity.getDate());
        if (entity.getAccountId() != null) {
            GlAccount glAccount = new GlAccount();
            glAccount.setId(entity.getAccountId().getId());
            dto.setAccountId(glAccount.getId());
        }

        if (entity.getGeneralJournalId() != null) {
            dto.setGeneralJournalId(entity.getGeneralJournalId().getId());
        }

        dto.setTaxFlagFinal(entity.getTaxFlagFinal());
        dto.setRemarks(entity.getRemarks());
        dto.setDiscount(entity.getDiscount());
        dto.setDiscountType(entity.getDiscountType());
        dto.setDiscHalala(entity.getDiscHalala());
        dto.setDueDate(entity.getDueDate());
        dto.setPaymentType(entity.getPaymentType());
        dto.setPostFlag(entity.getPostFlag());
        dto.setSupplierInvoiceDate(entity.getSupplierInvoiceDate());
        dto.setRate(entity.getRate());
        dto.setInvAddingOrderId(entity.getInvAddingOrderId() != null ? entity.getInvAddingOrderId().getId() : null);
        dto.setInvPurchaseOrderId(entity.getInvPurchaseOrderId() != null ? entity.getInvPurchaseOrderId().getId() : null);
        dto.setProFactoryDeliveryId(entity.getProFactoryDeliveryId() != null ? entity.getProFactoryDeliveryId().getId() : null);
        dto.setDueperiod(entity.getDueperiod());
        dto.setPricetype(entity.getPricetype());
        dto.setInvoiceType(entity.getInvoiceType());
        dto.setProof(entity.getProof() == 1);
        dto.setOfferCopoun(entity.getOfferCopoun());

        if (entity.getOrganizationSiteId() != null) {
            dto.setOrganizationSiteId(organizationSiteService.mapToDTOSmall(entity.getOrganizationSiteId()));
        }
        dto.setSupplierInvoiceNumber(entity.getSupplierInvoiceNumber());
        if (entity.getInvInventoryId() != null) {
            InvInventoryDTO inventoryDTO = inventoryService.mapToDTO(entity.getInvInventoryId());
            dto.setInvInventoryId(inventoryDTO);
        }

        if (entity.getGalaryId() != null) {
            InvInventoryDTO inventoryDTO = inventoryService.mapToDTO(entity.getGalaryId());
            dto.setGallaryId(inventoryDTO);
        }
        if (dto.getScreenName() != null) {
            if (ScreenNameClassEnum.INVPURCHASEINVOICEFORM.equals(dto.getScreenName())) {
                if (dto.getInventorySetupDTO().getSellBuy()) {
                    if (entity.getInvAddingOrderId() != null) {
                        dto.setParent(entity.getInvAddingOrderId().getId());
                        dto.setInvPurchaseInvoiceId(entity.getInvAddingOrderId().getId());
                        dto.setLoadedSerial(entity.getInvAddingOrderId().getSerial());
                    }
                } else {
                    if (entity.getInvPurchaseOrderId() != null) {
                        dto.setParent(entity.getInvPurchaseOrderId().getId());
                        dto.setInvPurchaseInvoiceId(entity.getInvPurchaseOrderId().getId());
                        dto.setLoadedSerial(entity.getInvPurchaseOrderId().getSerial());
                    }
                }
            } else if (ScreenNameClassEnum.PROWORKORDER.equals(dto.getScreenName()) && entity.getInvQotationId() != null) {
                dto.setInvQotationId(entity.getInvQotationId().getId());
                dto.setLoadedSerial(entity.getInvQotationId().getSerial());
            } else if (dto.getScreenName().equals(ScreenNameClassEnum.INVSALESINVOICEFORM)) {
                dto.setParent(entity.getParent() == null ? null : entity.getParent().getId());
                dto.setInvPurchaseInvoiceId(entity.getParent() == null ? null : entity.getParent().getId());
                dto.setProFactoryDeliveryId(entity.getProFactoryDeliveryId() == null ? null : entity.getProFactoryDeliveryId().getId());
                dto.setLoadedSerial(entity.getParent() == null ? null : entity.getParent().getSerial());
            } else if (dto.getScreenName().equals(ScreenNameClassEnum.INVRETURNPURCHASEFORM) && entity.getParent() != null) {
                dto.setParent(entity.getParent().getId());
                dto.setInvPurchaseInvoiceId(entity.getParent().getId());
                dto.setLoadedSerial(entity.getParent().getSerial());
            } else if (dto.getScreenName().equals(ScreenNameClassEnum.INVRETURNSALESFORM) && entity.getParent() != null) {
                dto.setParent(entity.getParent().getId());
                dto.setInvPurchaseInvoiceId(entity.getParent().getId());
                dto.setLoadedSerial(entity.getParent().getSerial());
            } else if (dto.getScreenName().equals(ScreenNameClassEnum.PROFACTORYDELIVERY) && entity.getParent() != null) {
                dto.setInvPurchaseInvoiceId(entity.getId());
                dto.setLoadedSerial(entity.getSerial());
            }
        }

        dto.setExtraCost(entity.getExtraCost());
        dto.setTaxdiscflag(entity.getTaxdiscflag());
        dto.setTaxdiscvalue(entity.getTaxdiscvalue());
        if (entity.getInvDelegatorId() != null) {
            InventoryDelegatorDTO inventoryDelegatorDTO = new InventoryDelegatorDTO();
            inventoryDelegatorDTO.setId(entity.getInvDelegatorId().getId());
            inventoryDelegatorDTO.setName(entity.getInvDelegatorId().getName());
            inventoryDelegatorDTO.setCode(entity.getInvDelegatorId().getCode());
            dto.setInvDelegatorId(inventoryDelegatorDTO);
        }
        if (entity.getGlBankId() != null) {
            GlBankDTO glBankDTO = new GlBankDTO();
            glBankDTO.setId(entity.getGlBankId().getId());
            glBankDTO.setName(entity.getGlBankId().getName());
            glBankDTO.setCode(entity.getGlBankId().getCode());
            dto.setGlBankId(glBankDTO);
        }
        dto.setTaxflag(entity.getTaxflag());

        dto.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dto.setCreatedDate(entity.getCreationDate());
        dto.setIndex(entity.getSerial());
        dto.setType(entity.getType());
        dto.setCompanyId(entity.getCompanyId().getId() != null ? entity.getCompanyId().getId() : null);
        dto.setCreatedByName(entity.getCreatedBy() != null ? entity.getCreatedBy().getName() : null);

        List<InvDetailDTO> invPurchaseInvoiceDetailsListDTO;
        if (isLoaded) {
            invPurchaseInvoiceDetailsListDTO = invPurchaseInvoiceDetailsService.getAllInvPurchaseInvoiceDetailsByInvPurchaseInvoiceIdDTO(dto);
        } else {
            invPurchaseInvoiceDetailsListDTO = invPurchaseInvoiceDetailsService.getAllInvPurchaseInvoiceDetailList(dto);
        }
        dto.setInvPurchaseInvoiceDetailsDeletedList(new ArrayList<InvDetailDTO>());
        dto.setInvPurchaseInvoiceDetailList(invPurchaseInvoiceDetailsListDTO);

        dto = invGlobalService.updateSummition(dto);
        return dto;
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

    @Override
    public Long getTotalRegistorsForGallary(Integer type, Integer galaryId, Integer branchId, Map<String, Object> filters, Boolean isGallary) {
        StringBuilder builder = new StringBuilder();
        filters.put("type", type);
        filters.put("branchId", branchId);
        filters.put("galaryId", galaryId);
        builder.append("SELECT count(s) FROM InvPurchaseInvoice s WHERE   s.branchId.id = :branchId AND s.type =:type ");
        if (isGallary) {
            builder.append("  AND  (:galaryId is Null OR  s.galaryId.id = :galaryId) ");
        } else {
            builder.append("  AND  (:galaryId is Null OR  s.invInventoryId.id = :galaryId) ");
        }
        appendFilter(filters, builder);
        Long details = dao.findEntityByQuery(builder.toString(), filters);
        return details == null ? 0 : details;
    }

    private void appendFilter(Map<String, Object> filters, StringBuilder builder) {
        if (filters.get("globalFilter") != null && !filters.get("globalFilter").toString().isEmpty()) {
            String filter = " and ( FUNCTION('DATE_FORMAT',s.date, '%d-%m-%Y')  like CONCAT('%',:globalFilter ,'%') OR  s.serial like CONCAT('%',:globalFilter ,'%') OR  s.serialTax like CONCAT('%',:globalFilter ,'%')  OR s.invInventoryId.name  like CONCAT('%',:globalFilter ,'%') OR s.galaryId.name  like CONCAT('%',:globalFilter ,'%') OR s.organizationSiteId.name like CONCAT('%',:globalFilter ,'%') OR s.organizationSiteId.code like CONCAT('%',:globalFilter ,'%')  OR s.organizationSiteId.mobile like CONCAT('%',:globalFilter ,'%') OR s.paymentType  like CONCAT('%',:globalFilter ,'%') OR s.createdBy.name like CONCAT('%',:globalFilter ,'%') OR s.remarks like CONCAT('%',:globalFilter ,'%'))";
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

    @Override
    public List<InvInvoiceDTO> getALLInvPurchaseInvoiceDTOByTypeAndBranchIdFroGallary(Integer type, Integer galaryId, int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters, Boolean isGallary) {
        filters.put("type", type);
        filters.put("branchId", branchId);
        filters.put("galaryId", galaryId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT s FROM InvPurchaseInvoice s WHERE  s.branchId.id = :branchId AND s.type =:type  ");
        if (isGallary) {
            builder.append("  AND  (:galaryId is Null OR  s.galaryId.id = :galaryId) ");
        } else {
            builder.append("  AND  (:galaryId is Null OR  s.invInventoryId.id = :galaryId) ");
        }
        appendFilter(filters, builder);
        builder.append(" order by s.serial DESC");
        List<InvPurchaseInvoice> details = dao.findListByQuery(builder.toString(), filters, first, pageSize);
        return returnListDTOForList(details);
    }

    @Override
    public List<InvInvoiceDTO> getALLInvPurchaseInvoiceDTOByTypeAndBranchIdFroGallary2(Integer type, Integer galaryId, Integer customerId) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("customerId", customerId);
        params.put("galaryId", galaryId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT s FROM InvPurchaseInvoice s WHERE  s.type =:type AND  (:galaryId is Null OR  s.galaryId.id = :galaryId) AND s.organizationSiteId.id = :customerId");
        builder.append(" order by s.serial DESC");
        List<InvPurchaseInvoice> details = dao.findListByQuery(builder.toString(), params);
        return returnListDTOForList(details);
    }

    @Override
    public List<InvInvoiceDTO> getALLInvPurchaseInvoiceDTOByStatus(PrintProductionDTO printProductionDTO, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", printProductionDTO.getInvoiceStatus());
        params.put("galaryId", printProductionDTO.getGallaryFrom().getId());

        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT s FROM InvPurchaseInvoice s WHERE s.branchId.id = :branchId AND  s.type =:type AND s.id not in (SELECT p.invPurchaseInvoiceId.id from ProFactoryDelivery p ) ");
        if (printProductionDTO.getGallaryFrom() != null) {
            builder.append(" AND  (:galaryId is Null OR  s.galaryId.id = :galaryId) ");
        }
        if (printProductionDTO.getFromDate() != null) {
            params.put("dateFrom", printProductionDTO.getFromDate());
            builder.append(" AND   s.date >= :dateFrom ");
        }
        if (printProductionDTO.getToDate() != null) {
            params.put("dateTo", printProductionDTO.getToDate());
            builder.append(" AND   s.date <= :dateTo ");
        }
        builder.append(" order by s.serial ");
        List<InvPurchaseInvoice> details = dao.findListByQuery(builder.toString(), params);
        return returnListDTOForList(details);
    }

    @Override
    public InvInvoiceDTO getInvPurchaseInvoiceDTOBySerial(Integer serial, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("serial", serial);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT s FROM InvPurchaseInvoice s WHERE  s.branchId.id =:branchId AND s.serial = :serial ");
        List<InvPurchaseInvoice> details = dao.findListByQuery(builder.toString(), params);

        if (details != null && !details.isEmpty()) {
            InvInvoiceDTO invInvoiceDTO = initDataForList(null, details.get(0));
            return invInvoiceDTO;
        } else {
            return null;
        }
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
        if (invInvoiceDTO == null) {
            invInvoiceDTO = new InvInvoiceDTO();
        }
        commonData(invInvoiceDTO, invPurchaseInvoice);
        return invInvoiceDTO;
    }

    private void commonData(InvInvoiceDTO invInvoiceDTO, InvPurchaseInvoice entity) {

        invInvoiceDTO.setId(entity.getId());
        invInvoiceDTO.setSerial(entity.getSerial());
        invInvoiceDTO.setSerialTax(entity.getSerialTax());
        invInvoiceDTO.setInvoiceSerial(entity.getSerial());
        invInvoiceDTO.setDate(entity.getDate());
        invInvoiceDTO.setIndex(entity.getSerial());
        invInvoiceDTO.setPostFlag(entity.getPostFlag());
        invInvoiceDTO.setRemarks(entity.getRemarks());
        invInvoiceDTO.setDueDate(entity.getDueDate());
        invInvoiceDTO.setNumberOfToob(invoiceMovementReportService.summitionTob(entity.getId()));
        String invoiceStatus = invPurchaseInvoiceService.findInvoiceStatus(entity.getId());

        if (invoiceStatus != null && !invoiceStatus.isEmpty()) {
            invInvoiceDTO.setInvoiceStatus(invoiceStatus);
        } else {
            invInvoiceDTO.setInvoiceStatus("بالمعرض");
        }

        if (entity.getDueDate() != null) {
            invInvoiceDTO.setDiffrentTime(entity.getDueDate().getTime() - (new Date()).getTime());
        }
        if (entity.getGalaryId() != null) {
            InvInventoryDTO invInventoryDTO = new InvInventoryDTO();
            invInventoryDTO.setId(entity.getGalaryId().getId());
            invInventoryDTO.setName(entity.getGalaryId().getName());
            invInventoryDTO.setCode(entity.getGalaryId().getCode());
            invInvoiceDTO.setGallary(invInventoryDTO);
            invInvoiceDTO.setGallaryId(invInventoryDTO);
            invInvoiceDTO.setGallaryName(invInventoryDTO.getName());
        }
        if (entity.getOrganizationSiteId() != null) {
            InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
            invOrganizationSiteDTO.setId(entity.getOrganizationSiteId().getId());
            invOrganizationSiteDTO.setName(entity.getOrganizationSiteId().getName());
            invOrganizationSiteDTO.setCode(entity.getOrganizationSiteId().getCode());
            invOrganizationSiteDTO.setMobile(entity.getOrganizationSiteId().getMobile());
            invInvoiceDTO.setOrganizationSiteName(entity.getOrganizationSiteId().getName());
            invInvoiceDTO.setOrganizationSiteId(invOrganizationSiteDTO);
            invInvoiceDTO.setCustomerCode(invOrganizationSiteDTO.getCode());
            invInvoiceDTO.setCustomerName(invOrganizationSiteDTO.getName());
            invInvoiceDTO.setCustomerMobile(invOrganizationSiteDTO.getMobile());
        }
        if (entity.getInvInventoryId() != null) {
            InvInventoryDTO invInventoryDTO = new InvInventoryDTO();
            invInventoryDTO.setId(entity.getInvInventoryId().getId());
            invInventoryDTO.setName(entity.getInvInventoryId().getName());
            invInventoryDTO.setCode(entity.getInvInventoryId().getCode());
            invInvoiceDTO.setInvInventoryId(invInventoryDTO);
        }
        if (entity.getInvDelegatorId() != null) {
            InventoryDelegatorDTO inventoryDelegatorDTO = new InventoryDelegatorDTO();
            inventoryDelegatorDTO.setId(entity.getInvDelegatorId().getId());
            inventoryDelegatorDTO.setName(entity.getInvDelegatorId().getName());
            inventoryDelegatorDTO.setCode(entity.getInvDelegatorId().getCode());
            invInvoiceDTO.setInvDelegatorId(inventoryDelegatorDTO);
            invInvoiceDTO.setInvDelegatorName(entity.getInvDelegatorId().getName());
        }

        invInvoiceDTO.setPaymentType(entity.getPaymentType());

        if (entity.getCreatedBy() != null) {
            invInvoiceDTO.setCreatedByName(entity.getCreatedBy().getName());
        }
    }

    @Override
    public String deleteInvPurchaseInvoiceDTO(InvInvoiceDTO invPurchaseInvoice, Integer isPurchaseOrSales) {
        if (invPurchaseInvoice != null && invPurchaseInvoice.getId() > 0) {
            if (proFactoryDeliveryService.checkProFactoryDelivery(invPurchaseInvoice.getId())) {
                glBankTransactionService.deleteGlBankTransactionByInvoiceId(invPurchaseInvoice.getId());
                List<InvPurchaseInvoiceDetail> detailList = invPurchaseInvoiceDetailsService.getAllInvPurchaseInvoiceDetailsByInvPurchaseInvoiceId(invPurchaseInvoice.getId());
                deleteDetails(detailList, isPurchaseOrSales);
                InvPurchaseInvoice invPurchaseInvoice1 = new InvPurchaseInvoice();
                invPurchaseInvoice1.setId(invPurchaseInvoice != null ? invPurchaseInvoice.getId() : null);
                dao.deleteEntity(invPurchaseInvoice1);
                return null;
            } else {
                return "تم التسليم للمشغل";
            }
        }
        return null;
    }

    @Override
    public InvInvoiceDTO returnInvInvoiceSummarySumation(InvPurchaseInvoice entity, BigDecimal sumDetailNet) {
        InvInvoiceDTO invInvoiceDTO = new InvInvoiceDTO();
        BigDecimal valueAfterDiscount = BigDecimal.ZERO;
        if (entity != null) {
            if (entity.getDiscountType() == 0) {
                valueAfterDiscount = sumDetailNet.subtract(entity.getDiscount().add(entity.getDiscHalala()));
            } else if (entity.getDiscountType() == 1) {
                valueAfterDiscount = sumDetailNet.subtract(entity.getDiscHalala()).subtract((entity.getDiscount().multiply(sumDetailNet)).divide(new BigDecimal(100)));
            }
            invInvoiceDTO.setValue(valueAfterDiscount);
            invInvoiceDTO.setValueLocal(invInvoiceDTO.getValue().multiply(entity.getRate()));
            invInvoiceDTO.setDiscHalala(entity.getDiscHalala());

            if (entity.getTaxflag()) {
//                invPurchaseInvoiceDTO.getTotalNetAfterDiscount().multiply(BigDecimal.valueOf(0.15))
                invInvoiceDTO.setTaxValue(invInvoiceDTO.getValue().multiply(BigDecimal.valueOf(0.15)).setScale(3, BigDecimal.ROUND_DOWN));

            } else if (!entity.getTaxflag()) {
                invInvoiceDTO.setTaxValue(BigDecimal.ZERO);
            }
            invInvoiceDTO.setTaxValueLocal(invInvoiceDTO.getTaxValue().multiply(entity.getRate()));

            if (entity.getTaxdiscflag() != null && entity.getTaxdiscflag()) {
                invInvoiceDTO.setTaxDiscValue(invInvoiceDTO.getValue().multiply(new BigDecimal(0.01)).setScale(3, BigDecimal.ROUND_DOWN));

            } else if (entity.getTaxdiscflag() == null || !entity.getTaxdiscflag()) {
                invInvoiceDTO.setTaxDiscValue(BigDecimal.ZERO);
            }
            invInvoiceDTO.setTaxDiscValueLocal((invInvoiceDTO.getTaxDiscValue().multiply(entity.getRate())));
            invInvoiceDTO.setRate(entity.getRate());
            invInvoiceDTO.setFinalNet(invInvoiceDTO.getValue().add(invInvoiceDTO.getTaxValue()).subtract(invInvoiceDTO.getTaxDiscValue()));
        }
        return invInvoiceDTO;

    }

    public BigDecimal finalNetForInvPurchaseInvoice(Integer invPurchaseInvoiceId) {
        if (invPurchaseInvoiceId != null && invPurchaseInvoiceId > 0) {
            Map<String, Object> params = new HashMap<>();
            params.put("invPurchaseInvoiceId", invPurchaseInvoiceId);
            List<BigDecimal> entitySum = dao.findListByQuery("SELECT sum(i.net) FROM InvPurchaseInvoiceDetail i WHERE i.invPurchaseInvoiceId.id = :invPurchaseInvoiceId Group by i.invPurchaseInvoiceId.id", params);
            InvPurchaseInvoice entity = dao.findEntityById(InvPurchaseInvoice.class, invPurchaseInvoiceId);
            InvInvoiceDTO invInvoiceDTO = returnInvInvoiceSummarySumation(entity, (entitySum == null || entitySum.isEmpty()) ? BigDecimal.ZERO : entitySum.get(0));
            return invInvoiceDTO.getFinalNet();
        }
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal remainForInvPurchaseInvoice(Integer invPurchaseInvoiceId) {
        if (invPurchaseInvoiceId != null && invPurchaseInvoiceId > 0) {
            BigDecimal payment = findPayed(invPurchaseInvoiceId);
            BigDecimal total = finalNetForInvPurchaseInvoice(invPurchaseInvoiceId);
            BigDecimal remain = (total.subtract(payment)).setScale(2, RoundingMode.HALF_UP);
            return remain;
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal findPayed(Integer invPurchaseInvoiceId) {
        Map<String, Object> params = new HashMap<>();
        params.put("invPurchaseInvoiceId", invPurchaseInvoiceId);
        List<BigDecimal> details = dao.findListByQuery("SELECT  sum(i.value)    FROM GlBankTransaction i WHERE i.invoiceId.id = :invPurchaseInvoiceId Group by i.invoiceId.id", params);
        List<BigDecimal> detailsReturn = dao.findListByQuery("SELECT  sum(i.value)    FROM GlBankTransactionDetail i WHERE i.invoiceId.id = :invPurchaseInvoiceId Group by i.invoiceId.id", params);
        BigDecimal payment = (details != null && !details.isEmpty()) ? details.get(0).subtract((detailsReturn != null && !detailsReturn.isEmpty()) ? detailsReturn.get(0) : BigDecimal.ZERO) : BigDecimal.ZERO;
        return payment;
    }

    @Override
    public String findInvoiceStatus(Integer invPurchaseInvoiceId) {
        List<String> statusList = null;
        if (invPurchaseInvoiceId != null && invPurchaseInvoiceId > 0) {
            statusList = dao.executeNativeQuery("SELECT \n"
                    + "        (CASE\n"
                    + "            WHEN (pfd.`type` = 0) THEN 'تسليم للمشغل'\n"
                    + "            WHEN (pfd.`type` = 1) THEN 'فى الطريق'\n"
                    + "            WHEN (pfd.`type` = 2) THEN 'جاهز '\n"
                    + "            WHEN (pfd.`type` = 3) THEN 'تسليم للعميل'\n"
                    + "        END) AS `status` \n"
                    + "FROM toby.pro_factory_delivery pfd left join inv_purchase_invoice ipi on ipi.id = pfd.inv_purchase_invoice_id\n"
                    + "where inv_purchase_invoice_id = " + invPurchaseInvoiceId + "\n"
                    + "union all \n"
                    + "SELECT 'تسليم للعميل' AS `status` \n"
                    + "FROM inv_purchase_invoice ipi \n"
                    + "where id = " + invPurchaseInvoiceId + " and type = 1");
        }
        return (statusList != null && !statusList.isEmpty()) ? statusList.get(statusList.size() - 1) : "";
    }

    @Override
    public List<FactoryInvoiceStatus> findInvoiceStatus(Date dateFrom, Date dateTo, Integer status, Integer gallaryId, Integer branchId) {
        List<FactoryInvoiceStatus> factoryInvoiceStatuseList = new ArrayList<>();
        Map<Integer, FactoryInvoiceStatus> mapList = new HashMap<>();

//        if (status == null || status == 9) {
//            Map<String, Object> params = new HashMap<>();
//            params.put("branchId", branchId);
//            StringBuilder builder = new StringBuilder();
//            builder.append("SELECT i  FROM InvPurchaseInvoice i WHERE  i.branchId.id = :branchId ");
//            if (dateFrom != null) {
//                params.put("dateFrom", dateFrom);
//                builder.append(" AND i.date >= :dateFrom ");
//            }
//            if (dateFrom != null) {
//                params.put("dateTo", dateTo);
//                builder.append(" AND i.date <= :dateTo ");
//            }
//            if (gallaryId != null) {
//                params.put("gallaryId", gallaryId);
//                builder.append(" AND i.galaryId.id = :gallaryId ");
//            }
//            List<InvPurchaseInvoice> details = dao.findListByQuery(builder.toString(), params);
//            for (InvPurchaseInvoice invPurchaseInvoice : details) {
//                FactoryInvoiceStatus factoryInvoiceStatus = new FactoryInvoiceStatus();
//                factoryInvoiceStatus.setEmployee(invPurchaseInvoice.getCreatedBy().getName());
//                factoryInvoiceStatus.setClientName(invPurchaseInvoice.getOrganizationSiteId().getName());
//                factoryInvoiceStatus.setGallery(invPurchaseInvoice.getGalaryId().getName());
//                factoryInvoiceStatus.setInvoiceDate(invPurchaseInvoice.getDate());
//                factoryInvoiceStatus.setInvoiceNumber(invPurchaseInvoice.getSerial());
//                factoryInvoiceStatus.setNumber(invPurchaseInvoice.get);
//                factoryInvoiceStatus.setStage("بالمعرض");
//                factoryInvoiceStatus.setStageDate(invPurchaseInvoice.getDate());
//                factoryInvoiceStatus.setStageTime(invPurchaseInvoice.getDate());
//                mapList.put(invPurchaseInvoice.getSerial(), factoryInvoiceStatus);
//            }
//        }
        Map<String, Object> params1 = new HashMap<>();
        params1.put("branchId", branchId);
        StringBuilder builder1 = new StringBuilder();
        builder1.append("SELECT i  FROM ProFactoryDelivery i WHERE  i.invPurchaseInvoiceId.branchId.id = :branchId ");
        if (dateFrom != null) {
            params1.put("dateFrom", dateFrom);
            builder1.append(" AND i.invPurchaseInvoiceId.date >= :dateFrom ");
        }
        if (dateFrom != null) {
            params1.put("dateTo", dateTo);
            builder1.append(" AND i.invPurchaseInvoiceId.date <= :dateTo ");
        }
        if (gallaryId != null) {
            params1.put("gallaryId", gallaryId);
            builder1.append(" AND i.invPurchaseInvoiceId.galaryId.id = :gallaryId ");
        }
        if (status != null) {
            params1.put("status", status);
            builder1.append(" AND i.type = :status ");
        }

        List<ProFactoryDelivery> res = dao.findListByQuery(builder1.toString(), params1);
        for (ProFactoryDelivery proFactoryDelivery : res) {
            FactoryInvoiceStatus factoryInvoiceStatus = new FactoryInvoiceStatus();
            factoryInvoiceStatus.setEmployee(proFactoryDelivery.getCreatedBy().getName());
            factoryInvoiceStatus.setClientName(proFactoryDelivery.getInvPurchaseInvoiceId().getOrganizationSiteId().getName());
            factoryInvoiceStatus.setGallery(proFactoryDelivery.getInvPurchaseInvoiceId().getGalaryId().getName());
            factoryInvoiceStatus.setInvoiceDate(proFactoryDelivery.getDate());
            factoryInvoiceStatus.setInvoiceNumber(proFactoryDelivery.getSerial());
            factoryInvoiceStatus.setNumber(invoiceMovementReportService.summitionTob(proFactoryDelivery.getInvPurchaseInvoiceId().getId()));

            if (proFactoryDelivery.getType() == 0) {
                factoryInvoiceStatus.setStage("تسليم للمشغل");
            } else if (proFactoryDelivery.getType() == 1) {
                factoryInvoiceStatus.setStage("تسليم للمعرض");
            } else if (proFactoryDelivery.getType() == 2) {
                factoryInvoiceStatus.setStage("استلام بالمعرض ");
            } else {
                factoryInvoiceStatus.setStage("تسليم للعميل");
            }

            factoryInvoiceStatus.setStageDate(proFactoryDelivery.getDate());
            mapList.put(proFactoryDelivery.getSerial(), factoryInvoiceStatus);
        }
        factoryInvoiceStatuseList = new ArrayList<>(mapList.values());

        return factoryInvoiceStatuseList;
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

    @Override // إجماليات الفئات 
    public List<SummitionItemsView> findSummitionQuantity(Integer gallaryId, Integer branchId, Date dateFrom, Date dateTo, Integer groupId, List<InvInventoryDTO> invInventoryDTOs, List<ProGroupDTO> proGroupListSelected, Integer invoiceType) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT "
                + " sum(i.number) , "
                + " sum(i.net)  , "
                + " sum(i.costAvarage)  , "
                + " i.itemId.proGroupId.id  , "
                + " i.itemId.proGroupId.code  , "
                + " i.itemId.proGroupId.name , "
                + " sum(i.netAfterDiscountHead) ");
//        tobNumberFactoryDelivary(builder, dateFrom, params, dateTo, gallaryId, groupId , 0);
//        tobNumberFactoryDelivary(builder, dateFrom, params, dateTo, gallaryId, groupId , 1);
//        tobNumberFactoryDelivary(builder, dateFrom, params, dateTo, gallaryId, groupId , 2);
        builder.append(" FROM InvPurchaseInvoiceDetail i WHERE  i.invPurchaseInvoiceId.branchId.id = :branchId AND i.invPurchaseInvoiceId.type = 4 ");
        if (dateFrom != null) {
            params.put("dateFrom", dateFrom);
            builder.append(" AND i.invPurchaseInvoiceId.date >= :dateFrom ");
        }
        if (dateFrom != null) {
            params.put("dateTo", dateTo);
            builder.append(" AND i.invPurchaseInvoiceId.date <= :dateTo ");
        }
        if (invoiceType != null) {
            params.put("invoiceType", invoiceType);
            builder.append(" and i.invPurchaseInvoiceId.invoiceType = :invoiceType ");
        }

        if (invInventoryDTOs != null && !invInventoryDTOs.isEmpty()) {
            StringBuilder Selected = null;

            for (InvInventoryDTO invInventoryDTO : invInventoryDTOs) {
                if (Selected == null) {
                    Selected = new StringBuilder();
                    Selected.append(invInventoryDTO.getId());
                } else {
                    Selected.append(',').append(invInventoryDTO.getId());
                }
            }
            builder.append(" AND i.invPurchaseInvoiceId.galaryId.id in (").append(Selected.toString()).append(" )");
        } else {
            if (gallaryId != null) {
                params.put("gallaryId", gallaryId);
                builder.append(" AND i.invPurchaseInvoiceId.galaryId.id = :gallaryId ");
            }
        }

        if (proGroupListSelected != null && !proGroupListSelected.isEmpty()) {
            StringBuilder group = null;

            for (ProGroupDTO proGroupDTO : proGroupListSelected) {
                if (group == null) {
                    group = new StringBuilder();
                    group.append(proGroupDTO.getId());
                } else {
                    group.append(',').append(proGroupDTO.getId());
                }
            }
            builder.append(" AND i.itemId.proGroupId.id in (").append(group.toString()).append(" )");
        } else {
            if (groupId != null) {
                params.put("groupId", groupId);
                builder.append(" AND i.itemId.proGroupId.id = :groupId ");
            }
        }

        builder.append(" group by i.itemId.proGroupId.id order by i.itemId.proGroupId.code ");
        List<Object[]> res = dao.findListByQuery(builder.toString(), params);

        return getTheListBasedOnAccClass(res, gallaryId, dateFrom, dateTo, invInventoryDTOs, proGroupListSelected);
    }

    private BigDecimal tobNumberFactoryDelivary(Integer gallaryId, Date dateFrom, Date dateTo, Integer groupId, Integer type, List<InvInventoryDTO> invInventoryDTOs, List<ProGroupDTO> proGroupListSelected) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        params.put("type", type);
        builder.append(" SELECT  sum(pfdd.number) as number FROM ProFactoryDeliveryDetail pfdd "
                + " WHERE pfdd.proFactoryDeliveryId.type = :type ");
        if (dateFrom != null) {
            params.put("dateFrom", dateFrom);
            builder.append(" AND pfdd.proFactoryDeliveryId.date >= :dateFrom ");
        }
        if (dateFrom != null) {
            params.put("dateTo", dateTo);
            builder.append(" AND pfdd.proFactoryDeliveryId.date <= :dateTo ");
        }
        if (invInventoryDTOs != null && !invInventoryDTOs.isEmpty()) {
            StringBuilder Selected = null;
            for (InvInventoryDTO invInventoryDTO : invInventoryDTOs) {
                if (Selected == null) {
                    Selected = new StringBuilder();
                    Selected.append(invInventoryDTO.getId());
                } else {
                    Selected.append(',').append(invInventoryDTO.getId());
                }
            }
            builder.append(" AND pfdd.proFactoryDeliveryId.invPurchaseInvoiceId.galaryId.id in (").append(Selected.toString()).append(" )");
        } else {
            if (gallaryId != null) {
                params.put("gallaryId", gallaryId);
                builder.append(" AND pfdd.proFactoryDeliveryId.invPurchaseInvoiceId.galaryId.id = :gallaryId ");
            }
        }

        if (proGroupListSelected != null && !proGroupListSelected.isEmpty()) {
            StringBuilder group = null;
            for (ProGroupDTO proGroupDTO : proGroupListSelected) {
                if (group == null) {
                    group = new StringBuilder();
                    group.append(proGroupDTO.getId());
                } else {
                    group.append(',').append(proGroupDTO.getId());
                }
            }
            builder.append(" AND pfdd.itemId.proGroupId.id in (").append(group.toString()).append(" )");
        } else {
            if (groupId != null) {
                params.put("groupId", groupId);
                builder.append(" AND pfdd.itemId.proGroupId.id = :groupId ");
            }
        }

//        if (groupId != null) {
//            params.put("groupId", groupId);
//            builder.append(" AND pfdd.itemId.proGroupId.id = :groupId ");
//        }
        builder.append(" group by pfdd.itemId.proGroupId.id ");

        List<BigDecimal> res = dao.findListByQuery(builder.toString(), params);
        return (res != null && !res.isEmpty()) ? res.get(0) : BigDecimal.ZERO;
    }

    public List<SummitionItemsView> getTheListBasedOnAccClass(List<Object[]> res, Integer gallaryId, Date dateFrom, Date dateTo, List<InvInventoryDTO> invInventoryDTOs, List<ProGroupDTO> proGroupListSelected) {

        List<SummitionItemsView> summitionItemsViews = new ArrayList<>();

        Iterator it = res.iterator();
        while (it.hasNext()) {
            Object[] line = (Object[]) it.next();
            SummitionItemsView summitionItemsView = new SummitionItemsView();
//            summitionItemsView.setQuantity((BigDecimal) line[0]);
            summitionItemsView.setNumber((BigDecimal) line[0]);
//            summitionItemsView.setAllQuantity((BigDecimal) line[2]);
            summitionItemsView.setAllCost((BigDecimal) line[1]);
            BigDecimal netAfterDiscountHead = (BigDecimal) line[6];
//            if (netAfterDiscountHead != null && netAfterDiscountHead.compareTo(BigDecimal.ZERO) == 1) {
//                BigDecimal x = netAfterDiscountHead.divide(new BigDecimal(100));
            summitionItemsView.setAllCost(netAfterDiscountHead);
//            }
            summitionItemsView.setAllAvarageCost((BigDecimal) line[2]);
//            summitionItemsView.setAllAvarageCost((BigDecimal) line[4]);
            Integer groupId = (Integer) line[3];
            summitionItemsView.setCode((Integer) line[4]);
            summitionItemsView.setName((String) line[5]);
            summitionItemsView.setFactoryNumber(tobNumberFactoryDelivary(gallaryId, dateFrom, dateTo, groupId, 0, invInventoryDTOs, proGroupListSelected));
            summitionItemsView.setSendNumber(tobNumberFactoryDelivary(gallaryId, dateFrom, dateTo, groupId, 1, invInventoryDTOs, proGroupListSelected));
            summitionItemsView.setDeliveryNumber(tobNumberFactoryDelivary(gallaryId, dateFrom, dateTo, groupId, 2, invInventoryDTOs, proGroupListSelected));
            summitionItemsViews.add(summitionItemsView);
        }
        TreeMap<Integer, SummitionItemsView> params = new TreeMap<>();
        for (SummitionItemsView summitionItemsViewNew : summitionItemsViews) {
            params.put(summitionItemsViewNew.getCode(), summitionItemsViewNew);
        }
        summitionItemsViews = new ArrayList<SummitionItemsView>(params.values());
        return summitionItemsViews;
    }

    @Override
    public List<ClientsNoMovment> findClientsNoMovment(Integer branchId, Integer gallaryId, Date date, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("gallaryId", gallaryId);
        params.put("date", date);
        params.put("type", type);
        List<ClientsNoMovment> list = new ArrayList<>();
        String query = "select ios.code , ios.name , ios.mobile , ipi.date , sum(ifnull(ipid.net,0)) "
                + "from toby.inv_purchase_invoice_detail ipid left join toby.inv_purchase_invoice ipi on \n"
                + "ipi.id = ipid.inv_purchase_invoice_id left join \n"
                + "toby.inv_organization_site ios on ios.id = ipi.organization_site_id\n"
                + "where ipi.organization_site_id is not null and ipi.type = " + type + " and ipi.branch_id = " + branchId + " and \n"
                + "ipi.organization_site_id in (\n"
                + "select id  from toby.inv_organization_site ios2 where ios2.branch_id = " + branchId + " and ios2.id in (\n"
                + "select distinct(ipi2.organization_site_id) from toby.inv_purchase_invoice ipi2 where ipi2.organization_site_id is not null\n"
                + "and ipi2.galary_Id = " + gallaryId + " ) and ios2.id not in (\n"
                + "select distinct(ipi3.organization_site_id) from toby.inv_purchase_invoice ipi3 where ipi3.organization_site_id is not null\n"
                + "and ipi3.galary_Id = " + gallaryId + " and ipi3.date > '" + date + "'\n"
                + "))\n"
                + "group by ipi.organization_site_id order by ios.code , ipi.date asc";
        List<Object[]> res = dao.executeNativeQuery(query);

        list = getTheListClientsNoMovment(res);

        return list;
    }

    public List<ClientsNoMovment> getTheListClientsNoMovment(List<Object[]> res) {

        List<ClientsNoMovment> GeneralJournalDetailsBasedOnAccClassList = new ArrayList<>();

        Iterator it = res.iterator();
        while (it.hasNext()) {
            Object[] line = (Object[]) it.next();
            ClientsNoMovment detailsIterated = new ClientsNoMovment();
            detailsIterated.setClientNumber((String) line[0]);
            detailsIterated.setClientName((String) line[1]);
            detailsIterated.setPhoneNumber((String) line[2]);
            detailsIterated.setLastInvoiceDate((Date) line[3]);
            detailsIterated.setTotalInvoices((BigDecimal) line[4]);
            GeneralJournalDetailsBasedOnAccClassList.add(detailsIterated);
        }
        return GeneralJournalDetailsBasedOnAccClassList;
    }

    @Override
    public List<SummitionItemsView> getTheListBasedOnStagesList(Integer gallaryId, Date dateFrom,
            Date dateTo, Integer branchId, TobyUser tobyUser, List<InvInventoryDTO> invInventoryDTOs, List<ProGroupDTO> proGrouplist) {

//        List<ProGroupDTO> proGrouplist = new ArrayList<>();
//        if (groupId == null) {
//            proGrouplist = proGroupService.getallProGroupByBranchIdDTO(tobyUser);
//        } else {
//            ProGroupDTO dTO = proGroupService.findProGroupDTO(groupId);
//            proGrouplist.add(dTO);
//        }
        List<SummitionItemsView> summitionItemsViews = new ArrayList<>();
        if (proGrouplist != null && !proGrouplist.isEmpty()) {
            for (ProGroupDTO groupDTO : proGrouplist) {
                SummitionItemsView summitionItemsView = new SummitionItemsView();
                summitionItemsView.setCode(groupDTO.getCode());
                summitionItemsView.setName(groupDTO.getName());
                summitionItemsView.setFactoryNumber(tobNumberFactoryDelivary(gallaryId, dateFrom, dateTo, groupDTO.getId(), 0, invInventoryDTOs, null));
                summitionItemsView.setSendNumber(tobNumberFactoryDelivary(gallaryId, dateFrom, dateTo, groupDTO.getId(), 1, invInventoryDTOs, null));
                summitionItemsView.setDeliveryNumber(tobNumberFactoryDelivary(gallaryId, dateFrom, dateTo, groupDTO.getId(), 2, invInventoryDTOs, null));
                summitionItemsView.setCustomerNumber(tobNumberFactoryDelivary(gallaryId, dateFrom, dateTo, groupDTO.getId(), 3, invInventoryDTOs, null));
                if (summitionItemsView.getFactoryNumber().compareTo(BigDecimal.ZERO) == 1
                        || summitionItemsView.getSendNumber().compareTo(BigDecimal.ZERO) == 1
                        || summitionItemsView.getDeliveryNumber().compareTo(BigDecimal.ZERO) == 1) {
                    summitionItemsViews.add(summitionItemsView);
                }

                TreeMap<Integer, SummitionItemsView> params = new TreeMap<>();
                for (SummitionItemsView summitionItemsViewNew : summitionItemsViews) {
                    params.put(summitionItemsViewNew.getCode(), summitionItemsViewNew);
                }
                summitionItemsViews = new ArrayList<SummitionItemsView>(params.values());
            }
        }
        return summitionItemsViews;
    }

    public GeneralJournal tarhil(InvInvoiceDTO dto, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        GeneralJournal journal = null;
        if (dto.getScreenName().equals(ScreenNameClassEnum.PROWORKORDER)) {
            journal = generaljournalService.addGeneralJournalGlobal(dto.getGlYearSelection(), 1013, dto.getSerial(), dto.getDate(), dto.getRemarks(), tobyUser);

            if (dto.getGallaryId() != null && dto.getGallaryId().getCostCenterId() != null && dto.getGallaryId().getCostCenterId().getId() != null) {
                Integer salesTaxAccount = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"SALES_TAX_ACCOUNT\" AND a.accountId IS NOT NULL", params);
                // حساب القيمة المضافة 
                generaljournalDetailsService.addGenDetalil(true, dto.getFinalNet(), journal.getId(),
                        (dto.getOrganizationSiteId() == null || dto.getOrganizationSiteId().getAccountId() == null || dto.getOrganizationSiteId().getAccountId().getId() == null) ? null : dto.getOrganizationSiteId().getAccountId().getId(),
                        (dto.getGallaryId() == null || dto.getGallaryId().getCostCenterId() == null || dto.getGallaryId().getCostCenterId().getId() == null) ? null : dto.getGallaryId().getCostCenterId().getId(),
                        dto.getRemarks(), tobyUser);
//  حساب المبيعات
                Integer salesAccountNumber = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"SALES_ACCOUNT_NUMBER\" AND a.accountId IS NOT NULL", params);
                generaljournalDetailsService.addGenDetalil(false, dto.getTotalNetAfterDiscount(), journal.getId(),salesAccountNumber,
                        (dto.getGallaryId() == null || dto.getGallaryId().getCostCenterId() == null || dto.getGallaryId().getCostCenterId().getId() == null) ? null : dto.getGallaryId().getCostCenterId().getId(),
                        dto.getRemarks(), tobyUser);
                generaljournalDetailsService.addGenDetalil(false, dto.getTaxvalue(), journal.getId(), salesTaxAccount,
                        (dto.getGallaryId() == null || dto.getGallaryId().getCostCenterId() == null || dto.getGallaryId().getCostCenterId().getId() == null) ? null : dto.getGallaryId().getCostCenterId().getId(),
                        dto.getRemarks(), tobyUser);

                Map<String, Object> params2 = new HashMap();
                params2.put("invPurchaseInvoiceId", dto.getId());
                StringBuilder sql = new StringBuilder();
                sql.append("SELECT sum(ipid.costAvarage) FROM InvPurchaseInvoiceDetail ipid  WHERE ipid.invPurchaseInvoiceId.id = :invPurchaseInvoiceId ");

                List<BigDecimal> itemsStores = dao.findListByQuery(sql.toString(), params2);

                // الشيئ الثالث هو انشاء مراكز التكاليف واستدعا مركز تكلفة الإدارة
                Integer managerCostAccount = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"MANAGER_CURRENT_ACCOUNT\" AND a.accountId IS NOT NULL", params);
                Integer costOfSoldGoods = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"COST_OF_GOODS_SOLD_ACCOUNT_NUMBER\" AND a.accountId IS NOT NULL", params);
                Integer storeAccountNumber = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"STORE_ACCOUNT_NUMBER\" AND a.accountId IS NOT NULL", params);

                generaljournalDetailsService.addGenDetalil(true, itemsStores.get(0), journal.getId(),
                        (dto.getGallaryId() == null || dto.getGallaryId().getRunningAccount() == null || dto.getGallaryId().getRunningAccount().getId() == null) ? null : dto.getGallaryId().getRunningAccount().getId(),
                        835, dto.getRemarks(), tobyUser);

                generaljournalDetailsService.addGenDetalil(false, itemsStores.get(0), journal.getId(),
                        storeAccountNumber,
                        835, dto.getRemarks(), tobyUser);

                generaljournalDetailsService.addGenDetalil(true, itemsStores.get(0), journal.getId(),
                        costOfSoldGoods,
                        (dto.getGallaryId() == null || dto.getGallaryId().getCostCenterId() == null || dto.getGallaryId().getCostCenterId().getId() == null) ? null : dto.getGallaryId().getCostCenterId().getId(),
                        dto.getRemarks(), tobyUser);
                generaljournalDetailsService.addGenDetalil(false, itemsStores.get(0), journal.getId(),
                        managerCostAccount,
                        (dto.getGallaryId() == null || dto.getGallaryId().getCostCenterId() == null || dto.getGallaryId().getCostCenterId().getId() == null) ? null : dto.getGallaryId().getCostCenterId().getId(),
                        dto.getRemarks(), tobyUser);

            }

        } else if (dto.getScreenName().equals(ScreenNameClassEnum.INVPURCHASEINVOICEFORM)) {
            journal = generaljournalService.addGeneralJournalGlobal(dto.getGlYearSelection(), 1010, dto.getSerial(), dto.getDate(), dto.getRemarks(), tobyUser);

            Integer storeAccountNumber = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"STORE_ACCOUNT_NUMBER\" AND a.accountId IS NOT NULL", params);
            Integer purchseTaxAccount = (Integer) dao.findEntityByQuery("SELECT a.accountId.id FROM GlAnalyticsAccounts a where a.branchId.id =  :branchId AND a.code = \"PURCHASE_TAX_ACCOUNT\" AND a.accountId IS NOT NULL", params);

            generaljournalDetailsService.addGenDetalil(true, dto.getTotalNetAfterDiscount(), journal.getId(), storeAccountNumber,
                    (dto.getGallaryId() == null || dto.getGallaryId().getCostCenterId() == null || dto.getGallaryId().getCostCenterId().getId() == null) ? null : dto.getGallaryId().getCostCenterId().getId(),
                    dto.getRemarks(), tobyUser);
            generaljournalDetailsService.addGenDetalil(false, dto.getFinalNet(), journal.getId(),
                    (dto.getOrganizationSiteId() == null || dto.getOrganizationSiteId().getAccountId() == null || dto.getOrganizationSiteId().getAccountId().getId() == null) ? null : dto.getOrganizationSiteId().getAccountId().getId(),
                    (dto.getGallaryId() == null || dto.getGallaryId().getCostCenterId() == null || dto.getGallaryId().getCostCenterId().getId() == null) ? null : dto.getGallaryId().getCostCenterId().getId(),
                    dto.getRemarks(), tobyUser);
            generaljournalDetailsService.addGenDetalil(false, dto.getTaxvalue(), journal.getId(), purchseTaxAccount,
                    (dto.getGallaryId() == null || dto.getGallaryId().getCostCenterId() == null || dto.getGallaryId().getCostCenterId().getId() == null) ? null : dto.getGallaryId().getCostCenterId().getId(),
                    dto.getRemarks(), tobyUser);

        }
        return journal == null ? null : journal;
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
                && !ScreenNameClassEnum.INVUPDATE.equals(screenNameClassEnum)
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

}
