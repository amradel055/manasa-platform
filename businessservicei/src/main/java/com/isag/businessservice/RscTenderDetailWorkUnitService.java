/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.RscTenderDetailWorkUnitDTO;
import com.isag.entity.IsagUser;
import com.isag.entity.RscTenderDetailWorkUnit;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface RscTenderDetailWorkUnitService {

    public List<RscTenderDetailWorkUnitDTO> getBranchRscTenderDetailWorkUnitByBranch(Integer branchId);

    public RscTenderDetailWorkUnitDTO findTenderDetailWorkUnit(Integer workUnitsGuide);

    public void deleteTenderDetailWorkUnit(RscTenderDetailWorkUnitDTO unit, IsagUser isagUser);

    public List<RscTenderDetailWorkUnitDTO> getTenderDetailWorkUnitByTenderId(Integer tenderid);

    public Map<Integer, RscTenderDetailWorkUnit> getMapTenderDetailWorkUnitByTenderId(Integer tenderid);

    public List<RscTenderDetailWorkUnitDTO> getAllTenderRoots(Integer branchId);

    public List<RscTenderDetailWorkUnitDTO> getAllRscTenderDetailWorkUnitByBranchIdAndserial(Integer branchId, Integer serial);

    public List<RscTenderDetailWorkUnitDTO> getAllRscTenderDetailWorkUnitByBranchIdAndserialAndParent(Integer branchId, Integer serial, Integer parentId);

    public String addTenderDetailWorkUnitList(List<RscTenderDetailWorkUnitDTO> tenderDetailWorkUnitDTOlist, Integer tenderId, IsagUser isagUser);

    public void deleteTenderDetailWorkUnitList(List<RscTenderDetailWorkUnitDTO> tenderDetailWorkUnitDTOlist, IsagUser isagUser);

}
