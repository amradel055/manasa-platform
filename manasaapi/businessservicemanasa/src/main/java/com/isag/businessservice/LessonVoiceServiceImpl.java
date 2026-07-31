/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.LessonDTO;
import com.isag.dto.LessonVoiceDTO;
import com.isag.entity.Branch;
import com.isag.entity.IgmaCompany;
import com.isag.entity.Lesson;
import com.isag.entity.IgmaUser;
import com.isag.entity.LessonVoice;
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
public class LessonVoiceServiceImpl implements LessonVoiceService {
    
    @EJB
    private GenericDAO dao;
    
    @Override
    public List<String> findAllLessons(Integer lessonId) {
        Map<String, Object> params = new HashMap<>();
        params.put("lessonId", lessonId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM LessonVoice d WHERE d.lessonId.id = :lessonId ");
        List<LessonVoice> list = dao.findListByQuery(builder.toString(), params);
        List<String> nameList = new ArrayList<>();
        for (LessonVoice name : list) {
            nameList.add(name.getVoice());
        }
        return nameList;
    }
    
    @Override
    public List<LessonVoiceDTO> findlessinList(Integer lessonId) {
        Map<String, Object> params = new HashMap<>();
        params.put("lessonId", lessonId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM LessonVoice d WHERE d.lessonId.id = :lessonId ");
        List<LessonVoice> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }
    
    @Override
    public void deleteLesson(Integer lessonId) {
        String sql = "delete from LessonVoice d where d.id > 0 AND d.lessonId.id = " + lessonId;
        dao.executeDeleteQuery(sql);
    }
    
    @Override
    public void deleteVoice(String voice) {
        String sql = "delete from LessonVoice d where d.id > 0 AND d.voice = '" + voice + "'";
        dao.executeDeleteQuery(sql);
    }
    
    @Override
    public void deleteLessonVoice(Integer id) {
        LessonVoice lessonVoice = new LessonVoice();
        lessonVoice.setId(id);
        dao.deleteEntity(lessonVoice);
    }
    
    @Override
    public List<LessonVoiceDTO> save(List<LessonVoiceDTO> lessonVoiceDTOList, IgmaUser isagUser, Integer lessonId) {
        List<LessonVoiceDTO> list = new ArrayList<>();
        deleteLesson(lessonId);
        for (LessonVoiceDTO lessonVoiceDTO : lessonVoiceDTOList) {
            list.add(save(lessonVoiceDTO, isagUser));
        }
        return list;
    }
    
    @Override
    public LessonVoiceDTO saveList(List<LessonVoiceDTO> LessonVoiceDTOist, Integer lessonId, Integer branchId, Integer companyId) {
        LessonVoiceDTO msg = new LessonVoiceDTO();
        if (LessonVoiceDTOist != null && !LessonVoiceDTOist.isEmpty()) {
            for (LessonVoiceDTO lessonVoice : LessonVoiceDTOist) {
                if (lessonVoice != null && lessonId != null && branchId != null && companyId != null
                        && lessonVoice != null) {
                    LessonVoice enity = new LessonVoice();
                    Branch branch = new Branch();
                    branch.setId(branchId);
                    enity.setBranchId(branch);
                    
                    IgmaUser isagUser = new IgmaUser();
                    isagUser.setId(61);
                    enity.setCreatedBy(isagUser);
                    
                    IgmaCompany isagCompany = new IgmaCompany();
                    isagCompany.setId(companyId);
                    enity.setCompanyId(isagCompany);
                    
                    enity.setVoice(lessonVoice.getVoice());
                    Lesson lesson = new Lesson();
                    lesson.setId(lessonId);
                    enity.setLessonId(lesson);
                    
                    enity.setCreationDate(new Date());
                    
                    dao.updateEntity(enity);
                } else {
                    msg.setMsg("لا يوجد بيانات  ");
                }
            }
        }
        return msg;
    }
    
    @Override
    public LessonVoiceDTO saveListV2(List<String> LessonVoiceDTOist, Integer lessonId, Integer branchId, Integer companyId) {
        LessonVoiceDTO msg = new LessonVoiceDTO();
        if (LessonVoiceDTOist != null && !LessonVoiceDTOist.isEmpty()) {
            for (String lessonVoice : LessonVoiceDTOist) {
                if (lessonVoice != null && lessonId != null && branchId != null && companyId != null) {
                    LessonVoice enity = new LessonVoice();
                    Branch branch = new Branch();
                    branch.setId(branchId);
                    enity.setBranchId(branch);
                    
                    IgmaUser isagUser = new IgmaUser();
                    isagUser.setId(61);
                    enity.setCreatedBy(isagUser);
                    
                    IgmaCompany isagCompany = new IgmaCompany();
                    isagCompany.setId(companyId);
                    enity.setCompanyId(isagCompany);
                    
                    enity.setVoice(lessonVoice);
                    Lesson lesson = new Lesson();
                    lesson.setId(lessonId);
                    enity.setLessonId(lesson);
                    
                    enity.setCreationDate(new Date());
                    
                    dao.updateEntity(enity);
                } else {
                    msg.setMsg("لا يوجد بيانات  ");
                }
            }
        }
        return msg;
    }
    
    public LessonVoiceDTO save(LessonVoiceDTO lessonVoiceDTO, IgmaUser isagUser) {
        LessonVoiceDTO msg = new LessonVoiceDTO();
        LessonVoice lessonVoice = null;
        
        if (lessonVoiceDTO != null) {
            if (isagUser == null) {
                isagUser = new IgmaUser();
                isagUser.setId(61);
                Branch branch = new Branch();
                branch.setId(lessonVoiceDTO.getBranchId());
                isagUser.setBranchId(branch);
                IgmaCompany isagCompany = new IgmaCompany();
                isagCompany.setId(lessonVoiceDTO.getCompanyId());
                isagUser.setCompanyId(isagCompany);
            }
            lessonVoice = mapToEntity(lessonVoiceDTO, isagUser, lessonVoice);
            lessonVoice = dao.updateEntity(lessonVoice);
            lessonVoiceDTO = mapTODTO(lessonVoice);
            msg.setMsg("تم الحفظ بنجاح");
            msg.setMsgType("success");
            return lessonVoiceDTO;
        } else {
            msg.setMsg("لا يوجد بيانات  ");
            msg.setMsgType("fail");
            return msg;
        }
    }
    
    public void updateimage(LessonVoiceDTO lessonVoiceDTO, IgmaUser isagUser) {
        
        if (lessonVoiceDTO != null) {
            LessonVoice lessonVoice = mapToEntity(lessonVoiceDTO, isagUser , null);
            LessonVoice updateEntity = dao.updateEntity(lessonVoice);
        }
    }
    


private List<LessonVoiceDTO> returnListDTO(List<LessonVoice> list) {
        List<LessonVoiceDTO> dTOList = new ArrayList<>();
        for (LessonVoice entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private LessonVoiceDTO mapTODTO(LessonVoice entity) {
        LessonVoiceDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    private LessonVoiceDTO initMapToDTO(LessonVoice entity) {

        LessonVoiceDTO dTO = new LessonVoiceDTO();

        dTO.setId(entity.getId());
        dTO.setVoice(entity.getVoice());
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

    private LessonVoice mapToEntity(LessonVoiceDTO dTO, IgmaUser isagUser, LessonVoice entity) {
        if (entity == null) {
            entity = new LessonVoice();
        }
        entity.setVoice(dTO.getVoice());
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
