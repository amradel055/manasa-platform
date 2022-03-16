package com.toby.businessservice;

import com.toby.core.GenericDAO;
import com.toby.dto.ProProductionStagesDTO;
import com.toby.entity.Branch;
import com.toby.entity.ProProductionStages;
import com.toby.entity.TobyCompany;
import com.toby.entity.TobyUser;
import com.toby.entity.TobyUserProproduction;
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
 * @author H
 */
@Stateless
public class ProProductionStagesServiceImpl implements ProProductionStagesService {

    @EJB
    private GenericDAO dao;
    @EJB
    private ProProductionStagesService proProductionStagesService;

    @Override
    public ProProductionStagesDTO saveProProductionStage(ProProductionStagesDTO proProductionStagesDTO, TobyUser tobyUser) {
        if (proProductionStagesDTO.getName() == null | "".equals(proProductionStagesDTO.getName()) | "".equals(proProductionStagesDTO.getNameEn()) | proProductionStagesDTO.getNameEn() == null | proProductionStagesDTO.getPrice() == null) {
            proProductionStagesDTO.setMsg("من فضلك ادخل القيم");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", proProductionStagesDTO.getMsg()));
            return null;
        } else {
//            if (proProductionStagesService.checkForLastStage(tobyUser) != null && proProductionStagesDTO.getTypeStage() == 0) {
                ProProductionStages proProductionStage = mapToEntity(proProductionStagesDTO, tobyUser);
                proProductionStage = dao.updateEntity(proProductionStage);
                return mapToDTO(proProductionStage);
//            } else {
//                proProductionStagesDTO.setMsg("فيه مرحلة متسجله الاخيرة قبل كده");
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", proProductionStagesDTO.getMsg()));
//                return null;
//            }
        }

    }

    @Override
    public List<ProProductionStagesDTO> getAllProProductionStagesByBranchId(Integer branchId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<ProProductionStages> proProductionStagesList = dao.findListByQuery("SELECT p FROM ProProductionStages p WHERE p.branchId.id = :branchId ORDER BY p.id DESC", params);
        List<ProProductionStagesDTO> proProductionStagesListDTO = mapToDTOList(proProductionStagesList, tobyUser);
        return proProductionStagesListDTO;
    }

    @Override
    public List<ProProductionStages> getAllProProductionStagesByBranchId3(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
//        List<ProProductionStages> proProductionStagesList = dao.findListByQuery("SELECT p FROM ProProductionStages p WHERE p.branchId.id = :branchId AND p.serial in (6) ORDER BY p.id DESC", params);
//        List<ProProductionStages> proProductionStagesList = dao.findListByQuery("SELECT p FROM ProProductionStages p WHERE p.branchId.id = :branchId AND p.serial in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27) ORDER BY p.id DESC", params);
//        List<ProProductionStages> proProductionStagesList = dao.findListByQuery("SELECT p FROM ProProductionStages p WHERE p.branchId.id = :branchId AND p.serial in (1,50) ORDER BY p.id DESC", params); // سروال  & صديري 
//        List<ProProductionStages> proProductionStagesList = dao.findListByQuery("SELECT p FROM ProProductionStages p WHERE p.branchId.id = :branchId AND p.serial in (7) ORDER BY p.id DESC", params); // تطريز توب مغربي
//        List<ProProductionStages> proProductionStagesList = dao.findListByQuery("SELECT p FROM ProProductionStages p WHERE p.branchId.id = :branchId AND p.serial in (5) ORDER BY p.id DESC", params); // تطريز خفيف
//        List<ProProductionStages> proProductionStagesList = dao.findListByQuery("SELECT p FROM ProProductionStages p WHERE p.branchId.id = :branchId AND p.serial in (22) ORDER BY p.id DESC", params); // اللوجو & موديل طقطق وزرار
        List<ProProductionStages> proProductionStagesList = dao.findListByQuery("SELECT p FROM ProProductionStages p WHERE p.branchId.id = :branchId AND p.serial in (1,2,3,4,8,9,11,12,13,14,15,16,17,18,19,20,21,23,24,25,26,27,28) ORDER BY p.id DESC", params); // تعديل
        return proProductionStagesList;
    }

    @Override
    public void deleteProProductionStages(ProProductionStagesDTO proProductionStagesDTO, TobyUser tobyUser) {
        ProProductionStages proProductionStage = mapToEntity(proProductionStagesDTO, tobyUser);
        dao.deleteEntity(proProductionStage);

    }

    @Override
    public List<ProProductionStagesDTO> getAllProproductionByBranchId(TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", tobyUser.getBranchId().getId());
        List<ProProductionStages> proProductionStageseList = dao.findListByQuery("SELECT e FROM ProProductionStages e WHERE e.branchId.id = :branchId ", params);
        return mapToDTOList(proProductionStageseList, tobyUser);
    }

    @Override
    public List<ProProductionStages> getAllProproductionStagesByBranchId(Integer branchId, TobyUser tobyUser) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<ProProductionStages> proProductionStageseList = dao.findListByQuery("SELECT e FROM ProProductionStages e WHERE e.branchId.id = :branchId ", params);
        return proProductionStageseList;
    }

    @Override
    public List<ProProductionStages> getAllProproductionListByUserAndBranch(TobyUser tobyUser, Integer branchId) {

        List<TobyUserProproduction> productionStageses = getAllTobyUserProproductionListByUserAndBranch(tobyUser.getId(), branchId);

        List<ProProductionStages> list = new ArrayList<>();

        for (TobyUserProproduction productionStagese : productionStageses) {
            ProProductionStages productionStagesNew = new ProProductionStages();
            productionStagesNew.setId(productionStagese.getProproductionId().getId());
            productionStagesNew.setName(productionStagese.getProproductionId().getName());
            list.add(productionStagese.getProproductionId() != null ? productionStagese.getProproductionId() : null);
        }

        return list;
    }

    @Override
    public List<ProProductionStagesDTO> getAllProproductionStagesListByUserAndBranch(TobyUser tobyUser, Integer branchId) {

        List<TobyUserProproduction> productionStageses = getAllTobyUserProproductionListByUserAndBranch(tobyUser.getId(), branchId);

        List<ProProductionStages> list = new ArrayList<>();

        for (TobyUserProproduction productionStagese : productionStageses) {
            list.add(productionStagese.getProproductionId() != null ? productionStagese.getProproductionId() : null);
        }

        return mapToDTOList(list, tobyUser);
    }

    @Override
    public List<TobyUserProproduction> getAllTobyUserProproductionListByUserAndBranch(Integer userId, Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        params.put("userId", userId);
        return dao.findListByQuery("SELECT e FROM TobyUserProproduction e WHERE e.branchId.id = :branchId  AND e.userId.id = :userId", params);
    }

    @Override
    public List<ProProductionStagesDTO> getAllProproductionStages(TobyUser tobyUser, Integer branchId) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", branchId);
        List<ProProductionStages> proProductionStageseList = dao.findListByQuery(" SELECT e FROM ProProductionStages e where e.branchId.id = :branchId ", params);
        return mapToDTOList(proProductionStageseList, tobyUser);

    }

    //  mapToEntity
    public ProProductionStages mapToEntity(ProProductionStagesDTO proProductionStagesDTO, TobyUser tobyUser) {
        ProProductionStages proProductionStage = new ProProductionStages();
        proProductionStage.setId(proProductionStagesDTO.getId());
        proProductionStage.setName(proProductionStagesDTO.getName());
        proProductionStage.setNameEn(proProductionStagesDTO.getNameEn());
        proProductionStage.setNameIn(proProductionStagesDTO.getNameIn());
        proProductionStage.setPrice(proProductionStagesDTO.getPrice());
        proProductionStage.setHostName(proProductionStagesDTO.getHostName());
        proProductionStage.setTypeStage(proProductionStagesDTO.getTypeStage());
        if (proProductionStagesDTO.getCreatedBy() != null) {
            TobyUser user = new TobyUser();
            user.setId(proProductionStagesDTO.getCreatedBy());
            proProductionStage.setCreatedBy(user);
            proProductionStage.setCreationDate(proProductionStagesDTO.getCreatedDate());
            proProductionStage.setModifiedBy(tobyUser);
            proProductionStage.setModificationDate(new Date());
            if (proProductionStagesDTO.getCompanyId() != null) {
                TobyCompany company = new TobyCompany();
                company.setId(proProductionStagesDTO.getCompanyId());
                proProductionStage.setCompanyId(company);
            }

            if (proProductionStagesDTO.getBranchId() != null) {
                Branch branch = new Branch();
                branch.setId(proProductionStagesDTO.getBranchId());
                proProductionStage.setBranchId(branch);
            }
        } else {
            proProductionStage.setCreatedBy(tobyUser);
            proProductionStage.setCreationDate(new Date());
            proProductionStage.setCompanyId(tobyUser.getCompanyId());
            proProductionStage.setBranchId(tobyUser.getBranchId());
        }

        return proProductionStage;
    }

    //mapToDTO
    public ProProductionStagesDTO mapToDTO(ProProductionStages proProductionStages) {

        ProProductionStagesDTO proProductionStagesDTO = new ProProductionStagesDTO();
        proProductionStagesDTO.setId(proProductionStages.getId());
        proProductionStagesDTO.setSerial(proProductionStages.getSerial());
        proProductionStagesDTO.setName(proProductionStages.getName());
        proProductionStagesDTO.setNameEn(proProductionStages.getNameEn());
        proProductionStagesDTO.setNameIn(proProductionStages.getNameIn());
        proProductionStagesDTO.setPrice(proProductionStages.getPrice());
        proProductionStagesDTO.setHostName(proProductionStages.getHostName());
        proProductionStagesDTO.setTypeStage(proProductionStages.getTypeStage());
        proProductionStagesDTO.setCreatedBy(proProductionStages.getCreatedBy() != null ? proProductionStages.getCreatedBy().getId() : null);
        proProductionStagesDTO.setCreatedDate(proProductionStages.getCreationDate());
        proProductionStagesDTO.setBranchId(proProductionStages.getBranchId() != null ? proProductionStages.getBranchId().getId() : null);
        proProductionStagesDTO.setCompanyId(proProductionStages.getCompanyId() != null ? proProductionStages.getCompanyId().getId() : null);

        return proProductionStagesDTO;
    }

    public List<ProProductionStagesDTO> mapToDTOList(List<ProProductionStages> proProductionStagesList, TobyUser tobyUser) {
        List<ProProductionStagesDTO> proProductionStagesDTOList = new ArrayList<>();
        for (ProProductionStages proProductionStage : proProductionStagesList) {
            proProductionStagesDTOList.add(mapToDTO(proProductionStage));
        }
        return proProductionStagesDTOList;
    }

    @Override
    public void deleteProProductionStage(Integer proProductionStageId) {
        dao.executeDeleteQuery("delete from ProProductionStages p where p.id= " + proProductionStageId);
    }

    @Override
    public ProProductionStagesDTO findProProductionStages(Integer id) {
        ProProductionStages proProductionStages = dao.findEntityById(ProProductionStages.class, id);

        return mapToDTO(proProductionStages);
    }

    @Override
    public ProProductionStages checkForLastStage(TobyUser tobyUser) {
        return dao.findEntityByQuery("SELECT p from ProProductionStages p where p.typeStage=1");
    }

    @Override
    public ProProductionStages getCheckStage(TobyUser tobyUser) {
        Map<String, Object> params = new HashMap();
        params.put("branchId", tobyUser.getBranchId().getId());
        return dao.findEntityByQuery("Select p from ProProductionStages p where p.typeStage=1 and p.branchId.id=:branchId", params);

    }

}
