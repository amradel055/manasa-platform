/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.CommentsDTO;
import com.isag.entity.Branch;
import com.isag.entity.IsagCompany;
import com.isag.entity.Comments;
import com.isag.entity.IsagUser;
import com.isag.entity.Lesson;
import com.isag.entity.Student;
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
public class CommentsServiceImpl implements CommentsService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<CommentsDTO> findCommentsByLessonId(Integer lessonId) {
        Map<String, Object> params = new HashMap<>();
        params.put("lessonId", lessonId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Comments d WHERE d.lessonId.id = :lessonId order by d.id asc ");
        List<Comments> list = dao.findListByQuery(builder.toString(), params);
        return returnListDTO(list);
    }

    @Override
    public CommentsDTO save(CommentsDTO commentsDTO, IsagUser isagUser) {
        CommentsDTO msg = new CommentsDTO();
        Comments comments = null;
        if (commentsDTO.getId() != null) {
            comments = dao.findEntityById(Comments.class, commentsDTO.getId());
        }
        if (commentsDTO != null && commentsDTO.getLessonId() != null) {
            if (isagUser == null) {
                isagUser = new IsagUser();
                isagUser.setId(61);
                Branch branch = new Branch();
                branch.setId(commentsDTO.getBranchId());
                isagUser.setBranchId(branch);
                IsagCompany isagCompany = new IsagCompany();
                isagCompany.setId(commentsDTO.getCompanyId());
                isagUser.setCompanyId(isagCompany);
            }
            comments = mapToEntity(commentsDTO, isagUser, comments);
            dao.updateEntity(comments);
            msg.setMsg("تم الحفظ بنجاح");
            msg.setMsgType("success");
            return msg;
        } else {
            msg.setMsg("لا يوجد بيانات  ");
            msg.setMsgType("fail");
            return msg;
        }
    }

    @Override
    public void deleteComments(CommentsDTO commentsDTO) {
        Comments entity = new Comments();
        entity.setId(commentsDTO.getId());
        dao.deleteEntity(entity);
    }

    @Override
    public void deleteComments(Integer commentId) {
        Comments entity = new Comments();
        entity.setId(commentId);
        dao.deleteEntity(entity);
    }

    @Override
    public void deleteCommentsByLesson(Integer lessonId) {

        Map<String, Object> params = new HashMap<>();
        params.put("lessonId", lessonId);
        String sql1 = "update Comments c  set c.parent = null where c.id > 0 AND c.lessonId.id = :lessonId";
        dao.executeUpdateQuery(sql1,params);
        String sql = "delete from Comments d where d.id > 0 AND d.lessonId.id = " + lessonId;
        dao.executeDeleteQuery(sql);
    }

    private List<CommentsDTO> returnListDTO(List<Comments> list) {
        List<CommentsDTO> dTOList = new ArrayList<>();
        for (Comments entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private CommentsDTO mapTODTO(Comments entity) {
        CommentsDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    private CommentsDTO initMapToDTO(Comments entity) {

        CommentsDTO dTO = new CommentsDTO();
        dTO.setId(entity.getId());
        dTO.setComment(entity.getComment());

        if (entity.getLessonId() != null) {
            dTO.setLessonId(entity.getLessonId() == null ? null : entity.getLessonId().getId());
        }
        if (entity.getParent() != null) {
            dTO.setParent(entity.getParent().getId());
            dTO.setParentName(entity.getParent().getStudentId() == null ? null : entity.getParent().getStudentId().getFullName());
        }
        if (entity.getStudentId() != null) {
            dTO.setStudentId(entity.getStudentId().getId());
            dTO.setStudentName(entity.getStudentId() == null ? null : entity.getStudentId().getFullName());
            dTO.setProfilePicture(entity.getStudentId() == null ? null : entity.getStudentId().getImagePath());
        }

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private Comments mapToEntity(CommentsDTO dTO, IsagUser isagUser, Comments entity) {
        if (entity == null) {
            entity = new Comments();
        }

        entity.setId(dTO.getId());
        entity.setComment(dTO.getComment());

        if (dTO.getLessonId() != null) {
            Lesson lesson = new Lesson();
            lesson.setId(dTO.getLessonId());
            entity.setLessonId(lesson);
        }
        if (dTO.getParent() != null) {
            Comments comments = new Comments();
            comments.setId(dTO.getParent());
            entity.setParent(comments);
        }
        if (dTO.getStudentId() != null) {
            Student student = new Student();
            student.setId(dTO.getStudentId());
            entity.setStudentId(student);
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
