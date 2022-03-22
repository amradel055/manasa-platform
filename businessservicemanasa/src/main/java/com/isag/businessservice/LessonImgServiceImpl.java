/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.LessonDTO;
import com.isag.dto.LessonImgDTO;
import com.isag.entity.Branch;
import com.isag.entity.IgmaCompany;
import com.isag.entity.Lesson;
import com.isag.entity.IgmaUser;
import com.isag.entity.LessonImg;
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
public class LessonImgServiceImpl implements LessonImgService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<String> findAllLessons(Integer lessonId) {
        Map<String, Object> params = new HashMap<>();
        params.put("lessonId", lessonId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM LessonImg d WHERE d.lessonId.id = :lessonId ");
        List<LessonImg> list = dao.findListByQuery(builder.toString(), params);
        List<String> nameList = new  ArrayList<>();
        for(LessonImg name  : list){
            nameList.add(name.getImg());
        }
        return nameList;
    }

    @Override
    public void deleteLesson(Integer lessonId) {
        String sql = "delete from LessonImg d where d.id > 0 AND d.lessonId.id = " + lessonId;
        dao.executeDeleteQuery(sql);
    }
    
    

    @Override
    public List<LessonImgDTO> save(List<LessonImgDTO> lessonImgDTOList, IgmaUser isagUser , Integer lessonId) {
        List<LessonImgDTO> list = new ArrayList<>();
        deleteLesson(lessonId);
        for (LessonImgDTO lessonImgDTO : lessonImgDTOList) {
            list.add(save(lessonImgDTO, isagUser));
        }
        return list;
    }

    @Override
    public LessonImgDTO saveList(List<String> LessonImgDTOist, Integer lessonId, Integer branchId, Integer companyId) {
        LessonImgDTO msg = new LessonImgDTO();
        if(LessonImgDTOist != null && !LessonImgDTOist.isEmpty()){
        for (String lessonImg : LessonImgDTOist) {
            if (lessonImg != null && lessonId != null && branchId != null && companyId != null
                    && lessonImg != null) {
                LessonImg enity = new LessonImg();
                Branch branch = new Branch();
                branch.setId(branchId);
                enity.setBranchId(branch);

                IgmaUser isagUser = new IgmaUser();
                isagUser.setId(61);
                enity.setCreatedBy(isagUser);

                IgmaCompany isagCompany = new IgmaCompany();
                isagCompany.setId(companyId);
                enity.setCompanyId(isagCompany);
                
                enity.setImg(lessonImg);
                Lesson lesson = new  Lesson();
                lesson.setId(lessonId);
                enity.setLessonId(lesson);
                
                enity.setCreationDate(new Date());

                dao.updateEntity(enity);
            } else {
                msg.setMsg("لا يوجد بيانات  ");
            }
        } }
        return msg;
    }

    public LessonImgDTO save(LessonImgDTO lessonImgDTO, IgmaUser isagUser) {
        LessonImgDTO msg = new LessonImgDTO();
        LessonImg lessonImg = null;

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

    private List<LessonImgDTO> returnListDTO(List<LessonImg> list) {
        List<LessonImgDTO> dTOList = new ArrayList<>();
        for (LessonImg entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private LessonImgDTO mapTODTO(LessonImg entity) {
        LessonImgDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    private LessonImgDTO initMapToDTO(LessonImg entity) {

        LessonImgDTO dTO = new LessonImgDTO();

        dTO.setId(entity.getId());
        dTO.setImg(entity.getImg());
        if (entity.getLessonId() != null) {
            LessonDTO lessonDTO = new LessonDTO();
            lessonDTO.setId(entity.getLessonId().getId());
            dTO.setLessonId(lessonDTO);
        }

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());
        dTO.setCompanyId(entity.getCompanyId().getId());

        return dTO;
    }

    private LessonImg mapToEntity(LessonImgDTO dTO, IgmaUser isagUser, LessonImg entity) {
        if (entity == null) {
            entity = new LessonImg();
        }
        entity.setImg(dTO.getImg());
        if (dTO.getLessonId() != null) {
            Lesson lesson = new Lesson();
            lesson.setId(dTO.getLessonId().getId());
            entity.setLessonId(lesson);
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
