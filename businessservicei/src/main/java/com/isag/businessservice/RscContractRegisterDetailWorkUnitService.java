/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;
import com.isag.dto.RscContractRegisterDetailWorkUnitDTO;
import com.isag.entity.IsagUser;
import com.isag.entity.RscContractRegister;

import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface RscContractRegisterDetailWorkUnitService {
public List<RscContractRegisterDetailWorkUnitDTO> getBranchContractRegisterDetailWorkUnitByBranch(Integer branchId);

public RscContractRegisterDetailWorkUnitDTO findContractDetailWorkUnit(Integer workUnitsGuide);
public void deleteContractDetailWorkUnit(RscContractRegisterDetailWorkUnitDTO unit, IsagUser isagUser);
public List<RscContractRegisterDetailWorkUnitDTO> getContractDetailWorkUnitByContractId(Integer id);
public List<RscContractRegisterDetailWorkUnitDTO> getAllContractRoots(Integer branchId);
public List<RscContractRegisterDetailWorkUnitDTO> getAllContractRegisterDetailWorkUnitByBranchIdAndserial(Integer branchId, Integer serial);
public List<RscContractRegisterDetailWorkUnitDTO> getAllContractRegisterDetailWorkUnitByBranchIdAndserialAndParent(Integer branchId, Integer serial, Integer parentId);
public void addContractDetailWorkUnitList(List<RscContractRegisterDetailWorkUnitDTO> contractDetailWorkUnitDTOlist, RscContractRegister contractId, IsagUser isagUser);
public void deleteContractDetailWorkUnitList(List<RscContractRegisterDetailWorkUnitDTO> contractDetailWorkUnitDTOlist);

   

}
