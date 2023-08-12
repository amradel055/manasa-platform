/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.InboxMessagesDTO;
import com.isag.dto.InboxReplyDTO;
import com.isag.entity.Branch;
import com.isag.entity.IgmaUser;
import com.isag.entity.InboxMessages;
import com.isag.entity.InboxReply;
import com.isag.entity.Materials;
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
public class InboxMessageServiceImpl implements InboxMessageService {

    @EJB
    private GenericDAO dao;
    @EJB
    private InboxReplyService inboxReplyService;

    @Override
    public InboxMessagesDTO save(InboxMessagesDTO dto, IgmaUser igmaUser) {
        if (dto != null) {
            InboxMessages entity = mapToEntity(dto, igmaUser);
            entity = dao.updateEntity(entity);
            dto.setId(entity.getId());
            return dto;
        }
        return dto;
    }

    @Override
    public void delete(Integer id) {
        InboxMessages studentPeriodsOrder = dao.findEntityById(InboxMessages.class, id);
        dao.deleteEntity(studentPeriodsOrder);
    }

    @Override
    public InboxMessagesDTO findById(Integer id) {
        InboxMessages entity = dao.findEntityById(InboxMessages.class, id);
        return mapToDTO(entity, true);
    }

    @Override
    public void readDone(Integer id) {
        InboxMessages entity = dao.findEntityById(InboxMessages.class, id);
        entity.setReadDone(1);
    }

    private InboxMessagesDTO mapToDTO(InboxMessages entity, boolean isDetail) {
        if (entity != null) {
            InboxMessagesDTO dto = new InboxMessagesDTO();
            if (isDetail) {
                List<InboxReplyDTO> replys = inboxReplyService.find(entity.getId());
                dto.setInboxReplys(replys);
            }
            dto.setId(entity.getId());
            dto.setBody(entity.getBody());
            dto.setMaterialId(entity.getMaterialId() == null ? null : entity.getMaterialId().getId());
            dto.setMaterialName(entity.getMaterialId() == null ? null : entity.getMaterialId().getName());
            dto.setReadDone(entity.getReadDone());
            dto.setSentDate(entity.getSentDate());
            dto.setStudentId(entity.getStudentId() == null ? null : entity.getStudentId().getId());
            dto.setStudentName(entity.getStudentId() == null ? null : entity.getStudentId().getFullName());
            dto.setSubject(entity.getSubject());
            dto.setImage(entity.getImage());
            dto.setTeacherId(entity.getTeacherId() == null ? null : entity.getTeacherId().getId());
            dto.setTeacherName(entity.getTeacherId() == null ? null : entity.getTeacherId().getName());
            return dto;
        }
        return null;
    }

    private InboxMessages mapToEntity(InboxMessagesDTO dto, IgmaUser igmaUser) {
        if (dto != null) {
            InboxMessages entity = new InboxMessages();
            entity.setId(dto.getId());
            entity.setBody(dto.getBody());
            Materials material = new Materials(dto.getMaterialId());
            entity.setMaterialId(material);
            entity.setReadDone(dto.getReadDone());
            entity.setSentDate(dto.getSentDate());
            Student student = new Student(dto.getStudentId());
            entity.setStudentId(student);
            entity.setSubject(dto.getSubject());
            entity.setImage(dto.getImage());
            IgmaUser teacher = new IgmaUser(dto.getTeacherId());
            entity.setTeacherId(teacher);

            if (dto.getId() == null) {
                entity.setCreatedBy(igmaUser);
                entity.setCreationDate(new Date());
            } else {
                entity.setId(dto.getId());
                entity.setModificationDate(new Date());
                entity.setModifiedBy(igmaUser);
            }

            Branch branch = new Branch();
            branch.setId(232);
            entity.setBranchId(branch);
            entity.setCompanyId(igmaUser.getCompanyId());

            return entity;
        }
        return null;
    }

    @Override
    public List<InboxMessagesDTO> find(Integer studentId, Integer teacherId, Integer schoolId, Integer materialId, Integer readDone) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder builder = new StringBuilder();

        builder.append("SELECT d FROM InboxMessages d WHERE 1=1    ");
        if (studentId != null) {
            params.put("studentId", studentId);
            builder.append(" AND d.studentId.id = :studentId ");
        }
        if (teacherId != null) {
            params.put("teacherId", teacherId);
            builder.append(" AND d.teacherId.id = :teacherId");
        }
        if (materialId != null) {
            params.put("materialId", materialId);
            builder.append(" AND d.materialId.id = :materialId");
        }
        if (schoolId != null) {
            params.put("schoolId", schoolId);
            builder.append(" AND d.materialId.stageId.id = :schoolId");
        }
        if (readDone != null) {
            if (readDone == 0) {
                builder.append(" AND ( d.readDone = 0 or d.readDone is null )  ");
            } else {
                params.put("readDone", readDone);
                builder.append(" AND d.materialId.readDone = :readDone");
            }
        }
        builder.append(" order by d.sentDate ASC ");

        List<InboxMessages> list = dao.findListByQuery(builder.toString(), params);
        if (list != null && !list.isEmpty()) {
            List<InboxMessagesDTO> listDTO = new ArrayList<>();
            for (InboxMessages entity : list) {
                listDTO.add(mapToDTO(entity, false));
            }
            return listDTO;
        }
        return null;
    }

}
