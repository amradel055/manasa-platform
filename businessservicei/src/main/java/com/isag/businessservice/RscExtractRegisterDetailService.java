/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.RscExtractRegisterDetailDTO;
import com.isag.entity.IsagUser;
import com.isag.entity.RscExtractRegister;
import com.isag.entity.RscExtractRegisterDetail;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq004
 */
@Remote
public interface RscExtractRegisterDetailService {

    public List<RscExtractRegisterDetailDTO> getBranchRscExtractRegisterDetailByBranch(Integer branchId);

    public RscExtractRegisterDetailDTO findExtractRegisterDetail(Integer workUnitsGuide);

    public void deleteExtractRegisterDetail(RscExtractRegisterDetailDTO unit, IsagUser isagUser);

    public List<RscExtractRegisterDetailDTO> getPriceExtractRegisterDetailByExtractId(Integer id);

    public List<RscExtractRegisterDetailDTO> getAllRscExtractRegisterDetailByBranchIdAndserial(Integer branchId, Integer serial);

    public void addExtractRegisterDetailList(List<RscExtractRegisterDetailDTO> rscExtractRegisterDetailDTOlist, RscExtractRegister extractRegister, IsagUser isagUser);

    public void updateExtractRegisterDetailList(List<RscExtractRegisterDetail> rscExtractRegisterDetailList);

    public void deleteExtractRegisterDetailList(List<RscExtractRegisterDetailDTO> detailDTOList);

    public BigDecimal getRampValue(Integer id, Integer unitId);

    public BigDecimal getContractQty(Integer id, Integer unitId);

    public BigDecimal getOldQty(Integer id, Integer unitId);

    public BigDecimal getPriceSell(Integer id, Integer unitId);

    public RscExtractRegisterDetail getdetail(Integer contarctId, Integer unitId);

}
