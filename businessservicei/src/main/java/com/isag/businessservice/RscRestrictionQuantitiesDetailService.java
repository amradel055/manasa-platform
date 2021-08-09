/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.RscRestrictionQuantitiesDetailDTO;
import com.isag.entity.IsagUser;
import com.isag.entity.RscRestrictionQuantities;
import com.isag.entity.RscRestrictionQuantitiesDetail;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface RscRestrictionQuantitiesDetailService {

    public List<RscRestrictionQuantitiesDetailDTO> getBranchRscRestrictionQuantitiesDetailByBranch(Integer branchId);

    public RscRestrictionQuantitiesDetailDTO findRestrictionQuantitiesDetail(Integer workUnitsGuide);

    public void deleteRestrictionQuantitiesDetail(RscRestrictionQuantitiesDetailDTO unit, IsagUser isagUser);
    
    public void deleteRestrictionQuantitiesDetailListByExtractID(Integer RestrictionQuantitiesID, Integer Branchid) ;

    public List<RscRestrictionQuantitiesDetailDTO> getAllRscRestrictionQuantitiesDetailByBranchIdAndserial(Integer branchId, Integer serial);

    public void addRestrictionQuantitiesDetailList(List<RscRestrictionQuantitiesDetailDTO> rscRestrictionQuantitiesDetailDTOlist, RscRestrictionQuantities restrictionQuantities, IsagUser isagUser);

    public List<RscRestrictionQuantitiesDetailDTO> getRestrictionQuantitiesDetailByRestrictionId(Integer id);

    public void updateRestrictionQuantitiesDetailList(List<RscRestrictionQuantitiesDetail> quantitiesDetailList);

    public void deleteRestrictionQuantitiesDetailList(List<RscRestrictionQuantitiesDetailDTO> detailDTOList);

}
