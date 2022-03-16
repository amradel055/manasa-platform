/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.dto.InstProfitRateDTO;
import com.isag.entity.IsagUser;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author `*"<:ahmedShaaban:>"*`
 */
@Remote
public interface InstProfitRateService {

    public List<InstProfitRateDTO> getAllInstProfitRatesListByBranchId(Integer branchId);

    public InstProfitRateDTO addInstProfitRate(InstProfitRateDTO profitRateDTO);

    public InstProfitRateDTO findInstProfitRate(Integer profitRateId);

    public InstProfitRateDTO updateInstProfitRate(InstProfitRateDTO profitRateDTO);

    public void deleteInstProfitRate(InstProfitRateDTO profitRateDTO, IsagUser isagUser);

    public List<InstProfitRateDTO> addInstProfitRate(List<InstProfitRateDTO> profitRateDTOList, IsagUser isagUser);

    public BigDecimal getInstProfitRatesByMonthAndBranchId(Integer branchId,Integer monthNumber);
}
