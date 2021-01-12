/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import com.toby.core.GenericDAO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvInventoryTransactionDTO;
import com.toby.dto.InvInventoryTransactionDetailDTO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.InvPurchaseInvoiceDTO1;
import com.toby.dto.InvPurchaseOrderDTO;
import com.toby.dto.SymbolDTO;
import com.toby.entity.Branch;
import com.toby.entity.InvInventory;
import com.toby.entity.InvInventoryTransaction;
import com.toby.entity.InvItem;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.InvPurchaseInvoice;
import com.toby.entity.InvPurchaseInvoiceDetail;
import com.toby.entity.InvPurchaseOrder;
import com.toby.entity.QuantityItemNotFinish;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author user4
 */
@Stateless
public class InvInventoryTransactionServiceImpl implements InvInventoryTransactionService {

    @EJB
    GenericDAO dao;
    @EJB
    InvPurchaseInvoiceDetailService invPurchaseInvoiceDetailService;
    @EJB
    InvInventoryTransactionDetailService invInventoryTransactionDetailService;
    @EJB
    QuantityItemsStoreAddExitService quantityItemsStoreAddExitService;

    @Override
    public InvInventoryTransactionDTO saveInvInventorryTransaction(InvInventoryTransactionDTO invInventoryTransactionDTO, TobyUser tobyUser) {
        //   Map<Integer, BigDecimal> m = quantityItemsStoreAddExitService.findInventoryDTOList(tobyUser, invInventoryTransactionDTO.getInvInventoryId().getId());
//        for (InvInventoryTransactionDetailDTO invInventoryTransactionDetailDTO : invInventoryTransactionDTO.getInvInventoryTransactionDetailDTOList()) {
//            if (invInventoryTransactionDetailDTO.getQuantity().compareTo(m.get(invInventoryTransactionDetailDTO.getItemId().getId())) > 0) {
//                invInventoryTransactionDTO.setMsg("هذه الكميه مش موجوده في المخزن " + invInventoryTransactionDetailDTO.getItemId().getName() + " - " + invInventoryTransactionDetailDTO.getItemId().getCode());
//                return invInventoryTransactionDTO;
//
//            }
//        }
        InvInventoryTransaction invInventoryTransaction = mapToEntity(invInventoryTransactionDTO, tobyUser);
        invInventoryTransaction = dao.updateEntity(invInventoryTransaction);
        invInventoryTransactionDTO.setInvInventoryTransactionDetailDTOList(invInventoryTransactionDetailService.savainvInventoryTransactionDetailDTO(invInventoryTransactionDTO.getInvInventoryTransactionDetailDTOList(), invInventoryTransaction.getId(), tobyUser));

        return invInventoryTransactionDTO;

    }

    @Override
    public void saveInvInventorryTransactionList(List<InvInventoryTransactionDTO> invInventoryTransactionDTOList, TobyUser tobyUser) {
        for (InvInventoryTransactionDTO dTO : invInventoryTransactionDTOList) {
            saveInvInventorryTransaction(dTO, tobyUser);
        }

    }

    public InvInventoryTransaction mapToEntity(InvInventoryTransactionDTO invInventoryTransactionDTO, TobyUser tobyUser) {
        InvInventoryTransaction invInventoryTransaction = new InvInventoryTransaction();
        invInventoryTransaction.setId(invInventoryTransactionDTO.getId());
//        invInventoryTransaction.setCreationDate(invInventoryTransactionDTO.getCreatedDate());
        invInventoryTransaction.setDate(invInventoryTransactionDTO.getDate());
        invInventoryTransaction.setInvDelegatorId(invInventoryTransactionDTO.getInvDelegatorId());
        invInventoryTransaction.setMarkDisable(invInventoryTransactionDTO.getMarkDisable());
        invInventoryTransaction.setMarkEdit(invInventoryTransactionDTO.getMarkDisable());
        invInventoryTransaction.setMsg(invInventoryTransactionDTO.getMsg());
        invInventoryTransaction.setPostFlag(0);
        invInventoryTransaction.setRemark(invInventoryTransactionDTO.getRemark());
        invInventoryTransaction.setSerial(invInventoryTransactionDTO.getSerial());
        invInventoryTransaction.setStatus(invInventoryTransactionDTO.getStatus());
        invInventoryTransaction.setSupplierDate(invInventoryTransactionDTO.getSupplierDate());
        invInventoryTransaction.setSupplierInvoice(invInventoryTransactionDTO.getSupplierInvoice());
        invInventoryTransaction.setType(invInventoryTransactionDTO.getType());
        invInventoryTransaction.setIsdeleted(invInventoryTransactionDTO.getIsdeleted());
        if (invInventoryTransactionDTO.getInvpurchaseinvoiceId() != null) {
            InvPurchaseInvoice invPurchaseInvoice = new InvPurchaseInvoice();
            invPurchaseInvoice.setId(invInventoryTransactionDTO.getInvpurchaseinvoiceId().getId());
            invInventoryTransaction.setInvpurchaseinvoiceId(invPurchaseInvoice);
        }
        if (invInventoryTransactionDTO.getPurchaseOrderId() != null) {
            InvPurchaseOrder invPurchaseOrder = new InvPurchaseOrder();
            invPurchaseOrder.setId(invInventoryTransactionDTO.getPurchaseOrderId().getId());
            invInventoryTransaction.setPurchaseOrderId(invPurchaseOrder);
        }
        if (invInventoryTransactionDTO.getOrganizationSiteId() != null) {
            InvOrganizationSite invOrganizationSite = new InvOrganizationSite();
            invOrganizationSite.setId(invInventoryTransactionDTO.getOrganizationSiteId().getId());
            invInventoryTransaction.setOrganizationSiteId(invOrganizationSite);
        }
        if (invInventoryTransactionDTO.getInvInventoryId() != null) {
            InvInventory invInventory = new InvInventory();
            invInventory.setId(invInventoryTransactionDTO.getInvInventoryId().getId());
            invInventoryTransaction.setInvInventoryId(invInventory);
        }
        if (invInventoryTransactionDTO.getPurchaseOrderId() != null) {
            InvPurchaseOrder invPurchaseOrder = new InvPurchaseOrder();
            invPurchaseOrder.setId(invInventoryTransactionDTO.getPurchaseOrderId().getId());
            invInventoryTransaction.setPurchaseOrderId(invPurchaseOrder);
        }
        if (invInventoryTransactionDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(invInventoryTransactionDTO.getCreatedBy());
            invInventoryTransaction.setCreatedBy(user);
            invInventoryTransaction.setCreationDate(invInventoryTransactionDTO.getCreatedDate());
            invInventoryTransaction.setModifiedBy(tobyUser);
            invInventoryTransaction.setModificationDate(new Date());
            if (invInventoryTransactionDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(invInventoryTransactionDTO.getCompanyId());
                invInventoryTransaction.setCompanyId(company);
            }
            if (invInventoryTransactionDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(invInventoryTransactionDTO.getBranchId());
                invInventoryTransaction.setBranchId(branch);
            }
        } else {
            invInventoryTransaction.setCreatedBy(tobyUser);
            invInventoryTransaction.setCreationDate(new Date());
            invInventoryTransaction.setCompanyId(tobyUser.getCompanyId());
            invInventoryTransaction.setBranchId(tobyUser.getBranchId());

        }
        if (invInventoryTransactionDTO.getId() == null) {
            Map<String, Object> Gserial = new HashMap<>();
            Gserial.put("branchId", tobyUser.getBranchId().getId());
            Integer S = 0;
            synchronized (S) {
                try {
                    S = dao.findEntityByQuery("SELECT MAX(e.serial) FROM InvInventoryTransaction e  WHERE e.branchId.id =:branchId ", Gserial);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (S == null) {
                S = 0;

            }
            invInventoryTransaction.setSerial(S + 1);
        }
        return invInventoryTransaction;
    }

    //mapToDTO
    public InvInventoryTransactionDTO mapToDTO(InvInventoryTransaction invInventoryTransaction) {

        InvInventoryTransactionDTO invInventoryTransactionDTO = new InvInventoryTransactionDTO();
        invInventoryTransactionDTO.setDate(invInventoryTransaction.getDate());
        invInventoryTransactionDTO.setId(invInventoryTransaction.getId());
        invInventoryTransactionDTO.setCreatedDate(invInventoryTransaction.getCreationDate());
        invInventoryTransactionDTO.setInvDelegatorId(invInventoryTransaction.getInvDelegatorId());
        invInventoryTransactionDTO.setMarkDisable(invInventoryTransaction.getMarkDisable());
        invInventoryTransactionDTO.setMarkEdit(invInventoryTransaction.getMarkEdit());
        invInventoryTransactionDTO.setMsg(invInventoryTransaction.getMsg());
        invInventoryTransactionDTO.setPostFlag(invInventoryTransaction.getPostFlag());
        invInventoryTransactionDTO.setRemark(invInventoryTransaction.getRemark());
        invInventoryTransactionDTO.setSerial(invInventoryTransaction.getSerial());
        invInventoryTransactionDTO.setStatus(invInventoryTransaction.getStatus());
        invInventoryTransactionDTO.setSupplierDate(invInventoryTransaction.getSupplierDate());
        invInventoryTransactionDTO.setSupplierInvoice(invInventoryTransaction.getSupplierInvoice());
        invInventoryTransactionDTO.setType(invInventoryTransaction.getType());
        invInventoryTransactionDTO.setIsdeleted(invInventoryTransaction.getIsdeleted());
        invInventoryTransactionDTO.setIndex(invInventoryTransaction.getSerial());

        invInventoryTransactionDTO.setCreatedBy(invInventoryTransaction.getCreatedBy() != null ? invInventoryTransaction.getCreatedBy().getId() : null);
        invInventoryTransactionDTO.setCreatedDate(invInventoryTransaction.getCreationDate());
        invInventoryTransactionDTO.setBranchId(invInventoryTransaction.getBranchId().getId() != null ? invInventoryTransaction.getBranchId().getId() : null);
        invInventoryTransactionDTO.setCompanyId(invInventoryTransaction.getCompanyId() != null ? invInventoryTransaction.getCompanyId().getId() : null);
        if (invInventoryTransaction.getInvpurchaseinvoiceId() != null) {
            InvPurchaseInvoiceDTO1 invPurchaseInvoiceDTO = new InvPurchaseInvoiceDTO1();
            invPurchaseInvoiceDTO.setId(invInventoryTransaction.getInvpurchaseinvoiceId().getId());
            invInventoryTransactionDTO.setInvpurchaseinvoiceId(invPurchaseInvoiceDTO);
        }
        if (invInventoryTransaction.getPurchaseOrderId() != null) {
            InvPurchaseOrderDTO invPurchaseOrderDTO = new InvPurchaseOrderDTO();
            invPurchaseOrderDTO.setId(invInventoryTransaction.getPurchaseOrderId().getId());
            invInventoryTransactionDTO.setPurchaseOrderId(invPurchaseOrderDTO);
        }
        if (invInventoryTransaction.getOrganizationSiteId() != null) {
            InvOrganizationSiteDTO invOrganizationSiteDTO = new InvOrganizationSiteDTO();
            invOrganizationSiteDTO.setId(invInventoryTransaction.getOrganizationSiteId().getId());
            invInventoryTransactionDTO.setOrganizationSiteId(invOrganizationSiteDTO);
        }
        if (invInventoryTransaction.getInvInventoryId() != null) {
            InvInventoryDTO invInventoryDTO = new InvInventoryDTO();
            invInventoryDTO.setId(invInventoryTransaction.getInvInventoryId().getId());
            invInventoryTransactionDTO.setInvInventoryId(invInventoryDTO);
        }
        if (invInventoryTransaction.getPurchaseOrderId() != null) {
            InvPurchaseOrderDTO invPurchaseOrderDTO = new InvPurchaseOrderDTO();
            invPurchaseOrderDTO.setId(invInventoryTransaction.getPurchaseOrderId().getId());
            invInventoryTransactionDTO.setPurchaseOrderId(invPurchaseOrderDTO);
        }
        return invInventoryTransactionDTO;
    }

    public List<InvInventoryTransactionDTO> mapToDTOList(List<InvInventoryTransaction> invInventoryTransactionList, TobyUser tobyUser) {
        List<InvInventoryTransactionDTO> invInventoryTransactionDTOList = new ArrayList<>();
        for (InvInventoryTransaction invInventoryTransaction : invInventoryTransactionList) {
            invInventoryTransactionDTOList.add(mapToDTO(invInventoryTransaction));
        }
        return invInventoryTransactionDTOList;
    }

    @Override
    public InvInventoryTransactionDTO getInvInventoryTransactionDetailsByPurchaseId(InvInventoryTransactionDTO invInventoryTransactionDTO, InvPurchaseInvoiceDTO1 invPurchaseInvoiceId, TobyUser tobyUser) {

        Map<String, Object> params = new HashMap<>();
        invInventoryTransactionDTO.setSupplierDate(invPurchaseInvoiceId.getDate());
        params.put("invPurchaseInvoiceId", invPurchaseInvoiceId.getId());         //////  AND ((e.type = 0 OR e.type = 2) AND e.active = 1) ORDER BY e.code
        List<InvPurchaseInvoiceDetail> detailses = dao.findListByQuery("SELECT e FROM InvPurchaseInvoiceDetail e WHERE e.invPurchaseInvoiceId.id=:invPurchaseInvoiceId ", params);
        invInventoryTransactionDTO.setInvInventoryTransactionDetailDTOList(mapToDTOList1(invInventoryTransactionDTO, detailses, tobyUser));
        return invInventoryTransactionDTO;
    }
//

    public List<InvInventoryTransactionDetailDTO> mapToDTOList1(InvInventoryTransactionDTO invInventoryTransactionDTO, List<InvPurchaseInvoiceDetail> invPurchaseInvoiceDetailList, TobyUser tobyUser) {
        List<InvInventoryTransactionDetailDTO> invInventoryTransactionDetailDTOList = new ArrayList<>();
        invInventoryTransactionDTO.setSumQuantity(BigDecimal.ZERO);
        Map<Integer, BigDecimal> map = quantityItemsStoreAddExitService.findInventoryDTOList(tobyUser, invInventoryTransactionDTO.getInvInventoryId().getId());

        for (InvPurchaseInvoiceDetail invPurchaseInvoiceDetail1 : invPurchaseInvoiceDetailList) {
            invInventoryTransactionDTO.setSumQuantity(invPurchaseInvoiceDetail1.getQuantity().add(invInventoryTransactionDTO.getSumQuantity()));
            invInventoryTransactionDetailDTOList.add(mapToDTO1(invPurchaseInvoiceDetail1, map));
        }

        return invInventoryTransactionDetailDTOList;
    }

    private InvInventoryTransactionDetailDTO mapToDTO1(InvPurchaseInvoiceDetail invPurchaseInvoiceDetail, Map<Integer, BigDecimal> map) {
        InvInventoryTransactionDetailDTO invInventoryTransactionDetailDTO = new InvInventoryTransactionDetailDTO();
        SymbolDTO symbolDTO = new SymbolDTO();

        invInventoryTransactionDetailDTO.setQuantity(invPurchaseInvoiceDetail.getQuantity());
        invInventoryTransactionDetailDTO.setIndex(invPurchaseInvoiceDetail.getId());

        if (invPurchaseInvoiceDetail.getItemId().getId() != null) {
            InvItemDTO invItemDTO = new InvItemDTO();
            invItemDTO.setId(invPurchaseInvoiceDetail.getItemId().getId());
            invInventoryTransactionDetailDTO.setItemId(invItemDTO);
        }
        if (invPurchaseInvoiceDetail.getUnitId() != null) {
            symbolDTO.setId(invPurchaseInvoiceDetail.getUnitId().getId());
            symbolDTO.setName(invPurchaseInvoiceDetail.getItemId().getUnitId().getName());
            invInventoryTransactionDetailDTO.setUnitId(symbolDTO);
        }
        invInventoryTransactionDetailDTO.setAvailableQuantity(map.get(invInventoryTransactionDetailDTO.getItemId().getId()));

        if (invInventoryTransactionDetailDTO.getQuantity().compareTo(invInventoryTransactionDetailDTO.getAvailableQuantity()) <= 0) {

            return invInventoryTransactionDetailDTO;
        } else {
            invInventoryTransactionDetailDTO.setMsg("كمية هذا الصنف غير مو جودة بالمخزن " + invInventoryTransactionDetailDTO.getItemId().getName() + " - " + invInventoryTransactionDetailDTO.getItemId().getCode());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", invInventoryTransactionDetailDTO.getMsg()));
            return invInventoryTransactionDetailDTO;
        }
    }

    @Override
    public InvInventoryTransactionDTO getInvInventoryTransactionDetailsByPurchaseIdFromView(InvInventoryTransactionDTO invInventoryTransactionDTO, InvPurchaseInvoiceDTO1 invoiceId, Integer invinventoryid, TobyUser tobyUser) {

        Map<String, Object> params = new HashMap<>();
        invInventoryTransactionDTO.setSupplierDate(invoiceId.getDate());
        params.put("invoiceId", invoiceId.getId());
        params.put("invinventoryid", invinventoryid);

        List<QuantityItemNotFinish> detalies = dao.findListByQuery("SELECT e FROM QuantityItemNotFinish e where e.invoiceId =:invoiceId and e.invinventoryid=:invinventoryid", params);
        invInventoryTransactionDTO.setInvInventoryTransactionDetailDTOList(mapToDTOList2(invInventoryTransactionDTO, detalies, tobyUser));
        return invInventoryTransactionDTO;
    }

    public List<InvInventoryTransactionDetailDTO> mapToDTOList2(InvInventoryTransactionDTO invInventoryTransactionDTO, List<QuantityItemNotFinish> notFinishs, TobyUser tobyUser) {
        List<InvInventoryTransactionDetailDTO> invInventoryTransactionDetailDTOList = new ArrayList<>();
        invInventoryTransactionDTO.setSumQuantity(BigDecimal.ZERO);
        Map<Integer, BigDecimal> map = quantityItemsStoreAddExitService.findInventoryDTOList(tobyUser, invInventoryTransactionDTO.getInvInventoryId().getId());
        for (QuantityItemNotFinish quantityItemNotFinish : notFinishs) {
            invInventoryTransactionDTO.setSumQuantity(quantityItemNotFinish.getQuantity().add(invInventoryTransactionDTO.getSumQuantity()));
            invInventoryTransactionDetailDTOList.add(mapToDTO2(quantityItemNotFinish, map));

        }

        return invInventoryTransactionDetailDTOList;

    }

    public InvInventoryTransactionDetailDTO mapToDTO2(QuantityItemNotFinish quantityItemNotFinish, Map<Integer, BigDecimal> map) {
        InvInventoryTransactionDetailDTO invInventoryTransactionDetailDTO = new InvInventoryTransactionDetailDTO();

        invInventoryTransactionDetailDTO.setQuantity(quantityItemNotFinish.getQuantity());
        invInventoryTransactionDetailDTO.setIndex(quantityItemNotFinish.getRowNum());

        if (quantityItemNotFinish.getItemId() > 0) {
            InvItemDTO invItemDTO = new InvItemDTO();
            invItemDTO.setId(quantityItemNotFinish.getItemId());
            invItemDTO.setName(quantityItemNotFinish.getItemName());
            invInventoryTransactionDetailDTO.setItemId(invItemDTO);
        }
        if (quantityItemNotFinish.getItemUnit() != null) {
            SymbolDTO symbolDTO = new SymbolDTO();
            symbolDTO.setId(quantityItemNotFinish.getItemUnit());
            symbolDTO.setName(quantityItemNotFinish.getUnitName());
            invInventoryTransactionDetailDTO.setUnitId(symbolDTO);

        }
        invInventoryTransactionDetailDTO.setAvailableQuantity(map.get(invInventoryTransactionDetailDTO.getItemId().getId()));

        return invInventoryTransactionDetailDTO;
    }

    @Override
    public List<InvInventoryTransactionDTO> getInventoryTransactionList(TobyUser tobyUser) {
        Map<String, Object> paramas = new HashMap<>();
        paramas.put("branchId", tobyUser.getBranchId().getId());
        List<InvInventoryTransaction> details = dao.findListByQuery("SELECT e FROM InvInventoryTransaction e where e.branchId.id=:branchId and( e.isdeleted != 1 or e.isdeleted is NULL )", paramas);

        return mapToDTOList(details, tobyUser);
    }
     @Override
    public List<InvInventoryTransactionDTO> getInventoryTransactionListTocash(TobyUser tobyUser) {
        Map<String, Object> paramas = new HashMap<>();
        paramas.put("branchId", tobyUser.getBranchId().getId());
                List<InvInventoryTransaction> details = dao.findListByQuery("SELECT e FROM InvInventoryTransaction e where e.branchId.id=:branchId and e.type = 1 and( e.isdeleted != 1 or e.isdeleted is NULL )", paramas);

        return mapToDTOList(details, tobyUser);
    }

    @Override
    public InvInventoryTransactionDTO getInvInventoryTransactionDTO(Integer invInventoryTransactionDTOId, TobyUser tobyUser) {
        InvInventoryTransaction invInventoryTransaction = dao.findEntityById(InvInventoryTransaction.class, invInventoryTransactionDTOId);
        return mapToDTO(invInventoryTransaction);
    }

//    @Override
//    public void deleteInvInventoryTransaction(InvInventoryTransactionDTO invInventoryTransactionDTO) {
//        Map<String,Object> map =new HashMap<>(); 
//        map.put(null, map);
//      if (invInventoryTransactionDTO.getId() != null && invInventoryTransactionDTO.getId() > 0) {
//            dao.executeDeleteQuery("delete from GlBankTransactionDetail e WHERE e.id > 0 AND e.glBankTransactionId.id = " + glBankTransaction.getId());
//        }
//        dao.deleteEntity(glBankTransaction);
//    }
//    }
    @Override
    public InvInventoryTransactionDTO deleteInvInventoryTransaction(InvInventoryTransactionDTO invInventoryTransactionDTO, TobyUser tobyUser) {
        InvInventoryTransaction invInventoryTransaction = mapToEntity(invInventoryTransactionDTO, tobyUser);
        invInventoryTransaction.setIsdeleted(1);
        dao.updateEntity(invInventoryTransaction);
        return mapToDTO(invInventoryTransaction);
    }

}
