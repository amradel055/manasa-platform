/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.PeriodsDTO;
import com.isag.dto.SchoolStagesGuideDTO;
import com.isag.entity.Branch;
import com.isag.entity.IgmaCompany;
import com.isag.entity.IgmaUser;
import com.isag.entity.Materials;
import com.isag.entity.Periods;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq003
 */
@Stateless
public class PeriodsServiceImpl implements PeriodsService {

    @EJB
    private GenericDAO dao;

    @EJB
    private SchoolStagesGuideService schoolStagesGuideService;

    @Override
    public List<PeriodsDTO> findPeriods(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Periods d WHERE d.branchId.id = :branchId And d.teacherId is null ");
        List<Periods> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }

    @Override
    public List<PeriodsDTO> findPeriodsV2(Integer branchId, Integer teacherId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        params.put("teacherId", teacherId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Periods d WHERE d.branchId.id = :branchId And d.teacherId.id = :teacherId ");
        List<Periods> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }

    @Override
    public List<PeriodsDTO> findPeriodsByTeacherId(Integer teacherId) {
        Map<String, Object> params = new HashMap<>();
        params.put("teacherId", teacherId);
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT d FROM Periods d WHERE d.teacherId.id = :teacherId ");
        List<Periods> list = dao.findListByQuery(builder.toString(), params);
//        if(list == null || list.isEmpty()){
//            return findPeriods(branchId);
//        }
        return returnListDTO(list);
    }

    @Override
    public List<PeriodsDTO> findPeriodsByMaterialId(Integer materialId) {
        Map<String, Object> params = new HashMap<>();
        params.put("materialId", materialId);
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT d FROM Periods d WHERE d.materialId.id = :materialId ");
        List<Periods> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }

    public Boolean validatePeriods(PeriodsDTO periodsDTO) {
        Map<String, Object> params = new HashMap();
        params.put("teacherId", periodsDTO.getTeacherId());
        params.put("name", periodsDTO.getName());
        List<Periods> periodses = dao.findListByQuery(" SELECT e FROM Periods e WHERE e.teacherId.id = :teacherId  AND e.name = :name ", params);
        if (periodses != null && !periodses.isEmpty()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public PeriodsDTO findPeriodsById(Integer periodsId) {
        Periods periods = dao.findEntityById(Periods.class, periodsId);
        return mapTODTO(periods);
    }

    @Override
    public void deletePeriods(Integer periodsDTO) {
        Periods entity = dao.findEntityById(Periods.class, periodsDTO);

        dao.deleteEntity(entity);
    }

    @Override
    public void savePeriods(PeriodsDTO periodsDTO, IgmaUser igmaUser) {
        Periods entity = mapToEntity(periodsDTO, igmaUser, null);
        dao.updateEntity(entity);

    }

    @Override
    public PeriodsDTO save(PeriodsDTO periodsDTO, IgmaUser igmaUser) {
        PeriodsDTO msg = new PeriodsDTO();
        Periods periods = null;
        if (periodsDTO.getId() != null) {
            periods = dao.findEntityById(Periods.class, periodsDTO.getId());
        }
        if (periodsDTO.getTeacherId() != null
                && periodsDTO.getBranchId() != null
                && periodsDTO.getCompanyId() != null
                && periodsDTO.getName() != null) {
            if (igmaUser == null) {
                igmaUser = new IgmaUser();
                igmaUser.setId(61);
                Branch branch = new Branch();
                branch.setId(periodsDTO.getBranchId());
                igmaUser.setBranchId(branch);
                IgmaCompany isagCompany = new IgmaCompany();
                isagCompany.setId(periodsDTO.getCompanyId());
                igmaUser.setCompanyId(isagCompany);
            }
            if (validatePeriods(periodsDTO)) {
                periods = mapToEntity(periodsDTO, igmaUser, periods);
                dao.updateEntity(periods);
                msg.setMsg("تم الحفظ بنجاح");
                msg.setMsgType("success");
                return msg;
            } else {
                msg.setMsg(" مستخدم بالفعل");
                msg.setMsgType("fail");
                return msg;
            }
        } else {
            msg.setMsg("لا يوجد بيانات  ");
            msg.setMsgType("fail");
            return msg;
        }
    }

    @Override
    public List<Integer> findPeriodAndChildIds(List<Integer> periodParentIds) {
        List<Integer> periodIds = new ArrayList<>();
        StringBuilder sb = null ;
        if (periodParentIds.isEmpty()) {
            return periodIds;
        }else{
            for(Integer p : periodParentIds){
                if(sb == null){
                    sb =  new StringBuilder();
                    sb.append(p);
                }else{
                    sb.append(" , ").append(p);
                }
            }
        }

        String sql = "SELECT p FROM Periods p WHERE p.id IN ( "+ sb +" )";
//        Map<String, Object> params = Collections.singletonMap("periodParentIds", periodParentIds);

        Map<String, Object> params = new HashMap<>();
//        params.put("periodParentIds", periodParentIds);
//        params.put("periodParentIds", sb.toString());

        List<Periods> result = dao.findListByQuery(sql, params);

        if (result != null && !result.isEmpty()) {
            for (Periods entity : result) {
                periodIds.add(entity.getId());
                if (entity.getChildPeriods() != null && !entity.getChildPeriods().isEmpty()) {
                    for (Periods child : entity.getChildPeriods()) {
                        periodIds.add(child.getId());
                    }
                }
            }
        }
        return periodIds;
    }

    private List<PeriodsDTO> returnListDTO(List<Periods> list) {
        List<PeriodsDTO> dTOList = new ArrayList<>();
        for (Periods entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private PeriodsDTO mapTODTO(Periods entity) {
        PeriodsDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    private PeriodsDTO initMapToDTO(Periods entity) {

        PeriodsDTO dTO = new PeriodsDTO();
        dTO.setId(entity.getId());
        dTO.setName(entity.getName());

        dTO.setAutoTime(entity.getAutoTime());
        dTO.setQuestionNumber(entity.getQuestionNumber());

        dTO.setParent(entity.getParentPeriod() != null ? entity.getParentPeriod().getId() : null);
        dTO.setParentName(entity.getParentPeriod() != null ? entity.getParentPeriod().getName() : null);
        dTO.setMaterialId(entity.getMaterialId() != null ? entity.getMaterialId().getId() : null);

        if (entity.getMaterialId() != null) {
            List<SchoolStagesGuideDTO> schoolStagesGuide = schoolStagesGuideService.findStage(entity.getMaterialId().getId());
            dTO.setSchoolStagesGuideDTO(schoolStagesGuide.get(0));

        }

        dTO.setMaterialName(entity.getMaterialId() != null ? entity.getMaterialId().getName() : null);
        if (entity.getTeacherId() != null) {
            dTO.setTeacherId(entity.getTeacherId() != null ? entity.getTeacherId().getId() : null);
        }
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dTO.setBranchId(entity.getBranchId() != null ? entity.getBranchId().getId() : null);

        return dTO;
    }

    private Periods mapToEntity(PeriodsDTO dTO, IgmaUser igmaUser, Periods entity) {
        if (entity == null) {
            entity = new Periods();
        }

        entity.setId(dTO.getId());
        entity.setName(dTO.getName());

        entity.setAutoTime(dTO.getAutoTime());
        entity.setQuestionNumber(dTO.getQuestionNumber());

        if (dTO.getParent() != null) {
            Periods periods = new Periods();
            periods.setId(dTO.getParent());
            entity.setParentPeriod(periods);
        }

        if (igmaUser.getId() != null) {
            IgmaUser igmaUser1 = new IgmaUser();
            igmaUser1.setId(igmaUser.getId());
        }

        if (dTO.getTeacherId() != null) {
            IgmaUser teacher = new IgmaUser();
            teacher.setId(dTO.getTeacherId());
            entity.setTeacherId(teacher);
        }

        if (dTO.getId() == null) {
            entity.setCreatedBy(igmaUser);
            entity.setCreationDate(new Date());
        } else {
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(igmaUser);
        }
        if (dTO.getMaterialId() != null) {
            Materials materials = new Materials();
            materials.setId(dTO.getMaterialId());
            entity.setMaterialId(materials);

        }

        Branch branch = new Branch();
        branch.setId(dTO.getBranchId());
//        here kartha
        entity.setBranchId(branch);
        entity.setCompanyId(igmaUser.getCompanyId());
        return entity;
    }

}
