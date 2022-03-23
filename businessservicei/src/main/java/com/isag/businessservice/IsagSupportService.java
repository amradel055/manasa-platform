/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isag.businessservice;

import com.isag.businessservice.reports.searchBean.IsagSupportSearchBean;
import com.isag.dto.IsagSupportDTO;
import com.isag.entity.IsagScreen;
import com.isag.entity.IsagSupport;
import com.isag.entity.IsagUser;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author hhhh
 */
@Remote
public interface IsagSupportService {

    public IsagSupportDTO save(IsagSupportDTO isagSupportDTO, IsagUser isagUser);

    public List<IsagSupport> getData(Integer serial);

    public List<IsagSupportDTO> findBranchData(Integer branchId);
    
    public List<IsagSupportDTO> findData(IsagSupportDTO isagSupportDTO);

    public List<IsagScreen> getAllScreens(Integer moduleType);
    
    public void deleteIsagSupport(Integer isagSupportId);
    
    public IsagSupportDTO findIsagSupport(Integer id);
    
    public List<IsagSupportDTO> getIsagSupportByBranch(Integer branchId, IsagSupportSearchBean isagSupportSearchBean);

    public List<IsagSupportDTO> findDataByBranch(IsagSupportDTO isagSupportDTO) ;
    
}
