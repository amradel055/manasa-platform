package com.isag.businessservice;

import com.isag.bussinessservice.global.InvPurchaseReturnSave;
import com.isag.core.GenericDAO;
import com.isag.dto.InvDailogueData;
import com.isag.dto.InvDetailDTO;
import com.isag.dto.InvInventoryDTO;
import com.isag.dto.InvInvoiceDTO;
import com.isag.entity.Branch;
import com.isag.entity.InvInventory;
import com.isag.entity.InvStrip;
import com.isag.entity.InvStripDetail;
import com.isag.entity.InvTransfer;
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
public class InvStripServiceImp implements InvStripService {

    @EJB
    private GenericDAO dao;
    @EJB
    private GenericService genericService;
    @EJB
    private InvStripDetailService invStripDetailService;
    @EJB
    private InvGlobalService invGlobalService;
    @EJB
    QuantityItemsStoreByDateService quantityItemsStoreByDateService;

    InvPurchaseReturnSave invPurchaseReturnSave;

    @Override
    public InvPurchaseReturnSave addInvStrip(InvStrip invStrip, List<InvStripDetail> invStripDetailDetailList, List<InvStripDetail> StripDetailDeleted) {
        invPurchaseReturnSave = new InvPurchaseReturnSave();
        Integer serial;
        if (invStrip.getId() != null) {
            dao.updateEntity(invStrip);
        } else {
            serial = genericService.getMaxGenericSerialByType(InvStrip.class, invStrip.getBranchId().getId(), null);
            invStrip.setSerial(serial);
            dao.saveEntity(invStrip);
        }

        for (InvStripDetail deleted : StripDetailDeleted) {
            invStripDetailService.deleteInvStripDetails(deleted);
        }

        for (InvStripDetail detail : invStripDetailDetailList) {
            detail.setInvStripId(invStrip);
            if (detail.getId() != null) {
                invStripDetailService.updateInvStripDetails(detail);
            } else {
                serial = getMaxSerialForInvoiceDetail(invStrip);
                detail.setSerial(serial);
                invStripDetailService.addInvStripDetails(detail);
            }
        }
        invPurchaseReturnSave.setInvStrip(invStrip);
        invPurchaseReturnSave.setInvStripDetailList(invStripDetailService.getAllInvStripDetails(invStrip.getId()));
        return invPurchaseReturnSave;
    }

    @Override
    public void deleteInvStrip(InvStrip invStrip, Integer invStripIdToPass) {
        if (invStripIdToPass != null && invStripIdToPass > 0) {
            dao.executeDeleteQuery("delete from InvStripDetail e WHERE e.id > 0 AND e.invStripId.id = " + invStripIdToPass);
        }
        dao.deleteEntity(invStrip);
    }

    @Override
    public List<InvStrip> getALLInvStrip() {
        return dao.findAll(InvStrip.class);
    }

    @Override
    public List<InvStrip> getALLInvStripByCompanyId(Integer Id) {
        return dao.findEntityListByCompanyId(InvStrip.class, Id);
    }

    @Override
    public List<InvStrip> getALLInvStripByBranchId(Integer Id, Integer type) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", Id);
        params.put("type", type);
        List<InvStrip> invStripList = dao.findListByQuery("SELECT e FROM InvStrip e WHERE e.branchId.id = :branchId AND e.type= :type", params);
        return invStripList;
    }

    @Override
    public List<InvStripDetail> getALLInvStripDetailsByInvStrip(Integer invStripId) {
        List<InvStripDetail> details = invStripDetailService.getAllInvStripDetails(invStripId);
        return details;
    }

    @Override
    public InvStrip findInvStripByInvStripId(Integer invStripId) {
        return dao.findEntityById(InvStrip.class, invStripId);
    }

    @Override
    public synchronized Integer getMaxSerialForInvoiceDetail(InvStrip invStrip) {
        Map<String, Object> params = new HashMap<>();
        params.put("invStripId", invStrip.getId());

        Integer serial = dao.findEntityByQuery("SELECT max(e.serial) FROM InvStripDetail e WHERE e.invStripId.id =:invStripId ", params);

        if (serial == null) {
            return 1;
        } else {
            serial = serial + 1;
            return serial;
        }
    }

    @Override
    public InvInvoiceDTO findInvStripById(InvInvoiceDTO invInvoiceDTO) {
        InvStrip invStrip = dao.findEntityById(InvStrip.class, invInvoiceDTO.getId());
        InvInvoiceDTO invPurchaseInvoiceDTO = mapToDTO(invStrip, invInvoiceDTO);
        invPurchaseInvoiceDTO = invGlobalService.updateSummition(invPurchaseInvoiceDTO);
        return invPurchaseInvoiceDTO;
    }

    private InvInvoiceDTO mapToDTO(InvStrip entity, InvInvoiceDTO invInvoiceDTO) {

        invInvoiceDTO = initData(invInvoiceDTO, entity);

        return invInvoiceDTO;
    }

    //mapToEntity
    private InvStrip mapToEntity(InvInvoiceDTO invInvoiceDTO, IsagUser isagUser) {

        InvStrip invStrip = new InvStrip();

        if (invInvoiceDTO.getInvInventoryId() != null) {
            InvInventory invInventory = new InvInventory();
            invInventory.setId(invInvoiceDTO.getInvInventoryId().getId());
            invStrip.setInventoryId(invInventory);
        }

        invStrip.setId(invInvoiceDTO.getId());

        invStrip.setStripDate(invInvoiceDTO.getDate());
        invStrip.setRemarks(invInvoiceDTO.getRemarks());
        invStrip.setStripDocument(invInvoiceDTO.getSupplierInvoiceNumber());
        invStrip.setType(invInvoiceDTO.getType());

        if (invInvoiceDTO.getCreatedBy() != null) {
            invStrip.setSerial(invInvoiceDTO.getSerial());
            IsagUser user = new IsagUser();
            user.setId(invInvoiceDTO.getCreatedBy());
            invStrip.setCreatedBy(user);
            invStrip.setCreationDate(invInvoiceDTO.getCreatedDate());
            invStrip.setModifiedBy(isagUser);
            invStrip.setModificationDate(new Date());
            if (invInvoiceDTO.getCompanyId() != null) {
                IsagCompany company = new IsagCompany();
                company.setId(invInvoiceDTO.getCompanyId());
                invStrip.setCompanyId(company);
            }

            if (invInvoiceDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(invInvoiceDTO.getBranchId());
                invStrip.setBranchId(branch);
            }
        } else {
            invStrip.setCreatedBy(isagUser);
            invStrip.setCreationDate(new Date());
            invStrip.setCompanyId(isagUser.getCompanyId());
            invStrip.setBranchId(isagUser.getBranchId());
        }

        return invStrip;
    }

    private InvInvoiceDTO initData(InvInvoiceDTO invInvoiceDTO, InvStrip entity) {
        invInvoiceDTO.setId(entity.getId());
        invInvoiceDTO.setSerial(entity.getSerial());
        invInvoiceDTO.setCompanyActivity(entity.getBranchId().getCompanyActivity());
        invInvoiceDTO.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);
        invInvoiceDTO.setDate(entity.getStripDate());
        invInvoiceDTO.setRemarks(entity.getRemarks());
        invInvoiceDTO.setSupplierInvoiceNumber(entity.getStripDocument());
        invInvoiceDTO.setType(entity.getType());

        if (entity.getInventoryId() != null) {
            InvInventoryDTO invInventoryDTO = new InvInventoryDTO();
            invInventoryDTO.setId(entity.getInventoryId().getId());
            invInventoryDTO.setName(entity.getInventoryId().getName());
            invInventoryDTO.setCode(entity.getInventoryId().getCode());
            invInvoiceDTO.setInvInventoryIdTo(invInventoryDTO);
        }

        invInvoiceDTO.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        invInvoiceDTO.setCreatedDate(entity.getCreationDate());
        invInvoiceDTO.setIndex(entity.getId());
        invInvoiceDTO.setCompanyId(entity.getCompanyId().getId() != null ? entity.getCompanyId().getId() : null);
        invInvoiceDTO.setCreatedByName(entity.getCreatedBy() != null ? entity.getCreatedBy().getName() : null);

        List<InvDetailDTO> invPurchaseInvoiceDetailsListDTO = invStripDetailService.getAllInvTransferDetailsByInvPurchaseInvoiceIdDTO(invInvoiceDTO, entity.getBranchId().getId());

        invInvoiceDTO.setInvPurchaseInvoiceDetailList(invPurchaseInvoiceDetailsListDTO);
        invInvoiceDTO.setInvPurchaseInvoiceDetailsDeletedList(new ArrayList<InvDetailDTO>());

        invInvoiceDTO = invGlobalService.updateSummition(invInvoiceDTO);
        return invInvoiceDTO;
    }

    @Override
    public InvInvoiceDTO save(InvInvoiceDTO invInvoiceDTO, IsagUser isagUser) {
        try {
            invInvoiceDTO = validateSave(invInvoiceDTO);

            InvStrip invStrip;
            if (invInvoiceDTO != null && (invInvoiceDTO.getMsg() == null || invInvoiceDTO.getMsg().isEmpty())) {
                invStrip = mapToEntity(invInvoiceDTO, isagUser);
                if (invStrip.getId() != null) {
                    //بعمل حفظ فى حالة التعديل
                    invStrip = dao.updateEntity(invStrip);
                } else {
                    // فى حالة ان الفاتورة جديدة بروح اجيب السيريال الاول 
                    Integer serial = genericService.getMaxGenericSerialByType(InvStrip.class, invStrip.getBranchId().getId(), null);
                    invStrip.setSerial(serial);

                    invStrip = dao.updateEntity(invStrip);
                    invInvoiceDTO.setId(invStrip.getId());
                }

                invStripDetailService.deleteInvTransferDetailListDTO(invInvoiceDTO.getInvPurchaseInvoiceDetailsDeletedList());
                String error;
                error = invStripDetailService.addInvStripDetailsDTO(invInvoiceDTO.getInvPurchaseInvoiceDetailList(), invInvoiceDTO, invInvoiceDTO.getScreenName(), isagUser);
                if (error != null && !error.isEmpty()) {
                    invInvoiceDTO.setMsg(error);
                    dao.rollbackQuery();
                } else {
                    invInvoiceDTO = mapToDTO(invStrip, invInvoiceDTO);
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

    @Override
    public InvInvoiceDTO convertInvStripListToInvInvoice(InvInvoiceDTO invInvoiceDTO) {

        List<InvDetailDTO> detailList = new ArrayList<>();
        
        if (invInvoiceDTO.getInvDailogueDataListSelected() != null && !invInvoiceDTO.getInvDailogueDataListSelected().isEmpty()) {
            Map<String, InvDetailDTO> mapInvDetailDTO = new HashMap<>();
            for (InvDailogueData invDailogueData : invInvoiceDTO.getInvDailogueDataListSelected()) {
                InvInvoiceDTO invInvoiceDTO1 = new InvInvoiceDTO();
                invInvoiceDTO1.setId(invDailogueData.getId());
                invInvoiceDTO1 = findInvStripById(invInvoiceDTO1);
                if (invInvoiceDTO1.getInvPurchaseInvoiceDetailList() != null && !invInvoiceDTO1.getInvPurchaseInvoiceDetailList().isEmpty()) {
                    for (InvDetailDTO detailDTO : invInvoiceDTO1.getInvPurchaseInvoiceDetailList()) {
                        if (mapInvDetailDTO.containsKey(detailDTO.getItemsBarcodesDetail().getBarcode())) {
                            InvDetailDTO dTO = mapInvDetailDTO.get(detailDTO.getItemsBarcodesDetail().getBarcode());
                            dTO.setActualQuantity(detailDTO.getActualQuantity().add(dTO.getActualQuantity()));
                            dTO.setCostAvarage(detailDTO.getCostAvarage());
                            mapInvDetailDTO.put(dTO.getItemsBarcodesDetail().getBarcode(), dTO);
                        } else {
                            detailDTO.setId(null);
                            mapInvDetailDTO.put(detailDTO.getItemsBarcodesDetail().getBarcode(), detailDTO);
                        }
                    }
                }
            }
            List<InvDetailDTO> detailDTOList = new ArrayList(mapInvDetailDTO.values());

            invInvoiceDTO.setInvStripDetailList(detailDTOList);
            
            for(InvDetailDTO dTO : detailDTOList){
                dTO.setAvailableQuantityRow(quantityItemsStoreByDateService.getQuantatyItemByinventory(invInvoiceDTO.getInvInventoryId().getId(), dTO.getItemsBarcodesDetail().getItemId(), invInvoiceDTO.getInventorySetupDTO().getSellBuy(), invInvoiceDTO.getDate()));
                if(dTO.getActualQuantity().compareTo(dTO.getAvailableQuantityRow()) != 0){
                    detailList.add(dTO);
                }
            }
        }

        invInvoiceDTO.setInvPurchaseInvoiceDetailList(detailList);
        return invInvoiceDTO;
    }

}
