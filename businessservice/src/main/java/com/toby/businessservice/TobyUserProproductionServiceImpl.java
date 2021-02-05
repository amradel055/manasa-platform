package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.ProProductionStagesDTO;
import com.toby.dto.TobyUserDTO;
import com.toby.dto.TobyUserProproductionDTO;
import com.toby.entity.Branch;
import com.toby.entity.ProProductionStages;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import com.toby.entity.TobyUserProproduction;
import com.toby.entity.TobyUserRole;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class TobyUserProproductionServiceImpl implements TobyUserProproductionService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<TobyUserDTO> getUsersWithProductionStagesForBranch(Integer selectedBranchId) {
        List<TobyUserDTO> tobyUserDTOList = new ArrayList<>();
        Map<String, Object> params = new HashMap();
        Map<Integer, TobyUserDTO> userStagesMap = new HashMap();
        params.put("id", selectedBranchId);
        String sql = "SELECT t FROM TobyUserProproduction t where t.branchId.id = :id";
        List<TobyUserProproduction> tobyUserProproductionList = dao.findListByQuery(sql, params);
        TobyUserProproductionDTO tobyUserProproductionDTO;
        TobyUserDTO tobyUserDTO;
        for (TobyUserProproduction tobyUserProproduction : tobyUserProproductionList) {
            if (userStagesMap.containsKey(tobyUserProproduction.getUserId().getId())) {
                tobyUserDTO = userStagesMap.get(tobyUserProproduction.getUserId().getId());
            } else {
                tobyUserDTO = new TobyUserDTO();
                tobyUserDTO.setName(tobyUserProproduction.getUserId().getName());
                tobyUserDTO.setUserCode(tobyUserProproduction.getUserId().getUserCode());
            }
            tobyUserProproductionDTO = new TobyUserProproductionDTO();
            tobyUserProproductionDTO.setUserId(tobyUserProproduction.getUserId().getId());
            tobyUserProproductionDTO.setProproductionId(tobyUserProproduction.getProproductionId().getId());
            tobyUserProproductionDTO.setProproductionName(tobyUserProproduction.getProproductionId().getName());
            tobyUserProproductionDTO.setProproductionPrice(tobyUserProproduction.getProproductionId().getPrice());
            tobyUserProproductionDTO.setId(tobyUserProproduction.getId());
            tobyUserDTO.getTobyUserProproductionDTOList().add(tobyUserProproductionDTO);
            userStagesMap.put(tobyUserProproduction.getUserId().getId(), tobyUserDTO);
        }
        tobyUserDTOList = new ArrayList<>(userStagesMap.values());
        return tobyUserDTOList;
    }

    @Override
    public void deleteProductionStages(TobyUserRole userRole, ProProductionStagesDTO productionStages) {
        String sql = "delete from TobyUserProproduction e  where e.branchId.id  = " + userRole.getBranchId().getId()
                + " AND e.userId.id = " + userRole.getUserId().getId() + " AND e.proproductionId.id = " + productionStages.getId();
        dao.executeDeleteQuery(sql);
    }

    @Override
    public void deleteProductionStages(TobyUserRole userRole, ProProductionStages productionStages) {
        String sql = "delete from TobyUserProproduction e  where e.branchId.id  = " + userRole.getBranchId().getId()
                + " AND e.userId.id = " + userRole.getUserId().getId() + " AND e.proproductionId.id = " + productionStages.getId();
        dao.executeDeleteQuery(sql);
    }

    @Override
    public TobyUserProproduction updateTobyUserProproduction(TobyUserProproduction tobyUserProproduction, TobyUser tobyUser) {
        return dao.updateEntity(tobyUserProproduction);
    }

    public TobyUserProproduction mapToEntity(TobyUserProproductionDTO tobyUserProproductionDTO, TobyUser tobyUser) {
        TobyUserProproduction tobyUserProproduction = new TobyUserProproduction();
        tobyUserProproduction.setId(tobyUserProproductionDTO.getId());
        if (tobyUserProproductionDTO.getProproductionId() != null) {
            ProProductionStages proProductionStage = new ProProductionStages();
            proProductionStage.setId(tobyUserProproductionDTO.getProproductionId());
            tobyUserProproduction.setProproductionId(proProductionStage);
        }
        if (tobyUserProproductionDTO.getUserId() != null) {
            TobyUser user = new TobyUser();
            user.setId(tobyUserProproductionDTO.getUserId());
            tobyUserProproduction.setUserId(user);
        }
        if (tobyUserProproductionDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(tobyUserProproductionDTO.getCreatedBy());
            tobyUserProproduction.setCreatedBy(user);
            tobyUserProproduction.setCreationDate(tobyUserProproductionDTO.getCreatedDate());
            tobyUserProproduction.setModifiedBy(tobyUser);
            tobyUserProproduction.setModificationDate(new Date());
            if (tobyUserProproductionDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(tobyUserProproductionDTO.getCompanyId());
                tobyUserProproduction.setCompanyId(company);
            }

            if (tobyUserProproductionDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(tobyUserProproductionDTO.getBranchId());
                tobyUserProproduction.setBranchId(branch);
            }
        } else {
            tobyUserProproduction.setCreatedBy(tobyUser);
            tobyUserProproduction.setCreationDate(new Date());
            tobyUserProproduction.setCompanyId(tobyUser.getCompanyId());
            tobyUserProproduction.setBranchId(tobyUser.getBranchId());
        }
        return tobyUserProproduction;
    }

    public TobyUserProproductionDTO mapToDTO(TobyUserProproduction tobyUserProproduction) {
        TobyUserProproductionDTO tobyUserProproductionDTO = new TobyUserProproductionDTO();
        tobyUserProproductionDTO.setId(tobyUserProproduction.getId());
        tobyUserProproductionDTO.setProproductionId(tobyUserProproduction.getProproductionId() != null ? tobyUserProproduction.getProproductionId().getId() : null);
        tobyUserProproductionDTO.setUserId(tobyUserProproduction.getUserId() != null ? tobyUserProproduction.getUserId().getId() : null);
        tobyUserProproductionDTO.setCreatedBy(tobyUserProproduction.getCreatedBy() != null ? tobyUserProproduction.getCreatedBy().getId() : null);
        tobyUserProproductionDTO.setCreatedDate(tobyUserProproduction.getCreationDate());
        tobyUserProproductionDTO.setBranchId(tobyUserProproduction.getBranchId().getId() != null ? tobyUserProproduction.getBranchId().getId() : null);
        tobyUserProproductionDTO.setCompanyId(tobyUserProproduction.getCompanyId() != null ? tobyUserProproduction.getCompanyId().getId() : null);

        return tobyUserProproductionDTO;
    }

    public List<TobyUserProproductionDTO> mapToDTOList(List<TobyUserProproduction> tobyUserProproductions) {

        List<TobyUserProproductionDTO> tobyUserProproductionDTOs = new ArrayList<>();
        for (TobyUserProproduction tobyUserProproduction : tobyUserProproductions) {
            tobyUserProproductionDTOs.add(mapToDTO(tobyUserProproduction));
        }
        return tobyUserProproductionDTOs;
    }

}
