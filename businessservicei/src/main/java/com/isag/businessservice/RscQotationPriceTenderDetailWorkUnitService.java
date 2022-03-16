/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.RscQotationPriceTenderDetailWorkUnitDTO;
import com.isag.entity.IsagUser;
import com.isag.entity.RscQotationPriceTender;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface RscQotationPriceTenderDetailWorkUnitService {

    public List<RscQotationPriceTenderDetailWorkUnitDTO> getBranchQotationPriceTenderWorkUnitByBranch(Integer branchId);

    public RscQotationPriceTenderDetailWorkUnitDTO findTenderDetailWorkUnit(Integer workUnitsGuide);

    public void deleteTenderDetailWorkUnit(RscQotationPriceTenderDetailWorkUnitDTO unit, IsagUser isagUser);

    public List<RscQotationPriceTenderDetailWorkUnitDTO> getRscQotationPriceTenderDetailWorkUnitByTenderId(Integer id);

    public List<RscQotationPriceTenderDetailWorkUnitDTO> getAllTenderRoots(Integer branchId);

    public List<RscQotationPriceTenderDetailWorkUnitDTO> getAllQotationPriceTenderWorkUnitByBranchIdAndserial(Integer branchId, Integer serial);

    public List<RscQotationPriceTenderDetailWorkUnitDTO> getAllQotationPriceTenderWorkUnitByBranchIdAndserialAndParent(Integer branchId, Integer serial, Integer parentId);

    public void addTenderDetailWorkUnitList(List<RscQotationPriceTenderDetailWorkUnitDTO> tenderDetailWorkUnitDTOlist, RscQotationPriceTender tenderId, IsagUser isagUser);

    public void deleteTenderDetailWorkUnitList(List<RscQotationPriceTenderDetailWorkUnitDTO> tenderDetailWorkUnitDTOlist);

    public void deleteRscQotationPriceTenderDetailWorkUnitByTenderId(Integer TenderId);
}
