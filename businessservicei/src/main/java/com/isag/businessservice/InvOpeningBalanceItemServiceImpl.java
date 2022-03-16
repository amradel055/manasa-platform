/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.bussinessservice.global.InvPurchaseReturnSave;
import com.isag.core.GenericDAO;
import com.isag.dto.InvDetailDTO;
import com.isag.dto.InvInventoryDTO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.Branch;
import com.isag.entity.InvInventory;
import com.isag.entity.InvOpenningBalanceItem;
import com.isag.entity.InvOpenningBalanceItemDetail;
import com.isag.entity.InvUpdate;
import com.isag.entity.IsagCompany;
import com.isag.entity.IsagUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hhhh
 */
@Stateless
public class InvOpeningBalanceItemServiceImpl implements InvOpeningBalanceItemService {

    @EJB
    private GenericDAO dao;
    @EJB
    private GenericService genericService;
    @EJB
    private InvOpeningBalanceItemDetailService balanceItemDetailService;
    @EJB
    private InvGlobalService invGlobalService;
    @EJB
    private InvOpeningBalanceItemDetailService invOpeningBalanceItemDetailService;
    
    InvPurchaseReturnSave invPurchaseReturnSave;
    @Override
    public InvPurchaseReturnSave addInvOpeningBalanceItem(InvOpenningBalanceItem invOpenningBalanceItem,
            List<InvOpenningBalanceItemDetail> invOpenningBalanceItemDetailList,
            List<InvOpenningBalanceItemDetail> balanceItemDetailsDeleted) {
            invPurchaseReturnSave = new InvPurchaseReturnSave();
        Integer serial;
        if (invOpenningBalanceItem.getId() != null) {
            dao.updateEntity(invOpenningBalanceItem);
        } else {
            serial = genericService.getMaxGenericSerialByType(InvOpenningBalanceItem.class, invOpenningBalanceItem.getBranchId().getId(), null);
            invOpenningBalanceItem.setSerial(serial);
            dao.saveEntity(invOpenningBalanceItem);
        }

        for (InvOpenningBalanceItemDetail deleted : balanceItemDetailsDeleted) {
            balanceItemDetailService.deleteInvOpenningBalanceItemDetail(deleted);
        }

        for (InvOpenningBalanceItemDetail detail : invOpenningBalanceItemDetailList) {
            detail.setBalanceItemId(invOpenningBalanceItem);
            if (detail.getId() != null) {
                balanceItemDetailService.updateInvOpenningBalanceItemDetail(detail);
            } else {
                serial = getMaxSerialForInvoiceDetail(invOpenningBalanceItem);
                detail.setSerial(serial);
                balanceItemDetailService.addInvOpenningBalanceItemDetail(detail);
            }
        }
        invPurchaseReturnSave.setInvOpenningBalanceItem(invOpenningBalanceItem);
        invPurchaseReturnSave.setInvOpenningBalanceItemDetailList(balanceItemDetailService.getAllInvOpenningBalanceItemDetails(invOpenningBalanceItem.getId()));
        return invPurchaseReturnSave;
    }

    @Override
    public void deleteInvOpeningBalanceItem(InvOpenningBalanceItem balanceItem) {
        if (balanceItem.getId() != null && balanceItem.getId() > 0) {
            dao.executeDeleteQuery("delete from InvOpenningBalanceItemDetail e WHERE e.id > 0 AND e.balanceItemId.id = " + balanceItem.getId());
        }
        dao.deleteEntity(balanceItem);
    }

    @Override
    public List<InvOpenningBalanceItem> getALLInvOpeningBalanceItem() {
        return dao.findAll(InvOpenningBalanceItem.class);
    }

    @Override
    public List<InvOpenningBalanceItem> getALLInvOpeningBalanceItemByCompanyId(Integer Id) {
        return dao.findEntityListByCompanyId(InvOpenningBalanceItem.class, Id);
    }

    @Override
    public List<InvOpenningBalanceItem> getInvOpeningBalanceItemByBranchId(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<InvOpenningBalanceItem> invQotations = dao.findListByQuery("SELECT e FROM InvOpenningBalanceItem e WHERE e.branchId.id = :branchId", params);
        return invQotations;
    }

    @Override
    public List<InvOpenningBalanceItemDetail> getALLInvOpeningBalanceItemDetailsByInvOpeningBalanceItem(Integer id) {
        Map<String, Object> params = new HashMap<>();
        params.put("balanceItemId", id);
        List<InvOpenningBalanceItemDetail> details = dao.findListByQuery("SELECT e FROM InvOpenningBalanceItemDetail e WHERE e.balanceItemId.id = :balanceItemId", params);
        return details;
    }

    @Override
    public InvOpenningBalanceItem findInvOpeningBalanceItemByInvOpeningBalanceItemId(Integer openingBalanceId) {
        return dao.findEntityById(InvOpenningBalanceItem.class, openingBalanceId);
    }

    public synchronized Integer getMaxSerialForInvoiceDetail(InvOpenningBalanceItem balanceItem) {
        Map<String, Object> params = new HashMap<>();
        params.put("balanceItemId", balanceItem.getId());

        Integer serial = dao.findEntityByQuery("SELECT max(e.serial) FROM InvOpenningBalanceItemDetail e WHERE e.balanceItemId.id =:balanceItemId ", params);

        if (serial == null) {
            return 1;
        } else {
            serial = serial + 1;
            return serial;
        }
    }
    
    @Override
    public InvInvoiceDTO findOpeningBalanceById(InvInvoiceDTO invInvoiceDTO) {
        InvOpenningBalanceItem invOpenningBalanceItem = dao.findEntityById(InvOpenningBalanceItem.class, invInvoiceDTO.getId());
        InvInvoiceDTO invPurchaseInvoiceDTO = mapToDTO(invOpenningBalanceItem, invInvoiceDTO);
        invPurchaseInvoiceDTO = invGlobalService.updateSummition(invPurchaseInvoiceDTO);
        return invPurchaseInvoiceDTO;
    }
    

    private InvInvoiceDTO mapToDTO(InvOpenningBalanceItem entity, InvInvoiceDTO invInvoiceDTO) {

        invInvoiceDTO = initData(invInvoiceDTO, entity);

        return invInvoiceDTO;
    }

    //mapToEntity
    private InvOpenningBalanceItem mapToEntity(InvInvoiceDTO invInvoiceDTO, IsagUser isagUser) {

        InvOpenningBalanceItem invOpenningBalanceItem = new InvOpenningBalanceItem();

        if (invInvoiceDTO.getInvInventoryId() != null) {
            InvInventory invInventory = new InvInventory();
            invInventory.setId(invInvoiceDTO.getInvInventoryId().getId());
            invOpenningBalanceItem.setInvInventoryId(invInventory);
        }

        invOpenningBalanceItem.setId(invInvoiceDTO.getId());

        invOpenningBalanceItem.setDate(invInvoiceDTO.getDate());
        invOpenningBalanceItem.setRemark(invInvoiceDTO.getRemarks());
        invOpenningBalanceItem.setDocumentStrip(invInvoiceDTO.getSupplierInvoiceNumber());

        if (invInvoiceDTO.getCreatedBy() != null) {
            invOpenningBalanceItem.setSerial(invInvoiceDTO.getSerial());
            IsagUser user = new IsagUser();
            user.setId(invInvoiceDTO.getCreatedBy());
            invOpenningBalanceItem.setCreatedBy(user);
            invOpenningBalanceItem.setCreationDate(invInvoiceDTO.getCreatedDate());
            invOpenningBalanceItem.setModifiedBy(isagUser);
            invOpenningBalanceItem.setModificationDate(new Date());
            if (invInvoiceDTO.getCompanyId() != null) {
                IsagCompany company = new IsagCompany();
                company.setId(invInvoiceDTO.getCompanyId());
                invOpenningBalanceItem.setCompanyId(company);
            }

            if (invInvoiceDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(invInvoiceDTO.getBranchId());
                invOpenningBalanceItem.setBranchId(branch);
            }
        } else {
            invOpenningBalanceItem.setCreatedBy(isagUser);
            invOpenningBalanceItem.setCreationDate(new Date());
            invOpenningBalanceItem.setCompanyId(isagUser.getCompanyId());
            invOpenningBalanceItem.setBranchId(isagUser.getBranchId());
        }

        return invOpenningBalanceItem;
    }

    private InvInvoiceDTO initData(InvInvoiceDTO dto, InvOpenningBalanceItem entity) {
        dto.setId(entity.getId());
        dto.setSerial(entity.getSerial());
        dto.setCompanyActivity(entity.getBranchId().getCompanyActivity());
        dto.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
        dto.setDate(entity.getDate());
        dto.setRemarks(entity.getRemark());
        dto.setSupplierInvoiceNumber(entity.getDocumentStrip());

        if (entity.getInvInventoryId() != null) {
            InvInventoryDTO invInventoryDTO = new InvInventoryDTO();
            invInventoryDTO.setId(entity.getInvInventoryId().getId());
            invInventoryDTO.setName(entity.getInvInventoryId().getName());
            invInventoryDTO.setCode(entity.getInvInventoryId().getCode());
            dto.setInvInventoryId(invInventoryDTO);
        }

        dto.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dto.setCreatedDate(entity.getCreationDate());
        dto.setIndex(entity.getId());
        dto.setCompanyId(entity.getCompanyId().getId() != null ? entity.getCompanyId().getId() : null);
        dto.setCreatedByName(entity.getCreatedBy() != null ? entity.getCreatedBy().getName() : null);

        List<InvDetailDTO> invPurchaseInvoiceDetailsListDTO = invOpeningBalanceItemDetailService.getAllInvOpenningBalanceItemDetailByInvPurchaseInvoiceIdDTO(dto, entity.getBranchId().getId());

        dto.setInvPurchaseInvoiceDetailList(invPurchaseInvoiceDetailsListDTO);
        dto.setInvPurchaseInvoiceDetailsDeletedList(new ArrayList<InvDetailDTO>());

        dto = invGlobalService.updateSummition(dto);
        return dto;
    }
    
    @Override
    public InvInvoiceDTO save(InvInvoiceDTO invInvoiceDTO, IsagUser isagUser) {
        try {
            invInvoiceDTO = validateSave(invInvoiceDTO);

            InvOpenningBalanceItem invOpenningBalanceItem;
            if (invInvoiceDTO != null && (invInvoiceDTO.getMsg() == null || invInvoiceDTO.getMsg().isEmpty())) {
                invOpenningBalanceItem = mapToEntity(invInvoiceDTO, isagUser);
                if (invOpenningBalanceItem.getId() != null) {
                    //بعمل حفظ فى حالة التعديل
                    invOpenningBalanceItem = dao.updateEntity(invOpenningBalanceItem);
                } else {
                    // فى حالة ان الفاتورة جديدة بروح اجيب السيريال الاول 
                    Integer serial = genericService.getMaxSerialForInvoice(InvOpenningBalanceItem.class, invOpenningBalanceItem.getBranchId().getId(), invOpenningBalanceItem.getInvInventoryId().getId(), null);
                    if (serial == null) {
                        invInvoiceDTO.setMsg("من فضلك اعد حفظ الفاتورة");
                        dao.rollbackQuery();
                        return invInvoiceDTO;
                    }
                    invOpenningBalanceItem.setSerial(serial);
                    
                    invOpenningBalanceItem = dao.updateEntity(invOpenningBalanceItem);
                    invInvoiceDTO.setId(invOpenningBalanceItem.getId());
                }

                invOpeningBalanceItemDetailService.deleteInvOpenningBalanceItemDetailListDTO(invInvoiceDTO.getInvPurchaseInvoiceDetailsDeletedList());

                String error = invOpeningBalanceItemDetailService.addInvOpenningBalanceItemDetailDTO(invInvoiceDTO.getInvPurchaseInvoiceDetailList(), invInvoiceDTO,invInvoiceDTO.getScreenName(),isagUser);

                if (error != null && !error.isEmpty()) {
                    invInvoiceDTO.setMsg(error);
                    dao.rollbackQuery();
                } else {
                    invInvoiceDTO = mapToDTO(invOpenningBalanceItem, invInvoiceDTO);
                    invInvoiceDTO = invGlobalService.updateSummition(invInvoiceDTO);
                }

            }
        } catch (Exception e) {
            invInvoiceDTO.setMsg(e.toString());
            dao.rollbackQuery();
        }

        return invInvoiceDTO;
    }
    
     public InvInvoiceDTO validateSave(InvInvoiceDTO invPurchaseInvoiceDTO) {
        StringBuilder error = new StringBuilder();

        if (invPurchaseInvoiceDTO.getInvInventoryId() == null) {
            error.append("\n يجب اختيار مخزن");
        }

        if (!error.toString().isEmpty()) {
            invPurchaseInvoiceDTO.setMsg(error.toString());
        }
        return invPurchaseInvoiceDTO;
    }
     



}
