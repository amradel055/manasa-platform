/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.HrEmergencyContactDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author `*":<ahmedShaaban>:"*`
 */
@Remote
public interface HrEmergencyContactService {

    public HrEmergencyContactDTO addHrEmergencyContact(HrEmergencyContactDTO hrEmergencyContactDTO);

    public List<HrEmergencyContactDTO> addHrEmergencyContactList(List<HrEmergencyContactDTO> hrEmergencyContactDTOList);


    public void deleteHrEmergencyContact(HrEmergencyContactDTO hrEmergencyContactDTO, IsagUser isagUser);
    public void deleteByEmployeeId(Integer employeeId);
    public HrEmergencyContactDTO findbyEmployeeId(Integer employeeId);

    public HrEmergencyContactDTO addHrEmergencyContact(HrEmergencyContactDTO hrEmergencyContactDTO, IsagUser isagUser);

}
