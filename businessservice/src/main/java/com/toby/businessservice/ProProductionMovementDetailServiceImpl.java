package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.ProProductMovementDTO;
import com.toby.dto.ProProductMovementDetailDTO;
import com.toby.entity.Branch;
import com.toby.entity.InvPurchaseInvoice;
import com.toby.entity.ProProductMovement;
import com.toby.entity.ProProductMovementDetail;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class ProProductionMovementDetailServiceImpl implements ProProductionMovementDetailService {

    @EJB
    private GenericDAO dao;

    @Override
    public ProProductMovementDTO findByParent(ProProductMovementDTO dTO) {
        ProProductMovementDTO proProductMovementDTO = new ProProductMovementDTO();
        Map<String, Object> params = new HashMap<>();
        params.put("Id", dTO.getId());
        List<ProProductMovementDetail> proProductMovementDetails = dao.findListByQuery("select p from ProProductMovementDetail p where p.proProductionDeliveryId.id=:id", params);
        proProductMovementDTO = dTO;
        proProductMovementDTO.setProProductMovementDetailDTOList(mapToDTOList(proProductMovementDetails));
        return proProductMovementDTO;

    }

    @Override
    public List<ProProductMovementDetailDTO> addProProductMovementDetailList(List<ProProductMovementDetailDTO> proProductMovementDetailDTOList, Integer id, TobyUser tobyUser) {
        List<ProProductMovementDetailDTO> list = new ArrayList<>();
        for (ProProductMovementDetailDTO detail : proProductMovementDetailDTOList) {
            detail.setProProductionDeliveryId(id);
            ProProductMovementDetail proProductMovementDetail = mapToEntity(detail, tobyUser);
            dao.saveEntity(proProductMovementDetail);
            ProProductMovementDetailDTO proProductMovementDetailDTO = mapToDTO(proProductMovementDetail);
            list.add(proProductMovementDetailDTO);

        }
        return list;
    }

    @Override
    public List<ProProductMovementDetailDTO> getDetailsBySerial(Integer deliveryId) {
        Map<String, Object> params = new HashMap<>();
        params.put("deliveryId", deliveryId);
        List<ProProductMovementDetail> proProductMovementDetails = dao.findListByQuery("select p from ProProductMovementDetail p where p.proProductionDeliveryId.id=:deliveryId", params);
        return mapToDTOList(proProductMovementDetails);
    }

    @Override
    public List<ProProductMovementDetailDTO> getDetailsByDelivery(Integer deliveryId) {
        Map<String, Object> params = new HashMap<>();
        params.put("deliveryId", deliveryId);
        List<ProProductMovementDetail> proProductMovementDetails = dao.findListByQuery("select p from ProProductMovementDetail p where p.proProductionDeliveryId.invOrganizationSiteId.id=:deliveryId and p.proProductionDeliveryId.type=1", params);
        return mapToDTOList(proProductMovementDetails);
    }

    public ProProductMovementDetail mapToEntity(ProProductMovementDetailDTO proProductMovementDetailDTO, TobyUser tobyUser) {
        ProProductMovementDetail proProductMovementDetail = new ProProductMovementDetail();
        proProductMovementDetail.setId(proProductMovementDetailDTO.getId());
        if (proProductMovementDetailDTO.getInvPurchesInvoiceId() != null) {
            InvPurchaseInvoice invPurchesInvoice = new InvPurchaseInvoice();
            invPurchesInvoice.setId(proProductMovementDetailDTO.getInvPurchesInvoiceId());
            proProductMovementDetail.setInvPurchesInvoiceId(invPurchesInvoice);
        }
        if (proProductMovementDetailDTO.getProProductionDeliveryId() != null) {
            ProProductMovement proProductMovement = new ProProductMovement();
            proProductMovement.setId(proProductMovementDetailDTO.getProProductionDeliveryId());
            proProductMovementDetail.setProProductionDeliveryId(proProductMovement);
        }
        if (proProductMovementDetailDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(proProductMovementDetailDTO.getCreatedBy());
            proProductMovementDetail.setCreatedBy(user);
            proProductMovementDetail.setCreationDate(proProductMovementDetailDTO.getCreatedDate());
            proProductMovementDetail.setModifiedBy(tobyUser);
            proProductMovementDetail.setModificationDate(new Date());
            if (proProductMovementDetailDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(proProductMovementDetailDTO.getCompanyId());
                proProductMovementDetail.setCompanyId(company);
            }

            if (proProductMovementDetailDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(proProductMovementDetailDTO.getBranchId());
                proProductMovementDetail.setBranchId(branch);
            }
        } else {
            proProductMovementDetail.setCreatedBy(tobyUser);
            proProductMovementDetail.setCreationDate(new Date());
            proProductMovementDetail.setCompanyId(tobyUser.getCompanyId());
            proProductMovementDetail.setBranchId(tobyUser.getBranchId());
        }

        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        params.put("proProductMovementId", proProductMovementDetail.getProProductionDeliveryId().getId());
        Integer serialmax = 0;
        synchronized (serialmax) {
            try {
                serialmax = dao.findEntityByQuery("SELECT MAX(p.serial) FROM ProProductMovementDetail p  WHERE p.branchId.id =:branchId and p.proProductionDeliveryId.id =:proProductMovementId ", params);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (serialmax == null) {
            serialmax = 0;
        }
        proProductMovementDetail.setSerial(serialmax + 1);

        return proProductMovementDetail;
    }

    public ProProductMovementDetailDTO mapToDTO(ProProductMovementDetail proProductMovementDetail) {
        ProProductMovementDetailDTO proProductMovementDetailDTO = new ProProductMovementDetailDTO();
        proProductMovementDetailDTO.setId(proProductMovementDetail.getId());
        proProductMovementDetailDTO.setSerial(proProductMovementDetail.getSerial());
        proProductMovementDetailDTO.setInvPurchesInvoiceId(proProductMovementDetail.getInvPurchesInvoiceId() != null ? proProductMovementDetail.getInvPurchesInvoiceId().getId() : null);
        proProductMovementDetailDTO.setInvPurchesInvoiceSerial(proProductMovementDetail.getInvPurchesInvoiceId() != null ? proProductMovementDetail.getInvPurchesInvoiceId().getSerial() : null);
        proProductMovementDetailDTO.setInvPurchesInvoiceIdBackUp(proProductMovementDetail.getInvPurchesInvoiceId() != null ? proProductMovementDetail.getInvPurchesInvoiceId().getId() : null);
        proProductMovementDetailDTO.setProProductionDeliveryId(proProductMovementDetail.getProProductionDeliveryId() != null ? proProductMovementDetail.getProProductionDeliveryId().getId() : null);
        proProductMovementDetailDTO.setCreatedBy(proProductMovementDetail.getCreatedBy() != null ? proProductMovementDetail.getCreatedBy().getId() : null);
        proProductMovementDetailDTO.setCreatedDate(proProductMovementDetail.getCreationDate());
        proProductMovementDetailDTO.setBranchId(proProductMovementDetail.getBranchId().getId() != null ? proProductMovementDetail.getBranchId().getId() : null);
        proProductMovementDetailDTO.setCompanyId(proProductMovementDetail.getCompanyId() != null ? proProductMovementDetail.getCompanyId().getId() : null);
        return proProductMovementDetailDTO;
    }

    public ProProductMovementDetailDTO mapInvPurchaseInvoiceToDTO(InvPurchaseInvoice purchaseInvoice) {
        ProProductMovementDetailDTO proProductMovementDetailDTO = new ProProductMovementDetailDTO();
        proProductMovementDetailDTO.setInvPurchesInvoiceSerial(purchaseInvoice.getSerial());
        proProductMovementDetailDTO.setInvPurchesInvoiceId(purchaseInvoice.getId());
        return proProductMovementDetailDTO;
    }

    public List<ProProductMovementDetailDTO> mapToDTOList(List<ProProductMovementDetail> proProductMovementDetails) {

        List<ProProductMovementDetailDTO> proProductMovementDetailDTOs = new ArrayList<>();
        for (ProProductMovementDetail proProductMovementDetail : proProductMovementDetails) {
            proProductMovementDetailDTOs.add(mapToDTO(proProductMovementDetail));
        }
        return proProductMovementDetailDTOs;
    }

    public List<ProProductMovementDetailDTO> mapInvPurchaseInvoiceToDTOList(List<InvPurchaseInvoice> invPurchaseInvoices) {
        List<ProProductMovementDetailDTO> proProductMovementDetailDTOs = new ArrayList<>();
        for (InvPurchaseInvoice invoice : invPurchaseInvoices) {
            proProductMovementDetailDTOs.add(mapInvPurchaseInvoiceToDTO(invoice));
        }
        return proProductMovementDetailDTOs;
    }

    @Override
    public List<ProProductMovementDetailDTO> getinpurchaseinvoice(TobyUser tobyUser) {
        Map<String, Object> map = new HashMap<>();
        map.put("branchId", tobyUser.getBranchId().getId());
        List<ProProductMovementDetail> details = dao.findListByQuery("SELECT e FROM ProProductMovementDetail e where e.branchId.id=:branchId", map);

        return mapToDTOList(details);

    }

    @Override
    public List<ProProductMovementDetailDTO> getAllInvPurchaseInvoice(TobyUser tobyUser, ProProductMovementDTO movementDTO) {
        Map<String, Object> map = new HashMap<>();
        map.put("galary", movementDTO.getInvGalary() != null ? movementDTO.getInvGalary().getId() : null);
        List<InvPurchaseInvoice> invPurchaseInvoices = dao.findListByQuery("SELECT i from InvPurchaseInvoice i where i.invInventoryId.id=:galary and i.type = true and (i.isdeleted !=1 or i.isdeleted is null) and i.id not in (SELECT p.invPurchesInvoiceId.id from ProProductMovementDetail p where p.proProductionDeliveryId.type = 3)", map);
        return mapInvPurchaseInvoiceToDTOList(invPurchaseInvoices);
    }

    @Override
    public List<ProProductMovementDetailDTO> getAllInvoiceFromGalaryNotInClient(TobyUser tobyUser) {
        List<ProProductMovementDetail> movementDetails = dao.findListByQuery("SELECT p from ProProductMovementDetail p where  p.proProductionDeliveryId.type=2 and p.invPurchesInvoiceId.id not in (SELECT d.invPurchesInvoiceId.id from ProProductMovementDetail d where  d.proProductionDeliveryId.type=4)");
        return mapToDTOList(movementDetails);
    }

    @Override
    public List<ProProductMovementDetailDTO> getAllInvoiceByClient(TobyUser tobyUser, InvOrganizationSiteDTO dTO) {
        Map<String, Object> map = new HashMap<>();
        map.put("organizationSiteId", dTO.getId());
        List<ProProductMovementDetail> movementDetails = dao.findListByQuery("SELECT p from ProProductMovementDetail p where  p.proProductionDeliveryId.type=2 and p.invPurchesInvoiceId.organizationSiteId.id =:organizationSiteId and p.invPurchesInvoiceId.id not in (SELECT d.invPurchesInvoiceId.id from ProProductMovementDetail d where  d.proProductionDeliveryId.type=4) ", map);
        //List<InvPurchaseInvoice> invoices =dao.findListByQuery("SELECT i from InvPurchaseInvoice i where  i.organizationSiteId.id =:organizationSiteId and i.id not in(SELECT p.invPurchesInvoiceId.id from ProProductMovementDetail p where p.proProductionDeliveryId.type=4 and p.proProductionDeliveryId.invOrganizationSiteId.id=:organizationSiteId)",map);
        //return mapInvPurchaseInvoiceToDTOList(invoices);
        return mapToDTOList(movementDetails);
    }

    @Override
    public List<ProProductMovementDetailDTO> getAllByType(Integer type) {
        List<ProProductMovementDetail> movementDetails = dao.findListByQuery("SELECT p from ProProductMovementDetail p where  p.proProductionDeliveryId.type = " + type);
        return mapToDTOList(movementDetails);

    }

    @Override
    public void deleteByInvoice(ProProductMovementDetailDTO movementDetailDTO) {
        dao.executeDeleteQuery("delete from ProProductMovementDetail p where p.invPurchesInvoiceId.id = " + movementDetailDTO.getInvPurchesInvoiceId() + " and p.proProductionDeliveryId.id = " + movementDetailDTO.getProProductionDeliveryId());
    }

    @Override
    public void deleteBySelected(ProProductMovementDetailDTO movementDetailDTO) {
        dao.executeDeleteQuery("delete from ProProductMovementDetail pd where pd.id= " + movementDetailDTO.getId());
    }

}
