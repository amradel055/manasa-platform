/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.RscContractRegisterDetailWorkUnitDTO;
import com.isag.dto.RscContractRegisterDTO;
import com.isag.entity.IsagUser;

import java.util.List;
import java.util.Set;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface RscContractRegisterService {

    public List<RscContractRegisterDTO> getBranchRscContractRegisterActiveByBranch(Integer branchId);

    public RscContractRegisterDTO findRscContractRegister(Integer tenderId);

    public RscContractRegisterDTO addRscContractRegister(RscContractRegisterDTO tenderDTO , IsagUser isagUser);

    public void deleteRscContractRegister(RscContractRegisterDTO tenderDTO, IsagUser isagUser);

    public List<RscContractRegisterDTO> getAllRscContractRegisterByBranchIdAndserial(Integer branchId, Integer serial);

    public List<RscContractRegisterDTO> getAllRscContractRegisterByOwner(Integer customerId, Integer branchId);

    public List<RscContractRegisterDTO> getAllRscContractRegisterByBranchIdAndserialAndID(Integer branchId, Integer serial, Integer selectedId);

    public RscContractRegisterDTO  getAllRscContractRegisterByBranchIdAndTender(Integer branchId, Integer tenderId);
    
    public Set<Integer> getRscQotationPriceTenderIDByBranch(Integer branchId);
}
