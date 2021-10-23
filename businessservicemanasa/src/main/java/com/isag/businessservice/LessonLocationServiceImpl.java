/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.LessonLocationDTO;
import com.isag.entity.Branch;
import com.isag.entity.IgmaCompany;
import com.isag.entity.LessonLocation;
import com.isag.entity.IgmaUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author hq003
 */
@Stateless
public class LessonLocationServiceImpl implements LessonLocationService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<LessonLocationDTO> findAllLessonLocation(Integer lessonId) {
        List<LessonLocation> list = dao.findListByQuery("SELECT d FROM LessonLocationDTO d  ");
        return returnListDTO(list);
    }

    @Override
    public void deleteLessonLocation(Integer lessonId) {
        String sql = "delete from LessonLocation d where d.id > 0 AND d.lessonId.id = " + lessonId;
        dao.executeDeleteQuery(sql);
    }

    @Override
    public List<LessonLocationDTO> save(List<LessonLocationDTO> lessonImgDTOList, IgmaUser isagUser , Integer lessonId) {
        List<LessonLocationDTO> list = new ArrayList<>();
        for (LessonLocationDTO lessonImgDTO : lessonImgDTOList) {
            list.add(save(lessonImgDTO, isagUser));
        }
        return list;
    }

    public LessonLocationDTO save(LessonLocationDTO lessonImgDTO, IgmaUser isagUser) {
        LessonLocationDTO msg = new LessonLocationDTO();
        LessonLocation lessonImg = null;

        if (lessonImgDTO != null) {
            if (isagUser == null) {
                isagUser = new IgmaUser();
                isagUser.setId(61);
                Branch branch = new Branch();
                branch.setId(lessonImgDTO.getBranchId());
                isagUser.setBranchId(branch);
                IgmaCompany isagCompany = new IgmaCompany();
                isagCompany.setId(lessonImgDTO.getCompanyId());
                isagUser.setCompanyId(isagCompany);
            }
            lessonImg = mapToEntity(lessonImgDTO, isagUser, lessonImg);
            lessonImg = dao.updateEntity(lessonImg);
            lessonImgDTO = mapTODTO(lessonImg);
            msg.setMsg("تم الحفظ بنجاح");
            msg.setMsgType("success");
            return lessonImgDTO;
        } else {
            msg.setMsg("لا يوجد بيانات  ");
            msg.setMsgType("fail");
            return msg;
        }
    }

    private List<LessonLocationDTO> returnListDTO(List<LessonLocation> list) {
        List<LessonLocationDTO> dTOList = new ArrayList<>();
        for (LessonLocation entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private LessonLocationDTO mapTODTO(LessonLocation entity) {
        LessonLocationDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    private LessonLocationDTO initMapToDTO(LessonLocation entity) {

        LessonLocationDTO dTO = new LessonLocationDTO();

        dTO.setId(entity.getId());
        dTO.setName(entity.getName());

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());
        dTO.setCompanyId(entity.getCompanyId().getId());

        return dTO;
    }

    private LessonLocation mapToEntity(LessonLocationDTO dTO, IgmaUser isagUser, LessonLocation entity) {
        if (entity == null) {
            entity = new LessonLocation();
        }
        entity.setName(dTO.getName());

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
