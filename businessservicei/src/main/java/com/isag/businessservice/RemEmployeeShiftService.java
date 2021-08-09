/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.RemEmployeeShiftDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface RemEmployeeShiftService {

    public RemEmployeeShiftDTO findEmployeeShift(Integer employeeShiftId);

    public void deleteEmployeeShift(RemEmployeeShiftDTO employeeShiftesDTO);

    public RemEmployeeShiftDTO save(RemEmployeeShiftDTO employeeShiftesDTO, IsagUser isagUser);

    public List<RemEmployeeShiftDTO> findEmployeeShifts(Integer branchId);

}
