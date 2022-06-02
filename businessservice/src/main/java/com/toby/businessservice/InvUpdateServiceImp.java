package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.InvDetailDTO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvInvoiceDTO;
import com.toby.dto.SymbolDTO;
import com.toby.entity.Branch;
import com.toby.entity.InvInventory;
import com.toby.entity.InvItem;
import com.toby.entity.InvUpdate;
import com.toby.entity.InvUpdateDetail;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
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
    @EJB
    private InvInventoryService invInventoryService;

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
        invPurchaseInvoiceDTO = invGlobalService.updateSummition(invPurchaseInvoiceDTO, false);
        return invPurchaseInvoiceDTO;
    }

    @Override
    public InvInvoiceDTO save(InvInvoiceDTO invInvoiceDTO, TobyUser tobyUser) {
        try {
            invInvoiceDTO = validateSave(invInvoiceDTO);

            InvUpdate invUpdate;
            if (invInvoiceDTO != null && (invInvoiceDTO.getMsg() == null || invInvoiceDTO.getMsg().isEmpty())) {
                invUpdate = mapToEntity(invInvoiceDTO, tobyUser);
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
                error = invUpdateDetailService.addInvUpdateDetailsDTO(invInvoiceDTO.getInvPurchaseInvoiceDetailList(), invInvoiceDTO, invInvoiceDTO.getScreenName(), tobyUser);
                if (error != null && !error.isEmpty()) {
                    invInvoiceDTO.setMsg(error);
                    dao.rollbackQuery();
                } else {
                    invInvoiceDTO = mapToDTO(invUpdate, invInvoiceDTO);
                    invInvoiceDTO = invGlobalService.updateSummition(invInvoiceDTO, false);
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
    private InvUpdate mapToEntity(InvInvoiceDTO invInvoiceDTO, TobyUser tobyUser) {

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
            TobyUser user = new TobyUser();
            user.setId(invInvoiceDTO.getCreatedBy());
            invUpdate.setCreatedBy(user);
            invUpdate.setCreationDate(invInvoiceDTO.getCreatedDate());
            invUpdate.setModifiedBy(tobyUser);
            invUpdate.setModificationDate(new Date());
            if (invInvoiceDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(invInvoiceDTO.getCompanyId());
                invUpdate.setCompanyId(company);
            }

            if (invInvoiceDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(invInvoiceDTO.getBranchId());
                invUpdate.setBranchId(branch);
            }
        } else {
            invUpdate.setCreatedBy(tobyUser);
            invUpdate.setCreationDate(new Date());
            invUpdate.setCompanyId(tobyUser.getCompanyId());
            invUpdate.setBranchId(tobyUser.getBranchId());
        }

        return invUpdate;
    }

    private InvInvoiceDTO initData(InvInvoiceDTO dto, InvUpdate entity) {
        if (dto == null) {
            dto = new InvInvoiceDTO();
        }
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
//
//        List<InvDetailDTO> invPurchaseInvoiceDetailsListDTO = invUpdateDetailService.getAllInvTransferDetailsByInvPurchaseInvoiceIdDTO(dto, entity.getBranchId().getId());
//
//        dto.setInvPurchaseInvoiceDetailList(invPurchaseInvoiceDetailsListDTO);
//        dto.setInvPurchaseInvoiceDetailsDeletedList(new ArrayList<InvDetailDTO>());
//
//        dto = invGlobalService.updateSummition(dto, false);
        return dto;
    }

    private InvInvoiceDTO mapToDTO(InvUpdate entity, InvInvoiceDTO invInvoiceDTO) {

        invInvoiceDTO = initData(invInvoiceDTO, entity);

        return invInvoiceDTO;
    }

    @Override
    public InvInvoiceDTO saveNewAll(InvInvoiceDTO invInvoiceDTO, TobyUser tobyUser) {
        invInvoiceDTO = saveNew(invInvoiceDTO, tobyUser);
        invInvoiceDTO.setInvPurchaseInvoiceDetailList(addInvUpdateDetailsDTONewList(invInvoiceDTO.getInvPurchaseInvoiceDetailList(), invInvoiceDTO.getId(), tobyUser));
        return invInvoiceDTO;
    }

    public List<InvDetailDTO> addInvUpdateDetailsDTONewList(List<InvDetailDTO> detailDTOList, Integer invUpdateId, TobyUser tobyUser) {
        StringBuilder error = null;
        List<InvDetailDTO> list = new ArrayList<>();int i = 0 ;
        for (InvDetailDTO detailDTO : detailDTOList) {
            error = invGlobalService.validateSelectItem(detailDTO, error);
//            System.out.println(detailDTO.getItemsBarcodesDetail().getBarcode() + " -> " + i++);
            if (error != null && !error.toString().isEmpty()) {
                detailDTO.setMsg(error.toString());
                return null;
            }
            InvUpdateDetail detail;

            detail = mapToEntityDetail(detailDTO, invUpdateId, tobyUser);
                detail = dao.updateEntity(detail);
                detailDTO.setId(detail.getId());
            detailDTO.setSerial(detail.getSerial());
            list.add(detailDTO);
        }

        return list;
    }

    private InvUpdateDetail mapToEntityDetail(InvDetailDTO dTO, Integer invUpdateId, TobyUser tobyUser) {
        InvUpdateDetail entity = new InvUpdateDetail();

        entity.setSerial(dTO.getSerial());
        entity.setItemBarcode(dTO.getItemBarcode());
        entity.setOldAmount(dTO.getAvailableQuantityRow());

        entity.setNewAmount(dTO.getActualQuantity());
        entity.setDifference(dTO.getActualQuantity().subtract(dTO.getAvailableQuantityRow()));
        entity.setWasalate(dTO.getWasalate());
        entity.setCostAvarage(dTO.getCostAvarage());

        InvUpdate invUpdate = new InvUpdate();
        invUpdate.setId(invUpdateId);
        entity.setInvUpdateId(invUpdate);

        if (dTO.getItemsBarcodesDetail() != null || dTO.getItemsBarcodesDetailTrans() != null) {
            InvItem invItem = new InvItem();
            invItem.setId(dTO.getItemsBarcodesDetail() != null ? dTO.getItemsBarcodesDetail().getItemId() : dTO.getItemsBarcodesDetailTrans().getItemId());
            entity.setInvItemId(invItem);
            entity.setItemBarcode(dTO.getItemsBarcodesDetail() != null ? dTO.getItemsBarcodesDetail().getBarcode() : dTO.getItemsBarcodesDetailTrans().getBarcode());
        }

        if (entity.getUnitId() != null) {
            SymbolDTO unitDTO = new SymbolDTO();
            unitDTO.setId(entity.getUnitId().getId());
            unitDTO.setName(entity.getUnitId().getName());
            unitDTO.setSerial(entity.getUnitId().getSerial());
            dTO.setUnitId(unitDTO);
        }

        if (dTO.getId() == null) {
            if (entity.getSerial() == null) {
                Map<String, Object> params = new HashMap<>();
                params.put("invUpdateId", invUpdateId);
                Integer max = genericService.getMaxGenericSerialForDetail(InvUpdateDetail.class, "invUpdateId", invUpdateId);
                entity.setSerial(max);
            }
            entity.setCreatedBy(tobyUser);
            entity.setCreationDate(new Date());
        } else {
            TobyUser user = new TobyUser();
            user.setId(dTO.getCreatedBy());
            entity.setCreatedBy(user);
            entity.setCreationDate(dTO.getCreatedDate());
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(tobyUser);
        }

        entity.setCompanyId(tobyUser.getCompanyId());
        return entity;
    }

    @Override
    public InvInvoiceDTO saveNew(InvInvoiceDTO invInvoiceDTO, TobyUser tobyUser) {
        try {
            invInvoiceDTO = validateSaveNew(invInvoiceDTO);

            InvUpdate invUpdate;
            if (invInvoiceDTO != null && (invInvoiceDTO.getMsg() == null || invInvoiceDTO.getMsg().isEmpty())) {
                invUpdate = mapToEntity(invInvoiceDTO, tobyUser);
                if (invUpdate.getId() != null) {
                    //بعمل حفظ فى حالة التعديل
                    invUpdate = dao.updateEntity(invUpdate);
                } else {
                    // فى حالة ان الفاتورة جديدة بروح اجيب السيريال الاول 
                    Integer serial = genericService.getMaxGenericSerialByType(InvUpdate.class, invUpdate.getBranchId().getId(), null);
                    invUpdate.setSerial(serial);

                    invUpdate = dao.updateEntity(invUpdate);
                    invInvoiceDTO.setId(invUpdate.getId());
                    invInvoiceDTO.setSerial(serial);
                }
            }
        } catch (Exception e) {
            invInvoiceDTO.setMsg(e.toString());
            dao.rollbackQuery();
        }

        return invInvoiceDTO;
    }

    public InvInvoiceDTO validateSaveNew(InvInvoiceDTO invPurchaseInvoiceDTO) {
        StringBuilder error = new StringBuilder();

        if (invPurchaseInvoiceDTO.getInvInventoryId() == null) {
            error.append("\n يجب اختيار مخزن");
        }

        if (invPurchaseInvoiceDTO.getDate() == null) {
            error.append("\n يجب اختيار تاريخ");
        }

        if (!error.toString().isEmpty()) {
            invPurchaseInvoiceDTO.setMsg(error.toString());
        }
        return invPurchaseInvoiceDTO;
    }

    @Override
    public InvInventoryDTO findinvinventoryDTO(Integer stripSerial, Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("stripSerial", stripSerial);
        List<InvInventory> invStripList = dao.findListByQuery("SELECT e.inventoryId FROM InvStrip e WHERE e.branchId.id = :branchId AND e.serial= :stripSerial", params);
        return invInventoryService.maptoDTOSmall(null, invStripList.get(0));
    }

    @Override
    public InvInvoiceDTO findInvInvoiceByInvUpdateId(Integer invUpdateId) {
        InvUpdate invUpdate = dao.findEntityById(InvUpdate.class, invUpdateId);
        return mapToDTO(invUpdate, null);
    }
}
