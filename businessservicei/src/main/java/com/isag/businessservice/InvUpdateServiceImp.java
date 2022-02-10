package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.InvDetailDTO;
import com.isag.dto.InvInventoryDTO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.Branch;
import com.isag.entity.InvInventory;
import com.isag.entity.InvUpdate;
import com.isag.entity.InvUpdateDetail;
import com.isag.entity.IsagCompany;
import com.isag.entity.IsagUser;
import java.util.ArrayList;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author khaled
 */
@Stateless
public class InvUpdateServiceImp implements InvUpdateService {

    @EJB
    private GenericDAO dao;
    @EJB
    private GenericService genericService;
    @EJB
    private InvUpdateDetailService invUpdateDetailService;
    @EJB
    private InvGlobalService invGlobalService;

    @Override
    public InvUpdate addInvUpdate(InvUpdate invUpdate, List<InvUpdateDetail> invUpdateDetailList, List<InvUpdateDetail> UpdateDetailDeleted) {
        Integer serial;
        if (invUpdate.getId() != null) {
            dao.updateEntity(invUpdate);
        } else {
            serial = genericService.getMaxGenericSerialByType(InvUpdate.class, invUpdate.getBranchId().getId(), null);
            invUpdate.setSerial(serial);
            dao.saveEntity(invUpdate);
        }

        for (InvUpdateDetail deleted : UpdateDetailDeleted) {
            invUpdateDetailService.deleteInvUpdateDetails(deleted);
        }

        for (InvUpdateDetail detail : invUpdateDetailList) {
            detail.setInvUpdateId(invUpdate);
            if (detail.getId() != null) {
                invUpdateDetailService.updateInvUpdateDetails(detail);
            } else {
                serial = getMaxSerialForInvoiceDetail(invUpdate);
                detail.setSerial(serial);
                invUpdateDetailService.addInvUpdateDetails(detail);
            }
        }
        return invUpdate;
    }

    @Override
    public void deleteInvUpdate(InvUpdate invUpdate, Integer invUpdateIdToPass) {
        if (invUpdateIdToPass != null && invUpdateIdToPass > 0) {
            dao.executeDeleteQuery("delete from InvUpdateDetail e WHERE e.id > 0 AND e.invUpdateId.id = " + invUpdateIdToPass);
        }
        dao.deleteEntity(invUpdate);
    }

    @Override
    public List<InvUpdate> getALLInvUpdate() {
        return dao.findAll(InvUpdate.class);
    }

    @Override
    public List<InvUpdate> getALLInvUpdateByCompanyId(Integer Id) {
        return dao.findEntityListByCompanyId(InvUpdate.class, Id);
    }

    @Override
    public List<InvUpdate> getALLInvUpdateByBranchId(Integer Id, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", Id);

        List<InvUpdate> invUpdateList = dao.findListByQuery("SELECT e FROM InvUpdate e WHERE e.branchId.id = :branchId ", params);
        return invUpdateList;
    }

    @Override
    public List<InvUpdateDetail> getALLInvUpdateDetailsByInvUpdate(Integer invUpdateId) {
        List<InvUpdateDetail> details = invUpdateDetailService.getAllInvUpdateDetails(invUpdateId);
        return details;
    }

    @Override
    public InvUpdate findInvUpdateByInvUpdateId(Integer invUpdateId) {
        return dao.findEntityById(InvUpdate.class, invUpdateId);
    }

    @Override
    public synchronized Integer getMaxSerialForInvoiceDetail(InvUpdate invUpdate) {
        Map<String, Object> params = new HashMap<>();
        params.put("invUpdateId", invUpdate.getId());

        Integer serial = dao.findEntityByQuery("SELECT max(e.serial) FROM InvUpdateDetail e WHERE e.invUpdateId.id =:invUpdateId ", params);

        if (serial == null) {
            return 1;
        } else {
            serial = serial + 1;
            return serial;
        }
    }
    
    @Override
    public InvInvoiceDTO findInvUpdateById(InvInvoiceDTO invInvoiceDTO) {
        InvUpdate invUpdate = dao.findEntityById(InvUpdate.class, invInvoiceDTO.getId());
        InvInvoiceDTO invPurchaseInvoiceDTO = mapToDTO(invUpdate, invInvoiceDTO);
        invPurchaseInvoiceDTO = invGlobalService.updateSummition(invPurchaseInvoiceDTO);
        return invPurchaseInvoiceDTO;
    }
    @Override
    public InvInvoiceDTO findInvUpdateBySerial(InvInvoiceDTO invInvoiceDTO,Integer branchId) {
        
          Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("serial", invInvoiceDTO.getSerial());
        InvUpdate invUpdate = dao.findEntityByQuery("SELECT e FROM InvUpdate e WHERE  e.branchId.id = :branchId  AND e.serial = :serial ", params);
        InvInvoiceDTO invPurchaseInvoiceDTO = mapToDTO(invUpdate, invInvoiceDTO);
        invPurchaseInvoiceDTO = invGlobalService.updateSummition(invPurchaseInvoiceDTO);
        return invPurchaseInvoiceDTO;
    }

    private InvInvoiceDTO mapToDTO(InvUpdate entity, InvInvoiceDTO invInvoiceDTO) {

        invInvoiceDTO = initData(invInvoiceDTO, entity);

        return invInvoiceDTO;
    }

    //mapToEntity
    private InvUpdate mapToEntity(InvInvoiceDTO invInvoiceDTO, IsagUser isagUser) {

        InvUpdate invUpdate = new InvUpdate();

        if (invInvoiceDTO.getInvInventoryId() != null) {
            InvInventory invInventory = new InvInventory();
            invInventory.setId(invInvoiceDTO.getInvInventoryId().getId());
            invUpdate.setInventoryId(invInventory);
        }

        invUpdate.setId(invInvoiceDTO.getId());

        invUpdate.setDate(invInvoiceDTO.getDate());
        invUpdate.setRemarks(invInvoiceDTO.getRemarks());
        invUpdate.setDocument(invInvoiceDTO.getSupplierInvoiceNumber());

        if (invInvoiceDTO.getCreatedBy() != null) {
            invUpdate.setSerial(invInvoiceDTO.getSerial());
            IsagUser user = new IsagUser();
            user.setId(invInvoiceDTO.getCreatedBy());
            invUpdate.setCreatedBy(user);
            invUpdate.setCreationDate(invInvoiceDTO.getCreatedDate());
            invUpdate.setModifiedBy(isagUser);
            invUpdate.setModificationDate(new Date());
            if (invInvoiceDTO.getCompanyId() != null) {
                IsagCompany company = new IsagCompany();
                company.setId(invInvoiceDTO.getCompanyId());
                invUpdate.setCompanyId(company);
            }

            if (invInvoiceDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(invInvoiceDTO.getBranchId());
                invUpdate.setBranchId(branch);
            }
        } else {
            invUpdate.setCreatedBy(isagUser);
            invUpdate.setCreationDate(new Date());
            invUpdate.setCompanyId(isagUser.getCompanyId());
            invUpdate.setBranchId(isagUser.getBranchId());
        }

        return invUpdate;
    }

    private InvInvoiceDTO initData(InvInvoiceDTO dto, InvUpdate entity) {
        dto.setId(entity.getId());
        dto.setSerial(entity.getSerial());
        dto.setCompanyActivity(entity.getBranchId().getCompanyActivity());
        dto.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
        dto.setDate(entity.getDate());
        dto.setRemarks(entity.getRemarks());
        dto.setSupplierInvoiceNumber(entity.getDocument());

        if (entity.getInventoryId() != null) {
            InvInventoryDTO invInventoryDTO = new InvInventoryDTO();
            invInventoryDTO.setId(entity.getInventoryId().getId());
            invInventoryDTO.setName(entity.getInventoryId().getName());
            invInventoryDTO.setCode(entity.getInventoryId().getCode());
            dto.setInvInventoryId(invInventoryDTO);
        }

        dto.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dto.setCreatedDate(entity.getCreationDate());
        dto.setIndex(entity.getId());
        dto.setCompanyId(entity.getCompanyId().getId() != null ? entity.getCompanyId().getId() : null);
        dto.setCreatedByName(entity.getCreatedBy() != null ? entity.getCreatedBy().getName() : null);

        List<InvDetailDTO> invPurchaseInvoiceDetailsListDTO = invUpdateDetailService.getAllInvTransferDetailsByInvPurchaseInvoiceIdDTO(dto, entity.getBranchId().getId());

        dto.setInvPurchaseInvoiceDetailList(invPurchaseInvoiceDetailsListDTO);
        dto.setInvPurchaseInvoiceDetailsDeletedList(new ArrayList<InvDetailDTO>());

        dto = invGlobalService.updateSummition(dto);
        return dto;
    }

    @Override
    public InvInvoiceDTO save(InvInvoiceDTO invInvoiceDTO, IsagUser isagUser) {
        try {
            invInvoiceDTO = validateSave(invInvoiceDTO);

            InvUpdate invUpdate;
            if (invInvoiceDTO != null && (invInvoiceDTO.getMsg() == null || invInvoiceDTO.getMsg().isEmpty())) {
                invUpdate = mapToEntity(invInvoiceDTO, isagUser);
                if (invUpdate.getId() != null) {
                    //بعمل حفظ فى حالة التعديل
                    invUpdate = dao.updateEntity(invUpdate);
                } else {
                    // فى حالة ان الفاتورة جديدة بروح اجيب السيريال الاول 
                    Integer serial = genericService.getMaxGenericSerialByType(InvUpdate.class, invUpdate.getBranchId().getId(), null);
                    invUpdate.setSerial(serial);

                    invUpdate = dao.updateEntity(invUpdate);
                    invInvoiceDTO.setId(invUpdate.getId());
                }

                invUpdateDetailService.deleteInvUpdateDetailListDTO(invInvoiceDTO.getInvPurchaseInvoiceDetailsDeletedList());
                String error;
                error = invUpdateDetailService.addInvUpdateDetailsDTO(invInvoiceDTO.getInvPurchaseInvoiceDetailList(), invInvoiceDTO,invInvoiceDTO.getScreenName(), isagUser);
                if (error != null && !error.isEmpty()) {
                    invInvoiceDTO.setMsg(error);
                    dao.rollbackQuery();
                } else {
                    invInvoiceDTO = mapToDTO(invUpdate, invInvoiceDTO);
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
