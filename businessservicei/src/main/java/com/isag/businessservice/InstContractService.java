/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.InstallmentSearchBean;
import com.isag.dto.InstContractDTO;
import com.isag.dto.InstContractDetailDTO;
import com.isag.entity.InstContract;
import com.isag.entity.InstContractDetail;
import com.isag.views.InstInstallmentView;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;

/**
 *
 * @author ahmed
 */
@Remote
public interface InstContractService {

    public InstContract addInstContract(InstContract instContract, List<InstContractDetail> instContractDetailList, List<InstContractDetail> instContractDetailListDeleted);

    public InstContract addInstContractforTasaheel(InstContract instContract, List<InstContractDetail> instContractDetailList);

    public void deleteInstContract(InstContract instContract);

    public List<InstContract> getALLInstContractByCompanyId(Integer companyId);

    public List<InstContract> getALLInstContractByBranchId(Integer branchId);

    public List<InstContract> getALLInstContractSettlmentByBranchId(Integer branchId);

    public List<InstContract> getALLInstContractRecievableByBranchId(Integer branchId);

    public List<InstContract> getALLInstContractTransferByBranchId(Integer branchId);

    public InstContract findInstContractById(Integer instContractId);

    public InstContract addOneInstContract(InstContract instContract, InstContractDetail instContractDetail);

    public Integer getMaxSerail(InstContract instContract);

    public InstContract updateInstContract(InstContract instContract);

    public List<InstContract> getALLInstContractByBranchIdAndCustomerId(Integer branchId, Integer customerId);

    public InstContract updateIntsContract(InstContract instContract);

    public Long getTotalRegistors(Integer branchId, Map<String, Object> filters);

    public List<InstInstallmentView> getALLInstContractTransferByBranchId(int first, int pageSize, String sortField, Boolean asc, Integer branchId, Map<String, Object> filters);

    public Map<Integer, List<InstContractDetailDTO>> getALLInstContractByBranchIds(Integer branchId);

    public InstContract addInstContract(InstContract instContract);

    public String findMaxCodeForInstContractByBranchId(Integer branchId, Integer type);

    public Boolean CheckInstContractByBranchIdAndSerial(Integer branchId, Integer Serial);

    public InstContract findInstContractByBranchIdAndSerial(Integer branchId, Integer Serial);

    public List<InstContractDTO> findInstContractByProject(InstallmentSearchBean installmentSearchBean);

}
