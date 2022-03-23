/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.RscGenaricLoad;
import com.isag.dto.RscQotationPriceTenderDetailWorkUnitDTO;
import com.isag.dto.RscQotationPriceTenderDTO;
import com.isag.entity.IsagUser;

import java.util.List;
import java.util.Set;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface RscQotationPriceTenderService {

    public List<RscQotationPriceTenderDTO> getBranchRscQotationPriceTenderActiveByBranch(Integer branchId);

    public List<RscQotationPriceTenderDTO> getRscQotationPriceTenderByTenderId(Integer tenderId);

    public RscQotationPriceTenderDTO findRscQotationPriceTender(Integer tenderId);

    public RscQotationPriceTenderDTO addRscQotationPriceTender(RscQotationPriceTenderDTO tenderDTO, List<RscQotationPriceTenderDetailWorkUnitDTO> RscQotationPriceTenderDetailWorkUnitDTOList, List<RscQotationPriceTenderDetailWorkUnitDTO> RscQotationPriceTenderDetailWorkUnitDTODeletedList, IsagUser isagUser);

    public void deleteRscQotationPriceTender(RscQotationPriceTenderDTO tenderDTO, IsagUser isagUser);

    public List<RscQotationPriceTenderDTO> getAllRscQotationPriceTenderByBranchIdAndserial(Integer branchId, Integer serial);

    public List<RscQotationPriceTenderDTO> getAllRscQotationPriceTenderByBranchIdAndserialAndID(Integer branchId, Integer serial, Integer selectedId);

    public List<RscQotationPriceTenderDetailWorkUnitDTO> loadrscGenaric(RscGenaricLoad rscGenaricLoad);

    public Set<Integer> getrscPriceTenderSetByBranch(Integer branchId);

    public Set<Integer> getrscTenderSetByBranch(Integer branchId);
}
