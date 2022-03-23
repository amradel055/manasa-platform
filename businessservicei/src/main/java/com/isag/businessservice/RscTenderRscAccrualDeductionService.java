/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.RscTenderRscAccrualDeductionDTO;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author AhmedEssam
 */
@Remote
public interface RscTenderRscAccrualDeductionService {

    public RscTenderRscAccrualDeductionDTO addRscTenderRscAccrualDeductionD(RscTenderRscAccrualDeductionDTO rscTenderRscAccrualDeductionDTO, Integer tenderId, IsagUser isagUser);

    public String addRscTenderRscAccrualDeductionDList(List<RscTenderRscAccrualDeductionDTO> rscTenderRscAccrualDeductionDTOList, Integer tenderId, IsagUser isagUser);

    public List<RscTenderRscAccrualDeductionDTO> getAllRscTenderRscAccrualDeductionList(Integer tenderId, Integer type);

    public void deleteRscTenderRscAccrualDeductionDList(List<RscTenderRscAccrualDeductionDTO> rscTenderRscAccrualDeductionDTOList, IsagUser isagUser);

    public void deleteByTenderId(Integer tenderId);
}
