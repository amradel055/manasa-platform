/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.DocumentsDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface DocumentsService {

    public List<DocumentsDTO> findDocumentsByLessonId(Integer lessonId);
    
    public List<String> findDocumentsByLessonIdLinks(Integer lessonId);

    public DocumentsDTO save(DocumentsDTO documentsDTO, IsagUser isagUser);
    
    public DocumentsDTO saveList(List<String> documentsDTOist, Integer lessonId, Integer branchId, Integer companyId);

    public void deleteDocuments(DocumentsDTO documentsDTO);

    public void deleteDocuments(Integer documentId);
    
     public void deleteDocumentsByLesson(Integer lessonId);
}
