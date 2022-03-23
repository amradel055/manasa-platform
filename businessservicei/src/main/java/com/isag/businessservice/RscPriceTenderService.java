/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.RscGenaricLoad;
import com.isag.dto.RscPriceTenderDTO;
import com.isag.dto.RscPriceTenderDetailWorkUnitDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import java.util.Set;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface RscPriceTenderService {

    public List<RscPriceTenderDTO> getBranchRscPriceTenderActiveByBranch(Integer branchId);

    public RscPriceTenderDTO findRscPriceTender(Integer tenderId);

    public RscPriceTenderDTO addRscPriceTender(RscPriceTenderDTO tenderDTO, List<RscPriceTenderDetailWorkUnitDTO> RscPriceTenderDetailWorkUnitDTOList, List<RscPriceTenderDetailWorkUnitDTO> rscPriceTenderDetailWorkUnitDTODeletedList, IsagUser isagUser);

    public void deleteRscPriceTender(RscPriceTenderDTO tenderDTO, IsagUser isagUser);

    public List<RscPriceTenderDTO> getAllRscPriceTenderByBranchIdAndserial(Integer branchId, Integer serial);

    public List<RscPriceTenderDTO> getAllRscPriceTenderByBranchIdAndserialAndID(Integer branchId, Integer serial, Integer selectedId);

    public void deleteByTenderId(Integer tenderId);

    public List<RscGenaricLoad> loadRscPriceTenderByBranch(Integer branchId);

    public Set<Integer> getAllTenderByBranchId(Integer branchId);
}
