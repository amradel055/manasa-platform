/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.HrEmpQualificationsDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
@Remote
public interface HrEmpQualificationsService {

    public List<HrEmpQualificationsDTO> addHrEmpQualificationsList(List<HrEmpQualificationsDTO> hrEmergencyContactDTOList, IsagUser isagUser, Integer employeeID);

    public List<HrEmpQualificationsDTO> findHrEmpQualificationsByEmployeeId(Integer employeeId);

    public void deleteHrEmpQualifications(HrEmpQualificationsDTO qualifications, IsagUser isagUser);

    public void deleteHrEmpQualificationsList(List<HrEmpQualificationsDTO> hrEmergencyContactDTOList, IsagUser isagUser);

    public HrEmpQualificationsDTO addHrEmpQualifications(HrEmpQualificationsDTO hrEmergencyContactDTO, IsagUser isagUser);

    public void deleteByEmployeeId(Integer employeeId);

}
