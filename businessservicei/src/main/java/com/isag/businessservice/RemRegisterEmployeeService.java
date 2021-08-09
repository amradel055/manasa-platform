/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.RemRegisterEmployeeDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface RemRegisterEmployeeService {

    public RemRegisterEmployeeDTO findRegisterEmployee(Integer registerEmployeeId);

    public void deleteRegisterEmployee(RemRegisterEmployeeDTO registerEmployeeDTO);

    public RemRegisterEmployeeDTO save(RemRegisterEmployeeDTO registerEmployeeDTO, IsagUser isagUser);

    public List<RemRegisterEmployeeDTO> findRegisterEmployeeList(Integer branchId);

}
