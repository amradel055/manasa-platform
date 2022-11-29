/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.api.dto.SearchStudentDTO;
import com.isag.dto.StudentPeriodsOrderDTO;
import com.isag.entity.IgmaUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface StudentPeriodsOrderService {

    public void save(StudentPeriodsOrderDTO studentPeriodsOrderDTO, IgmaUser isagUser);

    public void delete(Integer studentPeriodId);

    public StudentPeriodsOrderDTO findStudentPeriod(Integer studentPeriodId);

    public List<StudentPeriodsOrderDTO> findStudemtPeriodOrderDTO(Integer branchId);

    public List<SearchStudentDTO> searchStudent(Integer periodId, Integer materialId);
    
    public void transaction(Integer studentPeriodOrderId, IgmaUser igmaUser)  ;

}
