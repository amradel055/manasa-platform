package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.InvItemDTO;
import com.toby.dto.ProGroupDTO;
import com.toby.dto.ProItemProductionStagesDTO;
import com.toby.dto.ProProductionStagesDTO;
import com.toby.entity.Branch;
import com.toby.entity.InvItem;
import com.toby.entity.ProGroup;
import com.toby.entity.ProItemProductionStages;
import com.toby.entity.ProProductionStages;
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
public class ProItemProductionStagesServiceImpl implements ProItemProductionStagesService {

    @EJB
    private GenericDAO dao;
    @EJB
    private ProProductionStagesService proProductionStagesService;
    @EJB
    private InvItemService invItemService;

    @Override
    public List<ProItemProductionStagesDTO> getAllInvItem(TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        List<ProProductionStages> productionStageses = dao.findListByQuery("SELECT p FROM ProProductionStages p WHERE p.branchId.id = :branchId ", params);
        return mapProductionStagesListToDTOList(productionStageses, tobyUser);

    }

    @Override
    public ProItemProductionStagesDTO findByDTO(TobyUser tobyUser, ProItemProductionStagesDTO dTO) {
        Map<String, Object> params = new HashMap<>();
        params.put("invItemId", dTO.getInvItemId() != null ? dTO.getInvItemId().getId() : null);
        params.put("stageId", dTO.getProProductionStagesId() != null ? dTO.getProProductionStagesId().getId() : null);
        ProItemProductionStages proItemProductionStages = dao.findEntityByQuery("SELECT p FROM ProItemProductionStages p WHERE p.invItemId.id =:invItemId and p.proProductionStagesId.id =:stageId", params);
        if (proItemProductionStages == null) {
            return null;
        } else {
            return mapToDTO(proItemProductionStages);
        }
    }

    @Override
    public List<ProItemProductionStagesDTO> getAll(TobyUser tobyUser, Integer proGroupId) {
        Map<String, Object> params = new HashMap<>();
        params.put("proGroupId", proGroupId);
        List<ProItemProductionStages> proItemProductionStageses = dao.findListByQuery("SELECT p FROM ProItemProductionStages p WHERE p.proGroupId.id =:proGroupId", params);
//        List<ProItemProductionStages> proItemProductionStageses = dao.findListByQuery("SELECT p FROM ProItemProductionStages p WHERE p.branchId.id = :branchId and p.invItemId.id =:invItemId", params);
        return mapToDTOList(proItemProductionStageses, tobyUser);
    }

    @Override
    public ProItemProductionStagesDTO save(ProItemProductionStagesDTO proItemProductionStagesDTO, TobyUser tobyUser) {
        ProItemProductionStages proItemProductionStages = mapToEntity(proItemProductionStagesDTO, tobyUser);
        proItemProductionStages = dao.updateEntity(proItemProductionStages);
        return mapToDTO(proItemProductionStages);
    }

    public ProItemProductionStages mapToEntity(ProItemProductionStagesDTO proItemProductionStagesDTO, TobyUser tobyUser) {
        ProItemProductionStages proItemProductionStages = new ProItemProductionStages();
        proItemProductionStages.setId(proItemProductionStagesDTO.getId());
        proItemProductionStages.setSerial(proItemProductionStagesDTO.getSerial());
        if (proItemProductionStagesDTO.getInvItemId() != null) {
            InvItem invItem = new InvItem();
            invItem.setId(proItemProductionStagesDTO.getInvItemId().getId());
            proItemProductionStages.setInvItemId(invItem);
        }
        if (proItemProductionStagesDTO.getProGroupId() != null) {
            ProGroup proGroup = new ProGroup();
            proGroup.setId(proItemProductionStagesDTO.getProGroupId().getId());
            proItemProductionStages.setProGroupId(proGroup);
        }
        
        if (proItemProductionStagesDTO.getProProductionStagesId() != null) {
            ProProductionStages productionStages = new ProProductionStages();
            productionStages.setId(proItemProductionStagesDTO.getProProductionStagesId().getId());
            proItemProductionStages.setProProductionStagesId(productionStages);
        }
        if (proItemProductionStagesDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(proItemProductionStagesDTO.getCreatedBy());
            proItemProductionStages.setCreatedBy(user);
            proItemProductionStages.setCreationDate(proItemProductionStagesDTO.getCreatedDate());
            proItemProductionStages.setModifiedBy(tobyUser);
            proItemProductionStages.setModificationDate(new Date());
            if (proItemProductionStagesDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(proItemProductionStagesDTO.getCompanyId());
                proItemProductionStages.setCompanyId(company);
            }

            if (proItemProductionStagesDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(proItemProductionStagesDTO.getBranchId());
                proItemProductionStages.setBranchId(branch);
            }
        } else {
            proItemProductionStages.setCreatedBy(tobyUser);
            proItemProductionStages.setCreationDate(new Date());
            proItemProductionStages.setCompanyId(tobyUser.getCompanyId());
            proItemProductionStages.setBranchId(tobyUser.getBranchId());
        }

        return proItemProductionStages;
    }

    public ProItemProductionStagesDTO mapToDTO(ProItemProductionStages proItemProductionStages) {
        ProItemProductionStagesDTO proItemProductionStagesDTO = new ProItemProductionStagesDTO();
        proItemProductionStagesDTO.setId(proItemProductionStages.getId());
        proItemProductionStagesDTO.setSerial(proItemProductionStages.getSerial());
        proItemProductionStagesDTO.setIndex(proItemProductionStages.getId());
        if (proItemProductionStages.getInvItemId() != null) {
            InvItemDTO invItem = new InvItemDTO();
            invItem.setId(proItemProductionStages.getInvItemId().getId());
            proItemProductionStagesDTO.setInvItemId(invItem);
        }
        
        if (proItemProductionStages.getProGroupId() != null) {
            ProGroupDTO proGroupDTO = new ProGroupDTO();
            proGroupDTO.setId(proItemProductionStages.getProGroupId().getId());
            proItemProductionStagesDTO.setProGroupId(proGroupDTO);
        }

        if (proItemProductionStages.getProProductionStagesId() != null) {
            ProProductionStagesDTO productionStages = new ProProductionStagesDTO();
            productionStages.setId(proItemProductionStages.getProProductionStagesId().getId());
            productionStages.setName(proItemProductionStages.getProProductionStagesId().getName());
            proItemProductionStagesDTO.setProProductionStagesId(productionStages);
            proItemProductionStagesDTO.setProProductionStagesIdBak(productionStages);
        }
        proItemProductionStagesDTO.setCreatedBy(proItemProductionStages.getCreatedBy() != null ? proItemProductionStages.getCreatedBy().getId() : null);
        proItemProductionStagesDTO.setCreatedDate(proItemProductionStages.getCreationDate());
        proItemProductionStagesDTO.setBranchId(proItemProductionStages.getBranchId().getId() != null ? proItemProductionStages.getBranchId().getId() : null);
        proItemProductionStagesDTO.setCompanyId(proItemProductionStages.getCompanyId() != null ? proItemProductionStages.getCompanyId().getId() : null);

        return proItemProductionStagesDTO;
    }

    public List<ProItemProductionStagesDTO> mapToDTOList(List<ProItemProductionStages> proItemProductionStageses, TobyUser tobyUser) {
        List<ProItemProductionStagesDTO> proItemProductionStagesDTOs = new ArrayList<>();
        for (ProItemProductionStages productionStages : proItemProductionStageses) {
            proItemProductionStagesDTOs.add(mapToDTO(productionStages));
        }
        return proItemProductionStagesDTOs;
    }

    public List<ProItemProductionStagesDTO> mapProductionStagesListToDTOList(List<ProProductionStages> productionStageses, TobyUser tobyUser) {
        List<ProItemProductionStagesDTO> proItemProductionStagesDTOs = new ArrayList<>();
        for (ProProductionStages stage : productionStageses) {
            proItemProductionStagesDTOs.add(mapProProductionStagesToDTO(stage));
        }
        return proItemProductionStagesDTOs;

    }

    public ProItemProductionStagesDTO mapProProductionStagesToDTO(ProProductionStages productionStage) {
        ProItemProductionStagesDTO proItemProductionStagesDTO = new ProItemProductionStagesDTO();
        if (productionStage.getId() != null) {
            ProProductionStagesDTO productionStages = new ProProductionStagesDTO();
            productionStages.setId(productionStage.getId());
            productionStages.setName(productionStage.getName());
            proItemProductionStagesDTO.setProProductionStagesId(productionStages);
        }

        return proItemProductionStagesDTO;
    }

    @Override
    public void deleteByStage(ProItemProductionStagesDTO itemStages) {
        Integer stagesId;
        if (itemStages.getProProductionStagesIdBak() != null) {
            stagesId = itemStages.getProProductionStagesIdBak().getId();
        } else {
            stagesId = null;
        }
        dao.executeDeleteQuery("delete from ProItemProductionStages p where p.invItemId.id = " + itemStages.getInvItemId().getId() + " and p.proProductionStagesId.id = " + stagesId);

    } 
   
    @Override
    public void saveAll(Map<String, InvItem> invItemMap, TobyUser tobyUser) {
        Integer x = 1;
        List<ProProductionStages> stagesList = proProductionStagesService.getAllProProductionStagesByBranchId3(182);
        for (Map.Entry<String, InvItem> entry : invItemMap.entrySet()) {
            System.out.println("   item  " + entry.getKey() + " -> " + x);
            x++;
            List<InvItem> itemList = invItemService.findItemByCode(entry.getKey(), 182);
            if (itemList != null && !itemList.isEmpty()) {
                ProItemProductionStages proItemProductionStages = new ProItemProductionStages();
                InvItem item = itemList.get(0);
                
                for(ProProductionStages productionStages : stagesList){
                    proItemProductionStages.setBranchId(tobyUser.getBranchId());
                    proItemProductionStages.setCompanyId(tobyUser.getCompanyId());
                    proItemProductionStages.setCreatedBy(tobyUser);
                    proItemProductionStages.setCreationDate(new Date());
                    proItemProductionStages.setInvItemId(item);
                    proItemProductionStages.setProProductionStagesId(productionStages);
                    dao.updateEntity(proItemProductionStages);
                }
                
            }
        }
    }

}
