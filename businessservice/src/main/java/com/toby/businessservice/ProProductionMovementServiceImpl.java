package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.InvInventoryDTO;
import com.toby.dto.InvOrganizationSiteDTO;
import com.toby.dto.ProProductMovementDTO;
import com.toby.entity.Branch;
import com.toby.entity.InvInventory;
import com.toby.entity.InvOrganizationSite;
import com.toby.entity.ProProductMovement;
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
public class ProProductionMovementServiceImpl implements ProProductionMovementService {

    @EJB
    private GenericDAO dao;

    @EJB
    private ProProductionMovementService proProductionMovementService;
    @EJB
    private ProProductionMovementDetailService proProductionMovementDetailService;

    @Override
    public List<ProProductMovementDTO> getAllByType(Integer type) {
        List<ProProductMovement> proProductMovements = dao.findListByQuery("select p from ProProductMovement p where p.type = " + type + " ORDER BY p.id DESC");
        return mapToDTOList1(proProductMovements);
    }

    @Override
    public ProProductMovementDTO addProProductMovement(ProProductMovementDTO proProductMovementDTO, int type, TobyUser tobyUser) {
        ProProductMovement proProductMovement = mapToEntity(proProductMovementDTO, type, tobyUser);
        proProductMovement = dao.updateEntity(proProductMovement);
        ProProductMovementDTO proProductMovementDTO1 = mapToDTO(proProductMovement);
        proProductMovementDTO1.setProProductMovementDetailDTOList(proProductionMovementDetailService.addProProductMovementDetailList(proProductMovementDTO.getProProductMovementDetailDTOList(), proProductMovementDTO1.getId(), tobyUser));

        return proProductMovementDTO1;
    }

    @Override
    public List<ProProductMovementDTO> getMovementSerial() {
        List<ProProductMovement> proProductMovements = dao.findListByQuery("select p from ProProductMovement p where p.type = 1 and p.id not in(select p.parent.id from ProProductMovement p where p.type=2)");
        return mapToDTOList(proProductMovements);
    }

    @Override
    public ProProductMovementDTO getMovementByDelivery(ProProductMovementDTO DTO) {
        Map<String, Object> params = new HashMap<>();
        params.put("invOrganizationSiteId", DTO.getInvOrganizationSiteId().getId());
        ProProductMovement proProductMovement = dao.findEntityByQuery("select p from ProProductMovement p where p.invOrganizationSiteId.id =:invOrganizationSiteId and p.type=1 ", params);
        ProProductMovementDTO proProductMovementDTO = mapToDTO(proProductMovement);
        proProductMovementDTO.setProProductMovementDetailDTOList(proProductionMovementDetailService.getDetailsBySerial(proProductMovementDTO.getId()));
        return proProductMovementDTO;
    }

    public ProProductMovement mapToEntity(ProProductMovementDTO proProductMovementDTO, int type, TobyUser tobyUser) {
        ProProductMovement proProductMovement = new ProProductMovement();
        proProductMovement.setId(proProductMovementDTO.getId());
        proProductMovement.setDate(proProductMovementDTO.getDate());
        proProductMovement.setTime(proProductMovementDTO.getTime());
        proProductMovement.setRemark(proProductMovementDTO.getRemark());
        proProductMovement.setSerial(proProductMovementDTO.getSerial());
        if (proProductMovementDTO.getParent() != null) {
            ProProductMovement proProductMovement1 = new ProProductMovement();
            proProductMovement1.setId(proProductMovementDTO.getParent().getId());
            proProductMovement1.setSerial(proProductMovementDTO.getParent().getSerial());
            proProductMovement.setParent(proProductMovement1);
        }
        if (proProductMovementDTO.getInventory() != null) {
            InvInventory inventory = new InvInventory();
            inventory.setId(proProductMovementDTO.getInventory().getId());
            inventory.setName(proProductMovementDTO.getInventory().getName());
            proProductMovement.setInvInventoryId(inventory);
        }
        if (proProductMovementDTO.getInvGalary() != null) {
            InvInventory invGalary = new InvInventory();
            invGalary.setId(proProductMovementDTO.getInvGalary().getId());
            invGalary.setName(proProductMovementDTO.getInvGalary().getName());
            proProductMovement.setInvGalaryId(invGalary);
        }
        if (proProductMovementDTO.getInvOrganizationSiteId() != null) {
            InvOrganizationSite organizationSite = new InvOrganizationSite();
            organizationSite.setId(proProductMovementDTO.getInvOrganizationSiteId().getId());
            organizationSite.setName(proProductMovementDTO.getInvOrganizationSiteId().getName());
            if (type == 4) {
                proProductMovement.setClient(organizationSite);
            } else {
                proProductMovement.setInvOrganizationSiteId(organizationSite);
            }
        }

        if (proProductMovementDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            proProductMovement.setType(proProductMovementDTO.getType());
            user.setId(proProductMovementDTO.getCreatedBy());
            proProductMovement.setCreatedBy(user);
            proProductMovement.setCreationDate(proProductMovementDTO.getCreatedDate());
            proProductMovement.setModifiedBy(tobyUser);
            proProductMovement.setModificationDate(new Date());
            if (proProductMovementDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(proProductMovementDTO.getCompanyId());
                proProductMovement.setCompanyId(company);
            }

            if (proProductMovementDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(proProductMovementDTO.getBranchId());
                proProductMovement.setBranchId(branch);
            }
        } else {
            proProductMovement.setType(type);
            proProductMovement.setCreatedBy(tobyUser);
            proProductMovement.setCreationDate(new Date());
            proProductMovement.setCompanyId(tobyUser.getCompanyId());
            proProductMovement.setBranchId(tobyUser.getBranchId());

            /// Get Serial ///
            Map<String, Object> params = new HashMap<>();
            params.put("branchId", tobyUser.getBranchId().getId());
            params.put("type", type);
            Integer serialmax = 0;
            synchronized (serialmax) {
                try {
                    serialmax = dao.findEntityByQuery("SELECT MAX(p.serial) FROM ProProductMovement p  WHERE p.branchId.id =:branchId and p.type = :type ", params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (serialmax == null) {
                serialmax = 0;
            }
            proProductMovement.setSerial(serialmax + 1);
        }

        return proProductMovement;
    }

    public ProProductMovementDTO mapToDTO(ProProductMovement proProductMovement) {
        ProProductMovementDTO proProductMovementDTO = new ProProductMovementDTO();
        proProductMovementDTO.setId(proProductMovement.getId());

        proProductMovementDTO.setDate(proProductMovement.getDate());
        proProductMovementDTO.setTime(proProductMovement.getTime());
        proProductMovementDTO.setRemark(proProductMovement.getRemark());
        proProductMovementDTO.setSerial(proProductMovement.getSerial());
        proProductMovementDTO.setType(proProductMovement.getType());
        if (proProductMovement.getParent() != null) {
            ProProductMovementDTO parent = new ProProductMovementDTO();
            parent.setId(proProductMovement.getParent().getId());
            parent.setSerial(proProductMovement.getParent().getSerial());
            proProductMovementDTO.setParent(parent);
        }

        if (proProductMovement.getInvInventoryId() != null) {
            InvInventoryDTO inventoryDTO = new InvInventoryDTO();
            inventoryDTO.setId(proProductMovement.getInvInventoryId().getId());
            inventoryDTO.setName(proProductMovement.getInvInventoryId().getName());
            inventoryDTO.setCode(proProductMovement.getInvInventoryId().getCode());
            proProductMovementDTO.setInventory(inventoryDTO);
        }
        if (proProductMovement.getInvGalaryId() != null) {
            InvInventoryDTO invGalary = new InvInventoryDTO();
            invGalary.setId(proProductMovement.getInvGalaryId().getId());
            invGalary.setName(proProductMovement.getInvGalaryId().getName());
            invGalary.setCode(proProductMovement.getInvGalaryId().getCode());
            proProductMovementDTO.setInvGalary(invGalary);
        }
        if (proProductMovement.getType() == 4) {
            if (proProductMovement.getClient() != null) {
                InvOrganizationSiteDTO organizationSiteDTO = new InvOrganizationSiteDTO();
                organizationSiteDTO.setId(proProductMovement.getClient().getId());
                organizationSiteDTO.setName(proProductMovement.getClient().getName());
                proProductMovementDTO.setInvOrganizationSiteId(organizationSiteDTO);
            }

        } else {
            if (proProductMovement.getInvOrganizationSiteId() != null) {
                InvOrganizationSiteDTO organizationSiteDTO = new InvOrganizationSiteDTO();
                organizationSiteDTO.setId(proProductMovement.getInvOrganizationSiteId().getId());
                organizationSiteDTO.setName(proProductMovement.getInvOrganizationSiteId().getName());
                proProductMovementDTO.setInvOrganizationSiteId(organizationSiteDTO);
            }
        }

        //proProductMovementDTO.setParent(proProductMovement.getParent()!=null ? proProductMovement.getParent().getId():null);
        proProductMovementDTO.setCreatedBy(proProductMovement.getCreatedBy() != null ? proProductMovement.getCreatedBy().getId() : null);
        proProductMovementDTO.setCreatedDate(proProductMovement.getCreationDate());
        proProductMovementDTO.setBranchId(proProductMovement.getBranchId().getId() != null ? proProductMovement.getBranchId().getId() : null);
        proProductMovementDTO.setCompanyId(proProductMovement.getCompanyId() != null ? proProductMovement.getCompanyId().getId() : null);

        return proProductMovementDTO;
    }

    public List<ProProductMovementDTO> mapToDTOList(List<ProProductMovement> proProductMovements) {

        List<ProProductMovementDTO> proProductMovementDTOs = new ArrayList<>();
        for (ProProductMovement proProductMovement : proProductMovements) {
            proProductMovementDTOs.add(mapToDTO(proProductMovement));
        }
        return proProductMovementDTOs;
    }

    public List<ProProductMovementDTO> mapToDTOList1(List<ProProductMovement> proProductMovements) {

        List<ProProductMovementDTO> proProductMovementDTOs = new ArrayList<>();
        for (ProProductMovement proProductMovement : proProductMovements) {
            ProProductMovementDTO dto = mapToDTO(proProductMovement);
            dto.setProProductMovementDetailDTOList(proProductionMovementDetailService.getDetailsBySerial(dto.getId()));
            proProductMovementDTOs.add(dto);
        }
        return proProductMovementDTOs;
    }

    @Override
    public void deleteBySelected(Integer id) {
        dao.executeDeleteQuery("delete from ProProductMovementDetail pd where pd.proProductionDeliveryId.id= " + id);
        dao.executeDeleteQuery("delete from ProProductMovement p where p.id= " + id);
    }

    @Override
    public ProProductMovementDTO getById(Integer id) {
        ProProductMovement proProductMovement = dao.findEntityById(ProProductMovement.class, id);
        return mapToDTO(proProductMovement);
    }

}
