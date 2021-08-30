/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.PeriodsDTO;
import com.isag.entity.Branch;
import com.isag.entity.IgmaCompany;
import com.isag.entity.IgmaUser;
import com.isag.entity.Periods;
import com.isag.entity.Teachers;
import java.util.ArrayList;
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

    @Override
    public List<PeriodsDTO> findPeriods(Integer branchId) {
        Map<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Periods d WHERE d.branchId.id = :branchId  ");
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
    public void deletePeriods(PeriodsDTO periodsDTO) {
        Periods entity = new Periods();
        entity.setId(periodsDTO.getId());
        dao.deleteEntity(entity);
    }

    @Override
    public void deletePeriods(Integer periodsDTO) {
        Periods entity = new Periods();
        entity.setId(periodsDTO);
        dao.deleteEntity(entity);
    }

    @Override
    public PeriodsDTO save(PeriodsDTO periodsDTO, IgmaUser isagUser) {
        PeriodsDTO msg = new PeriodsDTO();
        Periods periods = null;
        if (periodsDTO.getId() != null) {
            periods = dao.findEntityById(Periods.class, periodsDTO.getId());
        }
        if (periodsDTO.getTeacherId() != null
                && periodsDTO.getBranchId() != null
                && periodsDTO.getCompanyId() != null
                && periodsDTO.getName() != null) {
            if (isagUser == null) {
                isagUser = new IgmaUser();
                isagUser.setId(61);
                Branch branch = new Branch();
                branch.setId(periodsDTO.getBranchId());
                isagUser.setBranchId(branch);
                IgmaCompany isagCompany = new IgmaCompany();
                isagCompany.setId(periodsDTO.getCompanyId());
                isagUser.setCompanyId(isagCompany);
            }
            if (validatePeriods(periodsDTO)) {
                periods = mapToEntity(periodsDTO, isagUser, periods);
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

        if (entity.getTeacherId() != null) {
            dTO.setTeacherId(entity.getTeacherId().getId());
        }
        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private Periods mapToEntity(PeriodsDTO dTO, IgmaUser isagUser, Periods entity) {
        if (entity == null) {
            entity = new Periods();
        }

        entity.setId(dTO.getId());
        entity.setName(dTO.getName());

        if (dTO.getTeacherId() != null) {
            IgmaUser igmaUser = new IgmaUser();
            igmaUser.setId(dTO.getTeacherId());
            entity.setTeacherId(igmaUser);
        }

        if (dTO.getId() == null) {

            entity.setCreatedBy(isagUser);
            entity.setBranchId(isagUser.getBranchId());
            entity.setCreationDate(new Date());
        } else {
            entity.setId(dTO.getId());
            entity.setModificationDate(new Date());
            entity.setModifiedBy(isagUser);
        }

        entity.setBranchId(isagUser.getBranchId());
        entity.setCompanyId(isagUser.getCompanyId());
        return entity;
    }

}
