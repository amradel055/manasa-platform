/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.bussinessservice.global.InvPurchaseReturnSave;
import com.toby.core.GenericDAO;
import com.toby.dto.CostCenterDTO;
import com.toby.dto.GlAdminUnitDTO;
import com.toby.dto.GlBankDTO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvInventoryTransactionDTO;
import com.toby.dto.InvInventoryTransactionDetailDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.InvPurchaseInvoiceDTO;
import com.toby.dto.InvPurchaseInvoiceDTO1;
import com.toby.dto.InvPurchaseInvoiceDetailDTO;
import com.toby.dto.InventoryDelegatorDTO;
import com.toby.dto.SymbolDTO;
import com.toby.entity.Branch;
import com.toby.entity.CostCenter;
import com.toby.entity.Currency;
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
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import com.toby.views.QuantityItemsStore;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author H
 */
@Stateless
public class InvPurchaseInvoiceImpl implements InvPurchaseInvoiceService {

    @EJB
    private GenericDAO dao;
    @EJB
    InvPurchaseInvoiceDetailService invPurchaseInvoiceDetailService;
    @EJB
    QuantityItemsStoreAddExitService quantityItemsStoreAddExitService;
    @EJB
    private InvInventoryTransactionService invInventoryTransactionService;
    @EJB
    private GenericService genericService;
    @EJB
    private InventorySetupService inventorySetupService;
    @EJB
    private InvPurchaseOrderDetailsService invPurchaseOrderDetailsService;
    @EJB
    private InvPurchaseOrderService invPurchaseOrderService;
    @EJB
    InvPurchaseInvoiceDetailsService invPurchaseInvoiceDetailsService;
    @EJB
    private QuantityItemsStoreByDateService quantityItemsStoreByDateService;
    @EJB
    private InvAddingOrderDetailsService invAddingOrderDetailsService;
    @EJB
    private QuantityItemsStoreService itemsStoreService;
    @EJB
    private InvReservationDetailService invReservationDetailService;
    @EJB
    private InvItemService invItemService;
    @EJB
    private InvQuotationDetailsService invQuotationDetailsService;
    @EJB
    private InvTransferDetailService invTransferDetailService;
    @EJB
    private InvAddingOrderService invAddingOrderService;
    @EJB
    private InvQuotationService invQuotationService;
    @EJB
    private InvReservationService invReservationService;
    @EJB
    private InvTransferService invTransferService;

    InvPurchaseReturnSave invPurchaseReturnSave;

    public List<InvPurchaseInvoice> findInvPurchaseInvoiceforCustomer(Integer customerId) {
        Map<String, Object> params = new HashMap<>();

        params.put("customerId", customerId);
        return dao.findListByQuery("SELECT i FROM InvPurchaseInvoice i WHERE i.branchId.id = :branchId", params);

    }

    @Override
    public InvPurchaseInvoiceDTO1 addInvPurchaseInvoice(InvPurchaseInvoiceDTO1 invPurchaseInvoiceDTO, TobyUser tobyUser, List<InvInventoryDTO> inventoryDTOList) {
        Map<Integer, Map<Integer, BigDecimal>> mapList = new HashMap<>();
        for (InvInventoryDTO inventoryDTO : inventoryDTOList) {
            Map<Integer, BigDecimal> avialableQuantityMap = quantityItemsStoreAddExitService.findInventoryDTOList(tobyUser, inventoryDTO.getId());
            mapList.put(inventoryDTO.getId(), avialableQuantityMap);
        }
        Map<Integer, BigDecimal> quantitiesOfItems = new HashMap<>();
        List<Integer> inventoryId = new ArrayList<>();
        for (InvPurchaseInvoiceDetailDTO detailDTO : invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailDTOList()) {
            if (quantitiesOfItems.get(detailDTO.getItemId().getId()) != null) {
                quantitiesOfItems.put(detailDTO.getItemId().getId(), quantitiesOfItems.get(detailDTO.getItemId().getId()).add(detailDTO.getTotalQuantityRow().multiply(detailDTO.getUnitsItem().getScrewing() != null ? detailDTO.getUnitsItem().getScrewing() : BigDecimal.ONE)));
            } else {
                quantitiesOfItems.put(detailDTO.getItemId().getId(), (detailDTO.getTotalQuantityRow().multiply(detailDTO.getUnitsItem().getScrewing() != null ? detailDTO.getUnitsItem().getScrewing() : BigDecimal.ONE)));
            }
            if (!inventoryId.contains(detailDTO.getInvInventoryDTO().getId())) {
                inventoryId.add(detailDTO.getInvInventoryDTO().getId());
            }
        }
        for (InvPurchaseInvoiceDetailDTO detailDTO : invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailDTOList()) {
            Map<Integer, BigDecimal> avialableQuantityMap = mapList.get(detailDTO.getInvInventoryDTO().getId());
            if (quantitiesOfItems.get(detailDTO.getItemId().getId()).compareTo(avialableQuantityMap.get(detailDTO.getItemId().getId())) > 0) {
                invPurchaseInvoiceDTO.setMsg("كمية هذا الصنف  اكبر من الكمية المو جودة بالمخزن " + detailDTO.getItemId().getName() + " - " + detailDTO.getItemId().getCode() + " الكمية " + quantitiesOfItems.get(detailDTO.getItemId().getId()));
                return invPurchaseInvoiceDTO;
            }
        }
        InvPurchaseInvoice invPurchaseInvoice = mapToEntity(invPurchaseInvoiceDTO, tobyUser);
        invPurchaseInvoice = dao.updateEntity(invPurchaseInvoice);
        InvPurchaseInvoiceDTO1 invPurchaseInvoiceDTO1 = new InvPurchaseInvoiceDTO1();
        invPurchaseInvoiceDTO1.setInvPurchaseInvoiceDetailDTOList(invPurchaseInvoiceDetailService.addInvPurchaseInvoiceDetailList(invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailDTOList(), invPurchaseInvoice.getId(), tobyUser));
        invPurchaseInvoiceDTO1 = mapToDTO(invPurchaseInvoice, tobyUser);
        invPurchaseInvoiceDTO1.setInvPurchaseInvoiceDetailDTOList(invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailDTOList());

        saveInvInventoryTransaction(invPurchaseInvoiceDTO1, inventoryId, tobyUser);
        return invPurchaseInvoiceDTO1;

    }

    public void saveInvInventoryTransaction(InvPurchaseInvoiceDTO1 invPurchaseInvoiceDTO, List<Integer> inventoryId, TobyUser tobyUser) {
        List<InvInventoryTransactionDTO> inventoryTransactionDTOList = new ArrayList<>();
        for (Integer inventory : inventoryId) {

            InvInventoryTransactionDTO invInventoryTransactionDTO1 = new InvInventoryTransactionDTO();
            invInventoryTransactionDTO1.setInvInventoryTransactionDetailDTOList(new ArrayList<InvInventoryTransactionDetailDTO>());
            InvInventoryDTO inventoryDTO = new InvInventoryDTO();
            inventoryDTO.setId(inventory);

            invInventoryTransactionDTO1.setInvInventoryId(inventoryDTO);
            invInventoryTransactionDTO1.setDate(invPurchaseInvoiceDTO.getDate());
            invInventoryTransactionDTO1.setInvpurchaseinvoiceId(invPurchaseInvoiceDTO);
            invInventoryTransactionDTO1.setRemark(invPurchaseInvoiceDTO.getRemarks());
            invInventoryTransactionDTO1.setType(1);

            for (InvPurchaseInvoiceDetailDTO invPurchaseInvoiceDetailDTO : invPurchaseInvoiceDTO.getInvPurchaseInvoiceDetailDTOList()) {
                InvInventoryTransactionDetailDTO inventoryTransactionDetailDTO = new InvInventoryTransactionDetailDTO();
                if (invPurchaseInvoiceDetailDTO.getInvInventoryDTO().getId().equals(inventory)) {
                    inventoryTransactionDetailDTO.setItemId(invPurchaseInvoiceDetailDTO.getItemId());
                    inventoryTransactionDetailDTO.setQuantity(invPurchaseInvoiceDetailDTO.getNumber().multiply(invPurchaseInvoiceDetailDTO.getQuantity()));
                    inventoryTransactionDetailDTO.setScrewing(invPurchaseInvoiceDetailDTO.getScrewing());

                    SymbolDTO symbolDTO = new SymbolDTO();
                    symbolDTO.setId(invPurchaseInvoiceDetailDTO.getUnitsItemsSelected());
                    inventoryTransactionDetailDTO.setUnitId(symbolDTO);

                    invInventoryTransactionDTO1.getInvInventoryTransactionDetailDTOList().add(inventoryTransactionDetailDTO);
                }
            }
            inventoryTransactionDTOList.add(invInventoryTransactionDTO1);
        }
        invInventoryTransactionService.saveInvInventorryTransactionList(inventoryTransactionDTOList, tobyUser);

    }

    //mapToEntity
    public InvPurchaseInvoice mapToEntity(InvPurchaseInvoiceDTO1 invPurchaseInvoiceDTO, TobyUser tobyUser) {

        InvPurchaseInvoice invPurchaseInvoice = new InvPurchaseInvoice();
        invPurchaseInvoice.setDate(invPurchaseInvoiceDTO.getDate());
        invPurchaseInvoice.setDiscount(invPurchaseInvoiceDTO.getDiscount());
        invPurchaseInvoice.setDiscountType(invPurchaseInvoiceDTO.getDiscountType());
        invPurchaseInvoice.setDueDate(invPurchaseInvoiceDTO.getDueDate());
        invPurchaseInvoice.setInvoiceType(invPurchaseInvoiceDTO.getInvoiceType());

        invPurchaseInvoice.setId(invPurchaseInvoiceDTO.getId());
        invPurchaseInvoice.setDueperiod(invPurchaseInvoiceDTO.getDueperiod());
        invPurchaseInvoice.setRecievedate(invPurchaseInvoiceDTO.getRecievedate());
        invPurchaseInvoice.setRemarks(invPurchaseInvoiceDTO.getRemarks());
        invPurchaseInvoice.setIsdeleted(invPurchaseInvoiceDTO.getIsdeleted());
        invPurchaseInvoice.setPostFlag(0);
        invPurchaseInvoice.setPaymentType(0);
        invPurchaseInvoice.setType(1);
        if (invPurchaseInvoiceDTO.getInvInventoryDTO() != null) {
            InvInventory invInventory = new InvInventory();
            invInventory.setId(invPurchaseInvoiceDTO.getInvInventoryDTO().getId());
            invInventory.setName(invPurchaseInvoiceDTO.getInvInventoryDTO().getName());
            invPurchaseInvoice.setInvInventoryId(invInventory);
        }
        if (invPurchaseInvoiceDTO.getOrganizationSiteIdDTO() != null) {
            InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
            invOrganizationSite.setId(invPurchaseInvoiceDTO.getOrganizationSiteIdDTO().getId());
            invOrganizationSite.setName(invPurchaseInvoiceDTO.getOrganizationSiteIdDTO().getName());
            invPurchaseInvoice.setOrganizationSiteId(invOrganizationSite);
        }

        if (invPurchaseInvoiceDTO.getAccountId() != null) {
            GlAccount glAccount = new GlAccount();
            glAccount.setId(invPurchaseInvoiceDTO.getAccountId());
            invPurchaseInvoice.setAccountId(glAccount);
        }
        if (invPurchaseInvoiceDTO.getAdminUnitDTO() != null) {
            GlAdminUnit glAdminUnit = new GlAdminUnit();
            glAdminUnit.setId(invPurchaseInvoiceDTO.getAdminUnitDTO().getId());
            invPurchaseInvoice.setAdminUnitId(glAdminUnit);
        }
        if (invPurchaseInvoiceDTO.getCostCenterDTO() != null) {
            CostCenter costCenter = new CostCenter();
            costCenter.setId(invPurchaseInvoiceDTO.getCostCenterDTO().getId());
            invPurchaseInvoice.setCostCenterId(costCenter);
        }
        if (invPurchaseInvoiceDTO.getCurrencyId() != null) {
            Currency currency = new Currency();
            currency.setId(invPurchaseInvoiceDTO.getCurrencyId());
            invPurchaseInvoice.setCurrencyId(currency);
        }
        if (invPurchaseInvoiceDTO.getGlBankId() != null) {
            GlBank glBank = new GlBank();
            glBank.setId(invPurchaseInvoiceDTO.getGlBankId().getId());
            invPurchaseInvoice.setGlBankId(glBank);
        }
        if (invPurchaseInvoiceDTO.getInventoryDelegatorDTO() != null) {
            InventoryDelegator inventoryDelegator = new InventoryDelegator();
            inventoryDelegator.setId(invPurchaseInvoiceDTO.getInventoryDelegatorDTO().getId());
            invPurchaseInvoice.setInvDelegatorId(inventoryDelegator);
        }
        if (invPurchaseInvoiceDTO.getGallary() != null) {
            InvInventory invInventory = new InvInventory();
            invInventory.setId(invPurchaseInvoiceDTO.getGallary().getId());
            invPurchaseInvoice.setInvInventoryId(invInventory);
        }
        if (invPurchaseInvoiceDTO.getCreatedBy() != null) {
            invPurchaseInvoice.setRecieved(invPurchaseInvoiceDTO.getRecieved());
            TobyUser user = new TobyUser();
            user.setId(invPurchaseInvoiceDTO.getCreatedBy());
            invPurchaseInvoice.setCreatedBy(user);
            invPurchaseInvoice.setCreationDate(invPurchaseInvoiceDTO.getCreatedDate());
            invPurchaseInvoice.setModifiedBy(tobyUser);
            invPurchaseInvoice.setModificationDate(new Date());
            if (invPurchaseInvoiceDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(invPurchaseInvoiceDTO.getCompanyId());
                invPurchaseInvoice.setCompanyId(company);
            }

            if (invPurchaseInvoiceDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(invPurchaseInvoiceDTO.getBranchId());
                invPurchaseInvoice.setBranchId(branch);
            }
        } else {
            invPurchaseInvoice.setCreatedBy(tobyUser);
            invPurchaseInvoice.setCreationDate(new Date());
            invPurchaseInvoice.setCompanyId(tobyUser.getCompanyId());
            invPurchaseInvoice.setBranchId(tobyUser.getBranchId());
        }

        /// Get Serial ///
        if (invPurchaseInvoiceDTO.getId() == null) {
            Map<String, Object> params = new HashMap<>();
            params.put("branchId", tobyUser.getBranchId().getId());
            Integer serialmax = 0;
            synchronized (serialmax) {
                try {
                    serialmax = dao.findEntityByQuery("SELECT MAX(g.serial) FROM InvPurchaseInvoice g  WHERE g.branchId.id =:branchId ", params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (serialmax == null) {
                serialmax = 0;
            }
            invPurchaseInvoice.setSerial(serialmax + 1);
        } else {
            invPurchaseInvoice.setSerial(invPurchaseInvoiceDTO.getSerial());
        }
        return invPurchaseInvoice;
    }

    public InvPurchaseInvoiceDTO1 mapToDTO(InvPurchaseInvoice invPurchaseInvoice, TobyUser tobyUser) {

        InvPurchaseInvoiceDTO1 invPurchaseInvoiceDTO = new InvPurchaseInvoiceDTO1();
        invPurchaseInvoiceDTO.setCreatedBy(invPurchaseInvoice.getCreatedBy() != null ? invPurchaseInvoice.getCreatedBy().getId() : null);
        invPurchaseInvoiceDTO.setCreatedDate(invPurchaseInvoice.getCreationDate());
        invPurchaseInvoiceDTO.setBranchId(invPurchaseInvoice.getBranchId() != null ? invPurchaseInvoice.getBranchId().getId() : null);
        invPurchaseInvoiceDTO.setDate(invPurchaseInvoice.getDate());
        invPurchaseInvoiceDTO.setDiscount(invPurchaseInvoice.getDiscount());
        invPurchaseInvoiceDTO.setDiscountType(invPurchaseInvoice.getDiscountType());
        invPurchaseInvoiceDTO.setDueDate(invPurchaseInvoice.getDueDate());
        invPurchaseInvoiceDTO.setId(invPurchaseInvoice.getId());
        invPurchaseInvoiceDTO.setPaymentType(invPurchaseInvoice.getPaymentType());
        invPurchaseInvoiceDTO.setPostFlag(invPurchaseInvoice.getPostFlag());
        invPurchaseInvoiceDTO.setSerial(invPurchaseInvoice.getSerial());
        invPurchaseInvoiceDTO.setIndex(invPurchaseInvoice.getSerial());
        invPurchaseInvoiceDTO.setRecievedate(invPurchaseInvoice.getRecievedate());
        invPurchaseInvoiceDTO.setDueperiod(invPurchaseInvoice.getDueperiod());
        invPurchaseInvoiceDTO.setRecieved(invPurchaseInvoice.getRecieved());
        invPurchaseInvoiceDTO.setRemarks(invPurchaseInvoice.getRemarks());
        invPurchaseInvoiceDTO.setIsdeleted(invPurchaseInvoice.getIsdeleted());
        invPurchaseInvoiceDTO.setInvoiceType(invPurchaseInvoice.getInvoiceType());
//        invPurchaseInvoiceDTO.setType(invPurchaseInvoice.getType());

        invPurchaseInvoiceDTO.setCompanyId(invPurchaseInvoice.getCompanyId().getId() != null ? invPurchaseInvoice.getCompanyId().getId() : null);
        invPurchaseInvoiceDTO.setCreatedByName(invPurchaseInvoice.getCreatedBy() != null ? invPurchaseInvoice.getCreatedBy().getName() : null);

        if (invPurchaseInvoice.getOrganizationSiteId() != null) {
            InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
            invOrganizationSiteDTO.setId(invPurchaseInvoice.getOrganizationSiteId().getId());
            invOrganizationSiteDTO.setName(invPurchaseInvoice.getOrganizationSiteId().getName());

            invPurchaseInvoiceDTO.setOrganizationSiteIdDTO(invOrganizationSiteDTO);
        }

        if (invPurchaseInvoice.getAccountId() != null) {
            GlAccount glAccount = new GlAccount();
            glAccount.setId(invPurchaseInvoice.getAccountId().getId());
            invPurchaseInvoiceDTO.setAccountId(glAccount.getId());
        }
        if (invPurchaseInvoice.getAdminUnitId() != null) {
            GlAdminUnitDTO adminUnitDTO = new GlAdminUnitDTO();
            adminUnitDTO.setId(invPurchaseInvoice.getAdminUnitId().getId());
            adminUnitDTO.setName(invPurchaseInvoice.getAdminUnitId().getName());
            adminUnitDTO.setCode(invPurchaseInvoice.getAdminUnitId().getCode());
            invPurchaseInvoiceDTO.setAdminUnitDTO(adminUnitDTO);
        }
        if (invPurchaseInvoice.getCostCenterId() != null) {
            CostCenterDTO centerDTO = new CostCenterDTO();
            centerDTO.setId(invPurchaseInvoice.getCostCenterId().getId());
            centerDTO.setName(invPurchaseInvoice.getCostCenterId().getName());
            centerDTO.setCode(invPurchaseInvoice.getCostCenterId().getCode());
            invPurchaseInvoiceDTO.setCostCenterDTO(centerDTO);
        }
        if (invPurchaseInvoice.getCurrencyId() != null) {
            Currency currency = new Currency();
            currency.setId(invPurchaseInvoice.getCurrencyId().getId());
            invPurchaseInvoiceDTO.setCurrencyId(currency.getId());
        }
        if (invPurchaseInvoice.getGlBankId() != null) {
            GlBankDTO glBankDTO = new GlBankDTO();
            glBankDTO.setId(invPurchaseInvoice.getGlBankId().getId());
            glBankDTO.setName(invPurchaseInvoice.getGlBankId().getName());
            glBankDTO.setCode(invPurchaseInvoice.getGlBankId().getCode());
            invPurchaseInvoiceDTO.setGlBankId(glBankDTO);
        }
        if (invPurchaseInvoice.getInvDelegatorId() != null) {
            InventoryDelegatorDTO inventoryDelegatorDTO = new InventoryDelegatorDTO();
            inventoryDelegatorDTO.setId(invPurchaseInvoice.getInvDelegatorId().getId());
            inventoryDelegatorDTO.setName(invPurchaseInvoice.getInvDelegatorId().getName());
            inventoryDelegatorDTO.setCode(invPurchaseInvoice.getInvDelegatorId().getCode());
            invPurchaseInvoiceDTO.setInventoryDelegatorDTO(inventoryDelegatorDTO);
        }
        if (invPurchaseInvoice.getInvInventoryId() != null) {
            InvInventoryDTO invInventoryDTO = new InvInventoryDTO();
            invInventoryDTO.setId(invPurchaseInvoice.getInvInventoryId().getId());
            invInventoryDTO.setName(invPurchaseInvoice.getInvInventoryId().getName());
            invInventoryDTO.setCode(invPurchaseInvoice.getInvInventoryId().getCode());
            invPurchaseInvoiceDTO.setGallary(invInventoryDTO);
        }
        return invPurchaseInvoiceDTO;
    }

    public InvPurchaseInvoiceDTO1 mapToDTOForProFactoryDelivery(InvPurchaseInvoice invPurchaseInvoice, TobyUser tobyUser) {

        InvPurchaseInvoiceDTO1 invPurchaseInvoiceDTO = new InvPurchaseInvoiceDTO1();
        invPurchaseInvoiceDTO.setCreatedBy(invPurchaseInvoice.getCreatedBy() != null ? invPurchaseInvoice.getCreatedBy().getId() : null);
        invPurchaseInvoiceDTO.setCreatedDate(invPurchaseInvoice.getCreationDate());
        invPurchaseInvoiceDTO.setBranchId(invPurchaseInvoice.getBranchId() != null ? invPurchaseInvoice.getBranchId().getId() : null);
        invPurchaseInvoiceDTO.setDate(invPurchaseInvoice.getDate());
        invPurchaseInvoiceDTO.setDiscount(invPurchaseInvoice.getDiscount());
        invPurchaseInvoiceDTO.setDiscountType(invPurchaseInvoice.getDiscountType());
        invPurchaseInvoiceDTO.setDueDate(invPurchaseInvoice.getDueDate());
        invPurchaseInvoiceDTO.setId(invPurchaseInvoice.getId());
        invPurchaseInvoiceDTO.setPaymentType(invPurchaseInvoice.getPaymentType());
        invPurchaseInvoiceDTO.setPostFlag(invPurchaseInvoice.getPostFlag());
        invPurchaseInvoiceDTO.setSerial(invPurchaseInvoice.getSerial());
        invPurchaseInvoiceDTO.setIndex(invPurchaseInvoice.getSerial());
        invPurchaseInvoiceDTO.setRecievedate(invPurchaseInvoice.getRecievedate());
        invPurchaseInvoiceDTO.setDueperiod(invPurchaseInvoice.getDueperiod());
        invPurchaseInvoiceDTO.setRecieved(invPurchaseInvoice.getRecieved());
        invPurchaseInvoiceDTO.setRemarks(invPurchaseInvoice.getRemarks());
        invPurchaseInvoiceDTO.setIsdeleted(invPurchaseInvoice.getIsdeleted());
        invPurchaseInvoiceDTO.setInvoiceType(invPurchaseInvoice.getInvoiceType());
//        invPurchaseInvoiceDTO.setType(invPurchaseInvoice.getType());

        invPurchaseInvoiceDTO.setCompanyId(invPurchaseInvoice.getCompanyId().getId() != null ? invPurchaseInvoice.getCompanyId().getId() : null);
        invPurchaseInvoiceDTO.setCreatedByName(invPurchaseInvoice.getCreatedBy() != null ? invPurchaseInvoice.getCreatedBy().getName() : null);

        if (invPurchaseInvoice.getOrganizationSiteId() != null) {
            InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
            invOrganizationSiteDTO.setId(invPurchaseInvoice.getOrganizationSiteId().getId());
            invOrganizationSiteDTO.setName(invPurchaseInvoice.getOrganizationSiteId().getName());

            invPurchaseInvoiceDTO.setOrganizationSiteIdDTO(invOrganizationSiteDTO);
        }

        if (invPurchaseInvoice.getAccountId() != null) {
            GlAccount glAccount = new GlAccount();
            glAccount.setId(invPurchaseInvoice.getAccountId().getId());
            invPurchaseInvoiceDTO.setAccountId(glAccount.getId());
        }
        if (invPurchaseInvoice.getAdminUnitId() != null) {
            GlAdminUnitDTO adminUnitDTO = new GlAdminUnitDTO();
            adminUnitDTO.setId(invPurchaseInvoice.getAdminUnitId().getId());
            adminUnitDTO.setName(invPurchaseInvoice.getAdminUnitId().getName());
            adminUnitDTO.setCode(invPurchaseInvoice.getAdminUnitId().getCode());
            invPurchaseInvoiceDTO.setAdminUnitDTO(adminUnitDTO);
        }
        if (invPurchaseInvoice.getCostCenterId() != null) {
            CostCenterDTO centerDTO = new CostCenterDTO();
            centerDTO.setId(invPurchaseInvoice.getCostCenterId().getId());
            centerDTO.setName(invPurchaseInvoice.getCostCenterId().getName());
            centerDTO.setCode(invPurchaseInvoice.getCostCenterId().getCode());
            invPurchaseInvoiceDTO.setCostCenterDTO(centerDTO);
        }
        if (invPurchaseInvoice.getCurrencyId() != null) {
            Currency currency = new Currency();
            currency.setId(invPurchaseInvoice.getCurrencyId().getId());
            invPurchaseInvoiceDTO.setCurrencyId(currency.getId());
        }
        if (invPurchaseInvoice.getGlBankId() != null) {
            GlBankDTO glBankDTO = new GlBankDTO();
            glBankDTO.setId(invPurchaseInvoice.getGlBankId().getId());
            glBankDTO.setName(invPurchaseInvoice.getGlBankId().getName());
            glBankDTO.setCode(invPurchaseInvoice.getGlBankId().getCode());
            invPurchaseInvoiceDTO.setGlBankId(glBankDTO);
        }
        if (invPurchaseInvoice.getInvDelegatorId() != null) {
            InventoryDelegatorDTO inventoryDelegatorDTO = new InventoryDelegatorDTO();
            inventoryDelegatorDTO.setId(invPurchaseInvoice.getInvDelegatorId().getId());
            inventoryDelegatorDTO.setName(invPurchaseInvoice.getInvDelegatorId().getName());
            inventoryDelegatorDTO.setCode(invPurchaseInvoice.getInvDelegatorId().getCode());
            invPurchaseInvoiceDTO.setInventoryDelegatorDTO(inventoryDelegatorDTO);
        }
        if (invPurchaseInvoice.getInvInventoryId() != null) {
            InvInventoryDTO invInventoryDTO = new InvInventoryDTO();
            invInventoryDTO.setId(invPurchaseInvoice.getInvInventoryId().getId());
            invInventoryDTO.setName(invPurchaseInvoice.getInvInventoryId().getName());
            invInventoryDTO.setCode(invPurchaseInvoice.getInvInventoryId().getCode());
            invPurchaseInvoiceDTO.setGallary(invInventoryDTO);
        }
        return invPurchaseInvoiceDTO;
    }

    public List<InvPurchaseInvoiceDTO1> mapToDTOList(List<InvPurchaseInvoice> invPurchaseInvoiceList, TobyUser tobyUser) {
        List<InvPurchaseInvoiceDTO1> invPurchaseInvoiceDTOList = new ArrayList<>();
        for (InvPurchaseInvoice invPurchaseInvoice : invPurchaseInvoiceList) {
            invPurchaseInvoiceDTOList.add(mapToDTO(invPurchaseInvoice, tobyUser));
        }
        return invPurchaseInvoiceDTOList;
    }

    @Override
    public List<InvPurchaseInvoiceDTO1> findInvPurchaseInvoicefordelgatorDTO(TobyUser tobyUser) {

        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());         //////  AND ((e.type = 0 OR e.type = 2) AND e.active = 1) ORDER BY e.code
        List<InvPurchaseInvoice> detailses = dao.findListByQuery("SELECT e FROM InventoryDelegator e WHERE e.branchId.id = :branchId ", params);
        return mapToDTOList(detailses, tobyUser);
    }

    @Override
    public List<InvPurchaseInvoiceDTO1> getlistPURCHASEORNO(TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());         //////  AND ((e.type = 0 OR e.type = 2) AND e.active = 1) ORDER BY e.code
        List<InvPurchaseInvoice> detailses = dao.findListByQuery("SELECT e FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId ", params);
        return mapToDTOList(detailses, tobyUser);
    }

    @Override
    public List<InvPurchaseInvoiceDTO1> findInvPurchaseInvoiceDTOList(TobyUser tobyUser) {

        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        List<InvPurchaseInvoice> invPurchaseInvoiceList = dao.findListByQuery("SELECT e FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId AND (e.type = 1 and( e.isdeleted != 1 or e.isdeleted is NULL )) order by e.serial desc", params);
        return mapToDTOList(invPurchaseInvoiceList, tobyUser);
    }

    @Override
    public List<InvPurchaseInvoice> findInvPurchaseInvoiceList(TobyUser tobyUser) {

        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        return dao.findListByQuery("SELECT e FROM InvPurchaseInvoice e WHERE e.branchId.id = :branchId AND (e.type = 1 and( e.isdeleted != 1 or e.isdeleted is NULL )) order by e.serial desc", params);

    }

    @Override
    public List<InvPurchaseInvoiceDTO1> findInvPurchaseInvoiceDTOListByReceved(TobyUser tobyUser) {

        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        //        SELECT * FROM toby.inv_purchase_invoice where ((id not in (select inv_purchaseinvoice_Id from inv_inventory_transaction  where inv_purchaseinvoice_Id is not null and branch_id = 81) and branch_id = 81)
        //        or(proof = 1 and (recieved is null or recieved = 0) and customer_accept = 1 ));
        List<InvPurchaseInvoice> invPurchaseInvoiceList = dao.findListByQuery("SELECT e FROM InvPurchaseInvoice e WHERE ((e.id not in (select g.invpurchaseinvoiceId.id from InvInventoryTransaction g  where g.invpurchaseinvoiceId != null and g.branchId.id =:branchId) and e.branchId.id =:branchId)or(e.proof = 1 and (e.recieved =null or e.recieved = 0) and e.customeraccept = 1 ))", params);
        return mapToDTOList(invPurchaseInvoiceList, tobyUser);
    }

    @Override
    public List<InvPurchaseInvoiceDTO1> getPurchaseIdFromView(TobyUser tobyUser) {

        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        List<InvPurchaseInvoice> invPurchaseInvoiceList = dao.findListByQuery("SELECT e FROM InvPurchaseInvoice e WHERE (e.id in(select f.invoiceId from FindPurchaseNotFinishView f )and e.branchId.id =:branchId)  ", params);
        return mapToDTOList(invPurchaseInvoiceList, tobyUser);
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
    public InvPurchaseInvoiceDTO1 findInvPurchaseInvoiceDTO(Integer invPurchaseInvoiceDTOId, TobyUser tobyUser) {
        InvPurchaseInvoice invPurchaseInvoice = dao.findEntityById(InvPurchaseInvoice.class, invPurchaseInvoiceDTOId);
        return mapToDTO(invPurchaseInvoice, tobyUser);
    }

    @Override
    public InvPurchaseInvoiceDTO1 deleteInvPurchaseInvoiceDTO(InvPurchaseInvoiceDTO1 invPurchaseInvoiceDTO, TobyUser tobyUser) {
        InvPurchaseInvoice invPurchaseInvoice = mapToEntity(invPurchaseInvoiceDTO, tobyUser);
        invPurchaseInvoice.setIsdeleted(1);
        dao.updateEntity(invPurchaseInvoice);
        invPurchaseInvoiceDetailService.deleteInvPurchaseInvoiceDetailDTO(invPurchaseInvoice, tobyUser);
        return mapToDTO(invPurchaseInvoice, tobyUser);

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
    public InvPurchaseInvoice findInvPurchaseInvoiceById(Integer invPurchaseInvoiceId) {
        InvPurchaseInvoice invPurchaseInvoice = dao.findEntityById(InvPurchaseInvoice.class, invPurchaseInvoiceId);
        return invPurchaseInvoice;
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
//                Integer serialTax = getMaxSerialTaxForInvoice(invPurchaseInvoice.getBranchId().getId(), invPurchaseInvoice.getInvInventoryId().getId(), invPurchaseInvoice.getType(), invPurchaseInvoice.getTaxFlagFinal() == null ? Boolean.FALSE : invPurchaseInvoice.getTaxFlagFinal());
//                invPurchaseInvoice.setSerialTax(serialTax);
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
    public synchronized Integer getMaxSerialTaxForInvoice(Integer branchId, Integer inventoryId, Boolean type, Boolean taxFalgFinal) {
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
    public void deleteInvPurchaseInvoice(InvPurchaseInvoice invPurchaseInvoice, Integer isPurchaseOrSales) {
        if (invPurchaseInvoice.getId() != null && invPurchaseInvoice.getId() > 0) {
            List<InvPurchaseInvoiceDetail> detailList = invPurchaseInvoiceDetailsService.getAllInvPurchaseInvoiceDetailsByInvPurchaseInvoiceId(invPurchaseInvoice.getId());
            deleteDetails(detailList, isPurchaseOrSales);
            dao.deleteEntity(invPurchaseInvoice);
        }
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

    public Integer restorDetailStatus(BigDecimal quantity, BigDecimal finalQuantity) {
        if (quantity.compareTo(finalQuantity) == 0) {
            return 0;
        } else if (finalQuantity.compareTo(BigDecimal.ZERO) == 1 && quantity.compareTo(finalQuantity) == 1) {
            return 1;
        } else {
            return 2;
        }

    }

    private QuantityItemsStore findQuantityItemNow(InvPurchaseInvoice invPurchaseInvoice, InvPurchaseInvoiceDetail invPurchaseInvoiceDetail, InventorySetup invSetup) {
        QuantityItemsStore quantityItemsStore;
        quantityItemsStore = itemsStoreService.findInventoryItem(invPurchaseInvoice.getInvInventoryId().getId(),
                invPurchaseInvoice.getBranchId().getId(), invPurchaseInvoiceDetail.getItemId().getId(), invSetup.getSellBuy());
        return quantityItemsStore;
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
    public List<InvPurchaseInvoice> findInvPurchaseInfvoiceforCustomer(Integer customerId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



}
