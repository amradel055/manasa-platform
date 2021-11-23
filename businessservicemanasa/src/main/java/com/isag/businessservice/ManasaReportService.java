/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.APIStudentDataDTO;
import com.isag.dto.ExamsDTO;
import com.isag.dto.QuestionsDTO;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface ManasaReportService {
 
    public Integer findExamStudentNumber(Integer examId);
    
    public List<ExamsDTO> findExamBestStudents(Integer examId);
    
    public List<QuestionsDTO> findExamMostMisatkes(Integer examId);
}
