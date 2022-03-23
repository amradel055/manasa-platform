/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.HrEmployeeFellowsDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
@Remote
public interface HrEmployeeFellowsService {

    public List<HrEmployeeFellowsDTO> addHrEmployeeFellowsList(List<HrEmployeeFellowsDTO> hrEmployeeFellowsDTOList, IsagUser isagUser, Integer employeeId);

    public List<HrEmployeeFellowsDTO> findHrEmployeeFellowsByEmployeeId(Integer employeeId);

    public void deleteHrEmployeeFellows(HrEmployeeFellowsDTO hrEmployeeFellowsDTO, IsagUser isagUser);

    public void deleteHrEmployeeFellowsList(List<HrEmployeeFellowsDTO> hrEmergencyContactDTOList, IsagUser isagUser);

    public void deleteHrEmployeeFellowsByEmployeeId(Integer employeeId);

    public HrEmployeeFellowsDTO addHrEmployeeFellows(HrEmployeeFellowsDTO hrEmployeeFellowsDTO, IsagUser isagUser);
}
