package com.toby.businessservice;

import com.toby.bussinessservice.global.InvPurchaseReturnSave;
import com.toby.core.GenericDAO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvInventoryTransactionDTO;
import com.toby.dto.InvStripDTO;
import com.toby.entity.Branch;
import com.toby.entity.InvInventory;
import com.toby.entity.InvInventoryTransaction;
import com.toby.entity.InvStrip;
import com.toby.entity.InvStripDetail;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
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

    public InvStrip mapToEntity(InvStripDTO invStripDTO, TobyUser tobyUser) {
        InvStrip invStrip = new InvStrip();
        invStrip.setId(invStripDTO.getId());
        invStrip.setRemarks(invStripDTO.getRemarks());
        invStrip.setSerial(invStripDTO.getSerial());
        invStrip.setStripDate(invStripDTO.getStripDate());
        invStrip.setStripDocument(invStripDTO.getStripDocument());
        invStrip.setType(invStripDTO.getType());

        if (invStripDTO.getInventoryId() != null) {
            InvInventory invInventory = new InvInventory();
            invInventory.setId(invStripDTO.getInventoryId().getId());
            invInventory.setName(invStripDTO.getInventoryId().getName());
            invStrip.setInventoryId(invInventory);
        }

        if (invStripDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(invStripDTO.getCreatedBy());
            invStrip.setCreatedBy(user);
            invStrip.setCreationDate(invStripDTO.getCreatedDate());
            invStrip.setModifiedBy(tobyUser);
            invStrip.setModificationDate(new Date());
            if (invStripDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(invStripDTO.getCompanyId());
                invStrip.setCompanyId(company);
            }
            if (invStripDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(invStripDTO.getBranchId());
                invStrip.setBranchId(branch);
            }
        } else {
            invStrip.setCreatedBy(tobyUser);
            invStrip.setCreationDate(new Date());
            invStrip.setCompanyId(tobyUser.getCompanyId());
            invStrip.setBranchId(tobyUser.getBranchId());

        }
        return invStrip;
    }

    public InvStripDTO mapToDTO(InvStrip invStrip, TobyUser tobyUser) {
        InvStripDTO invStripDTO = new InvStripDTO();
        invStripDTO.setId(invStrip.getId());
        invStripDTO.setRemarks(invStrip.getRemarks());
        invStripDTO.setSerial(invStrip.getSerial());
        invStripDTO.setStripDate(invStrip.getStripDate());
        invStripDTO.setStripDocument(invStrip.getStripDocument());
        invStripDTO.setType(invStrip.getType());
        invStripDTO.setIndex(invStrip.getId());
        if (invStrip.getInventoryId() != null) {
            InvInventoryDTO invInventoryDTO = new InvInventoryDTO();
            invInventoryDTO.setId(invStrip.getInventoryId().getId());
            invInventoryDTO.setName(invStrip.getInventoryId().getName());
            invStripDTO.setInventoryId(invInventoryDTO);
        }
        invStripDTO.setCreatedBy(invStrip.getCreatedBy() != null ? invStrip.getCreatedBy().getId() : null);
        invStripDTO.setCreatedDate(invStrip.getCreationDate());
        invStripDTO.setBranchId(invStrip.getBranchId().getId() != null ? invStrip.getBranchId().getId() : null);
        invStripDTO.setCompanyId(invStrip.getCompanyId() != null ? invStrip.getCompanyId().getId() : null);

        return invStripDTO;

    }

    @Override
    public InvStripDTO findInvStripByInvStripIdDTO(Integer invStripId , TobyUser tobyUser) {
        return mapToDTO(dao.findEntityById(InvStrip.class, invStripId), tobyUser);
    }
}
