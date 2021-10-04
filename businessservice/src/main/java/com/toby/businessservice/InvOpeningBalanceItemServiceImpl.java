/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toby.businessservice;

import com.toby.bussinessservice.global.InvPurchaseReturnSave;
import com.toby.core.GenericDAO;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.InvOpenningBalanceItemDTO;
import com.toby.dto.InvOpenningBalanceItemDetailDTO;
import com.toby.entity.Branch;
import com.toby.entity.InvInventory;
import com.toby.entity.InvOpenningBalanceItem;
import com.toby.entity.InvOpenningBalanceItemDetail;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
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
    private InvOpeningBalanceItemDetailService invOpeningBalanceItemDetailService;
    @EJB
    private InvGlobalService invGlobalService;
    
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
        List<InvOpenningBalanceItem> invQotations = dao.findListByQuery("SELECT e FROM InvOpenningBalanceItem e WHERE e.branchId.id = :branchId order by e.id desc", params);
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
    public List<InvOpenningBalanceItemDTO> getInvOpeningBalanceItemByBranchIdDTO(Integer Id, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", Id);
        List<InvOpenningBalanceItem> invQotations = dao.findListByQuery("SELECT e FROM InvOpenningBalanceItem e WHERE e.branchId.id = :branchId", params);
        return mapToDTOList(invQotations, tobyUser);
    }

    public InvOpenningBalanceItem mapToEntity(InvOpenningBalanceItemDTO invOpenningBalanceItemDTO, TobyUser tobyUser) {
        InvOpenningBalanceItem invOpenningBalanceItem = new InvOpenningBalanceItem();
        invOpenningBalanceItem.setId(invOpenningBalanceItemDTO.getId());
        invOpenningBalanceItem.setDate(invOpenningBalanceItemDTO.getDate());
        invOpenningBalanceItem.setDocumentStrip(invOpenningBalanceItemDTO.getDocumentStrip());
        invOpenningBalanceItem.setRemark(invOpenningBalanceItemDTO.getRemark());
        invOpenningBalanceItem.setSerial(invOpenningBalanceItemDTO.getSerial());

        if (invOpenningBalanceItemDTO.getInvInventoryId() != null) {
            InvInventory invInventory = new InvInventory();
            invInventory.setId(invOpenningBalanceItemDTO.getInvInventoryId().getId());
            invInventory.setName(invOpenningBalanceItemDTO.getInvInventoryId().getName());
            invOpenningBalanceItem.setInvInventoryId(invInventory);

        }
        if (invOpenningBalanceItemDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(invOpenningBalanceItemDTO.getCreatedBy());
            invOpenningBalanceItem.setCreatedBy(user);
            invOpenningBalanceItem.setCreationDate(invOpenningBalanceItemDTO.getCreatedDate());
            invOpenningBalanceItem.setModifiedBy(tobyUser);
            invOpenningBalanceItem.setModificationDate(new Date());
            if (invOpenningBalanceItemDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(invOpenningBalanceItemDTO.getCompanyId());
                invOpenningBalanceItem.setCompanyId(company);
            }
            if (invOpenningBalanceItemDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(invOpenningBalanceItemDTO.getBranchId());
                invOpenningBalanceItem.setBranchId(branch);
            }
        } else {
            invOpenningBalanceItem.setCreatedBy(tobyUser);
            invOpenningBalanceItem.setCreationDate(new Date());
            invOpenningBalanceItem.setCompanyId(tobyUser.getCompanyId());
            invOpenningBalanceItem.setBranchId(tobyUser.getBranchId());

        }
        return invOpenningBalanceItem;
    }
    private InvInvoiceDTO mapToDTO(InvOpenningBalanceItem entity, InvInvoiceDTO invInvoiceDTO) {

        invInvoiceDTO = initData(invInvoiceDTO, entity);

        return invInvoiceDTO;
    }


    public InvOpenningBalanceItemDTO mapToDTO(InvOpenningBalanceItem invOpenningBalanceItem, TobyUser tobyUser) {
        InvOpenningBalanceItemDTO invOpenningBalanceItemDTO = new InvOpenningBalanceItemDTO();
        invOpenningBalanceItemDTO.setId(invOpenningBalanceItem.getId());
        invOpenningBalanceItemDTO.setDate(invOpenningBalanceItem.getDate());
        invOpenningBalanceItemDTO.setDocumentStrip(invOpenningBalanceItem.getDocumentStrip());
        invOpenningBalanceItemDTO.setRemark(invOpenningBalanceItem.getRemark());
        invOpenningBalanceItemDTO.setSerial(invOpenningBalanceItem.getSerial());
        invOpenningBalanceItemDTO.setIndex(invOpenningBalanceItem.getId());
        invOpenningBalanceItemDTO.setDate(new Date());

        if (invOpenningBalanceItem.getInvInventoryId() != null) {
            InvInventoryDTO invInventoryDTO = new InvInventoryDTO();
            invInventoryDTO.setId(invOpenningBalanceItem.getInvInventoryId().getId());
            invInventoryDTO.setName(invOpenningBalanceItem.getInvInventoryId().getName());
            invOpenningBalanceItemDTO.setInvInventoryId(invInventoryDTO);

        }
        invOpenningBalanceItemDTO.setCreatedBy(invOpenningBalanceItem.getCreatedBy() != null ? invOpenningBalanceItem.getCreatedBy().getId() : null);
        invOpenningBalanceItemDTO.setCreatedDate(invOpenningBalanceItem.getCreationDate());
        invOpenningBalanceItemDTO.setBranchId(invOpenningBalanceItem.getBranchId().getId() != null ? invOpenningBalanceItem.getBranchId().getId() : null);
        invOpenningBalanceItemDTO.setCompanyId(invOpenningBalanceItem.getCompanyId() != null ? invOpenningBalanceItem.getCompanyId().getId() : null);
        return invOpenningBalanceItemDTO;
    }

    public List<InvOpenningBalanceItemDTO> mapToDTOList(List<InvOpenningBalanceItem> invOpenningBalanceItemList, TobyUser tobyUser) {
        List<InvOpenningBalanceItemDTO> invOpenningBalanceItemDTOList = new ArrayList<>();
        for (InvOpenningBalanceItem invOpenningBalanceItem : invOpenningBalanceItemList) {
            invOpenningBalanceItemDTOList.add(mapToDTO(invOpenningBalanceItem, tobyUser));
        }
        return invOpenningBalanceItemDTOList;
    }

    @Override
    public InvOpenningBalanceItemDTO deleteInvOpeningBalanceItemDTO(InvOpenningBalanceItemDTO balanceItem, TobyUser tobyUser) {
        InvOpenningBalanceItem invOpenningBalanceItem = mapToEntity(balanceItem, tobyUser);
        dao.updateEntity(invOpenningBalanceItem);
        return mapToDTO(invOpenningBalanceItem, tobyUser);
    }

    @Override
    public InvOpenningBalanceItemDTO updateSummition(InvOpenningBalanceItemDTO invOpenningBalanceItemDTO) {
        try {

            BigDecimal quantity, cost;

            for (InvOpenningBalanceItemDetailDTO detailEntity : invOpenningBalanceItemDTO.getInvOpenningBalanceItemDetailDTOList()) {

                quantity = detailEntity.getQuantity() != null ? detailEntity.getQuantity() : BigDecimal.ZERO;
                cost = detailEntity.getCost() != null ? detailEntity.getCost() : BigDecimal.ZERO;

                invOpenningBalanceItemDTO.setTotal(quantity.multiply(cost));
                detailEntity.setNet(invOpenningBalanceItemDTO.getTotal().setScale(2, BigDecimal.ROUND_UP));

                invOpenningBalanceItemDTO.setTotalQuatity(invOpenningBalanceItemDTO.getTotalQuatity().add(quantity));
                invOpenningBalanceItemDTO.setTotalNet((invOpenningBalanceItemDTO.getTotalNet().add(detailEntity.getNet())).setScale(2, BigDecimal.ROUND_UP));
            }
        } catch (Exception e) {
//            saveError(e, "InvOpeningBalanceItemFormMB", "updateSummition");
        }
        return invOpenningBalanceItemDTO;
    }

    @Override
    public boolean validateDetails(InvOpenningBalanceItemDTO invOpenningBalanceItemDTO) {
        boolean bool = true;
        for (InvOpenningBalanceItemDetailDTO invOpenningBalanceItemDetailDTO1 : invOpenningBalanceItemDTO.getInvOpenningBalanceItemDetailDTOList()) {
            if (invOpenningBalanceItemDetailDTO1 == null || invOpenningBalanceItemDetailDTO1.getItemId() == null || invOpenningBalanceItemDetailDTO1.getQuantity() == null || invOpenningBalanceItemDetailDTO1.getCost() == null) {
                bool = false;
                invOpenningBalanceItemDTO.setMsg(" الصنف - العدد - التكلفة -  يجب وضع قيمة ");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", invOpenningBalanceItemDTO.getMsg()));
            }
        }
        return bool;
    }

    @Override
    public InvOpenningBalanceItemDTO validateQuantityColum(InvOpenningBalanceItemDetailDTO invOpenningBalanceItemDetailDTOSelected, InvOpenningBalanceItemDTO invOpenningBalanceItemDTO) {

        BigDecimal sumQuantaty = new BigDecimal(0);
        if (invOpenningBalanceItemDetailDTOSelected != null && invOpenningBalanceItemDetailDTOSelected.getItemId() != null) {
            for (InvOpenningBalanceItemDetailDTO invOpenningBalanceItemDetailDTO2 : invOpenningBalanceItemDTO.getInvOpenningBalanceItemDetailDTOList()) {
                if (invOpenningBalanceItemDetailDTO2.getQuantity() != null) {
                    sumQuantaty = sumQuantaty.add(invOpenningBalanceItemDetailDTO2.getQuantity());
                }
            }

        }

        invOpenningBalanceItemDTO.setSumquantaty(sumQuantaty);
        return invOpenningBalanceItemDTO;

    }

    @Override
    public InvOpenningBalanceItemDTO validatePriceColumn(InvOpenningBalanceItemDetailDTO invOpenningBalanceItemDetailDTOSelected, InvOpenningBalanceItemDTO invOpenningBalanceItemDTO) {

        BigDecimal sumNet = new BigDecimal(0);
        if (invOpenningBalanceItemDetailDTOSelected != null && invOpenningBalanceItemDetailDTOSelected.getItemId() != null) {
            for (InvOpenningBalanceItemDetailDTO invOpenningBalanceItemDetailDTO2 : invOpenningBalanceItemDTO.getInvOpenningBalanceItemDetailDTOList()) {
                if (invOpenningBalanceItemDetailDTO2.getNet() != null) {
                    sumNet = sumNet.add(invOpenningBalanceItemDetailDTO2.getNet());
                }
            }
        }

        invOpenningBalanceItemDTO.setSumNet(sumNet);
        return invOpenningBalanceItemDTO;

    }

    @Override
    public InvOpenningBalanceItemDTO saveInvOpenningBalanceItemDTO(InvOpenningBalanceItemDTO invOpenningBalanceItemDTO, TobyUser tobyUser) {
        InvOpenningBalanceItem invOpenningBalanceItem = mapToEntity(invOpenningBalanceItemDTO, tobyUser);
        invOpenningBalanceItem = dao.updateEntity(invOpenningBalanceItem);
       invOpenningBalanceItemDTO.setInvOpenningBalanceItemDetailDTOList(balanceItemDetailService.saveInvOpenningBalanceItemDetailDTO(invOpenningBalanceItemDTO.getInvOpenningBalanceItemDetailDTOList(), invOpenningBalanceItem.getId(), tobyUser));

        return invOpenningBalanceItemDTO;
    }
    
    @Override
    public InvInvoiceDTO save(InvInvoiceDTO invInvoiceDTO, TobyUser tobyUser) {
        try {
            invInvoiceDTO = validateSave(invInvoiceDTO);

            InvOpenningBalanceItem invOpenningBalanceItem;
            if (invInvoiceDTO != null && (invInvoiceDTO.getMsg() == null || invInvoiceDTO.getMsg().isEmpty())) {
                invOpenningBalanceItem = mapToEntity(invInvoiceDTO, tobyUser);
                if (invOpenningBalanceItem.getId() != null) {
                    //بعمل حفظ فى حالة التعديل
                    invOpenningBalanceItem = dao.updateEntity(invOpenningBalanceItem);
                } else {
                    // فى حالة ان الفاتورة جديدة بروح اجيب السيريال الاول 
                    Integer serial = genericService.getMaxSerialForOpenningBalance(InvOpenningBalanceItem.class, invOpenningBalanceItem.getBranchId().getId(), invOpenningBalanceItem.getInvInventoryId().getId(), null);
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

                String error = invOpeningBalanceItemDetailService.addInvOpenningBalanceItemDetailDTO(invInvoiceDTO.getInvPurchaseInvoiceDetailList(), invInvoiceDTO,invInvoiceDTO.getScreenName(),tobyUser);

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
     
     //mapToEntity
    private InvOpenningBalanceItem mapToEntity(InvInvoiceDTO invInvoiceDTO, TobyUser tobyUser) {

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
            TobyUser user = new TobyUser();
            user.setId(invInvoiceDTO.getCreatedBy());
            invOpenningBalanceItem.setCreatedBy(user);
            invOpenningBalanceItem.setCreationDate(invInvoiceDTO.getCreatedDate());
            invOpenningBalanceItem.setModifiedBy(tobyUser);
            invOpenningBalanceItem.setModificationDate(new Date());
            if (invInvoiceDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(invInvoiceDTO.getCompanyId());
                invOpenningBalanceItem.setCompanyId(company);
            }

            if (invInvoiceDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(invInvoiceDTO.getBranchId());
                invOpenningBalanceItem.setBranchId(branch);
            }
        } else {
            invOpenningBalanceItem.setCreatedBy(tobyUser);
            invOpenningBalanceItem.setCreationDate(new Date());
            invOpenningBalanceItem.setCompanyId(tobyUser.getCompanyId());
            invOpenningBalanceItem.setBranchId(tobyUser.getBranchId());
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
    public InvInvoiceDTO findOpeningBalanceById(InvInvoiceDTO invInvoiceDTO) {
        InvOpenningBalanceItem invOpenningBalanceItem = dao.findEntityById(InvOpenningBalanceItem.class, invInvoiceDTO.getId());
        InvInvoiceDTO invPurchaseInvoiceDTO = mapToDTO(invOpenningBalanceItem, invInvoiceDTO);
        invPurchaseInvoiceDTO = invGlobalService.updateSummition(invPurchaseInvoiceDTO);
        return invPurchaseInvoiceDTO;
    }
    
     


}
