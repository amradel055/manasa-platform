/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.core.GenericDAO;
import com.isag.dto.DocumentsDTO;
import com.isag.entity.Branch;
import com.isag.entity.IgmaCompany;
import com.isag.entity.Documents;
import com.isag.entity.IgmaUser;
import com.isag.entity.Lesson;
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
public class DocumentsServiceImpl implements DocumentsService {

    @EJB
    private GenericDAO dao;

    @Override
    public List<DocumentsDTO> findDocumentsByLessonId(Integer lessonId) {
        Map<String, Object> params = new HashMap<>();
        params.put("lessonId", lessonId);
        List<DocumentsDTO> dTOList = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d FROM Documents d WHERE d.lessonId.id = :lessonId order by d.id desc ");
        List<Documents> list = dao.findListByQuery(builder.toString(), params);
        dTOList = returnListDTO(list);
        return dTOList;
    }

    @Override
    public List<String> findDocumentsByLessonIdLinks(Integer lessonId) {
        Map<String, Object> params = new HashMap<>();
        params.put("lessonId", lessonId);
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT d.link FROM Documents d WHERE d.lessonId.id = :lessonId ");
        List<String> list = dao.findListByQuery(builder.toString(), params);
        return list;
    }

    @Override
    public DocumentsDTO save(DocumentsDTO documentsDTO, IgmaUser isagUser) {
        DocumentsDTO msg = new DocumentsDTO();
        Documents documents = null;
        if (documentsDTO.getId() != null) {
            documents = dao.findEntityById(Documents.class, documentsDTO.getId());
        }
        if (documentsDTO != null && documentsDTO.getLessonId() != null
                && documentsDTO.getLink() != null) {
            if (isagUser == null) {
                isagUser = new IgmaUser();
                isagUser.setId(61);
                Branch branch = new Branch();
                branch.setId(documentsDTO.getBranchId());
                isagUser.setBranchId(branch);
                IgmaCompany isagCompany = new IgmaCompany();
                isagCompany.setId(documentsDTO.getCompanyId());
                isagUser.setCompanyId(isagCompany);
            }
            documents = mapToEntity(documentsDTO, isagUser, documents);
            dao.updateEntity(documents);
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
    public DocumentsDTO saveList(List<DocumentsDTO> documentsDTOist, Integer lessonId, Integer branchId, Integer companyId) {
        DocumentsDTO msg = new DocumentsDTO();
        for (DocumentsDTO documentsDTO : documentsDTOist) {
            if (documentsDTO != null && lessonId != null && branchId != null && companyId != null
                    && documentsDTO != null) {
                Documents documents = new Documents();
                Branch branch = new Branch();
                branch.setId(branchId);
                documents.setBranchId(branch);

                IgmaUser isagUser = new IgmaUser();
                isagUser.setId(61);
                documents.setCreatedBy(isagUser);

                IgmaCompany isagCompany = new IgmaCompany();
                isagCompany.setId(companyId);
                documents.setCompanyId(isagCompany);
                
                documents.setLink(documentsDTO.getLink());
                Lesson lesson = new  Lesson();
                lesson.setId(lessonId);
                documents.setLessonId(lesson);
                
                documents.setCreationDate(new Date());

                dao.updateEntity(documents);
            } else {
                msg.setMsg("لا يوجد بيانات  ");
            }
        }
        return msg;
    }
    
    @Override
    public DocumentsDTO saveListV2(List<String> documentsDTOist, Integer lessonId, Integer branchId, Integer companyId) {
        DocumentsDTO msg = new DocumentsDTO();
        for (String documentsDTO : documentsDTOist) {
            if (documentsDTO != null && lessonId != null && branchId != null && companyId != null) {
                Documents documents = new Documents();
                Branch branch = new Branch();
                branch.setId(branchId);
                documents.setBranchId(branch);

                IgmaUser isagUser = new IgmaUser();
                isagUser.setId(61);
                documents.setCreatedBy(isagUser);

                IgmaCompany isagCompany = new IgmaCompany();
                isagCompany.setId(companyId);
                documents.setCompanyId(isagCompany);
                
                documents.setLink(documentsDTO);
                Lesson lesson = new  Lesson();
                lesson.setId(lessonId);
                documents.setLessonId(lesson);
                
                documents.setCreationDate(new Date());

                dao.updateEntity(documents);
            } else {
                msg.setMsg("لا يوجد بيانات  ");
            }
        }
        return msg;
    }

    @Override
    public void deleteDocuments(DocumentsDTO documentsDTO) {
        Documents entity = new Documents();
        entity.setId(documentsDTO.getId());
        dao.deleteEntity(entity);
    }

    @Override
    public void deleteDocuments(Integer documentId) {
        Documents entity = new Documents();
        entity.setId(documentId);
        dao.deleteEntity(entity);
    }

    @Override
    public void deleteDocumentsByLesson(Integer lessonId) {
        String sql = "delete from Documents d where d.id > 0 AND d.lessonId.id = " + lessonId;
        dao.executeDeleteQuery(sql);
    }
    
    @Override
    public void deleteDocuments(String document) {
        String sql = "delete from Documents d where d.id > 0 AND d.link = '" + document + "'";
        dao.executeDeleteQuery(sql);
    }

    private List<DocumentsDTO> returnListDTO(List<Documents> list) {
        List<DocumentsDTO> dTOList = new ArrayList<>();
        for (Documents entity : list) {
            dTOList.add(mapTODTO(entity));
        }
        return dTOList;
    }

    private DocumentsDTO mapTODTO(Documents entity) {
        DocumentsDTO dTO = initMapToDTO(entity);

        return dTO;
    }

    private DocumentsDTO initMapToDTO(Documents entity) {

        DocumentsDTO dTO = new DocumentsDTO();
        dTO.setId(entity.getId());
        dTO.setLink(entity.getLink());

        if (entity.getLessonId() != null) {
            dTO.setLessonId(entity.getLessonId().getId());
        }

        dTO.setIndex(entity.getId());
        dTO.setCreatedDate(entity.getCreationDate());
        dTO.setCreatedBy(entity.getCreatedBy().getId());
        dTO.setBranchId(entity.getBranchId().getId());

        return dTO;
    }

    private Documents mapToEntity(DocumentsDTO dTO, IgmaUser isagUser, Documents entity) {
        if (entity == null) {
            entity = new Documents();
        }

        entity.setId(dTO.getId());
        entity.setLink(dTO.getLink());

        if (dTO.getLessonId() != null) {
            Lesson lesson = new Lesson();
            lesson.setId(dTO.getLessonId());
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
