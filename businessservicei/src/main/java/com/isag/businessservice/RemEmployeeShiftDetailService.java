/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.RemEmployeeShiftDTO;
import com.isag.dto.RemEmployeeShiftDetailDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq003
 */
@Remote
public interface RemEmployeeShiftDetailService {

    public RemEmployeeShiftDetailDTO findEmployeeShiftDetail(Integer employeeShiftId);
    
    public List<RemEmployeeShiftDetailDTO> findAllEmployeeShiftDetail(Integer employeeShiftId);

    public void deleteEmployeeShiftDetail(RemEmployeeShiftDetailDTO employeeShiftesDTO);
    
    public void deleteAllEmployeeShiftDetail(List<RemEmployeeShiftDetailDTO> employeeShiftDetailesDTOList);

    public RemEmployeeShiftDetailDTO save(RemEmployeeShiftDetailDTO employeeShiftesDTO, IsagUser isagUser,Integer remEmployeeShiftId);
    
    public List<RemEmployeeShiftDetailDTO> saveAll(List<RemEmployeeShiftDetailDTO> employeeShiftDetailesDTOList, IsagUser isagUser,Integer remEmployeeShiftId) ;
    
    public RemEmployeeShiftDTO addDetail(RemEmployeeShiftDTO remEmployeeShiftDTO, Integer index);

}
