/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.RscPriceTenderDetailWorkUnitDTO;
import com.isag.entity.IsagUser;
import com.isag.entity.RscPriceTender;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface RscPriceTenderDetailWorkUnitService {
public List<RscPriceTenderDetailWorkUnitDTO> getBranchRscPriceTenderDetailWorkUnitByBranch(Integer branchId);
public List<RscPriceTenderDetailWorkUnitDTO> getRscPriceTenderDetailWorkUnitChilds(Integer parentId);
public RscPriceTenderDetailWorkUnitDTO findTenderDetailWorkUnit(Integer workUnitsGuide);
public void deleteTenderDetailWorkUnit(RscPriceTenderDetailWorkUnitDTO unit, IsagUser isagUser);
public List<RscPriceTenderDetailWorkUnitDTO> getPriceTenderDetailWorkUnitByTenderId(Integer id);
public List<RscPriceTenderDetailWorkUnitDTO> getAllTenderRoots(Integer branchId);
public List<RscPriceTenderDetailWorkUnitDTO> getAllRscPriceTenderDetailWorkUnitByBranchIdAndserial(Integer branchId, Integer serial);
public List<RscPriceTenderDetailWorkUnitDTO> getAllRscPriceTenderDetailWorkUnitByBranchIdAndserialAndParent(Integer branchId, Integer serial, Integer parentId);
public RscPriceTenderDetailWorkUnitDTO addTenderDetailWorkUnitList(List<RscPriceTenderDetailWorkUnitDTO> tenderDetailWorkUnitDTOlist, RscPriceTender tenderId, IsagUser isagUser);
public void deleteTenderDetailWorkUnitList(List<RscPriceTenderDetailWorkUnitDTO> tenderDetailWorkUnitDTOlist);
public void deletebyPriceTenderId (Integer priceTenderId);

   

}
