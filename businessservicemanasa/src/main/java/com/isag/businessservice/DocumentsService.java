/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.DocumentsDTO;
import com.isag.entity.IgmaUser;
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

    public DocumentsDTO save(DocumentsDTO documentsDTO, IgmaUser isagUser);
    
    public DocumentsDTO saveList(List<DocumentsDTO> documentsDTOist, Integer lessonId, Integer branchId, Integer companyId);
    
    public DocumentsDTO saveListV2(List<String> documentsDTOist, Integer lessonId, Integer branchId, Integer companyId);

    public void deleteDocuments(DocumentsDTO documentsDTO);

    public void deleteDocuments(Integer documentId);
    
     public void deleteDocumentsByLesson(Integer lessonId);
     
     public void deleteDocuments(String document);
}
