/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface ManasaReportService {
 
    public  Integer findExamStudentNumber(Integer examId);
  
}
