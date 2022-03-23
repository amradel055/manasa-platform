/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.HrSalariesStructureEffectDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
@Remote
public interface HrSalariesEffectService {

    public void deleteSalariesEffect(HrSalariesStructureEffectDTO hrSalariesEffectDTO, IsagUser isagUser);

    public HrSalariesStructureEffectDTO addSalariesEffect(HrSalariesStructureEffectDTO hrSalariesEffectDTO, IsagUser isagUser);

    public HrSalariesStructureEffectDTO findSalariesEffect(Integer id);

    public List<HrSalariesStructureEffectDTO> addHrSalariesEffectList(List<HrSalariesStructureEffectDTO> hrSalariesEffectDTOList, IsagUser isagUser, Integer employeeID);

    public List<HrSalariesStructureEffectDTO> findSalariesEffectById(Integer Id);

    public List<HrSalariesStructureEffectDTO> findSalariesEffectByEmployeeId(Integer employeeId);

}
