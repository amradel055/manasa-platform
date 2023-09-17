/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
//import com.isag.dto.LessonDTO;
import com.isag.dto.LessonVideosDTO;
import com.isag.entity.Branch;
import com.isag.entity.IgmaCompany;
import com.isag.entity.Lesson;
import com.isag.entity.IgmaUser;
import com.isag.entity.LessonVideos;
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
public class LessonVideosServiceImpl implements LessonVideosService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<LessonVideosDTO> findlessinList(Integer lessonId) {
        Map<String, Object> params = new HashMap<>();
        params.put("lessonId", lessonId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM LessonVideos d WHERE d.lessonId.id = :lessonId ");
        List<LessonVideos> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }

    @Override
    public List<String> findlessonList(Integer lessonId, Integer type) {
        Map<String, Object> params = new HashMap<>();
        List<String> listString = null;
        params.put("lessonId", lessonId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM LessonVideos d WHERE d.lessonId.id = :lessonId ");
        List<LessonVideos> list = dao.findListByQuery(builder.toString(), params);
        if (list != null && !list.isEmpty()) {
            listString = new ArrayList<>();
            for (LessonVideos lessonVideos : list) {
                if (null != type) {
                    switch (type) {
                        case 0:
                            if (( lessonVideos.getVideoLink() != null && !lessonVideos.getVideoLink().isEmpty())) {
                                listString.add(lessonVideos.getVideoLink());
                            }
                            break;
                        case 1:
                            if ((lessonVideos.getVimeoLink() != null && !lessonVideos.getVimeoLink().isEmpty())) {
                                listString.add(lessonVideos.getVimeoLink());
                            }
                            break;
                        case 2:
                            if ((lessonVideos.getYoutubeLink() != null && !lessonVideos.getYoutubeLink().isEmpty())) {
                                listString.add(lessonVideos.getYoutubeLink());
                            }
                            break;
                        default:
                            break;
                    }
                }

            }

        }
        return listString;
    }

    @Override
    public void deleteLesson(Integer lessonId) {
        String sql = "delete from LessonVideos d where d.id > 0 AND d.lessonId.id = " + lessonId;
        dao.executeDeleteQuery(sql);
    }

    @Override
    public void deleteVideos(String voice) {
        String sql = "delete from LessonVideos d where d.id > 0 AND d.voice = '" + voice + "'";
        dao.executeDeleteQuery(sql);
    }

    @Override
    public void deleteLessonVideos(Integer id) {
        LessonVideos lessonVideos = new LessonVideos();
        lessonVideos.setId(id);
        dao.deleteEntity(lessonVideos);
    }

//    @Override
//    public List<LessonVideosDTO> save(List<LessonVideosDTO> lessonVideosDTOList, IgmaUser isagUser, Integer lessonId) {
//        List<LessonVideosDTO> list = new ArrayList<>();
//        deleteLesson(lessonId);
//        for (LessonVideosDTO lessonVideosDTO : lessonVideosDTOList) {
//            list.add(save(lessonVideosDTO, isagUser));
//        }
//        return list;
//    }
    @Override
    public LessonVideosDTO saveList(List<String> LessonVideosDTOist, Integer lessonId, Integer branchId, Integer companyId, Integer type) {
        LessonVideosDTO msg = new LessonVideosDTO();
        if (LessonVideosDTOist != null && !LessonVideosDTOist.isEmpty()) {
            for (String lessonVideos : LessonVideosDTOist) {
                if (lessonVideos != null && lessonId != null && branchId != null && null != companyId) {
                    LessonVideos enity = new LessonVideos();
                    Branch branch = new Branch();
                    branch.setId(branchId);
                    enity.setBranchId(branch);

                    IgmaUser isagUser = new IgmaUser();
                    isagUser.setId(61);
                    enity.setCreatedBy(isagUser);

                    IgmaCompany isagCompany = new IgmaCompany();
                    isagCompany.setId(companyId);
                    enity.setCompanyId(isagCompany);

                    if (null != type) {
                        switch (type) {
                            case 0:
                                enity.setVideoLink(lessonVideos);
                                break;
                            case 1:
                                enity.setVimeoLink(lessonVideos);
                                break;
                            case 2:
                                enity.setYoutubeLink(lessonVideos);
                                break;
                            default:
                                break;
                        }
                    }

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

//    public LessonVideosDTO save(LessonVideosDTO lessonVideosDTO, IgmaUser isagUser) {
//        LessonVideosDTO msg = new LessonVideosDTO();
//        LessonVideos lessonVideos = null;
//        
//        if (lessonVideosDTO != null) {
//            if (isagUser == null) {
//                isagUser = new IgmaUser();
//                isagUser.setId(61);
//                Branch branch = new Branch();
//                branch.setId(lessonVideosDTO.getBranchId());
//                isagUser.setBranchId(branch);
//                IgmaCompany isagCompany = new IgmaCompany();
//                isagCompany.setId(lessonVideosDTO.getCompanyId());
//                isagUser.setCompanyId(isagCompany);
//            }
//            lessonVideos = mapToEntity(lessonVideosDTO, isagUser, lessonVideos);
//            lessonVideos = dao.updateEntity(lessonVideos);
//            lessonVideosDTO = mapTODTO(lessonVideos);
//            msg.setMsg("تم الحفظ بنجاح");
//            msg.setMsgType("success");
//            return lessonVideosDTO;
//        } else {
//            msg.setMsg("لا يوجد بيانات  ");
//            msg.setMsgType("fail");
//            return msg;
//        }
//    }
    private List<LessonVideosDTO> returnListDTO(List<LessonVideos> list) {
        List<LessonVideosDTO> dTOList = new ArrayList<>();
        for (LessonVideos entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private LessonVideosDTO mapTODTO(LessonVideos entity) {
        LessonVideosDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    private LessonVideosDTO initMapToDTO(LessonVideos entity) {

        LessonVideosDTO dTO = new LessonVideosDTO();

        dTO.setId(entity.getId());
        dTO.setVideoLink(entity.getVideoLink());
        dTO.setVimeoLink(entity.getVimeoLink());
        dTO.setYoutubeLink(entity.getYoutubeLink());
        if (entity.getLessonId() != null) {
            dTO.setLessonId(entity.getLessonId().getId());
        }

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());
        dTO.setCompanyId(entity.getCompanyId().getId());

        return dTO;
    }

//    private LessonVideos mapToEntity(LessonVideosDTO dTO, IgmaUser isagUser, LessonVideos entity) {
//        if (entity == null) {
//            entity = new LessonVideos();
//        }
//        entity.setVideoLink(dTO.getVideoLink());
//                    entity.setVimeoLink(dTO.getVimeoLink());
//                    entity.setYoutubeLink(dTO.getYoutubeLink());
//        if (dTO.getLessonId() != null) {
//            Lesson lesson = new Lesson();
//            lesson.setId(dTO.getLessonId());
//            entity.setLessonId(lesson);
//        }
//
//        if (dTO.getId() == null) {
//
//            entity.setCreatedBy(isagUser);
//            entity.setBranchId(isagUser.getBranchId());
//            entity.setCreationDate(new Date());
//        } else {
//            entity.setId(dTO.getId());
//            entity.setModificationDate(new Date());
//            entity.setModifiedBy(isagUser);
//        }
//
//        entity.setBranchId(isagUser.getBranchId());
//        entity.setCompanyId(isagUser.getCompanyId());
//        return entity;
//    }
}
