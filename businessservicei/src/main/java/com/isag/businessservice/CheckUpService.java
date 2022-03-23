/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.entity.HosCheckUp;
import com.isag.entity.HosCheckUpDetail;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hq002
 */
@Remote
public interface CheckUpService {

    public HosCheckUp addHosCheckUp(HosCheckUp hosCheckUp, List<HosCheckUpDetail> hosCheckUpDetailList, List<HosCheckUpDetail> hosCheckUpDetailListDeleted);

    public void deleteHosCheckUp(HosCheckUp hosCheckUp);

    public List<HosCheckUp> getALLHosCheckUpByCompanyId(Integer companyId);

    public List<HosCheckUp> getALLHosCheckUpSettlmentByBranchId(Integer branchId);

    public List<HosCheckUp> getALLHosCheckUpRecievableByBranchId(Integer branchId);

    public List<HosCheckUp> getALLHosCheckUpTransferByBranchId(Integer branchId);

    public HosCheckUp findHosCheckUpById(Integer hosCheckUpId);

    public HosCheckUp addOneHosCheckUp(HosCheckUp hosCheckUp, HosCheckUpDetail checkUpDetail);

    public Integer getMaxSerail(HosCheckUp hosCheckUp);

    public HosCheckUp updateHosCheckUp(HosCheckUp hosCheckUp);

}
